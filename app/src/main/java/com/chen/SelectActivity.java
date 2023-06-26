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
import com.google.gson.reflect.TypeToken;

public class SelectActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_username,et_phone;
    private Button btn_select;
    private static final int SELECT_SUCCESS = 1;
    private static final int SELECT_FAIL = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        et_username = findViewById(R.id.et_username);
        et_phone = findViewById(R.id.et_phone);
        btn_select = findViewById(R.id.btn_select);
        btn_select.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String username = et_username.getText().toString();
        String phone = et_phone.getText().toString();
        OkHttpUtils.get(Const.PORTAL_URI+"user/reset/select/?username="+username+"&phone="+phone,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);
                Gson gson = new Gson();
                ServerResponse<UserVo> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<UserVo>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    SharedPreferencesUtil util = SharedPreferencesUtil.getInstance(SelectActivity.this);
                    util.delete("user");
                    util.putString("user",gson.toJson(serverResponse.getDate()));
                    Intent intent = new Intent(SelectActivity.this,VerityActivity.class);
                    startActivity(intent);
                    Message message = mHandler.obtainMessage();
                    message.what = SELECT_SUCCESS;
                    mHandler.sendMessage(message);
                } else {
                    Message message = mHandler.obtainMessage();
                    message.obj = serverResponse.getMsg();
                    message.what = SELECT_FAIL;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SELECT_SUCCESS:
                    Toast.makeText(SelectActivity.this, "查询成功", Toast.LENGTH_SHORT).show();
                    break;
                case SELECT_FAIL:
                    String message = (String) msg.obj;
                    Toast.makeText(SelectActivity.this,message,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}