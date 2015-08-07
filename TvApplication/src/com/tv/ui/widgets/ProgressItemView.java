package com.tv.ui.widgets;

import com.tv.app.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View.OnTouchListener;
import android.view.View.OnKeyListener;
import android.view.View.OnFocusChangeListener;

public class ProgressItemView extends LinearLayout implements OnTouchListener, OnKeyListener, OnFocusChangeListener{

	private final float resolution = (float)1.0;
	private LinearLayout txtLayout;
    private LinearLayout progressLayout;
    private FrameLayout progressFrame;
    private SeekBar progressBar;
    private TextView progressText;
    private int progress = 0;
    private int min = 0, max = 100;
    private Drawable bar;
    private Drawable unsel_bar;
    private Drawable unsel_barBg;
    LayerDrawable unsel_seekbarBg;
    LayerDrawable seekbarBg;
    
    public final int FONT_SIZE = 24;
    public  int progressLayoutWidth = 526;
    public  int progressLayoutHeight = 50;
    public LinearLayout.LayoutParams barLp;
    private boolean hasFocus = false;
    private boolean isEnabled = true;
    
    private ProgressItemListener pItemListener;
    
	@SuppressWarnings("deprecation")
	public ProgressItemView(Context context, int width, int height) {
		super(context);
		this.setFocusable(true);
		this.setOnKeyListener(this);
		this.setOnFocusChangeListener(this);
		this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.progressLayoutWidth = width;
		this.progressLayoutHeight = height;
		
		bar = getResources().getDrawable(R.drawable.process_sel);
        getResources().getDrawable(R.drawable.process_sel);
        unsel_bar = getResources().getDrawable(R.drawable.process_unsel_gray);
        unsel_barBg = getResources().getDrawable(R.drawable.process_unsel);

        Drawable[] layers = new Drawable[2];
        layers[1] = new ClipDrawable(bar, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        layers[0] = unsel_barBg;
        seekbarBg = new LayerDrawable(layers);

        Drawable[] unsellayers = new Drawable[2];
        unsellayers[1] = new ClipDrawable(unsel_bar, Gravity.LEFT, ClipDrawable.HORIZONTAL);
        unsellayers[0] = unsel_barBg;
        unsel_seekbarBg = new LayerDrawable(unsellayers);
        
        progressFrame = new FrameLayout(context);
        progressFrame.setLayoutParams(new LayoutParams((int) (progressLayoutWidth),
        		LayoutParams.WRAP_CONTENT));

        txtLayout = new LinearLayout(context);
        txtLayout.setBackgroundResource(R.drawable.process_unsel_scroll);
        txtLayout.setGravity(Gravity.CENTER);

        progressText = new TextView(context);
        progressText.setTextSize(FONT_SIZE);
        progressText.setTextColor(Color.WHITE);
        progressText.setGravity(Gravity.CENTER);
        progressText.setText(progress + "");
        txtLayout.addView(progressText);

        progressLayout = new LinearLayout(context);
        progressLayout.setGravity(Gravity.CENTER);

        progressBar = new SeekBar(context);
        progressBar.setBackgroundDrawable(unsel_bar);
        progressBar.setProgressDrawable(null);

        progressBar.setThumb(getResources().getDrawable(R.drawable.transparent));
        barLp = new LinearLayout.LayoutParams((int) (progressLayoutWidth - 50 / resolution),
                LayoutParams.WRAP_CONTENT);
        progressBar.setThumbOffset(0);
        progressBar.setFocusable(false);
        progressBar.setOnTouchListener(this);
        progressLayout.addView(progressBar, barLp);
        progressFrame.addView(progressLayout, new FrameLayout.LayoutParams(progressLayoutWidth, LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        progressFrame.addView(txtLayout, new LinearLayout.LayoutParams(50, 50, Gravity.CENTER));
        progressFrame.setPadding(0, 5, 0, 5);
        this.addView(progressFrame);
	}
	
	public void setBindData(int min, int max, int curValue){
		this.min = min;
		this.max = max;
		this.progress = curValue;
		progressBar.setMax(max - min);
        refreshUI();
		if (isEnabled) {
			if (hasFocus) {
				progressText.setTextColor(Color.BLACK);
			} else {
				progressText.setTextColor(Color.WHITE);
			}
			this.setFocusable(true);
		} else {
			progressText.setTextColor(Color.DKGRAY);
			this.setFocusable(false);
		}
		this.setFocusable(isEnabled);
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_UP)
			return false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if (progress > min) {
				progress--;
				refreshUI();
				pItemListener.OnProgressItemChangeListener(progress);
			}
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (progress < max) {
				progress++;
				refreshUI();
				pItemListener.OnProgressItemChangeListener(progress);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("gky", "process=====" + progressBar.getProgress());
		progress = progressBar.getProgress();
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            pItemListener.OnProgressItemChangeListener(progress);
            return true;
        }
        this.requestFocus();  
        setTextPos(progressBar.getProgress());
        pItemListener.OnProgressItemChangeListener(progress);
        return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		this.hasFocus = hasFocus;
		Log.i("gky", "updateBgDrawable hasFocus " + hasFocus);
		if (hasFocus) {
			progressText.setTextColor(Color.BLACK);
			txtLayout.setBackgroundResource(R.drawable.process_sel_scroll);
			progressBar.setBackgroundDrawable(bar);
			progressBar.setProgressDrawable(null);
			Log.d("gky", "progressBar1 onfocus y=" + progressBar.getY());
			Log.d("gky", "progressBar1 onfocus h=" + progressBar.getHeight());
		} else {
			if (isEnabled) {
				progressText.setTextColor(Color.WHITE);
			} else {
				progressText.setTextColor(Color.DKGRAY);
			}
			txtLayout.setBackgroundResource(R.drawable.process_unsel_scroll);
			progressBar.setBackgroundDrawable(unsel_bar);
			progressBar.setProgressDrawable(null);
			Log.d("gky", "progressBar2 nofocus y=" + progressBar.getY());
			Log.d("gky", "progressBar2 nofocus h=" + progressBar.getHeight());
		}
		// 第一次不刷新黄色进度
		progressBar.setProgress(progress - 1 - min);
		progressBar.setProgress(progress - min);
	}
	
	public void refreshUI() {
		// TODO Auto-generated method stub
		progressBar.setProgress(progress - min);
        progressText.setText(progress + "");
        setTextPos(progress);
	}
	
	public void setTextPos(int pos)
    {
		progressText.setText(pos + "");
		if (hasFocus) {
			progressText.setTextColor(Color.BLACK);
		} else {
			if (isEnabled) {
				progressText.setTextColor(Color.WHITE);
			} else {
				progressText.setTextColor(Color.DKGRAY);
			}
		}
		float x = (int) (25 / resolution) + (pos - min)
				* ((int) (425 / resolution) / (float) (progressBar.getMax()));
		txtLayout.setX(x);
		invalidate();
    }
	
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public void setItemListener(ProgressItemListener pItemListener) {
		this.pItemListener = pItemListener;
	}

	/**
	 * 
	 * @author gky
	 *
	 */
	public interface ProgressItemListener{
		/**
		 * 
		 * @param value
		 */
		public void OnProgressItemChangeListener(int value);
	}

	

	
}
