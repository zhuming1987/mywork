package com.tv.ui.PowerSetingView;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class TvPowerSettingDialog extends TvBaseDialog{

	private TvPowerSettingView powerSettingView;
	private DialogListener dialogListener;
	
	public TvPowerSettingDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_DEMO);
		// TODO Auto-generated constructor stub		
		powerSettingView = new TvPowerSettingView(TvContext.context);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(powerSettingView);
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
		switch (cmd) {
		case DIALOG_SHOW:
			super.showUI();
			powerSettingView.updateView(null);
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
