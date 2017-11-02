package com.cnsunway.saas.wash.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.util.Log;
import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.dialog.PayChoiceDialog;
import com.cnsunway.saas.wash.dialog.WayOfShareDialog;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.Order;
import com.cnsunway.saas.wash.model.PayResult;
import com.cnsunway.saas.wash.model.ShareInfo;
import com.cnsunway.saas.wash.model.WebData;
import com.cnsunway.saas.wash.resp.RechargeAlipayResp;
import com.cnsunway.saas.wash.resp.RechargeWxpayResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.util.AlipayTool;
import com.cnsunway.saas.wash.util.ShareUtil;
import com.cnsunway.saas.wash.util.WepayTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;


public class WebActivity extends CordovaActivity implements PayChoiceDialog.SelectAlipayListener, PayChoiceDialog.SelectWepayListener{
	String url;
	String title = "";
	ViewGroup container;
	String prefix1 = "native://pay?data=";
	String prefix2 = "native://goback?data=";
	String prefix3 = "native://share?data=";
	String prefix4 = "tel:";
	String prefix5 = "native://?placeOrder=";
	String prefix6 = "native://share?payShare=";
	String prefix7 = "native://?bindCards=";
	String prefix8 = "native://?coupon=";
	String prefix9 = "native://invoiceClose";
	String prefix10 = "https://";

	//	:native://?placeOrder={"placeOrder":"order"}
	PayChoiceDialog rechargeDialog;
	String chargePrice = "";
	String callBack;
	TextView titleText;
	Order order;
	String hash = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_web);
		super.onCreate(savedInstanceState);
		url = getIntent().getStringExtra("url");

		if(TextUtils.isEmpty(url)){
			url = Const.Request.newBalance;
		}

		title = getIntent().getStringExtra("title");
		titleText = (TextView) findViewById(R.id.text_title);
		titleText.setText("加载中...");
		String orderJson = getIntent().getStringExtra("order");
		if (!TextUtils.isEmpty(orderJson)){
			order = (Order) JsonParser.jsonToObject(orderJson,Order.class);
		}
//		titleTex.setText(R.string.recharge);
		container = (ViewGroup)findViewById(R.id.ll_webview_container);
		alipayTool = new AlipayTool(this, getHandler());
		wepayTool = new WepayTool(this);
		rechargeDialog = new PayChoiceDialog(this).builder();
		rechargeDialog.setAlipayListener(this);
		rechargeDialog.setWepayListener(this);
		startRechargeVolley = new JsonVolley(this, Const.Message.MSG_START_RECHAEGE_SUCC, Const.Message.MSG_START_RECHAEGE_FAIL);
		wayOfShareDialog = new WayOfShareDialog(this).builder();
		showCenterLoading();
		registerReceiver(wepayResultReceiver, new IntentFilter(Const.MyFilter.FILTER_WE_PAY_RESULT));
		if(UserInfosPref.getInstance(this).getUser() == null){
			loadUrl(url);
		}else {

			String query = "";
			try {
				URL u = new URL(url);
				if(TextUtils.isEmpty(u.getQuery())){
					query = "token="+UserInfosPref.getInstance(this).getUser().getToken()+"&mobile=" + UserInfosPref.getInstance(this).getUserName();
				}else {
					query = u.getQuery() +"&token="+UserInfosPref.getInstance(this).getUser().getToken()+"&mobile=" + UserInfosPref.getInstance(this).getUserName();
				}
//				url = u.getProtocol()+ "://" + u.getHost()+"/"+ u.getPath() +"?"+ query;
				url = u.getProtocol()+ "://" + u.getHost() +"?"+ query;
				if(!TextUtils.isEmpty(u.getRef())){
					url += "#" + u.getRef();
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			loadUrl(url);

//			if(url.contains("?")){
//				int hashIndex = url.lastIndexOf("#");
//				if(hashIndex > 0){
//					hash = url.substring(hashIndex,url.length());
//					url = url.substring(0,hashIndex);
//				}
//				url = url+"&temp="+UserInfosPref.getInstance(this).getUser().getToken()+"&mobile=" + UserInfosPref.getInstance(this).getUserName();
//				url += hash;
//				loadUrl(url);
////				loadUrl(url+"&temp="+UserInfosPref.getInstance(this).getUser().getToken()+"&mobile=" + UserInfosPref.getInstance(this).getUserName());
//			}else {
//				int hashIndex = url.lastIndexOf("#");
//				if(hashIndex > 0){
//					hash = url.substring(hashIndex,url.length());
//					url = url.substring(0,hashIndex);
//				}
//				loadUrl(url+"?temp="+UserInfosPref.getInstance(this).getUser().getToken()+"&mobile=" + UserInfosPref.getInstance(this).getUserName()+ hash);
//
//			}

		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(wepayResultReceiver);
	}

	protected void createViews() {
		//Why are we setting a constant as the ID? This should be investigated
//		appView.getView().setId(100);
		appView.getView().setLayoutParams(new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
//		appView.getView().get

//		Log.e("---------","webview:" + ();
		if(appView.getView() instanceof WebView){
			WebView webView = (WebView) appView.getView();
			webView.getSettings();
			webView.getSettings().setSupportZoom(true);
			webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		}

		container.addView(appView.getView());
		if (preferences.contains("BackgroundColor")) {
			int backgroundColor = preferences.getInteger("BackgroundColor", Color.BLACK);
			// Background of activity:
			appView.getView().setBackgroundColor(backgroundColor);
		}

		appView.getView().requestFocusFromTouch();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (appView.canGoBack()) {
				appView.backHistory();
			} else {
				finish();
			}
		}
		return true;
	}

	public void back(View view){
//		if (appView.canGoBack()) {
//			appView.backHistory();
//		} else {
//			finish();
//		}
		finish();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (appView.canGoBack()) {
					appView.backHistory();
				} else {
					finish();
				}
				break;
		}
		if (appView != null) {
			appView.getPluginManager().postMessage("onOptionsItemSelected", item);
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 * Called when a message is sent to plugin.
	 *
	 * @param id            The message id
	 * @param data          The message data
	 * @return              Object or null
	 */
	public Object onMessage(String id, Object data) {
		callBack = "";
		if ("onReceivedError".equals(id)) {
			JSONObject d = (JSONObject) data;
			try {
				this.onReceivedError(d.getInt("errorCode"), d.getString("description"), d.getString("url"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if ("exit".equals(id)) {
			finish();
		}
		if(id.equals("onPageStarted")){

		}else if(id.equals("onPageFinished")){
			hideCenterLoading();
		}else if(id.equals("shouldOverrideUrlLoading")){
			try {
				String  operation = data + "";
				Log.e("operation","operation:" + data);
				operation = operation.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
				operation = URLDecoder.decode(operation + "", "utf-8");
				pareseData(operation);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else if(id.equals("web_title")){
			if(data != null){
				String titleData = data+"";
				if(!titleData.contains(WEB_SERVER1) && !titleData.contains(WEB_SERVER2)){
					titleText.setText(data+"");
				}

			}
		}
		return null;
	}

	private final  String WEB_SERVER1  = "wxdev.landaojia.com";
	private final  String WEB_SERVER2  = "wx.sunwayxiyi.com";

	private final int MSG_CALL = 1;
	private final int MSG_DO_ORDER = 2;
	private final int MSG_TO_SHARE = 3;
	private final int MSG_TO_BIND = 4;
	private final int MSG_TO_COUPONN = 5;
	private final int MSG_TO_INVOICE_CLOSED = 6;
	private void pareseData(String data){
		if(TextUtils.isEmpty(data)){
			return;
		}

		if(data.startsWith(prefix1)){
			data = data.substring(prefix1.length(),data.length());
			getHandler().obtainMessage(Const.Message.MSG_WEB_OPERATION_PAY,data).sendToTarget();
		}else if(data.startsWith(prefix2)){
			data = data.substring(prefix2.length(),data.length());
			getHandler().obtainMessage(Const.Message.MSG_WEB_OPERATION_GOBACK,data).sendToTarget();
		}else if(data.startsWith(prefix3)){
			data = data.substring(prefix3.length(),data.length());
			getHandler().obtainMessage(Const.Message.MSG_WEB_OPERATION_SHARE,data).sendToTarget();
		}else if(data.startsWith(prefix4)){
			getHandler().obtainMessage(MSG_CALL,data).sendToTarget();
		}else if(data.startsWith(prefix5)){
			data = data.substring(prefix5.length(),data.length());
			getHandler().obtainMessage(MSG_DO_ORDER,data).sendToTarget();
		}else if(data.startsWith(prefix6)){
			data = data.substring(prefix6.length(),data.length());
			getHandler().obtainMessage(MSG_TO_SHARE,data).sendToTarget();
		}else if(data.startsWith(prefix7)){
			data = data.substring(prefix7.length(),data.length());
			getHandler().obtainMessage(MSG_TO_BIND,data).sendToTarget();
		}else if(data.startsWith(prefix8)){
			data = data.substring(prefix8.length(),data.length());
			getHandler().obtainMessage(MSG_TO_COUPONN,data).sendToTarget();
		}else if(data.startsWith(prefix9)){
//			OperationToast.showOperationResult(getApplicationContext(),"开票成功",R.mipmap.success_icon);
			getHandler().obtainMessage(MSG_TO_INVOICE_CLOSED,data).sendToTarget();
		}else if(data.startsWith(prefix10)){
			if(data.contains(".aspx") || data.contains("/promotion/load")||data.contains("qN/login")){
				loadUrl(data);
			}
		}
	}

	String depositOrderNo;
	AlipayTool alipayTool;
	WepayTool wepayTool;
	private static final String ALIPEY_PAY_SUCC = "9000";
	private static final String ALIPEY_PAYING = "8000";
	@Override
	protected void handlerMessage(Message msg) {
		super.handlerMessage(msg);
		switch (msg.what){
			case Const.Message.MSG_WEB_OPERATION_PAY:
				String data = (String) msg.obj;
				if(TextUtils.isEmpty(data)){
					return;
				}
				WebData webData = (WebData) JsonParser.jsonToObject(data,WebData.class);
				chargePrice = webData.getPrice();
				callBack = webData.getCallback();
				rechargeDialog.setAmmout(webData.getPrice());
				rechargeDialog.show();
				break;

			case Const.Message.MSG_START_RECHAEGE_SUCC:
				if (msg.arg1 == Const.Request.REQUEST_SUCC) {
					if (rechargeChoice == ALIPAY_RECHARGE) {
						RechargeAlipayResp alipayResp = (RechargeAlipayResp) JsonParser.jsonToObject(msg.obj + "", RechargeAlipayResp.class);
						depositOrderNo =   alipayResp.getData().getOutTradeNo();
						alipayTool.pay(alipayResp.getData().getParams());
					} else if (rechargeChoice == WX_RECHARGE) {
						RechargeWxpayResp wepayResp = (RechargeWxpayResp) JsonParser.jsonToObject(msg.obj + "", RechargeWxpayResp.class);
						depositOrderNo = wepayResp.getData().getOutTradeNo();
						wepayTool.pay(wepayResp.getData().getParams());
					}
				} else {
					if(!TextUtils.isEmpty(msg.obj+"")){
						OperationToast.showOperationResult(this, msg.obj+"",0);
					}else {
						OperationToast.showOperationResult(this, R.string.recharge_fail);
					}
					rechargeDialog.cancel();
				}
				break;

			case Const.Message.MSG_ALIPAY_RESULT:
				rechargeDialog.cancel();
				PayResult payResult = new PayResult((String) msg.obj);
				String resultStatus = payResult.getResultStatus();
				if (TextUtils.equals(resultStatus, ALIPEY_PAY_SUCC)) {
					paySucc();
				} else {
					if (TextUtils.equals(resultStatus, ALIPEY_PAYING)) {
					} else {
						OperationToast.showOperationResult(this, R.string.recharge_fail);
					}
				}
				break;

			case Const.Message.MSG_WEB_OPERATION_GOBACK:
				if (appView.canGoBack()) {
					appView.backHistory();
				} else {
					finish();
				}
				break;

			case Const.Message.MSG_WEB_OPERATION_SHARE:
				ShareInfo shareInfo = (ShareInfo) JsonParser.jsonToObject(msg.obj+"",ShareInfo.class);
				wayOfShareDialog.setShareInfo(shareInfo);
				wayOfShareDialog.show();
				break;

			case MSG_CALL:
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(msg.obj+"")));
				break;

			case MSG_DO_ORDER:
				if(UserInfosPref.getInstance(this).getUser() == null){
					Intent intent = new Intent(this,LoginActivity.class);
					intent.putExtra("do_order",true);
					startActivity(intent);
				}else {
					startActivity(new Intent(this,DoOrderActivity2.class));
				}
				break;
			case MSG_TO_SHARE:
				wayOfShareDialog = new WayOfShareDialog(this).builder();
//				wayOfShareDialog.show();
				if (order!=null){
					new ShareUtil(wayOfShareDialog).share(this,order);
				}

				break;

			case MSG_TO_BIND:
				if(UserInfosPref.getInstance(this).getUser() == null){
					Intent intent = new Intent(this,LoginActivity.class);
					startActivity(intent);
				}else {
					startActivity(new Intent(this,BindCouponActivity.class));
				}

				break;
			case MSG_TO_COUPONN:
				if(UserInfosPref.getInstance(this).getUser() == null){
					Intent intent = new Intent(this,LoginActivity.class);
					startActivity(intent);
				}else {
					startActivity(new Intent(this,CouponActivity.class));
				}

				break;

			case MSG_TO_INVOICE_CLOSED:
				finish();
				break;
		}

	}

	WayOfShareDialog wayOfShareDialog;

	int rechargeChoice = 0;
	private static final int ALIPAY_RECHARGE = 33;
	private static final int WX_RECHARGE = 32;
	JsonVolley startRechargeVolley;

	@Override
	public void selectAlipay() {
		if(TextUtils.isEmpty(chargePrice)){
			OperationToast.showOperationResult(this,R.string.selected_recharge_price);
			return;
		}
		rechargeChoice = ALIPAY_RECHARGE;
		startRechargeVolley.addParams("paymentAmount",chargePrice);
		startRechargeVolley.addParams("payChannel", ALIPAY_RECHARGE + "");
		setOperationMsg(getString(R.string.paying));
		LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
		startRechargeVolley.requestPost(Const.Request.startRecharge, this, handler, UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
	}

	@Override
	public void selectWepay() {
		if(TextUtils.isEmpty(chargePrice)){
			OperationToast.showOperationResult(this,R.string.selected_recharge_price);
			return;
		}
		rechargeChoice = WX_RECHARGE;
		startRechargeVolley.addParams("paymentAmount", chargePrice);
		startRechargeVolley.addParams("payChannel", WX_RECHARGE + "");
		setOperationMsg(getString(R.string.paying));
		LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
		startRechargeVolley.requestPost(Const.Request.startRecharge, this, handler, UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
	}

	BroadcastReceiver wepayResultReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int reuslt = intent.getIntExtra("pay_result", 0);
			if (reuslt == 1) {
				rechargeDialog.cancel();
				paySucc();
			} else {
				OperationToast.showOperationResult(context, R.string.recharge_fail);
				rechargeDialog.cancel();
			}
		}
	};

	private void paySucc() {
		if(!TextUtils.isEmpty(callBack)){
			appView.loadUrl(callBack);
		}
	}
}
