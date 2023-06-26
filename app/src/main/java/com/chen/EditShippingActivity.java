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
import com.chen.vo.ShippingVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EditShippingActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_receiverName,et_receiverPhone,et_receiverProvince,et_receiverCity,
            et_receiverDistrict,et_receiverAddress;
    private Button btn_shipping_save;
    private String ReceiverName,ReceiverPhone,ReceiverProvince,ReceiverCity,
            ReceiverDistrict,ReceiverAddress;
    private int ShippingId;
    private static final int SHIPPING_DATE = 1;
    private static final int SHIPPING_UPDATE_SUCCESS = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shipping);
        et_receiverName = findViewById(R.id.et_receiverName);
        et_receiverPhone = findViewById(R.id.et_receiverPhone);
        et_receiverProvince = findViewById(R.id.et_receiverProvince);
        et_receiverCity = findViewById(R.id.et_receiverCity);
        et_receiverDistrict = findViewById(R.id.et_receiverDistrict);
        et_receiverAddress = findViewById(R.id.et_receiverAddress);
        btn_shipping_save = findViewById(R.id.btn_shipping_save);
        btn_shipping_save.setOnClickListener(this);
        ShippingId = getIntent().getIntExtra("ShippingId",0);
        query_shipping(ShippingId);
    }

    private void query_shipping(Integer shippingId){
        OkHttpUtils.get(Const.PORTAL_URI+"shipping/query/"+shippingId,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<ShippingVo>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    ShippingVo shippingVo = (ShippingVo) serverResponse.getDate();
                    Message message = mHandler.obtainMessage();
                    message.what = SHIPPING_DATE;
                    message.obj = shippingVo;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void edit_shipping(ShippingVo shippingVo){
        et_receiverName.setText(shippingVo.getReceiverName());
        et_receiverAddress.setText(shippingVo.getReceiverAddress());
        et_receiverCity.setText(shippingVo.getReceiverCity());
        et_receiverDistrict.setText(shippingVo.getReceiverDistrict());
        et_receiverPhone.setText(shippingVo.getReceiverPhone());
        et_receiverProvince.setText(shippingVo.getReceiverProvince());
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case SHIPPING_DATE:
                    ShippingVo shippingVo = (ShippingVo) msg.obj;
                    edit_shipping(shippingVo);
                    break;
                case SHIPPING_UPDATE_SUCCESS:
                    Toast.makeText(EditShippingActivity.this, "收获地址修改成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onClick(View view) {
        ReceiverName = et_receiverName.getText().toString();
        ReceiverPhone = et_receiverPhone.getText().toString();
        ReceiverProvince = et_receiverProvince.getText().toString();
        ReceiverCity = et_receiverCity.getText().toString();
        ReceiverDistrict = et_receiverDistrict.getText().toString();
        ReceiverAddress = et_receiverAddress.getText().toString();
        if (TextUtils.isEmpty(ReceiverName)||TextUtils.isEmpty(ReceiverPhone)||TextUtils.isEmpty(ReceiverProvince)||TextUtils.isEmpty(ReceiverCity)||TextUtils.isEmpty(ReceiverDistrict)||TextUtils.isEmpty(ReceiverAddress)){
            Toast.makeText(EditShippingActivity.this,"参数不能为空",Toast.LENGTH_SHORT).show();
        } else {
                OkHttpUtils.get(Const.PORTAL_URI+"shipping/update.do?receiverName="+ReceiverName+"&receiverPhone="+ReceiverPhone+
                        "&receiverProvince="+ReceiverProvince+"&receiverCity="+ReceiverCity+"&receiverDistrict="+ReceiverDistrict+
                        "&receiverAddress="+ReceiverAddress+"&id="+ShippingId,new OkHttpCallback(){
                    @Override
                    public void onFinish(String status, String msg) {
                        Gson gson = new Gson();
                        ServerResponse serverResponse = gson.fromJson(msg, ServerResponse.class);
                        int status1 = serverResponse.getStatus();
                        if (status1 == 0) {
                            Intent intent = new Intent(EditShippingActivity.this, MyShippingActivity.class);
                            startActivity(intent);
                            Message message = mHandler.obtainMessage();
                            message.what = SHIPPING_UPDATE_SUCCESS;
                            mHandler.sendMessage(message);
                        }
                    }
                });
            }
    }
}