package com.tv.app;

import com.tv.framework.define.TvContext;
import com.tv.framework.plugin.PlatformPlugin;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

/**
 * 
 * @author gky
 *
 */
public class TvPlayerView {

	private FrameLayout contentView = null;
	
	private static TvPlayerView instance = null;
	
	public static TvPlayerView getInstance(){
		if(instance == null)
			instance = new TvPlayerView();
		return instance;
	}
	
	public void init(){
		
		contentView = new FrameLayout(TvContext.context);
		contentView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	public void updateView(PlatformPlugin tvPlugin){
		Log.i("gky", "TvPlayerView::updateView");
		Message msg = new Message();
		msg.obj = tvPlugin;
		mHandler.sendMessage(msg);
	}
	
	private Handler mHandler = new Handler(Looper.getMainLooper()){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			PlatformPlugin tvPlugin = (PlatformPlugin)msg.obj;
			if(tvPlugin != null){
				Log.i("gky", "TvPlayerView::mHandler updateView");
				View view = tvPlugin.getCommonManager().getSurfaceView();
				Log.i("gky", "TvPlayerView::mHandler view is " + view);
				contentView.removeAllViews();
				contentView.addView(view);
				contentView.invalidate();
			}
		}
		
	};

	public ViewGroup getContentView() {
		return contentView;
	}

}
