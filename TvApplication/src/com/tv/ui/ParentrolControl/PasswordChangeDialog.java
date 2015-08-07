package com.tv.ui.ParentrolControl;

import android.app.Instrumentation;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;
import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;

public class PasswordChangeDialog extends PasswordBaseDialog{

	int passwordChanged=1234;//修改之后的密码
	int passwordConfirm=1234;//确认密码
	String titleChanged=TvContext.context.getString(R.string.TV_CFG_LOCK_CHANGE_PASSWORD);
	String titleComfirm=TvContext.context.getString(R.string.TV_CFG_LOCK_COMFIRM_PASSWORD);
	PasswordView passwordChangeView=null;
	PasswordView passwordConfirmView=null;
	static public PasswordChangeDialog passwordChangeDialog=new PasswordChangeDialog();
	public static PasswordChangeDialog getInstance()
	{
		return passwordChangeDialog;
	}
	private PasswordChangeDialog() {
		super();
		// TODO Auto-generated constructor stub
		setAutoDismiss(false);
		WindowManager.LayoutParams dialogParams= new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialogParams.x=0;
		dialogParams.y=0;
		this.getWindow().setAttributes(dialogParams);
		
		passwordChangeView=new PasswordView(TvContext.context,0,0,edit_Weith, edit_Height,margin_v,margin_h,passwordChanged,titleChanged,15,30,PASSWORDSIZE)
		{
			@Override
			public void passwordwrong(String input) {
				// TODO Auto-generated method stub
				passwordChanged=getTempInputPassword();
				passwordConfirmView.setPassword(passwordChanged);
				passwordConfirmView.getlayout().requestFocus();
				Log.v("wxj","passwordChangeView passwordwrong()::passwordChanged="+passwordChanged);
			}
			@Override
			public void passwordright(String input) {
				// TODO Auto-generated method stub
				passwordChanged=getTempInputPassword();
				passwordConfirmView.setPassword(passwordChanged);
				passwordConfirmView.getlayout().requestFocus();
				Log.v("wxj","passwordChangeView passwordright()::passwordChanged="+passwordChanged);
			}
			@Override
	    	public void passwordsuper(String input) {
				// TODO Auto-generated method stub
				passwordwrong(input);
			}
		};

		passwordConfirmView=new PasswordView(TvContext.context,0,edit_Height*2,edit_Weith, edit_Height,margin_v,margin_h,passwordChanged,titleComfirm,15,30,PASSWORDSIZE)
		{
			@Override
			public void passwordwrong(String input) {
				// TODO Auto-generated method stub
				Log.v("wxj","passwordConfirmView passwordwrong()::passwordChanged="+passwordChanged);
				TvUIControler.getInstance().showMniToast(TvContext.context.getResources().getString(R.string.TV_CFG_LOCK_CHANGE_PASSWORD_FAIL));
				passwordConfirmView.clearPassword();
				passwordChangeView.clearPassword();
				passwordChangeView.getlayout().requestFocus();
			}
			@Override
			public void passwordright(String input) {
				// TODO Auto-generated method stub
				TvUIControler.getInstance().showMniToast(TvContext.context.getResources().getString(R.string.TV_CFG_LOCK_CHANGE_PASSWORD_SUCCESS));
//				PasswordChangeDialog.this.dismissUI();
//				KeyEvent keyBack=new KeyEvent(KeyEvent.ACTION_DOWN,KeyEvent.KEYCODE_BACK);
				

				Log.v("wxj","passwordConfirmView passwordwrong()::passwordChanged="+passwordChanged);
				TvPluginControler.getInstance().getParentalControlManager().setPassword(passwordChanged);
				Thread t=new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
		                    Instrumentation inst=new Instrumentation();
		                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
		                } catch (Exception e) {
		                    // TODO: handle exception
		                }
					}
				});
				t.start();
			}
			@Override
			public void passwordsuper(String input) {
				// TODO Auto-generated method stub
				passwordwrong(input);
			
			}
		};
		
		FrameLayout layout=new FrameLayout(TvContext.context);
		layout.addView(passwordChangeView.getlayout());
		layout.addView(passwordConfirmView.getlayout());
		setDialogContentView(layout,new LayoutParams(edit_Weith,edit_Height*4));
		
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener=dialogListener;
		return true;
	}
	
	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) {
		case DIALOG_SHOW:
			passwordChangeView.clearPassword();
			passwordConfirmView.clearPassword();
			passwordChangeView.setTitle(TvContext.context.getString(R.string.TV_CFG_LOCK_CHANGE_PASSWORD));
			passwordConfirmView.setTitle(TvContext.context.getString(R.string.TV_CFG_LOCK_COMFIRM_PASSWORD));
			super.showUI();
			passwordChangeView.getlayout().requestFocus();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
			{
				dialogListener.onDismissDialogDone(objects);
			}
			break;
		}
		return false;
	}
}
