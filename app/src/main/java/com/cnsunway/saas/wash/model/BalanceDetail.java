package com.cnsunway.saas.wash.model;

public class BalanceDetail extends BalanceItemParent{

    String extraMemo;
    String changeAmount;
    String paymentAmount;
    String rechargeChannel;
    String source;
    String memo;
    String action;
    String occurDate;
    String changeAmountDesc;
    String currentAmount;
    public String getExtraMemo() {
        return extraMemo;
    }
    public void setExtraMemo(String extraMemo) {
        this.extraMemo = extraMemo;
    }

    public String getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(String changeAmount) {
        this.changeAmount = changeAmount;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getRechargeChannel() {
        return rechargeChannel;
    }

    public void setRechargeChannel(String rechargeChannel) {
        this.rechargeChannel = rechargeChannel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(String occurDate) {
        this.occurDate = occurDate;
    }

    public String getChangeAmountDesc() {
        return changeAmountDesc;
    }

    public void setChangeAmountDesc(String changeAmountDesc) {
        this.changeAmountDesc = changeAmountDesc;
    }

    public String getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(String currentAmount) {
        this.currentAmount = currentAmount;
    }
}
