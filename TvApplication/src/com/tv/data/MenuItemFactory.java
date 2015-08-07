package com.tv.data;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuItemFactory {
	
	public HashMap<String, MenuItem> IDtoItem=new HashMap<String, MenuItem>();
	
	private static MenuItemFactory menuItemFactory=new MenuItemFactory();
	
	private MenuItemFactory(){};
	
	public static MenuItemFactory getInstance()
	{
		return menuItemFactory;
	}
	
	public MenuItem createMenuItem(String id, String name,boolean isVisable)
	{
		MenuItem item=new MenuItem(id,name,isVisable,null);
		IDtoItem.put(id,item);
		return item;
	} 
	
	public MenuItem createMenuItem(String id)
	{
		MenuItem item=new MenuItem(id,"",true,null);
		IDtoItem.put(id,item);
		return item;
	}
	
	public MenuItem createMenuItem(String id, String name,ArrayList<MenuItem> childList)
	{
		MenuItem item=new MenuItem(id,name,true,childList);
		IDtoItem.put(id,item);
		return item;
	}
	
	
}
