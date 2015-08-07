package com.tv.ui.widgets;

import com.tv.app.TvUIControler;

import android.content.Context;
import android.widget.LinearLayout;

public abstract class ConfigView extends LinearLayout
{
    protected static float div = TvUIControler.getInstance().getResolutionDiv();
    protected static float dipDiv = TvUIControler.getInstance().getDipDiv();

    public static int width = (int) (485 / div);
    public static int height = (int) (60 / div);
    protected static float selectedScale = 1.0f;

    public ConfigView(Context context)
    {
        super(context);
        this.setLayoutParams(new LayoutParams(width, height));
    }

    public abstract void setSelected(boolean selected);
}
