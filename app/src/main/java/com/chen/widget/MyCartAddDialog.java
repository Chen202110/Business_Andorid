package com.chen.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.chen.R;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.CartVo;
import com.chen.vo.ProductListVo;
import com.chen.vo.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class MyCartAddDialog implements View.OnClickListener {

    private Dialog dialog;
    private View view;
    private ImageView iv_close,iv_count_minus,iv_count_add;
    private Button btn_ok;
    private EditText et_count;
    private Integer productId;
    private static final int ADD_CART_SUCCESS = 1;
    public MyCartAddDialog(Context context, Integer productId){
        this.productId = productId;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_cart,null);
        dialog = new Dialog(context);
        iv_close = view.findViewById(R.id.iv_close);
        iv_count_minus = view.findViewById(R.id.iv_count_minus);
        iv_count_add = view.findViewById(R.id.iv_count_add);
        btn_ok = view.findViewById(R.id.btn_ok);
        et_count = view.findViewById(R.id.et_count);
        iv_close.setOnClickListener(this);
        iv_count_minus.setOnClickListener(this);
        iv_count_add.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
    }

    public void show(){
        dialog.getWindow().setContentView(view);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        int count = Integer.parseInt(TextUtils.isEmpty(et_count.getText().toString()) ? "1"
                : et_count.getText().toString());
        switch (view.getId()){
            case R.id.iv_close:
                dialog.dismiss();
                break;
            case R.id.iv_count_minus:
                if (count > 1){
                    count -= 1;
                }
                et_count.setText(count+"");
                break;
            case R.id.iv_count_add:
                et_count.setText((++count) + "");
                break;
            case R.id.btn_ok:
                String number = TextUtils.isEmpty(et_count.getText().toString()) ? "1"
                        : et_count.getText().toString();
                add_cart(productId,number);
                dialog.dismiss();
                break;
        }
    }
    public void add_cart(Integer productId,String number){
        OkHttpUtils.get(Const.PORTAL_URI+"cart/"+productId+"/"+number,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<CartVo> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<CartVo>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    Message message = mHandler.obtainMessage();
                    message.what = ADD_CART_SUCCESS;
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
            if(msg.what == ADD_CART_SUCCESS){
                Toast.makeText(view.getContext(), "购物车添加成功", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
