package com.tv.ui.EPGManage;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvUIControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.framework.data.TvEpgData;
import com.tv.framework.data.TvEpgData.EPGWindows;
import com.tv.framework.data.TvEpgDateListData;
import com.tv.framework.data.TvEpgInfos;
import com.tv.framework.data.TvLoadingData;
import com.tv.framework.data.TvProgramInfo;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.Loading.TvLoadingDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class EPGManageView extends LinearLayout implements DialogListener{

	private Context mContext;
	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();
    private LinearLayout overallLayout;
    private TextView titleText;
    private LinearLayout typeLayout;
    private TextView chlistTextView,dateTextView,programTextView,infomationTextView;
    private ImageView chlistImageView,dateImageView,programImageView;
    private LinearLayout bodyLayout;
    private ScrollView channellistScrollview,channeldateScrollView,programScrollView,epginfomationScrollView;
    private LinearLayout channellistLayout,channeldateLayout,programLayout;
    private LinearLayout infomationLayout;
    private TextView epginfomationnameTextView,epginfomationdaTextView,epginfomationTextView;
    private LinearLayout redLayout,yellowLayout,blueLayout;
    private ImageView redImageView,yellowImageView,blueImageView;
    private TextView redtipsTextView,yellowtipsTextView,bluetipsTextView;
    private ImageView epgItemDivider;
    private ArrayList<EPGManageItemView> epgchannellistItemView = new ArrayList<EPGManageItemView>();
    private ArrayList<EPGChannelDateItemView> epgDateListView= new ArrayList<EPGChannelDateItemView>();
    private ArrayList<EPGProgramNameItemView> epgprogramlistItemView = new ArrayList<EPGProgramNameItemView>();
    private EPGManageDialog parentDialog = null;
    private QuickKeyListener quickKeyListener;
    
    private ArrayList<TvProgramInfo> channelList = null;
    private ArrayList<TvEpgDateListData> epgDateList = null;
    private TvEpgInfos epgInfoList = null;
    private TvProgramInfo curProgramInfo = null;
    
    private TvLoadingDialog loadingDialog = null;
    private static final int SHOW_LOADING = 1;
    private static final int DISMISS_LOADING = 2;
    private static final int UPDATE_PROLISH = 3;
    private static final int CLEAR_DATE_PRO_INFO = 4;
    private static final int UPDATE_DATE_PRO_INFO = 5;
    private static final int UPDATE_EPGLISH = 6;
    private int refreshCount = 0;
    private boolean chLock = true;
    private boolean dateLock = true;
    private static int TRANSPARENT_COLOR = 0xb0000000;
	
	 public EPGManageView(Context context) {
		super(context);
		mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        setFocusable(true);
        this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
        
        overallLayout = new LinearLayout(context);
        overallLayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(overallLayout, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        
        titleText = initTextView(context.getString(R.string.epgmanagetitle), (int) (52 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        titleText.setBackgroundColor(Color.parseColor("#3598DC"));
        overallLayout.addView(titleText, (int)(1680/div) , (int)(120/div));
        
        typeLayout = new LinearLayout(context);
        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
        typeLayout.setBackgroundColor(Color.parseColor("#323232"));
        overallLayout.addView(typeLayout, (int)(1680/div) , (int)(120/div));
        
        chlistTextView = initTextView(context.getString(R.string.epgchannellist), (int) (42 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(chlistTextView, (int)(459/div) , (int)(120/div));
        
        chlistImageView = initiImageView();
        typeLayout.addView(chlistImageView);
        
        dateTextView = initTextView(context.getString(R.string.epgchanneldate), (int) (42 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(dateTextView, (int)(119/div) , (int)(120/div));
        
        dateImageView = initiImageView();
        typeLayout.addView(dateImageView);
        
        programTextView = initTextView(context.getString(R.string.epgprogramname), (int) (42 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(programTextView, (int)(599/div) , (int)(120/div));
        
        programImageView = initiImageView();
        typeLayout.addView(programImageView);
        
        infomationTextView = initTextView(context.getString(R.string.epginfomation), (int) (42 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(infomationTextView, (int)(500/div) , (int)(120/div));
        
        bodyLayout = new LinearLayout(context);
        bodyLayout.setOrientation(LinearLayout.HORIZONTAL);
        overallLayout.addView(bodyLayout, (int)(1680/div) , (int)(840/div));
        
        channellistScrollview = new ScrollView(mContext);
        channellistScrollview.setBackgroundColor(Color.parseColor("#191919"));
        channellistScrollview.setVerticalScrollBarEnabled(false);
        channellistScrollview.setHorizontalScrollBarEnabled(false);
        channellistScrollview.setScrollbarFadingEnabled(true);
        channellistScrollview.setAlwaysDrawnWithCacheEnabled(true);
        channellistScrollview.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        channellistLayout = new LinearLayout(mContext);
        channellistLayout.setOrientation(LinearLayout.VERTICAL);
        channellistScrollview.addView(channellistLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        bodyLayout.addView(channellistScrollview,(int)(460/div) , (int)(840/div));
        
        channeldateScrollView = new ScrollView(mContext);
        channeldateScrollView.setBackgroundColor(Color.parseColor("#000000"));
        channeldateScrollView.setVerticalScrollBarEnabled(false);
        channeldateScrollView.setHorizontalScrollBarEnabled(false);
        channeldateScrollView.setScrollbarFadingEnabled(true);
        channeldateScrollView.setAlwaysDrawnWithCacheEnabled(true);
        channeldateScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        channeldateLayout = new LinearLayout(mContext);
        channeldateLayout.setOrientation(LinearLayout.VERTICAL);
        channeldateScrollView.addView(channeldateLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        bodyLayout.addView(channeldateScrollView,(int)(120/div) , (int)(840/div));
        
        programScrollView = new ScrollView(mContext);
        programScrollView.setBackgroundColor(Color.parseColor("#1E4765"));
        programScrollView.setVerticalScrollBarEnabled(false);
        programScrollView.setHorizontalScrollBarEnabled(false);
        programScrollView.setScrollbarFadingEnabled(true);
        programScrollView.setAlwaysDrawnWithCacheEnabled(true);
        programScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        programLayout = new LinearLayout(mContext);
        programLayout.setOrientation(LinearLayout.VERTICAL);
        programScrollView.addView(programLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        bodyLayout.addView(programScrollView,(int)(600/div) , (int)(840/div));
        
        infomationLayout = new LinearLayout(context);
        infomationLayout.setBackgroundColor(Color.parseColor("#191919"));
        infomationLayout.setOrientation(LinearLayout.VERTICAL);
        infomationLayout.setFocusable(false);
        bodyLayout.addView(infomationLayout, (int)(500/div) , (int)(840/div));
        
        epginfomationnameTextView = initTextView(null, (int) (32 / dipDiv),
        		Color.WHITE, Gravity.LEFT | Gravity.CENTER_VERTICAL);
        epginfomationnameTextView.setPadding((int)(40/div), 0, 0, 0);
        epginfomationnameTextView.setTextColor(Color.GRAY);
        infomationLayout.addView(epginfomationnameTextView, (int)(500/div) , (int)(80/div));
        
        epginfomationdaTextView = initTextView(null, (int) (28 / dipDiv),
        		Color.WHITE, Gravity.LEFT | Gravity.CENTER_VERTICAL);
        epginfomationdaTextView.setPadding((int)(40/div), 0, 0, 0);
        epginfomationdaTextView.setTextColor(Color.GRAY);
        infomationLayout.addView(epginfomationdaTextView, (int)(500/div) , (int)(60/div));

        epginfomationScrollView = new ScrollView(context);
        epginfomationScrollView.setBackgroundColor(Color.parseColor("#191919"));
        epginfomationScrollView.setVerticalScrollBarEnabled(true);
        epginfomationScrollView.setHorizontalScrollBarEnabled(false);
        epginfomationScrollView.setScrollbarFadingEnabled(true);
        epginfomationScrollView.setAlwaysDrawnWithCacheEnabled(true);
        epginfomationScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        epginfomationScrollView.setOnFocusChangeListener(new OnFocusChangeListener() 
        {			
			@Override
			public void onFocusChange(View view, boolean hasFocus) 
			{
				// TODO Auto-generated method stub
				if(hasFocus)
				{
					epginfomationTextView.setBackgroundColor(Color.parseColor("#3598DC"));
					epginfomationTextView.setTextColor(Color.WHITE);
					epginfomationnameTextView.setTextColor(Color.WHITE);
					epginfomationdaTextView.setTextColor(Color.WHITE);
				}
				else
				{
					epginfomationTextView.setBackgroundColor(Color.parseColor("#191919"));
					epginfomationTextView.setTextColor(Color.GRAY);
					epginfomationnameTextView.setTextColor(Color.GRAY);
					epginfomationdaTextView.setTextColor(Color.GRAY);
				}
			}
		});
        epginfomationScrollView.setOnKeyListener(new OnKeyListener() 
        {			
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) 
			{
				// TODO Auto-generated method stub
				onKeyDown(keyCode, event);
				return true;
			}
		});
        infomationLayout.addView(epginfomationScrollView,(int)(500/div) , (int)(580/div));
        
        epginfomationTextView = initTextView(null, (int) (24 / dipDiv),
        		Color.WHITE, Gravity.LEFT);
        epginfomationTextView.setPadding((int)(40/div), 0, (int)(30/div), 0);
        epginfomationTextView.setTextColor(Color.GRAY);
        epginfomationScrollView.addView(epginfomationTextView,(int)(500/div) , (int)(580/div));	

        yellowLayout = new LinearLayout(context);
        yellowLayout.setOrientation(LinearLayout.HORIZONTAL);
        yellowLayout.setPadding((int)(40/div), 0, 0, 0);
        yellowLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        infomationLayout.addView(yellowLayout, (int)(500/div) , (int)(40/div));
        
        yellowImageView = new ImageView(context);
        yellowImageView.setBackgroundResource(R.drawable.epg_yellow);
        yellowImageView.setPadding((int)(20/div), (int)(20/div), (int)(20/div), (int)(20/div));
        yellowLayout.addView(yellowImageView, (int)(20/div) , (int)(20/div));
        
        yellowtipsTextView = initTextView(context.getString(R.string.epgtipsschedule), (int) (24 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        yellowtipsTextView.setPadding((int)(25/div), 0, 0, 0);
        yellowLayout.addView(yellowtipsTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
//        redLayout = new LinearLayout(context);
//        redLayout.setOrientation(LinearLayout.HORIZONTAL);
//        redLayout.setPadding((int)(40/div), 0, 0, 0);
//        redLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//        infomationLayout.addView(redLayout, (int)(500/div) , (int)(40/div));
//        
//        redImageView = new ImageView(context);
//        redImageView.setBackgroundResource(R.drawable.epg_red);
//        redImageView.setPadding((int)(20/div), (int)(20/div), (int)(20/div), (int)(20/div));
//        redLayout.addView(redImageView, (int)(20/div) , (int)(20/div));
//        
//        redtipsTextView = initTextView(context.getString(R.string.epgtipsrecord), (int) (24 / dipDiv),
//        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
//        redtipsTextView.setPadding((int)(25/div), 0, 0, 0);
//        redLayout.addView(redtipsTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        blueLayout = new LinearLayout(context);
        blueLayout.setOrientation(LinearLayout.HORIZONTAL);
        blueLayout.setPadding((int)(40/div), 0, 0, 0);
        blueLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        infomationLayout.addView(blueLayout, (int)(500/div) , (int)(40/div));
        
        blueImageView = new ImageView(context);
        blueImageView.setBackgroundResource(R.drawable.epg_blue);
        blueImageView.setPadding((int)(20/div), (int)(20/div), (int)(20/div), (int)(20/div));
        blueLayout.addView(blueImageView, (int)(20/div) , (int)(20/div));
        
        bluetipsTextView = initTextView(context.getString(R.string.epgtipsreminder), (int) (24 / dipDiv),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        bluetipsTextView.setPadding((int)(25/div), 0, 0, 0);
        blueLayout.addView(bluetipsTextView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        loadingDialog = new TvLoadingDialog();
        loadingDialog.setDialogListener(null);
    }

	 public void updateView()
	 {
		 Log.i("gky", getClass().toString() + "-->updateView");
		 TvPluginControler.getInstance().getEpgManager().enableEpgBarkerChannel();
		 curProgramInfo = TvPluginControler.getInstance().getChannelManager().getCurrentTvProgramInfo();
	     Log.i("gky", getClass().toString() +"-->updateView:: UpdateChannelListView");
	     UpdateChannelListView();
	     
	     UpdateEpgDateView();
	     
		 TvPluginControler.getInstance().getEpgManager().resetAllNextBaseTime();
		 
		 final TvLoadingDialog tvLoadingDialog = new TvLoadingDialog();
 		 tvLoadingDialog.processCmd(TvConfigTypes.TV_DIALOG_LOADING,DialogCmd.DIALOG_SHOW,new TvLoadingData(TvContext.context.getResources().getString(R.string.str_please_wait),null));
 		 new Thread(new Runnable() {
 			 @Override
 			 public void run() {
 				 // TODO Auto-generated method stub    					
 				 try 
 				 {
 					 Thread.sleep(3500);
 				 } catch (InterruptedException e) 
 				 {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				 }
 				 epgInfoList = TvPluginControler.getInstance().getEpgManager().getCurEpgEventInfos(curProgramInfo);
 				 mHandler.sendEmptyMessage(UPDATE_EPGLISH);		
 				 tvLoadingDialog.processCmd(TvConfigTypes.TV_DIALOG_LOADING,DialogCmd.DIALOG_DISMISS,null);
 			 }
 		 }).start();
	 }
	 
	 public void UpdateChannelListView()
	 {
		 Log.i("gky", getClass().toString() +"-->UpdateChannelListView");
		 channelList = TvPluginControler.getInstance().getChannelManager().getDtvChannelList();
		 if(null != channelList && curProgramInfo != null)
		 {
			 channellistLayout.removeAllViews();
			 epgchannellistItemView.clear();
			 for (TvProgramInfo channelInfo : channelList) 
			 {
				 EPGManageItemView view = new EPGManageItemView(mContext, this);
				 view.setItem1text("CH " + channelInfo.number);
				 view.setItem2text(channelInfo.serviceName);
				 channellistLayout.addView(view, LayoutParams.MATCH_PARENT, (int) (120 / div));
				 
				 epgItemDivider = new ImageView(mContext);
	             epgItemDivider.setBackgroundResource(R.drawable.setting_line);
	             LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,(int) (1 / div));
	             layoutParams.setMargins((int) (20 / div), 0, (int) (20 / div), 0);
	             channellistLayout.addView(epgItemDivider, layoutParams);
				 
				 epgchannellistItemView.add(view);
			 }
			 for(int i = 0;i<epgchannellistItemView.size();i++)
			 {
				 if(epgchannellistItemView.get(i).getItem2text() != null)
				 {
					 if(epgchannellistItemView.get(i).getItem2text().equals(curProgramInfo.serviceName)
							 && epgchannellistItemView.get(i).getItem1text().equals("CH " + curProgramInfo.number))
					 {
						 epgchannellistItemView.get(i).resetFocus();
						 channellistLayout.setFocusable(true);
						 channeldateLayout.setFocusable(false);
						 programLayout.setFocusable(false);
						 epginfomationScrollView.setFocusable(false);
						 TvEpgData.ChannelListPosition = i;
						 TvEpgData.FocusWindow = TvEpgData.EPGWindows.EPG_WINDOW_CHANNEL_LIST;
					 } 
				 }
				 else 
				 {
					 if(epgchannellistItemView.get(i).getItem1text().equals("CH " + curProgramInfo.number))
					 {
						 epgchannellistItemView.get(i).resetFocus();
						 channellistLayout.setFocusable(true);
						 channeldateLayout.setFocusable(false);
						 programLayout.setFocusable(false);
						 epginfomationScrollView.setFocusable(false);
						 TvEpgData.ChannelListPosition = i;
						 TvEpgData.FocusWindow = TvEpgData.EPGWindows.EPG_WINDOW_CHANNEL_LIST;
					 } 
				 }
			 }
		 }
		 Log.i("gky", getClass().toString() +"-->UpdateChannelListView::end");
		 
	 }
	 
	 public void UpdateEpgDateView()
	 {
		 epgDateList = TvPluginControler.getInstance().getEpgManager().getEPGDateList();
		 if(null != epgDateList && epgDateList.size() > 0)
		 {
			 channeldateLayout.removeAllViews();
			 epgDateListView.clear();
			 for (TvEpgDateListData epgDateListData : epgDateList)
			 {
				 Log.i("gky", getClass().getName() + "---UpdateEpgDateView "+epgDateListData.EpgDate);
				 EPGChannelDateItemView view = new EPGChannelDateItemView(mContext, this);
				 view.setItem1text(epgDateListData.EpgDate);
				 view.setItem2text(epgDateListData.EpgWeek);
				 channeldateLayout.addView(view, LayoutParams.MATCH_PARENT, (int) (120 / div));
				 
				 epgItemDivider = new ImageView(mContext);
	             epgItemDivider.setBackgroundResource(R.drawable.setting_line);
	             LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,(int) (1 / div));
	             layoutParams.setMargins((int) (10 / div), 0, (int) (10 / div), 0);
	             channeldateLayout.addView(epgItemDivider, layoutParams);
	             
				 epgDateListView.add(view);
			 }
			 chLock = false;
		 }
		 channeldateLayout.invalidate();
	 }
	 
	 public void UpdateProgramListView()
	 {
		 Log.i("gky", getClass().toString() + "-->UpdateProgramListView");
		 if(null != epgInfoList)
		 {
			 programLayout.removeAllViews();
			 epgprogramlistItemView.clear();
			 for(int i = 0;i < epgInfoList.size();i++)
			 {
				 Log.i("gky", getClass().getName() + "::UpdateProgramListView "+epgInfoList.getEventName(i));
				 EPGProgramNameItemView view = new EPGProgramNameItemView(mContext, this);
				 view.setItem1text(epgInfoList.getTimeInfo(i));
				 view.setItem2text(epgInfoList.getEventName(i));
				 programLayout.addView(view, LayoutParams.MATCH_PARENT, (int) (120 / div));
				 
				 epgItemDivider = new ImageView(mContext);
	             epgItemDivider.setBackgroundResource(R.drawable.setting_line);
	             LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,(int) (1 / div));
	             layoutParams.setMargins((int) (30 / div), 0, (int) (30 / div), 0);
	             programLayout.addView(epgItemDivider, layoutParams);
				 
	             epgprogramlistItemView.add(view);
			 }
			 dateLock = false;
		 }
		 programLayout.invalidate();
		 Log.i("gky", getClass().toString() + "-->UpdateProgramListView end");
	 }
	 
	 public void UpdateInfomationListView(int index)
	 {
		if(epgInfoList != null && index >= 0 && index < epgInfoList.size())
		 {
			 Log.i("gky", getClass().getName() + "--->UpdateInfomationListView "+epgInfoList.getEventName(index));
			 epginfomationnameTextView.setText(epgInfoList.getEventName(index));
			 epginfomationdaTextView.setText(epgInfoList.getTimeInfo(index));
			 if(null == epgInfoList.getEventDescription(index)||epgInfoList.getEventDescription(index).equals(""))
			 {
				 epginfomationTextView.setText(R.string.str_source_info_no_information); 
			 }
			 else
			 {
				 epginfomationTextView.setText(epgInfoList.getEventDescription(index));
			 }			 
		 }
		 else
		 {
			 epginfomationTextView.setText(R.string.str_source_info_no_information);
		 }
	 }
	 
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_UP)
			return false; 
		Log.i("gky", getClass().toString() + "-->onKeyDown::keycode is " + keyCode + " FocusWindow is " + TvEpgData.FocusWindow.toString()
				+" "+TvEpgData.ChannelListPosition+" "+TvEpgData.EPGDateListPosition+" "+TvEpgData.ProgramListPosition);
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MEDIA_REWIND
				|| keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_ENTER_EPG)
		{
			TvEpgData.ChannelListPosition = 0;
			TvEpgData.EPGDateListPosition = 0;
			TvEpgData.ProgramListPosition = 0;
			TvEpgData.EPGInformationPosition = 0;
			parentDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_MANAGE,DialogCmd.DIALOG_DISMISS, null);
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			switch (TvEpgData.FocusWindow) {
			case EPG_WINDOW_CHANNEL_LIST:
				if (TvEpgData.ChannelListPosition < epgchannellistItemView.size() ){
					if(TvEpgData.ChannelListPosition < epgchannellistItemView.size() - 1)
						TvEpgData.ChannelListPosition++;
					epgchannellistItemView.get(TvEpgData.ChannelListPosition).resetFocus();
					repeatUpdateEpgInfo();
					//mHandler.sendEmptyMessage(CLEAR_DATE_PRO_INFO);
				}
				Log.i("gky", getClass().toString()+ "-->onKeyDown::DOWN::EPG_WINDOW_CHANNEL_LIST "+TvEpgData.ChannelListPosition);
				return true;
			case EPG_WINDOW_EPG_DATE:
				if (TvEpgData.EPGDateListPosition < epgDateListView.size()){
					if(TvEpgData.EPGDateListPosition < epgDateListView.size() - 1)
						TvEpgData.EPGDateListPosition++;
					epgDateListView.get(TvEpgData.EPGDateListPosition).resetFocus();
					epgInfoList = TvPluginControler.getInstance().getEpgManager().getEpgInfos(channelList.get(TvEpgData.ChannelListPosition), 
							epgDateList.get(TvEpgData.EPGDateListPosition).EpgFullDate);
					mHandler.sendEmptyMessage(UPDATE_PROLISH);
				}
				Log.i("gky", getClass().toString()+ "-->onKeyDown::DOWN::EPG_WINDOW_EPG_DATE "+TvEpgData.EPGDateListPosition);
				return true;
			case EPG_WINDOW_PROGRAM_LIST:
				if (TvEpgData.ProgramListPosition < epgprogramlistItemView.size()){
					if(TvEpgData.ProgramListPosition < epgprogramlistItemView.size() - 1)
						TvEpgData.ProgramListPosition++;
					UpdateInfomationListView(TvEpgData.ProgramListPosition);
					epgprogramlistItemView.get(TvEpgData.ProgramListPosition)
							.resetFocus();
				}
				Log.i("gky", getClass().toString()+ "-->onKeyDown::DOWN::EPG_WINDOW_PROGRAM_LIST "+TvEpgData.ProgramListPosition);
				return true;
			case EPG_WINDOW_EPG_INFORMATION:				
				epginfomationScrollView.scrollBy(0,epginfomationScrollView.getMeasuredHeight());
				return true;				
			default:
				break;
			}
			return false;
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			switch (TvEpgData.FocusWindow) {
			case EPG_WINDOW_CHANNEL_LIST:
				if (TvEpgData.ChannelListPosition >= 0){
					if(TvEpgData.ChannelListPosition != 0)
						TvEpgData.ChannelListPosition--;
					epgchannellistItemView.get(TvEpgData.ChannelListPosition).resetFocus();
					repeatUpdateEpgInfo();
					//mHandler.sendEmptyMessage(CLEAR_DATE_PRO_INFO);
				}
				Log.i("gky", getClass().toString()+ "-->onKeyDown::UP::EPG_WINDOW_CHANNEL_LIST "+TvEpgData.ChannelListPosition);
				return true;
			case EPG_WINDOW_EPG_DATE:
				if(TvEpgData.EPGDateListPosition >= 0){
					if(TvEpgData.EPGDateListPosition != 0)
						TvEpgData.EPGDateListPosition--;
					epgDateListView.get(TvEpgData.EPGDateListPosition).resetFocus();
					epgInfoList = TvPluginControler.getInstance().getEpgManager().getEpgInfos(channelList.get(TvEpgData.ChannelListPosition), 
							epgDateList.get(TvEpgData.EPGDateListPosition).EpgFullDate);
					mHandler.sendEmptyMessage(UPDATE_PROLISH);
				}
				Log.i("gky", getClass().toString()+ "-->onKeyDown::UP::EPG_WINDOW_EPG_DATE "+TvEpgData.EPGDateListPosition);
				return true;
			case EPG_WINDOW_PROGRAM_LIST:
				if (TvEpgData.ProgramListPosition >= 0){
					if(TvEpgData.ProgramListPosition != 0)
						TvEpgData.ProgramListPosition--;
					UpdateInfomationListView(TvEpgData.ProgramListPosition);
					epgprogramlistItemView.get(TvEpgData.ProgramListPosition)
							.resetFocus();
				}
				Log.i("gky", getClass().toString()+ "-->onKeyDown::UP::EPG_WINDOW_PROGRAM_LIST "+TvEpgData.ProgramListPosition);
				return true;
			case EPG_WINDOW_EPG_INFORMATION:				
				epginfomationScrollView.scrollBy(0,-epginfomationScrollView.getMeasuredHeight());
				return true;		
			default:
				break;
			}
			return false;
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{
			switch(TvEpgData.FocusWindow)
			{
			case EPG_WINDOW_CHANNEL_LIST:
				break;
			case EPG_WINDOW_EPG_DATE:
				channellistLayout.setFocusable(true);
				channeldateLayout.setFocusable(false);
				programLayout.setFocusable(false);
				epginfomationScrollView.setFocusable(false);
				epgchannellistItemView.get(TvEpgData.ChannelListPosition).resetFocus();
				TvEpgData.FocusWindow = TvEpgData.EPGWindows.EPG_WINDOW_CHANNEL_LIST;
				TvEpgData.EPGDateListPosition = 0;
				break;
			case EPG_WINDOW_PROGRAM_LIST:
				channellistLayout.setFocusable(false);
				channeldateLayout.setFocusable(true);
				programLayout.setFocusable(false);
				epginfomationScrollView.setFocusable(false);
				epgDateListView.get(TvEpgData.EPGDateListPosition).resetFocus();
				TvEpgData.FocusWindow = TvEpgData.EPGWindows.EPG_WINDOW_EPG_DATE;
				TvEpgData.ProgramListPosition = 0;
				break;
			case EPG_WINDOW_EPG_INFORMATION:
				channellistLayout.setFocusable(false);
				channeldateLayout.setFocusable(false);
				programLayout.setFocusable(true);
				epginfomationScrollView.setFocusable(false);
				epgprogramlistItemView.get(0).resetFocus();				
				TvEpgData.FocusWindow = TvEpgData.EPGWindows.EPG_WINDOW_PROGRAM_LIST;
				TvEpgData.EPGInformationPosition = 0;
				break;
			default:
				break;
			}
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
		{
			switch(TvEpgData.FocusWindow)
			{
			case EPG_WINDOW_CHANNEL_LIST:
				if(chLock)
					return true;
				epgchannellistItemView.get(TvEpgData.ChannelListPosition).setBackgroundColor(Color.parseColor("#153E5A"));
				channellistLayout.setFocusable(false);
				channeldateLayout.setFocusable(true);
				programLayout.setFocusable(false);
				epginfomationScrollView.setFocusable(false);
				epgDateListView.get(0).resetFocus();
				TvEpgData.FocusWindow = TvEpgData.EPGWindows.EPG_WINDOW_EPG_DATE;
				break;
			case EPG_WINDOW_EPG_DATE:
				if(dateLock)
					return true;
				epgDateListView.get(TvEpgData.EPGDateListPosition).setBackgroundColor(Color.parseColor("#173A50"));
				channellistLayout.setFocusable(false);
				channeldateLayout.setFocusable(false);
				programLayout.setFocusable(true);
				epginfomationScrollView.setFocusable(false);
				epgprogramlistItemView.get(0).resetFocus();
				TvEpgData.FocusWindow = TvEpgData.EPGWindows.EPG_WINDOW_PROGRAM_LIST;
				break;
			case EPG_WINDOW_PROGRAM_LIST:
				channellistLayout.setFocusable(false);
				channeldateLayout.setFocusable(false);
				programLayout.setFocusable(false);
				epginfomationScrollView.setFocusable(true);
				epginfomationScrollView.requestFocus();
				TvEpgData.FocusWindow = TvEpgData.EPGWindows.EPG_WINDOW_EPG_INFORMATION;
				break;
			case EPG_WINDOW_EPG_INFORMATION:
				break;
			default:
				break;
			}
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER
				|| keyCode == KeyEvent.KEYCODE_ENTER)
		{
			if(TvEpgData.FocusWindow == EPGWindows.EPG_WINDOW_CHANNEL_LIST)
			{
				if (TvEpgData.ChannelListPosition >= 0
						&& TvEpgData.ChannelListPosition < channelList.size()) {
					TvPluginControler.getInstance().getChannelManager()
							.setProgram(channelList.get(TvEpgData.ChannelListPosition));
					refreshCount = 0;
					repeatUpdateEpgInfo();
				}
			}
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_YELLOW)
		{
			parentDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_MANAGE,DialogCmd.DIALOG_DISMISS, null);
			EPGScheduleListDialog EPGScheduleListDialog = new EPGScheduleListDialog();
			EPGScheduleListDialog.setDialogListener(this);
			EPGScheduleListDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_SCHEDULE_LIST, DialogCmd.DIALOG_SHOW, null);
			return true;
		}
//		else if((keyCode == KeyEvent.KEYCODE_PROG_RED) /*&& (epgInfoList != null) && (epgInfoList.size() > 0)*/)
//		{
//			parentDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_MANAGE,DialogCmd.DIALOG_DISMISS, null);
//			if(TvEpgData.FocusWindow == EPGWindows.EPG_WINDOW_CHANNEL_LIST)
//			{
//				EPGRecordSettingDialog EPGRecordSettingDialog = new EPGRecordSettingDialog();
//				EPGRecordSettingDialog.setDialogListener(this);
//				Intent data = new Intent();
//				data.putExtra("EPGInfos", epgInfoList);
//				data.putParcelableArrayListExtra("ChannelList", channelList);
//				data.putExtra("EpgIndex", TvEpgData.ProgramListPosition);
//				data.putExtra("ChannelIndex", TvEpgData.ChannelListPosition);
//				data.putExtra("isProgramList", false);
//				TvEpgData.ChannelListPosition = 0;
//				TvEpgData.EPGDateListPosition = 0;
//				TvEpgData.ProgramListPosition = 0;
//				TvEpgData.EPGInformationPosition = 0;
//				EPGRecordSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_RECORD_SETTING, DialogCmd.DIALOG_SHOW, data);
//			}
//			else if(TvEpgData.FocusWindow == EPGWindows.EPG_WINDOW_PROGRAM_LIST)
//			{
//				EPGRecordSettingDialog EPGRecordSettingDialog = new EPGRecordSettingDialog();
//				EPGRecordSettingDialog.setDialogListener(null);
//				Intent data = new Intent();
//				data.putExtra("EPGInfos", epgInfoList);
//				data.putParcelableArrayListExtra("ChannelList", channelList);
//				data.putExtra("EpgIndex", TvEpgData.ProgramListPosition);
//				data.putExtra("ChannelIndex", TvEpgData.ChannelListPosition);
//				data.putExtra("isProgramList", true);
//				TvEpgData.ChannelListPosition = 0;
//				TvEpgData.EPGDateListPosition = 0;
//				TvEpgData.ProgramListPosition = 0;
//				TvEpgData.EPGInformationPosition = 0;
//				EPGRecordSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_RECORD_SETTING, DialogCmd.DIALOG_SHOW, data);
//			}
//			else
//			{
//				EPGRecordSettingDialog EPGRecordSettingDialog = new EPGRecordSettingDialog();
//				EPGRecordSettingDialog.setDialogListener(null);
//				Intent data = new Intent();
//				data.putExtra("EPGInfos", epgInfoList);
//				data.putParcelableArrayListExtra("ChannelList", channelList);
//				data.putExtra("EpgIndex", TvEpgData.ProgramListPosition);
//				data.putExtra("ChannelIndex", TvEpgData.ChannelListPosition);
//				data.putExtra("isProgramList", false);
//				TvEpgData.ChannelListPosition = 0;
//				TvEpgData.EPGDateListPosition = 0;
//				TvEpgData.ProgramListPosition = 0;
//				TvEpgData.EPGInformationPosition = 0;
//				EPGRecordSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_RECORD_SETTING, DialogCmd.DIALOG_SHOW, data);
//			}
//			return true;
//		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_BLUE /*&& epgInfoList != null && epgInfoList.size() > 0*/)
		{
			parentDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_MANAGE,DialogCmd.DIALOG_DISMISS, null);
			EPGReminderSettingDialog EPGReminderSettingDialog = new EPGReminderSettingDialog();
			EPGReminderSettingDialog.setDialogListener(this);
			Intent data = new Intent();
			data.putExtra("EPGInfos", epgInfoList);
			data.putParcelableArrayListExtra("ChannelList", channelList);
			data.putExtra("EpgIndex", TvEpgData.ProgramListPosition);
			data.putExtra("ChannelIndex", TvEpgData.ChannelListPosition);
			TvEpgData.ChannelListPosition = 0;
			TvEpgData.EPGDateListPosition = 0;
			TvEpgData.ProgramListPosition = 0;
			TvEpgData.EPGInformationPosition = 0;
			EPGReminderSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_REMINDER_SETTING, DialogCmd.DIALOG_SHOW, data);
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_GREEN || keyCode == KeyEvent.KEYCODE_PROG_RED)
		{
			return true;
		}
		return quickKeyListener.onQuickKeyDownListener(keyCode, event);
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
     private ImageView initiImageView()
     {
		ImageView line = new ImageView(mContext);
		line.setBackgroundColor(Color.parseColor("#4D4D4D"));
		line.setLayoutParams(new LinearLayout.LayoutParams((int) (1 / dipDiv), LayoutParams.MATCH_PARENT));
		return line;
     }
     
     public void setParentDialog(EPGManageDialog parentDialog) {
 		this.parentDialog = parentDialog;
 	 }
     
     public Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case SHOW_LOADING:
				loadingDialog.processCmd(null, DialogCmd.DIALOG_SHOW, new TvLoadingData("", ""));
				break;
			case DISMISS_LOADING:
				if(loadingDialog.isShowing())
					loadingDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
				break;
			case UPDATE_PROLISH:
				if(epgInfoList != null)
					UpdateProgramListView();
				break;			
			case CLEAR_DATE_PRO_INFO:
				channeldateLayout.removeAllViews();
				epgDateListView.clear();
				programLayout.removeAllViews();
				epgprogramlistItemView.clear();
				epginfomationdaTextView.setText("");
				epginfomationnameTextView.setText("");
				epginfomationTextView.setText("");
				TvEpgData.EPGDateListPosition = 0;
				TvEpgData.ProgramListPosition = 0;
				chLock = true;
				dateLock = true;
//				resetBgColor(true);
				break;
			case UPDATE_EPGLISH:
				if(epgInfoList != null && epgInfoList.size() > 0){
			    	 UpdateProgramListView();
			    	 UpdateInfomationListView(TvEpgData.ProgramListPosition);
			     }
			     else 
			     {
			    	 epginfomationTextView.setText(R.string.str_source_info_no_information);
				 } 
				 break;
			case UPDATE_DATE_PRO_INFO:
				if(epgInfoList != null && epgInfoList.size() > 0)
				{
					Log.i("gky", getClass().getName() + "-->handleMessage updateView refreshCount is "+refreshCount);
					refreshCount = 0;
					//resetBgColor(false);
					UpdateEpgDateView();
					UpdateProgramListView();
					UpdateInfomationListView(TvEpgData.ProgramListPosition);
				}
				else 
				{
					refreshCount++;
					if(refreshCount < 3){//正常情况下，各个频道时间一致，刷新基数不必太高
						repeatUpdateEpgInfo();
					}
					else{
						Log.i("gky", getClass().getName() + "-->handleMessage refreshCount is "+refreshCount);
						refreshCount = 0;
						mHandler.sendEmptyMessage(CLEAR_DATE_PRO_INFO);
						// TvUIControler.getInstance().showMniToast(mContext.getResources().getString(R.string.str_tip_empty_epg_list));
						epginfomationTextView.setText(R.string.str_source_info_no_information);
					}
				}
			}
		}
     };
     
     public void resetBgColor(boolean isTrans){
    	 if(isTrans)
    	 {
    		 typeLayout.setBackgroundColor(TRANSPARENT_COLOR);
    		 channellistScrollview.setBackgroundColor(TRANSPARENT_COLOR);
    		 channeldateScrollView.setBackgroundColor(TRANSPARENT_COLOR);
    		 programScrollView.setBackgroundColor(TRANSPARENT_COLOR);
    		 infomationLayout.setBackgroundColor(TRANSPARENT_COLOR);
    	 }
    	 else 
    	 {
    		 typeLayout.setBackgroundColor(Color.parseColor("#323232"));
    		 channellistScrollview.setBackgroundColor(Color.parseColor("#191919"));
    		 channeldateScrollView.setBackgroundColor(Color.parseColor("#000000"));
    		 programScrollView.setBackgroundColor(Color.parseColor("#1E4765"));
    		 infomationLayout.setBackgroundColor(Color.parseColor("#191919"));
    	 }
     }
     
     public void repeatUpdateEpgInfo(){
    	long start = System.currentTimeMillis();
		TvPluginControler.getInstance().getEpgManager().resetAllNextBaseTime();
		epgInfoList = TvPluginControler.getInstance().getEpgManager().getCurEpgEventInfos(channelList.get(TvEpgData.ChannelListPosition));
		mHandler.sendEmptyMessage(UPDATE_DATE_PRO_INFO);
		long end = System.currentTimeMillis();
		Log.i("gky", getClass().getSimpleName() + " repeatUpdateEpgInfo take "+(end-start)+"ms");
     }

	@Override
	public boolean onShowDialogDone(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object... obj) {
		// TODO Auto-generated method stub
		if(parentDialog != null)
			parentDialog.showUI();
		return 0;
	}
}
