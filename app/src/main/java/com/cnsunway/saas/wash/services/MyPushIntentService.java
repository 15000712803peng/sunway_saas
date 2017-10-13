package com.cnsunway.saas.wash.services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cnsunway.saas.wash.model.ActivityMessage;
import com.cnsunway.saas.wash.model.OrderMessage;
import com.cnsunway.saas.wash.util.NotificationUtil;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

/**
 * Developer defined push intent service.
 * Remember to call {@link com.umeng.message.PushAgent#setPushIntentServiceClass(Class)}.
 *
 * @author lucas
 */
//完全自定义处理类
public class MyPushIntentService extends UmengMessageService {
    private static final String TAG = MyPushIntentService.class.getName();
    NotificationManager notificationManager;
    Handler messageHander;
    private final int ORDER_MSG = 1;
    private final int ACTIVITY_MSG = 2;
    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        messageHander = new Handler(getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int what = msg.what;
                if(what == ORDER_MSG){
                    OrderMessage orderMessage = (OrderMessage) msg.obj;
//                    Log.e("---------","order message:" + orderMessage);
                    new NotificationUtil().notitiy(notificationManager,MyPushIntentService.this,orderMessage.getTicker(),orderMessage.getText(),orderMessage.getOrderNo());
                }else if(what == ACTIVITY_MSG){
                    ActivityMessage activityMessage = (ActivityMessage) msg.obj;
                    new NotificationUtil().notitiyActivity(notificationManager,MyPushIntentService.this,activityMessage);
                }
            }
        };
    }

    @Override
    public void onMessage(Context context, Intent intent) {
        try {
            //可以通过MESSAGE_BODY取得消息体
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

//                    Toast.makeText(getApplicationContext(),"on message",Toast.LENGTH_SHORT).show();

                }
            });
            String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
            UMessage msg = new UMessage(new JSONObject(message));

            // code  to handle message here
            Log.e("message","message:" +message);
            String msgType = msg.extra.get("msgType");

            if (msgType!= null){
                //底部导航栏显示小红点
//                Log.e("---------------","msg_type:" + msgType);
                if(msgType.equals("1")|| msgType.equals("21")||message.equals("22")||message.equals("23")||msgType.equals("ORDER_IN_STORE")||msgType.equals("ORDER_WASHING")||msgType.equals("ORDER_ASSIGNED")||msgType.equals("ORDER_CANCELED")){
                    EventBus.getDefault().post("pushSucc");
                    if(msgType.equals("1")||msgType.equals("ORDER_IN_STORE")||msgType.equals("ORDER_WASHING")||msgType.equals("ORDER_ASSIGNED")||msgType.equals("ORDER_CANCELED")){
                        OrderMessage orderMessage = new OrderMessage();
                        orderMessage.setText(msg.extra.get("text"));
                        orderMessage.setTicker(msg.extra.get("ticker"));
//                        if(msg.title.equals("已接单")){
//                            orderMessage.setTicker("您的订单已经被接单，请稍等片刻取送员正在火速赶来~");
//                        }else if(msg.title.equals("支付成功")){
//                            orderMessage.setTicker("我们已经收到您的洗涤费用，开始为您检查衣物，请耐心等待~");
//                        }else if(msg.title.equals("送返中")){
//                            orderMessage.setTicker("您的衣服已洗涤完成，取送员正在送返中~");
//                        }else if(msg.title.equals("已送达")){
//                            orderMessage.setTicker("您的衣服已送达，如有疑问请联系客服：");
//                        }else if(msg.title.equals("已完成")){
//                            orderMessage.setTicker("太棒了您的订单已完成了，您可以对我们的服务质量进行评价，您的反馈是我们不断进步的动力");
//                        }
                        orderMessage.setOrderNo(msg.extra.get("orderNo"));
//                        orderMessage.setText("订单编号：" +msg.extra.get("orderNo") +  "，点击查看详情");
                        //订单消息和活动消息
                        Message notifyMsg = messageHander.obtainMessage(ORDER_MSG,orderMessage);
                        messageHander.sendMessage(notifyMsg);
                    }
                    if (msgType.equals("21")||message.equals("22")||message.equals("23")){
                        //活动消息
                        EventBus.getDefault().post("pushActivitySucc");
                        ActivityMessage activityMessage = new ActivityMessage();
                        if(msgType.equals("21")){
                            //充值
                            activityMessage.setType(21);
                            activityMessage.setTicker(msg.extra.get("ticker"));
                            activityMessage.setText(msg.extra.get("text"));
                            Message notifyMsg = messageHander.obtainMessage(ACTIVITY_MSG,activityMessage);
                            messageHander.sendMessage(notifyMsg);
                        }else if(msgType.equals("22")){
                            //优惠券
                            activityMessage.setType(22);
                            activityMessage.setTicker(msg.extra.get("ticker"));
                            activityMessage.setText(msg.extra.get("text"));
                        }else if(msgType.equals("23")){
                            //新活动
                            activityMessage.setType(23);
                            activityMessage.setTicker(msg.extra.get("ticker"));
                            activityMessage.setText(msg.extra.get("text"));
                        }
                    }
                }


            }
            // ...

            // 对完全自定义消息的处理方式，点击或者忽略
            boolean isClickOrDismissed = true;
            if (isClickOrDismissed) {
                //完全自定义消息的点击统计
                UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
            } else {
                //完全自定义消息的忽略统计
                UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
            }

            // 使用完全自定义消息来开启应用服务进程的示例代码
            // 首先需要设置完全自定义消息处理方式
            // mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
            // code to handle to start/stop service for app process
            JSONObject json = new JSONObject(msg.custom);
            String topic = json.getString("topic");

            if (topic != null && topic.equals("appName:startService")) {
                // 在友盟portal上新建自定义消息，自定义消息文本如下
                //{"topic":"appName:startService"}

            } else if (topic != null && topic.equals("appName:stopService")) {
                // 在友盟portal上新建自定义消息，自定义消息文本如下
                //{"topic":"appName:stopService"}

            }
        } catch (Exception e) {

        }
    }
}
