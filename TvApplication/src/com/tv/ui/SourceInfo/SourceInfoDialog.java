package com.tv.ui.SourceInfo;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.framework.data.AtvSourceInfoData;
import com.tv.framework.data.DtvSourceInfoData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog;

public class SourceInfoDialog extends TvBaseDialog{

	private AtvSourceInfoView atvSourceInfoView;
	private DtvSourceInfoView dtvSourceInfoView;
	private DtvSimpleSourceInfoView dtvSimpleSourceInfoView;
	private DialogListener dialogListener;
	private AtvSourceInfoData atvInfoDatas;
	private DtvSourceInfoData dtvInfoDatas;
	private QuickKeyListener quickKeyListener;
	private Boolean isFlag = false;
	private Thread timethread;
	int dismissCount = 10;
	private HandlerThread proUpDownHT = null;
	private ProUpDownHandler proUpDownHandler = null;
	public static final int PRODOWN = 0;
	public static final int PROUP = 1;
	public static final int DTVINFO = 2;
	public static final int ATVINFO = 3;
	public static final int UPDATEINFO = 4;
	
	private TvEnumInputSource inputSource = TvEnumInputSource.E_INPUT_SOURCE_VGA;
	private Timer timer;
	public static boolean isSignal;
	
	public class timeTimerTask extends TimerTask
    {   
        @Override  
    	public void run() 
        {   
    	    // TODO Auto-generated method stub
        	boolean preSignal = isSignal;
        	isSignal = TvPluginControler.getInstance().getCommonManager().isSignalState();
            if(preSignal!=isSignal)
            {
            	Message msgDown = new Message();
				msgDown.what = UPDATEINFO;
				threadMainLoopHandler.sendMessage(msgDown);
            } 
    	}   
    }
	
	private class ProUpDownHandler extends Handler
	{
		public ProUpDownHandler(Looper looper) {
			// TODO Auto-generated constructor stub
			super(looper);
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what == PRODOWN)
			{
				TvPluginControler.getInstance().getChannelManager().programDown();
				Message msgDown = new Message();
				msgDown.what = PRODOWN;
				threadMainLoopHandler.sendMessage(msgDown);
			}
			else if(msg.what == PROUP)
			{
				TvPluginControler.getInstance().getChannelManager().programUp();
				Message msgUp = new Message();
				msgUp.what = PROUP;
				threadMainLoopHandler.sendMessage(msgUp);
			}
			else if(msg.what == DTVINFO)
			{
				dtvInfoDatas = TvPluginControler.getInstance().getChannelManager().updataDtvSourceInfo(true);
				Message msgDTVINFO = new Message();
				msgDTVINFO.what = DTVINFO;
				threadMainLoopHandler.sendMessage(msgDTVINFO);
			}
			else if(msg.what == ATVINFO)
			{
				atvInfoDatas = TvPluginControler.getInstance().getChannelManager().updataAtvSourceInfo();
				Message msgATVINFO = new Message();
				msgATVINFO.what = ATVINFO;
				threadMainLoopHandler.sendMessage(msgATVINFO);
			}
		}
		
	}
	
	Handler threadMainLoopHandler =new Handler(Looper.getMainLooper()){
        public void handleMessage(android.os.Message msg) {
        	if(msg.what == PRODOWN || msg.what == PROUP)
			{				
				processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO,DialogCmd.DIALOG_UPDATE, isFlag);
			}
			else if(msg.what == DTVINFO)
			{
				if(dtvInfoDatas != null  && isFlag != null)
				{
					if(isFlag)
					{
						
						if(dtvSimpleSourceInfoView != null){
							dtvSimpleSourceInfoView.update(dtvInfoDatas);
							}else{
							Log.i("yangcheng","dtvSimpleSourceInfoView == null");
								}
					}
					else 
					{
						if(null != dtvSourceInfoView)
						{
							dtvSourceInfoView.update(dtvInfoDatas);	
						}
					}
				}
			}
			else if(msg.what == ATVINFO)
			{				
				atvSourceInfoView.update(atvInfoDatas);
			}else if(msg.what == UPDATEINFO)
			{				
				processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO,DialogCmd.DIALOG_UPDATE, isFlag);
			}                                           
        }
     };
	
	public SourceInfoDialog(int keyCode) {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_SOURCE_INFO);
		// TODO Auto-generated constructor stub	
		if(proUpDownHT == null){
			proUpDownHT = new HandlerThread("proUpDownHandler");
			Log.d("wangxian", "proUpDownHT ======== " + proUpDownHT);
		}
		
		if(!proUpDownHT.isAlive())
		{
			proUpDownHT.start();
		}
		
		if(proUpDownHandler == null)
		{
			proUpDownHandler = new ProUpDownHandler(proUpDownHT.getLooper());
			Log.d("wangxian", "proUpDownHandler ======= " + proUpDownHandler);
		}
		
		this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		inputSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
		if(inputSource.equals(TvEnumInputSource.E_INPUT_SOURCE_DTV))
		{
			if(keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN || keyCode == KeyEvent.KEYCODE_CHANNEL_UP
					|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
					|| keyCode == KeyEvent.KEYCODE_ALTERNATE)
			{
				dtvSimpleSourceInfoView = new DtvSimpleSourceInfoView(TvContext.context);
				dtvSimpleSourceInfoView.setParentDialog(this);
				setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				setDialogContentView(dtvSimpleSourceInfoView);
			}
			else
			{
				dtvSourceInfoView = new DtvSourceInfoView(TvContext.context);
				dtvSourceInfoView.setParentDialog(this);
				setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				setDialogContentView(dtvSourceInfoView);
			}			
		}
		else 
		{
			atvSourceInfoView = new AtvSourceInfoView(TvContext.context);
			atvSourceInfoView.setParentDialog(this);
			setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
					WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			setDialogContentView(atvSourceInfoView);
		}	
				
		setAutoDismiss(false); 
		
		timer = new Timer();   
        timer.schedule(new timeTimerTask(), 0, 500);
		
        timethread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				while(dismissCount>0)
				{
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dismissCount--;
				}
				Log.i("gky", getClass().getSimpleName() + " timethread "+dismissCount);
                processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO, DialogCmd.DIALOG_DISMISS,true);
			}
		});
		timethread.start();
	}
	
	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return true;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		isFlag = (Boolean)obj;
		switch (cmd) {
		case DIALOG_SHOW:
			if(!this.isShowing())
			{
				super.showUI();
				if(inputSource.equals(TvEnumInputSource.E_INPUT_SOURCE_DTV))
				{
					Log.d("wangxian","current time start----------= " + System.currentTimeMillis());
					dtvInfoDatas = TvPluginControler.getInstance().getChannelManager().updataDtvSourceInfo(true);
					Log.d("wangxian","current time end----------= " + System.currentTimeMillis());
					int i = 0;
					while(dtvInfoDatas == null && i < 5)
					{
						dtvInfoDatas = TvPluginControler.getInstance().getChannelManager().updataDtvSourceInfo(true);
						i++;
					}
					if(dtvInfoDatas != null)
					{
						if(isFlag)
						{
							dtvSimpleSourceInfoView.update(dtvInfoDatas);
						}
						else 
						{
							dtvSourceInfoView.update(dtvInfoDatas);	
						}
					}
				}
				else 
				{
					atvInfoDatas = TvPluginControler.getInstance().getChannelManager().updataAtvSourceInfo();
					atvSourceInfoView.update(atvInfoDatas);
				}
			}							
			break;
		case DIALOG_UPDATE:
			if(inputSource.equals(TvEnumInputSource.E_INPUT_SOURCE_DTV))
			{
				Message msgDown = new Message();
				msgDown.what = DTVINFO;
				if(proUpDownHandler != null)
				{
					proUpDownHandler.sendMessageDelayed(msgDown,3500);
				}
			}
			else if(inputSource.equals(TvEnumInputSource.E_INPUT_SOURCE_ATV))
			{
				Message msgDown = new Message();
				msgDown.what = ATVINFO;
				if(proUpDownHandler != null)
				{
					proUpDownHandler.sendMessageDelayed(msgDown, 3500);
				}
			}
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			if(timer != null)
			{
				timer.cancel();
			}
			break;
		}
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub		
		if(event.getAction() == KeyEvent.ACTION_UP)
			return false;
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU || keyCode == KeyEvent.KEYCODE_INFO)
		{
			processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO,DialogCmd.DIALOG_DISMISS, true);
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN)
		{			
			Message msgDown = new Message();
			msgDown.what = PRODOWN;
			if(proUpDownHandler != null)
			{
				proUpDownHandler.sendMessage(msgDown);
			}
			dismissCount = 10;
			
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_CHANNEL_UP)
		{		
			Message msgUp = new Message();
			msgUp.what = PROUP;
			if(proUpDownHandler != null)
			{
				proUpDownHandler.sendMessage(msgUp);
			}
			dismissCount = 10;
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_0||keyCode == KeyEvent.KEYCODE_1
				||keyCode == KeyEvent.KEYCODE_2||keyCode == KeyEvent.KEYCODE_3
				||keyCode == KeyEvent.KEYCODE_4||keyCode == KeyEvent.KEYCODE_5
				||keyCode == KeyEvent.KEYCODE_6||keyCode == KeyEvent.KEYCODE_7
				||keyCode == KeyEvent.KEYCODE_8||keyCode == KeyEvent.KEYCODE_9)
		{			
			processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO,DialogCmd.DIALOG_UPDATE, isFlag);
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{			
			return true;
		}
		return quickKeyListener.onQuickKeyDownListener(keyCode, event);
	}
}
