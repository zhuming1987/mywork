package com.tv.ui.LNBSetting;

import java.util.ArrayList;
import java.util.List;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnFocusChangeListener;

public class MotorSettingEnumItemView extends LinearLayout implements OnKeyListener, OnFocusChangeListener{

	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
	public final int arrowWidth = 18;
	public final int FONT_SIZE = 30;
    public final int FONT_SIZE_SELECTED = 36;
    public boolean HasFocus = false;
    public boolean isEnabled = true;
	
	private EnumItemListener eItemListener = null;

	private TextView titleTextView;
	private ImageView leftArrow;
    private ImageView rightArrow;
    private LinearLayout textLayout;
    private TextView enumText1;
    private TextView enumText2;
    private TextView enumText3;
    private List<String> enumStrList;
    private int currentIndex;
    private boolean loop = false;
	
	public MotorSettingEnumItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setLayoutParams(new LayoutParams((int)(845/div), (int)(80/div)));
		this.setPadding((int)(15/div), (int)(5/div), 0, (int)(5/div));
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setOnKeyListener(this);
		this.setOnFocusChangeListener(this);
		
		titleTextView = new TextView(context);
		titleTextView.setTextColor(Color.WHITE);
		titleTextView.setTextSize((float)(36/dip));
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		titleTextView.setSingleLine(true);
		titleTextView.setEllipsize(TruncateAt.MARQUEE);
		titleTextView.setMarqueeRepeatLimit(-1);
		LayoutParams titletxtParams = new LayoutParams((int)(200/div), LayoutParams.MATCH_PARENT);
		titleTextView.setLayoutParams(titletxtParams);
		this.addView(titleTextView);
		
		textLayout = new LinearLayout(context);
        textLayout.setGravity(Gravity.CENTER_VERTICAL);
        textLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        LayoutParams textParams = new LayoutParams((int)(526/div), (int)(60/div));
        textParams.leftMargin = (int)(80/div);
        textParams.gravity = Gravity.CENTER_VERTICAL;
        textLayout.setLayoutParams(textParams);
        this.addView(textLayout);

        leftArrow = new ImageView(context);
        leftArrow.setScaleType(ScaleType.CENTER_INSIDE);
        leftArrow.setImageResource(R.drawable.left_arrow);
        textLayout.addView(leftArrow);
        
        enumText1 = new TextView(context);
        enumText1.getPaint().setAntiAlias(true);
        enumText1.setTextSize(FONT_SIZE/dip);
        enumText1.setTextColor(Color.WHITE);
        enumText1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        enumText1.setSingleLine();
        enumText1.setAlpha(0.6f);
        enumText1.setPadding((int) (10 / div), 0, (int) (10 / div), 0);
        enumText1.setEllipsize(TruncateAt.START);
        enumText1.setLayoutParams(new LayoutParams((int)(120/div), (int)(68/div)));
        textLayout.addView(enumText1);
        
        enumText2 = new TextView(context);
        enumText2.setTextSize(FONT_SIZE_SELECTED/dip);
        enumText2.setGravity(Gravity.CENTER);
        enumText2.setSingleLine(true);
        enumText2.setEllipsize(TruncateAt.MARQUEE);
        enumText2.setMarqueeRepeatLimit(-1);
        enumText2.setPadding((int) (5 / div), 0, (int) (5 / div), 0);
        enumText2.setLayoutParams(new LayoutParams((int)(200/div), (int)(68/div)));
        textLayout.addView(enumText2);
        
        enumText3 = new TextView(context);
        enumText3.getPaint().setAntiAlias(true);
        enumText3.setTextSize(FONT_SIZE/dip);
        enumText3.setTextColor(Color.WHITE);
        enumText3.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        enumText3.setSingleLine();
        enumText3.setAlpha(0.6f);
        enumText3.setEllipsize(TruncateAt.END);
        enumText3.setPadding((int) (10 / div), 0, (int) (10 / div), 0);
        enumText3.setLayoutParams(new LayoutParams((int)(120/div), (int)(68/div)));
        textLayout.addView(enumText3);
        
        rightArrow = new ImageView(context);
        rightArrow.setScaleType(ScaleType.CENTER_INSIDE);
        rightArrow.setImageResource(R.drawable.right_arrow);
        textLayout.addView(rightArrow); 

        LayoutParams imgLeftLp = new LayoutParams(arrowWidth, LayoutParams.MATCH_PARENT);
        imgLeftLp.gravity = Gravity.LEFT;
        leftArrow.setLayoutParams(imgLeftLp);
        LayoutParams imgRightLp = new LayoutParams(arrowWidth, LayoutParams.MATCH_PARENT);
        rightArrow.setLayoutParams(imgRightLp);
        imgRightLp.gravity = Gravity.RIGHT;
        leftArrow.setVisibility(View.INVISIBLE);
        rightArrow.setVisibility(View.INVISIBLE);
        textLayout.setGravity(Gravity.CENTER);
        
	}

	public void updateView(String titlString, int defual_index, ArrayList<String> data){
		titleTextView.setText(titlString);
		if(data.size() == 0)
			return;
		if (enumStrList == null)
			enumStrList = new ArrayList<String>();
		enumStrList.clear();
		if (data.size() == 2) {
			for (String string : data) {
				enumStrList.add(string);
			}
			currentIndex = defual_index;
			loop = false;
		} else {
			currentIndex = defual_index;
			for (String string : data) {
				enumStrList.add(string);
			}
			if (currentIndex >= enumStrList.size())
				currentIndex = 0;
			if (enumStrList.size() >= 3)
				loop = true;
			else
				loop = false;
		}
		if (isEnabled) {
			if (loop) {
				leftArrow.setVisibility(View.VISIBLE);
				rightArrow.setVisibility(View.VISIBLE);
			} else {
				leftArrow.setVisibility(View.INVISIBLE);
				rightArrow.setVisibility(View.INVISIBLE);
			}
			this.setFocusable(true);
		} else {
			this.setFocusable(false);
		}
		this.setFocusable(isEnabled);
		refreshUI();
	}
	
	public void refreshUI() {
		// TODO Auto-generated method stub
		if (enumStrList.size() == 0)
			return;
		if (loop) {
			enumText1.setText(currentIndex >= 1 ? enumStrList
					.get(currentIndex - 1)
					: enumStrList.get(enumStrList.size() - 1));
			enumText2.setText(enumStrList.get(currentIndex));
			enumText3.setText(currentIndex < enumStrList.size() - 1 ? enumStrList
							.get(currentIndex + 1) : enumStrList.get(0));
			if (this.hasFocus()) {
				leftArrow.setVisibility(View.VISIBLE);
				rightArrow.setVisibility(View.VISIBLE);
			} else {
				leftArrow.setVisibility(View.INVISIBLE);
				rightArrow.setVisibility(View.INVISIBLE);
			}
		} else {
			enumText1.setText(currentIndex >= 1 ? enumStrList
					.get(currentIndex - 1) : "");
			enumText2.setText(enumStrList.get(currentIndex));
			enumText3.setText(currentIndex < enumStrList.size() - 1 ? enumStrList
							.get(currentIndex + 1) : "");

			leftArrow.setVisibility(View.INVISIBLE);
			rightArrow.setVisibility(View.INVISIBLE);
		}
		setText2Color();
	}

	private void setText2Color() {
		if (HasFocus) {
			if (isEnabled) {
				enumText1.setTextColor(Color.BLACK);
				enumText2.setTextColor(Color.BLACK);
				enumText3.setTextColor(Color.BLACK);
			} else {
				enumText1.setTextColor(Color.GRAY);
				enumText2.setTextColor(Color.GRAY);
				enumText3.setTextColor(Color.GRAY);
			}
		} else {
			if (isEnabled) {
				enumText1.setTextColor(Color.WHITE);
				enumText2.setTextColor(Color.WHITE);
				enumText3.setTextColor(Color.WHITE);
			} else {
				enumText1.setTextColor(Color.GRAY);
				enumText2.setTextColor(Color.GRAY);
				enumText3.setTextColor(Color.GRAY);
			}
		}
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.ACTION_UP)
			return false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			executeLeft();
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			executeRight();
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
		case KeyEvent.KEYCODE_DPAD_UP:
			if(eItemListener != null)
				eItemListener.onEnumItemChangeListener(this, keyCode, currentIndex);
			return false;
		case KeyEvent.KEYCODE_MENU:
		case KeyEvent.KEYCODE_BACK:
			if(eItemListener != null)
				eItemListener.onEnumItemChangeListener(this, keyCode, currentIndex);
		}
		return false;
	}
	
	public void executeLeft()
    {
        if (currentIndex > 0)
        {
            currentIndex--;
            refreshUI();
            if(eItemListener != null)
            	eItemListener.onEnumItemChangeListener(this, KeyEvent.KEYCODE_DPAD_LEFT,currentIndex);
        } else if (loop)
        {
            currentIndex = enumStrList.size() - 1;
            refreshUI();
            if(eItemListener != null)
            	eItemListener.onEnumItemChangeListener(this, KeyEvent.KEYCODE_DPAD_LEFT,currentIndex);
        }
    }

    public void executeRight()
    {
        if (currentIndex < enumStrList.size() - 1)
        {
            currentIndex++;
            refreshUI();
            if(eItemListener != null)
            	eItemListener.onEnumItemChangeListener(this, KeyEvent.KEYCODE_DPAD_RIGHT,currentIndex);
        } else if (loop)
        {
            currentIndex = 0;
            refreshUI();
            if(eItemListener != null)
            	eItemListener.onEnumItemChangeListener(this, KeyEvent.KEYCODE_DPAD_RIGHT,currentIndex);
        }
    }
    
    @Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		this.HasFocus = hasFocus;
		if (hasFocus) {
			titleTextView.setTextColor(Color.parseColor("#3598DC"));
			titleTextView.setSelected(true);
			textLayout.setBackgroundResource(R.drawable.blue_sel_bg);
			enumText1.setTextColor(Color.BLACK);
			enumText2.setTextColor(Color.BLACK);
			enumText2.setSelected(true);
			enumText3.setTextColor(Color.BLACK);
			if (loop) {
				leftArrow.setVisibility(View.VISIBLE);
				rightArrow.setVisibility(View.VISIBLE);
			} else {
				leftArrow.setVisibility(View.INVISIBLE);
				rightArrow.setVisibility(View.INVISIBLE);
			}

		} else {
			titleTextView.setTextColor(Color.WHITE);
			titleTextView.setSelected(false);
			enumText2.setSelected(false);
			textLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
			if (isEnabled) {
				enumText1.setTextColor(Color.WHITE);
				enumText2.setTextColor(Color.WHITE);	
				enumText3.setTextColor(Color.WHITE);
			} else {
				enumText1.setTextColor(Color.GRAY);
				enumText2.setTextColor(Color.GRAY);
				enumText3.setTextColor(Color.GRAY);
			}
			leftArrow.setVisibility(View.INVISIBLE);
			rightArrow.setVisibility(View.INVISIBLE);
		}
	}
    
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		this.setFocusable(isEnabled);
		this.setFocusableInTouchMode(isEnabled);
	}
	
	public void setItemListener(EnumItemListener eItemListener) {
		this.eItemListener = eItemListener;
	}

	/**
	 * 控件状态变化接口
	 * @author gky
	 *
	 */
	public interface EnumItemListener{
		public void onEnumItemChangeListener(View view, int keyCode, int index);
	}

}
