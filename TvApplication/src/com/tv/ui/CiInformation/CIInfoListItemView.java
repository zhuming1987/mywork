package com.tv.ui.CiInformation;

import android.content.Context;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.ui.base.TvMenuConfigDefs;

import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CIInfoListItemView extends RelativeLayout implements OnKeyListener, OnFocusChangeListener,OnClickListener{

	private static final float div1 = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
    private TextView itemTextView;
    private onKeyDownListener keyDownListener;
    public  int index;
    
   public CIInfoListItemView(Context context,onKeyDownListener keyDownListener, int index)
    {
		super(context);
		// TODO Auto-generated constructor stub
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setClickable(true);
        this.setOnKeyListener(this);
        this.setOnClickListener(this);
        this.setOnFocusChangeListener(this);
        this.setEnabled(true);
        this.keyDownListener = keyDownListener;
        this.index = index;
		RelativeLayout.LayoutParams mainLayout = new RelativeLayout.LayoutParams(
                (int)(TvMenuConfigDefs.setting_item_w/div1), LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(mainLayout);
        
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        
        itemTextView = new TextView(context);
        itemTextView.setTextColor(Color.WHITE);
        itemTextView.setTextSize(36 / dip);
        itemTextView.setSingleLine(true);
        itemTextView.setEllipsize(TruncateAt.MARQUEE);
        itemTextView.setMarqueeRepeatLimit(-1);
        this.addView(itemTextView, textViewParams);
        
	}

    public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	if(event.getAction() == KeyEvent.ACTION_UP)
    		return false;
//    	keyDownListener.keyDownListener(keyCode, event);
//		return true;
    	if(keyCode == KeyEvent.KEYCODE_BACK
				|| keyCode == KeyEvent.KEYCODE_MENU 
				     || keyCode == KeyEvent.KEYCODE_HOME
				     || keyCode == KeyEvent.KEYCODE_TV_INPUT)
		{
			keyDownListener.keyDownListener(keyCode, event);
			return true;
		}
		
		return false;
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		Log.i("gky", "CIInfoListItemView::onFocusChange:hasFocus is " + hasFocus);
		if (hasFocus)
        {
			this.setBackgroundResource(R.drawable.yellow_sel_bg);
            itemTextView.setTextColor(Color.BLACK);
            this.invalidate();
        } else
        {
        	this.setBackground(null);
        	itemTextView.setTextColor(Color.WHITE);
            this.invalidate();
        }
	}
	
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		this.invalidate();
		keyDownListener.ItemClickListener(index);
	}
	
	public interface onKeyDownListener
	{
		public void keyDownListener(int keyCode, KeyEvent event);
		
		public void ItemClickListener(int index);
	}

	public void updateView(String string) {
		// TODO Auto-generated method stub
		  itemTextView.setText(string);
	}

	
}