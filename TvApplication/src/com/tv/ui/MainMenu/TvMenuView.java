package com.tv.ui.MainMenu;

import java.util.ArrayList;

import com.skyworth.config.SkyConfigDefs.SKY_CFG_TV_SYSTEM_TYPE;
import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.app.TvUIControler;
import com.tv.data.MenuItem;
import com.tv.data.TvType;
import com.tv.framework.define.TvConfigDefs.TV_CONFIG_SET;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvConfigTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.MainMenu.TvMenuItemView.OnMenuItemKeyListener;
import com.tv.ui.MainMenu.TvMenuItemView.OnMenuItemClickListener;
import com.tv.ui.ParentrolControl.PasswordMainMenuDialog;
import com.tv.ui.SettingView.TvSettingDialog;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.base.TvMenuConfigDefs;
import com.tv.ui.utils.LogicTextResource;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class TvMenuView extends LinearLayout 
	implements OnMenuItemClickListener, OnMenuItemKeyListener, DialogListener{
	
	private ArrayList<MenuItem> dataList;
	private Context context;
	private TvMenuDialog parentDialog;
	private int countNum;
	private int focusIndex;
	
	private LeftViewTitleLayout titleLayout;
	private LeftViewBodyLayout bodyLayout;
	
	private QuickKeyListener quickKeyListener;
	
	private final float resolution = TvUIControler.getInstance().getResolutionDiv();
	private final float dipDiv = TvUIControler.getInstance().getDipDiv();
	
	public TvMenuView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
		this.context = context;
		this.setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(new LayoutParams((int) (TvMenuConfigDefs.menu_bg_w/resolution),
                (int)(945/resolution)));
        this.setBackgroundResource(R.drawable.setting_bg);
        
        titleLayout = new LeftViewTitleLayout(context);
        titleLayout.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
        this.addView(titleLayout);
        bodyLayout = new LeftViewBodyLayout(context);
        this.addView(bodyLayout);
        
	}
	
	public void updatView(ArrayList<MenuItem> data){
		Log.i("gky", "TvMenuView updateView");
		titleLayout.bindData("main_setting.png", getCurMenuName());
		dataList = null;
		bodyLayout.removeAllViews();
		dataList = data;
		countNum = data.size();
		for (int i = 0,index = 0; i < countNum; i++) {
			//Log.i("gky", "data.get(i).ishide() is " + data.get(i).ishide());
			if(data.get(i).ishide())
				continue;
			TvMenuItemView view = new TvMenuItemView(context,index++,this,this,this);
			view.updateView(data.get(i));
			bodyLayout.addView(view);
		}		
	}

	@Override
	public void OnMenuItemClickListener(int index, String name) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvMenuView::OnMenuItemClickListener:: index is " + index
				+ " name is " + name);
		focusIndex = index;
		TV_CONFIG_SET menuConfigSet = TV_CONFIG_SET.valueOf(name);
		parentDialog.dismissUI();
		if(menuConfigSet.equals(TV_CONFIG_SET.TV_CFG_CHANNEL_SETTING)
		  &&TvPluginControler.getInstance().getParentalControlManager().isSystemLock())
		{
			PasswordMainMenuDialog pass= PasswordMainMenuDialog.getInstance();
			pass.setDialogListener(this);
			pass.processCmd(TvConfigTypes.TV_DIALOG_INPUT_PASSWORD, DialogCmd.DIALOG_SHOW,name);
		}else if(menuConfigSet.equals(TV_CONFIG_SET.TV_CFG_LOCK_SETTING)) 
		{
			PasswordMainMenuDialog pass= PasswordMainMenuDialog.getInstance();
			pass.setDialogListener(this);
			pass.processCmd(TvConfigTypes.TV_DIALOG_INPUT_PASSWORD, DialogCmd.DIALOG_SHOW,name);
		}
		else{
			TvSettingDialog settingDialog = new TvSettingDialog();
			settingDialog.setDialogListener(this);
			settingDialog.processCmd(TvConfigTypes.TV_DIALOG_SETTING, DialogCmd.DIALOG_SHOW, TvType.getInstance().getSubMenuItem(name));
		}
	}

	@Override
	public boolean onKeyDownExecute(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvMenuView::onKeyDownExecute keycode is " + keyCode);
		switch (keyCode) {
			/*case KeyEvent.KEYCODE_MENU:*/
			case KeyEvent.KEYCODE_BACK:
				parentDialog.processCmd(TvConfigTypes.TV_DIALOG_MAIN_MENU, DialogCmd.DIALOG_DISMISS, null);
				return true;
		}
		return quickKeyListener.onQuickKeyDownListener(keyCode, event);
	}
	
	public TvBaseDialog getParentDialog() {
		return parentDialog;
	}

	public void setParentDialog(TvMenuDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	@Override
	public void onRequestFocus(int index) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvMenuView::onRequestFocus index is " + index + " count is " + bodyLayout.getChildCount());
		index = index<0?(bodyLayout.getChildCount()-1):index;
		index = index>(bodyLayout.getChildCount()-1)?0:index;
		focusIndex = index;
		bodyLayout.getChildAt(index).requestFocus();
	}

	@Override
	public boolean onShowDialogDone(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object...obj) {
		// TODO Auto-generated method stub
		parentDialog.processCmd(null, DialogCmd.DIALOG_SHOW, TvType.getInstance().getMenuData());
		bodyLayout.getChildAt(focusIndex).requestFocus();
		return 0;
	}

	public String getCurMenuName(){
		String curValue = LogicTextResource.getInstance(context).getText(R.string.str_common);
		TvEnumInputSource curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
		switch (curSource) {
		case E_INPUT_SOURCE_ATV:
			curValue = "ATV";
			break;
		case E_INPUT_SOURCE_DTV:
			if(TvPluginControler.getInstance().getCommonManager().getTvType() != SKY_CFG_TV_SYSTEM_TYPE.SKY_CFG_TV_SYSTEM_PAL_DVBS_DVBS2){
				curValue = "DTV";
			}
			else {
				int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
				if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS
						|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2)
				{
					curValue = "DVBS";
				}else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT
						|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT2)
				{
					curValue = "DVBT";
				}
				else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBC)
				{
					curValue = "DVBC";
				}
				
			}
			break;
		case E_INPUT_SOURCE_VGA:
			if(TvPluginControler.getInstance().getCommonManager().isPCorScart())
				curValue = "PC";
			else
				curValue = "Scart";
			break;
		case E_INPUT_SOURCE_HDMI:
		case E_INPUT_SOURCE_HDMI2:
		case E_INPUT_SOURCE_HDMI3:
		case E_INPUT_SOURCE_HDMI4:
			curValue = "HDMI Menu";
			break;
		case E_INPUT_SOURCE_YPBPR:
		case E_INPUT_SOURCE_YPBPR2:
		case E_INPUT_SOURCE_YPBPR3:
			curValue = "YUV Menu";
			break;
		case E_INPUT_SOURCE_CVBS:
		case E_INPUT_SOURCE_CVBS2:
		case E_INPUT_SOURCE_CVBS3:
		case E_INPUT_SOURCE_CVBS4:
			curValue = "AV Menu";
			break;
		default:
			curValue = "Common Menu";
			break;
		}
		return curValue;
	}
}
