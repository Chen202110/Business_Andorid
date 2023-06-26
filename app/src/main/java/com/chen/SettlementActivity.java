package com.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.chen.adapter.MyOrderAdapter;
import com.chen.alipay.PayResult;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.OrderItemVo;
import com.chen.vo.OrderVo;
import com.chen.vo.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;


public class SettlementActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_order_no,tv_receiverProvince,tv_receiverCity,tv_receiverDistrict,tv_receiverAddress,tv_receiverName,tv_receiverPhone,
            tv_total_count,tv_total_price,tv_postage,tv_payment,tv_settlement_count,tv_settlement_price;
    private RecyclerView rv_order_item;
    private Button btn_submit;
    private Integer ShippingId;
    private String orderInfo;
    private static final int CREATE_ORDER = 1;
    private static final int SDK_PAY_FLAG = 2;
    private static final int ORDER_INFO = 3;
    private String OrderNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement);
        init_unit();
        create_order();
    }

    private void init_unit(){
        tv_order_no = findViewById(R.id.tv_order_no);
        tv_postage = findViewById(R.id.tv_postage);
        tv_receiverProvince = findViewById(R.id.tv_receiverProvince);
        tv_receiverCity = findViewById(R.id.tv_receiverCity);
        tv_receiverDistrict = findViewById(R.id.tv_receiverDistrict);
        tv_receiverAddress = findViewById(R.id.tv_receiverAddress);
        tv_receiverName = findViewById(R.id.tv_receiverName);
        tv_receiverPhone = findViewById(R.id.tv_receiverPhone);
        tv_total_count = findViewById(R.id.tv_total_count);
        tv_total_price = findViewById(R.id.tv_total_price);
        tv_payment = findViewById(R.id.tv_payment);
        tv_settlement_count = findViewById(R.id.tv_settlement_count);
        tv_settlement_price = findViewById(R.id.tv_settlement_price);
        rv_order_item = findViewById(R.id.rv_order_item);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        ShippingId = getIntent().getIntExtra("ShippingId",0);
    }

    private void create_order(){
        OkHttpUtils.get(Const.PORTAL_URI+"order/create/"+ShippingId,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<OrderVo> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<OrderVo>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    OrderVo orderVo = serverResponse.getDate();
                    Message message = mHandler.obtainMessage();
                    message.obj = orderVo;
                    message.what = CREATE_ORDER;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    /**
     * 支付宝支付业务示例
     */
    public void payV2() {

        final Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(SettlementActivity.this);

                Map<String, String> result = alipay.payV2(orderInfo,true);
                Log.i("msp", result.toString());
                Message msg = mHandler.obtainMessage();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private void render_order(OrderVo orderVo){
        OrderNo = String.valueOf(orderVo.getOrderNo());
        tv_order_no.setText(String.valueOf(orderVo.getOrderNo()));
        tv_receiverProvince.setText(orderVo.getShippingVo().getReceiverProvince());
        tv_receiverCity.setText(orderVo.getShippingVo().getReceiverCity());
        tv_receiverDistrict.setText(orderVo.getShippingVo().getReceiverDistrict());
        tv_receiverAddress.setText(orderVo.getShippingVo().getReceiverAddress());
        tv_receiverName.setText(orderVo.getShippingVo().getReceiverName());
        tv_receiverPhone.setText(orderVo.getShippingVo().getReceiverPhone());
        tv_total_count.setText(String.valueOf(orderVo.getOrderItemVoList().size()));
        tv_total_price.setText(String.valueOf(orderVo.getPayment().doubleValue()));
        tv_postage.setText(String.valueOf(orderVo.getPostage()));
        tv_payment.setText(String.valueOf(orderVo.getPayment().doubleValue()));
        tv_settlement_count.setText(String.valueOf(orderVo.getOrderItemVoList().size()));
        tv_settlement_price.setText(String.valueOf(orderVo.getPayment().doubleValue()));
        render_order_item(orderVo.getOrderItemVoList());
        getOrderInfo();
    }

    private void render_order_item(List<OrderItemVo> orderItemVoList){
        MyOrderAdapter adapter = new MyOrderAdapter(this,orderItemVoList);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_order_item.setAdapter(adapter);
        rv_order_item.setLayoutManager(manager);
        rv_order_item.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    private void getOrderInfo(){
        OkHttpUtils.get(Const.PORTAL_URI+"pay/pay.do?orderNo="+OrderNo,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    Message message = mHandler.obtainMessage();
                    message.obj = serverResponse.getDate();
                    message.what = ORDER_INFO;
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
                case CREATE_ORDER:
                    OrderVo orderVo = (OrderVo) msg.obj;
                    render_order(orderVo);
                    break;
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(SettlementActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettlementActivity.this,HomeActivity.class);
                        intent.putExtra("id",2);
                        startActivity(intent);
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(SettlementActivity.this,"支付失败",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettlementActivity.this,HomeActivity.class);
                        intent.putExtra("id",2);
                        startActivity(intent);
                    }
                    break;
                case ORDER_INFO:
                    orderInfo = (String) msg.obj;
                    EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_submit){
            payV2();
        }
    }

}