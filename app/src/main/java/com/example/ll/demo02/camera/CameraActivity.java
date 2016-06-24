package com.example.ll.demo02.camera;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ll.demo02.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.channels.FileChannel;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_camera;
    public final int PHOTO_REQUESTCODE = 1000;
    public final int PERMISSION_CAMERA = 1001;
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案
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
                //在6.0或6.0以上的才需要请求权限
                if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(CameraActivity.this,new String[]{Manifest.permission.CAMERA},PERMISSION_CAMERA);
                } else {
                    if (!hasCamera()){
                        Toast.makeText(getApplicationContext(),"摄像头工作不正常！！",Toast.LENGTH_LONG).show();
                    } else {
                        takePic();
                    }
                }
                break;

            default:
                break;
        }
    }


    //权限请求回传
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CAMERA:
                for (int grantResult : grantResults) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission Granted
                        if (!hasCamera()){
                            Toast.makeText(getApplicationContext(),"摄像头工作不正常！！",Toast.LENGTH_LONG).show();
                        } else {
                            takePic();
                        }
                    } else {
                        // Permission Denied
                        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
                        builder.setTitle("注意！！");
                        builder.setMessage("没有授权！！！");
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "!!!!!!!!!!!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startAppSettings();
                            }
                        });
                        builder.show();
                    }
                }
                break;
        }
    }


    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivity(intent);
    }


    //拍照回传
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUESTCODE:
                if (resultCode == RESULT_OK) {
//                    Bundle bundle = data.getExtras();
//                    Bitmap tmp = (Bitmap) bundle.get("data");
                    Toast.makeText(this, "拍照成功！！", Toast.LENGTH_LONG).show();
                    saveBitmapToFile(new File(Environment.getExternalStorageDirectory(), "temp1.jpg"), new File(Environment.getExternalStorageDirectory(), "11.jpg").getPath());
                } else {
                    Toast.makeText(this, "拍照失败！！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }


    //拍照
    public void takePic() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tempFile = new File(Environment.getExternalStorageDirectory(), "temp1.jpg");//存储卡根目录
        try {
            if (!tempFile.exists()) {
                tempFile.createNewFile();
            } else {
                if (tempFile.delete()) {
                    tempFile.createNewFile();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imgUri = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, PHOTO_REQUESTCODE);
    }


    //有没有摄像头
    public boolean hasCamera() {
        PackageManager pm = getApplicationContext().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }


    //改变照片的大小后再存储
    public void saveBitmapToFile(File file, String newpath) {

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
            options.inSampleSize = 6;//采样率压缩
            if (file.length() / 1000000 >= 2){//当原图超过2M的时候，缩放系数加1
                options.inSampleSize = 7;//采样率压缩
            }

            FileInputStream fis = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(fis, null, options);
//            Bitmap selectedBitmap = BitmapFactory.decodeFile(file.getPath(),options);
            fis.close();

            ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos3);
            File aa3 = new File(Environment.getExternalStorageDirectory() + "/33.jpg");
            FileOutputStream outputStream3 = new FileOutputStream(aa3);
            outputStream3.write(baos3.toByteArray());
            outputStream3.flush();
            outputStream3.close();

            int picDegree = getBitmapDegree(file.getPath());
            Log.i("file","照片旋转角度==" + picDegree);
            selectedBitmap = rotateBitmapByDegree(selectedBitmap,picDegree);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //choose another format if PNG doesn't suit you
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);//照片质量压缩

            File aa = new File(newpath);
            FileOutputStream outputStream = new FileOutputStream(aa);
            outputStream.write(baos.toByteArray());
            outputStream.flush();
            outputStream.close();

            File aaNew = new File(newpath);
            long fileSize = aaNew.length() / 1000;//手机上应该是除以1000
            Log.i("file", "照片大小=="+fileSize);
            if (fileSize > 100) {
                Toast.makeText(CameraActivity.this, ">100!!", Toast.LENGTH_SHORT).show();
            }



            //通过缩放图片像素来减少图片占用内存大小
            Bitmap result = Bitmap.createBitmap(selectedBitmap.getWidth() / 2, selectedBitmap.getHeight() / 2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Rect rect = new Rect(0,0,selectedBitmap.getWidth() / 2, selectedBitmap.getHeight() / 2);
            canvas.drawBitmap(selectedBitmap, null, rect, null);

            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            result.compress(Bitmap.CompressFormat.JPEG, 85, baos2);
            File aa2 = new File(Environment.getExternalStorageDirectory() + "/22.jpg");
            FileOutputStream outputStream2 = new FileOutputStream(aa2);
            outputStream2.write(baos2.toByteArray());
            outputStream2.flush();
            outputStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }



    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm 需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
        returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }



}
