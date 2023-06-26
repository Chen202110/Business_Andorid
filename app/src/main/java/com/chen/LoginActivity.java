package com.chen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.utils.SharedPreferencesUtil;
import com.chen.vo.ServerResponse;
import com.chen.vo.UserVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username,et_password;
    private Button btn_login,btn_forget;
    private TextView tv_register;
    private static final int LOGIN_SUCCESS = 1;
    private static final int LOGIN_FAIL = 2;
    private boolean is_first_login = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_forget = findViewById(R.id.btn_forget);
        tv_register = findViewById(R.id.tv_register);
        btn_login.setOnClickListener(this);
        btn_forget.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_login:
                //获取用户名
                String username = et_username.getText().toString();
                //获取密码
                String password = et_password.getText().toString();
                //请求接口：okhttp
                OkHttpUtils.get(Const.PORTAL_URI+"user/login.do?username=" + username + "&password=" + password,
                        new OkHttpCallback(){
                            @Override
                            public void onFinish(String status, String msg) {
                                super.onFinish(status, msg);
                                //解析接口返回的数据
                                Gson gson = new Gson();
                                ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                                //ServerResponse<UserVo> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<UserVo>>(){}.getType());
                                int status1 = serverResponse.getStatus();
                                if (status1 == 0){//登录成功
                                    //保存用户信息
//                                    SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences("userinfo",MODE_PRIVATE);
//                                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                                    editor.putBoolean("isLogin",true);
//                                    editor.putString("user",msg);
//                                    editor.commit();
                                    ServerResponse<UserVo> login_date = gson.fromJson(msg,new TypeToken<ServerResponse<UserVo>>(){}.getType());
                                    SharedPreferencesUtil util = SharedPreferencesUtil.getInstance(LoginActivity.this);
                                    util.delete("isLogin");
                                    util.delete("user");
                                    util.putBoolean("isLogin",true);
                                    util.putString("user",gson.toJson(login_date.getDate()));
                                    String likes = login_date.getDate().getLikes();
                                    is_first_login = likes == null;
                                    if (is_first_login){
                                        Intent intent = new Intent(LoginActivity.this,LaunchActivity.class);
                                        startActivity(intent);
                                    }else{
                                        //Activity跳转
                                        Intent intent1 = new Intent(LoginActivity.this,HomeActivity.class);
                                        LoginActivity.this.startActivity(intent1);
                                        Message message = mHandler.obtainMessage();
                                        message.what = LOGIN_SUCCESS;
                                        mHandler.sendMessage(message);
                                    }

                                }else{
                                    Message message = mHandler.obtainMessage();
                                    message.what = LOGIN_FAIL;
                                    message.obj = serverResponse.getMsg();
                                    mHandler.sendMessage(message);
                                }
                            }
                        });
                break;
            case R.id.tv_register:
                intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_forget:
                intent = new Intent(this,SelectActivity.class);
                startActivity(intent);
                break;
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOGIN_SUCCESS:
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN_FAIL:
                    String message = (String) msg.obj;
                    Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
