package com.tv.ui.ChannelSearch;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class DVBSAutoSearchSettingDialog extends TvBaseDialog{

	private DVBSAutoSearchSettingView DVBSAutoSearchSettingView;
	private DialogListener dialogListener;
	public DVBSAutoSearchSettingDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_DVBS_AUTO_SEARCH_SETTING);
		// TODO Auto-generated constructor stub
		DVBSAutoSearchSettingView = new DVBSAutoSearchSettingView(TvContext.context,this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(DVBSAutoSearchSettingView);
		setAutoDismiss(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU){
			processCmd(TvConfigTypes.TV_DIALOG_DVBS_AUTO_SEARCH_SETTING,DialogCmd.DIALOG_DISMISS, null);
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		if(!key.equals(TvConfigTypes.TV_DIALOG_DVBS_AUTO_SEARCH_SETTING)) return false;
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
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

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		this.dialogListener = dialogListener;
		return true;
	}
}