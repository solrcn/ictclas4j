package com.gftech.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * ���ļ���صĳ��ò���
 * 
 * @author Administrator
 */
public class GFFile {

	/**
	 * ��ָ���������ļ��ж�ȡָ����������Ϣ
	 * 
	 * @param fileName
	 *            ָ���������ļ���
	 * @param propName
	 *            ָ�������������
	 * @return ����ָ�������Ӧ��ֵ
	 */
	public static String getConfig(String fileName, String propName) throws IOException {
		String value = null;

		if (fileName != null && propName != null) {
			StringBuffer sb = readFile(fileName);
			if (sb != null) {
				String[] ps = sb.toString().split("\r\n");
				for (String str : ps) {
					if (str.startsWith("#") || str.startsWith("��"))
						continue;
					int index = str.indexOf("=");
					if (index > 0 && index < str.length() - 1) {
						String name = str.substring(0, index);
						if (propName.equals(name)) {
							int index2 = str.indexOf("#");
							if (index2 > index)
								value = str.substring(index + 1, index2);
							else
								value = str.substring(index + 1);
							value = value.trim();
							break;
						}
					}
				}
			}
		}
		return value;
	}

	/**
	 * �޸������ļ�������
	 * 
	 * @param fileName
	 *            �����ļ���
	 * @param propName
	 *            ������
	 * @param newValue
	 *            ��ֵ
	 */
	public static boolean setConfig(String fileName, String propName, String newValue) throws FileNotFoundException, IOException {

		if (fileName != null && propName != null && newValue != null) {
			StringBuffer sb = readFile(fileName);
			if (sb != null) {
				String str = null;
				boolean flag = false;
				String[] ps = sb.toString().split("\r\n");
				if (ps != null && ps.length > 0) {
					for (int i = 0; i < ps.length; i++) {
						str = ps[i];
						if (str.startsWith("#") || str.startsWith("��"))
							continue;
						int index = str.indexOf("=");
						if (index > 0 && index < str.length() - 1) {
							String name = str.substring(0, index);
							if (propName.equals(name)) {
								flag = true;
								ps[i] = str.substring(0, index) + "=" + newValue;
								break;
							}
						}
					}

					String ws = "";
					if (!flag) {
						ws += propName + "=" + newValue + "\r\n";
					}
					for (int i = 0; i < ps.length; i++)
						ws += ps[i] + "\r\n";
					writeTxtFile(fileName, ws, false);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * �������ļ��ж�ȡ���е�������Ϣ
	 * 
	 * @param fileName
	 *            �����ļ���
	 * @return �ѽ��������MAP��
	 */
	public static HashMap<String,String> getConfig(String fileName) throws IOException {
		HashMap<String, String> confs = null;

		if (fileName != null) {
			Properties props = new Properties();
			try {
				String name = null;
				FileInputStream fin = null;
				Enumeration propNames = null;
				File file = null;
				String value = null;

				file = new File(fileName);
				if (file.exists()) {

					fin = new FileInputStream(file);
					props.load(fin);
					propNames = props.propertyNames();
					confs = new HashMap<String, String>(1);
					while (propNames.hasMoreElements()) {
						name = (String) propNames.nextElement();
						value = props.getProperty(name);
						confs.put(name, value);

					}
					fin.close();
				}
				props.clear();
			} catch (IOException e) {
				throw new IOException();
			}
		}

		return confs;

	}

	/**
	 * ��־��¼.����ֱ��д��ķ���
	 * 
	 * @param fileName
	 *            ָ�������־���ļ��������������·����Ҳ�����Ǿ���·��
	 * @param msg
	 *            ��־���ݣ�������msg�ַ����м����"\n"���ﵽ���е�Ŀ��
	 * @param append
	 *            д�뷽ʽ�Ƿ�����׷�ӵķ�ʽд��,�������׷�ӷ�ʽ��������ļ��е�����
	 */
	public static boolean log(String fileName, String msg, boolean append) throws IOException {
		FileWriter fw = null;
		PrintWriter out = null;

		if (fileName != null && msg != null)
			try {
				String parent;
				File fp;

				File file = new File(fileName);
				// ����ļ������ڣ��ʹ���һ�������Ŀ¼Ҳ�����ڣ�Ҳ����һ��
				if (!file.exists()) {
					parent = file.getParent();
					if (parent != null) {
						fp = new File(parent);

						if (!fp.isDirectory())
							fp.mkdirs();
					}

				}

				String[] msgs = msg.split("\n");
				fw = new FileWriter(file, append);
				out = new PrintWriter(fw);
				out.println("------" + new Date() + "-------");
				for (int i = 0; i < msgs.length; i++) {
					out.println(msgs[i]);
				}
				out.println("");
				out.flush();
				out.close();
				return true;
			} catch (IOException e) {
				throw new IOException();
			} finally {
				if (out != null)
					out.close();
			}
		return false;
	}

	/**
	 * ��ָ�����ļ�����д��ָ������Ϣ��
	 * <p>
	 * ����ļ������ڣ����Զ����´���һ����
	 * <p>
	 * ���Ҫд������ݵ����лس����з�������д���ʱ����л��С�
	 * <p>
	 * ��д��ʱ�����ʱ����Ϣ��
	 * 
	 * @param fileName
	 * @param msg
	 * @return
	 */
	public static boolean log(String fileName, String msg) throws IOException {
		try {

			return log(fileName, msg, true);

		} catch (IOException e) {
			throw new IOException();
		}
	}

	/**
	 * ���ַ�����ʽ��ȡȫ������
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return �ı��ļ�������
	 */
	public static String readTxtFile(String fileName) throws IOException {
		String result = null;

		ArrayList<String> list = readTxtFile2(fileName);
		if (list != null) {
			result = "";

			for (String value : list) {
				result += value + "\n";
			}

		}
		return result;
	}

	public static ArrayList<String> readTxtFile2(String fileName) throws IOException {
		ArrayList<String> result = null;
		FileInputStream fin = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		File file = null;
		String value = null;

		if (fileName != null) {
			file = new File(fileName);
			if (file.exists()) {
				result = new ArrayList<String>(); 
				fin = new FileInputStream(file);
				in = new InputStreamReader(fin);
				br = new BufferedReader(in);
				while ((value = br.readLine()) != null) {
					result.add(value);
				}

			}
		}
		return result;
	}

	/**
	 * ��ȡ�ı��ļ��ĵ�row�����ݣ��ӣ���ʼ
	 * <p>
	 * ����в����Ǹ�ֵ����ָ���ǵ����ڼ���
	 * 
	 * @param fileName
	 *            �ı��ļ���
	 * @param row
	 *            �ڼ���
	 * @return ָ���е�����
	 */
	public static String readTxtFile(String fileName, int row) throws IOException {
		String result = null;
		FileInputStream fin = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		File file = null;
		String value = null;
		int i = 0;

		if (fileName != null) {
			file = new File(fileName);
			if (file.exists()) {
				try {
					fin = new FileInputStream(file);
					in = new InputStreamReader(fin);
					br = new BufferedReader(in);
					while ((value = br.readLine()) != null) {
						if (row >= 0 && row == i)
							return value;
						i++;
					}

					if (row < 0) {
						row = i + row;
						if (row < 0)
							return null;
						result = readTxtFile(fileName, row);
					}
				} catch (IOException e) {
					throw new IOException();
				}
			}
		}
		return result;
	}

	/**
	 * ��ȡ�������ļ�
	 * 
	 * @param fileName
	 *            �ļ�·��
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBinFile(String fileName) throws IOException {
		byte[] data = null;

		FileInputStream fin = null;
		DataInputStream in = null;
		List<Byte> bl = null;
		File file = null;
		int value = -1;
		if (fileName != null) {
			file = new File(fileName);
			if (file.exists()) {

				try {
					fin = new FileInputStream(file);
					in = new DataInputStream(fin);
					bl = new ArrayList<Byte>();
					while ((value = in.read()) != -1) {
						bl.add((byte) value);
					}
				} catch (IOException e) {
					throw new IOException();
				}

				data = new byte[bl.size()];
				for (int i = 0; i < bl.size(); i++) {
					data[i] = bl.get(i);
				}
			}
		}
		return data;
	}

	/**
	 * ���ɶ������ļ�
	 * 
	 * @param fileName
	 *            �ļ�·��
	 * @param data
	 *            ������������
	 * @return
	 * @throws IOException
	 */
	public static boolean writeBinFile(String fileName, byte[] data) throws IOException {
		FileOutputStream fo = null;
		DataOutputStream out = null;

		if (fileName != null && data != null)
			try {
				String parent;
				File fp;

				File file = new File(fileName);
				// ����ļ������ڣ��ʹ���һ�������Ŀ¼Ҳ�����ڣ�Ҳ����һ��
				if (!file.exists()) {
					parent = file.getParent();
					if (parent != null) {
						fp = new File(parent);
						if (!fp.isDirectory())
							fp.mkdirs();
					}
				}

				fo = new FileOutputStream(file);
				out = new DataOutputStream(fo);
				out.write(data);
				out.flush();
				out.close();
				return true;
			} catch (IOException e) {
				throw new IOException();
			} finally {
				if (out != null)
					out.close();
			}
		return false;
	}

	public static File createFile(String fileName) throws IOException {

		if (fileName != null) {
			File file = new File(fileName);
			// ����ļ������ڣ��ʹ���һ�������Ŀ¼Ҳ�����ڣ�Ҳ����һ��
			if (!file.exists()) {
				String regex = GFFinal.FILE_SEP;
				ArrayList<String> list = new ArrayList<String>();
				String[] strs = GFString.atomSplit(fileName);
				StringBuffer sb = new StringBuffer();
				for (String str : strs) {
					if (regex.equals(str)) {
						list.add(sb.toString());
						sb.delete(0, sb.capacity());
					} else
						sb.append(str);
				}

				StringBuffer parent = new StringBuffer();
				for (int i = 0; i < list.size(); i++) {
					if (i > 0)
						parent.append(GFFinal.FILE_SEP);
					parent.append(list.get(i));

					File fp = new File(parent.toString());
					if (!fp.isDirectory())
						fp.mkdir();
				}
				if (file.createNewFile())
					return file;

			}
			return file;
		}
		return null;
	}

	/**
	 * д�ı��ļ�.���д���������л��з�"\n"�Ļ�,�Զ���д���ļ��л���
	 * 
	 * @param fileName
	 *            �ļ�·��
	 * @param txt
	 *            Ҫд����ļ���Ϣ
	 * @return
	 * @throws IOException
	 */
	public static boolean writeTxtFile(String fileName, String txt) throws IOException {
		return writeTxtFile(fileName, txt, true);
	}

	/**
	 * д�ı��ļ�.���д���������л��з�"\n"�Ļ�,�Զ���д���ļ��л���
	 * 
	 * @param fileName
	 *            �ļ�·��
	 * @param txt
	 *            Ҫд����ļ���Ϣ
	 * @param isAppend
	 *            �Ƿ���׷�ӵķ�ʽд��
	 * @return
	 * @throws IOException
	 */
	public static boolean writeTxtFile(String fileName, String txt, boolean isAppend) throws IOException {
		FileWriter fw = null;
		PrintWriter out = null;

		if (fileName != null && txt != null)
			try {
				String parent;
				File fp;

				File file = new File(fileName);
				// ����ļ������ڣ��ʹ���һ�������Ŀ¼Ҳ�����ڣ�Ҳ����һ��
				if (!file.exists()) {
					parent = file.getParent();
					if (parent != null) {
						fp = new File(parent);

						if (!fp.isDirectory())
							fp.mkdirs();
					}

				}

				String[] msgs = txt.split("\n");
				fw = new FileWriter(file, isAppend);
				out = new PrintWriter(fw);
				for (int i = 0; i < msgs.length; i++) {
					out.println(msgs[i]);
				}
				out.flush();
				out.close();
				return true;
			} catch (IOException e) {
				throw new IOException();
			} finally {
				if (out != null)
					out.close();
			}
		return false;
	}

	public static boolean writeTxtFile(String fileName, ArrayList<String> txtList, boolean isAppend) throws IOException {
		FileWriter fw = null;
		PrintWriter out = null;

		if (fileName != null && txtList != null && txtList.size() > 0)
			try {
				String parent;
				File fp;

				File file = new File(fileName);
				// ����ļ������ڣ��ʹ���һ�������Ŀ¼Ҳ�����ڣ�Ҳ����һ��
				if (!file.exists()) {
					parent = file.getParent();
					if (parent != null) {
						fp = new File(parent);

						if (!fp.isDirectory())
							fp.mkdirs();
					}

				}

				fw = new FileWriter(file, isAppend);
				for (String txt : txtList) {
					String[] msgs = txt.split("\n");
					out = new PrintWriter(fw);
					for (int i = 0; i < msgs.length; i++) {
						out.println(msgs[i]);
					}
					out.flush();
				}
				out.close();
				return true;
			} catch (IOException e) {
				throw new IOException();
			} finally {
				if (out != null)
					out.close();
			}
		return false;
	}

	public static boolean delDir(String dirName) {
		return delDir(dirName, false);
	}

	/**
	 * ɾ��ָ��Ŀ¼����������ΪdirName��Ŀ¼
	 * 
	 * @param dirName
	 *            �ļ�������
	 * @param isDelCurDir
	 *            �Ƿ�ɾ����ǰ�ļ���
	 */
	public static boolean delDir(String dirName, boolean isDelCurDir) {
		boolean result = false;
		if (dirName != null) {
			File file = new File(dirName);
			if (file.exists() && file.isDirectory()) {
				for (File fl : file.listFiles()) {
					if (fl.isFile()) {
						System.out.println(fl.getAbsolutePath());
						fl.delete();
					} else
						delDir(fl.getPath(), true);
				}
				if (isDelCurDir) {
					String[] files = file.list();
					if (files == null || files.length == 0)
						file.delete();
				}

				result = true;
			}
		}

		return result;
	}

	/**
	 * ɾ��ָ�����Ƶ��ļ��м�������������ļ�
	 * 
	 * @param dir
	 *            ָ��Ҫɾ��Ŀ�����ڵ��ļ���
	 * @param delDirName
	 *            Ҫɾ�����ļ�������
	 */
	public static void delAppointedDir(String dir, String delDirName) {
		if (dir != null && delDirName != null) {
			File file = new File(dir);
			if (file.isDirectory()) {
				String[] children = file.list();
				for (String child : children) {
					String path = dir + "\\" + child;
					File file2 = new File(path);
					if (file2.isDirectory() && path.toUpperCase().indexOf(delDirName.toUpperCase()) == path.length() - delDirName.length())
						GFFile.delDir(path, true);
					else
						delAppointedDir(path, delDirName);
				}
			}
		}
	}

	public static boolean delFile(String file) {
		if (file != null) {
			File f = new File(file);
			if (f.isFile())
				return f.delete();
		}

		return false;
	}

	public static StringBuffer readFile(String fileName) {
		StringBuffer text = new StringBuffer();
		File file = new File(fileName);
		if (file.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
				int bytesRead;
				byte[] buf = new byte[1024];
				while ((bytesRead = fis.read(buf)) != -1) {
					text.append(new String(buf, 0, bytesRead, "GBK"));
				}
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	public static boolean writeFile(String fileName, String text, boolean isAppend) {
		File file = new File(fileName);
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file, isAppend);
			fos.write(text.getBytes("GBK"));
			fos.flush();
			fos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean copyFile(String from, String to) {

		File fromFile, toFile;
		fromFile = new File(from);
		if (!fromFile.exists()) {
			return false;
		}
		toFile = new File(to);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			toFile.createNewFile();
			fis = new FileInputStream(fromFile);
			fos = new FileOutputStream(toFile);
			int bytesRead;
			byte[] buf = new byte[4 * 1024]; // 4K buffer
			while ((bytesRead = fis.read(buf)) != -1) {
				fos.write(buf, 0, bytesRead);
			}
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ��һ���ļ����µ������ļ�copy��ָ��������һ���ļ�����
	 * 
	 * @param fromDir
	 * @param toDir
	 * @return
	 */
	public static boolean copyDir(String from, String to) {
		File fromDir = new File(from);
		if (!fromDir.exists()) {
			return false;
		}
		File toDir = new File(to);
		if (!toDir.exists())
			toDir.mkdir();
		for (File file : fromDir.listFiles()) {
			if (file.isDirectory()) {
				copyDir(file.getAbsolutePath(), to + "/" + file.getName());
			} else {
				copyFile(file.getAbsolutePath(), to + "/" + file.getName());
			}
		}
		return true;
	}

	/**
	 * ��ȡһ��Ŀ¼�µ������ļ���,��Ŀ¼�ų�����
	 * 
	 * @param path
	 * @return
	 */
	public static ArrayList<String> getFilesOfDir(String path) {
		ArrayList<String> result = null;
		if (path != null) {
			File file = new File(path);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					result = new ArrayList<String>();
					for (File f : files) {
						if (f.isFile())
							result.add(f.getName());
					}
				}
			}
		}

		return result;
	}

	/**
	 * �г�ָ��Ŀ¼�µ������ļ����ļ��У�������Ŀ¼
	 * 
	 * @param path
	 * @return
	 */
	public static ArrayList<String> listAllFiles(String path) {
		ArrayList<String> result = null;
		if (path != null) {
			File file = new File(path);
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				if (files != null) {
					result = new ArrayList<String>();
					for (File f : files) {
						String absPath = f.getAbsolutePath();
						result.add(absPath);
						if (f.isDirectory()) {
							ArrayList<String> subList = listAllFiles(absPath);
							for (int i = 0; subList != null && i < subList.size(); i++) {
								String str = subList.get(i);
								result.add(str);
							}
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * ���ɴ��󱨸档
	 * <p>
	 * ���ɵĴ��󱨸�Ϊ���֣�һ���Ǽ�¼�������ļ����У�һ��һ���ļ�
	 * <p>
	 * �ڶ���Ϊ���ʹ��󱨸浽Ӧ�ó������ϵͳ��AMS)
	 * 
	 * @param level
	 *            ���󼶱�
	 *            <p>
	 *            0��һ�����
	 *            <p>
	 *            1��Ӧ�ó��򼶴���
	 *            <p>
	 *            2��ϵͳ������
	 *            <p>
	 *            3����������
	 * @param appName
	 *            Ӧ�ó�������
	 * @param className
	 *            ����
	 * @param methodName
	 *            ������
	 * @param errDesc
	 *            ��������
	 */
	public static void logErr(int level, String appName, String className, String methodName, String errDesc) {
		String allInfo = null;
		String sysInfo = GFCommon.getSystemInfo();

		if (errDesc != null) {
			allInfo = sysInfo + "\n\n" + "host:" + GFNet.getLocalHost() + "\n" + "appName:" + appName + "\n" + "className:" + className + "\n"
					+ "methodName:" + methodName + "\n" + "errLevel:" + level + "\n" + "errDesc:" + errDesc;

			logFile("err", allInfo);
			// sendToAMS(allInfo);//send to app manage system
		}

	}

	/**
	 * ��¼��־��Ϣ��
	 * <p>
	 * Ĭ������£�����־д�뵽��ǰ·����logs�ļ��е��С�
	 * <p>
	 * �ļ�����������Ϊ��"logs\\"+fileName+date+".txt"
	 * 
	 * @param fileName
	 *            �ļ�������ָ�����fileName����
	 * @param content
	 *            ��־����
	 * @see com.gftech.util.GFDate
	 * @see com.gftech.util.GFFile
	 * @return д��ɹ�����true,���򷵻�false
	 */
	public static boolean logFile(String fileName, String content) {
		boolean result = false;
		String name = null;
		String strDate = null;

		if (fileName != null && content != null) {
			strDate = GFDate.getCurrentDate("yyyymmdd");
			name = "logs\\" + fileName + strDate + ".txt";

			try {
				result = log(name, content);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * �ж�ָ�����ļ��Ƿ����
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return
	 */
	public static boolean isExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * ȡ��һ��XML�ļ��ĸ��ڵ㰴DOM��ʽ���н���
	 * 
	 * @param xmlFile
	 * @return
	 */
	public static Element getRootElement(String xmlFile) {
		Element root = null;

		// XML�����塣
		SAXBuilder builder = new SAXBuilder();
		try {
			Document doc = builder.build(new File(xmlFile));

			// �õ���Ԫ��
			root = doc.getRootElement();
		} catch (JDOMException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	/**
	 * �ַ����Ƿ�ָ�����ļ���·��
	 * 
	 * @param path
	 */
	public static boolean isFilePath(String path) {
		String[] invalidChar = { "\\", "/", "*", ":", "?", "i", "<", ">", "|" };

		if (path != null && path.length() > 0) {
			String[] ss = path.split("\\");
			for (int i = 0; i < path.length(); i++) {
				for (int j = 0; j < invalidChar.length; j++)
					if (ss[i].indexOf(invalidChar[j]) != -1) {
						if (i == 0 && ":".equals(invalidChar[j]) && ss[i].length() == 2 && GFString.isLetter(ss[i].substring(0, 1)))
							break;
						else
							return false;
					}

			}
		}

		return false;
	}

	/**
	 * �õ�һ���ı��ļ�����һ��Ŀ¼�������ı��ļ�������
	 * 
	 * @param path
	 *            �ļ�·��
	 * @param fliter
	 *            ��Ҫ�����˵��ļ���׺
	 * @return ������
	 */
	public static int getAllFilesLines(String path, String... fliter) throws IOException {
		int result = 0;
		if (path != null) {
			ArrayList<String> allFiles = listAllFiles(path);
			for (int i = 0; allFiles != null && i < allFiles.size(); i++) {
				boolean flag = false;
				String file = allFiles.get(i);
				if (fliter != null) {
					for (String str : fliter) {
						if (file.lastIndexOf(str) == file.length() - str.length()) {
							flag = true;
							break;
						}
					}
				} else
					flag = true;

				if (flag) {
					ArrayList<String> lines = readTxtFile2(file);
					if (lines != null) {
						result += lines.size();
						System.out.println(file + "," + lines.size());
					}
				}
			}
		}
		return result;
	}

	public static boolean serialize(String fileName, Object obj) throws IOException {
		if (fileName != null && obj != null) {
			FileOutputStream fw = null;
			ObjectOutputStream out = null;

			try {
				String parent;
				File fp;

				File file = new File(fileName);
				// ����ļ������ڣ��ʹ���һ�������Ŀ¼Ҳ�����ڣ�Ҳ����һ��
				if (!file.exists()) {
					parent = file.getParent();
					if (parent != null) {
						fp = new File(parent);

						if (!fp.isDirectory())
							fp.mkdirs();
					}

				}

				fw = new FileOutputStream(fileName);
				out = new ObjectOutputStream(fw);
				out.writeObject(obj);
				out.flush();
				out.close();
				return true;
			} catch (IOException e) {
				throw new IOException();
			} finally {
				if (out != null)
					out.close();
			}
		}
		return false;

	}

	public static Object diserialize(String fileName) throws IOException {
		Object result = null;
		if (fileName != null) {
			FileInputStream fw = null;
			ObjectInputStream out = null;

			try {
				String parent;
				File fp;

				File file = new File(fileName);
				// ����ļ������ڣ��ʹ���һ�������Ŀ¼Ҳ�����ڣ�Ҳ����һ��
				if (!file.exists()) {
					parent = file.getParent();
					if (parent != null) {
						fp = new File(parent);

						if (!fp.isDirectory())
							fp.mkdirs();
					}

				}

				fw = new FileInputStream(file);
				out = new ObjectInputStream(fw);
				result = out.readObject();
				out.close();
			} catch (IOException e) {
				throw new IOException();
			} catch (ClassNotFoundException e) {
				throw new IOException();
			} finally {
				if (out != null)
					out.close();
			}
		}
		return result;

	}
}