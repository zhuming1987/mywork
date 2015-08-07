package com.tv.ui.ListView;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.ui.base.TvMenuConfigDefs;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;

public class TvListItemView extends RelativeLayout implements OnKeyListener, OnFocusChangeListener,OnClickListener{
	
	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
    private ImageView checkedView;
    private TextView textView;
    
    private onKeyDownListener keyDownListener;
    public  int index;
	
    public TvListItemView(Context context,onKeyDownListener keyDownListener, int index) {
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
                (int)(TvMenuConfigDefs.setting_item_w/div), LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(mainLayout);
        
        RelativeLayout.LayoutParams checkedViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        checkedViewParams.rightMargin = (int) (20 / div);
        checkedViewParams.leftMargin = (int) (80 / div);
        checkedViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        checkedView = new ImageView(context);
        checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
        checkedView.setId(2001);
        this.addView(checkedView, checkedViewParams);
        
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        textViewParams.addRule(RelativeLayout.RIGHT_OF, checkedView.getId());
        textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //textViewParams.leftMargin = (int) (80 / div);
        textView = new TextView(context);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(36 / dip);
        textView.setSingleLine(true);
        textView.setEllipsize(TruncateAt.MARQUEE);
        textView.setMarqueeRepeatLimit(-1);
        this.addView(textView, textViewParams);
        
	}

    public void updateView(String data)
    {
    	textView.setText(data);
    }
    
	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			keyDownListener.keyDownListener(keyCode, event);
			return true;
		}
		return false;
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvListItemView::onFocusChange:hasFocus is " + hasFocus);
		if (hasFocus)
        {
			this.setBackgroundResource(R.drawable.yellow_sel_bg);
            //checkedView.setImageResource(R.drawable.leftlist_souce_focus_icon);
            textView.setTextColor(Color.BLACK);
            this.invalidate();
        } else
        {
        	this.setBackgroundDrawable(null);
            //checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
            textView.setTextColor(Color.WHITE);
            this.invalidate();
        }
	}
	
	public void setChecked(boolean flag)
	{
		if(flag)
		{
			checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
		}
		else 
		{
			checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
		}
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		setChecked(true);
		this.invalidate();
		keyDownListener.ItemClickListener(index);
	}
	
	public interface onKeyDownListener
	{
		public void keyDownListener(int keyCode, KeyEvent event);
		
		public void ItemClickListener(int index);
	}

	
}
