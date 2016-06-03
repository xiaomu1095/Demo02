
package com.example.ll.demo02.swipeactivity;

import android.os.Bundle;

import com.example.ll.demo02.R;

public class SecondActivity extends SwipeActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blk);
        setSwipeAnyWhere(false);
    }
}
