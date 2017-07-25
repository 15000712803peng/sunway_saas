package com.cnsunway.wash.resp;

import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.Order;

/**
 * Created by LL on 2015/12/9.
 */
public class OrderDetailResp extends BaseResp {
    Order data;

    public Order getData() {
        return data;
    }

    public void setData(Order data) {
        this.data = data;
    }
}
