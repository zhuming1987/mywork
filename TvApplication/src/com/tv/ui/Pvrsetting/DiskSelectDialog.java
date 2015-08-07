package com.tv.ui.Pvrsetting;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.data.DiskSelectInfoData;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog;

public class DiskSelectDialog extends TvBaseDialog
{

	private DiskSelectView diskSelectView;
	private DialogListener dialogListener;
	private String dialogFlag = "";
	private TvToastFocusDialog diskTipDialog;
	
	private DiskSelectInfoData diskSelectInfoDatas;
	
	public DiskSelectDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_DISK_SELECT);
		// TODO Auto-generated constructor stub		
		diskSelectInfoDatas = TvPluginControler.getInstance().getPvrManager().updateDiskSelectInfo();
		if(diskSelectInfoDatas.usbDriverCount<1)
		{
			diskTipDialog = new TvToastFocusDialog();
			diskTipDialog.setOnBtClickListener(new OnBtClickListener() {				
				@Override
				public void onClickListener(boolean flag) {
					// TODO Auto-generated method stub									
				}
			});
		}
		else 
		{
			diskSelectView = new DiskSelectView(TvContext.context);
			diskSelectView.setParentDialog(this);
			setDialogAttributes(Gravity.LEFT | Gravity.TOP,
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			setDialogContentView(diskSelectView);
			setAutoDismiss(false);
		}
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return true;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj)
	{
		// TODO Auto-generated method stub
		if(obj != null)
		{
			dialogFlag = obj.toString();
		}	
		switch (cmd) {
		case DIALOG_SHOW:
			
			if(diskSelectInfoDatas.usbDriverCount<1)
			{
				diskTipDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
						new TvToastFocusData("", "", TvContext.context.getResources().getString(R.string.str_tip_plug_usb),1));
			}
			else 
			{
				super.showUI();	
				diskSelectView.update(diskSelectInfoDatas,dialogFlag);
			}			
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
				if(obj != null)
					dialogListener.onDismissDialogDone(obj);
				else 
					dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}
}
