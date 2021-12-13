package com.a225.model.vo;

import java.awt.Graphics;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.a225.model.manager.ElementManager;
import com.a225.model.manager.MoveTypeEnum;

/**
 * ��ɫ��
 * @ClassName: Character  
 * @Description:Ϊ��Һ͵��Եĸ��࣬�����������Թ��е����Ժͷ���    
 * @author: WeiXiao
 * @CreateDate: 2019��4��18�� ����5:27:20
 */
public class Character extends SuperElement{
	public final static int INIT_SPEED = 4; //��ʼ�ƶ��ٶ�
	
	protected boolean dead;//��¼�Ƿ���
	protected MoveTypeEnum moveType;
	protected int speed;//�ƶ��ٶ�
	protected int speedItemCount;//��Ч�еļ��ٿ�����
	protected int changeDirectionCount;//��Ч�еķ���ı俨����
	protected int stopitemCount;//��Ч�е��������ֹͣ������
	protected int bubblePower;//�ڵ�����
	protected int bubbleNum;//��¼����Ѿ����˶��ٸ�ը��
	protected int bubbleLargest;//��������ԷŶ��ٸ�ը������ʼֵΪ3
	public int score;
	protected int heathPoint;//�������ֵ
	protected boolean isUnstoppable;//����Ƿ����޵�
	protected int unstoppableCount;//�޵п�����
	protected boolean isShowing;//�Ƿ�ҪչʾԪ��

	public Character(int x, int y, int w, int h) {
		super(x, y, w, h);
		moveType = MoveTypeEnum.STOP;
		speedItemCount = 0;
		changeDirectionCount=0;
		stopitemCount=0;
		bubblePower = 1;
		bubbleNum = 0;
		bubbleLargest = 1;
		heathPoint = 1;
		isUnstoppable = false;
		unstoppableCount = 0;
		isShowing = true;
		speed = INIT_SPEED;
		score = 0;
		dead = false;
	}
	
	public void setHealthPoint(int change) {
		if(change<0)
		{
			if(isUnstoppable)return;
			setUnstoppable(3);//����ֵ����ʱ���޵�һ��ʱ��
		}
		heathPoint += change;
//		����ֵΪ0������
		if(heathPoint<=0) 
		{
			setDead(true);
			setX(-100);
			setY(-100);
		}
	}
	


	//	�ı�һ��ʱ����ƶ��ٶ�,�����ٶ���Ҫ�����ı����ͳ�����ʱ�䣨�룩
	public void changeSpeed(double times,int lastTime) {
		speed = (int)(speed*times);
		Timer timer = new Timer(true);
		speedItemCount++;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				speedItemCount--;
				if(speedItemCount==0) {
					speed = INIT_SPEED;					
				}
			}
		};
		timer.schedule(task,lastTime*1000);
	}

//	�ı�һ��ʱ��ķ��򣬷����෴
	public void changeDirection(int lastTime) {
		speed = -speed;
		Timer timer = new Timer(true);
		changeDirectionCount++;
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				changeDirectionCount--;
				if(changeDirectionCount==0) {
					speed = INIT_SPEED;		

				}
			}
		};
		timer.schedule(task,lastTime*1000);
	}
	
//	ʹ������Ҿ�ֹһ��ʱ��
	public void setOtherStop(int lastTime) {
//		�����Լ���������
		List<SuperElement> playerList = ElementManager.getManager().getElementList("player");
		for (int i = 0; i < playerList.size(); i++) {
			Player player = (Player) playerList.get(i);
			if(player!=this)
			{
				player.setSpeed(0);
				setSpeedToInital(lastTime,player);
			}
		}
		List<SuperElement> NPCList = ElementManager.getManager().getElementList("npc");
		for (int i = 0; i < NPCList.size(); i++) {
			Npc npc = (Npc) NPCList.get(i);
			if(npc!=this)
			{
				npc.setSpeed(0);
				setSpeedToInital(lastTime,npc);
			}
			
		}
	}
	
//	һ��ʱ���ָ���ʼ�ٶ�
//	lastTime ����ʱ�䣬character �������õõ���Ч������
	public void setSpeedToInital(int lastTime,final Character character)
	{
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				character.stopitemCount = character.stopitemCount-1;
				if(speedItemCount==0) {
					character.speed = INIT_SPEED;					
				}
			}
		};
		timer.schedule(task,lastTime*1000);
	}
	
//	�޵�һ��ʱ�䣬���ᱻը��
//	lastTime ����ʱ��
	public void setUnstoppable(int lastTime)
	{
		isUnstoppable = true;
		unstoppableCount++;
		unstoppableChangeImg(lastTime);
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				unstoppableCount--;
				if(unstoppableCount==0) isUnstoppable = false;	
			}
		};
		timer.schedule(task,lastTime*1000);
	}
//	ͼƬ��˸
	public void unstoppableChangeImg(final int lastTime) {
		Timer timer = new Timer();
        final int times = lastTime*1000/100;//����
        TimerTask task1 = new TimerTask() {// ͼƬ��ʧ
        	int count = 0;
            @Override
			public void run() {
                isShowing = false; 
                count++;
                if(count == times/5) {
                	isShowing = true;//����Ϊ������ʾ
                	this.cancel();
                }
            }
        };
        TimerTask task2 =  new TimerTask() {//ͼƬ����
        	int count = 0;
            @Override
			public void run() {
                isShowing = true;
                count++;
                if(count == times) this.cancel();
            }
        };
        timer.scheduleAtFixedRate(task1, 0, 500);//0�ӳ٣�ÿ500ms����һ��
	    timer.scheduleAtFixedRate(task2, 0, 100);//0�ӳ٣�ÿ100ms����һ��
	}
	
	public void bubbleAddPower() {
		bubblePower++;
	}
	@Override
	public void showElement(Graphics g) {}

	@Override
	public void move() {}

	@Override
	public void destroy() {}
	

	
	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public MoveTypeEnum getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveTypeEnum moveType) {
		this.moveType = moveType;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeedItemCount() {
		return speedItemCount;
	}

	public void setSpeedItemCount(int speedItemCount) {
		this.speedItemCount = speedItemCount;
	}

	public int getBubblePower() {
		return bubblePower;
	}

	public void setBubblePower(int bubblePower) {
		this.bubblePower = bubblePower;
	}

	public int getBubbleNum() {
		return bubbleNum;
	}

	public void setBubbleNum(int bubbleNum) {
		this.bubbleNum = bubbleNum;
	}

	public int getBubbleLargest() {
		return bubbleLargest;
	}

	public void setBubbleLargest(int bubbleLargest) {
		this.bubbleLargest = bubbleLargest;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	

	public int getChangeDirectionCount() {
		return changeDirectionCount;
	}

	public void setChangeDirectionCount(int changeDirectionCount) {
		this.changeDirectionCount = changeDirectionCount;
	}

	public int getStopitemCount() {
		return stopitemCount;
	}

	public void setStopitemCount(int stopitemCount) {
		this.stopitemCount = stopitemCount;
	}

	public int getHeathPoint() {
		return heathPoint;
	}

	public boolean isisUnstoppable() {
		return isUnstoppable;
	}

	public void setisUnstoppable(boolean unstoppable) {
		this.isUnstoppable = unstoppable;
	}
}
