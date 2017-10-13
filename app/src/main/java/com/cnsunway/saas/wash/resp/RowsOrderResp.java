package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.RowsOrder;

/**
 * Created by Administrator on 2017/10/13 0013.
 */

public class RowsOrderResp extends BaseResp{

    RowsOrder data;

    public RowsOrder getData() {
        return data;
    }

    public void setData(RowsOrder data) {
        this.data = data;
    }
}
