package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.RowsOrder;
import com.cnsunway.saas.wash.model.RowsStore;
import com.cnsunway.saas.wash.model.Store;

import java.util.List;

/**
 * Created by Administrator on 2017/10/13 0013.
 */

public class RowsStoreResp extends BaseResp{

    List<Store> data;

    public List<Store> getData() {
        return data;
    }

    public void setData(List<Store> data) {
        this.data = data;
    }
}
