package com.cnsunway.wash.resp;

import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.OrderPaiedInfo;

/**
 * Created by LL on 2016/1/8.
 */
public class OrderPayDetailResp extends BaseResp {
    OrderPaiedInfo data;

    public OrderPaiedInfo getData() {
        return data;
    }

    public void setData(OrderPaiedInfo data) {
        this.data = data;
    }
}
