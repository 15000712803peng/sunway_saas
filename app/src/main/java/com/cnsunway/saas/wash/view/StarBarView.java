package com.cnsunway.saas.wash.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cnsunway.saas.wash.R;

/**
 * Created by Administrator on 2015/12/16.
 */
public class StarBarView extends LinearLayout implements View.OnClickListener {
    Context mcontext;
    ImageView star1, star2, star3, star4, star5;
    public int count;
    boolean s1 = false;
    boolean s2 = false;
    boolean s3 = false;
    boolean s4 = false;
    boolean s5 = false;


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

    private void reset(ImageView imageView){
        if(imageView == star1){
            star1.setImageResource(R.mipmap.star_2);
            count = 0;
            s1 = false;
        }else if(imageView == star2){
            star2.setImageResource(R.mipmap.star_2);
            count = 1;
            s2 = false;
        }else if(imageView == star3){
            star3.setImageResource(R.mipmap.star_2);
            count = 2;
            s3 = false;
        }else if(imageView == star4){
            star4.setImageResource(R.mipmap.star_2);
            count = 3;
            s4 = false;
        }else if(imageView == star5){
            star5.setImageResource(R.mipmap.star_2);
            count = 4;
            s5 = false;
        }

    }
    @Override
    public void onClick(View v) {
        if (v == star1) {
            if(s1){
                if(!s2)
                    reset(star1);
            }else {
                star1.setImageResource(R.mipmap.star_1);
                star2.setImageResource(R.mipmap.star_2);
                star3.setImageResource(R.mipmap.star_2);
                star4.setImageResource(R.mipmap.star_2);
                star5.setImageResource(R.mipmap.star_2);
                count = 1;
                s1 = true;
            }

        } else if (v == star2) {
            if(s2){
                if(!s3)
                    reset(star2);
            }else {
                star1.setImageResource(R.mipmap.star_1);
                star2.setImageResource(R.mipmap.star_1);
                star3.setImageResource(R.mipmap.star_2);
                star4.setImageResource(R.mipmap.star_2);
                star5.setImageResource(R.mipmap.star_2);
                count = 2;
                s2 = true;
            }

        } else if (v == star3) {
            if(s3){
                if(!s4)
                    reset(star3);
            }else {
                star1.setImageResource(R.mipmap.star_1);
                star2.setImageResource(R.mipmap.star_1);
                star3.setImageResource(R.mipmap.star_1);
                star4.setImageResource(R.mipmap.star_2);
                star5.setImageResource(R.mipmap.star_2);
                count = 3;
                s3 = true;
            }

        } else if (v == star4) {
            if(s4){
                if(!s5)
                    reset(star4);
            }else {
                star1.setImageResource(R.mipmap.star_1);
                star2.setImageResource(R.mipmap.star_1);
                star3.setImageResource(R.mipmap.star_1);
                star4.setImageResource(R.mipmap.star_1);
                star5.setImageResource(R.mipmap.star_2);
                count = 4;
                s4 = true;
            }

        } else if (v == star5) {
            if(s5){
                reset(star5);
            }else {
                star1.setImageResource(R.mipmap.star_1);
                star2.setImageResource(R.mipmap.star_1);
                star3.setImageResource(R.mipmap.star_1);
                star4.setImageResource(R.mipmap.star_1);
                star5.setImageResource(R.mipmap.star_1);
                count = 5;
                s5 = true;
            }

        }
    }


}
