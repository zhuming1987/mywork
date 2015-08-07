package com.tv.ui.installguide;
/**
 * Copyright (C) 2012 The SkyTvOS Project
 *
 * Version     Date           Author
 * ─────────────────────────────────────
 *           2012-8-22         admin
 *
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class AlwaysMarqueeTextView extends TextView
{
    public AlwaysMarqueeTextView(Context context){
        super(context);
    }

    public AlwaysMarqueeTextView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    public AlwaysMarqueeTextView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
    }

    public boolean isFocused(){
        return true;
    }
    @Override
    protected void onFocusChanged(boolean focused, int direction,
       Rect previouslyFocusedRect) {
    }
}
