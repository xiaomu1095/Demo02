package com.example.ll.demo02.style;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.ll.demo02.R;

public class StyleActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_shape41 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);


        btn_shape41 = (Button) findViewById(R.id.btn_shape41);
        if (btn_shape41 != null){
            btn_shape41.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_shape41:
                btn_shape41.startAnimation(AnimationUtils.loadAnimation(StyleActivity.this, R.anim.fade_out));
                break;

            default:
                break;
        }

    }
}
