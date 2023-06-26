package com.chen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chen.utils.SharedPreferencesUtil;
import com.chen.vo.UserVo;

public class UserSafeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_update_userinfo,btn_update_password;
    private TextView tv_username,tv_phone,tv_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_safe);
        btn_update_userinfo = findViewById(R.id.btn_update_userinfo);
        btn_update_password = findViewById(R.id.btn_update_password);
        tv_username = findViewById(R.id.tv_username);
        tv_phone = findViewById(R.id.tv_phone);
        tv_email = findViewById(R.id.tv_email);
        btn_update_password.setOnClickListener(this);
        btn_update_userinfo.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean isLogin = SharedPreferencesUtil.getInstance(this).readBoolean("isLogin");
        if (isLogin){
            UserVo userVo = (UserVo) SharedPreferencesUtil.getInstance(this).readObject("user", UserVo.class);
            tv_username.setText(userVo.getUsername());
            tv_phone.setText(userVo.getPhone());
            tv_email.setText(userVo.getEmail());
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()){
            case R.id.btn_update_userinfo:
                intent = new Intent(this,UpdateUserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_update_password:
                intent = new Intent(this,UpdatePasswordActivity.class);
                startActivity(intent);
                break;
        }
    }
}