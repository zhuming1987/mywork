package com.tv.app;

import java.util.ArrayList;

import com.skyworth.config.SkyConfigDefs.SKY_CFG_RC_TYPE;
import com.tv.factoryHotkey.FactoryHotKeyActivity;
import com.tv.data.MenuItem;
import com.tv.data.TvType;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigDefs.TV_CFG_SWITCH_AD_ENUM_TYPE;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvContext;
import com.tv.framework.data.TvToastFocusData;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ListView.TvListViewDialog;
import com.tv.ui.MainMenu.TvMenuDialog;
import com.tv.ui.Pvrsetting.DiskSelectDialog;
import com.tv.ui.SettingView.TvSettingDialog;
import com.tv.ui.SourceInfo.SourceInfoDialog;
import com.tv.ui.Teletext.TvTeletextDialog;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.AudioLanguage.AudioLanguageListViewDialog;
import com.tv.ui.ChannelNum.TvChannelNumDialog;
import com.tv.ui.EPGManage.EPGManageDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.ChannelEdit.ChannelEditDialog;
import com.tv.framework.define.TvConfigTypes.EnumChOpType;

import android.content.ComponentName;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

public class TvQuickKeyControler {

	private static TvQuickKeyControler instance = null;
	private static final String TAG = "TvQuickKeyControler";
	
	public static TvQuickKeyControler getInstance(){
		if(instance == null)
			instance = new TvQuickKeyControler();
		return instance;
	}
	
	public interface QuickKeyListener
	{
		public boolean onQuickKeyDownListener(int keyCode, KeyEvent event);
	}
	
	QuickKeyListener quickKeyListener = new QuickKeyListener() {
		
		@Override
		public boolean onQuickKeyDownListener(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			Log.i("gky", "TvQuickKeyControler::onQuickKeyDownListener:keycode is " + keyCode);
			TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
			switch (keyCode) 
			{
				case KeyEvent.KEYCODE_MENU:
				{
					if (TvType.getInstance().isInit()) {
						TvMenuDialog menuDialog = new TvMenuDialog();
						menuDialog.setDialogListener(null);
						menuDialog.processCmd(TvConfigTypes.TV_DIALOG_MAIN_MENU, DialogCmd.DIALOG_SHOW, TvType.getInstance().getMenuData());
					}		
					return true;
				}				
				case KeyEvent.KEYCODE_MEDIA_FUNCTION://INDEX
					if(TvPluginControler.getInstance().getCommonManager().getCurrentInputSource() == TvEnumInputSource.E_INPUT_SOURCE_DTV)
					{
						TvUIControler.getInstance().reMoveAllDialogs();
					    DiskSelectDialog diskSelectDialog = new DiskSelectDialog();
					    diskSelectDialog.setDialogListener(null);
					    diskSelectDialog.processCmd(TvConfigTypes.TV_DIALOG_DISK_SELECT, DialogCmd.DIALOG_SHOW, "PVRChannelListView");
					    return true;
					}
					else
					{
						break;
					}
					//wangxian
				case KeyEvent.KEYCODE_CHANNEL_UP:
				{					
					if(curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV || 
							curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV || 
							     curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV2)
					{
						TvPluginControler.getInstance().getChannelManager().programUp();
						SourceInfoDialog sourceInfoDialog = new SourceInfoDialog(keyCode);
						sourceInfoDialog.setDialogListener(null);
						sourceInfoDialog.processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO, DialogCmd.DIALOG_SHOW, true);
					}					
				}
					return true;
				case KeyEvent.KEYCODE_CHANNEL_DOWN:
				{
					if(curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV || 
							curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV || 
							     curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV2)
					{
						TvPluginControler.getInstance().getChannelManager().programDown();
						SourceInfoDialog sourceInfoDialog = new SourceInfoDialog(keyCode);
						sourceInfoDialog.setDialogListener(null);
						sourceInfoDialog.processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO, DialogCmd.DIALOG_SHOW, true);
					}					
				}
					return true;
				case KeyEvent.KEYCODE_MEDIA_BOOKING://TEXT
					TvUIControler.getInstance().reMoveAllDialogs();
					TvTeletextDialog teletextDialog = new TvTeletextDialog();
					teletextDialog.setDialogListener(null);
					teletextDialog.processCmd(TvConfigTypes.TV_DIALOG_TELETEXT, DialogCmd.DIALOG_SHOW, null);
					return true;
				case KeyEvent.KEYCODE_SUBTITLE_SETTING://subtitle
				{
					TvUIControler.getInstance().reMoveAllDialogs();
					if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV){
						MenuItem data = new MenuItem(TvConfigDefs.TV_CFG_SUBTITLE_LANGUAGE, "system_setting", true, null);
						TvListViewDialog listViewDialog = new TvListViewDialog();
						listViewDialog.setDialogListener(new DialogListener() {
							
							@Override
							public boolean onShowDialogDone(int ID) {
								// TODO Auto-generated method stub
								return false;
							}
							
							@Override
							public int onDismissDialogDone(Object... obj) {
								// TODO Auto-generated method stub								
								return 0;
							}
						});
						listViewDialog.processCmd(TvConfigTypes.TV_DIALOG_LISTVIEW, DialogCmd.DIALOG_SHOW, data);
					}
				}
				return true;
				case KeyEvent.KEYCODE_FACTORY_FACTORY_MODE: //factory hotkey
					if(TvPluginControler.getInstance().getCommonManager().getFactoryHotkeyEnable() == 1)
					{
						Intent it = new Intent();
						it.setComponent(new ComponentName("com.skyworth.factorymenu","com.skyworth.factorymenu.SkyFactoryActivity"));
						it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						it.putExtra("StartScene", "Factory");
						TvContext.context.startActivity(it);
						return true;
					}
					else
					{
						break;
					}
				case KeyEvent.KEYCODE_FACTORY_WHITE_BALANCE: //WB hotkey
					if(TvPluginControler.getInstance().getCommonManager().getFactoryHotkeyEnable() == 1)
					{
						Intent wb = new Intent();
						wb.setComponent(new ComponentName("com.skyworth.factorymenu","com.skyworth.factorymenu.SkyFactoryActivity"));
						wb.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						wb.putExtra("StartScene", "White Balance");
						TvContext.context.startActivity(wb);
						return true;
					}
					else
					{
						break;
					}
				case KeyEvent.KEYCODE_FACTORY_AUTO_ADC:  //Auto ADC hotkey
					if(TvPluginControler.getInstance().getCommonManager().getFactoryHotkeyEnable() == 1)
					{
						Intent adc = new Intent();
						adc.setComponent(new ComponentName("com.skyworth.factorymenu","com.skyworth.factorymenu.SkyFactoryActivity"));
						adc.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						adc.putExtra("StartScene", "Auto ADC");
						TvContext.context.startActivity(adc);
						return true;
					}
					else
					{
						break;
					}
				case KeyEvent.KEYCODE_FACTORY_CA_CARD: //Factory MAC and IP
					if(TvPluginControler.getInstance().getCommonManager().getFactoryHotkeyEnable() == 1)
					{
						Intent macIp = new Intent();
						macIp.setComponent(new ComponentName("com.skyworth.factorymenu","com.skyworth.factorymenu.SkyFactoryActivity"));
						macIp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						macIp.putExtra("StartScene", "MAC IP");
						TvContext.context.startActivity(macIp);
						return true;
					}
					else
					{
						break;
					}
				case KeyEvent.KEYCODE_FACTORY_BUS_OFF:
				case KeyEvent.KEYCODE_FACTORY_RESET:
				case KeyEvent.KEYCODE_FACTORY_OUTSET:
				case KeyEvent.KEYCODE_FACTORY_BARCODE:
				case KeyEvent.KEYCODE_FACTORY_AGING_MODE:
//				case KeyEvent.KEYCODE_FACTORY_HDMI1:
				case KeyEvent.KEYCODE_FACTORY_VGA:
				case KeyEvent.KEYCODE_COOCAA:
				case KeyEvent.KEYCODE_VOICE_END:
				case KeyEvent.KEYCODE_FACTORY_UPLAYER:
					if(TvPluginControler.getInstance().getCommonManager().getFactoryHotkeyEnable() == 1)
					{
						Log.d(TAG, "factory hotkey = 1");
						if(keyCode == KeyEvent.KEYCODE_FACTORY_BUS_OFF)
						{
							Log.d(TAG, "bus off hotkey");
							Intent it = new Intent(TvActivity.instance, FactoryHotKeyActivity.class);
							it.putExtra(FactoryHotKeyActivity.OPKEY, FactoryHotKeyActivity.BUSOFF);
							TvActivity.instance.startActivity(it);
						}
						else if(keyCode == KeyEvent.KEYCODE_VOICE_END)
						{
							if(TvEnumInputSource.E_INPUT_SOURCE_DTV == TvPluginControler.getInstance().getCommonManager().getCurrentInputSource())
							{
								Log.i("zhm","EPGManageDialog will show");
								TvUIControler.getInstance().reMoveAllDialogs();
								EPGManageDialog epgManageDialog = new EPGManageDialog();
								epgManageDialog.setDialogListener(null);
								epgManageDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_MANAGE, DialogCmd.DIALOG_SHOW, null);
							}
						}
						else if(keyCode == KeyEvent.KEYCODE_FACTORY_BARCODE)
						{
							Log.d(TAG, "bar code hotkey");
							Intent it = new Intent(TvActivity.instance, FactoryHotKeyActivity.class);
							it.putExtra(FactoryHotKeyActivity.OPKEY, FactoryHotKeyActivity.BARCODE);
							TvActivity.instance.startActivity(it);
						}
						else if(keyCode == KeyEvent.KEYCODE_FACTORY_AGING_MODE)
						{
							Log.d(TAG, "aging mode hotkey");
							Intent it = new Intent(TvActivity.instance, FactoryHotKeyActivity.class);
							it.putExtra(FactoryHotKeyActivity.OPKEY, FactoryHotKeyActivity.M_MODE);
							TvActivity.instance.startActivity(it);
						}
						else if(keyCode == KeyEvent.KEYCODE_FACTORY_RESET)
						{
							TvPluginControler.getInstance().getCommonManager().factoryRestore();
						}
						else if(keyCode == KeyEvent.KEYCODE_FACTORY_UPLAYER)
						{
							Intent it = new Intent();
						    it.setComponent(new ComponentName("com.tianci.localmedia","com.tianci.localmedia.SkyLocalMedia"));
							TvActivity.instance.startActivity(it);
						}
//						else if(keyCode == KeyEvent.KEYCODE_FACTORY_HDMI1) // //Factory hotkey HDMI1
//						{
//							try{
//								TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_HDMI.ordinal());
//							} catch (RemoteException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
						else if(keyCode == KeyEvent.KEYCODE_FACTORY_VGA)  //Factory hotkey VGA
						{
							try{
								TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_VGA.ordinal());
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else if(keyCode == KeyEvent.KEYCODE_COOCAA)  //Factory hotkey DTV
						{
							try{
								TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV.ordinal());
							} catch (RemoteException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
							
						return true;
					}
					else
					{
						break;
					}
				case KeyEvent.KEYCODE_INFO:
				case KeyEvent.KEYCODE_PROG_YELLOW:
					if(keyCode == KeyEvent.KEYCODE_INFO && TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_1)
					{
						TvUIControler.getInstance().reMoveAllDialogs();
						SourceInfoDialog sourceInfoDialog2 = new SourceInfoDialog(keyCode);
						sourceInfoDialog2.setDialogListener(null);
						sourceInfoDialog2.processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO, DialogCmd.DIALOG_SHOW, false);
						return true;
					}
					else if(keyCode == KeyEvent.KEYCODE_PROG_YELLOW && TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_2)
					{
						TvUIControler.getInstance().reMoveAllDialogs();
						SourceInfoDialog sourceInfoDialog2 = new SourceInfoDialog(keyCode);
						sourceInfoDialog2.setDialogListener(null);
						sourceInfoDialog2.processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO, DialogCmd.DIALOG_SHOW, false);
						return true;
					}
					else
					{
						return false;
					}
				case KeyEvent.KEYCODE_MEDIA_DELETE://RADIO
				{
					if(TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_1)
					{
						TvUIControler.getInstance().reMoveAllDialogs();
						Log.d("gky", getClass().toString() + " switchToRadio curSource is "+curSource);
						if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV)
						{
							boolean ret = TvPluginControler.getInstance().getChannelManager().switchToRadio();
							if(ret){
								TvUIControler.getInstance().
									showMniToast(TvContext.context.getResources().getString(R.string.str_tip_switch_to_radio));
							}else{
								TvUIControler.getInstance().
									showMniToast(TvContext.context.getResources().getString(R.string.str_tip_switch_to_dtv));
							}
							return true;
						}
						return false;
					}
					else
					{
						return false;
					}
				}
				case KeyEvent.KEYCODE_IMAGE_MODE://A.D
				{
					boolean isDTV = (TvPluginControler.getInstance().getCommonManager().getCurrentInputSource() == TvEnumInputSource.E_INPUT_SOURCE_DTV);
					boolean isSignal = TvPluginControler.getInstance().getCommonManager().isSignalState();
					if(isDTV && isSignal)
					{
						TV_CFG_SWITCH_AD_ENUM_TYPE eType = TvPluginControler.getInstance().getCommonManager().switchAD();
						TvToastFocusDialog tvToastFocusDialog = new TvToastFocusDialog();
						tvToastFocusDialog.setOnBtClickListener(new OnBtClickListener() {
							
							@Override
							public void onClickListener(boolean flag) {
								// TODO Auto-generated method stub
							}
						});
						switch (eType) {
						case TV_CFG_SWITCH_AD_TRUE:
							tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
									new TvToastFocusData("", "",TvContext.context.getResources().getString(R.string.TV_CFG_SWITCH_AD_TRUE),1));
							break;
						case TV_CFG_SWITCH_AD_FALSE:
							tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
									new TvToastFocusData("", "",TvContext.context.getResources().getString(R.string.TV_CFG_SWITCH_AD_FALSE),1));
							break;
						case TV_CFG_SWITCH_AD_NONE:
							tvToastFocusDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
									new TvToastFocusData("", "",TvContext.context.getResources().getString(R.string.TV_CFG_SWITCH_AD_NONE),1));
							break;
						}
					}
				}
					return true;
				case KeyEvent.KEYCODE_ENTER_EPG://EPG
				case KeyEvent.KEYCODE_MEDIA_REWIND:
					if(keyCode == KeyEvent.KEYCODE_ENTER_EPG && TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_1)
					{
						{
							if(TvEnumInputSource.E_INPUT_SOURCE_DTV == TvPluginControler.getInstance().getCommonManager().getCurrentInputSource())
							{
								Log.i("zhm","EPGManageDialog will show");
								TvUIControler.getInstance().reMoveAllDialogs();
								EPGManageDialog epgManageDialog = new EPGManageDialog();
								epgManageDialog.setDialogListener(null);
								epgManageDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_MANAGE, DialogCmd.DIALOG_SHOW, null);
							}
						}
						return true;
					}
					else if(keyCode == KeyEvent.KEYCODE_MEDIA_REWIND && TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_2)
					{
						{
							if(TvEnumInputSource.E_INPUT_SOURCE_DTV == TvPluginControler.getInstance().getCommonManager().getCurrentInputSource())
							{
								Log.i("zhm","EPGManageDialog will show");
								TvUIControler.getInstance().reMoveAllDialogs();
								EPGManageDialog epgManageDialog = new EPGManageDialog();
								epgManageDialog.setDialogListener(null);
								epgManageDialog.processCmd(TvConfigTypes.TV_DIALOG_EPG_MANAGE, DialogCmd.DIALOG_SHOW, null);
							}
						}
						return true;
					}
					else
					{
						return false;
					}
				case KeyEvent.KEYCODE_MEDIA_FAVORITES://FAV	
				case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
					if(keyCode == KeyEvent.KEYCODE_MEDIA_FAVORITES && TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_1)
					{
						{
							Log.i("gky",getClass().toString()+"show ChannelEditDialog");
							TvUIControler.getInstance().reMoveAllDialogs();
							if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV
									|| curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV){
								ChannelEditDialog ChannelEditDialog = new ChannelEditDialog();
								ChannelEditDialog.setDialogListener(null);
								ChannelEditDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumChOpType.E_CH_OPTION_FAV);
							}
						}
						return true;
					}
					else if(keyCode == KeyEvent.KEYCODE_MEDIA_FAST_FORWARD && TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_2)
					{
						{
							Log.i("gky",getClass().toString()+"show ChannelEditDialog");
							TvUIControler.getInstance().reMoveAllDialogs();
							if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV
									|| curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV){
								ChannelEditDialog ChannelEditDialog = new ChannelEditDialog();
								ChannelEditDialog.setDialogListener(null);
								ChannelEditDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumChOpType.E_CH_OPTION_FAV);
							}
						}
						return true;
					}
					else
					{
						return false;
					}
				case KeyEvent.KEYCODE_DPAD_CENTER:
				{
					Log.i("gky",getClass().toString()+"show ChannelEditDialog");
					TvUIControler.getInstance().reMoveAllDialogs();
					if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV
							|| curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV){
						ChannelEditDialog ChannelEditDialog = new ChannelEditDialog();
						ChannelEditDialog.setDialogListener(null);
						ChannelEditDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumChOpType.E_CH_OPTION_LIST);
					}
				}
				return true;				
				case KeyEvent.KEYCODE_PROG_RED:// REC.
				{	
					if(TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_1)
					{
						if(TvPluginControler.getInstance().getCommonManager().getCurrentInputSource() == TvEnumInputSource.E_INPUT_SOURCE_DTV)
						{
							TvUIControler.getInstance().reMoveAllDialogs();
							String flag = "record";
							DiskSelectDialog SelectDialog = new DiskSelectDialog();
							SelectDialog.setDialogListener(null);
						    SelectDialog.processCmd(TvConfigTypes.TV_DIALOG_DISK_SELECT, DialogCmd.DIALOG_SHOW, flag);
						}
						
						return true;
					}
					else if(TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_2)
					{
						TvUIControler.getInstance().reMoveAllDialogs();
						Log.d("gky", getClass().toString() + " switchToRadio curSource is "+curSource);
						if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV)
						{
							boolean ret = TvPluginControler.getInstance().getChannelManager().switchToRadio();
							if(ret){
								TvUIControler.getInstance().
									showMniToast(TvContext.context.getResources().getString(R.string.str_tip_switch_to_radio));
							}else{
								TvUIControler.getInstance().
									showMniToast(TvContext.context.getResources().getString(R.string.str_tip_switch_to_dtv));
							}
						return true;
						}
						return false;
					}
					else
					{
						return false;
					}
				}
				case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
					if(TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_2)
					{
						if(TvPluginControler.getInstance().getCommonManager().getCurrentInputSource() == TvEnumInputSource.E_INPUT_SOURCE_DTV)
						{
							TvUIControler.getInstance().reMoveAllDialogs();
							String flag = "record";
							DiskSelectDialog SelectDialog = new DiskSelectDialog();
							SelectDialog.setDialogListener(null);
						    SelectDialog.processCmd(TvConfigTypes.TV_DIALOG_DISK_SELECT, DialogCmd.DIALOG_SHOW, flag);
						}
						
						return true;
					}
					else
					{
						return false;
					}
				case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE://T.shift
				{
					if(TvPluginControler.getInstance().getCommonManager().getCurrentInputSource() == TvEnumInputSource.E_INPUT_SOURCE_DTV)
					{
						TvUIControler.getInstance().reMoveAllDialogs();
						String flag = "timeshitf";
						DiskSelectDialog SelectDialog = new DiskSelectDialog();
						SelectDialog.setDialogListener(null);
					    SelectDialog.processCmd(TvConfigTypes.TV_DIALOG_DISK_SELECT, DialogCmd.DIALOG_SHOW, flag);
					}
					
					return true;
				}
				  //wangxian
				case KeyEvent.KEYCODE_AUDIO_SETTING://Nicam/Audio
				case KeyEvent.KEYCODE_PROG_GREEN:
				case KeyEvent.KEYCODE_MEDIA_NEXT:
					if(keyCode == KeyEvent.KEYCODE_AUDIO_SETTING && TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_1)
					{
						{
							TvUIControler.getInstance().reMoveAllDialogs();
							boolean isSignal = TvPluginControler.getInstance().getCommonManager().isSignalState();
							if(curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV && isSignal){
								MenuItem soundList = new MenuItem(TvConfigDefs.TV_CFG_SOUND_SETTING,"sound_setting",true,new ArrayList<MenuItem>());
								MenuItem MtsItem= new MenuItem(TvConfigDefs.TV_CFG_MTS_MODE,"",true,null);
								MenuItem NicamItem = new MenuItem(TvConfigDefs.TV_CFG_NICAM_MODE,"",true,null);
								soundList.AddChlid(NicamItem);
								soundList.AddChlid(MtsItem);
								TvSettingDialog settingDialog = new TvSettingDialog();
								settingDialog.setDialogListener(null);
								settingDialog.processCmd(TvConfigTypes.TV_DIALOG_SETTING, DialogCmd.DIALOG_SHOW,soundList);
							}else if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV && isSignal){
								MenuItem data = new MenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE_SETTING, "sound_setting", true, null);
								AudioLanguageListViewDialog listViewDialog = new AudioLanguageListViewDialog();
								listViewDialog.setDialogListener(null);
								listViewDialog.processCmd(TvConfigTypes.TV_DIALOG_LISTVIEW, DialogCmd.DIALOG_SHOW, data);
							}
						}
						return true;
					}
					else if(keyCode == KeyEvent.KEYCODE_PROG_GREEN && TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_2)
					{
						TvUIControler.getInstance().reMoveAllDialogs();
						boolean isSignal = TvPluginControler.getInstance().getCommonManager().isSignalState();
						if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV && isSignal){
							MenuItem data = new MenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE_SETTING, "sound_setting", true, null);
							AudioLanguageListViewDialog listViewDialog = new AudioLanguageListViewDialog();
							listViewDialog.setDialogListener(null);
							listViewDialog.processCmd(TvConfigTypes.TV_DIALOG_LISTVIEW, DialogCmd.DIALOG_SHOW, data);
						}
						return true;
					}
					else if(keyCode == KeyEvent.KEYCODE_MEDIA_NEXT && TvPluginControler.getInstance().getCommonManager().getRcType() == SKY_CFG_RC_TYPE.SKY_CFG_RC_MODE_2)
					{
						TvUIControler.getInstance().reMoveAllDialogs();
						boolean isSignal = TvPluginControler.getInstance().getCommonManager().isSignalState();
						if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV && isSignal){
							MenuItem data = new MenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE_SETTING, "sound_setting", true, null);
							AudioLanguageListViewDialog listViewDialog = new AudioLanguageListViewDialog();
							listViewDialog.setDialogListener(null);
							listViewDialog.processCmd(TvConfigTypes.TV_DIALOG_LISTVIEW, DialogCmd.DIALOG_SHOW, data);
						}
						return true;
					}
					else
					{
						return false;
					}
				case KeyEvent.KEYCODE_ALTERNATE://RETURN
				{
					TvPluginControler.getInstance().getChannelManager().returnToPreviousProgram();
					TvUIControler.getInstance().reMoveAllDialogs();
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SourceInfoDialog sourceInfoDialog = new SourceInfoDialog(keyCode);
					sourceInfoDialog.setDialogListener(null);
					sourceInfoDialog.processCmd(TvConfigTypes.TV_DIALOG_SOURCE_INFO, DialogCmd.DIALOG_SHOW, true);
				}
					return true;
				case KeyEvent.KEYCODE_0:
				case KeyEvent.KEYCODE_1:
				case KeyEvent.KEYCODE_2:
				case KeyEvent.KEYCODE_3:
				case KeyEvent.KEYCODE_4:
				case KeyEvent.KEYCODE_5:
				case KeyEvent.KEYCODE_6:
				case KeyEvent.KEYCODE_7:
				case KeyEvent.KEYCODE_8:
				case KeyEvent.KEYCODE_9:
				{
					if(curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV
							|| curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV){
						TvUIControler.getInstance().reMoveAllDialogs();
						TvChannelNumDialog tvChannelNumDialog = new TvChannelNumDialog();
						tvChannelNumDialog.processCmd(null, DialogCmd.DIALOG_SHOW, keyCode);
					}
				}
				return true;
			}
			return false;
		}
	};

	public QuickKeyListener getQuickKeyListener() {
		
		return quickKeyListener;
	}
	
}
