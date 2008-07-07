package org.ictclas4j.bean;

import java.util.HashMap;
/**
 * ���Ա�ǣ��ڱ�����Ա�ע�Ļ����Ͻ�������չ
 * @author sinboy
 * @since 2007.8.1
 *
 */
public class POSTag {
	public static final int SEN_BEGIN = 1 ;// ���ӵĿ�ʼ��ǡ�ʼ##ʼ��

	public static final int SEN_END = 4 ;// ���ӵĽ�����ǡ�ĩ##ĩ��

	public static final int ADJ_GEN = str2int("ag");// 'A'<<8)+'g';//Ag ������

	// ���ݴ������ء����ݴʴ���Ϊ
	// a�����ش����ǰ������A��

	public static final int ADJ = str2int("a");// a ���ݴ� ȡӢ�����ݴ� adjective �ĵ�1

	// ����ĸ��

	public static final int ADJ_AD = str2int("ad");// ad ���δ� ֱ����״������ݴʡ����ݴʴ��� a

	// �͸��ʴ���d ����һ��

	public static final int ADJ_NOUN = str2int("an");// an ���δ�

	// �������ʹ��ܵ����ݴʡ����ݴʴ��� a
	// �����ʴ���n ����һ��

	public static final int BIE = str2int("b");// b ����� ȡ���֡��𡱵���ĸ��

	public static final int CONJ = str2int("c");// c ���� ȡӢ������ conjunction �ĵ�1

	// ����ĸ��

	public static final int ADV_GEN = str2int("dg");// dg ������ ���������ء����ʴ���Ϊ

	// d�����ش����ǰ������D��

	public static final int ADV = str2int("d");// d ���� ȡ adverb �ĵ�2 ����ĸ�������1

	// ����ĸ���������ݴʡ�

	public static final int EXC = str2int("e");// e ̾�� ȡӢ��̾�� exclamation �ĵ�1

	// ����ĸ��

	public static final int FANG = str2int("f");// f ��λ�� ȡ���֡�����

	public static final int GEN = str2int("g");// g ����

	// ����������ض�����Ϊ�ϳɴʵġ��ʸ�����ȡ���֡���������ĸ��

	public static final int HEAD = str2int("h");// h ǰ�ӳɷ� ȡӢ�� head �ĵ�1 ����ĸ��

	public static final int IDIOM = str2int("i");// i ���� ȡӢ����� idiom �ĵ�1 ����ĸ��

	public static final int JIAN = str2int("j");// j ������� ȡ���֡��򡱵���ĸ��

	public static final int SUFFIX = str2int("k");// k ��ӳɷ�

	public static final int TEMP = str2int("l");// l ϰ����

	// ϰ������δ��Ϊ����е㡰��ʱ�ԡ���ȡ���١�����ĸ��

	public static final int NUM = str2int("m");// m ���� ȡӢ�� numeral �ĵ�3 ����ĸ��n��u�������á�

	public static final int NOUN_GEN = str2int("ng");// Ng ������ ���������ء����ʴ���Ϊn�����ش����ǰ������N��
	public static final int NOUN = str2int("n");// n ���� ȡӢ������ noun �ĵ�1 ����ĸ��

	public static final int NOUN_AGENT = str2int("na");// �����̣�ȡagent�ĵ�һ����ĸ

	public static final int NOUN_BUSS = str2int("nb");// �̼ң�ȡbussinesser�ĵ�һ����ĸ
 

	public static final int NOUN_LOGO = str2int("nl");//  �̱ꡢƷ��	ȡLogo�ĵ�һ����ĸ
	public static final int NOUN_LOGO_AUTO=str2int("nla");//����Ʒ��	ȡauto�ĵ�һ����ĸ
	public static final int NOUN_LOGO_CLOTHING=str2int("nlc");//�·����Ʒ��	ȡclothing�ĵ�һ����ĸ 
	public static final int NOUN_LOGO_DELICATESSEN=str2int("nld");//ʳƷ���Ʒ��	ȡdelicatessen������ĸ
	public static final int NOUN_LOGO_ELEC=str2int("nld");//�ҵ�Ʒ��	ȡelectrical appliances������ĸ
	public static final int NOUN_LOGO_FITMENT=str2int("nlf");//nlf	�Ҿ�Ʒ��	ȡfitment������ĸ
	public static final int NOUN_LOGO_HOUSE=str2int("nlh");//nlh	����Ʒ��	ȡhouse�ĵ�һ����ĸ
	public static final int NOUN_LOGO_IT=str2int("nli");//nli	IT���Ʒ��	ȡIT������ĸ
	public static final int NOUN_LOGO_IT_COMPUTER=str2int("nlic");//nlic	�������	ȡcomputer������ĸ
	public static final int NOUN_LOGO_IT_DIGITAL=str2int("nlid");//nlid	����Ʒ��	ȡdigital�ĵ�һ����ĸ
	public static final int NOUN_LOGO_IT_MOBILE=str2int("nlim");//nlim	�ֻ�Ʒ��	ȡmobile�ĵ�һ����ĸ
	public static final int NOUN_LOGO_LEECHDOM=str2int("nll");//nll	ҽҩƷ��	ȡleechdom������ĸ
	public static final int NOUN_LOGO_TOILETRY=str2int("nlt");//nlt	ϴ��Ʒ��	ȡtoiletry������ĸ
	public static final int NOUN_LOGO_WATCH=str2int("nlw");//nlw	�ֱ�Ʒ��	ȡwatch������ĸ

	public static final int NOUN_MYSELF = str2int("nm");// �Զ��������

	public static final int NOUN_PERSON = str2int("nr");// nr ���� ���ʴ��� n

	// �͡���(ren)������ĸ����һ��

	public static final int NOUN_SPACE = str2int("ns");// ns ���� ���ʴ��� n �ʹ����ʴ���s
	 
	public static final int NOUN_SPACE_BUS_STATION = str2int("nsb");//nsb	����վ��	ȡbus������ĸ
	public static final int NOUN_SPACE_COMMUNITY = str2int("nsc");//nsc	������С��	ȡcommunity������ĸ
	public static final int NOUN_SPACE_FENGJING = str2int("nsf");//nsf	�羰��ʤ	ȡ����ƴ��feng������ĸ
	public static final int NOUN_SPACE_NATION = str2int("nsg");//nsg	����	ȡ����ƴ��guo������ĸ
	public static final int NOUN_SPACE_BUILDING = str2int("nsj");//nsj	������	ȡ����ƴ��jian������ĸ
	public static final int NOUN_SPACE_HOSPITAL = str2int("nsh");//nsh	ҽԺ	ȡhospital������ĸ
	public static final int NOUN_SPACE_MARKETPLACE = str2int("nsm");//nsm	�̳����̵�	ȡmarketplace�ĵ�һ����ĸ
	public static final int NOUN_SPACE_ROAD = str2int("nsr");//nsr	�ֵ�	ȡroad������ĸ
	public static final int NOUN_SPACE_SCHOOL = str2int("nss");//nss	ѧУ	ȡschool��ǰ������ĸ
	public static final int NOUN_SPACE_SCHOOL_PRIMARY = str2int("nssp");//nssp	Сѧ	ȡprimary school�ĵ�һ����ĸ
	public static final int NOUN_SPACE_SCHOOL_MIDDLE = str2int("nssm");//nssm	��ѧ	ȡmiddle school�ĵ�һ����ĸ
	public static final int NOUN_SPACE_SCHOOL_UNIVERSITY = str2int("nssu");//nssu	��ѧ	ȡuniversity�ĵ�һ����ĸ
	public static final int NOUN_SPACE_RESTAURANT = str2int("nsu");//nsu	�͹�	
	public static final int NOUN_SPACE_PROVINCE = str2int("ns1");//ns1	ʡ����������
	public static final int NOUN_SPACE_CITY = str2int("ns2");//ns2	��
	public static final int NOUN_SPACE_COUNTY = str2int("ns2");//ns3	�أ�����
	public static final int NOUN_SPACE_TOWN = str2int("ns4");//ns4	�磨��	 
	public static final int NOUN_SPACE_VILLAGE = str2int("ns5");//ns5	��	

	public static final int NOUN_ORG = str2int("nt");// nt �������� ���š�����ĸΪ

	// t�����ʴ���n ��t ����һ��

	public static final int NOUN_LETTER = str2int("nx");// Ӣ�Ļ�Ӣ�������ַ���

	public static final int NOUN_ZHUAN = str2int("nz");// nz ����ר�� ��ר������ĸ�ĵ� 1����ĸΪz�����ʴ���n ��z ����һ�� 
	public static final int NOUN_ZHUAN_AUTO = str2int("nza");// nza	����ר��	ȡauto������ĸ
	public static final int NOUN_ZHUAN_CLOTHING = str2int("nzc");// nzc	�·����	ȡchemistry������ĸ
	public static final int NOUN_ZHUAN_DELICATESSEN = str2int("nzd");// nzd	ʳƷ���ר��	ȡdelicatessen������ĸ
	public static final int NOUN_ZHUAN_ELEC = str2int("nze");// nze	�ҵ����ר��	ȡelectric������ĸ
	public static final int NOUN_ZHUAN_IT = str2int("nzi");// nzi	IT���ר��	ȡit������ĸ
	public static final int NOUN_ZHUAN_FITMENT = str2int("nzf");// nzf	�Ҿ����ר��	ȡfitment������ĸ
	public static final int NOUN_ZHUAN_HOUSE = str2int("nzh");// nzh	�������ר��	ȡhouse������ĸ
	public static final int NOUN_ZHUAN_LEECHDOM = str2int("nzl");// nzl	ҽҩר��	ȡleechdom�ĵ�һ����ĸ
	public static final int NOUN_ZHUAN_MACHINE = str2int("nzm");// nzm	��е�豸ר��	ȡmachine����ĸ
	public static final int NOUN_ZHUAN_SPORT= str2int("nzs");// nzs	�˶����ר��	ȡsport������ĸ
	public static final int NOUN_ZHUAN_TIOLETRY = str2int("nzt");// nzt	ϴ�����ר��	ȡtoiletry������ĸ
	public static final int NOUN_ZHUAN_WORK = str2int("nzw");// nzw	ְҵר��	ȡwork������ĸ
	public static final int NOUN_ZHUAN_TOOL = str2int("nz");// nzy	��������ר��	ȡ����ƴ��yiqi������ĸ
	public static final int NOUN_ZHUAN_CHEMICAL = str2int("nz");// nzz	����ר��	

	public static final int ONOM = str2int("o");// o ������ ȡӢ�������� onomatopoeia �ĵ�1 ����ĸ��

	public static final int PREP = str2int("p");// p ��� ȡӢ���� prepositional �ĵ�1 ����ĸ��

	public static final int QUAN = str2int("q");// q ���� ȡӢ�� quantity �ĵ�1 ����ĸ��

	public static final int PRONOUN = str2int("r");// r ���� ȡӢ����� pronoun �ĵ�2 ����ĸ,��p �����ڽ�ʡ�

	public static final int SPACE = str2int("s");// s ������ ȡӢ�� space �ĵ�1 ����ĸ��

	public static final int TIME_GEN = str2int("tg");// g ʱ���� ʱ��������ء�ʱ��ʴ���Ϊ t,�����صĴ���g ǰ������T��

	public static final int TIME = str2int("t");// t ʱ��� ȡӢ�� time �ĵ�1 ����ĸ��

	public static final int AUXI = str2int("u");// u ���� ȡӢ������ auxiliary

	public static final int VERB_GEN = str2int("vg");// vg ������ ���������ء����ʴ���Ϊ v�������صĴ���g ǰ������V��

	public static final int VERB = str2int("v");// v ���� ȡӢ�ﶯ�� verb �ĵ�һ����ĸ��

	public static final int VERB_AD = str2int("vd");// vd ������ ֱ����״��Ķ��ʡ����ʺ͸��ʵĴ��벢��һ��

	public static final int VERB_NOUN = str2int("vn");// vn ������ ָ�������ʹ��ܵĶ��ʡ����ʺ����ʵĴ��벢��һ��

	public static final int PUNC = str2int("w");// w ������

	public static final int NO_GEN = str2int("x");// x �������� ��������ֻ��һ�����ţ���ĸ x ͨ�����ڴ���δ֪������ �š�

	public static final int YUQI = str2int("y");// y ������ ȡ���֡������ĸ��

	public static final int STATUS = str2int("z");// z ״̬�� ȡ���֡�״������ĸ��ǰһ����ĸ��

	public static final int UNKNOWN = str2int("un");// un δ֪�� ����ʶ��ʼ��û��Զ�����顣ȡӢ��Unkonwn ��������ĸ��(�Ǳ����׼��CSW �ִ��ж���)
	
	private static final HashMap<Integer,Double> weightMap=initWeightMap();
	
	private static HashMap<Integer,Double> initWeightMap(){
		HashMap<Integer,Double> map=new HashMap<Integer,Double>();
		map.put(ADJ_GEN,0.1);
		map.put(ADJ, 0.7);
		map.put(ADJ_AD, 0.2);
		map.put(ADJ_NOUN,0.9);
		map.put( BIE, 0.1);
		map.put(CONJ, 0.3);
		map.put(ADV_GEN, 0.1);
		map.put(ADV, 0.4);
		map.put(EXC, 0.1);
		map.put(FANG, 0.2);
		map.put(GEN, 0.1);
		map.put( HEAD,0.2);
		map.put(IDIOM,1.0);
		map.put(JIAN,1.0);
		map.put(SUFFIX,0.2);
		map.put(TEMP, 1.0);
		map.put(NUM, 0.9);
		map.put(NOUN_GEN,1.05);
		map.put(NOUN, 1.0);
		map.put(NOUN_AGENT, 1.1);
		map.put(NOUN_BUSS,1.1);   
		map.put(NOUN_LOGO_AUTO, 1.4);
		map.put(NOUN_LOGO_CLOTHING, 1.4);
		map.put(NOUN_LOGO_DELICATESSEN, 1.4);
		map.put(NOUN_LOGO_ELEC, 1.4);
		map.put(NOUN_LOGO_FITMENT, 1.4);
		map.put(NOUN_LOGO_IT, 1.4);
		map.put(NOUN_LOGO_IT_COMPUTER, 1.41);
		map.put(NOUN_LOGO_IT_DIGITAL, 1.41);
		map.put(NOUN_LOGO_IT_MOBILE, 1.41);
		map.put(NOUN_LOGO_LEECHDOM, 1.4);
		map.put(NOUN_LOGO_TOILETRY, 1.4);
		map.put(NOUN_LOGO_WATCH, 1.4); 
		map.put(NOUN_MYSELF, 1.1);
		map.put(NOUN_PERSON, 1.12);
		map.put(NOUN_SPACE, 1.2);  
		map.put(NOUN_SPACE_BUS_STATION, 1.25);
		map.put(NOUN_SPACE_COMMUNITY, 1.25);
		map.put(NOUN_SPACE_FENGJING, 1.25);
		map.put(NOUN_SPACE_NATION, 1.25);
		map.put(NOUN_SPACE_BUILDING, 1.25);
		map.put(NOUN_SPACE_HOSPITAL, 1.25);
		map.put(NOUN_SPACE_MARKETPLACE, 1.25);
		map.put(NOUN_SPACE_ROAD, 1.25);
		map.put(NOUN_SPACE_SCHOOL, 1.25);
		map.put(NOUN_SPACE_SCHOOL_PRIMARY, 1.256);
		map.put(NOUN_SPACE_SCHOOL_MIDDLE, 1.256);
		map.put(NOUN_SPACE_SCHOOL_UNIVERSITY, 1.256);
		map.put(NOUN_SPACE_RESTAURANT, 1.25);
		map.put(NOUN_SPACE_PROVINCE, 1.25);
		map.put(NOUN_SPACE_CITY, 1.3);
		map.put(NOUN_SPACE_COUNTY, 1.35);
		map.put(NOUN_SPACE_TOWN, 1.4); 
		map.put(NOUN_SPACE_VILLAGE, 1.45);
		map.put( NOUN_ORG,1.3);
		map.put( NOUN_ZHUAN, 1.3); 
		map.put( NOUN_ZHUAN_AUTO, 1.35);
		map.put( NOUN_ZHUAN_CLOTHING, 1.35);
		map.put( NOUN_ZHUAN_DELICATESSEN, 1.35);
		map.put( NOUN_ZHUAN_ELEC, 1.35);
		map.put( NOUN_ZHUAN_IT, 1.35);
		map.put( NOUN_ZHUAN_FITMENT, 1.35);
		map.put( NOUN_ZHUAN_HOUSE, 1.35);
		map.put( NOUN_ZHUAN_LEECHDOM, 1.35);
		map.put( NOUN_ZHUAN_MACHINE, 1.35);
		map.put( NOUN_ZHUAN_SPORT, 1.35);
		map.put( NOUN_ZHUAN_TIOLETRY, 1.35);
		map.put( NOUN_ZHUAN_WORK, 1.35);
		map.put( NOUN_ZHUAN_TOOL, 1.35);
		map.put( NOUN_ZHUAN_CHEMICAL, 1.35);  
		map.put( ONOM, 0.1);
		map.put(PREP, 0.2);
		map.put( QUAN,0.3);
		map.put(PRONOUN,0.2);
		map.put(SPACE, 0.2);
		map.put(TIME_GEN,0.1);
		map.put( TIME,1.15);
		map.put( AUXI,0.1);
		map.put(VERB_GEN, 0.1);
		map.put( VERB,0.5);
		map.put(VERB_AD, 0.5);
		map.put(VERB_NOUN, 0.9);
		map.put(PUNC, 0.0);
		map.put( NO_GEN,0.1);
		map.put( YUQI,0.1);
		map.put(STATUS, 0.3);
		map.put(UNKNOWN,0.5);
		return map;
	}
	

	/**
	 * Ȩ��ϵ�������ʣ�n��=1�����ݲ�ͬ�����ھ����е���Ҫ�̶�������
	 * 
	 * @param pos
	 *            ����
	 * @return
	 */
	public static double getWeightCoefficient(int pos) {
		double result = 1;
		 if(weightMap!=null){
			 Double dl=weightMap.get(pos);
			 if(dl!=null)
				 result=dl;
		 }
		return result;
	}

	public static int str2int(String str) {
		int result = 0;
		if (str != null) {
			char[] cs = str.toCharArray();
			if (cs.length <= 4) {
				for (int i = 0; i < cs.length; i++) {
					result += cs[i] << ((3 - i) * 8);
				}
			}
		}
		return result;
	}

	/**
	 * ��������ʾ�Ĵ���ת���ַ�����ʾ�ġ�����ֱ�Ӳ鿴����ʽ
	 * @param pos ����
	 * @return
	 */
	public static String int2str(int pos) {
		StringBuffer bs = new StringBuffer();
		char[] cs = new char[4];
		cs[0] = (char) (pos >> 24);
		cs[1] = (char) ((pos >> 16) & 0xFF);
		cs[2] = (char) ((pos >> 8) & 0xFF);
		cs[3] = (char) (pos & 0xFF);
		for (char c : cs)
			if (c > 0)
				bs.append(c);

		return bs.toString();
	}
	
	public static int old2new(int old){
		return old<<16;
	}
}
