package com.tv.data;

import java.util.ArrayList;

public class tvObserver {
	
	private ArrayList<MenuItem> observerList=new ArrayList<MenuItem>();
		
		public void onchenge(int msg)
		{
			for(MenuItem temp:observerList)
			{
				temp.update();
			}
		}
		public void registerObserver(MenuItem menuItem)
		{
			observerList.add(menuItem);
		}
}
