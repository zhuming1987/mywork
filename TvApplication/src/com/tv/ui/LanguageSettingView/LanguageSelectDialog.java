package com.tv.ui.LanguageSettingView;

import android.view.Gravity;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.SettingView.TvSettingView;
import com.tv.ui.base.TvBaseDialog;

public class LanguageSelectDialog extends TvBaseDialog{

	private LanguageSelectView languageSelectView;
	private DialogListener dialogListener;
	
	public LanguageSelectDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_LANGUAGE_SETTING);
		// TODO Auto-generated constructor stub		
		languageSelectView = new LanguageSelectView(TvContext.context);
		languageSelectView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(languageSelectView);
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return true;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) {
		case DIALOG_SHOW:
			super.showUI();
			languageSelectView.updateView();
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			Object[] objects = null;
			if(null != dialogListener)
				dialogListener.onDismissDialogDone(objects);
			break;
		}
		return false;
	}
	

}
