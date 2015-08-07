package com.tv.ui.utils;

import java.lang.reflect.Field;

import com.tv.app.R;
import com.tv.app.R.string;

import android.content.Context;
import android.util.Log;

public class LogicTextResource {
	private static LogicTextResource instance = null;
	public static LogicTextResource getInstance(Context cxt){
		synchronized (LogicTextResource.class) {
			if(instance == null){
				instance = new LogicTextResource(cxt);
			}
		}
		return instance;
	}
	
	Context context = null;
	Field[] fields = null;
	string obj = new string();
	public LogicTextResource(){}
	public LogicTextResource(Context cxt){
		this.context = cxt;
	}
	
	public void init(){
		Class resClazz = R.string.class;
		fields = resClazz.getDeclaredFields();
	}
	
	public String getText(String resKey){
		if(resKey == null){
			Log.d("gky", "getText resid=null");
			return null;
		}
		if(fields == null){
			init();
		}
		int resId = 0;
		try{
			resId = getResidByReskey(resKey);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(resId == 0){
			Log.d("gky", "not found " + resKey + " in xml");
			return resKey;
		}
		
		if(context != null){
			String text = context.getString(resId);
			return text;
		}
		return null;
	}
	
	public String getText1(String resKey){
		if(resKey == null){
			Log.d("gky", "getText resid=null");
			return null;
		}
		if(fields == null){
			init();
		}
		int resId = 0;
		try{
			resId = getResidByReskey(resKey);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(resId == 0){
			Log.d("gky", "not found " + resKey + " in xml");
			return resKey;
		}
		
		if(context != null){
			String text = context.getString(resId);
			return text;
		}
		return null;
	}
	
	public String getText(int resid){
		if(context != null){
			String text = context.getString(resid);
			return text;
		}
		return null;
	}
	
	private boolean contains(String resid){
		if(fields != null){
			for (Field field: fields) {
				if(field.getName().equals(resid)){
					return true;
				}
			}		
		}
		return false;
	} 
	
	private int getResidByReskey(String resKey){
		try {
			if(resKey != null){
				Field field = getField(resKey);
				if(field != null){
					return field.getInt(obj);
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	
	private Field getField(String resKey){
		if(resKey != null){
			if(fields != null){
				for (Field field: fields) {
					if(field.getName().equals(resKey)){
						return field;
					}
				}		
			}
		}
		return null;
	}
	
	public void setContext(Context cxt){
		this.context = cxt;
	}
	
}
