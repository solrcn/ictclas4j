/*
 * Created on 2004-5-24
 *
 * �����ݿ���ص�һЩ��
 */
package com.gftech.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * 
 * �������ݿ�Ĺ����ӿڡ� �˽ӿڷ�װ�˷������ݿ��һ�еײ������ʹ�ô˽ӿ�֮����Ҫ�ͻ��˳���
 * ���ķ������ݿ�ײ���κ�ϸ�ڣ��ͻ��˳���ֻ�������Ҫִ��SELECT����UPDATE�������ɡ�
 * �˽ӿ�ʵ�������ӳصĹ��������ڷ���Ƶ����ʱ���Զ�����������Ŀ��ͬʱҲ�����ڿ��е�ʱ
 * �Ѷ���������Զ��رյ������ҿ��������ں����ݿ�����ӱ�����֮���Զ������ݿ⽨���µ� ���ӡ�
 * 
 * ��˵�����˰汾�����˺����ݿ���Զ����ӣ�
 * 
 * @author sinboy
 * @vesion 2.0
 */

public class GFDB {
	private String dbName;

	private ArrayList<GFConn> idleConnPool;// ���г�

	private ArrayList<GFConn> usingConnPool;// ʹ�ó�

	private final int maxConns = 150;// ���������Ŀ

	private final int maxUserCount = 149;// ÿ���������������û���Ŀ

	private final int timeout = 60000;// ���ӵ�������ʱ��

	private final int waitTime = 30000;// �û������ȴ�ʱ��

	private String dbUrl = null;// ���ӵ�ַ

	private String dbDriver = null;// ���ݿ�����

	private String dbUser = null;// ��½�û���

	private String dbPwd = null;// ��½����

	private ThreadGroup group = null;

	public GFDB(String dbName, String driver, String url, String user, String pwd) {

		this.dbName = dbName;
		dbDriver = driver;
		dbUrl = url;
		dbUser = user;
		dbPwd = pwd;
		if (driver != null && url != null)
			init();
	}

	private void init() {

		Connection conn = null;
		GFConn _conn = null;

		// init the connection pool
		idleConnPool = new ArrayList<GFConn>(0);
		usingConnPool = new ArrayList<GFConn>(0);

		conn = buildConn();
		if (conn != null) {
			_conn = new GFConn();
			_conn.setConn(conn);
			idleConnPool.add(_conn);
		} else {

			try {
				System.err.println("\n\n�����ݿ�����ӱ����ã���ͼ���½������ӡ�����");
				Thread.sleep(10000);
				init();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}

		if (group == null)
			group = new ThreadGroup("ThreadGroup");
		// if the manager thread is not active ,then run it
		if (!isActive(group, "manager"))
			manager();
	}

	/**
	 * ����Զ�����ݿ�
	 * 
	 * @return ���ӳɹ�����TRUE
	 */
	private Connection buildConn() {
		Connection conn = null;
		try {
			Class.forName(dbDriver);
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			if (conn != null) {

				String str = "������Զ�����ݿ�" + dbName+" "+dbUrl + "������:" + conn;
				System.out.println(str);
			}

		} catch (SQLException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * �����ӳ���ȡ��һ�����õ�����. ������г����У���ӿ��г���ȡ��Ȼ���userCount��һ�����Ѵ�����
	 * �ƶ�ʹ�ó��У������ж�ʹ�ó����Ƿ��п��õ����ӣ�һ�������� ��ʹ���û���Ŀ��û�дﵽ���ֵ��Ϊ���õ����ӣ�����������ڴ�
	 * ���ӷ�����û�ʹ�ã����ʹ�ó���û�п��õ����ӣ������ӵ�����Ŀ ��û�дﵽϵͳ���Ƶ����ֵ�������´���һ�����ӹ��û�ʹ�ã�����
	 * �ȴ�һ��ʱ���ٲ鿴�Ƿ��п������ӡ�
	 * 
	 * @param timeout
	 *            ���ȴ�ʱ�䣬������ʱ���򷵻�NULL
	 * @return
	 */
	public synchronized GFConn getConn() {
		Connection conn = null;
		GFConn _conn = null;

		if (idleConnPool != null && idleConnPool.size() > 0) {
			// ����ط����ܻ������Խ���쳣. ���ƻ���ͬ������
			_conn = (GFConn) idleConnPool.remove(0);
			// System.out.println("remove from idleConnPool:"+_conn);
			_conn.setLastAccessTime();
			_conn.changeUserCount(1);
			usingConnPool.add(_conn);
			return _conn;
		} else {
			if (usingConnPool != null) {
				if (usingConnPool.size() > 0) {
					// System.out.println("usingConnPool.size():"+usingConnPool.size());
					for (int i = 0; i < usingConnPool.size(); i++) {
						_conn = (GFConn) usingConnPool.get(i);
						if (_conn.getUserCount() < maxUserCount) {
							_conn.changeUserCount(1);
							return _conn;
						}

					}
					_conn = null;
				}

				// ���Ŀǰû�п��õ����Ӳ���û�дﵽ����������Ŀ��������Ϊ�û�
				// ����һ�����ӣ�����ȴ�һ��ʱ�俴�Ƿ������ӱ��ͷš�
				if (usingConnPool.size() < maxConns) {
					conn = buildConn();
					if (conn != null) {
						_conn = new GFConn();
						_conn.setConn(conn);
						_conn.changeUserCount(1);
						usingConnPool.add(_conn);

						return _conn;
					}
				} else {
					try {
						Thread.sleep(waitTime);
						System.out.println("No usable connection,sleep...");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					getConn();
				}
			}
		}
		return null;
	}

	public void changeUserCount(GFConn conn, int step) {
		if (conn != null) {
			conn.changeUserCount(step);
		}
	}

	/**
	 * ��ѯԶ�����ݿ�,���ҷ���һ����¼��
	 * 
	 * @param sql
	 *            Ҫ��ѯ��SQL���
	 * @return �Զ����¼��
	 */
	public GFResultSet query(String sql) {
		GFConn _conn = null;
		GFResultSet myrs = null;

		if (sql != null) {
			// �����ӳ���ȡ��һ�����е�����
			_conn = getConn();

			if (_conn != null) {
				try {
					// System.out.println("_conn:"+_conn);
					myrs = _conn.query(sql);
					_conn.changeUserCount(-1);
					// System.out.println("release the stmt");
				} catch (SQLException e) {
					init();
				}
			}

		}
		return myrs;
	}

	/**
	 * ִ�����磺INSERT��UPDATE��DELETE����
	 * 
	 * @param sql
	 *            SQL���
	 * @return ���ظ��µ�����
	 */
	public int execute(String sql) {
		int updateCount = 0;// ���µ�����
		GFConn _conn = null;

		if (sql != null) {
			_conn = getConn();
			if (_conn != null) {
				try {
					updateCount = _conn.execute(sql);
					_conn.changeUserCount(-1);
				} catch (SQLException e) {

					init();
				}
			}

		}
		return updateCount;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	private boolean isActive(ThreadGroup group, String threadName) {

		if (group != null && threadName != null) {
			Thread[] thd = new Thread[group.activeCount()];
			group.enumerate(thd);

			for (int i = 0; i < thd.length; i++)
				if (thd[i].getName().equals(threadName))
					return true;
		}

		return false;
	}

	public synchronized void clearPool() {
		Connection conn = null;
		if (idleConnPool != null && usingConnPool != null) {
			for (GFConn gfconn : usingConnPool) {
				conn = gfconn.getConn();
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

			for (GFConn gfconn : idleConnPool) {
				conn = gfconn.getConn();
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		idleConnPool = new ArrayList<GFConn>();
		usingConnPool = new ArrayList<GFConn>();
	}

	/**
	 * �����쳣�����д���������IO�쳣
	 * 
	 * @param e
	 */
	public void catchException(Exception e) {
		if (e != null)
			clearPool();
	}

	/**
	 * �������ӳ��е����ӡ� ���usingConnPool�е����ӵ�ʹ���û���ĿΪ0����Ѵ����ӷŵ�idleConnPool�У�
	 * ���idleConnPool�е����ӳ������ĵȴ�ʱ��û��ʹ�ã���رմ����ӣ����ӿ��г������
	 * 
	 */
	private void manager() {
		Thread thread = new Thread(group, "manager") {
			public void run() {
				GFConn _conn = null;
				while (true) {
					// System.out.println("manager is running...");
					try {
						sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (usingConnPool != null) {
						// ("using conn pool:"+usingConnPool.size() );
						for (int i = 0; i < usingConnPool.size(); i++) {
							_conn = (GFConn) usingConnPool.get(i);
							// System.out.println(_conn+":userCount is
							// :"+_conn.getUserCount() );
							if (_conn.getUserCount() == 0) {
								_conn.setLastAccessTime();
								idleConnPool.add(_conn);
								usingConnPool.remove(_conn);
							}
						}
					}

					if (idleConnPool != null) {
						// System.out.println("idle conn
						// pool:"+idleConnPool.size() );
						if (idleConnPool.size() > 1) {
							for (int i = 1; i < idleConnPool.size(); i++) {
								_conn = (GFConn) idleConnPool.get(i);
								// System.out.println(_conn+":timeout is :"+
								// (System.currentTimeMillis()
								// -_conn.getLastAccessTime() ));
								if (_conn.isTimeout(timeout)) {
									_conn.forceClose();
									idleConnPool.remove(_conn);
								}
							}
						}
					}
				}
			}
		};

		thread.start();

	}
}
