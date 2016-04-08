package com.example.ll.demo02.db;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.ll.demo02.R;

import org.jetbrains.annotations.NotNull;

public class LoaderActivity extends AppCompatActivity {

    private LoaderManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);


        // 获取Loader管理器。
        manager = getLoaderManager();
        // 初始化并启动一个Loader，设定标识为1000，并制定一个回调函数。
        manager.initLoader(1000, null, callbacks);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            @NotNull
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }


    public void floatingActionButtonClicked(View view){
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }



    //使用ContentProvider添加数据的测试
    public void testadd() throws Throwable {
        //获取ContentResolver对象，完成对ContentProvider的调用
        ContentResolver contentResolver = this.getContentResolver();
        //构建我们的uir，这个uri
        Uri insertUri = Uri.parse("content://com.example.mydbdemo.StudentProvider/student");
        ContentValues values = new ContentValues();
        values.put("name", "zhaokaikai");
        values.put("age", 91);
        values.put("school", "bbbb");
        //返回值为我们刚插入进入的数据的uri地址
        Uri uri = contentResolver.insert(insertUri, values);
        Log.i("TAG", uri.toString());
    }



    // Loader的回调接口，在这里异步加载数据库的内容，显示在ListView上，同时能够自动更新
    private LoaderManager.LoaderCallbacks<Cursor> callbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // 在Loader创建的时候被调用，这里使用一个ContentProvider获取数据，所以使用CursorLoader返回数据
            Uri uri = Uri.parse("content://com.example.loadermanagertest.PersonContentProvider/person");
            CursorLoader loader = new CursorLoader(LoaderActivity.this, uri, null, null, null, null);
            Log.i("TAG", "--->>onCreateLoader被执行。");

            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //把数据提取出来，放到适配器中完成对UI的更新操作（刷新SimpleCursorAdapter的数据）

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };


}
