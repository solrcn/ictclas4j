package org.ictclas4j.remote;

/**
 * <pre>
 * ��������
 * 1���ִʴ�������
 * -1���ִʴ����Ӧ
 * 2��������������
 * -2������������Ӧ
 * 3��ɾ����������
 * -3��ɾ��������Ӧ
 * 4����ѯ�����������
 * -4����ѯ���������Ӧ
 * </pre>
 * 
 * @author sinboy
 * @since 2007.6.28
 */
public interface CmdID {
	byte SEG_REQUEST = 1;

	byte SEG_RESPONSE = -1;

	byte CREATE_INDEX_REQUEST = 2;

	byte CREATE_INDEX_RESPONSE = -2;

	byte DEL_INDEX_REQUEST = 3;

	byte DEL_INDEX_RESPONSE = -3;

	byte QUERY_INDEX_REQUEST = 4;

	byte QUERY_INDEX_RESPONSE = -4;

	byte BATCH_DEL_INDEX_REQUEST = 5;

	byte BATCH_DEL_INDEX_RESPONSE = -5;

	byte UPDATE_CORPFLAG_REQUEST = 6;

	byte UPDATE_CORPFLAG_RESPONSE = -6;
	
	byte BATCH_CREATE_INDEX_REQUEST=7;
	
	byte BATCH_CREATE_INDEX_RESPONSE=-7;
	
	//ԭ���ִ����󣬼����ɵķִʽ��ΪSegAtom�ı�ʾ��ʽ�����������յ��ַ�����ʾ���
	byte RAW_SEG_REQUEST = 8;

	byte RAW_SEG_RESPONSE = -8;
}
