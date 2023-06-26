package com.chen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.chen.adapter.LaunchAdapter;

public class LaunchActivity extends AppCompatActivity {

    private int[] launchImageArray = {R.drawable.guide_bg1,R.drawable.guide_bg2, R.drawable.guide_bg3, R.drawable.guide_bg4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ViewPager mVpLaunch = findViewById(R.id.vp_launch);
        LaunchAdapter adapter = new LaunchAdapter(this,launchImageArray);
        mVpLaunch.setAdapter(adapter);
        mVpLaunch.setCurrentItem(0);
    }
}