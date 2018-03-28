package com.lgf.slidecontentlayout.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by ligf on 2018/3/26.
 */

public class SubRelativeLayout extends RelativeLayout {

    private static final String TAG = "SubRelativeLayout";

    public SubRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "#### onInterceptTouchEvent ACTION_DOWN");

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "#### onInterceptTouchEvent ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG, "#### onInterceptTouchEvent ACTION_UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "#### onTouchEvent ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "#### onTouchEvent ACTION_MOVE");

                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG, "#### onTouchEvent ACTION_UP");

                break;
        }
        return super.onTouchEvent(event);
    }
}
