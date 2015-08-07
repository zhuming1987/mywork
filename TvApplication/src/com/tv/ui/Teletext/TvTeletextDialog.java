package com.tv.ui.Teletext;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.skyworth.config.SkyConfigDefs.SKY_CFG_RC_TYPE;
import com.tv.app.R;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvConfigTypes.TvEnumTeletextCommand;
import com.tv.framework.define.TvConfigTypes.TvEnumTeletextMode;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog;

public class TvTeletextDialog extends TvBaseDialog{

	public TvTeletextDialog() {
		super(TvContext.context, R.style.dialog,TvConfigTypes.TV_DIALOG_TELETEXT);
		// TODO Auto-generated constructor stub
		setDialogAttributes(Gravity.CENTER,  WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		setDialogContentView(new View(TvContext.context));
		setAutoDismiss(false);
	}

	@Override
	public boolean setDialogListener(DialogListener dialogListener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean processCmd(String key, DialogCmd cmd, Object obj) {
		// TODO Auto-generated method stub
		switch (cmd) {
		case DIALOG_SHOW:
		case DIALOG_UPDATE:
			super.showUI();
			boolean isText = TvPluginControler.getInstance().getChannelManager().isTtxChannel();
			boolean isSignal = TvPluginControler.getInstance().getCommonManager().isSignalState();
			TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
			Log.i("gky", "TvTeletextDialog::processCmd::isText is " + isText+" curSource is "+curSource.toString()
					+" isSignal is "+isSignal);
			if(isText && isSignal
					&& (curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV
						|| curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV
						|| currentInputSourceIs("CVBS")
						|| currentInputSourceIs("SVIDEO")
						|| currentInputSourceIs("SCART"))){
				TvPluginControler.getInstance().getCommonManager().openTeletext(TvEnumTeletextMode.TTX_MODE_NORMAL);
			}
			else{
				TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
				tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {
					
					@Override
					public void onClickListener(boolean flag) {
						// TODO Auto-generated method stub
						Log.i("gky", "onBtClick is " + flag);
						TvTeletextDialog.this.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
					}
				});
				tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
						new TvToastFocusData("", "", "The Pragram No Teletext!",1)); 
			}			
			break;
		case DIALOG_HIDE:
			break;
		case DIALOG_DISMISS:
			Log.i("gky", getClass().getName() + "-->processCmd dismiss TXT and close TXT");
			super.dismissUI();
			TvPluginControler.getInstance().getCommonManager().closeTeletext();
			break;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvTeletextDialog::onKeyDown:keycode is " + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_0);
            return true;
        case KeyEvent.KEYCODE_1:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_1);
            return true;
        case KeyEvent.KEYCODE_2:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_2);
            return true;
        case KeyEvent.KEYCODE_3:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_3);
            return true;
        case KeyEvent.KEYCODE_4:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_4);
            return true;
        case KeyEvent.KEYCODE_5:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_5);
            return true;
        case KeyEvent.KEYCODE_6:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_6);
            return true;
        case KeyEvent.KEYCODE_7:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_7);
            return true;
        case KeyEvent.KEYCODE_8:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_8);
            return true;
        case KeyEvent.KEYCODE_9:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.DIGIT_9);
            return true;
        case KeyEvent.KEYCODE_DPAD_UP:
        case KeyEvent.KEYCODE_CHANNEL_UP:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.PAGE_UP);
            return true;
        case KeyEvent.KEYCODE_DPAD_DOWN:
        case KeyEvent.KEYCODE_CHANNEL_DOWN:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.PAGE_DOWN);
            return true;
        case KeyEvent.KEYCODE_DPAD_LEFT:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.PAGE_LEFT);
            return true;
        case KeyEvent.KEYCODE_DPAD_RIGHT:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.PAGE_RIGHT);
            return true;
        case KeyEvent.KEYCODE_MEDIA_BOOKING:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.NORMAL_MODE_NEXT_PHASE);
            if(!TvPluginControler.getInstance().getChannelManager().isTeletextDisplayed())
            	processCmd(null, DialogCmd.DIALOG_DISMISS, null);
            return true;
        case KeyEvent.KEYCODE_BACK:
            processCmd(null, DialogCmd.DIALOG_DISMISS, null);
            return true;
        case KeyEvent.KEYCODE_PROG_BLUE:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.CYAN);
            return true;
        case KeyEvent.KEYCODE_PROG_YELLOW:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.YELLOW);
            return true;
        case KeyEvent.KEYCODE_PROG_GREEN:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.GREEN);
            return true;
        case KeyEvent.KEYCODE_PROG_RED:
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.RED);
            return true;
        case KeyEvent.KEYCODE_MEDIA_AUDIO_CONTROL://MIX
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.MIX);
            return true;
//        case KeyEvent.KEYCODE_MEDIA_ORIGINAL_SOUNDTRACK:
//           TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.UPDATE);
//            return true;
        case KeyEvent.KEYCODE_SUBTITLE_SETTING://SUBTITLE
        	if(TvPluginControler.getInstance().getCommonManager().isTeletextSubtitleChannel()){
				if(TvPluginControler.getInstance().getCommonManager().isTeletextDisplayed()){
					TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.SUBTITLE_NAVIGATION);
				}else{
					TvPluginControler.getInstance().getCommonManager().openTeletext(TvEnumTeletextMode.TTX_MODE_SUBTITLE_NAVIGATION);
				}
			}
        	 return true;
        case KeyEvent.KEYCODE_MEDIA_SONG_SYSTEM://SIZE
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.SIZE);
            return true;
        case KeyEvent.KEYCODE_MEDIA_FUNCTION://INDEX
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.INDEX);
            return true;
        case KeyEvent.KEYCODE_MEDIA_RECORD://SUB-PAGE
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.SUBPAGE);
            return true;  
        case KeyEvent.KEYCODE_MEDIA_RELATIONSHIP://HOLD
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.HOLD);
            return true;
        case KeyEvent.KEYCODE_DISPLAY_MODE://REVEAL
            TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.REVEAL);
            return true;
        case KeyEvent.KEYCODE_MEDIA_SELECTED_SONGS: //CANCEL
        	if(TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_2)
        	{
        		TvPluginControler.getInstance().getCommonManager().sendTeletextCommand(TvEnumTeletextCommand.UPDATE);
        		return true;
        	}
        	else
        	{
        		return false;
        	}
		default:
			return false;
		}
	}
	
	public boolean currentInputSourceIs(final String source) {
        TvEnumInputSource currInputSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
        if (source.equals("CVBS")) {
            if (currInputSource.ordinal() >= TvEnumInputSource.E_INPUT_SOURCE_CVBS.ordinal()
                    && currInputSource.ordinal() <= TvEnumInputSource.E_INPUT_SOURCE_CVBS_MAX.ordinal())
                return true;
        } else if (source.equals("SVIDEO")) {
            if (currInputSource.ordinal() >= TvEnumInputSource.E_INPUT_SOURCE_SVIDEO.ordinal()
                    && currInputSource.ordinal() <= TvEnumInputSource.E_INPUT_SOURCE_SVIDEO_MAX.ordinal())
                return true;
        } else if (source.equals("SCART")) {
            if (currInputSource.ordinal() >= TvEnumInputSource.E_INPUT_SOURCE_SCART.ordinal()
                    && currInputSource.ordinal() <= TvEnumInputSource.E_INPUT_SOURCE_SCART_MAX.ordinal())
                return true;
        }
        return false;
    }

}
