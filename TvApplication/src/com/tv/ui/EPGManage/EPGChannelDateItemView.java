package com.tv.ui.EPGManage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tv.app.TvUIControler;

@SuppressLint("ViewConstructor") 
public class EPGChannelDateItemView extends LinearLayout implements OnFocusChangeListener, OnTouchListener, OnKeyListener
{
	
	private Context mContext;
	private TextView epgitem1TextView,epgitem2TextView;
	private EPGManageView parentView;
	
	public static float div = TvUIControler.getInstance().getResolutionDiv();
	public static float dipDiv = TvUIControler.getInstance().getDipDiv();
	
	public EPGChannelDateItemView(Context context,EPGManageView parentView) {
		super(context);
        this.setOnKeyListener(this);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setEnabled(true);
        this.setOnTouchListener(this);
        this.setOnFocusChangeListener(this);
        this.setOnKeyListener(this);
        mContext = context;
        this.parentView = parentView;
        
        this.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams itemLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,(int) (120 / div));
        itemLayoutParams.setMargins(0, (int) (5 / div), 0, (int) (5 / div));
        this.setLayoutParams(itemLayoutParams);
        this.setOrientation(LinearLayout.VERTICAL);
        
        epgitem1TextView = new TextView(mContext);
        epgitem1TextView.setTextSize((int) (32 / dipDiv));
        epgitem1TextView.setTextColor(Color.GRAY);
        epgitem1TextView.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(epgitem1TextView, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        
        epgitem2TextView = new TextView(mContext);
        epgitem2TextView.setTextSize((int) (32 / dipDiv));
        epgitem2TextView.setTextColor(Color.GRAY);
        epgitem2TextView.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(epgitem2TextView, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(KeyEvent.ACTION_DOWN == event.getAction())
		{
			Log.i("gky", getClass().toString() + "-->onKey keycode is "+ keyCode);
			return parentView.onKeyDown(keyCode,event);
		}
		return false;
	}
	public void setItem1text(String str)
	{
		epgitem1TextView.setText(str);
	}
	
	public String getItem1text()
	{
		return (String) epgitem1TextView.getText();
	}
	
	public void setItem2text(String str)
	{
		epgitem2TextView.setText(str);
	}
	
	public String getItem2text()
	{
		return (String) epgitem2TextView.getText();
	}
	
	public void resetFocus()
    {
        if (this != null)
        {
            this.setFocusable(true);
            this.requestFocus();
            this.getOnFocusChangeListener().onFocusChange(this, true);
        }
    }
	
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean isFocus) {
		// TODO Auto-generated method stub
		if(isFocus)
		{
			this.setBackgroundColor(Color.parseColor("#3598DC"));
			epgitem1TextView.setTextColor(Color.WHITE);
			epgitem2TextView.setTextColor(Color.WHITE);
			epgitem1TextView.setTextSize((int) (36 / dipDiv));
			epgitem2TextView.setTextSize((int) (36 / dipDiv));
		}
		else
		{
			this.setBackgroundColor(Color.parseColor("#000000"));
			epgitem1TextView.setTextColor(Color.GRAY);
			epgitem2TextView.setTextColor(Color.GRAY);
			epgitem1TextView.setTextSize((int) (32 / dipDiv));
			epgitem2TextView.setTextSize((int) (32 / dipDiv));
		}
	}

}
