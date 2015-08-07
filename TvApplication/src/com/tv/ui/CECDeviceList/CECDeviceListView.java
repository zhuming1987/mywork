package com.tv.ui.CECDeviceList;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvUIControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.framework.define.TvConfigTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SettingView.TvSettingItemView.OnSettingKeyDownListener;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.utils.LogicTextResource;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class CECDeviceListView extends LinearLayout implements OnSettingKeyDownListener, DialogListener
{
	private Context mContext;
	private static float div =TvUIControler.getInstance().getResolutionDiv();
    private CECDeviceListDialog parentDialog = null;
	
	private ScrollView scrollView;
	private LeftViewTitleLayout titleLayout;
    private LeftViewBodyLayout bodyLayout;
    LinearLayout listlayout;
    LinearLayout.LayoutParams itemParams;
    private ImageView cecdeviceItemDivider;
    
    private CECDeviceListItemView[] selecteItems = new CECDeviceListItemView[10];
    ArrayList<String> deviceList = new ArrayList<String>();
    
    private QuickKeyListener quickKeyListener;

	@SuppressWarnings("deprecation")
	public CECDeviceListView(Context context){
		// TODO Auto-generated constructor stub
		super(context);
        mContext = context;
    	this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
    	LinearLayout.LayoutParams laytoutLp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		setLayoutParams(laytoutLp);
		this.setBackgroundResource(R.drawable.setting_bg);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		
		titleLayout = new LeftViewTitleLayout(mContext);
		titleLayout.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		titleLayout.bindData("system_setting.png",LogicTextResource.getInstance(context).getText(R.string.TV_CFG_HDMID_CEC_DEVICE_LIST_SETTING));
		
		this.addView(titleLayout);
		
		bodyLayout = new LeftViewBodyLayout(mContext);
		bodyLayout.setPadding((int)(50 / div), (int)(30 / div), (int)(50 / div), (int)(50 / div));
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		this.addView(bodyLayout);
		
		scrollView = new ScrollView(mContext);
		scrollView.setVerticalScrollBarEnabled(true);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setScrollbarFadingEnabled(true);
		scrollView.setAlwaysDrawnWithCacheEnabled(true);
		scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		listlayout = new LinearLayout(mContext);
        listlayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(listlayout, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));		
        bodyLayout.addView(scrollView);

        itemParams = new LinearLayout.LayoutParams((int) (480 / div), (int) (67 / div));
        itemParams.topMargin = (int) (20 / div);
        itemParams.leftMargin = (int) (20 / div);
        itemParams.rightMargin = (int) (20 / div);
        itemParams.bottomMargin = (int) (20 / div);
        itemParams.gravity = Gravity.CENTER_VERTICAL;
	}
	public void updateView()
	{
		deviceList = TvPluginControler.getInstance().getCommonManager().getCecDeviceList();
		listlayout.removeAllViews();
		if (deviceList.size() > 0)
        {
            for (int i = 0; i < deviceList.size(); i++)
            {
                selecteItems[i] = new CECDeviceListItemView(mContext,this);
                cecdeviceItemDivider = new ImageView(mContext);
                cecdeviceItemDivider.setBackgroundResource(R.drawable.setting_line);
                listlayout.addView(cecdeviceItemDivider, new LayoutParams(LayoutParams.MATCH_PARENT,(int) (2 / div)));
                selecteItems[i].setId(i);
                listlayout.addView(selecteItems[i], itemParams);
                selecteItems[i].setOnClickListener(new LisenerItem());
            }
            setItemSelectFocusChange(deviceList);
        }
		else
		{
			listlayout.setFocusable(true);
			listlayout.requestFocus();
			listlayout.setOnKeyListener(new OnKeyListener() {				
				@Override
				public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
					// TODO Auto-generated method stub
					if (keycode == KeyEvent.KEYCODE_BACK /*|| keycode == KeyEvent.KEYCODE_MENU*/) {
						onKeyDown(keycode, null);
						return true;
					}
					return false;
				}
			});
		}
	}
    private void setItemSelectFocusChange(ArrayList<String> dataList)
    {
		for (int i = 0; i < dataList.size(); i++) {
			CECDeviceListItemView itemView = selecteItems[i];
			itemView.update(dataList.get(i).toString());
			itemView.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View view, boolean hasFocus) {
					((CECDeviceListItemView) view).setBackGroundColorUser(hasFocus);
				}
			});
		}
    }
	public boolean onKeyDown(int keyCode, KeyEvent event)
    {
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)
        {
            if (selecteItems[0] != null && !selecteItems[0].isFocused())
            {
                selecteItems[0].requestFocus();
                selecteItems[0].setChecked(true);
                checkedSelectedItem(1);
            }
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/)
        {
        	parentDialog.processCmd(TvConfigTypes.TV_DIALOG_CEC_DEVICE_LIST,DialogCmd.DIALOG_DISMISS, null);
			return true;
        }
        return quickKeyListener.onQuickKeyDownListener(keyCode, event);
    }
	
	class LisenerItem implements OnClickListener
    {
        public void onClick(View view)
        {
            int id = view.getId();
            TvPluginControler.getInstance().getCommonManager().setCecDeviceIndex(id);
            checkedSelectedItem(id);
            parentDialog.setDialogListener(null);
            parentDialog.processCmd(TvConfigTypes.TV_DIALOG_CEC_DEVICE_LIST,DialogCmd.DIALOG_DISMISS, null);
        }
    }

	
	private void checkedSelectedItem(int id)
    {
    	if (deviceList.size() == 0)
        {
            return;
        }
        for (int i = 0; i < deviceList.size(); i++)
        {
        	CECDeviceListItemView view = selecteItems[i];
            boolean isChecked = view.getchecked();
            if (view.getId() == id)
            {
                if (!isChecked)
                {
                    view.setChecked(true);
                } 
            } else
            {
                view.setChecked(false);
            }
        }
    }

    public void setParentDialog(CECDeviceListDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	@Override
	public boolean onShowDialogDone(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object...obj) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void OnKeyDownListener(int keyCode) {
		// TODO Auto-generated method stub
    
	}

	@Override
	public void setFocus(int index, int keycode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnPage(int keycode, int index) {
		// TODO Auto-generated method stub
		
	}
}
