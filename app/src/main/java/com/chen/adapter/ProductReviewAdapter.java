package com.chen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chen.R;
import com.chen.vo.ProductReviewVo;

import java.util.List;

public class ProductReviewAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private View view;
    private List<ProductReviewVo> productReviewVoList;
    public ProductReviewAdapter(Context context,List<ProductReviewVo> productReviewVos){
        this.mContext = context;
        this.productReviewVoList = productReviewVos;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item_product_review,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder holder1 = (ItemHolder) holder;
        Integer role = productReviewVoList.get(position).getRole();
        holder1.tv_username.setText(productReviewVoList.get(position).getUserName());
        if (role == 1){
            holder1.tv_username.setTextColor(view.getContext().getColor(R.color.black));
            holder1.tv_manager.setVisibility(View.GONE);
        } else {
            holder1.tv_username.setTextColor(view.getContext().getColor(R.color.red));
        }
        holder1.tv_send_time.setText(productReviewVoList.get(position).getCreateTime());
        holder1.tv_review.setText(productReviewVoList.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return productReviewVoList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView tv_username,tv_send_time,tv_review,tv_manager;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv_review = itemView.findViewById(R.id.tv_review);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_send_time = itemView.findViewById(R.id.tv_send_time);
            tv_manager = itemView.findViewById(R.id.tv_manager);
        }
    }
}
