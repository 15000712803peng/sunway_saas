package com.cnsunway.wash.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.framework.utils.VersionUtil;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.umeng.message.PushAgent;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;


/**
 * Created by LL on 2015/12/23.
 */
public class FlashActivity extends BaseActivity {

    UserInfosPref userInfos;
    TextView versionText;
    PushAgent mPushAgent;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        Log.e("----------","sha1:" + sHA1(this));
        Log.e("type:","type1:" + MessageType.ORDER_CANCLE.type + "--type2:" +MessageType.ORDER_PAYED.type);
        userInfos = UserInfosPref.getInstance(this);
        if(TextUtils.isEmpty(userInfos.getUmengToken())){
            mPushAgent = PushAgent.getInstance(this);
            userInfos.setUmengToken(mPushAgent.getRegistrationId());
        }
        versionText = (TextView) findViewById(R.id.text_version);
        versionText.setText(VersionUtil.getAppVersionName(getApplicationContext()));
//        textVersion = (TextView) findViewById(R.id.tv_welcome_version);
//        FontUtil.applyFont(this, textVersion, "OpenSans-Regular.ttf");
//        textVersion.setText("- " + VersionUtil.getAppVersionName(this) + " -");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userInfos.isFirstLogin()) {
                    startActivity(new Intent(FlashActivity.this, GuideActivity.class));
                } else {
//                    startActivity(new Intent(FlashActivity.this, LoginActivity.class));
                    startActivity(new Intent(FlashActivity.this, HomeActivity2.class));
                }
                finish();
            }
        }, 2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        rotateImage.clearAnimation();

    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.e("pixels","width:" +getScreenWidth(this)+" height:" + getScreenHeight(this));
        super.onWindowFocusChanged(hasFocus);
    }

    public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum MessageType{


        ORDER_CANCLE("cancel"),
        ORDER_PAYED("payed");
        private  MessageType(String myType){
            this.type = myType;
        }
        private String type;
    }

}
