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
import com.chen.vo.ServerResponse;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username,et_password,et_phone,et_email,et_question,et_answer;
    Button btn_register;
    private static final int REGISTER_SUCCESS = 1;
    private static final int REGISTER_FAIL = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_question = findViewById(R.id.et_question);
        et_answer = findViewById(R.id.et_answer);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_register){
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();
            String phone = et_phone.getText().toString();
            String email = et_email.getText().toString();
            String question = et_question.getText().toString();
            String answer = et_answer.getText().toString();
            OkHttpUtils.get(Const.PORTAL_URI+"user/register.do?username=" + username + "&password=" + password + "&phone=" + phone +
                    "&email=" + email + "&question=" + question + "&answer=" + answer,new OkHttpCallback(){
                @Override
                public void onFinish(String status, String msg) {
                    super.onFinish(status, msg);
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                    int status1 = serverResponse.getStatus();
                    if (status1 == 0){
                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                        RegisterActivity.this.startActivity(intent);
                        Message message = mHandler.obtainMessage();
                        message.what = REGISTER_SUCCESS;
                        mHandler.sendMessage(message);
                    } else {
                        Message message = mHandler.obtainMessage();
                        message.what = REGISTER_FAIL;
                        message.obj = serverResponse.getMsg();
                        mHandler.sendMessage(message);
                    }
                }
            });
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REGISTER_SUCCESS:
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    break;
                case REGISTER_FAIL:
                    String message = (String) msg.obj;
                    Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}