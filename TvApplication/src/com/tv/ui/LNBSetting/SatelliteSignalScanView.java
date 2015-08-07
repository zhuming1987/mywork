package com.tv.ui.LNBSetting;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvSearchTypes;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ChannelSearch.DTVChannelSearchDialog;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;

import android.content.Context;
import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SatelliteSignalScanView extends LinearLayout implements OnKeyListener
{
	private SatelliteSignalScanDialog parentDialog;
	private DialogListener dialogListener;
	private Context mContext;
	private static int index;
	
	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dip = TvUIControler.getInstance().getDipDiv();
    
    private LinearLayout mainLayout,bodyLayout;
    private TextView titleText;
    private LinearLayout scanModeLayout,netsearchLayout,serviceTypeLayout,searchoptionLayout,okLayout;
    private LinearLayout modeItemLayout,netsearchItemLayout,typeItemLayout,searchoptionItemLayout;
    private TextView scanModeTitle,netsearchTitle,serviceTypeTitle,searchoptionTitle;
    private TextView scanModeText,netsearchText,serviceTypeText,searchoptionText;
    private Button okButton;
    private ImageView divideLine;   
    private ImageView leftArrow,rightArrow,leftArrow2,rightArrow2,leftArrow3,rightArrow3,leftArrow4,rightArrow4;
	
    private int mainWidth = (int)(1100/div);
    private int titleHeight = (int)(120/div);
    private int bodyHeight = (int)(960/div);
    
    private final String[] mScanMode = TvContext.context.getResources().getStringArray(R.array.str_array_scan_mode);
    private final String[] mNetworkSearch = TvContext.context.getResources().getStringArray(R.array.str_array_network_search_option);
    private final String[] mServicetype = TvContext.context.getResources().getStringArray(R.array.str_array_service_type);
    private final String[] mSearchOption = TvContext.context.getResources().getStringArray(R.array.str_array_search_option);
	public SatelliteSignalScanView(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
        setOrientation(LinearLayout.VERTICAL);
		initView();
	}
	
	private void initView()
	{
		mainLayout = new LinearLayout(mContext);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(mainLayout, new LayoutParams(mainWidth,LayoutParams.MATCH_PARENT));
        
        titleText = initTextView(mContext.getString(R.string.str_lnb_signal_scan), (int) (52 / dip),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        titleText.setBackgroundColor(Color.rgb(53,152,220));
        mainLayout.addView(titleText, LayoutParams.MATCH_PARENT, titleHeight);
        
        bodyLayout = new LinearLayout(mContext);
        bodyLayout.setOrientation(LinearLayout.VERTICAL);
        bodyLayout.setBackgroundColor(Color.rgb(25,26,28));
        mainLayout.addView(bodyLayout, LayoutParams.MATCH_PARENT, bodyHeight);
        
        LayoutParams itemParams = new LayoutParams((int)(400/div),LayoutParams.WRAP_CONTENT);
        itemParams.gravity = Gravity.CENTER_VERTICAL;
        itemParams.leftMargin = (int)(30/div); 
        
        LayoutParams contentParams = new LayoutParams((int)(600/div),LayoutParams.WRAP_CONTENT);
        contentParams.gravity = Gravity.CENTER_VERTICAL;
        contentParams.leftMargin = (int)(20/div);   
        
        scanModeLayout = new LinearLayout(mContext);
        scanModeLayout.setOrientation(LinearLayout.HORIZONTAL);
        scanModeTitle = new TextView(mContext); 
        scanModeTitle.setTextSize((int) (36 / dip));
        scanModeTitle.setText(R.string.str_lnb_signal_scan_mode_title);
        scanModeLayout.addView(scanModeTitle,itemParams);
        modeItemLayout = new LinearLayout(mContext);
        modeItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        modeItemLayout.setGravity(Gravity.CENTER_VERTICAL);
        leftArrow = new ImageView(mContext);
        leftArrow.setBackgroundResource(R.drawable.simulation_arrow_left);
        modeItemLayout.addView(leftArrow,(int)(42/div),LayoutParams.WRAP_CONTENT);
        scanModeText = new TextView(mContext);
        scanModeText.setTextSize((int) (36 / dip));
        scanModeText.setGravity(Gravity.CENTER);
        modeItemLayout.addView(scanModeText,(int)(518/div),LayoutParams.WRAP_CONTENT);
        rightArrow = new ImageView(mContext);
        rightArrow.setBackgroundResource(R.drawable.simulation_arrow_right);
        modeItemLayout.addView(rightArrow,(int)(42/div),LayoutParams.WRAP_CONTENT);
        scanModeLayout.addView(modeItemLayout,contentParams);
        scanModeLayout.setOnKeyListener(this);
        scanModeLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					scanModeTitle.setTextColor(Color.BLUE);
					scanModeText.setTextColor(Color.BLACK);
					modeItemLayout.setBackgroundResource(R.drawable.blue_sel_bg);
				}
				else 
				{
					scanModeTitle.setTextColor(Color.WHITE);
					scanModeText.setTextColor(Color.WHITE);
					modeItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
				}
			}
		});
        bodyLayout.addView(scanModeLayout, LayoutParams.MATCH_PARENT,titleHeight);
        
        addDivideLine();
        
        netsearchLayout = new LinearLayout(mContext);
        netsearchLayout.setOrientation(LinearLayout.HORIZONTAL);
        netsearchTitle = new TextView(mContext);  
        netsearchTitle.setTextSize((int) (36 / dip));
        netsearchTitle.setText(R.string.str_lnb_signal_network_search_title);
        netsearchLayout.addView(netsearchTitle,itemParams);
        netsearchItemLayout = new LinearLayout(mContext);
        netsearchItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        netsearchItemLayout.setGravity(Gravity.CENTER_VERTICAL);
        leftArrow3 = new ImageView(mContext);
        leftArrow3.setBackgroundResource(R.drawable.simulation_arrow_left);
        netsearchItemLayout.addView(leftArrow3,(int)(42/div),LayoutParams.WRAP_CONTENT);
        netsearchText = new TextView(mContext);
        netsearchText.setTextSize((int) (36 / dip));
        netsearchText.setGravity(Gravity.CENTER);
        netsearchItemLayout.addView(netsearchText,(int)(518/div),LayoutParams.WRAP_CONTENT);
        rightArrow3 = new ImageView(mContext);
        rightArrow3.setBackgroundResource(R.drawable.simulation_arrow_right);
        netsearchItemLayout.addView(rightArrow3,(int)(42/div),LayoutParams.WRAP_CONTENT);
        netsearchLayout.addView(netsearchItemLayout,contentParams);
        netsearchLayout.setOnKeyListener(this);
        netsearchLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					netsearchTitle.setTextColor(Color.BLUE);
					netsearchText.setTextColor(Color.BLACK);
					netsearchItemLayout.setBackgroundResource(R.drawable.blue_sel_bg);
				}
				else 
				{
					netsearchTitle.setTextColor(Color.WHITE);
					netsearchText.setTextColor(Color.WHITE);
					netsearchItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
				}
			}
		});
        bodyLayout.addView(netsearchLayout, LayoutParams.MATCH_PARENT,titleHeight);
        
        addDivideLine();
        
        serviceTypeLayout = new LinearLayout(mContext);
        serviceTypeLayout.setOrientation(LinearLayout.HORIZONTAL);
        serviceTypeTitle = new TextView(mContext);  
        serviceTypeTitle.setTextSize((int) (36 / dip));
        serviceTypeTitle.setText(R.string.str_lnb_signal_service_type_title);
        serviceTypeLayout.addView(serviceTypeTitle,itemParams);
        typeItemLayout = new LinearLayout(mContext); 
        typeItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        typeItemLayout.setGravity(Gravity.CENTER_VERTICAL);
        leftArrow2 = new ImageView(mContext);
        leftArrow2.setBackgroundResource(R.drawable.simulation_arrow_left);
        typeItemLayout.addView(leftArrow2,(int)(42/div),LayoutParams.WRAP_CONTENT);
        serviceTypeText = new TextView(mContext);
        serviceTypeText.setTextSize((int) (36 / dip));
        serviceTypeText.setGravity(Gravity.CENTER);
        typeItemLayout.addView(serviceTypeText,(int)(518/div),LayoutParams.WRAP_CONTENT);
        rightArrow2 = new ImageView(mContext);
        rightArrow2.setBackgroundResource(R.drawable.simulation_arrow_right);
        typeItemLayout.addView(rightArrow2,(int)(42/div),LayoutParams.WRAP_CONTENT);
        serviceTypeLayout.addView(typeItemLayout,contentParams);     
        serviceTypeLayout.setOnKeyListener(this);
        serviceTypeLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					serviceTypeTitle.setTextColor(Color.BLUE);
					serviceTypeText.setTextColor(Color.BLACK);
					typeItemLayout.setBackgroundResource(R.drawable.blue_sel_bg);
				}
				else 
				{
					serviceTypeTitle.setTextColor(Color.WHITE);
					serviceTypeText.setTextColor(Color.WHITE);
					typeItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
				}
			}
		});
        bodyLayout.addView(serviceTypeLayout, LayoutParams.MATCH_PARENT,titleHeight);
        
        addDivideLine();
        
        searchoptionLayout = new LinearLayout(mContext);
        searchoptionLayout.setOrientation(LinearLayout.HORIZONTAL);
        searchoptionTitle = new TextView(mContext);  
        searchoptionTitle.setTextSize((int) (36 / dip));
        searchoptionTitle.setText(R.string.str_lnb_signal_search_option_title);
        searchoptionLayout.addView(searchoptionTitle,itemParams);
        searchoptionItemLayout = new LinearLayout(mContext);        
        searchoptionItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        searchoptionItemLayout.setGravity(Gravity.CENTER_VERTICAL);
        leftArrow4 = new ImageView(mContext);
        leftArrow4.setBackgroundResource(R.drawable.simulation_arrow_left);
        searchoptionItemLayout.addView(leftArrow4,(int)(42/div),LayoutParams.WRAP_CONTENT);
        searchoptionText = new TextView(mContext);
        searchoptionText.setTextSize((int) (36 / dip));
        searchoptionText.setGravity(Gravity.CENTER);
        searchoptionItemLayout.addView(searchoptionText,(int)(518/div),LayoutParams.WRAP_CONTENT);
        rightArrow4 = new ImageView(mContext);
        rightArrow4.setBackgroundResource(R.drawable.simulation_arrow_right);
        searchoptionItemLayout.addView(rightArrow4,(int)(42/div),LayoutParams.WRAP_CONTENT);
        searchoptionLayout.addView(searchoptionItemLayout,contentParams);     
        searchoptionLayout.setOnKeyListener(this);
        searchoptionLayout.setOnFocusChangeListener(new OnFocusChangeListener() 
        {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					searchoptionTitle.setTextColor(Color.BLUE);
					searchoptionText.setTextColor(Color.BLACK);
					searchoptionItemLayout.setBackgroundResource(R.drawable.blue_sel_bg);
				}
				else 
				{
					searchoptionTitle.setTextColor(Color.WHITE);
					searchoptionText.setTextColor(Color.WHITE);
					searchoptionItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
				}
			}
		}); 
        bodyLayout.addView(searchoptionLayout, LayoutParams.MATCH_PARENT,titleHeight);
        
        addDivideLine();
        
        okLayout = new LinearLayout(mContext);
        okLayout.setGravity(Gravity.CENTER);
        okButton = new Button(mContext);
        okButton.setBackgroundResource(R.drawable.setting_unselect_bg);
        okButton.setText(R.string.str_lnb_signal_comfirm_button);
        okButton.setTextSize((int)(30/dip));  
        okLayout.addView(okButton,(int)(600/div),(int)(60/div));
        okLayout.setOnKeyListener(this);
        okLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startSearchChannel();
            }
        });
        okLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					okButton.setTextColor(Color.BLACK);
					okButton.setBackgroundResource(R.drawable.blue_sel_bg);
				}
				else 
				{
					okButton.setTextColor(Color.WHITE);
					okButton.setBackgroundResource(R.drawable.setting_unselect_bg);
				}
			}
		});
        bodyLayout.addView(okLayout,LayoutParams.MATCH_PARENT,titleHeight);       
	}
	
	public void updateView() 
	{
		// TODO Auto-generated method stub
        index = 5;
        scanModeLayout.setFocusable(false);
        netsearchLayout.setFocusable(false);
        serviceTypeLayout.setFocusable(false);
        searchoptionLayout.setFocusable(false);
        okLayout.setFocusable(true);
		okLayout.requestFocus();
        
        scanModeText.setText(mScanMode[TvSearchTypes.ScanModeindex]);
        netsearchText.setText(mNetworkSearch[TvSearchTypes.NetworkSearchindex]);
        serviceTypeText.setText(mServicetype[TvSearchTypes.Servicetypeindex]);
        searchoptionText.setText(mSearchOption[TvSearchTypes.SearchOptionindex]);
        OptionLinkNetWork();
	}
	
	public void setParentDialog(SatelliteSignalScanDialog parentDialog) 
    {
		this.parentDialog = parentDialog;
	}

	private TextView initTextView(String str, int textSize, int textColor, int gravity)
    {
        TextView text = new TextView(mContext);
        text.setText(str);
        text.setTextSize(textSize);
        text.setTextColor(textColor);
        text.setGravity(gravity);
        return text;
    }
	 
    private void addDivideLine()
    {
		divideLine = new ImageView(mContext);
		divideLine.setBackgroundResource(R.drawable.setting_line);
		divideLine.setLeft((int)(20/div));
		bodyLayout.addView(divideLine, new LayoutParams((int) (1040 / div),(int) (2 / div)));
    }    
    
    private void startSearchChannel()
    {	
    	Log.d("wangxian", "TvSearchTypes.ScanModeindex = " + TvSearchTypes.ScanModeindex + "=========TvSearchTypes.NetworkSearchindex = " + TvSearchTypes.NetworkSearchindex + "======TvSearchTypes.Servicetypeindex==" + TvSearchTypes.Servicetypeindex);
    	TvPluginControler.getInstance().getChannelManager().setScanOption(TvSearchTypes.ScanModeindex,
    			TvSearchTypes.NetworkSearchindex, TvSearchTypes.Servicetypeindex);
    	Log.v("zhm","DVB_S2 Start Search!!");
    	try {
			TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV2.ordinal());
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
    	parentDialog.processCmd(TvConfigTypes.TV_DIALOG_SATELLITE_SCAN,DialogCmd.DIALOG_DISMISS, null);
    	DTVChannelSearchDialog dtvChannelSearchDialog = new DTVChannelSearchDialog();
		dtvChannelSearchDialog.setDialogListener(dialogListener);
		dtvChannelSearchDialog.processCmd(TvConfigTypes.TV_DIALOG_DTV_AUTO_SEARCH, DialogCmd.DIALOG_SHOW, "DVBS");
    }

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
			{
				switch(index)
				{
					case 1:
						if(TvSearchTypes.SearchOptionindex == 0)
						{
							scanModeLayout.setFocusable(false);
					        netsearchLayout.setFocusable(false);
					        serviceTypeLayout.setFocusable(true);
					        searchoptionLayout.setFocusable(false);
					        okLayout.setFocusable(false);
							serviceTypeLayout.requestFocus();
							index = 3;
						}
						else
						{
							scanModeLayout.setFocusable(false);
					        netsearchLayout.setFocusable(true);
					        serviceTypeLayout.setFocusable(false);
					        searchoptionLayout.setFocusable(false);
					        okLayout.setFocusable(false);
							netsearchLayout.requestFocus();
							index = 2;
						}
						break;
					case 2:
						scanModeLayout.setFocusable(false);
				        netsearchLayout.setFocusable(false);
				        serviceTypeLayout.setFocusable(true);
				        searchoptionLayout.setFocusable(false);
				        okLayout.setFocusable(false);
						serviceTypeLayout.requestFocus();
						index = 3;
						break;
					case 3:
						scanModeLayout.setFocusable(false);
				        netsearchLayout.setFocusable(false);
				        serviceTypeLayout.setFocusable(false);
				        searchoptionLayout.setFocusable(true);
				        okLayout.setFocusable(false);
				        searchoptionLayout.requestFocus();
						index = 4;
						break;	
					case 4:
						scanModeLayout.setFocusable(false);
				        netsearchLayout.setFocusable(false);
				        serviceTypeLayout.setFocusable(false);
				        searchoptionLayout.setFocusable(false);
				        okLayout.setFocusable(true);
						okLayout.requestFocus();
						index = 5;
						break;
					case 5:
						scanModeLayout.setFocusable(true);
				        netsearchLayout.setFocusable(false);
				        serviceTypeLayout.setFocusable(false);
				        searchoptionLayout.setFocusable(false);
				        okLayout.setFocusable(false);
						scanModeLayout.requestFocus();
						index = 1;
						break;
				}
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
			{
				switch(index)
				{
					case 1:
						scanModeLayout.setFocusable(false);
				        netsearchLayout.setFocusable(false);
				        serviceTypeLayout.setFocusable(false);
				        searchoptionLayout.setFocusable(false);
				        okLayout.setFocusable(true);
						okLayout.requestFocus();
						index = 5;
						break;
					case 2:
						scanModeLayout.setFocusable(true);
				        netsearchLayout.setFocusable(false);
				        serviceTypeLayout.setFocusable(false);
				        searchoptionLayout.setFocusable(false);
				        okLayout.setFocusable(false);
				        scanModeLayout.requestFocus();
						index = 1;
						break;
					case 3:
						if(TvSearchTypes.SearchOptionindex == 0)
						{
							scanModeLayout.setFocusable(true);
					        netsearchLayout.setFocusable(false);
					        serviceTypeLayout.setFocusable(false);
					        searchoptionLayout.setFocusable(false);
					        okLayout.setFocusable(false);
					        scanModeLayout.requestFocus();
							index = 1;
						}
						else
						{
							scanModeLayout.setFocusable(false);
					        netsearchLayout.setFocusable(true);
					        serviceTypeLayout.setFocusable(false);
					        searchoptionLayout.setFocusable(false);
					        okLayout.setFocusable(false);
					        netsearchLayout.requestFocus();
							index = 2;
						}
						break;
					case 4:
						scanModeLayout.setFocusable(false);
				        netsearchLayout.setFocusable(false);
				        serviceTypeLayout.setFocusable(true);
				        searchoptionLayout.setFocusable(false);
				        okLayout.setFocusable(false);
				        serviceTypeLayout.requestFocus();
				        index = 3;
						break;
					case 5:
						scanModeLayout.setFocusable(false);
				        netsearchLayout.setFocusable(false);
				        serviceTypeLayout.setFocusable(false);
				        searchoptionLayout.setFocusable(true);
				        okLayout.setFocusable(false);
				        searchoptionLayout.requestFocus();
						index = 4;
						break;
				}
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			{
				switch(index)
				{
					case 1:
						if(TvSearchTypes.ScanModeindex > 0)
						{
							TvSearchTypes.ScanModeindex--;
							updatescanmode(TvSearchTypes.ScanModeindex);
						}
						else
						{
							TvSearchTypes.ScanModeindex = mScanMode.length - 1;
							updatescanmode(TvSearchTypes.ScanModeindex);
						}
						break;
					case 2:
						if(TvSearchTypes.NetworkSearchindex > 0)
						{
							TvSearchTypes.NetworkSearchindex--;
							updatenetworksearch(TvSearchTypes.NetworkSearchindex);
						}
						else
						{
							TvSearchTypes.NetworkSearchindex = mNetworkSearch.length - 1;
							updatenetworksearch(TvSearchTypes.NetworkSearchindex);
						}
						break;
					case 3:
						if(TvSearchTypes.Servicetypeindex > 0)
						{
							TvSearchTypes.Servicetypeindex--;
							updateservicetype(TvSearchTypes.Servicetypeindex);
						}
						else
						{
							TvSearchTypes.Servicetypeindex = mServicetype.length - 1;
							updateservicetype(TvSearchTypes.Servicetypeindex);
						}
						break;
					case 4:
						if(TvSearchTypes.SearchOptionindex > 0)
						{
							TvSearchTypes.SearchOptionindex--;
							updatesearchoptione(TvSearchTypes.SearchOptionindex);
						}
						else
						{
							TvSearchTypes.SearchOptionindex = mSearchOption.length - 1;
							updatesearchoptione(TvSearchTypes.SearchOptionindex);
						}
						OptionLinkNetWork();
						break;
				}
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
			{
				switch(index)
				{
					case 1:
						if(TvSearchTypes.ScanModeindex < mScanMode.length - 1)
						{
							TvSearchTypes.ScanModeindex++;
							updatescanmode(TvSearchTypes.ScanModeindex);
						}
						else
						{
							TvSearchTypes.ScanModeindex = 0;
							updatescanmode(TvSearchTypes.ScanModeindex);
						}
						break;
					case 2:
						if(TvSearchTypes.NetworkSearchindex < mNetworkSearch.length - 1)
						{
							TvSearchTypes.NetworkSearchindex++;
							updatenetworksearch(TvSearchTypes.NetworkSearchindex);
						}
						else
						{
							TvSearchTypes.NetworkSearchindex = 0;
							updatenetworksearch(TvSearchTypes.NetworkSearchindex);
						}
						break;
					case 3:
						if(TvSearchTypes.Servicetypeindex < mServicetype.length - 1)
						{
							TvSearchTypes.Servicetypeindex++;
							updateservicetype(TvSearchTypes.Servicetypeindex);
						}
						else
						{
							TvSearchTypes.Servicetypeindex = 0;
							updateservicetype(TvSearchTypes.Servicetypeindex);
						}
						break;
					case 4:
						if(TvSearchTypes.SearchOptionindex < mSearchOption.length - 1)
						{
							TvSearchTypes.SearchOptionindex++;
							updatesearchoptione(TvSearchTypes.SearchOptionindex);
						}
						else
						{
							TvSearchTypes.SearchOptionindex = 0;
							updatesearchoptione(TvSearchTypes.SearchOptionindex);
						}
						OptionLinkNetWork();
						break;
				    }
			}
		}
		return false;
	}
	private void updatescanmode(int index)
	{
		scanModeText.setText(mScanMode[index]);
	}
	private void updatenetworksearch(int index)
	{
		netsearchText.setText(mNetworkSearch[index]);
	}
	private void updateservicetype(int index)
	{
        serviceTypeText.setText(mServicetype[index]);
	}
	private void updatesearchoptione(int index)
	{
        searchoptionText.setText(mSearchOption[index]);
	}
	
	private void OptionLinkNetWork(){
        if(TvSearchTypes.SearchOptionindex == 0){
        	netsearchText.setTextColor(Color.GRAY);
        	netsearchTitle.setTextColor(Color.GRAY);
        	leftArrow3.setVisibility(View.INVISIBLE);
        	rightArrow3.setVisibility(View.INVISIBLE);
        }else{
        	netsearchText.setTextColor(Color.WHITE);
        	netsearchTitle.setTextColor(Color.WHITE);
        	leftArrow3.setVisibility(View.VISIBLE);
        	rightArrow3.setVisibility(View.VISIBLE);
        }
	}
}
