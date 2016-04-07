package com.example.ll.demo02.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.ll.demo02.bean.VersionInfo;
import com.example.ll.demo02.handler.UpdateHandler;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateOnline {

    private Context context;
    private static int UPDATA_CLIENT = 1;
    private static int GET_UNDATAINFO_ERROR = 2;
    private static int DOWN_ERROR = 3;
    private static int ISNEW_VERSION = 4;



    public UpdateOnline(Context context) {
        this.context = context;
    }


    UpdateHandler handler = new UpdateHandler(context);

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    showUpdataDialog();
//                    break;
//                case 2:
//                    Toast.makeText(context, "锟斤拷取锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷息失锟斤拷", Toast.LENGTH_LONG).show();
//                    break;
//                case 3:
//                    Toast.makeText(context, "锟斤拷锟斤拷锟铰版本失锟斤拷", Toast.LENGTH_LONG).show();
//                    break;
//                case 4:
//                    Toast.makeText(context, "锟斤拷目前使锟矫的版本为锟斤拷锟铰的版本锟斤拷锟斤拷锟斤拷锟斤拷锟�", Toast.LENGTH_LONG).show();
//                    break;
//            }
//        }
//    };




    /**
     * 从服务器获取APK信息
     *
     * @param context
     */
    private void getAppVersion(final Context context) {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("https://github.com/hongyangAndroid")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = DOWN_ERROR;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                VersionInfo versionInfo = new Gson().fromJson(response.toString(), VersionInfo.class);
                if (versionInfo.getVersion().equals(String.valueOf(APPUtil.getVersionCode(context)))){
                    Message msg = new Message();
                    msg.what = ISNEW_VERSION;
                    handler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = GET_UNDATAINFO_ERROR;
                    handler.sendMessage(msg);
                }
            }

        });
    }



    /**
     * 安装APK
     *
     * @param file
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
