package com.tv.ui.ChannelSearch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.ui.LNBSetting.SatelliteItemView;

@SuppressLint("ViewConstructor")
public class DVBSSatelliteItemView  extends RelativeLayout implements OnFocusChangeListener, OnTouchListener, OnKeyListener
{
	private String tag = "zhm";
    private static float div = TvUIControler.getInstance().getResolutionDiv();
    private static float dip = TvUIControler.getInstance().getDipDiv();
    private TextView numView,nameView,bandView;
    private Context mContext;
    private boolean isItemCheck = false;
    private boolean hasFocus = false;
    private final static int ID_CHECKVIEW= 100;
    private final static int ID_NUMTEXT= 101;
    private final static int ID_NAMETEXT= 102;
    
    private DVBSSatelliteListView parentView;

    public DVBSSatelliteItemView(Context context, DVBSSatelliteListView parentView)
    {
        super(context);
        mContext = context;
        this.setOnKeyListener(this);
        this.setOnFocusChangeListener(this);
        this.parentView = parentView;
        initViews();
    }

    private void initViews()
    {
        this.setGravity(Gravity.CENTER_VERTICAL);
        
        RelativeLayout.LayoutParams numViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        numViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        numViewParams.addRule(RelativeLayout.RIGHT_OF, ID_CHECKVIEW);
        numViewParams.leftMargin = (int)(40/div);
        
        RelativeLayout.LayoutParams nameViewParams = new RelativeLayout.LayoutParams(
        		(int)(470/div), LayoutParams.WRAP_CONTENT);
        nameViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        nameViewParams.addRule(RelativeLayout.RIGHT_OF, ID_NUMTEXT);
        nameViewParams.leftMargin = (int)(40/div);
        
        RelativeLayout.LayoutParams bandViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        bandViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        bandViewParams.addRule(RelativeLayout.RIGHT_OF, ID_NAMETEXT);
        
        numView = new TextView(mContext);
        numView.setTextColor(Color.WHITE);
        numView.setTextSize(36 / dip);
        numView.setId(ID_NUMTEXT);
        this.addView(numView, numViewParams);
        
        nameView = new TextView(mContext);
        nameView.setTextColor(Color.WHITE);
        nameView.setTextSize(36 / dip);
        nameView.setId(ID_NAMETEXT);
        nameView.getPaint().setAntiAlias(true);
        nameView.setSingleLine(true);
        nameView.setEllipsize(TruncateAt.MARQUEE);
        nameView.setMarqueeRepeatLimit(-1);
        this.addView(nameView, nameViewParams);
        
        bandView = new TextView(mContext);
        bandView.setTextColor(Color.WHITE);
        bandView.setTextSize(36 / dip);
        this.addView(bandView, bandViewParams);
        this.setFocusable(true);
        this.setClickable(true);
    }

    public void update(String textStr,String textStr2,String textStr3)
    {
        numView.setText(textStr);
        nameView.setText(textStr2);
        bandView.setText(textStr3);
    }

    public String getNameString()
    {
        return nameView.getText().toString();
    }
    
    public void setChecked(boolean isChecked)
    {
        isItemCheck = isChecked;
        upDateRadioImage();
    }

    public void setFocused(boolean isFoucused)
    {
        hasFocus = isFoucused;
    }

    public boolean getFocused()
    {
        return hasFocus;
    }

    public boolean getchecked()
    {
        return isItemCheck;
    }

    private void upDateRadioImage()
    {
        if (hasFocus)
        {
            nameView.setTextColor(Color.BLACK);
            numView.setTextColor(Color.BLACK);
            bandView.setTextColor(Color.BLACK);
        } else
        {
            nameView.setTextColor(Color.WHITE);
            numView.setTextColor(Color.WHITE);
            bandView.setTextColor(Color.WHITE);
        }
    }

    public void setBackGroundColorUser(boolean hasFocus)
    {
        if (hasFocus)
        {
        	this.setBackgroundResource(R.drawable.yellow_sel_bg);

        	nameView.setTextColor(Color.BLACK);
            numView.setTextColor(Color.BLACK);
            bandView.setTextColor(Color.BLACK);
        	
            hasFocus = true;
        } else
        {
        	nameView.setTextColor(Color.WHITE);
            numView.setTextColor(Color.WHITE);
            bandView.setTextColor(Color.WHITE);
            this.setBackgroundDrawable(null);
            
            hasFocus = false;
        }
    }
    
    @Override
	public boolean onKey(View view, int keyCode, KeyEvent event) 
    {
		// TODO Auto-generated method stub
    	parentView.onKeyDown(keyCode, event);
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	 public void setItemSelectFocusChange()
	 {
		 this.setOnFocusChangeListener(new OnFocusChangeListener() 
		 {
			public void onFocusChange(View view, boolean hasFocus) 
			{
				((DVBSSatelliteItemView) view).setBackGroundColorUser(hasFocus);
			}
		 });
	 }
	 
	public void checkedSelectedItem(int id) 
	{
		boolean isChecked = this.getchecked();
		Log.v(tag, "view.getId() = " + this.getId());
		Log.v(tag, "id = " + id);
		if (this.getId() == id) 
		{
			if (!isChecked) 
			{
				this.setChecked(true);
			}
		} 
		else 
		{
			this.setChecked(false);
		}
	}
}