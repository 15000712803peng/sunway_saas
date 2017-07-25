package com.cnsunway.wash.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author YOLANDA
 * @Time 2015年3月30日 下午4:55:56
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	/**分享到微信接口**/
	private IWXAPI mWxApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWxApi = WXAPIFactory.createWXAPI(this, Const.WE_APP_ID);
		mWxApi.handleIntent(getIntent(), this);

	}



	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		mWxApi.handleIntent(intent, this);

	}

	@Override
	public void onResp(BaseResp baseResp) {



		switch (baseResp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				sendBroadcast(new Intent(Const.MyFilter.FILTER_SHARE_SUCC));
		       break;

		}

		new Handler(getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				finish();
			}
		});
	}

	@Override
	public void onReq(BaseReq baseResp) {
		try {

		} catch (Exception e) {
		}
	}
}
