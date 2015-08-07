package com.tv.ui.CiInformation;


import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.CiUIEventData;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.CiInformation.CIInfoListItemView.onKeyDownListener;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvMenuConfigDefs;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class CIInformationView extends LinearLayout implements onKeyDownListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private static float dip = TvUIControler.getInstance().getDipDiv();
	private Context context;
	private ScrollView scrollView;
	private TextView subtitleTextView;
	private LeftViewTitleLayout titleLayout;
	private CIInfobodyLayout bodyLayout;
	private LinearLayout listlayout;
	private LinearLayout.LayoutParams itemParams;
	private ArrayList<String> dataList = new ArrayList<String>();
	private ArrayList<CIInfoListItemView> viewList = new ArrayList<CIInfoListItemView>();
    private CIInformationDialog parentDialog = null;
    private CiUIEventData dataTemp = null;
    
    public CIInformationView(Context context)
    {
        super(context);
		// TODO Auto-generated constructor stub
	    this.context = context;
    	LinearLayout.LayoutParams laytoutLp = new LinearLayout.LayoutParams((int)(TvMenuConfigDefs.setting_view_bg_w/div),LayoutParams.MATCH_PARENT);
		this.setLayoutParams(laytoutLp);
		this.setBackgroundResource(R.drawable.setting_bg);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		
		titleLayout = new LeftViewTitleLayout(context);
		titleLayout.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		this.addView(titleLayout);

		bodyLayout = new CIInfobodyLayout(context);
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		this.addView(bodyLayout);
		
		subtitleTextView = new TextView(context);
		subtitleTextView.setTextSize(42/dip);
		subtitleTextView.setTextColor(Color.WHITE);
		subtitleTextView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		
		LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(100/div));	
		subParams.topMargin = (int)(5/div);
		subParams.bottomMargin = (int)(5/div);
		subParams.leftMargin = (int)(15/div);
		bodyLayout.addView(subtitleTextView,subParams);
		
		scrollView = new ScrollView(context);
		scrollView.setVerticalScrollBarEnabled(true);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setScrollbarFadingEnabled(true);
		scrollView.setAlwaysDrawnWithCacheEnabled(true);
		scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		scrollView.setPadding((int) (25 / div), (int) (0 / div),
				(int) (25 / div), (int) (0 / div));
		listlayout = new LinearLayout(context);
        listlayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(listlayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));		
        bodyLayout.addView(scrollView);

        itemParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (70 / div));
        itemParams.topMargin = (int) (15 / div);
        itemParams.leftMargin = (int) (10 / div);
        itemParams.rightMargin = (int) (10 / div);
        itemParams.bottomMargin = (int) (15 / div);
        itemParams.gravity = Gravity.CENTER_VERTICAL;
    }
    public void updateView(CiUIEventData data)
	{
    	dataTemp = data;
		if(!data.titleString.equals(""))
			titleLayout.bindData("tv_channel_set.png", data.titleString);
		else
			titleLayout.bindData("tv_channel_set.png", TvContext.context.getResources().getString(R.string.TV_CFG_CI_INFORMATION));
		dataList.clear();
		listlayout.removeAllViews();
		viewList.clear();
		Log.i("gky", "CiINformationView::updateView " + data.dataList);
		
		Log.i("gky", getClass().toString() + "-->updateView "+data.subTitleString+"/"+data.bottomString);
		subtitleTextView.setText(data.subTitleString);
		
		for(String item : data.dataList)
		{
			dataList.add(item);
		}
		Log.i("gky", "TvListView::updateView dataList size is " + dataList.size());
		
		ImageView Divider = new ImageView(context);
		Divider.setBackgroundResource(R.drawable.setting_line);
		listlayout.addView(Divider, new LayoutParams((int)(845/div),(int) (1 / div)));
		
		for (int i = 0; i < dataList.size(); i++) 
		{
			CIInfoListItemView view = new CIInfoListItemView(context,this,i);
			view.updateView(dataList.get(i));
			listlayout.addView(view,itemParams);
			viewList.add(view);
			ImageView ItemDivider = new ImageView(context);
            ItemDivider.setBackgroundResource(R.drawable.setting_line);
            listlayout.addView(ItemDivider, new LayoutParams((int)(845/div),(int) (1 / div)));
		}
		
		int default_index = 0;
		if (viewList != null && viewList.size() > 0) {
			viewList.get(default_index).requestFocus();
			viewList.get(default_index).setSelected(true);
			viewList.get(default_index).getOnFocusChangeListener()
					.onFocusChange(viewList.get(default_index), true);
		}
	}

	public void setParentDialog(CIInformationDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	public void keyDownListener(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("gky", getClass().toString() + "-->keyDownListener keycode is "+keyCode);
		//正在测试过程中,不响应任何按键
		int res = TvPluginControler.getInstance().getCilManager().getCUProgress();
		Log.i("gky", getClass().toString() + "-->check upgrade progress res is "+res);
		if(res != 0){
			TvPluginControler.getInstance().getCommonManager().enableHotkey(false);
			return;
		}
		TvPluginControler.getInstance().getCommonManager().enableHotkey(true);
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			TvPluginControler.getInstance().getCilManager().backMenu();
		}
		else if(keyCode == KeyEvent.KEYCODE_MENU)
		{
			parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
		}
	}

	public void ItemClickListener(int index) {
		// TODO Auto-generated method stub
		Log.i("gky", "CIInformationView::ItemClickListener: index is " + index);
		TvPluginControler.getInstance().getCilManager().answerMenu((short)index);
	}


}
