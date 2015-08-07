package com.tv.data;

import java.util.ArrayList;

public class TvType {
	
	private TvType(){
		setTvTpye();
	};
	
	private static TvType tvTpye=new TvType();
	
	public static TvType getInstance()
	{
		return tvTpye;
	}
	
	private static DataControler dataControler;
	
	private void setTvTpye()
	{
		dataControler=DVBTDataControler.getInstance();
	}
	
	public DataControler getDataControler() {
		return dataControler;
	}

	public ArrayList<MenuItem> getMenuData()
	{
		//menuData=dataControler.getAllList();
		//return menuData;
		return dataControler.getAllList();
	}
	
	public ArrayList<MenuItem> getQuickMenuData()
	{
		//quickMenuData = dataControler.getQuickMenuList();
		//return quickMenuData;
		return dataControler.getQuickMenuList();
	}
	
	public ArrayList<MenuItem> getQuickMenuForOther()
	{
		return dataControler.getQuickMenuForOther();
	}
	
	public MenuItem getSubMenuItem(String key)
	{
		return dataControler.getMenuItemById(key);
	}
	
	public boolean isInit() {
		// TODO Auto-generated method stub
		return dataControler.isInit();
	}
	
}
