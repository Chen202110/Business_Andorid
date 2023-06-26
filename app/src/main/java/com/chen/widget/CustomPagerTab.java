package com.chen.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerTabStrip;

import com.chen.R;
import com.chen.utils.Utils;

public class CustomPagerTab extends PagerTabStrip {
    private int textColor = Color.BLACK;
    private int textSize = 15;
    public CustomPagerTab(@NonNull Context context) {
        super(context);
    }

    public CustomPagerTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null){
            TypedArray attrArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomPagerTab);
            textColor = attrArray.getColor(R.styleable.CustomPagerTab_textColor,textColor);
            textSize = Utils.px2sp(context,attrArray.getDimension(R.styleable.CustomPagerTab_textSize,textSize));
            int customBackground = attrArray.getResourceId(R.styleable.CustomPagerTab_customBackground,0);
            int customOrientation = attrArray.getResourceId(R.styleable.CustomPagerTab_customOrientation,0);
            int customGravity = attrArray.getResourceId(R.styleable.CustomPagerTab_customGravity,0);
            attrArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        setTextColor(textColor);
        setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        super.onDraw(canvas);
    }
}
