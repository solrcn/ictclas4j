package org.ictclas4j.bean;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * �ִ�ͼ���еĽڵ㣬ʵ�ʱ�ʾͼ�е�һ����
 * 
 * @author sinboy
 * 
 */
public class SegNode implements Cloneable, Serializable {

	private int col;// ��ʾ�ߵĺ��������յ�

	private int row;// ��ʾ�ߵ�ǰ���������

	private double weight;// Ȩ��

	private int pos;

	private int freq;

	private String word;

	private String srcWord;// �ִ�ǰ��Ӧ��ԭʼ����

	private ArrayList<AdjoiningPos> allPos;// ���ԣ�һ���ʿ��ܶ�Ӧ�������

	private int gbkID;// srcWord�е�һ���ַ���Ӧ��GBK_ID

	private static final long serialVersionUID = 10000L;

	public SegNode() {

	}

	public SegNode(int row, int col, int pos, double weight, String word) {
		this.row = row;
		this.col = col;
		this.pos = pos;
		this.weight = weight;
		this.word = word;

	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getLen() {
		return srcWord != null ? srcWord.getBytes().length : -1;
	}

	public void addPos(AdjoiningPos pos) {
		if (allPos == null)
			allPos = new ArrayList<AdjoiningPos>();
		this.allPos.add(pos);
	}

	public ArrayList<AdjoiningPos> getAllPos() {
		return allPos;
	}

	public void setAllPos(ArrayList<AdjoiningPos> posList) {
		this.allPos = posList;
	}

	public int getPosSize() {
		return allPos != null ? allPos.size() : -1;

	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public void appendWord(String word) {
		if (this.word == null)
			this.word = word;
		else
			this.word += word;
	}

	public String getSrcWord() {
		return srcWord;
	}

	public void setSrcWord(String srcWord) {
		this.srcWord = srcWord;
	}

	public int getGbkID() {
		return gbkID;
	}

	public void setGbkID(int gbkID) {
		this.gbkID = gbkID;
	}

	public SegNode clone() throws CloneNotSupportedException {
		return (SegNode) super.clone();

	}

	public SegAtom toSegAtom() {
		SegAtom sa = new SegAtom();
		ArrayList<Pos> posList = new ArrayList<Pos>();
		sa.setWord(srcWord);
		if (allPos != null){
			for (AdjoiningPos apos : allPos) {
				Pos pos = apos.getPos();
				int tag=pos.getTag();
				if(tag<0)
					pos.setTag(-tag);
				if (apos.isBest()) {
					pos.setVisible(true);
					posList.add(pos);
				} else if (pos.isVisible()) {
					posList.add(pos);
				}
			}
			
			sa.setPosList(posList);
		}
		return sa;
	}
}
