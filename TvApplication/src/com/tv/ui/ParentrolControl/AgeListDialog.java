package com.tv.ui.ParentrolControl;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.ListView.TvListView;
import com.tv.ui.ListView.TvListViewDialog;

public class AgeListDialog extends TvListViewDialog{
	@Override
	public void initUI() {
		// TODO Auto-generated method stub
		listView = new AgelistView(TvContext.context);
		listView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(listView);
	}
}
