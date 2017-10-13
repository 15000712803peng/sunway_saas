package com.cnsunway.saas.wash.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String TYPE = "type";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int type = intent.getIntExtra(TYPE, -1);
        Toast.makeText(context,"notification receiver",Toast.LENGTH_LONG).show();
        if (action.equals("notification_clicked")) {
            //处理点击事件
        }
        
        if (action.equals("notification_cancelled")) {
            //处理滑动清除和点击删除事件
            if (type != -1) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(type);
            }
        }
    }
}