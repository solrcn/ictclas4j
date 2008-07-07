package org.ictclas4j.segment;

import java.util.ArrayList;

import org.ictclas4j.bean.Dictionary;
import org.ictclas4j.bean.PronunDict;
import org.ictclas4j.bean.SegNode;

/**
 * ƴд����ֻ���Զ���ʿ���о����� һ�ǰ�ƴ�����о������ǰ��������ƶȽ��о���
 * 
 * @author sinboy
 * @since 2007.6.12
 * 
 */
public class SpellCorrection {
	private Dictionary coreDict;

	private PronunDict pronunDict;

	public SpellCorrection(Dictionary coreDict, PronunDict pronunDict) {
		this.coreDict = coreDict;
		this.pronunDict = pronunDict;
	}

	public void correct(ArrayList<SegNode> sns) {
		correctByPronunciation(sns);
		correctByWordface(sns);
	}

	/**
	 * ����ƴ�����о������磺�಴��-->�Ჴ��
	 * 
	 * @param sns
	 * @param coreDict
	 */
	private void correctByPronunciation(ArrayList<SegNode> sns) {
		if (sns != null && sns.size() > 0 && coreDict != null) {

		}
	}

	/**
	 * �������������о������磺ϲ������ɽ-->ϲ������ɽ
	 * 
	 * @param sns
	 */
	private void correctByWordface(ArrayList<SegNode> sns) {
		if (sns != null && sns.size() > 0 && coreDict != null) {

		}
	}

}
