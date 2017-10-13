package com.cnsunway.saas.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;


public class LoadingDialog extends Dialog {

	private TextView tipsLoadingMsg;

	private String message = null;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LoadingDialog(Context context) {
		this(context, context.getResources().getString(R.string.loading));

	}

	public LoadingDialog(Context context, String message) {
		this(context, R.style.CustomDialog, message);
	}

	public LoadingDialog(Context context, int theme, String message) {
		super(context, theme);
		this.message = message;
	
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		ImageView iv = (ImageView) findViewById(R.id.toastbox_anim);
		AnimationDrawable ad = (AnimationDrawable) iv.getBackground();
		ad.start();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.loading_dialog);
		tipsLoadingMsg = (TextView) findViewById(R.id.toastbox_message);
		tipsLoadingMsg.setText(this.message);
	}

	public void setText(String message) {
		this.message = message;

	}

	public void setText(int resId) {
		setText(getContext().getResources().getString(resId));
	}

}
