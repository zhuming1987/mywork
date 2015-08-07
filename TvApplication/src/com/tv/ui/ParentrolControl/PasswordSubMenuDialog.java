package com.tv.ui.ParentrolControl;

import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout.LayoutParams;




import com.tv.app.R;
import com.tv.data.TvType;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SettingView.TvNextItemView;
import com.tv.ui.SettingView.TvSettingDialog;
import com.tv.ui.SettingView.TvSettingItemView;

public class PasswordSubMenuDialog extends PasswordBaseDialog{
	TvSettingItemView item=null;
	int password=1234;
	String title=TvContext.context.getString(R.string.TV_CFG_LOCK_INPUT_PASSWORD);
	static private PasswordSubMenuDialog passwordDialog=new PasswordSubMenuDialog();
	PasswordView passwordView=null;
	private boolean passwordFlagSubMenu=false;
	static public PasswordSubMenuDialog getInstance()
	{
		return passwordDialog;
	}
	
	public boolean getPasswordFlagSubMenu()
	{
		return passwordFlagSubMenu;
	}
	private PasswordSubMenuDialog() {
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
				passwordFlagSubMenu=false;
				this.clearPassword();
				this.passwordwrongInforText();
			}
			@Override
			public void passwordright(String input) {
				// TODO Auto-generated method stub
				PasswordSubMenuDialog.this.dismissUI();
				TvSettingDialog settingDialog = new TvSettingDialog();
				passwordFlagSubMenu=true;
				item.OnExecuteSet(TvNextItemView.ENTER);
				passwordFlagSubMenu=false;
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
			item=(TvSettingItemView) obj;
			passwordView.clearPassword();
			passwordView.setPassword(TvPluginControler.getInstance().getParentalControlManager().getPassword());
			passwordView.setTitle(TvContext.context.getString(R.string.TV_CFG_LOCK_INPUT_PASSWORD));
			super.showUI();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			if(null != dialogListener)
			{
				dialogListener.onDismissDialogDone(null);
			}
			break;
		}
		return false;
	}
}
