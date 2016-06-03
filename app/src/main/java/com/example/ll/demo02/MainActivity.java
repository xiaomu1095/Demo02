package com.example.ll.demo02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.ll.demo02.aosv.ViewPagerTabListViewActivity;
import com.example.ll.demo02.base.BaseActivity;
import com.example.ll.demo02.drawerlayout.DrawerLayoutDemo;
import com.example.ll.demo02.drawerlayout.ScrollingActivity;
import com.example.ll.demo02.drawerlayout.TabActivity;
import com.example.ll.demo02.fragment.FragmentActivity;
import com.example.ll.demo02.mvp.LoginActivity;
import com.example.ll.demo02.rxjava.RxJavaActivity;
import com.example.ll.demo02.style.StyleActivity;
import com.example.ll.demo02.test.TestOneActivity;
import com.example.ll.demo02.utils.time.FastDateFormat;
import com.example.ll.demo02.utils.FileLog;
import com.example.ll.demo02.utils.SDCardUtil;
import com.example.ll.demo02.zxing.ZxingActivity;

public class MainActivity extends BaseActivity {

    private static final String EXITACTION = "action.exit";
    private FastDateFormat fastDateFormat = null;
    private ExitReceiver exitReceiver = new ExitReceiver();

    private RecyclerView recycler_view_test_rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, DefaultIntro.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
//                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();



        super.onCreate(savedInstanceState);

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(EXITACTION);
        registerReceiver(exitReceiver, filter);

        //初始化变量(时间转换的使用)
        fastDateFormat = FastDateFormat.getInstance("yyyyMMddHHmmssSSS");

        //Java线程
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                FileLog.d("tag","ceshi");
//                Log.i("tag","test");
//            }
//        });

        setContentView(R.layout.activity_main);

        recycler_view_test_rv = (RecyclerView) findViewById(R.id.recycler_view_test_rv);

        MyAdapter myAdapter = new MyAdapter(this);
        myAdapter.setOnItemClickLitener(new MyAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position + 1) {
                    case 1:
                        startActivity(new Intent(MainActivity.this,FragmentActivity.class));     //FragmentOneActivity
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this,StyleActivity.class));     //样式
                        FileLog.d("tag",fastDateFormat.format(System.currentTimeMillis())+"startActivity(new Intent(MainActivity.this,StyleActivity.class));");
                        break;
                    case 3:
                        String freeBytes = SDCardUtil.getFreeBytes(SDCardUtil.getSDCardPath()) + "";

                        Toast.makeText(MainActivity.this, freeBytes, Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(MainActivity.this,TestOneActivity.class));     //切换Fragment时实现数据保持
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));     //MVP
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this,DrawerLayoutDemo.class));     //DrawerLayout
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this,TabActivity.class));     //TabLayout
                        break;
                    case 7:
                        startActivity(new Intent(MainActivity.this,ScrollingActivity.class));     //ScrollingLayout
                        break;
                    case 8:
                        startActivity(new Intent(MainActivity.this,RxJavaActivity.class));     //RxJava
                        break;
                    case 9:
                        startActivity(new Intent(MainActivity.this,ZxingActivity.class));     //Zxing
                        break;
                    case 10:
                        startActivity(new Intent(MainActivity.this,ViewPagerTabListViewActivity.class));     //ViewPagerTabListViewActivity
                        break;
                    case 11:
                        startActivity(new Intent(MainActivity.this, com.example.ll.demo02.swipeactivity.MainActivity.class));     //SwipeActivity
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
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
