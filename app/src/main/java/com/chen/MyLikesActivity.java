package com.chen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chen.adapter.MyLikeCategoryAdapter;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.CategoryNameVo;
import com.chen.vo.ServerResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;

public class MyLikesActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_ok;
    private RecyclerView rv_classify;
    private static final int CATEGORY_DATE = 1;
    private static final int USER_LIKES = 2;
    MyLikeCategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_likes);
        init_unit();
    }
    private void init_unit(){
        rv_classify = findViewById(R.id.rv_classify);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        get_category_date();
    }
    private void get_category_date(){
        OkHttpUtils.get(Const.PORTAL_URI+"category/two/name.do",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<List<CategoryNameVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<CategoryNameVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    Message message = mHandler.obtainMessage();
                    message.what = CATEGORY_DATE;
                    message.obj = serverResponse.getDate();
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void render_category(List<CategoryNameVo> categoryNameVoList){
        adapter = new MyLikeCategoryAdapter(this,categoryNameVoList);
        GridLayoutManager manager = new GridLayoutManager(this,5);
        rv_classify.setLayoutManager(manager);
        rv_classify.setAdapter(adapter);

    }

    private void setUserLikes(String likes){
        OkHttpUtils.get(Const.PORTAL_URI+"user/set/likes?likes="+likes,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    Message message = mHandler.obtainMessage();
                    message.what = USER_LIKES;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case CATEGORY_DATE:
                    List<CategoryNameVo> categoryNameVoList = (List<CategoryNameVo>) msg.obj;
                    render_category(categoryNameVoList);
                    break;
                case USER_LIKES:
                    Toast.makeText(MyLikesActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MyLikesActivity.this,HomeActivity.class);
                    startActivity(intent);
            }
        }
    };

    @Override
    public void onClick(View view) {
        List<Integer> ids = adapter.list();
        if (ids.size() == 0){
            Toast.makeText(this,"请挑选一到三个商品类别吧！",Toast.LENGTH_SHORT).show();
        } else {
            StringBuilder sb = new StringBuilder();
            for (Integer id:ids){
                sb.append(id);
                sb.append(",");
            }
            String likes = sb.substring(0,sb.length()-1);
            setUserLikes(likes);
        }
    }
}