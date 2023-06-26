package com.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chen.adapter.MyShippingAdapter;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.ServerResponse;
import com.chen.vo.ShippingVo;
import com.chen.widget.RecyclerExtras;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MyShippingActivity extends AppCompatActivity implements View.OnClickListener, RecyclerExtras.OnItemClickListener, RecyclerExtras.OnItemLongClickListener {

    private RecyclerView rv_shipping;
    private Button btn_add_shipping;
    private static final int SHIPPING_DATE = 1;
    private static final int SHIPPING_NULL = 2;
    private static final int SHIPPING_DELETE_SUCCESS = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shipping);
        rv_shipping = findViewById(R.id.rv_shipping);
        btn_add_shipping = findViewById(R.id.btn_add_shipping);
        btn_add_shipping.setOnClickListener(this);
        init_shipping();
    }

    private void init_shipping(){
        OkHttpUtils.get(Const.PORTAL_URI+"shipping/list.do",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<List<ShippingVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<ShippingVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    List<ShippingVo> shippingVoList = serverResponse.getDate();
                    Message message = handler.obtainMessage();
                    message.obj = shippingVoList;
                    message.what = SHIPPING_DATE;
                    handler.sendMessage(message);
                }
                if (status1 == 35){
                    Message message = handler.obtainMessage();
                    message.what = SHIPPING_NULL;
                    handler.sendMessage(message);
                }
            }
        });
    }

    private void render_shipping(List<ShippingVo> shippingVoList){
        MyShippingAdapter adapter = new MyShippingAdapter(this,shippingVoList);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_shipping.setLayoutManager(manager);
        rv_shipping.setAdapter(adapter);
        rv_shipping.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case SHIPPING_DATE:
                    List<ShippingVo> shippingVoList = (List<ShippingVo>) msg.obj;
                    render_shipping(shippingVoList);
                    break;
                case SHIPPING_NULL:
                    rv_shipping.setVisibility(View.GONE);
                case SHIPPING_DELETE_SUCCESS:
                    Toast.makeText(MyShippingActivity.this,"地址删除成功",Toast.LENGTH_SHORT).show();
                    init_shipping();
                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        Intent intent = null;
        if (view.getId() == R.id.btn_add_shipping){
            intent = new Intent(this,AddShippingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this,EditShippingActivity.class);
        intent.putExtra("ShippingId",view.getId());
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        AlertDialog dialog = new AlertDialog.Builder(MyShippingActivity.this).create();
        dialog.setTitle("提示");
        dialog.setMessage("您真的要删除该地址吗？删除地址会导致未付款的订单不能支付");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                OkHttpUtils.get(Const.PORTAL_URI+"shipping/delete/"+view.getId(),new OkHttpCallback(){
                    @Override
                    public void onFinish(String status, String msg) {
                        Gson gson = new Gson();
                        ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                        int count = serverResponse.getStatus();
                        if (count == 0){
                            Message message = handler.obtainMessage();
                            message.what = SHIPPING_DELETE_SUCCESS;
                            handler.sendMessage(message);
                        }
                    }
                });
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}