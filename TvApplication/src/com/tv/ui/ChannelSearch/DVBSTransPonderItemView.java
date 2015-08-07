package com.tv.ui.ChannelSearch;

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
import com.tv.framework.data.TvTransponderListData;

public class DVBSTransPonderItemView extends RelativeLayout implements OnFocusChangeListener, OnTouchListener, OnKeyListener
{
	private String tag = "zhm";
	private static float div = TvUIControler.getInstance().getResolutionDiv();
    private static float dip = TvUIControler.getInstance().getDipDiv();
    private TextView numView,frequencyView,symbolRateView,polarityView;
    private Context mContext;
    private boolean isItemCheck = false;
    private boolean hasFocus = false;
    private final static int ID_CHECKVIEW= 100;
    private final static int ID_NUMTEXT= 101;
    private final static int ID_FREQUENCYTEXT= 102;
    private final static int ID_SYMBOLREATETEXT= 103;
    
    private DVBSTransPonderListView parentView;

	public DVBSTransPonderItemView(Context context, DVBSTransPonderListView dvbsTransPonderListView) {
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
        this.setOnKeyListener(this);
        this.setOnFocusChangeListener(this);
        this.parentView = dvbsTransPonderListView;
        initViews();
	}
	
	public void initViews(){
		this.setGravity(Gravity.CENTER_VERTICAL);
        
        RelativeLayout.LayoutParams numViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        numViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        numViewParams.addRule(RelativeLayout.RIGHT_OF, ID_CHECKVIEW);
        numViewParams.leftMargin = (int)(80/div);
        
        RelativeLayout.LayoutParams frequencyViewParams = new RelativeLayout.LayoutParams(
        		LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        frequencyViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        frequencyViewParams.addRule(RelativeLayout.RIGHT_OF, ID_NUMTEXT);
        frequencyViewParams.leftMargin = (int)(40/div);
        
        RelativeLayout.LayoutParams symbolRateViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        symbolRateViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        symbolRateViewParams.addRule(RelativeLayout.RIGHT_OF, ID_FREQUENCYTEXT);
        symbolRateViewParams.leftMargin = (int)(40/div);
        
        RelativeLayout.LayoutParams bandViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        bandViewParams.addRule(RelativeLayout.CENTER_VERTICAL);
        bandViewParams.addRule(RelativeLayout.RIGHT_OF, ID_SYMBOLREATETEXT);
        bandViewParams.leftMargin = (int)(40/div);
        
        numView = new TextView(mContext);
        numView.setTextColor(Color.WHITE);
        numView.setTextSize(36 / dip);
        numView.setId(ID_NUMTEXT);
        this.addView(numView, numViewParams);
        
        frequencyView = new TextView(mContext);
        frequencyView.setTextColor(Color.WHITE);
        frequencyView.setTextSize(36 / dip);
        frequencyView.setId(ID_FREQUENCYTEXT);
        frequencyView.getPaint().setAntiAlias(true);
        frequencyView.setSingleLine(true);
        frequencyView.setEllipsize(TruncateAt.MARQUEE);
        frequencyView.setMarqueeRepeatLimit(-1);
        this.addView(frequencyView, frequencyViewParams);
        
        symbolRateView = new TextView(mContext);
        symbolRateView.setTextColor(Color.WHITE);
        symbolRateView.setTextSize(36 / dip);
        symbolRateView.setId(ID_SYMBOLREATETEXT);
        symbolRateView.getPaint().setAntiAlias(true);
        symbolRateView.setSingleLine(true);
        symbolRateView.setEllipsize(TruncateAt.MARQUEE);
        symbolRateView.setMarqueeRepeatLimit(-1);        
        this.addView(symbolRateView, symbolRateViewParams);
        
        polarityView = new TextView(mContext);
        polarityView.setTextColor(Color.WHITE);
        polarityView.setTextSize(36 /dip);
        this.addView(polarityView,bandViewParams);
        
        this.setFocusable(true);
        this.setClickable(true);
	}
	
	public void updateView(TvTransponderListData data){
		
		numView.setText(data.getNumber());
		frequencyView.setText(data.getFrequency());
		symbolRateView.setText(data.getSymbolRate());
		polarityView.setText(data.getPolarType());
	}
	
	public String getNameString()
    {
        return numView.getText().toString();
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
        	numView.setTextColor(Color.BLACK);
        	frequencyView.setTextColor(Color.BLACK);
        	symbolRateView.setTextColor(Color.BLACK);
        	polarityView.setTextColor(Color.BLACK);
        } else
        {
        	numView.setTextColor(Color.WHITE);
        	frequencyView.setTextColor(Color.WHITE);
        	symbolRateView.setTextColor(Color.WHITE);
        	polarityView.setTextColor(Color.WHITE);
        }
    }

    public void setBackGroundColorUser(boolean hasFocus)
    {
        if (hasFocus)
        {
        	this.setBackgroundResource(R.drawable.yellow_sel_bg);

        	numView.setTextColor(Color.BLACK);
        	frequencyView.setTextColor(Color.BLACK);
        	symbolRateView.setTextColor(Color.BLACK);
        	polarityView.setTextColor(Color.BLACK);
        	
            hasFocus = true;
        } else
        {
        	numView.setTextColor(Color.WHITE);
        	frequencyView.setTextColor(Color.WHITE);
        	symbolRateView.setTextColor(Color.WHITE);
        	polarityView.setTextColor(Color.WHITE);
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
