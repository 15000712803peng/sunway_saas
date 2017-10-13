package com.cnsunway.saas.wash.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.view.pagerindicator.LinePageIndicator;

import java.util.ArrayList;

public class GuideActivity extends InitActivity implements View.OnClickListener {


    ViewPager guidPager;
    LinePageIndicator linePageIndicator;
    TextView enterText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_guide);
        super.onCreate(savedInstanceState);
        UserInfosPref.getInstance(this).setFirstLogin(false);
        enterText = (TextView) findViewById(R.id.text_enter);
        enterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuideActivity.this,HomeActivity2.class));
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        View guide1 = getLayoutInflater().inflate(R.layout.guide_item,null);
        RelativeLayout guide1Parent = (RelativeLayout) guide1.findViewById(R.id.ll_guide);
        guide1Parent.setBackgroundResource(R.mipmap.guide1);
        View guide2 = getLayoutInflater().inflate(R.layout.guide_item,null);
        RelativeLayout guide1Parent2 = (RelativeLayout) guide2.findViewById(R.id.ll_guide);
        guide1Parent2.setBackgroundResource(R.mipmap.guide2);
        View guide3 = getLayoutInflater().inflate(R.layout.guide_item,null);
        RelativeLayout guide1Parent3 = (RelativeLayout) guide3.findViewById(R.id.ll_guide);
        guide1Parent3.setBackgroundResource(R.mipmap.guide3);
        ImageView enterText = (ImageView) guide3.findViewById(R.id.text_experience);
        enterText.setVisibility(View.VISIBLE);
        enterText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GuideActivity.this,HomeActivity2.class));
                finish();
            }
        });
        ArrayList<View> guideViews = new ArrayList<>();
        guideViews.add(guide1);
        guideViews.add(guide2);
        guideViews.add(guide3);
        guidPager = (ViewPager) findViewById(R.id.guide_pager);
        guidPager.setAdapter(new GuideAdapter(this,guideViews));
        linePageIndicator = (LinePageIndicator) findViewById(R.id.indicator);

        linePageIndicator.setViewPager(guidPager);
        guidPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    linePageIndicator.setSelectedColor(Color.parseColor("#111D2D"));
                }else if(position == 1){
                    linePageIndicator.setSelectedColor(Color.parseColor("#20B1D9"));
                }else if(position == 2){
                    linePageIndicator.setSelectedColor(Color.parseColor("#F8451B"));
                }
                linePageIndicator.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public class GuideAdapter extends PagerAdapter {
        private ArrayList<View> mListViews = null;

        public GuideAdapter(Context context, ArrayList<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            View v = mListViews.get(position);
            ((ViewPager) container).addView(v);
            return v;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    @Override
    protected void handlerMessage(Message msg) {

    }
}
