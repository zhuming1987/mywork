package com.tv.ui.base;

import com.tv.app.TvUIControler;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigDefs.TV_CFG_TV_MENU_STICKER_DEMO_ENUM_TYPE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UITimer extends BroadcastReceiver{
	public static int DismissMsg=1001;
	public static int defaultShowTime=10;
	SharedPreferences SharedPreferences;
	public static int showTime=defaultShowTime;
	public static int leftTime=showTime;
	//线程是否在运行的标志位，保证同一时间只有一个线程在运行
	private boolean isrun=false;
	private static UITimer uiTimer=new UITimer();
	/*
	 * 单例模式
	 */
	public UITimer()
	{
		SharedPreferences=TvContext.context.getSharedPreferences("showTime",Context.MODE_PRIVATE);
		getShowTimeFromDB();
	}
	
	public static UITimer getInstance()
	{
		return uiTimer;
	}
	
	private void setShowTimeToDB(int showTime)
	{
		Editor edit=SharedPreferences.edit();
		edit.putInt("currentShowTime", showTime);
		edit.commit();
	}
	
	private void getShowTimeFromDB()
	{
		Log.v("wxj","before getShowTimeFromDB,showTime="+showTime);
		showTime=SharedPreferences.getInt("currentShowTime",10);
		freshTime();
	}
		
	public void freshTime() {
		leftTime=showTime;
	}
	
	public void setHandler(Handler handler)
	{
		UIhandler=handler;
	}

	public void startTime()
	{
		if(!isrun)
		{	
			isrun=true;
			Thread thread=new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						while(leftTime>0)
						{
							leftTime--;
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block

							}
						}
						Message msg = new Message();
						msg.what=DismissMsg;
						UIhandler.sendMessage(msg);
						isrun=false;
						freshTime();
					}
				});
			thread.start();
		}
	}
	
	public Handler UIhandler =new Handler();
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if(arg1.getAction().equals("com.tv.ui.base.UITimer.getShowTimeFromDB"))
		{
			getShowTimeFromDB();
		}
		
	}
	
}
