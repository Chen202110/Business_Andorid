package com.chen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.chen.common.Const;
import com.chen.vo.ProductListVo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class MyViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<ProductListVo> productListVoList;

    public MyViewPagerAdapter(Context context,List<ProductListVo> productListVos){
        this.mContext = context;
        this.productListVoList = productListVos;
    }

    @Override
    public int getCount() {
        return productListVoList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setId(productListVoList.get(position).getId());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view,position);
                }
            }
        });
        Glide.with(mContext).load(Const.IMAGE_URI+productListVoList.get(position).getBannerImage()).into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(container);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return productListVoList.get(position).getName();
    }

    private RecyclerExtras.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(RecyclerExtras.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
}
