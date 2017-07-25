package com.cnsunway.wash.dialog;

/**
 * Created by peter on 16/3/25.
 */

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunway.wash.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/12/7.
 */
public class BottomListDialog implements View.OnClickListener {
    private LoadingDialog loadingDialog;
    private Context context;
    RelativeLayout container;
    ViewGroup llBack;
    Display display;
    Dialog dialog;
    View view;
    ListView listView;
    String[] itemTitles;
    int[] itemTags;
    int[] itemImages;
    int[] itemColors;

    public BottomListDialog builder(String[] titles,int[] tags,int[] colors,int[] images,AdapterView.OnItemClickListener listener) {
        view = LayoutInflater.from(context).inflate(
                R.layout.dialog_bottom, null);
        container = (RelativeLayout) view.findViewById(R.id.rl_dialog_container);
        listView = (ListView)view.findViewById(R.id.listview);
        llBack = (ViewGroup)view.findViewById(R.id.ll_back);
        llBack.setOnClickListener(this);
        itemTitles = titles;
        itemColors = colors;
        itemTags = tags;
        itemImages = images;
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(listener);
        dialog = new Dialog(context, R.style.transparentStyle);
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
    public BottomListDialog builder(String[] titles,int[] tags,AdapterView.OnItemClickListener listener) {
        return builder(titles, tags, null, null, listener);
    }

    @Override
    public void onClick(View view) {
        cancel();
    }

    public BottomListDialog(final Context context) {

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

    private BaseAdapter listAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            if(itemTitles == null) {
                return 0;
            }
            return itemTitles.length;
        }

        @Override
        public Object getItem(int position) {
            if(position >= 0 && position < itemTitles.length) {
                return itemTitles[position];
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            if(position >= 0 && position < itemTags.length) {
                return itemTags[position];
            }
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = View.inflate(context,R.layout.layout_dialog_bottom,null);
                ViewHolder holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            ViewHolder holder = (ViewHolder)convertView.getTag();
            if(itemColors != null) {
                if (position >= 0 && position < itemColors.length) {
                    holder.llItem.setBackgroundColor(context.getResources().getColor(itemColors[position]));
                }
            }
            if(position >= 0 && position < itemTitles.length) {
                holder.tvItem.setText(itemTitles[position]);
            }

            return convertView;
        }
    };

    static class ViewHolder {
        @Bind(R.id.ll_item)
        ViewGroup llItem;
        @Bind(R.id.tv_item)
        TextView tvItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
