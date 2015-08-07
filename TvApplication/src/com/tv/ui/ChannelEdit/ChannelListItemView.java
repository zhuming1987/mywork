package com.tv.ui.ChannelEdit;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.data.TvType;
import com.tv.framework.data.TvOperatorProfileInfo;
import com.tv.framework.data.TvProgramInfo;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.EnumChOpType;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;

public class ChannelListItemView extends LinearLayout implements View.OnKeyListener, View.OnFocusChangeListener{

	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
	private static final int[] drawableResIds = {
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
	
	private TvProgramInfo curProgramInfo = null;
	private TvOperatorProfileInfo curOpfileInfo = null;
	
	private int index;
	private boolean hasFocus = false;
	private EnumChOpType chOpType = EnumChOpType.E_CH_OPTION_EDIT;
	private onItemKeyListener itemKeyListener = null;
	private onItemFocusListener itemFocusListener = null;
	private ChannelListView parentView = null;
	
	public ChannelListItemView(Context context, onItemKeyListener itemKeyListener, 
			onItemFocusListener itemFocusListener,int index, ChannelListView parentView) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setLayoutParams(new LayoutParams((int)(845/div), (int)(100/div)));
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setOnKeyListener(this);
		this.setOnFocusChangeListener(this);
		/*this.setFocusable(true);
		this.setFocusableInTouchMode(true);*/
		this.itemKeyListener = itemKeyListener;
		this.itemFocusListener = itemFocusListener;
		this.index = index;
		this.parentView = parentView;
		this.chOpType = parentView.getCurEnumChOpType();
		
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
		/*favoriteParams.rightMargin = (int)(10/div);
		favoriteParams.leftMargin = (int)(50/div);
		favoriteParams.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;*/
		favoriteImageView.setLayoutParams(favoriteParams);
		tipLayout.addView(favoriteImageView);
		
		skipImageView = new ImageView(context);
		skipImageView.setImageResource(R.drawable.channel_edit_skip);
		skipImageView.setVisibility(View.INVISIBLE);
		skipImageView.setPadding((int)(5/div), 0, (int)(5/div), 0);
		LayoutParams skipParams = new LayoutParams((int)(60/div), (int)(50/div));
		/*skipParams.rightMargin = (int)(10/div);
		skipParams.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;*/
		skipImageView.setLayoutParams(skipParams);
		tipLayout.addView(skipImageView);
		
		lockImageView = new ImageView(context);
		lockImageView.setImageResource(R.drawable.channel_edit_lock);
		lockImageView.setVisibility(View.INVISIBLE);
		lockImageView.setPadding((int)(5/div), 0, (int)(5/div), 0);
		LayoutParams lockParams = new LayoutParams((int)(60/div), (int)(50/div));
		/*lockParams.rightMargin = (int)(10/div);
		lockParams.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;*/
		lockImageView.setLayoutParams(lockParams);
		tipLayout.addView(lockImageView);
		
		encryptImageView = new ImageView(context);
		encryptImageView.setImageResource(R.drawable.channel_edit_encrypt);
		encryptImageView.setVisibility(View.INVISIBLE);
		encryptImageView.setPadding((int)(5/div), 0, (int)(5/div), 0);
		LayoutParams encryptParams = new LayoutParams((int)(60/div), (int)(50/div));
		/*encryptParams.rightMargin = (int)(10/div);
		encryptParams.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;*/
		encryptImageView.setLayoutParams(encryptParams);
		tipLayout.addView(encryptImageView);
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public void updateView(TvProgramInfo proInfo){
		this.curProgramInfo = proInfo;
		if(curProgramInfo.serviceType >=0 && curProgramInfo.serviceType < drawableResIds.length)
			chTypeImageView.setImageResource(drawableResIds[proInfo.serviceType]);
		if(curProgramInfo.serviceType == TvConfigTypes.SERVICE_TYPE_ATV)
			chNumTextView.setText(String.valueOf(curProgramInfo.number+1));
		else
			chNumTextView.setText(String.valueOf(curProgramInfo.number));
		chNameTextView.setText(curProgramInfo.serviceName);
		if(curProgramInfo.favorite == TvConfigTypes.TV_CFG_TRUE)
			favoriteImageView.setVisibility(View.VISIBLE);
		else
			favoriteImageView.setVisibility(View.GONE);
		if(curProgramInfo.isSkip)
			skipImageView.setVisibility(View.VISIBLE);
		else
			skipImageView.setVisibility(View.GONE);
		if(curProgramInfo.isLock)
			lockImageView.setVisibility(View.VISIBLE);
		else
			lockImageView.setVisibility(View.GONE);
		if(curProgramInfo.isScramble)
			encryptImageView.setVisibility(View.VISIBLE);
		else
			encryptImageView.setVisibility(View.GONE);
	}
	
	public void updateView(TvOperatorProfileInfo opInfo){
		this.curOpfileInfo = opInfo;
		chNumTextView.setVisibility(View.INVISIBLE);
		chNameTextView.setText(curOpfileInfo.getOperatorProfileName());
		int sysType = curOpfileInfo.getOPSysType();
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

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		// TODO Auto-generated method stub
		Log.i("gky", getClass().toString() + "-->onFocusChange:: "+hasFocus + " / "+index);
		this.hasFocus = hasFocus;
		if(itemFocusListener != null)
			itemFocusListener.onItemFocusChangeListener(hasFocus, index, curProgramInfo);
		if(this.hasFocus){
			this.setBackground(TvContext.context.getResources().getDrawable(R.drawable.yellow_sel_bg));
		}else {
			this.setBackground(null);
		}
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("gky", getClass().toString() + "-->onKey keyCode is "+keyCode);
		if(parentView.isShowingLoad())//正在加载过程中,不响应按键
			return true;
		switch (keyCode) {
			case KeyEvent.KEYCODE_PROG_RED:
				if(itemKeyListener != null && event.getAction() == KeyEvent.ACTION_DOWN)
					itemKeyListener.onItemKeyDownListener(event, index, curProgramInfo, curOpfileInfo);
				break;
			case KeyEvent.KEYCODE_PROG_GREEN:
				if(event.getAction() == KeyEvent.ACTION_UP)
					break;
				if(chOpType == EnumChOpType.E_CH_OPTION_EDIT)
				{
					if(itemKeyListener != null)
						itemKeyListener.onItemKeyDownListener(event, index, curProgramInfo, curOpfileInfo);
				}else if(chOpType == EnumChOpType.E_CH_OPTION_LOCK){
					/**
					 * curProgramInfo为当前高亮的频道
					 */
					if(curProgramInfo.isLock){
						//解锁
						lockImageView.setVisibility(GONE);		
					}else{
						//上锁
						lockImageView.setVisibility(VISIBLE);
					}
					TvPluginControler.getInstance().getParentalControlManager().setChannelLock(curProgramInfo);	
				}				
				break;
			case KeyEvent.KEYCODE_PROG_YELLOW:
				if(event.getAction() == KeyEvent.ACTION_UP)
					break;
				if(parentView.isMoving())
					break;
				if(chOpType == EnumChOpType.E_CH_OPTION_EDIT
						|| chOpType == EnumChOpType.E_CH_OPTION_LOCK){
					if(curProgramInfo.favorite == TvConfigTypes.TV_CFG_TRUE){
						favoriteImageView.setVisibility(View.GONE);
						curProgramInfo.favorite = TvConfigTypes.TV_CFG_FALSE;
						TvPluginControler.getInstance().getChannelManager().deleteProgramFromFavorite(curProgramInfo);
					}else {
						favoriteImageView.setVisibility(View.VISIBLE);
						curProgramInfo.favorite = TvConfigTypes.TV_CFG_TRUE;
						TvPluginControler.getInstance().getChannelManager().addProgramToFavorite(curProgramInfo);
					}
				}
				break;
			case KeyEvent.KEYCODE_PROG_BLUE:
				if(event.getAction() == KeyEvent.ACTION_UP)
					break;
				if(parentView.isMoving())
					break;
				if(chOpType == EnumChOpType.E_CH_OPTION_EDIT
						|| chOpType == EnumChOpType.E_CH_OPTION_LOCK){
					if(curProgramInfo.isSkip){
						skipImageView.setVisibility(View.GONE);
					}else {
						skipImageView.setVisibility(VISIBLE);
					}
					TvPluginControler.getInstance().getChannelManager().setProgramSkip(curProgramInfo);
				}else if(chOpType == EnumChOpType.E_CH_OPTION_OPFILE){
					if(itemKeyListener != null)
						itemKeyListener.onItemKeyDownListener(event, index, curProgramInfo, curOpfileInfo);
				}
				break;
			case KeyEvent.KEYCODE_MENU:
			case KeyEvent.KEYCODE_BACK:
				if(itemKeyListener != null)
					itemKeyListener.onItemKeyDownListener(event, index, curProgramInfo, curOpfileInfo);
				break;
			case KeyEvent.KEYCODE_ENTER:
			case KeyEvent.KEYCODE_DPAD_CENTER:
				if(chOpType != EnumChOpType.E_CH_OPTION_OPFILE){
					/*if(curProgramInfo.serviceType == TvConfigTypes.SERVICE_TYPE_ATV
							&& parentView.getCurSource() == TvEnumInputSource.E_INPUT_SOURCE_DTV){
						try {
							TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_ATV.ordinal());
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else if(curProgramInfo.serviceType != TvConfigTypes.SERVICE_TYPE_ATV
							&& parentView.getCurSource() == TvEnumInputSource.E_INPUT_SOURCE_ATV){
						try {
							TvPluginControler.getInstance().getSwitchSource().switchSource(TvEnumInputSource.E_INPUT_SOURCE_DTV.ordinal());
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}*/
					TvPluginControler.getInstance().getChannelManager().setProgram(curProgramInfo);
					if(itemKeyListener != null)
						itemKeyListener.onItemKeyDownListener(event, index, curProgramInfo, curOpfileInfo);
				}
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_DPAD_DOWN:
				if(itemKeyListener != null)
					itemKeyListener.onItemKeyDownListener(event, index, curProgramInfo, curOpfileInfo);
				break;
			default:
				return false;
		}
		return true;
	}

	public void resetTextColor(int color){
		chNumTextView.setTextColor(color);
		chNameTextView.setTextColor(color);
	}
	
	public TvProgramInfo getCurProgramInfo() {
		return curProgramInfo;
	}
	
	public boolean isSelected(){
		return hasFocus;
	}

	/**
	 * 作为回调接口，向父View传递keyCode
	 * @author Administrator
	 *
	 */
	public interface onItemKeyListener{
		public boolean onItemKeyDownListener(KeyEvent event, int index, TvProgramInfo proInfo, final TvOperatorProfileInfo oProfileInfo);
	}
	
	public interface onItemFocusListener{
		public void onItemFocusChangeListener(boolean hasFocus, int index, TvProgramInfo proInfo); 
	}
}
