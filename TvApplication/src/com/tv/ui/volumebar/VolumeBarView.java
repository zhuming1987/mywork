package com.tv.ui.volumebar;

import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tv.app.R;
import com.tv.plugin.TvPluginControler;

public class VolumeBarView extends LinearLayout implements View.OnKeyListener, SeekBar.OnSeekBarChangeListener
{
    private Context mContext;
    private ImageView voiceIcon;
    private TextView currentVoiceText; // 当前数值
    private SeekBar voiceBar;
    
    private VolumeBarDialog parentDialog;
    
    private float resolution = TvUIControler.getInstance().getResolutionDiv();
    private float dip = TvUIControler.getInstance().getDipDiv();

    int BAR_WIDTH = (int) (820 / resolution);
    int BAR_HEIGHT = (int) (85 / resolution);
    int LINE_WIDTH = (int) (1100 / resolution);
    int LINE_HEIGHT = (int) (150 / resolution);
    int TEXTSIZE = (int) (50 / dip);
    
    private int minVal = 0;
    private int maxVal = 100;
    private static int curVal = 50;
    private Drawable bar;
    private Drawable barBg;
    private LayerDrawable seekbarBg;

    private LinearLayout mVoiceLayout = null; //加入此layout 将音量条移入

    public VolumeBarView(Context context)
    {
        super(context);
        mContext = context;
        createView();
    }

    private void createView()
    {
    	if(resolution == 1.0f){
    		BAR_WIDTH = (int) (565 / resolution);
    		BAR_HEIGHT = (int) (56 / resolution);
    	    LINE_WIDTH = (int) (836 / resolution);
    	    LINE_HEIGHT = (int) (111 / resolution);
    	}
    	
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);
        mVoiceLayout = new LinearLayout(mContext);
        mVoiceLayout.setGravity(Gravity.CENTER_VERTICAL);
        mVoiceLayout.setOrientation(LinearLayout.HORIZONTAL);
        mVoiceLayout.setLayoutParams(new LinearLayout.LayoutParams(LINE_WIDTH, LINE_HEIGHT));
        mVoiceLayout.setBackgroundResource(R.drawable.short_cut_voice_bar_bg);
        
        bar = getContext().getResources().getDrawable(R.drawable.short_cut_voice_bar);
        barBg = getContext().getResources().getDrawable(R.drawable.short_cut_voice_bar_bg);

        // icon
        voiceIcon = new ImageView(mContext);
        LinearLayout.LayoutParams voiceIconLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        voiceIconLp.setMargins((int) (30 / resolution), 0, (int) (15 / resolution), 0);
        voiceIcon.setImageResource(R.drawable.short_cut_voice_icon);

        // value
        currentVoiceText = new TextView(mContext);
        LinearLayout.LayoutParams curTxtLp = new LinearLayout.LayoutParams((int) (95 / resolution),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        curTxtLp.setMargins(0, 0, (int) (15 / resolution), 0);
        currentVoiceText.setTextSize(TEXTSIZE);
        currentVoiceText.setTextColor(Color.WHITE);
        currentVoiceText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        currentVoiceText.setText(curVal + "");
        TextPaint tp = currentVoiceText.getPaint();
        tp.setFakeBoldText(true); // 字体加粗

        // seekbar
        voiceBar = new SeekBar(mContext);
        LinearLayout.LayoutParams voiceBarLp = new LinearLayout.LayoutParams(BAR_WIDTH, BAR_HEIGHT);
        voiceBarLp.leftMargin = (int)(10/resolution);
        Drawable[] layers = new Drawable[2]; // 初始化音量bar
        layers[1] = new ClipDrawable(bar, Gravity.START, ClipDrawable.HORIZONTAL);
        layers[0] = barBg;
        seekbarBg = new LayerDrawable(layers);
        voiceBar.setProgressDrawable(seekbarBg);
        Drawable thumb = getContext().getResources().getDrawable(R.drawable.short_cut_voice_1);
        Matrix matrix = new Matrix(); 
        matrix.postScale(0.8f,0.8f);
        Bitmap bitmap = ((BitmapDrawable)thumb).getBitmap();
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        if(resolution == 1.0f){
        	voiceBar.setThumb(thumb);
        }else {
        	voiceBar.setThumb(new BitmapDrawable(resizeBmp));
		}
        voiceBar.setThumbOffset(0);
        voiceBar.setFocusable(true);
        voiceBar.requestFocus();
        voiceBar.setOnKeyListener(this);
        voiceBar.setOnSeekBarChangeListener(this);
        mVoiceLayout.addView(voiceIcon, voiceIconLp);
        mVoiceLayout.addView(currentVoiceText, curTxtLp);
        mVoiceLayout.addView(voiceBar, voiceBarLp);
        this.addView(mVoiceLayout);        
    }

    public void updateView()
    {
    	curVal = TvPluginControler.getInstance().getCommonManager().getVolume(mContext);
        currentVoiceText.setText(String.valueOf(curVal));
        voiceBar.setMax(maxVal);
        voiceBar.setProgress(curVal);
        if (curVal == 0)
        {
            voiceBar.setThumb(getContext().getResources().getDrawable(R.drawable.short_cut_voice_0));
        } else
        {
            voiceBar.setThumb(getContext().getResources().getDrawable(R.drawable.short_cut_voice_1));
        }
        voiceIcon.setImageResource(R.drawable.short_cut_voice_icon);
    }

    private void refreshUI()
    {
        currentVoiceText.setText(String.valueOf(curVal));
        voiceBar.setProgress(curVal);
        if (curVal == 0)
        {
            voiceBar.setThumb(getContext().getResources().getDrawable(R.drawable.short_cut_voice_0));
        } else
        {
            voiceBar.setThumb(getContext().getResources().getDrawable(R.drawable.short_cut_voice_1));
        }
    }

    public void setVolume(int keycode){
    	if(keycode == KeyEvent.KEYCODE_VOLUME_DOWN){
    		curVal--;
        	if(curVal >= minVal){
        		TvPluginControler.getInstance().getCommonManager().setVolume(mContext, curVal);
        		refreshUI();
        		parentDialog.reMoveTimeOut();
        	}else {
				curVal = minVal;
			}
    	}else if(keycode == KeyEvent.KEYCODE_VOLUME_UP){
    		curVal++;
    		if(curVal <= maxVal){
    			TvPluginControler.getInstance().getCommonManager().setVolume(mContext, curVal);
    			refreshUI();
    			parentDialog.reMoveTimeOut();
    		}else {
				curVal = maxVal;
			}
    	}   	
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
        	if(keyCode == KeyEvent.KEYCODE_BACK){
        		parentDialog.dismissUI();
        	}
        }
        return false;
    }

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

    public void setParentDialog(VolumeBarDialog parentDialog){
    	this.parentDialog = parentDialog;
    }
}
