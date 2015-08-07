package com.tv.ui.ChannelSearch;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.ui.utils.LogicTextResource;

@SuppressLint("ViewConstructor")
public class DVBSEnumItemView extends LinearLayout implements OnKeyListener, OnFocusChangeListener {

	private static float div = TvUIControler.getInstance().getResolutionDiv();
    private static float dipDiv = TvUIControler.getInstance().getDipDiv();
	private Context context;
	private int width;
	private int height;
	public final int arrowWidth = (int)(18/div);
	public final int FONT_SIZE = (int)(30/dipDiv);
    public final int FONT_SIZE_SELECTED = (int)(40/dipDiv);
    public boolean HasFocus = false;
    public boolean isEnabled = true;
	
	private EnumItemListener eItemListener = null;

	private ImageView leftArrow;
    private ImageView rightArrow;
    private LinearLayout textLayout;
    private TextView enumText1;
    private TextView enumText2;
    private TextView enumText3;
    private List<String> enumStrList;
    private int currentIndex;
    private boolean loop = false;
	private static int borderPadding = (int) (15 / div);
	
	public DVBSEnumItemView(Context context, int width, int height) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.width = width;
		this.height = height;
		this.setPadding(borderPadding, borderPadding, borderPadding, borderPadding);
		this.setFocusable(true);
		this.setOnKeyListener(this);
		this.setOnFocusChangeListener(this);
		
		initView();
	}
	
	public void initView(){
		
		textLayout = new LinearLayout(context);
        textLayout.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams textlp = new LayoutParams(this.width, this.height);
        textLayout.setLayoutParams(textlp);

        textLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        int enumTextSideW = (int)(120/div);
        int enumTextCenterW = (int)(200/div);
        LayoutParams tvParamsSide = new LayoutParams(enumTextSideW, (int) (50 / div));
        LayoutParams tvParamsCenter = new LayoutParams(enumTextCenterW, (int) (50 / div));

        leftArrow = new ImageView(context);
        leftArrow.setScaleType(ScaleType.CENTER_INSIDE);
        leftArrow.setImageResource(R.drawable.left_arrow);
        textLayout.addView(leftArrow);
        
        enumText1 = new TextView(context);
        enumText1.getPaint().setAntiAlias(true);
        enumText1.setTextSize(FONT_SIZE);
        enumText1.setTextColor(Color.WHITE);
        enumText1.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        enumText1.setSingleLine();
        enumText1.setAlpha(0.6f);
        enumText1.setPadding((int) (10 / div), 0, (int) (10 / div), 0);
        textLayout.addView(enumText1);
        
        enumText2 = new TextView(context);
        enumText2.setWidth(enumTextCenterW);
        enumText2.setTextSize(FONT_SIZE_SELECTED);
        enumText2.setGravity(Gravity.CENTER);
        enumText2.setSingleLine();
        enumText2.setEllipsize(TruncateAt.MARQUEE);
        enumText2.setMarqueeRepeatLimit(-1);
        enumText2.setPadding((int) (5 / div), 0, (int) (5 / div), 0);
        textLayout.addView(enumText2);
        
        enumText3 = new TextView(context);
        enumText3.getPaint().setAntiAlias(true);
        enumText3.setTextSize(FONT_SIZE);
        enumText3.setTextColor(Color.WHITE);
        enumText3.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        enumText3.setSingleLine();
        enumText3.setAlpha(0.6f);
        enumText3.setPadding((int) (10 / div), 0, (int) (10 / div), 0);
        textLayout.addView(enumText3);
        
        rightArrow = new ImageView(context);
        rightArrow.setScaleType(ScaleType.CENTER_INSIDE);
        rightArrow.setImageResource(R.drawable.right_arrow);
        textLayout.addView(rightArrow);

        enumText1.setLayoutParams(tvParamsSide);
        enumText2.setLayoutParams(tvParamsCenter);
        enumText3.setLayoutParams(tvParamsSide);
        enumText1.setEllipsize(TruncateAt.START);
        enumText3.setEllipsize(TruncateAt.END);

        LayoutParams imgLeftLp = new LayoutParams(arrowWidth, LayoutParams.MATCH_PARENT);
        imgLeftLp.gravity = Gravity.LEFT;
        leftArrow.setLayoutParams(imgLeftLp);
        LayoutParams imgRightLp = new LayoutParams(arrowWidth, LayoutParams.MATCH_PARENT);
        rightArrow.setLayoutParams(imgRightLp);
        imgRightLp.gravity = Gravity.RIGHT;
        leftArrow.setVisibility(View.INVISIBLE);
        rightArrow.setVisibility(View.INVISIBLE);
        textLayout.setGravity(Gravity.CENTER);
        this.addView(textLayout,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        enumText1.setFocusable(true);
        enumText1.setFocusableInTouchMode(true);
        enumText1.setEnabled(true);
        
        OnTouchListener leftTouch = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				executeLeft();
				return false;
			}

		};
		
		OnTouchListener rightTouch = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				executeRight();
				return false;
			}
		};

        enumText1.setOnTouchListener(leftTouch);
        leftArrow.setOnTouchListener(leftTouch);
        enumText3.setOnTouchListener(rightTouch);
        rightArrow.setOnTouchListener(rightTouch);
	}

	public void setBindData(int defual_index, ArrayList<String> data){
		if (enumStrList == null)
			enumStrList = new ArrayList<String>();
		enumStrList.clear();
		if (data.size() == 2) {
			for (String string : data) {
				enumStrList.add(LogicTextResource.getInstance(context).getText(string));
			}
			currentIndex = defual_index;
			loop = false;
		} else {
			currentIndex = defual_index;
			for (String string : data) {
				enumStrList.add(LogicTextResource.getInstance(context).getText(string));
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
			enumText2.setSelected(true);
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
			enumText2.setSelected(true);
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
		}
		return false;
	}
	
	public void executeLeft()
    {
        if (currentIndex > 0)
        {
            currentIndex--;
            refreshUI();
            eItemListener.onEnumItemChangeListener(enumStrList.get(currentIndex),currentIndex);
        } else if (loop)
        {
            currentIndex = enumStrList.size() - 1;
            refreshUI();
            eItemListener.onEnumItemChangeListener(enumStrList.get(currentIndex),currentIndex);
        }
    }

    public void executeRight()
    {
        if (currentIndex < enumStrList.size() - 1)
        {
            currentIndex++;
            refreshUI();
            eItemListener.onEnumItemChangeListener(enumStrList.get(currentIndex),currentIndex);
        } else if (loop)
        {
            currentIndex = 0;
            refreshUI();
            eItemListener.onEnumItemChangeListener(enumStrList.get(currentIndex),currentIndex);
        }
    }
    
    @Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		this.HasFocus = hasFocus;
		if (hasFocus) {
			textLayout.setBackgroundResource(R.drawable.yellow_sel_bg);
			enumText2.setSelected(true);
			enumText1.setTextColor(Color.BLACK);
			enumText2.setTextColor(Color.BLACK);
			enumText3.setTextColor(Color.BLACK);
			if (loop) {
				leftArrow.setVisibility(View.VISIBLE);
				rightArrow.setVisibility(View.VISIBLE);
			} else {
				leftArrow.setVisibility(View.INVISIBLE);
				rightArrow.setVisibility(View.INVISIBLE);
			}

		} else {
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
	}
	
	public void setItemListener(EnumItemListener eItemListener) {
		this.eItemListener = eItemListener;
	}

	public interface EnumItemListener{
		public void onEnumItemChangeListener(String data, int index);
	}

}
