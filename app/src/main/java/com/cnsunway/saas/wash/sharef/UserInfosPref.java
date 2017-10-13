package com.cnsunway.saas.wash.sharef;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.cnsunway.saas.wash.cnst.Const;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.model.AddrHistoryItem;
import com.cnsunway.saas.wash.model.HistoryItems;
import com.cnsunway.saas.wash.model.LocationForService;
import com.cnsunway.saas.wash.model.User;

import java.util.List;

public class UserInfosPref {

    private static UserInfosPref instance;

    private SharedPreferences userInfos;

    private UserInfosPref(Context context) {
        userInfos = context.getSharedPreferences("app_user_info", 0);
    }

    public static UserInfosPref getInstance(Context context) {

        if (instance == null) {
            instance = new UserInfosPref(context);
        }
        return instance;
    }

    public void saveUserName(String userName) {
        userInfos.edit().putString("userName", userName).commit();
    }

    public String getUserName() {
        return userInfos.getString("userName", Const.EMPTY);
    }

    public void savePwd(String pwd) {
        userInfos.edit().putString("pwd", pwd).commit();
    }

    public String getPwd() {
        return userInfos.getString("pwd", Const.EMPTY);
    }

    public void setSavePwd(boolean isSave) {
        userInfos.edit().putBoolean("save_pwd", isSave).commit();

    }

    public boolean isSavePwd() {
        return userInfos.getBoolean("save_pwd", false);
    }

	public void saveUser(User user) {
		if(user == null){
			userInfos.edit().putString("user", Const.EMPTY).commit();
			return;
		}
		userInfos.edit().putString("user", JsonParser.objectToJsonStr(user)).commit();
	}
//
//    public void saveCities(Cities cities){
//        if(cities == null){
//            userInfos.edit().putString("cities", Const.EMPTY).commit();
//            return;
//        }
//        userInfos.edit().putString("cities", JsonParser.ObjectToJsonStr(cities)).commit();
//    }

//    public Cities getCities(){
//        String json = userInfos.getString("cities", Const.EMPTY);
//        if (TextUtils.isEmpty(json))
//            return null;
//        return (Cities) JsonParser.jsonToObject(json, Cities.class);
//
//    }
//
//
	public User getUser() {
		String json = userInfos.getString("user", Const.EMPTY);
		if (TextUtils.isEmpty(json))
			return null;
		return (User) JsonParser.jsonToObject(json, User.class);
	}

    public void setUmengToken(String token) {
        if(TextUtils.isEmpty(token)){
            token = "";
        }
        userInfos.edit().putString("umeng_token", token).commit();
    }

    public String getUmengToken() {
        return userInfos.getString("umeng_token", Const.EMPTY);
    }

    public boolean isFirstLogin(){
        return userInfos.getBoolean("first_login", true);
    }

    public void setFirstLogin(boolean isFirst){
        userInfos.edit().putBoolean("first_login", isFirst).commit();
    }

    public boolean isShowMarketing(){
        return userInfos.getBoolean("is_show", false);
    }

    public void hasShowMarkeing(boolean isShow){
        userInfos.edit().putBoolean("is_show", true).commit();
    }



    public void saveCurrentCity(String city){
        userInfos.edit().putString("current_city", city).commit();
    }

    public String getCurrentCity(){
        return userInfos.getString("current_city", Const.EMPTY);
    }

    public static boolean isUpdateCheck = true;

    public boolean isUpdateCheck() {
        return isUpdateCheck;
    }

    public void setUpdateCheck(boolean isUpdateCheck) {
        this.isUpdateCheck = isUpdateCheck;
    }

    public void setServiceCityCode(String cityCode){
        if(TextUtils.isEmpty(cityCode)){
            cityCode = "021";
        }
        userInfos.edit().putString("service_city_code",cityCode).commit();
    }

    public String getServiceCityCode(){
        return userInfos.getString("service_city_code","");
    }

    public void saveLocationForServer(LocationForService locationForService){
        if(locationForService == null){
            userInfos.edit().putString("location_for_service","").commit();
            return;
        }
        userInfos.edit().putString("location_for_service",JsonParser.objectToJsonStr(locationForService)).commit();

    }

    public LocationForService getLocationServer (){
        String locationSerer = userInfos.getString("location_for_service","");
        if(TextUtils.isEmpty(locationSerer)){
            return new LocationForService();

        }

        return (LocationForService) JsonParser.jsonToObject(locationSerer,LocationForService.class);
    }

    public void addHistory(AddrHistoryItem item){
        String historyJson = userInfos.getString("history_items","");
        HistoryItems items = (HistoryItems) JsonParser.jsonToObject(historyJson,HistoryItems.class);
        if(items == null){
            items = new HistoryItems();
            items.getItemList().add(item);
        }else {
            if(!items.getItemList().contains(item)){
                items.getItemList().add(item);
            }
        }

        userInfos.edit().putString("history_items",JsonParser.objectToJsonStr(items)).commit();
    }

    public List<AddrHistoryItem> getAllHistory(){
        String historyItemsStr = userInfos.getString("history_items","");
        HistoryItems items = (HistoryItems) JsonParser.jsonToObject(historyItemsStr,HistoryItems.class);
        if(items != null){
            if(items.getItemList() != null){
                return items.getItemList();
            }
        }
        return null;
    }


}
