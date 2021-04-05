package com.example.noriter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 전효승 on 2018-03-10.
 */

public class EventListViewAdapter extends BaseAdapter{

    private ArrayList<EventListViewItem> listViewItemList = new ArrayList<EventListViewItem>() ;

    public EventListViewAdapter(){}

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.event_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        //ImageView ImageView = (ImageView) convertView.findViewById(R.id.event_imageview) ;
        TextView TitleTextView = (TextView) convertView.findViewById(R.id.event_title) ;
        TextView dateTextView = (TextView) convertView.findViewById(R.id.event_date) ;
        TextView addressTextView = (TextView) convertView.findViewById(R.id.event_address) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득

        EventListViewItem listViewItem = listViewItemList.get(position);
        // 아이템 내 각 위젯에 데이터 반영
        //ImageView.setImageDrawable();
        //리스트 이미지 세팅

        TitleTextView.setText(listViewItem.gettitle());
        dateTextView.setText(listViewItem.getdate());
        addressTextView.setText(listViewItem.getaddress());

        return convertView;
    }
    public void addItem(int eventindex, String eventimage, String writer, String title, String date, String address) {
        EventListViewItem item = new EventListViewItem();

        item.seteventimage(eventimage);
        item.setwriter(writer);
        item.settitle(title);
        item.setaddress(address);
        item.setdate(date);

        listViewItemList.add(item);
    }
}
