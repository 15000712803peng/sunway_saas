package com.cnsunway.saas.wash.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cnsunway.saas.wash.activity.WebActivity;
import com.cnsunway.saas.wash.dialog.WayOfShareDialog;
import com.cnsunway.saas.wash.model.Order;

/**
 * Created by LL on 2016/3/29.
 */
public class ShareUtil {

    WayOfShareDialog wayOfShareDialog;
    public ShareUtil(WayOfShareDialog wayOfShareDialog){
        this.wayOfShareDialog = wayOfShareDialog;
    }

    public  void share(final Context context,Order order){
        String action = order.getAction();
       if(TextUtils.isEmpty(action)){
           return;
       }
        if(action.equals("share")){
            wayOfShareDialog.setShareInfo(order.getShareInfo());
            wayOfShareDialog.show();
            }else if(action.equals("direct")){
                final Intent intent = new Intent(context,WebActivity.class);
                intent.putExtra("url",order.getDirectInfo().getDirectUrl());
                intent.putExtra("title",order.getDirectInfo().getDirectTitle());
                context.startActivity(intent);
            }
        }

    public  void share(final Context context,Order order,String key){
        String action = order.getAction();
        if(TextUtils.isEmpty(action)){
            return;
        }
        if(action.equals("share")){
            wayOfShareDialog.setShareInfo(order.getShareInfo(),key);
            wayOfShareDialog.show();
        }else if(action.equals("direct")){
            final Intent intent = new Intent(context,WebActivity.class);
            intent.putExtra("url",order.getDirectInfo().getDirectUrl());
            intent.putExtra("title",order.getDirectInfo().getDirectTitle());
            context.startActivity(intent);
        }
    }
    }

