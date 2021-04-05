package com.example.noriter;

/**
 * Created by 전효승 on 2018-03-10.
 */

public class EventListViewItem {
    private int eventindex ;
    private String eventimage ;
    private String writer;
    private String title ;
    private String date ;
    private String address ;

    public void setwriter(String icon) {
        writer = icon ;
    }
    public void seteventimage(String icon) {
        eventimage = icon ;
    }
    public void settitle(String title1) {
        title = title1 ;
    }
    public void setdate(String desc) {
        date = desc ;
    }
    public void setaddress(String adress2) {
        address = adress2 ;
    }
    public void seteventindex(int pcindex1) { eventindex = pcindex1 ; }

    public String getwriter() {
        return this.writer ;
    }
    public String geteventimage() {
        return this.eventimage ;
    }
    public String gettitle() {
        return this.title ;
    }
    public String getdate() {
        return this.date ;
    }
    public String getaddress() {
        return this.address ;
    }
    public int geteventindex() { return this.eventindex ; }
}
