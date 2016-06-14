package com.example.ll.demo02.jni;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ll.demo02.R;

public class JNIActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);

        TextView jni_tv = (TextView) findViewById(R.id.jni_tv);

        NDKJniUtils ndkJniUtils = new NDKJniUtils();

        if (jni_tv != null) {
            jni_tv.setText(ndkJniUtils.getCLanguageString());
        }
    }
}
