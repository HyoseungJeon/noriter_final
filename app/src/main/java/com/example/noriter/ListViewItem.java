package com.example.noriter;

/**
 * Created by 전효승 on 2018-03-05.
 */
import android.graphics.drawable.Drawable;


public class ListViewItem {
    private int pcindex ;
    private Drawable image ;
    private String name ;
    private String number ;
    private String address ;
    private boolean favorites ;

    public void setImage(Drawable icon) {
        image = icon ;
    }
    public void setname(String title) {
        name = title ;
    }
    public void setnumber(String desc) {
        number = desc ;
    }
    public void setaddress(String adress2) {
        address = adress2 ;
    }
    public void setfavorites(boolean favorites1) { favorites = favorites1 ; }
    public void setpcindex(int pcindex1) { pcindex = pcindex1 ; }

    public Drawable getImage() {
        return this.image ;
    }
    public String getname() {
        return this.name ;
    }
    public String getnumber() {
        return this.number ;
    }
    public String getaddress() {
        return this.address ;
    }
    public boolean getfavorites() { return this.favorites ; }
    public int getpcindex() { return this.pcindex ; }
}
