package com.whh.model.vo;

import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.ImageIcon;

import com.whh.model.load.ElementLoad;
import com.whh.model.manager.ElementManager;

public class Items extends SuperElement{
	
	private int variety;//����
	private ImageIcon img;//ͼƬ
	private int moveX;
	
	public Items(int x,int y,int w,int h,int variety,ImageIcon img) {
		super(x,y,w,h);
		this.variety = variety;
		this.img = img;
		setLayer((y+h)/40);
	}
	
	public static Items createItems(int x,int y,int v){
		ImageIcon img = ElementLoad.getElementLoad().getMap().get("item"+v);
		return new Items(x,y,32,48,v,img);
	}
	
	public int getVariety() {
		return variety;
	}

	public void setVariety(int variety) {
		this.variety = variety;
	}

	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}

	@Override
	public void move() {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void destroy() {
		// TODO �Զ����ɵķ������
		
	}

	public void update(){
		super.update();
		burst();
		updateImage();
	}
	
	public void updateImage() {
		// TODO �Զ����ɵķ������
		moveX=(moveX>=3)?0:moveX+1;
	}

	@Override
	public void showElement(Graphics g) {
		// TODO �Զ����ɵķ������ 
		g.drawImage(img.getImage(), 
				getX()+4, getY()+20-getH(),        //��Ļ���Ͻ�����
				getX()+getW()+4, getY()+20,        //��Ļ���½�����
				32*moveX, 0,    			 			 //ͼƬ���Ͻ�����
				32*(moveX+1), 48,    		 		 //ͼƬ���½�����
				null);
	}
	
	
	public void burst() {
		if(isVisible()) {
				if(ElementManager.getManager().getBubblelist().get((getLayer()-1)*15+(getX()+getW()/2)/40)==2||ElementManager.getManager().getBubblelist().get((getLayer()-1)*15+(getX()+getW()/2)/40)==3) {
					setVisible(false);
				}
		}
	}
}
