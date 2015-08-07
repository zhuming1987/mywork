package com.tv.ui.EPGManage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.tv.app.R;
import com.tv.ui.widgets.ConfigView;

@SuppressLint("ViewConstructor")
public class EPGSettigButtonView extends ConfigView
{
    private Button button;
    private static int textSize = (int) (36 / dipDiv);
    private static int textColorSelected = Color.BLACK;
    private static int textColor = Color.WHITE;

    public EPGSettigButtonView(Context context,int width, int height)
    {
        super(context);
        button = new Button(context);
        button.setTextSize(textSize);
        button.setTextColor(textColor);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundResource(R.drawable.setting_unselect_bg);
        button.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                setSelected(hasFocus);
            }
        });
        this.addView(button, new LayoutParams(width, height));
    }

    @Override
    public void setSelected(boolean selected)
    {
        if (selected)
        {
//            this.setScaleX(selectedScale);
//            this.setScaleY(selectedScale);
            button.setBackgroundResource(R.drawable.blue_sel_bg);
            button.setTextColor(textColorSelected);
        } else
        {
//            this.setScaleX(1.0f);
//            this.setScaleY(1.0f);
            button.setBackgroundResource(R.drawable.setting_unselect_bg);
            button.setTextColor(textColor);
        }
    }

    public void setOnClickListener(OnClickListener listener)
    {
        button.setOnClickListener(listener);
    }

    public void setButtonText(String textStr)
    {
        button.setText(textStr);
    }

    public void setButtonText(int textResId)
    {
        button.setText(textResId);
    }
}
