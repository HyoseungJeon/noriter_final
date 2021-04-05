package com.example.noriter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 전효승 on 2018-03-15.
 */

public class review_listview_adapter extends BaseAdapter {
    private ArrayList<review_listview_item> listViewItemList = new ArrayList<review_listview_item>() ;

    public review_listview_adapter(){}

    @Override
    public int getCount() {return listViewItemList.size();}

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView nameTextView = (TextView) convertView.findViewById(R.id.review_listview_name) ;
        TextView dateTextView = (TextView) convertView.findViewById(R.id.review_listview_date) ;
        TextView titleTextView = (TextView) convertView.findViewById(R.id.review_listview_title) ;


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        review_listview_item listViewItem = listViewItemList.get(position);
        // 아이템 내 각 위젯에 데이터 반영
        nameTextView.setText(listViewItem.getname());
        dateTextView.setText(listViewItem.getdate());
        titleTextView.setText(listViewItem.gettitle());

        return convertView;
    }
    public void addItem(int rindex, int pindex, String name, String date, String title, String Contents) {
        review_listview_item item = new review_listview_item();
        item.setrindex(rindex);
        item.setpindex(pindex);
        item.setname(name);
        item.setdate(date);
        item.settitle(title);
        item.setContents(Contents);

        listViewItemList.add(item);
    }
}
