package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.CommentDetail;

/**
 * Created by Administrator on 2017/10/16 0016.
 */

public class CommentDetailResp extends BaseResp{
    CommentDetail data;
    public CommentDetail getData() {
        return data;
    }

    public void setData(CommentDetail data) {
        this.data = data;
    }




}
