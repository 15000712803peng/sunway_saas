package com.cnsunway.wash.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cnsunway.wash.R;

/**
 * Created by Administrator on 2015/12/16.
 */
public class StarBarView extends LinearLayout implements View.OnClickListener {
    Context mcontext;
    ImageView star1, star2, star3, star4, star5;
    public int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public StarBarView(Context context) {
        super(context);

    }

    public StarBarView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.star_bar, this);
        initView(context);

    }

    private void initView(Context context) {
        mcontext = context;
        star1 = (ImageView) findViewById(R.id.img_star1);
        star2 = (ImageView) findViewById(R.id.img_star2);
        star3 = (ImageView) findViewById(R.id.img_star3);
        star4 = (ImageView) findViewById(R.id.img_star4);
        star5 = (ImageView) findViewById(R.id.img_star5);
        star1.setImageResource(R.mipmap.star_1);
        star2.setImageResource(R.mipmap.star_1);
        star3.setImageResource(R.mipmap.star_1);
        star4.setImageResource(R.mipmap.star_1);
        star5.setImageResource(R.mipmap.star_1);
        count = 5;

        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == star1) {
            star1.setImageResource(R.mipmap.star_1);
            star2.setImageResource(R.mipmap.star_2);
            star3.setImageResource(R.mipmap.star_2);
            star4.setImageResource(R.mipmap.star_2);
            star5.setImageResource(R.mipmap.star_2);
            count = 1;
        } else if (v == star2) {
            star1.setImageResource(R.mipmap.star_1);
            star2.setImageResource(R.mipmap.star_1);
            star3.setImageResource(R.mipmap.star_2);
            star4.setImageResource(R.mipmap.star_2);
            star5.setImageResource(R.mipmap.star_2);
            count = 2;
        } else if (v == star3) {
            star1.setImageResource(R.mipmap.star_1);
            star2.setImageResource(R.mipmap.star_1);
            star3.setImageResource(R.mipmap.star_1);
            star4.setImageResource(R.mipmap.star_2);
            star5.setImageResource(R.mipmap.star_2);
            count = 3;
        } else if (v == star4) {
            star1.setImageResource(R.mipmap.star_1);
            star2.setImageResource(R.mipmap.star_1);
            star3.setImageResource(R.mipmap.star_1);
            star4.setImageResource(R.mipmap.star_1);
            star5.setImageResource(R.mipmap.star_2);
            count = 4;
        } else if (v == star5) {
            star1.setImageResource(R.mipmap.star_1);
            star2.setImageResource(R.mipmap.star_1);
            star3.setImageResource(R.mipmap.star_1);
            star4.setImageResource(R.mipmap.star_1);
            star5.setImageResource(R.mipmap.star_1);
            count = 5;
        }
    }


}
