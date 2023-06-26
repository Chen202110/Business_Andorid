package com.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chen.adapter.MySingleAdapter;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.OrderVo;
import com.chen.vo.ServerResponse;
import com.chen.widget.RecyclerExtras;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MyOrderActivity extends AppCompatActivity implements RecyclerExtras.OnItemClickListenerForNameAndMark {
    private RecyclerView rv_order;
    private TextView tv_order_date_null;
    private static final int LIST_ORDER_DATE = 1;
    private static final int ORDER_DATE_NULL = 2;
    private static final int DELETE_ORDER = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        init_unit();
    }

    private void init_unit() {
        tv_order_date_null = findViewById(R.id.tv_order_date_null);
        rv_order = findViewById(R.id.rv_order);
        get_order_date();
    }


    private void get_order_date(){
        OkHttpUtils.get(Const.PORTAL_URI+"order/list.do",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<List<OrderVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<OrderVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    Message message = mHandler.obtainMessage();
                    message.obj = serverResponse.getDate();
                    message.what = LIST_ORDER_DATE;
                    mHandler.sendMessage(message);
                }else if (status1 == 39){
                    Message message = mHandler.obtainMessage();
                    message.what = ORDER_DATE_NULL;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void render_list_order(List<OrderVo> orderVos){
        MySingleAdapter adapter = new MySingleAdapter(this,orderVos);
        adapter.setOnItemOrderListener(this);
        adapter.setOnDeleteOrderClickListener(this);
        rv_order.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rv_order.setLayoutManager(manager);
        rv_order.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    private void delete_order(String orderNo){
        OkHttpUtils.get(Const.PORTAL_URI+"order/delete.do/"+orderNo,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    Message message = mHandler.obtainMessage();
                    message.what = DELETE_ORDER;
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
                case LIST_ORDER_DATE:
                    rv_order.setVisibility(View.VISIBLE);
                    tv_order_date_null.setVisibility(View.GONE);
                    List<OrderVo> orderVoList = (List<OrderVo>) msg.obj;
                    render_list_order(orderVoList);
                    break;
                case ORDER_DATE_NULL:
                    tv_order_date_null.setVisibility(View.VISIBLE);
                    rv_order.setVisibility(View.GONE);
                    break;
                case DELETE_ORDER:
                    Toast.makeText(MyOrderActivity.this,"订单删除成功",Toast.LENGTH_SHORT).show();
                    get_order_date();
            }
        }
    };

    @Override
    public void onItemNameAndMarkClick(View view, int position, String name, int mark) {
        switch (mark){
            case 1:
                Long orderNo = Long.valueOf(name);
                Intent intent = new Intent(this,DetailOrderActivity.class);
                intent.putExtra("orderNo",orderNo);
                startActivity(intent);
                break;
            case 2:
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle("提示");
                dialog.setMessage("您真的要删除订单吗？");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete_order(name);
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
        }
    }
}