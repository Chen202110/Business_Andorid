package com.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.chen.adapter.MyProductItemAdapter;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.ProductListVo;
import com.chen.vo.ServerResponse;
import com.chen.widget.RecyclerExtras;
import com.chen.widget.SpacesItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class CategoryNoSubActivity extends AppCompatActivity implements RecyclerExtras.OnItemClickListenerForMark {
    private TextView tv_title;
    private RecyclerView rv_content;
    private static final int PRODUCT_LIST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_no_sub);
        tv_title = findViewById(R.id.tv_title);
        rv_content = findViewById(R.id.rv_content);
        String categoryName = getIntent().getStringExtra("categoryName");
        int categoryId = getIntent().getIntExtra("categoryId",0);
        tv_title.setText(categoryName);

        OkHttpUtils.get(Const.PORTAL_URI+"product/list/"+categoryId,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<List<ProductListVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<ProductListVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    List<ProductListVo> productListVoList = serverResponse.getDate();
                    Message message = mHandler.obtainMessage();
                    message.obj = productListVoList;
                    message.what = PRODUCT_LIST;
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
            if (msg.what == PRODUCT_LIST){
                List<ProductListVo> productListVoList = (List<ProductListVo>) msg.obj;
                MyProductItemAdapter adapter = new MyProductItemAdapter(CategoryNoSubActivity.this,productListVoList);
                GridLayoutManager manager = new GridLayoutManager(CategoryNoSubActivity.this,2);
                adapter.setOnItemClickListener(CategoryNoSubActivity.this);
                rv_content.setLayoutManager(manager);
                rv_content.setAdapter(adapter);
                rv_content.setItemAnimator(new DefaultItemAnimator());
                rv_content.addItemDecoration(new SpacesItemDecoration(5));
                rv_content.addItemDecoration(new DividerItemDecoration(CategoryNoSubActivity.this,DividerItemDecoration.HORIZONTAL));
                rv_content.addItemDecoration(new DividerItemDecoration(CategoryNoSubActivity.this,DividerItemDecoration.VERTICAL));
            }
        }
    };

    @Override
    public void onItemMarkClick(View view, int position, Integer mark) {
        Intent intent = new Intent(this,ProductDetailActivity.class);
        intent.putExtra("productId",view.getId());
        startActivity(intent);
    }
}