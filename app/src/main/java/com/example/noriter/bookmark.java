package com.example.noriter;

import android.app.Application;

import java.util.ArrayList;

public class bookmark extends Application{
    private ArrayList<Integer> bookmarkid;

    public ArrayList getlist(){
        return bookmarkid;
    }

    public void addlist(int id){
        bookmarkid.add(id);
    }
}
