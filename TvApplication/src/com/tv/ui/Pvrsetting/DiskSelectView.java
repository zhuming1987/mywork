package com.tv.ui.Pvrsetting;
import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvQuickKeyControler;
import com.tv.app.TvUIControler;
import com.tv.app.TvQuickKeyControler.QuickKeyListener;
import com.tv.framework.data.DiskSelectInfoData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.plugin.TvPluginControler;
import com.tv.ui.SettingView.TvSettingItemView.OnSettingKeyDownListener;
import com.tv.ui.base.LeftViewBodyLayout;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;

import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;


public class DiskSelectView extends LinearLayout implements OnSettingKeyDownListener, DialogListener
{
	private static float div =TvUIControler.getInstance().getResolutionDiv();
	private QuickKeyListener quickKeyListener;
    private ArrayList<DiskSelectItemView> selecteItems;
    private Context mContext;
    private final int backColor = 0xFF222A2D;
    public static final int hostTitleColor = 0xFFEDC40F;
    public static final int shareTitleColor = 0xFF3D9AD8;
    public final int SHAREBUTTONID = 8;
    public String sendStr = "";
    int data_count = 0;
    private DiskSelectDialog parentDialog = null;
    private String itemPath;
    private String dlString = "";
    
    private ScrollView scrollView;
	private LeftViewTitleLayout titleLayout;
	private LeftViewBodyLayout bodyLayout;
	private ImageView diskItemDivider;
    LinearLayout listlayout;
    LinearLayout.LayoutParams item0Params;
    LinearLayout.LayoutParams itemParams;
    
    private DiskSelectInfoData data = null;
 
    public DiskSelectView(Context context)
    {
        super(context);
        mContext = context;
        initViews();
        this.setBackgroundColor(backColor);
    }

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
		titleLayout.bindData("system_setting.png", this.getResources().getString(R.string.TV_CFG_PVR_SELECT_DISK));
		this.addView(titleLayout);
		
		bodyLayout = new LeftViewBodyLayout(mContext);
		bodyLayout.setPadding(50, 30, 50, 50);
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

    public void update(DiskSelectInfoData data,String dialogFlag)
    {
    	dlString = dialogFlag;
    	selecteItems = new ArrayList<DiskSelectItemView>();

    	this.data = data;
    	data_count = data.usbDriverCount;
        listlayout.removeAllViews();
        if (data_count > 0)
        {
            for (int i = 0; i < data_count; i++)
            {            	
            	selecteItems.add(new DiskSelectItemView(mContext, this));
            	diskItemDivider = new ImageView(mContext);
            	diskItemDivider.setBackgroundResource(R.drawable.setting_line);
                listlayout.addView(diskItemDivider, new LayoutParams(LayoutParams.MATCH_PARENT,(int) (2 / div)));
                selecteItems.get(i).setId(i);
                listlayout.addView(selecteItems.get(i), itemParams);
                
                itemPath = data.usbDriverPath.get(i).toString();
                selecteItems.get(i).setOnClickListener(new ListenerItem());
            
    			DiskSelectItemView itemView = selecteItems.get(i);
    			itemView.update(data.usbDriverLabel.get(i).toString(),data.usbFreeSize.get(i).toString());
    			itemView.setOnFocusChangeListener(new OnFocusChangeListener() {
    				public void onFocusChange(View view, boolean hasFocus) {
    					((DiskSelectItemView) view).setBackGroundColorUser(hasFocus);
    				}
    			});   		
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
    	if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP)
        {
            if (selecteItems.get(0) != null && !selecteItems.get(0).isFocused())
            {
            	selecteItems.get(0).requestFocus();
            	selecteItems.get(0).setChecked(true);
                checkedSelectedItem(1);
            }
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)
        {
        	parentDialog.processCmd(TvConfigTypes.TV_DIALOG_DISK_SELECT,DialogCmd.DIALOG_DISMISS, null);
			return true;
        }
        return quickKeyListener.onQuickKeyDownListener(keyCode, event);
    }

    class ListenerItem implements OnClickListener
    {
        public void onClick(View view)
        {
            int id = view.getId();
            itemPath = data.usbDriverPath.get(id);
            String usbLabel = data.usbDriverLabel.get(id);
            if(usbLabel.contains("FAT"))
            {
            	TvPluginControler.getInstance().getPvrManager().setPVRParas(itemPath, TvConfigTypes.E_FILE_SYS_FAT32);
            	TvPluginControler.getInstance().getPvrManager().setSelectDiskPath(itemPath);
            }
            else if(usbLabel.contains("NTFS")){
				TvPluginControler.getInstance().getPvrManager().setPVRParas(itemPath, TvConfigTypes.E_FILE_SYS_NTFS);
				TvPluginControler.getInstance().getPvrManager().setSelectDiskPath(itemPath);
            }
            
			if(dlString.equals("PVRChannelListView"))
            {     	
                parentDialog.processCmd(TvConfigTypes.TV_DIALOG_DISK_SELECT,DialogCmd.DIALOG_DISMISS, itemPath);
				PVRChannelListDialog pvrChannelListDialog = new PVRChannelListDialog();
                pvrChannelListDialog.setDialogListener(null);               
            	pvrChannelListDialog.diskPath = itemPath;
            	pvrChannelListDialog.diskLabel = usbLabel;
                pvrChannelListDialog.processCmd(TvConfigTypes.TV_DIALOG_PVR_CHANNELLIST, DialogCmd.DIALOG_SHOW, null); 
            } 
			else if(dlString.equals("timeshitf") || dlString.equals("record"))
			{
				TvUIControler.getInstance().reMoveAllDialogs();
				PvrTimeshitfDlg dlg = new PvrTimeshitfDlg();
				dlg.selectPath = itemPath;
				dlg.selectLabel = usbLabel;
						
				dlg.processCmd(TvConfigTypes.TV_DIALOG_PVR_TIMESHITH_DLG, DialogCmd.DIALOG_SHOW, dlString);
			}
            else
			{
			    parentDialog.processCmd(TvConfigTypes.TV_DIALOG_DISK_SELECT,DialogCmd.DIALOG_DISMISS, usbLabel);
			}
			checkedSelectedItem(id);
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
        	DiskSelectItemView view = selecteItems.get(i);
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

    public void setParentDialog(DiskSelectDialog parentDialog) {
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
