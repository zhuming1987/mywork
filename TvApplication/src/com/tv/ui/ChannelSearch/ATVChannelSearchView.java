package com.tv.ui.ChannelSearch;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.utils.LogicTextResource;
import com.tv.ui.widgets.ProgressBarCustom;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ATVChannelSearchView extends LinearLayout{
	
    private Context mContext;

    private LeftViewTitleLayout titleLayout;
    private LeftViewBodyLayout bodyLayout;

    private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();

    private ProgressBarCustom progressBar;
    private FrameLayout manualTipLayout;
    private TextView frequencyText, segmentText, channelCountText;
    private TextView backHintText;

    boolean canProcessKey = false;

    public ATVChannelSearchView(Context context)
    {
        super(context);
        mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        setFocusable(true);
        titleLayout = new LeftViewTitleLayout(context);
        this.addView(titleLayout);

        bodyLayout = new LeftViewBodyLayout(context);
        bodyLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(bodyLayout);

        LinearLayout chcountLayout = new LinearLayout(context);
        chcountLayout.setOrientation(LinearLayout.HORIZONTAL);
        chcountLayout.setGravity(Gravity.CENTER);
        bodyLayout.addView(chcountLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        
        channelCountText = initTextView("", (int) (36 / dipDiv));
        chcountLayout.addView(channelCountText,new LayoutParams(LayoutParams.WRAP_CONTENT,
        		LayoutParams.MATCH_PARENT));
        
        int leftGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        int rightGravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

        FrameLayout frameLayout = new FrameLayout(context);
        LayoutParams frameParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        frameParams.topMargin = (int) (40 / div);
        bodyLayout.addView(frameLayout, frameParams);

        manualTipLayout = new FrameLayout(context);
        manualTipLayout.setBackgroundColor(Color.parseColor("#aa3f3d39"));
        TextView leftArrowText = initTextView("<", (int) (30 / dipDiv));
        TextView rightArrowText = initTextView(">", (int) (30 / dipDiv));
        manualTipLayout.addView(leftArrowText, new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, leftGravity));
        manualTipLayout.addView(rightArrowText, new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, rightGravity));
        manualTipLayout.setVisibility(View.INVISIBLE);
        frameLayout.addView(manualTipLayout, new FrameLayout.LayoutParams((int) (815 / div),
                (int) (70 / div), Gravity.CENTER));

        progressBar = new ProgressBarCustom(context);
        int barWidth = (int) (670 / div);
        int barHeight = (int) (15 / div);
        FrameLayout.LayoutParams barLp = new FrameLayout.LayoutParams(barWidth, barHeight,
                Gravity.CENTER);
        progressBar.setSize(barWidth, barHeight);
        frameLayout.addView(progressBar, barLp);
        
        LinearLayout frequencyLayout = new LinearLayout(context);
        frequencyLayout.setOrientation(LinearLayout.HORIZONTAL);
        frequencyLayout.setPadding((int)(25 / div), 0, 0, 0);
        frequencyLayout.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        bodyLayout.addView(frequencyLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        
        frequencyText = initTextView("", (int) (36 / dipDiv));
        frequencyLayout.addView(frequencyText,new LayoutParams(LayoutParams.WRAP_CONTENT,
        		LayoutParams.MATCH_PARENT));

        LinearLayout segmentLayout = new LinearLayout(context);
        segmentLayout.setOrientation(LinearLayout.HORIZONTAL);
        segmentLayout.setPadding(0, 0, (int)(25 / div), 0);
        segmentLayout.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        bodyLayout.addView(segmentLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        
        segmentText = initTextView("", (int) (36 / dipDiv));
        segmentLayout.addView(segmentText,new LayoutParams(LayoutParams.WRAP_CONTENT,
        		LayoutParams.MATCH_PARENT));
        
        LinearLayout backHinLayout = new LinearLayout(context);
        backHinLayout.setOrientation(LinearLayout.HORIZONTAL);
        backHinLayout.setPadding(0, 0, (int)(25 / div), 0);
        backHinLayout.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        bodyLayout.addView(backHinLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        
        backHintText = initTextView(context.getString(R.string.back_hint), (int) (32 / dipDiv),
                Color.parseColor("#C1C1C1"), Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        backHintText.setPadding(0, (int) (20 / div), 0, 0);
        backHinLayout.addView(backHintText,new LayoutParams(LayoutParams.WRAP_CONTENT,
        		LayoutParams.MATCH_PARENT));
        
        Uiinit();
    }

    public void updateView(ChannelSearchData data)
    {
    	//Log.i("gky", "ATVChannelSearchView::updateView:atvCount is " + data.atvCount
    		//	+" Frequency is " + data.Frequency + " band is " + data.band);
    	channelCountText.setText(String.format("%s: %s", mContext.getString(R.string.program_tv),
                data.atvCount));
        progressBar.setProgress(Integer.parseInt(data.percent));
        frequencyText.setText(String.format("%s: %s", mContext.getString(R.string.frequency),
                data.Frequency));
        segmentText.setText(String.format("%s: %s", mContext.getString(R.string.frequency_segment),
                data.band));
    }

    private TextView initTextView(String str, int textSize)
    {
        return initTextView(str, textSize, Color.WHITE);
    }

    private TextView initTextView(String str, int textSize, int textColor)
    {
        return initTextView(str, textSize, textColor, Gravity.CENTER);
    }

    private TextView initTextView(String str, int textSize, int textColor, int gravity)
    {
        TextView text = new TextView(mContext);
        text.setText(str);
        text.setTextSize(textSize);
        text.setTextColor(textColor);
        text.setGravity(gravity);
        return text;
    }
    private void Uiinit()
    {
//    	titleLayout.bindData("tv_auto_search.png", "ATV AutoSearch");
    	titleLayout.bindData("tv_auto_search.png", LogicTextResource.getInstance(TvContext.context).getText(R.string.auto_search));
    	
    	channelCountText.setText(String.format("%s: %s", mContext.getString(R.string.program_tv),
                "0"));
        progressBar.setProgress(Integer.parseInt("0"));
        frequencyText.setText(String.format("%s: %s%s", mContext.getString(R.string.frequency),
                "48.25","MHz"));
        segmentText.setText(String.format("%s: %s", mContext.getString(R.string.frequency_segment),
                "VHF-L"));
    }
}