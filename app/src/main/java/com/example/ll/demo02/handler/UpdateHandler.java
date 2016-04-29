package com.example.ll.demo02.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class UpdateHandler extends Handler {

    private Context context;

    public UpdateHandler(Context context) {
        super();
        this.context = context;
    }

    public UpdateHandler(Callback callback) {
        super(callback);
    }

    public UpdateHandler(Looper looper) {
        super(looper);
    }

    public UpdateHandler(Looper looper, Callback callback) {
        super(looper, callback);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 1:
//              showUpdataDialog();
                break;
            case 2:
                Toast.makeText(context, "获取服务器更新信息失败", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(context, "下载新版本失败", Toast.LENGTH_LONG).show();
                break;
            case 4:
                Toast.makeText(context, "您目前使用的版本为最新的版本，无需更新", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void dispatchMessage(Message msg) {
        super.dispatchMessage(msg);
    }

    @Override
    public String getMessageName(Message message) {
        return super.getMessageName(message);
    }

    @Override
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return super.sendMessageAtTime(msg, uptimeMillis);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
