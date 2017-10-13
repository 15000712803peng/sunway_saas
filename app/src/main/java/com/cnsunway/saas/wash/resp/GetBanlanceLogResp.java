package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.PagerBanlanceDetail;

/**
 * Created by LL on 2016/2/25.
 */
public class GetBanlanceLogResp extends BaseResp {
    PagerBanlanceDetail data;

    public PagerBanlanceDetail getData() {
        return data;
    }

    public void setData(PagerBanlanceDetail data) {
        this.data = data;
    }
}
