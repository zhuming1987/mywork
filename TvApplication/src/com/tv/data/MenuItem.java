package com.tv.data;

import java.util.ArrayList;

import android.util.Log;

import com.tv.framework.data.TvSetData;
import com.tv.framework.define.TvConfigTypes.TvConfigType;
import com.tv.plugin.TvPluginControler;

public class MenuItem {
	
	private String id = null;
	private String name = null;
	private TvConfigType itemType = null;
	private boolean isVisable = true;
	private TvSetData itemDate = null;
	private boolean ishide = false;
	private ArrayList<MenuItem> chlidList=null;
	
	private String TAG = "MenuItem";

	
	public MenuItem(String id, String name,boolean isVisable,ArrayList<MenuItem> chlidList) {
		this.id = id;
		this.name = name;
		this.itemDate = getConfigDate();
		this.itemType = getConfigType();
		this.isVisable = getVisable();
		this.ishide = getHide();
		this.chlidList=chlidList;
	}
	
	private TvSetData getConfigDate()
	{
		return TvPluginControler.getInstance().getConfigData(this.id);
	}
	
	private TvConfigType getConfigType()
	{
		return  itemDate==null?TvConfigType.TV_CONFIG_SINGLE:TvConfigType.valueOf(itemDate.getType());
	}
	
	private boolean getVisable()
	{
		if(itemDate!=null)
		{
			return itemDate.isEnable();
		}
		else 
		{
			return true;
		}
	}
	
	private boolean getHide()
	{
		if(itemDate!=null)
		{
			return itemDate.isHide();
		}
		else 
		{
			return false;
		}
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isVisable() {
		return isVisable;
	}
	
	public boolean ishide()
	{
		return ishide;
	}

	public void setVisable(boolean isVisable) { 
		this.isVisable = isVisable;
	}
	
	public TvSetData getItemData()
	{
		return this.itemDate;
	}
	
	public TvConfigType getItemType()
	{
        return this.itemType;
	}
	
	public void AddChlid(MenuItem ChlidMenuItem)
	{
		chlidList.add(ChlidMenuItem);
	}
	
	public ArrayList<MenuItem> getChlidList()
	{
		return chlidList;
	}
	
	public void update()
	{
		Log.e(TAG, "update() call getConfigDate()");
		
		this.itemDate = getConfigDate();
		
		Log.e(TAG, "update() call getConfigType()");
		
		this.itemType = getConfigType();
		
		Log.e(TAG, "update() call getVisable()");
		this.isVisable = getVisable();
		
		Log.e(TAG, "update() call getHide()");
		this.ishide =getHide();
	}
	
}

