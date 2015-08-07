package com.tv.ui.Loading;

import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvLoadingData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class TvLoadingDialog extends TvBaseDialog{

	private TvLoadingView tvLoadingView;
	private DialogListener dialogListener;
	
	public TvLoadingDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_LOADING);
		// TODO Auto-generated constructor stub
		tvLoadingView = new TvLoadingView(TvContext.context);
		setDialogAttributes(Gravity.CENTER,  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(tvLoadingView, new LayoutParams((int) (802 / TvUIControler.getInstance().getResolutionDiv()),
                (int) (422 / TvUIControler.getInstance().getResolutionDiv())));
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
		TvLoadingData data = (TvLoadingData)obj;
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			Log.i("gky", "TvLoadingDialog::processCmd DIALOG_SHOW");
			super.showUI();
			if(obj != null)
			{
				tvLoadingView.updateView(data.title,data.content);
			}
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			Log.i("gky", "TvLoadingDialog::processCmd DIALOG_DISMISS");
			super.dismissUI();
			tvLoadingView.stopLoading();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}

}
