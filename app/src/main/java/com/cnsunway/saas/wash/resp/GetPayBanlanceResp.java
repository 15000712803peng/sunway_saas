package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.Banlance;

/**
 * Created by Administrator on 2017/10/19 0019.
 */

public class GetPayBanlanceResp extends BaseResp{
    Banlance data;

    public Banlance getData() {
        return data;
    }

    public void setData(Banlance data) {
        this.data = data;
    }
}
