package com.chen.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;// 空白间隔

    public SpacesItemDecoration(int space){this.space = space;}

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = space; // 左边空白间隔
        outRect.right = space; // 右边空白间隔
        outRect.bottom = space; // 上方空白间隔
        outRect.top = space; // 下方空白间隔
    }
}
