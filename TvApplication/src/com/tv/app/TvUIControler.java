package com.tv.app;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.minitoast.TvMiniToastDialog;

public class TvUIControler {
	
	private  float resolutionDiv = 1.0f;
	private  float dipDiv = 1.0f;
	private  int displayWidth = 1920;
	private  int displayHeight = 1080;
 	
	private static TvUIControler instance = null;
	
	public static TvUIControler getInstance()
	{
		if(instance == null)
		{
			instance = new TvUIControler();
		}
		
		return instance;
	}
	
	public TvUIControler()
	{	
		Log.i("gky", "TvUIControler::init");
		initResolutionDiv();
		initDipDiv();
	}
	
	private void initResolutionDiv()
	{
		Point size = new Point();
		Display display = ((WindowManager) TvContext.context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
		if(display == null)
		{
			Log.i("gky", "TvUIControler::initResolution:display is null");
			displayWidth = 1920;
			displayHeight = 1080;
		}
		else
		{
			display.getSize(size);
			displayWidth = size.x;
			displayHeight = size.y;
			Log.i("gky", "TvUIControler::initResolution: width is " + displayWidth
					+" height is " + displayHeight);
		}
		Log.i("gky", "TvUIControler::initResolution: width is " + displayWidth);
		switch (displayWidth) 
		{
			case 3840:
				resolutionDiv = 0.5f;
				break;
			case 1920:
				resolutionDiv = 1.0f;
				break;
			case 1366:
				resolutionDiv = 1.4f;
				break;
			case 1280:
				resolutionDiv = 1.5f;
				break;
			default:
				resolutionDiv = 1.0f;
				break;
		}
	}
	
	private void initDipDiv()
	{
		DisplayMetrics dm = TvContext.context.getApplicationContext().getResources().getDisplayMetrics();
		Log.i("gky", "TvUIControler::initDipDiv:density is "+dm.density + " default is " + DisplayMetrics.DENSITY_DEFAULT);
		dipDiv = dm.density * resolutionDiv;
		Log.i("gky", "TvUIControler::initDipDiv:dipDiv is "+dipDiv);
	}
	
	public void removeDialog(String key)
	{
		TvBaseDialog.removeDialog(key);
	}
	
	public void reMoveAllDialogs(){
		TvBaseDialog.removeAllDialogs();
	}

	public  float getResolutionDiv() {
		return resolutionDiv;
	}

	public  float getDipDiv() {
		return dipDiv;
	}

	public int getDisplayWidth() {
		return displayWidth;
	}

	public int getDisplayHeight() {
		return displayHeight;
	}
	
	public synchronized void showMniToast(final String msg){
		if(!msg.equals("")){
			TvMiniToastDialog miniToastDialog = new TvMiniToastDialog();
			miniToastDialog.processCmd(null, DialogCmd.DIALOG_SHOW, msg);
		}
	}
	
	public boolean isDialogShowing(String dlgName){
		return TvBaseDialog.isDialogShowing(dlgName);
	}
	
	public boolean isDialogShowing(){
		return TvBaseDialog.isDialogShowing();
	}
}
