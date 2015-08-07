package com.tv.ui.widgets;

import java.util.List;

import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PopupSpinner extends LinearLayout
{
    private Context context;
    private LinearLayout titleLayout;
    private AlwaysMarqueeTextView titleText;
    private ImageView arrowImage;
    private ScrollView scroll;
    private LinearLayout scrollLayout;

    private static float div = TvUIControler.getInstance().getResolutionDiv();
    private static float dipDiv = TvUIControler.getInstance().getDipDiv();
    private int maxShowCount = 6;
    private int width, height, itemHeight;
    private int titleBgRes = R.drawable.wifi_edit_text_unsel;
//    private int titleBgRes = R.drawable.dropdown_bg;
    private int titleBgSelectedRes = R.drawable.wifi_edit_text_sel;
    private int dropDownBgRes = R.drawable.dropdown_bg;
    private int itemSelectedRes = R.drawable.item_selected_yellow;
    private int textSize = (int) (36 / dipDiv);
    private int paddingX = (int) (25 / div);
    private int spacing = (int)(10 / div);
    private int focusTextColor = Color.parseColor("#353535");

    private List<String> dataList;
    private int selectedIndex = 0; // 当前选中index
    private int scanIndex = 0; // 当前焦点index
    private boolean isExpanded = false; // 当前状�?�：收起：false、展�?：true
    private LayoutParams textLayoutParams;
    private int scrollOffset = 0;
    
    public OnSelectChangeListener listener;
    
    public PopupSpinner(Context context, int width, int itemHeight, int maxShowCount)
    {
        super(context);
        this.context = context;
        this.width = width;
        this.height = itemHeight * maxShowCount;
        this.maxShowCount = maxShowCount;
        this.itemHeight = itemHeight;
        this.setFocusable(true);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setOnKeyListener(keyListener);
        this.setOnFocusChangeListener(titleFocusChangeListener);

        initView();
    }
    
    private void initView()
    {
        titleLayout = new LinearLayout(context);
        titleLayout.setFocusable(true);
        titleLayout.setClickable(true);
        titleLayout.setOnKeyListener(keyListener);
        titleLayout.setBackgroundResource(titleBgRes);
        titleLayout.setGravity(Gravity.CENTER_VERTICAL);
        OnClickListener titleClickListener = new OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                if (isExpanded)
                    setExpanded(false);
                else
                    setExpanded(true);
            }
        };
        titleLayout.setOnClickListener(titleClickListener);
        titleLayout.setOnFocusChangeListener(titleFocusChangeListener);
        
        titleText = newTextView(textSize);
        //titleText.setFocusable(false);
        titleText.setOnFocusChangeListener(titleFocusChangeListener);
        //titleText.setClickable(false);
        titleText.setOnClickListener(titleClickListener);
        titleLayout.addView(titleText, new LayoutParams(width - itemHeight, itemHeight));

        arrowImage = new ImageView(context);
        arrowImage.setImageResource(R.drawable.dropdown_arrow);
        arrowImage.setScaleType(ScaleType.CENTER_INSIDE);
        titleLayout.addView(arrowImage, new LayoutParams(itemHeight, itemHeight));
        LayoutParams titleParams = new LayoutParams(width, itemHeight);
        titleParams.bottomMargin = spacing;
        this.addView(titleLayout, titleParams);
        
        scroll = new ScrollView(context);
        scroll.setBackgroundResource(dropDownBgRes);
        scroll.setVisibility(View.GONE);
        scroll.scrollTo(0, 0);
        this.addView(scroll, new LayoutParams(width, height));

        scrollLayout = new LinearLayout(context);
        scrollLayout.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(scrollLayout, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        
        textLayoutParams = new LayoutParams(width, itemHeight);
    }

    public PopupSpinner(Context context, int width, int itemHeight)
    {
        this(context, width, itemHeight, 6);
    }

    public void setSelectedIndex(int index)
    {
        if (index < 0 || index >= dataList.size())
            index = 0;
        if(this.selectedIndex != index)
        {
            setScanIndex(index);
            this.selectedIndex = index;
            titleText.setText(dataList.get(selectedIndex));
            if(listener != null){
                listener.onSelectedIndexChange(this, index);
                listener.onSelectedTextChange(this, dataList.get(index));
            }
        }
        titleText.setText(dataList.get(selectedIndex));
        setExpanded(false);
    }

    public void setSelectedText(String str)
    {
        titleText.setText(str);
        if(dataList == null)
            return;
        int index = dataList.indexOf(str);
        if(index != -1){
            this.selectedIndex = index;
        }
        else
            this.selectedIndex = 0;
    }
    
    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public String getSelectedString()
    {
        if(dataList == null)
        {
            return "";
        }
            return dataList.get(selectedIndex);
    }

    public void setData(List<String> data)
    {
        setData(data, 0);
    }

    @SuppressWarnings("deprecation")
	public void setData(List<String> data, int index)
    {
        this.dataList = data;
        if (data == null || data.size() == 0)
        {
            setSelectedText("---");
            return;
        }

        setSelectedIndex(index);
        Log.i("spinner_test", "popup id = "+this.getId()+", dataSize = "+dataList.size());
        for (int i = 0; i < dataList.size(); i++)
        {
            if (i < scrollLayout.getChildCount())
            {
                TextView itemText = (TextView) scrollLayout.findViewWithTag(i);
                itemText.setText(dataList.get(i));
            } else
            {
                TextView itemText = newTextView(textSize, paddingX);
                itemText.setTag(i);
                itemText.setText(dataList.get(i));
                itemText.setOnKeyListener(keyListener);
                itemText.setOnClickListener(itemClickListener);
                itemText.setOnFocusChangeListener(itemFocusChangeListener);
//                itemText.setFocusable(false);
//                itemText.setFocusableInTouchMode(true);
                itemText.setBackgroundDrawable(null);
                scrollLayout.addView(itemText, textLayoutParams);
            }
        }
        if (dataList.size() < scrollLayout.getChildCount())
        {
            scrollLayout.removeViews(dataList.size(),
                    scrollLayout.getChildCount() - dataList.size());
        }
        if (dataList.size() < maxShowCount)
            this.updateViewLayout(scroll, new LayoutParams(width, LayoutParams.WRAP_CONTENT));
        else
            this.updateViewLayout(scroll, new LayoutParams(width, height));
    }

    private AlwaysMarqueeTextView newTextView(int textSize)
    {
        return newTextView(textSize, paddingX);
    }

    private AlwaysMarqueeTextView newTextView(int textSize, int paddingX)
    {
        return newTextView(textSize, Color.WHITE, Gravity.LEFT | Gravity.CENTER_VERTICAL, paddingX);
    }

    private AlwaysMarqueeTextView newTextView(int textSize, int textColor, int gravity, int paddingX)
    {
        AlwaysMarqueeTextView text = new AlwaysMarqueeTextView(context);
        text.setTextSize(textSize);
        text.setSingleLine();
        text.setTextColor(textColor);
        text.setGravity(gravity);
        text.setPadding(paddingX, 0, paddingX, 0);
        text.setFocusable(true);
        text.setFocusableInTouchMode(true);
        text.setClickable(true);
        return text;
    }

    private OnClickListener itemClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            int tag = (Integer) view.getTag();
            if (tag >= 0 && tag < dataList.size())
            {
                setSelectedIndex(tag);
            }
            setExpanded(false);
        }
    };

    private OnFocusChangeListener titleFocusChangeListener = new OnFocusChangeListener()
    {
        @Override
        public void onFocusChange(View view, boolean hasFocus)
        {
            Log.i("test_marquee", "Spinner onFocusChanged, hasFocus = "+hasFocus);
            if (hasFocus)
            {
                titleLayout.setBackgroundResource(titleBgSelectedRes);
                titleText.requestFocus();
            }
            else{
                titleLayout.setBackgroundResource(titleBgRes);
                titleText.clearFocus();
            }
        }
    };
    
    private OnFocusChangeListener itemFocusChangeListener = new OnFocusChangeListener()
    {
        @SuppressWarnings("deprecation")
		@Override
        public void onFocusChange(View view, boolean hasFocus)
        {
            TextView text = (TextView) view;
            if (hasFocus)
            {
                TextView oldText = (TextView) scrollLayout.findViewWithTag(scanIndex);
                oldText.setBackgroundDrawable(null);
                oldText.setTextColor(Color.WHITE);
                text.setBackgroundResource(itemSelectedRes);
                text.setTextColor(focusTextColor);
            }
            else{
                text.setBackgroundDrawable(null);
                text.setTextColor(Color.WHITE);
            }
            text.setPadding(paddingX, 0, paddingX, 0);
        }
    };

    private void setExpanded(boolean expanded)
    {
        if(this.dataList == null)
            return;
        this.isExpanded = expanded;
        scroll.scrollTo(0, 0);
        if (expanded)
        {
            scroll.setVisibility(View.VISIBLE);
            titleText.clearFocus();
            setScanIndex(selectedIndex);
            if(scanIndex > dataList.size() - maxShowCount)
                scrollOffset = (dataList.size() - maxShowCount) * itemHeight;
            else
                scrollOffset = scanIndex * itemHeight;
            Log.i("popup_scroll", "scrollOffset = "+scrollOffset);
            scroll.scrollTo(0, scrollOffset);
            listener.onExpanded(this);
        }
        else
        {
            scroll.setVisibility(View.GONE);
            titleText.requestFocus();
        }
    }
    
    public void closeItems()
    {
        if(this.isExpanded)
        {
            this.isExpanded = false;
            scroll.setVisibility(View.GONE);
        }
    }

    private OnKeyListener keyListener = new OnKeyListener()
    {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            if(event.getAction() == KeyEvent.ACTION_DOWN)
            {
                return executeKeyDown(keyCode);
            }
            else
                return true;
        }
    };
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // TODO Auto-generated method stub
        return executeKeyDown(keyCode);
    }
    
    private boolean executeKeyDown(int keyCode)
    {
//        Log.i("spinner_test", "execute key down, scanIndex = "+
//                scanIndex+", selectedIndex = "+selectedIndex+
//                ", isExpanded = "+isExpanded+", keycode = "+keyCode);
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
                if (isExpanded)
                    setSelectedIndex(scanIndex);
                else
                    setExpanded(true);
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                if(isExpanded)
                {
                    if(scanIndex > 0)
                        scanForward();
                    return true;
                }
                else
                    return false;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if(isExpanded)
                {
                    if(scanIndex < dataList.size() - 1)
                        scanNext();
                    return true;
                }
                else{
                    return false;
                }
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if(isExpanded)
                    return true;
                else
                    return false;
            case KeyEvent.KEYCODE_BACK:
                if (isExpanded)
                {
                    setExpanded(false);
                    return true;
                }
                return false;
        }
        return false;
    }

    private void scanForward(){
        setScanIndex(scanIndex - 1);
        if(scrollOffset > scanIndex * itemHeight)
        {
            scrollOffset = scanIndex * itemHeight;
            scroll.scrollTo(0, scrollOffset);
        }
    }
    private void scanNext()
    {
        setScanIndex(scanIndex + 1);
        if(scrollOffset < (scanIndex - maxShowCount + 1) * itemHeight)
        {
            scrollOffset = (scanIndex - maxShowCount + 1) * itemHeight;
            scroll.scrollTo(0, scrollOffset);
        }
    }
    @SuppressWarnings("deprecation")
	private void setScanIndex(int index)
    {
        if (index < 0 || index >= dataList.size())
            return;
        if(scanIndex != index)
        {
            TextView oldText = (TextView) scrollLayout.findViewWithTag(scanIndex);
            if(oldText != null){
                oldText.setBackgroundDrawable(null);
                oldText.setTextColor(Color.WHITE);
                oldText.clearFocus();
            }
        }
        
        scanIndex = index;
        TextView newText = (TextView) scrollLayout.findViewWithTag(scanIndex);
        newText.setBackgroundResource(itemSelectedRes);
        newText.setTextColor(focusTextColor);
        newText.setPadding(paddingX, 0, paddingX, 0);
        newText.requestFocus();
    }

    // 下面都是设置文字大小，text的Padding等等，为了满足UI要求的设�?
    public void setTextSize(int size)
    {
        this.textSize = size;
        titleText.setTextSize(textSize);
        for (int i = 0; i < scrollLayout.getChildCount(); i++)
        {
            TextView itemText = (TextView) scrollLayout.findViewWithTag(i);
            itemText.setTextSize(textSize);
        }
    }

    public void setTextPadding(int paddingX)
    {
        this.paddingX = paddingX;
        titleText.setPadding(paddingX, 0, paddingX, 0);
        for (int i = 0; i < scrollLayout.getChildCount(); i++)
        {
            TextView itemText = (TextView) scrollLayout.findViewWithTag(i);
            itemText.setPadding(paddingX, 0, paddingX, 0);
        }
        scrollLayout.setPadding(paddingX, 0, paddingX, 0);
    }

    public void setTitleBgRes(int resId, int focusedResId)
    {
        this.titleBgRes = resId;
        this.titleBgSelectedRes = focusedResId;
    }

    public void setItemsBgRes(int dropDownBgRes, int itemSelectedResId)
    {
        this.dropDownBgRes = dropDownBgRes;
        this.itemSelectedRes = itemSelectedResId;
        scroll.setBackgroundResource(dropDownBgRes);
    }
    
    public void setArrowRes(int arrowRes){
        arrowImage.setImageResource(arrowRes);
    }
    
    public void setOnSelectChangeListener(OnSelectChangeListener listener){
        this.listener = listener;
    }
    
    public interface OnSelectChangeListener{
        public void onSelectedTextChange(PopupSpinner spinner, String text);
        public void onSelectedIndexChange(PopupSpinner spinner, int index);
        public void onExpanded(PopupSpinner spinner);
    }
}