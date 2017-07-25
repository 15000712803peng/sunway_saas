package com.cnsunway.wash.data;

import android.graphics.Color;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.PolygonOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class ServiceAreaData {

    public static PolygonOptions getServiceArea(String jsonArray){
        PolygonOptions serviceArea = new PolygonOptions();
        try {
            JSONArray array = new JSONArray(jsonArray);
            for (int i = 0 ; i < array.length();++i){
                JSONObject obj = (JSONObject) array.get(i);
                double lat = obj.getDouble("lat");
                double lng = obj.getDouble("lng");
                serviceArea.add(new LatLng(lat,lng));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        serviceArea1.add(new LatLng(31.338695,121.380805));
//        serviceArea1.add(new LatLng(31.345833,121.394429));
//        serviceArea1.add(new LatLng(31.354977,121.431805));
//        serviceArea1.add(new LatLng(31.375608,121.501409));
//        serviceArea1.add(new LatLng(31.357543,121.507669));
//        //----
//        serviceArea1.add(new LatLng(31.32965,121.56002));
//        serviceArea1.add(new LatLng(31.331959,121.607816));
//        serviceArea1.add(new LatLng(31.338154,121.614156));
//        serviceArea1.add(new LatLng(31.313136,121.495068));
//        serviceArea1.add(new LatLng(31.155699,121.64866));
//
//        // --------
//
//        serviceArea1.add(new LatLng(31.152981,121.619789));
//        serviceArea1.add(new LatLng(31.147215,121.594368));
//        serviceArea1.add(new LatLng(31.143799,121.56208));
//        serviceArea1.add(new LatLng(31.132192,121.644712));
//        serviceArea1.add(new LatLng(31.120569,121.431639));
//
//        //-------
//
//        serviceArea1.add(new LatLng(31.118056,121.389153));
//        serviceArea1.add(new LatLng(31.155803,121.367438));
//        serviceArea1.add(new LatLng(31.183549,121.56208));
//        serviceArea1.add(new LatLng(31.132192,121.358083));
//        serviceArea1.add(new LatLng(31.208221,121.353989));
//
//        //----------
//
//        serviceArea1.add(new LatLng(31.224373,121.348008));
//        serviceArea1.add(new LatLng(31.238469,121.347547));
//        serviceArea1.add(new LatLng(31.250515,121.352461));
//        serviceArea1.add(new LatLng(31.266521,121.359949));
//        serviceArea1.add(new LatLng(31.288901,121.359917));
//
//        //----------

//        serviceArea1.add(new LatLng(31.315234,121.365035));
//        serviceArea1.add(new LatLng(31.332143,121.359627));
        serviceArea.strokeWidth(15)
                .strokeColor(Color.parseColor("#5020B1D9"))
                .fillColor(Color.parseColor("#5020B1D9"));
        return serviceArea;
    }
//    public static PolygonOptions getServiceArea2(){
//
//        serviceArea2 = new PolygonOptions();
//        serviceArea2.add(new LatLng(31.169893,121.342302));
//        serviceArea2.add(new LatLng(31.172886,121.347719));
//        serviceArea2.add(new LatLng(31.182689,121.357438));
//        serviceArea2.add(new LatLng(31.157179,121.36703));
//        serviceArea2.add(new LatLng(31.139008,121.376707));
//        serviceArea2.add(new LatLng(31.117811,121.389293));
//        serviceArea2.add(new LatLng(31.119143,121.415144));
//        serviceArea2.add(new LatLng(31.12334,121.447175));
//        serviceArea2.add(new LatLng(31.119166,121.449469));
//        serviceArea2.add(new LatLng(31.115377,121.450131));
//        serviceArea2.add(new LatLng(31.10514,121.441975));
//        serviceArea2.add(new LatLng(31.100158,121.442596));
//        serviceArea2.add(new LatLng(31.081101,121.440041));
//        serviceArea2.add(new LatLng(31.078574,121.435011));
//        serviceArea2.add(new LatLng(31.078025,121.429537));
//        serviceArea2.add(new LatLng(31.07663,121.424106));
//        serviceArea2.add(new LatLng(31.06774,121.401141));
//        serviceArea2.add(new LatLng(31.108442,121.379579));
//        serviceArea2.add(new LatLng(31.122863,121.370203));
//        serviceArea2.add(new LatLng(31.12042,121.361085));
//        serviceArea2.add(new LatLng(31.124553,121.359069));
//        serviceArea2.add(new LatLng(31.131487,121.351732));
//        serviceArea2.add(new LatLng(31.149991,121.339845));
//        serviceArea2.add(new LatLng(31.165505,121.330533));
//        serviceArea2.strokeWidth(15)
//                .strokeColor(Color.argb(50, 1, 1, 1))
//                .fillColor(Color.argb(50, 1, 1, 1));
//        return serviceArea2;
//    }
}
