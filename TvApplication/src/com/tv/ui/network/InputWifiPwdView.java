package com.tv.ui.network;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.tv.app.R;
import com.tv.app.TvUIControler;
import com.tv.framework.data.InputWifiPwdData;
import com.tv.framework.define.TvConfigTypes;
import com.tv.framework.define.TvContext;
import com.tv.ui.base.TvBaseDialog.DialogCmd;
import com.tv.ui.network.defs.AccessPoint;
import com.tv.ui.network.defs.NetBgAndTitleLayout;
import com.tv.ui.network.defs.NetConstant;

public class InputWifiPwdView extends LinearLayout
{
    private Context context;
    public float div = TvUIControler.getInstance().getResolutionDiv();
    InputWifiPwdDialog parentDialog;
    private InputWifiPwdData inputWifiPwdData;
    private TextView title;
    private TextView nameTextView;
    private TextView inputName;
    private TextView pwdTextView;
    public EditText inputPwd;
    private TextView errorTextView;

    private Button btn1;
    private Button btn2;
    private final int ID_BTN1 = 1;
    private final int ID_BTN2 = 2;
    private final int ID_PWD = 3;
    private final int ID_ERRORTXT = 4;
    public static String wifiConnectBroadCastAction = "com.skyworth.wifi.withpassword.connect";
    private float dipDiv = TvUIControler.getInstance().getDipDiv();
    
    public TextView getErrorTextView() 
    {
		return errorTextView;
	}

	public void setErrorTextView(TextView errorTextView) 
	{
		this.errorTextView = errorTextView;
	}

	public InputWifiPwdView(Context context)
    {
        super(context);
        this.context = context;
        createView();
    }

    private void createView()
    {
        LinearLayout passwd = new LinearLayout(context);   
        passwd.setBackgroundResource(R.drawable.dialog_bg);
        passwd.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        passwd.setOrientation(LinearLayout.VERTICAL);
        // title
        title = new TextView(context);
        LinearLayout.LayoutParams txtLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT);
        title.setTextColor(Color.WHITE);
        title.setPadding((int)(50 / div), (int)(30 / div), 0, 0);
        title.setTextSize(NetConstant.FONT_TXT_SIZE_CONTENT);
        title.setText(R.string.INPUT_WIFI_PWD_HINT);
        passwd.addView(title, txtLp);

        LinearLayout pwdLayout = new LinearLayout(context);
        pwdLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams pwdLayoutLP = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        pwdLayout.setId(ID_PWD);
        passwd.addView(pwdLayout, pwdLayoutLP);

        inputPwd = new EditText(context);
        LinearLayout.LayoutParams inputPwdLp = new LinearLayout.LayoutParams((int) (400 / div),
                (int) (60 / div));
        inputPwdLp.setMargins((int)(150 / div), (int)(30 / div), (int)(150 / div), 0);
        inputPwd.setHint(context.getText(R.string.INPUT_WIFI_PWD_EDIT));
        inputPwd.setHintTextColor(Color.GRAY);
        inputPwd.setTextSize(NetConstant.FONT_TXT_SIZE_CONTENT);
        inputPwd.setCursorVisible(true);
        inputPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // 这里的密码不能做任何限制，谁也不知道某个wifi的密码使用了什么特殊符号
        inputPwd.addTextChangedListener(inputPwdWatcher);
        inputPwd.setNextFocusDownId(ID_BTN1);

        inputPwd.setBackgroundResource(R.drawable.wifi_edit_text);
        inputPwd.setOnKeyListener(onKeyListener);
        inputPwd.setOnEditorActionListener(nextSoftBtnListener);
        pwdLayout.addView(inputPwd, inputPwdLp);

        // error tips
        errorTextView = new TextView(context);
        LinearLayout.LayoutParams errorLp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int)(60 / div));
        errorLp.gravity = Gravity.CENTER_HORIZONTAL;
        errorTextView.setTextColor(Color.RED);
        errorTextView.setTextSize(NetConstant.FONT_TXT_SIZE_SMALL / dipDiv);
        errorTextView.setVisibility(View.INVISIBLE);
        errorTextView.setGravity(Gravity.CENTER);
        errorTextView.setId(ID_ERRORTXT);
        errorTextView.setText("Error information..........");
        pwdLayout.addView(errorTextView, errorLp);
        // btn
        LinearLayout btnLayout = new LinearLayout(context);
        LinearLayout.LayoutParams btnLayoutLP = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        btnLayout.setOrientation(LinearLayout.HORIZONTAL);
        btnLayout.setPadding((int)(80 / div), (int)(0 / div), 0, 0);

        btn1 = newButton(ID_BTN1);
        btn2 = newButton(ID_BTN2);

        LinearLayout.LayoutParams btn1Lp = new LinearLayout.LayoutParams((int)(200 / div),
                (int)(70 / div));
        LinearLayout.LayoutParams btn2Lp = new LinearLayout.LayoutParams((int)(200 / div),
                (int)(70 / div));
        btn2Lp.setMargins((int)(100 / div), 0, 0, 0);
        btn1.setText(R.string.wifiButtonConnect);
        btn2.setText(R.string.wifiButtonExit);
        btnLayout.addView(btn1, btn1Lp);
        btnLayout.addView(btn2, btn2Lp);
        pwdLayout.addView(btnLayout, btnLayoutLP);
        
        
        this.addView(passwd);
        
        inputPwd.requestFocus();

    }

    public void setParentDialog(InputWifiPwdDialog parentDialog) 
    {
		this.parentDialog = parentDialog;
	}
    
    private Button newButton(int id)
    {
        Button btn = new Button(context);
        btn.setFocusableInTouchMode(true);
        btn.setId(id);
        btn.setTextSize(NetConstant.FONT_TXT_SIZE_BTN);
        btn.setOnClickListener(btnOnClickListener);
        btn.setOnKeyListener(onKeyListener);
        btn.setOnFocusChangeListener(btnOnFocusChangeLis);
        btn.setBackgroundResource(R.drawable.buttonstate);
        btn.setFocusable(true);
        return btn;
    }
   
    public void update(InputWifiPwdData data)
    {
    	if (data != null)
        {
    		inputWifiPwdData = data;
            errorTextView.setVisibility(View.INVISIBLE);
            inputPwd.requestFocus();

            // update 相关数据
            title.setText(data.getTitle());
            nameTextView.setText(data.getNameString());
            inputName.setText(data.getInputName());
            pwdTextView.setText(data.getPwdString());
            inputPwd.setText(data.getInputPwd());
            btn1.setText(data.getBtn1());
            btn2.setText(data.getBtn2());

            CharSequence text = inputPwd.getText();
            if (text instanceof Spannable)
            {
                Spannable spanText = (Spannable) text;
                Selection.setSelection(spanText, text.length());
            }
        }
    }
	
    OnClickListener btnOnClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
        	if (v.getId() == ID_BTN1)
            {
        		if (isPwdError())
                {
                    return;
                }
            	Intent wifiConnect = new Intent(wifiConnectBroadCastAction);
            	CharSequence text = inputPwd.getText();
            	String password = "";
            	if(text.length() == 0)
            		password = "";
            	else
            	{
            		password = text.toString();
            	}
            	wifiConnect.putExtra("msg", password);
				(context).sendBroadcast(wifiConnect);
            }
            if (v.getId() == ID_BTN2)
            {
            	parentDialog.processCmd(TvConfigTypes.INPUT_NETWORK_PASSWORD, DialogCmd.DIALOG_DISMISS, null);
            }
        }
    };
	
    OnKeyListener onKeyListener = new OnKeyListener()
    {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event)
        {
            if (event.getAction() == KeyEvent.ACTION_DOWN)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    sendBackCmdToService();
                    return true;
                }
//                if(keyCode == KeyEvent.KEYCODE_)
//                {
//                  InputMethodManager inputMethodManager = (InputMethodManager) TvContext.context.getSystemService(Context.INPUT_METHOD_SERVICE);  
//                     if(inputMethodManager.isActive())
//                         inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0); 
//                }
            }
            return false;
        }
    };
  
    OnEditorActionListener nextSoftBtnListener = new OnEditorActionListener()
    {
    	 public boolean onEditorAction(TextView v, int actionId, KeyEvent event) 
    	 {  
             if(actionId == EditorInfo.IME_ACTION_NEXT){  
                 InputMethodManager imm = (InputMethodManager) v  
                         .getContext().getSystemService(  
                                 Context.INPUT_METHOD_SERVICE);  
                 if (imm.isActive()) {  
                     imm.hideSoftInputFromWindow(  
                             v.getApplicationWindowToken(), 0);  
                        }  
               }
             return false;
        }
   };
    OnFocusChangeListener btnOnFocusChangeLis = new OnFocusChangeListener()
    {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            Button btn = (Button) v;
            if (hasFocus)
            {
                btn.setTextColor(Color.BLACK);
            } else
            {
                btn.setTextColor(Color.WHITE);
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
        	Log.i("wangxian", "input wifi pwd view onkeydown back.....");
            this.parentDialog.processCmd(TvConfigTypes.INPUT_NETWORK_PASSWORD, DialogCmd.DIALOG_DISMISS, null);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    TextWatcher inputPwdWatcher = new TextWatcher()
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (s.length() >= 8)
            {
                errorTextView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void afterTextChanged(Editable edt)
        {
            
        }
    };

    private boolean isPwdError()
    {
        if (inputPwd.getText().length() < 8)
        {
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(R.string.wifiInvialidPwd);
            return true;
        }
        return false;
    }

    public void sendBackCmdToService()
    {
    	
    }

}
