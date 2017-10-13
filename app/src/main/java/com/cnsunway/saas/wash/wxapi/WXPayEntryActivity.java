package com.cnsunway.saas.wash.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.cnsunway.saas.wash.cnst.Const;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private IWXAPI api;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.pay_result);
		api = WXAPIFactory.createWXAPI(this, Const.WE_APP_ID);
		api.handleIntent(getIntent(), this);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			int code = resp.errCode;
			Intent payResult = new Intent(Const.MyFilter.FILTER_WE_PAY_RESULT);
			if (code == BaseResp.ErrCode.ERR_OK) {
				// Toast.makeText(getApplicationContext(), "支付成功",
				// Toast.LENGTH_LONG).show();
				payResult.putExtra("pay_result", 1);
				sendBroadcast(payResult);

			} else {
				payResult.putExtra("pay_result", 2);
				sendBroadcast(payResult);
			}
			new Handler(getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					finish();
				}
			});
		}

	}
}