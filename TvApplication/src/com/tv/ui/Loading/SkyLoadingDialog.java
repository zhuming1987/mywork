package com.tv.ui.Loading;

import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;
import com.tv.ui.base.TvBaseDialog.DialogListener;


public class SkyLoadingDialog extends TvBaseDialog{

    SkyLoadingView loadingview;
    private DialogListener dialogListener;
    
    public SkyLoadingDialog() 
    {
    	super(TvContext.context, R.style.dialog,TvConfigTypes.TV_LOADING_DIALOG);
        loadingview = new SkyLoadingView(TvContext.context);
        LinearLayout.LayoutParams layoutLp = new LinearLayout.LayoutParams(800,1080);
        loadingview.setParentDialog(this);
		setDialogAttributes(Gravity.TOP | Gravity.CLIP_HORIZONTAL, 
				 WindowManager.LayoutParams.TYPE_TOAST);
		setDialogContentView(loadingview, layoutLp);
		setAutoDismiss(false);
    }

	public boolean setDialogListener(DialogListener dialogListener) 
	{
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return false;
	}



	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) 
	{
		// TODO Auto-generated method stub
		//if(!key.equals(TvConfigTypes.TV_LOADING_DIALOG)) return false;
		switch (cmd) 
		{
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			loadingview.update();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			loadingview.rotate.cancel();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}
}
