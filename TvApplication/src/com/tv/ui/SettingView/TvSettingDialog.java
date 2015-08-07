package com.tv.ui.SettingView;

import com.tv.app.R;
import com.tv.data.MenuItem;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

public class TvSettingDialog extends TvBaseDialog{

	private TvSettingView settingView;
	private DialogListener dialogListener;
	
	public TvSettingDialog() {
		super(TvContext.context, R.style.dialog, TvConfigTypes.TV_DIALOG_SETTING);
		// TODO Auto-generated constructor stub
		settingView = new TvSettingView(TvContext.context);
		settingView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(settingView);
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return true;
	}
	
	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		Log.i("gky", getClass().getName() +"-->processCmd:key is " + key + " cmd is " + cmd.toString()
				+ " obj is " + obj);
		MenuItem data = (MenuItem)obj;
		switch (cmd) {
			case DIALOG_SHOW:
			case DIALOG_UPDATE:
				super.showUI();
				settingView.updateView(data);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_DOWN
				&& (keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/)){
			processCmd(null, DialogCmd.DIALOG_DISMISS, null);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
