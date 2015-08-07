package com.tv.ui.Pvrsetting;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog;

public class PVRChannelListDialog extends TvBaseDialog
{

	private PVRChannelListView pvrChannelListView;
	private DialogListener dialogListener;
	public String diskPath;
 	public String diskLabel;
	
	public PVRChannelListDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_PVR_CHANNELLIST);
		// TODO Auto-generated constructor stub		
		pvrChannelListView = new PVRChannelListView(TvContext.context);
		pvrChannelListView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(pvrChannelListView);
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
	    String flag = (String) obj;
		switch (cmd) {
		case DIALOG_SHOW:
			super.showUI();
			pvrChannelListView.update(diskPath,diskLabel);
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			if(null == flag)
			{
				TvPluginControler.getInstance().getPvrManager().exitPlayBack();
			}			
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}
}
