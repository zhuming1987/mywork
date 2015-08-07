package com.tv.ui.widgets;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.widget.TextView;

public class AlwaysMarqueeTextView extends TextView
{
    public AlwaysMarqueeTextView(Context context)
    {
        super(context);
        this.setSingleLine();
        this.setEllipsize(TruncateAt.MARQUEE);
        this.setMarqueeRepeatLimit(-1);
    }

    @Override
    public boolean isFocused()
    {
        // TODO Auto-generated method stub
        return true;
    }
}
