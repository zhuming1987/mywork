package com.tv.ui.SettingView;

import java.util.ArrayList;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.data.MenuItem;
import com.tv.data.TvType;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvConfigDefs.TV_CONFIG_SET;
import com.tv.framework.define.TvConfigTypes.TvConfigType;
import com.tv.framework.define.TvContext;
import com.tv.ui.SettingView.TvSettingItemView.OnSettingKeyDownListener;
import com.tv.ui.SettingView.TvSettingItemView.SettingViewChangeListener;
import com.tv.ui.base.LeftViewTitleLayout;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.base.TvBaseDialog.DialogListener;
import com.tv.ui.base.TvMenuConfigDefs;
import com.tv.ui.utils.LogicTextResource;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class TvSettingView extends LinearLayout implements OnSettingKeyDownListener, DialogListener{

	private Context context;
	private TvSettingDialog parentDialog;
	private ArrayList<MenuItem> dataList;
	private MenuItem data;
	private ArrayList<TvSettingItemView> viewList;
	private int default_index = 0;
	private int current_index = 0;
	private String current_index_id = "";
	private static final float resolution = TvUIControler.getInstance().getResolutionDiv();
	
	private ScrollView scrollView;
	private LeftViewTitleLayout titleLayout;
	private LinearLayout bodyLayout;
	
	public TvSettingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		LinearLayout.LayoutParams laytoutLp = new LinearLayout.LayoutParams(
				(int)(TvMenuConfigDefs.setting_view_bg_w/resolution),(int)(945/resolution));
		setLayoutParams(laytoutLp);
		this.setBackgroundResource(R.drawable.setting_bg);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER);
		
		titleLayout = new LeftViewTitleLayout(context);
		titleLayout.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		this.addView(titleLayout);
		
		scrollView = new ScrollView(context);
		scrollView.setVerticalScrollBarEnabled(false);
		scrollView.setHorizontalScrollBarEnabled(false);
		scrollView.setPadding((int) (25 / resolution), (int) (40 / resolution),
				(int) (25 / resolution), (int) (0 / resolution));
		scrollView.setScrollbarFadingEnabled(true);
		scrollView.setAlwaysDrawnWithCacheEnabled(true);
		scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
		
		bodyLayout = new LinearLayout(context);
		bodyLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(bodyLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.addView(scrollView, new LayoutParams(LayoutParams.MATCH_PARENT, Math.round(TvMenuConfigDefs.setting_view_bg_h/resolution)));
		
		dataList = new ArrayList<MenuItem>();
		viewList = new ArrayList<TvSettingItemView>();
		
	}

	public void updateView(MenuItem data){
		Log.i("gky", "TvSettingView::updateView");
		bodyLayout.removeAllViews();
		viewList.clear();
		
		if(data == null) return;
		this.data = data;
		
		dataList = data.getChlidList();
		
		titleLayout.bindData(data.getName()+".png", 
				LogicTextResource.getInstance(TvContext.context).getText(data.getId()));
		
		TvConfigType type;
		Log.i("gky", getClass().getName() + "--updateView dataList size is "+dataList.size());
		if (dataList.size() == 0)
		{
			ImageView line = new ImageView(context);
			line.setBackgroundResource(R.drawable.setting_line);
			LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams(
					(int) (790 / resolution), 1);
			lineLp.setMargins((int) (30 / resolution), 0,
					(int) (30 / resolution), 0);
			bodyLayout.addView(line, lineLp);
			return;
		}
		final ArrayList <TvSettingItemView> MyViewList=new ArrayList<TvSettingItemView>();
		ArrayList<Thread> threadArray=new ArrayList<Thread>();
		for(int i=0;i<dataList.size();i++)
		{
			MyViewList.add(null);
		}
		
		
		
		for (int temp=0;temp<dataList.size();temp++)
		{
			MenuItem item = dataList.get(temp);

			type = item.getItemType();
			TvSettingItemView MyView;
			switch (type) {
			case TV_CONFIG_ENUM:
				{
					MyView = new TvEnumItemView(context, temp, this, this);	
				}
				break;
			case TV_CONFIG_SWITCH:
				{
					MyView = new TvEnumItemView(context, temp, this, this);
					
				}
				break;
			case TV_CONFIG_RANGE:
				{
					MyView = new TvProgressItemView(context, temp, this, this);
					
				}
				break;
			case TV_CONFIG_SINGLE:
				{
					MyView = new TvNextItemView(context, temp, this, this);
				}
				break;
			default:
				MyView=new TvNextItemView(context, temp, this, this);
				break;
			}
			final int count=temp;
			final TvSettingItemView fView=MyView;
			final MenuItem fItem=item;
			Thread t=new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.v("wxj",Thread.currentThread().getName()+ "Thread start ");
					fItem.update();
					fView.updateView(fItem);
					if(!fItem.ishide())
					{
						MyViewList.set(count, fView);
					}
					Log.v("wxj",Thread.currentThread().getName()+ "Thread end ");
				}
			});
			threadArray.add(t);
			t.start();
		}
		Log.v("wxj", "before join");
		try {
			for(Thread t: threadArray)
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.v("wxj", "end join");

		for(int i=0;i<MyViewList.size();i++)
		{
			TvSettingItemView view=MyViewList.get(i);
			if(view==null)
			{
				continue;
			}
			viewList.add(view);
			bodyLayout.addView(view);
			
			ImageView line = new ImageView(context);
			
			line.setBackgroundResource(R.drawable.setting_line);
			
			LinearLayout.LayoutParams lineLp = new LinearLayout.LayoutParams((int) (790 / resolution), 1);
			lineLp.setMargins((int) (30 / resolution), 0,(int) (30 / resolution), 0);
			
			bodyLayout.addView(line, lineLp);
		}
		
		int i_default = 0;
		for (int i = i_default; i < viewList.size(); i++)
		{
			if(viewList.get(i).isEnabled){
				TvSettingItemView focusview = (TvSettingItemView) viewList.get(i);
				if(!current_index_id.equals("")){
					if(current_index_id.equals(focusview.getDataId())){
						current_index = i;
						current_index_id = focusview.getDataId();
						focusview.resetFocus();
						break;
					}
				}else {
					current_index = i;
					current_index_id = focusview.getDataId();
					focusview.resetFocus();
					break;
				}
			}
		}
		default_index = current_index;
		
		if (default_index < 8)
		{
			scrollView.scrollTo(0, 0);
		} else
		{
			scrollView.post(new Runnable()
			{
				public void run()
				{
					// TODO Auto-generated method stub
					scrollView.scrollTo(0,
							(int) ((default_index - 7) * 108 / resolution));
				}
			});
		}
		Log.i("gky", getClass().getName() + "--updateView end");
	}

	@Override
	public void OnKeyDownListener(int keyCode) {
		// TODO Auto-generated method stub
		Log.i("gky", getClass().getName() + "-->OnKeyDownListener handle MENU/BACK key");
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU)
		{
			parentDialog.processCmd(TvConfigTypes.TV_DIALOG_SETTING,DialogCmd.DIALOG_DISMISS, null);
		}
			
	}

	public void setParentDialog(TvSettingDialog parentDialog) {
		this.parentDialog = parentDialog;
	}

	public TvSettingDialog getParentDialog() {
		return parentDialog;
	}

	@Override
	public void setFocus(int index, int keycode) {
		// TODO Auto-generated method stub
		Log.d("gky", getClass().toString() + " setFocus index is " + index
				+ " keycode is " + keycode);
		if (index < 0 || index >= viewList.size())
		{
			return;
		}
		if (keycode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			for (int i = index; i < viewList.size(); i++)
			{
				if (viewList.get(i).isEnabled)
				{
					current_index = i;
					current_index_id = viewList.get(i).getDataId();
					viewList.get(i).resetFocus();
					return;
				}
			}
		} else if (keycode == KeyEvent.KEYCODE_DPAD_UP)
		{
			for (int i = index; i >= 0; i--)
			{
				if (viewList.get(i).isEnabled)
				{
					current_index = i;
					current_index_id = viewList.get(i).getDataId();
					viewList.get(i).resetFocus();
					return;
				}
			}
		}
	}

	@Override
	public void turnPage(int keycode, int index) {
		// TODO Auto-generated method stub
		Log.d("gky", getClass().toString() + " turnPage index is " + index);
		if (keycode == KeyEvent.KEYCODE_DPAD_DOWN) {
			for (int i = index; i < viewList.size(); i++) {
				if (viewList.get(i).isEnabled) {
					scrollView.smoothScrollTo(0, (int) (scrollView.getScrollY() + 108
							/ resolution * (i - index + 1)));
					break;
				}
			}

		} else if (keycode == KeyEvent.KEYCODE_DPAD_UP) {
			for (int i = index; i >= 0; i--) {
				if (viewList.get(i).isEnabled) {
					if (i == 8) {
						scrollView.smoothScrollTo(0, 0);
					} else {
						scrollView.smoothScrollTo(0,
								(int) (scrollView.getScrollY() - 108 / resolution
										* (i - index + 1)));
					}
					break;
				}
			}
		}
	}

	@Override
	public boolean onShowDialogDone(int ID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int onDismissDialogDone(Object...obj) {
		// TODO Auto-generated method stub
		Log.i("gky", getClass().getName() + "--onDismissDialogDone \n current_index is "+current_index
				+"\n"+" data is "+data.getId());
		parentDialog.processCmd(null, DialogCmd.DIALOG_SHOW, data);
		if(current_index < viewList.size())
			viewList.get(current_index).resetFocus();
		if(obj != null){
			String data = deserialize(obj);
			((TvNextItemView)viewList.get(current_index)).updateView(data);
		}
		return 0;
	}
	
	public String deserialize(Object[] objects){
		if(objects == null)
			return null;
		String temp = "";
		for (Object object : objects) {
			temp += (String)object;
		}
		return temp;
	}

	public int getCurrent_index() {
		return current_index;
	}

	public ArrayList<TvSettingItemView> getViewList() {
		return viewList;
	}

	public void setCurrent_index(int current_index) {
		this.current_index = current_index;
	}
	
	SettingViewChangeListener settingViewChangeListener = new SettingViewChangeListener() {
		
		@Override
		public void OnSettingViewChangeListener(ArrayList<MenuItem> data) {
			// TODO Auto-generated method stub
			Message msg = new Message();
			msg.obj = data;
			msg.what = 0;
			mHandler.sendMessage(msg);
		}
	};
	
	private Handler mHandler = new Handler(){

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == 0) {
				Log.i("gky", getClass().getName() + "--SyncViewChange");
				ArrayList<MenuItem> data = (ArrayList<MenuItem>)msg.obj;
				for (MenuItem menuItem : data) {
					for (TvSettingItemView view : viewList) {
						if (menuItem.getId().equals(view.getData().getId())) {
							switch (menuItem.getItemType()) {
								case TV_CONFIG_ENUM:
								case TV_CONFIG_SWITCH:
									((TvEnumItemView) view).updateView(menuItem);
									break;
								case TV_CONFIG_RANGE:
									((TvProgressItemView) view).updateView(menuItem);
									break;
								case TV_CONFIG_SINGLE:
									((TvNextItemView) view).updateView(menuItem);
									break;
								default:
									break;
							}
						}
					}
				}
			}
		}
		
	};

	public SettingViewChangeListener getSettingViewChangeListener() {
		return settingViewChangeListener;
	}

}
