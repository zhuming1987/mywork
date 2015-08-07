package com.tv.ui.network.defs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WifiListView extends LinearLayout
{
    private Context context;
    private ImageView line;

    public WifiListView(Context context)
    {
        super(context);
        this.context = context;
        createView();
    }

    public WifiListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        createView();
    }

    private void createView()
    {
        this.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutLp);
    }

    public void addLine()
    {
        line = new ImageView(context);
    }

}
