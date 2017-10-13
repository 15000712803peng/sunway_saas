package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.Order;

import java.util.List;

/**
 * Created by LL on 2015/12/8.
 */
public class OrdersResp extends BaseResp {
    List<Order> data;
    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
