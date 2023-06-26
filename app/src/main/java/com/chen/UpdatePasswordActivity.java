package com.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.common.Const;
import com.chen.utils.MD5Utils;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.ServerResponse;
import com.chen.widget.ChangePasswordDialog;
import com.google.gson.Gson;

public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_oldPassword;
    private TextView tv_forget_password;
    private Button btn_verity;
    private String oldPassword;
    private final static int GET_OLDPASSWORD = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        et_oldPassword = findViewById(R.id.et_oldPassword);
        tv_forget_password = findViewById(R.id.tv_forget_password);
        btn_verity = findViewById(R.id.btn_verity);
        btn_verity.setOnClickListener(this);
        tv_forget_password.setOnClickListener(this);
        get_oldPassword();
    }

    private void get_oldPassword(){
        OkHttpUtils.get(Const.PORTAL_URI+"user/get/password",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse serverResponse = gson.fromJson(msg, ServerResponse.class);
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    String oldPassword = (String) serverResponse.getDate();
                    Message message = mHandler.obtainMessage();
                    message.obj = oldPassword;
                    message.what = GET_OLDPASSWORD;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void verity_password(){
        String write_Password = et_oldPassword.getText().toString();
        if (TextUtils.isEmpty(write_Password)){
            Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
        } else {
            if (oldPassword.equals(MD5Utils.getMD5Code(write_Password))){
                Toast.makeText(this,"验证通过",Toast.LENGTH_SHORT).show();
                ChangePasswordDialog dialog = new ChangePasswordDialog(this);
                dialog.show();
            } else {
                Toast.makeText(this,"密码错误",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void forget_password(){
        Intent intent = new Intent(this,VerityActivity.class);
        startActivity(intent);
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_OLDPASSWORD:
                    oldPassword = (String) msg.obj;
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_verity){
            verity_password();
        } else {
            forget_password();
        }
    }
}