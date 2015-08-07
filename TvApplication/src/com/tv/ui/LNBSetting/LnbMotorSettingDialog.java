package com.tv.ui.LNBSetting;

import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class LnbMotorSettingDialog extends TvBaseDialog{

	private LnbMotorSettingView lnbMotorSettingView = null;
	private DialogListener dialogListener = null;
	
	public LnbMotorSettingDialog() {
		super(TvContext.context, R.style.dialog, TvConfigTypes.TV_DIALOG_LNB_MOTOR_SETTING);
		// TODO Auto-generated constructor stub
		lnbMotorSettingView = new LnbMotorSettingView(TvContext.context);
		lnbMotorSettingView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setContentView(lnbMotorSettingView);
		setAutoDismiss(false);
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
		Log.i("gky", getClass().toString() + cmd.toString());
		Integer optionType = (Integer)obj;
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			lnbMotorSettingView.updateView(optionType);
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
