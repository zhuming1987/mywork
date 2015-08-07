package com.tv.ui.ChannelSearch;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.data.TvSatelliteListData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.widgets.ItemView;
import com.tv.ui.widgets.ProgressBarCustom;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class DTVChannelSearchView extends LinearLayout{
	
    private Context context;

    private LeftViewTitleLayout titleLayout;
    private LeftViewBodyLayout bodyLayout;

    private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();

    private ImageView dtvprogranmsDivider, radioprogranmsDivider, dataprogranmsDivider, searchProgressDivider, segmentDivider;
    private ProgressBarCustom searchProgressBar;
    private TextView searchProgressText;
    private TextView channelNoText, segmentText, backHintText, dtvprogranmsText, radioprogranmsText, dataprogranmsText;
    
    private int curDtvRoute;
    
    public DTVChannelSearchView(Context context)
    {
        super(context);
        this.context = context;
        setOrientation(LinearLayout.VERTICAL);
        setFocusable(true);

        titleLayout = new LeftViewTitleLayout(context);
        this.addView(titleLayout);

        bodyLayout = new LeftViewBodyLayout(context);
        bodyLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(bodyLayout);

        int leftGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        int leftPadding = (int) (20 / div);
        
        dtvprogranmsText = newTextView(leftGravity, leftPadding);
        ItemView dtvprogranmsItem = new ItemView(context);
        dtvprogranmsItem.setTipText(R.string.channel_programs);
        dtvprogranmsItem.addRightView(dtvprogranmsText);
        bodyLayout.addView(dtvprogranmsItem);
        
        dtvprogranmsDivider = new ImageView(context);
        dtvprogranmsDivider.setBackgroundResource(R.drawable.setting_line);
        bodyLayout.addView(dtvprogranmsDivider, new LayoutParams(ItemView.width - (int) (30 / div),
                (int) (2 / div)));

        radioprogranmsText = newTextView(leftGravity, leftPadding);
        ItemView radioprogranmsItem = new ItemView(context);
        radioprogranmsItem.addRightView(radioprogranmsText);
        bodyLayout.addView(radioprogranmsItem);

        radioprogranmsDivider = new ImageView(context);
        radioprogranmsDivider.setBackgroundResource(R.drawable.setting_line);
        bodyLayout.addView(radioprogranmsDivider, new LayoutParams(ItemView.width - (int) (30 / div),
                (int) (2 / div)));
        
        dataprogranmsText = newTextView(leftGravity, leftPadding);
        ItemView dataprogranmsItem = new ItemView(context);
        dataprogranmsItem.addRightView(dataprogranmsText);
        bodyLayout.addView(dataprogranmsItem);
        
        dataprogranmsDivider = new ImageView(context);
        dataprogranmsDivider.setBackgroundResource(R.drawable.setting_line);
        bodyLayout.addView(dataprogranmsDivider, new LayoutParams(ItemView.width - (int) (30 / div),
                (int) (2 / div)));

        LayoutParams rightTextParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        LinearLayout searchProgressLayout = new LinearLayout(context);
        searchProgressLayout.setOrientation(LinearLayout.HORIZONTAL);
        searchProgressLayout.setGravity(Gravity.CENTER);
        searchProgressLayout.setPadding((int)(80 / div), 0, 0, 0);
        bodyLayout.addView(searchProgressLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        
        searchProgressBar = newProgressBar(R.drawable.progress_green, R.drawable.progressbg);
        searchProgressBar.setLayoutParams(new LayoutParams((int)(500/div),LayoutParams.WRAP_CONTENT));
        searchProgressText = newTextView();
        searchProgressText.setLayoutParams(rightTextParams);
        searchProgressLayout.addView(searchProgressBar);
        searchProgressLayout.addView(searchProgressText);
        
        searchProgressDivider = new ImageView(context);
        searchProgressDivider.setBackgroundResource(R.drawable.setting_line);
        bodyLayout.addView(searchProgressDivider, new LayoutParams(ItemView.width - (int) (30 / div),
                (int) (2 / div)));
        
        LinearLayout channelNoLayout = new LinearLayout(context);
        channelNoLayout.setOrientation(LinearLayout.HORIZONTAL);
        channelNoLayout.setPadding((int)(20 / div), 0, 0, 0);
        channelNoLayout.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        bodyLayout.addView(channelNoLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        
        channelNoText = initTextView("", (int) (36 / dipDiv));
        channelNoText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        channelNoLayout.addView(channelNoText,new LayoutParams(LayoutParams.WRAP_CONTENT,
        		LayoutParams.MATCH_PARENT));
        
        LinearLayout segmentLayout = new LinearLayout(context);
        segmentLayout.setOrientation(LinearLayout.HORIZONTAL);
        segmentLayout.setPadding(0, 0, (int)(20 / div), 0);
        segmentLayout.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        bodyLayout.addView(segmentLayout, new LayoutParams((int) (720 / div),
                LayoutParams.WRAP_CONTENT));
        
        segmentText = initTextView("", (int) (36 / dipDiv));
        segmentLayout.addView(segmentText,new LayoutParams(LayoutParams.WRAP_CONTENT,
        		LayoutParams.MATCH_PARENT));
        
        segmentDivider = new ImageView(context);
        segmentDivider.setBackgroundResource(R.drawable.setting_line);
        bodyLayout.addView(segmentDivider, new LayoutParams(ItemView.width - (int) (30 / div),
                (int) (2 / div)));
        
        LinearLayout backHinLayout = new LinearLayout(context);
        backHinLayout.setOrientation(LinearLayout.HORIZONTAL);
        backHinLayout.setPadding(0, 0, (int)(20 / div), 0);
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
    	dtvprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_dtv), data.dtvCount));
    	radioprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_radio), data.radioCount));
    	dataprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_data), data.otherCount));
    	searchProgressBar.setProgress(Integer.parseInt(data.percent));
    	searchProgressText.setText(String.format("%s%s", data.percent, "%"));
    	if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS
				|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2)
		{
    		channelNoText.setText(String.format("%s: %sMHz", context.getString(R.string.TV_CFG_FREQUENCY_SET), data.FrequencyKHz));
        	segmentText.setText(String.format("%s", TvPluginControler.getInstance().getChannelManager().getCurrentSatelliteName()));
		}else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT
				|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT2)
		{
	    	channelNoText.setText(String.format("%s: %s", context.getString(R.string.TV_CFG_CHANNEL_NO), data.channelNumber));
	    	segmentText.setText(String.format("%s: %s", context.getString(R.string.frequency_segment), data.band));
		}else {
	    	channelNoText.setText(String.format("%s: %s", context.getString(R.string.TV_CFG_CHANNEL_NO), data.channelNumber));
	    	segmentText.setText(String.format("%s: %s", context.getString(R.string.frequency_segment), data.band));
		}
    }

    private TextView newTextView()
    {
        return newTextView(Gravity.CENTER, 0);
    }

    private TextView newTextView(int gravity, int leftPadding)
    {
        TextView text = new TextView(context);
        text.setTextSize((int) (36 / dipDiv));
        text.setTextColor(Color.WHITE);
        text.setGravity(gravity);
        text.setPadding(leftPadding, 0, 0, 0);
        text.setSingleLine();
        return text;
    }

    private ProgressBarCustom newProgressBar(int barResId, int barBgResId)
    {
        ProgressBarCustom progressBar = new ProgressBarCustom(context);
        int barWidth = (int) (500 / div);
        int barHeight = (int) (15 / div);
        progressBar.setSize(barWidth, barHeight);
        progressBar.setDrawableResId(barResId, barBgResId);
        progressBar.setLayoutParams(new LayoutParams(barWidth, barHeight));
        return progressBar;
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
        TextView text = new TextView(context);
        text.setText(str);
        text.setTextSize(textSize);
        text.setTextColor(textColor);
        text.setGravity(gravity);
        return text;
    }
    private void Uiinit()
    {
    	curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
    	Log.v("zhm","GetDtvRoute == " + curDtvRoute);
    	if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS
				|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2)
		{
    		titleLayout.bindData("tv_auto_search.png", getResources().getString(R.string.TV_CFG_DVBS_AUTO_SEARCH));
		}else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT
				|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT2)
		{
			titleLayout.bindData("tv_auto_search.png", getResources().getString(R.string.TV_CFG_DVBT_AUTO_SEARCH));
		}else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBC)
		{
			titleLayout.bindData("tv_auto_search.png", getResources().getString(R.string.TV_CFG_DVBC_AUTO_SEARCH));
		}
    	
    	dtvprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_dtv), "0"));
    	radioprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_radio), "0"));
    	dataprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_data), "0"));
    	searchProgressBar.setProgress(Integer.parseInt("0"));
    	searchProgressText.setText(String.format("%s%s", "0", "%"));
    	channelNoText.setText(String.format("%s: %s", context.getString(R.string.TV_CFG_CHANNEL_NO), "0"));
    	segmentText.setText(String.format("%s: %s", context.getString(R.string.frequency_segment), "VHF-L"));
    }
}
