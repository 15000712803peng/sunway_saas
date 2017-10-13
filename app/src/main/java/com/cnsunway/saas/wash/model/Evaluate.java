package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2015/12/18.
 */
public class Evaluate {
    private String  orderNo;//
    private int etiquetteScore;//
    private int calpriceScore;//
    private int cleanScore;//
    private int smellScore;//
    private int pickTimeScore;//
    private int sendTimeScore;//
    private String  comment;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getEtiquetteScore() {
        return etiquetteScore;
    }

    public void setEtiquetteScore(int etiquetteScore) {
        this.etiquetteScore = etiquetteScore;
    }

    public int getCalpriceScore() {
        return calpriceScore;
    }

    public void setCalpriceScore(int calpriceScore) {
        this.calpriceScore = calpriceScore;
    }

    public int getCleanScore() {
        return cleanScore;
    }

    public void setCleanScore(int cleanScore) {
        this.cleanScore = cleanScore;
    }

    public int getSmellScore() {
        return smellScore;
    }

    public void setSmellScore(int smellScore) {
        this.smellScore = smellScore;
    }

    public int getPickTimeScore() {
        return pickTimeScore;
    }

    public void setPickTimeScore(int pickTimeScore) {
        this.pickTimeScore = pickTimeScore;
    }

    public int getSendTimeScore() {
        return sendTimeScore;
    }

    public void setSendTimeScore(int sendTimeScore) {
        this.sendTimeScore = sendTimeScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
