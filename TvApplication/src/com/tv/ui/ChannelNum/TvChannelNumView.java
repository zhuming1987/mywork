package com.tv.ui.ChannelNum;

import com.tv.app.TvUIControler;
import com.tv.data.TvType;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SourceInfo.SourceInfoDialog;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnKeyListener;

public class TvChannelNumView extends LinearLayout implements OnKeyListener{

	private float div = TvUIControler.getInstance().getResolutionDiv();
	private float dip = TvUIControler.getInstance().getDipDiv();
	
	private TextView textView_1;
	private TextView textView_2;
	private TextView textView_3;
	
	private TvChannelNumDialog parentDialog = null;
	private Handler mHandler = new Handler();
	public int channelNum = 0;
	
	private static final int TIMER = 3000;
	
	public TvChannelNumView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setOnKeyListener(this);
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = (int)(55/div);
		layoutParams.rightMargin = (int)(77/div);
		this.setLayoutParams(layoutParams);
		textView_1 = initTextView();
		this.addView(textView_1);
		textView_2 = initTextView();
		this.addView(textView_2);
		textView_3 = initTextView();
		this.addView(textView_3);
		textView_1.setVisibility(View.GONE);
		textView_2.setVisibility(View.GONE);
		textView_3.setVisibility(View.GONE);
	}

	public void updateView(int keyCode){
		Log.i("gky", "TvChannelNumView::updateView:: keyCode is " + keyCode);
		if(keyCode == KeyEvent.KEYCODE_0 && channelNum == 0){
			parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
			return;
		}
		int num = keyCode - KeyEvent.KEYCODE_0;
		if(channelNum == 0){
			textView_1.setVisibility(View.VISIBLE);
			textView_1.setText(num+"");
			channelNum = num;	
		}else if(channelNum > 0 && channelNum < 10){
			textView_2.setVisibility(View.VISIBLE);
			textView_2.setText(num+"");
			channelNum = channelNum * 10 + num;
		}else if(channelNum > 9 && channelNum < 100 ){
			textView_3.setVisibility(View.VISIBLE);
			textView_3.setText(num+"");
			channelNum = channelNum * 10 + num;
		}
		mHandler.removeCallbacks(timeRunnable);
		mHandler.postDelayed(timeRunnable, TIMER);
	}
	
	public TextView initTextView(){
		TextView textView = new TextView(TvContext.context);
		textView.setTextSize((int)(100/dip));
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER);
		textView.setPadding((int)(5/div), 0, (int)(5/div), 0);
		textView.setSingleLine();
		return textView;
	}

	public void setParentDialog(TvChannelNumDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_UP)
			return false;
		Log.i("gky", "TvChannelNumView::onKey::keyCode is " + keyCode);
		switch (keyCode) {
			case KeyEvent.KEYCODE_0:
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_9:
				updateView(keyCode);
				return true;
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				setToProgram();
				return true;
			case KeyEvent.KEYCODE_BACK:
				mHandler.removeCallbacks(timeRunnable);
				parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
				return true;
		}
		return false;
	}
	
	public Runnable timeRunnable = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			setToProgram();
		}
	};
	
	public void setToProgram(){
		mHandler.removeCallbacks(timeRunnable);
		parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
		boolean ret = TvPluginControler.getInstance().getChannelManager().setProgram(channelNum);
		Log.i("gky", "TvChannelNumView::setToProgram::ret is " + ret + " channelNum is " + channelNum);
		TvUIControler.getInstance().reMoveAllDialogs();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/**打个补丁,atv只搜一个台,切其他的频道会造成无信号状态*/
		TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
		if(curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV){
			int curCh = TvPluginControler.getInstance().getChannelManager().getCurrentChannelNumber();
			curCh++;
			boolean isSignal = TvPluginControler.getInstance().getCommonManager().isSignalState();
			if(curCh != channelNum && !isSignal){
				Log.e("gky", getClass().getSimpleName() + "#######--->ERROR switch to Ch:"+channelNum+ " failed Signal is "+isSignal+" and return");
				TvPluginControler.getInstance().getChannelManager().setProgram(curCh);
			}
		}
		/******************************/
		SourceInfoDialog sourceInfoDialog = new SourceInfoDialog(KeyEvent.KEYCODE_ALTERNATE);
		sourceInfoDialog.setDialogListener(null);
		sourceInfoDialog.processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO,DialogCmd.DIALOG_SHOW, true);
	}
}
