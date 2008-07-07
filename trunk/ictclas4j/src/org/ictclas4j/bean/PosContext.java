package org.ictclas4j.bean;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.gftech.util.GFNet;

/**
 * ���������ģ������������������еĹ�ϵ���磺���Ե������ֵ�Ƶ�ʡ����ڳ��ֵ�Ƶ�ʵ�
 * 
 * @author sinboy
 * @since 2008.6.4
 * 
 */
public class PosContext {

	private int totalPosFreq;// ���д��Գ��ִ������ܺ�

	private HashMap<Integer, Integer> freqMap;// ��Ƶ��,����ΪKey����ƵΪValue

	private HashMap<String, Integer> adjoiningFreqMap;// �ڽӴ�Ƶ��,�õ�I�����Ե�ֵ�ӵ�J�����Ե�ֵ��ΪKey�����磺��123843,892342��

	static Logger logger = Logger.getLogger(PosContext.class);

	public PosContext() {
		this(null);
	}

	public PosContext(String fileName) {
		freqMap = new HashMap<Integer, Integer>();
		adjoiningFreqMap = new HashMap<String, Integer>();
		load(fileName);
	}

	public boolean load(String fileName) {
		if (fileName != null) {
			File file = new File(fileName);
			if (!file.canRead())
				return false;// fail while opening the file

			try {
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));

				// ��ȡ�ܴ�Ƶ
				totalPosFreq = GFNet.readInt32(in);
				// ��ȡ����
				int posCount = GFNet.readInt32(in);
				logger.debug("tableLen:" + posCount);

				// ��ȡ���Ժʹ�Ƶ
				int[] posTable = new int[posCount];
				for (int i = 0; i < posCount; i++) {
					posTable[i] = GFNet.readInt32(in);
					int pos = posTable[i];
					int freq = GFNet.readInt32(in);
					freqMap.put(pos, freq);
					logger.debug("pos[" + i + "]:" + pos + "/" + POSTag.int2str(pos) + ",freq:" + freq);
				}

				// ��ȡ�ڽӴ�Ƶ
				for (int i = 0; i < posCount; i++) {
					for (int j = 0; j < posCount; j++) {
						int adjoiningFreq = GFNet.readInt32(in);
						StringBuffer key = new StringBuffer();
						key.append(posTable[i]).append(",").append(posTable[j]);
						adjoiningFreqMap.put(key.toString(), adjoiningFreq);
					}
				}

				in.close();
				return true;
			} catch (FileNotFoundException e) {
				logger.debug(e);
			} catch (IOException e) {
				logger.debug(e);
			}
		}
		return false;
	}

	public int getTotalPosFreq() {
		return totalPosFreq;
	}

	public HashMap<Integer, Integer> getFreqMap() {
		return freqMap;
	}

	public HashMap<String, Integer> getAdjoiningFreqMap() {
		return adjoiningFreqMap;
	}

	/**
	 * ����Ƶ��
	 * 
	 * @param pos
	 * @return
	 */
	public int getFreq(int pos) {
		if (freqMap != null) {
			Integer value = freqMap.get(pos);
			if (value != null)
				return value;
		}

		return 0;
	}

	// �ڽӴ���Ƶ��
	public int getAdjoiningFreq(int prevPos, int curPos) {
		if (adjoiningFreqMap != null) {
			StringBuffer key = new StringBuffer();
			key.append(prevPos).append(",").append(curPos);
			Integer freq = adjoiningFreqMap.get(key.toString());
			if (freq != null) {
				return freq;
			}
		}

		return 0;

	}

	// �������������ڽӵĿ�����
	public double computePossibility(int prevPos, int curPos) {
		double result = 0;
		// return a lower value, not 0 to prevent data sparse
		if (getFreq(prevPos) == 0 || getFreq(curPos) == 0)
			return 0.000001;

		int adjoiningFreq = getAdjoiningFreq(prevPos, curPos);
		int prevFreq = getFreq(prevPos);

		// 0.9 and 0.1 is a value based experience
		result = 0.9 * (double) adjoiningFreq;
		result /= (double) prevFreq;
		result += 0.1 * ((double) prevFreq / totalPosFreq);

		return result;
	}

}
