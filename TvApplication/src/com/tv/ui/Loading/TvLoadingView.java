package com.tv.ui.Loading;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

public class TvLoadingView extends LinearLayout{

	private LinearLayout contentLayout;
	private TextView tipTextView;
    private TextView contentView;
    private ImageView loadingImage;
    private Animation rotateAnimation;
    
    private final float resolution = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();
    
    private  int width = (int) (1170/resolution);
    private  int height = (int) (600/resolution);
    
	public TvLoadingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		//this.setBackgroundResource(R.drawable.dialog_bg);
		this.setLayoutParams(new LayoutParams(width, height));
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.requestFocus();
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER);
        
        LinearLayout loadingLayout = new LinearLayout(context);
        LinearLayout.LayoutParams loadParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        loadParams.rightMargin = (int)(30/resolution);
        this.addView(loadingLayout,loadParams);
        loadingImage = new ImageView(mContext);
        loadingImage.setBackgroundResource(R.drawable.loading);
        rotateAnimation = new RotateAnimation(0.0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        loadingImage.setAnimation(rotateAnimation);
        loadingLayout.addView(loadingImage);
        
        contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        this.addView(contentLayout, contentParams);
        tipTextView = new TextView(context);
        tipTextView.setTextSize((int)(36/dipDiv));
        tipTextView.setTextColor(Color.WHITE);
        contentLayout.addView(tipTextView);
        contentView = new TextView(context);
        contentView.setTextSize((int)(36/dipDiv));
        contentView.setTextColor(Color.WHITE);
        contentView.setPadding(0, (int)(15/resolution), 0, (int)(15/resolution));
        contentLayout.addView(contentView);
	}

	public void updateView(String tip, String content){
		if(tip.equals("") && content.equals("")){
			contentLayout.setVisibility(View.GONE);
		}else {
			contentView.setText(content);
			tipTextView.setText(tip);
		}
		loadingImage.startAnimation(rotateAnimation);
	}
	
	public void stopLoading(){
		rotateAnimation.cancel();
	}
}
