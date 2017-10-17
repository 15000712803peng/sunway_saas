package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.ProductCatogery;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public class ProductCatogeriesResp extends BaseResp{

    List<ProductCatogery> data;

    public List<ProductCatogery> getData() {
        return data;
    }

    public void setData(List<ProductCatogery> data) {
        this.data = data;
    }
}
