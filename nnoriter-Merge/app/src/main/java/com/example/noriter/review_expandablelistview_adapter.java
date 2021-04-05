package com.example.noriter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by 전효승 on 2018-03-16.
 */

public class review_expandablelistview_adapter extends BaseExpandableListAdapter {
    private ArrayList<review_listview_item> listViewItemList = new ArrayList<review_listview_item>() ;

    public review_expandablelistview_adapter(){}

    @Override
    public int getGroupCount() {
        return listViewItemList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listViewItemList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listViewItemList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final int pos = groupPosition;
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
        review_listview_item listViewItem = listViewItemList.get(groupPosition);
        // 아이템 내 각 위젯에 데이터 반영
        nameTextView.setText(listViewItem.getname());
        dateTextView.setText(listViewItem.getdate());
        titleTextView.setText(listViewItem.gettitle());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final int pos = groupPosition;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_listview_contents, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView contentsTextView = (TextView) convertView.findViewById(R.id.review_listview_contents_contents) ;


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        review_listview_item listViewItem = listViewItemList.get(groupPosition);
        // 아이템 내 각 위젯에 데이터 반영
        contentsTextView.setText(listViewItem.getContents());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
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
