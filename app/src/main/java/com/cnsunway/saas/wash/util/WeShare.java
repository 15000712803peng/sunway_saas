package com.cnsunway.saas.wash.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.model.ShareInfo;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.InputStream;
import java.net.URL;

public class WeShare {

	private IWXAPI api;
	Context context;
	ShareInfo shareInfo;
	String key;
	public ShareInfo getShareInfo() {
		return shareInfo;
	}

	public void setShareInfo(ShareInfo shareInfo) {
		this.shareInfo = shareInfo;
	}


	public WeShare(Context context, ShareInfo shareInfo) {
		this.context = context;
		this.shareInfo = shareInfo;
		api = WXAPIFactory.createWXAPI(context, Const.WE_APP_ID, false);
		api.registerApp(Const.WE_APP_ID);
	}

	public void share(final int type,String key){
		this.key = key;
		if(shareInfo != null && shareInfo.getShareImgUrl() != null){
			new AsyncTask<Void,Void,Bitmap>() {
				@Override
				protected Bitmap doInBackground(Void... params) {
//					return readBitmap(context, R.raw.prize_cover);
//					return readBitmap(context, R.raw.welcome_logo);
					return readBitmapFormUrl(shareInfo.getShareImgUrl());
				}
				protected void onPostExecute(Bitmap result) {
					super.onPostExecute(result);
					sendShare(type,result);
				}

			}.execute((Void) null);
		}else{
			sendShare(type,null);
		}
	}

	public void sendShare(int type,Bitmap bitmap) {
		WXWebpageObject webpage = new WXWebpageObject();

		if(shareInfo != null && !TextUtils.isEmpty(shareInfo.getShareUrl())){
			if(!TextUtils.isEmpty(key)){
				webpage.webpageUrl = shareInfo.getShareUrl() + "&key="+key;
			}else {
				webpage.webpageUrl = shareInfo.getShareUrl();
			}

		}
		WXMediaMessage msg = new WXMediaMessage(webpage);
		if(shareInfo != null && !TextUtils.isEmpty(shareInfo.getShareTitle())){
			msg.title = shareInfo.getShareTitle();
		}

		if(shareInfo != null && !TextUtils.isEmpty(shareInfo.getShareText())){
			msg.description = shareInfo.getShareText();
		}
		Log.e("step","step:description" + shareInfo.getShareText());
		Bitmap thumb = readBitmap(context, R.raw.prize_cover);
		if(bitmap != null){
			thumb = bitmap;
		}
		msg.thumbData = MyUtil.bmpToByteArray(thumb, true);
//		Log.e("step","thumbData:" + msg.thumbData);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
        if(type == 1){
			req.scene = SendMessageToWX.Req.WXSceneSession;
		}else if(type == 2){
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}
		boolean isSend = api.sendReq(req);
		Log.e("--------", "is send:" + isSend);
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}

	public static Bitmap readBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);

		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static Bitmap readBitmapFormUrl(String url) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		try {
			return BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
		} catch (Exception e) {}

		return null;
	}
}
