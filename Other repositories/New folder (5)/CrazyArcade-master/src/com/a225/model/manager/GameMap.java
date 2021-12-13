package com.a225.model.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.a225.main.GameController;
import com.a225.model.loader.ElementLoader;
import com.a225.model.vo.MagicBox;
import com.a225.model.vo.MapFloor;
import com.a225.model.vo.MapFragility;
import com.a225.model.vo.MapObstacle;
import com.a225.model.vo.MapSquare;
import com.a225.model.vo.Npc;
import com.a225.model.vo.Player;
import com.a225.model.vo.SuperElement;


/**
 * ��ͼ��
 * @ClassName: Map  
 * @Description: ��ͼ��   
 * @author: DaXiao
 * @CreateDate: 2019��4��11�� ����21��11
 */
public class GameMap {
	private int windowW;
	private int windowH;
	private static int mapRows;
	private static int mapCols;
	private static int biasX;
	private static int biasY;
	
	private static List<List<String>> mapList;//��ͼ
	
	//�Զ��巽�����Ͷ�Ӧö����
	public enum SquareType{
		OBSTACLE('0'),FLOOR('1'),FRAGILITY('2'),ITEM('3'),PLAYER_1('6'),PLAYER_2('7'),NPC('8'),BUBBLE('9');
		
		private char value = 0;
		
		private SquareType(char value) {
			this.value = value;
		}
		
		public static SquareType valueOf(char c) {    //��д�Ĵ�int��enum��ת������  
	        switch (c) {  
	        case '0':  return OBSTACLE;	//�ϰ���
	        case '1':  return FLOOR;	//�ذ�
	        case '2':  return FRAGILITY;//���ƻ���  
	        case '3':  return ITEM;  	//����
	        case '6':  return PLAYER_1;	//���1
	        case '7':  return PLAYER_2;	//���2
	        case '8':  return NPC;		//NPC
	        case '9':  return BUBBLE;  	//ը��
	        default:  
	            return null;  
	        }  
	    }  
	  
	    public char value() {  
	        return this.value;  
	    }  
	}
	
	//���캯��
	public GameMap(int windowW,int windowH) {
		this.windowW = windowW;
		this.windowH = windowH;
	}
	
	//�����ذ�
	private void createFloor() {
		Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
		List<SuperElement> floorList = ElementManager.getManager().getElementList("floor");
		String type = null;
		//�ӵ�ͼ�����ļ��еõ�����һ�ֵذ�
		for(int i=0;i<mapRows;i++) {
			for(int j=0;j<mapCols;j++) {
				if(mapList.get(i).get(j).equals("11")){
					type = "11";
					break;
				}
				if(mapList.get(i).get(j).equals("12")){
					type = "12";
					break;
				}
			}
		}
		//����ڵذ幹�캯��������һ������
		for(int i=0;i<mapRows;i++) {
			for(int j=0;j<mapCols;j++) {
				floorList.add(MapFloor.createMapFloor(typeMap.get(type),i, j));	
			}
		}
		
	}
	
	//������ͼԪ��
	private void createSquare() {
		Map<String, List<String>> typeMap = ElementLoader.getElementLoader().getSquareTypeMap();
		Map<String, List<SuperElement>>elmenteMap = ElementManager.getManager().getMap();
		Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
		int npcNum = 0;
		for (int i = 0; i < mapRows; i++) {
			for (int j = 0; j < mapCols; j++) {
				String type = mapList.get(i).get(j);
				switch (type.charAt(0)) {
				case '0':
					if(type.equals("00")) break;//����ǽ
					elmenteMap.get("obstacle").add(MapObstacle.createMapObstacle(typeMap.get(type), i, j));
					break;
				case '2': 
					elmenteMap.get("fragility").add(MapFragility.createMapFragility(typeMap.get(type), i, j));
					break;
				case '3':
					elmenteMap.get("magicBox").add(MagicBox.createMagicBox(i, j));
					break;
				case '6':
					initPlayer(i, j, 0);
					break;
				case '7':
					if(GameController.isTwoPlayer())
						initPlayer(i, j, 1);
					else {
						switch (type.charAt(1)) {
						case '1':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcA"), i, j, npcNum++));break;
						case '2':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcB"), i, j, npcNum++));break;
						case '3':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcC"), i, j, npcNum++));break;
						default:break;
						}
					}
					break;
				case '8':
					switch (type.charAt(1)) {
					case '1':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcA"), i, j, npcNum++));break;
					case '2':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcB"), i, j, npcNum++));break;
					case '3':elmenteMap.get("npc").add(Npc.createNpc(gameInfoMap.get("npcC"), i, j, npcNum++));break;
					default:break;
					}
					
					break;
				
				default:
					break;
				}
			}
		}
		GameController.setNpcNum(npcNum);
	}
	
	public void createMap(String pro){
		try {
			mapList = ElementLoader.getElementLoader().readMapPro(pro);
			List<String> size = ElementLoader.getElementLoader().getGameInfoMap().get("mapSize");
			mapRows = Integer.parseInt(size.get(0));
			mapCols = Integer.parseInt(size.get(1));
			biasX = (windowW-MapSquare.PIXEL_X*mapCols)/2;
			biasY = (windowH-MapSquare.PIXEL_Y*mapRows)/2;
			createFloor();
			createSquare();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����ͼ���ؽ�ɫ
	 * @param i 
	 * @param j
	 * @param num ��ţ����1��0�����2��1
	 */
	private void initPlayer(int i, int j, int num) {
		List<SuperElement> playerList = ElementManager.getManager().getMap().get("player");
		if(playerList.size()==(GameController.isTwoPlayer()?2:1)) {
			List<Integer> locList = GameMap.getXY(i,j);
			playerList.get(num).setX(locList.get(0));
			playerList.get(num).setY(locList.get(1));
		} else {
			Map<String, List<String>> gameInfoMap = ElementLoader.getElementLoader().getGameInfoMap();
			for(SuperElement se:playerList) {
				Player player = (Player) se;
				if(player.getPlayerNum()==num) {
					return;
				}
			}
			Player player = null;
			if(num==0) {
				player = Player.createPlayer(gameInfoMap.get("playerOne"), i, j, num);				
			} else if(num==1) {
				player = Player.createPlayer(gameInfoMap.get("playerTwo"), i, j, num);
			} else {
				return;
			}
			playerList.add(num, player);				
		}
	}
	
	/**
	 * ��ȡ��ͼij��ķ�������
	 * @param i
	 * @param j
	 * @return ��������
	 */
	public SquareType getBlockSquareType(int i,int j) {
		String str = mapList.get(i).get(j);
		return SquareType.valueOf(str.charAt(0));
	}
	
	/**
	 * ��ȡ��ͼij��ķ�������
	 * @param list ij�б�
	 * @return ��������
	 */
	public SquareType getBlockSquareType(List<Integer> list) {
		String str = mapList.get(list.get(0)).get(list.get(1));
		return SquareType.valueOf(str.charAt(0));
	}
	
	/**
	 * ���õ�ͼij�㷽������
	 * @param list ij�б�
	 * @param type
	 */
	public void setBlockSquareType(List<Integer> list,SquareType type) {
		mapList.get(list.get(0)).set(list.get(1), type.value+"");
	}
	
	/**
	 * ���õ�ͼij�㷽������
	 * @param i
	 * @param j
	 * @param type
	 */
	public void setBlockSquareType(int i,int j,SquareType type) {
		mapList.get(i).set(j, type.value+"");
	}
	
	/**
	 * �жϷ����Ƿ����ϰ���
	 * @param i
	 * @param j
	 * @return �Ƿ����ϰ���
	 */
	public boolean blockIsObstacle(int i,int j) {
		if(outOfBoundary(i, j)) return true;
		
		String type = mapList.get(i).get(j);
		if(type.charAt(0) == SquareType.OBSTACLE.value) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ��ȡijλ���Ƿ��ͨ��
	 * @param list
	 * @return ��ͨ��
	 */
	public boolean blockIsWalkable(List<Integer> list) {
		String type = mapList.get(list.get(0)).get(list.get(1));
		if(type.charAt(0) == SquareType.OBSTACLE.value
				||type.charAt(0) == SquareType.FRAGILITY.value
				||type.charAt(0) == SquareType.ITEM.value) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * ��ȡijλ���Ƿ��ͨ��
	 * @param list
	 * @return ��ͨ��
	 */
	public boolean blockIsWalkable(int i,int j) {
		String type = mapList.get(i).get(j);
		if(type.charAt(0) == SquareType.OBSTACLE.value
				||type.charAt(0) == SquareType.FRAGILITY.value
				||type.charAt(0) == SquareType.ITEM.value) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * �ж��Ƿ񳬳��߽�
	 * @param list ij�б�
	 * @return �Ƿ񳬳��߽�
	 */
	public boolean outOfBoundary(List<Integer> list) {
		int i = list.get(0);
		int j = list.get(1);
		if (i<0||i>=mapRows||j<0||j>=mapCols) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * �ж��Ƿ񳬳��߽�
	 * @param i
	 * @param j
	 * @return �Ƿ񳬳��߽�
	 */
	public boolean outOfBoundary(int i,int j) {
		if (i<0||i>=mapRows||j<0||j>=mapCols) {
			return true;
		} else {
			return false;
		}
	}
	
	
	//��xyת��Ϊij 0��i 1��j
	public static List<Integer> getIJ(int x,int y){
		List<Integer> list = new ArrayList<>();
		list.add((y-biasY)/MapSquare.PIXEL_Y);
		list.add((x-biasX)/MapSquare.PIXEL_X);
		return list;
	}
	
	//��ijת��Ϊxy 0��x 1��y
	public static List<Integer> getXY(int i,int j){
		List<Integer> tempList = new ArrayList<>();
		tempList.add(i*MapSquare.PIXEL_Y+biasY);
		tempList.add(j*MapSquare.PIXEL_X+biasX);
		return tempList;
	}
	public static List<Integer> getXY(List<Integer> list){
		List<Integer> tempList = new ArrayList<>();
		tempList.add(list.get(1)*MapSquare.PIXEL_X+biasX);
		tempList.add(list.get(0)*MapSquare.PIXEL_Y+biasY);
		return tempList;
	}
	/**
	 * ��յ�ͼ�г��������Ķ���
	 */
	public void clearMapOther() {
		ElementManager.getManager().getElementList("obstacle").clear();
		ElementManager.getManager().getElementList("fragility").clear();
		ElementManager.getManager().getElementList("floor").clear();
		ElementManager.getManager().getElementList("explode").clear();
		ElementManager.getManager().getElementList("magicBox").clear();
		ElementManager.getManager().getElementList("npc").clear();
		ElementManager.getManager().getElementList("bubble").clear();
	}
	
	/**
	 * ��յ�ͼ���ж���
	 */
	public void clearMapALL() {
		ElementManager.getManager().getElementList("player").clear();
		clearMapOther();
	}

	public static List<List<String>> getMapList(){
		return mapList; 
	}
	public static int getBiasX() {
		return biasX;
	}
	public static int getBiasY() {
		return biasY;
	}
	public static int getMapRows() {
		return mapRows;
	}
	public static int getMapCols() {
		return mapCols;
	}
	
	
	
}
