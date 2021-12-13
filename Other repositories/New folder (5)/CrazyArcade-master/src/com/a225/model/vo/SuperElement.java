package com.a225.model.vo;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Ԫ�ظ���
 * @author Jenson
 * ������ϷԪ�صĸ���
 */
public abstract class SuperElement {
	
	//Ԫ������
	private int x;
	private int y;
	private int w;
	private int h;
	//��¼�Ƿ���
	private boolean alive; 
	
	//���캯��
	private SuperElement() {}
	public SuperElement(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		alive = true;
	}
	
	//ģ����
	public void update() {
		move();
		destroy();
	}
	
	//Ԫ��չʾ
	public abstract void showElement(Graphics g);
	
	//Ԫ���ƶ�
	public abstract void move();
	
	//Ԫ������
	public abstract void destroy();
	
	//this pk �������������
	public boolean crash(SuperElement se) {
		Rectangle r1 = new Rectangle(x, y, w, h);
		Rectangle r2 = new Rectangle(se.x, se.y, se.w, se.h);
		return r1.intersects(r2);//�н�����Χtrue
	}
	
	//���� pk �������ȽϹ���
	public static boolean crash(SuperElement se1,SuperElement se2) {
		Rectangle r1 = new Rectangle(se1.x, se1.y, se1.w, se1.h);
		Rectangle r2 = new Rectangle(se2.x, se2.y, se2.w, se2.h);
		return r1.intersects(r2);//�н�����Χtrue
	}
	
	//getters and setters
	public void setGeometry(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public int getTopBound() {
		return y;
	}
	
	public int getLeftBound() {
		return x;
	}
	
	public int getRightBound() {
		return x+w;
	}
	
	public int getBottomBound() {
		return y+h;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	public boolean isAlive() {
		return alive;
	}
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
}
