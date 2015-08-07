package com.tv.ui.CECDeviceList;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tv.app.R;
import com.tv.app.TvUIControler;

public class CECDeviceListItemView extends RelativeLayout implements OnFocusChangeListener, OnTouchListener, OnKeyListener
{
    private static float div = TvUIControler.getInstance().getResolutionDiv();
    private static float dip = TvUIControler.getInstance().getDipDiv();
    private ImageView checkedView;
    private TextView textView;
    private Context mContext;
    private boolean isItemCheck = false;
    private boolean hasFocus = false;
    private CECDeviceListView parentView;

    public CECDeviceListItemView(Context context, CECDeviceListView parentView)
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
        checkedViewParams.leftMargin = (int) (80 / div);
        checkedViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        checkedView = new ImageView(mContext);
        checkedView.setId(2001);
        this.addView(checkedView, checkedViewParams);
        RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textViewParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        textViewParams.addRule(RelativeLayout.RIGHT_OF, checkedView.getId());
        textViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textViewParams.leftMargin = (int) (80 / div);
        textView = new TextView(mContext);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(36 / dip);
        this.addView(textView, textViewParams);
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

    public void update(String textStr)
    {
        textView.setText(textStr);
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

    public String getItemText()
    {
        return textView.getText().toString();
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
                textView.setTextColor(Color.BLACK);
            } else
            {
                checkedView.setImageResource(R.drawable.leftlist_souce_unfocus_icon);
                textView.setTextColor(Color.WHITE);
            }
        }
    }

    public void setBackGroundColorUser(boolean hasFocus)
    {
        if (hasFocus)
        {
        	this.setBackgroundResource(R.drawable.yellow_sel_bg);

        	textView.setTextColor(Color.BLACK);
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
            textView.setTextColor(Color.WHITE);
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
		if (keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/) {
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