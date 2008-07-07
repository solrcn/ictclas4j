package org.ictclas4j.bean;

import java.io.Serializable;


/**
 * �ڽӴ��Ա��
 * 
 * @author sinboy
 * @since 2007.5.22
 * 
 */
public class AdjoiningPos  implements Cloneable, Serializable{ 
	private Pos pos;// ���Ա�� 

	private double value; // �ڽ�ֵ

	private int prev;// ǰһ���ʵ�N�������к͸ô�����ƥ�����һ�����±�λ�ã�

	private boolean isBest;
 
	private static final long serialVersionUID = 10000L;
	public AdjoiningPos() {

	}
	
	public AdjoiningPos(int tag,double value) {
      this.pos=new Pos();
      this.pos.setTag(tag);
      this.value=value;
	}
	

	public AdjoiningPos(Pos pos, double value) {
		this.pos = pos;
		this.value = value;
	}

 
	public Pos getPos() {
		return pos;
	}

	public void setPos(Pos pos) {
		this.pos = pos;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getPrev() {
		return prev;
	}

	public void setPrev(int prevPos) {
		this.prev = prevPos;
	}

	public boolean isBest() {
		return isBest;
	}

	public void setBest(boolean isBest) {
		this.isBest = isBest;
	}

	 

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("pos:").append(pos);
		sb.append(",values:").append(value);
		sb.append(",isBest:").append(isBest);
		return sb.toString();

	}
	
	public AdjoiningPos clone() throws CloneNotSupportedException {
		return (AdjoiningPos) super.clone();

	}

}
