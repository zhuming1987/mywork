package com.tv.ui.AudioLanguage;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.data.MenuItem;
import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.AudioLanguage.AudioLanguageListItemView.onKeyDownListener;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvMenuConfigDefs;
import com.tv.ui.utils.LogicTextResource;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class AudioLanguageListView extends LinearLayout implements  onKeyDownListener
{

	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
	
    public static final int hostTitleColor = 0xFFEDC40F;
    public static final int shareTitleColor = 0xFF3D9AD8;
    
    private AudioLanguageListViewDialog parentDialog = null;
    private Context mContext;
    
    private ScrollView scrollView;
	private LeftViewTitleLayout titleLayout;
	private LeftViewBodyLayout bodyLayout;
    private LinearLayout listlayout;
    private LinearLayout.LayoutParams itemParams;
    
    private ArrayList<String> dataList = new ArrayList<String>();
    protected ArrayList<AudioLanguageListItemView> viewList = new ArrayList<AudioLanguageListItemView>();
    protected MenuItem data;
    
	public AudioLanguageListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	    this.mContext = context;
    	LinearLayout.LayoutParams laytoutLp = new LinearLayout.LayoutParams((int)((TvMenuConfigDefs.setting_view_bg_w+100)/div),LayoutParams.MATCH_PARENT);
		this.setLayoutParams(laytoutLp);
		this.setBackgroundResource(R.drawable.setting_bg);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		
		titleLayout = new LeftViewTitleLayout(context);
		titleLayout.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		this.addView(titleLayout);
		
		bodyLayout = new LeftViewBodyLayout(context);
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		this.addView(bodyLayout);
		
		scrollView = new ScrollView(context);
		scrollView.setVerticalScrollBarEnabled(true);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setScrollbarFadingEnabled(true);
		scrollView.setAlwaysDrawnWithCacheEnabled(true);
		scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		scrollView.setPadding((int) (25 / div), (int) (40 / div),
				(int) (25 / div), (int) (0 / div));
		listlayout = new LinearLayout(context);
        listlayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(listlayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        bodyLayout.addView(scrollView);

        itemParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (67 / div));
        itemParams.topMargin = (int) (20 / div);
        itemParams.leftMargin = (int) (10 / div);
        itemParams.rightMargin = (int) (10 / div);
        itemParams.bottomMargin = (int) (20 / div);
        itemParams.gravity = Gravity.CENTER_VERTICAL;
	}
	
	public void updateView(MenuItem data)
	{
		this.data = data;
		titleLayout.bindData(data.getName()+".png", LogicTextResource.getInstance(TvContext.context).getText(data.getId()));
		
		dataList.clear();
		listlayout.removeAllViews();
		viewList.clear();
		Log.i("gky", "TvListView::updateView " + data.getItemData());
		for(String item : ((TvEnumSetData)data.getItemData()).getEnumList())
		{
			dataList.add(LogicTextResource.getInstance(TvContext.context).getText(item));
		}
		Log.i("gky", "TvListView::updateView dataList size is " + dataList.size());
		
		ImageView Divider = new ImageView(mContext);
		Divider.setBackgroundResource(R.drawable.setting_line);
		listlayout.addView(Divider, new LayoutParams((int)(790/div),(int) (1 / div)));
		
		for (int i = 0; i < dataList.size(); i++) 
		{
			AudioLanguageListItemView view = new AudioLanguageListItemView(mContext, this, i);
			view.updateView(dataList.get(i));
			listlayout.addView(view,itemParams);
			viewList.add(view);
			ImageView ItemDivider = new ImageView(mContext);
            ItemDivider.setBackgroundResource(R.drawable.setting_line);
            listlayout.addView(ItemDivider, new LayoutParams((int)(790/div),(int) (1 / div)));
		}
		
		int default_index = ((TvEnumSetData)data.getItemData()).getCurrentIndex();
		if(default_index == -1)
			return;
		viewList.get(default_index).requestFocus();
		viewList.get(default_index).setChecked(true);
		viewList.get(default_index).getOnFocusChangeListener().onFocusChange(viewList.get(default_index), true);
	}

	public void setParentDialog(AudioLanguageListViewDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	@Override
	public void keyDownListener(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
		{
			
		}
	}

	@Override
	public void ItemClickListener(int index) {
		// TODO Auto-generated method stub
		Log.i("gky", "TvListView::ItemClickListener: index is " + index);
		for(AudioLanguageListItemView view : viewList)
		{
			if(view.index == index)
				continue;
			view.setChecked(false);
		}
		TvEnumSetData eData = (TvEnumSetData) data.getItemData();
		eData.setCurrentIndex(index);
		TvPluginControler.getInstance().setConfigData(data.getId(), eData);
	}

	
}
