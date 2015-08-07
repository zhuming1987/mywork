package com.tv.ui.ChannelSearch;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.tv.app.TvUIControler;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;

public class DVBSLNBTypeListView extends LinearLayout implements OnKeyListener{

	private float div = TvUIControler.getInstance().getResolutionDiv();
	private DVBSLNBTypeListDialog parentDialog = null;
	
	private LeftViewTitleLayout titleLayout;
    private ScrollView bodyScrollview;
    private ImageView divideLine; 
    private LinearLayout lnbtypelistLayout;
    private int bobyWidth = (int)(845/div);
    private int bodyHeight = (int)(960/div);
	
	public DVBSLNBTypeListView(Context context,DVBSLNBTypeListDialog parentDialog) {
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
        lnbtypelistLayout = new LeftViewBodyLayout(context);
        lnbtypelistLayout.setOrientation(LinearLayout.VERTICAL);
        bodyScrollview.addView(lnbtypelistLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        this.addView(bodyScrollview, bobyWidth , bodyHeight);
    }
	
	public void updateView()
    {
		
    }
	
	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
