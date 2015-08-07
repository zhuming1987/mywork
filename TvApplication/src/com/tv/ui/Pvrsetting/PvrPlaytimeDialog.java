package com.tv.ui.Pvrsetting;

import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.Pvrsetting.PvrPlaytimeView.OnDlgClickListener;
import com.tv.ui.base.TvBaseDialog;

public class PvrPlaytimeDialog extends TvBaseDialog
{
	private PvrPlaytimeView pvrPlaytimeView;
	private DialogListener dialogListener;
	
	public PvrPlaytimeDialog() 
	{
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_PVRPLAYTIME);
		// TODO Auto-generated constructor stub
		pvrPlaytimeView = new PvrPlaytimeView(TvContext.context);
		pvrPlaytimeView.setParentDialog(this);
		setDialogAttributes(Gravity.CENTER,  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(pvrPlaytimeView, new LayoutParams((int) (802 / TvUIControler.getInstance().getResolutionDiv()),
				(int) (422 / TvUIControler.getInstance().getResolutionDiv())));		
	}
	
	@Override
	public boolean setDialogListener(DialogListener dialogListener)
	{
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		
		return false;		
	}
	
	public void setOnBtClickListener(OnDlgClickListener onBtClickListener)
	{
		pvrPlaytimeView.setOnBtClickListener(onBtClickListener);		
	}
	
	public int GetJimeTime()
	{
		int total = 0;
		int hour = pvrPlaytimeView.GetHour();
		int minute = pvrPlaytimeView.GetMinute();
		int second = pvrPlaytimeView.GetSecond();
		total = hour*3600 + minute*60 + second;
		
		return total;
	}
	
	public void ClearValue()
	{
		pvrPlaytimeView.ClearValue();
	}
	
	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj)
	{
		// TODO Auto-generated method stub
		switch (cmd) 
		{
		case DIALOG_SHOW:			
		case DIALOG_UPDATE:
			super.showUI();
			pvrPlaytimeView.updateView();
			break;
			
		case DIALOG_HIDE:
			break;
			
		case DIALOG_DISMISS:
			super.dismissUI();
			
			if(null != dialogListener)
			{
				dialogListener.onDismissDialogDone(null,null);
			}
			
			break;
		}
		return false;
	}

}
