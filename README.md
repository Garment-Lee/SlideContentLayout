# SlideContentLayout
仿照百度地图的上层地址列表的上拉、下拉的拖动效果，当手指离开屏幕时，地址列表有三种状态：全部展示，展示一半，隐藏到底部。

## 特点

	1. 继承于RelativeLayout，不用我们自己测量、布局、绘制View。
	
	2. 可以嵌套不同的View，包括RecyclerView、ListView等，并实现滑动事件冲突的处理。
	
	3. View的移动动画的实现，使用setY()，通过设置View的位置，实现移动动画。
	
	4. VelocityTracker的使用，根据VelocityTracker得到Y方向的移动速度和当前的View的位置，从而切换到不同的显示状态。

## 实现思路

	1. 需要兼容不同的View的拖动，所以使用一个外层布局SlideContentLayout，用于嵌套需要拖动的内容，也就是拖动的View。
	我们实现SlideContentLayout的拖动，就是相当于实现View的拖动。
	
	2. 嵌套View可能也是一个可拖动的View，所以需要处理滑动事件冲突。在SlideContentLayout中的onInterceptTouchEvent()
	方法中根据条件进行事件的拦截，在onTouchEvent()方法中实现当前SlideContentLayout的滑动操作。
	
	3. 由于不同的嵌套View，它的拦截条件可能不一样，所以把拦截判断抽象成一个接口，在用户使用时，根据自己使用的嵌套View，
	实现该接口，然后把接口对象传到SlideContentLayout中。
	
	4. 松开手时，SlideContentLayout根据当前的位置移动到对应的状态，这时使用属性动画进行View的移动。

    
## 事件拦截机制分析

![](https://github.com/Garment-Lee/SlideContentLayout/raw/master/img/slidecontentlayout.png)  


#### 手指下滑
	当SlideContentLayout位于①，当嵌套View已经滑动到最顶端时，SlideContentLayout拦截事件，随手指滑动进行向下移动操作。
	当SlideContentLayout位于①-③之间，直接拦截事件，随手指滑动进行向下移动操作。
	当SlideContentLayout位于③，不拦截事件。


#### 手指上滑
	当SlideContentLayout位于③-①，拦截事件，随着手指向上移动。
	当SlideContentLayout位于①，不拦截事件。
	

## 效果如下




## 用法
 ##### 在xml中配置：
 
 ```java
 <?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android";
    xmlns:app="http://schemas.android.com/apk/res-auto";
    xmlns:tools="http://schemas.android.com/tools";
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#ffffff">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:background="#ff0000"
        android:id="@+id/ll_search_head"
        android:orientation="horizontal">
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:text="I am the head title"
            android:layout_gravity="center_vertical"/>
    </LinearLayout>
    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:id="@+id/rl_data_content_layout"
        android:layout_below="@+id/ll_search_head"
        android:layout_marginTop="10dp">
        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/btn_test1"
            android:text="click me to title layout activity"
            android:layout_marginTop="20dp"
            android:stateListAnimator="@null"/>
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/tv_test1"
            android:text="i am a map"
            android:layout_below="@id/btn_test1"/>
        <com.lgf.slidecontentlayout.slidecontentlayout.SlideContentLayout
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:id="@+id/slide_layout"
            android:background="#ffffff">
            <com.lgf.slidecontentlayout.test.CustomRecyclerView
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:id="@+id/recyclerview_data_list">
            </com.lgf.slidecontentlayout.test.CustomRecyclerView>
        </com.lgf.slidecontentlayout.slidecontentlayout.SlideContentLayout>
    </RelativeLayout>
</RelativeLayout>
```

##### 在Activity中实现拦截判断接口

```java
@Override
    public boolean checkIfIntercept() {
        View firstChild = mDataRecyclerView.getChildAt(0);
        boolean shouldIntercept;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mDataRecyclerView.getLayoutManager();
        int firstVisiblePosition = linearLayoutManager.findFirstVisibleItemPosition();
        if (firstVisiblePosition == 0 && firstChild.getTop() == 0) {
            shouldIntercept = true;
        } else {
            shouldIntercept = false;
        }
        return shouldIntercept;
    }

```

##### 设置接口实现对象到SlideContentLayout对象中

```java
mSlideContentLayout.setInterceptChecker(this);

```

## 不足的地方
　　SlideContentLayout位于①状态，此时手指上滑，此时SlideContentLayout不会拦截事件，嵌套View会上滑；上滑一段时间，手指下滑，此时嵌套View会下滑，当嵌套View的顶端处于可见状态，继续下滑时，SlideContentLayout不会拦截事件，执行下滑的动作。


## 问题
　　Demo中的嵌套View为RecyclerView，通过调试发现，滑动RecyclerView时，其父布局SlideContentLayout的onInterceptTouchEvent()不会被调用，所以SlideContentLayout无法拦截滑动事件，所以出现了上面的问题。



