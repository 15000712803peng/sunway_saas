package com.cnsunway.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.dialog.OperationToast;
import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.framework.net.JsonVolley;
import com.cnsunway.wash.framework.net.StringVolley;
import com.cnsunway.wash.framework.utils.JsonParser;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.model.User;
import com.cnsunway.wash.resp.UpdateUserResp;
import com.cnsunway.wash.sharef.UserInfosPref;

/**
 * Created by hp on 2017/6/5.
 */
public class BindCouponActivity extends InitActivity implements View.OnClickListener {
    TextView userText, bindText,titleText;
    EditText couponNumEdit, couponPawEdit, inviteEdit;
    UserInfosPref userInfosPref;
    String couponNum,couponpaw,userName;
    StringVolley userVolley;
    JsonVolley bindVolley;

    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bind_coupon);
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void initViews() {
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText(R.string.bind_coupon);
        userText = (TextView) findViewById(R.id.tv_user);
        couponNumEdit = (EditText) findViewById(R.id.et_coupon_number);
        couponPawEdit = (EditText) findViewById(R.id.et_coupon_password);
        bindText = (TextView) findViewById(R.id.tv_bind);
        bindText.setOnClickListener(this);
    }
    public void back(View view) {
        finish();
    }
    @Override
    protected void initData() {
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        userVolley = new StringVolley(this, Const.Message.MSG_PROFILE_SUCC, Const.Message.MSG_PROFILE_FAIL);
        userVolley.requestGet(Const.Request.profile, getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
        bindVolley = new JsonVolley(this,Const.Message.MSG_BIND_SUCC, Const.Message.MSG_BIND_FAIL);
    }
    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_PROFILE_SUCC:
                /*if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    userName =  UserInfosPref.getInstance(this).getUserName();
                    userText.setText(userName);
                }*/
                UpdateUserResp respSucc = (UpdateUserResp) JsonParser.jsonToObject(msg.obj + "", UpdateUserResp.class);
                User updateUser = respSucc.getData();
                if(updateUser != null) {
                    UserInfosPref userInfos = UserInfosPref.getInstance(this);
                    userInfos.saveUser(updateUser);

                    if (!TextUtils.isEmpty(updateUser.getMobile())) {
                        userText.setText(updateUser.getMobile());
                        bindVolley.addParams("userName",updateUser.getMobile());
                    }
                }
                break;
            case Const.Message.MSG_BIND_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    showMessageToast(this.getString(R.string.bind_succ));
                    sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_MINE_TABS));
                    finish();
                }else {
                    if(msg.obj == null){
                        showMessageToast(this.getString(R.string.bind_fail));
                    }else {
                        showMessageToast(msg.obj + "");
                    }

                }

                break;
            case Const.Message.MSG_BIND_FAIL:
                showMessageToast(this.getString(R.string.request_fail));
                break;
        }

    }


    protected void showMessageToast(String message) {
        OperationToast.showOperationResult(this.getApplicationContext(),
                message, 0);
    }
    @Override
    public void onClick(View v) {
        if(v == bindText){
            couponNum = couponNumEdit.getText().toString().trim();
            couponpaw = couponPawEdit.getText().toString().trim();

            if (TextUtils.isEmpty(couponNum)){
                Toast.makeText(this, "请输入卡号", Toast.LENGTH_SHORT).show();
                return;
            }else {
                bindVolley.addParams("account",couponNum);
            }
            if (TextUtils.isEmpty(couponpaw)){
                Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }else {
                bindVolley.addParams("pwd",couponpaw);
            }
            LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
            bindVolley.requestPost(Const.Request.bind,this,getHandler(),UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
            );
        }

    }
}
