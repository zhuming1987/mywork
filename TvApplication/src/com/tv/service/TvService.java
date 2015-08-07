package com.tv.service;

import java.util.List;

import com.tv.app.R;
import com.tv.app.TvActivity;
import com.tv.app.TvUIControler;
import com.tv.data.TvType;
import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigDefs.TV_CFG_SLEEP_TIMER_ENUM_TYPE;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.service.aidl.IOverseaTvService;
import com.tv.service.aidl.ISwitchSourceListener;
import com.tv.ui.CountDown.TvCountDownDialog;
import com.tv.ui.CountDown.TvCountFunc;
import com.tv.ui.SettingView.TvSettingDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.volumebar.VolumeBarDialog;
import com.tv.ui.volumebar.VolumeBarView;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;

public class TvService extends Service{
	
	private Handler sleeptimehandler = new Handler();
	private Handler offtimehandler = new Handler();
	private Handler energysavinghandler = new Handler();
    
	private ExternalSourceBinder exSourceBinder;
	TvSettingDialog quickDialog = null;
	
	SharedPreferences sp = TvContext.context.getSharedPreferences(TvConfigDefs.TV_CFG_SLEEP_TIMER, Context.MODE_PRIVATE);
	private long sleepTimeLongth = 999999999;
	private static final String TAG = "TvService";
	
	private static boolean isPowering = false;
	
	private static final int TIME_CLOCK = 1001;
	
	private VolumeBarDialog volumeBarDialog = null;
	
	public class ExternalSourceBinder extends IOverseaTvService.Stub
	{

		@Override
		public void setSource() throws RemoteException {
			// TODO Auto-generated method stub
			Log.i("wangxian", "  external source setSource() succeed !!!!!!!!!!");
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					TvType.getInstance().getDataControler().updateByAll();
//				}
//			}).start();
			//updateHDMIDisplayFormat(); 6M10G无3D功能,取消自动3D功能
		}

		@Override
		public void initSwitchSourceListener(ISwitchSourceListener iSwitchSourceListener)
				throws RemoteException {
			// TODO Auto-generated method stub
			Log.i("gky", getClass().toString() + "initSwitchSourceListener is "+iSwitchSourceListener);
			TvPluginControler.getInstance().setSwitchSource(iSwitchSourceListener);
		}
		
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("gky", getClass().getSimpleName() + "----------->onCreate isPowering is "+isPowering);
		
		new TvBroadcastReceiver();
		
		exSourceBinder = new ExternalSourceBinder();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				TvType.getInstance(); 
			}
		}).start();
		
		if(sp.getInt(TvConfigDefs.SLEEP_TIME_COUNT, 0) != 0)
		{
			sleeptimehandler.postDelayed(sleeptimerunnable,sleepTimeLongth);
		}
		
		if(sp.getInt(TvConfigDefs.AUTO_SLEEP_COUNT, 0) != 0)
		{
			energysavinghandler.postDelayed(energysavingrunnable, sp.getInt(TvConfigDefs.AUTO_SLEEP_COUNT, 9999*60*60*1000));
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG,"service onBind");
		return exSourceBinder;
	}

	public class TvBroadcastReceiver extends BroadcastReceiver{
		
		private EventHandler mHandler = null;
		
		private static final int QUICK_KEY_MSG = 741;
		private static final int MSG_VOLUME_KEY = 49;
		
		public TvBroadcastReceiver()
		{
			mHandler = new EventHandler();
			
			IntentFilter quickFilter = new IntentFilter();
			quickFilter.addAction(TvConfigTypes.ACTION_QUICK_KEY);
			TvContext.context.registerReceiver(this, quickFilter);
			
			IntentFilter keyFilter = new IntentFilter();
			keyFilter.addAction(TvConfigTypes.ACTION_ALLKEY_EVENT);
			TvContext.context.registerReceiver(this, keyFilter);
			
			IntentFilter timerFilter = new IntentFilter();
			timerFilter.addAction(TvConfigTypes.ACTION_KEY_EVENT);
			TvContext.context.registerReceiver(this, timerFilter);
			
			IntentFilter hotKeyFilter = new IntentFilter();
			hotKeyFilter.addAction("com.android.sky.SendHotKey");
			TvContext.context.registerReceiver(this, hotKeyFilter);
			
			IntentFilter offTimerFilter = new IntentFilter();
			offTimerFilter.addAction(TvConfigTypes.ACTION_START_OFF_TIMER);
			TvContext.context.registerReceiver(this, offTimerFilter);
			
			IntentFilter sleepTimerFilter = new IntentFilter();
			sleepTimerFilter.addAction(TvConfigTypes.ACTION_CHANGE_SLEEP_TIME);
			TvContext.context.registerReceiver(this, sleepTimerFilter);
			
			IntentFilter autoStartFilter = new IntentFilter();
			autoStartFilter.addAction(Intent.ACTION_TIME_TICK);
			TvContext.context.registerReceiver(this, autoStartFilter);
			
			IntentFilter keyEventLockFilter = new IntentFilter();
			keyEventLockFilter.addAction(TvConfigTypes.ACTION_KEY_EVNET_KEYLOCKED);
			TvContext.context.registerReceiver(this, keyEventLockFilter);
			
			IntentFilter timezoneChangeFilter = new IntentFilter();
			timezoneChangeFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
			TvContext.context.registerReceiver(this, timezoneChangeFilter);
			
			IntentFilter voluemKey = new IntentFilter();
			voluemKey.addAction("com.skyworth.volume");
			TvContext.context.registerReceiver(this, voluemKey);
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//Log.i("gky", "onReceive action is " + intent.getAction());
			String action = intent.getAction();
			if(TvConfigTypes.ACTION_ALLKEY_EVENT.equals(action)){
				StickerDemoUIBroadReceiver.StartStickerDemoOnTimer();
			}
			else if(TvConfigTypes.ACTION_QUICK_KEY.equals(action))
			{
				if(!isApplicationBlack())
				{
					boolean flag_TXT = TvPluginControler.getInstance().getCommonManager().isTeletextDisplayed();
					boolean isAutoSearch = TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH)
							|| TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH);
					if((!flag_TXT)&&(!isAutoSearch))
					{
						mHandler.sendEmptyMessage(QUICK_KEY_MSG);
					}							
				}					
			}
			else if(TvConfigTypes.ACTION_KEY_EVENT.equals(action))
			{
				if(sp.getInt(TvConfigDefs.SLEEP_TIME_COUNT, 0) != 0)
				{
					//重置SleepTime计时器
					sleeptimehandler.removeCallbacks(sleeptimerunnable);
					sleeptimehandler.postDelayed(sleeptimerunnable,sleepTimeLongth);
				}
				
				if(sp.getInt(TvConfigDefs.AUTO_SLEEP_COUNT, 0) != 0)
				{
					//重置EnergySaving计时器
					energysavinghandler.removeCallbacks(energysavingrunnable);
					energysavinghandler.postDelayed(energysavingrunnable, sp.getInt(TvConfigDefs.AUTO_SLEEP_COUNT, 9999*60*60*1000));
				}
			}
			else if("com.android.sky.SendHotKey".equals(action))
			{
				int keyCode = intent.getIntExtra("specialKey",-1);
				//Log.i("gky", "specialKey key is " + keyCode);
				switch (keyCode) {
					case KeyEvent.KEYCODE_HOME:
					case KeyEvent.KEYCODE_TV_INPUT:
					case KeyEvent.KEYCODE_POWER:
					{
						Log.v("zhm","KeyEvent.KEYCODE_POWER--->>");
						if(keyCode == KeyEvent.KEYCODE_POWER){
							Log.i("gky", getClass().getSimpleName() + "#####---POWER----######---SET TRUE---######");
							TvService.isPowering = true;
						}
						if(keyCode != KeyEvent.KEYCODE_POWER)
						{
							TvUIControler.getInstance().reMoveAllDialogs();
						}
						else if(keyCode == KeyEvent.KEYCODE_POWER && TvPluginControler.getInstance().getCommonManager().getSTRStatus() == 1)
						{
							TvUIControler.getInstance().reMoveAllDialogs();	
							TvEnumSetData sleepTimerSet = new TvEnumSetData();
							sleepTimerSet.setCurrentIndex(0);
							TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_SLEEP_TIMER, sleepTimerSet);
							offtimehandler.removeCallbacks(offtimerunnable, null);
							offtimehandler.postDelayed(offtimerunnable, 999999999);
						}
						if(TvPluginControler.getInstance().getPvrManager().isRecording())
						{
							Log.v("zhm","TvPluginControler.getInstance().getPvrManager().isRecording() == true");
							TvPluginControler.getInstance().getPvrManager().stopRecord();
						}
						else if(TvPluginControler.getInstance().getPvrManager().isPlaybacking())
						{
							Log.v("zhm","TvPluginControler.getInstance().getPvrManager().isPlaybacking() == true");
							TvPluginControler.getInstance().getPvrManager().stopPlayback();
						}
						else if(TvPluginControler.getInstance().getPvrManager().isTimeShiftRecording())
						{
							Log.v("zhm","TvPluginControler.getInstance().getPvrManager().isTimeShiftRecording() == true");
							TvPluginControler.getInstance().getPvrManager().stopTimeShiftRecord();
						}
							
					}
						break;
					default:
						break;
				}
			}
			else if(TvConfigTypes.ACTION_START_OFF_TIMER.equals(action)){
//				long offTimeLong = intent.getLongExtra("offtimelong", 0);
//				offtimehandler.postDelayed(offtimerunnable, offTimeLong);
			}else if(TvConfigTypes.ACTION_CHANGE_SLEEP_TIME.equals(action)){
				long offTimeLong = intent.getLongExtra(TvConfigDefs.SLEEP_TIME_LONGTH,999999999);
				Log.v(TAG,"offTimeLong="+offTimeLong);
				offtimehandler.removeCallbacks(offtimerunnable, null);
				offtimehandler.postDelayed(offtimerunnable, offTimeLong);
			}else if(action.equals(Intent.ACTION_TIME_TICK)){
			}else if(TvConfigTypes.ACTION_KEY_EVNET_KEYLOCKED.equals(action))
			{
				TvUIControler.getInstance().showMniToast(TvContext.context.getResources().getString(R.string.TV_CFG_LOCK_KEY_EVENT_LOCK));
			}else if(action.equals(Intent.ACTION_TIMEZONE_CHANGED)){
				Log.i("gky", getClass().getSimpleName() + Thread.currentThread().getName()+ "-------->TimeZoneChange "+intent.getStringExtra("time-zone"));
				mHandler.sendEmptyMessage(TIME_CLOCK);
			}else if(action.equals("com.skyworth.volume")){
				Message msg = mHandler.obtainMessage();
				msg.what = MSG_VOLUME_KEY;
				msg.arg1 = intent.getIntExtra("specialKey",-1);
				mHandler.sendMessageAtFrontOfQueue(msg);
			}
		}
		
		@SuppressLint("HandlerLeak")
		public class EventHandler extends Handler
		{

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
					case QUICK_KEY_MSG:
						if((null==quickDialog)||(!quickDialog.isShowing()))
						{
							if(TvPluginControler.getInstance().getPvrManager().isRecording())
							{
								Log.v("zhm","TvPluginControler.getInstance().getPvrManager().isRecording() == true");
								TvPluginControler.getInstance().getPvrManager().stopRecord();
							}
							else if(TvPluginControler.getInstance().getPvrManager().isPlaybacking())
							{
								Log.v("zhm","TvPluginControler.getInstance().getPvrManager().isPlaybacking() == true");
								TvPluginControler.getInstance().getPvrManager().stopPlayback();
							}
							else if(TvPluginControler.getInstance().getPvrManager().isTimeShiftRecording())
							{
								Log.v("zhm","TvPluginControler.getInstance().getPvrManager().isTimeShiftRecording() == true");
								TvPluginControler.getInstance().getPvrManager().stopTimeShiftRecord();
							}
							TvUIControler.getInstance().reMoveAllDialogs();
							quickDialog = new TvSettingDialog();
							if(isTvAppRunning())
							{
								quickDialog.
									processCmd(TvConfigTypes.TV_DIALOG_SETTING, DialogCmd.DIALOG_SHOW, TvType.getInstance().getQuickMenuData().get(0));
							}								
							else
							{
								quickDialog.
									processCmd(TvConfigTypes.TV_DIALOG_SETTING, DialogCmd.DIALOG_SHOW, TvType.getInstance().getQuickMenuForOther().get(0));
							}	
						}
						break;
					case TIME_CLOCK:
						TvPluginControler.getInstance().getCommonManager().initSyncTimeAndCountry();
						break;
					case MSG_VOLUME_KEY:
						if(volumeBarDialog == null)
							volumeBarDialog = new VolumeBarDialog();
						if(!volumeBarDialog.isShowing())
							volumeBarDialog.showUI();
						volumeBarDialog.setVolume(msg.arg1);
						break;
					default:
						break;
				}
			}			
		}
	}
	
	static String[] blackApps = {
		"com.skyworth.cooface5.mainpage",
		//"com.tianci.localmedia",
		"com.tianci.tvmanager",
		"com.tianci.loader",
		"com.tianci.systeminfo",
		"com.tianci.qrcode",
		"com.tianci.samplehome",
		"com.tianci.pictureplayer",
		"com.netrange.launcher",
	};
	
	public static boolean isApplicationBlack() {
		ActivityManager am = (ActivityManager) TvContext.context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			Log.i("gky", "topActivity is " + topActivity.getPackageName());
			for (String appName : blackApps) {
				if(appName.equals(topActivity.getPackageName()))
					return true;
			}			
		}
		return false;
	}
	
	public static boolean isTvAppRunning(){
		ActivityManager am = (ActivityManager) TvContext.context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			Log.i("gky", "topActivity is " + topActivity.getPackageName()+" "+topActivity.getClassName());
			if("com.tv.app".equals(topActivity.getPackageName()))
				return true;
		}
		return false;
	}
	
	public static boolean isTvLauncherRunning(){
		ActivityManager am = (ActivityManager) TvContext.context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = am.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			Log.i("gky", "topActivity is " + topActivity.getPackageName()+" "+topActivity.getClassName());
			if("com.tianci.samplehome".equals(topActivity.getPackageName()))
				return true;
		}
		return false;
	}
	
	public static boolean isMstarServiceRunning(){
		ActivityManager aManager = (ActivityManager) TvContext.context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runAppProcessInfos = aManager.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : runAppProcessInfos) {
			Log.i("gky", "#######---->"+runningAppProcessInfo.processName+"/"+runningAppProcessInfo.importance);
			if(runningAppProcessInfo.processName.contains("com.mstar.tv.service")
					&& runningAppProcessInfo.importance != RunningAppProcessInfo.IMPORTANCE_FOREGROUND
					&& runningAppProcessInfo.importance != RunningAppProcessInfo.IMPORTANCE_VISIBLE){
				return true;
			}
		}
		return false;
	}
	
	Runnable sleeptimerunnable=new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			boolean isAutoSearch = TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH)
					|| TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH);
			boolean agingFlag = TvPluginControler.getInstance().getCommonManager().isAgingMode();
						
			if((!isAutoSearch) || (!agingFlag))
			{
				ShowTvCountDownDialog(sleeptimehandler,this,TvConfigDefs.SLEEP_TIME_COUNT);
				//sleeptimehandler.postDelayed(this, 2000);
			}
		}
	};
	
	public Runnable offtimerunnable=new Runnable(){
		@Override
		public void run() {
		    // TODO Auto-generated method stub
			Log.v("wxj","offtimerunnable");
			TvEnumSetData sleepTimerSet = new TvEnumSetData();
			sleepTimerSet.setCurrentIndex(TV_CFG_SLEEP_TIMER_ENUM_TYPE.TV_CFG_SLEEP_TIMER_OFF.ordinal());
			TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_SLEEP_TIMER,sleepTimerSet);
			
			TvType.getInstance().getDataControler().IDtoItem(TvConfigDefs.TV_CFG_SLEEP_TIMER).update();
			
			ShowTvCountDownDialog(offtimehandler,this,null);
			//offtimehandler.postDelayed(this, 2000);
		}
	};
	
	public Runnable energysavingrunnable=new Runnable(){
		@Override
		public void run() {
		    // TODO Auto-generated method stub
			ShowTvCountDownDialog(energysavinghandler,this,TvConfigDefs.AUTO_SLEEP_COUNT);
//			energysavinghandler.postDelayed(this, 2000);
		}
	};
	
	public void ShowTvCountDownDialog(Handler handler, Runnable runnable, String Type)
	{
		final Handler mHandler = handler;
		final Runnable mRunnable = runnable;
		final String mTimerTypeString = Type;
		TvCountDownDialog CountDownDialog = new TvCountDownDialog(new TvCountFunc()
		{
			@Override
			public void CountFinishFunction() {
				// TODO Auto-generated method stub
				Log.v(TAG,"TvService ,CountFinishFunction");
				TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
				if(curSource == TvEnumInputSource.E_INPUT_SOURCE_VGA)
				{
					Log.v("zhm","set VGA standby --->>");
					SystemProperties.set("mstar.vga.standby", "1");
				}
				TvActivity.instance.powerOffResume = true;
				TvPluginControler.getInstance().getCommonManager().standbySystem("standby");
			}
			
		});
		CountDownDialog.setDialogListener(new DialogListener() {
			
			@Override
			public boolean onShowDialogDone(int ID) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int onDismissDialogDone(Object... obj) {
				// TODO Auto-generated method stub
				int ret = (Integer) obj[0];
				if(ret != 0)
				{
					mHandler.removeCallbacks(mRunnable);
					mHandler.postDelayed(mRunnable, sp.getInt(mTimerTypeString, 0));
				}
				return 0;
			}
		});
		CountDownDialog.processCmd(TvConfigTypes.TV_DIALOG_COUNT_DOWN, DialogCmd.DIALOG_SHOW, null);
	}
	
	public void updateHDMIDisplayFormat() {
		TvEnumInputSource curSource = TvPluginControler.getInstance()
				.getCommonManager().getCurrentInputSource();
		if (curSource == TvEnumInputSource.E_INPUT_SOURCE_HDMI
				|| curSource == TvEnumInputSource.E_INPUT_SOURCE_HDMI2
				|| curSource == TvEnumInputSource.E_INPUT_SOURCE_HDMI3
				|| curSource == TvEnumInputSource.E_INPUT_SOURCE_HDMI4) {
			Log.i("gky", getClass().toString() + "-->updateHDMIDisplayFormat curSource is HDMI");
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					TvPluginControler.getInstance().getCommonManager()
							.updateHDMIDisplayFormatForThreeD();
				}
			}).start();
		}
	}
	
	@SuppressWarnings("unused")
	private boolean currentInputSourceIs(final String source) {
        TvEnumInputSource currInputSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
        if (source.equals("CVBS")) {
            if (currInputSource.ordinal() >= TvEnumInputSource.E_INPUT_SOURCE_CVBS.ordinal()
                    && currInputSource.ordinal() <= TvEnumInputSource.E_INPUT_SOURCE_CVBS_MAX.ordinal())
                return true;
        } else if (source.equals("SVIDEO")) {
            if (currInputSource.ordinal() >= TvEnumInputSource.E_INPUT_SOURCE_SVIDEO.ordinal()
                    && currInputSource.ordinal() <= TvEnumInputSource.E_INPUT_SOURCE_SVIDEO_MAX.ordinal())
                return true;
        } else if (source.equals("SCART")) {
            if (currInputSource.ordinal() >= TvEnumInputSource.E_INPUT_SOURCE_SCART.ordinal()
                    && currInputSource.ordinal() <= TvEnumInputSource.E_INPUT_SOURCE_SCART_MAX.ordinal())
                return true;
        }
        return false;
    }
	
	public static boolean isPowering(){
		return isPowering;
	}
	
	public static void setPowering(boolean flag){
		TvService.isPowering = flag;
	}
}
