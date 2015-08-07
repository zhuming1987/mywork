package com.tv.ui.ToastFocus;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvContext;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class TvToastFocusView extends LinearLayout implements OnClickListener{

	private RelativeLayout mainlayout;
    private TextView contentView;
    private Button sureButton, cancelButton;
    private String information = "";
    private final float resolution = TvUIControler.getInstance().getResolutionDiv();
    private float disDiv = TvUIControler.getInstance().getDipDiv();
    private int contentViewWidth = (int) (670 / resolution);
    private int contentViewHeigth = (int) (180 / resolution);
    private int buttonWith = (int) (241 / resolution);
    private int buttonHeigth = (int) (60 / resolution);
    private  int width = (int) (1170/resolution);
    private  int height = (int) (600/resolution);
    public static final int SUREBUTTONID = 1;
    public static final int CANCELBUTTONID = 2;
    private TextView t_tips;
    
    private OnBtClickListener onBtClickListener = null;
    private TvToastFocusDialog parentDialog = null;
    
	public TvToastFocusView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setBackgroundResource(R.drawable.dialog_bg);
		this.setLayoutParams(new LayoutParams(width, height));
		this.setFocusable(true);
        this.requestFocus();
        this.setFocusableInTouchMode(true);
        
        mainlayout = new RelativeLayout(context);
        LinearLayout.LayoutParams mainLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        RelativeLayout contentLayout = new RelativeLayout(context);
        contentLayout.setId(1001);
        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
                contentViewWidth, contentViewHeigth);
        contentParams.topMargin = (int) (50 / resolution);
        contentParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        contentView = new TextView(context);
        contentView.setTextColor(Color.WHITE);
        contentView.setText(information);
        contentView.setTextSize(45 / disDiv);
        contentView.setGravity(Gravity.CENTER);//DEBUG

        RelativeLayout.LayoutParams contentViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        contentViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        contentLayout.addView(contentView, contentViewParams);
        mainlayout.addView(contentLayout, contentParams);

        LinearLayout buttomLayout = new LinearLayout(context);

        LinearLayout.LayoutParams sureParams = new LinearLayout.LayoutParams(buttonWith,
                buttonHeigth);
        sureParams.rightMargin = (int) (60 / resolution);
        sureButton = new Button(context);
        sureButton.setTextColor(Color.WHITE);
        sureButton.setBackgroundResource(R.drawable.dialog_button);
        sureButton.setId(SUREBUTTONID);
        sureButton.setTextSize(35 / disDiv);
        sureButton.setOnClickListener(this);
        sureButton.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                // TODO Auto-generated method stub
            	if (KeyEvent.ACTION_UP == event.getAction())
                	return false;
                if(keyCode == KeyEvent.KEYCODE_BACK)
                {
                	if(onBtClickListener != null)
        				onBtClickListener.onClickListener(false);
                	parentDialog.dismissUI();
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT||keyCode == KeyEvent.KEYCODE_DPAD_RIGHT||
                		keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
                {
                	Log.v("yangcheng", "KEYCODE_DPAD_LEFT||KEYCODE_DPAD_CENTER");
                	return false;
                }
                return true;
            }
        });
        buttomLayout.addView(sureButton, sureParams);

        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(buttonWith,
                buttonHeigth);
        cancelButton = new Button(context);
        cancelButton.setTextColor(Color.WHITE);
        cancelButton.setBackgroundResource(R.drawable.dialog_button);
        cancelButton.setId(CANCELBUTTONID);
        cancelButton.setTextSize(35 / disDiv);
        cancelButton.setOnClickListener(this);
        cancelButton.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                // TODO Auto-generated method stub
                if (KeyEvent.ACTION_UP == event.getAction())
                	return false;
                if(keyCode == KeyEvent.KEYCODE_BACK){
                	if(onBtClickListener != null)
        				onBtClickListener.onClickListener(false);
                	parentDialog.dismissUI();
                }
                if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT||keyCode == KeyEvent.KEYCODE_DPAD_RIGHT||
                		keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
                {
                	Log.v("yangcheng", "KEYCODE_DPAD_LEFT||KEYCODE_DPAD_CENTER");
                	return false;
                }
                return true;
            }
        });
        buttomLayout.addView(cancelButton, cancelParams);

        RelativeLayout.LayoutParams buttomParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        buttomParams.topMargin = (int) (40 / resolution);
        buttomParams.addRule(RelativeLayout.BELOW, contentLayout.getId());
        buttomParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        mainlayout.addView(buttomLayout, buttomParams);

        mainLayoutParams.gravity = Gravity.CENTER;
        t_tips = new TextView(context);
        RelativeLayout.LayoutParams tipslp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        tipslp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        t_tips.setTextColor(Color.GRAY);
        t_tips.setGravity(Gravity.RIGHT);
        t_tips.setTextSize(24/disDiv);
        
        mainlayout.addView(t_tips,tipslp);
        this.addView(mainlayout, mainLayoutParams);
               
        cancelButton.setFocusable(true);
        cancelButton.setFocusableInTouchMode(true);
        sureButton.setFocusable(true);
        sureButton.setFocusableInTouchMode(true);
        sureButton.requestFocus();
	}

	public void updateView(TvToastFocusData data){
		if(data.getBtnNum()==1)
		{
            cancelButton.setVisibility(View.GONE);
        }
        else
        {
            cancelButton.setVisibility(View.VISIBLE);
        }
		sureButton.setText(TvContext.context.getResources().getString(R.string.button_ok_str));
		cancelButton.setText(TvContext.context.getResources().getString(R.string.button_cancel_str));
		contentView.setText(data.getContent());
		t_tips.setText(data.getTips());
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case SUREBUTTONID:
			if(onBtClickListener != null)
				onBtClickListener.onClickListener(true);
			break;
		case CANCELBUTTONID:
			if(onBtClickListener != null)
				onBtClickListener.onClickListener(false);
			break;
		}
		parentDialog.dismissUI();
	}

	public void setParentDialog(TvToastFocusDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	public void setOnBtClickListener(OnBtClickListener onBtClickListener) {
		this.onBtClickListener = onBtClickListener;
	}

	public interface OnBtClickListener
	{
		public void onClickListener(boolean flag);
	}
}
