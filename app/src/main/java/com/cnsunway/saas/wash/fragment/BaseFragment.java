package com.cnsunway.saas.wash.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LL on 2016/1/5.
 */
public abstract  class BaseFragment extends Fragment{

    protected View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragmentDatas();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return getView();
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    protected Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (getActivity() == null ||getActivity().isFinishing()) {
                return;
            }
            handlerMessage(msg);
        }
    };

    public Handler getHandler() {
        return handler;
    }

    protected void dealActivityIntent(Class activity) {
        Intent intent = new Intent(getActivity(), activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void dealActivityIntent(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected abstract void handlerMessage(Message msg);
    protected abstract void initFragmentDatas();
    protected abstract void initMyViews(View view);

}
