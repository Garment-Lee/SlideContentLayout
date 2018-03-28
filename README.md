# SlideContentLayout
仿照百度地图的上层地址列表的上拉、下拉的拖动效果，当手指离开屏幕时，地址列表有三种状态：全部展示，展示一半，隐藏到底部。

## 特点

	1. 继承于RelativeLayout，不用我们自己测量、布局、绘制View。
	2. 可以嵌套不同的View，包括RecyclerView、ListView等，并实现滑动事件冲突的处理。
	3. View的移动动画的实现，使用setY()，通过设置View的位置，实现移动动画。
	4. VelocityTracker的使用，根据VelocityTracker得到Y方向的移动速度和当前的View的位置，从而切换到不同的显示状态。

