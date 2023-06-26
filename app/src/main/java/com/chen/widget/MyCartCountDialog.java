package com.chen.widget;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.chen.R;
import com.chen.bean.MessageEvent;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.CartVo;
import com.chen.vo.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

public class MyCartCountDialog extends AppCompatActivity implements View.OnClickListener{

    private Dialog dialog;
    private View view;
    private ImageView iv_close,iv_count_minus,iv_count_add;
    private Button btn_ok;
    private EditText et_count;
    private Integer productId;
    private Integer count;
    private static final int PRODUCT_COUNT_UPDATE_SUCCESS = 1;

    public MyCartCountDialog(Context context, Integer productId,Integer count){
        this.productId = productId;
        this.count = count;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_cart,null);
        dialog = new Dialog(context);
        iv_close = view.findViewById(R.id.iv_close);
        iv_count_minus = view.findViewById(R.id.iv_count_minus);
        iv_count_add = view.findViewById(R.id.iv_count_add);
        btn_ok = view.findViewById(R.id.btn_ok);
        et_count = view.findViewById(R.id.et_count);
        et_count.setText(count+"");
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
                update_cart_count(productId,number);
                dialog.dismiss();
                break;
        }
    }
    public void update_cart_count(Integer productId,String number){
        OkHttpUtils.get(Const.PORTAL_URI+"cart/update.do/"+productId+"/"+number,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<CartVo> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<CartVo>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    CartVo cartVo = serverResponse.getDate();
                    MessageEvent messageEvent = new MessageEvent(cartVo);
                    EventBus.getDefault().post(messageEvent);
                    Message message = mHandler.obtainMessage();
                    message.what = PRODUCT_COUNT_UPDATE_SUCCESS;
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
            if (msg.what == PRODUCT_COUNT_UPDATE_SUCCESS){
                Toast.makeText(view.getContext(), "商品数量修改成功",Toast.LENGTH_LONG).show();
            }
        }
    };
}
