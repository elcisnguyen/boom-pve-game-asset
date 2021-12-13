package com.a225.model.manager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a225.model.loader.ElementLoader;
import com.a225.model.vo.SuperElement;
import com.a225.util.Utils;

/**
 * Ԫ�ع�����
 * ����ģʽ
 * @author Jenson
 */
public class ElementManager {
	//Ԫ�ع���������
	private static ElementManager elementManager;
	static {
		elementManager = new ElementManager();
	}
	
	//Ԫ�ص�Map����
	private Map<String, List<SuperElement>> map;
	
	//ͼ��˳��map
	private Map<String,Integer> priorityMap;
	
	//��Ϸ��ͼ
	private GameMap gameMap;
	
	
	//��ʼ������
	protected void init() {
		Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
		List<String> windowSize = gameInfoMap.get("windowSize");
		gameMap = new GameMap(Integer.parseInt(windowSize.get(0)),Integer.parseInt(windowSize.get(1)));
		map = new HashMap<>();
		priorityMap = new HashMap<>();
	}
	
	//��ʼ��Ԫ���б��ֵ�
	private void initMap() {
		map.put("player", new ArrayList<SuperElement>());//���
		map.put("bubble", new ArrayList<SuperElement>());//ˮ��
		map.put("explode",new ArrayList<SuperElement>());//ˮ�ݱ�ը
		map.put("fragility", new ArrayList<SuperElement>());//���ƻ�����
		map.put("floor", new ArrayList<SuperElement>());//�ذ�
		map.put("obstacle", new ArrayList<SuperElement>());//�����ƻ�����
		map.put("magicBox", new ArrayList<SuperElement>());//����
		map.put("npc", new ArrayList<SuperElement>());
	}
	
	//��ʼ��ͼ�����ȼ��ֵ�
	private void initPriorityMap() {
		priorityMap.put("player", 50);
		priorityMap.put("npc", 45);
		priorityMap.put("bubble", 10);
		priorityMap.put("obstacle", 40);
		priorityMap.put("explode", 30);
		priorityMap.put("magicBox", 25);
		priorityMap.put("fragility", 20);
		priorityMap.put("bubble", 10);
		priorityMap.put("floor", -10);
	}
	
	//���캯��
	private ElementManager() {
		init();//��ʼ������
		initMap();//��ʼ��Ԫ���б��ֵ�
		initPriorityMap();//��ʼ��ͼ�����ȼ��ֵ�
	}
	
	
	//ͼ�����ȼ��Ƚ���
	public Comparator<String> getMapKeyComparator() {
		return new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				int p1 = priorityMap.get(o1);
				int p2 = priorityMap.get(o2);
				if(p1 > p2) {
					return 1;
				} else if(p1 < p2) {
					return -1;
				} else {
					return 0;
				}
			}
		};
	}
	
	//���map����
	public Map<String, List<SuperElement>> getMap(){
		return map;
	}
	
	//�õ�Ԫ��list
	public List<SuperElement> getElementList(String key){
		return map.get(key);
	}
	
	//Ԫ�ع��������
	public static ElementManager getManager() {
		return elementManager;
	}
	
	//��ȡ��Ϸ��ͼ��
	public GameMap getGameMap() {
		return gameMap;
	}
	
	public void loadMap(){
		int mapNum = Integer.parseInt(ElementLoader.getElementLoader().getGameInfoMap().get("mapNum").get(0));
		gameMap.createMap("stage"+(Utils.random.nextInt(mapNum)+1)+"Map");
	}

	public void overGame(Boolean over) {
		if(over) {
			gameMap.clearMapALL();
			//ʧ�ܶ���
		} else {
			gameMap.clearMapOther();
			//��ϲ����
		}
	}


}

