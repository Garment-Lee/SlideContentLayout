package com.lgf.slidecontentlayout.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by ligf on 2018/3/21.
 *
 *  RecyclerView的onTouchEvent()方法返回true，会把所有的事件都消耗掉。
 */

public class CustomRecyclerView extends RecyclerView {

    private static final String TAG = "CustomRecyclerView";

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "#### dispatchTouchEvent :"+ ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {

        switch (e.getAction()){
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
        boolean result = super.onInterceptTouchEvent(e);
        Log.i(TAG, "#### onInterceptTouchEvent return:" + result);

        return result;
    }

    private float initY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "#### onTouchEvent ACTION_DOWN");
                initY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "#### onTouchEvent ACTION_MOVE");
                float currentY = e.getRawY();
                boolean isUp = (currentY - initY) < -2;
//                if (shouldIntercept() && !isUp){
//                    initY = currentY;
//                    Log.i(TAG, "#### onTouchEvent return:" + false);
//                    return false;
//                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "#### onTouchEvent ACTION_UP");

                break;
        }
           boolean result = super.onTouchEvent(e);
        Log.i(TAG, "#### onTouchEvent return:" + result);

        return result;
    }

    private boolean shouldIntercept() {
        View firstChild = getChildAt(0);
        boolean shouldIntercept;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
        if (firstVisiblePosition == 0 && firstChild.getTop() == 0) {
            shouldIntercept = true;
        } else {
            shouldIntercept = false;
        }
        return shouldIntercept;
    }
}
