package com.tv.ui.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import com.tv.app.TvUIControler;
import com.tv.ui.Animation.PageAnimation;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public abstract class TvBaseDialog extends Dialog{

	private String dialogType;
	private View bodyView;
	private boolean hasAnimation = false;
	
	private boolean autoDismiss =true;
	/**
	 * 设备无关像素比
	 */
	protected float div=TvUIControler.getInstance().getResolutionDiv();
	
	public static HashMap<String, TvBaseDialog> hash_dialogs = new HashMap<String, TvBaseDialog>();
	
	private static final ReentrantLock lock = new ReentrantLock();
	
	/**
	 * 是否自动消失，不设置的话，默认会自动消失
	 */
	public void setAutoDismiss(boolean flag)
	{
		autoDismiss=flag;
	}
	
	Handler TimerHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==UITimer.DismissMsg)
			{
				if(autoDismiss)
				{
					Log.v("gky",getClass().getName() + "-->dismisHandler");
					TvBaseDialog.this.setDialogListener(null);
					TvBaseDialog.this.processCmd(dialogType, DialogCmd.DIALOG_DISMISS, null);
				}
			}
			super.handleMessage(msg);
			
		}
	};
	UITimer uitimer=UITimer.getInstance();
	
	
	public enum DialogCmd{
		DIALOG_SHOW,
		DIALOG_HIDE,
		DIALOG_DISMISS,
		DIALOG_UPDATE
	}
	
	/**
	 * 创建窗口
	 * @param context
	 * @param theme
	 */
	public TvBaseDialog(Context context, int theme, String dialogType) {
		super(context,theme);
		this.dialogType = dialogType;
		
	}

	/**
	 * 设置窗口属性
	 * @param gravity
	 * @param windowType
	 */
	protected void setDialogAttributes(int gravity, int windowType)
    {
        getWindow().setGravity(gravity);
        getWindow().setType(windowType);
    }
	
	/**
	 * 设置窗口加载的View
	 * @param view
	 */
	protected void setDialogContentView(View view){
		bodyView = view;
		setContentView(view);
	}
	
	protected void setDialogContentView(View view, LayoutParams layoutParams){
		bodyView = view;
		setContentView(view, layoutParams);
	}
	
	/**
	 * 设计监听回调接口
	 * @param dialogListener
	 * @return
	 */
	public  abstract  boolean setDialogListener(DialogListener dialogListener);
	/**
	 * 定义统一的窗口控制接口函数，子类均要实现此函数
	 * @param cmd
	 * @param obj
	 */
	public abstract boolean processCmd(String key, DialogCmd cmd,Object obj);
	
	/**
	 * 打开窗口显示
	 */
	public void showUI(){
		if(lock.isLocked())//如果此时正在对hash_dialogs操作则无法启动新的Dialog
			return;
		uitimer.setHandler(TimerHandler);
		uitimer.startTime();
		hash_dialogs.put(dialogType, this);
		if(hasAnimation){//可设置动画效果开关
			super.show();
			bodyView.setAnimation(PageAnimation.getInstance().getleftOutAnimation());
		}else {
			super.show();
		}
	}
	
	/**
	 * 关闭窗口显示
	 */
	public void dismissUI(){
		if(lock.isLocked())//如果此时正在对hash_dialogs操作则无法关闭现有Dialog
		{
			Log.e("TVBASEDIALG", "lijinjin dismissUI() false");
			
			return;
		}
			
		Log.i("gky", getClass().getSimpleName() + ":dismissUI "+dialogType);
		hash_dialogs.remove(dialogType);
		uitimer.freshTime();
		if(hasAnimation){//可设置动画效果开关
			bodyView.setAnimation(PageAnimation.getInstance().getleftOutAnimation());
			dismissHandler.sendEmptyMessageDelayed(0, PageAnimation.getInstance().getDuration()+50);
		}else {
			Log.i("gky", getClass().getSimpleName() + ":super.dismiss "+dialogType);
			super.dismiss();
		}
	}
	
	public static void removeDialog(String key)
	{
		if(hash_dialogs.containsKey(key))
		{
			TvBaseDialog dialog = hash_dialogs.get(key);
			if(dialog != null && dialog.isShowing())
				dialog.processCmd(key, DialogCmd.DIALOG_DISMISS, null);
			Log.i("gky", "removeDialog "+key+" sucessfully!");
		}
	}
	
	public static void removeAllDialogs()
	{
		Log.i("gky", TvBaseDialog.class.getName() + "-->removeAllDialogs and lock");
		lock.lock();
		try {
			Iterator<Entry<String, TvBaseDialog>> iter = hash_dialogs.entrySet().iterator();
			Log.i("gky", TvBaseDialog.class.getName() + "-->removeAllDialog: count "+hash_dialogs.size()+"/"+iter.hasNext());
			while (iter.hasNext()) {
				Entry<String, TvBaseDialog> entry = iter.next();
				TvBaseDialog dialog = entry.getValue();
				if (dialog != null) {
					Log.i("gky", TvBaseDialog.class.getName() + "-->removeAllDialog: "+entry.getKey());
					dialog.setDialogListener(null);
					dialog.processCmd(entry.getKey(), DialogCmd.DIALOG_DISMISS, null);
					dialog.dismiss();
				}
			}
			Log.i("gky", TvBaseDialog.class.getName() + "-->removeAllDialogs and clear hash_dialogs");
			hash_dialogs.clear();
			UITimer.getInstance().freshTime();
		}finally{
			lock.unlock();
			Log.i("gky", TvBaseDialog.class.getName() + "-->removeAllDialogs and unlock");
		}
		
	}
	
	public static boolean isDialogShowing(String key){
		if(hash_dialogs.containsKey(key)){
			return true;
		}
		return false;
	}
	
	public static boolean isDialogShowing(){	
		return !hash_dialogs.isEmpty();
	}
	
	Handler dismissHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			dismiss();
			super.handleMessage(msg);
		}
	};
	
	/**
	 * 窗口监听
	 * @author Administrator
	 *
	 */
	public interface DialogListener{
		
		/**
		 * 窗口显示回调接口
		 * @param ID
		 * @return
		 */
		public boolean onShowDialogDone(int ID);
		
		/**
		 * 窗口关闭回调接口
		 * @return
		 */
		public int onDismissDialogDone(Object...obj);
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		uitimer.freshTime();
		return super.dispatchKeyEvent(event);
	}
	
}
