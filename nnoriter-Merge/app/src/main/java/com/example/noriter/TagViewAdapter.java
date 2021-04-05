package com.example.noriter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 전효승 on 2018-03-10.
 */

public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.ViewHolder> {
    ArrayList<Drawable> items = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pcinfo_tag,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drawable item = items.get(position);
        holder.tag.setBackground(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void add(Drawable tagimage){
        items.add(tagimage);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView tag;
        public ViewHolder(View itemView){
            super(itemView);
            tag = itemView.findViewById(R.id.pcinfo_tag_imageview);
        }
    }
}

