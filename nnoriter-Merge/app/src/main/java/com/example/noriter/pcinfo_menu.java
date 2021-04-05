package com.example.noriter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by 전효승 on 2018-03-06.
 */

public class pcinfo_menu extends AppCompatActivity{

    Handler handler = new Handler();
    String Imageurl;
    @Override
    protected void onCreate (@Nullable Bundle bundle){

        super.onCreate(bundle);
        setContentView(R.layout.pcinfo_menu);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        Imageurl = intent.getStringExtra("menu");
        final ImageView menu = (ImageView) findViewById(R.id.pcinfo_menu_image);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                // TODO Auto-generated method stub
                try{
                    // 걍 외우는게 좋다 -_-;
                    URL url = new URL(Imageurl);
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    final Drawable d = new BitmapDrawable(bm);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            menu.setBackground(d);
                        }
                    });
                    menu.setBackground(d);
                } catch(Exception e){

                }

            }
        });
        t1.start();
    }
}
