package com.tv.ui.SourceInfo;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.AtvSourceInfoData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvConfigTypes.TvEnumProgramCountType;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SettingView.TvSettingItemView.OnSettingKeyDownListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class AtvSourceInfoView extends LinearLayout implements OnSettingKeyDownListener, DialogListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
	private Context context;
	
	private SourceInfoDialog parentDialog = null;
	private TvEnumInputSource inputSource;

    private ChannelInfoView rightTopView;
    private LinearLayout bottomLayout;
    private LinearLayout frontbottom;
    private LinearLayout behindbottom;
    private TextView channelNOText;
    private ImageView favimage;
    private ImageView teletexImage;
    private TextView sourceTypeText;
    private TextView baseInfoText;
    private TextView timeText;

    public static int VGANoSupportFlag = 0;
    
    private int frontWidth = (int) (760 / div);
    private int behindWidth = (int) (1160 / div);
    private int height = (int) (144 / div);
    
    public AtvSourceInfoView(Context context)
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
        rightTopView.setVisibility(GONE);
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
        LinearLayout.LayoutParams frontParams = new LinearLayout.LayoutParams(frontWidth, LayoutParams.MATCH_PARENT);
        bottomLayout.addView(frontbottom, frontParams);
        
        channelNOText = new TextView(context);
        channelNOText.setVisibility(GONE);
        channelNOText.setTextSize((int) (68 / dip));
        channelNOText.setTextColor(Color.WHITE);
        channelNOText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams channelNOParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        channelNOParams.leftMargin = (int)(40 / div);
        frontbottom.addView(channelNOText,channelNOParams);
        
        favimage = new ImageView(context);
        favimage.setVisibility(GONE);
        favimage.setImageResource(R.drawable.favprogam);
        LinearLayout.LayoutParams favimageParams = new LinearLayout.LayoutParams((int)(50 / div),(int)(50 / div));
        favimageParams.leftMargin = (int)(30 / div);
        frontbottom.addView(favimage, favimageParams);
        
        sourceTypeText = new TextView(context);
        sourceTypeText.setTextSize((int) (60 / dip));
        sourceTypeText.setTextColor(Color.WHITE);
        sourceTypeText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams sourceTypeParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        sourceTypeParams.leftMargin = (int)(80 / div);
        frontbottom.addView(sourceTypeText,sourceTypeParams);
        
        teletexImage=new ImageView(context);     
        teletexImage.setBackgroundResource(R.drawable.teletxt);
        teletexImage.setVisibility(GONE);
        LinearLayout.LayoutParams teletexParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        teletexParams.width = (int) (80 / div);
        teletexParams.height = (int) (40 / div);
        teletexParams.leftMargin = (int)(150 / div);
        teletexParams.topMargin = (int) (5 / div);
        frontbottom.addView(teletexImage,teletexParams);
        
        behindbottom = new LinearLayout(context);
        behindbottom.setOrientation(VERTICAL);
        LinearLayout.LayoutParams behindParams = new LinearLayout.LayoutParams(behindWidth, LayoutParams.MATCH_PARENT);
        behindParams.leftMargin = (int)(50 / div);
        bottomLayout.addView(behindbottom, behindParams);
        
        baseInfoText = new TextView(context);
        baseInfoText.setTextSize((int) (44 / dip));
        baseInfoText.setTextColor(Color.rgb(54,153,220));
        baseInfoText.setGravity(Gravity.LEFT);
        LinearLayout.LayoutParams baseInfoParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        baseInfoParams.topMargin = (int)(15 / div);
        behindbottom.addView(baseInfoText,baseInfoParams);      

        timeText = new TextView(context);
        timeText.setTextSize((int) (34 / dip));
        timeText.setTextColor(Color.rgb(199,197,198));
        timeText.setGravity(Gravity.LEFT);
        LinearLayout.LayoutParams timeParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        timeParams.topMargin = (int)(10 / div);
        timeParams.bottomMargin = (int)(15 / div);
        behindbottom.addView(timeText, timeParams);               
    }
    
    public void update(AtvSourceInfoData data) {
		// TODO Auto-generated method stub
    	this.setVisibility(View.VISIBLE);

        frontbottom.setVisibility(View.VISIBLE);
        behindbottom.setVisibility(View.VISIBLE);
        
        sourceTypeText.setText(data.sourceType); 
        timeText.setText(TvPluginControler.getInstance().getChannelManager().getCurrentTime()); 
        
        inputSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
       
        if(SourceInfoDialog.isSignal)
    	{
    		if ( inputSource == TvEnumInputSource.E_INPUT_SOURCE_ATV)
    		{
    			rightTopView.atvUpdate(data);
    			
    			channelNOText.setVisibility(VISIBLE);
    			channelNOText.setText(data.channelNumber);	              	        
    	        baseInfoText.setText(data.colorSystem + " " + " "+ data.soundSystem + " " + " "+ data.soundFormat);
    	        	        
    	        if(data.skipFlag)
    	        {
    	        	channelNOText.setTextColor(Color.RED);
    	        }
    	        else
    	        {
    	        	channelNOText.setTextColor(Color.WHITE);
    	        }
    	        
    	        if(data.favFlag == 1)
    	        {
    	        	favimage.setVisibility(VISIBLE);
    	        }
    	        else
    	        {
    	        	favimage.setVisibility(GONE);
    	        }
    	        
    	        if(data.teletxtFlag)
                {
                	teletexImage.setVisibility(VISIBLE);
                }	
                else
                {
                	teletexImage.setVisibility(GONE);
                }
    		}
    		else if ( inputSource == TvEnumInputSource.E_INPUT_SOURCE_VGA)
    		{
    			if(VGANoSupportFlag == 1)
    			{
    				baseInfoText.setText(R.string.TV_VGA_RESOLUTION_UNSUPPORT);
    			}
    			else 
    			{
    				baseInfoText.setText(data.resolution);
				}   			
    		}
    		else 
    		{
    			baseInfoText.setText(data.resolution);  			
    		}
    	}
    	else 
    	{
    		int atvprogramcount = TvPluginControler.getInstance().getChannelManager().getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_ATV.ordinal());
    		Log.v("zhm","ATV Source Info atvprogramcount = " + atvprogramcount);
    		if ( inputSource == TvEnumInputSource.E_INPUT_SOURCE_ATV)
    		{
    			rightTopView.atvUpdate(data);
    			
    			channelNOText.setVisibility(VISIBLE);
    			channelNOText.setText(data.channelNumber);	
    			teletexImage.setVisibility(GONE);
    	        baseInfoText.setText("");
    	        if(atvprogramcount > 0)
    	        {
	    	        if(data.skipFlag)
	    	        {
	    	        	channelNOText.setTextColor(Color.RED);
	    	        }
	    	        else
	    	        {
	    	        	channelNOText.setTextColor(Color.WHITE);
	    	        }
	    	        
	    	        if(data.favFlag == 1)
	    	        {
	    	        	favimage.setVisibility(VISIBLE);
	    	        }
	    	        else
	    	        {
	    	        	favimage.setVisibility(GONE);
	    	        }
    	        }
    	        else 
    	        {
    	        	channelNOText.setTextColor(Color.RED);
				}
    		} 
    		else 
    		{
    			baseInfoText.setText("");
    		}
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
