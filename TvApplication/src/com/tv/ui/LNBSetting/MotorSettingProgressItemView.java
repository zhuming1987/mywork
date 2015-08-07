package com.tv.ui.LNBSetting;

import java.math.BigDecimal;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View.OnKeyListener;
import android.view.View.OnFocusChangeListener;

public class MotorSettingProgressItemView extends LinearLayout implements OnKeyListener, OnFocusChangeListener{

	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
	private TextView titleTextView;
	private LinearLayout bodyLayout;
    private SeekBar progressBar;
    private TextView progressText;
    private int progress = 0;
    private int min = 0, max = 100;
    private boolean isEnabled = true;
   

    private ProgressItemListener pItemListener;
    
	public MotorSettingProgressItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setOnKeyListener(this);
		this.setOnFocusChangeListener(this);
		this.setLayoutParams(new LayoutParams((int)(845/div), (int)(80/div)));
		this.setPadding((int)(15/div), (int)(5/div), 0, (int)(5/div));
		this.setGravity(Gravity.CENTER_VERTICAL);
		
		titleTextView = new TextView(context);
		titleTextView.setTextColor(Color.WHITE);
		titleTextView.setTextSize((float)(36/dip));
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		titleTextView.setSingleLine(true);
		titleTextView.setEllipsize(TruncateAt.MARQUEE);
		titleTextView.setMarqueeRepeatLimit(-1);
		LayoutParams titletxtParams = new LayoutParams((int)(200/div), LayoutParams.MATCH_PARENT);
		titleTextView.setLayoutParams(titletxtParams);
		this.addView(titleTextView);
		
		bodyLayout = new LinearLayout(context);
		bodyLayout.setGravity(Gravity.CENTER_VERTICAL);
		bodyLayout.setBackground(null);
        LayoutParams bodyParams = new LayoutParams((int)(526/div), (int)(60/div));
        bodyParams.leftMargin = (int)(80/div);
        bodyParams.gravity = Gravity.CENTER;
        bodyLayout.setLayoutParams(bodyParams);
        this.addView(bodyLayout);
        
        progressBar = new SeekBar(context);
        progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.seekbar_bg));
        progressBar.setMinimumHeight((int)(10/div));
        progressBar.setThumb(context.getResources().getDrawable(R.drawable.transparent));
        LayoutParams progressParams = new LayoutParams((int)(400/div), (int)(10/div));
        progressParams.setMargins((int)(5/div), 0, 0, 0);
        progressBar.setLayoutParams(progressParams);
        bodyLayout.addView(progressBar);
        
        progressText = new TextView(context);
        progressText.setTextColor(Color.WHITE);
        progressText.setTextSize((float)(36/dip));
        progressText.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        progressText.setSingleLine(true);
        progressText.setEllipsize(TruncateAt.MARQUEE);
        progressText.setMarqueeRepeatLimit(-1);
		LayoutParams progressTxtParams = new LayoutParams((int)(120/div), (int)(100/div));
		progressText.setLayoutParams(progressTxtParams);
		bodyLayout.addView(progressText);
		
	}
	
	public void updateView(String title, int cur, int min, int max){
		titleTextView.setText(title);
		this.min = min;
		this.max = max;
		progressBar.setMax(max);
		this.progress = cur;
		progressBar.setProgress(cur);
		progressText.setText(new BigDecimal(progress+"").multiply(new BigDecimal("0.1")).toString());
		
		if(isEnabled){
			this.setFocusable(true);
			this.setFocusableInTouchMode(true);
		}else {
			this.setFocusable(false);
			this.setFocusableInTouchMode(false);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if(hasFocus){
			titleTextView.setTextColor(Color.parseColor("#3598DC"));
			titleTextView.setSelected(true);
			progressText.setTextColor(Color.parseColor("#3598DC"));
		}else {
			titleTextView.setTextColor(Color.WHITE);
			titleTextView.setSelected(false);
			if(isEnabled){
				progressText.setTextColor(Color.WHITE);
			}
			else {
				progressText.setTextColor(Color.GRAY);
			}
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_UP)
			return false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(progress > min){
				progress--;
				progressBar.setProgress(progress);
				progressText.setText(new BigDecimal(progress+"").multiply(new BigDecimal("0.1")).toString());
				if(pItemListener != null)
					pItemListener.OnProgressItemChangeListener(this, progress, keyCode);
			}
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(progress < max){
				progress++;
				progressBar.setProgress(progress);
				new BigDecimal(progress+"").multiply(new BigDecimal("0.1"));
				progressText.setText(new BigDecimal(progress+"").multiply(new BigDecimal("0.1")).toString());
				if(pItemListener != null)
					pItemListener.OnProgressItemChangeListener(this, progress, keyCode);
			}
			return true;
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_BACK:
			if(pItemListener != null)
				pItemListener.OnProgressItemChangeListener(this, progress, keyCode);
			return true;
		default:
			break;
		}
		return false;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		this.setFocusable(isEnabled);
		this.setFocusableInTouchMode(isEnabled);
		progressBar.setFocusable(isEnabled);
		progressBar.setFocusableInTouchMode(isEnabled);
	}
	
	public interface ProgressItemListener{
		
		public void OnProgressItemChangeListener(View view, int value, int keyCode);
	}

	public void setItemChangeListener(ProgressItemListener pItemListener) {
		this.pItemListener = pItemListener;
	}

}
