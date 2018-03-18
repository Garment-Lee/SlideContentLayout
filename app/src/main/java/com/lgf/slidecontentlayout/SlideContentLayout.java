package com.lgf.slidecontentlayout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by garment on 2018/3/18.
 */

public class SlideContentLayout extends RelativeLayout {

    private static final String TAG = "SlideContentLayout";

    private MarginLayoutParams marginLayoutParams;
    private boolean hasLoadFlag = false;
    private int topMargin;
    private int parentHeight = 1200;

    public SlideContentLayout(Context context) {
        this(context, null);
    }

    public SlideContentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public SlideContentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !hasLoadFlag) {
            Log.i(TAG, "onLayout getLayoutParams l:" + l + ",t:" + t + ",r:" + r + ",b:" + b);
            marginLayoutParams = (MarginLayoutParams) getLayoutParams();
            Log.i(TAG, "onLayout topMargin:" + marginLayoutParams.topMargin + ",bottomMargin" + marginLayoutParams.bottomMargin
                    + ",width:" + marginLayoutParams.width + ",height:" + marginLayoutParams.height);

            Log.i(TAG, "onLayout getMeasuredWidth:" + getMeasuredWidth() + ",getMeasuredHeight:" + getMeasuredHeight());
            Log.i(TAG, "onLayout getWidth:" + getWidth() + ",getHeight:" + getHeight());
            hasLoadFlag = true;

        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean shouldInterceptFlag = false;
        topMargin = marginLayoutParams.topMargin;
        Log.i(TAG, "onInterceptTouchEvent topMargin:" + topMargin);
        if (topMargin >= 0) {
            shouldInterceptFlag = true;
        }
        return shouldInterceptFlag;
    }

    float initX;
    float initY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initY = event.getY();
                Log.i(TAG, "onTouchEvent  ACTION_DOWN topMargin:" + topMargin);
                if (marginLayoutParams.topMargin >= 0) {
                    return true;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "onTouchEvent  ACTION_MOVE topMargin:" + topMargin);
                Log.i(TAG, "onTouchEvent  ACTION_MOVE parentHeight:" + parentHeight);

                marginLayoutParams = (MarginLayoutParams) getLayoutParams();

                float delY = event.getY() - initY;
                if (delY > 0){
                    if (Math.abs(delY) > 2 && (marginLayoutParams.height > 20)){
                        marginLayoutParams.height = marginLayoutParams.height - (int) delY;
                        initX = event.getY();
                        setLayoutParams(marginLayoutParams);
                    }
                }
                if (delY < 0){
                    if (Math.abs(delY) > 2 && marginLayoutParams.height < parentHeight){
                        marginLayoutParams.height = marginLayoutParams.height - (int) delY;
                        initX = event.getY();
                        setLayoutParams(marginLayoutParams);
                    }
                }

                Log.i(TAG, "onTouchEvent  ACTION_MOVE height:" + marginLayoutParams.height);


                break;
        }
        return super.onTouchEvent(event);
    }
}
