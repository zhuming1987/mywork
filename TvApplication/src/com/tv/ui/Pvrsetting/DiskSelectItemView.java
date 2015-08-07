package com.tv.ui.Pvrsetting;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiskSelectItemView extends RelativeLayout implements OnFocusChangeListener, OnTouchListener, OnKeyListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
    private ImageView checkedView;
    private TextView textView1;
    private TextView textView2;
    private Context mContext;
    private boolean isItemCheck = false;
    private boolean hasFocus = false;
    
    private DiskSelectView parentView;

    public DiskSelectItemView(Context context, DiskSelectView parentView)
    {
        super(context);
        this.setOnKeyListener(this);
        mContext = context;
        this.parentView = parentView;
        initViews();
    }

    private void initViews()
    {
    	RelativeLayout.LayoutParams mainLayout = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(mainLayout);
        RelativeLayout.LayoutParams checkedViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        checkedViewParams.rightMargin = (int) (20 / div);
        checkedViewParams.leftMargin = (int) (10 / div);
        checkedViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        checkedView = new ImageView(mContext);
        checkedView.setId(2001);
        this.addView(checkedView, checkedViewParams);
        RelativeLayout.LayoutParams textViewParams1 = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        textViewParams1.addRule(RelativeLayout.RIGHT_OF, checkedView.getId());
        textViewParams1.addRule(RelativeLayout.CENTER_IN_PARENT);
        textViewParams1.leftMargin = (int) (20 / div);
        textView1 = new TextView(mContext);
        textView1.setTextColor(Color.WHITE);
        textView1.setTextSize(30 / dip);
        textView1.setId(2014);
        this.addView(textView1, textViewParams1);
        RelativeLayout.LayoutParams textViewParams2 = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        textViewParams2.addRule(RelativeLayout.RIGHT_OF, textView1.getId());
        textViewParams2.leftMargin = (int) (15 / div);
        textView2 = new TextView(mContext);
        textView2.setTextColor(Color.WHITE);
        textView2.setTextSize(30 / dip);       
        this.addView(textView2, textViewParams2);
        this.setFocusable(true);
        this.setClickable(true);
        if (isItemCheck)
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
        } else
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
        }
    }

    public void update(String textStr , String textStr2)
    {
        textView1.setText(textStr);
        textView2.setText(textStr2);
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
        if (isItemCheck)
        {
            checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
        } else
        {
            if (hasFocus)
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_focus_icon);
                textView1.setTextColor(Color.BLACK);
                textView2.setTextColor(Color.BLACK);
            } else
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
                textView1.setTextColor(Color.WHITE);
                textView2.setTextColor(Color.WHITE);
            }
        }
    }

    public void setBackGroundColorUser(boolean hasFocus)
    {
        if (hasFocus)
        {
        	this.setBackgroundResource(R.drawable.yellow_sel_bg);

        	textView1.setTextColor(Color.BLACK);
        	textView2.setTextColor(Color.BLACK);
            if (isItemCheck)
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_sel_icon);
            } else
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_focus_icon);
            }
            hasFocus = true;
        } else
        {
            textView1.setTextColor(Color.WHITE);
            textView2.setTextColor(Color.WHITE);
            if (!isItemCheck)
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
            }
            this.setBackgroundDrawable(null);
            hasFocus = false;
        }
    }
    
    @Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
			parentView.onKeyDown(keyCode, null);
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}
}
