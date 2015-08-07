package com.tv.ui.EPGManage;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvContext;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EPGSettingEnterItemView extends LinearLayout implements View.OnKeyListener, View.OnFocusChangeListener{

	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
	private TextView contentText;
	private TextView titleTextView;
	
	private boolean isEnabled = true;
	
	private EnterItemViewListener enterItemViewListener = null;
    
	public EPGSettingEnterItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setLayoutParams(new LayoutParams((int)(845/div), (int)(80/div)));
		this.setPadding((int)(15/div), (int)(5/div), 0, (int)(5/div));
		this.setOnFocusChangeListener(this);
		this.setOnKeyListener(this);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setGravity(Gravity.CENTER);
		
		titleTextView = new TextView(context);
		titleTextView.setTextColor(Color.WHITE);
		titleTextView.setTextSize((float)(36/dip));
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		titleTextView.setSingleLine(true);
		titleTextView.setEllipsize(TruncateAt.MARQUEE);
		titleTextView.setMarqueeRepeatLimit(-1);
		LayoutParams titletxtParams = new LayoutParams((int)(200/div), LayoutParams.MATCH_PARENT);
		titleTextView.setLayoutParams(titletxtParams);
		//this.addView(titleTextView);
		
		contentText = new TextView(context);
        contentText.getPaint().setAntiAlias(true);
        contentText.setTextColor(Color.WHITE);
        contentText.setBackgroundResource(R.drawable.setting_unselect_bg);
        contentText.setTextSize(30/dip);
        contentText.setGravity(Gravity.CENTER);
        LayoutParams contextParams = new LayoutParams((int)(526/div), (int)(60/div));
        //contextParams.leftMargin = (int)(80/div);
        contextParams.gravity = Gravity.CENTER;
        contentText.setLayoutParams(contextParams);
        this.addView(contentText);
	}

	public void updateView(String titleString) {
		// TODO Auto-generated method stub
		titleTextView.setText(titleString);
		contentText.setText(TvContext.context.getResources().getString(R.string.str_enter));
		if(isEnabled){
			this.setFocusable(true);
			this.setFocusableInTouchMode(true);
		}else {
			this.setFocusable(false);
			this.setFocusableInTouchMode(false);
			contentText.setTextColor(Color.GRAY);
		}
	}
	
	public void resetUI(String contentTxt){
		titleTextView.setVisibility(View.GONE);
		contentText.setText(contentTxt);
		if(isEnabled){
			this.setFocusable(true);
			this.setFocusableInTouchMode(true);
		}else {
			this.setFocusable(false);
			this.setFocusableInTouchMode(false);
			contentText.setTextColor(Color.GRAY);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (hasFocus) {
			titleTextView.setTextColor(Color.parseColor("#3598DC"));
			titleTextView.setSelected(true);
			contentText.setBackgroundResource(R.drawable.blue_sel_bg);
			contentText.setTextColor(Color.BLACK);
		} else {
			titleTextView.setTextColor(Color.WHITE);
			titleTextView.setSelected(false);
			contentText.setBackgroundResource(R.drawable.setting_unselect_bg);
			if(isEnabled){
				contentText.setTextColor(Color.WHITE);
			}else {
				contentText.setTextColor(Color.GRAY);
			}
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_UP)
			return false;
		if(keyCode == KeyEvent.KEYCODE_ENTER
				|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_BACK){
			if(enterItemViewListener != null)
				enterItemViewListener.onEnterItemViewListener(this, keyCode);
			return true;
		}
		return false;
	}

	public interface EnterItemViewListener{
		public void onEnterItemViewListener(View view, int keyCode);
	}

	public void setEnterItemViewListener(EnterItemViewListener enterItemViewListener) {
		this.enterItemViewListener = enterItemViewListener;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
}
