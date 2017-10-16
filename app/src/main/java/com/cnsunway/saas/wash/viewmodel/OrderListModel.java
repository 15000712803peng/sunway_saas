package com.cnsunway.saas.wash.viewmodel;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.net.BaseResp;
import com.cnsunway.saas.wash.framework.net.JsonVolley;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.Order;
import com.cnsunway.saas.wash.model.RowsOrder;
import com.cnsunway.saas.wash.resp.RowsOrderResp;
import com.cnsunway.saas.wash.sharef.UserInfosPref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//import com.cnsunway.saas.wash.sharef.UserInfosPref;

/**
 * Created by peter on 16/3/21.
 */
public class OrderListModel extends ViewModel{

    private static long serverNowOffset;//当前时间＋offset
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * ViewModel不应该和Activity有任何关联
     */
    private static Activity activity;
    public static final String PROPERTY_HOMELISTS = "homeLists";
    public static final String PROPERTY_RESPONSE = "resp";
    private BaseResp resp;
    private List<Order> homeLists;
    private int page = 1;
    private int total;
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity a) {
        this.activity = a;
    }


    public List<Order> getHomeLists() {
        return homeLists;
    }

    public void setHomeLists(List<Order> homeLists) {
        this.homeLists = homeLists;
        changeSupport.firePropertyChange(PROPERTY_HOMELISTS, null, homeLists);
    }

    public BaseResp getResp() {
        return resp;
    }

    public void setResp(BaseResp resp) {
        this.resp = resp;
        changeSupport.firePropertyChange(PROPERTY_RESPONSE,resp,resp);
    }

    protected void handlerMessage(Message msg) {
        switch (msg.what) {
            case Const.Message.MSG_ORDER_DONE_SUCC:
                if (msg.arg1 == Const.Request.REQUEST_SUCC) {
                    RowsOrderResp initResp = (RowsOrderResp) JsonParser.jsonToObject(msg.obj + "", RowsOrderResp.class);
                    setServerNow(initResp.getNow());
                    RowsOrder searchData = initResp.getData();
                    if(searchData != null) {
                        List<Order> initOrders = searchData.getList();
                        total = searchData.getTotal();
                        if (searchData.getPageNum() == 1) {
                            setHomeLists(initOrders);
                        } else {
                            getHomeLists().addAll(initOrders);
                            setHomeLists(getHomeLists());
                        }
                    }else{
                        setHomeLists(null);
                    }
                    setRequestStatus(ViewModel.STATUS_REQUEST_SUCC);
                    setResp(initResp);

                } else if (msg.arg1 == Const.Request.REQUEST_FAIL) {
                    setRequestStatus(ViewModel.STATUS_REQUEST_FAIL);
                    Bundle bundle = msg.getData();
                    if(bundle != null && bundle.getString("data") != null){
                        BaseResp resp = (BaseResp) JsonParser.jsonToObject(bundle.getString("data"), BaseResp.class);
                        setResp(resp);
                    }
                }
                break;
            case Const.Message.MSG_ORDER_DONE_FAIL:
                setRequestStatus(ViewModel.STATUS_NET_ERROR);
                break;


        }
    }

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (getActivity() == null ||getActivity().isFinishing()) {
                return;
            }
            handlerMessage(msg);
        }
    };

    public Handler getHandler() {
        return handler;
    }

    public void requestOrders(Activity activity){
        setActivity(activity);
        UserInfosPref userInfos = UserInfosPref.getInstance(activity);
        LocationForService locationForService = UserInfosPref.getInstance(activity).getLocationServer();
        JsonVolley orderVolley = new JsonVolley(activity, Const.Message.MSG_ORDER_DONE_SUCC, Const.Message.MSG_ORDER_DONE_FAIL);
        orderVolley.addParams("rows",10);
        orderVolley.addParams("page",1);
        orderVolley.requestPost(Const.Request.inservice,
                getHandler(), userInfos.getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }

    public static String getOrderTimeForHome(Order order){
        int second = getOrderTimeSecond(order);
        int day = second/(3600*24);
        if(day >= 1){
            return day +"天" + (second%(3600*24))/3600 + "时";
        }else if(second > 3600){
            return second/3600 + "时" + (second%3600)/60 + "分";
        }else if(second > 60){
            return second/60 + "分";
        }else{
            return second + "秒";
        }
    }
    public static String getOrderTimeForDetail(Order order){
        int second = getOrderTimeSecond(order);
//        int day = second/(3600*24);
//        second -= day*24*3600;
        int hour = second/3600;
        second -= hour*3600;
        int minute = second/60;
        second -= minute*60;
        return "" + hour + "’’" + String.format("%02d",minute) + "’’" + String.format("%02d",second);
    }
    public static String getVirtualStatusStr(Order order){
        OrderCircle status = getVirtualOrderStatus(order);
        return getOrderStatus(order) + " " + "#"+ (status.ordinal()+1)+"/16";
    }
    public static void setServerNow(String serverNow){
        try {
            Date serverDate = dateFormat.parse(serverNow);//10点
            Date date = new Date();//11点
            serverNowOffset = serverDate.getTime() - date.getTime();//-1点
        }catch (Exception e){
            e.printStackTrace();
            serverNowOffset = 0;
        }
    }

    public static Date toServerTime(Date localDate){
        Date date = new Date(localDate.getTime() + serverNowOffset);
        return date;
    }

    public static Date toTime(String time){
        Date localDate = null;
        if(time == null){
            return null;
        }
        try {
            localDate = dateFormat.parse(time);
            return localDate;
        }catch (Exception e){
            e.printStackTrace();
        }
        return localDate;
    }

    private static int msToSecond(long ms){
        return (int)(ms/1000);
    }

    public static String getOrderCircleOnDoorDate(Order order){
        Date setoutDate = toTime(order.getSetoutDate());
        Date expectDate = toTime(order.getExpectDateB());
        if(expectDate == null){
            //never be here
            return order.getSetoutDate();
        }
        if(setoutDate == null){
            return order.getExpectDateB();
        }
        if(setoutDate.getTime() < expectDate.getTime()) {
            return order.getSetoutDate();
        }
        return order.getExpectDateB();
    }

    public static float getOrderSubProgress(Order order) {
        OrderCircle status = getVirtualOrderStatus(order);
        return (status.ordinal() + 1) / 16.0f;
    }

    public static float getOrderSubProgressOld(Order order) {
        OrderCircle status = getVirtualOrderStatus(order);
        Order newOrder = OrderCacheManager.get(activity).updateOrder(order);
        if(order.getStatus() == Order.STATUS_WAIT_OUT){
            //虚状态
            if(newOrder.getDisinfectingDate() != null){
                Date date = new Date(toTime(newOrder.getDisinfectingDate()).getTime() + newOrder.getDisinfectingDuration());
                date = toServerTime(date);
                newOrder.setCaringDate(dateFormat.format(date));
                date = new Date(date.getTime()+newOrder.getCaringDuration());
                newOrder.setQualifyingDate(dateFormat.format(date));
                date = new Date(date.getTime()+newOrder.getQualifyingDuration());
                newOrder.setPackingDate(dateFormat.format(date));
                date = new Date(date.getTime()+newOrder.getPackingDuration());
                newOrder.setOutstoreDate(dateFormat.format(date));
            }

        }
        String[] dates = new String[]{order.getCreatedDate(),//OrderCircle.CIRCLE_NEW
                order.getAcceptDate(),//OrderCircle.CIRCLE_ACCEPTED
                getOrderCircleOnDoorDate(order),//OrderCircle.CIRCLE_ONDOORING
                order.getCalPriceDate(),//OrderCircle.CIRCLE_WAITPAY
                order.getPayDate(),//OrderCircle.CIRCLE_PAIED
                order.getPickDate(),//OrderCircle.CIRCLE_PICKED

                order.getInstoreDate(),//OrderCircle.CIRCLE_INSTORE
                order.getUnpackDate(),//OrderCircle.CIRCLE_WASHING
                order.getWashDoneDate(),//OrderCircle.CIRCLE_DISINFECTING
                newOrder.getDisinfectingDate(),//OrderCircle.CIRCLE_CARING
                newOrder.getCaringDate(),//OrderCircle.CIRCLE_QUALIFYING
                newOrder.getQualifyingDate(),//OrderCircle.CIRCLE_PACKING
                newOrder.getPackingDate(),//OrderCircle.CIRCLE_OUTSTORE

                order.getOutstoreDate(),//OrderCircle.CIRCLE_WAYBACK
                order.getArriveDate(),//OrderCircle.CIRCLE_ARRIVED
                order.getCompleteDate(),//OrderCircle.CIRCLE_DONE
        };
        int index = status.ordinal();
        Date start = toTime(dates[index]);

        if(start == null){
            return 1.0f;
        }
        long ms = toServerTime(new Date()).getTime() - start.getTime();
        long second = ms /1000;
        long durationExpect = Integer.MAX_VALUE;
        switch (status){
            case CIRCLE_NEW:
                durationExpect = 10*60;
                break;
            case CIRCLE_ACCEPTED:
                durationExpect = Integer.MAX_VALUE;
                return 0.0f;
            case CIRCLE_ONDOORING:
                durationExpect = 40*60;
                break;
            case CIRCLE_WAITPAY:
                durationExpect = 10*60;
                break;
            case CIRCLE_PAIED:
                durationExpect = 10*60;
                break;
            case CIRCLE_PICKED:
                durationExpect = 10*60;
                break;
            case CIRCLE_INSTORE:
                durationExpect = 90*60;
                break;
            case CIRCLE_WASHING:
                durationExpect = 42*60*60;
                if(order.getContainShoes() == 1){
                    durationExpect = 144*60*60;
                }
                break;
            case CIRCLE_DISINFECTING:
                durationExpect = 15*60;
                break;
            case CIRCLE_CARING:
                durationExpect = 30*60;
                break;
            case CIRCLE_QUALIFYING:
                durationExpect = 10*60;
                break;
            case CIRCLE_PACKING:
                durationExpect = 15*60;
                break;
            case CIRCLE_OUTSTORE:
                durationExpect = 50*60;
                break;
            case CIRCLE_WAYBACK:
                durationExpect = 90*60;
                break;
            case CIRCLE_ARRIVED:
                durationExpect = 21*60*60;
                break;
            case CIRCLE_DONE:
                durationExpect = 24*60*60;
                break;
        }

        return second*1.0f/durationExpect;
    }
    /**
     * 订单用时计算
     * @param order
     * @return
     */
    private static int getOrderTimeSecond(Order order){
        if(order == null){
            return 0;
        }
        //
        long ms = 0;
        String createDateStr = order.getCreatedDate();
        //预约完成
        if(order.getStatus() < Order.STATUS_ACCEPTED){
            if(createDateStr != null) {
                Date createDate = toTime(createDateStr);
                Date now = toServerTime(new Date());
                ms = now.getTime() - createDate.getTime();
            }
            if(ms < 0){
                ms = 0;
            }
            return msToSecond(ms);
        }
        String acceptDateStr = order.getAcceptDate();
        if(createDateStr != null && acceptDateStr != null ){
            Date createDate = toTime(createDateStr);
            Date acceptDate = toTime(acceptDateStr);
            ms = acceptDate.getTime() - createDate.getTime();
        }

        if(ms < 0){
            ms = 0;
        }
        if(order.getStatus() == Order.STATUS_ACCEPTED){
            Date now = toServerTime(new Date());
            Date expectedTime = toTime(order.getExpectDateB());
            Date acceptDate = toTime(acceptDateStr);

            if(expectedTime != null && now.getTime() > expectedTime.getTime()){
                if(acceptDate != null && acceptDate.getTime() > expectedTime.getTime()){
                    ms += now.getTime() - acceptDate.getTime();
                }
            }
            return msToSecond(ms);
        }
        //返洗
        if(order.getType() == 2 && order.getStatus() == Order.STATUS_DONE){
            Date createTime = toTime(order.getCreatedDate());
            //优先使用到达时间
            Date completeTime = toTime(order.getArriveDate());
            if(completeTime == null){
                completeTime = toTime(order.getCompleteDate());
            }
            if(completeTime != null && createTime != null){
                ms = (completeTime.getTime() - createTime.getTime());
                return msToSecond(ms);
            }
        }


        Date now = toServerTime(new Date());
        if(order.getStatus() < Order.STATUS_TO_PICK){
            Date expectedTime = toTime(order.getExpectDateB());
            if(expectedTime != null && now.getTime() > expectedTime.getTime()){
                if(expectedTime != null && now.getTime() > expectedTime.getTime()){
                    ms += (now.getTime() - expectedTime.getTime());
                }
                return msToSecond(ms);
            }
            return msToSecond(ms);
        }

        Date expectedTime = toTime(order.getExpectDateB());
        Date pickedTme = toTime(order.getSetoutDate());
        if(expectedTime == null && pickedTme == null){
            return msToSecond(ms);
        }
        Date onDoorTime = pickedTme;
        if(pickedTme == null){
            onDoorTime = expectedTime;
        }else if(expectedTime == null){
            onDoorTime = pickedTme;
        }else if(pickedTme.getTime() > expectedTime.getTime()){
            onDoorTime = expectedTime;
        }

        //返洗单用创建时间，因为setout时间服务器给错了
        if(order.getType() == 2){
            Date createTime = toTime(order.getCreatedDate());
            ms += (now.getTime() - createTime.getTime());
            return msToSecond(ms);
        }

        if(order.getStatus() == Order.STATUS_DONE || order.getStatus() == Order.STATUS_ARRIVED) {
            //优先使用到达时间
            Date doneDate = toTime(order.getArriveDate());
            if(doneDate == null && order.getCompleteDate() != null){
                doneDate = toTime(order.getCompleteDate());
            }
            if(doneDate != null) {
                ms += (doneDate.getTime() - onDoorTime.getTime());
            }
            return msToSecond(ms);
        }
        ms += (now.getTime()-onDoorTime.getTime());
        return msToSecond(ms);
    }

    /**
     * 显示3大状态
     * @param order
     * @return
     */
    public static String getOrderStatusSummarize(Order order){
        if(order.getStatus() < Order.STATUS_IN_STORE){
            return "取件";
        }else if(order.getStatus() < Order.STATUS_WAY_BACK){
            return "洗涤";
        }else if(order.getStatus() <= Order.STATUS_DONE){
            return "送返";
        }else{
            return "";
        }
    }
    /**
     * 16个状态
     * @param order
     * @return
     */
    public static String getOrderStatus(Order order){
        switch(order.getStatus()){
            case Order.STATUS_NEW:
            case Order.STATUS_ALLOC:
            case Order.STATUS_TO_ASSIGN:
                return "预约完成";
            case Order.STATUS_ACCEPTED:
                Date now = toServerTime(new Date());
                Date expectedTime = toTime(order.getExpectDateB());
                if(expectedTime != null && now.getTime() > expectedTime.getTime()){
                    return "上门中";
                }

                return "待上门";
            case Order.STATUS_TO_PICK:
                if(order.getPayStatus() == 0){
                    return "上门中";
                }
                if(order.getPayStatus() == 10 ||
                        order.getPayStatus() == 20){
                    return "待支付";
                }
                if(order.getPayStatus() == 30){
                    return "已支付";
                }
                return "上门中";
            case Order.STATUS_PICKED:
                return "取件完成";
            case Order.STATUS_IN_STORE:
                return "入库检验";
            case Order.STATUS_WASHING:
                return "洗涤中";
            case Order.STATUS_WAIT_OUT:
                //TODO：虚状态
//            {
//                OrderCircle status = getVirtualOrderStatus(order);
//                if(status == OrderCircle.CIRCLE_DISINFECTING){
//                    return "杀菌中";
//                }else if(status == OrderCircle.CIRCLE_CARING){
//                    return "护理中";
//                }else if(status == OrderCircle.CIRCLE_QUALIFYING){
//                    return "质检中";
//                }else if(status == OrderCircle.CIRCLE_PACKING){
//                    return "包装中";
//                }
//            }
                return "待出库";
            case Order.STATUS_TO_CONFIRM:
                return "待出库";
            case Order.STATUS_WAY_BACK:
                return "送返中";
            case Order.STATUS_ARRIVED:
                return "已送达";
            case Order.STATUS_DONE:
                return "订单完成";
            case Order.STATUS_CANCEL:
                return "订单被取消";
        }
        return order.toString();
    }

    /**
     * 包含虚状态
     * @param order
     * @return
     */
    public static OrderCircle getVirtualOrderStatus(Order order){

        switch(order.getStatus()){
            case Order.STATUS_NEW:
            case Order.STATUS_ALLOC:
            case Order.STATUS_TO_ASSIGN:
                return OrderCircle.CIRCLE_NEW;
            case Order.STATUS_ACCEPTED:
                Date now = toServerTime(new Date());
                Date expectedTime = toTime(order.getExpectDateB());
                if(expectedTime != null && now.getTime() > expectedTime.getTime()){
                    return OrderCircle.CIRCLE_ONDOORING;
                }
                return OrderCircle.CIRCLE_ACCEPTED;
            case Order.STATUS_TO_PICK:
                if(order.getPayStatus() == 0){
                    return OrderCircle.CIRCLE_ONDOORING;
                }
                if(order.getPayStatus() == 10 ||
                        order.getPayStatus() == 20){
                    return OrderCircle.CIRCLE_WAITPAY;
                }
                if(order.getPayStatus() == 30){
                    return OrderCircle.CIRCLE_PAIED;
                }
                return OrderCircle.CIRCLE_ONDOORING;
            case Order.STATUS_PICKED:
                return OrderCircle.CIRCLE_PICKED;
            case Order.STATUS_IN_STORE:
                return OrderCircle.CIRCLE_INSTORE;
            case Order.STATUS_WASHING:
                return OrderCircle.CIRCLE_WASHING;
            case Order.STATUS_WAIT_OUT:
                //TODO：虚状态
            {
                order = OrderCacheManager.get(activity).updateOrder(order);
                Date start = toTime(genDisinfectingDate(order.getWashDoneDate()));
                if(start == null){
                    return OrderCircle.CIRCLE_OUTSTORE;
                }

                long end = start.getTime() + order.getDisinfectingDuration();
                Date date = new Date();
                date = toServerTime(date);
                if(date.getTime() >= start.getTime() && date.getTime() < end){
                    return OrderCircle.CIRCLE_DISINFECTING;
                }
                start = new Date(end);
                end = start.getTime() + order.getCaringDuration();
                if(date.getTime() >= start.getTime() && date.getTime() < end){
                    return OrderCircle.CIRCLE_CARING;
                }
                start = new Date(end);
                end = start.getTime() + order.getQualifyingDuration();
                if(date.getTime() >= start.getTime() && date.getTime() < end){
                    return OrderCircle.CIRCLE_QUALIFYING;
                }
                start = new Date(end);
                end = start.getTime() + order.getPackingDuration();
                if(date.getTime() >= start.getTime() && date.getTime() < end){
                    return OrderCircle.CIRCLE_PACKING;
                }
            }
                return OrderCircle.CIRCLE_OUTSTORE;
            case Order.STATUS_TO_CONFIRM:
                return OrderCircle.CIRCLE_OUTSTORE;
            case Order.STATUS_WAY_BACK:
                return OrderCircle.CIRCLE_WAYBACK;
            case Order.STATUS_ARRIVED:
                return OrderCircle.CIRCLE_ARRIVED;
            case Order.STATUS_DONE:
                return OrderCircle.CIRCLE_DONE;
            default:
                return OrderCircle.CIRCLE_NEW;
        }
    }
    public static String genDisinfectingDate(String washDoneDate){
        Date date = null;
        try {
            date = dateFormat.parse(washDoneDate);
        } catch (Exception e) {
            return washDoneDate;
        }

        if(date.getHours() < 8){
            date.setHours(8);
            date.setMinutes(0);
            date.setSeconds(0);
        }else if(date.getHours() >= 21){
            date.setHours(8);
            date.setMinutes(0);
            date.setSeconds(0);
            date.setDate(date.getDate()+1);
        }
        return dateFormat.format(date);

    }
    public void onRefresh() {
        page = 1;
        UserInfosPref userInfos = UserInfosPref.getInstance(activity);
        if(userInfos.getUser() == null){
            //退出登录
            setHomeLists(null);
            return;
        }
        LocationForService locationForService = UserInfosPref.getInstance(activity).getLocationServer();
        JsonVolley orderDoneVolley = new JsonVolley(activity, Const.Message.MSG_ORDER_DONE_SUCC, Const.Message.MSG_ORDER_DONE_FAIL);
        orderDoneVolley.addParams("page",page);
        orderDoneVolley.addParams("rows",10);
        orderDoneVolley.requestPost(Const.Request.inservice,
                getHandler(), userInfos.getUser() == null ? "" : userInfos.getUser().getToken(),locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict()
        );
    }

    public void onLoadMore() {
        page += 1;
        UserInfosPref userInfos = UserInfosPref.getInstance(activity);
        LocationForService locationForService = UserInfosPref.getInstance(activity).getLocationServer();
        JsonVolley orderDoneVolley = new JsonVolley(activity, Const.Message.MSG_ORDER_DONE_SUCC, Const.Message.MSG_ORDER_DONE_FAIL);
        orderDoneVolley.addParams("rows",10);
        orderDoneVolley.addParams("page",page);
        orderDoneVolley.requestPost(Const.Request.inservice,
                getHandler(), userInfos.getUser() == null ? "" : userInfos.getUser().getToken()
                ,locationForService.getCityCode(),locationForService.getProvince(),locationForService.getAdcode(),locationForService.getDistrict());
    }


}
