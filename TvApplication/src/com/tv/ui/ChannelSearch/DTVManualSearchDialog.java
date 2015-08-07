package com.tv.ui.ChannelSearch;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog;

public class DTVManualSearchDialog  extends TvBaseDialog{

	private DTVManualSearchView DTVManualSearchView;
	private DialogListener dialogListener;
	private QuickKeyListener quickKeyListener;
	public DTVManualSearchDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_DTV_MANUAL_SEARCH);
		// TODO Auto-generated constructor stub
		this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		DTVManualSearchView = new DTVManualSearchView(TvContext.context);
		DTVManualSearchView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(DTVManualSearchView);
		setAutoDismiss(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/){
			processCmd(TvConfigTypes.TV_DIALOG_DTV_MANUAL_SEARCH,DialogCmd.DIALOG_DISMISS, null);
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			return false;
		}
		else 
		{
			return true;
		}
	}
	
	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		if(!key.equals(TvConfigTypes.TV_DIALOG_DTV_MANUAL_SEARCH)) return false;
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			DTVManualSearchView.mRunthread = false;
			TvPluginControler.getInstance().getChannelManager().stopDtvManualSearch();
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		this.dialogListener = dialogListener;
		return true;
	}
}
