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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cnsunway.wash.R;

public class BanlancePayDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    LinearLayout container;
    Display display;
    Dialog dialog;
    View view;
    TextView okBtn;
    TextView cancelBtn;
    TextView balanceAmmountText,totalPriceText;
    String banlanceAmount;
    String totalPrice;

    public interface  OnBanlanceOkClickedLinstener{
        public void banlanceOkClicked();
    }

    OnBanlanceOkClickedLinstener banlanceOkClickedLinstener;


    public void setBanlanceOkLinstener(OnBanlanceOkClickedLinstener banlanceOkClickedLinstener) {
        this.banlanceOkClickedLinstener = banlanceOkClickedLinstener;
    }

    public BanlancePayDialog builder() {

        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_banlance_pay, null);
        container = (LinearLayout) view.findViewById(R.id.rl_dialog_container);
        dialog = new Dialog(context, R.style.CustomDialog2);
        okBtn = (TextView) view.findViewById(R.id.btn_ok);
        okBtn.setOnClickListener(this);
        balanceAmmountText = (TextView) view.findViewById(R.id.tv_dialog_balance_ammount);
        balanceAmmountText.setText(context.getString(R.string.balance_pay_tips) + "￥"+ banlanceAmount);
        cancelBtn = (TextView) view.findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(this);
        dialog.setContentView(view);
        totalPriceText = (TextView) view.findViewById(R.id.tv_dialog_order_ammount);
        totalPriceText.setText(context.getString(R.string.order_total_price_tips) + "￥"+totalPrice);
//        dialog.setCancelable(false);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) (display
                .getWidth() * 0.85), ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        container.setLayoutParams(params);
        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
        return this;
    }


    @Override
    public void onClick(View view) {
        if(view == okBtn){
            cancel();
            if(banlanceOkClickedLinstener != null){
                banlanceOkClickedLinstener.banlanceOkClicked();
            }

        }else if(view == cancelBtn){

            cancel();
        }
    }

    public BanlancePayDialog(final Context context,String balanceAmount,String totalPrice) {
        this.context = context;
        this.banlanceAmount = balanceAmount;
        this.totalPrice = totalPrice;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public void show() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
    public void cancel(){
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }



}
