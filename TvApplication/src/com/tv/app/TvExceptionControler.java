//package com.tv.app;
//
//import java.lang.Thread.UncaughtExceptionHandler;
//
//import com.tv.framework.define.TvContext;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Looper;
//import android.util.Log;
//import android.widget.Toast;
//
///**
// * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
// * 
// * @author user
// * 
// */
//public class TvExceptionControler implements UncaughtExceptionHandler {
//	
//	public static final String TAG = "TvExceptionControler";
//	
//	//系统默认的UncaughtException处理类 
//	private Thread.UncaughtExceptionHandler mDefaultHandler;
//	
//	private TvApplication application = null;
//	
//	private static TvExceptionControler instance = null;
//
//	/** 获取CrashHandler实例 ,单例模式 */
//	public static TvExceptionControler getInstance() {
//		if(instance == null)
//			instance = new TvExceptionControler();
//		return instance;
//	}
//
//	
//	public void init(TvApplication application) {
//		this.application = application;
//		//获取系统默认的UncaughtException处理器
//		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
//		//设置该CrashHandler为程序的默认处理器
//		Thread.setDefaultUncaughtExceptionHandler(this);
//	}
//
//	/**
//	 * 当UncaughtException发生时会转入该函数来处理
//	 */
//	@Override
//	public void uncaughtException(Thread thread, Throwable ex) {
//		if (!handleException(ex) && mDefaultHandler != null) {
//			//如果用户没有处理则让系统默认的异常处理器来处理
//			mDefaultHandler.uncaughtException(thread, ex);
//		} else {
//			try {
//				Thread.sleep(3000);
//				
//				Intent intent = new Intent(application.getApplicationContext(), TvActivity.class);
//	            PendingIntent restartIntent = PendingIntent.getActivity(  
//	            		application.getApplicationContext(), 0, intent,  
//	                    Intent.FLAG_ACTIVITY_NEW_TASK);                                               
//	            //退出程序
//	            Log.i("gky", "TvExceptionControler::uncaughtException:: finish");
//	            AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);  
//	            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,restartIntent); // 1秒钟后重启应用 
//	            application.finishActivity();
//			} catch (InterruptedException e) {
//				Log.e(TAG, "error : ", e);
//			}
//			//退出程序
//			//android.os.Process.killProcess(android.os.Process.myPid());
//			//System.exit(1);
//		}
//	}
//
//	/**
//	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
//	 * 
//	 * @param ex
//	 * @return true:如果处理了该异常信息;否则返回false.
//	 */
//	private boolean handleException(Throwable ex) {
//		if (ex == null) {
//			return false;
//		}
//		Log.i("gky", "TvExceptionControler::handleException:ex is " + ex.getMessage()
//				+"/n" + ex.toString());
//		//使用Toast来显示异常信息
//		new Thread() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				Toast.makeText(TvContext.context, "Program error, the upcoming reboot.", Toast.LENGTH_LONG).show();
//				Looper.loop();
//			}
//		}.start();
//		return true;
//	}
//}
//
