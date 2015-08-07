package com.tv.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class SelectMarqueeTextView extends TextView
{

    public SelectMarqueeTextView(Context context)
    {
        super(context);
    }

    public SelectMarqueeTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SelectMarqueeTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused()
    {
        return isSelected();
    }
}
