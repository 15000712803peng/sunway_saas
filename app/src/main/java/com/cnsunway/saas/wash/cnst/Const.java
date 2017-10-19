package com.cnsunway.saas.wash.cnst;

public class Const {
    public static final String TOKEN_TEST = "2e0963ca976d11e5b1fa8038bc0b555d";
    public static final String EMPTY = "";
    private static final String PACKAGE_NAME = "com.cnsunway.saas.wash";
    public static final String AMAP_KEY = "c400c88e96e075b82e8bdd0719577f82";
    public static final String WE_APP_ID = "wx70f2574b97ad060b";
    public static final String MCH_ID = "1394051902";
    public static final int MAN = 1;
    public static final int WOMAN = 2;
    public static final String MARKET_BALANCE = "BALANCE_RECHARGE";

    public static class Request {
        public static final int REQUEST_SUCC = 0;
        public static final int REQUEST_FAIL = 1;
        //        public static final String WX_SERVER = "http://wx.landaojia.com/ldj-app";
//        public static final String WX_SERVER = "http://wx.sunwayxiyi.com/ldj-app";
        public static final String WX_SERVER = "https://saas-app.sunwayxiyi.com/saas-app";
        public static final String WXTEST_SERVER = "http://wxtest.landaojia.com/ldj-app";
        public static final String WXDEV_SERVER = "http://wxdev.landaojia.com/ldj-app";
        public static final String TEMP_SERVER = "http://10.0.11.143:9090/ldj-app";
        public static String SERVER = "";
        public static String code;
        public static String login;
        public static String ordersearch;
        public static String homeinservice;
        public static String inservice;
        public static String shippingFee;
        public static String create;
        public static String done;
        public static String cancel;
        public static String detail;
        public static String search;
        public static String coupon;
        public static String markDefault;
        public static String deleteAddr;
        public static String creaetAddr;
        public static String updateAddr;
        public static String showDefaultAddr;
        public static String all;
        public static String pay;
        public static String confirmPay;
        public static String confirmDone;
        public static String payCancel;
        public static String addEvaluate;
        public static String settleAccounts;
        public static String getOrderPayDetail;
        public static String getPrePayInfo;
        public static String checkUpdate;
        public static String cityList;
        public static String showImages;
        public static String uploadPic;
        public static String operatoresSave;
        public static String profile;
        public static String startRecharge;
        public static String balancelog;
        public static String rechargeSucces;
        public static String exchangeCoupon;
        public static String confirmReceive;
        public static String priceList;
        public static String storeShow;
        public static String question;
        public static String about;
        public static String balance;//余额明细
        public static String invoices;
        public static String agreement;
        public static String rechargeagreement;
        public static String getFreightRule;
        public static String newBalance;
        public static String  notifyPaySuccess;
        public static String donePage;
        public static String categoris;
        public static String serviceprocess;
        public static String servicerange;
        public static String healthwash;
        public static String pastCoupons;
        public static String paySuccess;
        public static String log;
        public static String addrDetail;
        public static String shareKey;
        public static String cardList;
        public static String cardConsumeDetail;
        public static String bind;
        public static String messageAll;
        public static String messageRead;
        public static String allCityStores;
        public static String recharge;
        public static String hxAccount;
        public static String recommendStores;
        public static String storeDetail;
        public static String storeCategories;
        public static String products;
        public static String inServiceStores;
        public static String payBanlance;
        public static String evaluate;
        public static void setServer(String baseUrl) {
            SERVER = baseUrl;
            code = SERVER + "/user/login/send_verify_code";
            login = SERVER + "/user/login";
            ordersearch = SERVER + "/orders/search";
            homeinservice = SERVER + "/orders/searchOrdersInService";
            inservice = SERVER + "/orders";
            donePage = SERVER + "/orders/donePage";
            shippingFee = SERVER + "/orders/getShippingInfo";
            create = SERVER + "/orders/create";
            done = SERVER + "/orders/done";
            cancel = SERVER + "/orders/cancel";
            detail = SERVER + "/orders";
            search = SERVER +"/user/address/list";        //地址
            coupon = SERVER + "/accounts/coupons";
            markDefault = SERVER + "/useraddresses/mark_default"; //设置默认地址
            deleteAddr = SERVER + "/user/address/delete"; //删除地址
            creaetAddr = SERVER + "/user/address/create";            //创建地址
            updateAddr = SERVER + "/user/address/update";     //   修改地址
            showDefaultAddr = SERVER + "/user/address/default";   //展示默认地址
            all = SERVER + "/user/account/balance";
            pay = SERVER + "/pay/order";
            confirmPay = SERVER + "/pay/confirmPay";
            confirmDone = SERVER + "/orders";
            payCancel = SERVER + "/pay/cancelPay";
            addEvaluate = SERVER + "/orders/addEvaluate";
            settleAccounts = SERVER + "/pay/settleAccounts";
            getOrderPayDetail = SERVER + "/pay/getOrderPayDetail";
            getPrePayInfo = SERVER + "/pay/getPrePayInfo";
            notifyPaySuccess = SERVER +"/pay/notifyPaySuccess";
            checkUpdate = SERVER + "/version/check";
            cityList = SERVER + "/cities/all";
            showImages = SERVER + "/banners";
            uploadPic = SERVER + "/pic/upload_info";
            operatoresSave = SERVER + "/users/save";
            profile = SERVER + "/users/profile";
            startRecharge = SERVER + "/deposit/startRecharge";
            balancelog = SERVER + "/accounts/balance/log";
            rechargeSucces = SERVER + "/deposit/rechargeSucces";
            exchangeCoupon = SERVER + "/redPackets/exchangeCoupon";
            confirmReceive = SERVER + "/orders/addEvaluate";
            log = SERVER + "/promotion/share/log";
            shareKey = SERVER + "/promotion/share/key";
            categoris = SERVER + "/categoris";
            recommendStores = SERVER + "/stores/recommend";
            priceList = SERVER.replace("/ldj-app", "/") + "#/priceList";
            storeShow = SERVER.replace("/ldj-app", "/") + "#/storeShow";
            agreement = SERVER.replace("/ldj-app", "/") + "#/agreements";
            question = SERVER.replace("/ldj-app", "/") + "#/qa";
            about = SERVER.replace("/ldj-app", "/") + "#/aboutUs";
            balance = SERVER.replace("/ldj-app", "/") + "#/account";
            invoices = SERVER.replace("/ldj-app", "/") + "market/invoices/index.html";
            rechargeagreement = SERVER.replace("/ldj-app", "/") + "index.html#/tabs/depositAgreement";
            getFreightRule = SERVER + "/orders/getFreightRule";
            newBalance = SERVER.replace("/ldj-app", "/") + "#/recharge";
            serviceprocess = SERVER.replace("/ldj-app", "/") + "#/serviceProcess";
            servicerange = SERVER.replace("/ldj-app", "/") + "#/servicerange";
            healthwash = SERVER.replace("/ldj-app", "/") + "#/healthWash";
            pastCoupons = SERVER + "/accounts/coupons/past";
            paySuccess = SERVER.replace("/ldj-app", "/") + "market/pay_success/paySuccess.html?amount=";
            addrDetail = SERVER + "/useraddresses/detail";
            cardList = SERVER +"/cards/balance/search";
            cardConsumeDetail = SERVER +"/cards/consume/search";
            bind = SERVER + "/cards/activate";
            messageAll = SERVER +"/message/findAll";
            messageRead = SERVER + "/message/read";
            allCityStores = SERVER +"/cities/service_range";
            recharge = SERVER + "#/recharge";
            hxAccount = SERVER + "/users/ocs/account";
            storeDetail =SERVER + "/stores";

                    storeCategories = SERVER +"/stores";
            products = SERVER +"/stores";
            inServiceStores = SERVER +"/stores/in_service";
            payBanlance = SERVER + "/user/account/pay";
            evaluate =SERVER + "/orders";
        }

        static {
            setServer(WX_SERVER);
//            setServer(WXDEV_SERVER);
//           setServer(TEMP_SERVER);
        }
    }

    public static class PhoneRegex {
        public static final String PHONE = "1[03456789]\\d{9}";
    }

    public static class MyAction {
        public static final String ACTION_REFRESH_INSERVICE_ORDERS = PACKAGE_NAME + "action_refresh_inservice_orders";
    }

    public static class MyFilter {
        public static final String FILTER_REFRESH_INSERVICE_ORDERS = PACKAGE_NAME + ".filter_refresh_inservice_orders";
        public static final String FILTER_WE_PAY_RESULT = PACKAGE_NAME + ".we_pay_result";
        public static final String FILTER_REFRESH_HOME_ORDERS = PACKAGE_NAME + ".filter_refresh_home_orders";
        public static final String FILTER_REFRESH_ORDERS = PACKAGE_NAME + ".filter_refresh_orders";
        public static final String FILTER_REFRESH_TABS = PACKAGE_NAME + ".filter_refresh_tabs";
        public static final String FILTER_REFRESH_MINE_TABS = PACKAGE_NAME + ".filter_refresh_mine_tabs";
        public static final String FILTER_REFRESH_ADAPTER = PACKAGE_NAME + ".filter_refresh_adapter";
        public static final String FILTER_SHARE_SUCC = PACKAGE_NAME + ".filter_share_succ";
        public static final String FILTER_REFRESH_MESSAGE = PACKAGE_NAME + ".filter_refresh_message";
        public static final String FILTER_REFRESH_MESSAGE_COUNT = PACKAGE_NAME+".filter_refresh_message_count";
        public static final String FILTER_CITY_CHANGED = PACKAGE_NAME+".city_changed";
        public static final String FILTER_LOG_OUT = PACKAGE_NAME+".log_out";
        public static final String FILTER_HAS_NEW_HX_MESSAGE = PACKAGE_NAME+".filter_has_new_hx_message";
    }
    public static final String DEFAULT_CUSTOMER_APPKEY = "1129170725178435#kefuchannelapp45090";
    public static final String DEFAULT_CUSTOMER_ACCOUNT = "kefuchannelimid_720631";
    public static final String DEFAULT_ACCOUNT_PWD = "123456";
    public static final String DEFAULT_NICK_NAME = "";
    public static final String DEFAULT_TENANT_ID = "45083";
    public static final String DEFAULT_PROJECT_ID = "306713";

    public static final int MESSAGE_TO_DEFAULT = 0;
    public static final int MESSAGE_TO_PRE_SALES = 1;
    public static final int MESSAGE_TO_AFTER_SALES = 2;
    public static final String MESSAGE_TO_INTENT_EXTRA = "message_to";
    public static final String INTENT_CODE_IMG_SELECTED_KEY = "img_selected";
    public static final int INTENT_CODE_IMG_SELECTED_DEFAULT = 0;

    public static class Message {
        public static final int MSG_GET_CODE_SUCC = 1;
        public static final int MSG_GET_CODE_FAIL = 2;
        public static final int MSG_LOGIN_SUCC = 3;
        public static final int MSG_LOGIN_FAIL = 4;
        public static final int MSG_IN_SERVICE_SUCC = 5;
        public static final int MSG_IN_SERVICE_FAIL = 6;
        public static final int MSG_CREATE_ORDER_SUCC = 7;
        public static final int MSG_CREATE_ORDER_FAIL = 8;
        public static final int MSG_ORDER_DONE_SUCC = 9;
        public static final int MSG_ORDER_DONE_FAIL = 10;
        public static final int MSG_ORDER_CANCEL_SUCC = 11;
        public static final int MSG_ORDER_CANCEL_FAIL = 12;
        public static final int MSG_ORDER_DETAIL_SUCC = 13;
        public static final int MSG_ORDER_DETAIL_FAIL = 14;
        public static final int MSG_COUPON_LIST_SUCC = 15;
        public static final int MSG_COUPON_LIST_FAIL = 16;
        public static final int MSG_MORE_COUPON_SUCC = 17;
        public static final int MSG_MORE_COUPON_FAIL = 18;
        public static final int MSG_DEL_ADDR_SUCC = 19;
        public static final int MSG_DEL_ADDR_FAIL = 20;
        public static final int MSG_SEARCH_ADDR_SUCC = 21;
        public static final int MSG_SEARCH_ADDR_FAIL = 22;
        public static final int MSG_MARK_DEFAULT_SUCC = 23;
        public static final int MSG_MARK_DEFAULT_FAIL = 24;
        public static final int MSG_CREATE_ADDR_SUCC = 25;
        public static final int MSG_CREATE_ADDR_FAIL = 26;

        public static final int MSG_SHOW_DEFAULT_ADDR_SUCC = 27;
        public static final int MSG_SHOW_DEFAULT_ADDR_FAIL = 28;

        public static final int MSG_GET_PAY_SUCC = 29;
        public static final int MSG_GET_PAY_FAIL = 30;

        public static final int MSG_PAY_CONFIRM_SUCC = 31;
        public static final int MSG_PAY_CONFIRM_FAIL = 32;

        public static final int MSG_CONFIRM_DONE_SUCC = 33;
        public static final int MSG_CONFIRM_DONE_FAIL = 34;

        public static final int MSG_PAY_CANCEL_SUCC = 35;
        public static final int MSG_PAY_CANCEL_FAIL = 36;

        public static final int MSG_EVALUATE_SUCC = 37;
        public static final int MSG_EVALUATE_FAIL = 38;

        public static final int MSG_HOME_IN_SERVICE_SUCC = 39;
        public static final int MSG_HOME_IN_SERVICE_FAIL = 40;

        public static final int MSG_SHAKE = 41;

        public static final int MSG_SETTLE_ACCOUNTS_SUCC = 42;
        public static final int MSG_SETTLE_ACCOUNTS_FAIL = 43;
        public static final int MSG_GET_ORDER_PAY_DETAIL_SUCC = 44;
        public static final int MSG_GET_ORDER_PAY_DETAIL_FAIL = 45;
        public static final int MSG_GET_PAY_INFO_SUCC = 46;
        public static final int MSG_GET_PAY_INFO_FAIL = 47;
        public static final int MSG_GET_DEVICE_TOKEN = 48;

        public static final int MSG_DOWNLOAD_START = 49;
        public static final int MSG_DOWNLOAD = 50;
        public static final int MSG_DOWNLOAD_FINISH = 51;
        public static final int MSG_DOWNLOAD_FAIL = 52;

        public static final int MSG_CHECK_UPGRADE_SUCC = 53;
        public static final int MSG_CHECK_UPGRADE_FAIL = 54;


        public static final int MSG_GET_CITY_SUCC = 55;
        public static final int MSG_GET_CITY_FAIL = 56;

        public static final int MSG_GET_CITIES_SUCC = 57;
        public static final int MSG_GET_CITIES_FAIL = 58;

        public static final int MSG_GET_IMAGES_SUCC = 59;
        public static final int MSG_GET_IMAGES_FAIL = 60;

        public static final int MSG_UPLOADTOKEN_SUCC = 61;
        public static final int MSG_UPLOADTOKEN_FAIL = 62;
        public static final int MSG_UPLOADHEAD_SUCC = 63;
        public static final int MSG_UPLOADHEAD_FAIL = 64;
        public static final int MSG_PROFILE_SUCC = 65;
        public static final int MSG_PROFILE_FAIL = 66;
        public static final int MSG_SHIPPINGFEE_SUCC = 67;
        public static final int MSG_SHIPPINGFEE_FAIL = 68;

        public static final int MSG_ACCOUNT_ALL_SUCC = 95;
        public static final int MSG_ACCOUNT_ALL_FAIL = 96;


        public static final int MSG_SECEND_COUNT = 100;
        public static final int MSG_ALIPAY_RESULT = 101;

        public static final int MSG_START_RECHAEGE_SUCC = 102;
        public static final int MSG_START_RECHAEGE_FAIL = 103;
        public static final int MSG_GET_BANLANCE_LOG_SUCC = 104;
        public static final int MSG_GET_BANLANCE_LOG_FAIL = 105;

        public static final int MSG_RECHARGE_NOTIFY_SUCC = 106;
        public static final int MSG_RECHARGE_NOTIFY_FAIL = 107;
        public static final int MSG_EXCHANGE_COUPON_SUCC = 108;
        public static final int MSG_EXCHANGE_COUPON_FAIL = 109;

        public static final int MSG_GET_FREIGHT_RULE_SUCC = 110;
        public static final int MSG_GET_FREIGHT_RULE_FAIL = 111;
        public static final int MSG_WEB_OPERATION_PAY = 112;
        public static final int MSG_WEB_OPERATION_SHARE = 113;
        public static final int MSG_WEB_OPERATION_GOBACK = 114;
        public static final int MSG_PROGRESS_INCREASED = 115;

        public static final int MSG_CATEGORIS_SUCC = 116;
        public static final int MSG_CATEGORIS_FAIL = 117;
        public static final int MSG_PAST_COUPONS_SUCC = 118;
        public static final int MSG_PAST_COUPONS_FAIL = 119;

        public static final int LOG_SUCC = 120;
        public static final int LOG_FAIL= 121;
        public static final int MSG_ADDR_DETAIL_SUCC = 122;
        public static final int MSG_ADDR_DETAIL_FAIL = 123;
        public static final int MSG_SHARE_KEY_SUCC = 124;
        public static final int MSG_SHARE_KEY_FAIL = 125;
        public static final int MSG_GET_USER_SUCC = 126;
        public static final int MSG_GET_USER_FAIL = 127;
        public static final int MSG_CARD_LIST_SUCC = 128;
        public static final int MSG_CARD_LIST_FAIL = 129;

        public static final int MSG_BIND_SUCC = 130;
        public static final int MSG_BIND_FAIL = 131;

        public static final int MSG_SERVICE_CITY_SUCC = 132;
        public static final int MSG_SERVICE_CITY_FAIL = 133;

        public static final int MSG_MESSAGE_SUCC = 134;
        public static final int MSG_MESSAGE_FAIL = 135;
        public static final int MSG_READ_SUCC = 136;
        public static final int MSG_READ_FAIL = 137;

        public static final int MSG_GET_ALL_STORIES_SUCC = 138;
        public static final int MSG_GET_ALL_STORIES_FAIL = 139;
        public static final int MSG_GET_HX_ACCOUNT_SUCC = 140;
        public static final int MSG_GET_HX_ACCOUNT_FAIL = 141;

        public static final int MSG_GET_RECC_STORES_SUCC = 142;
        public static final int MSG_GET_RECC_STORES_FAIL = 143;

        public static final int MSG_GET_STORE_DETAIL_SUCC = 144;
        public static final int MSG_GET_STORE_DETAIL_FAIL = 145;

        public static final int MSG_GET_STORE_CATEGORIES_SUCC = 146;
        public static final int MSG_GET_STORE_CATEGORIES_FAIL = 147;

        public static final int MSG_GET_PRODUCTS_SUCC = 148;
        public static final int MSG_GET_PRODUCTS_FAIL = 149;

        public static final int MSG_GET_PAY_BANLANCE_SUCC = 150;
        public static final int MSG_GET_PAY_BANLANCE_FAIL = 151;

    }

    public static class CouponStatus {
        public static final int STATUS_COUPON_VALID = 1;
        public static final int STATUS_COUPON_LOCKED = 2;
        public static final int STATUS_COUPON_USED = 3;
        public static final int STATUS_COUPON_EXPIRED = 4;
    }

    public static class HxConfig{
        public static final String DEFAULT_CUSTOMER_APPKEY = "1162170807178812#kefuchannelapp45083";
        public static final String DEFAULT_TENANT_ID = "45083";
        public static final String DEFAULT_CUSTOMER_ACCOUNT = "kefuchannelimid_889432";

    }

    public static class OrderStatus {
        public static final int ORDER_STATUS_BOOKING = 100; //预约完成
        public static final int ORDER_STATUS_RESERVED = 101; //待上门
        public static final int ORDER_STATUS_PICKUP = 102; //上门中
        public static final int ORDER_STATUS_WAIT_TO_PAY = 103; //待支付
        public static final int ORDER_STATUS_PAID = 104; //已支付
        public static final int ORDER_STATUS_PAID_EXCEPTION = 105;//支付异常
        public static final int ORDER_STATUS_FETCH_COMPELTE = 106;//取件完成
        public static final int ORDER_STATUS_IN_STORE = 107;
        public static final int ORDER_STATUS_WASHING = 108;//洗涤中
        public static final int ORDER_STATUS_WASHING_FINISH = 109;//洗涤完成
        public static final int ORDER_STATUS_SENDING_BACK = 110;//送返中
        public static final int ORDER_STATUS_DELIVERED = 111;//已送达
        public static final int ORDER_STATUS_WASH_AGAIN = 112;//返洗中
        public static final int ORDER_STATUS_COMPLETED = 113;//订单完成
        public static final int ORDER_STATUS_CANCELED = 114;//订单取消
    }

    public static class Action {
        public static final String ACTION_LOCATION_SUCCEED = "Action_LocationSucceed";
    }

    public static class TAB{
        public static final int TAB_HOME = 0;
        public static final int TAB_ORDER = 1;
        public static final int TAB_DO_ORDER = 2;
        public static final int TAB_NOTIFICATION = 3;
        public static final int TAB_MINE = 4;
    }

}
