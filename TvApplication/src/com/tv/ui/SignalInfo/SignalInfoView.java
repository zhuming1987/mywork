package com.tv.ui.SignalInfo;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.SignalInfoData;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.utils.LogicTextResource;
import com.tv.ui.widgets.ItemView;
import com.tv.ui.widgets.ProgressBarCustom;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SignalInfoView extends LinearLayout{

	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();
    
    private Context context;
    
	private ProgressBarCustom signalQualityProgressBar;
    private ProgressBarCustom signalIntensityProgressBar;
    private TextView signalQualityText;
    private TextView signalIntensityText;
    private TextView netWorkText;
    private TextView tsIDText;
    private TextView serverText;
    private TextView modulationText;
    private TextView backHintText;
    
    private ImageView firstLineDivider;
    
    private LeftViewTitleLayout titleLayout;
    private LeftViewBodyLayout bodyLayout;
    
	public SignalInfoView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.setOrientation(LinearLayout.VERTICAL);

        titleLayout = new LeftViewTitleLayout(context);
        this.addView(titleLayout);

        bodyLayout = new LeftViewBodyLayout(context);
        bodyLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(bodyLayout);
        
        firstLineDivider = new ImageView(context);
        firstLineDivider.setBackgroundResource(R.drawable.setting_line);
        bodyLayout.addView(firstLineDivider, new LayoutParams(ItemView.width - (int) (30 / div),
                (int) (2 / div)));
        
        LayoutParams rightTextParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        
        signalQualityProgressBar = newProgressBar(R.drawable.progress_blue, R.drawable.progressbg);
        signalQualityText = newTextView();
        signalQualityText.setLayoutParams(rightTextParams);
        ItemView fistItem = new ItemView(context);
        fistItem.setTipText(R.string.signal_quality);
        fistItem.addRightView(signalQualityProgressBar);
        fistItem.addRightView(signalQualityText);
        bodyLayout.addView(fistItem, true);
        
        signalIntensityProgressBar = newProgressBar(R.drawable.progress_orange,
                R.drawable.progressbg);
        signalIntensityText = newTextView();
        signalIntensityText.setLayoutParams(rightTextParams);
        ItemView secondItem = new ItemView(context);
        secondItem.setTipText(R.string.signal_strength);
        secondItem.addRightView(signalIntensityProgressBar);
        secondItem.addRightView(signalIntensityText);
        bodyLayout.addView(secondItem, true);
        
        netWorkText = newTextView(Gravity.LEFT,(int) (30 / div));
        ItemView thirdItem = new ItemView(context);
        thirdItem.setTipText(R.string.signal_network);
        thirdItem.addRightView(netWorkText);
        bodyLayout.addView(thirdItem, true);
        
        tsIDText = newTextView(Gravity.LEFT,(int) (30 / div));
        ItemView fourthItem = new ItemView(context);
        fourthItem.setTipText(R.string.signal_tsid);
        fourthItem.addRightView(tsIDText);
        bodyLayout.addView(fourthItem, true);
        
        serverText = newTextView(Gravity.LEFT,(int) (30 / div));
        ItemView fifthItem = new ItemView(context);
        fifthItem.setTipText(R.string.signal_server);
        fifthItem.addRightView(serverText);
        bodyLayout.addView(fifthItem, true);
        /* 
        *delete by chenc
        modulationText = newTextView(Gravity.LEFT,(int) (30 / div));
        ItemView sixthItem = new ItemView(context);
        sixthItem.setTipText(R.string.signal_modulation);
        sixthItem.addRightView(modulationText);
        bodyLayout.addView(sixthItem, true);
        */
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
	}
	
	public void updateView(SignalInfoData data)
    {
    	Log.i("yangcheng", "SignalInfoView::" +" /" + data.signalQuality + " /" + data.signalStrength);
    	titleLayout.bindData("tv_channel_set.png", TvContext.context.getString(R.string.signal_info_title));
    	signalQualityProgressBar.setProgress(Integer.parseInt(data.signalQuality));
    	signalIntensityProgressBar.setProgress(Integer.parseInt(data.signalStrength));
    	signalQualityText.setText(String.format("%s%s", data.signalQuality, "%"));
    	signalIntensityText.setText(String.format("%s%s", data.signalStrength, "%"));
    	netWorkText.setText(data.netwrokVal);
    	tsIDText.setText(data.transportStreamVal);
    	serverText.setText(data.serviceVal);
    	//String modulationString = LogicTextResource.getInstance(TvContext.context).getText(data.modulationVal);
    	//modulationText.setText(modulationString);
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
        text.setTextSize((int) (36 / dipDiv));
        text.setTextColor(Color.WHITE);
        text.setGravity(gravity);
        text.setPadding(leftPadding, 0, 0, 0);
        text.setSingleLine();
        return text;
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
}
