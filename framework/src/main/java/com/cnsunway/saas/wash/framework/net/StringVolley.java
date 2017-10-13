package com.cnsunway.saas.wash.framework.net;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cnsunway.saas.wash.framework.inter.LoadingDialogInterface;
import com.cnsunway.saas.wash.framework.utils.JsonParser;
import com.cnsunway.saas.wash.framework.utils.Map2KV;
import com.cnsunway.saas.wash.framework.utils.ParamsParser;
import com.cnsunway.saas.wash.framework.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class StringVolley implements Response.ErrorListener, Response.Listener<String> {

    private static RequestQueue myQueue;
    WeakReference<Context> context;
    private int requestSucc = -1;
    private int requestFail = -1;
    Map<String, String> params = new HashMap<String, String>();
    private static final String REQUEST_TYPE = "10";
    private static final String CHANNEL = "22";
    LoadingDialogInterface dialog;
    WeakReference<Handler> handler;
    List<Object> orderedParams = new ArrayList<>();

    public StringVolley(Context context, int requestSucc, int requestFail) {
        this.context = new WeakReference<Context>(context);
        if (myQueue == null) {
            myQueue = Volley.newRequestQueue(context);
        }
        this.requestSucc = requestSucc;
        this.requestFail = requestFail;

    }

    public void addParams(String key, String value) {
        params.put(key, value);
    }

    public void addOrderedParams(Object p) {
        orderedParams.add(p);
    }

    public void addOneOrderedParams(Object p) {
        orderedParams.clear();
        orderedParams.add(p);
    }


    @Override
    public void onErrorResponse(VolleyError volleyError) {

//        Log.e("---------", "response:" + volleyError);
        if (handler != null && handler.get() != null && context != null && context.get() != null) {
            handler.get().obtainMessage(requestFail,
                    context.get().getString(R.string.request_fail)).sendToTarget();
        }
        if (dialog != null) {
            dialog.hideLoading();
        }
    }

    @Override
    public void onResponse(String s) {
        Log.e("---------", "response:" + s);
        if (dialog != null) {
            dialog.hideLoading();
        }
        if (s == null) {
            if (handler != null && handler.get() != null && context != null && context.get() != null)
                handler.get().obtainMessage(requestFail,
                        context.get().getString(R.string.request_fail)).sendToTarget();
            return;
        }

        int code = -1;
        JSONObject obj = null;
        try {
            obj = new JSONObject(s);
            code = obj.getInt("responseCode");
        } catch (JSONException e) {
            e.printStackTrace();
            if (handler != null && handler.get() != null && context != null && context.get() != null) {
                handler.get().obtainMessage(requestFail,
                        context.get().getString(com.cnsunway.saas.wash.framework.R.string.request_fail)).sendToTarget();
            }
        }
        if (handler == null || handler.get() == null) {
            return;
        }
        Message msg = handler.get().obtainMessage(requestSucc);
        if (code == NetParams.RESPONCE_NORMAL) {
            msg.arg1 = NetParams.RESPONCE_NORMAL;
            msg.obj = obj.toString();
        } else {
            msg.arg1 = NetParams.RESPONCE_UNNORAML;
            msg.arg2 = code;
            BaseResp errorResp = (BaseResp) JsonParser.jsonToObject(s, BaseResp.class);
            msg.obj = errorResp.getResponseMsg();
        }
        handler.get().sendMessage(msg);
    }

    public void requestPost(String url, Handler handler, final String token,final String cityCode,
                            final String province,
                            final String adcode,
                            final String district) {
        this.handler = new WeakReference<Handler>(handler);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                this, this) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("x-ldj-ctype", REQUEST_TYPE);
                headers.put("x-ldj-channel", CHANNEL);
                headers.put("x-ldj-token", token);

                headers.put("x-ldj-city",cityCode);
                headers.put("x-ldj-adcode",adcode);
                headers.put("x-ldj-district",district);
                headers.put("x-ldj-province",province);

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myQueue.add(stringRequest);
    }

    public void requestPost(String url, Handler handler, LoadingDialogInterface dialog, String token,final String cityCode,
                            final String province,
                            final String adcode,
                            final String district) {
        this.dialog = dialog;
        dialog.showLoading();
        requestPost(url, handler, token,cityCode,province,adcode,district);
    }


//    public void requestGet(String url, Handler handler, LoadingDialogInterface loadingDialog, final String token,final String cityCode,
//                           final String province,
//                           final String adcode,
//                           final String district) {
//        this.dialog = loadingDialog;
//        dialog.showLoading();
//        requestGet(url, handler, token,cityCode,province,adcode,district);
//
//    }

    public void requestGet(String url, Handler handler, LoadingDialogInterface loadingDialog,final String token,final String cityCode,
                           final String province,
                           final String adcode,
                           final String district) {
        this.dialog = loadingDialog;
        dialog.showLoading();
        requestGet(url, handler,cityCode,province,adcode,district);

    }

    public void _requestGet(String url, Handler handler, LoadingDialogInterface loadingDialog, final String token,final String cityCode,
                            final String province,
                            final String adcode,
                            final String district) {
        this.dialog = loadingDialog;
        dialog.showLoading();
        _requestGet(url, handler, token,cityCode,province,adcode,district);

    }

    public void _requestGet(String url, Handler handler, LoadingDialogInterface loadingDialog,final String cityCode,
                            final String province,
                            final String adcode,
                            final String district) {
        this.dialog = loadingDialog;
        dialog.showLoading();
        _requestGet(url, handler,cityCode,province,adcode,district);

    }

    public void requestGet(String url, Handler handler, final String token,final String cityCode,
                           final String province,
                           final String adcode,
                           final String district) {
        this.handler = new WeakReference<Handler>(handler);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + Map2KV.mapString(params),
                this, this) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("x-ldj-ctype", REQUEST_TYPE);
                headers.put("x-ldj-channel", CHANNEL);
                if (!TextUtils.isEmpty(token)) {
                    headers.put("x-ldj-token", token);
                }

                headers.put("x-ldj-city",cityCode);
                headers.put("x-ldj-adcode",adcode);
                headers.put("x-ldj-district",district);
                headers.put("x-ldj-province",province);

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myQueue.add(stringRequest);
    }

    public void requestGet(String url, Handler handler, final String token,LoadingDialogInterface loadingDialog,final String cityCode,
                           final String province,
                           final String adcode,
                           final String district) {
        this.handler = new WeakReference<Handler>(handler);
        this.dialog = loadingDialog;
        dialog.showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + Map2KV.mapString(params),
                this, this) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("x-ldj-ctype", REQUEST_TYPE);
                headers.put("x-ldj-channel", CHANNEL);
                if (!TextUtils.isEmpty(token)) {
                    headers.put("x-ldj-token", token);
                }

                headers.put("x-ldj-city",cityCode);
                headers.put("x-ldj-adcode",adcode);
                headers.put("x-ldj-district",district);
                headers.put("x-ldj-province",province);

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myQueue.add(stringRequest);
    }

    public void requestGet(String url, Handler handler,LoadingDialogInterface loadingDialog,final String cityCode,
                           final String province,
                           final String adcode,
                           final String district) {
        this.handler = new WeakReference<Handler>(handler);
        this.dialog = loadingDialog;
        dialog.showLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + Map2KV.mapString(params),
                this, this) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("x-ldj-ctype", REQUEST_TYPE);
                headers.put("x-ldj-channel", CHANNEL);
                headers.put("x-ldj-city",cityCode);
                headers.put("x-ldj-adcode",adcode);
                headers.put("x-ldj-district",district);
                headers.put("x-ldj-province",province);

                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myQueue.add(stringRequest);
    }

    public void _requestGet(String url, Handler handler, final String token, final String cityCode,
            final String province,
            final String adcode,
            final String district) {
        this.handler = new WeakReference<Handler>(handler);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
                + ParamsParser.parse(orderedParams), this, this) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("x-ldj-ctype", REQUEST_TYPE);
                headers.put("x-ldj-channel", CHANNEL);
                headers.put("x-ldj-city",cityCode);
                headers.put("x-ldj-adcode",adcode);
                headers.put("x-ldj-district",district);
                headers.put("x-ldj-province",province);
                if (!TextUtils.isEmpty(token)) {
                    headers.put("x-ldj-token", token);
                }

                return headers;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        myQueue.add(stringRequest);

    }


    public void requestGet(String url, Handler handler,final String cityCode,
                           final String province,
                           final String adcode,
                           final String district) {
        this.handler = new WeakReference<Handler>(handler);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                this, this) {
            @Override
            protected Map<String, String> getParams() {

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("x-ldj-ctype", REQUEST_TYPE);
                headers.put("x-ldj-channel", CHANNEL);
                headers.put("x-ldj-city",cityCode);
                headers.put("x-ldj-adcode",adcode);
                headers.put("x-ldj-district",district);
                headers.put("x-ldj-province",province);
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        myQueue.add(stringRequest);
    }

    public void _requestGet(String url, Handler handler,final String cityCode,
                            final String province,
                            final String adcode,
                            final String district) {
        this.handler = new WeakReference<Handler>(handler);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url
                + ParamsParser.parse(orderedParams), this, this) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("x-ldj-ctype", REQUEST_TYPE);
                headers.put("x-ldj-channel", CHANNEL);
                headers.put("x-ldj-city",cityCode);
                headers.put("x-ldj-adcode",adcode);
                headers.put("x-ldj-district",district);
                headers.put("x-ldj-province",province);
                return headers;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        myQueue.add(stringRequest);

    }
}
