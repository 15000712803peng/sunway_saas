package com.cnsunway.saas.wash.util;

import android.app.Activity;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.model.WepayConfig;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.LinkedList;
import java.util.List;

public class WepayTool {
	PayReq req;
	StringBuffer buffer;
	Activity activity;
	String outTradeNo;
	IWXAPI msgApi;
	String totalPay;
	public interface OnSendWxPayFailLienster{
		void sendWxPayFail();
	}

	OnSendWxPayFailLienster onSendWxPayFailLienster;

	public void setOnSendWxPayFailLienster(OnSendWxPayFailLienster onSendWxPayFailLienster) {
		this.onSendWxPayFailLienster = onSendWxPayFailLienster;
	}

	public WepayTool(Activity activity) {
		this.activity = activity;
		req = new PayReq();
		msgApi = WXAPIFactory.createWXAPI(activity, null);
		buffer = new StringBuffer();
	}

	private void genPayReq(WepayConfig config) {
		req.appId = config.getAppId();
		req.partnerId = Const.MCH_ID;
		req.prepayId = config.getPrepayid();
		req.packageValue = config.getPackageName();
		req.nonceStr = config.getNonceStr();
		req.timeStamp = config.getTimeStamp();
//		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//		signParams.add(new BasicNameValuePair("appid", req.appId));
//		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//		signParams.add(new BasicNameValuePair("package", req.packageValue));
//		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//		req.sign = genAppSign(signParams);
		req.sign = config.getSign();
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			builder.append(params.get(i).getName());
			builder.append('=');
			builder.append(params.get(i).getValue());
			builder.append('&');
		}
		builder.append("key=");
		builder.append(activity.getResources().getString(R.string.we_key));
//		buffer.append("sign str\n" + builder.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(builder.toString().getBytes());
		// Log.e("orion", appSign);
		return appSign;
	}

	public void pay(WepayConfig config) {
		msgApi.registerApp(config.getAppId());
		genPayReq(config);
	    boolean sendResult = msgApi.sendReq(req);
		if(!sendResult){
			if(onSendWxPayFailLienster != null){
				onSendWxPayFailLienster.sendWxPayFail();
			}
		}
	}

}
