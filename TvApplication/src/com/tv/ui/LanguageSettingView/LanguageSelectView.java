package com.tv.ui.LanguageSettingView;
import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvUIControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.framework.data.TvEnumSetData;
import com.tv.framework.data.TvLoadingData;
import com.tv.framework.data.TvSetData;
import com.tv.framework.data.TvToastFocusData;
import com.tv.framework.define.TvConfigDefs;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.Loading.TvLoadingDialog;
import com.tv.ui.SettingView.TvSettingItemView.OnSettingKeyDownListener;
import com.tv.ui.ToastFocus.TvToastFocusDialog;
import com.tv.ui.ToastFocus.TvToastFocusView.OnBtClickListener;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.utils.LogicTextResource;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class LanguageSelectView extends LinearLayout implements OnSettingKeyDownListener, DialogListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private QuickKeyListener quickKeyListener;
    private LanguageSelectItemView[] selecteItems = new LanguageSelectItemView[13];
    private Context mContext;
    private final int backColor = 0xFF222A2D;
    public static final int hostTitleColor = 0xFFEDC40F;
    public static final int shareTitleColor = 0xFF3D9AD8;
    public final int SHAREBUTTONID = 8;
    public String sendStr = "";
    int data_count = 0;
    private LanguageSelectDialog parentDialog = null;
    
    private ScrollView scrollView;
	private LeftViewTitleLayout titleLayout;
	private LeftViewBodyLayout bodyLayout;
	private ImageView languageItemDivider;
    LinearLayout listlayout;
    LinearLayout.LayoutParams item0Params;
    LinearLayout.LayoutParams itemParams;
    
    private ArrayList<String> dataList = new ArrayList<String>();
    private TvSetData data;
    private int languageIndex;
 
    public LanguageSelectView(Context context)
    {
        super(context);
        mContext = context;
        initViews();
        this.setBackgroundColor(backColor);
    }
    
    private Handler mHandler = new Handler()
    {
    	@SuppressWarnings("static-access")
		@Override
    	public void handleMessage(Message msg) 
    	{
        	parentDialog.removeAllDialogs();
        }
    };

    private void initViews()
    {
    	this.quickKeyListener = TvQuickKeyControler.getInstance().getQuickKeyListener();
    	LinearLayout.LayoutParams laytoutLp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		setLayoutParams(laytoutLp);
		this.setBackgroundResource(R.drawable.setting_bg);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		
		titleLayout = new LeftViewTitleLayout(mContext);
		titleLayout.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		titleLayout.bindData("system_setting.png", this.getResources().getString(R.string.TV_CFG_LANGUAGE));
		this.addView(titleLayout);
		
		bodyLayout = new LeftViewBodyLayout(mContext);
		bodyLayout.setPadding((int)(50 / div), (int)(30 / div), (int)(50 / div), (int)(50 / div));
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		this.addView(bodyLayout);
		
		scrollView = new ScrollView(mContext);
		scrollView.setVerticalScrollBarEnabled(true);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setScrollbarFadingEnabled(true);
		scrollView.setAlwaysDrawnWithCacheEnabled(true);
		scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		listlayout = new LinearLayout(mContext);
        listlayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(listlayout, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));		
        bodyLayout.addView(scrollView);

        itemParams = new LinearLayout.LayoutParams((int) (480 / div), (int) (67 / div));
        itemParams.topMargin = (int) (20 / div);
        itemParams.leftMargin = (int) (20 / div);
        itemParams.rightMargin = (int) (20 / div);
        itemParams.bottomMargin = (int) (20 / div);
        itemParams.gravity = Gravity.CENTER_VERTICAL;
    }

    public void updateView()
    {
    	data = TvPluginControler.getInstance().getConfigData(TvConfigDefs.TV_CFG_OSD_LANGUAGE);
    	for (String str : ((TvEnumSetData)data).getEnumList()) {
			dataList.add(LogicTextResource.getInstance(TvContext.context).getText(str));
		}
    	data_count = dataList.size();
        listlayout.removeAllViews();
        if (data_count > 0)
        {
            for (int i = 0; i < data_count; i++)
            {
                selecteItems[i] = new LanguageSelectItemView(mContext, this);
                languageItemDivider = new ImageView(mContext);
                languageItemDivider.setBackgroundResource(R.drawable.setting_line);
                listlayout.addView(languageItemDivider, new LayoutParams(LayoutParams.MATCH_PARENT,(int) (2 / div)));
                selecteItems[i].setId(i);
                listlayout.addView(selecteItems[i], itemParams);
                selecteItems[i].setOnClickListener(new LisenerItem());
            }
            setItemSelectFocusChange(dataList);
        }
        
        languageIndex = ((TvEnumSetData)data).getCurrentIndex();
        checkedSelectedItem(languageIndex);
    }

    private void setItemSelectFocusChange(ArrayList<String> dataList)
    {
    	for (int i = 0; i < data_count; i++) {
			LanguageSelectItemView itemView = selecteItems[i];
			itemView.update(dataList.get(i).toString());
			itemView.setOnFocusChangeListener(new OnFocusChangeListener() {
				public void onFocusChange(View view, boolean hasFocus) {
					((LanguageSelectItemView) view).setBackGroundColorUser(hasFocus);
				}
			});
		}
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)
        {
            if (selecteItems[0] != null && !selecteItems[0].isFocused())
            {
                selecteItems[0].requestFocus();
                selecteItems[0].setChecked(true);
                checkedSelectedItem(1);
            }
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK /*|| keyCode == KeyEvent.KEYCODE_MENU*/)
        {
        	parentDialog.processCmd(TvConfigTypes.TV_DIALOG_LANGUAGE_SETTING,DialogCmd.DIALOG_DISMISS, null);
			return true;
        }
        if(keyCode == KeyEvent.KEYCODE_MENU)
        	return true;
        
        return true;/*quickKeyListener.onQuickKeyDownListener(keyCode, event);*/
    }

    class LisenerItem implements OnClickListener
    {
        public void onClick(View view)
        {
            int id = view.getId();
            ((TvEnumSetData)data).setCurrentIndex(id);
            checkedSelectedItem(id);
            TvToastFocusDialog tipDialog = new TvToastFocusDialog();
            tipDialog.setDialogListener(null);
            tipDialog.setOnBtClickListener(new OnBtClickListener() {
				
				@Override
				public void onClickListener(boolean flag) {
					// TODO Auto-generated method stub
					if(flag){
						final TvLoadingDialog tvLoadingDialog = new TvLoadingDialog();
			    		tvLoadingDialog.processCmd(TvConfigTypes.TV_DIALOG_LOADING,DialogCmd.DIALOG_SHOW,new TvLoadingData(TvContext.context.getResources().getString(R.string.str_please_wait),
			    				TvContext.context.getResources().getString(R.string.str_tip_switch_language)));
			    		new Thread(new Runnable() {
			    			@Override
			    			public void run() {
			    				// TODO Auto-generated method stub    					
			    				try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
			    				TvPluginControler.getInstance().setConfigData(TvConfigDefs.TV_CFG_OSD_LANGUAGE,data);    				
			    				mHandler.sendEmptyMessage(0);
			    			}
			    		}).start();	
					}
				}
			});
            String tip = mContext.getString(R.string.SKY_CFG_TV_SWITCH_LANG_WILL_REBOOT_TV);
            TvToastFocusData data = new TvToastFocusData("", "", tip, 2);
            tipDialog.processCmd(null, DialogCmd.DIALOG_SHOW, data);
            
        }
    }

    private void checkedSelectedItem(int id)
    {
        if (data_count == 0)
        {
            return;
        }
        for (int i = 0; i < data_count; i++)
        {
        	LanguageSelectItemView view = selecteItems[i];
            boolean isChecked = view.getchecked();
            if (view.getId() == id)
            {
                if (!isChecked)
                {
                    view.setChecked(true);
                } 
            } else
            {
                view.setChecked(false);
            }
        }
    }

    public void setParentDialog(LanguageSelectDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	@Override
	public boolean onShowDialogDone(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object...obj) {
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
}
