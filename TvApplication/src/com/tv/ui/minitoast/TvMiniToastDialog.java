package com.tv.ui.minitoast;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class TvMiniToastDialog extends TvBaseDialog{

	private TvMiniToastView miniToastView = null;
	private CountDownHandler mHandler = null;
	
	public TvMiniToastDialog() {
		super(TvContext.context, R.style.dialog, TvConfigTypes.TV_DIALOG_MINITOAST);
		// TODO Auto-generated constructor stub
		miniToastView = new TvMiniToastView(TvContext.context);
		mHandler = new CountDownHandler();
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.y = (int)(351/TvUIControler.getInstance().getResolutionDiv());
		setContentView(miniToastView, params);
		setDialogAttributes(Gravity.CENTER,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);
		setAutoDismiss(false);
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		String contents = (String)obj;
		if(cmd == DialogCmd.DIALOG_SHOW)
		{
			super.showUI();
			miniToastView.updateView(contents);
			startCountdown();
			return true;
		}
		else if(cmd == DialogCmd.DIALOG_DISMISS)
		{
			super.dismissUI();
			return true;
		}
		return false;
	}
	
	public void startCountdown(){
		if (mHandler.hasMessages(0)) {
			mHandler.removeMessages(0);
		}
		Message msg = mHandler.obtainMessage();
		msg.what = 0;
		mHandler.sendMessageDelayed(msg, 2000);
	}

	public class CountDownHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			processCmd(null, DialogCmd.DIALOG_DISMISS, null);
		}
		
	}
}
