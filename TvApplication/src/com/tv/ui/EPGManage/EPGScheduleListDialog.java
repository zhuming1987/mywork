package com.tv.ui.EPGManage;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog;

public class EPGScheduleListDialog extends TvBaseDialog{

	private DialogListener dialogListener;
	private EPGScheduleListView EPGScheduleListView;
	
	public EPGScheduleListDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_EPG_SCHEDULE_LIST);
		TvQuickKeyControler.getInstance().getQuickKeyListener();
		EPGScheduleListView = new EPGScheduleListView(TvContext.context);
		EPGScheduleListView.setParentDialog(this);
		setDialogAttributes(Gravity.LEFT | Gravity.TOP, 
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(EPGScheduleListView);
		setAutoDismiss(false);
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		this.dialogListener = dialogListener;
		return false;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) {
			case DIALOG_SHOW:
			case DIALOG_UPDATE:
				super.showUI();
				EPGScheduleListView.updateView();
				break;
			case DIALOG_HIDE:
				break;
			case DIALOG_DISMISS:
				super.dismissUI();
				if(null != dialogListener)
					dialogListener.onDismissDialogDone(null,null);
				break;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {//列表为空时，做按键监听，退出
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_PROG_YELLOW){
				processCmd(null, DialogCmd.DIALOG_DISMISS, null);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
