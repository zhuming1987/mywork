package com.tv.ui.LNBSetting;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvSearchTypes;
import com.tv.framework.define.TvSearchTypes.SATELLITE_OPERATION_TYPE;
import com.tv.ui.base.TvBaseDialog;

public class SatelliteOperationDialog extends TvBaseDialog
{
	private DialogListener dialogListener;
	private SatelliteEditView satelliteEditView;
	private SatelliteAddView satelliteAddView;

	public SatelliteOperationDialog() 
	{
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_SATELLITE_EDIT);
		// TODO Auto-generated constructor stub
		if(TvSearchTypes.SATELLITE_SETTING_OPERATION_TYPE == SATELLITE_OPERATION_TYPE.SATELLITE_OPERATION_ADD)
		{
			satelliteAddView = new SatelliteAddView(TvContext.context);
			setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			setDialogContentView(satelliteAddView);
			satelliteAddView.setParentDialog(this);
			setAutoDismiss(false);	
		}
		else if(TvSearchTypes.SATELLITE_SETTING_OPERATION_TYPE == SATELLITE_OPERATION_TYPE.SATELLITE_OPERATION_EDIT)
		{
			satelliteEditView = new SatelliteEditView(TvContext.context);
			setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			setDialogContentView(satelliteEditView);
			satelliteEditView.setParentDialog(this);
			setAutoDismiss(false);
		}
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
			if(TvSearchTypes.SATELLITE_SETTING_OPERATION_TYPE == SATELLITE_OPERATION_TYPE.SATELLITE_OPERATION_ADD)
			{
				satelliteAddView.updateView((Integer) obj);
			}
			else if(TvSearchTypes.SATELLITE_SETTING_OPERATION_TYPE == SATELLITE_OPERATION_TYPE.SATELLITE_OPERATION_EDIT)
			{
				satelliteEditView.updateView((Integer) obj);
			}
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
		if(keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/)
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
