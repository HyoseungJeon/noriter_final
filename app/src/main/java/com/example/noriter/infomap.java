package com.example.noriter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
public class infomap extends AppCompatActivity{
    double lo_x;
    double lo_y;
    String name;
    public static NearActivity getInstance() { return new NearActivity(); }
    @Override
    protected void onCreate(@Nullable Bundle bundle){
        super.onCreate(bundle);
        boolean isGrantStorage = grantExternalStoragePermission();
        if(isGrantStorage) {
            setContentView(R.layout.pcmap);
            Intent intent = getIntent();
            lo_x=intent.getDoubleExtra("lo_x",0);
            lo_y=intent.getDoubleExtra("lo_y",0);
            name = intent.getStringExtra("name");
            callmap();


        }
    }
    private boolean grantExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
        }else{
            Toast.makeText(this, "External Storage Permission is Grant", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= 23) {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                //resume tasks needing this permission
            }
        }
    }
    private void callmap() {
        fragment3 fragment = new fragment3();
        Bundle bundle = new Bundle();
        bundle.putDouble("lo_x",lo_x);
        bundle.putDouble("lo_y",lo_y);
        bundle.putString("name",name);
        fragment.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.pcmap, fragment);
        fragmentTransaction.commit();
    }

}
