package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.net.StringVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.framework.utils.PhoneCheck;
import com.cnsunway.saas.wash.helper.ApkUpgradeHelper2;
import com.cnsunway.saas.wash.helper.HxHelper;
import com.cnsunway.saas.wash.helper.UmengEventHelper;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.resp.GetCodeResp;
import com.cnsunway.saas.wash.resp.LoginResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.EditTextUtils;
import com.cnsunway.saas.wash.util.FontUtil;
import com.cnsunway.saas.wash.util.IEditTextChangeListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/3/21.
 */
public class LoginActivity extends InitActivity implements View.OnClickListener {

    String phoneNum, userName, codes, inviteCode;
    TextView getCodeText, countText;
    TextView permission;
    EditText accountEdit, codesEdit, inviteEdit;
    RelativeLayout loginParent;
    Button btnLogin;
    StringVolley codeVolley, loginVolley;
    String deviceToken;
    UserInfosPref userInfos;
    private int timeCount = 60;
    ApkUpgradeHelper2 updateHelper;
    LinearLayout layoutLogin;
    RelativeLayout securityCodeRl;
    PushAgent mPushAgent;
    Boolean isLegal;
    boolean isDoOrder = false;
    int tab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        FontUtil.applyFont(this, layoutLogin, "OpenSans-Regular.ttf");
        isDoOrder = getIntent().getBooleanExtra("do_order",false);
        tab = getIntent().getIntExtra("tab",-1);

//        if (UserInfosPref.getInstance(this).isUpdateCheck()) {
//            updateHelper = new ApkUpgradeHelper2(this);
//            updateHelper.check(false);
//        }
        mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.enable(new IUmengRegisterCallback() {
//            @Override
//            public void onRegistered(String s) {
//                deviceToken = s;
//                userInfos.setUmengToken(deviceToken);
//            }
//        });
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {
                deviceToken = s;
                userInfos.setUmengToken(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e("onFailure","on failure:" + "s:" + s + "  s1:" +s1);
            }
        });
        PushAgent.getInstance(this).onAppStart();
//        startService(new Intent(this, MyPushIntentService.class));
        deviceToken = userInfos.getUmengToken();
        if (TextUtils.isEmpty(deviceToken)) {
            deviceToken = mPushAgent.getRegistrationId();
            getDeviceToken();
        }

//        if (LoginManager.get(this).isLogined()) {
//
//            Intent intent = new Intent(this, HomeActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//
//        }
        //editText监听
        EditTextUtils.ButtonChangeListener textChangeListener = new EditTextUtils.ButtonChangeListener(btnLogin);
        textChangeListener.addAllEditText(accountEdit,codesEdit);
        EditTextUtils.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {

            }

            @Override
            public void lengthChange(boolean hasSpecificLength) {
                if(hasSpecificLength){
//                    btnLogin.setBackgroundResource(R.color.order_wash);
                    loginParent.setBackgroundResource(R.mipmap.icon_login);
                }else{
//                    btnLogin.setBackgroundResource(R.color.order_wash2);
                    loginParent.setBackgroundResource(R.mipmap.icon_login_default);
                }
            }
        });
    }

    public void back(View view){
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
//        super.onBackPressed();
    }

    private void getDeviceToken() {
        if (TextUtils.isEmpty(deviceToken)) {
            getHandler().obtainMessage(Const.Message.MSG_GET_DEVICE_TOKEN)
                    .sendToTarget();
        } else {
            userInfos.setUmengToken(deviceToken);
        }
    }

    @Override
    protected void initData() {
        userInfos = UserInfosPref.getInstance(this);
        userInfos.setFirstLogin(false);
        codeVolley = new StringVolley(this, Const.Message.MSG_GET_CODE_SUCC, Const.Message.MSG_GET_CODE_FAIL);
        loginVolley = new StringVolley(this, Const.Message.MSG_LOGIN_SUCC, Const.Message.MSG_LOGIN_FAIL);
    }

    @Override
    protected void initViews() {
        layoutLogin = (LinearLayout) findViewById(R.id.ll_login);
        accountEdit = (EditText) findViewById(R.id.et_input_phone);
        codesEdit = (EditText) findViewById(R.id.et_input_codes);
        inviteEdit = (EditText) findViewById(R.id.et_input_invite_code);
        btnLogin = (Button) findViewById(R.id.btn_login);
        permission = (TextView) findViewById(R.id.tv_login_permission);
        securityCodeRl = (RelativeLayout)findViewById(R.id.rl_security_code);
        getCodeText = (TextView) findViewById(R.id.tv_get_code);
        getCodeText.setOnClickListener(this);
        countText = (TextView) findViewById(R.id.tv_count);
        loginParent = (RelativeLayout) findViewById(R.id.login_parent);
        btnLogin.setOnClickListener(this);
        permission.setOnClickListener(this);
    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_GET_CODE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    GetCodeResp resp = (GetCodeResp) JsonParser.jsonToObject(msg.obj + "", GetCodeResp.class);
                    timeCount = 60;
                    accountEdit.setEnabled(false);
                    getCodeText.setEnabled(false);
                    getCodeText.setVisibility(View.INVISIBLE);
                    countText.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.get_code_succ_prompt,
                            Toast.LENGTH_LONG).show();
                    startRef();
                } else {
                    OperationToast.showOperationResult(this, msg.obj + "", R.mipmap.wrong_icon);
                }
                break;

            case Const.Message.MSG_GET_CODE_FAIL:
                OperationToast.showOperationResult(this, R.string.request_fail,R.mipmap.wrong_icon);
                break;

            case Const.Message.MSG_SECEND_COUNT:
                if (msg.arg1 == 0) {
                    if (timer != null) {
                        timer.cancel();
                        accountEdit.setEnabled(true);
                        getCodeText.setEnabled(true);
                        securityCodeRl.setBackgroundResource(R.drawable.frame_get_codes);
                        getCodeText.setVisibility(View.VISIBLE);
                        countText.setVisibility(View.INVISIBLE);
                    }
                } else {
                    countText.setText(msg.arg1 + "s");
                }

                break;

            case Const.Message.MSG_LOGIN_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    LoginResp resp = (LoginResp) JsonParser.jsonToObject(msg.obj + "", LoginResp.class);
                    userInfos.saveUser(null);
                    userInfos.saveUser(resp.getData());
                    phoneNum = accountEdit.getText().toString().trim();
                    MobclickAgent.onProfileSignIn("Android_App",phoneNum);
                        userInfos.saveUserName(phoneNum);
                        HxHelper.getInstance(this).login(resp.getData().getMobile(),resp.getData().getHxPwd(),true);
                        UmengEventHelper.loginEvent(LoginActivity.this,phoneNum);
                        if(isDoOrder){
                            Intent intent = new Intent(this, DoOrderActivity.class);
                            startActivity(intent);
                            setResult(RESULT_OK);
                            finish();
                    }else {
                        Intent intent = new Intent(this, HomeActivity2.class);
                        intent.putExtra("tab",tab);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        Intent data = new Intent();
                        setResult(RESULT_CANCELED,data);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    OperationToast.showOperationResult(this, msg.obj + "", R.mipmap.wrong_icon);
                }
                break;

            case Const.Message.MSG_LOGIN_FAIL:
                OperationToast.showOperationResult(this, R.string.request_fail,R.mipmap.wrong_icon);
                break;

            case Const.Message.MSG_GET_DEVICE_TOKEN:
                deviceToken = mPushAgent.getRegistrationId();
                if (TextUtils.isEmpty(deviceToken)) {
                    getHandler().sendMessageDelayed(
                            getHandler().obtainMessage(
                                    Const.Message.MSG_GET_DEVICE_TOKEN), 500);
                } else {
                    userInfos.setUmengToken(deviceToken);
                }
                break;
        }

    }


    @Override
    public void onClick(View view) {
        if (view == getCodeText) {
            phoneNum = accountEdit.getText().toString().trim();
            if (TextUtils.isEmpty(phoneNum)) {
                OperationToast.showOperationResult(this, R.string.hint_no_phonenum,R.mipmap.reminder_icon);

                return;
            }

             isLegal = PhoneCheck.check(phoneNum);
                if (!isLegal) {
                    OperationToast.showOperationResult(this, R.string.illegal_phonenum,R.mipmap.wrong_icon);
            } else {
                securityCodeRl.setBackgroundResource(R.drawable.frame_get_codes2);
                userInfos.saveUserName(phoneNum);
                codeVolley.addParams("mobile", phoneNum);
                setOperationMsg(getString(R.string.get_check_code));
                    LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
                codeVolley.requestPost(Const.Request.code, getHandler(), this, "",locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
                );
            }

        } else if (view == btnLogin) {
            login();
        } else if (view == permission) {
            Intent intent = new Intent(LoginActivity.this,WebActivity.class);
            intent.putExtra("url",Const.Request.agreement);
            intent.putExtra("title","用户协议");
            startActivity(intent);
        }
    }

    private void login() {
        Log.e("--------------", "deviceToken: " + deviceToken);

        if (TextUtils.isEmpty(deviceToken)) {
            showMessageToast(getResources().getString(R.string.wait_data));
            return;
        }

        userName = accountEdit.getText().toString().trim();
        codes = codesEdit.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
//            showImageToast(getResources().getString(
//                    R.string.edit_username),R.mipmap.app_logo);
            showImageToast(getResources().getString(
                   R.string.edit_username),R.mipmap.reminder_icon);
            return;
        } else {
            isLegal = PhoneCheck.check(userName);
            if (!isLegal) {
                OperationToast.showOperationResult(this, R.string.illegal_phonenum,R.mipmap.wrong_icon);
            }
        }

        if (TextUtils.isEmpty(codes)) {
            showImageToast(getResources().getString(
                    R.string.edit_code),R.mipmap.reminder_icon);
            return;
        }

        inviteCode = inviteEdit.getText().toString().trim();
        loginVolley.addParams("mobile", userName);
        loginVolley.addParams("checkCode", codes);
        loginVolley.addParams("deviceToken", deviceToken);
        if (!TextUtils.isEmpty(inviteCode)) {
            loginVolley.addParams("promoterCode", inviteCode);
        }
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();

        setOperationMsg(getResources().getString(R.string.loading));
        loginVolley.requestPost(Const.Request.login, getHandler(), this, "",locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());

    }

    private void startRef() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = getHandler().obtainMessage();
                msg.what = Const.Message.MSG_SECEND_COUNT;
                msg.arg1 = timeCount--;
                msg.sendToTarget();
            }
        }, 0 * 1000, 1000);
    }

    private Timer timer;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
