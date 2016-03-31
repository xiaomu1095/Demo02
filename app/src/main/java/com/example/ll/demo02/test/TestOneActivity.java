package com.example.ll.demo02.test;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.example.ll.demo02.R;

public class TestOneActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private RadioButton mTab1;
    private RadioButton mTab2;
    private RadioButton mTab3;
    private RadioButton mTab4;
    private RadioButton mTab5;
    private FrameLayout mContainer;
    public CompoundButton currentButtonView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_one);
        mTab1 = (RadioButton) findViewById(R.id.radio_button0);
        mTab2 = (RadioButton) findViewById(R.id.radio_button1);
        mTab3 = (RadioButton) findViewById(R.id.radio_button2);
        mTab4 = (RadioButton) findViewById(R.id.radio_button3);
        mTab5 = (RadioButton) findViewById(R.id.radio_button4);
        mContainer = (FrameLayout) findViewById(R.id.container);
        mTab1.setOnCheckedChangeListener(this);
        mTab2.setOnCheckedChangeListener(this);
        mTab3.setOnCheckedChangeListener(this);
        mTab4.setOnCheckedChangeListener(this);
        mTab5.setOnCheckedChangeListener(this);
        mTab1.setOnClickListener(this);
        mTab2.setOnClickListener(this);
        mTab3.setOnClickListener(this);
        mTab4.setOnClickListener(this);
        mTab5.setOnClickListener(this);
        mTab1.performClick();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            android.support.v4.app.Fragment fragment = (android.support.v4.app.Fragment) mFragmentPagerAdapter.instantiateItem(mContainer, buttonView.getId());
            mFragmentPagerAdapter.setPrimaryItem(mContainer, 0, fragment);
            mFragmentPagerAdapter.finishUpdate(mContainer);
        }
    }


    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case R.id.radio_button1:
                    return FragmentTest.instantiation(2);
                case R.id.radio_button2:
                    return FragmentTest.instantiation(3);
                case R.id.radio_button3:
                    return FragmentTest.instantiation(4);
                case R.id.radio_button4:
                    return FragmentTest.instantiation(5);
                case R.id.radio_button0:
                default:
                    return FragmentTest.instantiation(1);
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    };


    @Override
    public void onClick(View v) {

    }
}