package com.tv.ui.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvLoadingData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.Loading.TvLoadingDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.WindowManager;

public class ScreenCap {
	
	private static final String TAG = "ScreenCap";
	
	public static ScreenCap instance = null;
	private String screenShotPath;
	TvLoadingDialog tvLoadingDialog;
	
	public static ScreenCap getInstance(){
		if(instance == null)
			instance = new ScreenCap();
		return instance;
	}
	
	public void GetandSaveCurrentImage() {
		tvLoadingDialog = new TvLoadingDialog();
		tvLoadingDialog.processCmd(null, DialogCmd.DIALOG_SHOW,
				new TvLoadingData("Please Wait.....",
						"ScreenCaping......"));
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap Bmp = null;
				String SavePath = null;
				SavePath = "/skydir";
				SavePath = SavePath + "/ScreenImages";
				Bmp = takeScreenShot();
				try {
					File path = new File(SavePath);
					String fileName = System.currentTimeMillis() + ".png";
					screenShotPath = SavePath + "/" + fileName;
					Log.i(TAG, "screenShotPaht is " + screenShotPath);
					File file = new File(screenShotPath);
					Log.i(TAG, "path.exits is " + path.exists());
					if (!path.exists()) {
						Log.v(TAG,
								"GetandSaveCurrentImage::mkdir "
										+ path.mkdirs());
					}
					Log.i(TAG, "file is " + file.exists());
					if (!file.exists()) {
						Log.v(TAG,
								"GetandSaveCurrentImage::create file"
										+ file.createNewFile());
					}
					FileOutputStream fos = null;
					fos = new FileOutputStream(file);
					Log.i(TAG, "fos is " + fos);
					if (null != fos) {
						Bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
						fos.flush();
						fos.close();					
						mHandler.sendEmptyMessage(1);
						tvLoadingDialog.processCmd(TvConfigTypes.TV_DIALOG_LOADING, DialogCmd.DIALOG_DISMISS, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.i(TAG, "error is " + e.toString());
					mHandler.sendEmptyMessage(2);
				}
			}
		}).start();
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler(){
        
        public void handleMessage(Message msg) {
        	
            switch (msg.what) {  
            case 0:				
            	break;
            case 1:
            	TvUIControler.getInstance().reMoveAllDialogs();
//            	Toast.makeText(TvActivity.activityContext,
//						"ScreenShot Successfully " + screenShotPath,
//						Toast.LENGTH_LONG).show();
            	ComponentName componentName = new ComponentName("com.tv.main", "com.tv.main.MainActivity");
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("func","publish");
				bundle.putString("image", screenShotPath);
				bundle.putString("proName", "NA");
				bundle.putString("proDescription", "NA");
				intent.putExtras(bundle);
				intent.setComponent(componentName);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				TvContext.context.startActivity(intent);
            	break;
            case 2:
//            	Toast.makeText(TvActivity.activityContext,
//						" Failed to ScreenShot " + screenShotPath,
//						Toast.LENGTH_LONG).show();
            	break;
            }  
       };  
    };
    
	public Bitmap takeScreenShot() {

		Bitmap mScreenBitmap;
		WindowManager mWindowManager;
		DisplayMetrics mDisplayMetrics;
		Display mDisplay;

		mWindowManager = (WindowManager) TvContext.context
				.getSystemService(Context.WINDOW_SERVICE);
		mDisplay = mWindowManager.getDefaultDisplay();
		mDisplayMetrics = new DisplayMetrics();
		mDisplay.getRealMetrics(mDisplayMetrics);

		float[] dims = { mDisplayMetrics.widthPixels,
				mDisplayMetrics.heightPixels };
		//mScreenBitmap = screenshot((int) dims[0], (int) dims[1]);
		mScreenBitmap = SurfaceControl.screenshot((int) dims[0], (int) dims[1]);//for android4.4
		if (mScreenBitmap != null) {
			Log.v(TAG, "mScreenBitmap=" + mScreenBitmap);
			return mScreenBitmap;
		}
		return null;
	}

    private Bitmap screenshot(int x,int y){
	    Class<?> surfaceClass = null;
		try {
			surfaceClass = Class.forName(Surface.class.getName());//for android4.2
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                     
	    Method screenShotMethod = null;
		try {
			screenShotMethod = surfaceClass.getMethod("screenshot", new Class[]{int.class ,int.class});
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, "error is " + e.getMessage());
		}
	    Bitmap bmp = null;            
	    try {
			 bmp = (Bitmap) screenShotMethod.invoke(null, new Object[]{(int)x,(int)y});
			 Log.v(TAG,"screenshot::bmp is "+bmp);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return bmp;
    }
    /** 
     * 获取SDCard的目录路径功能 
     *@return 路径
     **/
    public String getSDCardPath(){
    	File sdcardDir = null;
    	//判断USB是否存在
    	String usbPath = "mnt/usb/";
    	File usbFile = new File(usbPath);	
		if (usbFile.exists() && usbFile.isDirectory()){
			Log.i(TAG, "usbFile length is " + usbFile.list().length);
			if(usbFile.list().length != 0){
				for (String fileName : usbFile.list()) {
					File usbFilePath = new File(usbPath+fileName);
					Log.i(TAG, "fileName is " + fileName
							+" exist is " + usbFilePath.exists()
							+" canWrite is " + usbFilePath.canWrite()
							+" space is " + (usbFilePath.getUsableSpace()/(1024*1024)));
					if(usbFilePath.exists()
							&& ((usbFilePath.getUsableSpace()/(1024*1024))>=10))
						return usbPath+fileName;
				}
			}
		}
    	//判断SDCard是否存在
    	boolean sdcardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    	Log.i(TAG, "getSDCardPath:: sdcardExist is " + sdcardExist);
    	if(sdcardExist)
    	{ 
    		sdcardDir = Environment.getExternalStorageDirectory();
    		return sdcardDir.toString();
    	}
    	return null;
    }
}
