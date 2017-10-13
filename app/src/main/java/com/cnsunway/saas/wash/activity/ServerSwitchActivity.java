package com.cnsunway.saas.wash.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cnsunway.saas.wash.R;
import com.cnsunway.saas.wash.cnst.Const;

public class ServerSwitchActivity extends InitActivity {

    private ListView listview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_switch);

        TextView title = (TextView) findViewById(R.id.text_title);
        title.setText("切换服务器");
        listview = (ListView)findViewById(R.id.listview);
        String[] servers = new String[] {Const.Request.WX_SERVER,Const.Request.WXDEV_SERVER,Const.Request.WXTEST_SERVER};
        listview.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,servers));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Const.Request.setServer(Const.Request.WX_SERVER);
                        break;
                    case 1:
                        Const.Request.setServer(Const.Request.WXDEV_SERVER);
                        break;
                    case 2:
                        Const.Request.setServer(Const.Request.WXTEST_SERVER);
                        break;
                }

                setResult(RESULT_OK);
                finish();
            }
        });
    }

    public void back(View view){
        finish();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void handlerMessage(Message msg) {

    }

}
