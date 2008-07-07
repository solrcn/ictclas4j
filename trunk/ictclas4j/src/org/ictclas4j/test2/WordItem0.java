package org.ictclas4j.test2;

public class WordItem0 implements Cloneable {

	private String word;

	// �����������ʶ�ʵĴ���
	private int handle;

	// Ƶ�ȣ�����˵���ôʳ��������Ͽ��еĴ��������
	private int freq;

	private boolean isNotSave;// �Ƿ���Ҫ����

	private boolean isOvercast;// ����ô��Ѿ����ڣ�����ͬ������Ҳ��ͬ�����Ƿ񸲸�

	public WordItem0() {

	}

	public WordItem0(String word, int handle, int freq) {
		this.word = word;
		this.handle = handle;
		this.freq = freq;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int frequency) {
		this.freq = frequency;
	}

	public int getHandle() {
		return handle;
	}

	public void setHandle(int handle) {
		this.handle = handle;
	}

	public int getLen() {
		if (word != null)
			return word.getBytes().length;
		else
			return 0;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public boolean isNotSave() {
		return isNotSave;
	}

	public void setNotSave(boolean isSelfDefined) {
		this.isNotSave = isSelfDefined;
	}

	public boolean isOvercast() {
		return isOvercast;
	}

	public void setOvercast(boolean isOvercast) {
		this.isOvercast = isOvercast;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("word:").append(word).append(",");
		sb.append("handle:").append(handle).append(",");
		sb.append("freq:").append(freq).append(",");
		sb.append("isNotSave:").append(isNotSave).append(",");
		sb.append("isOverCast:").append(isOvercast);
		return sb.toString();

	}

	public WordItem0 clone() {
		WordItem0 result = null;
		try {
			result = (WordItem0) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return result;
	}
}
