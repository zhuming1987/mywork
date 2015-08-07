package com.tv.ui.CECDeviceList;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class CECDeviceListDialog extends TvBaseDialog{

	private CECDeviceListView cecDeviceListView;
	private DialogListener dialogListener = null;
	
	public CECDeviceListDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_CEC_DEVICE_LIST);
		// TODO Auto-generated constructor stub		
		cecDeviceListView = new CECDeviceListView(TvContext.context);
		cecDeviceListView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(cecDeviceListView);
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
		if(!key.equals(TvConfigTypes.TV_DIALOG_CEC_DEVICE_LIST))
		{
			return false;
		}
		
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			cecDeviceListView.updateView();
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
