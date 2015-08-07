package com.tv.ui.LNBSetting;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class SatelliteSignalScanDialog extends TvBaseDialog
{
	private DialogListener dialogListener;
	private SatelliteSignalScanView satelliteSignalScanView;

	public SatelliteSignalScanDialog() 
	{
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_SATELLITE_SCAN);
		// TODO Auto-generated constructor stub
		satelliteSignalScanView = new SatelliteSignalScanView(TvContext.context);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(satelliteSignalScanView);
		satelliteSignalScanView.setParentDialog(this);
		setAutoDismiss(false);	
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
		case DIALOG_UPDATE:
			super.showUI();
			satelliteSignalScanView.updateView();
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
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_PROG_BLUE)
		{
			processCmd(TvConfigTypes.TV_DIALOG_SATELLITE_SCAN,DialogCmd.DIALOG_DISMISS, null);
			LNBSettingDialog lnbSettingDialog = new LNBSettingDialog();
			lnbSettingDialog.setDialogListener(dialogListener);
			lnbSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_LNB_SETTING, DialogCmd.DIALOG_SHOW, null);
			return true;
		}
		return mCancelable;
	}
}
