package com.chen.widget;

import android.view.View;

import com.chen.bean.CategoryInfo;

import java.util.List;

public class RecyclerExtras {

    // 定义一个循环视图列表项的点击监听器接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // 定义一个循环视图列表项含name的点击监听器接口
    public interface OnItemClickListenerForName {
        void onItemNameClick(View view, int position, String name);
    }

    // 定义一个循环视图列表项含标记的点击监听器接口
    public interface OnItemClickListenerForMark {
        void onItemMarkClick(View view, int position, Integer mark);
    }

    // 定义一个循环视图列表项的长按监听器接口
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    // 定义一个循环视图列表项的删除监听器接口
    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(View view, int position);
    }

    // 定义一个循环视图列表项含count的点击监听器接口
    public interface OnItemClickListenerForCount {
        void onItemCountClick(View view, int position, int count);
    }
    // 定义一个循环视图列表项含name和Mark的点击监听器接口
    public interface OnItemClickListenerForNameAndMark {
        void onItemNameAndMarkClick(View view, int position, String name, int mark);
    }
}
