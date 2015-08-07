package com.tv.ui.SettingView;

import com.tv.app.R;
import com.tv.data.MenuItem;
import com.tv.framework.data.TvRangeSetData;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class TvProgressItemView extends TvSettingItemView{

	private Context mContext;
	private LinearLayout txtLayout;
    private LinearLayout progressLayout;
    private FrameLayout progressFrame;
    private SeekBar progressBar;
    private TextView progressText;
    private int progress = 0;
    private int min = 0, max = 100;
    private Drawable bar;
    private Drawable barBg;
    private Drawable unsel_bar;
    private Drawable unsel_barBg;
    LayerDrawable unsel_seekbarBg;
    LayerDrawable seekbarBg;
    
    public final int FONT_SIZE = 24;
    public final int progressLayoutWidth = 526;
    public final int progressLayoutHeight = 50;
    public LinearLayout.LayoutParams barLp;
    private boolean hasFocus = false;
    
	public TvProgressItemView(Context context, int index, TvSettingView parentView, OnSettingKeyDownListener keyListener) {
		super(context, index, parentView, keyListener);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		
		bar = getResources().getDrawable(R.drawable.process_sel);
        barBg = getResources().getDrawable(R.drawable.process_sel);
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
        progressFrame.setLayoutParams(new LayoutParams((int) (progressLayoutWidth/resolution),
        		LayoutParams.WRAP_CONTENT));

        txtLayout = new LinearLayout(mContext);
        txtLayout.setBackgroundResource(R.drawable.process_unsel_scroll);
        //LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams((int) (50 / resolution),
        //		(int) (50 / resolution));
        txtLayout.setGravity(Gravity.CENTER);

        progressText = new TextView(mContext);
        progressText.setTextSize(FONT_SIZE/dipDiv);
        progressText.setTextColor(Color.WHITE);
        progressText.setGravity(Gravity.CENTER);
        progressText.setText(progress + "");
        txtLayout.addView(progressText);

        progressLayout = new LinearLayout(context);
        //LayoutParams layoutLp = new LayoutParams((int) (progressLayoutWidth/resolution), (int)(progressLayoutHeight/resolution));
        progressLayout.setGravity(Gravity.CENTER);

        progressBar = new SeekBar(mContext);
        progressBar.setBackground(unsel_bar);
        //progressBar.setBackgroundColor(Color.YELLOW);
        progressBar.setProgressDrawable(null);

        progressBar.setThumb(getResources().getDrawable(R.drawable.transparent));
        if(resolution == 1.5f)
	        barLp = new LinearLayout.LayoutParams((int) ((progressLayoutWidth -50) / resolution),
	        		(int) (70 / resolution));
        else
        	barLp = new LinearLayout.LayoutParams((int) ((progressLayoutWidth -50) / resolution),
	        		(int) (50 / resolution));
        progressBar.setThumbOffset(0);
        progressBar.setFocusable(false);
        progressLayout.addView(progressBar, barLp);
        progressFrame.addView(progressLayout, new FrameLayout.LayoutParams((int) (progressLayoutWidth/resolution), (int)(progressLayoutHeight/resolution), Gravity.CENTER));
        progressFrame.addView(txtLayout, new LinearLayout.LayoutParams((int)(53/resolution), (int)(53/resolution), Gravity.CENTER));
        progressFrame.setPadding(0, (int)(5/resolution), 0, 0);
        viewLayout.addView(progressFrame);
		
	}

	
	@Override
	public void updateView(MenuItem data) {
		// TODO Auto-generated method stub
		super.updateView(data);
		TvRangeSetData rangData = (TvRangeSetData)data.getItemData(); 
        min = rangData.getMin();
        max = rangData.getMax();
        progress = rangData.getCurrent();
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
		bodyLayout.setFocusable(isEnabled);
	}
	
	@Override
	public void updateBgDrawable(boolean parentHasFocus) {
		// TODO Auto-generated method stub
		hasFocus = parentHasFocus;
		Log.i("gky", getClass().toString() + " updateBgDrawable hasFocus " + hasFocus);
		if (parentHasFocus) {
			progressText.setTextColor(Color.BLACK);
			txtLayout.setBackgroundResource(R.drawable.process_sel_scroll);
			progressBar.setBackground(bar);
			progressBar.setProgressDrawable(null);
		} else {
			if (isEnabled) {
				progressText.setTextColor(Color.WHITE);
			} else {
				progressText.setTextColor(Color.DKGRAY);
			}
			txtLayout.setBackgroundResource(R.drawable.process_unsel_scroll);
			progressBar.setBackground(unsel_bar);
			progressBar.setProgressDrawable(null);
		}
		progressBar.setProgress(progress - 1 - min);
		progressBar.setProgress(progress - min);
	}

	@Override
	public boolean executeKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_UP){
			//OnExecuteSet(progress);
			return false;
		}
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_LEFT:
				if (progress > min) {
					progress--;
					refreshUI();
					OnExecuteSet(progress);
				}
				return true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				if (progress < max) {
					progress++;
					refreshUI();
					OnExecuteSet(progress);
				}
				return true;
		}
		return false;
	}

	@Override
	public void refreshUI() {
		// TODO Auto-generated method stub
		progressBar.setProgress(progress - min);
        progressText.setText(progress + "");
        setTextPos(progress);
	}

	public void setTextPos(int pos)
    {
//		progressText.setText(pos + "");
//		if (hasFocus) {
//			progressText.setTextColor(Color.BLACK);
//		} else {
//			if (isEnabled) {
//				progressText.setTextColor(Color.WHITE);
//			} else {
//				progressText.setTextColor(Color.DKGRAY);
//			}
//		}
		float x = (int) (25 / resolution) + (pos - min)
				* ((int) (425 / resolution) / (float) (progressBar.getMax()));
		txtLayout.setX(x);
		invalidate();
    }

	
}
