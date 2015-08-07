package com.tv.plugin;

import com.tv.framework.plugin.TvPluginManager.PluginListener;

public class TvPluginCallback implements PluginListener{

	@Override
	public void pluinReady() {
		// TODO Auto-generated method stub
		TvPluginControler.getInstance().init();
	}

}
