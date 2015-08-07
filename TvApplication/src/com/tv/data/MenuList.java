package com.tv.data;

import java.util.ArrayList;

public class MenuList {

	private ArrayList<MenuItem> MenuItemList = new ArrayList<MenuItem>();
	
	private String id;
	
	private String name;
	
	
	public MenuList(String id,String name){
		this.id=id;
		this.name=name;
	}
	
	
	public MenuList(String name) {
		this.name = name;
	}

	/**
	 * 返回menuList
	 * @return menuList
	 */
	public ArrayList<MenuItem> getMenuItemList() {
		return MenuItemList;
	} 

	public String getName() {
		return name;
	}
	
	public String getid()
	{
		return id;
	}
	
	/**
	 * 按照ID去掉MenuItem
	 * @param ID 按照Menu的ID去掉List中的某项（）
	 */
	public void RemoveItemByID(String id)
	{
		for(MenuItem tempItem :MenuItemList)
		{
			if(tempItem.getId().equals(id))
			{	
				MenuItemList.remove(tempItem);
				break;
			}
		}
	}
	
	/**
	 * 添加一项到某个位置
	 * @param menuItem 添加的新项
	 * @param Postion 添加到的位置
	 */
	public void AddItem(int Postion,MenuItem menuItem)
	{
		MenuItemList.add(Postion, menuItem);
	}
	
	/**
	 * 添加一项到末尾
	 * @param menuItem 添加的新项
	 */
	public void AddItem(MenuItem menuItem)
	{
		MenuItemList.add(menuItem);
	}
	
	
	
}
