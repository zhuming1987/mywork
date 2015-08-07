package com.tv.ui.ChannelSearch;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.data.TvSatelliteListData;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.plugin.tvfuncs.ChannelManager.DtvManualSearchListener;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ChannelSearch.DVBSEnumItemView.EnumItemListener;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.widgets.ButtonConfigView;
import com.tv.ui.widgets.ItemView;

public class DVBSAutoSearchSettingView extends LinearLayout implements OnKeyListener {
	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dip = TvUIControler.getInstance().getDipDiv();
    
    private Context context;
    private LeftViewTitleLayout titleLayout;
    private ScrollView bodyScrollview;
    private LeftViewBodyLayout bodyLayout;
    
    private LinearLayout polarizationLayout,scanModeLayout,netsearchLayout,serviceTypeLayout;
    private DVBSEnumItemView polarizationItemView,scanModeItemView,netsearchItemView,serviceTypeItemView;
    private TextView satelliteText,transponderText,lnbtypeText,polarizationText,scanModeText,netsearchText,serviceTypeText;
    
    private ButtonConfigView startSearchButton,satelliteButton,transponderButton,lnbtypeButton;
    
    private DVBSAutoSearchSettingDialog parentDialog = null;
    
    private int bobyWidth = (int)(845/div);
    private int bodyHeight = (int)(960/div);
    
    TvEnumSetData scanmodeData;
    TvEnumSetData networkData;
    TvEnumSetData servicetData;
    TvEnumSetData polarizationData;
    TvEnumSetData searchoptionData;
    
    public DVBSAutoSearchSettingView(Context context,DVBSAutoSearchSettingDialog parentDialog)
    {
        super(context);
        this.context = context;
        this.parentDialog = parentDialog;
        this.setOrientation(LinearLayout.VERTICAL);

        titleLayout = new LeftViewTitleLayout(context);
        this.addView(titleLayout);

        bodyScrollview = new ScrollView(mContext);
        bodyScrollview.setBackgroundColor(Color.parseColor("#191919"));
        bodyScrollview.setVerticalScrollBarEnabled(true);
        bodyScrollview.setHorizontalScrollBarEnabled(false);
        bodyScrollview.setScrollbarFadingEnabled(true);
        bodyScrollview.setAlwaysDrawnWithCacheEnabled(true);
        bodyScrollview.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        bodyLayout = new LeftViewBodyLayout(context);
        bodyLayout.setOrientation(LinearLayout.VERTICAL);
        bodyScrollview.addView(bodyLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        this.addView(bodyScrollview, bobyWidth , bodyHeight);

        LayoutParams rightTextParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        
        satelliteButton = new ButtonConfigView(context);
        satelliteText = newTextView();
        satelliteText.setLayoutParams(rightTextParams);
        ItemView satelliteItem = new ItemView(context);
        satelliteItem.setTipText(R.string.str_satellite_list);
        satelliteItem.addRightView(satelliteButton);
        satelliteItem.addRightView(satelliteText);
        bodyLayout.addView(satelliteItem, true);
        satelliteButton.setButtonText("");
        satelliteButton.requestFocus();
        satelliteButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	setSatellite();
            }
        });
        
        transponderButton = new ButtonConfigView(context);
        transponderText = newTextView();
        transponderText.setLayoutParams(rightTextParams);
        ItemView transponderItem = new ItemView(context);
        transponderItem.setTipText(R.string.str_satellite_list);
        transponderItem.addRightView(transponderButton);
        transponderItem.addRightView(transponderText);
        bodyLayout.addView(transponderItem, true);
        transponderButton.setButtonText("");
        transponderButton.requestFocus();
        transponderButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	
            }
        });
        
        lnbtypeButton = new ButtonConfigView(context);
        lnbtypeText = newTextView();
        lnbtypeText.setLayoutParams(rightTextParams);
        ItemView lnbtypeItem = new ItemView(context);
        lnbtypeItem.setTipText(R.string.str_satellite_list);
        lnbtypeItem.addRightView(lnbtypeButton);
        lnbtypeItem.addRightView(lnbtypeText);
        bodyLayout.addView(lnbtypeItem, true);
        lnbtypeButton.setButtonText("");
        lnbtypeButton.requestFocus();
        lnbtypeButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        
		scanModeLayout = new LinearLayout(context);
        scanModeLayout.setOrientation(LinearLayout.HORIZONTAL);
        scanModeLayout.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams lv1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lv1.setMargins((int) (15 / div), 0, 0, 0);
		bodyLayout.addView(scanModeLayout, lv1);
		scanModeText = initTextView(context.getString(R.string.str_lnb_signal_scan_mode_title), (int) (36 / dip),
        		Color.WHITE, Gravity.CENTER_VERTICAL);
		scanModeItemView = new DVBSEnumItemView(context, (int) (500 / div),(int) (50 / div));
		scanModeItemView.setItemListener(scanmodeListener);
		LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams((int) (200 / div),LayoutParams.WRAP_CONTENT);
		lp1.setMargins(0, 0, (int) (30 / div), 0);
		scanModeLayout.addView(scanModeText, lp1);
		scanModeLayout.addView(scanModeItemView);
		ImageView line1 = new ImageView(context);
		line1.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp1 = new LinearLayout.LayoutParams(
				(int) (740 / div), 1);
		lineLp1.setMargins((int) (30 / div), (int) (5 / div),
				(int) (30 / div), (int) (5 / div));
		bodyLayout.addView(line1, lineLp1);
		
		netsearchLayout = new LinearLayout(context);
        netsearchLayout.setOrientation(LinearLayout.HORIZONTAL);
        netsearchLayout.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams lv2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lv2.setMargins((int) (15 / div), 0, 0, 0);
		bodyLayout.addView(netsearchLayout, lv2);
		netsearchText = initTextView(context.getString(R.string.str_lnb_signal_network_search_title), (int) (36 / dip),
        		Color.WHITE, Gravity.CENTER_VERTICAL);
		netsearchItemView = new DVBSEnumItemView(context, (int) (500 / div),(int) (50 / div));
		netsearchItemView.setItemListener(netsearchListener);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams((int) (200 / div),LayoutParams.WRAP_CONTENT);
		lp2.setMargins(0, 0, (int) (30 / div), 0);
		netsearchLayout.addView(netsearchText, lp2);
		netsearchLayout.addView(netsearchItemView);
		ImageView line2 = new ImageView(context);
		line2.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp2 = new LinearLayout.LayoutParams(
				(int) (740 / div), 1);
		lineLp2.setMargins((int) (30 / div), (int) (5 / div),
				(int) (30 / div), (int) (5 / div));
		bodyLayout.addView(line2, lineLp2);
		
		serviceTypeLayout = new LinearLayout(context);
        serviceTypeLayout.setOrientation(LinearLayout.HORIZONTAL);
        serviceTypeLayout.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams lv3 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lv3.setMargins((int) (15 / div), 0, 0, 0);
		bodyLayout.addView(serviceTypeLayout, lv3);
		serviceTypeText = initTextView(context.getString(R.string.str_lnb_signal_service_type_title), (int) (36 / dip),
        		Color.WHITE, Gravity.CENTER_VERTICAL);
		serviceTypeItemView = new DVBSEnumItemView(context, (int) (500 / div),(int) (50 / div));
		serviceTypeItemView.setItemListener(servicetypeListener);
		LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams((int) (200 / div),LayoutParams.WRAP_CONTENT);
		lp3.setMargins(0, 0, (int) (30 / div), 0);
		serviceTypeLayout.addView(serviceTypeText, lp3);
		serviceTypeLayout.addView(serviceTypeItemView);
		ImageView line3 = new ImageView(context);
		line3.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp3 = new LinearLayout.LayoutParams(
				(int) (740 / div), 1);
		lineLp3.setMargins((int) (30 / div), (int) (5 / div),
				(int) (30 / div), (int) (0 / div));
		bodyLayout.addView(line3, lineLp3);
        
        polarizationLayout = new LinearLayout(context);
        polarizationLayout.setOrientation(LinearLayout.HORIZONTAL);
        polarizationLayout.setGravity(Gravity.CENTER_VERTICAL);
		LinearLayout.LayoutParams lv = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		lv.setMargins((int) (15 / div), 0, 0, 0);
		bodyLayout.addView(polarizationLayout, lv);
		polarizationText = initTextView(context.getString(R.string.str_lnb_transponder_polarization), (int) (36 / dip),
      		Color.WHITE, Gravity.CENTER_VERTICAL);
		polarizationItemView = new DVBSEnumItemView(context, (int) (500 / div),(int) (50 / div));
		polarizationItemView.setItemListener(polarizationListener);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) (200 / div),LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, (int) (30 / div), 0);
		polarizationLayout.addView(polarizationText, lp);
		polarizationLayout.addView(polarizationItemView);
		ImageView line = new ImageView(context);
		line.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(
				(int) (740 / div), 1);
		lineLp.setMargins((int) (30 / div), (int) (5 / div),
				(int) (30 / div), (int) (5 / div));
		bodyLayout.addView(line, lineLp);
		
        ItemView buttonItem = new ItemView(context);
        startSearchButton = new ButtonConfigView(context);
        startSearchButton.setButtonText(R.string.start_search);
        startSearchButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startSearchChannel();
            }
        });
        buttonItem.addRightView(startSearchButton);
        bodyLayout.addView(buttonItem, true);
      
        Uiinit();
    }

    public void updateView(ChannelSearchData data)
    {
    	
    }

    private void startSearchChannel()
    {
    	
    }
    private TextView newTextView()
    {
        return newTextView(Gravity.CENTER, 0);
    }

    private TextView newTextView(int gravity, int leftPadding)
    {
        TextView text = new TextView(context);
        text.setTextSize((int) (36 / dip));
        text.setTextColor(Color.WHITE);
        text.setGravity(gravity);
        text.setPadding(leftPadding, 0, 0, 0);
        text.setSingleLine();
        return text;
    }
    private TextView initTextView(String str, int textSize, int textColor, int gravity)
    {
        TextView text = new TextView(context);
        text.setText(str);
        text.setTextSize(textSize);
        text.setTextColor(textColor);
        text.setGravity(gravity);
        text.getPaint().setAntiAlias(true);
        text.setSingleLine(true);
        text.setEllipsize(TruncateAt.MARQUEE);
        text.setMarqueeRepeatLimit(-1);
        text.setSelected(true);
        return text;
    }
    private void Uiinit()
    {
    	titleLayout.bindData("tv_auto_search.png", "DVBS Auto Search");
    	
    	TvSatelliteListData CurrentSatelliteInfo = TvPluginControler.getInstance().getChannelManager().getCurrentSatelliteInfo();
    	satelliteButton.setButtonText(CurrentSatelliteInfo.getNumber() + " " + CurrentSatelliteInfo.getName() + " " + CurrentSatelliteInfo.getBand());
    	
    	polarizationData = (TvEnumSetData) TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_DVBS_MANUAL_POLARIZATION);
    	scanmodeData = (TvEnumSetData) TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_DVBS_MANUAL_SCAN_MODE);
    	networkData = (TvEnumSetData) TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_DVBS_MANUAL_NETWORK_SEARCH);
    	servicetData = (TvEnumSetData) TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_DVBS_MANUAL_SERVICE);
    	
    	polarizationItemView.setBindData(polarizationData.getCurrentIndex(), polarizationData.getEnumList());
    	scanModeItemView.setBindData(scanmodeData.getCurrentIndex(), scanmodeData.getEnumList());
    	netsearchItemView.setBindData(networkData.getCurrentIndex(), networkData.getEnumList());
    	serviceTypeItemView.setBindData(servicetData.getCurrentIndex(), servicetData.getEnumList());
    }
    
    public void setSatellite()
    {
    	parentDialog.dismissUI();
    	DVBSSatelliteListDialog dvbsatellitelistDialog = new DVBSSatelliteListDialog();
    	dvbsatellitelistDialog.setDialogListener(null);
    	dvbsatellitelistDialog.processCmd(TvConfigTypes.TV_DIALOG_DVBS_SATELLITE_LIST, DialogCmd.DIALOG_SHOW, null);
    }
    
    
    
    public EnumItemListener polarizationListener = new EnumItemListener() {
		
		@Override
		public void onEnumItemChangeListener(String data, int index) {
			// TODO Auto-generated method stub
			Log.i("zhm", getClass().getName() + " " + data + "/" + index);
			polarizationData.setCurrentIndex(index);
			TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_DVBS_MANUAL_POLARIZATION, polarizationData);
		}
	};
    public EnumItemListener scanmodeListener = new EnumItemListener() {
		
		@Override
		public void onEnumItemChangeListener(String data, int index) {
			// TODO Auto-generated method stub
			Log.i("zhm", getClass().getName() + " " + data + "/" + index);
			scanmodeData.setCurrentIndex(index);
			TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_DVBS_MANUAL_SCAN_MODE, scanmodeData);
		}
	};
    public EnumItemListener netsearchListener = new EnumItemListener() {
		
		@Override
		public void onEnumItemChangeListener(String data, int index) {
			// TODO Auto-generated method stub
			Log.i("zhm", getClass().getName() + " " + data + "/" + index);
			networkData.setCurrentIndex(index);
			TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_DVBS_MANUAL_NETWORK_SEARCH, networkData);
		}
	};
    public EnumItemListener servicetypeListener = new EnumItemListener() {
		
		@Override
		public void onEnumItemChangeListener(String data, int index) {
			// TODO Auto-generated method stub
			Log.i("zhm", getClass().getName() + " " + data + "/" + index);
			servicetData.setCurrentIndex(index);
			TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_DVBS_MANUAL_SERVICE, servicetData);
		}
	};
	DtvManualSearchListener dtvManualSearchListener = new DtvManualSearchListener() {
		
		@Override
		public void OnDtvManualSearchListener(ChannelSearchData data)
		{
			// TODO Auto-generated method stub
			updateView(data);
		}
	};
	@Override
	public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
		// TODO Auto-generated method stub
		return false;
	}
}