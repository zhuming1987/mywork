package com.tv.ui.LNBSetting;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvSearchTypes;
import com.tv.framework.define.TvSearchTypes.TRANSPONDER_OPERATION_TYPE;
import com.tv.ui.base.TvBaseDialog;

public class TransponderOperationDialog extends TvBaseDialog
{
	private DialogListener dialogListener;
	private TransponderEditView transponderEditView;
	private TransponderAddView transponderAddView;

	public TransponderOperationDialog() 
	{
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_TRANSPONDER_OPERATION);
		// TODO Auto-generated constructor stub
		if(TvSearchTypes.TRANSPONDERE_SETTING_OPERATION_TYPE == TRANSPONDER_OPERATION_TYPE.TRANSPONDER_OPERATION_ADD)
		{
			transponderAddView = new TransponderAddView(TvContext.context);
			setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			setDialogContentView(transponderAddView);
			transponderAddView.setParentDialog(this);
			setAutoDismiss(false);	
		}
		else if(TvSearchTypes.TRANSPONDERE_SETTING_OPERATION_TYPE == TRANSPONDER_OPERATION_TYPE.TRANSPONDER_OPERATION_EDIT)
		{
			transponderEditView = new TransponderEditView(TvContext.context);
			setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			setDialogContentView(transponderEditView);
			transponderEditView.setParentDialog(this);
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
			if(TvSearchTypes.TRANSPONDERE_SETTING_OPERATION_TYPE == TRANSPONDER_OPERATION_TYPE.TRANSPONDER_OPERATION_ADD)
			{
				transponderAddView.updateView((Integer) obj);
			}
			else if(TvSearchTypes.TRANSPONDERE_SETTING_OPERATION_TYPE == TRANSPONDER_OPERATION_TYPE.TRANSPONDER_OPERATION_EDIT)
			{
				transponderEditView.updateView((Integer) obj);
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
