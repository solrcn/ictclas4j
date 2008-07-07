package com.gftech.util;

/**
 * ����������ʽ
 * 
 * @author sinboy
 * @since 2007.4.23
 * 
 */
public interface GFRegex {
	// �����ַ�,��������
	String NUM_CN = "[0-9��һ�����������߰˾�ʮҼ��������½��ƾ�ʰ]";

	// ����0�������ַ�
	String NUM_CN_NO_ZERO = "[1-9һ�����������߰˾�ʮҼ��������½��ƾ�ʰ]";

	// �������ַ�
	String NUM_CN_EXCLUDE = "[^0-9��һ�����������߰˾�ʮҼ��������½��ƾ�ʰ]";

	/**
	 * ��ʾ��Χ���ڵļ۸�
	 */
	String RANGE_LOW_PREFIX = "((������)|(������)|(������)|(������)|(����)|(���)|(���)|(����))";

	String RANGE_LOW_SUFFIX = "((����)|(����)|(֮��)|(֮��))";

	/**
	 * ��ʾ��Χ�м�ļ۸�
	 */
	String RANGE_EQUAL_PREFIX = "((���)|(��Լ)|(����)|Լ)";

	String RANGE_EQUAL_SUFFIX = "((����)|(����))";

	/**
	 * ��ʾ��Χ���ϵļ۸�
	 */
	String RANGE_GREAT_PREFIX = "((����)|(����)|(����)|(����)|(����)|(���)|(����))";

	String RANGE_GREAT_SUFFIX = "((����)|(֮��))";

	/**
	 * ��Χ���ӷ�
	 */
	String RANGE_MIDFIX = "��|��|��|-";

}
