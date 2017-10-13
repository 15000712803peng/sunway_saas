package com.cnsunway.saas.wash.model;

/**
 * Created by LL on 2015/12/12.
 */
public class OrderPro {

    int duration;
    int progress;

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

    public OrderPro(int progress, int duration) {
        this.duration = duration;
        this.progress = progress;
    }
}
