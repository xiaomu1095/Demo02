package com.example.ll.demo02.zxing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ll.demo02.R;
import com.google.zxing.activity.CaptureActivity;

public class ZxingActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_scan;
    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing);

        tv_result = (TextView) findViewById(R.id.tv_result);
        btn_scan = (Button) findViewById(R.id.btn_scan);

        if (btn_scan != null){
            btn_scan.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(ZxingActivity.this, CaptureActivity.class), 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");

            tv_result.setText(result);
        }
    }
}
