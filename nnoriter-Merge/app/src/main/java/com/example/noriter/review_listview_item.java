package com.example.noriter;

import android.graphics.drawable.Drawable;

/**
 * Created by 전효승 on 2018-03-15.
 */

public class review_listview_item {
    private int rindex;
    private int pindex;
    private String name;
    private String date;
    private String title;
    private String Contents;

    public void setrindex(int rindex1) {
        rindex = rindex1 ;
    }
    public void setpindex(int pindex1) {
        pindex = pindex1 ;
    }
    public void setname(String name1) {
        name = name1 ;
    }
    public void setdate(String date1) {
        date = date1 ;
    }
    public void settitle(String title1) { title = title1 ; }
    public void setContents(String Contents1) { Contents = Contents1 ; }

    public int getrindex() {
        return this.rindex ;
    }
    public int getpindex() { return this.pindex ; }
    public String getname() { return this.name ; }
    public String getdate() { return this.date ; }
    public String gettitle() { return this.title ; }
    public String getContents() { return this.Contents ; }

}
