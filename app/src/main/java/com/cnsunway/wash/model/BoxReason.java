package com.cnsunway.wash.model;

import android.widget.CheckBox;

/**
 * Created by LL on 2016/1/6.
 */
public class BoxReason {
    CheckBox box;
    String reason;
    public CheckBox getBox() {
        return box;
    }

    public void setBox(CheckBox box) {
        this.box = box;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BoxReason(CheckBox box, String reason) {
        this.box = box;
        this.reason = reason;
    }

    public BoxReason(){
        
    }


}
