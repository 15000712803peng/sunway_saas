package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.StoreBalance;

import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class AccountResp extends BaseResp {
    List<StoreBalance> data;


    public List<StoreBalance> getData() {
        return data;
    }

    public void setData(List<StoreBalance> data) {
        this.data = data;
    }
}
