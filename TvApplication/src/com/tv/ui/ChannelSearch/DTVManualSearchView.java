package com.tv.ui.ChannelSearch;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.data.SignalInfoData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvSearchTypes;
import com.tv.framework.plugin.tvfuncs.ChannelManager.DtvManualSearchListener;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.utils.InputFilterInt;
import com.tv.ui.utils.LogicTextResource;
import com.tv.ui.widgets.ButtonConfigView;
import com.tv.ui.widgets.InputConfigView;
import com.tv.ui.widgets.ItemView;
import com.tv.ui.widgets.ProgressBarCustom;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DTVManualSearchView extends LinearLayout
{
	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();
    
    private Context context;
    private LeftViewTitleLayout titleLayout;
    private LeftViewBodyLayout bodyLayout;
    private InputConfigView channelnumberInput;
    private ImageView dtvprogranmsDivider, radioprogranmsDivider, dataprogranmsDivider;
    private TextView dtvprogranmsText, radioprogranmsText, dataprogranmsText, backHintText;
    private ProgressBarCustom signalQualityProgressBar;
    private ProgressBarCustom signalIntensityProgressBar;
    private TextView signalQualityText;
    private TextView signalIntensityText;
    private ButtonConfigView startSearchButton;
    private SignalInfoData signalInfo;
    boolean mRunthread = true;
    private final static short DTV_SIGNAL_REFRESH_UI = 0x01;
    private DTVManualSearchDialog parentDialog = null;
    
    public DTVManualSearchView(Context context)
    {
        super(context);
        this.context = context;
        this.setOrientation(LinearLayout.VERTICAL);

        titleLayout = new LeftViewTitleLayout(context);
        this.addView(titleLayout);

        bodyLayout = new LeftViewBodyLayout(context);
        bodyLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        this.addView(bodyLayout);

        ItemView channelnumberItem = new ItemView(context);
        channelnumberItem.setTipText(R.string.TV_CFG_CHANNEL_NO);
        channelnumberInput = new InputConfigView(context);
        channelnumberInput.setInputFilter(new InputFilter[] { 
                new InputFilterInt(3, channelnumberInput.getInsideTextView()) });
        channelnumberItem.addRightView(channelnumberInput);
        bodyLayout.addView(channelnumberItem, true);

        int leftGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        int leftPadding = (int) (0 / div);
        
        dtvprogranmsText = newTextView(leftGravity, leftPadding);
        ItemView dtvprogranmsItem = new ItemView(context);
        dtvprogranmsItem.setTipText(R.string.channel_programs);
        dtvprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_dtv), "0"));
        dtvprogranmsItem.addRightView(dtvprogranmsText);
        bodyLayout.addView(dtvprogranmsItem);
        
        dtvprogranmsDivider = new ImageView(context);
        dtvprogranmsDivider.setBackgroundResource(R.drawable.setting_line);
        bodyLayout.addView(dtvprogranmsDivider, new LayoutParams(ItemView.width - (int) (30 / div),
                (int) (2 / div)));

        radioprogranmsText = newTextView(leftGravity, leftPadding);
        ItemView radioprogranmsItem = new ItemView(context);
        radioprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_radio), "0"));
        radioprogranmsItem.addRightView(radioprogranmsText);
        bodyLayout.addView(radioprogranmsItem);

        radioprogranmsDivider = new ImageView(context);
        radioprogranmsDivider.setBackgroundResource(R.drawable.setting_line);
        bodyLayout.addView(radioprogranmsDivider, new LayoutParams(ItemView.width - (int) (30 / div),
                (int) (2 / div)));
        
        dataprogranmsText = newTextView(leftGravity, leftPadding);
        ItemView dataprogranmsItem = new ItemView(context);
        dataprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_data), "0"));
        dataprogranmsItem.addRightView(dataprogranmsText);
        bodyLayout.addView(dataprogranmsItem);
        
        dataprogranmsDivider = new ImageView(context);
        dataprogranmsDivider.setBackgroundResource(R.drawable.setting_line);
        bodyLayout.addView(dataprogranmsDivider, new LayoutParams(ItemView.width - (int) (30 / div),
                (int) (2 / div)));
        
        ItemView buttonItem = new ItemView(context);
        startSearchButton = new ButtonConfigView(context);
        startSearchButton.setButtonText(R.string.start_search);
        startSearchButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	if(TvPluginControler.getInstance().getCilManager().isOpMode()){
            		TvUIControler.getInstance().showMniToast("Forbid to do channel tune under OP mode.\nPlease exit OP mode at first.");
    				getParentDialog().dismissUI();
            		return;
            	}
                startSearchChannel();
            }
        });
        buttonItem.addRightView(startSearchButton);
        bodyLayout.addView(buttonItem, true);
        
        LayoutParams rightTextParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        
        signalQualityProgressBar = newProgressBar(R.drawable.progress_blue, R.drawable.progressbg);
        signalQualityText = newTextView();
        signalQualityText.setLayoutParams(rightTextParams);
        ItemView fourthItem = new ItemView(context);
        fourthItem.setTipText(R.string.signal_quality);
        fourthItem.addRightView(signalQualityProgressBar);
        fourthItem.addRightView(signalQualityText);
        bodyLayout.addView(fourthItem, true);

        signalIntensityProgressBar = newProgressBar(R.drawable.progress_orange,
                R.drawable.progressbg);
        signalIntensityText = newTextView();
        signalIntensityText.setLayoutParams(rightTextParams);
        ItemView fifthItem = new ItemView(context);
        fifthItem.setTipText(R.string.signal_strength);
        fifthItem.addRightView(signalIntensityProgressBar);
        fifthItem.addRightView(signalIntensityText);
        bodyLayout.addView(fifthItem, true);

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
        mRunthread = true;
        new Thread(runnable).start();
    }

    public void updateView(ChannelSearchData data)
    {
    	Log.i("zhm", "DTVSearchHandler::handleMessage: " + data.dtvCount
				+" /" +data.radioCount + " /" + data.otherCount + " /" + data.scanstatus + " /" + data.signalQuality + " /" + data.signalStrength);
    	dtvprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_dtv), data.dtvCount));
    	radioprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_radio), data.radioCount));
    	dataprogranmsText.setText(String.format("%s: %s", context.getString(R.string.program_data), data.otherCount));
//    	signalQualityProgressBar.setProgress(Integer.parseInt(data.signalQuality));
//    	signalIntensityProgressBar.setProgress(Integer.parseInt(data.signalStrength));
//    	signalQualityText.setText(String.format("%s%s", data.signalQuality, "%"));
//    	signalIntensityText.setText(String.format("%s%s", data.signalStrength, "%"));
    }

    private void startSearchChannel()
    {
    	TvSearchTypes.CurrentDtvManualSearchChannelNum = Integer.parseInt(channelnumberInput.getText());
    	TvPluginControler.getInstance().getChannelManager().starDtvManualSearch(dtvManualSearchListener);
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
        int barWidth = (int) (410 / div);
        int barHeight = (int) (15 / div);
        progressBar.setSize(barWidth, barHeight);
        progressBar.setDrawableResId(barResId, barBgResId);
        progressBar.setProgress(0);
        progressBar.setLayoutParams(new LayoutParams(barWidth, barHeight));
        return progressBar;
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
    	int chnumber = TvPluginControler.getInstance().getChannelManager().getDvbtRFPhyNumber();
    	titleLayout.bindData("tv_auto_search.png", LogicTextResource.getInstance(context).getText(R.string.mannual_search));
    	channelnumberInput.setText(String.valueOf(chnumber));
    }
    private void updateSignalInfo(SignalInfoData signalInfo)
    {
    	signalQualityProgressBar.setProgress(Integer.parseInt(signalInfo.signalQuality));
    	signalIntensityProgressBar.setProgress(Integer.parseInt(signalInfo.signalStrength));
    	signalQualityText.setText(String.format("%s%s", signalInfo.signalQuality, "%"));
    	signalIntensityText.setText(String.format("%s%s", signalInfo.signalStrength, "%"));
    }
	DtvManualSearchListener dtvManualSearchListener = new DtvManualSearchListener() {
		
		@Override
		public void OnDtvManualSearchListener(ChannelSearchData data)
		{
			// TODO Auto-generated method stub
			updateView(data);
		}
	};
	private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (!mRunthread)
                    break;
                signalInfo = TvPluginControler.getInstance().getChannelManager().getSignalInfoData();
//                updateSignalInfo(signalInfo);
                dtvSignalHandler.sendEmptyMessageDelayed(DTV_SIGNAL_REFRESH_UI, 500);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private Handler dtvSignalHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case DTV_SIGNAL_REFRESH_UI:
                    if (null == signalInfo) {
                        return;
                    }
                    updateSignalInfo(signalInfo);
                    break;

                default:
                    break;
            }

        }
    };

	public DTVManualSearchDialog getParentDialog() {
		return parentDialog;
	}

	public void setParentDialog(DTVManualSearchDialog parentDialog) {
		this.parentDialog = parentDialog;
	}
    
    
}
