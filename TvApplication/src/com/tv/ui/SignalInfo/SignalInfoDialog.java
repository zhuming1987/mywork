package com.tv.ui.SignalInfo;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.data.SignalInfoData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog;

public class SignalInfoDialog extends TvBaseDialog{

	private DialogListener dialogListener;
	private SignalInfoView signalInfoView;
	
	public SignalInfoDialog(){
		super(TvContext.context, R.style.dialog, TvConfigTypes.TV_DIALOG_SIGNAL_INFO);
		setDialogAttributes(Gravity.TOP|Gravity.LEFT,  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		signalInfoView = new SignalInfoView(TvContext.context);
		setDialogContentView(signalInfoView);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/){
			processCmd(TvConfigTypes.TV_DIALOG_SIGNAL_INFO,DialogCmd.DIALOG_DISMISS, null);
			return true;
		}
		return false;
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
		switch (cmd) {
		case DIALOG_SHOW:
			SignalInfoData signalInfoData = TvPluginControler.getInstance().getChannelManager().getSignalInfoData();
			super.showUI();
			signalInfoView.updateView(signalInfoData);
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
}
