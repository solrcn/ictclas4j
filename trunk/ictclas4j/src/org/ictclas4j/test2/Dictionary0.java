package org.ictclas4j.test2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.ictclas4j.bean.POSTag;
import org.ictclas4j.util.Utility;

import com.gftech.util.GFNet;

public class Dictionary0 {
	/**
	 * �ʵ��,��6768��,GB2312����(before)
	 * 22034����gbk����+��ĸ���֣�now)
	 */
	public  WordTable0[] wts;

	private int wordCount;// �ʵĸ���

	private long totalFreq;
	 

	public int dict_count; 
	
	static Logger logger =Logger.getLogger(Dictionary0.class);

	public Dictionary0( ){
		this( false);
	}

	public Dictionary0( boolean isExtend) {
		init(isExtend);  
	}
	
	public void init(boolean isExtend) {
		wordCount = 0;
		totalFreq = 0;
		dict_count=isExtend?Utility.GBK_NUM_EXT:Utility.GB_NUM;
		wts = new  WordTable0[dict_count];
		 
	}

	public boolean load(String filename) {
		return load(filename, false);
	}

	/**
	 * �Ӵʵ���м��ش���.��6768��������ݿ�(����5���Ǻ����ַ�),ÿ�������ݿ�������ɸ�С���ݿ�,
	 * ÿ��С���ݿ�Ϊһ������,�����ݿ���ÿ���������ǹ�һ���ֿ�ͷ��.
	 * 
	 * @param fileName
	 *            ���Ĵʵ��ļ���
	 * @param isReset
	 *            �Ƿ�Ҫ����
	 * @return
	 */
	public boolean load(String fileName, boolean isReset) {
		File file;

		int[] nBuffer = new int[3];

		file = new File(fileName);
		if (!file.canRead())
			return false;// fail while opening the file

		int i=0,j=0;
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			for ( i = 0; i < dict_count; i++) { 
				WordTable0 wt=new WordTable0();  
//				 logger.info("��" + i);
				// �ʵ����д����������ʱ���õ�λ����(Сͷ��ǰ)��ʽ,��Ҫת��һ��
				int count = GFNet.readInt32(in);
//				 logger.info(" count:" + count);
				wt.setCount(count);  
				WordItem0[] wis = new WordItem0[count];
				for (j = 0; j < count; j++, wordCount++) {
					nBuffer[0] = GFNet.readInt32(in);
					nBuffer[1] = GFNet.readInt32(in);
					nBuffer[2] = GFNet.readInt32(in);

					WordItem0 wi = new WordItem0();
					wi.setHandle(nBuffer[2]);
					if (nBuffer[1] > 0)// String length is more than 0
					{
						byte[] word = Utility.readBytes(in, nBuffer[1]);
						wi.setWord(new String(word, "GBK"));

					} else
						wi.setWord("");
					 wi.setWord(Utility.getGBKWord(i)+wi.getWord());
					 StringBuffer printInfo = new StringBuffer();
					 printInfo.append(" wordLen:").append(nBuffer[1]);
					 printInfo.append(" freq:").append(nBuffer[0]);
					 printInfo.append(" handle:").append(POSTag.int2str(nBuffer[2]));
					 printInfo.append(" word:(").append(Utility.getGBKWord(i)).append(")").append(wi.getWord());
					 //logger.info(printInfo.toString());

					if (isReset)// Reset the frequency
						wi.setFreq(0);
					else
						wi.setFreq(nBuffer[0]);
					totalFreq += wi.getFreq();
					wis[j] = wi;
				}
				wt.setWords(wis); 
				wts[i]=wt;
			}

			in.close();
		} catch (FileNotFoundException e) {
			logger.error("load dict "+fileName+":",e);
		} catch (IOException e) {
			logger.error("load dict "+fileName+":",e);
			logger.error("i:"+i+",j:"+j);
		}
		
		return true;
	}

	public boolean save(String fileName){
		return save(fileName,false);
	}
 

	/**
	 * 
	 * @param fileName
	 * @param isSaveAll
	 *            �Ƿ񱣴����У����Ϊtrue�����Ե�isNotSave����
	 * @return
	 */
	public boolean save(String fileName, boolean isSaveAll) {

		int j;
		int[] nBuffer = new int[3];

		File file = new File(fileName);
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			for (int i = 0; i < dict_count; i++) {
				if(i==6768)
					System.out.println("test");
				int count = 0;
				WordTable0 wt=wts[i];
				if (!isSaveAll) {
					// restat the valid word count
					for (j = 0; j < wt.getCount(); j++) {
						WordItem0 wi = wt.getWordItem(j);
						if (wi!=null && !wi.isNotSave())
							count++;

					}
				} else
					count = wt.getCount();
				GFNet.writeInt32(out, count);
				for (j = 0; j < wt.getCount(); j++) {
					WordItem0 wi = wt.getWordItem(j);
					if (!isSaveAll && wi!=null && wi.isNotSave())
						continue;

					nBuffer[0] = wi.getFreq();
					nBuffer[1] = wi.getLen();
					nBuffer[2] = wi.getHandle();
					for (int n : nBuffer)
						GFNet.writeInt32(out, n);
					if (nBuffer[1] > 0)// String length is more than 0
						out.write(wi.getWord().getBytes());
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

}
