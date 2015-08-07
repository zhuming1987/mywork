package com.tv.ui.ToastFocus;

import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog;

public class TvToastFocusDialog extends TvBaseDialog{

	private TvToastFocusView toastFocusView;
	private DialogListener dialogListener;
	
	public TvToastFocusDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_TOAST_FOCUS);
		// TODO Auto-generated constructor stub
		toastFocusView = new TvToastFocusView(TvContext.context);
		toastFocusView.setParentDialog(this);
		setDialogAttributes(Gravity.CENTER,  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(toastFocusView, new LayoutParams((int) (802 / TvUIControler.getInstance().getResolutionDiv()),
                (int) (422 / TvUIControler.getInstance().getResolutionDiv())));
		setAutoDismiss(false);
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return false;
	}
	
	public void setOnBtClickListener(OnBtClickListener onBtClickListener) {
		toastFocusView.setOnBtClickListener(onBtClickListener);
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		TvToastFocusData toastFocusData = (TvToastFocusData)obj;
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			toastFocusView.updateView(toastFocusData);
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
