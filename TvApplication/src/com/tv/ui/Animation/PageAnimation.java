package com.tv.ui.Animation;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class PageAnimation
{
    public Animation leftInAnim, leftOutAnim;
    public Animation alphaInAnim, alphaOutAnim;
    public Animation upInAnim, upOutAnim;
    public Animation downInAnim, downOutAnim;
    public static PageAnimation animInstance;
    public int duration = 150;

    public PageAnimation()
    {
        initAnim();
    }

    public static PageAnimation getInstance()
    {
        if (animInstance == null)
        {
            animInstance = new PageAnimation();
        }
        return animInstance;
    }

    public void setDuration(int _duration)
    {
        duration = _duration;
    }

    public int getDuration()
    {
        return duration;
    }

    public Animation getleftInAnimation()
    {
        return leftInAnim;
    }

    public Animation getleftOutAnimation()
    {
        return leftOutAnim;
    }

    public Animation getAlphaInAnimation()
    {
        return alphaInAnim;
    }

    public Animation getAlphaOutAnimation()
    {
        return alphaOutAnim;
    }

    public Animation getUpInAnimtion()
    {
        return upInAnim;
    }

    public Animation getUpOutAnimation()
    {
        return upOutAnim;
    }

    public Animation getDownInAnimation()
    {
        return downInAnim;
    }

    public Animation getDownOutAnimation()
    {
        return downOutAnim;
    }

    private void initAnim()
    {

        leftInAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        leftInAnim.setFillAfter(true);
        leftInAnim.setDuration(duration);

        leftOutAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        leftOutAnim.setFillAfter(true);
        leftOutAnim.setDuration(duration);

        alphaInAnim = new AlphaAnimation(0.5f, 1.0f);
        alphaInAnim.setFillAfter(true);
        alphaInAnim.setDuration(duration);

        alphaOutAnim = new AlphaAnimation(1.0f, 0.0f);
        alphaOutAnim.setFillAfter(true);
        alphaOutAnim.setDuration(duration);

        upInAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0);
        upInAnim.setFillAfter(true);
        upInAnim.setDuration(duration);

        upOutAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f);
        upOutAnim.setFillAfter(true);
        upOutAnim.setDuration(duration);

        downInAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0);
        downInAnim.setFillAfter(true);
        downInAnim.setDuration(duration);
        
        downOutAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f);
        downOutAnim.setFillAfter(true);
        downOutAnim.setDuration(duration);
    }

    public enum AnimDirection
    {
        LEFT_RIGHT, UP_DOWN, ZOOM_OUT_ALPHA
    }

}
