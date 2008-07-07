package org.ictclas4j.run;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.ictclas4j.bean.DictLib;

/**
 * ������������������ 1.�ִʴ������� 2.������������ 3.����ɾ������ 4.���������ѯ����
 * 
 * @author sinboy
 * 
 */
public class ServerAdapter implements Runnable {

	private DictLib dictLib;

	static Logger logger = Logger.getLogger(ServerAdapter.class);

	public ServerAdapter(DictLib dictLib) {
		this.dictLib = dictLib;  
		manager();
	} 

	public void run() {
		try {
			Socket client = null;
			int listenPort = Config.listen_port();
			ServerSocket ss = new ServerSocket(listenPort);
			logger.info("����" + listenPort + "�˿ڣ��ȴ��ͻ��˵�����...");
			while (true) {
				client = ss.accept();
				logger.info("���յ�������" + client.toString());
				SegmentService service = new SegmentService(dictLib, client);
				service.start();
			}
		} catch (IOException e) {
			logger.error("�޷��������Կͻ��˵�����", e);
		}
	}

	// ÿ�����ϰ�ҹʱ���Զ�����
	private void manager() {
		Timer timer = new Timer();
		TimerTask tt2 = new TimerTask() {
			public void run() {
				loadNewWords();
			}
		};

		Date date = Config.update_keys_time();
		timer.schedule(tt2, date, Config.update_keys_interval() * 60000);

	}

	private void loadNewWords() {
		//TODO
//		String newBiWord = "data" + GFFinal.FILE_SEP + "new_bi_word.txt";
//		NewWordsUtil.loadNewBiWords(dictLib, newBiWord);
//		String newWord = "data" + GFFinal.FILE_SEP + "new_word.txt";
//		NewWordsUtil.loadNewWords(dictLib, newWord);
	}

}
