package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.HxAccount;

/**
 * Created by Administrator on 2017/8/14 0014.
 */

public class HxAccountResp extends BaseResp{
    HxAccount data;

    public HxAccount getData() {
        return data;
    }

    public void setData(HxAccount data) {
        this.data = data;
    }
}
