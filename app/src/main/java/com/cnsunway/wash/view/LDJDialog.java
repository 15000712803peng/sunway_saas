package com.cnsunway.wash.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.Window;

import com.cnsunway.wash.R;

/**
 * Created by peter on 16/3/21.
 */
public class LDJDialog  {
    public static void showDialog(Activity activity,String title,
                           CharSequence[] items,DialogInterface.OnClickListener listener){
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title).setItems(items,listener).setNegativeButton("确定",null).create();
//        Window window = dialog.getWindow();
//        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
//        window.setWindowAnimations(R.style.bottomdialog);  //添加动画
        dialog.show();
    }
    public static void showBottomDialog(Activity activity,String title,
                                 CharSequence[] items,DialogInterface.OnClickListener listener){
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title).setItems(items,listener).setNegativeButton("确定",null).create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.bottomdialog);  //添加动画
        dialog.show();
    }
}
