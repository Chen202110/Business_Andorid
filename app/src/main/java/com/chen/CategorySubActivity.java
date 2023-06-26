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
import android.widget.Toast;

import com.chen.adapter.MyCategorySubAdapter;
import com.chen.adapter.MyProductItemAdapter;
import com.chen.bean.CategoryInfo;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.CategoryVo;
import com.chen.vo.ProductListVo;
import com.chen.vo.ServerResponse;
import com.chen.widget.RecyclerExtras;
import com.chen.widget.SpacesItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class CategorySubActivity extends AppCompatActivity implements RecyclerExtras.OnItemClickListenerForMark{
    private TextView tv_title;
    private RecyclerView rv_content,rv_product;
    private static final int CATEGORY_DATE = 1;
    private static final int PRODUCT_LIST = 2;
    MyCategorySubAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_sub);
        //categoryId
        int categoryId = getIntent().getIntExtra("categoryId",0);
        //categoryName
        String categoryName = getIntent().getStringExtra("categoryName");
        tv_title = findViewById(R.id.tv_title);
        rv_content = findViewById(R.id.rv_content);
        rv_product = findViewById(R.id.rv_product);
        tv_title.setText(categoryName);
        OkHttpUtils.get(Const.PORTAL_URI+"category/sub/"+categoryId,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);
                Gson gson = new Gson();
                ServerResponse<List<CategoryVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<CategoryVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    List<CategoryVo> categoryVoList = serverResponse.getDate();
                    Message message = mHandler.obtainMessage();
                    message.what = CATEGORY_DATE;
                    message.obj = categoryVoList;
                    mHandler.sendMessage(message);
                }
            }
        });
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case CATEGORY_DATE:
                    List<CategoryVo> categoryVoList = (List<CategoryVo>) msg.obj;
                    render_categoryInfo(categoryVoList);
                    break;
                case PRODUCT_LIST:
                    List<ProductListVo> productListVoList = (List<ProductListVo>) msg.obj;
                    MyProductItemAdapter adapter = new MyProductItemAdapter(CategorySubActivity.this,productListVoList);
                    GridLayoutManager manager = new GridLayoutManager(CategorySubActivity.this,2);
                    adapter.setOnItemClickListener(CategorySubActivity.this);
                    rv_product.setLayoutManager(manager);
                    rv_product.setAdapter(adapter);
                    rv_product.addItemDecoration(new DividerItemDecoration(CategorySubActivity.this,DividerItemDecoration.HORIZONTAL));
                    rv_product.addItemDecoration(new DividerItemDecoration(CategorySubActivity.this,DividerItemDecoration.VERTICAL));
            }
        }
    };
    private void render_categoryInfo(List<CategoryVo> categoryVoList){
        List<CategoryInfo> categoryInfoList = new ArrayList<>();
        for (CategoryVo categoryVo:categoryVoList){
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.setName(categoryVo.getName());
            categoryInfo.setId(categoryVo.getId());
            categoryInfoList.add(categoryInfo);
        }
        GridLayoutManager manager = new GridLayoutManager(this,5);
        rv_content.setLayoutManager(manager);
        adapter = new MyCategorySubAdapter(this,categoryInfoList);
        adapter.setOnItemClickListener(this);;
        rv_content.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemMarkClick(View view, int position, Integer mark) {
        adapter.clearSelection(position);
        adapter.notifyDataSetChanged();
        switch (mark){
            case 1:
                OkHttpUtils.get(Const.PORTAL_URI+"product/list/"+view.getId(),new OkHttpCallback(){
                    @Override
                    public void onFinish(String status, String msg) {
                        Gson gson = new Gson();
                        ServerResponse<List<ProductListVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<ProductListVo>>>(){}.getType());
                        int status1 = serverResponse.getStatus();
                        if (status1 == 0){
                            List<ProductListVo> productListVoList = serverResponse.getDate();
                            Message message = mHandler.obtainMessage();
                            message.what = PRODUCT_LIST;
                            message.obj = productListVoList;
                            mHandler.sendMessage(message);
                        }
                    }
                });
                break;
            case 2:
                Intent intent = new Intent(CategorySubActivity.this, ProductDetailActivity.class);
                intent.putExtra("productId",view.getId());
                startActivity(intent);
                break;
        }
    }
}