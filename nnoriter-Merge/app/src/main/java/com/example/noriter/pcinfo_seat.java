package com.example.noriter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class pcinfo_seat extends AppCompatActivity {
    WebView seatview;
    String seatUrl;
    String Pc_seat;
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getSupportActionBar().setTitle("좌석 정보");
        setContentView(R.layout.pcinfo_seat);

        Intent intent = getIntent();
        Pc_seat = intent.getStringExtra("Pc_seat");
        seatUrl = getString(R.string.serverIP)+Pc_seat;
        seatview = (WebView) findViewById(R.id.pcinfo_seatview);
        seatview.loadUrl(seatUrl);
    }
}
