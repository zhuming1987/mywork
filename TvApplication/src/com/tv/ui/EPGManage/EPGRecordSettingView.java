package com.tv.ui.EPGManage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvEpgEventTimerInfo;
import com.tv.framework.data.TvEpgInfos;
import com.tv.framework.data.TvProgramInfo;
import com.tv.framework.data.TvStandardTime;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.EPGManage.EPGSettingEnterItemView.EnterItemViewListener;
import com.tv.ui.EPGManage.EPGSettingEnumItemView.EnumItemListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class EPGRecordSettingView extends LinearLayout implements EnumItemListener, EnterItemViewListener{

	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
	private Context mContext;
	
	private LinearLayout titleLayout = null;
	private TextView titleTextView = null;
	private ImageView titleImageView = null;
	
	private LinearLayout bodyLayout = null;
	private ScrollView bodyScrollView = null;
	
	private EPGSettingEnumItemView channelListView = null;
	private EPGSettingEnumItemView startMonthView = null;
	private EPGSettingEnumItemView startDayView = null;
	private EPGSettingEnumItemView startHourView = null;
	private EPGSettingEnumItemView startMinuteView = null;
	private EPGSettingEnumItemView endMonthView = null;
	private EPGSettingEnumItemView endDayView = null;
	private EPGSettingEnumItemView endHourView = null;
	private EPGSettingEnumItemView endMinuteView = null;
	private EPGSettingEnumItemView repeatModeView = null;
	private EPGSettingEnterItemView startRecordView = null;;
	
	private static final int ITEM_ID_CHANNEL_LIST = 1001;
	private static final int ITEM_ID_START_MONTH = 1002;
	private static final int ITEM_ID_START_DAY = 1003;
	private static final int ITEM_ID_START_HOUR = 1004;
	private static final int ITEM_ID_START_MINUTE = 1005;
	private static final int ITEM_ID_END_MONTH = 1006;
	private static final int ITEM_ID_END_DAY = 1007;
	private static final int ITEM_ID_END_HOUR = 1008;
	private static final int ITEM_ID_END_MINUTE = 1009;
	private static final int ITEM_ID_START_RECORD = 1010;
	private static final int ITEM_ID_REPEATE_MODE = 1011;
	
	private EPGRecordSettingDialog parentDialog = null;
	
	private static String[] months = TvContext.context.getResources().getStringArray(R.array.epg_record_moths);
	private static String[] days = TvContext.context.getResources().getStringArray(R.array.epg_record_days);
	private static String[] hours = TvContext.context.getResources().getStringArray(R.array.epg_record_hours);
	private static String[] minutes = TvContext.context.getResources().getStringArray(R.array.epg_record_minutes);
	private static String[] repeateModes = TvContext.context.getResources().getStringArray(R.array.epg_record_repeate_mode);
	private static String[] tips = TvContext.context.getResources().getStringArray(R.array.epg_record_time_tips);
	
	private static final int EPG_EVENT_RECORDER = 2;
	private static final int EPG_EVENT_RECORDER_EVENT_ID = 0x83;
	private static final int EPG_REPEAT_ONCE = 0x81;
    private static final int EPG_REPEAT_DAILY = 0x7F;
    private static final int EPG_REPEAT_WEEKLY = 0x82;
    private static final int TIME_GAP_FROM_NOW_SEC = 60;
    private static final int TIME_BASE_DEFAULT_DURATION_SEC = 600;
    
	private ArrayList<String> channelNames = new ArrayList<String>();
	private ArrayList<TvProgramInfo> channelList = null;
	private TvEpgInfos epgInfos = null;
	private int curChannelIndex = -1;
	private int curEpgIndex = -1;
	private boolean isRecordEventReady = false;
	private Time starTime = new Time();
	private Time endTime = new Time();
	private Time curTime = new Time();
	private TvEpgEventTimerInfo epgEventTimerInfo = new TvEpgEventTimerInfo();
	
	public EPGRecordSettingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams((int)(845/div), (int)(945/div)));
		
		titleLayout = new LinearLayout(context);
		titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		titleLayout.setPadding((int)(25/div), (int)(5/div), 0, (int)(5/div));
		titleLayout.setGravity(Gravity.CENTER_VERTICAL);
		titleLayout.setBackgroundColor(Color.parseColor("#3598DC"));
		titleImageView = new ImageView(context);
		titleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.tv_string_setting));
		LayoutParams titleImgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleImgParams.topMargin = (int)(15/div);
		titleImageView.setLayoutParams(titleImgParams);
		titleLayout.addView(titleImageView);
		titleTextView = new TextView(context);
		titleTextView.setTextSize((int)(52/dip));
		titleTextView.setTextColor(Color.WHITE);
		titleTextView.setText(context.getResources().getString(R.string.epgtipsrecord));
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams titletxtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		titletxtParams.leftMargin = (int)(25/div);
		titletxtParams.gravity = Gravity.CENTER_VERTICAL;
		titleTextView.setLayoutParams(titletxtParams);
		titleLayout.addView(titleTextView);
		this.addView(titleLayout, new LayoutParams((int)(845/div), (int)(140/div)));
		
		bodyScrollView = new ScrollView(context);
		bodyScrollView.setVerticalScrollBarEnabled(false);
		bodyScrollView.setHorizontalScrollBarEnabled(false);
		bodyScrollView.setScrollbarFadingEnabled(true);
		bodyScrollView.setAlwaysDrawnWithCacheEnabled(true);
		bodyScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		bodyScrollView.setBackgroundColor(Color.parseColor("#191919"));
		bodyScrollView.setPadding(0, (int)(5/div), 0, 0);
		
		bodyLayout = new LinearLayout(context);
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		bodyLayout.setBackgroundColor(Color.parseColor("#191919"));
		bodyScrollView.addView(bodyLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		this.addView(bodyScrollView, new LayoutParams(LayoutParams.MATCH_PARENT, (int)(945/div)));
	
		bodyLayout.addView(initDiverView());
		
		channelListView = new EPGSettingEnumItemView(context);
		channelListView.setId(ITEM_ID_CHANNEL_LIST);
		channelListView.setItemListener(this);
		bodyLayout.addView(channelListView);
		bodyLayout.addView(initDiverView());
		
		startMonthView = new EPGSettingEnumItemView(context);
		startMonthView.setId(ITEM_ID_START_MONTH);
		startMonthView.setItemListener(this);
		bodyLayout.addView(startMonthView);
		bodyLayout.addView(initDiverView());
		
		startDayView = new EPGSettingEnumItemView(context);
		startDayView.setId(ITEM_ID_START_DAY);
		startDayView.setItemListener(this);
		bodyLayout.addView(startDayView);
		bodyLayout.addView(initDiverView());
		
		startHourView = new EPGSettingEnumItemView(context);
		startHourView.setId(ITEM_ID_START_HOUR);
		startHourView.setItemListener(this);
		bodyLayout.addView(startHourView);
		bodyLayout.addView(initDiverView());
		
		startMinuteView = new EPGSettingEnumItemView(context);
		startMinuteView.setId(ITEM_ID_START_MINUTE);
		startMinuteView.setItemListener(this);
		bodyLayout.addView(startMinuteView);
		bodyLayout.addView(initDiverView());
		
		endMonthView = new EPGSettingEnumItemView(context);
		endMonthView.setId(ITEM_ID_END_MONTH);
		endMonthView.setItemListener(this);
		bodyLayout.addView(endMonthView);
		bodyLayout.addView(initDiverView());
		
		endDayView = new EPGSettingEnumItemView(context);
		endDayView.setId(ITEM_ID_END_DAY);
		endDayView.setItemListener(this);
		bodyLayout.addView(endDayView);
		bodyLayout.addView(initDiverView());
		
		endHourView = new EPGSettingEnumItemView(context);
		endHourView.setId(ITEM_ID_END_HOUR);
		endHourView.setItemListener(this);
		bodyLayout.addView(endHourView);
		bodyLayout.addView(initDiverView());
		
		endMinuteView = new EPGSettingEnumItemView(context);
		endMinuteView.setId(ITEM_ID_END_MINUTE);
		endMinuteView.setItemListener(this);
		bodyLayout.addView(endMinuteView);
		bodyLayout.addView(initDiverView());
		
		repeatModeView = new EPGSettingEnumItemView(context);
		repeatModeView.setId(ITEM_ID_REPEATE_MODE);
		repeatModeView.setItemListener(this);
		bodyLayout.addView(repeatModeView);
		bodyLayout.addView(initDiverView());
		
		startRecordView = new EPGSettingEnterItemView(context);
		startRecordView.setId(ITEM_ID_START_RECORD);
		startRecordView.setEnterItemViewListener(this);
		bodyLayout.addView(startRecordView);
		bodyLayout.addView(initDiverView());
	}
	
	public void updateView(Intent data){
		if(data != null && data.getExtras() != null){
			channelList = data.getParcelableArrayListExtra("ChannelList");
			epgInfos = data.getParcelableExtra("EPGInfos");
			curChannelIndex = data.getIntExtra("ChannelIndex", -1);
			curEpgIndex = data.getIntExtra("EpgIndex", -1);
			isRecordEventReady = data.getBooleanExtra("isProgramList", false);
			Log.i("gky", getClass().getName() + "-->updateView:: curChannelIndex is "+curChannelIndex
					+" curEpgIndex is "+curEpgIndex + " isRecordEventReady is "+isRecordEventReady);
			epgEventTimerInfo.enTimerType = EPG_EVENT_RECORDER;
			epgEventTimerInfo.serviceType = channelList.get(curChannelIndex).serviceType;
			epgEventTimerInfo.serviceNumber = channelList.get(curChannelIndex).number;
			Log.i("gky", getClass().getName() + "-->updateView:: serviceType is "+epgEventTimerInfo.serviceType
					+" serviceNumber is "+epgEventTimerInfo.serviceNumber);
			if(isRecordEventReady && (null != epgInfos)){
				epgEventTimerInfo.enRepeatMode = EPG_EVENT_RECORDER_EVENT_ID;
				epgEventTimerInfo.eventID = epgInfos.getEventId(curEpgIndex);
				epgEventTimerInfo.startTime = epgInfos.getStartTime(curEpgIndex);
				epgEventTimerInfo.durationTime = epgInfos.getDurationTime(curEpgIndex);
				Log.i("gky", getClass().getName() + "-->updateView:: eventID is "+epgEventTimerInfo.eventID
						+" startTime is "+epgEventTimerInfo.startTime
						+" durationTime is "+epgEventTimerInfo.durationTime);
				bodyLayout.removeAllViews();
				startRecordView.resetUI("Record this event");
				bodyLayout.addView(initDiverView());
				bodyLayout.addView(startRecordView);
				bodyLayout.addView(initDiverView());
			}else {
				epgEventTimerInfo.eventID = 0;
				epgEventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
				
				for (TvProgramInfo proInfo : channelList) {
					channelNames.add("CH "+proInfo.number+" "+proInfo.serviceName);
				}
				channelListView.updateView("Channel Name", curChannelIndex, channelNames);
				
				TvStandardTime curTime = TvPluginControler.getInstance().getCommonManager().getCurTimer();
				curTime.second = 0;
				final int curTimeGapInS = (int) (curTime.toMillis(true) / 1000) + TIME_GAP_FROM_NOW_SEC;
				Log.i("gky", getClass().getName() + "-->updateView:: curTimeGapInS is "+curTimeGapInS);
				int eventStartTimeInS = 0;
				if(null == epgInfos)
				{
					eventStartTimeInS = curTimeGapInS;
				}
				else
				{
					eventStartTimeInS = epgInfos.getOriginalStartTime(curEpgIndex);
				}
		        int eventDurationInS = TIME_BASE_DEFAULT_DURATION_SEC;
		        Log.i("gky", getClass().getName() + "-->updateView:: eventStartTimeInS is "+eventStartTimeInS
						+" eventDurationInS is "+eventDurationInS);
		        final int eventEndTimeInS = eventStartTimeInS + eventDurationInS;
	            if ((curTimeGapInS > eventStartTimeInS) && (curTimeGapInS < eventEndTimeInS)) {
	                eventDurationInS = eventEndTimeInS - curTimeGapInS;
	                eventStartTimeInS = curTimeGapInS;
	            }
	            
	            epgEventTimerInfo.startTime = eventStartTimeInS;
	            epgEventTimerInfo.durationTime = eventDurationInS;
	            Log.i("gky", getClass().getName() + "-->updateView:: eventID is "+epgEventTimerInfo.eventID
						+" startTime is "+epgEventTimerInfo.startTime
						+" durationTime is "+epgEventTimerInfo.durationTime);
	            starTime.set(eventStartTimeInS * 1000L);
	            endTime.set(eventEndTimeInS * 1000L);
	            Log.i("gky", getClass().getName() + "-->updateView:: startTime is "+starTime.toString());
	            Log.i("gky", getClass().getName() + "-->updateView:: endTime is "+endTime.toString());
	            startMonthView.updateView("Start Month", starTime.month, new ArrayList<String>(Arrays.asList(months)));
	            startDayView.updateView("Start Day", starTime.monthDay-1/*1-31*/, new ArrayList<String>(Arrays.asList(days)));
	            startHourView.updateView("Start Hour", starTime.hour, new ArrayList<String>(Arrays.asList(hours)));
	            startMinuteView.updateView("Start Minute", starTime.minute, new ArrayList<String>(Arrays.asList(minutes)));
			
	            endMonthView.updateView("End Month", endTime.month, new ArrayList<String>(Arrays.asList(months)));
	            endDayView.updateView("End Day", endTime.monthDay-1/*1-31*/, new ArrayList<String>(Arrays.asList(days)));
	            endHourView.updateView("End Hour", endTime.hour, new ArrayList<String>(Arrays.asList(hours)));
	            endMinuteView.updateView("End Minute", endTime.minute, new ArrayList<String>(Arrays.asList(minutes)));
			
	            repeatModeView.updateView("Repeate Mode", 0, new ArrayList<String>(Arrays.asList(repeateModes)));
			
	            startRecordView.resetUI("Record this event");
			}	
		}
	}

	private ImageView initDiverView(){
		ImageView diverLine = new ImageView(mContext);
		diverLine.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / div), 1);
		lineLp.setMargins((int) (30 / div), (int)(5/div),(int) (30 / div), (int)(5/div));
		diverLine.setLayoutParams(lineLp);
		return diverLine;
	}
	
	@Override
	public void onEnterItemViewListener(View view, int keyCode) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_BACK){
			backToMenu();
			return ;
		}
		Log.i("gky", getClass().toString() + "-->onEnterItemViewListener id is "+view.getId());
		if(view.getId() == ITEM_ID_START_RECORD){
			if(isRecordEventReady){
				epgEventTimerInfo.enTimerType = EPG_EVENT_RECORDER;
				int ret = TvPluginControler.getInstance().getEpgManager().addEpgNewEvent(epgEventTimerInfo);
				TvUIControler.getInstance().showMniToast(tips[ret]);
			}else {
				TvStandardTime curTime = TvPluginControler.getInstance().getCommonManager().getCurTimer();
				if(curTime.after(starTime)){
					TvUIControler.getInstance().showMniToast(tips[2]);
				}else if(starTime.after(endTime)){
					TvUIControler.getInstance().showMniToast(tips[4]);
				}else{
					TimeZone tz = TimeZone.getDefault();
	                int timezoneOffsetSeconds = tz.getOffset(starTime.toMillis(true))/1000;
	                int localTimeSeconds = (int) (starTime.toMillis(true) / 1000) + timezoneOffsetSeconds;
	                Time epgEventTime = new Time();
	                epgEventTime.set((long)localTimeSeconds*1000 - tz.getRawOffset());
	                int epgTimeOffsetSeconds = (int)TvPluginControler.getInstance().getEpgManager().getEpgEventOffsetTime(epgEventTime, true);
	                epgEventTimerInfo.startTime = localTimeSeconds - epgTimeOffsetSeconds;
					epgEventTimerInfo.durationTime = (int)(endTime.toMillis(true) / 1000) 
	                        - (int) (starTime.toMillis(true) / 1000);
					//		- epgEventTimerInfo.startTime;
					int ret = TvPluginControler.getInstance().getEpgManager().addEpgNewEvent(epgEventTimerInfo);
					TvUIControler.getInstance().showMniToast(tips[ret]);
				}
			}
		}
		backToMenu();
	}

	@Override
	public void onEnumItemChangeListener(View view, int keyCode, int index) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_BACK){
			backToMenu();
			return ;
		}
		Log.i("gky", getClass().toString() + "-->onEnumItemChangeListener id is "+view.getId() + " index is "+index);
		switch (view.getId()) {
			case ITEM_ID_CHANNEL_LIST:
				curChannelIndex = index;
				epgEventTimerInfo.serviceType = channelList.get(curChannelIndex).serviceType;
				epgEventTimerInfo.serviceNumber = channelList.get(curChannelIndex).number;
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: serviceType is "+epgEventTimerInfo.serviceType
						+" serviceNumber is "+epgEventTimerInfo.serviceNumber);
				break;
			case ITEM_ID_START_MONTH:
				starTime.month = Integer.parseInt(months[index])-1;/*0-11*/
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: starTime.month is "+starTime.month);
				break;
			case ITEM_ID_START_DAY:
				starTime.monthDay = Integer.parseInt(days[index]);
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: starTime.monthDay is "+starTime.monthDay);
				break;
			case ITEM_ID_START_HOUR:
				starTime.hour = Integer.parseInt(hours[index]);
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: starTime.hour is "+starTime.hour);
				break;
			case ITEM_ID_START_MINUTE:
				starTime.minute = Integer.parseInt(minutes[index]);
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: starTime.minute is "+starTime.minute);
				break;
			case ITEM_ID_END_MONTH:
				endTime.month = Integer.parseInt(months[index])-1;/*0-11*/
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: endTime.month is "+endTime.month);
				break;
			case ITEM_ID_END_DAY:
				endTime.monthDay = Integer.parseInt(days[index]);
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: endTime.monthDay is "+endTime.monthDay);
				break;
			case ITEM_ID_END_HOUR:
				endTime.hour = Integer.parseInt(hours[index]);
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: endTime.hour is "+endTime.hour);
				break;
			case ITEM_ID_END_MINUTE:
				endTime.minute = Integer.parseInt(minutes[index]);
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: endTime.minute is "+endTime.minute);
				break;
			case ITEM_ID_REPEATE_MODE:
				if(index == 0){
					epgEventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
					startMonthView.grayView(false);
					startDayView.grayView(false);
					endMonthView.grayView(false);
					endDayView.grayView(false);
				}else if(index == 1){
					epgEventTimerInfo.enRepeatMode = EPG_REPEAT_DAILY;
					startMonthView.grayView(true);
					startDayView.grayView(true);
					endMonthView.grayView(true);
					endDayView.grayView(true);
				}else if(index == 2){
					epgEventTimerInfo.enRepeatMode = EPG_REPEAT_WEEKLY;
					startMonthView.grayView(false);
					startDayView.grayView(false);
					endMonthView.grayView(false);
					endDayView.grayView(false);
				}
				Log.i("gky", getClass().getName() + "-->onEnumItemChangeListener:: enRepeatMode is "+epgEventTimerInfo.enRepeatMode);
				break;
			default:
				break;
		}
	}
	
	private void backToMenu(){
		Log.i("gky", getClass().toString() + "-->backMenu");
		parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
	}

	public void setParentDialog(EPGRecordSettingDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

}
