package com.tv.ui.LNBSetting;

import com.tv.app.R;
import com.tv.app.TvUIControler;

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
import android.widget.RelativeLayout.LayoutParams;

public class SetupItemView extends LinearLayout implements OnFocusChangeListener, OnTouchListener, OnKeyListener
{
    private static float div = TvUIControler.getInstance().getResolutionDiv();
    private static float dip = TvUIControler.getInstance().getDipDiv();
	private Context mContext;
	private TextView mtextView;
    private boolean hasFocus = false;
    private int listNum = -1;
	
	private LNBSettingView parentView;
	
	public SetupItemView(Context context, LNBSettingView parentView) {
		super(context);
		// TODO Auto-generated constructor stub
        mContext = context;
        this.setOnKeyListener(this);
        this.setOnFocusChangeListener(this);
        this.parentView = parentView;
        
        initViews();
	}
	
    private void initViews()
    {
    	this.setGravity(Gravity.CENTER_VERTICAL);
    	
    	LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
    			LayoutParams.MATCH_PARENT,
    			LayoutParams.WRAP_CONTENT);
    	setLayoutParams(mLayoutParams);
    	
    	mtextView = new TextView(mContext);
    	mtextView.setTextColor(Color.WHITE);
    	mtextView.setTextSize(36 / dip);
    	mtextView.setPadding((int)(20/div), 0, 0, 0);
    	mtextView.setGravity(Gravity.CENTER);
    	this.addView(mtextView);
    	this.setFocusable(true);
    }
    
    public void update(String str,int num){
    	mtextView.setText(str);
    	listNum = num;
    }
    
    public int getListNum(){
    	return listNum;
    }
    
    public String getSetupName(){
    	return mtextView.getText().toString();
    }
    
    public void setFocused(boolean isFoucused)
    {
        hasFocus = isFoucused;
    }

    public boolean getFocused()
    {
        return hasFocus;
    }

	public boolean onKey(View view, int keyCode, KeyEvent event) 
    {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK 
		 || keyCode == KeyEvent.KEYCODE_MENU
		 || keyCode == KeyEvent.KEYCODE_PROG_RED 
		 || keyCode == KeyEvent.KEYCODE_PROG_GREEN
	     || keyCode == KeyEvent.KEYCODE_PROG_YELLOW
	     || keyCode == KeyEvent.KEYCODE_PROG_BLUE
	  	 || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
	  	 || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
	  	 || keyCode == KeyEvent.KEYCODE_DPAD_UP
	  	 || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		 && event.getAction()==KeyEvent.ACTION_DOWN)
		{
			parentView.onKeyDown(keyCode, event);
			return true;
		}
		return false;
	}

    public void setBackGroundColorUser(boolean hasFocus)
    {
        if (hasFocus)
        {
        	this.setBackgroundColor(Color.rgb(53,152,220));

        	mtextView.setTextColor(Color.BLACK);
        	this.hasFocus = true;
        } else
        {
        	mtextView.setTextColor(Color.WHITE); 
            this.setBackgroundDrawable(null);
            this.hasFocus = false;
        }
    }

	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}


	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
	
	 public void setItemSetupFocusChange(){
		 this.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View view, boolean hasFocus) {
				// TODO Auto-generated method stub
				((SetupItemView) view).setBackGroundColorUser(hasFocus);
			}
		});
	 }
	
}