package com.tv.ui.Loading;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.ui.network.NetworkSetupDialog;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SkyLoadingView extends LinearLayout{
    private Context mContext;
    public Animation rotate; // 旋转动画
    private ImageView dot; // 转圈图片
    private String hintText = "";
    private String loadingText = "";
    private TextView text;
    private TextView content;
    private SkyLoadingDialog parentdialog;
    SkyLoadingDialog parentDialog = null;
    private LinearLayout.LayoutParams rl2 = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    LinearLayout.LayoutParams rl1 = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dpiDiv = TvUIControler.getInstance().getDipDiv();
    int content_size = 42;
    int title_size = 48;

    public SkyLoadingView(Context context)
    {
        super(context);
        mContext = context;
        createView();
    }

    public void setText(String _text)
    {
        hintText = _text;
        text.setText(hintText);
    }

    private void createView()
    {
        this.setOrientation(LinearLayout.VERTICAL);
        this.setGravity(Gravity.CENTER | Gravity.TOP);
        this.setFocusable(false);
        // 圆环背景
        LinearLayout layout0 = new LinearLayout(mContext);
        LinearLayout.LayoutParams rl0 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.addView(layout0, rl0);

        // 图片
        LinearLayout.LayoutParams rl00 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rl00.topMargin = (int) (900 / div);
        dot = new ImageView(mContext);
        dot.setBackgroundResource(R.drawable.loading);
        // 初始化旋转动画
        rotate = new RotateAnimation(0.0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setRepeatMode(Animation.RESTART);
        rotate.setRepeatCount(Animation.INFINITE);
        dot.setAnimation(rotate);
        layout0.addView(dot, rl00);
    }
    
    public void update()
    {
        dot.startAnimation(rotate);
        this.setFocusableInTouchMode(true);
        this.setFocusable(true);
        this.requestFocus();
    }

    public void setParentDialog(SkyLoadingDialog parentDialog) 
    {
		this.parentDialog = parentDialog;
	}
}
