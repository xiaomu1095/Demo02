package com.example.ll.demo02.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.ll.demo02.db.SettingDbHelper;

/**
 * 如果我们只希望给其他应用提供数据，而不允许其他应用修改我们的数据，
 * 那么我们只需要实现onCreate()，getType()和query()这三个方法就可以了，其他的三个方法我们可以根据业务需求，实现或者是不实现
 */
public class MyContentProvider extends ContentProvider {

    // 数据库操作类，用于获取SQLiteDatabase
    private SettingDbHelper settingDbHelper;

    private static final int STUDENT = 1;
    private static final int STUDENTS = 2;

    // UriMatcher类是一个很重要的类，因为我们需要根据传入的uri，来判断执行相对应的操作
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    // 静态代码块用于初始化MATCHER需要匹配的uri
    static {
        // MATCHER.addURI(主机名(用于唯一标示一个ContentProvider,这个需要和清单文件中的authorities属性相同),
        // 路径(可以用来表示我们要操作的数据，路径的构建应根据业务而定),返回值(用于匹配uri的时候，作为匹配的返回值));
        MATCHER.addURI("com.example.mydbdemo.StudentProvider", "student", STUDENTS);
        MATCHER.addURI("com.example.mydbdemo.StudentProvider", "student/#", STUDENT);
    }





    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }



    //插入
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        SQLiteDatabase db = settingDbHelper.getWritableDatabase();
        switch (MATCHER.match(uri)) {
            case STUDENTS:
                long id = db.insert("student", "name", values);
                return ContentUris.withAppendedId(uri, id);
            default:
                throw new IllegalArgumentException("Uri不匹配");
//                throw new UnsupportedOperationException("Not yet implemented");
        }

    }


    // 进行数据的初始化操作
    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        settingDbHelper = new SettingDbHelper(getContext());
        return false;
    }


    // 查询
    // 如果uri为       content://com.example.mydbdemo.StudentProvider/student
    // 则代表查询所有的student表内的数据
    // 如果uri为       content://com.example.mydbdemo.StudentProvider/student/6
    // 则代表查询student表内id=6的数据
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db = settingDbHelper.getReadableDatabase();

        switch (MATCHER.match(uri)){
            case STUDENT:
                    return db.query(true,"table",projection,selection,selectionArgs,"groupby","hanving",sortOrder,"limit");
            case STUDENTS:
                return db.query(true,"table",projection,selection,selectionArgs,"groupby","hanving",sortOrder,"limit");
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
