package com.tv.ui.EPGManage;

import android.content.Intent;
import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class EPGRecordSettingDialog extends TvBaseDialog{
	

	private DialogListener dialogListener;
	private EPGRecordSettingView EPGRecordSettingView;
	
	public EPGRecordSettingDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_EPG_RECORD_SETTING);
		// TODO Auto-generated constructor stub
		EPGRecordSettingView = new EPGRecordSettingView(TvContext.context);
		EPGRecordSettingView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(EPGRecordSettingView);
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
				if((obj != null) && (obj instanceof Intent))
				{
					EPGRecordSettingView.updateView((Intent)obj);
				}
				
				break;
			case DIALOG_HIDE:
				break;
			case DIALOG_DISMISS:
				super.dismissUI();
				if(null != dialogListener)
					dialogListener.onDismissDialogDone(null,null);
				break;
		}
		return false;
	}
}