package com.whh.model.vo;

import java.awt.Graphics;
import java.util.List;

import javax.lang.model.element.Element;
import javax.swing.ImageIcon;

import com.whh.model.manager.ElementManager;

public class Bubble extends SuperElement{
	private ImageIcon img;//���ݵ�����
	private int power;
	private String name;//�ĸ�����ŵ�����
	private int boomX;
	private int boomY;
	private int moveX;
	private int time;
	private int i=0;
	private int ct;//���ݶ�̬�仯�ļ��ʱ�䣨/100ms��
	private float boomtime;
	private int flag;
	private static int rx;
	private static int ry;
	private int[] b;
	private boolean toppass;
	private boolean downpass;
	private boolean rightpass;
	private boolean leftpass;
	private List<Integer> bubblelist;
//	private ImageIcon img0,img1,img2,img3,img4,img5,img6,img7,img8;
	
	public Bubble() {}
	
	public Bubble(int x, int y, int w, int h,ImageIcon img,String name,int power) {
		super(x, y, w, h);
		this.img=img;
		this.power=power;
		this.name=name;
		ct=3;
	}
	
	public static Bubble createBubble(int x,int y,String url,String name,int power) {
		ImageIcon img=new ImageIcon(url);
		x=realx(x);//����Ӧ�÷ŵ���λ��
		y=realy(y);
		ElementManager.getManager().getBubblelist().set(x/40+y/40*15, 1);//bubblelist�е���Ӧ���Ӹ�Ϊ�����ݵ�����
		ElementManager.getManager().getMap().get("map1"+(y/40+1)).get(x/40).setAcrossPower(false);
		return new Bubble(x,y,40,40,img,name,power);//��������Ϊpower��
	}
	
	public void showElement(Graphics g) {
		toppass=true;
		downpass=true;
		rightpass=true;
		leftpass=true;
		bubblelist=ElementManager.getManager().getBubblelist();
		boomtime=8/power;
		if(time<=33&&bubblelist.get(getX()/40+getY()/40*15)==1) {//����δ��������������ʱ
			g.drawImage(img.getImage(), 
					getX(), getY(),
					getX()+getW(), getY()+getH(),    //��Ļ���½�����
					73*moveX+14, 21,    
					73*moveX+58, 62, 
					null);
		}
		else if(time<=33&&bubblelist.get(getX()/40+getY()/40*15)!=1) {
			for(;i<=power;i++) {
				if(i==0) {
					g.drawImage((new ImageIcon("img/bubble/pp0.png")).getImage(), getX(), getY(), getBW(), getBH(), null);
					bubblelist.set(getX()/40+getY()/40*15, 3);
				}
				else if(time>=boomtime*i) {
					if(getY()/40-i>=0&&bubblelist.get(getX()/40+(getY()/40-i)*15)==-1) {//�ø����Ƿ��Ѿ�������ը�������ݻٵ����ӣ�
						toppass=false;
						if(time==37)
							bubblelist.set(getX()/40+(getY()/40-i)*15,0);
					}
					if(toppass&&!PK(getX()/40,getY()/40-i)) {//���������û������
							if(i!=power)
								g.drawImage((new ImageIcon("img/bubble/pp5.png")).getImage(), getX(), getY()-i*getBH(), getBW(), getBH(), null);//���ϵ�pp
							else
								g.drawImage((new ImageIcon("img/bubble/pp3.png")).getImage(), getX(), getY()-i*getBH(), getBW(), getBH(), null);//���ϵ�pp
							bubblelist.set(getX()/40+(getY()/40-i)*15, 2);
							if(bubblelist.get(getX()/40+(getY()/40-i)*15)==1)
								bubblelist.set(getX()/40+(getY()/40-i)*15, -1);					
					}
					else {
						toppass=false;
					}
					if(getY()/40+i<13&&bubblelist.get(getX()/40+(getY()/40+i)*15)==-1) {//�ø����Ƿ��Ѿ�������ը�������ݻٵ����ӣ�
						downpass=false;
						g.drawImage((new ImageIcon("img/bubble/bubbleend.png")).getImage(), getX(), getY()+i*getBH(), getX()+getBW(), getY()+i*getBH()+getBH(), 40*(time-34), 0, 40*(time-33), 40, null);
						if(time==37)
							bubblelist.set(getX()/40+(getY()/40+i)*15,0);
					}
					if(downpass&&!PK(getX()/40,getY()/40+i)) {//���������
							if(i!=power)
								g.drawImage((new ImageIcon("img/bubble/pp6.png")).getImage(), getX(), getY()+i*getBH(), getBW(), getBH(), null);//���µ�pp
							else
								g.drawImage((new ImageIcon("img/bubble/pp4.png")).getImage(), getX(), getY()+i*getBH(), getBW(), getBH(), null);//���µ�pp
							bubblelist.set(getX()/40+(getY()/40+i)*15, 2);
						
					}
					else {
						downpass=false;
						g.drawImage((new ImageIcon("img/bubble/bubbleend.png")).getImage(), getX(), getY()+i*getBH(), getX()+getBW(), getY()+i*getBH()+getBH(), 40*(time-34), 0, 40*(time-33), 40, null);
					}
					if((getX()/40+i)<195&&bubblelist.get(getX()/40+i+getY()/40*15)==-1) {//�ø����Ƿ��Ѿ�������ը�������ݻٵ����ӣ�
						rightpass=false;
						g.drawImage((new ImageIcon("img/bubble/bubbleend.png")).getImage(),  getX()+i*getBW(), getY(),  getX()+i*getBW()+getBW(), getY()+getBH(), 40*(time-34), 0, 40*(time-33), 40, null);
						if(time==37)
							bubblelist.set(getX()/40+i+getY()/40*15,0);
					}
					if(rightpass&&!PK(getX()/40+i,getY()/40)) {//�ұ�
							if(i!=power)
								g.drawImage((new ImageIcon("img/bubble/pp7.png")).getImage(), getX()+i*getBW(), getY(), getBW(), getBH(), null);//���ҵ�pp
							else
								g.drawImage((new ImageIcon("img/bubble/pp1.png")).getImage(), getX()+i*getBW(), getY(), getBW(), getBH(), null);//���ҵ�pp
							bubblelist.set(getX()/40+i+getY()/40*15, 2);
						
					}
					else {
						rightpass=false;
					}
					if((getX()/40-i)>=0&&bubblelist.get(getX()/40-i+getY()/40*15)==-1) {//�ø����Ƿ��Ѿ�������ը�������ݻٵ����ӣ�
						leftpass=false;
						g.drawImage((new ImageIcon("img/bubble/bubbleend.png")).getImage(), getX()-i*getBW(), getY(),getX()-i*getBW()+getBW(), getY()+getBH(), 40*(time-34), 0, 40*(time-33), 40, null);
						if(time==37)
							bubblelist.set(getX()/40-i+getY()/40*15,0);
					}
					if(leftpass&&!PK(getX()/40-i,getY()/40)) {//���
							if(i!=power)
								g.drawImage((new ImageIcon("img/bubble/pp8.png")).getImage(), getX()-i*getBW(), getY(), getBW(), getBH(), null);//�����pp
							else
								g.drawImage((new ImageIcon("img/bubble/pp2.png")).getImage(), getX()-i*getBW(), getY(), getBW(), getBH(), null);//�����pp
							bubblelist.set(getX()/40-i+getY()/40*15, 2);
						
					}
					else {
						leftpass=false;
					}
				}
			}
			i=0;
			time=34;
		}
		else if((time-33)<6){//��ʱ�䱬ը�Ļ���
			if((time-33)<5) {
				for(;i<=power;i++) {
					if(i==0) {
						g.drawImage((new ImageIcon("img/bubble/pp0.png")).getImage(), getX(), getY(), getBW(), getBH(), null);
						bubblelist.set(getX()/40+getY()/40*15, 3);
					}
					else if(time>=boomtime*i) {
						if(toppass&&!PK(getX()/40,getY()/40-i)) {//���������û�����Ӻ�����ʱ
								if(i!=power)
									g.drawImage((new ImageIcon("img/bubble/pp5.png")).getImage(), getX(), getY()-i*getBH(), getBW(), getBH(), null);//���ϵ�pp
								else
									g.drawImage((new ImageIcon("img/bubble/pp3.png")).getImage(), getX(), getY()-i*getBH(), getBW(), getBH(), null);//���ϵ�pp
								bubblelist.set(getX()/40+(getY()/40-i)*15, 2);
								if(bubblelist.get(getX()/40+(getY()/40-i)*15)==1)
									bubblelist.set(getX()/40+(getY()/40-i)*15, -1);					
						}
						else if(toppass) {
							g.drawImage((new ImageIcon("img/bubble/bubbleend.png")).getImage(), getX(), getY()-i*getBH(), getX()+getBW(), getY()-i*getBH()+getBH(), 40*(time-34), 0, 40*(time-33), 40, null);
							toppass=false;
						}
						else {
							toppass=false;
						}
						if(downpass&&!PK(getX()/40,getY()/40+i)) {//���������
								if(i!=power)
									g.drawImage((new ImageIcon("img/bubble/pp6.png")).getImage(), getX(), getY()+i*getBH(), getBW(), getBH(), null);//���µ�pp
								else
									g.drawImage((new ImageIcon("img/bubble/pp4.png")).getImage(), getX(), getY()+i*getBH(), getBW(), getBH(), null);//���µ�pp
								bubblelist.set(getX()/40+(getY()/40+i)*15, 2);
							
						}
						else if(downpass) {
							g.drawImage((new ImageIcon("img/bubble/bubbleend.png")).getImage(), getX(), getY()+i*getBH(), getX()+getBW(), getY()+i*getBH()+getBH(), 40*(time-34), 0, 40*(time-33), 40, null);
							downpass=false;
						}
						else {
							downpass=false;
						}
						if(rightpass&&!PK(getX()/40+i,getY()/40)) {//�ұ�
								if(i!=power)
									g.drawImage((new ImageIcon("img/bubble/pp7.png")).getImage(), getX()+i*getBW(), getY(), getBW(), getBH(), null);//���ҵ�pp
								else
									g.drawImage((new ImageIcon("img/bubble/pp1.png")).getImage(), getX()+i*getBW(), getY(), getBW(), getBH(), null);//���ҵ�pp
								bubblelist.set(getX()/40+i+getY()/40*15, 2);
							
						}
						else if(rightpass) {
							g.drawImage((new ImageIcon("img/bubble/bubbleend.png")).getImage(), getX()+i*getBW(), getY(), getX()+i*getBW()+getBW(), getY()+getBH(), 40*(time-34), 0, 40*(time-33), 40, null);
							rightpass=false;
						}
						else {
							rightpass=false;
						}
						if(leftpass&&!PK(getX()/40-i,getY()/40)) {//���
								if(i!=power)
									g.drawImage((new ImageIcon("img/bubble/pp8.png")).getImage(), getX()-i*getBW(), getY(), getBW(), getBH(), null);//�����pp
								else
									g.drawImage((new ImageIcon("img/bubble/pp2.png")).getImage(), getX()-i*getBW(), getY(), getBW(), getBH(), null);//�����pp
								bubblelist.set(getX()/40-i+getY()/40*15, 2);
							
						}
						else if(leftpass) {
							leftpass=false;
							g.drawImage((new ImageIcon("img/bubble/bubbleend.png")).getImage(), getX()-i*getBW(), getY(), getX()-i*getBW()+getBW(), getY()+getBH(), 40*(time-34), 0, 40*(time-33), 40, null);
						}
						else {
							leftpass=false;
						}
					}
				}
				i=0;
			}
			else {
				for(int i=0;i<=power;i++) {
					if(i==0) {
						bubblelist.set(getX()/40+getY()/40*15,0);
					}
					else {
						if(getY()/40-i>=0)
							bubblelist.set(getX()/40+(getY()/40-i)*15, 0);	
						if(getY()/40+i<13)
							bubblelist.set(getX()/40+(getY()/40+i)*15, 0);
						if(getX()/40+i<195)
							bubblelist.set(getX()/40+i+getY()/40*15, 0);
						if(getX()/40-i>=0)
							bubblelist.set(getX()/40-i+getY()/40*15, 0);
					}
				}
			}
		}
		else {
			ElementManager.getManager().getMap().get("map1"+(getY()/40+1)).get(getX()/40).setAcrossPower(true);
			this.setVisible(false);
			if(name.equals("oneplayer")) {
				Player player=(Player)ElementManager.getManager().getMap().get("player").get(0);
				player.setAddbubnum(player.getAddbubnum()-1);
			}
			else if(name.equals("twoplayer")) {
				PlayerB playerB=(PlayerB)ElementManager.getManager().getMap().get("playerB").get(0);
				playerB.setAddbubnum(playerB.getAddbubnum()-1);
			}
		}
	}
	
	private static int realx(int x) {//���ݵ�ͼ�ĸ������޸����ݵ�λ��
		x=x/40*40;
		return x;
	}
	
	private static int realy(int y) {
		y=y/40*40;
		return y;
	}
	
	public boolean PK(int r,int i) {//i:�ڼ���(i+1��ʾ��map1�е�����)��r���ڼ���
		i+=1;
		bubblelist=ElementManager.getManager().getBubblelist();
		if(ElementManager.getManager().getMap().get("map1"+i).get(r).getBurstPower()==1) {//������
			ElementManager.getManager().getMap().get("map1"+i).get(r).setVisible(false);
			bubblelist.set(r+(i-1)*15, -1);
			if(time==37) {
				bubblelist.set(r+(i-1)*15, 0);
			}
			return true;
		}
		else if(ElementManager.getManager().getMap().get("map1"+i).get(r).getBurstPower()==0) {//����
			bubblelist.set(r+(i-1)*15, -1);
			return true;
		}
		else if(bubblelist.get(r+(i-1)*15)==3||bubblelist.get(r+(i-1)*15)==1||bubblelist.get(r+(i-1)*15)==-1) {
			bubblelist.set(r+(i-1)*15, -1);
			return true;
		}
		return false;//������
	}
	
	
	public void update() {
		updateImage();
		time++;
	}
	
	private void updateImage() {//����δ��ըʱ�Ķ�̬
		if(time<=40) {
			if(moveX==0&&(time/ct)%4==1) {
				moveX=1;
			}
			else if(moveX==1&&flag==0&&(time/ct)%4==2) {
				moveX=2;
				flag=1;
			}
			else if(moveX==2&&(time/ct)%4==3) {
				moveX=1;
			}
			else if(moveX==1&&(time/ct)%4==0){
				moveX=0;
				flag=0;
			}
		}
	}

	public int getPower() {
		return power;
	}
	
	public int getBW() {
		return 40;
	}
	public int getBH() {
		return 40;
	}

	
	@Override
	public void move() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	
}
