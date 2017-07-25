package com.cnsunway.wash.resp;

import com.cnsunway.wash.framework.net.BaseResp;
import com.cnsunway.wash.model.WxRechargeInfo;

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
