package com.cnsunway.saas.wash.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;

/**
 * Created by Administrator on 2017/6/8 0008.
 */

public class DoOrderSuccActivity extends BaseActivity{

    TextView titleText;
    String orderNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_order_succ);
        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText("预约成功");
        orderNo = getIntent().getStringExtra("orderNo");
    }

    public void back(View view){
        finish();
    }

    public void startDetail(View view){
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra("order_no", orderNo);
        startActivity(intent);
        finish();
    }
}
