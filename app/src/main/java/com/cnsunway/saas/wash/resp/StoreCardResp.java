package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.StoreCard;

import java.util.List;

/**
 * Created by Administrator on 2017/6/15 0015.
 */

public class StoreCardResp extends BaseResp{
    List<StoreCard> data;

    public List<StoreCard> getData() {
        return data;
    }

    public void setData(List<StoreCard> data) {
        this.data = data;
    }
}
