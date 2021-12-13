package com.a225.model.vo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.ImageIcon;

import com.a225.model.loader.ElementLoader;
import com.a225.model.manager.ElementManager;
import com.a225.model.manager.GameMap;
import com.a225.model.manager.MoveTypeEnum;
import com.a225.util.Utils;


/**
 * �����
 * @ClassName: Player  
 * @Description: ���VO��   
 * @author: WeiXiao
 * @CreateDate: 2019��4��11�� ����5:10:20
 */
public class Player extends Character{
	private ImageIcon img;
	private int moveX;
	private int moveY;
	private boolean attack;//��¼����״̬��Ĭ��Ϊfalse
	private boolean keepAttack;//��¼�Ƿ�Ϊһֱ���Ź�������ʵ��һ�ΰ���ֻ��һ��ˮ��
	private int playerNum;//��¼�ڼ�����ң�0Ϊ���һ��1Ϊ��Ҷ�
	
	//���캯��
	public Player(int x, int y, int w, int h, ImageIcon img, int playerNum) {
		super(x, y, w, h);
		this.img = img;
		this.playerNum = playerNum;
		moveX = 0;
		moveY = 0;
		attack = false;
		keepAttack = false;
		dead = false;
	}
	
	public static Player createPlayer(List<String> list,int playerNum) {
		//list = [PlayerA,x,y,w,h]
		int x = Integer.parseInt(list.get(1));
		int y = Integer.parseInt(list.get(2));
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//��ȡ��Դ��������ͼƬ�ֵ�
		return new Player(x, y, w, h, imageMap.get(list.get(0)),playerNum);
	}
	
	public static Player createPlayer(List<String> data,int i,int j,int playerNum) {
		int x = j*MapSquare.PIXEL_X+GameMap.getBiasX();
		int y = i*MapSquare.PIXEL_Y+GameMap.getBiasY();
		int w = MapSquare.PIXEL_X;
		int h = MapSquare.PIXEL_Y;
		Map<String, ImageIcon> imageMap = 
				ElementLoader.getElementLoader().getImageMap();//��ȡ��Դ��������ͼƬ�ֵ�
		return new Player(x, y, w, h, imageMap.get(data.get(0)),playerNum);
	}

	//չʾ����ͼƬ
	@Override
	public void showElement(Graphics g) {
		if(isShowing==false) return;
		g.drawImage(img.getImage(), 
				getX(), getY(), 	//��Ļ���Ͻ�����
				getX()+getW(), getY()+getH(), 	//��Ļ��������
				(moveX/6)*100+27, moveY*100+43, 			//ͼƬ��������
				(moveX/6)*100+72, moveY*100+99, 			//ͼƬ��������
				null);
		
		//��ʾ����
		String string = "Player" + Integer.toString(playerNum+1)+": " + Integer.toString(score);
		g.setColor(new Color(255, 153, 0));
		g.setFont(new Font("Times New Roman", Font.BOLD, 24));
		g.drawString(string, 0,(playerNum+1)*MapSquare.PIXEL_Y);
	}

	//�ƶ�
	@Override
	public void move() {
		int tx = getX();
		int ty = getY();
		
		switch(moveType) {
		case TOP: ty-=speed;break;
		case LEFT: tx-=speed;break;
		case RIGHT: tx+=speed;break;
		case DOWN: ty+=speed;break;
		case STOP:
		default:
			break;
		}
		
		boolean det1 = crashDetection(tx, ty, ElementManager.getManager().getElementList("obstacle"));
		boolean det2 = crashDetection(tx, ty, ElementManager.getManager().getElementList("fragility"));
		boolean det3 = bubbleCrashDetection(tx, ty, ElementManager.getManager().getElementList("bubble"));
		
		if(det1&&det2&&det3) {
			setX(tx);
			setY(ty);			
		}
	}
	
	/**
	 * �����δ��ըը������ײ
	 * @param tx ��ʱx
	 * @param ty ��ʱy
	 * @param list ը��list
	 * @return û����ײ
	 */
	private boolean bubbleCrashDetection(int tx, int ty, List<SuperElement> list) {
		for(SuperElement se:list) {
			switch(moveType) {
			case TOP: 
			case DOWN:
				if(Utils.between(getBottomBound(), se.getTopBound(), se.getBottomBound())
						||Utils.between(getTopBound(), se.getTopBound(), se.getBottomBound())
						||(getBottomBound()==se.getBottomBound()&&getTopBound()==se.getTopBound())) {
					return true;
				}
				break;
			case LEFT:
			case RIGHT:
				if(Utils.between(getLeftBound(), se.getLeftBound(), se.getRightBound())
						||Utils.between(getRightBound(), se.getLeftBound(), se.getRightBound())
						||(getLeftBound()==se.getLeftBound()&&getRightBound()==se.getRightBound())) {
					return true;
				}
				break;
			default:
				break;
			}
		}
		return crashDetection(tx, ty, list);
	}
	
	/**
	 * ��ײ���+ƽ���ƶ�
	 * @param tx
	 * @param ty
	 * @param list
	 * @return û����ײ
	 */
	private boolean crashDetection(int tx, int ty, List<SuperElement> list){
		int bias = 1;//�ж���ײƫ��ֵ
		int THRESHOLD = 25;//ƽ���ƶ���ֵ
		Rectangle playerRect = new Rectangle(tx, ty, getW(), getH());
		Random random = new Random();
		GameMap gameMap = ElementManager.getManager().getGameMap();
		
		for(SuperElement se:list) {
			Rectangle elementRect = new Rectangle(se.getX()+bias, se.getY()+bias, se.getW()-bias, se.getH()-bias);
			if(playerRect.intersects(elementRect)) {//�����ײ
				switch(moveType) {//�жϷ���
				case TOP:
				case DOWN:
					int width=Math.min(getX()+getW(),se.getX()+se.getW())-Math.max(getX(), se.getX());
					if(width>THRESHOLD) break;//������ֵ����ƽ������
					
					if(getX()<se.getX()) {//��������
						if(moveType==MoveTypeEnum.TOP&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), getTopBound()-10))) break;
						else if(moveType==MoveTypeEnum.DOWN&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound(), getBottomBound()+10))) break;
						for(int i=0;i<width;i++) {
							if(random.nextBoolean())
								setX(getX()-1);
						}
					} else {
						if(moveType==MoveTypeEnum.TOP&&!gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), getTopBound()-10))) break;
						else if(moveType==MoveTypeEnum.DOWN&&!gameMap.blockIsWalkable(GameMap.getIJ(getRightBound(), getBottomBound()+10))) break;
						for(int i=0;i<width;i++) {
							if(random.nextBoolean())
								setX(getX()+1);
						}
					}
					break;
				case LEFT:
				case RIGHT:
					int height=Math.min(getY()+getH(),se.getY()+se.getH())-Math.max(getY(), se.getY());
					if(height>THRESHOLD) break;
					
					if(getY()<se.getY()) {//���������
						if(moveType==MoveTypeEnum.LEFT&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound()-10, getTopBound()))) break;
						else if(moveType==MoveTypeEnum.RIGHT&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound()+10, getTopBound()))) break;
						for(int i=0;i<height;i++) {
							if(random.nextBoolean())
								setY(getY()-1);
						}
					} else {
						if(moveType==MoveTypeEnum.LEFT&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound()-10, getBottomBound()))) break;
						else if(moveType==MoveTypeEnum.RIGHT&&!gameMap.blockIsWalkable(GameMap.getIJ(getLeftBound()+10, getBottomBound()))) break;
						for(int i=0;i<height;i++) {
							if(random.nextBoolean())
								setY(getY()+1);
						}
					}
					break;
				default:break;
				}
				return false;
			}
		}
		return true;
	}
	
	
	
	//��д����ģ��
	@Override
	public void update() {
		if(!dead) {
			move();
			addBubble();
			updateImage();
			destroy();
		}
	}
	
	//����ͼƬ
	public void updateImage() {
		if(moveType==MoveTypeEnum.STOP){
			return;
		}
		
		if(++moveX>=24)
			moveX = 0;
		
		switch (moveType) {
		case TOP:moveY = 3;break;
		case LEFT:moveY = 1;break;
		case RIGHT:moveY = 2;break;
		case DOWN:moveY = 0;break;
		default:break;
		}
	}
	
	//�������
	public void addBubble() {
		List<Integer> loc = GameMap.getXY(GameMap.getIJ(getX()+getW()/2, getY()+getH()/2));
		GameMap gameMap = ElementManager.getManager().getGameMap();
		List<Integer> maplist = GameMap.getIJ(loc.get(0), loc.get(1));
		if(attack && !dead && bubbleNum<bubbleLargest &&  //�ж��Ƿ�Ϊ����״̬����ǰ��ը����С������ֵ����ǰλ��û��ը��
				gameMap.getBlockSquareType(maplist.get(0), maplist.get(1))!=GameMap.SquareType.BUBBLE) {

			List<SuperElement> list = 
					ElementManager.getManager().getElementList("bubble");
			list.add(Bubble.createBubble(loc.get(0), loc.get(1), ElementLoader.getElementLoader().getGameInfoMap().get("bubble"),playerNum,getBubblePower()));
			attack = false;
			bubbleNum++;
		}
	}

	@Override
	public void destroy() {}
	
	
	//gettes and setters
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

	public int getMoveY() {
		return moveY;
	}

	public void setMoveY(int moveY) {
		this.moveY = moveY;
	}

	public boolean isAttack() {
		return attack;
	}

	public void setAttack(boolean attack) {
		this.attack = attack;
	}

	public boolean isKeepAttack() {
		return keepAttack;
	}

	public void setKeepAttack(boolean keepAttack) {
		this.keepAttack = keepAttack;
	}
	
	public int getPlayerNum() {
		return this.playerNum;
	}

	@Override
	public boolean isDead() {
		return dead;
	}

	@Override
	public void setDead(boolean dead) {
		this.dead = dead;
	}


}
