package com.tv.app;

import java.util.ArrayList;

import com.tv.framework.define.TvContext;
import com.tv.framework.plugin.TvPluginManager;
import com.tv.plugin.TvPluginCallback;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class TvApplication extends Application{
	
	private ArrayList<Activity> list = new ArrayList<Activity>();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("gky", getClass().getName() +"--onCreate");
		TvContext.context = getApplicationContext();
		TvPluginManager.getInstance(TvContext.context).setPluginListener(new TvPluginCallback());
		if(TvPluginManager.getInstance(TvContext.context).loadSync())
			Log.i("gky", "load plugin is " + TvPluginManager.getInstance(TvContext.context).getTvPlugin().getName());
		//TvExceptionControler.getInstance().init(this);
	}

	public void removeActivity(Activity a){
		list.remove(a);
	}
	
	public void addActivity(Activity a){
		list.add(a);
	}
	
	public void finishActivity(){
		for (Activity activity : list) {  
            if (null != activity) {  
                activity.finish();  
            }  
        }
		//杀死该应用进程
       android.os.Process.killProcess(android.os.Process.myPid());  
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		String str = "";
		switch (level) {
			case TRIM_MEMORY_BACKGROUND:
				str = "TRIM_MEMORY_BACKGROUND";
				break;
			case TRIM_MEMORY_COMPLETE:
				str = "TRIM_MEMORY_COMPLETE";
				break;
			case TRIM_MEMORY_MODERATE:
				str = "TRIM_MEMORY_MODERATE";
				break;
			case TRIM_MEMORY_RUNNING_CRITICAL:
				str = "TRIM_MEMORY_RUNNING_CRITICAL";
				break;
			case TRIM_MEMORY_RUNNING_LOW:
				str = "TRIM_MEMORY_RUNNING_LOW";
				break;
			case TRIM_MEMORY_RUNNING_MODERATE:
				str = "TRIM_MEMORY_RUNNING_MODERATE";
				break;
			case TRIM_MEMORY_UI_HIDDEN:
				str = "TRIM_MEMORY_UI_HIDDEN";
				break;
		}
		Log.i("gky", getClass().getName() +"--onTrimMemory level is "+str);
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		Log.i("gky", getClass().getName() +"--onTerminate");
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Log.i("gky", getClass().getName() +"--onLowMemory");
	}

}
