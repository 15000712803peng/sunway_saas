package com.cnsunway.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;
import com.cnsunway.wash.adapter.ServiceCityAdapter;
import com.cnsunway.wash.cnst.Const;
import com.cnsunway.wash.framework.inter.LoadingDialogInterface;
import com.cnsunway.wash.framework.net.StringVolley;
import com.cnsunway.wash.model.ServiceCity;
import com.cnsunway.wash.model.ServiceCitys;
import com.cnsunway.wash.sharef.UserInfosPref;

import java.util.List;
import android.os.Handler;
import java.util.logging.LogRecord;

/**
 * Created by hp on 2017/6/15.
 */
public class ServiceCityDialog implements AdapterView.OnItemClickListener {
    Dialog dialog;
    private Context context;
    View view;
    RelativeLayout container;
    TextView cancel,currentCity;
    Display display;
    ListView optionalCity;
    //    ServiceCitys serviceCitys;
    List<ServiceCity> cities;
    ServiceCity defaultCity;


    public boolean isShowing(){
        if(dialog != null){
            return  dialog.isShowing();
        }
        return  false;
    }

    public  interface OnCitySelectedLinstenr{
        void onCitySelected(ServiceCity city);
    }

    OnCitySelectedLinstenr onCitySelectedLinstenr;



    public void setOnCitySelectedLinstenr(OnCitySelectedLinstenr onCitySelectedLinstenr) {
        this.onCitySelectedLinstenr = onCitySelectedLinstenr;
    }

    public List<ServiceCity> getCities() {
        return cities;
    }

    public void setCities(List<ServiceCity> cities) {
        this.cities = cities;
        initList(cities);
    }

    Handler handler;

    ServiceCityAdapter serviceCityAdapter;
    private LoadingDialog loadingDialog;
    public ServiceCityDialog(final Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        defaultCity = new ServiceCity();
        defaultCity.setCityName("上海市");
        defaultCity.setCityCode("021");

    }

    ServiceCity cityData;
    public void setCurrentCity(ServiceCity city){
        if(city == null){
            city = defaultCity;
        }
        cityData = city;
        if(city != null && !TextUtils.isEmpty(city.getCityName())){
            currentCity.setText(city.getCityName());
        }

        if(serviceCityAdapter != null){
            serviceCityAdapter.setCurrentCity(cityData);
            serviceCityAdapter.notifyDataSetChanged();
        }
    }

    public ServiceCityDialog builder() {
        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_city_select, null);
        container = (RelativeLayout) view.findViewById(R.id.rl_dialog_container);


        cancel = (TextView) view.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        currentCity = (TextView) view.findViewById(R.id.tv_current_city);
        optionalCity = (ListView) view.findViewById(R.id.lv_optional_city);

        dialog = new Dialog(context, R.style.CustomDialog3);
        dialog.setContentView(view);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth()*0.96), (int) (display
                .getHeight()*0.90));
        params.gravity = Gravity.BOTTOM;
        container.setLayoutParams(params);
        dialog.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        dialog.getWindow().setGravity(Gravity.BOTTOM);


        return this;

    }
    private void initList(List<ServiceCity> serviceCitys){
        if (serviceCityAdapter == null){
            serviceCityAdapter = new ServiceCityAdapter(context,serviceCitys);
            serviceCityAdapter.setServiceCitys(serviceCitys);
            optionalCity.setAdapter(serviceCityAdapter);
            optionalCity.setOnItemClickListener(this);

        }else {
            serviceCityAdapter.getServiceCitys().clear();
            serviceCityAdapter.getServiceCitys().addAll(serviceCitys);
            serviceCityAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        ServiceCity serviceCity = (ServiceCity) adapterView.getAdapter().getItem(position);
        if(onCitySelectedLinstenr != null){
            onCitySelectedLinstenr.onCitySelected(serviceCity);
        }
        cancel();
    }

    private LoadingDialog getLoadingDialog(String message) {

        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
        loadingDialog = new LoadingDialog(context, message);
        return loadingDialog;

    }
    protected void hideLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.cancel();
        }
    }
    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
    public void cancel() {
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }
    protected void showMessageToast(String message) {
        OperationToast.showOperationResult(context.getApplicationContext(),
                message, 0);
    }
}
