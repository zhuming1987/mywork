package com.tv.ui.SourceInfo;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.DtvSourceInfoData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SettingView.TvSettingItemView.OnSettingKeyDownListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DtvSimpleSourceInfoView extends LinearLayout implements OnSettingKeyDownListener, DialogListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
	private Context context;
	
	private SourceInfoDialog parentDialog = null;

    private ChannelInfoView rightTopView;
    private LinearLayout bottomLayout;
    private LinearLayout frontbottom;
    private LinearLayout behindbottom;
    private LinearLayout curProgLayout;
    private TextView channelNOText;
    private TextView channelNameText;
    private ImageView progImage;
    private TextView programNameText;
    private TextView timeText;
    private int frontWidth = (int) (760 / div);
    private int behindWidth = (int) (1160 / div);
    private int height = (int) (144 / div);
    
    private final int PROGRAM_NAME = 0;
    
    public DtvSimpleSourceInfoView(Context context)
    {
        super(context);
        this.context = context;
        initViews();   
    }
    
    private void initViews()
    {
    	setOrientation(LinearLayout.VERTICAL);
        setFocusable(false);

        FrameLayout frameLayout = new FrameLayout(context);
        this.addView(frameLayout, new LayoutParams((int) (1920 / div), (int) (1080 / div)));

        FrameLayout.LayoutParams channelNumParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.TOP);
        channelNumParams.rightMargin = (int) (77 / div);
        channelNumParams.topMargin = (int) (55 / div);
        rightTopView = new ChannelInfoView(context);
        frameLayout.addView(rightTopView, channelNumParams);

        bottomLayout = new LinearLayout(context);
        bottomLayout.setBackgroundColor(Color.argb(150, 0, 0, 0));
        bottomLayout.setOrientation(HORIZONTAL);
        FrameLayout.LayoutParams bottomParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, height, Gravity.BOTTOM);
        frameLayout.addView(bottomLayout, bottomParams);
        
        frontbottom = new LinearLayout(context);
        frontbottom.setBackgroundColor(Color.argb(150, 54, 153, 220));
        frontbottom.setOrientation(HORIZONTAL);
        frontbottom.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams frontParams = new LayoutParams(frontWidth, LayoutParams.MATCH_PARENT);
        bottomLayout.addView(frontbottom, frontParams);
        
        channelNOText = new TextView(context);
        channelNOText.setTextSize((int) (68 / dip));
        channelNOText.setTextColor(Color.WHITE);
        channelNOText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams channelNOParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        channelNOParams.leftMargin = (int)(40 / div);
        frontbottom.addView(channelNOText,channelNOParams);
        
        channelNameText = new TextView(context);
        channelNameText.setTextSize((int) (60 / dip));
        channelNameText.setTextColor(Color.WHITE);
        channelNameText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams channelNameParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        channelNameParams.leftMargin = (int)(40 / div);
        frontbottom.addView(channelNameText,channelNameParams);
        
        behindbottom = new LinearLayout(context);
        behindbottom.setOrientation(VERTICAL);
        LinearLayout.LayoutParams behindParams = new LayoutParams(behindWidth, LayoutParams.MATCH_PARENT);
        behindParams.leftMargin = (int)(50 / div);
        bottomLayout.addView(behindbottom, behindParams);
        
        curProgLayout = new LinearLayout(context);
        LinearLayout.LayoutParams curProgParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        curProgParams.topMargin = (int)(15 / div);
        behindbottom.addView(curProgLayout, curProgParams);
        
        progImage = new ImageView(context);
        progImage.setImageResource(R.drawable.play);
        LinearLayout.LayoutParams playimageParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        playimageParams.width = (int) (44 / div);
        playimageParams.height = (int) (44 / div);
        playimageParams.topMargin = (int) (10 / div);
        curProgLayout.addView(progImage, playimageParams);
        
        programNameText = new TextView(context);
        programNameText.setTextSize((int) (44 / dip));
        programNameText.setTextColor(Color.rgb(54,153,220));
        LinearLayout.LayoutParams progNameParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        progNameParams.leftMargin = (int) (40 / div);
        curProgLayout.addView(programNameText,progNameParams);      

        timeText = new TextView(context);
        timeText.setTextSize((int) (34 / dip));
        timeText.setTextColor(Color.rgb(199,197,198));
        timeText.setGravity(Gravity.LEFT);
        LinearLayout.LayoutParams timeParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        timeParams.topMargin = (int)(10 / div);
        timeParams.bottomMargin = (int)(15 / div);
        behindbottom.addView(timeText, timeParams);             
    }
    
    public void update(DtvSourceInfoData data) {
		// TODO Auto-generated method stub
    	this.setVisibility(View.VISIBLE);

        frontbottom.setVisibility(View.VISIBLE);
        behindbottom.setVisibility(View.VISIBLE);
        curProgLayout.setVisibility(View.VISIBLE);
        
        rightTopView.dtvUpdate(data);
        if(data.channelNumber.equals("0"))
        {
            channelNOText.setText("1");
            channelNOText.setTextColor(Color.RED);
        }
        else 
        {
            channelNOText.setText(data.channelNumber+"");
            channelNOText.setTextColor(Color.WHITE);
		}
        channelNameText.setText(data.channelName); 
        if(null!=data.epgEditInfos && 0 < data.epgEditInfos.size())
        {
        	programNameText.setText(data.epgEditInfos.get(PROGRAM_NAME));
        }
        else
        {
        	programNameText.setText("");
        }
        
        timeText.setText(TvPluginControler.getInstance().getChannelManager().getCurrentTime());
        
        if(data.skipFlag)
        {
        	channelNOText.setTextColor(Color.RED);
        }
	}
    
    public void setParentDialog(SourceInfoDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	@Override
	public boolean onShowDialogDone(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object... obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void OnKeyDownListener(int keyCode) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU||(keyCode == KeyEvent.KEYCODE_INFO))
		{
			parentDialog.processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO,DialogCmd.DIALOG_DISMISS, null);
		}
	}

	@Override
	public void setFocus(int index, int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnPage(int keycode, int index) {
		// TODO Auto-generated method stub
		
	}
}
