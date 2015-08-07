package com.tv.ui.NoSignal;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import com.tv.app.R;
import com.tv.app.TvUIControler;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoSignalTextView extends LinearLayout
{
    private LinearLayout textDialog;
    private TextView txtTitle1;
    private TextView txtTitle2;
    private ScheduledThreadPoolExecutor aniTimer;
    public int width = 0;
    public int height = 0;
    private float resolutionDiv;
    private final int DIALOG_WIDTH = 414;
    private final int DIALOG_HEIGHT = 240;
    float dpidiv;

    public NoSignalTextView(Context context)
    {
        super(context);
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
        width = TvUIControler.getInstance().getDisplayWidth();
        height = TvUIControler.getInstance().getDisplayHeight();
        dpidiv = TvUIControler.getInstance().getDipDiv();
        switch (width)
        {
            case 3840:
                resolutionDiv = 0.5f;
                break;
            case 1920:
                resolutionDiv = 1;
                break;
            case 1366:
                resolutionDiv = 1.4f;
                break;
            case 1280:
                resolutionDiv = 1.5f;
                break;
            default:
                resolutionDiv = 1;
                break;
        }
        setOrientation(HORIZONTAL);
        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textDialog = new LinearLayout(context);
        //textDialog.setBackgroundResource(R.drawable.dialog_bg);
        textDialog.setGravity(Gravity.CENTER);
        textDialog.setOrientation(LinearLayout.VERTICAL);
        textDialog.setLayoutParams(new LayoutParams((int) (DIALOG_WIDTH / resolutionDiv),
                (int) (DIALOG_HEIGHT / resolutionDiv)));
        txtTitle1 = new Text(context, (int)(72/dpidiv));
        txtTitle1.setText(context.getString(R.string.TV_CFG_NO_SIGNAL));
        txtTitle2 = new Text(context, (int)(38/dpidiv));
        txtTitle2.setText(" ");
        textDialog.addView(txtTitle1);
        // textDialog.addView(txtTitle2);
        addView(textDialog);
    }
    
    public void updateView(String data){
    	txtTitle1.setText(data);
    }

    public void showTitleList(List<String> txtList)
    {

        if (txtList != null)
        {
            int textCount = 0;
            if (txtList.size() == 1)
            {
                textCount = textDialog.getChildCount();
                if (textCount == 0)
                {
                    textDialog.addView(txtTitle1);
                } else if (textCount > 1)
                {
                    textDialog.removeAllViews();
                    textDialog.addView(txtTitle1);
                }
                txtTitle1.setText(txtList.get(0));
            } else if (txtList.size() > 1)
            {
                textCount = textDialog.getChildCount();
                if (textCount == 0)
                {
                    textDialog.addView(txtTitle1);
                    textDialog.addView(txtTitle2);
                } else if (textCount == 1)
                {
                    textDialog.addView(txtTitle2);
                }
                txtTitle1.setText(txtList.get(0));
                txtTitle2.setText(txtList.get(1));
            }
            setVisibility(View.VISIBLE);
        }
    }

    public void updateTitle(List<String> txtList)
    {
        if (txtList != null)
        {
            if (txtList.size() == 1)
            {
                txtTitle1.setText(txtList.get(0));
            } else if (txtList.size() > 1)
            {
                txtTitle1.setText(txtList.get(0));
                txtTitle2.setText(txtList.get(1));
            }
        }
    }

    private class Text extends TextView
    {
        public Text(Context context, int textsize)
        {
            super(context);
            // TODO Auto-generated constructor stub
            setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            setGravity(Gravity.CENTER);
            setTextSize((int) (textsize / resolutionDiv / dpidiv));
            setTextColor(Color.WHITE);
            setShadowLayer(4, 2, 1, Color.BLACK);
        }
    }
}
