package com.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.google.gson.reflect.TypeToken;

public class UpdateUserInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_username,et_email,et_phone;
    private Button btn_ok;
    private String username,email,phone;
    private static final int USERINFO_UPDATE_SUCCESS = 1;
    private static final int USERINFO_UPDATE_FAIL = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_phone = findViewById(R.id.et_phone);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean isLogin = SharedPreferencesUtil.getInstance(this).readBoolean("isLogin");
        if (isLogin){
            UserVo userVo = (UserVo) SharedPreferencesUtil.getInstance(this).readObject("user", UserVo.class);
            et_username.setText(userVo.getUsername());
            et_phone.setText(userVo.getPhone());
            et_email.setText(userVo.getEmail());
        }
    }

    private void update_userInfo(){
        username = et_username.getText().toString();
        email = et_email.getText().toString();
        phone = et_phone.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone)){
            Toast.makeText(this,"参数不能为空",Toast.LENGTH_SHORT).show();
        } else {
            OkHttpUtils.get(Const.PORTAL_URI+"user/update.do?username="+username+"&email="+email+"&phone="+phone,new OkHttpCallback(){
                @Override
                public void onFinish(String status, String msg) {
                    super.onFinish(status, msg);
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg, ServerResponse.class);
                    int status1 = serverResponse.getStatus();
                    if (status1 == 0){
                        ServerResponse<UserVo> update_date = gson.fromJson(msg,new TypeToken<ServerResponse<UserVo>>(){}.getType());
                        SharedPreferencesUtil util = SharedPreferencesUtil.getInstance(UpdateUserInfoActivity.this);
                        util.delete("isLogin");
                        util.delete("user");
                        util.putBoolean("isLogin",true);
                        util.putString("user",gson.toJson(update_date.getDate()));
                        Intent intent = new Intent(UpdateUserInfoActivity.this,UserSafeActivity.class);
                        startActivity(intent);
                        Message message = mHandler.obtainMessage();
                        message.what = USERINFO_UPDATE_SUCCESS;
                        mHandler.sendMessage(message);
                    } else {
                        Message message = mHandler.obtainMessage();
                        message.what = USERINFO_UPDATE_FAIL;
                        message.obj = serverResponse.getMsg();
                        mHandler.sendMessage(message);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_ok){
            update_userInfo();
        }
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case USERINFO_UPDATE_SUCCESS:
                    Toast.makeText(UpdateUserInfoActivity.this,"用户信息修改成功",Toast.LENGTH_SHORT).show();
                    break;
                case USERINFO_UPDATE_FAIL:
                    String message = (String) msg.obj;
                    Toast.makeText(UpdateUserInfoActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        }
    };
}