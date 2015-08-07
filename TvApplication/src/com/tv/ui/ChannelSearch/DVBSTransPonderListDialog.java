package com.tv.ui.ChannelSearch;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class DVBSTransPonderListDialog extends TvBaseDialog{

	private DVBSTransPonderListView DVBSTransPonderListView;
	private DialogListener dialogListener;
	public DVBSTransPonderListDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_DVBS_TRANSPONDER_LIST);
		// TODO Auto-generated constructor stub
		DVBSTransPonderListView = new DVBSTransPonderListView(TvContext.context,this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(DVBSTransPonderListView);
		setAutoDismiss(false);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU){
			processCmd(TvConfigTypes.TV_DIALOG_DVBS_TRANSPONDER_LIST,DialogCmd.DIALOG_DISMISS, null);
			return true;
		}
		if(keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			return false;
		}
		return false;
	}
	
	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		if(!key.equals(TvConfigTypes.TV_DIALOG_DVBS_TRANSPONDER_LIST)) return false;
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			DVBSTransPonderListView.updateView();
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
