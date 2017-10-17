package com.cnsunway.saas.wash.resp;

import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.model.Product;

import java.util.List;

/**
 * Created by Administrator on 2017/10/17 0017.
 */

public class ProductsResp extends BaseResp{

    List<Product> data;

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
