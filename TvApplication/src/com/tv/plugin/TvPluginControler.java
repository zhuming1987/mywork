package com.tv.plugin;

import android.util.Log;

import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.data.TvRangeSetData;
import com.tv.framework.data.TvSetData;
import com.tv.framework.data.TvSetDataFactory;
import com.tv.framework.data.TvSwitchSetData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.TvConfigType;
import com.tv.framework.define.TvContext;
import com.tv.framework.plugin.PlatformPlugin;
import com.tv.framework.plugin.TvPluginException;
import com.tv.framework.plugin.TvPluginManager;
import com.tv.framework.plugin.TvPluginParam;
import com.tv.framework.plugin.tvfuncs.ChannelManager;
import com.tv.framework.plugin.tvfuncs.CiManager;
import com.tv.framework.plugin.tvfuncs.CommonManager;
import com.tv.framework.plugin.tvfuncs.EpgManager;
import com.tv.framework.plugin.tvfuncs.HotelManager;
import com.tv.framework.plugin.tvfuncs.NetworkManager;
import com.tv.framework.plugin.tvfuncs.ParentalControlManager;
import com.tv.framework.plugin.tvfuncs.PvrManager;
import com.tv.service.aidl.ISwitchSourceListener;

/**
 * 
 * @author gengkaiyang
 *
 */
public class TvPluginControler
{

	private static TvPluginControler instance = null;
	
	private PlatformPlugin mTvPlugin;
	private ISwitchSourceListener mSwitchSource = null;
	
	private String TAG = "TvPluginControler";
	
	public static TvPluginControler getInstance(){
		if(instance == null)
			instance = new TvPluginControler();
		return instance;
	}
	
	public void init(){
		
		mTvPlugin = TvPluginManager.getInstance(TvContext.context).getTvPlugin();
		Log.i("gky","TvPluginContrler::init:mTvPlugin is " + mTvPlugin);
	}

	public ISwitchSourceListener getSwitchSource() {
		return mSwitchSource;
	}

	public void setSwitchSource(ISwitchSourceListener mSwitchSource) {
		this.mSwitchSource = mSwitchSource;
	}

	public TvSetData getConfigData(String key)
	{
		if(mTvPlugin == null)
		{
			return null;
		}
		
		TvSetData setData = null;
		try 
		{
			Log.e(TAG, "getConfigData() call mTvPlugin.getConfig() key: " + key);
			TvPluginParam retParam = mTvPlugin.getConfig(key, null);
			
			Log.e(TAG, "getConfigData() call createSetData()");
			
			setData = TvSetDataFactory.createSetData(retParam);
			if(setData != null)
			{
				setData.setName(key);
			}
			
			return setData;
		} 
		catch (TvPluginException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean setConfigData(String key, TvSetData data){
		if(mTvPlugin == null)
			return false;
		TvPluginParam param = new TvPluginParam();
		TvConfigType menuItemType = TvConfigType.valueOf(data.getType());
		switch (menuItemType) {
		case TV_CONFIG_ENUM:
			param.add(TvConfigTypes.MENU_SELECT_INDEX_KEY, Integer.valueOf(((TvEnumSetData)data).getCurrentIndex()));
			break;
		case TV_CONFIG_SWITCH:
			param.add(TvConfigTypes.MENU_SELECT_INDEX_KEY, Boolean.valueOf(((TvSwitchSetData)data).isOn()));
			break;
		case TV_CONFIG_RANGE:
			param.add(TvConfigTypes.MENU_SELECT_INDEX_KEY, Integer.valueOf(((TvRangeSetData)data).getCurrent()));
			break;
		case TV_CONFIG_SINGLE:
			break;
		default:
			break;
		}
		TvPluginParam retParam = null;
		try {
			retParam = mTvPlugin.setConfig(key, param);
			Log.i("gky", "TvPluginControler::setConfig:retParam is " + retParam);
		} catch (TvPluginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public CommonManager getCommonManager(){
		if(mTvPlugin != null)
			return mTvPlugin.getCommonManager();
		return null;
	}
	
	public HotelManager getHotelManager()
	{
		return mTvPlugin.getHotelManager();
	}
	
	public ChannelManager getChannelManager(){
		return mTvPlugin.getChannelManager();
	}
	
	public EpgManager getEpgManager(){
		return mTvPlugin.getEpgManager();
	}
	
	public PvrManager getPvrManager(){
		return mTvPlugin.getPvrManager();
	}
	
	public NetworkManager getNetworkManager(){
		return mTvPlugin.getNetworkManager();
	}
	
	public ParentalControlManager getParentalControlManager(){
		return mTvPlugin.getParentalControlManager();
	}
	
	public CiManager getCilManager(){
		return mTvPlugin.getCiManager();
	}
	
	
}
