package com.tv.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;

import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class StickerDemoUIBroadReceiver extends BroadcastReceiver{
	private static StickerDemoUIBroadReceiver stickerDemo=new StickerDemoUIBroadReceiver();
	private static final String tag = "chenconglog";

	public static boolean stickerdemoup = false;	
	private static Timer mTimer = null;
	private static long delay = 30*1000;
	private static long period = 30*1000;
	private static Object obj = new Object();

	static Handler m_handler = null;
	
	private static final String PACKAGE_NAME_OF_STICKER_DEMO = "com.tv.stickerdemo";
	/*
	 * 单例模式
	 */
	public StickerDemoUIBroadReceiver()
	{
	}
	
	public static StickerDemoUIBroadReceiver getInstance()
	{
		return stickerDemo;
	}
	
	/**
	 * 判断程序是否在前台运行
	 * 
	 * @param packageName
	 * @return
	 */
	public static boolean isOpen(String packageName) {
		if (packageName.equals("") | packageName == null)
			return false;
		ActivityManager am = (ActivityManager) TvContext.context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		for (RunningAppProcessInfo runinfo : runningAppProcesses) {
			String pn = runinfo.processName;
			Log.i(tag,"pn = "+pn+" runinfo.importance = "+runinfo.importance);
			if (pn.equals(packageName)
					&& runinfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
				return true;
		}
		return false;
	}
	
	
	private void showStickerDemoActivity(int arg) throws NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (arg == 1) {
			if (isOpen("com.tv.app")) {
				Log.i(tag + "_TV_CFG_STICKER_DEMO", "STICKER_DEMO will show");
				Intent intent = TvContext.context
						.getPackageManager()
						.getLaunchIntentForPackage(PACKAGE_NAME_OF_STICKER_DEMO);
				TvContext.context.startActivity(intent);
				synchronized (obj) {
					stickerdemoup = true;
				}
			} else {
				return;
			}
		} else {
			synchronized (obj) {
				stickerdemoup = false;
			}
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			ActivityManager manager = (ActivityManager) TvContext.context
					.getSystemService(TvContext.context.ACTIVITY_SERVICE);
			Method forceStopPackage = manager.getClass().getDeclaredMethod(
					"forceStopPackage", String.class);
			forceStopPackage.setAccessible(true);
			forceStopPackage.invoke(manager, PACKAGE_NAME_OF_STICKER_DEMO);

		}
	}
	
	public static void StartStickerDemoOnTimer(){
		synchronized (obj) {
			if(m_handler == null && stickerdemoup){
				m_handler = new Handler()
				{
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						if (isOpen("com.tv.app")
								&&!TvPluginControler.getInstance().getPvrManager().isTimeShiftRecording()
								&&!TvPluginControler.getInstance().getPvrManager().isRecording()
								&&!TvPluginControler.getInstance().getPvrManager().isPlaybacking()) {
					        	Intent intent = TvContext.context.getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME_OF_STICKER_DEMO);
					        	Log.i(tag,"start strickerdemoe");
					        	TvContext.context.startActivity(intent);
								} else {
									Log.i(tag,"tvapp don't open return");
									return;
								}
						}	
				};
				if(m_handler != null){
					Log.i(tag,"sendMessageDelayed");
					Message message = m_handler.obtainMessage(0);
					m_handler.sendMessageDelayed(message, delay);
				}
			}else{
				Log.i(tag,"onkey m_handler !=null cancel timer");
				if(m_handler!=null){
					m_handler.removeMessages(0);
					m_handler = null;
					StartStickerDemoOnTimer();
				}
			}
		}
	}
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if(arg1.getAction().equals("com.tv.ui.base.StickerDemoUIBroadReceiver.showStickerDemoActivity")){
			int onoff = arg1.getIntExtra("switch", 0);
			Log.i(tag,"onoff = "+onoff);
			try {
				Log.i(tag,"isOpen('com.tv.app') = "+isOpen("com.tv.app"));
				showStickerDemoActivity(onoff);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
