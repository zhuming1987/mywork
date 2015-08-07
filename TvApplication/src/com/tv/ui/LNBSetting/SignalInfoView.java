package com.tv.ui.LNBSetting;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.data.SignalInfoData;
import com.tv.ui.widgets.ItemView;
import com.tv.ui.widgets.ProgressBarCustom;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SignalInfoView extends LinearLayout{

	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dip = TvUIControler.getInstance().getDipDiv();
    
    private Context context;
    
	private ProgressBarCustom signalQualityProgressBar;
    private ProgressBarCustom signalStrengthProgressBar;
    private TextView signalQualityText;
    private TextView signalStrengthText;
  
	public SignalInfoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setBackgroundColor(Color.rgb(48, 49, 51));
		this.setGravity(Gravity.CENTER_VERTICAL);
        
        LayoutParams itemParams = new LayoutParams((int)(960/div),LayoutParams.WRAP_CONTENT);
        
        signalQualityProgressBar = newProgressBar(R.drawable.progress_green, R.drawable.progress_blue);
        signalQualityProgressBar.setProgress(0);
        signalQualityText = newTextView();
        signalQualityText.setText("0%");
        ItemView qualityItem = new ItemView(context);
        qualityItem.setTipText(R.string.str_lnb_signal_quality);
        qualityItem.addRightView(signalQualityProgressBar);
        qualityItem.addRightView(signalQualityText);
        this.addView(qualityItem,itemParams);
        
        signalStrengthProgressBar = newProgressBar(R.drawable.progress_orange,R.drawable.progress_blue);
        signalStrengthProgressBar.setProgress(0);
        signalStrengthText = newTextView();
        signalStrengthText.setText("0%");
        ItemView strengthItem = new ItemView(context);
        strengthItem.setTipText(R.string.str_lnb_signal_strength);
        strengthItem.addRightView(signalStrengthProgressBar);
        strengthItem.addRightView(signalStrengthText);
        this.addView(strengthItem,itemParams);
	}
	
	public void updateView(ChannelSearchData data)
    {
		Log.v("zhm","LNBSettingView -->> SignalInfoView -->>updateView -> " + data.signalQuality + data.signalStrength);
    	signalQualityProgressBar.setProgress(Integer.parseInt(data.signalQuality));
    	signalStrengthProgressBar.setProgress(Integer.parseInt(data.signalStrength));
    	signalQualityText.setText(data.signalQuality + "%");
    	signalStrengthText.setText(data.signalStrength + "%");
    }
	
	private ProgressBarCustom newProgressBar(int barResId, int barBgResId)
    {
        ProgressBarCustom progressBar = new ProgressBarCustom(context);
        int barWidth = (int) (410 / div);
        int barHeight = (int) (15 / div);
        progressBar.setSize(barWidth, barHeight);
        progressBar.setDrawableResId(barResId, barBgResId);
        progressBar.setLayoutParams(new LayoutParams(barWidth, barHeight));
        return progressBar;
    }

	private TextView newTextView()
    {
        return newTextView(Gravity.CENTER, 0);
    }
	
	private TextView newTextView(int gravity, int leftPadding)
    {
        TextView text = new TextView(context);
        text.setTextSize((int) (36 / dip));
        text.setTextColor(Color.WHITE);
        text.setGravity(gravity);
        text.setPadding(leftPadding, 0, 0, 0);
        text.setSingleLine();
        text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        return text;
    }
}
