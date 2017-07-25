package com.cnsunway.wash.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;

/**
 * Created by Administrator on 2015/12/7.
 */
public class PayChoiceDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    RelativeLayout container;
    Display display;
    Dialog dialog;
    View view;
    ImageView cancelImage;
    String amount;

    public interface SelectWepayListener{
        void selectWepay();
    }

    public interface SelectAlipayListener{
        void selectAlipay();
    }

    SelectAlipayListener alipayListener;
    SelectWepayListener wepayListener;

    LinearLayout alipayParent;
    LinearLayout wepayParent;
    TextView ammoutText;


    public void setAlipayListener(SelectAlipayListener alipayListener) {
        this.alipayListener = alipayListener;
    }


    public void setWepayListener(SelectWepayListener wepayListener) {
        this.wepayListener = wepayListener;
    }

    public void setAmmout(String ammout){
        ammoutText.setText("支付金额:" +"  "+ammout +"元");
    }

    public PayChoiceDialog builder() {
        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_pay_choice, null);
        container = (RelativeLayout) view.findViewById(R.id.rl_dialog_container);
        ammoutText = (TextView) view.findViewById(R.id.text_ammount);
        cancelImage = (ImageView) view.findViewById(R.id.image_dialog_cancel);
        cancelImage.setOnClickListener(this);
        dialog = new Dialog(context, R.style.CustomDialog3);
        alipayParent = (LinearLayout) view.findViewById(R.id.alipay_parent);
        alipayParent.setOnClickListener(this);
        wepayParent = (LinearLayout) view.findViewById(R.id.wepay_parent);
        wepayParent.setOnClickListener(this);
        dialog.setContentView(view);
//        dialog.setCancelable(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth()), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        container.setLayoutParams(params);
        dialog.getWindow().setWindowAnimations(R.style.main_menu_animstyle);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return this;
    }




    @Override
    public void onClick(View view) {
        if(view == alipayParent){
            if(alipayListener != null){
                alipayListener.selectAlipay();
            }
        }else if(view == wepayParent){
            if(wepayListener != null){
                wepayListener.selectWepay();
            }
        }else if(view == cancelImage){
            cancel();
        }
    }

    public PayChoiceDialog(final Context context) {

        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();

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
}
