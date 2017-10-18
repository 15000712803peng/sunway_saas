package com.cnsunway.saas.wash.activity;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;

/**
 * Created by Sunway on 2017/10/18.
 */

class ShowCommentActivity extends InitActivity {
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_comment);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("用户评价");

    }

    @Override
    protected void handlerMessage(Message msg) {

    }
}
