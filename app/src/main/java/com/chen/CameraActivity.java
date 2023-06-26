package com.chen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    public File file;
    public Uri imageUri;
    private Button btn_camera;
    private ImageView iv_camera;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int PHOTO_REQUEST_CUT = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        btn_camera = findViewById(R.id.btn_camera);
        iv_camera = findViewById(R.id.iv_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });
    }

    //启动相机
    public void startCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,getImageUri(getFile()));
        startActivityForResult(intent,REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA){
            if (resultCode == RESULT_OK){
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    iv_camera.setImageBitmap(bitmap);
                    //bitmap转成File
                    file = bitmap2File(bitmap);
                    OkHttpUtils.upload("http://192.168.7.113:8080/upload",file.getAbsolutePath(),new OkHttpCallback(){
                        @Override
                        public void onFinish(String status, String msg) {
                            super.onFinish(status, msg);
                            Log.e("com.chen",msg);
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    public void crop(Uri uri){
//        //裁剪图片意图
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        intent.setDataAndType(uri,"image/*");
//        intent.putExtra("crop","true");
//        //裁剪框比例：1：1
//        intent.putExtra("aspectX",1);
//        intent.putExtra("aspectY",1);
//        //裁剪后输出图片的尺寸大小
//        intent.putExtra("outputX",250);
//        intent.putExtra("outputY",250);
//
//        intent.putExtra("outputFormat","JPEG");//图片格式
//        intent.putExtra("noFaceDetection",true);//取消人脸识别
//        intent.putExtra("return_data",true);
//        //开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
//        startActivityForResult(intent,PHOTO_REQUEST_CUT);
//    }

    public File getFile(){
        file = new File(getExternalCacheDir(),"temp_photo.png");
        if (file.exists()){
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public Uri getImageUri(File file){
        if (Build.VERSION.SDK_INT >= 24){
            //通过FileProvider获取
            imageUri = FileProvider.getUriForFile(this,"com.chen.fileprovider",file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        return imageUri;
    }

    public File bitmap2File(Bitmap bm){
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bm.compress(Bitmap.CompressFormat.JPEG,80,bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}