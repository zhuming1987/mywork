package com.tv.ui.AudioLanguage;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.data.MenuItem;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class AudioLanguageListViewDialog extends TvBaseDialog{

	protected AudioLanguageListView listView;
	private DialogListener dialogListener;
	
	public AudioLanguageListViewDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_LISTVIEW);
		// TODO Auto-generated constructor stub
		initUI();
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return false;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		MenuItem data = (MenuItem)obj;
		switch (cmd) {
		case DIALOG_SHOW:
			super.showUI();
			listView.updateView(data);
			break;
		case DIALOG_UPDATE:
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}
	public void initUI()
	{
		listView = new AudioLanguageListView(TvContext.context);
		listView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(listView);
	}
}
