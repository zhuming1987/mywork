package com.tv.ui.ParentrolControl;

import android.util.Log;
import android.view.KeyEvent;
import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.data.TvType;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SourceInfo.SourceInfoDialog;
import com.tv.ui.base.TvBaseDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

public class PasswordBaseDialog extends TvBaseDialog{
	
	public DialogListener dialogListener;
	protected int edit_Weith=(int)(304/div);//密码框的宽度
	protected int edit_Height=(int)(94/div);//密码框的高
//	protected int margin_v=(int)(2/div);//边框的宽度
	protected int margin_v=2;//边框的宽度
	protected int margin_h=2;//边框的高度
	protected int PASSWORDSIZE=4;//密码个数
	protected float password_X=0;//起始位置X
	protected float password_Y=0;//起始位置Y
	protected QuickKeyListener mQuickKeyListener=null;
	public PasswordBaseDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_INPUT_PASSWORD);
		// TODO Auto-generated constructor stub
		mQuickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		setAutoDismiss(false);
		
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
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		Log.v("wxj","PasswordbaseDialog onKeyDown keyCode="+keyCode);
		switch (keyCode)
		{
			case KeyEvent.KEYCODE_CHANNEL_UP:
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_CHANNEL_DOWN:
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_MEDIA_BOOKING://TEXT
			case KeyEvent.KEYCODE_SUBTITLE_SETTING://SUBTITLE
			case KeyEvent.KEYCODE_PROG_RED://REC.
			case KeyEvent.KEYCODE_PROG_BLUE://BLUE
			case KeyEvent.KEYCODE_PROG_YELLOW://YELLOW
			case KeyEvent.KEYCODE_PROG_GREEN://GREEN
			case KeyEvent.KEYCODE_INFO://INFO
			case KeyEvent.KEYCODE_MEDIA_DELETE://RADIO
			case KeyEvent.KEYCODE_IMAGE_MODE://A.D
			case KeyEvent.KEYCODE_AUDIO_SETTING://Nicam/Audio
			case KeyEvent.KEYCODE_ALTERNATE://RETURN
			case KeyEvent.KEYCODE_ENTER_EPG://EPG
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_MEDIA_FUNCTION://INDEX
			case KeyEvent.KEYCODE_MEDIA_FAVORITES://FAV
//			case KeyEvent.KEYCODE_0:
//			case KeyEvent.KEYCODE_1:
//			case KeyEvent.KEYCODE_2:
//			case KeyEvent.KEYCODE_3:
//			case KeyEvent.KEYCODE_4:
//			case KeyEvent.KEYCODE_5:
//			case KeyEvent.KEYCODE_6:
//			case KeyEvent.KEYCODE_7:
//			case KeyEvent.KEYCODE_8:
//			case KeyEvent.KEYCODE_9:
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE://T.shift
			//factory hotkeys
			case KeyEvent.KEYCODE_FACTORY_FACTORY_MODE:
			case KeyEvent.KEYCODE_FACTORY_WHITE_BALANCE:
			case KeyEvent.KEYCODE_FACTORY_AUTO_ADC:
			case KeyEvent.KEYCODE_FACTORY_BUS_OFF:
			case KeyEvent.KEYCODE_FACTORY_RESET:
			case KeyEvent.KEYCODE_FACTORY_AGING_MODE:
			case KeyEvent.KEYCODE_FACTORY_OUTSET:
			case KeyEvent.KEYCODE_FACTORY_BARCODE:
			mQuickKeyListener.onQuickKeyDownListener(keyCode, event);
			break;
		}
		if(keyCode == KeyEvent.KEYCODE_MENU)
			return true;
		
		this.processCmd(TvConfigTypes.TV_DIALOG_LANGUAGE_SETTING,DialogCmd.DIALOG_DISMISS, null);
        return super.onKeyDown(keyCode, event);
    }

}
