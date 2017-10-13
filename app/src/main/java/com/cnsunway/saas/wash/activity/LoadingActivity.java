package com.cnsunway.saas.wash.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;


/**
 * Created by LL on 2015/11/17.
 */
public abstract  class LoadingActivity extends InitActivity{

    RelativeLayout loadingParent;
    RelativeLayout netFailParent;
    RelativeLayout noDataParent;
    ImageView loadingImage;
    AnimationDrawable loadingAni;
    TextView noDataText;
    ImageView netfailImage;
    ImageView noDataImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadingParent = (RelativeLayout) findViewById(R.id.rl_loading);
        netFailParent = (RelativeLayout) findViewById(R.id.rl_network_fail);
        noDataParent = (RelativeLayout) findViewById(R.id.rl_no_data);
        loadingImage = (ImageView) findViewById(R.id.iv_loading);
        loadingAni = (AnimationDrawable) loadingImage.getBackground();
        noDataText = (TextView) findViewById(R.id.tv_no_data);
        netfailImage = (ImageView) findViewById(R.id.img_network_fail);
        noDataImage = (ImageView) findViewById(R.id.img_no_data);
        netFailParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshFromParent();
            }
        });
        noDataImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshFromParent();
            }
        });
        super.onCreate(savedInstanceState);

    }



    protected void refreshFromParent(){

    }

    protected  void showCenterLoading(){
        loadingParent.setVisibility(View.VISIBLE);
        netFailParent.setVisibility(View.INVISIBLE);
        noDataParent.setVisibility(View.INVISIBLE);
        loadingAni.start();
    }

    protected  void hideCenterLoading(){
        loadingAni.stop();
        loadingParent.setVisibility(View.INVISIBLE);
    }


    protected  void showNetFail(){
        loadingParent.setVisibility(View.INVISIBLE);
        netFailParent.setVisibility(View.VISIBLE);
        noDataParent.setVisibility(View.INVISIBLE);

    }

    protected  void hideNetFail(){
        netFailParent.setVisibility(View.INVISIBLE);
    }


    protected  void showNoData(int msg){
        loadingParent.setVisibility(View.INVISIBLE);
        netFailParent.setVisibility(View.INVISIBLE);
        noDataParent.setVisibility(View.VISIBLE);
        noDataText.setText(msg);

    }

    protected  void showNoData(String msg){
        loadingParent.setVisibility(View.INVISIBLE);
        netFailParent.setVisibility(View.INVISIBLE);
        noDataParent.setVisibility(View.VISIBLE);
        noDataText.setText(msg);
    }

    protected void hideNoData(){
        noDataParent.setVisibility(View.INVISIBLE);
    }

}
