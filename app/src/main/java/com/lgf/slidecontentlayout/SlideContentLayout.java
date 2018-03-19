package com.lgf.slidecontentlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by garment on 2018/3/18.
 */

/**
 * 仿百度地图的地址列表的上拉，下拉拖动效果
 * 需指定上拉的最大高度，还有下拉的最小高度
 * 原理：通过设置View的位置，实现View的移动(如果使用改变View的大小进行移动动画，会发生抖动的现象)
 */
public class SlideContentLayout extends RelativeLayout {

    private static final String TAG = "SlideContentLayout";

    private MarginLayoutParams marginLayoutParams;
    private boolean hasLoadFlag = false;

    private RecyclerView recyclerView;
    private VelocityTracker mVelocityTracker;

    private int contentLayoutHeight;
    private ContentMode currentMode = ContentMode.MIDDLE_MODE;

    private enum ContentMode{
        FULL_MODE,
        MIDDLE_MODE,
        NONE_MODE
    }

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
            contentLayoutHeight = b - t;
            Log.i(TAG, "onLayout getLayoutParams l:" + l + ",t:" + t + ",r:" + r + ",b:" + b);
            marginLayoutParams = (MarginLayoutParams) getLayoutParams();

            Log.i(TAG, "onLayout getMeasuredWidth:" + getMeasuredWidth() + ",getMeasuredHeight:" + getMeasuredHeight());
            Log.i(TAG, "onLayout getWidth:" + getWidth() + ",getHeight:" + getHeight());
            hasLoadFlag = true;
            recyclerView = (RecyclerView) getChildAt(0);
        }
    }

    float initY1;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "#### onInterceptTouchEvent  ACTION_DOWN ");
                initY1 = ev.getRawY();
                if (null == mVelocityTracker) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(ev);

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "#### onInterceptTouchEvent  ACTION_MOVE ");
                float delY1 = ev.getRawY() - initY1;
                Log.i(TAG, "####onInterceptTouchEvent  ACTION_MOVE getY:" + getY());
                Log.i(TAG, "####onInterceptTouchEvent  ACTION_MOVE shouldIntercept():" + shouldIntercept());
                //手指下滑
                //进行事件拦截判断
                if (delY1 > 0 && Math.abs(delY1) > 2){
                    if (shouldIntercept() && (getY() == 0) || getY() > 0){
                        initY1 = ev.getRawY();
                        Log.i(TAG, "####onInterceptTouchEvent 11111111 return true... ");
                        //进行事件拦截
                        return true;
                    }
                }

                //手指上滑
                //进行事件拦截判断
                if (delY1 < 0 && Math.abs(delY1) > 2){
                    if ( getY() > 0){
                        initY1 = ev.getRawY();
                        Log.i(TAG, "####onInterceptTouchEvent 222222222 return true... ");
                        //进行事件拦截
                        return true;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "#### onInterceptTouchEvent  ACTION_UP ");


                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * recyclerview 是否拉到顶端
     * @return
     */
    private boolean shouldIntercept(){
        View firstChild = recyclerView.getChildAt(0);
        boolean shouldIntercept;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
        if (firstVisiblePosition == 0 && firstChild.getTop() == 0){
            shouldIntercept = true;
        } else {
            shouldIntercept = false;
        }
        return shouldIntercept;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "####onTouchEvent  ACTION_MOVE shouldIntercept():" + shouldIntercept());

                Log.i(TAG, "####onTouchEvent  ACTION_MOVE getY:" + getY());
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);

                float delY = event.getRawY() - initY1;
                Log.i(TAG, "####onTouchEvent  ACTION_MOVE delY:" + delY);

                //手指下滑
                //delY的判断，消除上下抖动
                if (delY > 0 && Math.abs(delY) > 2){
                    if (getY() >= 0){
                        initY1 = event.getRawY();
                        setY(getY() + delY);
                        Log.i(TAG, "####onTouchEvent  ACTION_MOVE down requestLayout:");

                        requestLayout();
                        //进行事件消耗
                        return true;
                    }
                }
                //手指上滑
                //delY的判断，消除上下抖动
                if (delY < 0 && Math.abs(delY) > 2){
                    if ( getY() > 0){
                        initY1 = event.getRawY();
                        if ((getY() - Math.abs(delY)) < 0){
                            setY(0);
                        } else {
                            setY(getY() - Math.abs(delY));
                        }
                        Log.i(TAG, "####onTouchEvent  ACTION_MOVE up requestLayout:");
                        requestLayout();
                        //进行事件消耗
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                processFinishEvent();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;

        }
        return super.onTouchEvent(event);
    }

    private void processFinishEvent(){
        autoMoveAnimator(1000);
    }

    private void autoMoveAnimator(int duration){
        float start = 0;
        float end = 0;
        if (getY()> 0 && getY() < contentLayoutHeight / 4){
            start = 0;
            end = - getY();
            Log.i(TAG, "#### autoMoveAnimator 111111111111111 end:" + end );

        }
        if (getY() > contentLayoutHeight/4 && getY() < contentLayoutHeight * 3 / 4){
            start = 0;
            end = contentLayoutHeight / 2 - getY();
            Log.i(TAG, "#### autoMoveAnimator 2222222222222222 end:" + end);

        }
        if (getY() > contentLayoutHeight * 3 / 4 && getY() < contentLayoutHeight){
            start = 0;
            end = contentLayoutHeight - getY() + -60;
            Log.i(TAG, "#### autoMoveAnimator 33333333333333333333 end:" + end );

        }

//        if (mode == ContentMode.FULL_MODE){
//            end = 0;
//        } else if (mode == ContentMode.MIDDLE_MODE){
//            end = contentLayoutHeight / 2;
//        } else if (mode == ContentMode.NONE_MODE){
//            end = contentLayoutHeight - 20;
//        }
        final float currentPosition = getY();
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float result = (float) animation.getAnimatedValue();
                Log.i(TAG, "#### onAnimationUpdate  onAnimationUpdate:" + result);
                setY(currentPosition + result);
                requestLayout();
            }
        });
        animator.setDuration(duration);
        animator.start();
    }
}
