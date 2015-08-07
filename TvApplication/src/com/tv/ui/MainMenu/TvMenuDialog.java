package com.tv.ui.MainMenu;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.data.MenuItem;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;


public class TvMenuDialog extends TvBaseDialog{

	private TvMenuView menuView;
	
	public TvMenuDialog() {
		super(TvContext.context,R.style.dialog,TvConfigTypes.TV_DIALOG_MAIN_MENU);
		// TODO Auto-generated constructor stub
		menuView = new TvMenuView(TvContext.context);
		menuView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(menuView);
	}

	@Override
	public boolean processCmd(String key,DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvMenuDialog::processCmd:key is " + key + " cmd is " + cmd.toString()
				+ " obj is " + obj);
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			if(obj instanceof ArrayList<?>){
				ArrayList<MenuItem> data = (ArrayList<MenuItem>)obj;
				menuView.updatView(data);
			}
			break;
		case DIALOG_HIDE:		
			break;
		case DIALOG_DISMISS:
			super.dismissUI();
			break;
		}
		return true;
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		return true;
	}
}
