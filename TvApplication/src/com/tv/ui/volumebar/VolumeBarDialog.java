package com.tv.ui.volumebar;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvContext;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

public class VolumeBarDialog extends Dialog{

	private VolumeBarView volumeBarView;
	private EventHandler mHandler;
	
	private static final int MSG_TIME_OUT = 1001;
	private static final int TIME_OUT = 2000;
	
    public VolumeBarDialog() {
		super(TvContext.context, R.style.dialog);
		// TODO Auto-generated constructor stub
		getWindow().setType( WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		LayoutParams lp = getWindow().getAttributes();
        lp.token = null;
        lp.gravity = Gravity.TOP;
        lp.y = (int)(800/TvUIControler.getInstance().getResolutionDiv());
        lp.type = LayoutParams.TYPE_VOLUME_OVERLAY;
        lp.width = (int)(800/TvUIControler.getInstance().getResolutionDiv());
        lp.height = LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(lp);
        
        volumeBarView = new VolumeBarView(TvContext.context);
        volumeBarView.setParentDialog(this);
        setContentView(volumeBarView);
        
        mHandler = new EventHandler();
        
	}

    public void setVolume(int keycode){
    	if(volumeBarView != null)
    		volumeBarView.setVolume(keycode);
    }

    public void showUI(){
    	volumeBarView.updateView();
    	show();
    	Message msg = mHandler.obtainMessage();
    	msg.what = MSG_TIME_OUT;
    	mHandler.sendMessageDelayed(msg, TIME_OUT);
    }
    
    public void dismissUI(){
    	super.dismiss();
    	mHandler.removeMessages(MSG_TIME_OUT);
    }
    
    public void reMoveTimeOut(){
    	mHandler.removeMessages(MSG_TIME_OUT);
    	Message msg = mHandler.obtainMessage();
    	msg.what = MSG_TIME_OUT;
    	mHandler.sendMessageDelayed(msg, TIME_OUT);
    }
    
    private class EventHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what == MSG_TIME_OUT){
				dismissUI();
			}
		}
    	
    }
}
