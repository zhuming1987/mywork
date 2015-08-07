package com.tv.ui.ParentrolControl;

import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;




import com.tv.app.R;
import com.tv.data.TvType;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SettingView.TvSettingDialog;

public class PasswordMainMenuDialog extends PasswordBaseDialog{
	String mainUIindex="";
	int password=1234;
	String title=TvContext.context.getString(R.string.TV_CFG_LOCK_INPUT_PASSWORD);
	static PasswordMainMenuDialog passwordDialog=new PasswordMainMenuDialog();
	PasswordView passwordView=null;
	static public PasswordMainMenuDialog getInstance()
	{
		return passwordDialog;
	}
	
	private PasswordMainMenuDialog() {
		super();
		// TODO Auto-generated constructor stub
		setAutoDismiss(false);
		password=TvPluginControler.getInstance().getParentalControlManager().getPassword();
		
		
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
				//TvPluginControler.getInstance().getParentalControlManager().unLockChannel();
				PasswordMainMenuDialog.this.dismissUI();
				TvSettingDialog settingDialog = new TvSettingDialog();
				settingDialog.setDialogListener(dialogListener);
				settingDialog.processCmd(TvConfigTypes.TV_DIALOG_SETTING, DialogCmd.DIALOG_SHOW, TvType.getInstance().getSubMenuItem(mainUIindex));
				
			}
		};
		setDialogContentView(passwordView.getlayout(),new LayoutParams(edit_Weith,edit_Height*2));
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) {
		case DIALOG_SHOW:
			mainUIindex=(String) obj;
			passwordView.clearPassword();
			passwordView.setPassword(TvPluginControler.getInstance().getParentalControlManager().getPassword());
			passwordView.setTitle(TvContext.context.getString(R.string.TV_CFG_LOCK_INPUT_PASSWORD));
			super.showUI();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			Object[] objects = null;
			if (null != dialogListener) {
				dialogListener.onDismissDialogDone(objects);
			}
			break;
		default:
			break;
		}
		return false;
	}
}
