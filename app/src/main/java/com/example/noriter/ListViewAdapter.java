package com.example.noriter;

/**
 * Created by 전효승 on 2018-03-05.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>() ;
    private String[] favorlist;
    private String favorites;
    private SharedPreferences ad;
    private SharedPreferences.Editor editor;
    public ListViewAdapter() {

    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pc_listview, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView ImageView = (ImageView) convertView.findViewById(R.id.listview_pcimage) ;
        TextView nameTextView = (TextView) convertView.findViewById(R.id.pclistview_name) ;
        TextView numberTextView = (TextView) convertView.findViewById(R.id.pclistview_num) ;
        TextView addressTextView = (TextView) convertView.findViewById(R.id.pclistview_address) ;
        //final CheckBox favoritCheckBox = (CheckBox) convertView.findViewById(R.id.pclistview_favorite)  ;



        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);
        // 아이템 내 각 위젯에 데이터 반영
        ImageView.setImageDrawable(listViewItem.getImage());
        nameTextView.setText(listViewItem.getname());
        numberTextView.setText(listViewItem.getnumber());
        addressTextView.setText(listViewItem.getaddress());


        return convertView;
    }
    public void addItem(int pcindex, Drawable image, String name, String number, String address, boolean favorites) {
        ListViewItem item = new ListViewItem();

        item.setpcindex(pcindex);
        item.setImage(image);
        item.setname(name);
        item.setnumber(number);
        item.setaddress(address);
        item.setfavorites(favorites);

        listViewItemList.add(item);
    }

    public void deleteItem(){

        listViewItemList.clear();

    }

}
