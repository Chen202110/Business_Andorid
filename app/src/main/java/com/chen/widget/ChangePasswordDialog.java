package com.chen.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chen.R;
import com.chen.UserSafeActivity;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.utils.SharedPreferencesUtil;
import com.chen.vo.ServerResponse;
import com.chen.vo.UserVo;
import com.google.gson.Gson;

public class ChangePasswordDialog implements View.OnClickListener {

    private Dialog dialog;
    private View view;
    private EditText et_first_password,et_second_password;
    private Button btn_submit;
    private String first_password,second_password;
    private Integer userId;
    private static final int PASSWORD_UPDATE_SUCCESS = 1;

    public ChangePasswordDialog(Context context){
        dialog = new Dialog(context);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_change_password,null);
        et_first_password = view.findViewById(R.id.et_first_password);
        et_second_password = view.findViewById(R.id.et_second_password);
        btn_submit = view.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        UserVo userVo = (UserVo) SharedPreferencesUtil.getInstance(view.getContext()).readObject("user", UserVo.class);
        userId = userVo.getId();
    }
    public void show(){
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.show();
    }

    private void change_password(){
        first_password = et_first_password.getText().toString();
        second_password = et_second_password.getText().toString();
        if (TextUtils.isEmpty(first_password) || TextUtils.isEmpty(second_password)){
            Toast.makeText(view.getContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
        } else if (!first_password.equals(second_password)){
            Toast.makeText(view.getContext(),"两次输入的密码不一致",Toast.LENGTH_SHORT).show();
        } else {
            OkHttpUtils.get(Const.PORTAL_URI+"user/reset.do?userId="+userId+"&password="+first_password,new OkHttpCallback(){
                @Override
                public void onFinish(String status, String msg) {
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                    int status1 = serverResponse.getStatus();
                    if (status1 == 0){
                        Intent intent = new Intent(view.getContext(), UserSafeActivity.class);
                        view.getContext().startActivity(intent);
                        Message message = mHandler.obtainMessage();
                        message.what = PASSWORD_UPDATE_SUCCESS;
                        mHandler.sendMessage(message);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit){
            change_password();
        }
    }
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == PASSWORD_UPDATE_SUCCESS){
                Toast.makeText(view.getContext(), "密码修改成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
