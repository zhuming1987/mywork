package com.tv.ui.EPGManage;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog;

public class EPGManageDialog extends TvBaseDialog{

	private DialogListener dialogListener;
	private EPGManageView EPGManageView;

	public EPGManageDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_EPG_MANAGE);
		// TODO Auto-generated constructor stub
		EPGManageView = new EPGManageView(TvContext.context);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(EPGManageView);
		EPGManageView.setParentDialog(this);
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
			EPGManageView.updateView();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			TvPluginControler.getInstance().getEpgManager().disableEpgBarkerChannel();
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}
}
