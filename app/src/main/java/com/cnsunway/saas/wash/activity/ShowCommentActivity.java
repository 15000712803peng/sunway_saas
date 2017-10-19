package com.cnsunway.saas.wash.activity;

import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.adapter.CommentsAdapter;
import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.dialog.OperationToast;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.net.NetParams;
import com.cnsunway.saas.wash.framework.utils.DateUtil;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.Comment;
import com.cnsunway.saas.wash.model.CommentDetail;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.resp.CommentDetailResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;
import com.cnsunway.saas.wash.view.XListView;

import java.util.List;

/**
 * Created by Sunway on 2017/10/18.
 */

class ShowCommentActivity extends InitActivity implements XListView.IXListViewListener {
    TextView title;
    JsonVolley storeVolley;
    LocationForService locationForService;
    String storeId;
    XListView commentList;
    int pageNum;
    int page;
    List<Comment> comments;
    CommentDetailResp resp;
    CommentsAdapter commentsAdapter;
    UserInfosPref userInfos;
    String token;
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
        commentList = (XListView) findViewById(R.id.list_comment);
        storeVolley.requestPost(Const.Request.storeDetail + "/" + storeId + "/comments",getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());


    }

    @Override
    protected void handlerMessage(Message msg) {
        switch (msg.what){
            case Const.Message.MSG_GET_STORE_DETAIL_SUCC:
                if(msg.arg1 == NetParams.RESPONCE_NORMAL){
                    commentList.setRefreshTime(DateUtil.getCurrentDate());
                    commentList.stopRefresh("刷新成功");
                    resp = (CommentDetailResp) JsonParser.jsonToObject(msg.obj +"",CommentDetailResp.class);
                    pageNum = resp.getData().getPageNum();     //总页数
                    page = resp.getData().getPages(); //页数
                    if (pageNum == 0){
                        return;
                    }
                    if (pageNum == 1){
                        commentList.setPullLoadEnable(true);
                        comments = resp.getData().getList();
                        initList(comments);
                    }else if (page>1){
                        comments = resp.getData().getList();
                        loadMoreList(comments);
                    }
                }
                break;

            case Const.Message.MSG_GET_STORE_DETAIL_FAIL:
                commentList.stopRefresh("刷新失败");
                commentList.stopLoadMore();
                OperationToast.showOperationResult(getApplication(),msg.obj+"",0);
                break;
        }
    }
    private void initList(List<Comment> comments){
        if (commentsAdapter == null){
            commentsAdapter = new CommentsAdapter(resp.getData().getList(),getApplication());
            commentsAdapter.setComments(comments);
            commentList.setAdapter(commentsAdapter);
        }else {
            commentsAdapter.clear();
            commentsAdapter.getComments().addAll(comments);
            commentsAdapter.notifyDataSetChanged();
        }

        if (page >= pageNum) {
            commentList.setPullLoadEnable(false);
        }
    }
    private void fillDetail(CommentDetail commentDetail){

           if (commentDetail.getList()!= null) {
               commentList.setAdapter(new CommentsAdapter(commentDetail.getList(),getApplication()));
               commentList.setRefreshTime(DateUtil.getCurrentDate());
               commentList.stopRefresh("刷新成功");
               pageNum = commentDetail.getPageNum();
               if (pageNum == 1){
                   commentList.setPullLoadEnable(true);
               } else if (pageNum>1){
//                   loadMoreList(comments);
               }
           }
    }
    private void loadMoreList(List<Comment> comments) {
        commentList.stopLoadMore();
        if (comments == null) {
            return;
        }
        commentsAdapter.getComments().addAll(comments);
        commentsAdapter.notifyDataSetChanged();
        if (page >= pageNum) {
            commentList.setPullLoadEnable(false);
        }
    }

    @Override
    public void onRefresh() {
        page = 1;
        storeVolley.addParams("pages",page);
        storeVolley.requestPost(Const.Request.storeDetail + "/" + storeId + "/comments",getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    @Override
    public void onLoadMore() {
        int nextPage = page  + 1;
        storeVolley.addParams ("pages",nextPage);
        storeVolley.requestPost(Const.Request.storeDetail + "/" + storeId + "/comments",getHandler(), UserInfosPref.getInstance(this).getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }
}
