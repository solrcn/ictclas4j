/*
 * Created on 2004-5-25
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.gftech.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ʵ�ֶ�Connection�ķ�װ��ͬʱΪ���������������ԣ�
 * ���һ�εķ���ʱ�䣨lastAccessTime)��ʹ�ô����ӵ��û���Ŀ(userCount).
 * lastAccessTime�������ǵ����Ӵ��ڿ��е��г���һ����ʱ��֮�󣬿��Ը��ݴ� ��Ϣ�ѿ��������ͷŵ���
 * userCount��������ȷ��ÿ�������ϸ��õ��û���Ŀ���ᳬ�������������������Ŀ�� ��ȷ����������α�Խ����쳣��
 * 
 * ע���û�����Ҫ���Ĵ���
 * 
 * @author SINBOY
 * @version 1.0 (2004.5)
 */
public class GFConn {
	private Connection conn;// �����ݿ������

	private long lastAccessTime;// ���һ�εķ���ʱ��

	private int userCount;// ʹ�ô����ӵ��û���Ŀ
  
	public GFConn() {
		conn = null;
		userCount = 0;
		lastAccessTime = System.currentTimeMillis();
	}

	public GFConn(Connection conn, int userCount) {
		this.conn = conn;
		this.userCount = userCount;
		lastAccessTime = System.currentTimeMillis();
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime() {
		lastAccessTime = System.currentTimeMillis();
	}

	/**
	 * �������ڿ���״̬��ʱ���Ƿ񳬹��˶���ĳ�ʱʱ��
	 * 
	 * @param maxTimeout
	 * @return
	 */
	public boolean isTimeout(int maxTimeout) {
		if (System.currentTimeMillis() - lastAccessTime > maxTimeout)
			return true;
		else
			return false;
	}

	public int getUserCount() {
		return userCount;
	}

	/**
	 * �ı��������û���ʹ����Ŀ��
	 * 
	 * @param step
	 *            ��������ֵ˵�����ӣ���ֵ˵������
	 */
	public void changeUserCount(int step) {
		userCount += step;
	}

	/**
	 * �ڴ������Ͻ������ݿ��ѯ
	 * 
	 * @param sql
	 *            SQL��ѯ���
	 * @return ��ѯ���Ľ����
	 */
	public GFResultSet query(String sql) throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;
		GFResultSet myrs = null;

		if (sql != null && conn != null) {
			try {
				// System.out.println("conn:"+conn);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				// userCount++;
				rs = stmt.executeQuery(sql);
				myrs = new GFResultSet(rs);

				// �ر��α꣬�Է�ֹ�α�Խ��
				rs.close();
				stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
				// System.out.println("errCode:"+e.getErrorCode() );
				if (e.getErrorCode() == 17002)
					throw new SQLException();
			}

		}

		if (myrs != null && myrs.next()) {
			myrs.beforeFirst();
			return myrs;
		}

		else
			return null;
	}

	/**
	 * ִ��UPDATE����
	 * 
	 * @param sql
	 * @return
	 */
	public synchronized int execute(String sql) throws SQLException {
		int updateCount = 0;// ���µ�����
		Statement stmt = null;

		if (sql != null && conn != null) {
			try {
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				stmt.execute(sql);
				updateCount = stmt.getUpdateCount();

				stmt.close();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (stmt != null)
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();

						throw new SQLException();
					}

			}
		}
		return updateCount;
	}

	public void close(){
		userCount --;
	}
	/**
	 * force close the Connection
	 */
	public void forceClose() {
		if (conn != null) {
			try {
				System.out.println("�رճ����ȴ�ʱ��Ŀձ����ӣ�" + conn);
				conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

}
