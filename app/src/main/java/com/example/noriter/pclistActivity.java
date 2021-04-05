package com.example.noriter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by 전효승 on 2018-03-05.
 */

public class pclistActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pclist);

        ListView listView = (ListView) findViewById(R.id.pclist);
        final ListViewAdapter adapter = new ListViewAdapter();
        listView.setAdapter(adapter);


        adapter.addItem(1,ContextCompat.getDrawable(this, R.drawable.xeno),"Xeno","933-0403",
                "서울시 동대문구 용신동",true);
        adapter.addItem(2,ContextCompat.getDrawable(this, R.drawable.pop),"3pop","933-0403",
                "서울시 마포구 흥동",true);
        adapter.addItem(3,ContextCompat.getDrawable(this, R.drawable.ssang),"아프리카","933-0403",
                "서울시 오매가 쓰리동",true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item

                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;


                Drawable iconDrawable = item.getImage() ;
                String name = item.getname() ;
                String number = item.getnumber() ;
                String address = item.getaddress();

                Intent intent = new Intent(getApplicationContext(), pcinfoActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("index",1);
                startActivity(intent);
                // TODO : use item data.
            }
        }) ;
    }
}
