package com.chen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.R;
import com.chen.vo.CategoryNameVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyLikeCategoryAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private View view;
    private List<CategoryNameVo> categoryNameVoList;
    private HashMap<String,Integer> map = new HashMap<>();

    public MyLikeCategoryAdapter(Context context,List<CategoryNameVo> categoryNameVos) {
        this.mContext = context;
        this.categoryNameVoList = categoryNameVos;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_like_category,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        holder1.tv_category.setText(categoryNameVoList.get(position).getName());
        holder1.cb_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b && map.size() < 3){
                    map.put(categoryNameVoList.get(position).getName(),categoryNameVoList.get(position).getId());
                } else if (!b && map.size() > 0){
                    map.remove(categoryNameVoList.get(position).getName());
                } else {
                    holder1.cb_check.setChecked(false);
                    Toast.makeText(view.getContext(),"最多选择三个商品类别！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<Integer> list(){
        List<Integer> list = new ArrayList<>();
        for (Integer integer:map.values()){
            list.add(integer);
        }
        return list;
    }

    @Override
    public int getItemCount() {
        return categoryNameVoList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private CheckBox cb_check;
        private TextView tv_category;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            cb_check = itemView.findViewById(R.id.cb_check);
            tv_category = itemView.findViewById(R.id.tv_category);
        }
    }
}
