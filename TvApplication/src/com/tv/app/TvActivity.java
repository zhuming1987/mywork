package com.tv.app;

import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.skyworth.config.SkyConfigDefs.SKY_CFG_TV_SYSTEM_TYPE;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.factoryHotkey.FactoryHotKeyActivity;
import com.tv.framework.data.CiUIEventData;
import com.tv.framework.data.DiskSelectInfoData;
import com.tv.framework.data.TvLoadingData;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvSearchTypes.EnumDeskEvent;
import com.tv.framework.define.TvSearchTypes.EnumScreenMode;
import com.tv.framework.define.TvSearchTypes.EnumSignalProgSyncStatus;
import com.tv.framework.plugin.tvfuncs.CiManager.CiCmdNotifyListener;
import com.tv.framework.plugin.tvfuncs.CiManager.CiStatusChangeEventListener;
import com.tv.framework.plugin.tvfuncs.CiManager.CiUIEventListener;
import com.tv.framework.plugin.tvfuncs.CiManager.CiUIOpRefreshQueryListener;
import com.tv.framework.plugin.tvfuncs.CommonManager.TvScreenModeEventListener;
import com.tv.plugin.TvPluginControler;
import com.tv.service.TvService;
import com.tv.ui.CiInformation.CIInformationDialog;
import com.tv.ui.CountDown.TvCountDownDialog;
import com.tv.ui.CountDown.TvCountFunc;
import com.tv.ui.Loading.TvLoadingDialog;
import com.tv.ui.ParentrolControl.PasswordUnLockChannelDialog;
import com.tv.ui.Pvrsetting.PvrTimeshitfDlg;
import com.tv.ui.SourceInfo.AtvSourceInfoView;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.installguide.InstallGuideActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class TvActivity extends Activity implements SurfaceHolder.Callback{
	
	private SurfaceHolder mSurfaceHolder = null;
	private SurfaceView mSurfaceView = null;

	private QuickKeyListener mQuickKeyListener = null;
	
	private TextView noSignalTip = null;
	private ScheduledThreadPoolExecutor aniTimer;
	private int mSignalMode = EnumDeskEvent.EV_SCREEN_SAVER_MODE.ordinal();
	private int mSignalStatus = EnumScreenMode.MSRV_DTV_SS_COMMON_VIDEO.ordinal();
	private int countNoSignal = 0;
	private TvEnumInputSource mCurSource = TvEnumInputSource.E_INPUT_SOURCE_ATV;
	private boolean agingFlag = false;
	private final int NOSIGNAL_WIDTH = 550;
    private final int NOSIGNAL_HEIGHT = 240;
    private IntentFilter ssFilter;
    private AudioManager mAudioManager;
	private int width = TvUIControler.getInstance().getDisplayWidth();
    private int height = TvUIControler.getInstance().getDisplayHeight();
    private float resolutionDiv = TvUIControler.getInstance().getResolutionDiv();	
	public static TvActivity instance;
    private boolean ishasFocus=true;
    static int keyPlayOrPause = KeyEvent.KEYCODE_MEDIA_PLAY;

    Handler noSignalSleepTimer = new Handler(Looper.getMainLooper());
    private long noSignalSleepLeft = 10 * 60 * 1000;
    private static final long noSignalSleepVGALeft =  1 * 60 * 1000;
    private boolean noSignalStatus = false;
    public static boolean powerOffResume = false;
    private TvCountDownDialog CountDownDialog = null;
    private static final String TAG = "TvActivity";
    
    private HandlerThread mNosingalHT =null;
    private HandlerThread mStartSettingHT = null;
    private HandlerThread mAttcHT = null;
    private NoSingalHandler mHandler= null;
    private StartHandler mStartHandler = null;
    private AttcHandler mAttcHandler = null;
    private boolean isAlreadySyncDTV = false;
	private boolean isAlreadySyncNTP = false;
	private ConnectivityManager netManager = null;
	
	public static void broadcastHotelLocalKey(int value){
		Log.i("gky", "broadcastHotelLocalKey value is " + value);
		
		Intent intent = new Intent();
		intent.setAction("com.skyworth.hotel.disablekey");
		intent.putExtra("mode", value);
		TvContext.context.sendBroadcast(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("gky", "##############TvActivity::onCreate###########");
		setContentView(R.layout.main_layout);
		createSurfaceView();
		instance = this;
		initView();
		mNosingalHT = new HandlerThread("NoSignal");
		mNosingalHT.start();
		mHandler = new NoSingalHandler(mNosingalHT.getLooper());
		
		mQuickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		
		startTvService();
		
		netManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.unloadSoundEffects();
		Settings.System.putInt(getContentResolver(), Settings.System.SOUND_EFFECTS_ENABLED, 0);
		
		//根据开机导航标志判断是否启动开机导航界面
		if(TvPluginControler.getInstance().getCommonManager() != null){
			if(TvPluginControler.getInstance().getCommonManager().getInstallGuideFlag())
			{
				startInstallGuiActivity();
			}
		}
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("removealldialogs");
		registerReceiver(broadcastReceiver, intentFilter);		
		IntentFilter hotKeyFilter = new IntentFilter();
		hotKeyFilter.addAction("com.android.sky.SendHotKey");
		registerReceiver(broadcastReceiver, hotKeyFilter);
		
		IntentFilter startPVRFilter = new IntentFilter();
		startPVRFilter.addAction("startpvr");
		registerReceiver(broadcastReceiver, startPVRFilter);
		
		IntentFilter switchtodtvsource = new IntentFilter();
		switchtodtvsource.addAction("switchtodtvsource");
		registerReceiver(broadcastReceiver, switchtodtvsource);
		
		IntentFilter keyDownFilter = new IntentFilter();
		keyDownFilter.addAction(TvConfigTypes.ACTION_ALLKEY_EVENT);
		registerReceiver(broadcastReceiver, keyDownFilter);
		
		// 注册source switch广播接收 
		TvPluginControler.getInstance().getCommonManager().registerCecCtrlEventListener();
		ssFilter = new IntentFilter();
		ssFilter.addAction(Intent.ACTION_EDIT);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(swtichSourceBroadcastReceiver, ssFilter);			
		
        mStartSettingHT = new HandlerThread("startSetting");
        mStartSettingHT.start();
        mStartHandler = new StartHandler(mStartSettingHT.getLooper());
        mStartHandler.sendEmptyMessage(0);
		
		//只是向framework层发送一下广播，同步一下panelLock的状态。
		TvPluginControler.getInstance().getParentalControlManager().setPanalLock(
				TvPluginControler.getInstance().getParentalControlManager().isPanalLock(),this);
		
		((TvApplication)getApplication()).addActivity(this);
	}
	
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if("com.android.sky.SendHotKey".equals(action))
			{
				int keyCode = intent.getIntExtra("specialKey",-1);
				Log.i("gky", "specialKey key is " + keyCode);
				switch (keyCode) {
					case KeyEvent.KEYCODE_POWER:
					{
						powerOffResume = true;
						if(CountDownDialog != null)
						{
							if(CountDownDialog.isShowing())
							{
								CountDownDialog.processCmd(TvConfigTypes.TV_DIALOG_COUNT_DOWN, DialogCmd.DIALOG_DISMISS, null);
							}
						}
					}
					break;
					default:
						break;
				}
			}
			else if(action.equals(TvConfigTypes.ACTION_ALLKEY_EVENT))
			{
				noSignalSleepTimer.removeCallbacks(noSignalSleepRunnable);
				noSignalStatus = false;
			}
			else if(action.equals("removealldialogs"))
			{
				if (TvUIControler.getInstance().isDialogShowing()) {
					TvUIControler.getInstance().reMoveAllDialogs();
				}
			}
			else if(action.equals("switchtodtvsource"))
			{				
				List<RunningTaskInfo> tasks = ((ActivityManager) TvContext.context
		                .getSystemService(TvContext.context.ACTIVITY_SERVICE)).getRunningTasks(1);
		        if (tasks.size() > 0)
		        {
		            ComponentName cn = tasks.get(0).topActivity;
		            String PackageName = cn.getPackageName();
		            Log.e(TAG, "lijinjin PackageName: " + PackageName);
	            	ActivityManager mAm;

					mAm = (ActivityManager) TvContext.context.getSystemService(TvContext.context.ACTIVITY_SERVICE); 

		            if(PackageName.contains("netrange"))
		            {
		            	Log.e(TAG, "lijinjin kill PackageName: " + PackageName);
						mAm.forceStopPackage("com.netrange.license");
						mAm.forceStopPackage("com.netrange.launcher");
						
						try 
						{
							Thread.sleep(1000);
						} 
						catch (InterruptedException e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						mAm.forceStopPackage("com.tianci.samplehome");
		            }
		            
		            if(PackageName.contains("samplehome"))
		            {
		            	
						mAm.forceStopPackage("com.tianci.samplehome");
		            }
		        }


				int dtveventRoute = intent.getIntExtra("dtvroute",-1);
				//int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
				Log.i(TAG,"lijinjin dtveventRoute = " + dtveventRoute);
				if(dtveventRoute == 2)
				{
					try
					{
						TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV2.ordinal());
					} 
					catch (RemoteException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
				}
				else if(dtveventRoute == 0)
				{
					try
					{
						TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV.ordinal());
					} 
					catch (RemoteException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					Log.i("chenclog","DVB-C");
					try
					{
						TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_KTV.ordinal());
					} 
					catch (RemoteException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			else if(action.equals("startpvr"))
			{
				DiskSelectInfoData diskSelectInfoDatas = TvPluginControler.getInstance().getPvrManager().updateDiskSelectInfo();
				if(diskSelectInfoDatas.usbDriverCount<1)
				{
					//提示没有U盘
				}
				else
				{
					String flag = "record";
					TvUIControler.getInstance().reMoveAllDialogs();
					PvrTimeshitfDlg dlg = new PvrTimeshitfDlg();
					int iLoop = 0;
					String usbLabel = "NTFS";
					for(iLoop = 0; iLoop < diskSelectInfoDatas.usbDriverCount; iLoop++)
					{
						usbLabel = diskSelectInfoDatas.usbDriverLabel.get(iLoop);
						if(usbLabel.contains("FAT"))
						{
							dlg.selectPath = diskSelectInfoDatas.usbDriverPath.get(iLoop);
							dlg.selectLabel = diskSelectInfoDatas.usbDriverLabel.get(iLoop);
							
							Log.e(TAG, "BroadcastReceiver() dlg.selectPath: " + dlg.selectPath);
							
							dlg.processCmd(TvConfigTypes.TV_DIALOG_PVR_TIMESHITH_DLG, DialogCmd.DIALOG_SHOW, flag);
							
							break;
						}
					}
					
					if(!usbLabel.contains("FAT"))
					{
						//提示格式化U盘
					}
					
					
					
				}
			}
		}
	};

	public void startTvService(){
		Intent intent=new Intent();
		intent.setAction("com.external.tvservice.connect");
		startService(intent);
	}
	
	
	public void startInstallGuiActivity()
	{
		Intent startGuide = new Intent();
		startGuide.setClass(this, InstallGuideActivity.class);
		startActivity(startGuide);
	}
	
	public void initView(){
		noSignalTip = (TextView) findViewById(R.id.noSignal_tip);
		noSignalTip.setVisibility(View.GONE);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("gky", "############TvActivity::onResume#################");
		checkCurSource();
		if(mAttcHT == null){
			mAttcHT = new HandlerThread("attachHandler");
		}
		
		if(!mAttcHT.isAlive())
		{
			mAttcHT.start();
		}
		
		if(mAttcHandler == null)
		{
			mAttcHandler = new AttcHandler(mAttcHT.getLooper());
		}
		
		mAttcHandler.sendEmptyMessage(0);
		
		boolean isSignalState = TvPluginControler.getInstance().getCommonManager().isSignalState();
		startAniTimer();
		if(!isSignalState){
			Message message = new Message();
			message.what = EnumDeskEvent.EV_SCREEN_SAVER_MODE.ordinal();
			Bundle bundle = new Bundle();
			bundle.putInt("Status", TvConfigTypes.TV_STATUS_NO_SIGNAL);
			bundle.putString("curSource", mCurSource.toString());
			Log.v(TAG, "OnResume ======= powerOffResume==" + powerOffResume);
			if (powerOffResume) {
				powerOffResume = false;
				bundle.putString("STRPowerResume", "true");
			}
			message.setData(bundle);
			uiHandler.sendMessage(message);
		}
		TvPluginControler.getInstance().getCilManager().initListener(ciUIEventListener);
		TvPluginControler.getInstance().getCilManager().registerCiOpRefreshQueryListener(ciUIOpRefreshQueryListener);
		TvPluginControler.getInstance().getCilManager().registerCiStatusChangeEventListener(ciStatusChangeEventListener);
		TvPluginControler.getInstance().getCilManager().registerCiCmdNotifyListener(ciCmdNotifyListener);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Log.i("gky", "###########TvActivity::onNewIntent###############");
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		ishasFocus=hasFocus;
		Log.i("gky", "TvActivity::onWindowFocusChanged::hasFocus is " + hasFocus);
		if(hasFocus){
			TvPluginControler.getInstance().getCilManager().initListener(ciUIEventListener);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("gky", "#############TvActivity::onPause###################");
		savePreSource();
		cleanAniTimer();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		TvPluginControler.getInstance().getCommonManager().releaseHandler();
		TvPluginControler.getInstance().getCilManager().releaseListener();
		TvPluginControler.getInstance().getCilManager().unregisterCiOpRefreshQueryListener();
		TvPluginControler.getInstance().getCilManager().unregisterCiStatusChangeEventListener();
		TvPluginControler.getInstance().getCilManager().unregisterCiCmdNotifyListener();
		Log.i("gky", "###########TvActivity::onStop#######################");
		Log.i("gky", getClass().getSimpleName() + "-------------onStop isPowering is "+TvService.isPowering());
		if(!TvPluginControler.getInstance().getCommonManager().getInstallGuideFlag())
		{
			if(TvService.isPowering())
				TvService.setPowering(false);
			else 
				TvPluginControler.getInstance().getCommonManager().releaseTvSource();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((TvApplication)getApplication()).removeActivity(this);
		// 取消source switch广播的注册
		unregisterReceiver(swtichSourceBroadcastReceiver);
		TvPluginControler.getInstance().getCommonManager().unregisterCecCtrlEventListener();
		Log.i("gky", "###############TvActivity::onDestroy####################");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvActivity::onKeyDown: keyCode is " + keyCode);
		boolean mhlSendFlag = false;
		boolean cecSendFlag = false;
		int keyCode_temp = keyCode;
		int keyCode_Mheg5 = keyCode;
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
		{
			keyCode_temp = KeyEvent.KEYCODE_ENTER;
			keyCode_Mheg5 = KeyEvent.KEYCODE_ENTER;
		}							
		
		if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
		{
			keyCode_temp = KeyEvent.KEYCODE_MEDIA_PLAY;
			//keyCode_Mheg5 = KeyEvent.KEYCODE_MEDIA_PLAY;
		}
		
		if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
		{
			if (keyPlayOrPause == KeyEvent.KEYCODE_MEDIA_PLAY)
			{
				keyCode_temp = KeyEvent.KEYCODE_MEDIA_PAUSE;
				keyPlayOrPause = KeyEvent.KEYCODE_MEDIA_PAUSE;
				//keyCode_Mheg5 = KeyEvent.KEYCODE_MEDIA_PAUSE;
			} 
			else if (keyPlayOrPause == KeyEvent.KEYCODE_MEDIA_PAUSE)
			{
				keyCode_temp = KeyEvent.KEYCODE_MEDIA_PLAY;
				keyPlayOrPause = KeyEvent.KEYCODE_MEDIA_PLAY;
				//keyCode_Mheg5 = KeyEvent.KEYCODE_MEDIA_PLAY;

			}
		}
		
		if(keyCode == KeyEvent.KEYCODE_ENTER_EPG)
		{
			keyCode_Mheg5 = KeyEvent.KEYCODE_GUIDE;
		}
		if(keyCode == KeyEvent.KEYCODE_MEDIA_BOOKING)
		{
			keyCode_Mheg5 = 262;//KEYCODE_TTX
		}
		
		mhlSendFlag = TvPluginControler.getInstance().getCommonManager().sendMhlKey(keyCode_temp);
		if(mhlSendFlag)
		{
			return true;
		}
		
		//增加cec部分的按键响应
		cecSendFlag = TvPluginControler.getInstance().getCommonManager().sendCecKey(keyCode_temp);
		Log.v("yangcheng", "cecSendFlag = " + cecSendFlag);
		if (cecSendFlag)
		{
			return true;
		}
		Log.d("wangxian", "keycode = " + keyCode);
		if(TvPluginControler.getInstance().getCommonManager().sendMheg5Key(keyCode_Mheg5))
		{
			return true;
		}
		Log.d("wangxian", "1111111 ");
		if (event.getAction() == KeyEvent.ACTION_UP) {
			Log.d("wangxian", "222222 ");
			return false;
		}
		switch (keyCode) {
			case KeyEvent.KEYCODE_MENU:
			case KeyEvent.KEYCODE_CHANNEL_UP:
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_CHANNEL_DOWN:
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_MEDIA_BOOKING://TEXT
			case KeyEvent.KEYCODE_SUBTITLE_SETTING://SUBTITLE
			case KeyEvent.KEYCODE_PROG_RED://REC.
			case KeyEvent.KEYCODE_PROG_BLUE://BLUE
			case KeyEvent.KEYCODE_PROG_YELLOW://YELLOW
			case KeyEvent.KEYCODE_PROG_GREEN://GREEN
			case KeyEvent.KEYCODE_INFO://INFO
			case KeyEvent.KEYCODE_MEDIA_DELETE://RADIO
			case KeyEvent.KEYCODE_IMAGE_MODE://A.D
			case KeyEvent.KEYCODE_AUDIO_SETTING://Nicam/Audio
			case KeyEvent.KEYCODE_ALTERNATE://RETURN
			case KeyEvent.KEYCODE_ENTER_EPG://EPG
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_MEDIA_FUNCTION://INDEX
			case KeyEvent.KEYCODE_MEDIA_FAVORITES://FAV
			case KeyEvent.KEYCODE_0:
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_9:
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE://T.shift
			//factory hotkeys
			case KeyEvent.KEYCODE_FACTORY_FACTORY_MODE:
			case KeyEvent.KEYCODE_FACTORY_WHITE_BALANCE:
			case KeyEvent.KEYCODE_FACTORY_AUTO_ADC:
			case KeyEvent.KEYCODE_FACTORY_BUS_OFF:
			case KeyEvent.KEYCODE_FACTORY_RESET:
			case KeyEvent.KEYCODE_FACTORY_AGING_MODE:
			case KeyEvent.KEYCODE_FACTORY_OUTSET:
			case KeyEvent.KEYCODE_FACTORY_BARCODE:
//			case KeyEvent.KEYCODE_FACTORY_HDMI1:
			case KeyEvent.KEYCODE_FACTORY_VGA:
			case KeyEvent.KEYCODE_COOCAA:
			case KeyEvent.KEYCODE_VOICE_END:
			case KeyEvent.KEYCODE_FACTORY_UPLAYER:
			case KeyEvent.KEYCODE_FACTORY_CA_CARD:
			case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
			case KeyEvent.KEYCODE_MEDIA_REWIND:
			case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
			case KeyEvent.KEYCODE_MEDIA_NEXT:
			//factory hotkeys
			{
				if(keyCode == KeyEvent.KEYCODE_FACTORY_AGING_MODE && TvPluginControler.getInstance().getCommonManager().getFactoryHotkeyEnable() == 1)
				{
					if(!TvPluginControler.getInstance().getCommonManager().isAgingMode())
					{
						agingFlag = true;
						noSignalSleepTimer.removeCallbacks(noSignalSleepRunnable);
					}else
					{
						agingFlag = false;
					}
				}
				return mQuickKeyListener.onQuickKeyDownListener(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void ShowTvCountDownDialog(Handler handler, Runnable runnable)
	{
		final Handler mHandler = handler;
		final Runnable mRunnable = runnable;
		 CountDownDialog = new TvCountDownDialog(new TvCountFunc()
		{
			@Override
			public void CountFinishFunction() 
			{
				// TODO Auto-generated method stub
				Log.v(TAG,"TvActivity ,CountFinishFunction");
//				TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
				if(mCurSource == TvEnumInputSource.E_INPUT_SOURCE_VGA)
				{
					Log.v("zhm","set VGA standby --->>");
					SystemProperties.set("mstar.vga.standby", "1");
				}
				Log.v(TAG, "OnResume ======= ShowTvCountDownDialog==" + powerOffResume);
				powerOffResume = true;
				TvPluginControler.getInstance().getCommonManager().standbySystem("standby");
				
			}
		});
		 
		CountDownDialog.setDialogListener(new DialogListener() 
		{
			@Override
			public boolean onShowDialogDone(int ID) 
			{
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public int onDismissDialogDone(Object... obj)
			{
				// TODO Auto-generated method stub
				int ret = (Integer) obj[0];
				if(ret != 0)
				{
					Log.v(TAG, "CountDownDialog========");
					mHandler.removeCallbacks(mRunnable);
					if(TvPluginControler.getInstance().getCommonManager().getCurrentInputSource() != TvEnumInputSource.E_INPUT_SOURCE_VGA)
					{
						mHandler.postDelayed(mRunnable, noSignalSleepLeft);
					}
					else
					{
						mHandler.postDelayed(mRunnable, noSignalSleepVGALeft);
					}
				}
				return 0;
			}
		});
		
		CountDownDialog.processCmd(TvConfigTypes.TV_DIALOG_COUNT_DOWN, DialogCmd.DIALOG_SHOW, null);
	}
		
	Runnable noSignalSleepRunnable = new Runnable()
	{
		@Override
		public void run()
		{
		    // TODO Auto-generated method stub
			boolean isAutoSearch = TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH)
					|| TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH);
			agingFlag = TvPluginControler.getInstance().getCommonManager().isAgingMode();
			Log.v(TAG,"screenModeEventListener noSignalSleepRunnable =====isAutoSearch = "+ isAutoSearch + "===agingFlag = " + agingFlag);
			if((!isAutoSearch) || (!agingFlag))
			{
				ShowTvCountDownDialog(noSignalSleepTimer,this);
			}
		}
	};
	
	public void createSurfaceView(){
		mSurfaceView = (SurfaceView)findViewById(R.id.surfaceview);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);	
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvActivity::surfaceCreated:holder is " + holder.getSurface().isValid() + " " +holder.equals(mSurfaceView));
		if(!holder.getSurface().isValid())
			return;
		Log.i("gky", "TvActivity::surfaceCreated:mSurfaceHolder is " + mSurfaceHolder.getClass().getName()
				+"\n ----------------and checkCurSource-------");
		checkCurSource();
		TvPluginControler.getInstance().getCommonManager().setDisplay(mSurfaceHolder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i("gky", getClass().getSimpleName() + "#########surfaceDestroyed########");
		Release();
	}

	/**
	 * 释放资源
	 */
	public void Release(){
		Log.i("gky", getClass().getSimpleName() + "#########Release do nothing########");
	}
	
	/**
	 * 监听底层信号状态
	 */
	public TvScreenModeEventListener mTvScreenModeEventListener = new TvScreenModeEventListener() 
	{
		@Override
		public synchronized void onTvScreenModeEventListener(Message message) 
		{
			// TODO Auto-generated method stub
			/**
			 * Status is 0 for MSRV_DTV_SS_INVALID_SERVICE 1 for
			 * MSRV_DTV_SS_NO_CI_MODULE 2 for MSRV_DTV_SS_NO_CI_MODULE 3 for
			 * MSRV_DTV_SS_SCRAMBLED_PROGRAM 4 for MSRV_DTV_SS_CH_BLOCK 5 for
			 * MSRV_DTV_SS_PARENTAL_BLOCK 6 for MSRV_DTV_SS_AUDIO_ONLY 7 for
			 * MSRV_DTV_SS_DATA_ONLY 8 for MSRV_DTV_SS_COMMON_VIDEO 9 for
			 * MSRV_DTV_SS_UNSUPPORTED_FORMAT 99 for NO_SIGNAL
			 * 
			 * message is 5 for EV_SIGNAL_LOCK 6 for EV_SIGNAL_UNLOCK 8 for
			 * EV_SCREEN_SAVER_MODE
			 */
			mSignalMode = message.what;
			mSignalStatus = message.getData().getInt("Status", -1);
            String curSourceString = message.getData().getString("curSource");
			TvEnumInputSource curSource = TvEnumInputSource.valueOf(curSourceString);
			
			if(false)
				Log.i("gky", "onTvScreenModeEventListener::message is "
					+ mSignalMode + " Status is " + mSignalStatus + " curSource is " + curSource.toString());			
			boolean isAutoSearch = TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH)
					|| TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_ATV_MANUAL_SEARCH)
					|| TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH)
					|| TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_DTV_MANUAL_SEARCH)
					|| TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_DVBS_MANUAL_SEARCH);
			
            if(isAutoSearch)
            {
			   Log.v(TAG,"onTvScreenModeEventListener return!");
			   noSignalTip.setVisibility(View.GONE);
			   return;
            }
			if (mSignalStatus == TvConfigTypes.TV_STATUS_NO_SIGNAL)
			{//无信号
				Log.v(TAG,"screenModeEventListener no signal");
				String powerStatus = message.getData().getString("STRPowerResume", "");
				Log.v(TAG,"screenModeEventListener powerStatus = " + powerStatus);
				if(powerStatus.equals("true") && !agingFlag)
				{
					noSignalStatus = true;
					noSignalSleepTimer.removeCallbacks(noSignalSleepRunnable);
					if(curSource != TvEnumInputSource.E_INPUT_SOURCE_VGA)
					{
						noSignalSleepTimer.postDelayed(noSignalSleepRunnable,noSignalSleepLeft);
					}
					else
					{
						Log.v(TAG,"screenModeEventListener PC post delayed");
						noSignalSleepTimer.postDelayed(noSignalSleepRunnable,noSignalSleepVGALeft);
					}
				}
				
				if(agingFlag)
				{
					noSignalTip.setVisibility(View.GONE);
					noSignalSleepTimer.removeCallbacks(noSignalSleepRunnable);
				}
				
				if(curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV)
				{
					if((noSignalTip.getVisibility() != View.VISIBLE) || (!noSignalTip.getText().equals("No Signal")))
					{
						countNoSignal++;
					}
					
					if((countNoSignal > 4) && (!agingFlag)  && (!isAutoSearch))
					{//新版软件没有出现切台过程中无信号的情况,因此减小计数基数
						if(isAutoSearch || TvUIControler.getInstance().isDialogShowing())
						{
							noSignalTip.setVisibility(View.GONE);
						}
						else
						{
							noSignalTip.setVisibility(View.VISIBLE);
						}

						if((!noSignalStatus) && (!isAutoSearch))
						{
							noSignalStatus = true;
							noSignalSleepTimer.removeCallbacks(noSignalSleepRunnable);
							if(curSource!= TvEnumInputSource.E_INPUT_SOURCE_VGA)
							{
								noSignalSleepTimer.postDelayed(noSignalSleepRunnable,noSignalSleepLeft);
							}
							else
							{
								noSignalSleepTimer.postDelayed(noSignalSleepRunnable,noSignalSleepVGALeft);
							}
						}
						noSignalTip.setText(getResources().getString(R.string.str_no_signal));
						countNoSignal = 0;
					}
				}
				else 
				{
					if(isAutoSearch || TvUIControler.getInstance().isDialogShowing())
					{
						noSignalTip.setVisibility(View.GONE);
					}
					else
					{
						noSignalTip.setVisibility(View.VISIBLE);
					}
					Log.i(TAG,"noSignalStatus == " + noSignalStatus + "=====isAutoSearch===" + isAutoSearch);
					if((!noSignalStatus) && (!isAutoSearch))
					{
						noSignalStatus = true;
						noSignalSleepTimer.removeCallbacks(noSignalSleepRunnable);
						if(curSource != TvEnumInputSource.E_INPUT_SOURCE_VGA)
						{
							noSignalSleepTimer.postDelayed(noSignalSleepRunnable,noSignalSleepLeft);
						}
						else
						{
							Log.v(TAG,"screenModeEventListener vga = =====");
							noSignalSleepTimer.postDelayed(noSignalSleepRunnable,noSignalSleepVGALeft);
						}
					}
					
					noSignalTip.setText(getResources().getString(R.string.str_no_signal));
					countNoSignal = 0;				
				}	

			} //end 无信号
			else 
			{//有信号
				Log.v(TAG,"screenModeEventListener has signal ");
				if(CountDownDialog != null)
				{
					if(CountDownDialog.isShowing())
					{
						Log.v(TAG,"screenModeEventListener has signal countdowndialog showing");
						CountDownDialog.processCmd(TvConfigTypes.TV_DIALOG_COUNT_DOWN, DialogCmd.DIALOG_DISMISS, null);
					}
				}
				
				if(noSignalStatus)
				{
					noSignalStatus = false;
				}
				
				noSignalSleepTimer.removeCallbacks(noSignalSleepRunnable);
				
				if (curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV)
				{
					if(mSignalMode == EnumDeskEvent.EV_SIGNAL_LOCK.ordinal())
					{						
						if(noSignalTip.getVisibility() != View.VISIBLE)
						{
							countNoSignal++;
						}
						
						if(countNoSignal > 4)
						{
							noSignalTip.setVisibility(View.VISIBLE);
							noSignalTip.setText(getResources().getString(R.string.str_program_lock));
							countNoSignal = 0;
							return;
						}
					}
				}
		
				if (curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV) 
				{
					switch (mSignalStatus)
					{
						case TvConfigTypes.DTV_SS_INVALID_SERVICE:
							countNoSignal++;
							Log.i("gky", getClass()+" ####################IVALID SERVICE---------------> "+countNoSignal);
							if(countNoSignal > 3)
							{
								Log.i("gky", getClass()+" ####################IVALID SERVICE####################");
								countNoSignal = 0;
								noSignalTip.setVisibility(View.VISIBLE);
								noSignalTip.setText(getResources().getString(R.string.str_invalid_service));
							}
							
							return;//直接返回,避免countNoSignal清零
							
						case TvConfigTypes.DTV_SS_NO_CI_MODULE:
							noSignalTip.setVisibility(View.VISIBLE);
							noSignalTip.setText(getResources().getString(R.string.str_no_ci_module));
							break;
							
						case TvConfigTypes.DTV_SS_CI_PLUS_AUTHENTICATION:
							noSignalTip.setVisibility(View.VISIBLE);
							noSignalTip.setText(getResources().getString(R.string.str_ci_plus_authentication));
							break;
							
						case TvConfigTypes.DTV_SS_SCRAMBLED_PROGRAM:
							noSignalTip.setVisibility(View.VISIBLE);
							noSignalTip.setText(getResources().getString(R.string.str_scrambled_program));
							break;
							
						case TvConfigTypes.DTV_SS_AUDIO_ONLY:
							noSignalTip.setVisibility(View.VISIBLE);
							noSignalTip.setText(getResources().getString(R.string.str_audio_only));
							break;
							
						case TvConfigTypes.DTV_SS_DATA_ONLY:
							noSignalTip.setVisibility(View.VISIBLE);
							noSignalTip.setText(getResources().getString(R.string.str_data_only));
							break;
							
						case TvConfigTypes.DTV_SS_UNSUPPORTED_FORMAT:
							noSignalTip.setVisibility(View.VISIBLE);
							noSignalTip.setText(getResources().getString(R.string.str_unsupported_format));
							break;
							
							//当前节目被加上节目锁和等级锁的时候
						case TvConfigTypes.DTV_SS_CH_BLOCK:
						case TvConfigTypes.DTV_SS_PARENTAL_BLOCK:
							if (!TvUIControler.getInstance().isDialogShowing()
									&& TvActivity.instance.isTopActivity()
									&& TvActivity.instance.ishasFocus
									&& !PasswordUnLockChannelDialog.getInstance()
											.getbeShowed()) {
								if (View.VISIBLE == noSignalTip.getVisibility()) {
									noSignalTip.setVisibility(View.GONE);
								}
								PasswordUnLockChannelDialog.getInstance()
										.setbeShowed(true);
								TvUIControler.getInstance().reMoveAllDialogs();
								PasswordUnLockChannelDialog pass = PasswordUnLockChannelDialog
										.getInstance();
								pass.setDialogListener(null);
								pass.processCmd(
										TvConfigTypes.TV_DIALOG_INPUT_PASSWORD,
										DialogCmd.DIALOG_SHOW, null);
							}
							break;
						case TvConfigTypes.DTV_SS_COMMON_VIDEO:
						default:
							noSignalTip.setVisibility(View.GONE);
							break;
					}
					
					countNoSignal = 0;//NoSignal和ProgramLock,invalid service需要延迟判断，避免切台和且通道时出现出现
				}
				else if(curSource == TvEnumInputSource.E_INPUT_SOURCE_CVBS
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_CVBS2
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_CVBS3
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_CVBS4)
				{
					countNoSignal = 0;//add
					if(View.VISIBLE == noSignalTip.getVisibility())
					{
						noSignalTip.setVisibility(View.GONE);
					}
				}
				else if(curSource == TvEnumInputSource.E_INPUT_SOURCE_YPBPR
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_YPBPR2
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_YPBPR3)
				{
					if(View.VISIBLE == noSignalTip.getVisibility())
					{
						noSignalTip.setVisibility(View.GONE);
					}
				}
				else if(curSource == TvEnumInputSource.E_INPUT_SOURCE_VGA)
				{
					if(mSignalStatus == EnumSignalProgSyncStatus.E_SIGNALPROC_STABLE_UN_SUPPORT_MODE.ordinal()
							|| mSignalStatus == EnumSignalProgSyncStatus.E_SIGNALPROC_UNSTABLE.ordinal())
					{
						noSignalTip.setVisibility(View.VISIBLE);
						noSignalTip.setText(getResources().getString(R.string.str_unsupported_format));
						AtvSourceInfoView.VGANoSupportFlag = 1;
						Toast.makeText(instance, "Not Support", Toast.LENGTH_LONG).show();
					}
					else if(mSignalStatus == EnumSignalProgSyncStatus.E_SIGNALPROC_AUTO_ADJUST.ordinal())
					{
						AtvSourceInfoView.VGANoSupportFlag = 0;
						final TvLoadingDialog tvLoadingDialog = new TvLoadingDialog();
						TvLoadingData adjustData = new TvLoadingData("", TvContext.context.getResources().getString(R.string.str_auto_adjust));
						tvLoadingDialog.processCmd(null, DialogCmd.DIALOG_SHOW,
								adjustData);
						
						uiHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								tvLoadingDialog.processCmd(null,DialogCmd.DIALOG_DISMISS, null);
							}
						}, 3000);
						
					}
					else if(View.VISIBLE == noSignalTip.getVisibility())
					{
						noSignalTip.setVisibility(View.GONE);
					}
					else
					{
						AtvSourceInfoView.VGANoSupportFlag = 0;
					}
				}
				else if(curSource == TvEnumInputSource.E_INPUT_SOURCE_HDMI
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_HDMI2
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_HDMI3
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_HDMI4)
				{
					if(mSignalStatus == EnumSignalProgSyncStatus.E_SIGNALPROC_STABLE_UN_SUPPORT_MODE.ordinal()
							|| mSignalStatus == EnumSignalProgSyncStatus.E_SIGNALPROC_UNSTABLE.ordinal())
					{
						noSignalTip.setVisibility(View.VISIBLE);
						noSignalTip.setText(getResources().getString(R.string.str_unsupported_format));
					}
					else
					{
						noSignalTip.setVisibility(View.GONE);
					}
				}
			}// end 有信号
		}
	};
	
	public boolean isTopActivity()
    {
        boolean isTop = false;
        ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		if (cn.getClassName().equals("com.tv.app.TvActivity")) {
			isTop = true;
		}
        return isTop;
    }
	
	public void initNoSignalPlay()
    {
		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = (int) (Math.random() * ((width - NOSIGNAL_WIDTH) / resolutionDiv));
		layoutParams.topMargin = (int) (Math.random() * ((height - NOSIGNAL_HEIGHT) / resolutionDiv));
		noSignalTip.setLayoutParams(layoutParams);
    }
	
	public void startAniTimer()
    {
        cleanAniTimer();
        aniTimer = new ScheduledThreadPoolExecutor(1);
        aniTimer.scheduleAtFixedRate(new Runnable()
        {
            private int count = 0;

            @Override
            public void run()
            {
                count++;
                if (count >= 3)
                {
		            mHandler.sendEmptyMessage(0);
                    count = 0;
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void cleanAniTimer()
    {
        if (aniTimer != null)
        {
        	Log.i("gky", "cleanAniTimer");
            aniTimer.shutdownNow();
            aniTimer = null;
        }
    }
    
    private class NoSingalHandler extends Handler{

		public NoSingalHandler(Looper looper) {
			// TODO Auto-generated constructor stub
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			 // TODO Auto-generated method stub
			mCurSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
        	boolean bSignalFlag = false;
			if(!TvPluginControler.getInstance().getCommonManager().isSignalState()){
        		Log.i(TAG, "handleMessage============set noSignalStatus false");
//        		noSignalStatus = false;
        		bSignalFlag = false;
    			agingFlag = TvPluginControler.getInstance().getCommonManager().isAgingMode();
        	}
        	else {
//        		noSignalStatus = true;
        		bSignalFlag = true;
			}
        	NetworkInfo activeInfo = netManager.getActiveNetworkInfo();
        	if((mCurSource != TvEnumInputSource.E_INPUT_SOURCE_DTV || !bSignalFlag)//!noSignalStatus)
					&& (activeInfo != null && activeInfo.isConnected())){
				if(!isAlreadySyncNTP){
					Log.i("gky","isAlreadySyncNTP");
					Settings.Global.putInt(TvContext.context.getContentResolver(),
							Settings.Global.TV_AUTO_TIME, 0);
					Settings.Global.putInt(TvContext.context.getContentResolver(),
							Settings.Global.AUTO_TIME, 1);
					TvPluginControler.getInstance().getCommonManager().setLinuxTimeSource(true);
					isAlreadySyncNTP = true;
				}
				isAlreadySyncDTV = false;
			}
			else {
				if(!isAlreadySyncDTV){
					Log.i("gky","isAlreadySyncDTV");
					Settings.Global.putInt(TvContext.context.getContentResolver(),
							Settings.Global.TV_AUTO_TIME, 1);
					Settings.Global.putInt(TvContext.context.getContentResolver(),
							Settings.Global.AUTO_TIME, 0);
					TvPluginControler.getInstance().getCommonManager().setLinuxTimeSource(false);
					isAlreadySyncDTV = true;
				}
				isAlreadySyncNTP = false;
			}
        	if(!bSignalFlag)//!noSignalStatus)
	        {
		        Message message = uiHandler.obtainMessage();
				message.what = EnumDeskEvent.EV_SCREEN_SAVER_MODE.ordinal();
				Bundle bundle = new Bundle();
	    		bundle.putString("curSource", mCurSource.toString());
	    		bundle.putInt("Status", TvConfigTypes.TV_STATUS_NO_SIGNAL);
	    		message.setData(bundle);
	    		uiHandler.sendMessage(message);
	        }
        	noSignalTip.post(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					initNoSignalPlay();
				}
        	});
		}
    	
    };
    
    private class AttcHandler extends Handler{

		public AttcHandler(Looper looper) {
			// TODO Auto-generated constructor stub
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			 // TODO Auto-generated method stub
			Log.i("gky", getClass().getSimpleName() + " "+Thread.currentThread().getName());
			TvPluginControler.getInstance().getCommonManager().attachHandler(uiHandler);
			mCurSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
			agingFlag = TvPluginControler.getInstance().getCommonManager().isAgingMode();
		}
    	
    }
	private Handler uiHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
        	Log.v(TAG,"uiHandler snedScreenMode message....");
        	mTvScreenModeEventListener.onTvScreenModeEventListener(msg);
        }
    };
    
	private class  StartHandler extends Handler
    {
        public StartHandler(Looper looper) {
			// TODO Auto-generated constructor stub
        	super(looper);
		}

		@Override
        public void handleMessage(Message msg)
        {
            // TODO Auto-generated method stub
        	Log.i("gky", Thread.currentThread().getName() + "StartHandler....start...............");
        	if(TvPluginControler.getInstance().getHotelManager().getHotelMode())
    		{
    			int fixVolume = TvPluginControler.getInstance().getHotelManager().getHotelFixVolume();
    			mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, fixVolume, 0);
    			
    			boolean lockKey = TvPluginControler.getInstance().getHotelManager().getHotelLockKey();
    			if(lockKey)
    			{
    				broadcastHotelLocalKey(1);
    			}
    			else
    			{
    				broadcastHotelLocalKey(0);
    			}
    		}

    		TvPluginControler.getInstance().getCommonManager().initSyncTimeAndCountry();
    		boolean agFlag = TvPluginControler.getInstance().getCommonManager().isAgingMode();
    		agingFlag = agFlag;
    		
    		if(agFlag)
    		{
    			Intent it = new Intent(TvActivity.instance, FactoryHotKeyActivity.class);
    			it.putExtra(FactoryHotKeyActivity.OPKEY, FactoryHotKeyActivity.M_MODE);
    			TvActivity.instance.startActivity(it);
    		}
    		
    		if(TvPluginControler.getInstance().getCommonManager().getWatchDogMode() == 1)
    		{
    			TvPluginControler.getInstance().getCommonManager().setWatchDogMode((short)1);
    		}
        }
    
     }
	
	public CiUIEventListener ciUIEventListener = new CiUIEventListener() {
		
		@Override
		public void onCiUIEventListener(CiUIEventData data) {
			// TODO Auto-generated method stub
			TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
			if(curSource == TvEnumInputSource.E_INPUT_SOURCE_STORAGE)
				return;
			
			Log.i("gky", getClass().getSimpleName() + "--------->onCiUIEventListener is data "+data+" ciCarStatus is "+data.ciCardStatus);
			if(data.ciCardStatus == TvConfigTypes.TVCI_UI_DATA_READY){
				boolean isShowing = TvUIControler.getInstance().isDialogShowing(TvConfigTypes.TV_DIALOG_CI_INFORMATION);
				if(!isShowing){
				    TvPluginControler.getInstance().getCilManager().releaseListener();
				    TvUIControler.getInstance().reMoveAllDialogs();
					CIInformationDialog ciInformationDialog = new CIInformationDialog();
					ciInformationDialog.setDialogListener(null);
					ciInformationDialog.processCmd(null, DialogCmd.DIALOG_SHOW, true);
				}
			}
			else if(data.ciCardStatus == TvConfigTypes.TVCI_UI_CARD_INSERTED)
			{
				TvUIControler.getInstance().showMniToast(TvContext.context.getResources().getString(R.string.str_ci_insert));
			}
			else if(data.ciCardStatus == TvConfigTypes.TVCI_UI_CARD_REMOVED)
			{
				TvUIControler.getInstance().showMniToast(TvContext.context.getResources().getString(R.string.str_ci_remove));
			}
		}
	};
	
	public CiUIOpRefreshQueryListener ciUIOpRefreshQueryListener = new CiUIOpRefreshQueryListener() {
		
		@Override
		public void onCiUIOpRefreshQueryListener(int event) {
			// TODO Auto-generated method stub
			Log.i("gky", getClass().getSimpleName() + "----->onCiUIOpRefreshQueryListener");
			if(event == TvConfigTypes.EVENT_UI_OP_REFRESH_QUERY){
				TvPluginControler.getInstance().getCilManager().ciClearOPSearchSuspended();
				TvToastFocusDialog dialog = new TvToastFocusDialog();
				dialog.setOnBtClickListener(new OnBtClickListener() {
					
					@Override
					public void onClickListener(boolean flag) {
						// TODO Auto-generated method stub
						if(flag){
							Log.i("gky", getClass().getSimpleName() + "----->sendCiOpSearchStart");
							TvPluginControler.getInstance().getCilManager().sendCiOpSearchStart(false);
						}
					}
				});
				TvToastFocusData data = new TvToastFocusData("", getString(R.string.str_tip_ciplus_op_confirmation_title), 
						getString(R.string.str_tip_ciplus_op_refresh), 2);
				dialog.processCmd(null, DialogCmd.DIALOG_SHOW, data);
			}
		}
	};
	
	public CiStatusChangeEventListener ciStatusChangeEventListener = new CiStatusChangeEventListener() {
		
		@Override
		public void onCiStatusChangeEventListener(int what, int arg1, int arg2) {
			// TODO Auto-generated method stub
			Log.i("gky", getClass().getSimpleName() + "----->onCiStatusChangeEventListener "+what+"/"+arg1+"/"+arg2);
			switch (what) {
				case TvConfigTypes.TVCI_STATUS_CHANGE_TUNER_OCCUPIED:
					switch (arg1) {
						case TvConfigTypes.CI_NOTIFY_CU_IS_PROGRESS:
							TvUIControler.getInstance().showMniToast(getString(R.string.str_tip_cam_upgrade_alarm));
							break;
						case TvConfigTypes.CI_NOTIFY_OP_IS_TUNING:
							TvUIControler.getInstance().showMniToast(getString(R.string.str_tip_op_tuning_alarm));
							break;
						default:
							Log.i("gky", getClass().getSimpleName() + "-----> unkonwn status is "+arg1);
							break;
					}
					TvUIControler.getInstance().removeDialog(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH);
					TvUIControler.getInstance().removeDialog(TvConfigTypes.TV_DIALOG_ATV_MANUAL_SEARCH);
					TvUIControler.getInstance().removeDialog(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH);
					TvUIControler.getInstance().removeDialog(TvConfigTypes.TV_DIALOG_DTV_MANUAL_SEARCH);
					break;
				default:
					break;
			}
		}
	};
	
	public CiCmdNotifyListener ciCmdNotifyListener = new CiCmdNotifyListener() {
		
		@Override
		public void onCiCmdNotifyListener(int what, int arg1, int arg2) {
			// TODO Auto-generated method stub
			Log.i("gky", getClass().getSimpleName() + "----->onCiCmdNotifyListener "+what+"/"+arg1+"/"+arg2);
			TvLoadingDialog dialog = new TvLoadingDialog();
			TvLoadingData data = new TvLoadingData(getString(R.string.str_tip_cha_please_wait),
					getString(R.string.str_tip_ci_async_cmd_switch_route));
			switch (what) {
				case TvConfigTypes.TVCI_ASYNC_COMMAND_NOTIFY:
					switch (arg1) {
						case TvConfigTypes.CI_ASYNC_CMD_SWITCH_ROUTE:
							dialog.processCmd(null, DialogCmd.DIALOG_SHOW, data);
							boolean ret = TvPluginControler.getInstance().getCommonManager().processTvAsyncCommand();
							if(!ret)
								Log.i("gky", getClass().getSimpleName() + "----->onCiCmdNotifyListener processTvAsyncCommand false");
							dialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
							break;
						case TvConfigTypes.CI_ASYNC_CMD_SWITCH_CI_SLOT:
							Log.i("gky", getClass().getSimpleName() + "----->onCiCmdNotifyListener CI_ASYNC_CMD_SWITCH_CI_SLOT");
							break;
						default:
							Log.i("gky", getClass().getSimpleName() + "----->onCiCmdNotifyListener unknown status "+arg1);
							break;
					}
					break;
				default:
					break;
			}
		}
	};
	
	BroadcastReceiver swtichSourceBroadcastReceiver = new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context paramContext, Intent paramIntent)
		{			
			int sc = paramIntent.getExtras().getInt("data"); 
			try 
			{
				TvPluginControler.getInstance().getSwitchSource().switchSource(sc);
			} 
			catch (RemoteException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	
	private void savePreSource(){
		SharedPreferences sharedPreferences = TvContext.context.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
		Log.i("gky", getClass().getName() + "---->sacePreSource curSource is "+curSource.toString());
		editor.clear();
		SKY_CFG_TV_SYSTEM_TYPE tvType = TvPluginControler.getInstance().getCommonManager().getTvType();
		if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV
				&& tvType == SKY_CFG_TV_SYSTEM_TYPE.SKY_CFG_TV_SYSTEM_PAL_DVBS_DVBS2){
			int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
			if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS
					|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2)
			{
				editor.putString("PreSource", TvEnumInputSource.E_INPUT_SOURCE_DTV2.toString());
			}else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT
					|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT2)
			{
				editor.putString("PreSource", TvEnumInputSource.E_INPUT_SOURCE_DTV.toString());
			}else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBC) //DVBC
			{
				editor.putString("PreSource", TvEnumInputSource.E_INPUT_SOURCE_KTV.toString());
			}
		}
		else {
			editor.putString("PreSource", curSource.toString());
		}
		editor.commit();
	}
	
	private TvEnumInputSource getPreSource(){
		SharedPreferences sharedPreferences = TvContext.context.getSharedPreferences(getClass().getSimpleName(), Context.MODE_PRIVATE);
		String cur = sharedPreferences.getString("PreSource", "");
		if(!cur.equals("")){
			return TvEnumInputSource.valueOf(cur);
		}
		return TvEnumInputSource.E_INPUT_SOURCE_NONE;
	}
	
	/**
	 *  The local media to stop running because of insufficient memory, 
	 *  you must check the source, otherwise unable to return to the correct source
	 */
	private void checkCurSource(){
		TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
		Log.i("gky", getClass().getName() + "--->checkCurSource  curSource is "+curSource.toString());
		if(curSource == TvEnumInputSource.E_INPUT_SOURCE_STORAGE
				|| curSource == TvEnumInputSource.E_INPUT_SOURCE_STORAGE2){
			TvEnumInputSource preSource = getPreSource();
			Log.i("gky", getClass().getName() + "--->checkCurSource preSource is "+preSource.toString());
			if(preSource != TvEnumInputSource.E_INPUT_SOURCE_NONE){
				TvPluginControler.getInstance().getCommonManager().setInputSource(preSource);
				try
				{
					Thread.sleep(5000);
				} 
				catch (InterruptedException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}

}
