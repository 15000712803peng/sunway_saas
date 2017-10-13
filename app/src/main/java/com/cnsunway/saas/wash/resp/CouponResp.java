package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.Coupon;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 */
public class CouponResp extends BaseResp {
   /* AllCouponResp data;
    public AllCouponResp getData() {
        return data;
    }

    public void setData(AllCouponResp data) {
        this.data = data;
    }*/

    List<Coupon> data;

        public List<Coupon> getData() {
            return data;
        }

        public void setData(List<Coupon> data) {
            this.data = data;
        }

}
