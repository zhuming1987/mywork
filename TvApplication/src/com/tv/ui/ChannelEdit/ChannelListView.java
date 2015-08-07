package com.tv.ui.ChannelEdit;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvLoadingData;
import com.tv.framework.data.TvOperatorProfileInfo;
import com.tv.framework.data.TvProgramInfo;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.data.TvOsType.EnumInputSource;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.EnumChOpType;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvConfigTypes.TvEnumProgramCountType;
import com.tv.framework.plugin.tvfuncs.CiManager.CiUIOpListener;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ChannelEdit.ChannelListItemView.onItemFocusListener;
import com.tv.ui.ChannelEdit.ChannelListItemView.onItemKeyListener;
import com.tv.ui.Loading.TvLoadingDialog;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ChannelListView extends LinearLayout implements onItemKeyListener, onItemFocusListener{

	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
	private Context mContext;
	
	private LinearLayout titleLayout = null;
	private TextView titleTextView = null;
	private ImageView titleImageView = null;
	
	private ScrollView bodyScrollView = null;
	private LinearLayout bodyLayout = null;
	private LinearLayout bottomLayout = null;
	
	private TextView favoriTextView;
	private TextView deleteTextView;
	private TextView moveTextView;
	private TextView skipTextView;
	
	private ArrayList<TvProgramInfo> proInfoList = new ArrayList<TvProgramInfo>();
	private ArrayList<TvOperatorProfileInfo> oProfileInfos = new ArrayList<TvOperatorProfileInfo>();
	private ArrayList<ChannelListItemView> chViewList = new ArrayList<ChannelListItemView>();
	
	private ChannelEditDialog parentDialog = null;
	private TvLoadingDialog mLoadingDialog = null;
	
	private static final int SHOW_DELETE_MSG = 1001;
	private static final int SHOW_SORT_NUM_MSG = 1002;
	private static final int SHOW_SORT_NAME_MSG = 1003;
    private static final int SHOW_SORT_NETID_MSG = 1004;
    private static final int SHOW_LAST_MSG = 1005;
    private static final int SHOW_MOVE_MSG = 1006;
    private static final int SHOW_FAILED_MSG = 1008;
    private static final int UPDATE_MSG = 1009;
    private static final int SHOW_LOADING = 1010;
    private static final int DISMISS_LOADING = 1011;
    private static final int DELETE_OPFILE = 1012;
    private static final int SHOW_OPFILE = 1013;
    
    private boolean isMoving = false;
    private boolean moveAble = false;
    private boolean isOpMode = false;
    private int startPosition = -1;
    private int targetPosition = -1;
    private int position = -1;
    private EnumChOpType chOpType = EnumChOpType.E_CH_OPTION_EDIT;
    private ChannelListItemView tmpItemView = null;
    private int curSelectedIndex = 0;
    private int defaultIndex = 0;
    private int totalCount = 0;
    private int curCount = 0;
    private short mOPCount = 0;
    private TvEnumInputSource curSource = null;
    TvProgramInfo curProInfo = null;
    boolean threadFlag = true;
	
	public ChannelListView(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		long s = System.currentTimeMillis();
		this.mContext = context;
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams((int)(845/div), (int)(945/div)));
		this.setBackgroundColor(Color.parseColor("#191919"));
		titleLayout = new LinearLayout(context);
		titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		titleLayout.setPadding((int)(25/div), (int)(5/div), 0, (int)(5/div));
		titleLayout.setGravity(Gravity.CENTER_VERTICAL);
		titleLayout.setBackgroundResource(R.drawable.title_bg_yellow);
		titleImageView = new ImageView(context);
		titleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.tv_channel_set));
		LayoutParams titleImgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleImgParams.topMargin = (int)(15/div);
		titleImageView.setLayoutParams(titleImgParams);
		titleLayout.addView(titleImageView);
		titleTextView = new TextView(context);
		titleTextView.setTextSize((int)(52/dip));
		titleTextView.setTextColor(Color.BLACK);
		titleTextView.setText(context.getResources().getString(R.string.TV_CFG_CHANNEL_EDIT));
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams titletxtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		titletxtParams.leftMargin = (int)(25/div);
		titletxtParams.gravity = Gravity.CENTER_VERTICAL;
		titleTextView.setLayoutParams(titletxtParams);
		titleLayout.addView(titleTextView);
		this.addView(titleLayout, new LayoutParams((int)(845/div), (int)(140/div)));
		
		bodyScrollView = new ScrollView(context);
		bodyScrollView.setVerticalScrollBarEnabled(false);
		bodyScrollView.setHorizontalScrollBarEnabled(false);
		bodyScrollView.setScrollbarFadingEnabled(true);
		bodyScrollView.setAlwaysDrawnWithCacheEnabled(true);
		bodyScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		bodyScrollView.setBackgroundColor(Color.parseColor("#191919"));
		bodyScrollView.setPadding(0, (int)(5/div), 0, 0);
		
		bodyLayout = new LinearLayout(context);
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		bodyLayout.setBackgroundColor(Color.parseColor("#191919"));
		bodyScrollView.addView(bodyLayout, new LayoutParams(LayoutParams.MATCH_PARENT,(int)(840/div)));
		this.addView(bodyScrollView, new LayoutParams(LayoutParams.MATCH_PARENT, (int)(840/div)));
		
		bottomLayout = new LinearLayout(context);
		bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
		bottomLayout.setBackgroundColor(Color.parseColor("#191919"));
		bottomLayout.setGravity(Gravity.CENTER);
		bottomLayout.setPadding((int)(5/div), (int)(5/div), (int)(5/div), (int)(5/div));
		this.addView(bottomLayout, new LayoutParams(LayoutParams.MATCH_PARENT,(int)(100/div)));
		
		deleteTextView = initBottomTextView();
		deleteTextView.setText(context.getResources().getString(R.string.str_channel_edit_r_delete));
		deleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.channellist_ic_red, 0, 0, 0);
		deleteTextView.setCompoundDrawablePadding((int)(5/div));
		bottomLayout.addView(deleteTextView);
		
		moveTextView = initBottomTextView();
		moveTextView.setText(context.getResources().getString(R.string.str_channel_edit_ok_move));
		moveTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.channellist_ic_green, 0, 0, 0);
		moveTextView.setCompoundDrawablePadding((int)(5/div));
		bottomLayout.addView(moveTextView);
		
		favoriTextView = initBottomTextView();
		favoriTextView.setText(context.getResources().getString(R.string.str_channel_edit_y_favorite));
		favoriTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.channellist_ic_yellow, 0, 0, 0);
		favoriTextView.setCompoundDrawablePadding((int)(5/div));
		bottomLayout.addView(favoriTextView);
		
		skipTextView = initBottomTextView();
		skipTextView.setText(context.getResources().getString(R.string.str_channel_edit_b_skip));
		skipTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.channellist_ic_blue, 0, 0, 0);
		skipTextView.setCompoundDrawablePadding((int)(5/div));
		bottomLayout.addView(skipTextView);
		
		mLoadingDialog = new TvLoadingDialog();
		
		//临时存储，用于频道移动功能
		tmpItemView = new ChannelListItemView(mContext, this, this, 0, this);
		
		curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
		curProInfo = TvPluginControler.getInstance().getChannelManager().getCurrentTvProgramInfo();
		
		TvPluginControler.getInstance().getCilManager().registerUiOpListener(mCiUIOpListener);
		
		oProfileInfos.clear();
		oProfileInfos = null;
		oProfileInfos = TvPluginControler.getInstance().getCilManager().getTvOperatorProfileInfos();
		if(oProfileInfos != null)
			mOPCount = (short) oProfileInfos.size();
		
		long e = System.currentTimeMillis();
	}
	
	public void updateView(EnumChOpType chOpType)
	{
		long s = System.currentTimeMillis();
		if(curSource == TvEnumInputSource.E_INPUT_SOURCE_ATV){
			totalCount = TvPluginControler.getInstance().getChannelManager().
					getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_ATV_DTV.ordinal());
		}else if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV){
			int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
			if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS
					|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2){
				totalCount = TvPluginControler.getInstance().getChannelManager().
						getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_ATV_DTV.ordinal());
			}else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT
					|| curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT2){
				totalCount = TvPluginControler.getInstance().getChannelManager().
						getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_ATV_DTV.ordinal());
			}
			else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBC){
				totalCount = TvPluginControler.getInstance().getChannelManager().
						getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_ATV_DTV.ordinal());
			}
		}
		this.chOpType = chOpType;
		proInfoList.clear();
		proInfoList = null;
		switch (this.chOpType) 
		{
			case E_CH_OPTION_EDIT:
				proInfoList = TvPluginControler.getInstance().getChannelManager().
						getTvProgramInfoList(curCount, totalCount);
				break;
			case E_CH_OPTION_FAV:
				titleTextView.setText(mContext.getResources().getString(R.string.TV_CFG_FAVORITE_LIST));
				moveTextView.setVisibility(View.GONE);
				favoriTextView.setVisibility(View.GONE);
				skipTextView.setVisibility(View.GONE);
				deleteTextView.setText("OK");
				deleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ok_focus, 0, 0, 0);
				proInfoList = TvPluginControler.getInstance().getChannelManager().
						getTvProgramInfoFavList();
				break;
			case E_CH_OPTION_LIST:
				isOpMode = TvPluginControler.getInstance().getCilManager().isOpMode();
				proInfoList = TvPluginControler.getInstance().getChannelManager().
						getTvProgramInfoList(curCount, totalCount);
				if(isOpMode){
					titleTextView.setText(mContext.getResources().getString(R.string.str_ci_channellist_op_title));
					moveTextView.setVisibility(View.GONE);
					deleteTextView.setText(mContext.getResources().getString(R.string.str_ci_channellist_exit_op));
					favoriTextView.setVisibility(View.GONE);
					skipTextView.setVisibility(View.GONE);
					
					if(proInfoList.size() == 0)
					{
						deleteTextView.setFocusable(true);
						deleteTextView.requestFocus();
						deleteTextView.setOnKeyListener(new OnKeyListener() {					
							@Override
							public boolean onKey(View view, int keyCode, KeyEvent event) {
								// TODO Auto-generated method stub
								switch (keyCode) 
								{
								    case KeyEvent.KEYCODE_PROG_RED:
									    TvPluginControler.getInstance().getCilManager().exitCiOperatorProfile();
									    break;
								}
								return true;
							}
						});
					}
				}else {
					if(mOPCount != 0 && curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV){	
						deleteTextView.setVisibility(View.VISIBLE);
					}else {
						deleteTextView.setVisibility(View.GONE);
						this.removeView(bottomLayout);
						this.removeView(bodyScrollView);
						this.addView(bodyScrollView, new LayoutParams(LayoutParams.MATCH_PARENT, (int)(945/div)) );
					}
					titleTextView.setText(mContext.getResources().getString(R.string.TV_CFG_CHANNEL_LIST));
					moveTextView.setVisibility(View.GONE);
					deleteTextView.setText(mContext.getResources().getString(R.string.str_ci_channellist_op_menu));
					favoriTextView.setVisibility(View.GONE);
					skipTextView.setVisibility(View.GONE);
				}				
				break;
			case E_CH_OPTION_LOCK:
				titleTextView.setText(mContext.getResources().getString(R.string.TV_CFG_LOCK_CHANNEL_LIST));
				moveTextView.setText(mContext.getResources().getString(R.string.str_channel_edit_g_lock));
				deleteTextView.setVisibility(View.GONE);
				favoriTextView.setVisibility(View.GONE);
				skipTextView.setVisibility(View.GONE);
				proInfoList = TvPluginControler.getInstance().getChannelManager().
						getTvProgramInfoList(curCount, totalCount);
				break;
			default:
				break;
		}
		//删除最后一个频道或频道列表为空直接退出
		if(proInfoList.size() == 0){
			mHandler.sendEmptyMessage(SHOW_LAST_MSG);
			return;
		}
		curCount += proInfoList.size();
		if(proInfoList != null && proInfoList.size() > 0)
		{			
			bodyLayout.removeAllViews();
			chViewList.clear();
			
			ImageView diverLine = new ImageView(mContext);
			diverLine.setBackgroundResource(R.drawable.setting_line);
			LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / div), 1);
			lineLp.setMargins((int) (30 / div), (int)(5/div),(int) (30 / div), (int)(5/div));
			diverLine.setLayoutParams(lineLp);
			bodyLayout.addView(diverLine);
			int index = 0;
			for (TvProgramInfo proInfo : proInfoList) 
			{
				
				ChannelListItemView itemView = new ChannelListItemView(mContext, this, this, index, this);
				if(proInfo.serviceType == curProInfo.serviceType
						&& proInfo.number == curProInfo.number)
					defaultIndex = index;
				itemView.updateView(proInfo);
				bodyLayout.addView(itemView);
				chViewList.add(itemView);
				
				ImageView line = new ImageView(mContext);
				line.setBackgroundResource(R.drawable.setting_line);
				line.setLayoutParams(lineLp);
				bodyLayout.addView(line);
				
				index++;
			}
		}
			
		if(this.chOpType != EnumChOpType.E_CH_OPTION_FAV){
			//创建匿名线程发送消息，启动loading
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mHandler.sendEmptyMessage(UPDATE_MSG);
					mHandler.sendEmptyMessage(SHOW_LOADING);
				}
			}).start();
		}
		else{//喜爱列表不做刷新，默认高亮第一条数据
			chViewList.get(0).setFocusable(true);
			chViewList.get(0).requestFocus();
			chViewList.get(0).getOnFocusChangeListener().onFocusChange(chViewList.get(0), true);
		}
		
		long e = System.currentTimeMillis();
		Log.i("gky", getClass().getSimpleName() + "----------->updateView end");
	}
	
	private void refreshDataToUI(){
		Log.i("gky", getClass().toString() + "-->refreshDataToUI");
		proInfoList.clear();
		proInfoList = null;
		switch (chOpType) 
		{
			case E_CH_OPTION_EDIT:
				proInfoList = TvPluginControler.getInstance().getChannelManager().
						getTvProgramInfoList(curCount, totalCount);
				break;
			case E_CH_OPTION_FAV:
				proInfoList = TvPluginControler.getInstance().getChannelManager().
						getTvProgramInfoFavList();
				break;
			case E_CH_OPTION_LIST:
				proInfoList = TvPluginControler.getInstance().getChannelManager().
						getTvProgramInfoList(curCount, totalCount);
				break;
			case E_CH_OPTION_LOCK:
				proInfoList = TvPluginControler.getInstance().getChannelManager().
					getTvProgramInfoList(curCount, totalCount);
				break;
			default:
				break;
		}
		if(proInfoList == null || proInfoList.size() == 0)
			return;
		curCount += proInfoList.size();
		Log.i("gky", getClass().toString() + "-->refreshDataToUI curCount is "+curCount);
		if(proInfoList != null && proInfoList.size() > 0)
		{
			LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / div), 1);
			lineLp.setMargins((int) (30 / div), (int)(5/div),(int) (30 / div), (int)(5/div));
			int index = chViewList.size();//刷新的index不再从0开始计数,因为是附加进去的
			for (TvProgramInfo proInfo : proInfoList) 
			{
				
				ChannelListItemView itemView = new ChannelListItemView(mContext, this, this, index, this);
				if(proInfo.serviceType == curProInfo.serviceType
						&& proInfo.number == curProInfo.number)
					defaultIndex = index;
				itemView.updateView(proInfo);
				bodyLayout.addView(itemView);
				chViewList.add(itemView);
				
				ImageView line = new ImageView(mContext);
				line.setBackgroundResource(R.drawable.setting_line);
				line.setLayoutParams(lineLp);
				bodyLayout.addView(line);
				
				index++;
			}
		}
		Log.i("gky", getClass().toString() + "-->refreshDataToUI end");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(UPDATE_MSG);
			}
		}, "refreshDataToUI").start();
	}
	
	private void refreshOpfileList(){
		Log.i("gky", getClass().getSimpleName() + "----->enter into refreshOpfileList");
		long s = System.currentTimeMillis();
		
		// 创建匿名线程发送消息，启动loading
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(threadFlag)
				{
					try {
						mHandler.sendEmptyMessage(SHOW_LOADING);
						Thread.sleep(100);
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			}
		}).start();
		mOPCount = 0;
		oProfileInfos.clear();
		oProfileInfos = null;
		oProfileInfos = TvPluginControler.getInstance().getCilManager().getTvOperatorProfileInfos();
		if(oProfileInfos == null)
			return;
		mOPCount = (short) oProfileInfos.size();
		
		bodyLayout.removeAllViews();
		bodyScrollView.removeView(bodyLayout);
		bodyScrollView.invalidate();
		this.removeView(bodyScrollView);
		bodyScrollView = new ScrollView(mContext);
		bodyScrollView.setVerticalScrollBarEnabled(false);
		bodyScrollView.setHorizontalScrollBarEnabled(false);
		bodyScrollView.setScrollbarFadingEnabled(true);
		bodyScrollView.setAlwaysDrawnWithCacheEnabled(true);
		bodyScrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		bodyScrollView.setBackgroundColor(Color.parseColor("#191919"));
		bodyScrollView.setPadding(0, (int)(5/div), 0, 0);
		bodyLayout = new LinearLayout(mContext);
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		bodyLayout.setBackgroundColor(Color.parseColor("#191919"));
		bodyScrollView.addView(bodyLayout, new LayoutParams(LayoutParams.MATCH_PARENT,(int)(840/div)));
		this.removeView(bottomLayout);
		this.invalidate();
		this.addView(bodyScrollView, new LayoutParams(LayoutParams.MATCH_PARENT, (int)(840/div)));
		this.addView(bottomLayout, new LayoutParams(LayoutParams.MATCH_PARENT,(int)(100/div)));
		if(oProfileInfos.size() > 0){			

			chViewList.clear();
			
			ImageView diverLine = new ImageView(mContext);
			diverLine.setBackgroundResource(R.drawable.setting_line);
			LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / div), 1);
			lineLp.setMargins((int) (30 / div), (int)(5/div),(int) (30 / div), (int)(5/div));
			diverLine.setLayoutParams(lineLp);
			bodyLayout.addView(diverLine);
			int index = 0;
			for (TvOperatorProfileInfo opInfo : oProfileInfos) 
			{			
				ChannelListItemView itemView = new ChannelListItemView(mContext, this, this, index, this);
				itemView.updateView(opInfo);
				bodyLayout.addView(itemView);
				chViewList.add(itemView);
				
				ImageView line = new ImageView(mContext);
				line.setBackgroundResource(R.drawable.setting_line);
				line.setLayoutParams(lineLp);
				bodyLayout.addView(line);
				
				index++;
			}
		}else {
			curCount = 0;
			updateView(EnumChOpType.E_CH_OPTION_LIST);
		}
		if(chViewList.size() > 0){			
			chViewList.get(0).setFocusable(true);
		    chViewList.get(0).requestFocus();
		    chViewList.get(0).getOnFocusChangeListener().onFocusChange(chViewList.get(0), true);
		}
		
		mHandler.sendEmptyMessage(DISMISS_LOADING);
		
		long e = System.currentTimeMillis();
		threadFlag=false;
	}
	
	@Override
	public boolean onItemKeyDownListener(KeyEvent event, int index, TvProgramInfo proInfo, final TvOperatorProfileInfo oProfileInfo) 
	{
		// TODO Auto-generated method stub
		switch (event.getKeyCode()) {
			case KeyEvent.KEYCODE_PROG_RED:
				if(isMoving)
					break;
				if(chOpType == EnumChOpType.E_CH_OPTION_EDIT)
				{
					TvPluginControler.getInstance().getChannelManager().deleteProgram(proInfo);
					mHandler.sendEmptyMessage(SHOW_DELETE_MSG);
				}else if(chOpType == EnumChOpType.E_CH_OPTION_LIST){
					boolean bOpMode = false;
					bOpMode = TvPluginControler.getInstance().getCilManager().isOpMode();
					if(mOPCount != 0 && curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV
						&& !bOpMode){
						
						this.chOpType = EnumChOpType.E_CH_OPTION_OPFILE;
						deleteTextView.setVisibility(View.VISIBLE);
						skipTextView.setVisibility(View.VISIBLE);
						titleTextView.setText(mContext.getResources().getString(R.string.str_ci_channellist_op_title));
						deleteTextView.setText(mContext.getString(R.string.str_ci_channellist_enter_op));
						skipTextView.setText(mContext.getString(R.string.str_ci_channellist_op_del));

						mHandler.sendEmptyMessage(SHOW_OPFILE);
					}else if (bOpMode){
						TvPluginControler.getInstance().getCilManager().exitCiOperatorProfile();
					}
				}else if(chOpType == EnumChOpType.E_CH_OPTION_OPFILE){
					short opIndex = oProfileInfo.getOPCacheResideIndex();
					if(oProfileInfo.getOPAcceisable()){
						TvPluginControler.getInstance().getCilManager().enterCiOperatorProfile(opIndex);
					}
				}
				break;
			case KeyEvent.KEYCODE_PROG_GREEN:
				if(curCount < totalCount){
					mHandler.sendEmptyMessage(SHOW_FAILED_MSG);
					break;
				}
				isMoving = !isMoving;
				setMoveAble(isMoving);
				position = getSelectedIndex();
				swapProgramInfo(tmpItemView, chViewList.get(position));
				if(isMoving){
					startPosition = position;
				}else {
					targetPosition = position;
					Log.i("", getClass().toString() + "-->onItemKeyDownListener:: move "+startPosition+"->"+targetPosition);
					if(startPosition != targetPosition){
						if(chViewList.get(startPosition).
								getCurProgramInfo().serviceType == TvConfigTypes.SERVICE_TYPE_DTV){
							TvPluginControler.getInstance().getChannelManager().moveProgram(startPosition, targetPosition);
						}else {
							int mDtvDelOrHideNum = TvPluginControler.getInstance().getChannelManager().getDtvDelOrHideNum();
							TvPluginControler.getInstance().getChannelManager().moveProgram(startPosition + mDtvDelOrHideNum, 
									targetPosition + mDtvDelOrHideNum);
						}
						mHandler.sendEmptyMessage(SHOW_MOVE_MSG);
					}
					defaultIndex = targetPosition;
					if(chViewList.size() > 0){
						if(targetPosition >= chViewList.size())
							break;
						/*TvPluginControler.getInstance().getChannelManager().setProgram(chViewList.get(targetPosition).getCurProgramInfo());*/
						if(chViewList.get(targetPosition).
								getCurProgramInfo().serviceType == TvConfigTypes.SERVICE_TYPE_DTV)
							TvPluginControler.getInstance().getChannelManager().playDtvCurrentProgram();
					}
				}
				break;
			case KeyEvent.KEYCODE_PROG_BLUE:
				TvToastFocusDialog delDialog = new TvToastFocusDialog();
				delDialog.setOnBtClickListener(new OnBtClickListener() {
					
					@Override
					public void onClickListener(boolean flag) {
						// TODO Auto-generated method stub
						if(flag){
							TvPluginControler.getInstance().getCilManager().deleteOpCacheByIndex(oProfileInfo.getOPCacheResideIndex());
							mHandler.sendEmptyMessage(DELETE_OPFILE);
						}
					}
				});
				String opName = oProfileInfo.getOperatorProfileName();
				TvToastFocusData delData = new TvToastFocusData("", "", mContext.getString(R.string.str_ci_channellist_op_del_msg)+opName+"]?", 2);
				delDialog.processCmd(null, DialogCmd.DIALOG_SHOW, delData);
				break;
			case KeyEvent.KEYCODE_MENU:
			case KeyEvent.KEYCODE_BACK:
				if(parentDialog != null){
					TvPluginControler.getInstance().getCilManager().unregisterUiOpListener();
					parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_UP:
				if(event.getAction() == KeyEvent.ACTION_UP && isMoving && moveAble){
					swapProgramInfo(chViewList.get(position), chViewList.get(getSelectedIndex()));
					swapProgramInfo(chViewList.get(getSelectedIndex()), tmpItemView);
					position = getSelectedIndex();
					bodyLayout.invalidate();
				}else if (event.getAction() == KeyEvent.ACTION_DOWN && isMoving) {
					if(checkChmoveble(event.getKeyCode(), getSelectedIndex())){
						//Log.i("", getClass().toString() + "-->onItemKeyDownListener:: ACTION_DOWN moveAle is true return false");
						moveAble = true;
						if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
							int next = getSelectedIndex() + 1;
							if(next < chViewList.size()){
								chViewList.get(next).setFocusable(true);
								chViewList.get(next).requestFocus();
								chViewList.get(next).getOnFocusChangeListener().onFocusChange(chViewList.get(next), true);
							}
						}
						else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP){
							int next = getSelectedIndex() - 1;
							if(next >= 0){
								chViewList.get(next).setFocusable(true);
								chViewList.get(next).requestFocus();
								chViewList.get(next).getOnFocusChangeListener().onFocusChange(chViewList.get(next), true);
							}
						}
					}else {
						//Log.i("", getClass().toString() + "-->onItemKeyDownListener:: ACTION_DOWN moveAle is false return true");
						moveAble = false;
					}
				}else if(event.getAction() == KeyEvent.ACTION_DOWN && !isMoving){
					if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN){
						int next = getSelectedIndex() + 1;
						if(next < chViewList.size()){
							chViewList.get(next).setFocusable(true);
							chViewList.get(next).requestFocus();
							chViewList.get(next).getOnFocusChangeListener().onFocusChange(chViewList.get(next), true);
						}
					}
					else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP){
						int next = getSelectedIndex() - 1;
						if(next >= 0){
							if(next == 0){
								bodyScrollView.scrollTo(0, 0);
							}
							chViewList.get(next).setFocusable(true);
							chViewList.get(next).requestFocus();
							chViewList.get(next).getOnFocusChangeListener().onFocusChange(chViewList.get(next), true);
						}
					}
				}
				return true;//方向键由我们自己处理，不再交由系统处理
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				if(curProInfo.serviceType != proInfo.serviceType){
					int switchToSource = EnumInputSource.E_INPUT_SOURCE_ATV.ordinal();
					if(proInfo.serviceType == TvConfigTypes.SERVICE_TYPE_ATV)
						switchToSource = EnumInputSource.E_INPUT_SOURCE_ATV.ordinal();
					else{
						int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
						if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT || curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT2)
							switchToSource = EnumInputSource.E_INPUT_SOURCE_DTV.ordinal();
						else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS || curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2)
							switchToSource = EnumInputSource.E_INPUT_SOURCE_DTV2.ordinal();
						else if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBC)
							switchToSource = EnumInputSource.E_INPUT_SOURCE_KTV.ordinal();
						else
							switchToSource = EnumInputSource.E_INPUT_SOURCE_DTV.ordinal();
					}
					try {
						TvPluginControler.getInstance().getSwitchSource().switchSource(switchToSource);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				curProInfo = proInfo;
				curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
				return true;
			default:
				return false;
		}
		return true;
	}
	
	@Override
	public void onItemFocusChangeListener(boolean hasFocus, int index,
			TvProgramInfo proInfo) {
		// TODO Auto-generated method stub
		if(hasFocus){
			this.curSelectedIndex = index;
		}
	}
	
	private TextView initBottomTextView()
	{
		TextView textView = new TextView(mContext);
		textView.setTextSize((float)(28/dip));
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		textView.setSingleLine(true);
		textView.setEllipsize(TruncateAt.MARQUEE);
		textView.setMarqueeRepeatLimit(-1);
		textView.setSelected(true);
		LayoutParams txtParams = new LayoutParams((int)(150/div), (int)(40/div));
		txtParams.gravity = Gravity.CENTER;
		textView.setLayoutParams(txtParams);
		return textView;
	}

	public void setParentDialog(ChannelEditDialog parentDialog) 
	{
		this.parentDialog = parentDialog;
	}

	private void swapProgramInfo(ChannelListItemView start, ChannelListItemView target){
		start.updateView(target.getCurProgramInfo());
	}
	
	private int getSelectedIndex(){
		return curSelectedIndex;
	}
	
	public EnumChOpType getCurEnumChOpType(){
		return this.chOpType;
	}
	
	public boolean isMoving(){
		return isMoving;
	}
	
	public TvEnumInputSource getCurSource(){
		return curSource;
	}
	
	public boolean isShowingLoad(){
		return mLoadingDialog.isShowing();
	}
	
	private boolean checkChmoveble(int keyCode, int selItem) {
        TvProgramInfo cur = null;
        TvProgramInfo next = null;
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (selItem >= (chViewList.size() - 1)) {
                return false;
            }
            cur = chViewList.get(selItem).getCurProgramInfo();
            next = chViewList.get(selItem + 1).getCurProgramInfo();
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (selItem == 0) {
                return false;
            }
            cur = chViewList.get(selItem).getCurProgramInfo();
            next = chViewList.get(selItem - 1).getCurProgramInfo();
        }
        if (cur.serviceType == next.serviceType) {
            return true;
        } else {
            return false;
        }
	}
	
	private void setMoveAble(boolean isAble){
		if (isAble) {
			deleteTextView.setVisibility(View.GONE);
			moveTextView.setVisibility(View.VISIBLE);
			favoriTextView.setVisibility(View.GONE);
			skipTextView.setVisibility(View.GONE);
		}
		else {
			deleteTextView.setVisibility(View.VISIBLE);
			moveTextView.setVisibility(View.VISIBLE);
			favoriTextView.setVisibility(View.VISIBLE);
			skipTextView.setVisibility(View.VISIBLE);
		}
	}
	
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			switch (msg.what) 
			{
				case SHOW_DELETE_MSG:
					Log.i("gky", "enter into muiHandler");
					TvUIControler.getInstance().
						showMniToast(mContext.getResources().getString(R.string.str_tip_delete_program));
					TvPluginControler.getInstance().getChannelManager().clearRadioCache();
					curCount = 0;
					updateView(chOpType);
//					refreshOpfileList();
					break;
				case SHOW_SORT_NUM_MSG:
					break;
				case SHOW_SORT_NAME_MSG:
					break;
				case SHOW_SORT_NETID_MSG:
					break;
				case SHOW_LAST_MSG:
					if(!isOpMode)
					{
						parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
						TvUIControler.getInstance().
							showMniToast(mContext.getResources().getString(R.string.str_tip_empty_channel_list));
					}									
					break;
				case SHOW_MOVE_MSG:
					Toast.makeText(mContext,R.string.str_tip_move_program,Toast.LENGTH_LONG).show();
//					TvUIControler.getInstance().
//						showMniToast(mContext.getResources().getString(R.string.str_tip_move_program));
					TvPluginControler.getInstance().getChannelManager().clearRadioCache();
					curCount = 0;
					updateView(chOpType);
					break;
				case UPDATE_MSG:
					totalCount -= TvPluginControler.getInstance().getChannelManager().getChDelOrHideNum();
					if(curCount < totalCount){
						refreshDataToUI();
					}else {
						if(defaultIndex >= chViewList.size())
							defaultIndex = chViewList.size() - 1;
						if(defaultIndex > 8){
							bodyScrollView.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									bodyScrollView.scrollTo(0, (int)((defaultIndex-2)*110/div));
								}
							});
						}else {
							bodyScrollView.scrollTo(0, 0);
						}
						chViewList.get(defaultIndex).setFocusable(true);
						chViewList.get(defaultIndex).requestFocus();
						chViewList.get(defaultIndex).getOnFocusChangeListener().onFocusChange(chViewList.get(defaultIndex), true);
						/*TvUIControler.getInstance().
							showMniToast(mContext.getResources().getString(R.string.str_tip_update_channel_list));*/
						mHandler.sendEmptyMessage(DISMISS_LOADING);
					}
					break;
				case SHOW_FAILED_MSG:
					TvUIControler.getInstance().
						showMniToast(mContext.getResources().getString(R.string.str_tip_updating_channel_list));
					break;
				case SHOW_LOADING:
					mLoadingDialog.processCmd(null, DialogCmd.DIALOG_SHOW, 
							new TvLoadingData(mContext.getResources().getString(R.string.str_tip_loading_chlist_title), 
									mContext.getResources().getString(R.string.str_tip_loading_chlist_content)));
					break;
				case DISMISS_LOADING:
					mLoadingDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
					break;
				case DELETE_OPFILE:
					TvUIControler.getInstance().showMniToast("Delete Profiles Sucessfully!");
					mOPCount--;
					if(mOPCount == 0){
						curCount = 0;
						updateView(EnumChOpType.E_CH_OPTION_LIST);
					}else{
						refreshOpfileList();
					}
					break;
				case SHOW_OPFILE:
					refreshOpfileList();
				
					break;
			}
		}
	};
	
	private CiUIOpListener mCiUIOpListener = new CiUIOpListener() {
		
		@Override
		public void onCiUIOpListener(int event) {
			// TODO Auto-generated method stub
			if(event == TvConfigTypes.EVENT_UI_OP_SERVICE_LIST){
				totalCount = TvPluginControler.getInstance().getChannelManager().
						getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_ATV_DTV.ordinal());
				curProInfo = TvPluginControler.getInstance().getChannelManager().getCurrentTvProgramInfo();
				curCount = 0;
				updateView(EnumChOpType.E_CH_OPTION_LIST);				
			}else if(event == TvConfigTypes.EVENT_UI_OP_EXIT_SERVICE_LIST){
				curProInfo = TvPluginControler.getInstance().getChannelManager().getCurrentTvProgramInfo();
				curCount = 0;
				updateView(EnumChOpType.E_CH_OPTION_LIST);
			}
		}
	};
}
