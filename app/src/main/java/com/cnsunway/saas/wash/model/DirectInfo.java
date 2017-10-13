package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2016/3/11.
 */
public class DirectInfo {
    String directUrl;
    String btnImgUrl;
    String directTitle;
    String btnText;

    public String getDirectTitle() {
        return directTitle;
    }
    public void setDirectTitle(String directTitle) {
        this.directTitle = directTitle;
    }
    public String getDirectUrl() {
        return directUrl;
    }

    public void setDirectUrl(String directUrl) {
        this.directUrl = directUrl;
    }

    public String getBtnImgUrl() {
        return btnImgUrl;
    }

    public void setBtnImgUrl(String btnImgUrl) {
        this.btnImgUrl = btnImgUrl;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }
}
