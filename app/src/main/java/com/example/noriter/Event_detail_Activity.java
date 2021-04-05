package com.example.noriter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by 전효승 on 2018-03-10.
 */

public class Event_detail_Activity extends AppCompatActivity{
    private int eventindex;
    private String eventImage;
    private String writer;
    private String date;
    String serverURL;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        getSupportActionBar().setTitle("이벤트 정보");
        setContentView(R.layout.detail_event2);

        Intent intent = getIntent();
        eventImage = intent.getStringExtra("eventImage");
        writer = intent.getStringExtra("writer");
        date = intent.getStringExtra("date");

        serverURL = getString(R.string.serverIP)+eventImage;

        ImageView event_image = (ImageView)findViewById(R.id.detail_event_imageview);
        TextView writertext = (TextView) findViewById(R.id.detail_event_writer);
        TextView datetext = (TextView) findViewById(R.id.detail_event_date);
        writertext.setText(writer);
        datetext.setText(date);
        event_image.setBackground(getDrawable(R.drawable.event_1));

        //리스트 이미지 세팅
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                // TODO Auto-generated method stub
                try{
                    // 걍 외우는게 좋다 -_-;
                    final ImageView iv = (ImageView)findViewById(R.id.detail_event_imageview);
                    URL url = new URL(serverURL);
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    final Drawable d = new BitmapDrawable(bm);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            iv.setBackground(d);
                        }
                    });
                    iv.setBackground(d);
                } catch(Exception e){

                }

            }
        });
        //t1.start();
        //eventindex를 이용해 데이터베이스를 불러오고 정보를 띄움
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homemenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
