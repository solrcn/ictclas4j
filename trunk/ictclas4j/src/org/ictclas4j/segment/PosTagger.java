package org.ictclas4j.segment;

import java.util.ArrayList;

import org.ictclas4j.bean.AdjoiningPos;
import org.ictclas4j.bean.DictLib;
import org.ictclas4j.bean.Dictionary;
import org.ictclas4j.bean.POSTag;
import org.ictclas4j.bean.Pos;
import org.ictclas4j.bean.PosContext;
import org.ictclas4j.bean.SegAtom;
import org.ictclas4j.bean.SegNode;
import org.ictclas4j.util.Utility;
import org.ictclas4j.util.Utility.TAG_TYPE;


/**
 * δ��¼�ʵĴ���
 * 
 * @author sinboy
 * @since 2007.5.17 updated
 * 
 */
public class PosTagger {
	private DictLib dictLib;

	private Dictionary coreDict;

	private Dictionary unknownDict;

	private PosContext context;

	private int pos;

	private TAG_TYPE tagType;

	String unknownFlags;

	public PosTagger(TAG_TYPE type, DictLib dictLib) {
		if (dictLib != null) {
			this.tagType = type;
			this.dictLib = dictLib;
			this.coreDict = dictLib.getCoreDict();

			switch (type) {
			case TT_PERSON:
				unknownFlags = "δ##��";
				pos = -POSTag.NOUN_PERSON;
				context = dictLib.getPersonContext();
				unknownDict = dictLib.getPersonUnknownDict();
				break;
			case TT_TRANS_PERSON:
				unknownFlags = "δ##��";
				pos = -POSTag.NOUN_PERSON;
				context = dictLib.getTransPersonContext();
				unknownDict = dictLib.getTransPersonUnknownDict();
				break;
			case TT_PLACE:
				unknownFlags = "δ##��";
				pos = -POSTag.NOUN_SPACE;
				context = dictLib.getPlaceContext();
				unknownDict = dictLib.getPlaceUnknownDict();
				break;
			default:
				pos = 0;
				context = dictLib.getLexContext();
				unknownDict = dictLib.getLexUnknownDict();
				break;
			}

		}
	}

	/**
	 * �Ӿ������ֵĽ���У��ҳ����������������������ʵ�δ��½��
	 * 
	 * @param segGraph
	 * @param coreDict
	 * @return
	 */
	public boolean recognise(SegGraph segGraph, ArrayList<SegNode> sns) {

		if (segGraph != null && sns != null && coreDict != null && unknownDict != null && context != null) {
			posTag(sns);
			getBestPos(sns);
			// DebugUtil.outputPostag(sns);
			switch (tagType) {
			case TT_PERSON:// Person recognition
				personRecognize(segGraph, sns);
				break;
			case TT_PLACE:// Place name recognition
			case TT_TRANS_PERSON:// Transliteration Person
				placeRecognize(segGraph, sns, coreDict);
				break;
			}
		}

		return true;
	}

	public boolean recognise(ArrayList<SegNode> sns) {

		if (sns != null && unknownDict != null && context != null) {
			posTag(sns);
			getBestPos(sns);
			// DebugUtil.outputPostag(sns);
			switch (tagType) {
			case TT_NORMAL:
				for (SegNode sn : sns) {
					if (sn.getPos() == 0) {
						sn.setPos(getBestTag(sn));
					}
				}
			}
		}

		return true;
	}

	/**
	 * �����еĴ��Խ��б��
	 * 
	 * @param frs
	 *            �����зֵĽ��
	 * @pararm startIndex ��ʼ���д��Ա�ǵ�λ��
	 * @param coreDict
	 *            ���Ĵʵ��
	 * @param unknownDict
	 *            δ��½�ʵ��
	 * @return ��һ����Ҫ��ʼ��λ��
	 */
	public void posTag(ArrayList<SegNode> sns) {

		if (sns != null && coreDict != null && unknownDict != null && context != null) {
			int i = 0;
			String curWord = null;

			for (; i < sns.size(); i++) {
				SegNode sn = sns.get(i);
				sn.setAllPos(null);
				curWord = sn.getSrcWord();
				int gbkID = sn.getGbkID();// dictLib.getGBKID(curWord);
				// if (tagType == Utility.TAG_TYPE.TT_NORMAL ||
				// !unknownDict.isExist(sn.getWord(), 44)) {
				//
				// }

				if (tagType != Utility.TAG_TYPE.TT_NORMAL) {

					// ��ȫ���ַ����ɰ�ǵ��ַ�
					if (tagType == Utility.TAG_TYPE.TT_TRANS_PERSON && i > 0) {
						String prevWord = sns.get(i - 1).getSrcWord();
						if (Utility.charType(prevWord) == Utility.CT_CHINESE) {
							if (".".equals(curWord))
								curWord = "��";
							else if ("-".equals(curWord))
								curWord = "��";
						}
					}

					if (sn.getPos() < 0) {
						AdjoiningPos pos = new AdjoiningPos( 0 , 0);
						sn.addPos(pos);
					} else {
						// ��unknownDict�ʵ���л�ȡ��ǰ�����д���
						SegAtom sa = unknownDict.getSegAtom(curWord, gbkID);
						for (int j = 0; sa != null && j < sa.getPosCount(); j++) {
							Pos pos = sa.getPos(j);
							double value = -Math.log((1 + pos.getFreq()));
							value += Math.log((context.getFreq(pos.getTag()) + sa.getPosCount() + 1));
							AdjoiningPos apos = new AdjoiningPos(pos , value);
							sn.addPos(apos);
						}

						if (Utility.SENTENCE_BEGIN.equals(curWord))
							sn.addPos(new AdjoiningPos( 100 , 0));

						else if (Utility.SENTENCE_END.equals(curWord))
							sn.addPos(new AdjoiningPos( 101 , 0));
						else {
							int freq = 0;
							sa = coreDict.getSegAtom(curWord, gbkID);
							if (sa != null) {
								double value = -Math.log((double) (1 + freq));
								value += Math.log((double) (context.getFreq(0) + sa.getPosCount()));
								sn.addPos(new AdjoiningPos( 0 , value));

							}
						}
					}
				} else {
					if (sn.getPos() > 0) {
						int tag = sn.getPos();
						double value = -Math.log(1 + sn.getFreq());
						value += Math.log(1 + context.getFreq(tag));
						if (value < 0)
							value = 0;
						sn.addPos(new AdjoiningPos( tag,  value));
					} else {
						if (sn.getPos() < 0) {
							sn.setPos(-sn.getPos());
							sn.addPos(new AdjoiningPos( -sn.getPos(),  sn.getFreq()));
						}
						SegAtom sa = coreDict.getSegAtom(curWord, gbkID);
						if (sa != null) {
							for (int j = 0; j < sa.getPosCount(); j++) {
								Pos pos = sa.getPos(j);
								double value = -Math.log(1 + pos.getFreq());
								value += Math.log(context.getFreq(pos.getTag()) + sa.getPosCount());
								sn.addPos(new AdjoiningPos(pos , value));
							}
						}
					}
				}

				if (sn.getAllPos() == null)
					guessPos(tagType, sn);

				// ���һ���ʽڵ��Ӧ��allPosΪnull����˵�����޷������ɴ�
				// ���Ĵ�������һ���ʵĴ���,���ǽ�����ʶ��ĩ##ĩ������
				if (i - 1 >= 0 && sns.get(i - 1).getPosSize() == -1) {
					if (sn.getPosSize() > 0) {
						Pos pos = sn.getAllPos().get(0).getPos();
						int ipos = pos.getTag() == POSTag.SEN_END ? POSTag.UNKNOWN : pos.getTag();
						AdjoiningPos apos = new AdjoiningPos( ipos , 0);
						sns.get(i - 1).addPos(apos);
					}
				}
			}

			// ���һ��������
			SegNode last = sns.get(i - 1);
			if (last != null) {
				SegNode sn = new SegNode();
				int tag = 0;
				if (tagType != Utility.TAG_TYPE.TT_NORMAL)
					tag = 101;
				else
					tag = 1;
				AdjoiningPos pos = new AdjoiningPos( tag, 0);
				sn.addPos(pos);
				sns.add(sn);
			}
		}
	}

	/**
	 * ȡ����һ���ʵ�N��������͵�ǰ�ʵĴ�����ƥ�����һ��
	 */
	private void getBestPos(ArrayList<SegNode> sns) {
		ArrayList<AdjoiningPos> prevAllPos = null;
		ArrayList<AdjoiningPos> allPos = null;
		if (sns != null && context != null) {
			for (int i = 0; i < sns.size(); i++) {
				if (i == 0) {
					int pos = tagType != Utility.TAG_TYPE.TT_NORMAL ? 100 : 0;
					prevAllPos = new ArrayList<AdjoiningPos>();
					prevAllPos.add(new AdjoiningPos(pos, 0));
				} else {
					prevAllPos = sns.get(i - 1).getAllPos();
				}
				allPos = sns.get(i).getAllPos();
				if (allPos != null)
					for (AdjoiningPos pos : allPos) {
						// �ҳ�ǰһ�����Ժ͵�ǰ�������п����ڽӵĴ���
						int bestPrev = 0;
						double minValue = 10000000;
						for (int k = 0; prevAllPos != null && k < prevAllPos.size(); k++) {
							AdjoiningPos prevPos = prevAllPos.get(k);
							double temp = context.computePossibility(prevPos.getPos().getTag(), pos.getPos().getTag());
							temp = -Math.log(temp) + prevPos.getValue();
							if (temp < minValue) {
								minValue = temp;
								bestPrev = k;
							}
						}

						pos.setPrev(bestPrev);
						pos.setValue(pos.getValue() + minValue);
					}
			}

			tagBest(sns);

			// for(SegNode sn:sns){
			// String word=sn.getSrcWord();
			// System.out.println(word+":");
			// for(AdjoiningPos ap:sn.getAllPos()){
			// System.out.println("
			// "+POSTag.int2str(ap.getPos())+","+ap.getValue()+","+ap.getPrev()+","+ap.isBest());
			// }
			// }
		}
	}

	// �²�ôʵĴ���
	private int guessPos(TAG_TYPE tagType, SegNode sn) {
		int result = -1;
		if (sn != null && context != null) {
			int charType;
			double freq = 0;

			String word = sn.getWord();
			if (word == null)
				return result;

			switch (tagType) {
			case TT_NORMAL:
				break;
			case TT_PERSON:
				if (word.indexOf("����") != -1) {
					freq = (double) 1 / (double) (context.getFreq(6) + 1);
					sn.addPos(new AdjoiningPos(6, freq));
				} else {
					freq = (double) 1 / (double) (context.getFreq(0) + 1);
					sn.addPos(new AdjoiningPos(0, freq));

					if (sn.getLen() >= 4) {
						freq = (double) 1 / (double) (context.getFreq(0) + 1);
						sn.addPos(new AdjoiningPos(0, freq));
						freq = (double) 1 / (double) (context.getFreq(11) * 8);
						sn.addPos(new AdjoiningPos(11, freq));
						freq = (double) 1 / (double) (context.getFreq(12) * 8);
						sn.addPos(new AdjoiningPos(12, freq));
						freq = (double) 1 / (double) (context.getFreq(13) * 8);
						sn.addPos(new AdjoiningPos(13, freq));
					} else if (sn.getLen() == 2) {
						freq = (double) 1 / (double) (context.getFreq(0) + 1);
						sn.addPos(new AdjoiningPos(0, freq));
						charType = Utility.charType(word);
						if (charType == Utility.CT_OTHER || charType == Utility.CT_CHINESE) {
							freq = (double) 1 / (double) (context.getFreq(1) + 1);
							sn.addPos(new AdjoiningPos(1, freq));
							freq = (double) 1 / (double) (context.getFreq(2) + 1);
							sn.addPos(new AdjoiningPos(2, freq));
							freq = (double) 1 / (double) (context.getFreq(3) + 1);
							sn.addPos(new AdjoiningPos(3, freq));
							freq = (double) 1 / (double) (context.getFreq(4) + 1);
							sn.addPos(new AdjoiningPos(4, freq));
						}
						freq = (double) 1 / (double) (context.getFreq(11) * 8);
						sn.addPos(new AdjoiningPos(11, freq));
						freq = (double) 1 / (double) (context.getFreq(12) * 8);
						sn.addPos(new AdjoiningPos(12, freq));
						freq = (double) 1 / (double) (context.getFreq(13) * 8);
						sn.addPos(new AdjoiningPos(13, freq));
					}
				}
				break;
			case TT_PLACE:
				freq = (double) 1 / (double) (context.getFreq(0) + 1);
				sn.addPos(new AdjoiningPos(0, freq));

				if (sn.getLen() >= 4) {
					freq = (double) 1 / (double) (context.getFreq(11) * 8);
					sn.addPos(new AdjoiningPos(11, freq));
					freq = (double) 1 / (double) (context.getFreq(12) * 8);
					sn.addPos(new AdjoiningPos(12, freq));
					freq = (double) 1 / (double) (context.getFreq(13) * 8);
					sn.addPos(new AdjoiningPos(13, freq));
				} else if (sn.getLen() == 2) {
					freq = (double) 1 / (double) (context.getFreq(0) + 1);
					sn.addPos(new AdjoiningPos(0, freq));
					charType = Utility.charType(word);
					if (charType == Utility.CT_OTHER || charType == Utility.CT_CHINESE) {

						freq = (double) 1 / (double) (context.getFreq(1) + 1);
						sn.addPos(new AdjoiningPos(1, freq));
						freq = (double) 1 / (double) (context.getFreq(2) + 1);
						sn.addPos(new AdjoiningPos(2, freq));
						freq = (double) 1 / (double) (context.getFreq(3) + 1);
						sn.addPos(new AdjoiningPos(3, freq));
						freq = (double) 1 / (double) (context.getFreq(4) + 1);
						sn.addPos(new AdjoiningPos(4, freq));
					}
					freq = (double) 1 / (double) (context.getFreq(11) * 8);
					sn.addPos(new AdjoiningPos(11, freq));
					freq = (double) 1 / (double) (context.getFreq(12) * 8);
					sn.addPos(new AdjoiningPos(12, freq));
					freq = (double) 1 / (double) (context.getFreq(13) * 8);
					sn.addPos(new AdjoiningPos(13, freq));
				}
				break;
			case TT_TRANS_PERSON:
				freq = (double) 1 / (double) (context.getFreq(0) + 1);
				sn.addPos(new AdjoiningPos(0, freq));
				if (!Utility.isAllChinese(word)) {
					if (Utility.isAllLetter(word)) {
						freq = (double) 1 / (double) (context.getFreq(1) + 1);
						sn.addPos(new AdjoiningPos(1, freq));
						freq = (double) 1 / (double) (context.getFreq(11) + 1);
						sn.addPos(new AdjoiningPos(11, freq));
						freq = (double) 1 / (double) (context.getFreq(2) * 2 + 1);
						sn.addPos(new AdjoiningPos(2, freq));
						freq = (double) 1 / (double) (context.getFreq(3) * 2 + 1);
						sn.addPos(new AdjoiningPos(3, freq));
						freq = (double) 1 / (double) (context.getFreq(12) * 2 + 1);
						sn.addPos(new AdjoiningPos(12, freq));
						freq = (double) 1 / (double) (context.getFreq(13) * 2 + 1);
						sn.addPos(new AdjoiningPos(13, freq));
					}
					freq = (double) 1 / (double) (context.getFreq(41) * 8);
					sn.addPos(new AdjoiningPos(41, freq));
					freq = (double) 1 / (double) (context.getFreq(42) * 8);
					sn.addPos(new AdjoiningPos(42, freq));
					freq = (double) 1 / (double) (context.getFreq(43) * 8);
					sn.addPos(new AdjoiningPos(43, freq));
				} else if (sn.getLen() >= 4) {
					freq = (double) 1 / (double) (context.getFreq(41) * 8);
					sn.addPos(new AdjoiningPos(41, freq));
					freq = (double) 1 / (double) (context.getFreq(42) * 8);
					sn.addPos(new AdjoiningPos(42, freq));
					freq = (double) 1 / (double) (context.getFreq(43) * 8);
					sn.addPos(new AdjoiningPos(43, freq));
				} else if (sn.getLen() == 2) {
					charType = Utility.charType(word);
					if (charType == Utility.CT_OTHER || charType == Utility.CT_CHINESE) {
						freq = (double) 1 / (double) (context.getFreq(1) * 2 + 1);
						sn.addPos(new AdjoiningPos(1, freq));
						freq = (double) 1 / (double) (context.getFreq(2) * 2 + 1);
						sn.addPos(new AdjoiningPos(2, freq));
						freq = (double) 1 / (double) (context.getFreq(3) * 2 + 1);
						sn.addPos(new AdjoiningPos(3, freq));
						freq = (double) 1 / (double) (context.getFreq(30) * 8 + 1);
						sn.addPos(new AdjoiningPos(30, freq));
						freq = (double) 1 / (double) (context.getFreq(11) * 4 + 1);
						sn.addPos(new AdjoiningPos(11, freq));
						freq = (double) 1 / (double) (context.getFreq(12) * 4 + 1);
						sn.addPos(new AdjoiningPos(12, freq));
						freq = (double) 1 / (double) (context.getFreq(13) * 4 + 1);
						sn.addPos(new AdjoiningPos(13, freq));
						freq = (double) 1 / (double) (context.getFreq(21) * 2 + 1);
						sn.addPos(new AdjoiningPos(21, freq));
						freq = (double) 1 / (double) (context.getFreq(22) * 2 + 1);
						sn.addPos(new AdjoiningPos(22, freq));
						freq = (double) 1 / (double) (context.getFreq(23) * 2 + 1);
						sn.addPos(new AdjoiningPos(23, freq));
					}
					freq = (double) 1 / (double) (context.getFreq(41) * 8);
					sn.addPos(new AdjoiningPos(41, freq));
					freq = (double) 1 / (double) (context.getFreq(42) * 8);
					sn.addPos(new AdjoiningPos(42, freq));
					freq = (double) 1 / (double) (context.getFreq(43) * 8);
					sn.addPos(new AdjoiningPos(43, freq));
				}
				break;
			default:
				break;
			}
			if (sn.getAllPos() != null)
				result = sn.getAllPos().size();
		}
		return result;
	}

	/**
	 * ����ģʽƥ��
	 * 
	 * <pre>
	 *           
	 *           BBCD 343 0.003606 
	 *           BBC 2 0.000021 
	 *           BBE 125 0.001314 
	 *           BBZ 30 0.000315 
	 *           BCD 62460 0.656624 
	 *           BEE 0 0.000000 
	 *           BE 13899 0.146116 
	 *           BG 869 0.009136 
	 *           BXD 4 0.000042 
	 *           BZ 3707 0.038971 
	 *           CD 8596 0.090367 
	 *           EE 26 0.000273 
	 *           FB 871 0.009157 
	 *           Y 3265 0.034324
	 *           XD 926 0.009735
	 *           
	 *           The person recognition patterns set
	 *           BBCD:��+��+��1+��2;
	 *           BBE: ��+��+����;
	 *           BBZ: ��+��+˫���ɴ�;
	 *           BCD: ��+��1+��2;
	 *           BE: ��+����;
	 *           BEE: ��+����+����;������
	 *           BG: ��+��׺
	 *           BXD: ��+��˫�����ֳɴ�+˫��ĩ��
	 *           BZ: ��+˫���ɴ�;
	 *           B: ��
	 *           CD: ��1+��2;
	 *           EE: ����+����;
	 *           FB: ǰ׺+��
	 *           XD: ��˫�����ֳɴ�+˫��ĩ��
	 *           Y: �յ����ɴ�
	 * </pre>
	 */
	private void personRecognize(SegGraph segGraph, ArrayList<SegNode> sns) {
		String sPos = null;
		String personName = null;
		// ����ʶ��ģʽ
		final String[] patterns = { "BBCD", "BBC", "BBE", "BBZ", "BCD", "BEE", "BE", "BG", "BXD", "BZ", "CDCD", "CD", "EE", "FB", "Y", "XD", "" };
		final double[] factor = { 0.003606, 0.000021, 0.001314, 0.000315, 0.656624, 0.000021, 0.146116, 0.009136, 0.000042, 0.038971, 0, 0.090367,
				0.000273, 0.009157, 0.034324, 0.009735, 0 };

		if (segGraph != null && sns != null) {
			int j = 1, k, nPos;
			boolean bMatched = false;

			sPos = word2pattern(sns);
			while (sPos != null && j < sPos.length()) {
				bMatched = false;
				for (k = 0; !bMatched && patterns[k].length() > 0; k++) {
					// �����ǰ�������з��ϸ�ģʽ���ִ������Ҹ��ִ�ǰ�󶼲���Բ�㣬����Ϊ��ƥ���
					if (sPos.substring(j).indexOf(patterns[k]) == 0 && !"��".equals(sns.get(j - 1).getWord())
							&& !"��".equals(sns.get(j + patterns[k].length()))) {// Find

						String temp = sPos.substring(j + 2);
						if (temp.length() > 1)
							temp = temp.substring(0, 1);

						// Rule 1 for exclusion:ǰ׺+��+��1(��2): ����(ǰ׺+��)ʧЧ��
						if ("FB".equals(patterns[k]) && ("E".equals(temp) || "C".equals(temp) || "G".equals(temp))) {
							continue;
						}

						nPos = j;
						personName = "";
						// Get the possible person name
						while (nPos < j + patterns[k].length()) {
							SegNode sn = sns.get(nPos);
							int gbkID = sn.getGbkID();// dictLib.getGBKID(sn.getSrcWord());
							if (sn.getPos() < 4 && unknownDict.getFreq(sn.getSrcWord(), sn.getPos(), gbkID) < Utility.LITTLE_FREQUENCY)
								personName += sn.getSrcWord();
							nPos += 1;
						}
						if ("CDCD".equals(patterns[k])) {
							if (GetForeignCharCount(personName) > 0)
								j += patterns[k].length() - 1;
							continue;
						}

						SegNode usn = new SegNode();
						usn.setRow(sns.get(j).getRow());
						usn.setCol(sns.get(j + patterns[k].length() - 1).getCol());
						usn.setWord(unknownFlags);
						usn.setSrcWord(personName);
						double value = -Math.log(factor[k]) + computePossibility(j, patterns[k].length(), sns);
						usn.setPos(pos);
						usn.setWeight(value);
						segGraph.insert(usn, true);

						j += patterns[k].length();
						bMatched = true;
					}
				}
				if (!bMatched)// Not matched, add j by 1
					j += 1;
			}

		}
	}

	// TODO:
	private int GetForeignCharCount(String personName) {
		return 0;
	}

	/**
	 * ����ģʽƥ��
	 * 
	 */
	private void placeRecognize(SegGraph segGraph, ArrayList<SegNode> sns, Dictionary coreDict) {
		if (segGraph != null && coreDict != null) {
			int start = 1;
			int end = 1;
			double dPanelty = 1;
			String srcWord = "";
			for (int i = 1; i < sns.size(); i++) {
				start = i;
				end = start;
				srcWord = sns.get(i).getSrcWord();
				if (getBestTag(sns, i) == 1) {
					for (end = i + 1; end < sns.size(); end++) {
						int bestTag = getBestTag(sns, end);
						if (bestTag == -1)
							continue;
						else if (bestTag == 1 || bestTag == 3) {
							if (end > i + 1)
								dPanelty += 1;
							srcWord += sns.get(end).getSrcWord();
						} else if (bestTag == 2)
							srcWord += sns.get(end).getSrcWord();
						else
							break;
					}

				} else if (getBestTag(sns, i) == 2) {
					dPanelty += 1;
					for (end = i + 1; end < sns.size(); end++) {
						int bestTag = getBestTag(sns, end);
						if (bestTag == -1)
							continue;
						else if (bestTag == 3) {
							if (end > i + 1)
								dPanelty += 1;
							srcWord += sns.get(end).getSrcWord();
						} else if (bestTag == 2)
							srcWord += sns.get(end).getSrcWord();
						else
							break;
					}
				}
				if (end > start) {
					SegNode newsn = new SegNode();
					newsn.setRow(sns.get(start).getRow());
					newsn.setCol(sns.get(end - 1).getCol());
					newsn.setPos(pos);
					newsn.setWord(unknownFlags);
					newsn.setSrcWord(srcWord);
					double value = computePossibility(start, end - start + 1, sns);
					newsn.setWeight(value);
					segGraph.insert(newsn, true);
				}
			}
		}
	}

	private int getBestTag(ArrayList<SegNode> sns, int index) {
		if (sns != null && index >= 0 && index < sns.size()) {
			SegNode sn = sns.get(index);
			return getBestTag(sn);

		}

		return -1;
	}

	private int getBestTag(SegNode sn) {
		if (sn != null) {
			ArrayList<AdjoiningPos> allPos = sn.getAllPos();
			if (allPos != null) {
				for (AdjoiningPos pos : allPos) {
					if (pos.isBest())
						return pos.getPos().getTag();
				}
			}
		}

		return -1;
	}

	// Judge whether the name is a given name
	public boolean isGivenName(String sName) {
		String firstChar;
		String secondChar;
		// given Name Possibility
		double gnp = 0;
		// singleNamePossibility
		double snp = 0;

		if (sName != null) {
			if (sName.getBytes().length != 4)
				return false;

			firstChar = sName.substring(0, 1);
			int gbkID1 = dictLib.getGBKID(firstChar);
			secondChar = sName.substring(1);
			int gbkID2 = dictLib.getGBKID(secondChar);

			// The possibility of P(Wi|Ti)
			gnp += Math.log((double) unknownDict.getFreq(firstChar, 2, gbkID1) + 1.0);
			gnp -= Math.log(context.getFreq(2) + 1.0);
			gnp += Math.log((double) unknownDict.getFreq(secondChar, 3, gbkID2) + 1.0);
			gnp -= Math.log(context.getFreq(3) + 1.0);
			// The possibility of conversion from 2 to 3
			gnp += Math.log(context.computePossibility(2, 3) + 1.0);
			gnp -= Math.log(context.getFreq(2) + 1.0);

			// The possibility of P(Wi|Ti)
			snp += Math.log((double) unknownDict.getFreq(firstChar, 1, gbkID1) + 1.0);
			snp -= Math.log(context.getFreq(1) + 1.0);
			snp += Math.log((double) unknownDict.getFreq(secondChar, 4, gbkID2) + 1.0);
			snp -= Math.log(context.getFreq(4) + 1.0);
			// The possibility of conversion from 1 to 4
			snp += Math.log(context.computePossibility(1, 4) + 1.0);
			snp -= Math.log(context.getFreq(1) + 1.0);

			// ����||m_dict.getFrequency(sFirstChar,1)/m_dict.getFrequency(sFirstChar,2)>=10
			// The possibility being a single given name is more than being a
			// 2-char given name
			if (snp >= gnp)
				return false;
			return true;
		}

		return false;
	}

	// �Ѿ������ηִʺ��������ʽת�������ַ���ģʽ
	private String word2pattern(ArrayList<SegNode> sns) {
		String result = null;

		if (sns != null) {
			result = "";
			for (SegNode sn : sns) {
				result += (char) (getBestTag(sn) + 'A');
			}

		}
		return result;
	}

	/**
	 * ��ǳ���Ѵ���
	 * 
	 * @param sns
	 */
	private void tagBest(ArrayList<SegNode> sns) {

		if (sns != null) {
			int size = sns.size();

			// �����ǿ�ʼ�ͽ������
			for (int i = size - 1, j = 0; i >= 0; i--) {
				SegNode sn = sns.get(i);
				ArrayList<AdjoiningPos> allPos = sn.getAllPos();
				if (allPos != null && allPos.size() > j) {
					AdjoiningPos pos = allPos.get(j);
					pos.setBest(true);
					j = pos.getPrev();
				} else if (i + 1 < size - 1) {
					int tag = getBestTag(sns.get(i + 1));
					AdjoiningPos pos = new AdjoiningPos(tag, 0);
					pos.setBest(true);
					sns.get(i).addPos(pos);
				}
				// ����ô�����ĸ���֣��������û��������ҵʵ�������������øôʵĴ���
				if (sn.getPos() == POSTag.NOUN_LETTER || sn.getPos() == POSTag.NUM) {
					for (AdjoiningPos pos : allPos) {
						if (pos.isBest() && pos.getPos().getTag() > 0) {
							sn.setPos(pos.getPos().getTag());
							break;
						}
					}
				}
			}
			// �ѽ�����ȥ�����õ�����Ŀ�Ľ�����Ϊ�˵õ����һ����ĩ����ĩ���ʵ����Ŵ���

			if (size > 1) {
				if (sns.get(size - 1).getWord() == null)
					sns.remove(size - 1);
			}
		}
	}

	private double computePossibility(int startPos, int length, ArrayList<SegNode> sns) {
		double retValue = 0, posPoss;

		if (sns != null && unknownDict != null && context != null) {
			for (int i = startPos; sns != null && i < startPos + length && i < sns.size(); i++) {
				SegNode sn = sns.get(i);
				int bestTag = getBestTag(sn);
				if (bestTag != -1) {
					int gbkID = sn.getGbkID();// dictLib.getGBKID(sn.getSrcWord());
					int freq = unknownDict.getFreq(sn.getSrcWord(), bestTag, gbkID);
					posPoss = Math.log((double) (context.getFreq(sn.getPos()) + 1));
					posPoss += -Math.log((double) (freq + 1));
					retValue += posPoss;
				}
			}
		}
		return retValue;
	}

	public Dictionary getUnknownDict() {
		return unknownDict;
	}

}
