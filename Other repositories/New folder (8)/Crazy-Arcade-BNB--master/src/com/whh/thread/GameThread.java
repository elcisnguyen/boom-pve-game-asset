package com.whh.thread;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.whh.model.manager.ElementManager;
import com.whh.model.vo.SuperElement;

public class GameThread extends Thread{
	public void run() {
		int i=1;
		while(true) {
			loadElement(i);
			runGame();
			i++;
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public void runGame(){
		while(true){
			Map<String,List<SuperElement>> map=
					ElementManager.getManager().getMap();
			if(map.get("player").size()==0||map.get("playerB").size()==0)
				break;
			Set<String> set=map.keySet();
			for(String key:set){//�������ڱ����Ĺ����У��������ڵ�Ԫ�ز����� ���ӻ���ɾ��
				List<SuperElement> list=map.get(key);
//				for(int i=list.size()-1;i>=0;i--){
				for(int i=0;i<list.size();i++){
					list.get(i).update();
					if(!list.get(i).isVisible()&&key.indexOf("map1")!=0){
						list.remove(i--);
					}
				}
				
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
//	���ƽ���
	private void loadElement(int i) {
		ElementManager.getManager().load(i);

	}
}
