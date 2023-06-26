package com.chen.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.ProductDetailActivity;
import com.chen.R;
import com.chen.vo.UserLikesProductVo;
import com.chen.widget.RecyclerExtras;

import java.util.List;

public class UserLikesAdapter extends RecyclerView.Adapter implements RecyclerExtras.OnItemClickListener {
    private Context mContext;
    private View view;
    private List<UserLikesProductVo> userLikesProductVoList;
    public UserLikesAdapter(Context context,List<UserLikesProductVo> userLikesProductVos){
        this.mContext = context;
        this.userLikesProductVoList = userLikesProductVos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_likes,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        holder1.tv_category.setText(userLikesProductVoList.get(position).getCategoryName());
        UserLikesProductAdapter adapter = new UserLikesProductAdapter(view.getContext(),userLikesProductVoList.get(position).getProductListVoList());
        GridLayoutManager manager = new GridLayoutManager(view.getContext(),2);
        holder1.rv_likes_content.setAdapter(adapter);
        holder1.rv_likes_content.setLayoutManager(manager);
        holder1.rv_likes_content.addItemDecoration(new DividerItemDecoration(view.getContext(),DividerItemDecoration.VERTICAL));
        holder1.rv_likes_content.addItemDecoration(new DividerItemDecoration(view.getContext(),DividerItemDecoration.HORIZONTAL));
        adapter.setOnItemClickListener(this);
    }

    @Override
    public int getItemCount() {
        return userLikesProductVoList.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
        intent.putExtra("productId",view.getId());
        view.getContext().startActivity(intent);
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tv_category;
        private RecyclerView rv_likes_content;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_category = itemView.findViewById(R.id.tv_category);
            rv_likes_content = itemView.findViewById(R.id.rv_likes_content);
        }
    }
}
