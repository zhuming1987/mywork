package com.tv.ui.ChannelNum;

import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class TvChannelNumDialog extends TvBaseDialog{
	
	private TvChannelNumView tvChannelNumView;
	
	public TvChannelNumDialog() {
		super(TvContext.context, R.style.dialog, TvConfigTypes.TV_DIALOG_CHANNEL_NUM);
		// TODO Auto-generated constructor stub
		tvChannelNumView = new TvChannelNumView(TvContext.context);
		tvChannelNumView.setParentDialog(this);
		setDialogAttributes(Gravity.RIGHT|Gravity.TOP, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(tvChannelNumView, new LayoutParams((int)(400/TvUIControler.getInstance().getResolutionDiv()),
				(int)(200/TvUIControler.getInstance().getResolutionDiv())));
	}
	
	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) {
		case DIALOG_SHOW:
			super.showUI();
			tvChannelNumView.updateView((Integer)obj);
			break;
		case DIALOG_HIDE:
		case DIALOG_UPDATE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			break;
		}
		return false;
	}

}
