package com.cnsunway.wash.dialog;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunway.wash.R;


public class OperationToast {

	public static void showOperationResult(Context context, String msg, int resID) {
		View toastRoot = LayoutInflater.from(context).inflate(R.layout.operation_toast,
				null);
		final Toast toast = new Toast(context.getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		TextView tv = (TextView) toastRoot.findViewById(R.id.toastbox_message);
		tv.setText(msg);
		if (resID > 0) {
			ImageView iv = (ImageView) toastRoot
					.findViewById(R.id.toastbox_icon);
			iv.setImageResource(resID);
		} else {
			ImageView iv = (ImageView) toastRoot
					.findViewById(R.id.toastbox_icon);
			iv.setVisibility(View.GONE);
		}
		toast.show();
		new Handler(context.getMainLooper()){

		}.postDelayed(new Runnable() {
			@Override
			public void run() {
				toast.cancel();
			}
		},1000);

	}

	public static void showOperationResult(Context context, int msg, int resID) {
		View toastRoot = LayoutInflater.from(context).inflate(R.layout.operation_toast,
				null);
		final Toast toast = new Toast(context.getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		TextView tv = (TextView) toastRoot.findViewById(R.id.toastbox_message);
		tv.setText(msg);
		if (resID > 0) {
			ImageView iv = (ImageView) toastRoot
					.findViewById(R.id.toastbox_icon);
			iv.setImageResource(resID);
		} else {
			ImageView iv = (ImageView) toastRoot
					.findViewById(R.id.toastbox_icon);
			iv.setVisibility(View.GONE);
		}
		toast.show();
		new Handler(context.getMainLooper()){

		}.postDelayed(new Runnable() {
			@Override
			public void run() {
				toast.cancel();
			}
		},1000);

	}




	public static void showOperationResult(Context context, int msg) {
		View toastRoot = LayoutInflater.from(context).inflate(R.layout.operation_toast,
				null);
		final Toast toast = new Toast(context.getApplicationContext());
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		TextView tv = (TextView) toastRoot.findViewById(R.id.toastbox_message);
		tv.setText(msg);
		ImageView iv = (ImageView) toastRoot
					.findViewById(R.id.toastbox_icon);
		iv.setVisibility(View.GONE);
		toast.show();
		new Handler(context.getMainLooper()){

		}.postDelayed(new Runnable() {
			@Override
			public void run() {
				toast.cancel();
			}
		},1000);

	}
}
