package org.ictclas4j.bean;

import java.util.ArrayList;

/**
 * GBK����ĺ��ֶ�Ӧ��ƴ��������¼������ͬ�ĺ��ֵ������б�
 * 
 * @author sinboy
 * 
 */
public class Pronun {
	private String word;//����

	private String pronun;//ƴ��

	private ArrayList<Integer> homophonyList;//��ͬ�������б�

	public String getGbkID() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public ArrayList<Integer> getHomophonyList() {
		return homophonyList;
	}

	public void setHomophonyList(ArrayList<Integer> homophonyList) {
		this.homophonyList = homophonyList;
	}

	public String getPronun() {
		return pronun;
	}

	public void setPronun(String pronun) {
		this.pronun = pronun;
	}
	
	public void addHomophony(int index){
		if(homophonyList==null)
			homophonyList=new ArrayList<Integer>();
		if(!homophonyList.contains(index))
			homophonyList.add(index);
	}

}
