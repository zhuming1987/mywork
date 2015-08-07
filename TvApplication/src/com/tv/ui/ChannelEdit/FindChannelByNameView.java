package com.tv.ui.ChannelEdit;

import java.lang.reflect.Field;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.define.TvConfigTypes.EnumChOpType;
import com.tv.framework.define.TvContext;
import com.tv.ui.utils.LogicTextResource;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SearchView.SearchAutoComplete;
import android.widget.TextView;
import android.widget.Toast;


public class FindChannelByNameView extends LinearLayout {
	private static final float div = TvUIControler.getInstance().getResolutionDiv();
	private static final float dip = TvUIControler.getInstance().getDipDiv();
//	private Context mContext;
//	private FindChannelDialog parentDialog = null;
	public SearchView searchView;
	InputMethodManager imm = null;
	public SearchAutoComplete searchInput = null;
	
	public FindChannelByNameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
//		this.mContext = context;
		this.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams totalParams = new LinearLayout.LayoutParams((int)(600/div), (int)(80/div));
		totalParams.gravity = Gravity.TOP;
		totalParams.leftMargin = (int)(10 / div);
		totalParams.topMargin = (int)(140 / div);
		this.setLayoutParams(totalParams);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		this.setBackgroundColor(Color.parseColor("#191919"));
		
		searchView = new SearchView(context);
		searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		searchView.setIconifiedByDefault(false);
		searchView.setQueryHint(LogicTextResource.getInstance(context).getText(R.string.searchHint));
		searchView.setBackgroundResource(R.drawable.searchinput);
		searchView.setFocusable(true);
		searchView.setFocusableInTouchMode(true);
		this.addView(searchView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		 try {
	            Field field = searchView.getClass().getDeclaredField("mQueryTextView");
	 
	            field.setAccessible(true);
	 
	            searchInput = (SearchAutoComplete) field.get(searchView);
	 
	            Log.i("wx", "searchInput.text = " + searchInput.getText().charAt(0));
	        } catch (Exception e) {
	 
	            e.printStackTrace();
	        }

		imm = (InputMethodManager)TvContext.context.getSystemService( Context.INPUT_METHOD_SERVICE );
		
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(TvContext.context, R.string.submitSearch, Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onQueryTextChange(String newText) {
                if (newText != null && newText.length() > 0) {
                	 Toast.makeText(TvContext.context, newText, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

		searchInput.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.i("wx", "-------searchInput.hasFocus() = " + searchInput.hasFocus());
//				if(searchInput.hasFocus())
//				{
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//				}
//				else
//				{
//					imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
//				}
			}
			
		});
		
	}
	
//	@Override
//    public boolean dispatchKeyEventPreIme(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {  	
//            	imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);
//            	searchView.requestFocus();
//            	return true;
////            }
//        }
//        return super.dispatchKeyEventPreIme(event);
//    }

	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() == KeyEvent.ACTION_DOWN){
			View curFocus = findFocus();
			Log.i("gky", "curFocus == " + curFocus);
			switch(event.getKeyCode())
			{
			case KeyEvent.KEYCODE_BACK:
				this.setVisibility(View.GONE);
				return true;
			default:
				break;
			}
		}
		return super.dispatchKeyEvent(event);
	}

}
