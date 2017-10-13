package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.Coupon;
import com.cnsunway.saas.wash.model.HistoryCoupons;
import com.cnsunway.saas.wash.model.Paginator;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class HistoryCouponResp extends BaseResp {
    HistoryCoupons data;


    public HistoryCoupons getData() {
        return data;
    }

    public void setData(HistoryCoupons data) {
        this.data = data;
    }
}
