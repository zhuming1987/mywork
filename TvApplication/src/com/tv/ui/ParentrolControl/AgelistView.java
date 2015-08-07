package com.tv.ui.ParentrolControl;

import android.content.Context;
import android.util.Log;

import com.tv.data.MenuItem;
import com.tv.framework.data.TvEnumSetData;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ListView.TvListItemView;
import com.tv.ui.ListView.TvListView;

public class AgelistView extends TvListView{

	public AgelistView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void ItemClickListener(int index) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvListView::ItemClickListener: index is " + index);
		//OFF的时候，是只有第一个item显示被锁
		if(index==0)
		{
			for(TvListItemView view : viewList)
			{
				if(view.index == 0)
				{	
					view.setChecked(true);
					continue;
				}
				view.setChecked(false);
			}
		}
		//非OFF的时候，所有大于这个等级的item，都显示被锁
		//假设锁的是11，这11~18都被锁
		else
		{
			for(TvListItemView view : viewList)
			{
				if(view.index >= index)
				{	
					view.setChecked(true);
					continue;
				}
				view.setChecked(false);
			}
		}
		TvEnumSetData eData = (TvEnumSetData) data.getItemData();
		eData.setCurrentIndex(index);
		TvPluginControler.getInstance().setConfigData(data.getId(), eData);
	}
	@Override
	public void updateView(MenuItem data) {
		// TODO Auto-generated method stub
		super.updateView(data);
		int default_index = ((TvEnumSetData)data.getItemData()).getCurrentIndex();
		//OFF的时候，是只有第一个item显示被锁
		if(default_index==0)
		{
			viewList.get(default_index).setChecked(true);
			
		}
		//非OFF的时候，所有大于这个等级的item，都显示被锁
		//假设锁的是11，这11~18都被锁
		else
		{
			for(int i=default_index;i<viewList.size();i++)
			{
				viewList.get(i).setChecked(true);
			}
		}
	}
}
