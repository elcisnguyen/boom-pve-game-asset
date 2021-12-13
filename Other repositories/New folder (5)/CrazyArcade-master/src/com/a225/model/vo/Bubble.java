package com.a225.model.vo;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;
import com.a225.model.manager.GameMap;

/**
 * ˮ��ը����
 * @author ���
 *
 */
public class Bubble extends SuperElement{
	
	private ImageIcon img;
	private int moveX;
	private int playerNum;//��ʾ��Ӧ��ҵ�ը����0Ϊ���һ��1Ϊ��Ҷ���2ΪnpcA��3ΪnpcB��4ΪnpcC
	private int imgW;
	private int imgH;
	private int power;

	//���캯��
	public Bubble(int x, int y, int w, int h, ImageIcon img, int imgW, int imgH, int playerNum,int power) {
		super(x, y, w, h);
		this.img = img;
		this.playerNum = playerNum;
		this.imgW = imgW;
		this.imgH = imgH;
		this.power = power;
		moveX = 0;
		//��ͼ��Ӧλ������Ϊ�ϰ������ͨ��
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> maplist = GameMap.getIJ(x, y);
		gameMap.setBlockSquareType(maplist.get(0), maplist.get(1), GameMap.SquareType.BUBBLE);
	}
	
	//��������
	public static Bubble createBubble(int x, int y,List<String> list,int playerNum,int power) {
		//list=[Bubble,w,h]
		int imgW = Integer.parseInt(list.get(1));
		int imgH = Integer.parseInt(list.get(2));
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//��ȡ��Դ��������ͼƬ�ֵ�
		return new Bubble(x, y, w, h, imageMap.get(list.get(0)), imgW, imgH ,playerNum, power);
	}

	@Override
	public void showElement(Graphics g) {
		g.drawImage(img.getImage(), 
				getX(), getY(), 	//��Ļ���Ͻ�����
				getX()+getW(), getY()+getH(), 	//��Ļ��������
				(moveX/8)*imgW, 0, 				//ͼƬ��������
				(moveX/8+1)*imgW, imgH, 			//ͼƬ��������
				null);
	}
	
	//��д����ģ��
	@Override
	public void update() {
		super.update();
		updateImage();
	}

	//����ͼƬ
	public void updateImage() {
		if(++moveX>=32)
			moveX = 0;
	}
	
	//ʹ�ü�ʱ����2.5��ı�Alive״̬
	@Override
	public void move() {
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				setAlive(false);
			}
		};
		timer.schedule(task, 2500);
	}
	

	@Override
	public void destroy() {
		if(!isAlive()) {	
			List<SuperElement> explodeList = 
					ElementManager.getManager().getElementList("explode");
			
			//�ı�ը������Ѿ�����ը����bubbleNum,��ʾ��ըЧ��������ExplodeBubble
			if(playerNum<2) {
				List<SuperElement> list2 = ElementManager.getManager().getElementList("player");
				Player player = (Player) list2.get(playerNum);
				player.setBubbleNum(player.getBubbleNum()-1);
				explodeList.add(BubbleExplode.createExplode(getX(), getY(), power,playerNum));
			}
			else {
				List<SuperElement> list2 = ElementManager.getManager().getElementList("npc");
				Npc npc = (Npc) list2.get(playerNum-2);
				npc.setBubbleNum(npc.getBubbleNum()-1);
				explodeList.add(BubbleExplode.createExplode(getX(), getY(), power,playerNum));
			}
			
			//����ͼλ����Ϊfloor
			GameMap gameMap = ElementManager.getManager().getGameMap();
			List<Integer> maplist = GameMap.getIJ(getX(), getY());
			gameMap.setBlockSquareType(maplist.get(0), maplist.get(1), GameMap.SquareType.FLOOR);
			
			
		}
	}

	//getters and setters
	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	public int getMoveX() {
		return moveX;
	}

	public void setMoveX(int moveX) {
		this.moveX = moveX;
	}
	
	public int getPlayerNum() {
		return this.playerNum;
	}

	public int getPower() {
		return power;
	}
}
