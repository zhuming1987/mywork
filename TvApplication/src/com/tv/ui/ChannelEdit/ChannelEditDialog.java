package com.tv.ui.ChannelEdit;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.EnumChOpType;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class ChannelEditDialog extends TvBaseDialog{

	private ChannelEditView channelListView;
	private DialogListener dialogListener;	
	
	public ChannelEditDialog() {
		super(TvContext.context, R.style.dialog, TvConfigTypes.TV_DIALOG_CHANNEL_EDIT);
		// TODO Auto-generated constructor stub		
		channelListView = new ChannelEditView(TvContext.context);
		channelListView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(channelListView);
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
		switch (cmd) {
			case DIALOG_SHOW:
			case DIALOG_UPDATE:
				super.showUI();
				EnumChOpType chOpType = (EnumChOpType)obj;
				channelListView.updateView(chOpType);
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
