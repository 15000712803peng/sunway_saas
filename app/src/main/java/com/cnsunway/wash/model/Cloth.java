package com.cnsunway.wash.model;

import java.io.Serializable;

/**
 * Created by peter on 15/10/28.
 */
//{"category":"9.9元","hangArea":"123","memo":"  备注","name":"AAA",
// "orderId":7,"price":9.90,"productId":1,"reason":null,"status":20,"washCode":"a"}
public class Cloth implements Serializable{
   int count;
    String totalPrice;
    String price;
    String name;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
