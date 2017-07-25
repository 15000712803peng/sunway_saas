package com.cnsunway.wash.model;

import java.util.List;

/**
 * Created by LL on 2016/1/11.
 */
public class Cities {

    public Cities(List<String> cities) {
        this.cities = cities;
    }

    public Cities() {
    }



    List<String> cities;


    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }
}
