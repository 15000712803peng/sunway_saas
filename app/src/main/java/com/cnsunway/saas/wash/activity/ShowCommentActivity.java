package com.cnsunway.saas.wash.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.adapter.CommentsAdapter;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.NetParams;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.Comment;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.resp.CommentDetailResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.view.XListView;

import java.util.List;

/**
 * Created by Sunway on 2017/10/18.
 */

public class ShowCommentActivity extends InitActivity implements XListView.IXListViewListener {
    TextView title;
    JsonVolley commentsVolley;
    LocationForService locationForService;
    String storeId;
    XListView commentList;
    int pageNum;
    int page = 1;
    List<Comment> comments;
    CommentDetailResp resp;
    CommentsAdapter commentsAdapter;
    UserInfosPref userInfos;
    String token;
    int rows = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_comment);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        storeId = getIntent().getStringExtra("store_id");
        locationForService = UserInfosPref.getInstance(this).getLocationServer();
        commentsVolley = new JsonVolley(this, Const.Message.MSG_GET_COMMENTS_SUCC,Const.Message.MSG_GET_COMMENTS_FAIL);

    }

    @Override
    protected void initViews() {
        title = (TextView) findViewById(R.id.text_title);
        title.setText("用户评价");
        commentList = (XListView) findViewById(R.id.list_comment);
        commentList.setPullLoadEnable(true);
        commentList.setPullRefreshEnable(true);
        commentList.setXListViewListener(this);
        commentsVolley.addParams("page",page);
        commentsVolley.addParams("rows",rows);
        commentsVolley.requestPost(Const.Request.comments + "/" + storeId + "/comments",getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());


    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_GET_COMMENTS_SUCC:
                if(msg.arg1 == NetParams.RESPONCE_NORMAL){
                    resp = (CommentDetailResp) JsonParser.jsonToObject(msg.obj +"",CommentDetailResp.class);
                    page = resp.getData().getPageNum();     //总页数
                    if (page == 1 && resp.getData().getList() != null && resp.getData().getList().size() > 0){
                        commentList.stopRefresh("刷新成功");
                        comments = resp.getData().getList();
                        initList(comments,resp.getData().isLastPage());
                    }else if (page>1 && resp.getData().getList() != null && resp.getData().getList().size() > 0){
                        comments = resp.getData().getList();
                        loadMoreList(comments,resp.getData().isLastPage());
                    }
                }
                break;

            case Const.Message.MSG_GET_COMMENTS_FAIL:
                commentList.stopRefresh("刷新失败");
                commentList.stopLoadMore();
                OperationToast.showOperationResult(getApplication(),msg.obj+"",0);
                break;
        }
    }
    private void initList(List<Comment> comments,boolean isLast){
        if (commentsAdapter == null){
            commentsAdapter = new CommentsAdapter(resp.getData().getList(),getApplication());
            commentsAdapter.setComments(comments);
            commentList.setAdapter(commentsAdapter);
        }else {
            commentsAdapter.clear();
            commentsAdapter.getComments().addAll(comments);
            commentsAdapter.notifyDataSetChanged();
        }

        if (isLast) {
            commentList.setPullLoadEnable(false);
        } else {
            commentList.setPullLoadEnable(true);
        }
    }

    private void loadMoreList(List<Comment> comments,boolean isLast) {
        commentList.stopLoadMore();
        if (comments == null) {
            return;
        }
        commentsAdapter.getComments().addAll(comments);
        commentsAdapter.notifyDataSetChanged();
        if (isLast) {
            commentList.setPullLoadEnable(false);
        } else {
            commentList.setPullLoadEnable(true);
        }
    }

    @Override
    public void onRefresh() {

        commentsVolley.addParams("page",1);
        commentsVolley.addParams("rows",rows);
        commentsVolley.requestPost(Const.Request.comments + "/" + storeId + "/comments",getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    @Override
    public void onLoadMore() {
        commentsVolley.addParams ("page",page  + 1);
        commentsVolley.addParams ("rows",rows);
        commentsVolley.requestPost(Const.Request.comments + "/" + storeId + "/comments",getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }
    public void back(View view){
        finish();
    }
}
