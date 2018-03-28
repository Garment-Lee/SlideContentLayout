package com.lgf.slidecontentlayout.slidecontentlayout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.RelativeLayout;

/**
 * Created by garment on 2018/3/18.
 */

/**
 * 仿百度地图的地址列表的上拉，下拉拖动效果
 * 需指定上拉的最大高度，还有下拉的最小高度
 * 原理：使用一个布局View包含RecyclerView，通过设置View的位置，实现View的移动(如果使用改变View的大小进行移动动画，会发生抖动的现象)，先通过onInterceptTouchEvent()进行
 * 事件的拦截（注意RecyclerView/ListView的滑动事件冲突的处理），然后通过onTouchEvent()进行事件的处理，这里是实现View的移动操作的地方。
 * 实现View在Y方向随手指移动是通过setY()接口，设置View在Y方向的位置，然后requestLayout()刷新View。
 */
public class SlideContentLayout extends RelativeLayout {

    private static final String TAG = "SlideContentLayout";

    /**
     * 是否第一次加载View的标志
     */
    private boolean hasLoadFlag = false;

    /**
     * 手指滑动的速度跟踪器
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 当前布局的高度
     */
    private int contentLayoutHeight;

    private ContentMode currentMode = ContentMode.MIDDLE_MODE;

    /**
     * 记录当前的手指位置
     */
    float initY1;

    private IInterceptChecker interceptChecker;

    private enum ContentMode {
        FULL_MODE,//全屏显示
        MIDDLE_MODE,//半屏显示
        NONE_MODE //不显示
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

            Log.i(TAG, "onLayout getMeasuredWidth:" + getMeasuredWidth() + ",getMeasuredHeight:" + getMeasuredHeight());
            Log.i(TAG, "onLayout getWidth:" + getWidth() + ",getHeight:" + getHeight());
            hasLoadFlag = true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "#### dispatchTouchEvent : " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "#### onInterceptTouchEvent.... ");
        switch (ev.getAction()) {
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
                Log.i(TAG, "#### onInterceptTouchEvent  ACTION_MOVE getY:" + getY());
                Log.i(TAG, "#### onInterceptTouchEvent  ACTION_MOVE shouldIntercept():" + shouldIntercept());
                //手指下滑判断
                if (delY1 > 0 && Math.abs(delY1) > 2) {
                    //进行事件拦截条件判断
                    if (shouldIntercept() && (getY() == 0) || getY() > 0) {
                        initY1 = ev.getRawY();
                        Log.i(TAG, "#### onInterceptTouchEvent 11111111 return true... ");
                        //进行事件拦截
                        return true;
                    }
                }

                //手指上滑判断
                if (delY1 < 0 && Math.abs(delY1) > 2) {
                    //进行事件拦截条件判断
                    if (getY() > 0) {
                        initY1 = ev.getRawY();
                        Log.i(TAG, "#### onInterceptTouchEvent 222222222 return true... ");
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
     * 是否需要拦截事件，内部调用
     *
     * @return
     */
    private boolean shouldIntercept() {
        //默认拦截
        if (interceptChecker == null){
            return true;
        }
        return interceptChecker.checkIfIntercept();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "#### onTouchEvent  ACTION_DOWN ");

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "#### onTouchEvent  ACTION_MOVE shouldIntercept():" + shouldIntercept());

                Log.i(TAG, "#### onTouchEvent  ACTION_MOVE getY:" + getY());
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);

                float delY = event.getRawY() - initY1;
                Log.i(TAG, "#### onTouchEvent  ACTION_MOVE delY:" + delY);

                //手指下滑
                //delY的判断，消除上下抖动
                if (delY > 0 && Math.abs(delY) > 2) {
                    if (getY() >= 0) {
                        initY1 = event.getRawY();
                        setY(getY() + delY);
                        Log.i(TAG, "#### onTouchEvent  ACTION_MOVE down requestLayout:");

                        requestLayout();
                        //进行事件消耗
                        return true;
                    }
                }
                //手指上滑
                //delY的判断，消除上下抖动
                if (delY < 0 && Math.abs(delY) > 2) {
                    if (getY() > 0) {
                        initY1 = event.getRawY();
                        if ((getY() - Math.abs(delY)) < 0) {
                            setY(0);
                        } else {
                            setY(getY() - Math.abs(delY));
                        }
                        Log.i(TAG, "#### onTouchEvent  ACTION_MOVE up requestLayout:");
                        requestLayout();
                        //进行事件消耗
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "#### onTouchEvent  ACTION_UP ");

                processFinishEvent();
                recycleVelocityTracker();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 手指离开屏幕时，根据当前的位置，确定View要进入到哪种模式（移动到对应的模式的位置），然后进行移动动画。
     */
    private void processFinishEvent() {
        float yVelocity = mVelocityTracker.getYVelocity();
        Log.i(TAG, "#### processFinishEvent  yVelocity:" + yVelocity);
        //默认使用500ms作为动画的时间
        autoMoveAnimator(500, yVelocity);
    }

    /**
     * 移动动画
     *
     * @param duration
     * @param yVelocity
     */
    private void autoMoveAnimator(int duration, float yVelocity) {
        float start = 0;
        float end = 0;
        //根据yVelocity和松手时y方向的位置，切换到不同的模式
        //可根据自己的需求，调节yVelocity的阀值
        //可根据自己的需求，调节不同模式的判断阀值，目前的判断阀值为：
        // 0 - 1/4*contentLayoutHeight为 NONE_MODE；
        // 1/4 *contentLayoutHeight - 3/4 * contentLayoutHeight为MIDDLE_MODE；
        // 3/4 * contentLayoutHeight - contentLayoutHeight为 FULL_MODE。
        if (getY() > 0 && getY() < contentLayoutHeight / 4) {
            if (yVelocity < -1000 && yVelocity > -2000) {
                start = 0;
                end = contentLayoutHeight / 2 - getY();
                Log.i(TAG, "#### processFinishEvent 111111111111");

            } else if (yVelocity < -2000) {
                start = 0;
                end = contentLayoutHeight - 100 - getY();
                Log.i(TAG, "#### processFinishEvent 2222222222222222");

            } else {
                start = 0;
                end = -getY();
                Log.i(TAG, "#### processFinishEvent 3333333333333333");

            }
            Log.i(TAG, "#### autoMoveAnimator 111111111111111 end:" + end);
        }
        if (getY() > contentLayoutHeight / 4 && getY() < contentLayoutHeight * 3 / 4) {
            if (yVelocity > 200) {
                start = 0;
                end = -getY();
                Log.i(TAG, "#### processFinishEvent 44444444444444444444");

            } else if (yVelocity < -200) {
                start = 0;
                end = contentLayoutHeight - getY() - 100;
                Log.i(TAG, "#### processFinishEvent 55555555555555555555");

            } else {
                start = 0;
                end = contentLayoutHeight / 2 - getY();
                Log.i(TAG, "#### processFinishEvent 666666666666666666");

            }

            Log.i(TAG, "#### autoMoveAnimator 2222222222222222 end:" + end);

        }
        if (getY() > contentLayoutHeight * 3 / 4 && getY() < contentLayoutHeight) {
            if (yVelocity > 200 && yVelocity < 1000) {
                start = 0;
                end = contentLayoutHeight / 2 - getY();
                Log.i(TAG, "#### processFinishEvent 77777777777777777");

            } else if (yVelocity > 1000) {
                start = 0;
                end = -getY();
                Log.i(TAG, "#### processFinishEvent 8888888888888888888888");

            } else {
                start = 0;
                end = contentLayoutHeight - getY() - 100;
                Log.i(TAG, "#### processFinishEvent 9999999999999999999");

            }

            Log.i(TAG, "#### autoMoveAnimator 33333333333333333333 end:" + end);

        }

        final float currentPosition = getY();
        //属性动画
        ValueAnimator animator = ValueAnimator.ofFloat(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float result = (float) animation.getAnimatedValue();
//                Log.i(TAG, "#### onAnimationUpdate  onAnimationUpdate:" + result);
                setY(currentPosition + result);
                requestLayout();
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    /**
     * 回收速度追踪器
     */
    private void recycleVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 设置拦截检测器
     * @param interceptChecker
     */
    public void setInterceptChecker(IInterceptChecker interceptChecker){
        this.interceptChecker = interceptChecker;
    }
}
