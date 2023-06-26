package com.chen.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chen.CategoryNoSubActivity;
import com.chen.CategorySubActivity;
import com.chen.ProductDetailActivity;
import com.chen.R;
import com.chen.adapter.MyCategoryAdapter;
import com.chen.adapter.MyViewPagerAdapter;
import com.chen.adapter.UserLikesAdapter;
import com.chen.bean.CategoryInfo;
import com.chen.common.Const;
import com.chen.utils.OkHttpCallback;
import com.chen.utils.OkHttpCallbackFile;
import com.chen.utils.OkHttpUtils;
import com.chen.vo.CategoryVo;
import com.chen.vo.ProductListVo;
import com.chen.vo.ServerResponse;
import com.chen.vo.UserLikesProductVo;
import com.chen.widget.RecyclerExtras;
import com.chen.widget.SpacesItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

//url okhttp-->byte[] ->BitmapFactory -->Bitmap
public class HomeFragment extends Fragment implements RecyclerExtras.OnItemClickListenerForName,RecyclerExtras.OnItemClickListener {
    private ViewPager vp_carousel;
    private RecyclerView rv_content_HA,rv_content_phone,rv_content_computer,rv_content_costume,rv_content_like;
    GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(),5);
    GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(),5);
    GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(),5);
    GridLayoutManager gridLayoutManager4 = new GridLayoutManager(getActivity(),5);
    private static final String URL_HA = Const.PORTAL_URI+"category/select/sub/家电";
    private static final String URL_PHONE= Const.PORTAL_URI+"category/select/sub/手机";
    private static final String URL_COMPUTER = Const.PORTAL_URI+"category/select/sub/电脑";
    private static final String URL_COSTUME = Const.PORTAL_URI+"category/select/sub/服装";
    private static final int CAROUSEL_NOTIFY = 1;
    private static final int CATEGORY_HA = 3;
    private static final int CATEGORY_PHONE = 4;
    private static final int CATEGORY_COMPUTER = 5;
    private static final int CATEGORY_COSTUME = 6;
    private static final int USER_LIKES_DATE = 7;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        vp_carousel = view.findViewById(R.id.vp_carousel);
        rv_content_HA = view.findViewById(R.id.rv_content_HA);
        rv_content_phone = view.findViewById(R.id.rv_content_phone);
        rv_content_computer = view.findViewById(R.id.rv_content_computer);
        rv_content_costume = view.findViewById(R.id.rv_content_costume);
        rv_content_like = view.findViewById(R.id.rv_content_like);

        OkHttpUtils.get(Const.PORTAL_URI+"product/carousel.do",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {

                //解析数据
                Gson gson = new Gson();
                ServerResponse<List<ProductListVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<ProductListVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    //获取数据
                    List<ProductListVo> productListVos = serverResponse.getDate();
                    Message message = mHandler.obtainMessage();
                    message.what = CAROUSEL_NOTIFY;
                    message.obj = productListVos;
                    mHandler.sendMessage(message);
                }
            }
        });

        getCategoryDate(URL_HA,CATEGORY_HA);
        getCategoryDate(URL_PHONE,CATEGORY_PHONE);
        getCategoryDate(URL_COMPUTER,CATEGORY_COMPUTER);
        getCategoryDate(URL_COSTUME,CATEGORY_COSTUME);
        getUserLikesDate();
        return view;
    }
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case CAROUSEL_NOTIFY:
                    List<ProductListVo> productListVoList = (List<ProductListVo>) msg.obj;
                    //创建view，渲染到viewpager上
                    render_carousel(productListVoList);
                    break;
                case CATEGORY_HA:
                    List<CategoryVo> categoryVoList1 = (List<CategoryVo>) msg.obj;
                    render_categoryInfo(categoryVoList1,gridLayoutManager1,rv_content_HA);
                    break;
                case CATEGORY_PHONE:
                    List<CategoryVo> categoryVoList2 = (List<CategoryVo>) msg.obj;
                    render_categoryInfo(categoryVoList2,gridLayoutManager2,rv_content_phone);
                    break;
                case CATEGORY_COMPUTER:
                    List<CategoryVo> categoryVoList3 = (List<CategoryVo>) msg.obj;
                    render_categoryInfo(categoryVoList3,gridLayoutManager3,rv_content_computer);
                    break;
                case CATEGORY_COSTUME:
                    List<CategoryVo> categoryVoList4 = (List<CategoryVo>) msg.obj;
                    render_categoryInfo(categoryVoList4,gridLayoutManager4,rv_content_costume);
                    break;
                case USER_LIKES_DATE:
                    List<UserLikesProductVo> userLikesProductVoList = (List<UserLikesProductVo>) msg.obj;
                    render_user_likes(userLikesProductVoList);
                    break;
            }
        }
    };

    //渲染轮播图
    private void render_carousel(List<ProductListVo> productListVoList){
        MyViewPagerAdapter adapter = new MyViewPagerAdapter(getActivity(),productListVoList);
        adapter.setOnItemClickListener(this);
        vp_carousel.setAdapter(adapter);
    }

    //渲染recycleView
    private void render_categoryInfo(List<CategoryVo> categoryVoList,GridLayoutManager gridLayoutManager,RecyclerView recyclerView){
        List<CategoryInfo> categoryInfoList = new ArrayList<>();
        for (int i=0;i<categoryVoList.size();i++){
            CategoryVo categoryVo = categoryVoList.get(i);
            String uri = Const.IMAGE_URI+categoryVo.getMainImage();
            String name = categoryVo.getName();
            Integer id = categoryVo.getId();
            CategoryInfo categoryInfo = new CategoryInfo();
            categoryInfo.setName(name);
            categoryInfo.setUrl_pic(uri);
            categoryInfo.setId(id);
            categoryInfoList.add(categoryInfo);
        }
        recyclerView.setLayoutManager(gridLayoutManager);
        MyCategoryAdapter adapter = new MyCategoryAdapter(getActivity(),categoryInfoList);
        adapter.setOnItemClickListenerForName(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
    }

    private void getCategoryDate(String url,int message_obj){
        OkHttpUtils.get(url,new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                super.onFinish(status, msg);
                Gson gson = new Gson();
                ServerResponse<List<CategoryVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<CategoryVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    List<CategoryVo> categoryVoList = serverResponse.getDate();
                    Message message = mHandler.obtainMessage();
                    message.what = message_obj;
                    message.obj = categoryVoList;
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void getUserLikesDate(){
        OkHttpUtils.get(Const.PORTAL_URI+"product/user/likes",new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse<List<UserLikesProductVo>> serverResponse = gson.fromJson(msg,new TypeToken<ServerResponse<List<UserLikesProductVo>>>(){}.getType());
                int status1 = serverResponse.getStatus();
                if (status1 == 0){
                    Message message = mHandler.obtainMessage();
                    message.what = USER_LIKES_DATE;
                    message.obj = serverResponse.getDate();
                    mHandler.sendMessage(message);
                }
            }
        });
    }

    private void render_user_likes(List<UserLikesProductVo> likesProductVoList){
        UserLikesAdapter adapter = new UserLikesAdapter(getContext(),likesProductVoList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rv_content_like.setAdapter(adapter);
        rv_content_like.setLayoutManager(manager);
    }

    @Override
    public void onItemNameClick(View view, int position, String name) {
        OkHttpUtils.get(Const.PORTAL_URI+"category/sub/"+view.getId(),new OkHttpCallback(){
            @Override
            public void onFinish(String status, String msg) {
                Gson gson = new Gson();
                ServerResponse serverResponse = gson.fromJson(msg,ServerResponse.class);
                ArrayList<Object> date = (ArrayList<Object>) serverResponse.getDate();
                if (date.size() == 0){
                    Intent intent = new Intent(getActivity(), CategoryNoSubActivity.class);
                    intent.putExtra("categoryId",view.getId());
                    intent.putExtra("categoryName",name);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), CategorySubActivity.class);
                    intent.putExtra("categoryId",view.getId());
                    intent.putExtra("categoryName",name);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra("productId",view.getId());
        startActivity(intent);
    }
}
