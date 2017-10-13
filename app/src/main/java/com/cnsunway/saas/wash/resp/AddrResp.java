package com.cnsunway.saas.wash.resp;


import com.cnsunway.saas.wash.model.Addr;

import java.util.List;

/**
 * Created by LL on 2015/12/10.
 */
public class AddrResp {

    List<Addr> data;
    public List<Addr> getData() {
        return data;
    }
    public void setData(List<Addr> data) {
        this.data = data;
    }
}
