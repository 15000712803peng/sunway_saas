package com.cnsunway.saas.wash.activity;

import android.os.Bundle;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.adapter.CommentsAdapter;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.NetParams;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.CommentDetail;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.resp.CommentDetailResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;

/**
 * Created by Sunway on 2017/10/18.
 */

class ShowCommentActivity extends InitActivity {
    TextView title;
    JsonVolley storeVolley;
    LocationForService locationForService;
    String storeId;
    ListView commentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_comment);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        storeId = getIntent().getStringExtra("store_id");
        locationForService = UserInfosPref.getInstance(this).getLocationServer();
        storeVolley = new JsonVolley(this, Const.Message.MSG_GET_STORE_DETAIL_SUCC,Const.Message.MSG_GET_STORE_DETAIL_FAIL);

    }

    @Override
    protected void initViews() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("用户评价");
        commentList = (ListView) findViewById(R.id.list_comment);
        storeVolley.requestPost(Const.Request.storeDetail + "/" + storeId + "/comments",getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());


    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_GET_STORE_DETAIL_SUCC:
                if(msg.arg1 == NetParams.RESPONCE_NORMAL){
                    CommentDetailResp resp = (CommentDetailResp) JsonParser.jsonToObject(msg.obj +"",CommentDetailResp.class);
                    fillDetail(resp.getData());
                }
                break;

            case Const.Message.MSG_GET_STORE_DETAIL_FAIL:
                break;
        }
    }
    private void fillDetail(CommentDetail commentDetail){

           if (commentDetail.getList()!= null) {
                      commentList.setAdapter(new CommentsAdapter(commentDetail.getList(),getApplication()));
           }
    }
}
