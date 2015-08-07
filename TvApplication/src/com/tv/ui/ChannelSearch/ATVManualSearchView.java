package com.tv.ui.ChannelSearch;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.define.TvSearchTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.widgets.ProgressBarCustom;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ATVManualSearchView extends LinearLayout{

	private Context mContext;

    private LeftViewTitleLayout titleLayout;
    private LeftViewBodyLayout bodyLayout;

    private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();

    private ProgressBarCustom progressBar;
    private FrameLayout manualTipLayout;
    private TextView frequencyText, segmentText, channelCountText;
    private TextView backHintText;
    private TextView PressHint;
    private TextView LongPressHint;
   
    boolean canProcessKey = false;
    
	public ATVManualSearchView(Context context)
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
        
        LinearLayout PressHintLayout = new LinearLayout(context);
        PressHintLayout.setOrientation(LinearLayout.HORIZONTAL);
        PressHintLayout.setPadding(0, 0, (int)(25 / div), 0);
        PressHintLayout.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        bodyLayout.addView(PressHintLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        PressHint = initTextView(context.getString(R.string.start_fine_search), (int) (32 / dipDiv),
                Color.parseColor("#C1C1C1"), Gravity.LEFT| Gravity.CENTER_VERTICAL);
        PressHint.setPadding(0, (int) (20 / div), 0, 0);
        PressHintLayout.addView(PressHint,new LayoutParams(LayoutParams.WRAP_CONTENT,
        		LayoutParams.MATCH_PARENT));
        
        LinearLayout LongPressHintLayout = new LinearLayout(context);
        LongPressHintLayout.setOrientation(LinearLayout.HORIZONTAL);
        LongPressHintLayout.setPadding(0, 0, (int)(25 / div), 0);
        LongPressHintLayout.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        bodyLayout.addView(LongPressHintLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        LongPressHint = initTextView(context.getString(R.string.start_manual_search), (int) (32 / dipDiv),
                Color.parseColor("#C1C1C1"), Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LongPressHint.setPadding(0, (int) (20 / div), 0, 0);
        LongPressHintLayout.addView(LongPressHint,new LayoutParams(LayoutParams.WRAP_CONTENT,
        		LayoutParams.MATCH_PARENT));
        
        LinearLayout backHinLayout = new LinearLayout(context);
        backHinLayout.setOrientation(LinearLayout.HORIZONTAL);
        backHinLayout.setPadding(0, 0, (int)(25 / div), 0);
        backHinLayout.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        bodyLayout.addView(backHinLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        backHintText = initTextView(context.getString(R.string.back_hint), (int) (32 / dipDiv),
                Color.parseColor("#C1C1C1"), Gravity.LEFT | Gravity.CENTER_VERTICAL);
        backHintText.setPadding(0, (int) (20 / div), 0, 0);
        backHinLayout.addView(backHintText,new LayoutParams(LayoutParams.WRAP_CONTENT,
        		LayoutParams.MATCH_PARENT));
        Uiinit();
    }

	public void updateView(ChannelSearchData data) {
		// TODO Auto-generated method stub
		float progress = (float)(data.FrequencyKHz - TvSearchTypes.ATV_MIN_FREQ) / (TvSearchTypes.ATV_MAX_FREQ - TvSearchTypes.ATV_MIN_FREQ) * 100;
		progressBar.setProgress((int)progress);
        frequencyText.setText(String.format("%s: %s", mContext.getString(R.string.frequency),
                data.Frequency));
        segmentText.setText(String.format("%s: %s", mContext.getString(R.string.frequency_segment),
                data.band));
	}
	
	private void Uiinit()
    {
    	String band = null;
    	int chnumber = TvPluginControler.getInstance().getChannelManager().getCurrentChannelNumber() + 1;
    	int frequencyKHz = TvPluginControler.getInstance().getChannelManager().getAtvCurrentFrequency();
    	if (frequencyKHz < TvSearchTypes.VHF_HIGH_FREQ) {
			band = TvSearchTypes.LOW_FREQ;
		} else if (frequencyKHz > TvSearchTypes.VHF_HIGH_FREQ
				&& frequencyKHz < TvSearchTypes.UHF_LOW_FREQ) {
			band = TvSearchTypes.MID_FREQ;
		} else if (frequencyKHz > TvSearchTypes.UHF_LOW_FREQ) {
			band = TvSearchTypes.HIGH_FREQ;
		}
    	float progress = (float)(frequencyKHz - TvSearchTypes.ATV_MIN_FREQ) / (TvSearchTypes.ATV_MAX_FREQ - TvSearchTypes.ATV_MIN_FREQ) * 100;
    	titleLayout.bindData("tv_auto_search.png", "ATV ManualSearch");
    	channelCountText.setText(String.format("%s: %s", mContext.getString(R.string.program_tv),
                "" + chnumber));
        progressBar.setProgress((int)(progress));
        frequencyText.setText(String.format("%s: %s", mContext.getString(R.string.frequency),
                "" + (frequencyKHz / 1000.0))+"MHz");
        segmentText.setText(String.format("%s: %s", mContext.getString(R.string.frequency_segment),
        		band));
        TvPluginControler.getInstance().getParentalControlManager().unLockChannel();
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
    
    public void updatePrograms()
    {
    	int frequencyKHz = TvPluginControler.getInstance().getChannelManager().getAtvCurrentFrequency();
    	Log.v("wxj","frequencyKHz="+frequencyKHz);
    	float progress = (float)(frequencyKHz - TvSearchTypes.ATV_MIN_FREQ) / (TvSearchTypes.ATV_MAX_FREQ - TvSearchTypes.ATV_MIN_FREQ) * 100;
        progressBar.setProgress((int)(progress));
        frequencyText.setText(String.format("%s: %s", mContext.getString(R.string.frequency),
               "" + (frequencyKHz / 1000.0))+"MHz"); 
    }
}
