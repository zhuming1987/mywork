package com.tv.ui.SettingView;

import java.util.ArrayList;
import java.util.List;

import com.tv.app.R;
import com.tv.data.MenuItem;
import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.data.TvSwitchSetData;
import com.tv.framework.define.TvConfigTypes.TvConfigType;
import com.tv.framework.define.TvContext;
import com.tv.ui.utils.LogicTextResource;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

public class TvEnumItemView extends TvSettingItemView{

	private ImageView leftArrow;
    private ImageView rightArrow;
    private LinearLayout textLayout;
    private TextView enumText1;
    private TextView enumText2;
    private TextView enumText3;
    private List<String> enumStrList;
    private int currentIndex;
    private boolean loop = false;
    
    public final int arrowWidth = 18;
    public final int textWidth = 485;
    public final int textHeight = 60;

    public final int FONT_SIZE = (int)(30/dipDiv);
    public final int FONT_SIZE_SELECTED = (int)(40/dipDiv);
    private boolean HasFocus = false;
    
	public TvEnumItemView(Context context,int index, TvSettingView parentView, OnSettingKeyDownListener keyListener) {
		super(context, index, parentView, keyListener);
		// TODO Auto-generated constructor stub
		textLayout = new LinearLayout(context);
        textLayout.setGravity(Gravity.CENTER_VERTICAL);
        LayoutParams textlp = new LayoutParams((int)(textWidth/resolution), (int)(textHeight/resolution));
        textLayout.setLayoutParams(textlp);

        textLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        int enumTextSideW = (int)(125/resolution);
        int enumTextCenterW = (int)(200/resolution);
        LayoutParams tvParamsSide = new LayoutParams(enumTextSideW, (int) (68 / resolution));
        LayoutParams tvParamsCenter = new LayoutParams(enumTextCenterW, (int) (68 / resolution));

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
        enumText1.setPadding((int) (10 / resolution), 0, (int) (10 / resolution), 0);
        textLayout.addView(enumText1);
        
        enumText2 = new TextView(context);
        enumText2.setWidth(enumTextCenterW);
        enumText2.setTextSize(FONT_SIZE_SELECTED);
        enumText2.setGravity(Gravity.CENTER);
        enumText2.setSingleLine();
        enumText2.setEllipsize(TruncateAt.MARQUEE);
        enumText2.setMarqueeRepeatLimit(-1);
        enumText2.setPadding((int) (5 / resolution), 0, (int) (5 / resolution), 0);
        textLayout.addView(enumText2);
        
        enumText3 = new TextView(context);
        enumText3.getPaint().setAntiAlias(true);
        enumText3.setTextSize(FONT_SIZE);
        enumText3.setTextColor(Color.WHITE);
        enumText3.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        enumText3.setSingleLine();
        enumText3.setAlpha(0.6f);
        enumText3.setPadding((int) (10 / resolution), 0, (int) (10 / resolution), 0);
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

        LayoutParams imgLeftLp = new LayoutParams((int)(arrowWidth/resolution), LayoutParams.MATCH_PARENT);
        imgLeftLp.gravity = Gravity.LEFT;
        leftArrow.setLayoutParams(imgLeftLp);
        LayoutParams imgRightLp = new LayoutParams((int)(arrowWidth/resolution), LayoutParams.MATCH_PARENT);
        rightArrow.setLayoutParams(imgRightLp);
        imgRightLp.gravity = Gravity.RIGHT;
        leftArrow.setVisibility(View.INVISIBLE);
        rightArrow.setVisibility(View.INVISIBLE);
        textLayout.setGravity(Gravity.CENTER);
        viewLayout.addView(textLayout,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        enumText1.setFocusable(true);
        enumText1.setFocusableInTouchMode(true);
        enumText1.setEnabled(true);
		
	}

	
	@Override
	public void updateView(MenuItem data) {
		// TODO Auto-generated method stub
		super.updateView(data);
		if (enumStrList == null)
			enumStrList = new ArrayList<String>();
		enumStrList.clear();
		if (data.getItemType().equals(TvConfigType.TV_CONFIG_SWITCH)) {
			enumStrList.add(TvContext.context.getResources().getString(R.string.TV_CFG_ON_MODE));
			enumStrList.add(TvContext.context.getResources().getString(R.string.TV_CFG_OFF_MODE));
			currentIndex = ((TvSwitchSetData)data.getItemData()).isOn()?0:1;
			loop = false;
		} else if (data.getItemType().equals(TvConfigType.TV_CONFIG_ENUM)) {
			currentIndex = ((TvEnumSetData)data.getItemData()).getCurrentIndex();
			for (String str : ((TvEnumSetData)data.getItemData()).getEnumList()) {
				enumStrList.add(LogicTextResource.getInstance(TvContext.context).getText(str));
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
		bodyLayout.setFocusable(isEnabled);
		refreshUI();
	}

	public void executeLeft()
    {
        if (currentIndex > 0)
        {
            currentIndex--;
            refreshUI();
            OnExecuteSet(currentIndex);
        } else if (loop)
        {
            currentIndex = enumStrList.size() - 1;
            refreshUI();
            OnExecuteSet(currentIndex);
        }
    }

    public void executeRight()
    {
        if (currentIndex < enumStrList.size() - 1)
        {
            currentIndex++;
            refreshUI();
            OnExecuteSet(currentIndex);
        } else if (loop)
        {
            currentIndex = 0;
            refreshUI();
            OnExecuteSet(currentIndex);
        }
    }
    
	@Override
	public void updateBgDrawable(boolean parentHasFocus) {
		// TODO Auto-generated method stub
		Log.d("gky", "updateBgDrawable=" + parentHasFocus);
		HasFocus = parentHasFocus;
		if (parentHasFocus) {
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

	@Override
	public boolean executeKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("gky", getClass().toString() + " executeKey keyCode=" + keyCode);
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
		return true;
	}

	@Override
	public void refreshUI() {
		// TODO Auto-generated method stub
		if (enumStrList.size() == 0)
			return;
		Log.i("gky", "TvEnumItemView::refreshUI:curStrList is " + enumStrList.get(currentIndex));
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

	public int getCurrentIndex() {
		return currentIndex;
	}
}
