package com.whh.frame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import com.whh.model.manager.ElementManager;
import com.whh.model.vo.SuperElement;

public class MyJPanel extends JPanel implements Runnable{
	public void paint(Graphics g) {
		
		super.paint(g);
//		this.setBackground(Color.black);
//		��һ���ж�ֵ  Ҳ����ʹ��ö��
//		1.ǰ����
//		2.gameRuntime
		gameRunTime(g);//Graphics ����
//		3.�νӶ���
		
	}

	private void gameRunTime(Graphics g) {
//		List<SuperElement> list=
//				ElementManager.getManager().getElementList("XX");
		Map<String,List<SuperElement>> map=
				ElementManager.getManager().getMap();
		Set<String> set=map.keySet();
		int playerlayer=1;
		int player2layer=1;
		if(!map.get("player").isEmpty()) {
			playerlayer = map.get("player").get(0).getLayer();
		}
		if(!map.get("playerB").isEmpty()) {
			player2layer = map.get("playerB").get(0).getLayer();
		}
//		List<String> list=new ArrayList<>();
//		list.addAll(set);
//		Collections.sort(list);//��Ȼ˳��
//		Collections.sort(list,"�Ƚ�������");//�Զ���˳��
//		֪ʶ�㣺java���϶����������� ��2���ӿ��й�
		for(String key:set){
			if(key.indexOf("player")==0||key.indexOf("items")==0) {
				
			}
			else if(key.indexOf("map1")==0) {
				List<SuperElement> list=map.get(key);
				for(int i=0;i<list.size();i++){
					list.get(i).showElement(g);
				}
				
				if(!map.get(key).isEmpty()) {
					List<SuperElement> list1=map.get("items"+map.get(key).get(0).getLayer());
					for(int i=0;i<list1.size();i++){
						list1.get(i).showElement(g);
					}
					
					if(map.get(key).get(0).getLayer()==playerlayer&&map.get(key).get(0).getLayer()==player2layer) {
						if(map.get("player").get(0).getY()>map.get("playerB").get(0).getY()) {
							List<SuperElement> list2=map.get("playerB");
							for(int i=0;i<list2.size();i++){
								list2.get(i).showElement(g);
							}
							list2=map.get("player");
							for(int i=0;i<list2.size();i++){
								list2.get(i).showElement(g);
							}
						}
						else {
							List<SuperElement> list2=map.get("player");
							for(int i=0;i<list2.size();i++){
								list2.get(i).showElement(g);
							}
							list2=map.get("playerB");
							for(int i=0;i<list2.size();i++){
								list2.get(i).showElement(g);
							}
						}
					}
					else {
						if(map.get(key).get(0).getLayer()==playerlayer) {
							List<SuperElement> list2=map.get("player");
							for(int i=0;i<list2.size();i++){
								list2.get(i).showElement(g);
							}
						}
						if(map.get(key).get(0).getLayer()==player2layer) {
							List<SuperElement> list2=map.get("playerB");
							for(int i=0;i<list2.size();i++){
								list2.get(i).showElement(g);
							}
						}
					}
				}
			}
			else {
				List<SuperElement> list=map.get(key);
				for(int i=0;i<list.size();i++){
					list.get(i).showElement(g);
				}
			}
		}
	}
	
	/**
	 * ʲô����д��
	 * 1.���м̳й�ϵ�� ������֮����﷨����(��̬��һ��ʵ��)
	 * 2.��д�ķ�������� ����ķ�����ǩ��һ��(����ֵ���������ƣ���������)
	 * 3.��д�ķ����������η�ֻ���Աȸ���ĸ��ӿ��ţ������Աȸ�����ӷ��
	 * 4.��д�ķ����׳��쳣�����Ա� ����ĸ��ӿ���
	 */
	@Override
	public void run(){
		while(true){//��ѭ��:����᲻ֹͣ��ˢ��
//			�̵߳�����
			try {
				Thread.sleep(100);//����
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.repaint();//Ҫ�� ����ٴ�ˢ��
		}
	}
}
