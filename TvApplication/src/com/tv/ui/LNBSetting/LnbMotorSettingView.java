package com.tv.ui.LNBSetting;

import java.util.ArrayList;
import java.util.Arrays;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvUserLocationData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigTypes.EnumMotorCmd;
import com.tv.framework.define.TvContext;
import com.tv.framework.define.TvConfigTypes.EnumMotorSet;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.LNBSetting.MotorSettingEnterItemView.EnterItemViewListener;
import com.tv.ui.LNBSetting.MotorSettingEnumItemView.EnumItemListener;
import com.tv.ui.LNBSetting.MotorSettingProgressItemView.ProgressItemListener;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LnbMotorSettingView extends LinearLayout implements EnumItemListener, ProgressItemListener, EnterItemViewListener{

	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
	
	private Context mContext;
	
	private LinearLayout titleLayout = null;
	private TextView titleTextView = null;
	private ImageView titleImageView = null;
	
	private LinearLayout bodyLayout = null;
	
	private MotorSettingEnumItemView motorAuto = null;
	private MotorSettingEnumItemView motorContinue = null;
	private MotorSettingEnumItemView moveSetup = null;
	private MotorSettingEnterItemView storePosition = null;
	private MotorSettingEnterItemView goToPosition = null;
	private MotorSettingEnterItemView goToReference = null;
	private MotorSettingEnterItemView setWestLimit = null;
	private MotorSettingEnterItemView setEastLimit = null;
	private MotorSettingEnterItemView disableLimit = null;
	private MotorSettingEnterItemView goToX = null;
	private MotorSettingEnumItemView location = null;
	private MotorSettingEnumItemView longitudeDir = null;
	private MotorSettingProgressItemView longitudeAngle = null;
	private MotorSettingEnumItemView latitudeDir = null;
	private MotorSettingProgressItemView latitudeAngle = null;
	
	private String[] moveSteupStr = TvContext.context.getResources().
			getStringArray(R.array.str_array_move_setup_option);
	private String[] longitudeDirection = TvContext.context.getResources().
			getStringArray(R.array.str_array_longitude_direction_option);
	private String[] latitudeDirection = TvContext.context.getResources().
			getStringArray(R.array.str_array_latitude_direction_option);
	private String motorAutoStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_moto_auto);
	private String motorContinueStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_moto_continue);
	private String moveSetupStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_move_setup);;
	private String storePositionStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_store_positon);;
	private String goToPositionStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_go_to_position);;
	private String goToReferenceStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_go_to_reference);;
	private String setWestLimitStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_set_west_limit);;
	private String setEastLimitStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_set_east_limit);;
	private String disableLimitStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_disable_limit);;
	private String goToXStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_go_to_x);;
	private String locationStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_location);;
	private String longitudeDicStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_longitude_direction);;
	private String longitudeAngleStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_longitude_angle);;
	private String latitudeDirStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_latitude_direction);;
	private String latitudeAngleStr = TvContext.context.getResources().
			getString(R.string.str_moto_set_latitude_angle);;
	
	private ArrayList<View> redDisEqc1_2List = new ArrayList<View>();
	private ArrayList<View> greenDisEqc1_2List = new ArrayList<View>();
	private ArrayList<View> redDisEqc1_3List = new ArrayList<View>();
	private ArrayList<View> greenDisEqc1_3List = new ArrayList<View>();
	private ArrayList<View> yellowDisEqc1_3List = new ArrayList<View>();
	
	private static final int ITEM_ID_MOTOR_AUTO = 1001;
	private static final int ITEM_ID_MOTOR_CONTINUE = 1002;
	private static final int ITEM_ID_MOVE_SETUP = 1003;
	private static final int ITEM_ID_GO_TO_POSITION = 1004;
	private static final int ITEM_ID_GO_TO_REFERENCE = 1005;
	private static final int ITEM_ID_STORE_POSITION = 1006;
	private static final int ITEM_ID_SET_WEST_LIMIT = 1007;
	private static final int ITEM_ID_SET_EAST_LIMIT = 1008;
	private static final int ITEM_ID_DISABLE_LIMIT = 1009;
	private static final int ITEM_ID_GO_TO_X = 1010;
	private static final int ITEM_ID_LOCATION = 1011;
	private static final int ITEM_ID_LONGITUDE_DIRECTION = 1012;
	private static final int ITEM_ID_LONGITUDE_ANGLE = 1013;
	private static final int ITEM_ID_LATITUDE_DIRECTION = 1014;
	private static final int ITEM_ID_LATITUDE_ANGLE = 1015;
	
	private TvUserLocationData userLocationData = null;
	
	private EnumMotorSet curMotorSet = EnumMotorSet.E_MOTOR_SET_NONE;
	private int mCurLongitudeDirection = TvConfigTypes.LONGITUDE_DIRECTION_EAST;
	private int mCurLatituedeDirection = TvConfigTypes.LATITUDE_DIRECTION_SOUTH;
	private static final int LOCATIONINFO_MANUAL_SLOT = 0;
	
	private LnbMotorSettingDialog parentDialog = null;
	
	public LnbMotorSettingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.setOrientation(LinearLayout.VERTICAL);
		this.setLayoutParams(new LayoutParams((int)(845/div), (int)(945/div)));
		
		titleLayout = new LinearLayout(context);
		titleLayout.setOrientation(LinearLayout.HORIZONTAL);
		titleLayout.setPadding((int)(25/div), (int)(5/div), 0, (int)(5/div));
		titleLayout.setGravity(Gravity.CENTER_VERTICAL);
		titleLayout.setBackgroundColor(Color.parseColor("#3598DC"));
		titleImageView = new ImageView(context);
		titleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.tv_string_dtv_service));
		LayoutParams titleImgParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleImgParams.topMargin = (int)(15/div);
		titleImageView.setLayoutParams(titleImgParams);
		titleLayout.addView(titleImageView);
		titleTextView = new TextView(context);
		titleTextView.setTextSize((int)(52/dip));
		titleTextView.setTextColor(Color.WHITE);
		titleTextView.setText(context.getResources().getString(R.string.str_title_motor_setting));
		titleTextView.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams titletxtParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		titletxtParams.leftMargin = (int)(25/div);
		titletxtParams.gravity = Gravity.CENTER_VERTICAL;
		titleTextView.setLayoutParams(titletxtParams);
		titleLayout.addView(titleTextView);
		this.addView(titleLayout, new LayoutParams((int)(845/div), (int)(140/div)));
		
		bodyLayout = new LinearLayout(context);
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		bodyLayout.setBackgroundColor(Color.parseColor("#191919"));
		this.addView(bodyLayout, new LayoutParams(LayoutParams.MATCH_PARENT,(int)(945/div)));
		
		initData();
	}
	
	private void initData(){
		motorAuto = new MotorSettingEnumItemView(mContext);
		motorAuto.updateView(motorAutoStr, 0, new ArrayList<String>(Arrays.asList(moveSteupStr)));
		motorAuto.setId(ITEM_ID_MOTOR_AUTO);
		motorAuto.setItemListener(this);
		redDisEqc1_2List.add(motorAuto);
		greenDisEqc1_2List.add(motorAuto);
		redDisEqc1_3List.add(motorAuto);
		greenDisEqc1_3List.add(motorAuto);
		
		motorContinue = new MotorSettingEnumItemView(mContext);
		motorContinue.updateView(motorContinueStr, 0, new ArrayList<String>(Arrays.asList(moveSteupStr)));
		motorContinue.setId(ITEM_ID_MOTOR_CONTINUE);
		motorContinue.setItemListener(this);
		redDisEqc1_2List.add(motorContinue);
		redDisEqc1_3List.add(motorContinue);
		
		moveSetup = new MotorSettingEnumItemView(mContext);
		moveSetup.updateView(moveSetupStr, 0, new ArrayList<String>(Arrays.asList(moveSteupStr)));
		moveSetup.setId(ITEM_ID_MOVE_SETUP);
		moveSetup.setItemListener(this);
		redDisEqc1_2List.add(moveSetup);
		greenDisEqc1_2List.add(moveSetup);
		redDisEqc1_3List.add(moveSetup);
		greenDisEqc1_3List.add(moveSetup);
		
		storePosition = new MotorSettingEnterItemView(mContext);
		storePosition.updateView(storePositionStr);
		storePosition.setId(ITEM_ID_STORE_POSITION);
		storePosition.setEnterItemViewListener(this);
		redDisEqc1_2List.add(storePosition);
		redDisEqc1_3List.add(storePosition);
		
		goToPosition = new MotorSettingEnterItemView(mContext);
		goToPosition.updateView(goToPositionStr);
		goToPosition.setId(ITEM_ID_GO_TO_POSITION);
		goToPosition.setEnterItemViewListener(this);
		redDisEqc1_2List.add(goToPosition);
		redDisEqc1_3List.add(goToPosition);
		
		goToReference = new MotorSettingEnterItemView(mContext);
		goToReference.updateView(goToReferenceStr);
		goToReference.setId(ITEM_ID_GO_TO_REFERENCE);
		goToReference.setEnterItemViewListener(this);
		redDisEqc1_2List.add(goToReference);
		greenDisEqc1_2List.add(goToReference);
		redDisEqc1_3List.add(goToReference);
		greenDisEqc1_3List.add(goToReference);
		
		setWestLimit = new MotorSettingEnterItemView(mContext);
		setWestLimit.updateView(setWestLimitStr);
		setWestLimit.setId(ITEM_ID_SET_WEST_LIMIT);
		setWestLimit.setEnterItemViewListener(this);
		greenDisEqc1_2List.add(setWestLimit);
		greenDisEqc1_3List.add(setWestLimit);
		
		setEastLimit = new MotorSettingEnterItemView(mContext);
		setEastLimit.updateView(setEastLimitStr);
		setEastLimit.setId(ITEM_ID_SET_EAST_LIMIT);
		setEastLimit.setEnterItemViewListener(this);
		greenDisEqc1_2List.add(setEastLimit);
		greenDisEqc1_3List.add(setEastLimit);
		
		disableLimit = new MotorSettingEnterItemView(mContext);
		disableLimit.updateView(disableLimitStr);
		disableLimit.setId(ITEM_ID_DISABLE_LIMIT);
		disableLimit.setEnterItemViewListener(this);;
		greenDisEqc1_2List.add(disableLimit);
		greenDisEqc1_3List.add(disableLimit);
		
		goToX = new MotorSettingEnterItemView(mContext);
		goToX.updateView(goToXStr);
		goToX.setId(ITEM_ID_GO_TO_X);
		goToX.setEnterItemViewListener(this);
		redDisEqc1_3List.add(goToX);
		
		userLocationData = TvPluginControler.getInstance().getChannelManager().getTvUserLocationData();
		location = new MotorSettingEnumItemView(mContext);
		location.updateView(locationStr, userLocationData.mCurrentLocationNumber, userLocationData.mLocationName);
		location.setId(ITEM_ID_LOCATION);
		location.setItemListener(this);
		
		longitudeDir = new MotorSettingEnumItemView(mContext);
		longitudeDir.updateView(longitudeDicStr, mCurLongitudeDirection, new ArrayList<String>(Arrays.asList(longitudeDirection)));
		longitudeDir.setId(ITEM_ID_LONGITUDE_DIRECTION);
		longitudeDir.setItemListener(this);
		
		longitudeAngle = new MotorSettingProgressItemView(mContext);
		longitudeAngle.updateView(longitudeAngleStr, userLocationData.mCurrentLongitudeAngle, 0, 1800);
		longitudeAngle.setId(ITEM_ID_LONGITUDE_ANGLE);
		longitudeAngle.setItemChangeListener(this);
		
		latitudeDir = new MotorSettingEnumItemView(mContext);
		latitudeDir.updateView(latitudeDirStr, mCurLatituedeDirection, new ArrayList<String>(Arrays.asList(latitudeDirection)));
		latitudeDir.setId(ITEM_ID_LATITUDE_DIRECTION);
		latitudeDir.setItemListener(this);
		
		latitudeAngle = new MotorSettingProgressItemView(mContext);
		latitudeAngle.updateView(latitudeAngleStr, userLocationData.mCurrentLatitudeAngle, 0, 1800);
		latitudeAngle.setId(ITEM_ID_LATITUDE_ANGLE);
		latitudeAngle.setItemChangeListener(this);
		
		yellowDisEqc1_3List.add(location);
		yellowDisEqc1_3List.add(longitudeDir);
		yellowDisEqc1_3List.add(longitudeAngle);
		yellowDisEqc1_3List.add(latitudeDir);
		yellowDisEqc1_3List.add(latitudeAngle);
	}
	public void updateView(int optionType){
		EnumMotorSet setType = EnumMotorSet.values()[optionType];
		this.curMotorSet = setType;
		switch (curMotorSet) {
		case E_MOTOR_SET_RED_DISEQC_1_2:
			initForRedDisEqc1_2();
			break;
		case E_MOTOR_SET_GREEN_DISEQC_1_2:
			initForGreenDisEqc1_2();
			break;
		case E_MOTOR_SET_RED_DISEQC_1_3:
			initForRedDisEqc1_3();
			break;
		case E_MOTOR_SET_GREEN_DISEQC_1_3:
			initForGreenDisEqc1_3();
			break;
		case E_MOTOR_SET_YELLOW_DISEQC_1_3:
			initForYellowDisEqc1_3();
			break;
		default:
			break;
		}
	}

	private void initForRedDisEqc1_2(){
		
		ImageView diverLine = new ImageView(mContext);
		diverLine.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / div), 1);
		lineLp.setMargins((int) (30 / div), (int)(5/div),(int) (30 / div), (int)(5/div));
		diverLine.setLayoutParams(lineLp);
		bodyLayout.addView(diverLine);
		
		for (View view : redDisEqc1_2List) {
			bodyLayout.addView(view);
			
			ImageView diverLine1= new ImageView(mContext);
			diverLine1.setBackgroundResource(R.drawable.setting_line);
			diverLine1.setLayoutParams(lineLp);
			bodyLayout.addView(diverLine1);
		}
		
	}
	
	private void initForGreenDisEqc1_2(){
		
		ImageView diverLine = new ImageView(mContext);
		diverLine.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / div), 1);
		lineLp.setMargins((int) (30 / div), (int)(5/div),(int) (30 / div), (int)(5/div));
		diverLine.setLayoutParams(lineLp);
		bodyLayout.addView(diverLine);
		
		for (View view : greenDisEqc1_2List) {
			bodyLayout.addView(view);
			
			ImageView diverLine1= new ImageView(mContext);
			diverLine1.setBackgroundResource(R.drawable.setting_line);
			diverLine1.setLayoutParams(lineLp);
			bodyLayout.addView(diverLine1);
		}
	}
	
	private void initForRedDisEqc1_3(){
		
		ImageView diverLine = new ImageView(mContext);
		diverLine.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / div), 1);
		lineLp.setMargins((int) (30 / div), (int)(5/div),(int) (30 / div), (int)(5/div));
		diverLine.setLayoutParams(lineLp);
		bodyLayout.addView(diverLine);
		
		for (View view : redDisEqc1_3List) {
			bodyLayout.addView(view);
			
			ImageView diverLine1= new ImageView(mContext);
			diverLine1.setBackgroundResource(R.drawable.setting_line);
			diverLine1.setLayoutParams(lineLp);
			bodyLayout.addView(diverLine1);
		}
	}
	
	private void initForGreenDisEqc1_3(){
		
		ImageView diverLine = new ImageView(mContext);
		diverLine.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / div), 1);
		lineLp.setMargins((int) (30 / div), (int)(5/div),(int) (30 / div), (int)(5/div));
		diverLine.setLayoutParams(lineLp);
		bodyLayout.addView(diverLine);
		
		for (View view : greenDisEqc1_3List) {
			bodyLayout.addView(view);
			
			ImageView diverLine1= new ImageView(mContext);
			diverLine1.setBackgroundResource(R.drawable.setting_line);
			diverLine1.setLayoutParams(lineLp);
			bodyLayout.addView(diverLine1);
		}
	}
	
	private void initForYellowDisEqc1_3(){
		
		ImageView diverLine = new ImageView(mContext);
		diverLine.setBackgroundResource(R.drawable.setting_line);
		LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / div), 1);
		lineLp.setMargins((int) (30 / div), (int)(5/div),(int) (30 / div), (int)(5/div));
		diverLine.setLayoutParams(lineLp);
		bodyLayout.addView(diverLine);
		
		for (View view : yellowDisEqc1_3List) {
			bodyLayout.addView(view);
			
			ImageView diverLine1= new ImageView(mContext);
			diverLine1.setBackgroundResource(R.drawable.setting_line);
			diverLine1.setLayoutParams(lineLp);
			bodyLayout.addView(diverLine1);
		}
		
		updateForLocationChange();
	}

	@Override
	public void onEnterItemViewListener(View view, int keyCode) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_BACK){
			backToMenu();
			return ;
		}
		Log.i("gky", getClass().toString() + "-->onEnterItemViewListener id is "+view.getId());
		switch (view.getId()) {
			case ITEM_ID_STORE_POSITION:
				TvPluginControler.getInstance().getChannelManager().
					sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_SAVE_SAT_POSITION);
				break;
			case ITEM_ID_GO_TO_POSITION:
				TvPluginControler.getInstance().getChannelManager().
					sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_GOTO_SAT_POSITION);
				break;
			case ITEM_ID_GO_TO_REFERENCE:
				TvPluginControler.getInstance().getChannelManager().
					sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_GOTO_REF_POINT);
				break;
			case ITEM_ID_SET_WEST_LIMIT:
				TvPluginControler.getInstance().getChannelManager().
					sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_SET_WEST_LIMIT);
				break;
			case ITEM_ID_SET_EAST_LIMIT:
				TvPluginControler.getInstance().getChannelManager().
					sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_SET_EAST_LIMIT);
				break;
			case ITEM_ID_DISABLE_LIMIT:
				TvPluginControler.getInstance().getChannelManager().
					sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_DISABLE_LIMIT);
				break;
			case ITEM_ID_GO_TO_X:
				TvPluginControler.getInstance().getChannelManager().
					sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_GOTO_X);
				break;
			default:
				break;
		}
		TvUIControler.getInstance().showMniToast("Set Ok!");
	}

	@Override
	public void OnProgressItemChangeListener(View view, int value,
			int keyCode) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_BACK){
			backToMenu();
			return ;
		}
		Log.i("gky", getClass().toString() + "-->OnProgressItemChangeListener id is "+view.getId() + " value is "+value);
		switch (view.getId()) {
			case ITEM_ID_LONGITUDE_ANGLE:
				if(mCurLongitudeDirection == TvConfigTypes.LONGITUDE_DIRECTION_EAST)
					userLocationData.mCurrentLongitudeAngle = value;
				else if(mCurLongitudeDirection == TvConfigTypes.LONGITUDE_DIRECTION_WEST)
					userLocationData.mCurrentLongitudeAngle = -value;
				updateSetForUserLocationSetting();
				break;
			case ITEM_ID_LATITUDE_ANGLE:
				if(mCurLatituedeDirection == TvConfigTypes.LATITUDE_DIRECTION_NORTH)
					userLocationData.mCurrentLatitudeAngle = value;
				else if(mCurLatituedeDirection == TvConfigTypes.LATITUDE_DIRECTION_SOUTH)
					userLocationData.mCurrentLatitudeAngle = -value;
				updateSetForUserLocationSetting();
				break;
			default:
				break;
		}
	}

	@Override
	public void onEnumItemChangeListener(View view, int keyCode, int index) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_MENU
				|| keyCode == KeyEvent.KEYCODE_BACK){
			backToMenu();
			return ;
		}
		Log.i("gky", getClass().toString() + "-->onEnumItemChangeListener id is "+view.getId() + " index is "+index);
		switch (view.getId()) {
			case ITEM_ID_MOTOR_AUTO:
				if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
				{
					EnumMotorCmd motorCmd = EnumMotorCmd.values()[index];
					TvPluginControler.getInstance().getChannelManager().sendDiSEqCMotorCommand(motorCmd);
				}
				else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)
				{
					motorAuto.updateView(motorAutoStr, 0, new ArrayList<String>(Arrays.asList(moveSteupStr)));
					TvPluginControler.getInstance().getChannelManager().sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_HALTMOTOR);
				}
				break;
			case ITEM_ID_MOTOR_CONTINUE:
				if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
				{
					EnumMotorCmd motorCmd = EnumMotorCmd.values()[index];
					TvPluginControler.getInstance().getChannelManager().sendDiSEqCMotorCommand(motorCmd);
				}
				else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)
				{
					motorContinue.updateView(motorContinueStr, 0, new ArrayList<String>(Arrays.asList(moveSteupStr)));
					TvPluginControler.getInstance().getChannelManager().sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_HALTMOTOR);
				}
				break;
			case ITEM_ID_MOVE_SETUP:
				if(index == 0)
					break;
				else if(index == 1)
					TvPluginControler.getInstance().getChannelManager().sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_STEP_EAST);
				else if(index == 2)
					TvPluginControler.getInstance().getChannelManager().sendDiSEqCMotorCommand(EnumMotorCmd.DISEQC_MOTOR_CMD_STEP_WEST);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				moveSetup.updateView(moveSetupStr, 0, new ArrayList<String>(Arrays.asList(moveSteupStr)));
				moveSetup.invalidate();
				this.invalidate();
				break;
			case ITEM_ID_LOCATION:
				userLocationData.mCurrentLocationNumber = index;
				updateForLocationChange();
				Log.i("gky", getClass().getName() + "------------>"+FocusFinder.getInstance().findNextFocus(bodyLayout, view, View.FOCUS_DOWN));
				break;
			case ITEM_ID_LONGITUDE_DIRECTION:
				this.mCurLongitudeDirection = index;
				if(mCurLongitudeDirection == TvConfigTypes.LONGITUDE_DIRECTION_EAST){
					if(userLocationData.mCurrentLongitudeAngle < 0)
						userLocationData.mCurrentLongitudeAngle = -userLocationData.mCurrentLongitudeAngle;
				}
				else if(mCurLongitudeDirection == TvConfigTypes.LONGITUDE_DIRECTION_WEST){
					if(userLocationData.mCurrentLongitudeAngle > 0)
						userLocationData.mCurrentLongitudeAngle = -userLocationData.mCurrentLongitudeAngle;
				}
				updateSetForUserLocationSetting();
				break;
			case ITEM_ID_LATITUDE_DIRECTION:
				this.mCurLatituedeDirection = index;
				if(mCurLatituedeDirection == TvConfigTypes.LATITUDE_DIRECTION_NORTH){
					if(userLocationData.mCurrentLatitudeAngle < 0)
						userLocationData.mCurrentLatitudeAngle = -userLocationData.mCurrentLatitudeAngle;
				}
				else if(mCurLatituedeDirection == TvConfigTypes.LATITUDE_DIRECTION_SOUTH){
					if(userLocationData.mCurrentLatitudeAngle > 0)
						userLocationData.mCurrentLatitudeAngle = -userLocationData.mCurrentLatitudeAngle;
				}
				updateSetForUserLocationSetting();
				break;
			default:
				break;
		}
	}
	
	private void backToMenu(){
		Log.i("gky", getClass().toString() + "-->backMenu");
		parentDialog.processCmd(null, DialogCmd.DIALOG_DISMISS, null);
	}

	public void setParentDialog(LnbMotorSettingDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	public void updateForLocationChange(){
		Log.i("gky", getClass().getSimpleName() + " mCurrentLocationNumber is "+userLocationData.mCurrentLocationNumber);
		if(userLocationData.mCurrentLocationNumber == 0){
			longitudeDir.setEnabled(true);
			longitudeAngle.setEnabled(true);
			latitudeDir.setEnabled(true);
			latitudeAngle.setEnabled(true);
		}else{
			longitudeDir.setEnabled(false);
			longitudeAngle.setEnabled(false);
			latitudeDir.setEnabled(false);
			latitudeAngle.setEnabled(false);
		}
		
		// longitude > 0 : east ; longitude < 0 : west
		if (userLocationData.mLongitudeAngle
				.get(userLocationData.mCurrentLocationNumber) > 0) {
			longitudeDir.updateView(longitudeDicStr,
					TvConfigTypes.LONGITUDE_DIRECTION_EAST,
					new ArrayList<String>(Arrays.asList(longitudeDirection)));
			longitudeAngle.updateView(longitudeAngleStr,
					userLocationData.mLongitudeAngle
							.get(userLocationData.mCurrentLocationNumber), 0,1800);
		} else {
			longitudeDir.updateView(longitudeDicStr,
					TvConfigTypes.LONGITUDE_DIRECTION_WEST,
					new ArrayList<String>(Arrays.asList(longitudeDirection)));
			longitudeAngle.updateView(longitudeAngleStr,
					-userLocationData.mLongitudeAngle
							.get(userLocationData.mCurrentLocationNumber), 0,1800);
		}
		userLocationData.mCurrentLongitudeAngle = userLocationData.mLongitudeAngle
				.get(userLocationData.mCurrentLocationNumber);

		// latitude > 0 : north ; latitude < 0 : south
		if (userLocationData.mLatitudeAngle
				.get(userLocationData.mCurrentLocationNumber) > 0) {
			latitudeDir.updateView(latitudeDirStr,
					TvConfigTypes.LATITUDE_DIRECTION_NORTH,
					new ArrayList<String>(Arrays.asList(latitudeDirection)));
			latitudeAngle.updateView(latitudeAngleStr,
					userLocationData.mLatitudeAngle
							.get(userLocationData.mCurrentLocationNumber), 0,1800);
		} else {
			latitudeDir.updateView(latitudeDirStr,
					TvConfigTypes.LATITUDE_DIRECTION_SOUTH,
					new ArrayList<String>(Arrays.asList(latitudeDirection)));
			latitudeAngle.updateView(latitudeAngleStr,
					-userLocationData.mLatitudeAngle
							.get(userLocationData.mCurrentLocationNumber), 0,1800);
		}
		
		userLocationData.mLongitudeAngle.set(LOCATIONINFO_MANUAL_SLOT, userLocationData.mCurrentLongitudeAngle);
		userLocationData.mLatitudeAngle.set(LOCATIONINFO_MANUAL_SLOT, userLocationData.mCurrentLatitudeAngle);
		
		updateSetForUserLocationSetting();
	}
	
	public void updateSetForUserLocationSetting(){
		
		TvPluginControler.getInstance().getChannelManager().
			setTvUserLocationSetting(userLocationData);
	}

}
