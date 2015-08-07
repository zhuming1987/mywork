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
import com.tv.framework.data.TvTransponderListData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

public class DVBSTransPonderListView extends LinearLayout implements OnKeyListener{
	private float div = TvUIControler.getInstance().getResolutionDiv();
    
    private LeftViewTitleLayout titleLayout;
    private ScrollView bodyScrollview;
    private ImageView divideLine; 
    
    private ArrayList<TvTransponderListData> tvTransponderList = new ArrayList<TvTransponderListData>();
    private ArrayList<DVBSTransPonderItemView> tvTransponderItemView = new ArrayList<DVBSTransPonderItemView>();
    private DVBSTransPonderItemView selecteItem;
    private LinearLayout transponderlistLayout;
    private int bobyWidth = (int)(845/div);
    private int bodyHeight = (int)(960/div);
    private int data_count = 0;
    private DVBSTransPonderListDialog parentDialog = null;
    
    public DVBSTransPonderListView(Context context,DVBSTransPonderListDialog parentDialog)
    {
        super(context);
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
        transponderlistLayout = new LeftViewBodyLayout(context);
        transponderlistLayout.setOrientation(LinearLayout.VERTICAL);
        bodyScrollview.addView(transponderlistLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        this.addView(bodyScrollview, bobyWidth , bodyHeight);
    }

    public void updateView()
    {
		 tvTransponderList = TvPluginControler.getInstance().getChannelManager().getTransponderList();
		 if(null != tvTransponderList)
		 {
			 data_count = tvTransponderList.size();
			 Log.v("zhm", "data_count = " + data_count);
			 transponderlistLayout.removeAllViews();
			 tvTransponderItemView.clear();
			 for (int i = 0; i < data_count; i++)
			 {
				 selecteItem = new DVBSTransPonderItemView(mContext, this);
				 selecteItem.updateView(tvTransponderList.get(i));
				 transponderlistLayout.addView(selecteItem, LayoutParams.MATCH_PARENT, (int) (120 / div));				 
				 addHDivideLine(transponderlistLayout);			 
	             tvTransponderItemView.add(selecteItem);   
	             tvTransponderItemView.get(i).setId(i);
	             tvTransponderItemView.get(i).setOnClickListener(new LisenerItem());
	             selecteItem.setItemSelectFocusChange();
			 }
			 String CurrentSatelliteNameString = TvPluginControler.getInstance().getChannelManager().getCurrentSatelliteName();
			 for(int i = 0;i<tvTransponderItemView.size();i++)
			 {			 
				 if(CurrentSatelliteNameString.equals(""))
				 {
					 tvTransponderItemView.get(0).setFocusable(true);
					 tvTransponderItemView.get(0).requestFocus();
				 }
				 else 
				 {
					 if(tvTransponderItemView.get(i).getNameString().equals(CurrentSatelliteNameString))
					 {
						 transponderlistLayout.setFocusable(true);
						 tvTransponderItemView.get(i).requestFocus();
					 }
				 }
			 }	
		 }
	 }

    class LisenerItem implements OnClickListener {
		public void onClick(View view) {
			int id = view.getId();
			TvPluginControler.getInstance().getChannelManager().SelectTransponder(String.valueOf(id));
			tvTransponderList.get(id).setSelectedFlag(true);
			parentDialog.dismissUI();
			DVBSTransPonderListDialog dvbstransponderlistdialog = new DVBSTransPonderListDialog();
			dvbstransponderlistdialog.setDialogListener(null);
			dvbstransponderlistdialog.processCmd(TvConfigTypes.TV_DIALOG_DVBS_TRANSPONDER_LIST, DialogCmd.DIALOG_SHOW, null);
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
