package com.tv.ui.ParentrolControl;

import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;




import com.tv.app.R;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;

public class PasswordUnLockChannelDialog extends PasswordBaseDialog{

	int password=1234;
	String title=TvContext.context.getString(R.string.TV_CFG_LOCK_INPUT_PASSWORD);
	static PasswordUnLockChannelDialog passwordDialog=new PasswordUnLockChannelDialog();
	PasswordView passwordView=null;
	private boolean beShowed;
	static public PasswordUnLockChannelDialog getInstance()
	{
		return passwordDialog;
	}
	
	private PasswordUnLockChannelDialog() {
		super();
		// TODO Auto-generated constructor stub
		setAutoDismiss(false);
		password=TvPluginControler.getInstance().getParentalControlManager().getPassword();
		beShowed=false;
		
		WindowManager.LayoutParams dialogParams= new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialogParams.x=0;
		dialogParams.y=0;
		
		this.getWindow().setAttributes(dialogParams);
		
		passwordView=new PasswordView(TvContext.context,0,0,edit_Weith, edit_Height,margin_v,margin_h,password,title,15,30,PASSWORDSIZE)
		{
			@Override
			public void passwordwrong(String input) {
				// TODO Auto-generated method stub
				this.clearPassword();
				this.passwordwrongInforText();
			}
			@Override
			public void passwordright(String input) {
				// TODO Auto-generated method stub
				TvPluginControler.getInstance().getParentalControlManager().unLockChannel();
				PasswordUnLockChannelDialog.this.dismissUI();
			}
		};
		FrameLayout layout = passwordView.getlayout();
		
		setDialogContentView(passwordView.getlayout(),new LayoutParams(edit_Weith,edit_Height*2));
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) {
		case DIALOG_SHOW:
			passwordView.clearPassword();
			passwordView.setPassword(TvPluginControler.getInstance().getParentalControlManager().getPassword());
			passwordView.setTitle(TvContext.context.getString(R.string.TV_CFG_LOCK_INPUT_PASSWORD));
			super.showUI();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			dismissUI();
			break;
		}
		return false;
	}
	public boolean getbeShowed()
	{
		return beShowed;
	}
	public void setbeShowed(boolean flag)
	{
		beShowed=flag;
	}
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		Log.v("wxj","dismiss");
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
				}		
				setbeShowed(false);
				Log.v("wxj","in thread,beShowed="+beShowed);
			}
		});
		t.start();
	}
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		super.hide();
		Log.v("wxj","hide");
Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
				}		
				setbeShowed(false);
				Log.v("wxj","in thread,beShowed="+beShowed);
			}
		});
		t.start();
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.hide();
		Log.v("wxj","onStop");
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
					// TODO: handle exception
				}		
				setbeShowed(false);
				Log.v("wxj","in thread,beShowed="+beShowed);
			}
		});
		t.start();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		/*switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			mQuickKeyListener.onQuickKeyDownListener(keyCode, event);
			break;
		default:
			break;
		}*/
		return super.onKeyDown(keyCode, event);
	}
}
