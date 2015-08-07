package com.tv.ui.ChannelEdit;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvOperatorProfileInfo;
import com.tv.framework.data.TvProgramInfo;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.data.TvOsType.EnumInputSource;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigTypes.EnumChOpType;
import com.tv.framework.define.TvConfigTypes.TvEnumInputSource;
import com.tv.framework.define.TvConfigTypes.TvEnumProgramCountType;
import com.tv.framework.plugin.tvfuncs.CiManager.CiUIOpListener;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

public class ChannelEditView extends LinearLayout implements OnScrollListener{

	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
	private Context mContext;
	private LinearLayout listLayout = null;
	private SortChannelView sortChannelView = null;
	private FindChannelByNameView findChannelView = null;
	private LinearLayout titleLayout = null;
	private TextView titleTextView = null;
	private ImageView titleImageView = null;
	
	private ChannelListView channelListView = null;
	private ChannelAdapter channelAdapter = null;
	private OperatorProfileAdapter oProfileAdapter = null;
	
	private LinearLayout bottomLayout = null;
	private TextView favoriTextView;
	private TextView deleteTextView;
	private TextView moveTextView;
	private TextView skipTextView;
	
	private ArrayList<TvProgramInfo> proInfos = new ArrayList<TvProgramInfo>();
	private ArrayList<TvOperatorProfileInfo> oProfileInfos = null;
	private DeskCiUIOpListener deskCiUIOpListener = new DeskCiUIOpListener();
	private TvProgramInfo curProInfo = null;
	private TvProgramInfo tempProInfo = new TvProgramInfo();
	private ChannelListOnKeyListener chListOnKeyListener = new ChannelListOnKeyListener();
	private ChannelEditDialog parentDialog = null;
	private EnumChOpType chOpType = EnumChOpType.E_CH_OPTION_EDIT;
	private TvEnumInputSource curSource = TvEnumInputSource.E_INPUT_SOURCE_ATV;
	private int totalCount = 0;
	private int mOPCount = 0;
	private int defaultIndex = 0;
	private int curPositon = 0;
	private int sourceIndex = 0;
	private int mDtvDelOrHideNum = 0;
	private int mChDelOrHideNum = 0;
	private boolean moveAble = false;
	private boolean isMoving = false;
	private HandlerThread switchCH = new HandlerThread("switchChThread");
	private SwitchProHandle switchProHandle = null;
	private int firstVisibleItem = 0;
	private int visibleItemCount = 0;
	private int totalItemCount = 0;
    
    private StateListDrawable stateListDrawable = new StateListDrawable();
	private Drawable focus = TvContext.context.getResources().getDrawable(R.drawable.yellow_sel_bg);
	private Drawable normal = null;
	
	long start = System.currentTimeMillis();
	
	public ChannelEditView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setLayoutParams(new LayoutParams((int)(1245/div), (int)(945/div)));
//		this.setBackgroundColor(Color.parseColor("#191919"));
		
		listLayout = new LinearLayout(context);
		listLayout.setOrientation(LinearLayout.VERTICAL);
		listLayout.setLayoutParams(new LayoutParams((int)(845/div), (int)(1080/div)));
		listLayout.setBackgroundColor(Color.parseColor("#191919"));
		
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
		listLayout.addView(titleLayout, new LayoutParams((int)(845/div), (int)(140/div)));
		
		stateListDrawable.addState(new int[]{android.R.attr.state_focused}, focus);
		stateListDrawable.addState(new int[]{android.R.attr.state_enabled}, normal);
		stateListDrawable.addState(new int[]{}, normal);
		
		getProInfoList();
		channelAdapter = new ChannelAdapter(context);
		channelAdapter.setProInfoList(proInfos);
		channelListView = new ChannelListView(context);
		channelListView.setAdapter(channelAdapter);
		channelListView.setOnKeyListener(chListOnKeyListener);
		channelListView.setBackgroundColor(Color.parseColor("#191919"));
		channelListView.setDividerHeight((int)(15/div));
		channelListView.setDivider(context.getResources().getDrawable(R.drawable.divider_line));
		channelListView.setSelector(stateListDrawable);
		channelListView.setFocusable(true);
		channelListView.setVerticalScrollBarEnabled(true);
		channelListView.setSelection(defaultIndex);
		channelListView.setOnScrollListener(this);
		channelListView.setHorizontalScrollBarEnabled(false);
		channelListView.setVerticalScrollBarEnabled(false);
		listLayout.addView(channelListView, new LayoutParams(LayoutParams.MATCH_PARENT, (int)(840/div)));
		
		bottomLayout = new LinearLayout(context);
		bottomLayout.setOnKeyListener(chListOnKeyListener);
		bottomLayout.setFocusable(false);
		bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
		bottomLayout.setBackgroundColor(Color.parseColor("#191919"));
		bottomLayout.setGravity(Gravity.CENTER);
		bottomLayout.setPadding((int)(5/div), (int)(5/div), (int)(5/div), (int)(5/div));
		
		
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
		
		listLayout.addView(bottomLayout, new LayoutParams(LayoutParams.MATCH_PARENT,(int)(100/div)));
		
		this.addView(listLayout);
		
		findChannelView = new FindChannelByNameView(context);
		findChannelView.setVisibility(View.GONE);
		
		sortChannelView = new SortChannelView(context);
		sortChannelView.setVisibility(View.GONE);
		
		this.addView(findChannelView);
		this.addView(sortChannelView);
		
		TvPluginControler.getInstance().getCilManager().registerUiOpListener(deskCiUIOpListener);
		
		switchCH.start();
		switchProHandle = new SwitchProHandle(switchCH.getLooper());
	}
	
	public void updateView(EnumChOpType eOpType){
		Log.v("yangcheng", "updateView.chOpType = " + eOpType);
		this.chOpType = eOpType;
		switch (chOpType){
		
			case E_CH_OPTION_EDIT:
				break;
			case E_CH_OPTION_FAV:
				titleTextView.setText(mContext.getResources().getString(R.string.TV_CFG_FAVORITE_LIST));
				moveTextView.setVisibility(View.GONE);
				favoriTextView.setVisibility(View.GONE);
				skipTextView.setVisibility(View.GONE);
				deleteTextView.setText("OK");
				deleteTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ok_focus, 0, 0, 0);
				getFavoriteProInfoList();
				channelAdapter.notifyDataSetChanged();
				channelListView.invalidate();
				break;
			case E_CH_OPTION_LIST:
				titleTextView.setText(mContext.getResources().getString(R.string.TV_CFG_CHANNEL_LIST));
				deleteTextView.setVisibility(View.GONE);
//				if(curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV2)
//				{
					favoriTextView.setVisibility(View.VISIBLE);
					favoriTextView.setText(R.string.channelFind);
					skipTextView.setVisibility(View.VISIBLE);
					skipTextView.setText(R.string.channelSort);
//				}
//				else
//				{
//					favoriTextView.setVisibility(View.GONE);
//					favoriTextView.setText(mContext.getResources().getString(R.string.str_channel_edit_y_favorite));
//					skipTextView.setVisibility(View.GONE);
//					skipTextView.setText(mContext.getResources().getString(R.string.str_channel_edit_b_skip));
//				}
				
				moveTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ok_focus, 0, 0, 0);
				if(TvPluginControler.getInstance().getCilManager().isOpMode()){
					titleTextView.setText(mContext.getResources().getString(R.string.str_ci_channellist_op_title));
					moveTextView.setVisibility(View.GONE);
					deleteTextView.setText(mContext.getResources().getString(R.string.str_ci_channellist_exit_op));
					deleteTextView.setVisibility(View.VISIBLE);
					favoriTextView.setVisibility(View.GONE);
					skipTextView.setVisibility(View.GONE);
				}else {
					getOpFiles();
					if(mOPCount != 0 && curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV){	
						deleteTextView.setVisibility(View.VISIBLE);
						moveTextView.setVisibility(View.GONE);
					}else {
						deleteTextView.setVisibility(View.GONE);
						moveTextView.setText("OK");
						moveTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ok_focus, 0, 0, 0);
					}
					titleTextView.setText(mContext.getResources().getString(R.string.TV_CFG_CHANNEL_LIST));
					deleteTextView.setText(mContext.getResources().getString(R.string.str_ci_channellist_op_menu));
//					if(curSource != TvEnumInputSource.E_INPUT_SOURCE_DTV2)
//					{
//						favoriTextView.setVisibility(View.GONE);
//						skipTextView.setVisibility(View.GONE);
//					}
				}	
				channelAdapter.notifyDataSetChanged();
				channelListView.invalidate();
				break;
			case E_CH_OPTION_LOCK:
				titleTextView.setText(mContext.getResources().getString(R.string.TV_CFG_LOCK_CHANNEL_LIST));
				moveTextView.setText(mContext.getResources().getString(R.string.str_channel_edit_g_lock));
				deleteTextView.setVisibility(View.GONE);
				favoriTextView.setVisibility(View.GONE);
				skipTextView.setVisibility(View.GONE);
				break;
			default:
				break;
		}
		if(channelListView.getCount() == 0){
			bottomLayout.setFocusable(true);
			bottomLayout.requestFocus();
		}else {
			bottomLayout.setFocusable(false);
			channelListView.setFocusable(true);
			channelListView.requestFocus();
		}
		long end = System.currentTimeMillis();
		Log.i("gky",getClass().getName() +"::updateView::loading take "+(end-start)+"ms");
	}
	
	private TextView initBottomTextView(){
		
		TextView textView = new TextView(mContext);
		textView.setTextSize((float)(28/dip));
		textView.setTextColor(Color.WHITE);
		textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		textView.setSingleLine(true);
		textView.setEllipsize(TruncateAt.MARQUEE);
		textView.setMarqueeRepeatLimit(-1);
		textView.setSelected(true);
		LayoutParams txtParams = new LayoutParams((int)(180/div), (int)(40/div));
		txtParams.gravity = Gravity.CENTER;
		textView.setLayoutParams(txtParams);
		return textView;
	}
	
	public void setParentDialog(ChannelEditDialog parentDialog){
		this.parentDialog = parentDialog;
	}
	
	private void getProInfoList(){
		long s = System.currentTimeMillis();
		totalCount = TvPluginControler.getInstance().getChannelManager().
				getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_ATV_DTV.ordinal());
		curProInfo = TvPluginControler.getInstance().getChannelManager().getCurrentTvProgramInfo();
		curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
		Log.i("gky", getClass().getSimpleName() + " getProInfoList totalCount: " + totalCount+ " curProInfo: "+curProInfo.serviceName);
		proInfos.clear();
		mChDelOrHideNum = 0;
		mDtvDelOrHideNum = 0;
		if(totalCount > 0){
			for (int queryIndex = 0; queryIndex < totalCount; queryIndex++) {
				TvProgramInfo proInfo = TvPluginControler.getInstance().getChannelManager().
						getProgramInfoByIndex(queryIndex);
				if(proInfo != null){
					if(proInfo.isDelete || !proInfo.isVisible){
						mChDelOrHideNum++;
						if(proInfo.serviceType == TvConfigTypes.SERVICE_TYPE_DTV)
							mDtvDelOrHideNum++;
						continue;
					}else {
						if(proInfo.serviceType == curProInfo.serviceType
								&& proInfo.number == curProInfo.number)
							defaultIndex = queryIndex - mChDelOrHideNum;
					}
					proInfos.add(proInfo);
				}
			}
		}
		long e = System.currentTimeMillis();
		Log.i("gky", getClass().getSimpleName() + " run getProInfoList take "+(e-s)+"ms");
	}
	
	private void getFavoriteProInfoList(){
		long s = System.currentTimeMillis();
		totalCount = TvPluginControler.getInstance().getChannelManager().
				getTvProgramCountByType(TvEnumProgramCountType.E_COUNT_ATV_DTV.ordinal());
		curProInfo = TvPluginControler.getInstance().getChannelManager().getCurrentTvProgramInfo();
		curSource = TvPluginControler.getInstance().getCommonManager().getCurrentInputSource();
		Log.i("gky", getClass().getSimpleName() + " getProInfoList totalCount: " + totalCount+ " curProInfo: "+curProInfo.serviceName);
		proInfos.clear();
		mChDelOrHideNum = 0;
		mDtvDelOrHideNum = 0;
		if(totalCount > 0){
			for (int queryIndex = 0; queryIndex < totalCount; queryIndex++) {
				TvProgramInfo proInfo = TvPluginControler.getInstance().getChannelManager().
						getProgramInfoByIndex(queryIndex);
				if(proInfo != null){
					if(proInfo.isDelete || !proInfo.isVisible || proInfo.favorite != TvConfigTypes.TV_CFG_TRUE){
						mChDelOrHideNum++;
						if(proInfo.serviceType == TvConfigTypes.SERVICE_TYPE_DTV)
							mDtvDelOrHideNum++;
						continue;
					}else {
						if(proInfo.serviceType == curProInfo.serviceType
								&& proInfo.number == curProInfo.number)
							defaultIndex = queryIndex - mChDelOrHideNum;
					}
					proInfos.add(proInfo);
				}
			}
		}
		long e = System.currentTimeMillis();
		Log.i("gky", getClass().getSimpleName() + " run getProInfoList take "+(e-s)+"ms");
	}
	
	private void getOpFiles(){
		long s = System.currentTimeMillis();
		oProfileInfos = TvPluginControler.getInstance().getCilManager().getTvOperatorProfileInfos();
		if(oProfileAdapter == null)
			oProfileAdapter = new OperatorProfileAdapter(mContext);
		if(oProfileInfos != null && oProfileInfos.size() > 0){
			mOPCount = oProfileInfos.size();
			oProfileAdapter.setOpInfos(oProfileInfos);
		}
		long e = System.currentTimeMillis();
		Log.i("gky", getClass().getSimpleName() + " run getOpFiles take "+(e-s)+"ms");
	}
	
	private void reFreshData(){
		proInfos.clear();
		getProInfoList();
		channelAdapter.notifyDataSetChanged();
		channelListView.invalidate();
	}
	
	private boolean checkChMoveAvle(int keyCode, int selItem){
		TvProgramInfo cur = null;
		TvProgramInfo next = null;
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (selItem >= (proInfos.size() - 1)) {
                return false;
            }
            cur = proInfos.get(selItem);
            next = proInfos.get(selItem + 1);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            if (selItem == 0) {
                return false;
            }
            cur = proInfos.get(selItem);
            next = proInfos.get(selItem - 1);
        }
        if (cur.serviceType == next.serviceType) {
            return true;
        } else {
            return false;
        }
	}
	
	private void swapTvProgramInfo(TvProgramInfo sInfo, TvProgramInfo eInfo){
		sInfo.queryIndex = eInfo.queryIndex;
		sInfo.number = eInfo.number;
		sInfo.majorNum = eInfo.majorNum;
		sInfo.minorNum = eInfo.minorNum;
		sInfo.progId = eInfo.progId;
		sInfo.favorite = eInfo.favorite;
		sInfo.isLock = eInfo.isLock;
		sInfo.isSkip = eInfo.isSkip;
		sInfo.isScramble = eInfo.isScramble;
		sInfo.isDelete = eInfo.isDelete;
		sInfo.isVisible = eInfo.isVisible;
		sInfo.isHide = eInfo.isHide;
		sInfo.serviceType = eInfo.serviceType;
		sInfo.screenMuteStatus = eInfo.screenMuteStatus;
		sInfo.serviceName = eInfo.serviceName;
		sInfo.frequency = eInfo.frequency;
		sInfo.transportStreamId = eInfo.transportStreamId;
		sInfo.serviceId = eInfo.serviceId;
		sInfo.antennaType = eInfo.antennaType;
	}
	
	private class ChannelListOnKeyListener implements View.OnKeyListener{

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if(false)Log.i("gky", getClass().getSimpleName() + " onKey: "+event.toString());
			if(event.getAction() == KeyEvent.ACTION_DOWN){
				if(proInfos.size() > 0)
				{
					if(channelListView.getSelectedItemPosition() >= proInfos.size())
						return false;
					final TvProgramInfo proInfo = proInfos.get(channelListView.getSelectedItemPosition());
				    switch (keyCode) 
				    {
				    case KeyEvent.KEYCODE_CHANNEL_DOWN:
				    	if((channelListView.getSelectedItemPosition() - 9) > 0)
				    		channelListView.setSelection((channelListView.getSelectedItemPosition() - 9));
				    	else
				    		channelListView.setSelection(0);
				    	break;
				    case KeyEvent.KEYCODE_CHANNEL_UP:
				    	if((channelListView.getSelectedItemPosition() + 9) < (channelListView.getCount() - 1))
				    		channelListView.setSelection((channelListView.getSelectedItemPosition() + 9));
				    	else if(visibleItemCount + firstVisibleItem != totalItemCount)
				    		channelListView.setSelection((channelListView.getCount() - 1));
				    	break;
				    case KeyEvent.KEYCODE_ENTER:
					case KeyEvent.KEYCODE_DPAD_CENTER:
						if(!isMoving){
							Log.i("gky", getClass().getSimpleName() + " enter to "+proInfo.serviceName+" from "+curProInfo.serviceName);
							switchProHandle.sendEmptyMessage(0);
						}
						return true;
					case KeyEvent.KEYCODE_BACK:
					/*case KeyEvent.KEYCODE_MENU:*/
						if(parentDialog != null){
							Log.i("gky", getClass().getSimpleName() + " back/menu dismiss");
							switchCH.quit();
							TvPluginControler.getInstance().getCilManager().unregisterUiOpListener();
							parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
						}
						return true;
					case KeyEvent.KEYCODE_PROG_RED:
						if(chOpType == EnumChOpType.E_CH_OPTION_EDIT && !isMoving){
							Log.i("gky", getClass().getSimpleName() + " RED delete CH "+proInfo.serviceName);
							TvPluginControler.getInstance().getChannelManager().deleteProgram(proInfo);
							TvProgramInfo delProgramInfo =  proInfos.remove(channelListView.getSelectedItemPosition());
							TvUIControler.getInstance().showMniToast("Delete CH:"+delProgramInfo.serviceName+" Successfully!");
							reFreshData();
						}else if(chOpType == EnumChOpType.E_CH_OPTION_LIST){
							Log.i("gky", getClass().getSimpleName()+" isOpMode:"+TvPluginControler.getInstance().getCilManager().isOpMode()
									+" mOPCount:"+mOPCount);
							if(!TvPluginControler.getInstance().getCilManager().isOpMode()
									&& mOPCount != 0 && curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV){
								chOpType = EnumChOpType.E_CH_OPTION_OPFILE;
								channelListView.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										deleteTextView.setVisibility(View.VISIBLE);
										skipTextView.setVisibility(View.VISIBLE);
										titleTextView.setText(mContext.getResources().getString(R.string.str_ci_channellist_op_title));
										deleteTextView.setText(mContext.getString(R.string.str_ci_channellist_enter_op));
										skipTextView.setText(mContext.getString(R.string.str_ci_channellist_op_del));
										getOpFiles();
										channelListView.setAdapter(oProfileAdapter);
										oProfileAdapter.notifyDataSetChanged();
									}
								});
							}else if(TvPluginControler.getInstance().getCilManager().isOpMode()){
								Log.i("yangcheng", " isOpMode exit");
								TvPluginControler.getInstance().getCilManager().exitCiOperatorProfile();
							}
						}else if(chOpType == EnumChOpType.E_CH_OPTION_OPFILE){
							TvOperatorProfileInfo oProfileInfo = oProfileInfos.get(channelListView.getSelectedItemPosition());
							if(oProfileInfo.getOPAcceisable())
								TvPluginControler.getInstance().getCilManager().enterCiOperatorProfile(oProfileInfo.getOPCacheResideIndex());
						}
						return true;
					case KeyEvent.KEYCODE_PROG_YELLOW:
						if(chOpType == EnumChOpType.E_CH_OPTION_EDIT && !isMoving){
							Log.i("gky", getClass().getSimpleName() + " YELLOW favorite CH:"+proInfo.serviceName);
							if(proInfo.favorite == TvConfigTypes.TV_CFG_TRUE){
								TvPluginControler.getInstance().getChannelManager().deleteProgramFromFavorite(proInfo);
								proInfo.favorite = TvConfigTypes.TV_CFG_FALSE;
							}else {
								TvPluginControler.getInstance().getChannelManager().addProgramToFavorite(proInfo);
								proInfo.favorite = TvConfigTypes.TV_CFG_TRUE;
							}
							channelAdapter.notifyDataSetChanged();
							channelListView.invalidate();
						}
						else if(chOpType == EnumChOpType.E_CH_OPTION_LIST)
						{
							if(sortChannelView.isShown())
							{
								sortChannelView.setVisibility(View.GONE);
							}
							
							if(findChannelView.isShown())
							{
								findChannelView.setVisibility(View.GONE);
							}
							else
							{
								findChannelView.setVisibility(View.VISIBLE);
								findChannelView.searchView.requestFocus();
							}
						}
						return true;
					case KeyEvent.KEYCODE_PROG_BLUE:
						if(chOpType == EnumChOpType.E_CH_OPTION_EDIT && !isMoving){
							Log.i("gky", getClass().getSimpleName() + " BLUE skip CH:"+proInfo.serviceName);
							TvPluginControler.getInstance().getChannelManager().setProgramSkip(proInfo);
							channelAdapter.notifyDataSetChanged();
							channelListView.invalidate();
						}else if(chOpType == EnumChOpType.E_CH_OPTION_OPFILE){
							TvToastFocusDialog delDialog = new TvToastFocusDialog();
							delDialog.setOnBtClickListener(new OnBtClickListener() {
								
								@Override
								public void onClickListener(boolean flag) {
									// TODO Auto-generated method stub
									if(flag){
										TvPluginControler.getInstance().getCilManager().
											deleteOpCacheByIndex(oProfileInfos.get(channelListView.getSelectedItemPosition()).getOPCacheResideIndex());
										mOPCount--;
										oProfileInfos.remove(channelListView.getSelectedItemPosition());
										oProfileAdapter.notifyDataSetChanged();
										if(mOPCount == 0){
											getProInfoList();
											channelListView.setAdapter(channelAdapter);
											channelListView.setSelection(defaultIndex);
											updateView(EnumChOpType.E_CH_OPTION_LIST);
										}
									}
								}
							});
							String opName = oProfileInfos.get(channelListView.getSelectedItemPosition()).getOperatorProfileName();
							TvToastFocusData delData = new TvToastFocusData("", "", mContext.getString(R.string.str_ci_channellist_op_del_msg)+opName+"]?", 2);
							delDialog.processCmd(null, DialogCmd.DIALOG_SHOW, delData);
						}
						else if(chOpType == EnumChOpType.E_CH_OPTION_LIST)
						{
							if(findChannelView.isShown())
							{
								findChannelView.setVisibility(View.GONE);
							}
							
							if(sortChannelView.isShown())
							{
								sortChannelView.setVisibility(View.GONE);
							}
							else
							{
								sortChannelView.setVisibility(View.VISIBLE);
								sortChannelView.sortTypeFreq.requestFocus();
							}
						}
						return true;
					case KeyEvent.KEYCODE_PROG_GREEN:
						Log.i("gky", getClass().getSimpleName() + " chOpType is "+chOpType.toString());
						if(chOpType == EnumChOpType.E_CH_OPTION_LOCK){
							Log.i("gky", getClass().getSimpleName() + " GREEN lock CH:"+proInfo.serviceName);
							TvPluginControler.getInstance().getParentalControlManager().setChannelLock(proInfo);
							channelAdapter.notifyDataSetChanged();
							channelListView.invalidate();
						}else if(chOpType == EnumChOpType.E_CH_OPTION_EDIT){
							isMoving = !isMoving;
							Log.i("gky", getClass().getSimpleName() + " GREEN move CH:"+proInfo);
							swapTvProgramInfo(tempProInfo, proInfo);
							curPositon = channelListView.getSelectedItemPosition();
							if(isMoving){
								sourceIndex = curPositon;
								deleteTextView.setVisibility(View.GONE);
								favoriTextView.setVisibility(View.GONE);
								skipTextView.setVisibility(View.GONE);
							}else{
								int targetIndex = curPositon;
								if(proInfos.get(sourceIndex).serviceType == TvConfigTypes.SERVICE_TYPE_DTV){
									TvPluginControler.getInstance().getChannelManager().moveProgram(sourceIndex, targetIndex);
								}else{
									TvPluginControler.getInstance().getChannelManager().moveProgram(sourceIndex+mDtvDelOrHideNum, targetIndex+mDtvDelOrHideNum);
								}
								deleteTextView.setVisibility(View.VISIBLE);
								favoriTextView.setVisibility(View.VISIBLE);
								favoriTextView.setText(mContext.getResources().getString(R.string.str_channel_edit_y_favorite));
								skipTextView.setVisibility(View.VISIBLE);
								skipTextView.setText(mContext.getResources().getString(R.string.str_channel_edit_b_skip));
								reFreshData();
								TvUIControler.getInstance().showMniToast("Move CH:"+proInfos.get(sourceIndex).serviceName+" Successfully!");
							}	
						}
						return true;
					case KeyEvent.KEYCODE_DPAD_RIGHT:
						if(sortChannelView.isShown())
						{
							sortChannelView.sortTypeFreq.requestFocus();
							return true;
						}
						if(findChannelView.isShown())
						{
							findChannelView.searchView.requestFocus();
							return true;
						}
					case KeyEvent.KEYCODE_DPAD_DOWN:
					case KeyEvent.KEYCODE_DPAD_UP:
						Log.i("gky", "channelListView.getSelectedItemPosition()=======" + channelListView.getSelectedItemPosition());
						if(isMoving){
							if(checkChMoveAvle(keyCode, channelListView.getSelectedItemPosition()))
								moveAble = true;
							else{
								moveAble = false;
								return true;
							}	
						}else if(keyCode == KeyEvent.KEYCODE_DPAD_UP && firstVisibleItem == 0
								&& channelListView.getSelectedItemPosition() == 0){
							channelListView.setSelection(channelListView.getCount()-1);
						}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN
								&& ((firstVisibleItem+visibleItemCount) == totalItemCount)
								&& (channelListView.getSelectedItemPosition() == (totalItemCount-1))){
							
							if(sortChannelView.isShown())
							{
								sortChannelView.sortTypeFreq.requestFocus();
								return true;
							}
							else
							{
								channelListView.setSelection(0);
							}
						}
						break;
					default:
						break;				
				    }
				}else{
                    switch (keyCode) 
				    {
				    case KeyEvent.KEYCODE_ENTER:
					case KeyEvent.KEYCODE_DPAD_CENTER:
					case KeyEvent.KEYCODE_PROG_YELLOW:
					case KeyEvent.KEYCODE_PROG_BLUE:
					case KeyEvent.KEYCODE_PROG_GREEN:
					case KeyEvent.KEYCODE_DPAD_DOWN:
					case KeyEvent.KEYCODE_DPAD_UP:
						return true;
					case KeyEvent.KEYCODE_BACK:
					/*case KeyEvent.KEYCODE_MENU:*/
						if(parentDialog != null){
							Log.i("gky", getClass().getSimpleName() + " back/menu dismiss");
							switchCH.quit();
							TvPluginControler.getInstance().getCilManager().unregisterUiOpListener();
							parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
						}
						return true;
					case KeyEvent.KEYCODE_PROG_RED:
						if(chOpType == EnumChOpType.E_CH_OPTION_EDIT && !isMoving){
							
						}else if(chOpType == EnumChOpType.E_CH_OPTION_LIST){
							if(!TvPluginControler.getInstance().getCilManager().isOpMode()
									&& mOPCount != 0 && curSource == TvEnumInputSource.E_INPUT_SOURCE_DTV){
								chOpType = EnumChOpType.E_CH_OPTION_OPFILE;
								channelListView.post(new Runnable() {						
									@Override
									public void run() {
										// TODO Auto-generated method stub
										deleteTextView.setVisibility(View.VISIBLE);
										skipTextView.setVisibility(View.VISIBLE);
										titleTextView.setText(mContext.getResources().getString(R.string.str_ci_channellist_op_title));
										deleteTextView.setText(mContext.getString(R.string.str_ci_channellist_enter_op));
										skipTextView.setText(mContext.getString(R.string.str_ci_channellist_op_del));
										getOpFiles();
										channelListView.setAdapter(oProfileAdapter);
										oProfileAdapter.notifyDataSetChanged();
									}
								});
							}else if(TvPluginControler.getInstance().getCilManager().isOpMode()){
								TvPluginControler.getInstance().getCilManager().exitCiOperatorProfile();
							}
						}else if(chOpType == EnumChOpType.E_CH_OPTION_OPFILE){
							TvOperatorProfileInfo oProfileInfo = oProfileInfos.get(channelListView.getSelectedItemPosition());
							if(oProfileInfo.getOPAcceisable())
								TvPluginControler.getInstance().getCilManager().enterCiOperatorProfile(oProfileInfo.getOPCacheResideIndex());
						}
						return true;						
					default:
						break;				
				    }
				}								
			}else if(event.getAction() == KeyEvent.ACTION_UP){
				
				if((keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)
						&& isMoving && moveAble){
					swapTvProgramInfo(proInfos.get(curPositon), proInfos.get(channelListView.getSelectedItemPosition()));
					swapTvProgramInfo(proInfos.get(channelListView.getSelectedItemPosition()), tempProInfo);
					curPositon = channelListView.getSelectedItemPosition();
					channelAdapter.notifyDataSetChanged();
					channelListView.invalidate();
					return true;
				}
			}
			return false;
		}
		
	}
	
	private class SwitchProHandle extends Handler{

		public SwitchProHandle(Looper looper) {
			super(looper);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			TvProgramInfo proInfo = proInfos.get(channelListView.getSelectedItemPosition());
			Log.i("gky", "----->"+Thread.currentThread().getName() + "<----- enter to "+proInfo.serviceName);
			TvPluginControler.getInstance().getChannelManager().setProgram(proInfo);
			int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
			if(curProInfo.serviceType != proInfo.serviceType){
				int switchToSource = EnumInputSource.E_INPUT_SOURCE_ATV.ordinal();
				if(proInfo.serviceType == TvConfigTypes.SERVICE_TYPE_ATV)
					switchToSource = EnumInputSource.E_INPUT_SOURCE_ATV.ordinal();
				else{
					
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
		}
	}
	private class ChannelListView extends ListView{
	    
		public ChannelListView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
	}
	
	private class ChannelListItemView extends LinearLayout{
		
		private final int[] drawableResIds = {
			R.drawable.atv,
			R.drawable.dtv,
			R.drawable.radio,
			R.drawable.data
		};
		
		private ImageView chTypeImageView;
		private TextView  chNumTextView;
		private TextView  chNameTextView;
		private ImageView favoriteImageView;
		private ImageView skipImageView;
		private ImageView lockImageView;
		private ImageView encryptImageView;
		
		public ChannelListItemView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			this.setOrientation(LinearLayout.HORIZONTAL);
			this.setLayoutParams(new LayoutParams((int)(845/div), (int)(100/div)));
			this.setGravity(Gravity.CENTER_VERTICAL);
			
			chTypeImageView = new ImageView(context);
			LayoutParams chTypeParams = new LayoutParams((int)(80/div), (int)(40/div));
			chTypeParams.leftMargin = (int)(25/div);
			chTypeImageView.setLayoutParams(chTypeParams);
			this.addView(chTypeImageView);
			
			chNumTextView = new TextView(context);
			chNumTextView.setTextSize((float)(34/dip));
			chNumTextView.setTextColor(Color.WHITE);
			chNumTextView.setSingleLine(true);
			chNumTextView.setGravity(Gravity.CENTER);
			LayoutParams chNumParams = new LayoutParams((int)(80/div), (int)(80/div));
			chNumParams.leftMargin = (int)(25/div);
			chNumTextView.setLayoutParams(chNumParams);
			this.addView(chNumTextView);
			
			chNameTextView = new TextView(context);
			chNameTextView.setTextSize((float)(34/dip));
			chNameTextView.setTextColor(Color.WHITE);
			chNameTextView.setSingleLine(true);
			chNameTextView.setEllipsize(TruncateAt.MARQUEE);
			chNameTextView.setMarqueeRepeatLimit(-1);
			chNameTextView.setSelected(true);
			chNameTextView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
			LayoutParams chNameParams = new LayoutParams((int)(300/div), (int)(80/div));
			chNameParams.leftMargin = (int)(15/div);
			chNameTextView.setLayoutParams(chNameParams);
			this.addView(chNameTextView);
			
			LinearLayout tipLayout = new LinearLayout(context);
			tipLayout.setOrientation(LinearLayout.HORIZONTAL);
			tipLayout.setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams tiParams = new LinearLayout.LayoutParams((int)(240/div), LayoutParams.MATCH_PARENT);
			tiParams.leftMargin = (int)(50/div);
			tipLayout.setLayoutParams(tiParams);
			this.addView(tipLayout);
			
			favoriteImageView = new ImageView(context);
			favoriteImageView.setImageResource(R.drawable.channel_edit_favorite);
			favoriteImageView.setVisibility(View.INVISIBLE);
			favoriteImageView.setPadding((int)(5/div), 0, (int)(5/div), 0);
			LayoutParams favoriteParams = new LayoutParams((int)(60/div), (int)(50/div));
			favoriteImageView.setLayoutParams(favoriteParams);
			tipLayout.addView(favoriteImageView);
			
			skipImageView = new ImageView(context);
			skipImageView.setImageResource(R.drawable.channel_edit_skip);
			skipImageView.setVisibility(View.INVISIBLE);
			skipImageView.setPadding((int)(5/div), 0, (int)(5/div), 0);
			LayoutParams skipParams = new LayoutParams((int)(60/div), (int)(50/div));
			skipImageView.setLayoutParams(skipParams);
			tipLayout.addView(skipImageView);
			
			lockImageView = new ImageView(context);
			lockImageView.setImageResource(R.drawable.channel_edit_lock);
			lockImageView.setVisibility(View.INVISIBLE);
			lockImageView.setPadding((int)(5/div), 0, (int)(5/div), 0);
			LayoutParams lockParams = new LayoutParams((int)(60/div), (int)(50/div));
			lockImageView.setLayoutParams(lockParams);
			tipLayout.addView(lockImageView);
			
			encryptImageView = new ImageView(context);
			encryptImageView.setImageResource(R.drawable.channel_edit_encrypt);
			encryptImageView.setVisibility(View.INVISIBLE);
			encryptImageView.setPadding((int)(5/div), 0, (int)(5/div), 0);
			LayoutParams encryptParams = new LayoutParams((int)(60/div), (int)(50/div));
			encryptImageView.setLayoutParams(encryptParams);
			tipLayout.addView(encryptImageView);
		}
		
		public void updateView(TvProgramInfo proInfo){
			if(proInfo.serviceType >=0 && proInfo.serviceType < drawableResIds.length)
				chTypeImageView.setImageResource(drawableResIds[proInfo.serviceType]);
			if(proInfo.serviceType == TvConfigTypes.SERVICE_TYPE_ATV)
				chNumTextView.setText(new BigDecimal(proInfo.number).add(new BigDecimal(1)).toString());
			else
				chNumTextView.setText(new BigDecimal(proInfo.number).toString());
			chNameTextView.setText(proInfo.serviceName);
			if(proInfo.favorite == TvConfigTypes.TV_CFG_TRUE)
				favoriteImageView.setVisibility(View.VISIBLE);
			else
				favoriteImageView.setVisibility(View.GONE);
			if(proInfo.isSkip)
				skipImageView.setVisibility(View.VISIBLE);
			else
				skipImageView.setVisibility(View.GONE);
			if(proInfo.isLock)
				lockImageView.setVisibility(View.VISIBLE);
			else
				lockImageView.setVisibility(View.GONE);
			if(proInfo.isScramble)
				encryptImageView.setVisibility(View.VISIBLE);
			else
				encryptImageView.setVisibility(View.GONE);
		}
		
		public void updateView(TvOperatorProfileInfo opInfo){
			chNumTextView.setVisibility(View.INVISIBLE);
			chNameTextView.setText(opInfo.getOperatorProfileName());
			int sysType = opInfo.getOPSysType();
			int curDtvRoute = TvPluginControler.getInstance().getCommonManager().getCurrentDtvRoute();
			if(sysType == TvOperatorProfileInfo.EnumDeliverySysType.E_DELIVERY_SYS_TDSD.ordinal()){
				if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT || curDtvRoute == TvConfigTypes.TV_ROUTE_DVBT2)
					chNameTextView.setTextColor(Color.WHITE);
				else
					chNameTextView.setTextColor(Color.GRAY);
				chTypeImageView.setImageResource(R.drawable.dvb_t);
			}else if(sysType == TvOperatorProfileInfo.EnumDeliverySysType.E_DELIVERY_SYS_CDSD.ordinal()){
				if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBC)
					chNameTextView.setTextColor(Color.WHITE);
				else
					chNameTextView.setTextColor(Color.GRAY);
				chTypeImageView.setImageResource(R.drawable.dvb_c);
			}else if(sysType == TvOperatorProfileInfo.EnumDeliverySysType.E_DELIVERY_SYS_SDSD.ordinal()){
				if(curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS || curDtvRoute == TvConfigTypes.TV_ROUTE_DVBS2)
					chNameTextView.setTextColor(Color.WHITE);
				else
					chNameTextView.setTextColor(Color.GRAY);
				chTypeImageView.setImageResource(R.drawable.dvb_s);
			}
		}
	}
	
	private class ChannelAdapter extends BaseAdapter{

		private Context mContext;
		private ArrayList<TvProgramInfo> proInfoList = null;
		
		public ChannelAdapter(Context context) {
			this.mContext = context;
		}
		
		public void setProInfoList(ArrayList<TvProgramInfo> proInfoList){
			this.proInfoList = proInfoList;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(proInfoList != null)
				return proInfoList.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){
				convertView = new ChannelListItemView(mContext);
				((ChannelListItemView)convertView).updateView(proInfoList.get(position));
				((ChannelListItemView)convertView).setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
			}else {
				((ChannelListItemView)convertView).updateView(proInfoList.get(position));
				((ChannelListItemView)convertView).setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
			}
			return convertView;
		}
		
	}
	
	private class OperatorProfileAdapter extends BaseAdapter{

		private Context mContext;
		private ArrayList<TvOperatorProfileInfo> opInfos = null;

		public OperatorProfileAdapter(Context mContext) {
			this.mContext = mContext;
		}
		
		public void setOpInfos(ArrayList<TvOperatorProfileInfo> opInfos) {
			this.opInfos = opInfos;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(opInfos != null)
				return opInfos.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){
				convertView = new ChannelListItemView(mContext);
				((ChannelListItemView)convertView).updateView(opInfos.get(position));
				((ChannelListItemView)convertView).setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
			}else {
				((ChannelListItemView)convertView).updateView(opInfos.get(position));
				((ChannelListItemView)convertView).setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
			}
			return convertView;
		}
	}
	
	private class DeskCiUIOpListener implements CiUIOpListener{

		@Override
		public void onCiUIOpListener(int event) {
			// TODO Auto-generated method stub
			Log.v("yangcheng", "onCiUIOpListener.event = " +event);
			if(event == TvConfigTypes.EVENT_UI_OP_SERVICE_LIST){
				getProInfoList();
				channelListView.setAdapter(channelAdapter);
				channelListView.setSelection(defaultIndex);
				updateView(EnumChOpType.E_CH_OPTION_LIST);				
			}else if(event == TvConfigTypes.EVENT_UI_OP_EXIT_SERVICE_LIST){
				getProInfoList();
				channelListView.setAdapter(channelAdapter);
				channelListView.setSelection(defaultIndex);
				updateView(EnumChOpType.E_CH_OPTION_LIST);
			}
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.firstVisibleItem = firstVisibleItem;
		this.visibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
		Log.i("gky", getClass()+" onScroll::firstVisibleItem: "+firstVisibleItem+" visibleItemCount: "+visibleItemCount+" totalItemCount: "+totalItemCount);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
