package com.example.noriter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 전효승 on 2018-03-05.
 */

public class pcinfoActivity extends AppCompatActivity {

    private PopupWindow popup_review;
    private View popupview_local;

    String myJSON;
    private static final String TAG_RESULTS = "result";
    String mJsonString;

    String serverURL;
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="result";
    private static final String TAG_Pname = "Pname";
    JSONArray peoples = null;
    String name;
    private int index;
    String Pc_name;
    String Pc_number;
    String Pc_address;
    int Pc_Card;
    int Pc_Printer;
    int Pc_Office;
    int Pc_Charger;
    String Pc_CPU;
    String Pc_GPU;
    int Pc_RAM = 0;
    String Pc_Pc_main;
    String Pc_PC_menu;
    double lo_x;
    double lo_y;
    String Pc_seat;
    String nullstring = "정보가 없습니다";

    ExpandableListView listView;
    review_expandablelistview_adapter adapter= new review_expandablelistview_adapter();

    private SharedPreferences ad;
    private String id;
    private String favorites;
    private String[] favorlist;

    private review_make_customDialog dialog;

    private Bitmap bm2;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getSupportActionBar().setTitle("PC방 정보");
        setContentView(R.layout.pcinfo);
        serverURL = getString(R.string.serverIP)+"review.php";
        //final TextView name = (TextView) findViewById(R.id.pcinfo_name);
        Intent intent = getIntent();
        Pc_name = intent.getStringExtra("name");
        Pc_number = intent.getStringExtra("number");
        Pc_address = intent.getStringExtra("address");
        Pc_Card = intent.getIntExtra("Card",0);
        Pc_Printer = intent.getIntExtra("Printer",0);
        Pc_Office = intent.getIntExtra("Office",0);
        Pc_Charger = intent.getIntExtra("Charger",0);
        Pc_CPU = intent.getStringExtra("CPU");
        Pc_GPU = intent.getStringExtra("GPU");
        Pc_RAM = intent.getIntExtra("RAM",0);
        Pc_Pc_main = intent.getStringExtra("Pc_main");
        Pc_PC_menu = intent.getStringExtra("PC_menu");
        lo_x=intent.getDoubleExtra("lo_x",0);
        lo_y=intent.getDoubleExtra("lo_y",0);
        index = intent.getIntExtra("pid",0);
        ad = getSharedPreferences("ad",MODE_PRIVATE);
        id = ad.getString("id","");
        favorites = ad.getString("favorites","");
        if(favorites.length() > 0)
            favorlist = favorites.split(",");
        Pc_seat = intent.getStringExtra("Pc_seat");

        TextView nameTextView = (TextView) findViewById(R.id.pcinfo_name);
        TextView numberTextView = (TextView) findViewById(R.id.pcinfo_number);
        TextView addressTextView = (TextView) findViewById(R.id.pcinfo_address);
        TextView CPUTextView = (TextView) findViewById(R.id.pcinfo_CPU);
        TextView GPUTextView = (TextView) findViewById(R.id.pcinfo_GPU);
        TextView RAMTextView = (TextView) findViewById(R.id.pcinfo_RAM);

        //좌석 페이지 로딩 (좌석 페이지 url 존재 시 시트 액티비티로 이동
        TextView seatTextView = (TextView) findViewById(R.id.pcinfo_seatButton);
        if(Pc_seat!= null && Pc_seat.length() >0){
            seatTextView.setText("좌석 상태 보기");
            seatTextView.setTextColor(getResources().getColor(R.color.noritercolor2));
            seatTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent3 = new Intent(getApplicationContext(),pcinfo_seat.class );
                    intent3.putExtra("Pc_seat",Pc_seat);
                    startActivity(intent3);
                }
            });
        }

        nameTextView.setText(Pc_name);
        numberTextView.setText(Pc_number);
        addressTextView.setText(Pc_address);
        if(Pc_CPU != null) {
            CPUTextView.setText(Pc_CPU);
        }
        else
            CPUTextView.setText(nullstring);
        if(Pc_GPU.length() !=4)
            GPUTextView.setText(Pc_GPU);
        else
            GPUTextView.setText(nullstring);
        if(Pc_RAM != 0)
            RAMTextView.setText(Integer.toString(Pc_RAM)+"GB");
        else
            RAMTextView.setText(nullstring);

        //피시방 메인화면 세팅
        /*ImageView pc_main = (ImageView) findViewById(R.id.mainpcpic) ;
        pc_main.setBackground(getDrawable(R.drawable.pc_main));
        index = intent.getIntExtra("index",0);*/

        //태그 관리
        TagViewAdapter tagAdapter = new TagViewAdapter();
        RecyclerView TagRecyclerView = (RecyclerView) findViewById(R.id.pcinfo_tagView);
        LinearLayoutManager TagLayoutManager = new LinearLayoutManager(getApplicationContext());
        TagLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        TagRecyclerView.setLayoutManager(TagLayoutManager);
        if(Pc_Card == 1)
            tagAdapter.add(getDrawable(R.drawable.tag_card_on));
        if(Pc_Charger == 1)
            tagAdapter.add(getDrawable(R.drawable.tag_charger_on));
        if(Pc_Office == 1)
            tagAdapter.add(getDrawable(R.drawable.tag_office_on));
        if(Pc_Printer == 1)
            tagAdapter.add(getDrawable(R.drawable.tag_printer_on));
        TagRecyclerView.setAdapter(tagAdapter);
        tagAdapter.notifyDataSetChanged();
        callmap();
        //Tab 관리
        TabHost tabHost1 = (TabHost) findViewById(R.id.TabHost) ;
        tabHost1.setup() ;

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("con1") ;
        ts1.setContent(R.id.tab1) ;
        ts1.setIndicator("사양") ;
        tabHost1.addTab(ts1) ;

        TabHost.TabSpec ts2 = tabHost1.newTabSpec("con2") ;
        ts2.setContent(R.id.tab2) ;
        ts2.setIndicator("좌석") ;
        tabHost1.addTab(ts2) ;

        TabHost.TabSpec ts3 = tabHost1.newTabSpec("con3") ;
        ts3.setContent(R.id.tab3) ;
        ts3.setIndicator("메뉴") ;
        tabHost1.addTab(ts3) ;

        TabHost.TabSpec ts4 = tabHost1.newTabSpec("con4") ;
        ts4.setContent(R.id.tab4) ;
        ts4.setIndicator("리뷰") ;
        tabHost1.addTab(ts4) ;

        //메인 PC 이미지 삽입
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                // TODO Auto-generated method stub
                try{
                    // 걍 외우는게 좋다 -_-;
                    final ImageView iv = (ImageView)findViewById(R.id.mainpcpic);
                    URL url = new URL(Pc_Pc_main);
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    final Drawable d = new BitmapDrawable(bm);
                    handler.post(new Runnable() {

                        @Override
                        public void run() {  // 화면에 그려줄 작업
                            iv.setBackground(d);
                        }
                    });
                } catch(Exception e){

                }
            }
        });
        t.start();

        //PC방 메뉴사진 클릭 리스너
        final ImageButton menu = (ImageButton) findViewById(R.id.pcinfo_menuImage);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), pcinfo_menu.class);
                intent2.putExtra("menu",Pc_PC_menu);
                startActivity(intent2);
            }
        });

        //PC방 메뉴사진 세팅
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {    // 오래 거릴 작업을 구현한다
                // TODO Auto-generated method stub
                try{
                    // 걍 외우는게 좋다 -_-;
                    //final ImageButton iv = (ImageButton)findViewById(R.id.pcinfo_menuImage);
                    URL url = new URL(Pc_PC_menu);
                    InputStream is = url.openStream();
                    final Bitmap bm = BitmapFactory.decodeStream(is);
                    bm2 = bm;
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

        //리뷰 작성 버튼 클릭 이벤트
        Button MakeReview = (Button) findViewById(R.id.pcinfo_review_make);
        dialog = new review_make_customDialog(pcinfoActivity.this);
        MakeReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.length() != 0) {
                    dialog.setServerURL(getString(R.string.serverIP));
                    dialog.setData(index, id);
                    dialog.callFunction();
                }
                else
                    Toast.makeText(pcinfoActivity.this, "로그인이 필요합니다", Toast.LENGTH_SHORT).show();
            }
        });

        //즐겨찾기 체크박스
        final CheckBox favcb = (CheckBox)findViewById(R.id.pcinfo_favor);

        if(favorites.length()>0) {
            for (int i = 0; i < favorlist.length; i++) {
                if (favorlist[i].equals(Integer.toString(index)))
                    favcb.setChecked(true);
            }
        }
        favcb.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favorites.length() > 0)
                    favorlist = favorites.split(",");
                SharedPreferences.Editor editor = ad.edit();
                if(favcb.isChecked()) {//즐겨찾기 추가
                    if(favorites.length() > 0){
                        editor.putString("favorites",favorites +","+Integer.toString(index));
                        favorites = favorites +","+Integer.toString(index);
                    }
                    else{
                        editor.putString("favorites",Integer.toString(index));
                        favorites = Integer.toString(index);
                    }
                    editor.apply();
                }
                else { // 즐겨찾기 삭제
                    String newfavorites="";
                    if(favorlist.length == 1){
                        editor.putString("favorites","");
                        favorites = "";
                    }
                    else{
                        for (int i = 0 ; i < favorlist.length ; i++){
                            if(!favorlist[i].equals(Integer.toString(index))){
                                newfavorites += favorlist[i]+",";
                            }
                        }
                        newfavorites = newfavorites.substring(0,newfavorites.length()-1);
                        editor.putString("favorites",newfavorites);
                        favorites = newfavorites;
                    }
                    editor.apply();
                }
            }
        });

        //리뷰 리스트뷰 세팅
        GetData task = new GetData();
        task.execute("");
        listView = (ExpandableListView) findViewById(R.id.pcinfo_reviewlist);
        adapter= new review_expandablelistview_adapter();
        listView.setAdapter(adapter);
        TextView empty_review = (TextView) findViewById(R.id.empty_review);
        listView.setEmptyView(empty_review);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                review_listview_item item = (review_listview_item) parent.getItemAtPosition(position) ;

                int rindex = item.getrindex();
                int pindex = item.getpindex();

            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(pcinfoActivity.this,
                    //"잠시만 기다려주세요", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);
            if (result == null){
                //mTextViewResult.setText(errorString);
            }
            else {
                mJsonString = result;
                showResult();
            }
        }
        @Override
        protected String doInBackground(String... params) {
            String searchKeyword = params[0];
            String postParameters;
            //검색 SQL문 세팅
            postParameters = "pid="+index;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;

                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }

                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }
    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            int pindex1;
            String name1;
            String date1;
            String title1;
            String content1;

            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                pindex1 = item.optInt("pid");
                name1 = item.optString("id");
                date1 = item.optString("date");
                title1 = item.optString("title");
                content1 = item.optString("context");
                adapter.addItem(i+1,pindex1,name1,date1,title1,content1);
            }

            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
    private void callmap() {
        fragment2 frag2 = new fragment2();
        Bundle bundle = new Bundle();
        bundle.putDouble("lo_x",lo_x);
        bundle.putDouble("lo_y",lo_y);
        bundle.putString("name",name);
        frag2.setArguments(bundle);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.info_map, frag2);
        fragmentTransaction.commit();
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

/*
<TextView
                            android:id="@+id/pcinfo_review_refresh"
                                    android:layout_alignParentRight="true"
                                    android:layout_width="wrap_content"
                                    android:layout_height="wrap_content"
                                    android:layout_margin="15dp"
                                    android:text="새로고침"
                                    android:textSize="15dp"
                                    android:textColor="@color/gray"
                                    android:clickable="true"/>*/
