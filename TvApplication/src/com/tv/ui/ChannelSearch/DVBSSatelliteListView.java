package com.tv.ui.ChannelSearch;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvSatelliteInfoData;
import com.tv.framework.data.TvSatelliteListData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

public class DVBSSatelliteListView extends LinearLayout implements OnKeyListener{
	private float div = TvUIControler.getInstance().getResolutionDiv();
    
    private Context context;
    private LeftViewTitleLayout titleLayout;
    private ScrollView bodyScrollview;
    private ImageView divideLine; 
    
    private ArrayList<TvSatelliteListData> tvSatelliteList = new ArrayList<TvSatelliteListData>();
    private ArrayList<DVBSSatelliteItemView> tvSatelliteItemView = new ArrayList<DVBSSatelliteItemView>();
    private TvSatelliteInfoData satelliteInfo = new TvSatelliteInfoData();
    private DVBSSatelliteItemView selecteItem;
    private LinearLayout satellitelistLayout;
    private int bobyWidth = (int)(845/div);
    private int bodyHeight = (int)(960/div);
    private int data_count = 0;
    private DVBSSatelliteListDialog parentDialog = null;
    
    public DVBSSatelliteListView(Context context,DVBSSatelliteListDialog parentDialog)
    {
        super(context);
        this.context = context;
        this.parentDialog = parentDialog;
        this.setOrientation(LinearLayout.VERTICAL);

        titleLayout = new LeftViewTitleLayout(context);
        titleLayout.bindData("tv_auto_search.png", "Satellite List");
        this.addView(titleLayout);

        bodyScrollview = new ScrollView(mContext);
        bodyScrollview.setBackgroundColor(Color.parseColor("#191919"));
        bodyScrollview.setVerticalScrollBarEnabled(true);
        bodyScrollview.setHorizontalScrollBarEnabled(false);
        bodyScrollview.setScrollbarFadingEnabled(true);
        bodyScrollview.setAlwaysDrawnWithCacheEnabled(true);
        bodyScrollview.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        satellitelistLayout = new LeftViewBodyLayout(context);
        satellitelistLayout.setOrientation(LinearLayout.VERTICAL);
        bodyScrollview.addView(satellitelistLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        this.addView(bodyScrollview, bobyWidth , bodyHeight);
    }

    public void updateView()
    {
    	 satelliteInfo = TvPluginControler.getInstance().getChannelManager().UpdateSatelliteInfo();
		 tvSatelliteList = TvPluginControler.getInstance().getChannelManager().getSatelliteList();
		 if(null != tvSatelliteList)
		 {
			 data_count = tvSatelliteList.size();
			 Log.v("zhm", "data_count = " + data_count);
			 satellitelistLayout.removeAllViews();
			 tvSatelliteItemView.clear();
			 for (int i = 0; i < data_count; i++)
			 {
				 selecteItem = new DVBSSatelliteItemView(mContext, this);
				 selecteItem.update(tvSatelliteList.get(i).getNumber(),tvSatelliteList.get(i).getName(),tvSatelliteList.get(i).getBand());
				 satellitelistLayout.addView(selecteItem, LayoutParams.MATCH_PARENT, (int) (120 / div));				 
				 addHDivideLine(satellitelistLayout);			 
	             tvSatelliteItemView.add(selecteItem);   
	             tvSatelliteItemView.get(i).setId(i);
	             tvSatelliteItemView.get(i).setOnClickListener(new LisenerItem());
	             selecteItem.setItemSelectFocusChange();
			 }
			 String CurrentSatelliteNameString = TvPluginControler.getInstance().getChannelManager().getCurrentSatelliteName();
			 for(int i = 0;i<tvSatelliteItemView.size();i++)
			 {			 
				 if(CurrentSatelliteNameString.equals(""))
				 {
					 tvSatelliteItemView.get(0).setFocusable(true);
					 tvSatelliteItemView.get(0).requestFocus();
				 }
				 else 
				 {
					 if(tvSatelliteItemView.get(i).getNameString().equals(CurrentSatelliteNameString))
					 {
						 satellitelistLayout.setFocusable(true);
						 tvSatelliteItemView.get(i).requestFocus();
					 }
				 }
			 }	
		 }
	 }

    class LisenerItem implements OnClickListener {
		public void onClick(View view) {
			int id = view.getId();
			TvPluginControler.getInstance().getChannelManager().SelectSatellite(String.valueOf(id));
			tvSatelliteList.get(id).setSelectedFlag(true);
			parentDialog.dismissUI();
			DVBSManualSearchDialog dvbsmanualsearchDialog = new DVBSManualSearchDialog();
			dvbsmanualsearchDialog.setDialogListener(null);
			dvbsmanualsearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DVBS_MANUAL_SEARCH, DialogCmd.DIALOG_SHOW, null);
		}
	}
    private void addHDivideLine(LinearLayout layout)
	 {
		divideLine = new ImageView(mContext);
		divideLine.setBackgroundResource(R.drawable.setting_line);
		LayoutParams divideParams = new LayoutParams((int) (730 / div),(int) (2 / div));
		divideParams.leftMargin = (int) (20 / div);
		divideParams.rightMargin = (int) (20 / div);
		layout.addView(divideLine, divideParams);
    } 
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
}
