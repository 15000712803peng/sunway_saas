package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.AliRechargeInfo;

/**
 * Created by LL on 2015/12/14.
 */
public class RechargeAlipayResp extends BaseResp {

    AliRechargeInfo data;

    public AliRechargeInfo getData() {
        return data;
    }

    public void setData(AliRechargeInfo data) {
        this.data = data;
    }
}
