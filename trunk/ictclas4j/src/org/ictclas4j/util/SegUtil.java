package org.ictclas4j.util;


public class SegUtil {

	/**
	 * inverted document freq(���ĵ�Ƶ�ʣ�
	 * 
	 * @param d
	 *            �ĵ�����
	 * @param dw
	 *            ���ֹؼ��ʵ��ĵ�����
	 * @return
	 */
	public static double IDF(long d, long dw) {
		double result = 0;
		if (dw <= 0)
			dw = 1;
		if (d > 0 && dw > 0) {
			result = Math.log((double) d / dw);
			result = result < 0 ? 0 : result;
		}
		return result;
	}

	/**
	 * Term freq(��Ƶ��
	 * 
	 * @param count
	 *            �ؼ������ĵ��г��ֵĴ���
	 * @param total
	 *            �ĵ��йؼ��ʵ�����
	 * @return
	 */
	public static double TF(int count, int total) {
		double result = 0;

		if (count >= 0 && total > 0) {
			result = (double) count / total;
		}
		return result;

	}

	/**
	 * ����������Ϣ������Ե��㷨
	 * 
	 * @param tf
	 *            ��Ƶ
	 * @param idf
	 *            ���ĵ�Ƶ��
	 */
	public static double TF_IDF(double tf, double idf) {
		return tf * idf;
	}

	/**
	 * �õ�һ���ؼ��ʵ�Ȩ�أ��ôʵĳ���*TF/IDF
	 * 
	 * @param word
	 *            �ؼ���
	 * @param count
	 *            �ؼ�����Դ�ı����ֵĴ���
	 * @param total
	 *            Դ�ı��Ĺؼ�������
	 * @param d
	 *            �ĵ�����
	 * @param dw
	 *            ���ָùؼ��ʵ��ĵ���Ŀ
	 * @return
	 */
	public static double getWeight(String word, int count, int total, long d, long dw) {
		double result = 0;
		if (word != null) {
			result = Math.log(word.length()) / 3.0 + TF_IDF(TF(count, total), IDF(d, dw));
		}
		return result;
	}
 


}
