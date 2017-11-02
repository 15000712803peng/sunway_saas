package com.cnsunway.saas.wash.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.alipay.sdk.app.PayTask;
import com.cnsunway.saas.wash.cnst.Const;

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
//				String result = alipay.pay("alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2017102009407049&biz_content=%7B%22body%22%3A%22%E6%B4%97%E8%A1%A3%E8%AE%A2%E5%8D%95%EF%BC%9AN171026G0DIP%22%2C%22out_trade_no%22%3A%22N171026G0DIPBYI%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%B4%97%E8%A1%A3%E8%AE%A2%E5%8D%95%EF%BC%9AN171026G0DIP%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&sign=cI%2FDYuH7uPRA%2BZOVVfGr8KR7vSk0Aw64DTdwSfnj6R1dUfBCDvUNAE3KdLtJE9WlThe27FKk25TqaoJNia6JgBCfWQw1iMeeCI0zzAAnznaiFqPqS8kjzkgq%2FsO4Ts59a7dy6VIbE3PSH3sQI0PfdmscWclny24TzK9Z3c%2BKAmzQRbWAaX8Ei85l1HEsLxC7Xt%2FkgI121EQ%2B8bQrZyOjOvXXuiAV9cdd1XGZSz7GO3WugDo7mvVvUyORDHe5F7VM7wwvZldtjxaJMBIHVNFsb6wrcsf3TsnGa2VymRlBCW8QdFFanGG2RRl9%2BL5zWkagckxtb9SHc%2BIE5K%2F2J7Bm5A%3D%3D&sign_type=RSA2&timestamp=2017-10-27+10%3A17%3A08&version=1.0&sign=cI%2FDYuH7uPRA%2BZOVVfGr8KR7vSk0Aw64DTdwSfnj6R1dUfBCDvUNAE3KdLtJE9WlThe27FKk25TqaoJNia6JgBCfWQw1iMeeCI0zzAAnznaiFqPqS8kjzkgq%2FsO4Ts59a7dy6VIbE3PSH3sQI0PfdmscWclny24TzK9Z3c%2BKAmzQRbWAaX8Ei85l1HEsLxC7Xt%2FkgI121EQ%2B8bQrZyOjOvXXuiAV9cdd1XGZSz7GO3WugDo7mvVvUyORDHe5F7VM7wwvZldtjxaJMBIHVNFsb6wrcsf3TsnGa2VymRlBCW8QdFFanGG2RRl9%2BL5zWkagckxtb9SHc%2BIE5K%2F2J7Bm5A%3D%3D");
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
