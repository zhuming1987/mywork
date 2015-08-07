package com.tv.ui.LNBSetting;

import android.content.Context;
import android.graphics.Color;
import android.text.method.DigitsKeyListener;
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

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.TvTransponderSetupPageData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.base.TvBaseDialog.DialogCmd;

public class TransponderAddView extends LinearLayout implements OnKeyListener
{
	private TransponderOperationDialog parentDialog;
	private Context mContext;
	private static int index;
	private static int polarizationindex;
	
	private float div = TvUIControler.getInstance().getResolutionDiv();
    private float dip = TvUIControler.getInstance().getDipDiv();
    
    private LinearLayout mainLayout,bodyLayout;
    private TextView titleText;
    private LinearLayout numLayout,frequencyLayout,symbolrateLayout,polarizationLayout,okLayout;
    private LinearLayout polItemLayout;
    private TextView numTitle,frequencyTitle,symbolrateTitle,polarizationTitle;
    private TextView numText,polarizationText;
    private EditText frequencyText,symbolrateText;
    private Button okButton;
    private ImageView divideLine;   
    private ImageView leftArrow,rightArrow;   
	
    private int mainWidth = (int)(1100/div);
    private int titleHeight = (int)(120/div);
    private int bodyHeight = (int)(960/div);
    
    private final String[] polarization = TvContext.context.getResources().getStringArray(R.array.str_array_transponder_polarization);
	public TransponderAddView(Context context) 
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
        titleText.setText(R.string.str_lnb_item_add);
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
        
        frequencyLayout = new LinearLayout(mContext);
        frequencyLayout.setOrientation(LinearLayout.HORIZONTAL);
        bodyLayout.addView(frequencyLayout, LayoutParams.MATCH_PARENT,titleHeight);
        frequencyTitle = new TextView(mContext);  
        frequencyTitle.setTextSize((int) (36 / dip));
        frequencyTitle.setText(R.string.str_lnb_transponder_frequency);
        frequencyLayout.addView(frequencyTitle,itemParams);
        frequencyText = new EditText(mContext);
        frequencyText.setTextSize((int) (36 / dip));
        frequencyText.setText("Edit Name");
        frequencyText.setGravity(Gravity.CENTER);
        frequencyLayout.addView(frequencyText,contentParams);
        String digits = "0123456789";
        frequencyText.setKeyListener(DigitsKeyListener.getInstance(digits));
        frequencyText.setOnKeyListener(this);
        frequencyText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					frequencyTitle.setTextColor(Color.BLUE);
				}
				else 
				{
					frequencyTitle.setTextColor(Color.WHITE);
				}
			}
		});
                
        addDivideLine();
        
        symbolrateLayout = new LinearLayout(mContext);
        symbolrateLayout.setOrientation(LinearLayout.HORIZONTAL);
        bodyLayout.addView(symbolrateLayout, LayoutParams.MATCH_PARENT,titleHeight);
        symbolrateTitle = new TextView(mContext);  
        symbolrateTitle.setTextSize((int) (36 / dip));
        symbolrateTitle.setText(R.string.str_lnb_transponder_symbol_rate);
        symbolrateLayout.addView(symbolrateTitle,itemParams);
        symbolrateText = new EditText(mContext);
        symbolrateText.setTextSize((int) (36 / dip));
        symbolrateText.setText("Edit Name");
        symbolrateText.setGravity(Gravity.CENTER);
        symbolrateText.setKeyListener(DigitsKeyListener.getInstance(digits));
        symbolrateLayout.addView(symbolrateText,contentParams);
        symbolrateText.setOnKeyListener(this);
        symbolrateText.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					symbolrateTitle.setTextColor(Color.BLUE);
				}
				else 
				{
					symbolrateTitle.setTextColor(Color.WHITE);
				}
			}
		});
        
        addDivideLine();
        
        polarizationLayout = new LinearLayout(mContext);
        polarizationLayout.setOrientation(LinearLayout.HORIZONTAL);
        bodyLayout.addView(polarizationLayout, LayoutParams.MATCH_PARENT,titleHeight);
        polarizationTitle = new TextView(mContext);  
        polarizationTitle.setTextSize((int) (36 / dip));
        polarizationTitle.setText(R.string.str_lnb_transponder_polarization);
        polarizationLayout.addView(polarizationTitle,itemParams);
        polItemLayout = new LinearLayout(mContext);
        polItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
        polItemLayout.setGravity(Gravity.CENTER_VERTICAL);
        polarizationLayout.addView(polItemLayout,contentParams);
        leftArrow = new ImageView(mContext);
        leftArrow.setBackgroundResource(R.drawable.simulation_arrow_left);
        polItemLayout.addView(leftArrow,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        polarizationText = new TextView(mContext);
        polarizationText.setTextSize((int) (36 / dip));
        polarizationText.setGravity(Gravity.CENTER);
        polItemLayout.addView(polarizationText,(int)(518/div),LayoutParams.WRAP_CONTENT);
        rightArrow = new ImageView(mContext);
        rightArrow.setBackgroundResource(R.drawable.simulation_arrow_right);
        polItemLayout.addView(rightArrow,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        polarizationLayout.setOnKeyListener(this);
        polarizationLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean isFocus) {
				// TODO Auto-generated method stub
				if(isFocus)
				{
					polarizationTitle.setTextColor(Color.BLUE);
					polarizationText.setTextColor(Color.BLACK);
					polItemLayout.setBackgroundResource(R.drawable.blue_sel_bg);
				}
				else 
				{
					polarizationTitle.setTextColor(Color.WHITE);
					polarizationText.setTextColor(Color.WHITE);
					polItemLayout.setBackgroundResource(R.drawable.setting_unselect_bg);
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
            	StartAddSatellite();
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
		TvTransponderSetupPageData satellitesetupdata = TvPluginControler.getInstance().getChannelManager().getAddTransponderPageData(number);
		polarizationindex = satellitesetupdata.polarization;
		
		numText.setText(String.valueOf(satellitesetupdata.number+1));
		frequencyText.setText(String.valueOf(satellitesetupdata.frequency));
		symbolrateText.setText(String.valueOf(satellitesetupdata.symbolrate));
		polarizationText.setText(polarization[satellitesetupdata.polarization]);
		
		frequencyText.setFocusable(true);
		symbolrateText.setFocusable(false);
		polarizationLayout.setFocusable(false);
		okLayout.setFocusable(false);
		frequencyText.requestFocus();
		index = 1;
	}
	
	public void setParentDialog(TransponderOperationDialog parentDialog) 
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
			Log.v("zhm","TransponderAddView keyCode == " + keyCode + ";");
			if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
			{
				switch(index)
				{
					case 1:
						frequencyText.setFocusable(false);
						symbolrateText.setFocusable(true);
						polarizationLayout.setFocusable(false);
						okLayout.setFocusable(false);
						symbolrateText.requestFocus();
						index = 2;
						break;
					case 2:
						frequencyText.setFocusable(false);
						symbolrateText.setFocusable(false);
						polarizationLayout.setFocusable(true);
						okLayout.setFocusable(false);
						polarizationLayout.requestFocus();
						index = 3;
						break;
					case 3:
						frequencyText.setFocusable(false);
						symbolrateText.setFocusable(false);
						polarizationLayout.setFocusable(false);
						okLayout.setFocusable(true);
						okLayout.requestFocus();
						index = 4;
						break;	
					case 4:
						frequencyText.setFocusable(true);
						symbolrateText.setFocusable(false);
						polarizationLayout.setFocusable(false);
						okLayout.setFocusable(false);
						frequencyText.requestFocus();
						index = 1;
						break;
				}
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
			{
				switch(index)
				{
					case 1:
						frequencyText.setFocusable(false);
						symbolrateText.setFocusable(false);
				        polarizationLayout.setFocusable(false);
				        okLayout.setFocusable(true);
						okLayout.requestFocus();
						index = 4;
						break;
					case 2:
						frequencyText.setFocusable(true);
						symbolrateText.setFocusable(false);
				        polarizationLayout.setFocusable(false);
				        okLayout.setFocusable(false);
				        frequencyText.requestFocus();
						index = 1;
						break;
					case 3:
						frequencyText.setFocusable(false);
						symbolrateText.setFocusable(true);
				        polarizationLayout.setFocusable(false);
				        okLayout.setFocusable(false);
				        symbolrateText.requestFocus();
						index = 2;
						break;
					case 4:
						frequencyText.setFocusable(false);
						symbolrateText.setFocusable(false);
				        polarizationLayout.setFocusable(true);
				        okLayout.setFocusable(false);
				        polarizationLayout.requestFocus();
						index = 3;
						break;
				}
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			{
				if(index == 3)
				{
					if(polarizationindex == 0)
					{
						polarizationindex = 1;
					}
					else
					{
						polarizationindex = 0;
					}
					updatePolarization(polarizationindex);
				}
			}
			else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
			{
				if(index == 3)
				{
					if(polarizationindex == 0)
					{
						polarizationindex = 1;
					}
					else
					{
						polarizationindex = 0;
					}
					updatePolarization(polarizationindex);
				}
			}
		}
		return false;
	}    
	
	private void StartAddSatellite()
	{
		TvPluginControler.getInstance().getChannelManager().setAddTransponder(Integer.valueOf(String.valueOf(frequencyText.getText())), 
				Integer.valueOf(String.valueOf(symbolrateText.getText())), polarizationindex);
		parentDialog.dismissUI();
		LNBSettingDialog lnbSettingDialog = new LNBSettingDialog();
		lnbSettingDialog.setDialogListener(null);
		lnbSettingDialog.processCmd(TvConfigTypes.TV_DIALOG_LNB_SETTING, DialogCmd.DIALOG_SHOW, null);
	}
	private void updatePolarization(int polarizationindex)
	{
		polarizationText.setText(polarization[polarizationindex]);
	}
}
