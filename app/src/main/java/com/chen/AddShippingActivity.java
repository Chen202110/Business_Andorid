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
import com.chen.vo.ServerResponse;
import com.google.gson.Gson;

public class AddShippingActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_receiverName,et_receiverPhone,et_receiverProvince,et_receiverCity,
            et_receiverDistrict,et_receiverAddress;
    private Button btn_shipping_save;
    private String ReceiverName,ReceiverPhone,ReceiverProvince,ReceiverCity,
            ReceiverDistrict,ReceiverAddress;
    private static final int ADD_SHIPPING_SUCCESS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipping);
        et_receiverName = findViewById(R.id.et_receiverName);
        et_receiverPhone = findViewById(R.id.et_receiverPhone);
        et_receiverProvince = findViewById(R.id.et_receiverProvince);
        et_receiverCity = findViewById(R.id.et_receiverCity);
        et_receiverDistrict = findViewById(R.id.et_receiverDistrict);
        et_receiverAddress = findViewById(R.id.et_receiverAddress);
        btn_shipping_save = findViewById(R.id.btn_shipping_save);
        btn_shipping_save.setOnClickListener(this);
    }

    private void get_receiverDate(){
        ReceiverName = et_receiverName.getText().toString();
        ReceiverPhone = et_receiverPhone.getText().toString();
        ReceiverProvince = et_receiverProvince.getText().toString();
        ReceiverCity = et_receiverCity.getText().toString();
        ReceiverDistrict = et_receiverDistrict.getText().toString();
        ReceiverAddress = et_receiverAddress.getText().toString();
        if (TextUtils.isEmpty(ReceiverName)||TextUtils.isEmpty(ReceiverPhone)||TextUtils.isEmpty(ReceiverProvince)||TextUtils.isEmpty(ReceiverCity)||TextUtils.isEmpty(ReceiverDistrict)||TextUtils.isEmpty(ReceiverAddress)){
            Toast.makeText(AddShippingActivity.this,"参数不能为空",Toast.LENGTH_SHORT).show();
        } else {
            OkHttpUtils.get(Const.PORTAL_URI+"shipping/add.do?receiverName="+ReceiverName+"&receiverPhone="+ReceiverPhone+
                    "&receiverProvince="+ReceiverProvince+"&receiverCity="+ReceiverCity+"&receiverDistrict="+ReceiverDistrict+
                    "&receiverAddress="+ReceiverAddress,new OkHttpCallback(){
                @Override
                public void onFinish(String status, String msg) {
                    Gson gson = new Gson();
                    ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                    int status1 = serverResponse.getStatus();
                    if (status1 == 0){
                        Intent intent = new Intent(AddShippingActivity.this,MyShippingActivity.class);
                        startActivity(intent);
                        Message message = mHandler.obtainMessage();
                        message.what = ADD_SHIPPING_SUCCESS;
                        mHandler.sendMessage(message);
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_shipping_save){
            get_receiverDate();
        }
    }
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == ADD_SHIPPING_SUCCESS){
                Toast.makeText(AddShippingActivity.this,"收获地址添加成功",Toast.LENGTH_SHORT).show();
            }
        }
    };
}