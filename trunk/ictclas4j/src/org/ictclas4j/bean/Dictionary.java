package org.ictclas4j.bean;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.ictclas4j.util.Utility;

import com.gftech.util.GFNet;
import com.gftech.util.GFString;

public class Dictionary {
	/**
	 * �ʵ��,��6768��,GB2312����(before) 22034����gbk����+��ĸ���֣�now)
	 */
	private WordTable[] wts;

	private int wordCount;// �ʵĸ���

	private long totalFreq;// �ܴ�Ƶ

	private int dict_count;

	static Logger logger = Logger.getLogger(Dictionary.class);

	public Dictionary() {
		this(null,false);
	}
	public Dictionary(String fileName) {
		this(fileName,false);
	}
	
	public Dictionary( boolean isExtend) {
		this(null,isExtend);
	}
	
	public Dictionary(String fileName,boolean isExtend) {
		init(isExtend);
		load(fileName);
	}

	public void init(boolean isExtend) {
		wordCount = 0;
		totalFreq = 0;
		dict_count = isExtend ? Utility.GBK_NUM_EXT : Utility.GB_NUM;
		wts = new WordTable[dict_count];

	}

	/**
	 * �Ӵʵ���м��ش���.��6768��������ݿ�(����5���Ǻ����ַ�),ÿ�������ݿ�������ɸ�С���ݿ�,
	 * ÿ��С���ݿ�Ϊһ������,�����ݿ���ÿ���������ǹ�һ���ֿ�ͷ��.
	 * 
	 * @param fileName
	 *            ���Ĵʵ��ļ���
	 * @return
	 */
	public boolean load(String fileName) {
		int i = 0, j = 0;
		File file = new File(fileName);
		if (!file.canRead())
			return false;// fail while opening the file

		try {
			long offset = 0;
			WordTable wt = new WordTable();
			SegAtom sa = new SegAtom();
			HashMap<String, SegAtom> wordMap = null;
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			for (i = 0; i < dict_count; i++) {
				try {
					WordTable wtClone = wt.clone();
					logger.debug("��" + i);
					// �ʵ����д����������ʱ���õ�λ����(Сͷ��ǰ)��ʽ,��Ҫת��һ��
					int count = GFNet.readInt32(in);
					logger.debug(" count:" + count);
					wtClone.setWordCount(count);
					int wordMaxLen = GFNet.readUInt8(in);
					wtClone.setWordMaxLen(wordMaxLen);
					offset += 5;
					wordMap = new HashMap<String, SegAtom>();
					for (j = 0; j < count; j++, wordCount++) {
						SegAtom saClone = sa.clone();
						int bc = saClone.read(in, 0);
						offset += bc;
						logger.debug(saClone);
						wordMap.put(saClone.getWord(), saClone);
						totalFreq += saClone.getTotalFreq();
					}
					wtClone.setWordMap(wordMap);
					wts[i] = wtClone;
				} catch (CloneNotSupportedException e) {
					logger.fatal("Load dict:", e);
				}
			}

			in.close();
		} catch (FileNotFoundException e) {
			logger.fatal("load dict " + fileName + ":", e);
		} catch (IOException e) {
			logger.fatal("load dict " + fileName + ":", e);
			logger.fatal("i:" + i + ",j:" + j);
		}

		return true;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean save(String fileName) {

		File file = new File(fileName);
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			for (int i = 0; i < dict_count; i++) {

				int count = 0;
				WordTable wt = wts[i];
				count = wt.getWordCount();
				GFNet.writeInt32(out, count);
				GFNet.writeInt8(out, wt.getWordMaxLen());
				Collection<SegAtom> atoms = wt.getWordMap().values();
				for (SegAtom atom : atoms) {
					int size = atom.write(out);
					System.out.println(i + "," + size);
				}
			}
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		return false;
	}

	public SegAtom getSegAtom(String word, int index) {
		SegAtom result = null;

		if (word != null && word.length() > 0) {
			if (index > 0 && index < wts.length) {
				WordTable wt = wts[index];
				result = wt.getSegAtom(word);

			}
		}
		return result;
	}
	
	public boolean addSegAtom(SegAtom sa,int index){
		
		if(sa!=null && index>=0 && index<dict_count){
			if(wts!=null){
				WordTable wt=wts[index];
				if(wt!=null){
					wt.addSegAtom(sa);
				}
			}
		}
		return false;
	}

	// ��ȡͬһ���ֿ�ͷ���������Ĺؼ��ʳ���
	public int getWordMaxLen(String word, int index) {
		int result = 0;
		if (word != null && word.length() > 0) {
			if (index > 0 && index < wts.length) {
				WordTable wt = wts[index];
				return wt.getWordMaxLen();
			}
		}
		return result;
	}

	public boolean strEqual(String b1, String b2) {
		if (b1 == null && b2 == null)
			return true;
		else if (b1 != null && b2 != null) {
			return b1.equals(b2);
		}
		return false;
	}

	public int getWordType(String word) {
		if (word != null) {
			int type = Utility.charType(word);
			int len = word.length();

			if (len > 0 && type == Utility.CT_CHINESE && GFString.isAllChinese(word))
				return Utility.WT_CHINESE;
			else if (len > 0 && type == Utility.CT_DELIMITER)
				return Utility.WT_DELIMITER;

		}
		return Utility.WT_OTHER;
	}

	/**
	 * �жϹؼ����Ƿ���ڸô���
	 * 
	 * @param word
	 * @param pos
	 * @return
	 */
	public boolean isExist(String word, int pos, int index) {
		if (word != null) {
			SegAtom atom = getSegAtom(word, index);
			if (atom != null) {
				return atom.hasPos(pos);
			}
		}

		return false;
	}

	public int getFreq(String word, int pos, int index) {
		if (word != null) {
			SegAtom atom = getSegAtom(word, index);
			if (atom != null) {
				return atom.getFreqByPos(pos);
			}
		}
		return 0;
	}

	public long totalFreq() {
		return totalFreq;
	}

	public int wordCount() {
		return wordCount;
	}

	public WordTable[] getWts() {
		return wts;
	}

	public void setWts(WordTable[] wts) {
		this.wts = wts;
	}

}
