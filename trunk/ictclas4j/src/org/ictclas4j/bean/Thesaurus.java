package org.ictclas4j.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.gftech.util.GFFile;
import com.gftech.util.GFString;

/**
 * ͬ��ʻ�࣬����ͬ��ʡ�����ʡ�����ʣ��������Լ�Ĺ�ϵ��Ȩֵ��ȷ���� Ȩֵ��-1<value<1��������1˵����˼��ȫ��ͬ������-1˵����˼��ȫ�෴
 * 
 * @author sinboy
 * @since 2007.8.12
 * 
 */
public class Thesaurus {
	// �洢�ʻ�Ĵʵ�
	private ArrayList<String> dict;

	// �洢������֮��Ĺ�ϵ
	private ArrayList<Relation> relations;

	// �洢ÿ�����ڴʵ�dict�е�λ��
	private HashMap<String, Integer> positions;
	 

	static Logger logger = Logger.getLogger(Thesaurus.class);

	public Thesaurus() {
		dict = new ArrayList<String>();
		relations = new ArrayList<Relation>();
		positions = new HashMap<String, Integer>();
	}
	
	 

	private class Pair {
		private String word1;

		private String word2;

		private int pos;

		private double value;

		public Pair(String line) {
			if (line != null) {
				line=line.replaceAll("��",",");
				String[] strs = line.split(",");
				if (strs.length == 3) {
					word1 = strs[0].toLowerCase();
					word2 = strs[1].toLowerCase(); 
					value = GFString.cdbl(strs[2]);
				}
			}
		}

		public int getPos() {
			return pos;
		}

		public double getValue() {
			return value;
		}

		public String getWord1() {
			return word1;
		}

		public String getWord2() {
			return word2;
		}

	}

	public boolean load(String file) {
		boolean result = false;

		if (file != null) {
			try {
				ArrayList<String> list = GFFile.readTxtFile2(file);
				// add to dict and positions hashMap
				for (String str : list) {
					Pair pair = new Pair(str);
					if (positions.get(pair.getWord1()) == null) {
						int size = dict.size();
						dict.add(pair.getWord1());
						positions.put(pair.getWord1(), size);
						 
					}
					if (positions.get(pair.getWord2()) == null) {
						int size = dict.size();
						dict.add(pair.getWord2());
						positions.put(pair.getWord2(), size);
						 
					}
				}

				// create their relations
				for (String str : list) {
					Pair pair = new Pair(str);
					int row = positions.get(pair.getWord1());
					int col = positions.get(pair.getWord2());
					Relation rel = new Relation(row, col, pair.getValue());

					if (row > col)// ��֤���е�Ԫ�ض������ڶ�ά���������
						swapRowCol(rel);

					// ���в���A & B's relation
					int pos = binarySearch(relations, rel);
					insert(relations, rel, pos); 

					// insert A&A's relation
					Relation rel2 = new Relation(row, row, 1);
					int pos2 = binarySearch(relations, rel2);
					insert(relations,rel2,pos2);
					
					// insert B&B's relation
					Relation rel3=new Relation(col,col,1);
					int pos3=binarySearch(relations,rel3);
					insert(relations,rel3,pos3);
				}
			} catch (IOException e) {
				logger.error("load thesaurus dict failed:", e);
			}
		}
		return result;
	}

	public boolean save(String file) {
		return false;
	}

	public boolean addItem(String word1, String word2, int pos, double value) {
		return false;
	}

	public ArrayList<ThesaurusItem> getThesaurus(String word) {
		ArrayList<ThesaurusItem> result = null;

		if (word != null) {
			word=word.toLowerCase();
			Integer integer = positions.get(word); 
			if(integer==null)
				return null;
			int pos=integer.intValue();
			if (pos >= 0 && pos < dict.size()) {
				Relation rel = new Relation(pos, pos, 0);
				int index = binarySearch(relations, rel);
				if (index >= 0) {
					result=new ArrayList<ThesaurusItem>();
					//read rows
					for (int j = index; j < relations.size(); j++) {
						Relation next = relations.get(j);
						if (next.getRow() == pos) {
							ThesaurusItem item = new ThesaurusItem(dict.get(next.getCol()), next.getValue());
							result.add(item);
						} else
							break;
					}
					
					//read cols
					for(int j=index-1;j>=0;j--){
						Relation prev=relations.get(j);
						if(prev.getCol()==pos){
							ThesaurusItem item = new ThesaurusItem(dict.get(prev.getRow()), prev.getValue());
							result.add(item);
						}
						 
					}
					
				}
			}

		}
		return result;
	}

	// ���ַ�����,������ص��Ǹ�ֵ��˵��û���ҵ���������ֵ�ľ���ֵ�ǲ���ֵ��ӽ���λ��
	private int binarySearch(ArrayList<Relation> list, Relation rel) {
		int result = -1;

		if (list != null && rel != null) {
			int start = 0;
			int end = list.size() - 1;
			int mid = (end - start) / 2;

			while (start <= end) {
				Relation midRel = list.get(mid);
				if (midRel.getRow() == rel.getRow() && (midRel.getCol() == rel.getCol() || rel.getCol() == -1)) {
					result = mid;
					return result;
				} else if (rel.getRow() > midRel.getRow() || rel.getRow() == midRel.getRow()
						&& rel.getCol() > midRel.getCol())
					start = mid + 1;
				else if (rel.getRow() < midRel.getRow() || rel.getRow() == midRel.getRow()
						&& rel.getCol() < midRel.getCol())
					end = mid - 1;

				if (start <= end)
					mid = (start + end) / 2;
			}

			if (mid == 0) {
				result = Integer.MIN_VALUE;
			} else
				result = -mid;
		}
		return result;
	}

	private void swapRowCol(Relation rel) {
		if (rel != null) {
			int temp = rel.getCol();
			rel.setCol(rel.getRow());
			rel.setRow(temp);
		}
	}

	private void insert(ArrayList<Relation> relations, Relation rel, int index) {
		if (relations != null && rel != null) {
			if (Math.abs(index) < relations.size()) {
				if (index >= 0)
					relations.set(index, rel);
				else {
					index = index == Integer.MIN_VALUE ? 0 : Math.abs(index);
					if (relations.size() == 0)
						relations.add(index, rel);
					else {
						Relation last = relations.get(index);
						if (last.getRow() < rel.getRow() || last.getRow() == rel.getRow()
								&& last.getCol() < rel.getCol())
							relations.add(index + 1, rel);
						else
							relations.add(index, rel);
					}
				}
			}
		}
	}
}



class Relation {
	private int row;// �ڶ�ά���е���ֵ

	private int col;// �ڶ�ά���е���ֵ

	private double value;// ������֮���Ȩֵ

	public Relation(int row, int col, double value) {
		this.row = row;
		this.col = col;
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

}