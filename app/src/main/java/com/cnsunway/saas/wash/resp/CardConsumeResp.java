package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.CardConsumeItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/15 0015.
 */

public class CardConsumeResp extends BaseResp{

    List<CardConsumeItem> data;

    public List<CardConsumeItem> getData() {
        return data;
    }

    public void setData(List<CardConsumeItem> data) {
        this.data = data;
    }
}
