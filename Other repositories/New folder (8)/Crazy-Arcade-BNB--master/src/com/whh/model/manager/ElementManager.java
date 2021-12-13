package com.whh.model.manager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.whh.model.load.ElementLoad;
import com.whh.model.manager.ElementFactory;
import com.whh.model.manager.ElementManager;
import com.whh.model.vo.MapSquare;
import com.whh.model.vo.SuperElement;

public class ElementManager {
//	����  NPCԪ�أ�����Ԫ�أ���������
	Map<String,List<SuperElement>> map;
	List<Integer> bubblelist;//ȫ��ͼ����list,0��ʾ�����ݣ�1�������ݣ�2�����ݱ�ը�ģ�-1�Ǳ�ը����
//	private MoveType moveType;
	
	//	��ʼ��
	protected void init(){
		bubblelist=new ArrayList<>();
		for(int i=0;i<195;i++) {
			bubblelist.add(0); 
		}
		map=new LinkedHashMap<>();
		List<SuperElement> list=new ArrayList<>();
		map.put("bgmap1", list);
		List<SuperElement> list1=new ArrayList<>();
		map.put("player", list1);
		List<SuperElement> listA=new ArrayList<>();
		map.put("playerB", listA);
		List<SuperElement> list2=new ArrayList<>();
		map.put("nbubble", list2);
		for(int i=1;i<=13;i++) {
			List<SuperElement> list3=new ArrayList<>();
			map.put("map1"+i, list3);
		}
		for(int i=1;i<=13;i++) {
			List<SuperElement> list4=new ArrayList<>();
			map.put("items"+i, list4);
		}

	}
//	�õ�һ�������� map����
	public Map<String, List<SuperElement>> getMap() {
		return map;
	}
//	�õ�һ��Ԫ�صļ���
	public List<SuperElement> getElementList(String key){
		return map.get(key);
	}
//	�õ�ȫ��ͼ������list
	public List<Integer> getBubblelist() {
		return bubblelist;
	}
	
//	��������Ҫһ��Ψһ������
	private static ElementManager elementManager;
//	���췽��˽�л�����ֻ���ڱ����п��� new
	private ElementManager(){
		init();
	}
	static{//��̬������ ��������ص�ʱ��ͻ�ִ��
		if(elementManager ==null){
			elementManager=new ElementManager();
		}
	}
//	�ṩ���������ⲿ���ʵ�Ψһ���   synchronized �̱߳�����
	public static /*synchronized*/ ElementManager getManager(){
//		if(elementManager ==null){
//			elementManager=new ElementManager();
//		}
		return elementManager;
	}
//	������Ҫ����Դ
	public void load(int g) {
		ElementLoad.getElementLoad().readImgPro();
		ElementLoad.getElementLoad().readMapPro();
		ElementLoad.getElementLoad().readImgPro2();
		ElementLoad.getElementLoad().readPlayPro();
		ElementLoad.getElementLoad().readtwoPlayPro();
		ElementLoad.getElementLoad().readImgPro3();
//		ElementLoad.getElementLoad().readGamepro();
//		����һ�� ״̬�����������ǰ��Ĺ�����Ϣ
//		......
		
		Set<String> set=map.keySet();
		for(String key:set){//�������ڱ����Ĺ����У��������ڵ�Ԫ�ز����� ���ӻ���ɾ��
			map.get(key).clear();
		}
		
		Map<String, List<String>> map1=
				ElementLoad.getElementLoad().getPlaymap();
		List<String> list1=map1.get("firstMapBG");
		String s1=list1.get(list1.size()-1);
		String[] arr1=s1.split(",");
		for(int i=0;i<arr1.length;i++) {
			map.get("bgmap1").add(MapSquare.createMapSquare((i%15)*40, (i/15)*40, arr1[i]));
		}
		
		map.get("player").add(ElementFactory.elementFactory("onePlayer"));
		map.get("playerB").add(ElementFactory.elementFactory("twoPlayer"));
		
		List<String> list2=map1.get("Map"+g);
		String s2=list2.get(list2.size()-1);
		String[] arr2=s2.split(",");
		for(int i=0;i<13;i++) {
			for(int j=0;j<15;j++) {
				int k=i+1;
				map.get("map1"+k).add(MapSquare.createMapSquare(j*40, i*40, arr2[i*15+j]));
			}
		
		}
	}

}
