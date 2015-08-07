package com.tv.ui.widgets;

import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemView extends LinearLayout
{
	private static float div = TvUIControler.getInstance().getResolutionDiv();
    private static float dipDiv = TvUIControler.getInstance().getDipDiv();
    public static int width = (int) (845 / div);
    public static int height = (int) (100 / div);
    private static int tipWidth = (int) (200 / div);
    private static int borderPadding = (int) (15 / div);
    private static int tipItemMargin = (int) (42 / div);
    private static int tipTextSize = (int) (36 / dipDiv);

    private TextView tipText;
    private View firstRightView;

    public ItemView(Context context)
    {
        super(context);
        this.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        this.setPadding(borderPadding, borderPadding, borderPadding, borderPadding);
        this.setLayoutParams(new LayoutParams(width, height));

        tipText = new TextView(context);
        tipText.setTextSize(tipTextSize);
        tipText.setTextColor(Color.WHITE);
        tipText.getPaint().setAntiAlias(true);
        tipText.setSingleLine(true);
        tipText.setEllipsize(TruncateAt.MARQUEE);
        tipText.setMarqueeRepeatLimit(-1);
        tipText.setSelected(true);
        tipText.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        this.addView(tipText, new LayoutParams(tipWidth, LayoutParams.WRAP_CONTENT));
    }

    public void setTipText(String tipStr)
    {
        tipText.setText(tipStr);
    }

    public void setTipText(int tipStrResId)
    {
        tipText.setText(tipStrResId);
    }

    public void setTipTextSize(int size)
    {
        tipText.setTextSize(size);
    }

    public void setTipTextColor(int color)
    {
        tipText.setTextColor(color);
    }

    public void addRightView(View view)
    {
        if (firstRightView == null)
        {
            firstRightView = view;
            LayoutParams params = (LayoutParams) firstRightView.getLayoutParams();
            if (params == null)
            {
                params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            }
            params.leftMargin = tipItemMargin;
            this.addView(firstRightView, params);
        } else
        {
            this.addView(view);
        }
    }
}
