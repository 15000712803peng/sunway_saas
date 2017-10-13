package com.cnsunway.saas.wash.model;

/**
 * Created by peter on 16/1/14.
 */
public class ShareInfo {
    public String getShareImgUrl() {
        return shareImgUrl;
    }

    public void setShareImgUrl(String shareImgUrl) {
        this.shareImgUrl = shareImgUrl;
    }

    public String getShareText() {
        return shareText;
    }

    public void setShareText(String shareText) {
        this.shareText = shareText;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String shareImgUrl;
    public String shareText;
    public String shareTitle;
    public String shareUrl;
    public String shareBtnText;
    public String appSharePromocode;

    public String getAppSharePromocode() {
        return appSharePromocode;
    }

    public void setAppSharePromocode(String appSharePromocode) {
        this.appSharePromocode = appSharePromocode;
    }

    public String getShareBtnText() {
        return shareBtnText;
    }

    public void setShareBtnText(String shareBtnText) {
        this.shareBtnText = shareBtnText;
    }
}
