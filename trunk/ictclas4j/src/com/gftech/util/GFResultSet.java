/*
 * Created on 2004-5-24
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.gftech.util;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 *  �������ѵ�ResultSet���ݼ�,���������ѯ���ݿⷵ�صļ�¼��.
 *  ʵ���Զ���ļ�¼����Ŀ���ǿ��Ծ���Ĺر�ResultSet��Statement��
 *  �Է�ֹ��Ϊ��������رյ��µ��α�Խ�硣
 * @author sinboy
 * 
 *
 */
public class GFResultSet {
	private int currentRow;//��ǰ����
	private int cols;//����
	private int rows;//����
	private ArrayList<HashMap> list;//������¼��
	private ArrayList<ArrayList> list2;//������¼����Ϊ�˸��ͻ��˳����ṩͨ����ŵõ���ֵ�ķ���
	private ArrayList<String> valueList;//��˳�����һ�е���ֵ���Ա�ͻ��������ͨ����Ŷ������������õ���ֵ
	
	/**
	 * �����ӡ�
	 * �ѱ�׼��ResultSet��¼��ת���Զ���ļ�¼��
	 * @param rs ��׼�ļ�¼��
	 */
	public GFResultSet(ResultSet rs){
	   currentRow=-1;
	   list=new ArrayList<HashMap>(0);
	   list2=new ArrayList<ArrayList>(0);
	  
	   
	   if(rs!=null)
	      convert(rs);
	}
	
	private void convert(ResultSet rs){
		HashMap<String,String> oneRow;
		String colName;
		String colValue;
		
		if(rs!=null){
			try{
			/**
			 * �õ�����������
			 */
			ResultSetMetaData rsMeta=rs.getMetaData();
			cols=rsMeta.getColumnCount();
			rs.last();
			rows=rs.getRow();
			
			//������ָ���ƶ���һ�е�ǰ��
            rs.beforeFirst();
			while(rs.next()){
				oneRow=new HashMap<String,String>(0);
		        valueList=new ArrayList<String>(0);
				for(int i=1;i<=cols;i++){
				  colName=rsMeta.getColumnName(i);
				  colValue=rs.getString(i);
				  oneRow.put(colName.toUpperCase() ,colValue );
				  valueList.add(colValue);
				}
				list.add(oneRow);
				list2.add(valueList);
			}
			
			
			}catch(SQLException e){
				e.printStackTrace() ;
			}
			
		}
	}
	
	/**
	 * �жϼ�¼�����Ƿ�����һ�м�¼
	 * @return  ����У�����TRUE
	 */
	public boolean next(){
		if(currentRow>=rows-1)
		  return false;
		
		currentRow++;
		return true;
	}
	
	/**
	 * �������������ش�������Ӧ����ֵ
	 * @param colName ����
	 * @return ��ֵ
	 */
	public String getString(String colName){
		HashMap oneRow=null;
		if(colName!=null){
			if(list!=null){
			
			oneRow=(HashMap)list.get(currentRow);
			return (String)oneRow.get(colName.toUpperCase() );
			}
		}
		return null;
	}
	 
	
	/**
	 * �ṩ�������������ֵ�ķ�����
	 * @param col �е����
	 * @return ��Ӧ����ֵ
	 */
	/*
	 * ������ֱ��ͨ��list��ʵ�֣�����
	 * Ŀǰ���ڵ���������HashMap.value()�õ�VALUES��COLLECTION֮��
	 * ͨ��ITERATOR������ʱ������ֵ�Ĵ����ǰ�ԭ�е�˳�����У�����
	 * �޷��ô˷�����ʵ�ְ���ŷ��ʡ�Դ�������£�
	 *  HashMap oneRow=null;
	 *  Collection ct=null;
	 * Iterator itr=null;
	 * String[] names=null;
	 * String colName=null;
	 * int index=-1;
	 * if(col>=0){
	 *    if(list!=null){
	 *     oneRow=(HashMap)list.get(currentRow);
	 *     ct=oneRow.values();
	 *     itr=ct.iterator();
	 *      while(itr.hasNext() ){
	 *         colName=(String)itr.next()  ;
	 *         index++;
	 *         if(col==index)
	 *           return colName;
	 *     }
	 *   }
	 * }
	 * return null;
	 * 
	 * 
	 * ����İ취��ÿһ�е���ֵ��˳��ŵ�ArrayList�У��ܿ�MAP��
	 */
	public String getString(int col){
		ArrayList alRow=null;
		 
		if(col>=0){
			if(list2!=null){
			   alRow=(ArrayList)list2.get(currentRow);
			   return (String)alRow.get(col);
			}
		}

		return null;
	}
   
   public void beforeFirst(){
   	 currentRow=-1;
   }
   
   /**
    * ���ؼ�¼��������
    * @return
    */
   public int cols(){
   	  return cols;
   }
   
   /**
    * ���ؼ�¼��������
    * @return
    */
   public int rows(){
   	return rows;
   }
}
