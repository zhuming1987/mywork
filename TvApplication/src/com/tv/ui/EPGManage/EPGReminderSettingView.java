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

public class EPGReminderSettingView extends LinearLayout implements EnumItemListener, EnterItemViewListener{
	
	private String TAG = "EPGReminderSettingView";
	
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
	private EPGSettingEnumItemView repeatModeView = null;
	private EPGSettingEnterItemView startRecordView = null;;
	
	private static final int ITEM_ID_CHANNEL_LIST = 1001;
	private static final int ITEM_ID_START_MONTH = 1002;
	private static final int ITEM_ID_START_DAY = 1003;
	private static final int ITEM_ID_START_HOUR = 1004;
	private static final int ITEM_ID_START_MINUTE = 1005;
	private static final int ITEM_ID_START_RECORD = 1010;
	private static final int ITEM_ID_REPEATE_MODE = 1011;
	
	private static final int TIME_BASE_DEFAULT_DURATION_SEC = 600;
	
	private EPGReminderSettingDialog parentDialog = null;
	
	private static String[] months = TvContext.context.getResources().getStringArray(R.array.epg_record_moths);
	private static String[] days = TvContext.context.getResources().getStringArray(R.array.epg_record_days);
	private static String[] hours = TvContext.context.getResources().getStringArray(R.array.epg_record_hours);
	private static String[] minutes = TvContext.context.getResources().getStringArray(R.array.epg_record_minutes);
	private static String[] repeateModes = TvContext.context.getResources().getStringArray(R.array.epg_record_repeate_mode);
	private static String[] tips = TvContext.context.getResources().getStringArray(R.array.epg_record_time_tips);
	
	private static final int EPG_EVENT_REMIDER = 1;
	private static final int EPG_REPEAT_ONCE = 0x81;
    private static final int EPG_REPEAT_DAILY = 0x7F;
    private static final int EPG_REPEAT_WEEKLY = 0x82;
    
	private ArrayList<String> channelNames = new ArrayList<String>();
	private ArrayList<TvProgramInfo> channelList = null;
	private TvEpgInfos epgInfos = null;
	private int curChannelIndex = -1;
	private int curEpgIndex = -1;
	private Time starTime = new Time();
	private int offsetTimeInS = 0;
	private TvEpgEventTimerInfo epgEventTimerInfo = new TvEpgEventTimerInfo();
	
	public EPGReminderSettingView(Context context) {
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
		titleTextView.setText(context.getResources().getString(R.string.epgtipsreminder));
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
			Log.i("gky", getClass().getName() + "-->updateView:: curChannelIndex is "+curChannelIndex
					+" curEpgIndex is "+curEpgIndex);
			epgEventTimerInfo.enTimerType = EPG_EVENT_REMIDER;
			epgEventTimerInfo.serviceType = channelList.get(curChannelIndex).serviceType;
			epgEventTimerInfo.serviceNumber = channelList.get(curChannelIndex).number;
			Log.i("gky", getClass().getName() + "-->updateView:: serviceType is "+epgEventTimerInfo.serviceType
					+" serviceNumber is "+epgEventTimerInfo.serviceNumber);
			
			epgEventTimerInfo.eventID = curEpgIndex;
			epgEventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;

			for (TvProgramInfo proInfo : channelList) {
				channelNames.add("CH " + proInfo.number + " "+ proInfo.serviceName);
			}
			channelListView.updateView("Channel Name", curChannelIndex, channelNames);
			
			offsetTimeInS = (int)TvPluginControler.getInstance().getEpgManager().getoffsetTimeInS();
			Log.i("gky", getClass().getName() + "-->updateView:: offsetTimeInS is "+ offsetTimeInS);
			if(null == epgInfos)
			{
				TvStandardTime curTime = TvPluginControler.getInstance().getCommonManager().getCurTimer();
				curTime.second = 0;
				final int curTimeGapInS = (int) (curTime.toMillis(true) / 1000);
				epgEventTimerInfo.startTime = curTimeGapInS;
				epgEventTimerInfo.durationTime = TIME_BASE_DEFAULT_DURATION_SEC;
			}
			else
			{
				epgEventTimerInfo.startTime = epgInfos.getStartTime(curEpgIndex) - offsetTimeInS;
				epgEventTimerInfo.durationTime = epgInfos.getDurationTime(curEpgIndex);
			}
			
			Log.i("gky", getClass().getName() + "-->updateView:: eventID is "
					+ epgEventTimerInfo.eventID + " startTime is "
					+ epgEventTimerInfo.startTime + " durationTime is "
					+ epgEventTimerInfo.durationTime);
			
			starTime.set((long)epgEventTimerInfo.startTime * 1000L);
			Log.i("gky", getClass().getName() + "-->updateView:: startTime is "+ starTime.toString());
			
			startMonthView.updateView("Month", starTime.month,
					new ArrayList<String>(Arrays.asList(months)));
			startDayView.updateView("Day", starTime.monthDay - 1/* 1-31 */,
					new ArrayList<String>(Arrays.asList(days)));
			startHourView.updateView("Hour", starTime.hour,
					new ArrayList<String>(Arrays.asList(hours)));
			startMinuteView.updateView("Minute", starTime.minute,
					new ArrayList<String>(Arrays.asList(minutes)));

			repeatModeView.updateView("Repeate Mode", 0, new ArrayList<String>(
					Arrays.asList(repeateModes)));

			startRecordView.resetUI("Reminder this event");
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
			//epgEventTimerInfo.startTime = (int)(starTime.toMillis(true) / 1000);
			epgEventTimerInfo.enTimerType = EPG_EVENT_REMIDER;
			
			TimeZone tz = TimeZone.getDefault();
			int timezoneOffsetSeconds = tz.getOffset(starTime.toMillis(true))/1000;
			int localTimeSeconds = (int) (starTime.toMillis(true) / 1000) + timezoneOffsetSeconds;
			Time epgEventTime = new Time();
			epgEventTime.set((long)localTimeSeconds*1000 - tz.getRawOffset());
			int epgTimeOffsetSeconds = (int)TvPluginControler.getInstance().getEpgManager().getEpgEventOffsetTime(epgEventTime, true);
			epgEventTimerInfo.startTime = localTimeSeconds - epgTimeOffsetSeconds;
	                
			Log.i(TAG, "--->addEpgNewEvent startTime is "+starTime.toString());
			Log.i(TAG, "--->addEpgNewEvent curTime   is "+TvPluginControler.getInstance().getCommonManager().getCurTimer().toString());
			int ret = TvPluginControler.getInstance().getEpgManager().addEpgNewEvent(epgEventTimerInfo);
			TvUIControler.getInstance().showMniToast(tips[ret]);
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
			case ITEM_ID_REPEATE_MODE:
				if(index == 0){
					epgEventTimerInfo.enRepeatMode = EPG_REPEAT_ONCE;
					startMonthView.grayView(false);
					startDayView.grayView(false);
				}else if(index == 1){
					epgEventTimerInfo.enRepeatMode = EPG_REPEAT_DAILY;
					startMonthView.grayView(true);
					startDayView.grayView(true);
				}else if(index == 2){
					epgEventTimerInfo.enRepeatMode = EPG_REPEAT_WEEKLY;
					startMonthView.grayView(false);
					startDayView.grayView(false);
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

	public void setParentDialog(EPGReminderSettingDialog parentDialog) {
		this.parentDialog = parentDialog;
	}
}
