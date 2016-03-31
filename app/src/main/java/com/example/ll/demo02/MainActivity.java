package com.example.ll.demo02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.ll.demo02.fragment.FragmentActivity;
import com.example.ll.demo02.style.StyleActivity;

public class MainActivity extends BaseActivity {

    private static final String EXITACTION = "action.exit";
    private ExitReceiver exitReceiver = new ExitReceiver();

    private RecyclerView recycler_view_test_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXITACTION);
        registerReceiver(exitReceiver, filter);


        setContentView(R.layout.activity_main);

        recycler_view_test_rv = (RecyclerView) findViewById(R.id.recycler_view_test_rv);

        MyAdapter myAdapter = new MyAdapter(this);
        myAdapter.setOnItemClickLitener(new MyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
                switch (position + 1) {
                    case 1:
                        startActivity(new Intent(MainActivity.this,FragmentActivity.class));     //FragmentOneActivity
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this,StyleActivity.class));     //样式
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });
        recycler_view_test_rv.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_test_rv.setAdapter(myAdapter);


    }


    private boolean mIsExit;
    @Override
    /**
     * 双击返回键退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 2000);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消广播
        unregisterReceiver(exitReceiver);
    }



    //将HomeActivity设置为singletask启动模式，利用BroadcastReceiver进行APP退出
    public class ExitReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.finish();
        }

    }

}
