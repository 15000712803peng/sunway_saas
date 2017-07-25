package com.cnsunway.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunway.wash.R;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.dialog.OperationToast;
import com.cnsunway.wash.framework.net.JsonVolley;
import com.cnsunway.wash.model.LocationForService;
import com.cnsunway.wash.sharef.UserInfosPref;
import com.cnsunway.wash.view.StarBarView;

/**
 * Created by Administrator on 2016/3/25.
 */
public class EvaluateActivity extends InitActivity implements View.OnClickListener {
    TextView textTitle, textSender;
    StarBarView starSender, starStore;
    TextView layoutEvaluate;
    String orderNo;
    String operScore, washingScore,evaluate;
    String senderName = "";
    UserInfosPref userInfos;
    JsonVolley evaluateVolley;
    TextView siteText;
    String siteName;
    EditText evaluateEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_evaluate3);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initViews() {
        textTitle = (TextView) findViewById(R.id.text_title);
        textTitle.setText("评价");
        siteText = (TextView) findViewById(R.id.text_site);
        siteText.setText("门店：" + siteName);
        textSender = (TextView) findViewById(R.id.tv_evaluate_sender);
        if(!TextUtils.isEmpty(senderName)){
            textSender.setText("收发员：" + senderName);
        }else {
            textSender.setText("收发员：");
        }

        starSender = (StarBarView) findViewById(R.id.view_star_sender);
        starStore = (StarBarView) findViewById(R.id.view_star_store);
        starSender.setCount(5);
        starStore.setCount(5);
        layoutEvaluate = (TextView) findViewById(R.id.rl_evaluate_complete);
        layoutEvaluate.setOnClickListener(this);
        evaluateEdit = (EditText) findViewById(R.id.et_evaluate);

    }

    @Override
    public void onClick(View v) {
        if (v == layoutEvaluate) {
            if (starSender.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "请评价收发员", Toast.LENGTH_SHORT).show();
            } else if (starStore.getCount() == 0) {
                Toast.makeText(getApplicationContext(), "请评价门店洗涤服务", Toast.LENGTH_SHORT).show();
            } else {
                publish();
            }
        }

    }

    private void publish() {
        operScore = "" + starSender.getCount();
        washingScore = "" + starStore.getCount();
        evaluate = evaluateEdit.getText().toString().trim();
        evaluateVolley.addParams("orderNo", orderNo);
        evaluateVolley.addParams("operScore", operScore);
        evaluateVolley.addParams("washingScore", washingScore);
        evaluateVolley.addParams("comment", evaluate);
        setOperationMsg(getString(R.string.operating));
        /*Log.e("operScore",operScore);
        Log.e("washingScore",washingScore);
        Log.e("orderNo",orderNo);
        Log.e("evaluate",evaluate);*/
        LocationForService locationForService = UserInfosPref.getInstance(this).getLocationServer();
        evaluateVolley.requestPost(Const.Request.confirmReceive, this,
                getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void initData() {
        orderNo = getIntent().getStringExtra("order_no");
        senderName = getIntent().getStringExtra("sender_name");
        siteName =getIntent().getStringExtra("site_name");
        userInfos = UserInfosPref.getInstance(this);
        evaluateVolley = new JsonVolley(this, Const.Message.MSG_EVALUATE_SUCC,
                Const.Message.MSG_EVALUATE_FAIL);
    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_EVALUATE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    OperationToast.showOperationResult(this, "评价成功", 0);
                    setResult(RESULT_OK);
                    sendBroadcast(new Intent(Const.MyFilter.FILTER_REFRESH_TABS));
                    finish();

                } else {
                    OperationToast.showOperationResult(this, msg.obj + "", 0);
                }
                break;

            case Const.Message.MSG_EVALUATE_FAIL:
                OperationToast.showOperationResult(this, R.string.request_fail);
                break;
        }
    }
}
