package com.cnsunway.wash.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.alipay.sdk.app.PayTask;
import com.cnsunway.wash.cnst.Const;

public class AlipayTool {
	Context context;
	Handler handler;

	public AlipayTool(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;


	}

	public void pay(final String payinfo){
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity) context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payinfo);
				Message msg = new Message();
				msg.what = Const.Message.MSG_ALIPAY_RESULT;
				msg.obj = result;
				handler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}





}
