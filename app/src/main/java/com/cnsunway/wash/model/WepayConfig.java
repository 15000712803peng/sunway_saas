package com.cnsunway.wash.model;

/**
 * Created by LL on 2015/11/27.
 */
public class WepayConfig {

    String appId;
    String nonceStr; //c06d06da9666a219db15cf575aff2824
    String packageName; //prepay_id=wx20151127104241f1842ba9100714204162",
    String sign ; //925877156B87C22DD0B5B369794D664F",
    String signType; //MD5",
    String timeStamp; // 1448592161"
    String prepayid;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    @Override
    public String toString() {
        return "WepayConfig{" +
                "appId='" + appId + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", packageName='" + packageName + '\'' +
                ", sign='" + sign + '\'' +
                ", signType='" + signType + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", prepayid='" + prepayid + '\'' +
                '}';
    }
}
