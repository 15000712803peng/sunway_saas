package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.WxRechargeInfo;

/**
 * Created by LL on 2015/12/14.
 */
public class RechargeWxpayResp extends BaseResp {

    WxRechargeInfo data;

    public WxRechargeInfo getData() {
        return data;
    }

    public void setData(WxRechargeInfo data) {
        this.data = data;
    }
}
