package com.tv.ui.LNBSetting;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvSatelliteSetupPageData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SatelliteEditView extends LinearLayout implements OnKeyListener
{
	private SatelliteOperationDialog parentDialog;
	private Context mContext;
	private static int index;
	private static int satellitenumber;
	private static int derectionindex;
	private static int anglevalue;
	private static int bandindex;
	
	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dip = TvUIControler.getInstance().getDipDiv();
    
    private LinearLayout mainLayout,bodyLayout;
    private TextView titleText;
    private LinearLayout numLayout,nameLayout,derectionLayout,angleLayout,bandLayout,okLayout;
    private LinearLayout decItemLayout,bandItemLayout,angleItemLayout;
    private TextView numTitle,nameTitle,derectionTitle,angleTitle,bandTitle;
    private TextView numText,derectionText,angleText,bandText;
    private EditText nameText;
    private Button okButton;
    private ImageView divideLine;   
    private ImageView leftArrow,rightArrow;   
	
    private int mainWidth = (int)(1100/div);
    private int titleHeight = (int)(120/div);
    private int bodyHeight = (int)(960/div);
    
    private final String[] mDerection = TvContext.context.getResources().getStringArray(R.array.str_array_satellite_derection);
    private final String[] mBand = TvContext.context.getResources().getStringArray(R.array.str_array_satellite_band);
	public SatelliteEditView(Context context) 
	{
		super(context);
		// TODO Auto-generated constructor stub
		mContext = context;
        setOrientation(LinearLayout.VERTICAL);
        setFocusable(true);
		initView();
	}
	
	private void initView()
	{
		mainLayout = new LinearLayout(mContext);
		mainLayout.setOrientation(LinearLayout.VERTICAL);
        this.addView(mainLayout, new LayoutParams(mainWidth,LayoutParams.MATCH_PARENT));
        
        titleText = initTextView(mContext.getString(R.string.str_lnb_signal_add), (int) (52 / dip),
        		Color.WHITE, Gravity.CENTER | Gravity.CENTER_VERTICAL);
        titleText.setBackgroundColor(Color.rgb(53,152,220));
        titleText.setText(R.string.str_lnb_item_edit);
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
        
        numLayout = new LinearLayout(mContext);
        numLayout.setOrientation(LinearLayout.HORIZONTAL);
        numLayout.setFocusable(false);
        bodyLayout.addView(numLayout, LayoutParams.MATCH_PARENT,titleHeight);
        numTitle = new TextView(mContext);  
        numTitle.setTextSize((int) (36 / dip));
        numTitle.setText(R.string.str_lnb_signal_number_title);
        numLayout.addView(numTitle,itemParams);
        numText = new TextView(mContext);
        numText.setTextSize((int) (36 / dip));
        numText.setText("007");
        numText.setGravity(Gravity.CENTER);
        numLayout.addView(numText,contentParams);
        
        addDivideLine();
        
        nameLayout = new LinearLayout(mContext);
        nameLayout.setOrientation(LinearLayout.HORIZONTAL);
        bodyLayout.addView(nameLayout, LayoutParams.MATCH_PARENT,titleHeight);
        nameTitle = new TextView(mContext);  
        nameTitle.setTextSize((int) (36 / dip));
        nameTitle.setText(R.string.str_lnb_signal_name_title);
        nameLayout.addView(nameTitle,itemParams);
        nameText = new EditText(mContext);
        nameText.setTextSize((int) (36 / dip));
        nameText.setText("Edit Name");
        nameText.setGravity(Gravity.CENTER);
        nameLayout.addView(nameText,contentParams);
        nameText.setOnKeyListener(this);
        nameText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					nameTitle.setTextColor(Color.BLUE);
				}
				else 
				{
					nameTitle.setTextColor(Color.WHITE);
				}
			}
		});
        
        addDivideLine();
        
        derectionLayout = new LinearLayout(mContext);
        derectionLayout.setOrientation(LinearLayout.HORIZONTAL);
        bodyLayout.addView(derectionLayout, LayoutParams.MATCH_PARENT,titleHeight);
        derectionTitle = new TextView(mContext);  
        derectionTitle.setTextSize((int) (36 / dip));
        derectionTitle.setText(R.string.str_lnb_signal_derection_title);
        derectionLayout.addView(derectionTitle,itemParams);
        decItemLayout = new LinearLayout(mContext);
        decItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        decItemLayout.setGravity(Gravity.CENTER_VERTICAL);
        derectionLayout.addView(decItemLayout,contentParams);
        leftArrow = new ImageView(mContext);
        leftArrow.setBackgroundResource(R.drawable.simulation_arrow_left);
        decItemLayout.addView(leftArrow,(int)(42/div),LayoutParams.WRAP_CONTENT);
        derectionText = new TextView(mContext);
        derectionText.setTextSize((int) (36 / dip));
        derectionText.setGravity(Gravity.CENTER);
        decItemLayout.addView(derectionText,(int)(518/div),LayoutParams.WRAP_CONTENT);
        rightArrow = new ImageView(mContext);
        rightArrow.setBackgroundResource(R.drawable.simulation_arrow_right);
        decItemLayout.addView(rightArrow,(int)(42/div),LayoutParams.WRAP_CONTENT);
        derectionLayout.setOnKeyListener(this);
        derectionLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					derectionTitle.setTextColor(Color.BLUE);
					derectionText.setTextColor(Color.BLACK);
					decItemLayout.setBackgroundResource(R.drawable.blue_sel_bg);
				}
				else 
				{
					derectionTitle.setTextColor(Color.WHITE);
					derectionText.setTextColor(Color.WHITE);
					decItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
				}
			}
		});
                
        addDivideLine();
        
        angleLayout = new LinearLayout(mContext);
        angleLayout.setOrientation(LinearLayout.HORIZONTAL);
        bodyLayout.addView(angleLayout, LayoutParams.MATCH_PARENT,titleHeight);
        angleTitle = new TextView(mContext);  
        angleTitle.setTextSize((int) (36 / dip));
        angleTitle.setText(R.string.str_lnb_signal_angle_title);
        angleLayout.addView(angleTitle,itemParams);
        angleItemLayout = new LinearLayout(mContext);
        angleItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        angleItemLayout.setGravity(Gravity.CENTER_VERTICAL);
        angleLayout.addView(angleItemLayout,contentParams);
        leftArrow = new ImageView(mContext);
        leftArrow.setBackgroundResource(R.drawable.simulation_arrow_left);
        angleItemLayout.addView(leftArrow,(int)(42/div),LayoutParams.WRAP_CONTENT);
        angleText = new TextView(mContext);
        angleText.setTextSize((int) (36 / dip));
        angleText.setGravity(Gravity.CENTER);
        angleItemLayout.addView(angleText,(int)(518/div),LayoutParams.WRAP_CONTENT);
        rightArrow = new ImageView(mContext);
        rightArrow.setBackgroundResource(R.drawable.simulation_arrow_right);
        angleItemLayout.addView(rightArrow,(int)(42/div),LayoutParams.WRAP_CONTENT);
        angleLayout.setOnKeyListener(this);
        angleLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					angleTitle.setTextColor(Color.BLUE);
					angleText.setTextColor(Color.BLACK);
					angleItemLayout.setBackgroundResource(R.drawable.blue_sel_bg);
				}
				else 
				{
					angleTitle.setTextColor(Color.WHITE);
					angleText.setTextColor(Color.WHITE);
					angleItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
				}
			}
		});
        
        addDivideLine();
        
        bandLayout = new LinearLayout(mContext);
        bandLayout.setOrientation(LinearLayout.HORIZONTAL);
        bodyLayout.addView(bandLayout, LayoutParams.MATCH_PARENT,titleHeight);
        bandTitle = new TextView(mContext);  
        bandTitle.setTextSize((int) (36 / dip));
        bandTitle.setText(R.string.str_lnb_signal_band_title);
        bandLayout.addView(bandTitle,itemParams);
        bandItemLayout = new LinearLayout(mContext);
        bandItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        bandItemLayout.setGravity(Gravity.CENTER_VERTICAL);
        bandLayout.addView(bandItemLayout,contentParams);
        leftArrow = new ImageView(mContext);
        leftArrow.setBackgroundResource(R.drawable.simulation_arrow_left);
        bandItemLayout.addView(leftArrow,(int)(42/div),LayoutParams.WRAP_CONTENT);
        bandText = new TextView(mContext);
        bandText.setTextSize((int) (36 / dip));
        bandText.setGravity(Gravity.CENTER);
        bandItemLayout.addView(bandText,(int)(518/div),LayoutParams.WRAP_CONTENT);
        rightArrow = new ImageView(mContext);
        rightArrow.setBackgroundResource(R.drawable.simulation_arrow_right);
        bandItemLayout.addView(rightArrow,(int)(42/div),LayoutParams.WRAP_CONTENT);
        bandLayout.setOnKeyListener(this);
        bandLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					bandTitle.setTextColor(Color.BLUE);
					bandText.setTextColor(Color.BLACK);
					bandItemLayout.setBackgroundResource(R.drawable.blue_sel_bg);
				}
				else 
				{
					bandTitle.setTextColor(Color.WHITE);
					bandText.setTextColor(Color.WHITE);
					bandItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
				}
			}
		});
                
        addDivideLine();
        
        okLayout = new LinearLayout(mContext);
        okLayout.setGravity(Gravity.CENTER);
        okButton = new Button(mContext);
        okButton.setBackgroundResource(R.drawable.setting_unselect_bg);
        okButton.setText(R.string.confirm);
        okButton.setTextSize((int)(30/dip));  
        okLayout.addView(okButton,(int)(600/div),(int)(60/div));
        okLayout.setOnKeyListener(this);
        okLayout.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                StartEditSatellite();
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
	
	public void updateView(int number) 
	{
		TvSatelliteSetupPageData satellitesetupdata = TvPluginControler.getInstance().getChannelManager().getEditSatellitePageData(number);
		satellitenumber = number;
		derectionindex = satellitesetupdata.SaDirection;
		anglevalue = satellitesetupdata.SaLongitudeAngle;
		bandindex = satellitesetupdata.SaBand;
		numText.setText(satellitesetupdata.SaNumber);
		nameText.setText(satellitesetupdata.SaName);
		derectionText.setText(mDerection[satellitesetupdata.SaDirection]);
		angleText.setText(String.valueOf((float)satellitesetupdata.SaLongitudeAngle/10));
		bandText.setText(mBand[satellitesetupdata.SaBand]);
		nameText.setFocusable(true);
		derectionLayout.setFocusable(false);
		angleLayout.setFocusable(false);
		bandLayout.setFocusable(false);
		okLayout.setFocusable(false);
		nameText.requestFocus();
		index = 1;
	}
	
	public void setParentDialog(SatelliteOperationDialog parentDialog) 
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

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_DOWN)
		{
			Log.v("zhm","SatelliteEditView keyCode == " + keyCode + ";");
			if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
			{
				switch(index)
				{
					case 1:
						nameText.setFocusable(false);
						derectionLayout.setFocusable(true);
						angleLayout.setFocusable(false);
						bandLayout.setFocusable(false);
						okLayout.setFocusable(false);
						derectionLayout.requestFocus();
						index = 2;
						break;
					case 2:
						nameText.setFocusable(false);
						derectionLayout.setFocusable(false);
						angleLayout.setFocusable(true);
						bandLayout.setFocusable(false);
						okLayout.setFocusable(false);
						angleLayout.requestFocus();
						index = 3;
						break;
					case 3:
						nameText.setFocusable(false);
						derectionLayout.setFocusable(false);
						angleLayout.setFocusable(false);
						bandLayout.setFocusable(true);
						okLayout.setFocusable(false);
						bandLayout.requestFocus();
						index = 4;
						break;	
					case 4:
						nameText.setFocusable(false);
						derectionLayout.setFocusable(false);
						angleLayout.setFocusable(false);
						bandLayout.setFocusable(false);
						okLayout.setFocusable(true);
						okLayout.requestFocus();
						index = 5;
						break;
					case 5:
						nameText.setFocusable(true);
						derectionLayout.setFocusable(false);
						angleLayout.setFocusable(false);
						bandLayout.setFocusable(false);
						okLayout.setFocusable(false);
						nameText.requestFocus();
						index = 1;
						break;
				}
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
			{
				switch(index)
				{
					case 1:
						nameText.setFocusable(false);
				        derectionLayout.setFocusable(false);
				        angleLayout.setFocusable(false);
				        bandLayout.setFocusable(false);
				        okLayout.setFocusable(true);
						okLayout.requestFocus();
						index = 5;
						break;
					case 2:
						nameText.setFocusable(true);
				        derectionLayout.setFocusable(false);
				        angleLayout.setFocusable(false);
				        bandLayout.setFocusable(false);
				        okLayout.setFocusable(false);
				        nameText.requestFocus();
						index = 1;
						break;
					case 3:
						nameText.setFocusable(false);
				        derectionLayout.setFocusable(true);
				        angleLayout.setFocusable(false);
				        bandLayout.setFocusable(false);
				        okLayout.setFocusable(false);
				        derectionLayout.requestFocus();
						index = 2;
						break;
					case 4:
						nameText.setFocusable(false);
				        derectionLayout.setFocusable(false);
				        angleLayout.setFocusable(true);
				        bandLayout.setFocusable(false);
				        okLayout.setFocusable(false);
				        angleLayout.requestFocus();
						index = 3;
						break;
					case 5:
						nameText.setFocusable(false);
				        derectionLayout.setFocusable(false);
				        angleLayout.setFocusable(false);
				        bandLayout.setFocusable(true);
				        okLayout.setFocusable(false);
				        bandLayout.requestFocus();
						index = 4;
						break;
				}
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			{
				switch (index) 
				{
					case 2:
						if(derectionindex ==0)
						{
							derectionindex = 1;
						}
						else {
							derectionindex = 0;
						}
						updateDerection(derectionindex);
						break;
					case 3:
						updateAngle(false);
						break;
					case 4:
						if(bandindex ==0)
						{
							bandindex = 1;
						}
						else {
							bandindex = 0;
						}
						updateband(bandindex);
						break;
					default:
						break;
				}
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
			{
				switch (index) 
				{
					case 2:
						if(derectionindex ==0)
						{
							derectionindex = 1;
						}
						else {
							derectionindex = 0;
						}
						updateDerection(derectionindex);
						break;
					case 3:
						updateAngle(true);
						break;
					case 4:
						if(bandindex ==0)
						{
							bandindex = 1;
						}
						else {
							bandindex = 0;
						}
						updateband(bandindex);
						break;
					default:
						break;
				}
			}
		}
		return false;
	}    
	
	private void StartEditSatellite()
	{
		TvPluginControler.getInstance().getChannelManager().setEditSatellite(String.valueOf(satellitenumber),
				String.valueOf(nameText.getText()), derectionindex, anglevalue, bandindex);
		parentDialog.dismissUI();
		LNBSettingDialog lnbSettingDialog = new LNBSettingDialog();
		lnbSettingDialog.setDialogListener(null);
		lnbSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_LNB_SETTING, DialogCmd.DIALOG_SHOW, null);
	}
	
	private void updateDerection(int derectionindex)
	{
		derectionText.setText(mDerection[derectionindex]);
	}
	private void updateAngle(Boolean operationflag)
	{
		if(true == operationflag && anglevalue < 1800)
		{
			anglevalue++;
			angleText.setText(String.valueOf((float)anglevalue/10));
		}
		else if(false == operationflag && anglevalue > 0)
		{
			anglevalue--;
			angleText.setText(String.valueOf((float)anglevalue/10));
		}
	}
	private void updateband(int bandindex)
	{
		bandText.setText(mBand[bandindex]);
	}
}
