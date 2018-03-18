package com.lgf.slidecontentlayout;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mDataContainerLayout;
    private SlideContentLayout mSlideContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews(){
//        mDataContainerLayout = (RelativeLayout) findViewById(R.id.rl_data_content_layout);
//        Button button = new Button(this);
//
//        mSlideContentLayout = new SlideContentLayout(this);
//        mSlideContentLayout.setBackgroundColor(Color.GREEN);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////        layoutParams.addRule(Gravity.BOTTOM);
//        layoutParams.height = 100;
//
//        mDataContainerLayout.addView(mSlideContentLayout, layoutParams);
    }
}
