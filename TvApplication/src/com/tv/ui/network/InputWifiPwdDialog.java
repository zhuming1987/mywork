package com.tv.ui.network;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.installguide.InstallGuideActivity;
import com.tv.ui.network.NetworkSetupView;

public class InputWifiPwdDialog extends TvBaseDialog{

	private InputWifiPwdView inputWifiPwdView;
	private DialogListener dialogListener;
	private float div = TvUIControler.getInstance().getResolutionDiv();
    
	public InputWifiPwdDialog(Context context) 
	{
		super(context, R.style.dialog,TvConfigTypes.INPUT_NETWORK_PASSWORD);
		inputWifiPwdView = new InputWifiPwdView(context);
		inputWifiPwdView.setParentDialog(this);
		Log.i("pxj", "InputWifiPwdDialog ::: set inputWifiPwdView attribute ");
		setDialogAttributes(Gravity.CENTER,  WindowManager.LayoutParams.TYPE_INPUT_METHOD_DIALOG);

		LinearLayout.LayoutParams layoutLp = new LinearLayout.LayoutParams(
                (int)(700 / div), (int)(320 / div));
	    layoutLp.gravity = Gravity.CENTER_HORIZONTAL;
		layoutLp.topMargin = (int)(200 / div);
		inputWifiPwdView.setY(-100 / div);
		setDialogContentView(inputWifiPwdView, layoutLp);


		setAutoDismiss(false);	

        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.showSoftInput(inputWifiPwdView.inputPwd, 0);
	}
	
	public InputWifiPwdDialog(Context context, int theme, String dialogType) 
	{
		super(context, theme, dialogType);
			// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean setDialogListener(DialogListener dialogListener) 
	{
			// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return false;
	}
	
	public InputWifiPwdView getInputWifiPwdView() 
	{
		return inputWifiPwdView;
	}

	public void setInputWifiPwdView(InputWifiPwdView inputWifiPwdView) 
	{
		this.inputWifiPwdView = inputWifiPwdView;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) 
	{
			// TODO Auto-generated method stub
//		if(!key.equals(TvConfigTypes.INPUT_NETWORK_PASSWORD)) 
//			return false;
		switch (cmd) 
		{
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();

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
