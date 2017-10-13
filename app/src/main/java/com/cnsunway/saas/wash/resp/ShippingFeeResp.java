package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.ShippingFee;


/**
 * Created by LL on 2016/3/7.
 */
public class ShippingFeeResp extends BaseResp {
    ShippingFee data;

    public ShippingFee getData() {
        return data;
    }

    public void setData(ShippingFee data) {
        this.data = data;
    }
}
