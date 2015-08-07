package com.tv.ui.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

public class InputFilterInt implements InputFilter
{
    private int max = 9;
    private EditText text;

    public InputFilterInt(EditText tv)
    {
        this.text = tv;
    }

    public InputFilterInt(int maxLength, EditText tv)
    {
        this.max = maxLength;
        this.text = tv;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
            int dend)
    {
        Log.i("test_filter", "source = " + source + ", start = " + start + ", end = " + end
                + ", dest = " + dest + ", dstart = " + dstart + ", dend = " + dend);
        int crtLength = dest.length();
        String numStr = getIntPart(source);
        if (crtLength == 0 || dend - dstart == crtLength)
            return numStr.substring(0, max > numStr.length() ? numStr.length() : max);

        int returnLength = max - dest.length() + dend - dstart;
        if (numStr.length() < returnLength)
            returnLength = numStr.length();
        if (dest.charAt(0) == '0')
        {
            if (dstart > 0)
            {
                text.setText(numStr.substring(0, returnLength));
                text.setSelection(returnLength);
                return "";
            }
            if (numStr.startsWith("0"))
                return "";
            return numStr.substring(0, returnLength);
        }
        if (numStr.startsWith("0"))
        {
            if (dstart == 0)
                return "";
            return numStr.substring(0, returnLength);
        }
        return numStr.substring(0, returnLength);
    }

    private boolean isNumChar(char c)
    {
        if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6'
                || c == '7' || c == '8' || c == '9' || c == 'A' || c == 'a')
            return true;
        return false;
    }

    private String getIntPart(CharSequence str)
    {
        if (str.length() > 0 && str.charAt(0) == '0')
            return "0";
        String numStr = "";
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (isNumChar(c))
                numStr += c;
            else
                break;
        }
        return numStr;
    }

}
