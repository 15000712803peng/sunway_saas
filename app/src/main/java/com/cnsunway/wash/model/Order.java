package com.cnsunway.wash.model;

import com.cnsunway.wash.cnst.Const;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable{

    public static final int STATUS_NEW = 100;
    public static final int STATUS_ALLOC = 120;
    public static final int STATUS_TO_ASSIGN = 140;
    public static final int STATUS_ACCEPTED = 200;
    public static final int STATUS_TO_PICK = 220;
    public static final int STATUS_PICKED = 240;
    public static final int STATUS_IN_STORE = 300;
    public static final int STATUS_IN_FACTORY = 320;
    public static final int STATUS_WASHING = 340;
    public static final int STATUS_WAIT_OUT = 360;
    public static final int STATUS_TO_CONFIRM = 380;
    public static final int STATUS_WAY_BACK = 400;
    public static final int STATUS_ARRIVED = 800;
    public static final int STATUS_DONE = 900;
    public static final int STATUS_CANCEL = 920;
    public static final int STATUS_OUT_RANGE = 990;
    public static final int PAYSTATUS_WAIT = 10;
    public static final int PAYSTATUS_PAYED = 30;

    Evaluate evaluate;
    String memo;
    String pickContact; //颜鹏",
    int type;/*: 1,*/
    String pickLatitude;//: 31.3561,
    String pickLogitude;//: 121.438677,
    String completeDate;//: null,
    String arriveDate;//: null,
    String acceptDate;//: null,
    String userId;//: 16,
    String operaterMobile;//: null,
    boolean gotBags;//: false,
    String assignDate;//: null,
    String freightCharge;//: null,
    String pickAddress;//: "北外环产业园 泰和路2038号d座203",
    String createdDate;//: null,
    String totalPrice;//: null,
    String departDate;//: null,
    String outstoreDate;//: null,
    int status;//: 120,
    String itemsTotalCount;//: null,
    String delivererMobile;//: null,
    String callDate;//: null,
    String delivererName;//: null,
    String pacakgeDate;//: null,
    String orderNo;//: "N151208I9OH8",
    int payStatus;//: 0,
    String instoreDate;//: null,
    String washDoneDate;//: null,
    String operaterName;//: null,
    String expectDateB;//: "2015-12-09 16:00:00",
    String operaterId;//: null,
    String expectDateE;//: "2015-12-09 17:00:00",
    String pickerMobile;//: "15000712803",
    String pickMobile;
    String pickGender;//: 1,
    List<OrderItem> items;
    List<Bag> bags;
    List<Cloth> clothes;
    String statusDes = "";

    String storeNickName;
    String storeNo;
    int clothesNum;



    boolean sharable;
    ShareInfo shareInfo;
    String setoutDate;
    String calPriceDate;
    String payDate;
    String pickDate;
    String unpackDate;//洗涤中的时间就是unpack date
    int statusVirtual;
    String disinfectingDate;
    String caringDate;
    String qualifyingDate;
    String packingDate;
    String pickerName;

    int disinfectingDuration;   //单位是毫秒
    int caringDuration;         //单位是毫秒
    int qualifyingDuration;     //单位是毫秒
    int packingDuration;        //单位是毫秒

    String action;
    DirectInfo directInfo;
    boolean evaluable;
    String freightInfo;

    String siteName;
    String orderTime;
    String deductedPrice;
    boolean clockOn;
    Coupon coupon;
    String deductMemo;

    public String getDeductMemo() {
        return deductMemo;
    }

    public void setDeductMemo(String deductMemo) {
        this.deductMemo = deductMemo;
    }

    public String getDeductedPrice() {
        return deductedPrice;
    }

    public void setDeductedPrice(String deductedPrice) {
        this.deductedPrice = deductedPrice;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public boolean isClockOn() {
        return clockOn;
    }

    public void setClockOn(boolean clockOn) {
        this.clockOn = clockOn;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    int duration = 1000;
    int progress = 60;

    int index = -1;

    public int getClothesNum() {
        return clothesNum;
    }

    public void setClothesNum(int clothesNum) {
        this.clothesNum = clothesNum;
    }

    public String getFreightInfo() {
        return freightInfo;
    }

    public void setFreightInfo(String freightInfo) {
        this.freightInfo = freightInfo;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isEvaluable() {
        return evaluable;
    }

    public void setEvaluable(boolean evaluable) {
        this.evaluable = evaluable;
    }

    public DirectInfo getDirectInfo() {
        return directInfo;
    }

    public void setDirectInfo(DirectInfo directInfo) {
        this.directInfo = directInfo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStatusDes() {
        return statusDes;
    }

    public void setStatusDes(String statusDes) {
        this.statusDes = statusDes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Evaluate getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(Evaluate evaluate) {
        this.evaluate = evaluate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPickContact() {
        return pickContact;
    }

    public void setPickContact(String pickContact) {
        this.pickContact = pickContact;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPickLatitude() {
        return pickLatitude;
    }

    public void setPickLatitude(String pickLatitude) {
        this.pickLatitude = pickLatitude;
    }

    public String getPickLogitude() {
        return pickLogitude;
    }

    public void setPickLogitude(String pickLogitude) {
        this.pickLogitude = pickLogitude;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public String getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(String acceptDate) {
        this.acceptDate = acceptDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOperaterMobile() {
        return operaterMobile;
    }

    public void setOperaterMobile(String operaterMobile) {
        this.operaterMobile = operaterMobile;
    }

    public boolean getGotBags() {
        return gotBags;
    }

    public void setGotBags(boolean gotBags) {
        this.gotBags = gotBags;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getFreightCharge() {
        return freightCharge;
    }

    public void setFreightCharge(String freightCharge) {
        this.freightCharge = freightCharge;
    }

    public String getPickAddress() {
        return pickAddress;
    }

    public void setPickAddress(String pickAddress) {
        this.pickAddress = pickAddress;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getOutstoreDate() {
        return outstoreDate;
    }

    public void setOutstoreDate(String outstoreDate) {
        this.outstoreDate = outstoreDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getItemsTotalCount() {
        return itemsTotalCount;
    }

    public void setItemsTotalCount(String itemsTotalCount) {
        this.itemsTotalCount = itemsTotalCount;
    }

    public String getDelivererMobile() {
        return delivererMobile;
    }

    public void setDelivererMobile(String delivererMobile) {
        this.delivererMobile = delivererMobile;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getDelivererName() {
        return delivererName;
    }

    public void setDelivererName(String delivererName) {
        this.delivererName = delivererName;
    }

    public String getPacakgeDate() {
        return pacakgeDate;
    }

    public void setPacakgeDate(String pacakgeDate) {
        this.pacakgeDate = pacakgeDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public String getInstoreDate() {
        return instoreDate;
    }

    public void setInstoreDate(String instoreDate) {
        this.instoreDate = instoreDate;
    }

    public String getWashDoneDate() {
        return washDoneDate;
    }

    public void setWashDoneDate(String washDoneDate) {
        this.washDoneDate = washDoneDate;
    }

    public String getOperaterName() {
        return operaterName;
    }

    public void setOperaterName(String operaterName) {
        this.operaterName = operaterName;
    }

    public String getExpectDateB() {
        return expectDateB;
    }

    public void setExpectDateB(String expectDateB) {
        this.expectDateB = expectDateB;
    }

    public String getOperaterId() {
        return operaterId;
    }

    public void setOperaterId(String operaterId) {
        this.operaterId = operaterId;
    }

    public String getExpectDateE() {
        return expectDateE;
    }

    public void setExpectDateE(String expectDateE) {
        this.expectDateE = expectDateE;
    }

    public String getPickerMobile() {
        return pickerMobile;
    }
    public void setPickerMobile(String pickMobile) {
        this.pickerMobile = pickMobile;
    }

    public String getPickMobile() {
        return pickMobile;
    }

    public void setPickMobile(String pickMobile) {
        this.pickMobile = pickMobile;
    }
    public String getPickGender() {
        return pickGender;
    }

    public void setPickGender(String pickGender) {
        this.pickGender = pickGender;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public List<Bag> getBags() {
        return bags;
    }

    public void setBags(List<Bag> bags) {
        this.bags = bags;
    }

    public List<Cloth> getClothes() {
        return clothes;
    }

    public void setClothes(List<Cloth> clothes) {
        this.clothes = clothes;
    }

    public String getSetoutDate() {
        return setoutDate;
    }

    public void setSetoutDate(String setoutDate) {
        this.setoutDate = setoutDate;
    }

    public String getCalPriceDate() {
        return calPriceDate;
    }

    public void setCalPriceDate(String calPriceDate) {
        this.calPriceDate = calPriceDate;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getUnpackDate() {
        return unpackDate;
    }

    public void setUnpackDate(String unpackDate) {
        this.unpackDate = unpackDate;
    }

    public int getStatusVirtual() {
        return statusVirtual;
    }

    public void setStatusVirtual(int statusVirtual) {
        this.statusVirtual = statusVirtual;
    }

    public String getDisinfectingDate() {
        return disinfectingDate;
    }

    public void setDisinfectingDate(String disinfectingDate) {
        this.disinfectingDate = disinfectingDate;
    }

    public String getCaringDate() {
        return caringDate;
    }

    public void setCaringDate(String caringDate) {
        this.caringDate = caringDate;
    }

    public String getQualifyingDate() {
        return qualifyingDate;
    }

    public void setQualifyingDate(String qualifyingDate) {
        this.qualifyingDate = qualifyingDate;
    }

    public String getPackingDate() {
        return packingDate;
    }

    public void setPackingDate(String packingDate) {
        this.packingDate = packingDate;
    }

    public int getDisinfectingDuration() {
        return disinfectingDuration;
    }

    public void setDisinfectingDuration(int disinfectingDuration) {
        this.disinfectingDuration = disinfectingDuration;
    }

    public int getCaringDuration() {
        return caringDuration;
    }

    public void setCaringDuration(int caringDuration) {
        this.caringDuration = caringDuration;
    }

    public int getQualifyingDuration() {
        return qualifyingDuration;
    }

    public void setQualifyingDuration(int qualifyingDuration) {
        this.qualifyingDuration = qualifyingDuration;
    }

    public int getPackingDuration() {
        return packingDuration;
    }

    public void setPackingDuration(int packingDuration) {
        this.packingDuration = packingDuration;
    }
    public String getStoreNickName() {
        return storeNickName;
    }

    public void setStoreNickName(String storeNickName) {
        this.storeNickName = storeNickName;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo;
    }

    public boolean isSharable() {
        return sharable;
    }

    public void setSharable(boolean sharable) {
        this.sharable = sharable;
    }

    public ShareInfo getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfo shareInfo) {
        this.shareInfo = shareInfo;
    }
    int containShoes;

    public int getContainShoes() {
        return containShoes;
    }

    public void setContainShoes(int containShoes) {
        this.containShoes = containShoes;
    }


//    public String getOrderStatusStr(Context context) {
//        if (status <= STATUS_ALLOC) {
//            setStatusDes("Booking");
//            return context.getString(R.string.order_status_booking);
//        } else if (status == STATUS_ACCEPT) {
//            setStatusDes("Reserved");
//            return context.getString(R.string.order_status_reserved);
//        } else if (status == STATUS_INPUTBAG) {
//
//            if(payStatus == PAYSTATUS_WAIT || payStatus == 20){
//                setStatusDes("Wait to Pay");
//                return context.getString(R.string.order_status_wait_to_pay);
//            }else if (payStatus == PAYSTATUS_PAYED) {
//                setStatusDes("Paid");
//                return context.getString(R.string.order_status_paid);
//            }else if (payStatus == 40) {
//                setStatusDes("Wait to Pay");
//                return context.getString(R.string.order_status_paid_exception);
//            }else {
//                setStatusDes("Pickup");
//                return context.getString(R.string.order_status_pickup);
//            }
//
//        } else if (status >= STATUS_PICK && status <= STATUS_WASHING) {
//            setStatusDes("Washing");
//            return context.getString(R.string.order_status_washing);
//        } else if (status == STATUS_TO_CONFIRM || status == STATUS_OUTSTORE) {
//            setStatusDes("Wash Finish");
//            return context.getString(R.string.order_status_wash_finish);
//        } else if (status == STATUS_DEPART) {
//            setStatusDes("Sending Back");
//            return context.getString(R.string.order_status_sending_back);
//        } else if (status == STATUS_ARRIVE) {
//            setStatusDes("Delivered");
//            return context.getString(R.string.order_status_delivered);
//        } else if (status == STATUS_COMPLETE) {
//            return "订单完成";
//        } else if (status == STATUS_CANCEL) {
//            return "已取消";
//        }
//        return "" + status;
//    }

    public boolean isCanDel() {
        return payStatus != PAYSTATUS_PAYED;
    }

    public int getOrderStatus(){
        if (status <= STATUS_TO_ASSIGN) {

            return Const.OrderStatus.ORDER_STATUS_BOOKING;
        } else if (status == STATUS_ACCEPTED) {

            return Const.OrderStatus.ORDER_STATUS_RESERVED;
        } else if (status == STATUS_TO_PICK) {
            if(payStatus == PAYSTATUS_WAIT || payStatus == 20){
                return Const.OrderStatus.ORDER_STATUS_WAIT_TO_PAY;
            }else if (payStatus == PAYSTATUS_PAYED) {

                return Const.OrderStatus.ORDER_STATUS_PAID;
            }else if (payStatus == 40) {

                return Const.OrderStatus.ORDER_STATUS_PAID_EXCEPTION;
            }else {

                return Const.OrderStatus.ORDER_STATUS_PICKUP;
            }
        } else if (status <= STATUS_PICKED){

            return Const.OrderStatus.ORDER_STATUS_FETCH_COMPELTE;

        }else if(status <= STATUS_IN_STORE){
            return Const.OrderStatus.ORDER_STATUS_IN_STORE;

        }else if(status <= STATUS_WASHING) {

            return Const.OrderStatus.ORDER_STATUS_WASHING;
        } else if (status == STATUS_TO_CONFIRM || status == STATUS_WAIT_OUT) {

            return Const.OrderStatus.ORDER_STATUS_WASHING_FINISH;
        } else if (status == STATUS_WAY_BACK) {

            return Const.OrderStatus.ORDER_STATUS_SENDING_BACK;
        } else if (status == STATUS_ARRIVED) {

            return Const.OrderStatus.ORDER_STATUS_DELIVERED;
        } else if (status == STATUS_DONE) {
            return Const.OrderStatus.ORDER_STATUS_COMPLETED;
        } else if (status == STATUS_CANCEL) {
            return Const.OrderStatus.ORDER_STATUS_CANCELED;
        }
        return  0;
    }

    public String getPickerName() {
        return pickerName;
    }

    public void setPickerName(String pickerName) {
        this.pickerName = pickerName;
    }

    @Override
    public String toString() {
        return "Order{" +
                "evaluate=" + evaluate +
                ", memo='" + memo + '\'' +
                ", pickContact='" + pickContact + '\'' +
                ", type=" + type +
                ", pickLatitude='" + pickLatitude + '\'' +
                ", pickLogitude='" + pickLogitude + '\'' +
                ", completeDate='" + completeDate + '\'' +
                ", arriveDate='" + arriveDate + '\'' +
                ", acceptDate='" + acceptDate + '\'' +
                ", userId='" + userId + '\'' +
                ", operaterMobile='" + operaterMobile + '\'' +
                ", gotBags=" + gotBags +
                ", assignDate='" + assignDate + '\'' +
                ", freightCharge='" + freightCharge + '\'' +
                ", pickAddress='" + pickAddress + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", departDate='" + departDate + '\'' +
                ", outstoreDate='" + outstoreDate + '\'' +
                ", status=" + status +
                ", itemsTotalCount='" + itemsTotalCount + '\'' +
                ", delivererMobile='" + delivererMobile + '\'' +
                ", callDate='" + callDate + '\'' +
                ", delivererName='" + delivererName + '\'' +
                ", pacakgeDate='" + pacakgeDate + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", payStatus=" + payStatus +
                ", instoreDate='" + instoreDate + '\'' +
                ", washDoneDate='" + washDoneDate + '\'' +
                ", operaterName='" + operaterName + '\'' +
                ", expectDateB='" + expectDateB + '\'' +
                ", operaterId='" + operaterId + '\'' +
                ", expectDateE='" + expectDateE + '\'' +
                ", pickerMobile='" + pickerMobile + '\'' +
                ", pickMobile='" + pickMobile + '\'' +
                ", pickGender='" + pickGender + '\'' +
                ", items=" + items +
                ", bags=" + bags +
                ", clothes=" + clothes +
                ", statusDes='" + statusDes + '\'' +
                ", storeNickName='" + storeNickName + '\'' +
                ", storeNo='" + storeNo + '\'' +
                ", clothesNum=" + clothesNum +
                ", sharable=" + sharable +
                ", shareInfo=" + shareInfo +
                ", setoutDate='" + setoutDate + '\'' +
                ", calPriceDate='" + calPriceDate + '\'' +
                ", payDate='" + payDate + '\'' +
                ", pickDate='" + pickDate + '\'' +
                ", unpackDate='" + unpackDate + '\'' +
                ", statusVirtual=" + statusVirtual +
                ", disinfectingDate='" + disinfectingDate + '\'' +
                ", caringDate='" + caringDate + '\'' +
                ", qualifyingDate='" + qualifyingDate + '\'' +
                ", packingDate='" + packingDate + '\'' +
                ", pickerName='" + pickerName + '\'' +
                ", disinfectingDuration=" + disinfectingDuration +
                ", caringDuration=" + caringDuration +
                ", qualifyingDuration=" + qualifyingDuration +
                ", packingDuration=" + packingDuration +
                ", action='" + action + '\'' +
                ", directInfo=" + directInfo +
                ", evaluable=" + evaluable +
                ", freightInfo='" + freightInfo + '\'' +
                ", siteName='" + siteName + '\'' +
                ", duration=" + duration +
                ", progress=" + progress +
                ", index=" + index +
                ", containShoes=" + containShoes +
                '}';
    }
}
