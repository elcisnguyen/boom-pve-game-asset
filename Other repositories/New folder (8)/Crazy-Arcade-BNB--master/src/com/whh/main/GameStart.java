package com.whh.main;

import com.whh.frame.MyJFrame;
import com.whh.frame.MyJPanel;
import com.whh.thread.GameListener;

public class GameStart {
//	������Ϸ����ڣ�����
	public static void main(String[] args) {
//		��Դ����
//		������أ��Զ���������
		MyJFrame jf=new MyJFrame();
		MyJPanel jp=new MyJPanel();
		GameListener listener=new GameListener();
		jf.setKeyListener(listener);
		jf.setJp(jp);//ע��
//		��������
		jf.addListener();
		jf.addJPanels();//����jp
//		��Ϸ��������ʼ��
		jf.start();
	}
	
	/**
	 * 1.����һ�� VO�࣬�̳�superElement
	 * 2.�ڹ�������ʵ����
	 * 3.�����ļ��н�������
	 * 4.�����Ҫ���������ڼ�����д����
	 */
}
