package com.tv.ui.CiInformation;

import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout.LayoutParams;

import com.tv.app.R;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ParentrolControl.PasswordBaseDialog;
import com.tv.ui.ParentrolControl.PasswordView;

public class CIInfoPwdDialog extends PasswordBaseDialog
{
	private int password = 1234;
	private String title=TvContext.context.getString(R.string.TV_CFG_LOCK_INPUT_PASSWORD);
	private static CIInfoPwdDialog passwordDialog = new CIInfoPwdDialog();
	private PasswordView passwordView=null;
	
	static public CIInfoPwdDialog getInstance()
	{
		return passwordDialog;
	}
	
	private CIInfoPwdDialog() {
		super();
		// TODO Auto-generated constructor stub
		setAutoDismiss(false);
		password = TvPluginControler.getInstance().getCilManager().getCIPassword();
		Log.i("gky", getClass().getSimpleName() +"  CIInfoPwdDialog password is "+password);
		super.PASSWORDSIZE = TvPluginControler.getInstance().getCilManager().getCIPasswordLength();
		Log.i("gky", getClass().getSimpleName() +"  CIInfoPwdDialog PASSWORDSIZE is "+super.PASSWORDSIZE);
		
		WindowManager.LayoutParams dialogParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialogParams.x = 0;
		dialogParams.y = 0;
		
		this.getWindow().setAttributes(dialogParams);
		
		passwordView = new PasswordView(TvContext.context,0,0,edit_Weith, edit_Height,margin_v,margin_h,password,title,25,30,PASSWORDSIZE)
		{
			public void passwordwrong(String input) {
				// TODO Auto-generated method stub
				this.clearPassword();
				this.passwordwrongInforText();
				Log.i("gky", getClass().getSimpleName()+"------>passwordwrong input is "+input);
				CIInfoPwdDialog.this.dismissUI();
				TvPluginControler.getInstance().getCilManager().answerEnq(input);
			}
			
			@Override
			public void passwordright(String input) {
				// TODO Auto-generated method stub
				Log.i("gky", getClass().getSimpleName() +"----->passwordright input is "+input);
				CIInfoPwdDialog.this.dismissUI();
				TvPluginControler.getInstance().getCilManager().answerEnq(input);
			}
		};
		setDialogContentView(passwordView.getlayout(),new LayoutParams(edit_Weith,edit_Height*2));
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) {
		case DIALOG_SHOW:
			Log.i("gky",getClass().toString()+"processCmd---->show ui");
			passwordView.clearPassword();
			//passwordView.setPassword(TvPluginControler.getInstance().getCilManager().getCIPassword());
			passwordView.setPassword(password);
			passwordView.setTitle((String)obj);
			super.showUI();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			break;
		default:
			break;
		}
		return false;
	}
}
