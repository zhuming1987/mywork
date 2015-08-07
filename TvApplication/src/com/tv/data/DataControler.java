package com.tv.data;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.tv.framework.define.TvConfigDefs;
import com.tv.ui.SettingView.TvSettingItemView.SettingViewChangeListener;
import android.content.Intent;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigDefs.TV_CONFIG_SET;
public abstract class DataControler {
	
	public ArrayList<MenuItem> allList = new ArrayList<MenuItem>();
	
	public ArrayList<MenuItem> quickMenuList = new ArrayList<MenuItem>();
	
	public ArrayList<MenuItem> quickMenuForOther = new ArrayList<MenuItem>();
	
	protected MenuItemFactory menuItemFactory=MenuItemFactory.getInstance();
	
	protected SettingViewChangeListener settingViewChangeListener = null;
	
	protected boolean isInit = false;
	
	private String TAG = "DataControler";
	/**
	 * 父类init数据，然后派生类rebuild数据。
	 */
	public DataControler()
	{
		initData();
		rebuildData(); 
		setInitState(true);
	}

	/**
	 * 初始化公共部分数据
	 */
	public void initData()
	{
		Log.v("gky","initData time is " + System.currentTimeMillis());
		
		MenuItem ThreeDItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_THREED_SETTING, "threed_setting", new ArrayList<MenuItem>());
		MenuItem threeDModeItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_3D_MODE);
		MenuItem threeDDepthItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_3D_DEPTH);
		MenuItem threeDLRItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_3D_LR_SWAP);
		MenuItem threeD3To2Item = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_3D_TO_2D);
		ThreeDItem.AddChlid(threeDModeItem);
		ThreeDItem.AddChlid(threeDDepthItem);
		ThreeDItem.AddChlid(threeDLRItem);
		ThreeDItem.AddChlid(threeD3To2Item);
		/**
		 * 图像设置
		 */
		MenuItem pictureList = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_PICTURE_SETTING,"picture_setting",new ArrayList<MenuItem>());
		MenuItem picturmodeItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_PICTURE_MODE);
		MenuItem brightnessItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_BRIGHTNESS);
		MenuItem constrastItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_CONTRAST);
		MenuItem colorItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_COLOR);
		//MenuItem hueItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_HUE);
		MenuItem ntscHueItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_NTSC_HUE);
		MenuItem sharpnessItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SHARPNESS);
		MenuItem backligtItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_MANUAL_BACKLIGHT_ADJUST);
		MenuItem colortempItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_COLOR_TEMPERATURE);
		MenuItem displaymodeItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_DISPLAY_MODE);
		MenuItem memcItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_MEMC_MODE);
		MenuItem DLCItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_DREAM_PANEL);
		MenuItem DNRItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_DNR);
		MenuItem mpegNRItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_MPEG_NR);
		MenuItem CinemaModeItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_CINEMA_MODE);
		
		pictureList.AddChlid(picturmodeItem);
		pictureList.AddChlid(brightnessItem);
		pictureList.AddChlid(constrastItem);
		pictureList.AddChlid(colorItem);
		pictureList.AddChlid(ntscHueItem);
	
		pictureList.AddChlid(sharpnessItem);
		pictureList.AddChlid(colortempItem);
		pictureList.AddChlid(displaymodeItem);
		pictureList.AddChlid(backligtItem);
		pictureList.AddChlid(memcItem);
		pictureList.AddChlid(DLCItem);
		pictureList.AddChlid(DNRItem);
		pictureList.AddChlid(mpegNRItem);
		pictureList.AddChlid(CinemaModeItem);
		
		/**
		 * 声音设置
		 */
		MenuItem soundList=menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SOUND_SETTING,"sound_setting",new ArrayList<MenuItem>());
		
		MenuItem MtsItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_MTS_MODE);
		MenuItem NicamItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_NICAM_MODE);
		MenuItem audioLanguageItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE);
	    //menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE_SETTING);
		MenuItem soundModeItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SOUND_MODE);
		//MenuItem bassItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_BASS);
		//MenuItem trubassItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TREBLE);
		MenuItem balanceItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_BALANCE);
		
		MenuItem equalizerItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_EQUALIZER_SETTING,"sound_setting",new ArrayList<MenuItem>());
		MenuItem equalizer100Item = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_EQUALIZER_100HZ);
		MenuItem equalizer500Item = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_EQUALIZER_500HZ);
		MenuItem equalizer1KItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_EQUALIZER_1KHZ);
		MenuItem equalizer3KItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_EQUALIZER_3KHZ);
		MenuItem equalizer10KItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_EQUALIZER_10KHZ);
		equalizerItem.AddChlid(equalizer100Item);
		equalizerItem.AddChlid(equalizer500Item);
		equalizerItem.AddChlid(equalizer1KItem);
		equalizerItem.AddChlid(equalizer3KItem);
		equalizerItem.AddChlid(equalizer10KItem);
		
		MenuItem AVLItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_AVL);
		MenuItem surroundItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SURROUND);
		MenuItem spdifItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SOUND_SPDIF);
		//MenuItem subwooferItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SUBWOOFER);
		//MenuItem subwooferVolumeItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SUBWOOFER_VOLUME);
		//MenuItem bbeItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_BBE_SOUND);
		//MenuItem audioOnlyItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SOUND_SEPERATE);
		
		// delete by wangxinjue 2014_11_27 for delete Nicam in MainMenu,
		// because we can use this function by QuickKey
		// and this function is ok,we can add it to MainMenu anytime.
		//soundList.AddChlid(NicamItem);
		//soundList.AddChlid(MtsItem);
		//soundList.AddChlid(audioLanguageItem);
		soundList.AddChlid(soundModeItem);
		//soundList.AddChlid(bassItem);
		//soundList.AddChlid(trubassItem);
		soundList.AddChlid(balanceItem);
		soundList.AddChlid(equalizerItem);
		soundList.AddChlid(AVLItem);
		soundList.AddChlid(surroundItem);
		soundList.AddChlid(spdifItem);
		//soundList.AddChlid(subwooferItem);
		//soundList.AddChlid(subwooferVolumeItem);
		//soundList.AddChlid(bbeItem);
		//soundList.AddChlid(audioOnlyItem);
		
		/**
		 * 频道设置
		 */
		MenuItem Installion=menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_CHANNEL_SETTING,"tv_channel_set",new ArrayList<MenuItem>());
		
		MenuItem autoSearchItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_AUTO_SEARCH,"tv_auto_search",new ArrayList<MenuItem>());
		MenuItem searchTyepATV = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SEARCH_TYPE_ATV);
		MenuItem searchTyepDVBT = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SEARCH_TYPE_DVBT);
		MenuItem searchTyepDVBC = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SEARCH_TYPE_DVBC);
		MenuItem searchTyepDVBS = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SEARCH_TYPE_DVBS);
		MenuItem fiveAtenna = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_FIVE_ATENNA);
		MenuItem startSearch = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_START_AUTO_SEARCH);
		autoSearchItem.AddChlid(searchTyepDVBS);
		autoSearchItem.AddChlid(searchTyepDVBT);
		//autoSearchItem.AddChlid(searchTyepDVBC);
		autoSearchItem.AddChlid(searchTyepATV);
		autoSearchItem.AddChlid(fiveAtenna);
		autoSearchItem.AddChlid(startSearch);
		
		MenuItem manualSearchItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_MANUAL_SEARCH,"tv_manual_search",new ArrayList<MenuItem>());
		MenuItem colorSystemItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TV_COLOR_SYSTEM); 
		MenuItem SoundSystemItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TV_SOUND_SYSTEM);
		MenuItem channelEditItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_CHANNEL_EDIT);
		//MenuItem favoriteItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_FAVORITE_LIST);
		MenuItem lnbSettingItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_LNB_SETTING);
		MenuItem signalInfoItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SIGNAL_INFO);		
		MenuItem ciInformation= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_CI_INFORMATION);
		Installion.AddChlid(autoSearchItem);
		Installion.AddChlid(manualSearchItem);
		Installion.AddChlid(lnbSettingItem);
		Installion.AddChlid(colorSystemItem);
		Installion.AddChlid(SoundSystemItem);
		Installion.AddChlid(channelEditItem);
		// delete by wangxinjue 2014_11_27 for delete favoriteList in MainMenu,
		// because we can use this function by QuickKey,
		// and this function is ok,we can add it to MainMenu anytime.
		//Installion.AddChlid(favoriteItem);
		Installion.AddChlid(signalInfoItem);
		Installion.AddChlid(ciInformation);
		
		/**
		 * PC设置
		 */
		MenuItem PC= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_VGA_SETTING,"vga_setting",new ArrayList<MenuItem>());
		
		MenuItem autoAdjustItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_VGA_AUTO_ADJUST);
		MenuItem H_positionItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_VGA_H_POSITION);
		MenuItem V_positionItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_VGA_V_POSITION);
		MenuItem phaseItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_VGA_PHASE);
		MenuItem frequencyItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_VGA_FREQUENCY);
		
		PC.AddChlid(autoAdjustItem);
		PC.AddChlid(H_positionItem);
		PC.AddChlid(V_positionItem);
		PC.AddChlid(phaseItem);
		PC.AddChlid(frequencyItem);
		
		
		MenuItem setting= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SYSTEM_SETTING,"system_setting",new ArrayList<MenuItem>());
		//语言设置
		MenuItem captionSetItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_CAPTION_SETTING,"system_setting", new ArrayList<MenuItem>());
		MenuItem captionSwitchItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_CAPTION_ONOFF);
		MenuItem ccModeItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_CC_MODE);
		captionSetItem.AddChlid(captionSwitchItem);
		captionSetItem.AddChlid(ccModeItem);
		
		MenuItem OSDLanguagetItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_LANGUAGE,"system_setting",new ArrayList<MenuItem>());
		MenuItem audioLanguageSetItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE_SET, "system_setting", new ArrayList<MenuItem>());
		MenuItem audioLanguagePrimayItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE_PRIMARY_SET);
		MenuItem audioLanguageSecondItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_AUDIO_LANGUAGE_SECOND_SET);
		audioLanguageSetItem.AddChlid(audioLanguagePrimayItem);
		audioLanguageSetItem.AddChlid(audioLanguageSecondItem);
		MenuItem subtitleItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SUBTITLE_LANGUAGE);
		MenuItem subtitleSettingItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SUBTITLE_LANGUAGE_SETTING, "system_setting", new ArrayList<MenuItem>());
		//MenuItem subtitleSwitchItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SUBTITLE_LANGUAGE_SWITCH);
		MenuItem subtitlePrimaryItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SUBTITLE_LANGUAGE_PRIMARY);
		MenuItem subtitleSecondItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SUBTITLE_LANGUAGE_SECONDE);
		//subtitleSettingItem.AddChlid(subtitleSwitchItem);
		subtitleSettingItem.AddChlid(subtitlePrimaryItem);
		subtitleSettingItem.AddChlid(subtitleSecondItem);
		MenuItem TTXlanguageItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TELETEXT_LANGUAGE);
		//MenuItem timeshiftItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TIME_SHIFT);
		
		MenuItem timeListItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TIME,"system_setting",new ArrayList<MenuItem>());
		MenuItem curTimeItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_CURRENT_TIME);
		MenuItem timeZoneItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TIME_ZONE_SETTING);
		MenuItem dayLightItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_DAY_LIGHT_SAVING);
		MenuItem sleepTimerItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_SLEEP_TIMER);
		
		MenuItem offTimeItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_OFF_TIMER,"system_setting",new ArrayList<MenuItem>());
		MenuItem yearItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TIME_YEAR);
		MenuItem monthItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TIME_MONTH);
		MenuItem dayItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TIME_DAY);
		MenuItem hourItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TIME_HOUR);
		MenuItem minuteItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_TIME_MINUTE);
		MenuItem startOffTimeItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_START_OFF_TIME);
		offTimeItem.AddChlid(yearItem);
		offTimeItem.AddChlid(monthItem);
		offTimeItem.AddChlid(dayItem);
		offTimeItem.AddChlid(hourItem);
		offTimeItem.AddChlid(minuteItem);
		offTimeItem.AddChlid(startOffTimeItem);
		
//		MenuItem engerySavingItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_ENGERY_SAVING,"system_setting", new ArrayList<MenuItem>());
		MenuItem autoSleepItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_AUTO_SLEEP);
//		MenuItem noSignalSleepItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_NO_SIGNAL_SLEEP);
//		engerySavingItem.AddChlid(autoSleepItem);
//		engerySavingItem.AddChlid(noSignalSleepItem);
		
		timeListItem.AddChlid(curTimeItem);
		timeListItem.AddChlid(timeZoneItem);
		timeListItem.AddChlid(dayLightItem);
		timeListItem.AddChlid(sleepTimerItem);
		//delete by wangxinjue 2014_11_27 for delete offTime in MainMenu,because SleepTime can replace this function
//		timeListItem.AddChlid(offTimeItem);
		timeListItem.AddChlid(autoSleepItem);
		MenuItem pvrItem= menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_PVR,"system_setting",new ArrayList<MenuItem>());
		MenuItem pvrSelectUSBItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_PVR_SELECT_USB);
		MenuItem pvrTimeShiftSizeItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_PVR_TIMESHIFT_SIZE);
		MenuItem pvrUSBFormatItme = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_PVR_USB_FORMAT);
		MenuItem pvrUSBSpeedItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_PVR_USB_SPEED);
		pvrItem.AddChlid(pvrSelectUSBItem);
		pvrItem.AddChlid(pvrTimeShiftSizeItem);
		pvrItem.AddChlid(pvrUSBFormatItme);
		pvrItem.AddChlid(pvrUSBSpeedItem);
		MenuItem cecMenuListItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_HDMI_CEC_MENU_LIST,"system_setting",new ArrayList<MenuItem>());
		MenuItem cecSwitchItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_HDMI_ONOFF_CEC);
		MenuItem cecAutoStandbyItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_HDMI_ONOFF_CEC_AUTO_STANDBY);
		MenuItem cecDeviceListItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_HDMID_CEC_DEVICE_LIST_SETTING);
		cecMenuListItem.AddChlid(cecSwitchItem);
		cecMenuListItem.AddChlid(cecAutoStandbyItem);
		cecMenuListItem.AddChlid(cecDeviceListItem);
		MenuItem menuShowTimeItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_MENU_SHOW_TIME);
		MenuItem menuStickeDemoItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_STICKER_DEMO);
		MenuItem resetItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_RECOVERY);
		setting.AddChlid(captionSetItem);
		setting.AddChlid(OSDLanguagetItem);
		setting.AddChlid(audioLanguageSetItem);
		setting.AddChlid(subtitleSettingItem);
		setting.AddChlid(TTXlanguageItem);
		setting.AddChlid(timeListItem);
		//setting.AddChlid(timeshiftItem);
		setting.AddChlid(pvrItem);
		setting.AddChlid(cecMenuListItem);
		setting.AddChlid(menuShowTimeItem);
		setting.AddChlid(menuStickeDemoItem);
		setting.AddChlid(resetItem);
		
		//童锁设置
		MenuItem Lock = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_LOCK_SETTING, "lock_setting", new ArrayList<MenuItem>());
		MenuItem systemLock =menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_LOCK_SYSTEM_LOCK);
		MenuItem changePassword =menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_LOCK_CHANGE_PASSWORD);
		MenuItem ageList =menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_LOCK_AGE_LIST);
		MenuItem channelList =menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_LOCK_CHANNEL_LIST);
		MenuItem panelLock =menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_PANEL_LOCK);
				
		Lock.AddChlid(systemLock);	
		Lock.AddChlid(changePassword);
		Lock.AddChlid(ageList);
		Lock.AddChlid(channelList);
		Lock.AddChlid(panelLock);
		
		allList.add(ThreeDItem);
		allList.add(pictureList);
		allList.add(soundList);
		allList.add(Installion);
		allList.add(setting);
		allList.add(PC);
		allList.add(Lock);
		
		MenuItem quickItem = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_QUICK_MENU_SETTING, "system_setting", new ArrayList<MenuItem>());
		quickItem.AddChlid(threeDModeItem);
		quickItem.AddChlid(displaymodeItem);
		quickItem.AddChlid(picturmodeItem);
		quickItem.AddChlid(soundModeItem);
		quickItem.AddChlid(backligtItem);
		quickItem.AddChlid(sleepTimerItem);
		quickItem.AddChlid(memcItem);
		quickMenuList.add(quickItem);
		
		MenuItem quickItemForOther = menuItemFactory.createMenuItem(TvConfigDefs.TV_CFG_QUICK_MENU_SETTING, "system_setting", new ArrayList<MenuItem>());
		quickItemForOther.AddChlid(threeDModeItem);
		quickItemForOther.AddChlid(pictureList);
		quickItemForOther.AddChlid(soundList);
		quickItemForOther.AddChlid(sleepTimerItem);
		quickMenuForOther.add(quickItemForOther);
		Log.i("gky", "initData end time is " + System.currentTimeMillis());
	}
	
	/**
	 * 重构initData之后的数据，完成派生类的差异数据
	 */
	public abstract void rebuildData();
	
	
	public void setInitState(boolean isInit)
	{
		this.isInit=isInit;
	}

	public boolean isInit()
	{
		return isInit;
	}
	public ArrayList<MenuItem> getAllList()
	{
		for (MenuItem menuItem : allList) {
			menuItem.update();
		}
		return allList;
	}
	
	public ArrayList<MenuItem> getQuickMenuList()
	{
		return quickMenuList;
	}
	
	public ArrayList<MenuItem> getQuickMenuForOther()
	{
		return quickMenuForOther;
	}
	
	public MenuItem IDtoItem(String id)
	{
		MenuItemFactory menuItemFactory=MenuItemFactory.getInstance();
		return menuItemFactory.IDtoItem.get(id);
	}
	
	static HashMap<String, List<String>> ResetItems = new HashMap<String, List<String>>();
    static {
    	//picture mode联动
    	List<String> ReleatePicMode = new ArrayList<String>();
    	ReleatePicMode.add(TvConfigDefs.TV_CFG_BRIGHTNESS);
    	ReleatePicMode.add(TvConfigDefs.TV_CFG_CONTRAST);
    	ReleatePicMode.add(TvConfigDefs.TV_CFG_COLOR);
    	//ReleatePicMode.add(TvConfigDefs.TV_CFG_HUE);
    	ReleatePicMode.add(TvConfigDefs.TV_CFG_NTSC_HUE);
    	ReleatePicMode.add(TvConfigDefs.TV_CFG_SHARPNESS);
    	//ReleatePicMode.add(TvConfigDefs.TV_CFG_MANUAL_BACKLIGHT_ADJUST);
    	//ReleatePicMode.add(TvConfigDefs.TV_CFG_COLOR_TEMPERATURE);
    	ResetItems.put(TvConfigDefs.TV_CFG_PICTURE_MODE, ReleatePicMode);
    	
    	//sound mode联动
    	List<String> ReleateSoundMode = new ArrayList<String>();
    	//ReleateSoundMode.add(TvConfigDefs.TV_CFG_BASS);
    	//ReleateSoundMode.add(TvConfigDefs.TV_CFG_TREBLE);
    	//ReleateSoundMode.add(TvConfigDefs.TV_CFG_EQUALIZER_SETTING);
    	ReleateSoundMode.add(TvConfigDefs.TV_CFG_EQUALIZER_100HZ);
    	ReleateSoundMode.add(TvConfigDefs.TV_CFG_EQUALIZER_500HZ);
    	ReleateSoundMode.add(TvConfigDefs.TV_CFG_EQUALIZER_1KHZ);
    	ReleateSoundMode.add(TvConfigDefs.TV_CFG_EQUALIZER_3KHZ);
    	ReleateSoundMode.add(TvConfigDefs.TV_CFG_EQUALIZER_10KHZ);
    	ResetItems.put(TvConfigDefs.TV_CFG_SOUND_MODE, ReleateSoundMode);
    	
    	List<String> releateThreeD = new ArrayList<String>();
    	releateThreeD.add(TvConfigDefs.TV_CFG_3D_DEPTH);
    	releateThreeD.add(TvConfigDefs.TV_CFG_3D_LR_SWAP);
    	ResetItems.put(TvConfigDefs.TV_CFG_3D_MODE, releateThreeD);
    	
    	List<String> releate3Dto2D = new ArrayList<String>();
    	releate3Dto2D.add(TvConfigDefs.TV_CFG_3D_LR_SWAP);
    	ResetItems.put(TvConfigDefs.TV_CFG_3D_TO_2D, releate3Dto2D);
    	
    	List<String> releateBalanceToSurround = new ArrayList<String>();
    	releateBalanceToSurround.add(TvConfigDefs.TV_CFG_BALANCE);
    	ResetItems.put(TvConfigDefs.TV_CFG_SURROUND, releateBalanceToSurround);
    	
    	List<String> releateHDMICEC = new ArrayList<String>();
    	releateHDMICEC.add(TvConfigDefs.TV_CFG_HDMID_CEC_DEVICE_LIST_SETTING);
    	releateHDMICEC.add(TvConfigDefs.TV_CFG_HDMI_ONOFF_CEC_AUTO_STANDBY);
    	ResetItems.put(TvConfigDefs.TV_CFG_HDMI_ONOFF_CEC, releateHDMICEC);
    	
    	ArrayList<String> pcAutoJustItem = new ArrayList<String>();
    	pcAutoJustItem.add(TvConfigDefs.TV_CFG_VGA_H_POSITION);
    	pcAutoJustItem.add(TvConfigDefs.TV_CFG_VGA_V_POSITION);
    	pcAutoJustItem.add(TvConfigDefs.TV_CFG_VGA_PHASE);
    	pcAutoJustItem.add(TvConfigDefs.TV_CFG_VGA_FREQUENCY);
    	ResetItems.put(TvConfigDefs.TV_CFG_VGA_AUTO_ADJUST, pcAutoJustItem);
    	
		
    	ArrayList<String> SystemLockItem = new ArrayList<String>();
    	SystemLockItem.add(TvConfigDefs.TV_CFG_LOCK_CHANGE_PASSWORD);
    	SystemLockItem.add(TvConfigDefs.TV_CFG_LOCK_AGE_LIST);
    	SystemLockItem.add(TvConfigDefs.TV_CFG_LOCK_CHANNEL_LIST);
    	SystemLockItem.add(TvConfigDefs.TV_CFG_PANEL_LOCK);
    	ResetItems.put(TvConfigDefs.TV_CFG_LOCK_SYSTEM_LOCK, SystemLockItem);
    }
    
    public ArrayList<MenuItem> getMainMenuData(){
    	for (MenuItem menuItem : allList) {
			menuItem.update();
		}
    	return allList;
    }
    
    public MenuItem getMenuItemById(String key){
    	Long s = System.currentTimeMillis();
    	int dataIndex = -1;
		TV_CONFIG_SET menuConfigSet = TV_CONFIG_SET.valueOf(key);
		switch (menuConfigSet) {
		case TV_CFG_THREED_SETTING:
			dataIndex = 0;
			break;
		case TV_CFG_PICTURE_SETTING:
			dataIndex = 1;
			break;
		case TV_CFG_SOUND_SETTING:
			dataIndex = 2;
			break;
		case TV_CFG_CHANNEL_SETTING:
			dataIndex = 3;
			break;
		case TV_CFG_SYSTEM_SETTING:
			dataIndex = 4;
			break;
		case TV_CFG_VGA_SETTING:
			dataIndex = 5;
			break;
		case TV_CFG_LOCK_SETTING:
			dataIndex = 6;
			break;
		default:
			dataIndex = -1;
			break;
		}
		if(dataIndex == -1)
			return null;
		MenuItem item = allList.get(dataIndex);
		/*for (MenuItem menuItem : item.getChlidList()) {
			menuItem.update();
		}*/
		long e = System.currentTimeMillis();
		Log.i(TAG, getClass().getSimpleName() + ":getMenuItemById take "+(e-s)+"ms");
		return item;
    }
    
    public MenuItemChangeListener menuItemChangeListener = new MenuItemChangeListener() {
		@Override
		public void OnMenuItemChangeListener(String key,
				SettingViewChangeListener listener) {
			// TODO Auto-generated method stub
			Log.i("gky", "DataControler::OnMenuItemChangeListener: key is " + key);
			settingViewChangeListener = listener;
			syncReleateItemsByKey(key);
		}
	};
	
	public MenuItemChangeListener getMenuItemChangeListener() {
		return menuItemChangeListener;
	}

	public void syncReleateItemsByKey(String key){
    	Log.d("gky", "syncReleateItemsByKey=" + key);
    	List<String> items = ResetItems.get(key);
    	ArrayList<MenuItem> syncList = new ArrayList<MenuItem>();
    	if(items != null){
    		for(int i = 0;i < items.size();i++){
    			IDtoItem(items.get(i)).update();
    			syncList.add(IDtoItem(items.get(i)));
    		}
    	}
    	if(settingViewChangeListener != null)
    		settingViewChangeListener.OnSettingViewChangeListener(syncList);
    }
	
	/**
	 * UI刷新回调接口
	 * @author gky
	 *
	 */
	public interface MenuItemChangeListener{
		public void OnMenuItemChangeListener(String key,SettingViewChangeListener listener);
	}
}
