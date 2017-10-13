package com.cnsunway.saas.wash.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.activity.CouponActivity;
import com.cnsunway.saas.wash.activity.LoginActivity;
import com.cnsunway.saas.wash.activity.OrderDetailActivity;

import com.cnsunway.saas.wash.activity.WebActivity;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.model.ActivityMessage;
import com.cnsunway.saas.wash.sharef.UserInfosPref;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/10 0010.
 */

public class NotificationUtil {
    public void notitiy(NotificationManager mNotiManager ,Context context, String ticker, String title,String orderId){
                    UserInfosPref userInfos = UserInfosPref.getInstance(context);
//
//        CharSequence text = context.getText(R.string.app_name);
//        Notification notification = new Notification(R.mipmap.ic_launcher, null,
//                  System.currentTimeMillis());
//            notification.icon = R.mipmap.ic_launcher;
//            notification.tickerText = ticker;
//           title =title;
//           text =title;
//          notification.flags = Notification.FLAG_AUTO_CANCEL;
           Intent pedometerIntent = new Intent();
            if (userInfos.getUser() == null) {
                pedometerIntent.setComponent(new ComponentName(context, LoginActivity.class));
                pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            } else {
                if(!TextUtils.isEmpty(orderId)){
//                    pedometerIntent.setAction("com.cnsunway.saas.wash.OrderDetailActivity2");
//                    Toast.makeText(context,"order id:" +orderId,Toast.LENGTH_SHORT).show();
                    pedometerIntent.putExtra("order_no",orderId);
                    pedometerIntent.setComponent(new ComponentName(context, OrderDetailActivity.class));
                }
            }

//        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(this, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);
//
//        Intent intentCancel = new Intent(this, NotificationBroadcastReceiver.class);
//        intentCancel.setAction("notification_cancelled");
//        intentCancel.putExtra(NotificationBroadcastReceiver.TYPE, type);
//        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, intentCancel, PendingIntent.FLAG_ONE_SHOT);

            PendingIntent contentIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(),
                    pedometerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////            notification.setLatestEventInfo(context, title, text, contentIntent);
         Notification.Builder builder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(ticker)
                .setContentText(title)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.app_logo)
                .setWhen(System.currentTimeMillis()).setDefaults(Notification.DEFAULT_VIBRATE)
//                .setSound()
                .setOngoing(false);
       Notification  notification=builder.getNotification();
        mNotiManager.notify((int)new Date().getTime(), notification);
    }

    public void notitiyActivity(NotificationManager mNotiManager , Context context, ActivityMessage msg){
        UserInfosPref userInfos = UserInfosPref.getInstance(context);
//
//        CharSequence text = context.getText(R.string.app_name);
//        Notification notification = new Notification(R.mipmap.ic_launcher, null,
//                  System.currentTimeMillis());
//            notification.icon = R.mipmap.ic_launcher;
//            notification.tickerText = ticker;
//           title =title;
//           text =title;
//          notification.flags = Notification.FLAG_AUTO_CANCEL;
        Intent pedometerIntent = new Intent();
        if (userInfos.getUser() == null) {
            pedometerIntent.setComponent(new ComponentName(context, LoginActivity.class));
            pedometerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
              if(msg.getType() == 21){
                  pedometerIntent.putExtra("url", Const.Request.balance);
                  pedometerIntent.putExtra("title", "余额明细");
                  pedometerIntent.setComponent(new ComponentName(context, WebActivity.class));
              }else if(msg.getType() == 22){
                  pedometerIntent.setComponent(new ComponentName(context, CouponActivity.class));
              }else if(msg.getType() == 23){

              }
//                    pedometerIntent.setAction("com.cnsunway.saas.wash.OrderDetailActivity2");
//                    Toast.makeText(context,"order id:" +orderId,Toast.LENGTH_SHORT).show();


        }

//        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(this, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);
//
//        Intent intentCancel = new Intent(this, NotificationBroadcastReceiver.class);
//        intentCancel.setAction("notification_cancelled");
//        intentCancel.putExtra(NotificationBroadcastReceiver.TYPE, type);
//        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, intentCancel, PendingIntent.FLAG_ONE_SHOT);

        PendingIntent contentIntent = PendingIntent.getActivity(context, (int)System.currentTimeMillis(),
                pedometerIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////            notification.setLatestEventInfo(context, title, text, contentIntent);
        Notification.Builder builder = new Notification.Builder(context)
                .setAutoCancel(true)
                .setContentTitle(msg.getTicker())
                .setContentText(msg.getText())
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.app_logo)
                .setWhen(System.currentTimeMillis()).setDefaults(Notification.DEFAULT_VIBRATE)
//                .setSound()
                .setOngoing(false);
        Notification  notification=builder.getNotification();
        mNotiManager.notify((int)new Date().getTime(), notification);
    }

}
