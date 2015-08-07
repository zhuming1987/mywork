package com.tv.ui.CountDown;

import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class TvCountDownDialog extends TvBaseDialog{

	private TvCountDownView countDownView;
	private DialogListener dialogListener;
	
	public TvCountDownDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_COUNT_DOWN);
		// TODO Auto-generated constructor stub
		countDownView = new TvCountDownView(TvContext.context);
		countDownView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(countDownView);
		setAutoDismiss(false);
	}
	
	public TvCountDownDialog(TvCountFunc func) {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_COUNT_DOWN);
		// TODO Auto-generated constructor stub
		countDownView = new TvCountDownView(func,TvContext.context);
		countDownView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(countDownView);
		setAutoDismiss(false);
	}
//	TvPluginControler.getInstance().getCommonManager().standbySystem(null);

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return false;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) 
		{
			case DIALOG_SHOW:
			case DIALOG_UPDATE:
				Log.d("wxj", "TvCountDownDialog show");
				super.showUI();
				countDownView.updataView();
			break;
			case DIALOG_HIDE:
				Log.d("wxj", "TvCountDownDialog hide");
				countDownView.shutdownCountScheduler();
				break;
			case DIALOG_DISMISS:
				Log.d("wxj", "TvCountDownDialog dismiss");
				countDownView.shutdownCountScheduler();
				super.dismissUI();
				if(obj != null)
				{
					dialogListener.onDismissDialogDone(obj);
				}
				break;
		}
		return true;
	}

}
