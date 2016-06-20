package com.example.ll.demo02.camera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ll.demo02.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_camera;
    public final int PHOTO_REQUESTCODE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        iv_camera = (ImageView) findViewById(R.id.iv_camera);
        if (iv_camera != null) {
            iv_camera.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
                if (!hasCamera()){
                    Toast.makeText(getApplicationContext(),"摄像头似乎工作不正常！！",Toast.LENGTH_LONG).show();
                } else {
                    takePic();
                }
                break;

            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUESTCODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Bundle bundle = data.getExtras();
                        String scanResult = bundle.getString("result");
                        Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "获取数据失败！！", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                break;
        }
    }


    //拍照
    public void takePic() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, new File(getFilesDir().getPath()+System.currentTimeMillis()+".jpg"));
        startActivityForResult(intent,PHOTO_REQUESTCODE);
    }


    //有没有摄像头
    public boolean hasCamera() {
        PackageManager pm = getApplicationContext().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    //改变照片的大小后再存储
    public static void saveBitmapToFile(File file, String newpath) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = 6;

            FileInputStream fis = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(fis, null, options);
            fis.close();


            File aa = new File(newpath);
            FileOutputStream outputStream = new FileOutputStream(aa);

            //choose another format if PNG doesn't suit you
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
