package com.tv.ui.LNBSetting;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvUIControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.data.MenuItemFactory;
import com.tv.framework.data.ChannelSearchData;
import com.tv.framework.data.TvSatelliteInfoData;
import com.tv.framework.data.TvSatelliteListData;
import com.tv.framework.data.TvTransponderListData;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvSearchTypes;
import com.tv.framework.define.TvConfigTypes.EnumMotorSet;
import com.tv.framework.define.TvSearchTypes.TRANSPONDER_OPERATION_TYPE;
import com.tv.framework.plugin.tvfuncs.ChannelManager.DtvManualSearchListener;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SettingView.TvSettingItemView.OnSettingKeyDownListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LNBSettingView extends LinearLayout implements OnSettingKeyDownListener, DialogListener
{
	boolean flagListInit=false;
	private String tag = "LNBSettingView";
	private Context mContext;
	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dip = TvUIControler.getInstance().getDipDiv();
    private LNBSettingView thisView=this;
    private LinearLayout overallLayout;
    private TextView titleText;
    private LinearLayout typeLayout,bodyLayout,signalLayout;
    private SignalInfoView signalInfoView;
    private TextView satelliteListText,lnbSetupListText,responderListText;
    private ImageView divideLine; 
    private ScrollView satellitelistScrollview,lnbSetupListScrollView;
    private LinearLayout satellitelistLayout,lnbSetupListLayout,responderLayout,reponderlistLayout,responderButtonLayout;
    private LinearLayout redLayout,redPositonLayout,greenLayout,greenLimitLayout,yellowLayout,yellowLocationLayout,blueLayout, infoLayout;
    private ImageView redImageView,greenImageView,blueImageView,yellowImageView;
    private TextView redtipsTextView,greentipsTextView,bluetipsTextView,yellowtipsTextView;
    private int columnsIndex=1;
    private int rowsIndex=0;
    private int IndexOfSatellite=0;
    private int IndexOfSetup=0;
    private int []IndexOfResponderChild;
    private int []SizeOfResponderChild;
    
    public static int  NUM_SATELLITE=1;
    public static int NUM_SETUP=NUM_SATELLITE+1;
    public static int NUM_RESPONDER=NUM_SETUP+1;
    public static boolean siganlScanFlag = false;
    

    private ArrayList<TvSatelliteListData> tvSatelliteList = new ArrayList<TvSatelliteListData>();
    private ArrayList<SatelliteItemView> tvSatelliteItemView = new ArrayList<SatelliteItemView>();
    private ArrayList<SetupItemView> tvSetupItemView = new ArrayList<SetupItemView>();
    private ArrayList<TransponderItemView> tvTransponderItemViews = new ArrayList<TransponderItemView>();
    private TvSatelliteListData satelliteList = new TvSatelliteListData();
    private TvSatelliteInfoData satelliteInfo = new TvSatelliteInfoData();
    private SatelliteItemView selecteItem;
    private SetupItemView setupItemView;
    private TransponderItemView transponderItemView;
    private LNBSettingDialog parentDialog = null;
    private QuickKeyListener quickKeyListener;
    private int data_count = 0;
    private int data_counts = 0;
    
    private ScrollView LNBTransponderList=null;
    private TvLNBListView LBNTaypPowerList= null;
    private TvLNBListView LBNLongCableList= null;
    private TvLNBListView LBN22KHZList= null;
    private TvLNBListView LBNTapyList= null;
    private TvLNBListView LBNDisEqC10List= null;
    private TvLNBListView LBNDisEqC11List= null;
    private TvLNBListView LBNDMontorList= null;
    
    private ArrayList <View> responderLayoutList=new ArrayList();
    
    
	public LNBSettingView(Context context,LNBSettingDialog parentDialog) 
	{
		super(context);
		mContext = context;
		this.parentDialog = parentDialog;
        setOrientation(LinearLayout.VERTICAL);
        setFocusable(true);
        TvQuickKeyControler.getInstance().getQuickKeyListener();
        initView();
	}
        
    private void initView()
    {
    	overallLayout = new LinearLayout(mContext);
        overallLayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(overallLayout, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        
        titleText = initTextView(mContext.getString(R.string.str_lnb_setup_title), (int) (52 / dip),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        titleText.setBackgroundColor(Color.parseColor("#3598DC"));
        overallLayout.addView(titleText, (int)(1920/div) , (int)(120/div));
        
        typeLayout = new LinearLayout(mContext);
        typeLayout.setOrientation(LinearLayout.HORIZONTAL);
        typeLayout.setBackgroundColor(Color.parseColor("#323232"));
        overallLayout.addView(typeLayout, (int)(1920/div) , (int)(120/div));
        
        satelliteListText = initTextView(mContext.getString(R.string.str_satellite_list), (int) (42 / dip),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(satelliteListText, (int)(769/div) , (int)(120/div));
        
        addVDivideLine(typeLayout);
        
        lnbSetupListText = initTextView(mContext.getString(R.string.str_lnb_setup_list), (int) (42 / dip),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(lnbSetupListText, (int)(378/div) , (int)(120/div));
        
        addVDivideLine(typeLayout);
        
        responderListText = initTextView(mContext.getString(R.string.str_responder_list), (int) (42 / dip),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        typeLayout.addView(responderListText, (int)(769/div) , (int)(120/div));
        
        bodyLayout = new LinearLayout(mContext);
        bodyLayout.setOrientation(LinearLayout.HORIZONTAL);
        overallLayout.addView(bodyLayout, (int)(1920/div) , (int)(720/div));
        
        satellitelistScrollview = new ScrollView(mContext);
        satellitelistScrollview.setBackgroundColor(Color.parseColor("#191919"));
        satellitelistScrollview.setVerticalScrollBarEnabled(true);
        satellitelistScrollview.setHorizontalScrollBarEnabled(false);
        satellitelistScrollview.setScrollbarFadingEnabled(true);
        satellitelistScrollview.setAlwaysDrawnWithCacheEnabled(true);
        satellitelistScrollview.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        satellitelistLayout = new LinearLayout(mContext);
        satellitelistLayout.setOrientation(LinearLayout.VERTICAL);
        satellitelistScrollview.addView(satellitelistLayout, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));		
        bodyLayout.addView(satellitelistScrollview,(int)(770/div) , (int)(720/div));
        
        
        lnbSetupListScrollView = new ScrollView(mContext);
        lnbSetupListScrollView.setBackgroundColor(Color.parseColor("#000000"));
        lnbSetupListScrollView.setVerticalScrollBarEnabled(true);
        lnbSetupListScrollView.setHorizontalScrollBarEnabled(false);
        lnbSetupListScrollView.setScrollbarFadingEnabled(true);
        lnbSetupListScrollView.setAlwaysDrawnWithCacheEnabled(true);
        lnbSetupListScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        lnbSetupListLayout = new LinearLayout(mContext);
        lnbSetupListLayout.setOrientation(LinearLayout.VERTICAL);
        lnbSetupListLayout.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        LayoutParams lnbSetupListParamas = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        lnbSetupListScrollView.addView(lnbSetupListLayout, lnbSetupListParamas);	
        bodyLayout.addView(lnbSetupListScrollView,(int)(380/div) , (int)(720/div));
        
        responderLayout = new LinearLayout(mContext);
        responderLayout.setOrientation(LinearLayout.VERTICAL);
        responderLayout.setBackgroundColor(Color.rgb(30, 71, 101));
        LayoutParams responderParams = new LayoutParams((int)(780/div),(int)(720/div));        
        bodyLayout.addView(responderLayout,responderParams);
        
        LNBTransponderList = new ScrollView(mContext);
        LNBTransponderList.setBackgroundColor(Color.parseColor("#000000"));
        LNBTransponderList.setVerticalScrollBarEnabled(true);
        LNBTransponderList.setHorizontalScrollBarEnabled(false);
        LNBTransponderList.setScrollbarFadingEnabled(true);
        LNBTransponderList.setAlwaysDrawnWithCacheEnabled(true);
        LNBTransponderList.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        reponderlistLayout = new LinearLayout(mContext);
        reponderlistLayout.setOrientation(VERTICAL);
        reponderlistLayout.setBackgroundColor(Color.parseColor("#000000"));
        LNBTransponderList.addView(reponderlistLayout,LayoutParams.MATCH_PARENT,(int)(600/div));
        
        ArrayList<TvTransponderListData>TvTransponderList=TvPluginControler.getInstance().getChannelManager().getTransponderList();
		data_counts = TvTransponderList.size();
		//tvTransponderItemViews.clear();
		for(int i = 0;i < data_counts; i++){
			Log.v("wxj","case 0:,i="+i);
			transponderItemView = new TransponderItemView(mContext, this ,parentDialog);
			transponderItemView.updateView(TvTransponderList.get(i));
			reponderlistLayout.addView(transponderItemView,LayoutParams.MATCH_PARENT, (int) (120 / div));
			addHDivideLine(reponderlistLayout);
			tvTransponderItemViews.add(transponderItemView);
			tvTransponderItemViews.get(i).setViewId(i);
			tvTransponderItemViews.get(i).setOnClickListener(new LisenerTransponder());
			transponderItemView.setItemTransponderFocusChange();
	 	}
		
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				UpdateSatelliteListView();
				flagListInit=true;
			}
		});
    	t.start();
    	
		int currentTPNumber = TvPluginControler.getInstance().getChannelManager().getCurrentTPNumber();
		if(data_counts > 0)
 		{
	 		if(currentTPNumber >= 0 && currentTPNumber < data_counts)
	 		{
	 			for(int i = 0; i < data_counts; i++)
	 			{
	 				if(tvTransponderItemViews.get(i).getTPNumber() == currentTPNumber + 1)
	 				{
	 					tvTransponderItemViews.get(i).setChecked(true);
	 				}
	 			}
	 		}
	 		if(data_counts < currentTPNumber)
	 		{
	 			tvTransponderItemViews.get(0).setChecked(true);
	 		}
 		}
        responderLayoutList.add(LNBTransponderList);
        
        LBNTaypPowerList=initTvListView(TvConfigDefs.TV_CFG_LNB_POWER);
        LBNLongCableList=initTvListView(TvConfigDefs.TV_CFG_LNB_LONG_CABLE);
        LBN22KHZList=initTvListView(TvConfigDefs.TV_CFG_LNB_22KHZ);
        LBNTapyList=initTvListView(TvConfigDefs.TV_CFG_LNB_TAPY);
        LBNDisEqC10List=initTvListView(TvConfigDefs.TV_CFG_LNB_DIS_EQC_10_ENUM_TYPE);
        LBNDisEqC11List=initTvListView(TvConfigDefs.TV_CFG_LNB_DIS_EQC_11_ENUM_TYPE);
        LBNDMontorList=initTvListView(TvConfigDefs.TV_CFG_LNB_MONTOR);
        
        for(TvLNBListItemView temp : LBNDMontorList.getViewList())
        {
        	temp.getCheckedView().setVisibility(View.GONE);
        	final int index=temp.getIndex();
        	temp.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					// TODO Auto-generated method stub
					if(!(arg2.getAction()==KeyEvent.ACTION_DOWN))
					{
						return true;
					}
					if(index==1)
					{
						if(arg2.getKeyCode()==KeyEvent.KEYCODE_PROG_RED)
						{
							LnbMotorSettingDialog lnbMotorSettingDialog = new LnbMotorSettingDialog();
//							lnbMotorSettingDialog.setDialogListener(thisView);
							lnbMotorSettingDialog.setDialogListener(null);
							lnbMotorSettingDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumMotorSet.E_MOTOR_SET_RED_DISEQC_1_2.ordinal()); 
						}
						else if(arg2.getKeyCode()==KeyEvent.KEYCODE_PROG_GREEN)
						{
							LnbMotorSettingDialog lnbMotorSettingDialog = new LnbMotorSettingDialog();
							lnbMotorSettingDialog.setDialogListener(null);
							lnbMotorSettingDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumMotorSet.E_MOTOR_SET_GREEN_DISEQC_1_2.ordinal()); 
						}
					}
					if(index==2)
					{
						if(arg2.getKeyCode()==KeyEvent.KEYCODE_PROG_RED)
						{
							LnbMotorSettingDialog lnbMotorSettingDialog = new LnbMotorSettingDialog();
							lnbMotorSettingDialog.setDialogListener(null);
							lnbMotorSettingDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumMotorSet.E_MOTOR_SET_RED_DISEQC_1_3.ordinal()); 
						}
						else if(arg2.getKeyCode()==KeyEvent.KEYCODE_PROG_GREEN)
						{
							LnbMotorSettingDialog lnbMotorSettingDialog = new LnbMotorSettingDialog();
							lnbMotorSettingDialog.setDialogListener(null);
							lnbMotorSettingDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumMotorSet.E_MOTOR_SET_GREEN_DISEQC_1_3.ordinal()); 
						}
						else if(arg2.getKeyCode()==KeyEvent.KEYCODE_PROG_YELLOW)
						{
							LnbMotorSettingDialog lnbMotorSettingDialog = new LnbMotorSettingDialog();
							lnbMotorSettingDialog.setDialogListener(null);
							lnbMotorSettingDialog.processCmd(null, DialogCmd.DIALOG_SHOW, EnumMotorSet.E_MOTOR_SET_YELLOW_DISEQC_1_3.ordinal()); 
						}
					}
					return thisView.onKeyDown(arg2.getKeyCode(), arg2);
				}
			});
        }
        
        LayoutParams ListParams = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(600/div));
        for(View temp:responderLayoutList)
        {
        	responderLayout.addView(temp,ListParams);	
        }
        
        IndexOfResponderChild=new int[] {0,0,0,0,0,0,0,0,0};
	    SizeOfResponderChild=new int[]{
	    		data_counts,	
	    		LBNTaypPowerList.getViewList().size(),
	    		LBNLongCableList.getViewList().size(),
	    		LBN22KHZList.getViewList().size(),
	    		LBNTapyList.getViewList().size(),
	    		LBNDisEqC10List.getViewList().size(),
	    		LBNDisEqC11List.getViewList().size(),
	    		LBNDMontorList.getViewList().size(),
	    };
	    Log.v("wxj","LBNDisEqC11List.getViewList().size(),"+LBNDisEqC11List.getViewList().size());
        responderButtonLayout = new LinearLayout(mContext);
        responderButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams responderButtonParams = new LayoutParams(LayoutParams.MATCH_PARENT,(int)(120/div));
        responderLayout.addView(responderButtonLayout,responderButtonParams);	
       
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        LinearLayout marginLayout = new LinearLayout(mContext);
        marginLayout.setOrientation(LinearLayout.HORIZONTAL); 
        marginLayout.setPadding((int)(80/div), 0, 0, 0);
        marginLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        
        responderButtonLayout.addView(marginLayout, params);
        

        
        
	  redLayout=initColorLayout(R.drawable.epg_red,R.string.str_lnb_item_add);
      responderButtonLayout.addView(redLayout, params);
      
      redPositonLayout=initColorLayout(R.drawable.epg_red,R.string.str_lnb_item_Position);
      redPositonLayout.setVisibility(View.GONE);
      responderButtonLayout.addView(redPositonLayout, params);
      
	  greenLayout=initColorLayout(R.drawable.epg_green,R.string.str_lnb_item_edit);
      responderButtonLayout.addView(greenLayout, params);
      
      greenLimitLayout=initColorLayout(R.drawable.epg_green,R.string.str_lnb_item_limit);
      greenLimitLayout.setVisibility(View.GONE);
      responderButtonLayout.addView(greenLimitLayout, params);
      
      
      yellowLayout=initColorLayout(R.drawable.epg_yellow,R.string.str_lnb_item_delete);
      responderButtonLayout.addView(yellowLayout, params);
      
      yellowLocationLayout=initColorLayout(R.drawable.epg_yellow,R.string.str_lnb_item_location);
      yellowLocationLayout.setVisibility(View.GONE);
      responderButtonLayout.addView(yellowLocationLayout, params);
      
      blueLayout=initColorLayout(R.drawable.epg_blue, R.string.str_lnb_item_scan);
      responderButtonLayout.addView(blueLayout, params);
      
      infoLayout = initColorLayout(R.drawable.info,R.string.str_lnb_info_hint);
      responderButtonLayout.addView(infoLayout, params);
      
      signalInfoView = new SignalInfoView(mContext);
      overallLayout.addView(signalInfoView, (int)(1920/div) , (int)(120/div));
      
      while(!flagListInit)
      {
    	  try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
    }

	public void updateView() {
		updateView(false);
		updateTransponderList();
	}

	public void updateView(boolean firstInit) {
		if (firstInit) {
//			UpdateSatelliteListView(tvSatelliteList);

			UpdateLNBSettingListView();

			UpdateResponderListView();
		} else {
			UpdateSatelliteListView();

			UpdateLNBSettingListView();

			 UpdateResponderListView();
		 }
		 ScanCurrentFreq();
	 }
	 
	 public void ScanCurrentFreq()
	 {
		 Log.v("zhm","LNBSettingView -->> ScanCurrent-->>");
		 Log.i("wangxian","not excute scanCurrentFreq----------------");
//		 TvPluginControler.getInstance().getChannelManager().dvbsScanCurrentFreq(dtvManualSearchListener);
	 }
	 
	 public void getSignalInfo()
	 {
		 siganlScanFlag = true;
		 Log.v("zhm","LNBSettingView -->> ScanCurrent-->>");
		 Log.i("wangxian","not excute scanCurrentFreq----------------");
		 TvPluginControler.getInstance().getChannelManager().dvbsScanCurrentFreq(dtvManualSearchListener);
	 }
	 
	 public void updateSignalInfo(ChannelSearchData data)
	 {
		 signalInfoView.updateView(data);
	 }
	 
	 public void UpdateSatelliteListView()
	 {

		 tvSatelliteList=TvPluginControler.getInstance().getChannelManager().getSatelliteList();
		 if(null != tvSatelliteList)
		 {
			 data_count = tvSatelliteList.size();
			 satellitelistLayout.removeAllViews();
			 tvSatelliteItemView.clear();
			 Log.i("wangxian", "==============data_count = " + data_count);
			 for (int i = 0; i < data_count; i++)
			 {
				 Log.i("wangxian","tvSatelliteList.get(i)=======" + tvSatelliteList.get(i).getName());
				 selecteItem = new SatelliteItemView(mContext, this ,parentDialog);
				 selecteItem.update(tvSatelliteList.get(i).getNumber(),tvSatelliteList.get(i).getName(),tvSatelliteList.get(i).getBand());
				 satellitelistLayout.addView(selecteItem, LayoutParams.MATCH_PARENT, (int) (120 / div));				 
				 addHDivideLine(satellitelistLayout);			 
	             tvSatelliteItemView.add(selecteItem);   
	             tvSatelliteItemView.get(i).setId(i);
	             tvSatelliteItemView.get(i).setOnClickListener(new LisenerItem());
			 }
             setItemSelectFocusChange(tvSatelliteItemView);
//			 String CurrentSatelliteNameString = TvPluginControler.getInstance().getChannelManager().getCurrentSatelliteName();
             Log.i("wangxian", "----------------CurrentSatelliteNumber start =====");
			 int CurrentSatelliteNumber = TvPluginControler.getInstance().getChannelManager().getCurrentSatelliteNumber();
			 Log.i("wangxian", "=========CurrentSatelliteNumber=" + CurrentSatelliteNumber);
			 
			 for(int i = 0;i<tvSatelliteItemView.size();i++)
			 {			 
				 if(CurrentSatelliteNumber >= data_count)//CurrentSatelliteNameString.equals(""))
				 {
					 TvPluginControler.getInstance().getChannelManager().SelectSatellite("0");
					 satellitelistLayout.setFocusable(true);
					 lnbSetupListLayout.setFocusable(false);
					 responderLayout.setFocusable(false);
					 tvSatelliteItemView.get(0).setFocusable(true);
					 tvSatelliteItemView.get(0).requestFocus();
					 tvSatelliteItemView.get(0).setChecked(true);
					 IndexOfSatellite = 0;
				 }
				 else 
				 {
					 if(tvSatelliteItemView.get(i).getNumberString().equals(String.valueOf(CurrentSatelliteNumber+1)))
					 {
						 Log.i("Satellite","CurrentSatelliteIndex i"+i);
						 satellitelistLayout.setFocusable(true);
						 lnbSetupListLayout.setFocusable(false);
						 responderLayout.setFocusable(false);
						 tvSatelliteItemView.get(i).requestFocus();
						 tvSatelliteItemView.get(i).setChecked(true);
						 IndexOfSatellite = i;
					 }
				 }
			 }	
		 }
	 }
	 
	 public void UpdateLNBSettingListView()
	 {
		 Log.i("wangxian", "UpdateLNBSettingListView==========");
		 lnbSetupListLayout.removeAllViews();
		 tvSetupItemView.clear();
		 String[] str = this.getResources().getStringArray(R.array.str_lnb_option_list);
		 Log.v(tag,"str = " + str);
		 
		 for(int i=0;i<str.length;i++)
		 {
			 setupItemView = new SetupItemView(mContext,this);
			 setupItemView.update(str[i],i);

			 tvSetupItemView.add(setupItemView);
			 lnbSetupListLayout.addView(setupItemView,LayoutParams.MATCH_PARENT,(int)(120/div));
			 
			 addHDivideLine(lnbSetupListLayout);
			 setupItemView.setItemSetupFocusChange();
		 }		 
	 }
	 
	 public void UpdateResponderListView()
	 {
		 int index=0;
		 Log.i("wangxian", "UpdateResponderListView=============");
		 for(int i=0;i<tvSetupItemView.size();i++)
		 {
			 if(tvSetupItemView.get(i).hasFocus())
			 {
				 index=i;
			 }
		 }
		 
		 for(View temp: responderLayoutList)
		 {
			 temp.setVisibility(View.GONE);
		 }
		 
		 switch(index)
		 {
		 	case 0:
		 		LNBTransponderList.setVisibility(View.VISIBLE);
		 		break;
		 	case 1:
		 		LBNTaypPowerList.setVisibility(View.VISIBLE);
		 	   break;
		 	case 2:
		 	    LBNLongCableList.setVisibility(View.VISIBLE);
		 	    break;
		 	case 3:
		 		LBN22KHZList.setVisibility(View.VISIBLE);
		 		break;	
		 	case 4:
		 		LBNTapyList.setVisibility(View.VISIBLE);
		 		break;
		 	case 5:
		 		LBNDisEqC10List.setVisibility(View.VISIBLE);
		 		break;
		 	case 6:
		 		LBNDisEqC11List.setVisibility(View.VISIBLE);
		 		break;
		 	case 7:
		 		LBNDMontorList.setVisibility(View.VISIBLE);
		 		break;	
		 }
	 }
	 
	 public void UpdateResponderChilidListView(int index)
	 {
		if( LNBTransponderList.getVisibility()==View.VISIBLE)
		{
			ArrayList<TvTransponderListData>TvTransponderList=TvPluginControler.getInstance().getChannelManager().getTransponderList();
	 		int tp_counts = TvTransponderList.size();
			responderLayout.setFocusable(true);
			reponderlistLayout.setFocusable(true);
			if(tp_counts > 0)
			{
				if(tp_counts < IndexOfResponderChild[0])
				{
					IndexOfResponderChild[0] = 0;
				}
				tvTransponderItemViews.get(IndexOfResponderChild[0]).setFocusable(true);
				tvTransponderItemViews.get(IndexOfResponderChild[0]).setFocused(true);
				tvTransponderItemViews.get(IndexOfResponderChild[0]).requestFocus();
			}
			else
			{
				TvSearchTypes.TRANSPONDERE_SETTING_OPERATION_TYPE = TRANSPONDER_OPERATION_TYPE.TRANSPONDER_OPERATION_ADD;
				parentDialog.dismissUI();
				TransponderOperationDialog TransponderAddDialog = new TransponderOperationDialog();
				TransponderAddDialog.setDialogListener(null);
				TransponderAddDialog.processCmd(TvConfigTypes.TV_DIALOG_SATELLITE_EDIT, DialogCmd.DIALOG_SHOW, 0);
			}
			
			for(TransponderItemView temp:tvTransponderItemViews)
			{
				temp.setItemTransponderFocusChange();
			}
		}
		else
		{
			for(int i=0;i<responderLayoutList.size();i++)
			{
				if(responderLayoutList.get(i).getVisibility()==View.VISIBLE)
				{
					((TvLNBListView)responderLayoutList.get(i)).getViewList().get(index).requestFocus();
				}
			}
		}
	 }
	 	 
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) 
	 {
		// TODO Auto-generated method stub
		Log.v("yangcheng","LNBSettingView.keyCode = " + keyCode);
		if(keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/)
		{
			parentDialog.processCmd(TvConfigTypes.TV_DIALOG_LNB_SETTING,DialogCmd.DIALOG_DISMISS, null);
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
				&&event.getAction()==KeyEvent.ACTION_DOWN)
        {
			int rowLongth=0;
			switch(columnsIndex)
			{
//				case NUM_SATELLITE:
				case 1:
					rowsIndex=IndexOfSatellite;
					rowLongth=tvSatelliteList.size();
					Log.v("wxj","rowLongth"+rowLongth);
					Log.v("wxj","IndexOfSatellite");
					if(keyCode == KeyEvent.KEYCODE_DPAD_UP&&rowsIndex>0)
					{
						IndexOfSatellite--;
						rowsIndex=IndexOfSatellite;
						Log.v("wxj","IndexOfSatellite--"+IndexOfSatellite);
					}
					if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN&&rowsIndex<rowLongth-1)
					{
						IndexOfSatellite++;
						rowsIndex=IndexOfSatellite;
						Log.v("wxj","IndexOfSatellite++"+IndexOfSatellite);
					}
					updateUIbyIndex(NUM_SATELLITE,rowsIndex);
					Log.v("wxj","IndexOfSatellite"+IndexOfSatellite);
					break;
//				case NUM_SETUP:
				case 2:
					rowsIndex=IndexOfSetup;
					rowLongth=tvSetupItemView.size();
					if(keyCode == KeyEvent.KEYCODE_DPAD_UP&&rowsIndex>0)
					{
						IndexOfSetup--;
						rowsIndex=IndexOfSetup;
					}
					if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN&&rowsIndex<rowLongth-1)
					{
						IndexOfSetup++;
						rowsIndex=IndexOfSetup;
					}
					updateUIbyIndex(NUM_SETUP,rowsIndex);
					Log.v("wxj","IndexOfSetup"+IndexOfSetup);
					break;
//				case NUM_RESPONDER:
				case 3:
					rowsIndex=IndexOfResponderChild[IndexOfSetup];
					rowLongth=SizeOfResponderChild[IndexOfSetup];
					Log.v("wxj","rowLongth"+rowLongth);
//					rowLongth=tvTransponderItemViews.size();
					if(keyCode == KeyEvent.KEYCODE_DPAD_UP&&rowsIndex>0)
					{
						IndexOfResponderChild[IndexOfSetup]--;
						rowsIndex=IndexOfResponderChild[IndexOfSetup];
					}
					if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN&&rowsIndex<rowLongth-1)
					{
						IndexOfResponderChild[IndexOfSetup]++;
						rowsIndex=IndexOfResponderChild[IndexOfSetup];
					}
					Log.v("wxj","IndexOfResponderChild[IndexOfSetup]"+IndexOfResponderChild[IndexOfSetup]);
					updateUIbyIndex(NUM_RESPONDER,rowsIndex);
					break;
			}
		
            return true;
        }
		else if((keyCode == KeyEvent.KEYCODE_DPAD_LEFT||keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
				&&event.getAction()==KeyEvent.ACTION_DOWN)
		{
			Log.v("wxj","KEYCODE_DPAD_LEFT,onKeyDown");
			if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT&&columnsIndex!=NUM_SATELLITE)
			{ 
				 columnsIndex--;
			}
			if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT&&columnsIndex!=NUM_RESPONDER)
			{ 
				 columnsIndex++;
			}
			Log.v("wxj","columnsIndex="+columnsIndex);
			switch(columnsIndex)
			{
//				case NUM_SATELLITE:
			case 1:
					updateUIbyIndex(columnsIndex,IndexOfSatellite);
					break;
//				case NUM_SETUP:
			case 2:
					updateUIbyIndex(columnsIndex,IndexOfSetup);
					break;
//				case NUM_RESPONDER:	
			case 3:
					updateUIbyIndex(columnsIndex,IndexOfResponderChild[IndexOfSetup]);
					break;
			}
			
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
		{
			return true;
		}
		else if(keyCode == KeyEvent.KEYCODE_PROG_BLUE)
		{
			if(LNBSettingView.siganlScanFlag)
			{
				Log.i("wangxian", "signal Scan Flag = true");
				TvPluginControler.getInstance().getChannelManager().StopDvbsScan();
			}
			parentDialog.dismissUI();
			SatelliteSignalScanDialog satelliteSignalScanDialog = new SatelliteSignalScanDialog();
			satelliteSignalScanDialog.setDialogListener(null);
			satelliteSignalScanDialog.processCmd(TvConfigTypes.TV_DIALOG_SATELLITE_SCAN, DialogCmd.DIALOG_SHOW, null);
			return true;
		}
		return false;//quickKeyListener.onQuickKeyDownListener(keyCode, event);
	 }
	 
	 private void setItemSelectFocusChange(ArrayList<SatelliteItemView> dataList)
     {
    	for (int i = 0; i < data_count; i++) {
    		SatelliteItemView itemView = tvSatelliteItemView.get(i);
			itemView.update(tvSatelliteList.get(i).getNumber(),tvSatelliteList.get(i).getName(),tvSatelliteList.get(i).getBand());
			itemView.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View view, boolean hasFocus) {
					((SatelliteItemView) view).setBackGroundColorUser(hasFocus);
				}
			});
		}
    } 
	 
	class LisenerItem implements OnClickListener {
		public void onClick(View view) {
			int id = view.getId();
			TvPluginControler.getInstance().getChannelManager().SelectSatellite(String.valueOf(id));
			tvSatelliteList.get(id).setSelectedFlag(true);
			checkedSelectedItem(id);
			updateTransponderList();
			ScanCurrentFreq();
		}
	}
	
	class LisenerTransponder implements OnClickListener {
		public void onClick(View view) {
			int id = ((TransponderItemView)view).getId();
			Log.v("zhm","TransponderItemView get ID == " + id);
			checkedTPSelectedItem(id);
			TvSearchTypes.SetCurrentTransponderNumber = getCurrentTPFocusedItem();
			TvPluginControler.getInstance().getChannelManager().SelectTransponder(String.valueOf(getCurrentTPFocusedItem()));
			ScanCurrentFreq();
			Log.v("zhm","SetCurrentTransponderNumber = " + TvSearchTypes.SetCurrentTransponderNumber);
		}
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
	 
	 private void addHDivideLine(LinearLayout layout)
	 {
		divideLine = new ImageView(mContext);
		divideLine.setBackgroundResource(R.drawable.setting_line);
		LayoutParams divideParams = new LayoutParams((int) (730 / div),(int) (2 / div));
		divideParams.leftMargin = (int) (20 / div);
		divideParams.rightMargin = (int) (20 / div);
		layout.addView(divideLine, divideParams);
     } 
	 
	 private void addVDivideLine(LinearLayout layout)
	 {
		divideLine = new ImageView(mContext);
		divideLine.setBackgroundResource(R.drawable.setting_line);
		layout.addView(divideLine, new LayoutParams((int) (2 / div),LayoutParams.MATCH_PARENT));
     } 
         
     public void setParentDialog(LNBSettingDialog parentDialog) {
 		this.parentDialog = parentDialog;
 	 }

     
    public int getCurrentFocusedItem()
    {
    	int itemIdex = 0;
    	for(int i = 0; i < tvSatelliteItemView.size(); i++)
    	{
    		if(tvSatelliteItemView.get(i).isFocused())
    		{
    			itemIdex = i;
    		}
    	}
    	return itemIdex;
    }
    
    public int getSatelliteItemNum()
    {
    	Log.i("wangxian", "satelliteItemNum = " + tvSatelliteItemView.size());
    	return tvSatelliteItemView.size();
    	
    }
    
    public int getCurrentTPFocusedItem()
    {
    	int itemIdex = 0;
    	for(int i = 0; i < tvTransponderItemViews.size(); i++)
    	{
    		if(tvTransponderItemViews.get(i).isFocused())
    		{
    			itemIdex = i;
    		}
    	}
    	return itemIdex;
    }
    
    public void checkedSelectedItem(int id) 
	{
    	if (data_count == 0)
        {
            return;
        }
    	for (int i = 0; i < data_count; i++)
        {
    		SatelliteItemView view = tvSatelliteItemView.get(i);
    		boolean isChecked = view.getchecked();
    		if(view.getId() == id) 
    		{   
    			if (!isChecked) 
    			{
    				view.setChecked(true);
    			}
    		} 
    		else 
    		{
    			view.setChecked(false);
    		}
        }
    	
	}
    
    public void checkedTPSelectedItem(int id) 
	{
    	if (data_counts == 0)
        {
            return;
        }
    	for (int i = 0; i < data_counts; i++)
        {
    		TransponderItemView view = tvTransponderItemViews.get(i);
    		boolean isChecked = view.getChecked();
    		if(view.getId() == id) 
    		{   
    			if (!isChecked) 
    			{
    				view.setChecked(true);
    			}
    		} 
    		else 
    		{
    			view.setChecked(false);
    		}
        }
	}
    
    public void updateTransponderList()
    {
 		tvTransponderItemViews.clear();
 		LNBTransponderList.removeAllViews();
 		
 		LNBTransponderList.setBackgroundColor(Color.parseColor("#000000"));
        LNBTransponderList.setVerticalScrollBarEnabled(true);
        LNBTransponderList.setHorizontalScrollBarEnabled(false);
        LNBTransponderList.setScrollbarFadingEnabled(true);
        LNBTransponderList.setAlwaysDrawnWithCacheEnabled(true);
        LNBTransponderList.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        reponderlistLayout = new LinearLayout(mContext);
        reponderlistLayout.setOrientation(VERTICAL);
        reponderlistLayout.setBackgroundColor(Color.parseColor("#000000"));
        LNBTransponderList.addView(reponderlistLayout,LayoutParams.MATCH_PARENT,(int)(600/div));
 		
    	ArrayList<TvTransponderListData>TvTransponderList=TvPluginControler.getInstance().getChannelManager().getTransponderList();
 		data_counts = TvTransponderList.size();
	    SizeOfResponderChild[0] = data_counts;
 		for(int i = 0;i < data_counts; i++){
 			Log.v("wxj","case 0:,i="+i);
 			transponderItemView = new TransponderItemView(mContext, this ,parentDialog);
 			transponderItemView.updateView(TvTransponderList.get(i));
 			reponderlistLayout.addView(transponderItemView,LayoutParams.MATCH_PARENT, (int) (120 / div));
 			addHDivideLine(reponderlistLayout);
 			tvTransponderItemViews.add(transponderItemView);
 			tvTransponderItemViews.get(i).setViewId(i);
 			tvTransponderItemViews.get(i).setOnClickListener(new LisenerTransponder());
 			transponderItemView.setItemTransponderFocusChange();
 	 	}
 		int currentTPNumber = TvPluginControler.getInstance().getChannelManager().getCurrentTPNumber();
 		Log.v("zhm", "data_counts = " + data_counts + "currentTPNumber = " + currentTPNumber);
 		if(data_counts > 0)
 		{
	 		if(currentTPNumber >= 0 && currentTPNumber < data_counts)
	 		{
	 			for(int i = 0; i < data_counts; i++)
	 			{
	 				if(tvTransponderItemViews.get(i).getTPNumber() == currentTPNumber + 1)
	 				{
	 					if(3 == columnsIndex)
	 					{
		 					satellitelistLayout.setFocusable(false);
							lnbSetupListLayout.setFocusable(false);
							responderLayout.setFocusable(true);
							tvTransponderItemViews.get(i).setFocusable(true);
							tvTransponderItemViews.get(i).setFocused(true);
							tvTransponderItemViews.get(i).requestFocus();
							IndexOfResponderChild[IndexOfSetup] = i;
	 					}
						tvTransponderItemViews.get(i).setChecked(true);
						TvPluginControler.getInstance().getChannelManager().SelectTransponder(String.valueOf(i));
	 				}
	 			}
	 		}
	 		if(data_counts <= currentTPNumber && currentTPNumber >= 1)
	 		{
	 			int n = currentTPNumber-1;
	 			if(3 == columnsIndex)
	 			{
		 			satellitelistLayout.setFocusable(false);
					lnbSetupListLayout.setFocusable(false);
					responderLayout.setFocusable(true);
					tvTransponderItemViews.get(n).setFocusable(true);
					tvTransponderItemViews.get(n).setFocused(true);
					tvTransponderItemViews.get(n).requestFocus();
					IndexOfResponderChild[IndexOfSetup] = n;
	 			}
				tvTransponderItemViews.get(n).setChecked(true);
				TvPluginControler.getInstance().getChannelManager().SelectTransponder(String.valueOf(n));
	 		}
 		}
    }
     
	@Override
	public boolean onShowDialogDone(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object... obj) {
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
	
	public TvLNBListView initTvListView(String ID)
	{
		TvLNBListView tvlistView=new TvLNBListView(mContext,this);
		tvlistView.updateView(MenuItemFactory.getInstance().createMenuItem(ID));
		tvlistView.setVisibility(View.GONE);
		responderLayoutList.add(tvlistView);
		return tvlistView;
	}
	private void updateUIbyIndex(int columnsIndex,int rowsIndex)
	{
		if(columnsIndex==NUM_SATELLITE)
		{
			satellitelistLayout.setFocusable(true);
			tvSatelliteItemView.get(rowsIndex).setFocusable(true);
			tvSatelliteItemView.get(rowsIndex).setFocused(true);
			tvSatelliteItemView.get(rowsIndex).requestFocus();
			
		}
		if(columnsIndex==NUM_SETUP)
		{
			lnbSetupListLayout.setFocusable(true);
			tvSetupItemView.get(rowsIndex).setFocusable(true);
			tvSetupItemView.get(rowsIndex).setFocused(true);
			tvSetupItemView.get(rowsIndex).requestFocus();
			UpdateResponderListView();
		}
		if(columnsIndex==NUM_RESPONDER)
		{
			UpdateResponderChilidListView(rowsIndex);
		}
		updateColorHint(columnsIndex,rowsIndex);
	}
	public LinearLayout initColorLayout(int image,int text)
	{
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout Layout = new LinearLayout(mContext);
        Layout.setOrientation(LinearLayout.HORIZONTAL); 
        Layout.setPadding((int)(20/div), 0, 0, 0);
        Layout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        
        ImageView imageView = new ImageView(mContext);
        imageView.setBackgroundResource(image);
        imageView.setPadding((int)(20/div), (int)(20/div), (int)(20/div), (int)(20/div));
//        if(image == R.drawable.info)
//        	Layout.addView(imageView, (int)(45/div),(int)(30/div));
//        else
        	Layout.addView(imageView, (int)(25/div) , (int)(25/div));
        
        TextView textView = initTextView(mContext.getString(text), (int) (30 / dip),
        		Color.WHITE, Gravity.CENTER);
        textView.setPadding((int)(10/div), 0, 0, 0);
        Layout.addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        return Layout;
	}   

	
	public void updateColorHint(int columnsIndex,int rowsIndex)
	{
		if(columnsIndex==1)
		{
			redLayout.setVisibility(View.VISIBLE);		
			greenLayout.setVisibility(View.VISIBLE);			
			yellowLayout.setVisibility(View.VISIBLE);
			blueLayout.setVisibility(View.VISIBLE);
			infoLayout.setVisibility(View.VISIBLE);
			redPositonLayout.setVisibility(View.GONE);
			greenLimitLayout.setVisibility(View.GONE);
			yellowLocationLayout.setVisibility(View.GONE);
		}
		else if(columnsIndex==2)
		{
			redLayout.setVisibility(View.GONE);
			greenLayout.setVisibility(View.GONE);
			yellowLayout.setVisibility(View.GONE);
			blueLayout.setVisibility(View.VISIBLE);
			infoLayout.setVisibility(View.GONE);
			redPositonLayout.setVisibility(View.GONE);
			greenLimitLayout.setVisibility(View.GONE);
			yellowLocationLayout.setVisibility(View.GONE);
		}else if(columnsIndex==3)
		{
			redLayout.setVisibility(View.GONE);
			greenLayout.setVisibility(View.GONE);
			yellowLayout.setVisibility(View.GONE);
			blueLayout.setVisibility(View.VISIBLE);
			infoLayout.setVisibility(View.GONE);
			redPositonLayout.setVisibility(View.GONE);
			greenLimitLayout.setVisibility(View.GONE);
			yellowLocationLayout.setVisibility(View.GONE);
			Log.v("wxj","IndexOfResponderChild[IndexOfResponderChild.length-1]"+IndexOfResponderChild[IndexOfResponderChild.length-1]);
			Log.v("wxj","rowsIndex"+rowsIndex);
			Log.v("wxj","IndexOfSetup"+IndexOfSetup);
			if(IndexOfSetup==0)
			{
				redLayout.setVisibility(View.VISIBLE);		
				greenLayout.setVisibility(View.VISIBLE);			
				yellowLayout.setVisibility(View.VISIBLE);
				blueLayout.setVisibility(View.VISIBLE);
				infoLayout.setVisibility(View.VISIBLE);
				redPositonLayout.setVisibility(View.GONE);
				greenLimitLayout.setVisibility(View.GONE);
				yellowLocationLayout.setVisibility(View.GONE);
			}
			else if(IndexOfSetup==7)
			{
				if(rowsIndex==1)
				{
					redLayout.setVisibility(View.GONE);
					greenLayout.setVisibility(View.GONE);
					yellowLayout.setVisibility(View.GONE);
					blueLayout.setVisibility(View.VISIBLE);
					infoLayout.setVisibility(View.GONE);
					redPositonLayout.setVisibility(View.VISIBLE);
					greenLimitLayout.setVisibility(View.VISIBLE);
					yellowLocationLayout.setVisibility(View.GONE);
				}else if(rowsIndex==2)
				{
					redLayout.setVisibility(View.GONE);
					greenLayout.setVisibility(View.GONE);
					yellowLayout.setVisibility(View.GONE);
					blueLayout.setVisibility(View.VISIBLE);
					infoLayout.setVisibility(View.GONE);
					redPositonLayout.setVisibility(View.VISIBLE);
					greenLimitLayout.setVisibility(View.VISIBLE);
					yellowLocationLayout.setVisibility(View.VISIBLE);
				}
				
			}
		}
	}
	
    DtvManualSearchListener dtvManualSearchListener = new DtvManualSearchListener() {
		
		@Override
		public void OnDtvManualSearchListener(ChannelSearchData data)
		{
			// TODO Auto-generated method stub
			updateSignalInfo(data);
		}
	};
	
}
