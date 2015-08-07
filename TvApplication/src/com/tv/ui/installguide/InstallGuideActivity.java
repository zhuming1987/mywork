package com.tv.ui.installguide;

import java.util.ArrayList;
import java.util.List;

import com.skyworth.config.SkyConfigDefs.SKY_CFG_TV_SYSTEM_TYPE;
import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.data.TvSetData;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvSearchTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ChannelSearch.ATVChannelSearchDialog;
import com.tv.ui.ChannelSearch.DTVChannelSearchDialog;
import com.tv.ui.LNBSetting.LNBSettingDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.network.NetworkSetupDialog;
import com.tv.ui.utils.LogicTextResource;
import com.tv.app.R;
import com.tv.app.TvActivity;
import com.tv.app.TvApplication;
import com.tv.app.TvUIControler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


@SuppressWarnings("deprecation")
public class InstallGuideActivity extends Activity{
	private int SIZE_OUTSIDE = 7;
	public LinearLayout rootLayout;
	public TextView welcomeText;
	public RelativeLayout mainLayout;
	public LinearLayout leftListLayout;
	public TextView leftViewLanguage;
	public TextView leftViewTimeZone;
	public TextView leftViewNetWork;
	public TextView leftViewSearchMode;
	public int leftFocusIndex = 0; 
	public LinearLayout previousLayout;
	public ImageView previousImage;
	public TextView previousVal;
	public LinearLayout nextLayout;
	public ImageView nextImage;
	public TextView nextVal;
	public LinearLayout centerLayout;
	public Button setNetwork;
	public LinearLayout searchLayout;
	public LinearLayout centerList;
	private AlwaysMarqueeTextView[] outside_items = new AlwaysMarqueeTextView[SIZE_OUTSIDE];
	private ImageView[] checkSearchModes = new ImageView[SIZE_OUTSIDE];
	private LinearLayout[] searchModeLayouts = new LinearLayout[SIZE_OUTSIDE];
	private TextView searchModeHints;
	private String outside_showStr[] = null;
	private ArrayList<String> outside_strings = new ArrayList<String>();  //当前页面List数组
	private float[] alpha_outside = {  0.25f, 0.4f, 0.55f, 0.7f, 0.55f, 0.4f,0.25f,}; //导航页面显示的渐变透明度
	private int outside_nextstep = 0;
	private int outside_prestep = 0;
	private int current_index = 0;  //当前页面的选中item的Index
	private TvSetData dataLanguage;   //Plugin返回的语言列表
	private TvSetData timeZones;
	private static SKY_CFG_TV_SYSTEM_TYPE tvType;     //TV制式类别值
	public static InstallGuideActivity ContextTemp;
    private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dipDiv = TvUIControler.getInstance().getDipDiv();
    private boolean ATVSearchStatus = false, DVBSSearchStatus = false, DVBTSearchStatus = false, DVBCSearchStatus = false, fiveAntennaStatus = false;
    
   //获取是否有TimeZone选择
	public int hasTimeZone()
	{ 
		TvSetData timeZone = TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_TIME_ZONE);
		if(timeZone == null)
			return 0;
		else
			return 1;
	}
	
	//获取当前5V选项是否显示
	private int isShowfiveAntenna()
	{
		TvEnumSetData currentFiveAntenna = (TvEnumSetData)TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_FIVE_ATENNA);
		if(currentFiveAntenna.isHide())
		{
			return 0;
		}
		else
		{
			return 1;
		}
	}
	
	//根据字符串获取状态
	private void getSearchStatus(int curIndex, String curItemStr)
	{
		Log.d("wangxian", "curIndex == " + curIndex + "_____curItemStr = " + curItemStr);
		switch(tvType)
		{
		case SKY_CFG_TV_SYSTEM_PAL_DVBT_DVBT2:
		case SKY_CFG_TV_SYSTEM_PAL_DVBS_DVBS2:
			if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_SEARCH_TYPE_ATV)))
			{
				if(ATVSearchStatus)
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				else
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
			}
			else if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_SEARCH_TYPE_DVBT)))
			{
				if(DVBTSearchStatus)
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				else
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
			}
			else if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_SEARCH_TYPE_DVBC)))
			{
				if(DVBCSearchStatus)
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				else
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
			}
			else if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_SEARCH_TYPE_DVBS)))
			{
				if(DVBSSearchStatus)
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				else
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
			}
			else if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_FIVE_ATENNA)))
			{
				if(fiveAntennaStatus)
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				else
				{
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
			}
			break;
		default:
			break;
		}
	}
	
	//根据字符串设置on/off状态
	private void setSearchStatus(int curIndex, String curItemStr)
	{
		Log.d("wangxian", "curIndex == " + curIndex + "_____curItemStr = " + curItemStr);
		switch(tvType)
		{
		case SKY_CFG_TV_SYSTEM_PAL_DVBT_DVBT2:
		case SKY_CFG_TV_SYSTEM_PAL_DVBS_DVBS2:
			if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_SEARCH_TYPE_ATV)))
			{
				if(ATVSearchStatus)
				{
					ATVSearchStatus = false;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
				else
				{
					ATVSearchStatus = true;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				TvPluginControler.getInstance().getChannelManager().setSearchATVStatus(ATVSearchStatus);
			}
			else if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_SEARCH_TYPE_DVBT)))
			{
				if(DVBTSearchStatus)
				{
					DVBTSearchStatus = false;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
				else
				{
					DVBTSearchStatus = true;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				TvPluginControler.getInstance().getChannelManager().setSearchDVBTStatus(DVBTSearchStatus);
			}
			else if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_SEARCH_TYPE_DVBC)))
			{
				if(DVBCSearchStatus)
				{
					DVBCSearchStatus = false;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
				else
				{
					DVBCSearchStatus = true;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				TvPluginControler.getInstance().getChannelManager().setSearchDVBCStatus(DVBCSearchStatus);
			}
			else if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_SEARCH_TYPE_DVBS)))
			{
				if(DVBSSearchStatus)
				{
					DVBSSearchStatus = false;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
				else
				{
					DVBSSearchStatus = true;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				TvPluginControler.getInstance().getChannelManager().setSearchDVBSStatus(DVBSSearchStatus);
			}
			else if(curItemStr.equals(LogicTextResource.getInstance(this).getText(R.string.TV_CFG_FIVE_ATENNA)))
			{
				if(fiveAntennaStatus)
				{
					fiveAntennaStatus = false;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_uncheck);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_uncheck);
					}
				}
				else
				{
					fiveAntennaStatus = true;
					if(curIndex == 3)
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_focus_check);
					}
					else
					{
						checkSearchModes[curIndex].setBackgroundResource(R.drawable.searchmode_unfocus_check);
					}
				}
				TvEnumSetData currentFiveAntenna = new TvEnumSetData();
				if(fiveAntennaStatus)
				{
					currentFiveAntenna.setCurrentIndex(1);
				}
				else
				{
					currentFiveAntenna.setCurrentIndex(0);
				}
				TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_FIVE_ATENNA, currentFiveAntenna);
			}
			break;
		default:
			break;
		}
	}
	
	//获取搜台开关及5V状态
	private void searchStatus()
	{
		switch(tvType)
		{
		   case SKY_CFG_TV_SYSTEM_PAL_DVBT_DVBT2:
		   case SKY_CFG_TV_SYSTEM_PAL_DVBS_DVBS2:
			   ATVSearchStatus = TvPluginControler.getInstance().getChannelManager().getSearchATVStatus();
			   DVBTSearchStatus = TvPluginControler.getInstance().getChannelManager().getSearchDVBTStatus();
			   DVBCSearchStatus = TvPluginControler.getInstance().getChannelManager().getSearchDVBCStatus();
			   DVBSSearchStatus = TvPluginControler.getInstance().getChannelManager().getSearchDVBSStatus();
			   if(isShowfiveAntenna() == 1)
			   {
				   TvEnumSetData currentFiveAntenna = (TvEnumSetData)TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_FIVE_ATENNA);
				   if(currentFiveAntenna.getCurrentIndex() == 1)
				   {
					   fiveAntennaStatus = true; 
				   }
				   else
				   {
					   fiveAntennaStatus = false;
				   }
			   }
			   Log.d("wangxian", "ATVSearchStatus = " + ATVSearchStatus + "____DVBTSearchStatus = " + DVBTSearchStatus + "_____DVBCSearchStatus = " + DVBCSearchStatus);
			   Log.d("wangxian", "DVBSSearchStatus = " + DVBSSearchStatus + "____DVBSSearchStatus = " + DVBSSearchStatus);
			   break;
		   default:
			   break;
	   
		}
		
	}
	
	//获取当前语言的显示字符串
	private String getCurLanguageString()
	{
		return LogicTextResource.getInstance(TvContext.context).getText(((TvEnumSetData)dataLanguage).getCurrent());
	}
	
	//获取当前语言的index
	private int getCurLanguageIndex()
	{
		dataLanguage = TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_OSD_LANGUAGE);
		return ((TvEnumSetData)dataLanguage).getCurrentIndex();
	}
	
	//获取所有语言的显示字符串
	private void getLanguageStrings()
	{
		
		dataLanguage = TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_OSD_LANGUAGE);
	    for (String str : ((TvEnumSetData)dataLanguage).getEnumList()) 
	    {
	    	 outside_strings.add(LogicTextResource.getInstance(TvContext.context).getText(str));
	    }
  
	}
	
	//获取当前时区的显示字符串
	private String getCurTimeZoneString()
	{
		return LogicTextResource.getInstance(TvContext.context).getText(((TvEnumSetData)timeZones).getCurrent());
	}
	
	//获取当前时区的index
	private int getCurTimeZoneIndex()
	{
		timeZones = TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_TIME_ZONE);
		return ((TvEnumSetData)timeZones).getCurrentIndex();
	}
	
	//获取所有时区的显示字符串
	private void getTimeZoneStrings()
	{
		timeZones = TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_TIME_ZONE);
		for (String str : ((TvEnumSetData)timeZones).getEnumList()) 
		{
	    	 outside_strings.add(LogicTextResource.getInstance(TvContext.context).getText(str));
	    }
	}
	
	//获取搜台方式的显示字符串
	private void getSearchModeStrings()
	{
		searchStatus();
		for(int i = 0; i < SIZE_OUTSIDE; i++)
		{
			checkSearchModes[i].setBackground(null);
		}
	    switch(tvType)
	    {
	    case SKY_CFG_TV_SYSTEM_PAL_DVBT_DVBT2:
		case SKY_CFG_TV_SYSTEM_PAL_DVBS_DVBS2:
			outside_strings.add(LogicTextResource.getInstance(ContextTemp).getText(R.string.TV_CFG_SEARCH_TYPE_ATV));
		    outside_strings.add(LogicTextResource.getInstance(ContextTemp).getText(R.string.TV_CFG_SEARCH_TYPE_DVBT));
//		    outside_strings.add(LogicTextResource.getInstance(ContextTemp).getText(R.string.TV_CFG_SEARCH_TYPE_DVBC));
		    outside_strings.add(LogicTextResource.getInstance(ContextTemp).getText(R.string.TV_CFG_SEARCH_TYPE_DVBS));
		    
		    if(isShowfiveAntenna() == 1)
		    {
			    outside_strings.add(LogicTextResource.getInstance(ContextTemp).getText(R.string.TV_CFG_FIVE_ATENNA));
		    }
		    break;
		default:
			outside_strings.add(LogicTextResource.getInstance(ContextTemp).getText(R.string.str_scan_atv));
			outside_strings.add(LogicTextResource.getInstance(ContextTemp).getText(R.string.str_scan_dtv));
		    outside_strings.add(LogicTextResource.getInstance(ContextTemp).getText(R.string.str_scan_atv_dtv));
		    break;
	   }
	}
	
	//开机导航设置完成后将开机导航标志设置为false
	private void setInstallGuideFlag()
	{
		TvPluginControler.getInstance().getCommonManager().setInstallGuideFlag(false);
	}
	
	private String[] outside_moveNext(List<String> strings, int move_count)
	{
        String str[] = new String[7];
        for (int i = 0; i <= 3; i++)
        {
            if (i + move_count < strings.size())
            {
                str[i] = strings.get(i + move_count);
            } else
            {
                str[i] = null;
            }
        }
        for (int i = 4; i <= 6; i++)
        {
            if (6 - i >= move_count)
            {
                str[i] = null;
            } else
            {
                str[i] = strings.get(i - 6 + move_count - 1);
            }
        }
        return str;
	 }
	 
	 public void setOutside_items()
	 {
	    for (int i = 0; i < 3; i++)
        {
            outside_items[i].setText(outside_showStr[i + 4]);
            if((hasTimeZone() != 0 && leftFocusIndex == 3) || (hasTimeZone() == 0 && leftFocusIndex == 2))
            {
	            if(outside_showStr[i + 4] == null)
	            {
	            	checkSearchModes[i].setBackground(null);
	            }
	            else
	            {
	            	getSearchStatus(i, outside_showStr[i + 4]);
	            }
            }
        }
	    
	    for (int i = 3; i < SIZE_OUTSIDE; i++)
        {
            outside_items[i].setText(outside_showStr[i - 3]);
            if((hasTimeZone() != 0 && leftFocusIndex == 3) || (hasTimeZone() == 0 && leftFocusIndex == 2))
            {
	            if(outside_showStr[i - 3] == null)
	            {
	            	checkSearchModes[i].setBackground(null);
	            }
	            else
	            {
	            	getSearchStatus(i, outside_showStr[i - 3]);
	            }
            }
        }
	 }
	 
	private void initListData()
	{
		getLanguageStrings();
		
		for (int i = 0; i < SIZE_OUTSIDE; i++)
        {	
			searchModeLayouts[i] = new LinearLayout(this);
			searchModeLayouts[i].setOrientation(LinearLayout.HORIZONTAL);
			searchModeLayouts[i].setGravity(Gravity.CENTER);
			
			outside_items[i] = new AlwaysMarqueeTextView(this);
			outside_items[i].setGravity(Gravity.CENTER);
            
			checkSearchModes[i] = new ImageView(this);
			LinearLayout.LayoutParams mCheckImageParams = new LinearLayout.LayoutParams(  
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			mCheckImageParams.gravity = Gravity.CENTER_VERTICAL;
			mCheckImageParams.setMargins((int)(30 / div), 0, (int)(100 / div), 0);
			
			checkSearchModes[i].setLayoutParams(mCheckImageParams);
            if (i == 3)
            {
            	outside_items[i].setTextSize((int)(58 / dipDiv));
                outside_items[i].setEllipsize(TruncateAt.valueOf("MARQUEE"));
    			outside_items[i].setTextColor(Color.WHITE);
    			outside_items[i].setLayoutParams(new LinearLayout.LayoutParams((int)(500 / div), (int)(80 / div)));
    			searchModeLayouts[i].setBackgroundResource(R.drawable.list_up);
    			outside_items[i].setEnabled(true);
    			outside_items[i].setClickable(true);
    			
            } else
            {
            	outside_items[i].setEllipsize(TruncateAt.valueOf("END"));
            	outside_items[i].setTextSize((int)(46 / dipDiv));
    			outside_items[i].setTextColor(Color.BLACK);
    			outside_items[i].setLayoutParams(new LinearLayout.LayoutParams((int)(500 / div), (int)(60 / div)));
            }
            outside_items[i].setSingleLine(true);         
            outside_items[i].setAlpha(alpha_outside[i]);
       }
		for (int i = 0; i < SIZE_OUTSIDE; i++)
		{
			searchModeLayouts[i].addView(outside_items[i]);
			searchModeLayouts[i].addView(checkSearchModes[i]);
		    centerList.addView(searchModeLayouts[i]);
		    checkSearchModes[i].setVisibility(View.GONE);
		}
		
		outside_showStr = outside_moveNext(outside_strings, getCurLanguageIndex());
		current_index = getCurLanguageIndex();
		setOutside_items();
	}
	
	public void refreshList()
	{
		outside_showStr = outside_moveNext(outside_strings, current_index);
		setOutside_items();
	}
	
	public void initView()
	{
		rootLayout = new LinearLayout(this);
		rootLayout.setLayoutParams(new LinearLayout.LayoutParams(  
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		rootLayout.setBackgroundResource(R.drawable.bg_1);  
		
		welcomeText = new TextView(this);  
		LinearLayout.LayoutParams mRootLayoutParams = new LinearLayout.LayoutParams(  
                LayoutParams.MATCH_PARENT, 170);
		welcomeText.setPadding((int)(100 / div), (int)(80 / div), 0, 0);
		welcomeText.setTextSize((int)(70 / dipDiv));
		welcomeText.setTextColor(Color.argb((int)(179 / div), 0, 0, 0));
		welcomeText.setText(R.string.install_guide_welcome_message);  
		rootLayout.addView(welcomeText,mRootLayoutParams);
        
		mainLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams mMainLayoutParams = new RelativeLayout.LayoutParams(  
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);	
		rootLayout.addView(mainLayout, mMainLayoutParams);
		
		leftListLayout = new LinearLayout(this);
		RelativeLayout.LayoutParams mLeftLayoutParams = new RelativeLayout.LayoutParams(  
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		leftListLayout.setOrientation(LinearLayout.VERTICAL);
		leftListLayout.setPadding((int)(100 / div), 0, 0, 0);
		leftListLayout.setId(1);

		leftViewLanguage = new TextView(this);
		leftViewLanguage.setPadding(0, (int)(30 / div), 0, 0);
		leftViewLanguage.setTextSize((int)(36 / dipDiv));
		leftViewLanguage.setTextColor(Color.argb(179, 255, 255, 255));
		leftViewLanguage.setText(R.string.left_item_language);
		leftFocusIndex = 0;
		
		leftViewTimeZone = new TextView(this);
		leftViewTimeZone.setPadding(0, (int)(30 / div), 0, 0);
		leftViewTimeZone.setTextSize((int)(30 / dipDiv));
		leftViewTimeZone.setText(R.string.left_item_timezone);
		leftViewTimeZone.setTextColor(Color.argb(127, 0, 0, 0));
		if(hasTimeZone() == 0){
			leftViewTimeZone.setVisibility(View.GONE);
		}
		
		leftViewNetWork = new TextView(this);
		leftViewNetWork.setPadding(0, (int)(30 / div), 0, 0);
		leftViewNetWork.setTextSize((int)(30 / dipDiv));
		leftViewNetWork.setText(R.string.left_item_network);
		leftViewNetWork.setTextColor(Color.argb(127, 0, 0, 0));
		
		leftViewSearchMode = new TextView(this);
		leftViewSearchMode.setPadding(0, (int)(30 / div), 0, 0);
		leftViewSearchMode.setTextSize((int)(30 / dipDiv));
		leftViewSearchMode.setText(R.string.left_item_searchmode);
		leftViewSearchMode.setTextColor(Color.argb(127, 0, 0, 0));
		
		leftListLayout.addView(leftViewLanguage);
		leftListLayout.addView(leftViewTimeZone);
		leftListLayout.addView(leftViewNetWork);
		leftListLayout.addView(leftViewSearchMode);
		mainLayout.addView(leftListLayout, mLeftLayoutParams);
		
		previousLayout = new LinearLayout(this);
		RelativeLayout.LayoutParams mPreviousLayoutParams = new RelativeLayout.LayoutParams(  
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mPreviousLayoutParams.addRule(RelativeLayout.BELOW, 1);
		mPreviousLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		mPreviousLayoutParams.addRule(RelativeLayout.ALIGN_LEFT, 1);
		mPreviousLayoutParams.topMargin = (int)(115/div);
		previousLayout.setPadding((int)(40 / div), 0, 0, 0);
		previousLayout.setOrientation(LinearLayout.HORIZONTAL);
		previousLayout.setGravity(Gravity.CENTER_VERTICAL);
		previousLayout.setId(2);
		
		previousImage = new ImageView(this);
		previousImage.setImageResource(R.drawable.ic_back_up);
		previousVal = new TextView(this);
        //previousVal.setPadding((int)(10 / div), (int)(20 / div), 0, 0);
        previousVal.setGravity(Gravity.CENTER);
		previousVal.setTextColor(Color.argb(127, 0, 0, 0));
        previousVal.setTextSize((int)(40 / dipDiv));
        
		previousLayout.addView(previousImage);
		previousLayout.addView(previousVal);
		
		nextLayout = new LinearLayout(this);
		RelativeLayout.LayoutParams mNextLayoutParams = new RelativeLayout.LayoutParams(  
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mNextLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mNextLayoutParams.addRule(RelativeLayout.ALIGN_TOP, 2);
		mNextLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
		nextLayout.setPadding((int)(40 / div), 0, (int)(40 / div), 0);
		nextLayout.setOrientation(LinearLayout.HORIZONTAL);
		nextLayout.setGravity(Gravity.CENTER_VERTICAL);
		nextLayout.setId(5);
		
		nextVal = new TextView(this);
		nextVal.setGravity(Gravity.CENTER);
		nextVal.setTextColor(Color.argb(127, 0, 0, 0));
		nextVal.setTextSize((int)(40 / dipDiv));
		//nextVal.setPadding(0, (int)(20 / div), 0, 0);
		nextVal.setText(R.string.next_hint);
		
		nextImage = new ImageView(this);
		nextImage.setPadding((int)(10 / div), 0, 0, 0);
		nextImage.setImageResource(R.drawable.ic_next_up);
		
		nextLayout.addView(nextVal);
		nextLayout.addView(nextImage);
        
		mainLayout.addView(previousLayout, mPreviousLayoutParams);
		mainLayout.addView(nextLayout, mNextLayoutParams);
		
		previousImage.setVisibility(View.INVISIBLE);
		previousVal.setVisibility(View.INVISIBLE);
		
		centerList = new LinearLayout(this);
		RelativeLayout.LayoutParams mCenterListParams = new RelativeLayout.LayoutParams(  
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		centerList.setOrientation(LinearLayout.VERTICAL);
		mCenterListParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		centerList.setId(4);
		centerList.setPadding(0, (int)(150 / div), 0, 0);
		mainLayout.addView(centerList, mCenterListParams);
		centerList.setVisibility(View.VISIBLE);
		
		centerLayout = new LinearLayout(this);
		RelativeLayout.LayoutParams mCenterLayoutParams = new RelativeLayout.LayoutParams(  
				LayoutParams.WRAP_CONTENT, (int)(300 / div));
		mCenterLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mCenterLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		centerLayout.setId(3);
		centerLayout.setPadding(0, 0, 0, (int)(120 / div));
		setNetwork = new Button(this);
        setNetwork.setTextSize((int)(48 / dipDiv));
        setNetwork.setTextColor(Color.argb(255, 255, 255, 255));
        
	    setNetwork.setText(R.string.set_network);
	    setNetwork.setBackgroundResource(R.drawable.buttonstate);
	    setNetwork.setTextAlignment((int)(4 / div));
	    setNetwork.setFocusable(true);
	    setNetwork.setFocusableInTouchMode(true);
	    setNetwork.setClickable(true);
	    setNetwork.setEnabled(true);
	    
	    centerLayout.addView(setNetwork);
		mainLayout.addView(centerLayout, mCenterLayoutParams);
		centerLayout.setVisibility(View.GONE);
		
		searchModeHints = new TextView(this);
		RelativeLayout.LayoutParams mBottomHintParams = new RelativeLayout.LayoutParams(  
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		mBottomHintParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mBottomHintParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		searchModeHints.setPadding(0, 0, 0, (int)(56 / div));
		searchModeHints.setTextSize((int)(38 / dipDiv));
		searchModeHints.setAlpha(0.5f);
		searchModeHints.setTextColor(Color.BLACK);
		searchModeHints.setText(R.string.searchhint);
		mainLayout.addView(searchModeHints, mBottomHintParams);
		searchModeHints.setVisibility(View.VISIBLE);
		
		setNetwork.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				NetworkSetupDialog set = new NetworkSetupDialog();
				set.processCmd(TvConfigTypes.TV_SETUP_NETWORK, 
						DialogCmd.DIALOG_SHOW, null);
			}
			
		});
		
		initListData();
		previousVal.setText(outside_strings.get(getCurLanguageIndex()).toString());
	}
	
	private void EnableHotkey(boolean b_enable)
	{
		if (!b_enable)
		{
			Log.i("wangxian", "enableHotkey.hot_key_disable.1" + b_enable);
			Settings.System.putInt(getContentResolver(), "hot_key_disable", 1);
			try {
				Log.i("wangxian", "Settings.System.getInt.hotkey = " + Settings.System.getInt(getContentResolver(), "hot_key_disable"));
			} catch (SettingNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		{
			Log.i("wangxian", "enableHotkey.hot_key_disable.0" + b_enable);
			Settings.System.putInt(getContentResolver(), "hot_key_disable", 0);
			try {
				Log.i("wangxian", "Settings.System.getInt.hotkey = " + Settings.System.getInt(getContentResolver(), "hot_key_disable"));
			} catch (SettingNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tvType = TvPluginControler.getInstance().getCommonManager().getTvType();
		searchStatus();
		initView();
		ContextTemp = this;
		EnableHotkey(false);
		setContentView(rootLayout);
		((TvApplication)getApplication()).addActivity(this);
	}

	@Override
	protected void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		((TvApplication)getApplication()).removeActivity(this);
	}

	private void resetStrings()
	{
		outside_nextstep = 0;
		outside_prestep = 0;
		if(leftFocusIndex == 0)
		   current_index = getCurLanguageIndex();
		else
		{
		  if(hasTimeZone() != 0)
		  {
			  if(leftFocusIndex == 1)
			  {
				  current_index = getCurTimeZoneIndex();
			  }
			  else
				  current_index = 0;
		  }
		  else
		  {
		      current_index = 0;
		  }
		}
        
		outside_strings = null;
		outside_strings = new ArrayList<String>();
		outside_showStr = null;
	}
	
	private void leftKey()
	{
		if(hasTimeZone() == 0)
		{
			if(leftFocusIndex == 1)
			{
				leftFocusIndex = 0;   //语言画面
				rootLayout.setBackgroundResource(R.drawable.bg_1);
				centerLayout.setVisibility(View.GONE);
				centerList.setVisibility(View.VISIBLE);
				for (int i = 0; i < SIZE_OUTSIDE; i++)
				{
				    checkSearchModes[i].setVisibility(View.GONE);
				}
				
				leftViewNetWork.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewNetWork.setTextSize((int)(30 / dipDiv));
				leftViewLanguage.setTextSize((int)(36 / dipDiv));
				leftViewLanguage.setTextColor(Color.argb(179, 255, 255, 255));
				previousImage.setVisibility(View.INVISIBLE);
				previousVal.setVisibility(View.INVISIBLE);
				searchModeHints.setVisibility(View.VISIBLE);
				resetStrings();
				getLanguageStrings();
				refreshList();
			}
			else if(leftFocusIndex == 2)
			{
				leftFocusIndex = 1;  //网络画面
				rootLayout.setBackgroundResource(R.drawable.bg_4);
				centerLayout.setVisibility(View.VISIBLE);
				setNetwork.setVisibility(View.VISIBLE);
				centerList.setVisibility(View.GONE);
				searchModeHints.setVisibility(View.GONE);
				setNetwork.requestFocus();
				leftViewSearchMode.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewSearchMode.setTextSize((int)(30 / dipDiv));
				leftViewNetWork.setTextColor(Color.argb(179, 255, 255, 255));
				leftViewNetWork.setTextSize((int)(36 / dipDiv));
				nextImage.setVisibility(View.VISIBLE);
				nextVal.setVisibility(View.VISIBLE);
				nextVal.setText(R.string.next_hint);
				previousVal.setText(getCurLanguageString());
			}
		}
		else
		{
			if(leftFocusIndex == 1)
			{
				leftFocusIndex = 0;  //语言画面
				rootLayout.setBackgroundResource(R.drawable.bg_1);
				leftViewTimeZone.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewTimeZone.setTextSize((int)(30 / dipDiv));
				leftViewLanguage.setTextSize((int)(36 / dipDiv));
				leftViewLanguage.setTextColor(Color.argb(179, 255, 255, 255));
				previousImage.setVisibility(View.INVISIBLE);
				previousVal.setVisibility(View.INVISIBLE);
				searchModeHints.setVisibility(View.VISIBLE);
				resetStrings();
				getLanguageStrings();
				refreshList();
			}
			else if(leftFocusIndex == 2)
			{
				leftFocusIndex = 1;    //时区画面
				rootLayout.setBackgroundResource(R.drawable.bg_3);
				centerLayout.setVisibility(View.GONE);
				centerList.setVisibility(View.VISIBLE);
				for (int i = 0; i < SIZE_OUTSIDE; i++)
				{
				    checkSearchModes[i].setVisibility(View.GONE);
				}
				leftViewNetWork.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewNetWork.setTextSize((int)(30 / dipDiv));
				leftViewTimeZone.setTextColor(Color.argb(179, 255, 255, 255));
				leftViewTimeZone.setTextSize((int)(36 / dipDiv));
				searchModeHints.setVisibility(View.VISIBLE);
				resetStrings();
				getTimeZoneStrings();
				refreshList();
				previousVal.setText(getCurLanguageString());
			}
			else if(leftFocusIndex == 3)
			{
				leftFocusIndex = 2;   //网络画面
				rootLayout.setBackgroundResource(R.drawable.bg_4);
				centerLayout.setVisibility(View.VISIBLE);
				setNetwork.setVisibility(View.VISIBLE);
				centerList.setVisibility(View.GONE);
				searchModeHints.setVisibility(View.GONE);
				setNetwork.requestFocus();
				leftViewSearchMode.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewSearchMode.setTextSize((int)(30 / dipDiv));
				leftViewNetWork.setTextColor(Color.argb(179, 255, 255, 255));
				leftViewNetWork.setTextSize((int)(36 / dipDiv));
				nextImage.setVisibility(View.VISIBLE);
				nextVal.setVisibility(View.VISIBLE);
				nextVal.setText(R.string.next_hint);
				previousVal.setText(getCurTimeZoneString());
			}
		}
	}
	
	private void rightKey()
	{
		if(hasTimeZone() == 0)
		{
			if(leftFocusIndex == 1)
			{
				leftFocusIndex = 2;  //搜台画面
				rootLayout.setBackgroundResource(R.drawable.bg_5);
				centerLayout.setVisibility(View.GONE);
				centerList.setVisibility(View.VISIBLE);
				for (int i = 0; i < SIZE_OUTSIDE; i++)
				{
				    checkSearchModes[i].setVisibility(View.VISIBLE);
				}
				searchModeHints.setVisibility(View.VISIBLE);
				leftViewNetWork.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewNetWork.setTextSize((int)(30 / dipDiv));
				leftViewSearchMode.setTextSize((int)(36 / dipDiv));
				leftViewSearchMode.setTextColor(Color.argb(179, 255, 255, 255));
				nextVal.setText(R.string.installsearch);
				resetStrings();
				getSearchModeStrings();
				refreshList();
				previousVal.setText("");  
			}
			else if(leftFocusIndex == 0)
			{	
				leftFocusIndex = 1;    //网络画面
				rootLayout.setBackgroundResource(R.drawable.bg_4);
				centerLayout.setVisibility(View.VISIBLE);
				setNetwork.setVisibility(View.VISIBLE);
				centerList.setVisibility(View.GONE);
				setNetwork.requestFocus();
				leftViewLanguage.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewLanguage.setTextSize((int)(30 / dipDiv));
				leftViewNetWork.setTextColor(Color.argb(179, 255, 255, 255));
				leftViewNetWork.setTextSize((int)(36 / dipDiv));
				previousImage.setVisibility(View.VISIBLE);
				previousVal.setVisibility(View.VISIBLE);
				searchModeHints.setVisibility(View.GONE);
				previousVal.setText(getCurLanguageString());
			}
		}
		else
		{
			if(leftFocusIndex == 2)
			{
				leftFocusIndex = 3;  //搜台画面
				rootLayout.setBackgroundResource(R.drawable.bg_5);
				centerLayout.setVisibility(View.GONE);
				centerList.setVisibility(View.VISIBLE);
				for (int i = 0; i < SIZE_OUTSIDE; i++)
				{
				    checkSearchModes[i].setVisibility(View.VISIBLE);
				}
				searchModeHints.setVisibility(View.VISIBLE);
				leftViewNetWork.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewNetWork.setTextSize((int)(30 / dipDiv));
				leftViewSearchMode.setTextSize((int)(36 / dipDiv));
				leftViewSearchMode.setTextColor(Color.argb(179, 255, 255, 255));
				nextVal.setText(R.string.installsearch);
				resetStrings();
				getSearchModeStrings();
				refreshList();
				previousVal.setText("");  
			}
			else if(leftFocusIndex == 1)
			{
				leftFocusIndex = 2;  //网络画面
				rootLayout.setBackgroundResource(R.drawable.bg_4);
				centerLayout.setVisibility(View.VISIBLE);
				setNetwork.setVisibility(View.VISIBLE);
				centerList.setVisibility(View.GONE);
				searchModeHints.setVisibility(View.GONE);
				setNetwork.requestFocus();
				leftViewTimeZone.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewTimeZone.setTextSize((int)(30 / dipDiv));
				leftViewNetWork.setTextColor(Color.argb(179, 255, 255, 255));
				leftViewNetWork.setTextSize((int)(36 / dipDiv));
				previousVal.setText(getCurTimeZoneString());  
			}
			else if(leftFocusIndex == 0)
			{
				leftFocusIndex = 1;   //时区画面
				rootLayout.setBackgroundResource(R.drawable.bg_3);
				centerLayout.setVisibility(View.GONE);
				centerList.setVisibility(View.VISIBLE);
				searchModeHints.setVisibility(View.VISIBLE);
				leftViewLanguage.setTextColor(Color.argb(127, 0, 0, 0));
				leftViewLanguage.setTextSize((int)(30 / dipDiv));
				leftViewTimeZone.setTextColor(Color.argb(179, 255, 255, 255));
				leftViewTimeZone.setTextSize((int)(36 / dipDiv));
				previousImage.setVisibility(View.VISIBLE);
				previousVal.setVisibility(View.VISIBLE);
				
				resetStrings();
				getTimeZoneStrings();
				refreshList();
				previousVal.setText(getCurLanguageString());
			}
		}
	}

	private String[] outside_movePre(List<String> strings, int move_count)
    {
        String str[] = new String[7];
        for (int i = 0; i <= 3; i++)
        {
            if (i + move_count >= 0 && i + move_count < strings.size())
            {
                str[i] = strings.get(i + move_count);
            } else
            {
                str[i] = null;
            }
        }
        for (int i = 4; i <= 6; i++)
        {
            if (6 - i >= move_count)
            {
                str[i] = null;
            } else
            {
                str[i] = strings.get(i - 6 + move_count - 1);
            }
        }
        return str;
    }
	
	private void outside_getFocos_downEvent(int nextstep)
    {
       if(current_index + nextstep >= outside_strings.size())
        {
            nextstep--;
        }
        if (nextstep < 0)
        {
            return;
        }
        current_index =  current_index+nextstep;
        outside_showStr = outside_moveNext(outside_strings, current_index);
        setOutside_items();
    }
	
	private void outside_getFocos_upEvent(int nextstep)
    {
        if(current_index - nextstep < 0)
        {
            nextstep--;
        }
        if (nextstep < 0)
        {
            return;
        }
        current_index =  current_index-nextstep;
        outside_showStr = outside_movePre(outside_strings, current_index);
        setOutside_items();
    }
		
	private void startSearchMode()
	{
		TvSearchTypes.isautosearch = true;
		TvSearchTypes.isDVBSAutoSearch = TvPluginControler.getInstance().getChannelManager().getSearchDVBSStatus();
		TvSearchTypes.isDVBCAutoSearch = TvPluginControler.getInstance().getChannelManager().getSearchDVBCStatus();
		TvSearchTypes.isDVBTAutoSearch = TvPluginControler.getInstance().getChannelManager().getSearchDVBTStatus();
		TvSearchTypes.isATVAutoSearch = TvPluginControler.getInstance().getChannelManager().getSearchATVStatus();
		Intent startGuide = new Intent();
	    switch(tvType)
	    {
	    case SKY_CFG_TV_SYSTEM_PAL_DVBT_DVBT2:
	    case SKY_CFG_TV_SYSTEM_PAL_DVBS_DVBS2:
	    {				
	    	if(DVBSSearchStatus)
	    	{
	    		try
				{
					TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV2.ordinal());
				} catch (RemoteException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		LNBSettingDialog lnbSettingDialog = new LNBSettingDialog();
	    		lnbSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_LNB_SETTING, DialogCmd.DIALOG_SHOW, null);
	    	}
	    	else if(DVBTSearchStatus)
	    	{
	    		Log.d("wangxian", "start DTV search....");				
				try {
					TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV.ordinal());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		DTVChannelSearchDialog dtvChannelSearchDialog = new DTVChannelSearchDialog();
	    		dtvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "DVBT");

			}
	    	else if(DVBCSearchStatus)
	    	{
	    		Log.d("wangxian", "start DVBC search....");				
				try {
					TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_KTV.ordinal());
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		DTVChannelSearchDialog dtvChannelSearchDialog = new DTVChannelSearchDialog();
	    		dtvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "DVBC");

			}
	    	else if(ATVSearchStatus)
	    	{
	    		Log.d("wangxian", "start ATV search....");				
	    		try {
	    			if(TvPluginControler.getInstance().getCommonManager().getCurrentInputSource() != TvEnumInputSource.E_INPUT_SOURCE_ATV)
	    			{
	    				TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_ATV.ordinal());
	    			}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		ATVChannelSearchDialog atvChannelSearchDialog = new ATVChannelSearchDialog();
	    		atvChannelSearchDialog.setDialogListener(null);
				atvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_ATV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "ATV");
	    	}
	    	
			EnableHotkey(true);
			setInstallGuideFlag();
	    	startGuide.setClass(this, TvActivity.class);
			startActivity(startGuide);

			this.finish();
	    }
	        break;
	    default:
	    	break;
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		// TODO Auto-generated method stub
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_LEFT:
			leftKey();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(hasTimeZone() == 0)
			{
				if(leftFocusIndex == 2)
				{
					startSearchMode();
				}
			}
			else
			{
				if(leftFocusIndex == 3)
				{
					startSearchMode();	
				}
			}
			rightKey();
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if(leftFocusIndex == 0)
			{
				((TvEnumSetData)dataLanguage).setCurrentIndex(current_index);
	             TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_OSD_LANGUAGE,dataLanguage);
			}
			else
			{
				if(hasTimeZone() != 0 && leftFocusIndex == 1)
				{
					((TvEnumSetData)timeZones).setCurrentIndex(current_index);
		             TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_TIME_ZONE,timeZones);
				}
				else if((hasTimeZone() != 0 && leftFocusIndex == 3) || (hasTimeZone() == 0 && leftFocusIndex == 2) )
				{
					setSearchStatus(3,outside_strings.get(current_index));
				}
			}
			
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			outside_nextstep = 1;
            outside_getFocos_downEvent(outside_nextstep);
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			 outside_prestep = 1;
             outside_getFocos_upEvent(outside_prestep);
			break;
		case KeyEvent.KEYCODE_BACK:
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
	}	
}
