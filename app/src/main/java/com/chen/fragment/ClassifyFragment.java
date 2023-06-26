package com.chen.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.CategoryNoSubActivity;
import com.chen.ProductDetailActivity;
import com.chen.R;
import com.chen.adapter.MyClassifyAdapter;
import com.chen.adapter.MyClassifyItemAdapter;
import com.chen.adapter.MyClassifySubAdapter;
import com.chen.bean.ClassifyInfo;
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

public class ClassifyFragment extends Fragment implements RecyclerExtras.OnItemClickListenerForMark {
    private RecyclerView rv_classify,rv_classify_sub,rv_classify_item;
    MyClassifyAdapter myClassifyAdapter;
    MyClassifySubAdapter myClassifySubAdapter;
    List<ClassifyInfo> classifyUrlList = ClassifyInfo.getClassifyUrlList();
    private static final int CLASSIFY_HA = 1;
    private static final int CLASSIFY_PHONE = 2;
    private static final int CLASSIFY_COMPUTER = 3;
    private static final int CLASSIFY_COSTUME = 4;
    private static final int NOT_HAVE_DATE = 5;
    private static final int HAVE_DATE = 6;
    private static final int PRODUCT_LIST = 7;
    private static Integer[] listMessageObj = {
            CLASSIFY_HA,CLASSIFY_PHONE,CLASSIFY_COMPUTER,CLASSIFY_COSTUME
    };

    LinearLayoutManager manager1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    LinearLayoutManager manager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    LinearLayoutManager manager3 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
    LinearLayoutManager manager4 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_classify,container,false);
        rv_classify = view.findViewById(R.id.rv_classify);
        rv_classify_sub = view.findViewById(R.id.rv_classify_sub);
        rv_classify_item = view.findViewById(R.id.rv_classify_item);
        init_classify();
        return view;
    }

    private void init_classify(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        myClassifyAdapter = new MyClassifyAdapter(getActivity(), ClassifyInfo.getClassifyNameList());
        rv_classify.setLayoutManager(linearLayoutManager);
        rv_classify.setAdapter(myClassifyAdapter);
        myClassifyAdapter.setOnItemClickListenerForMark(this);
        rv_classify.setItemAnimator(new DefaultItemAnimator());
        rv_classify.addItemDecoration(new SpacesItemDecoration(50));
        rv_classify.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemMarkClick(View view, int position, Integer mark) {
        switch (mark){
            case 1:
                myClassifyAdapter.clearSelection(position);
                myClassifyAdapter.notifyDataSetChanged();
                getClassifyInfoList(position,listMessageObj[position]);
                break;
            case 2:
                myClassifySubAdapter.clearSelection(position);
                myClassifySubAdapter.notifyDataSetChanged();
                OkHttpUtils.get(Const.PORTAL_URI+"product/sub/"+view.getId(),new OkHttpCallback(){
                    @Override
                    public void onFinish(String status, String msg) {
                        Gson gson = new Gson();
                        ServerResponse judge_date = gson.fromJson(msg,ServerResponse.class);
                        ArrayList<Object> date = (ArrayList<Object>) judge_date.getDate();
                        if (date.size() == 0){//没有数据
                            Message message = mHandler.obtainMessage();
                            message.obj = view.getId();
                            message.what = NOT_HAVE_DATE;
                            mHandler.sendMessage(message);
                        } else {//有数据
                            Message message = mHandler.obtainMessage();
                            message.obj = view.getId();
                            message.what = HAVE_DATE;
                            mHandler.sendMessage(message);
                        }
                    }
                });
                break;
            case 3:
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("productId",view.getId());
                startActivity(intent);
        }
        
    }

    public void getClassifyInfoList(int position,int message_obj){
        List<ClassifyInfo> classifyInfoList = new ArrayList<>();
        OkHttpUtils.get(classifyUrlList.get(position).parameter1,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);
                Gson gson = new Gson();
                ServerResponse<List<CategoryVo>> serverResponse = gson.fromJson(msg, new TypeToken<ServerResponse<List<CategoryVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    List<CategoryVo> categoryVoList = serverResponse.getDate();
                    for (int i=0;i<categoryVoList.size();i++){
                        ClassifyInfo classifyInfo = new ClassifyInfo(categoryVoList.get(i).getName(),categoryVoList.get(i).getId());
                        classifyInfoList.add(classifyInfo);
                    }
                    Message message = mHandler.obtainMessage();
                    message.what = message_obj;
                    message.obj = classifyInfoList;
                    mHandler.sendMessage(message);
                }
            }
        });
    }
    private void getDateByClassifyId(int id){
        OkHttpUtils.get(Const.PORTAL_URI+"product/list/"+id,new OkHttpCallback(){
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

    private void getDateByClassifyParentId(int id){
        OkHttpUtils.get(Const.PORTAL_URI+"product/sub/"+id,new OkHttpCallback(){
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

    private void init_classifySub(List<ClassifyInfo> classifyInfoList,LinearLayoutManager manager){
        myClassifySubAdapter = new MyClassifySubAdapter(getActivity(),classifyInfoList);
        myClassifySubAdapter.setOnItemClickListenerForMark(this);
        rv_classify_sub.setLayoutManager(manager);
        rv_classify_sub.setAdapter(myClassifySubAdapter);
        rv_classify_sub.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
    }
    private void init_classifyItem(List<ProductListVo> productListVoList){
        MyClassifyItemAdapter adapter = new MyClassifyItemAdapter(getActivity(),productListVoList);
        adapter.setOnItemClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),1);
        rv_classify_item.setLayoutManager(manager);
        rv_classify_item.setAdapter(adapter);
        rv_classify_item.setItemAnimator(new DefaultItemAnimator());
        rv_classify_item.addItemDecoration(new SpacesItemDecoration(5));
        rv_classify_item.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.HORIZONTAL));
        rv_classify_item.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what){
                case CLASSIFY_HA:
                    List<ClassifyInfo> classifyInfoListHA = (List<ClassifyInfo>) msg.obj;
                    init_classifySub(classifyInfoListHA,manager1);
                    break;
                case CLASSIFY_PHONE:
                    List<ClassifyInfo> classifyInfoListPhone = (List<ClassifyInfo>) msg.obj;
                    init_classifySub(classifyInfoListPhone,manager2);
                    break;
                case CLASSIFY_COMPUTER:
                    List<ClassifyInfo> classifyInfoListComputer = (List<ClassifyInfo>) msg.obj;
                    init_classifySub(classifyInfoListComputer,manager3);
                    break;
                case CLASSIFY_COSTUME:
                    List<ClassifyInfo> classifyInfoListCostume = (List<ClassifyInfo>) msg.obj;
                    init_classifySub(classifyInfoListCostume,manager4);
                    break;
                case NOT_HAVE_DATE:
                    int ClassifyId = (int) msg.obj;
                    getDateByClassifyId(ClassifyId);
                    break;
                case HAVE_DATE:
                    int ClassifyParentId = (int) msg.obj;
                    getDateByClassifyParentId(ClassifyParentId);
                    break;
                case PRODUCT_LIST:
                    List<ProductListVo> productListVoList = (List<ProductListVo>) msg.obj;
                    init_classifyItem(productListVoList);
                    break;
            }
        }
    };
    
}
