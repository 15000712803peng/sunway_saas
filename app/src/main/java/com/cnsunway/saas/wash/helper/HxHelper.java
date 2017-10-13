package com.cnsunway.saas.wash.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.cnsunway.saas.wash.activity.ChatActivity;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.sharef.Preferences;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.hyphenate.chat.ChatClient;
import com.hyphenate.chat.Conversation;
import com.hyphenate.helpdesk.callback.Callback;
import com.hyphenate.helpdesk.easeui.Constant;
import com.hyphenate.helpdesk.easeui.util.IntentBuilder;

/**
 * Created by Administrator on 2017/8/7 0007.
 */

public class HxHelper {

    static HxHelper instance;
    Activity activity;
    private ProgressDialog progressDialog;
    private HxHelper(Activity activity){
        this.activity = activity;
    }

    public static HxHelper getInstance(Activity activity){
        if(instance == null){
            instance = new HxHelper(activity);
        }
        return instance;
    }

    public void startChat(int index,Activity parent){

    }

    public void safeToChae(int index,Activity activity){
        if (!ChatClient.getInstance().isLoggedInBefore()){
            login(UserInfosPref.getInstance(activity).getUser().getMobile(),UserInfosPref.getInstance(activity).getUser().getHxPwd());
        }else {
            toChat(index,activity);
        }
    }

    public void toChat(int index,Activity parent){
        String queueName = "";
        switch (index){
            case Constant.MESSAGE_TO_AFTER_SALES:
                queueName = "shouhou";
                break;
            case Constant.MESSAGE_TO_PRE_SALES:
                queueName = "shouqian";
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.INTENT_CODE_IMG_SELECTED_KEY, 0);
        //设置点击通知栏跳转事件
        Conversation conversation = ChatClient.getInstance().chatManager().getConversation(Preferences.getInstance().getCustomerAccount());
        String titleName = null;
        if (conversation.getOfficialAccount() != null){
            titleName = conversation.getOfficialAccount().getName();
        }
        // 进入主页面
        Intent intent = new IntentBuilder(parent)
                .setTargetClass(ChatActivity.class)
                .setVisitorInfo(MessageHelper.createVisitorInfo(parent))
                .setServiceIMNumber(Preferences.getInstance().getCustomerAccount())
                .setScheduleQueue(MessageHelper.createQueueIdentity(queueName))
                .setTitleName(titleName)
//						.setScheduleAgent(MessageHelper.createAgentIdentity("ceshiok1@qq.com"))
                .setShowUserNick(true)
                .setBundle(bundle)
                .build();
        parent.startActivity(intent);
    }

    public void toChat(int index,Activity parent,String orderId){
        String queueName = "";
        switch (index){
            case Constant.MESSAGE_TO_AFTER_SALES:
                queueName = "shouhou";
                break;
            case Constant.MESSAGE_TO_PRE_SALES:
                queueName = "shouqian";
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.INTENT_CODE_IMG_SELECTED_KEY, 0);

        //设置点击通知栏跳转事件
        Conversation conversation = ChatClient.getInstance().chatManager().getConversation(Preferences.getInstance().getCustomerAccount());
        String titleName = null;
        if (conversation.getOfficialAccount() != null){
            titleName = conversation.getOfficialAccount().getName();
        }
        // 进入主页面
        Intent intent = new IntentBuilder(parent)
                .setTargetClass(ChatActivity.class)
                .setVisitorInfo(MessageHelper.createVisitorInfo(parent))
                .setServiceIMNumber(Preferences.getInstance().getCustomerAccount())
                .setScheduleQueue(MessageHelper.createQueueIdentity(queueName))
                .setTitleName(titleName)
//						.setScheduleAgent(MessageHelper.createAgentIdentity("ceshiok1@qq.com"))
                .setShowUserNick(true)
                .setBundle(bundle)
                .build();
        intent.putExtra("order_id",orderId);
        parent.startActivity(intent);
    }

    public void login(final String account, final String userPwd){

        if(TextUtils.isEmpty(account) || TextUtils.isEmpty(userPwd)){
            return;
        }
        if (!ChatClient.getInstance().isLoggedInBefore()){
            ChatClient.getInstance().login(account, userPwd, new Callback() {
                @Override
                public void onSuccess() {
                    if(progressDialog != null && progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }

                    toChat(Const.MESSAGE_TO_DEFAULT,activity);
                }

                @Override
                public void onError(int i, String s) {
                    if(progressDialog != null && progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
//            ChatClient.getInstance().createAccount(account, userPwd, new Callback() {
//                @Override
//                public void onSuccess() {
//
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //登录环信服务器
//                            if(progressDialog != null && progressDialog.isShowing()){
//                                progressDialog.dismiss();
//                            }
//                            ChatClient.getInstance().login(account, userPwd, new Callback() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onError(int i, String s) {
//
//                                }
//
//                                @Override
//                                public void onProgress(int i, String s) {
//
//                                }
//                            });
//                        }
//                    });
//                }
//                @Override
//                public void onError(final int errorCode, String error) {
//
//                    activity.runOnUiThread(new Runnable() {
//                        public void run() {
//                            if(progressDialog != null && progressDialog.isShowing()){
//                                progressDialog.dismiss();
//                            }
//                            if (errorCode == Error.NETWORK_ERROR){
//                                Toast.makeText(activity, R.string.network_unavailable, Toast.LENGTH_SHORT).show();
//                            }else if (errorCode == Error.USER_ALREADY_EXIST){
////                                Toast.makeText(activity, R.string.user_already_exists, Toast.LENGTH_SHORT).show();
////                              toChatActivity();
//                                ChatClient.getInstance().login(account, userPwd, new Callback() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//
//                                    @Override
//                                    public void onError(int i, String s) {
//
//                                    }
//
//                                    @Override
//                                    public void onProgress(int i, String s) {
//
//                                    }
//                                });
//                            }else if(errorCode == Error.USER_AUTHENTICATION_FAILED){
//                                Toast.makeText(activity, R.string.no_register_authority, Toast.LENGTH_SHORT).show();
//                            } else if (errorCode == Error.USER_ILLEGAL_ARGUMENT){
//                                Toast.makeText(activity, R.string.illegal_user_name, Toast.LENGTH_SHORT).show();
//                            }else {
//                                Toast.makeText(activity, "连接失败", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//                }
//                @Override
//                public void onProgress(int progress, String status) {
//
//                }
//            });
        }

    }
    public void login(final String account, final String userPwd,boolean init){
        if(TextUtils.isEmpty(account) || TextUtils.isEmpty(userPwd)){
            return;
        }
        if (!ChatClient.getInstance().isLoggedInBefore()){

            ChatClient.getInstance().login(account, userPwd, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
//            ChatClient.getInstance().createAccount(account, userPwd, new Callback() {
//                @Override
//                public void onSuccess() {
//
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //登录环信服务器
//
//                            ChatClient.getInstance().login(account, userPwd, new Callback() {
//                                @Override
//                                public void onSuccess() {
//                                }
//
//                                @Override
//                                public void onError(int i, String s) {
//
//                                }
//
//                                @Override
//                                public void onProgress(int i, String s) {
//
//                                }
//                            });
//                        }
//                    });
//                }

        }else {

        }

    }


}
