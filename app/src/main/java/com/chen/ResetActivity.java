package com.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.utils.SharedPreferencesUtil;
import com.chen.vo.ServerResponse;
import com.chen.vo.UserVo;
import com.google.gson.Gson;

public class ResetActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_first_password,et_second_password;
    private Button btn_reset;
    private static final int RESET_PASSWORD_SUCCESS = 1;
    private static final int RESET_PASSWORD_FAIL = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        et_first_password = findViewById(R.id.et_first_password);
        et_second_password = findViewById(R.id.et_second_password);
        btn_reset = findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String first_password = et_first_password.getText().toString();
        String second_password = et_second_password.getText().toString();
        if (first_password.equals(second_password)){
            UserVo userVo = (UserVo) SharedPreferencesUtil.getInstance(this).readObject("user", UserVo.class);
            Integer userId = userVo.getId();
            OkHttpUtils.get(Const.PORTAL_URI+"user/reset.do?userId="+userId+"&password="+first_password,new OkHttpCallback(){
                @Override
                public void onFinish(String status, String msg) {
                    super.onFinish(status, msg);
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                    int status1 = serverResponse.getStatus();
                    if (status1 == 0){
                        Intent intent = new Intent(ResetActivity.this,LoginActivity.class);
                        ResetActivity.this.startActivity(intent);
                        Message message = mHandle.obtainMessage();
                        message.what = RESET_PASSWORD_SUCCESS;
                        mHandle.sendMessage(message);
                    } else {
                       Message message = mHandle.obtainMessage();
                       message.obj = serverResponse.getMsg();
                       message.what = RESET_PASSWORD_FAIL;
                       mHandle.sendMessage(message);
                    }
                }
            });
        }else{
            Toast.makeText(this,"两次输入的密码不一致",Toast.LENGTH_LONG).show();
        }
    }
    @SuppressLint("HandlerLeak")
    Handler mHandle = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case RESET_PASSWORD_SUCCESS:
                    Toast.makeText(ResetActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                    break;
                case RESET_PASSWORD_FAIL:
                    String message = (String) msg.obj;
                    Toast.makeText(ResetActivity.this,message,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}