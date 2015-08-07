package com.tv.ui.SourceInfo;

import com.tv.app.TvUIControler;
import com.tv.framework.data.AtvSourceInfoData;
import com.tv.framework.data.DtvSourceInfoData;
import com.tv.framework.define.TvConfigTypes.TvEnumProgramCountType;
import com.tv.plugin.TvPluginControler;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChannelInfoView extends LinearLayout
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip =TvUIControler.getInstance().getDipDiv();
	private Context context;

    private LinearLayout layout;
    private TextView titleText;
    private LinearLayout detailLayout;
    private int maxDetailCount = 3;

    private int titleTextSize = (int) (100 / dip);
    private int detailTextSize = (int) (48 / dip);
    private int titleTextColor = Color.WHITE;
    private int titleTextColorSkipped = Color.RED;
    private int detailTextColor = Color.WHITE;

    private TextPaint titlePaint, detailPaint;

    public ChannelInfoView(Context context)
    {
        super(context);
        this.context = context;
        setFocusable(false);

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.RIGHT);
        LayoutParams params = new LayoutParams((int)(700 / div), LayoutParams.WRAP_CONTENT);
        this.addView(layout, params);

        titleText = newTextView(titleTextSize, titleTextColor, 0, 0);
        titlePaint = titleText.getPaint();
        layout.addView(titleText, new LayoutParams(LayoutParams.WRAP_CONTENT, (int)(150 / div)));

        detailLayout = new LinearLayout(context);
        detailLayout.setGravity(Gravity.RIGHT);
        detailLayout.setOrientation(LinearLayout.VERTICAL);
        detailLayout.setPadding(0, 0, 0, 0);
        layout.addView(detailLayout, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < maxDetailCount; i++) // 最多显示maxDetailCount个detail信息
        {
            TextView detailText = newTextView(detailTextSize, detailTextColor, 0, 0);
            if (detailPaint == null)
                detailPaint = detailText.getPaint();
            detailText.setTag(i);
            detailText.setVisibility(View.GONE);
            detailLayout.addView(detailText, new LayoutParams(LayoutParams.WRAP_CONTENT, (int)(78 / div)));
        }
    }

    private TextView newTextView(int textSize, int textColor, int xPadding, int yPadding)
    {
        TextView text = new TextView(context);
        text.setTextSize(textSize);
        text.setTextColor(textColor);
        text.setPadding(xPadding, yPadding, xPadding, yPadding);
        text.setSingleLine();
        text.setGravity(Gravity.RIGHT|Gravity.CENTER);
        return text;
    }

    public void atvUpdate(AtvSourceInfoData data)
    {
        this.setVisibility(View.VISIBLE);
        int atvprogramcount = TvPluginControler.getInstance().getChannelManager().getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_ATV.ordinal());
		Log.v("zhm","ATV Source TopInfo atvprogramcount = " + atvprogramcount);
        Log.v("zhm","ATV data.channelNumber == " + data.channelNumber);
        titleText.setText(data.channelNumber+"");
        if(atvprogramcount > 0)
        {
            if (data.skipFlag)
                titleText.setTextColor(titleTextColorSkipped);
            else
                titleText.setTextColor(titleTextColor);
        }
        else 
        {
        	titleText.setTextColor(titleTextColorSkipped);
		}

        this.invalidate();
        layout.invalidate();
    }
    
    public void dtvUpdate(DtvSourceInfoData data)
    {
        this.setVisibility(View.VISIBLE);
        int dtvprogramcount = TvPluginControler.getInstance().getChannelManager().getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_DTV.ordinal());
		Log.v("zhm","DTV Source TopInfo atvprogramcount = " + dtvprogramcount);
        Log.v("zhm","DTV data.channelNumber == " + data.channelNumber);
        if(data.channelNumber.equals("0"))
        {
        	titleText.setText("1");
        	titleText.setTextColor(titleTextColorSkipped);
        }
        else
        {
            titleText.setText(data.channelNumber+"");
            if (data.skipFlag)
                titleText.setTextColor(titleTextColorSkipped);
            else
                titleText.setTextColor(titleTextColor);
		}
        
        this.invalidate();
        layout.invalidate();
    }
}
