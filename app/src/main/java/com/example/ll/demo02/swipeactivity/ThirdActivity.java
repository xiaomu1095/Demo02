package com.example.ll.demo02.swipeactivity;

import android.app.Activity;
import android.os.Bundle;

import com.example.ll.demo02.R;

public class ThirdActivity extends Activity {

    SwipeBackFramelayout mSwipeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_swipe);

        mSwipeBack = (SwipeBackFramelayout) findViewById(R.id.swipe_back);
        mSwipeBack.setCallback(new SwipeBackFramelayout.Callback() {
            @Override
            public void onShouldFinish() {
                finish();
//                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_right);
                overridePendingTransition(0, R.anim.slide_out_right);
            }
        });
    }
}
