package com.cnsunway.saas.wash.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by peter on 16/1/6.
 */
@Table(name="OrderCache")
public class OrderCache extends Model{
    public OrderCache() {
        super();
    }
    @Column(name = "orderNo", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String orderNo;
    @Column(name = "disinfectingDuration")
    public int disinfectingDuration;   //单位是毫秒
    @Column(name = "caringDuration")
    public int caringDuration;         //单位是毫秒
    @Column(name = "qualifyingDuration")
    public int qualifyingDuration;     //单位是毫秒
    @Column(name = "packingDuration")
    public int packingDuration;        //单位是毫秒
    @Column(name = "disinfectingDate")
    public String disinfectingDate;

    @Column(name = "isNotifyed")
    public boolean isNotifyed;

    public static List<OrderCache> getAll() {
        return new Select().from(OrderCache.class).execute();
    }
    public static OrderCache get(String orderNo) {
        List<OrderCache> orders = new Select().from(OrderCache.class).where("orderNo = ?", orderNo).execute();
        if(orders.size() > 0){
            return orders.get(0);
        }
        return null;
    }
    public static void clearAll() {
        new Delete().from(OrderCache.class).execute();
    }
}
