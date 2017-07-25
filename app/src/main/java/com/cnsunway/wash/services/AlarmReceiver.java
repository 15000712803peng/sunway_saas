package com.cnsunway.wash.services;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cnsunway.wash.R;
import com.cnsunway.wash.activity.HomeActivity;
import com.cnsunway.wash.activity.LoginActivity;
import com.cnsunway.wash.model.OrderCache;
import com.cnsunway.wash.sharef.UserInfosPref;
import java.util.Date;

/**
 * Created by peter on 15/10/29.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private Context context;
    Notification notification;
    NotificationManager mNotiManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        mNotiManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
//        showNotification(context,intent);
    }

//    protected void showNotification(Context context, Intent intent) {
//        Log.e("-------------", "push: aaaaaaaaaaaaaaaa");
//
//        try {
//            String message = intent.getStringExtra(BaseConstants.MESSAGE_BODY);
////            UMessage msg = new UMessage(new JSONObject(message));
////            com.umeng.common.message.Log.e(TAG, "message=" + message);    //消息体
////            com.umeng.common.message.Log.e(TAG, "custom=" + msg.custom);    //自定义消息的内容
////            com.umeng.common.message.Log.e(TAG, "title=" + msg.title);    //通知标题
////            com.umeng.common.message.Log.e(TAG, "text=" + msg.text);    //通知内容
////            Log.e("---------------", "message:" + msg.ticker);
////            Log.e("---------------", "message:" + msg.title);
////            Log.e("---------------", "message:" + msg.text);
//            String text = intent.getStringExtra("message");
//            String title = intent.getStringExtra("title");
//            String ticker = intent.getStringExtra("ticker");
//            String orderNo = intent.getStringExtra("orderNo");
//            //order状态如果已经过了washout，不再提示
//            if(orderNo != null) {
//                OrderCache cache;
//                cache = OrderCache.get(orderNo);
//                if (cache != null && cache.isNotifyed) {
//                    return;
//                }
//            }
//
//            UserInfosPref userInfos = UserInfosPref.getInstance(context);
//
////            CharSequence text = getText(R.string.app_name);
////            String title, text;
//            notification = new Notification(R.mipmap.ic_launcher, null,
//                    System.currentTimeMillis());
//
//            notification.icon = R.mipmap.ic_launcher;
//            notification.tickerText = ticker;
////            title = msg.title;
////            text = msg.text;
//            notification.flags = Notification.FLAG_AUTO_CANCEL;
//
//            Intent pedometerIntent = new Intent();
//            if (userInfos.getUser() == null) {
//                pedometerIntent.setComponent(new ComponentName(context, LoginActivity.class));
//            } else {
//                pedometerIntent.setComponent(new ComponentName(context, HomeActivity.class));
//            }
//            pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//                    pedometerIntent, 0);
////            notification.setLatestEventInfo(context, title, text, contentIntent);
//            mNotiManager.notify((int)new Date().getTime(), notification);
//
//            if(orderNo != null) {
//                OrderCache cache;
//                cache = OrderCache.get(orderNo);
//                if (cache != null && !cache.isNotifyed) {
//                    cache.isNotifyed = true;
//                    cache.save();
//                    return;
//                }
//            }
//        } catch (Exception e) {
//        }
//    }

}
