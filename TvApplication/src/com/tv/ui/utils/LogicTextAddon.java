package com.tv.ui.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

import android.util.Log;

public class LogicTextAddon {
	private static LogicTextAddon instance = null;
	public static LogicTextAddon getInstance(){
		synchronized (LogicTextAddon.class) {
			if(instance == null){
				instance = new LogicTextAddon();
			}
			return instance;
		}
	}
	
	public LogicTextAddon(){
        File file = new File("");
        if (!file.exists())
        {
            Log.i("gky","the lang folder " + file + " is not exist.");
        }
        texts = new HashMap<String, String>();
        resLoader = new SkyResLoader();
        setDefaultLang();
	}
	
    private static final String langDir = "skydir/config/product/addon";
    private static String langFile = null;
    private static String currentLang = "chinese";
    private HashMap<String, String> texts;
    private SkyResLoader resLoader;
    
    private boolean loadTexts()
    {
        texts.clear();
        try{
            resLoader.load(langFile, "string", texts);
            Log.d("gky", "[" + texts.size() + "] texts loaded");
        } catch (Exception e){
            Log.i("gky","Load lang[" + langFile + "]failed:" + e.toString());
            return false;
        }
        return true;
    }
    
    public String getText(String res_id)
    {
    	if(res_id == null){
    		return null;
    	}
        String text = null;
        text = texts.get(res_id);
        if (text == null){
        }
        return text;
    }
    
    private void setDefaultLang(){
        String sysLang = Locale.getDefault().getLanguage();
        Log.d("gky", "syslang=" + sysLang);
        if (sysLang.equals("cn")){
            setLang("chinese");
        }else if (sysLang.equals("en")){
            setLang("english");
        }else
        {
            setLang("chinese");
        }
    }
    
    public boolean setLang(String lang)
    {
        langFile = langDir + File.separator + lang + ".xml";
        currentLang = lang;
        return loadTexts();
    }
}
