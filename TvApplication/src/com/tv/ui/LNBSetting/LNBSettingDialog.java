package com.tv.ui.LNBSetting;

import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog;

public class LNBSettingDialog extends TvBaseDialog{

	private DialogListener dialogListener;
	private LNBSettingView LNBSettingView;

	public LNBSettingDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_LNB_SETTING);
		// TODO Auto-generated constructor stub
		LNBSettingView = new LNBSettingView(TvContext.context,this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(LNBSettingView);
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
			LNBSettingView.updateView(true);
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			if(LNBSettingView.siganlScanFlag)
			{
				Log.i("wangxian", "signal stop scan");
				TvPluginControler.getInstance().getChannelManager().StopDvbsScan();
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				LNBSettingView.siganlScanFlag = false;
			}
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}
}
