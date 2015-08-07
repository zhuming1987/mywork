package com.tv.ui.widgets;

import com.tv.app.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputConfigView extends ConfigView
{
    private EditText text;
    private static int textSize = (int) (32 / dipDiv);
    private static int hintTextColor = Color.parseColor("#9E9E9E");
    private static int textColor = Color.WHITE;
    private Context context;
    
    public InputConfigView(Context context)
    {
        super(context);
        this.context = context;
        text = new EditText(context);
        text.setTextSize(textSize);
        text.setTextColor(textColor);
        text.setHintTextColor(hintTextColor);
        text.setGravity(Gravity.CENTER);
        text.setBackgroundResource(R.drawable.inputbg);
        text.setSelectAllOnFocus(true);
        text.setInputType(InputType.TYPE_CLASS_NUMBER);
        text.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                setSelected(hasFocus);
            }
        });
        text.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                /*EditText text = (EditText) v;
                text.setInputType(InputType.TYPE_CLASS_NUMBER);

                InputMethodManager m = (InputMethodManager) InputConfigView.this.context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                m.hideSoftInputFromInputMethod(text.getWindowToken(), 0);*/
            }
        });
        text.setOnKeyListener(new OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                EditText text = (EditText) v;
                text.setInputType(InputType.TYPE_CLASS_TEXT);
                return false;
            }
        });
        InputMethodManager m = (InputMethodManager) InputConfigView.this.context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        //m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        m.hideSoftInputFromWindow(text.getWindowToken(), 0);
        Log.i("gky","###################################hide input");
        this.addView(text, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void setSelected(boolean selected)
    {
        if (selected)
        {
            this.setScaleX(selectedScale);
            this.setScaleY(selectedScale);
            text.setBackgroundResource(R.drawable.inputbg_focus);
            text.setTypeface(Typeface.DEFAULT_BOLD);
        } else
        {
            this.setScaleX(1.0f);
            this.setScaleY(1.0f);
            text.setBackgroundResource(R.drawable.inputbg);
            text.setTypeface(Typeface.DEFAULT);
        }
    }

    public String getText()
    {
        return text.getText().toString();
    }

    public void setText(String textStr)
    {
        text.setText(textStr);
    }

    public void setHintText(String textStr)
    {
        text.setHint(textStr);
    }

    public void setHintText(int textResId)
    {
        text.setHint(textResId);
    }

    public void setInputFilter(InputFilter[] filters)
    {
        text.setFilters(filters);
    }

    public EditText getInsideTextView()
    {
        return text;
    }
}
