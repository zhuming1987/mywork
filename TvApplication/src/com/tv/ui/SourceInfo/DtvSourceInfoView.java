package com.tv.ui.SourceInfo;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.DtvSourceInfoData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.utils.LogicTextResource;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DtvSourceInfoView extends LinearLayout implements OnFocusChangeListener, OnTouchListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
	private Context context;
	
	private SourceInfoDialog parentDialog = null;

    private ChannelInfoView rightTopView;
    private LinearLayout mainLayout;
    private LinearLayout aboveLayout;
    private LinearLayout bottomLayout;
    private TableLayout frontabove;
    private LinearLayout behindabove;
    private LinearLayout proPeriodLayout;
    private LinearLayout proNameLayout;
    
    private TextView channelNumberText; 
    private TextView channelNameText;
    private TextView audioFormatText;
    private TextView videoFormatText;
    private TextView subtitleText; 
    private TextView audioLanguageText;
    private ImageView programTypeImage;
    private TextView resolutionText;
    private TextView programAgeText; 
    private TextView progCategoryText;
    private ImageView teletexImage;
    private TextView timeText;  
    
    private ImageView leftImage;
    private ImageView rightImage;
    private TextView programPeriodText;
    private TextView programNameTitle;
    private TextView programNameText;
    
    private ScrollView scrollView;
    private TextView programDescriptionText;
    private int frontWidth = (int) (1420 / div);
    private int behindWidth = (int) (500 / div);
    private int peroidWidth = (int) (70 / div);
    //private int height = (int) (430 / div);
    private int height = (int) (435 / div);
    private int aboveheight = (int) (296 / div);
    //private int bottomheight = (int) (136 / div);
    private int bottomheight = (int) (141 / div);
    
    private boolean firstEnterFlag = true;
    
    private final int PROGRAM_NAME = 0;
    private final int PROGRAM_AGE = 1;
    private final int PROGRAM_CATEGORY = 2;
    private final int PROGRAM_DESCRIPTION = 3;

    public DtvSourceInfoView(Context context)
    {
        super(context);
        this.context = context;
        initViews();         
    }
    
    private void initViews()
    {
    	setOrientation(LinearLayout.VERTICAL);
        setFocusable(true);
        this.requestFocus();

        FrameLayout frameLayout = new FrameLayout(context);
        this.addView(frameLayout, new LayoutParams((int) (1920 / div), (int) (1080 / div)));

        FrameLayout.LayoutParams channelNumParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.TOP);
        channelNumParams.rightMargin = (int) (77 / div);
        channelNumParams.topMargin = (int) (55 / div);
        rightTopView = new ChannelInfoView(context);
        frameLayout.addView(rightTopView, channelNumParams);

        mainLayout = new LinearLayout(context);
        mainLayout.setBackgroundColor(Color.rgb(25, 26, 28));
        mainLayout.setOrientation(VERTICAL);
        FrameLayout.LayoutParams mainParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, height, Gravity.BOTTOM);
        frameLayout.addView(mainLayout, mainParams);
        
        aboveLayout = new LinearLayout(context);
        aboveLayout.setOrientation(HORIZONTAL);
        aboveLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams frontParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, aboveheight);
        mainLayout.addView(aboveLayout, frontParams);
        
        frontabove = new TableLayout(context);
        frontabove.setGravity(Gravity.RIGHT);
        frontabove.setStretchAllColumns(true);          
        LinearLayout.LayoutParams frontaboveParams = new LinearLayout.LayoutParams(frontWidth,LayoutParams.MATCH_PARENT);
        aboveLayout.addView(frontabove, frontaboveParams);
        
        //生成4行的TableRow    
        TableRow tableRow1=new TableRow(context); 
        TableRow tableRow2=new TableRow(context);
        TableRow tableRow3=new TableRow(context);
        TableRow tableRow4=new TableRow(context);
        TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tableParams.leftMargin = (int) (20 / div);
        tableParams.rightMargin = (int) (20 / div);
        tableParams.topMargin = (int) (20 / div);
        frontabove.addView(tableRow1, tableParams);
        frontabove.addView(tableRow2, tableParams);
        frontabove.addView(tableRow3, tableParams);
        frontabove.addView(tableRow4, tableParams);
        
        //給TableRow1添加控件        
        channelNumberText=new TextView(context);     
        channelNumberText.setTextSize((int) (36 / dip));
        channelNumberText.setGravity(Gravity.LEFT);
        channelNumberText.setTextColor(Color.rgb(167,167,167));
        tableRow1.addView(channelNumberText);    
       
        audioFormatText=new TextView(context);     
        audioFormatText.setTextSize((int) (36 / dip));
        audioFormatText.setGravity(Gravity.LEFT);
        audioFormatText.setTextColor(Color.rgb(167,167,167));
        tableRow1.addView(audioFormatText);  
        
        subtitleText=new TextView(context);     
        subtitleText.setTextSize((int) (36 / dip));
        subtitleText.setGravity(Gravity.LEFT);
        subtitleText.setTextColor(Color.rgb(167,167,167));
        tableRow1.addView(subtitleText);
        
        //給TableRow2添加控件         
        channelNameText=new TextView(context);     
        channelNameText.setTextSize((int) (36 / dip));
        channelNameText.setGravity(Gravity.LEFT);
        channelNameText.setTextColor(Color.rgb(167,167,167));
        tableRow2.addView(channelNameText);    
       
        videoFormatText=new TextView(context);     
        videoFormatText.setTextSize((int) (36 / dip));
        videoFormatText.setGravity(Gravity.LEFT);
        videoFormatText.setTextColor(Color.rgb(167,167,167));
        tableRow2.addView(videoFormatText);  
        
        audioLanguageText=new TextView(context);     
        audioLanguageText.setTextSize((int) (36 / dip));
        audioLanguageText.setGravity(Gravity.LEFT);
        audioLanguageText.setTextColor(Color.rgb(167,167,167));
        tableRow2.addView(audioLanguageText); 
        
        //給TableRow3添加控件         
        programTypeImage=new ImageView(context);     
        programTypeImage.setBackgroundResource(R.drawable.dtv);
        TableLayout.LayoutParams programTypeParams = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        programTypeParams.width = (int) (80 / div);
        programTypeParams.height = (int) (40 / div);
        programTypeParams.topMargin = (int) (10 / div);
        LinearLayout layout1 = new LinearLayout(context);
        layout1.addView(programTypeImage,programTypeParams);
        tableRow3.addView(layout1);    
       
        resolutionText=new TextView(context);     
        resolutionText.setTextSize((int) (36 / dip));
        resolutionText.setGravity(Gravity.LEFT);
        resolutionText.setTextColor(Color.rgb(167,167,167));
        tableRow3.addView(resolutionText);  
        
        programAgeText=new TextView(context);     
        programAgeText.setTextSize((int) (36 / dip));
        programAgeText.setGravity(Gravity.LEFT);
        programAgeText.setTextColor(Color.rgb(167,167,167));
        tableRow3.addView(programAgeText); 
        
        //給TableRow4添加控件         
        timeText=new TextView(context);     
        timeText.setTextSize((int) (36 / dip));
        timeText.setGravity(Gravity.LEFT);
        timeText.setTextColor(Color.rgb(167,167,167));
        tableRow4.addView(timeText);    
       
        progCategoryText=new TextView(context);     
        progCategoryText.setTextSize((int) (36 / dip));
        progCategoryText.setGravity(Gravity.LEFT);
        progCategoryText.setTextColor(Color.rgb(167,167,167));
        tableRow4.addView(progCategoryText);  
        
        teletexImage=new ImageView(context);     
        teletexImage.setBackgroundResource(R.drawable.teletxt);
        teletexImage.setVisibility(INVISIBLE);
        TableLayout.LayoutParams teletexParams = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        teletexParams.width = (int) (80 / div);
        teletexParams.height = (int) (40 / div);
        teletexParams.topMargin = (int) (10 / div);
        LinearLayout layout2 = new LinearLayout(context);
        layout2.addView(teletexImage,teletexParams);
        tableRow4.addView(layout2);
        
        behindabove = new LinearLayout(context);
        behindabove.setBackgroundColor(Color.rgb(38, 39, 41));
        behindabove.setOrientation(VERTICAL);
        behindabove.setGravity(Gravity.RIGHT);
        LinearLayout.LayoutParams behindaboveParams = new LayoutParams(behindWidth,LayoutParams.MATCH_PARENT);   
        aboveLayout.addView(behindabove,behindaboveParams);
        
        proPeriodLayout = new LinearLayout(context);
        proPeriodLayout.setBackgroundColor(Color.rgb(53, 151, 216));
        proPeriodLayout.setOrientation(HORIZONTAL);
        proPeriodLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams proPeriodParams = new LayoutParams(LayoutParams.MATCH_PARENT,peroidWidth); 
        proPeriodParams.leftMargin = (int) (20 / div);
        proPeriodParams.rightMargin = (int) (20 / div);
        proPeriodParams.topMargin = (int) (20 / div);
        behindabove.addView(proPeriodLayout,proPeriodParams);
        proPeriodLayout.setFocusable(true);
        proPeriodLayout.setFocusableInTouchMode(true);

        leftImage = new ImageView(context);
        leftImage.setImageResource(R.drawable.simulation_arrow_left);
        LinearLayout.LayoutParams leftImageParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        leftImageParams.width = (int) (40 / div);
        leftImageParams.height = (int) (40 / div);
        leftImageParams.leftMargin = (int) (30 / div);
        proPeriodLayout.addView(leftImage, leftImageParams);
        
        programPeriodText = new TextView(context);
        programPeriodText.setTextSize((int) (36 / dip));
        programPeriodText.setGravity(Gravity.CENTER);
        programPeriodText.setTextColor(android.graphics.Color.WHITE);
        programPeriodText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        programPeriodText.getPaint().setFakeBoldText(true);//加粗
        proPeriodLayout.addView(programPeriodText, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)); 
        
        rightImage = new ImageView(context);
        rightImage.setImageResource(R.drawable.simulation_arrow_right);
        LinearLayout.LayoutParams rightImageParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        rightImageParams.width = (int) (40 / div);
        rightImageParams.height = (int) (40 / div);
        rightImageParams.rightMargin = (int) (30 / div);
        proPeriodLayout.addView(rightImage, rightImageParams);
        
        proNameLayout = new LinearLayout(context);
        proNameLayout.setOrientation(VERTICAL);
        proNameLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams proNameParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT); 
        proNameParams.leftMargin = (int) (20 / div);
        proNameParams.rightMargin = (int) (20 / div);
        proNameParams.topMargin = (int) (40 / div);
        behindabove.addView(proNameLayout,proNameParams); 
        
        programNameTitle = new TextView(context);
        programNameTitle.setTextSize((int) (34 / dip));
        programNameTitle.setTextColor(Color.rgb(146,146,146));
        programNameTitle.setGravity(Gravity.LEFT);
        proNameLayout.addView(programNameTitle, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)); 
        
        programNameText = new TextView(context);
        programNameText.setTextSize((int) (30 / dip));
        programNameText.setGravity(Gravity.LEFT);
        programNameText.setTextColor(Color.rgb(91,92,94));
        proNameLayout.addView(programNameText, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT)); 
        
        bottomLayout = new LinearLayout(context);
        behindabove.setBackgroundColor(Color.rgb(30, 31, 33));
        bottomLayout.setOrientation(HORIZONTAL);
        bottomLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, bottomheight);
        bottomParams.leftMargin = (int)(20 / div);
        bottomParams.bottomMargin = (int)(5 / div);
        mainLayout.addView(bottomLayout, bottomParams);
        
        scrollView = new ScrollView(context);
		scrollView.setVerticalScrollBarEnabled(true);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setScrollbarFadingEnabled(true);
		scrollView.setAlwaysDrawnWithCacheEnabled(true);
		scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		
		programDescriptionText = new TextView(context);
        programDescriptionText.setTextSize((int) (30 / dip));
        programDescriptionText.setGravity(Gravity.LEFT);
        programDescriptionText.setTextColor(Color.rgb(84,86,85));
        scrollView.addView(programDescriptionText, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT)); 
        
		bottomLayout.addView(scrollView);		              
    }
    
    public void update(DtvSourceInfoData data) {
		// TODO Auto-generated method stub
    	this.setVisibility(View.VISIBLE);
    	boolean isSignal = TvPluginControler.getInstance().getCommonManager().isSignalState();
    	
        rightTopView.dtvUpdate(data);
        mainLayout.setVisibility(View.VISIBLE);   
        aboveLayout.setVisibility(View.VISIBLE);
        frontabove.setVisibility(View.VISIBLE);
        
        if(!isSignal)
        {
        	if(data.channelNumber.equals("0"))
        	{
            	channelNumberText.setText(context.getString(R.string.str_source_info_channel)+"1"); 
            	channelNumberText.setTextColor(Color.RED);
        	}
        	else 
        	{
            	channelNumberText.setText(context.getString(R.string.str_source_info_channel)+data.channelNumber); 
            	channelNumberText.setTextColor(Color.WHITE);
			}
            audioFormatText.setText(context.getString(R.string.str_source_info_audioformat)+"");
            subtitleText.setText(context.getString(R.string.str_source_info_subtitle)+"");
            channelNameText.setText(context.getString(R.string.str_source_info_chname)+"");
            videoFormatText.setText(context.getString(R.string.str_source_info_videoformat)+"");
            audioLanguageText.setText(context.getString(R.string.str_source_info_audiolanguage)+"");
            resolutionText.setText(context.getString(R.string.str_source_info_resolution)+"");
            programAgeText.setText(context.getString(R.string.str_source_info_age)+"");
            timeText.setText(TvPluginControler.getInstance().getChannelManager().getCurrentTime());
            progCategoryText.setText(context.getString(R.string.str_source_info_category)+"");
            
            teletexImage.setVisibility(INVISIBLE);
            programTypeImage.setVisibility(INVISIBLE);
            
            behindabove.setVisibility(View.VISIBLE);
            programPeriodText.setText("");
            programNameTitle.setText("");
            programNameText.setText("");               
            programDescriptionText.setText(R.string.str_source_info_no_information);
        }
        else 
        {
        	channelNumberText.setText(context.getString(R.string.str_source_info_channel)+data.channelNumber); 
            audioFormatText.setText(context.getString(R.string.str_source_info_audioformat)+data.audioFormat);

            int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
			if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS || curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2)
			{
				subtitleText.setText(TvPluginControler.getInstance().getChannelManager().getCurrentSatelliteName());
			}
			else
			{
				subtitleText.setText(context.getString(R.string.str_source_info_subtitle)+data.subtitleNumber);
			}
            
			channelNameText.setText(context.getString(R.string.str_source_info_chname)+data.channelName);
            videoFormatText.setText(context.getString(R.string.str_source_info_videoformat)+data.videoFormat);
            audioLanguageText.setText(context.getString(R.string.str_source_info_audiolanguage)+data.audioLanguage);
            resolutionText.setText(context.getString(R.string.str_source_info_resolution)+data.resolution);           
            timeText.setText(TvPluginControler.getInstance().getChannelManager().getCurrentTime());   
            
            if(data.skipFlag)
            {
            	channelNumberText.setTextColor(Color.RED);
            }
            
            if(data.teletxtFlag)
            {
            	teletexImage.setVisibility(VISIBLE);
            }	
            else
            {
            	teletexImage.setVisibility(INVISIBLE);
            }
            
            String curProgramType = data.programType;
            if(curProgramType.equals("DATA"))
            {
            	programTypeImage.setBackgroundResource(R.drawable.data);
            }
            else if(curProgramType.equals("RADIO"))
            {
            	programTypeImage.setBackgroundResource(R.drawable.radio);
            }
            else if(curProgramType.equals("DTV"))
            {
            	programTypeImage.setBackgroundResource(R.drawable.dtv);
            }
            else 
            {
            	programTypeImage.setVisibility(INVISIBLE);
    		}
            
            behindabove.setVisibility(View.VISIBLE);
            programPeriodText.setText(data.programPeriod);
            programNameTitle.setText(R.string.str_source_info_programname);
                           
            bottomLayout.setVisibility(View.VISIBLE);
            
            if(null!=data.epgEditInfos)
            {
            	programAgeText.setText(context.getString(R.string.str_source_info_age)+data.epgEditInfos.get(PROGRAM_AGE));
            	programNameText.setText(data.epgEditInfos.get(PROGRAM_NAME));
            	String progCategory =  LogicTextResource.getInstance(TvContext.context).getText(data.epgEditInfos.get(PROGRAM_CATEGORY));
                progCategoryText.setText(context.getString(R.string.str_source_info_category)+progCategory);
            	if((data.epgEditInfos.get(PROGRAM_DESCRIPTION)).equals(""))
                {
                	programDescriptionText.setText(R.string.str_source_info_no_information);
                }
                else 
                {
                	programDescriptionText.setText(data.epgEditInfos.get(PROGRAM_DESCRIPTION));     
        		}
            }
            else
            {
            	programAgeText.setText(context.getString(R.string.str_source_info_age)+"");
            	programNameText.setText("");
            	progCategoryText.setText(context.getString(R.string.str_source_info_category)+"");
            	programDescriptionText.setText(R.string.str_source_info_no_information);
            }
		}              
	}
    
    public void setParentDialog(SourceInfoDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{
			DtvSourceInfoData dtvInfoDatas = TvPluginControler.getInstance().getChannelManager().updataDtvSourceInfo(true);
			update(dtvInfoDatas);
			parentDialog.dismissCount = 10;
			return true;
		}	
		else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
		{
			DtvSourceInfoData dtvInfoDatas = TvPluginControler.getInstance().getChannelManager().updataDtvSourceInfo(false);
			update(dtvInfoDatas);
			parentDialog.dismissCount = 10;
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_INFO)
		{
			if(firstEnterFlag)
			{
				firstEnterFlag = false;
				return true;
			}
			else
			{
				parentDialog.processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO,DialogCmd.DIALOG_UPDATE, false);
				return true;
			}					
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			scrollView.scrollBy(0,-scrollView.getMeasuredHeight());
			parentDialog.dismissCount = 10;
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			scrollView.scrollBy(0,scrollView.getMeasuredHeight());
			parentDialog.dismissCount = 10;
			return true;
		}
		else 
		{
			return parentDialog.onKeyDown(keyCode, event);
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

}
