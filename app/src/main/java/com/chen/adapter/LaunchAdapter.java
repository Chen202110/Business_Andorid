package com.chen.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.chen.MyLikesActivity;
import com.chen.R;

import java.util.ArrayList;

public class LaunchAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<View> mViewList = new ArrayList<View>();

    public LaunchAdapter(Context context,int[] imageArray){
        this.mContext = context;
        for (int i = 0; i < imageArray.length; i++){
            View view = LayoutInflater.from(context).inflate(R.layout.item_launch,null);
            ImageView mIvLaunch = view.findViewById(R.id.iv_launch);
            RadioGroup mRgIndicate = view.findViewById(R.id.rg_indicate);
            Button mBtnStart = view.findViewById(R.id.btn_start);
            mIvLaunch.setImageResource(imageArray[i]);
            for (int j = 0; j < imageArray.length ; j++) {
                RadioButton mRadio = new RadioButton(mContext);
                mRadio.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mRadio.setButtonDrawable(R.drawable.launch_guide);
                mRadio.setPadding(10, 10, 10, 10);
                mRgIndicate.addView(mRadio);
            }
            ((RadioButton) mRgIndicate.getChildAt(i)).setChecked(true);
            if (i == imageArray.length - 1){
                mBtnStart.setVisibility(View.VISIBLE);
                mBtnStart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(view.getContext(), MyLikesActivity.class);
                        view.getContext().startActivity(intent);
                    }
                });
            }
            mViewList.add(view);
        }
    }
    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    // 从容器中销毁指定位置的页面
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViewList.get(position));
    }

    // 实例化指定位置的页面，并将其添加到容器中
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }
}
