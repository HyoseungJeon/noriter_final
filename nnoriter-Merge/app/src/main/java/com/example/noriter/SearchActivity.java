package com.example.noriter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
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
import java.util.ArrayList;

//필터링 data 선언

public class SearchActivity extends AppCompatActivity{
    //필터링 data 선언
    private String ads_1, ads_2, ads_3, game;//일반 필터링 데이터

    String serverURL;

    private String[] ads_1item = {"전체,all","서울특별시,seoul"};
    private String[] gameitem = {"전체","오버워치","배틀그라운드","롤","검은사막"};
    private String[] init = {"전체"};
    private String[] getArray;
    private ArrayList<String> hangularray = new ArrayList<String>();
    private ArrayList<String> englisharray = new ArrayList<String>();
    private ArrayList<String> ads_2hangularray = new ArrayList<String>();
    private ArrayList<String> ads_2englisharray = new ArrayList<String>();
    private String[] ads_1array;
    private String[] ads_2array;
    private String[] ads_3array;
    private String[] ary;

    private int cpubrend;
    private int gen;
    private String core;
    private int ram;
    private String gpu;
    private boolean printer = false;
    private boolean card = false;
    private boolean office = false;
    private boolean charger = false;
    private String selectedGame;

    private PopupWindow popup_local;
    private ListView search_ListView;
    private ListViewAdapter adapter;
    private ArrayList<pcinfo_item> pclist = new ArrayList<pcinfo_item>();

    String mJsonString;
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="result";
    private static final String TAG_Pname = "Pname";
    private String postParameters;
    JSONArray peoples = null;
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getSupportActionBar().setTitle("검색");
        setContentView(R.layout.search_main);
        Button detailbutton = (Button) findViewById(R.id.detail);
        Button applybutton = (Button) findViewById(R.id.search_apply) ;
        //search_ListView.setAdapter(adapter);
        search_ListView = (ListView) findViewById(R.id.search_pclist);
        adapter = (ListViewAdapter) new ListViewAdapter();
        search_ListView.setAdapter(adapter);

        serverURL = getString(R.string.serverIP)+"php.php";

        //상세검색 정보 세팅
        Intent detailintent = getIntent();
        cpubrend = detailintent.getIntExtra("cpubrend",0);
        gen = detailintent.getIntExtra("gen",0);
        core = detailintent.getStringExtra("core");
        ram = detailintent.getIntExtra("ram",0);
        gpu = detailintent.getStringExtra("gpu");
        printer = detailintent.getBooleanExtra("printer",false);
        card = detailintent.getBooleanExtra("card",false);
        office = detailintent.getBooleanExtra("office",false);
        charger = detailintent.getBooleanExtra("charger",false);

        //스피너 세팅
        for(int i = 0 ; i < ads_1item.length ; i++){
            ary = ads_1item[i].split(",");
            hangularray.add(ary[0]);
            englisharray.add(ary[1]);
        }
        init[0] = hangularray.get(0);
        ads_1array = hangularray.toArray(new String[hangularray.size()]);
        ads_2array = init;
        ads_3array = init;
        final ArrayAdapter ads_1Adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, ads_1array);
        ArrayAdapter ads_2Adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, init);
        ArrayAdapter ads_3Adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, init);

        ArrayAdapter gameAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, gameitem);
        final Spinner ads_1Spinner = (Spinner) findViewById(R.id.local_tl);
        ads_1Spinner.setAdapter(ads_1Adapter);
        final Spinner ads_2Spinner = (Spinner) findViewById(R.id.local_rns);
        ads_2Spinner.setAdapter(ads_2Adapter);
        final Spinner ads_3Spinner = (Spinner) findViewById(R.id.local_rn);
        ads_3Spinner.setAdapter(ads_3Adapter);
        final Spinner gameSpinner = (Spinner) findViewById(R.id.game_games);
        gameSpinner.setAdapter(gameAdapter);

        gameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    cpubrend =0;
                    gen = 0;
                    core = null;
                    ram = 0;
                    gpu = null;
                    printer = false;
                    card = false;
                    office = false;
                    charger = false;}
                selectedGame = gameSpinner.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //PC 리스트뷰 세팅

        //상세검색 버튼 세팅
        detailbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                gameSpinner.setSelection(0);
                Intent deatailintent = new Intent(getApplicationContext(),detailSearchActivity.class );
                deatailintent.putExtra("cpubrend",cpubrend);
                deatailintent.putExtra("gen", gen-3);
                deatailintent.putExtra("core",core);
                deatailintent.putExtra("ram",ram);
                deatailintent.putExtra("gpu",gpu);
                deatailintent.putExtra("printer",printer);
                deatailintent.putExtra("card",card);
                deatailintent.putExtra("office",office);
                deatailintent.putExtra("charger",charger);

                startActivity(deatailintent);
            }
        });

        //popup menu 세팅
        /*final String local_data[] = {"서울", "경기", "수원", "대전", "대구", "부산"};
        Button search_local = (Button) findViewById(R.id.search_local);
        search_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View popupview_local = getLayoutInflater().inflate(R.layout.search_popup_local,null);
                popup_local = new PopupWindow(popupview_local, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                popup_local.setFocusable(true);
                popup_local.setAnimationStyle(-1);
                popup_local.showAtLocation(popupview_local, Gravity.CENTER,0,0);

                TabHost popupTabhost = (TabHost) popupview_local.findViewById(R.id.search_popup_local_tabhost) ;
                popupTabhost.setup() ;

                // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
                TabHost.TabSpec ts1 = popupTabhost.newTabSpec("con1") ;
                ts1.setContent(R.id.tab1) ;
                ts1.setIndicator("시/도") ;
                popupTabhost.addTab(ts1) ;

                TabHost.TabSpec ts2 = popupTabhost.newTabSpec("con2") ;
                ts2.setContent(R.id.tab2) ;
                ts2.setIndicator("시/군/구") ;
                popupTabhost.addTab(ts2) ;

                TabHost.TabSpec ts3 = popupTabhost.newTabSpec("con3") ;
                ts3.setContent(R.id.tab3) ;
                ts3.setIndicator("동/읍/면") ;
                popupTabhost.addTab(ts3) ;

                MyAdapter popupadapter = new MyAdapter(getApplicationContext(),R.layout.search_popup_local_itemview,local_data);
                GridView popup_grid = (GridView) popupview_local.findViewById(R.id.search_popup_local_grid);
                popup_grid.setAdapter(popupadapter);
            }
        });*/

        //적용 버튼 세팅
        applybutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
                adapter.deleteItem();
                pclist.clear();

                GetData task = new GetData();
                task.execute("");

                adapter.notifyDataSetChanged();
            }
        });
        //pc리스트 선택 버튼 세팅
        search_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                int Pc_index = pclist.get(position).getPcindex();
                String Pc_name = pclist.get(position).getName();
                String Pc_number = pclist.get(position).getNumber();
                String Pc_address = pclist.get(position).getAd_si() +" "+ pclist.get(position).getAd_rns()+" "+pclist.get(position).getAd_rn();
                int Pc_Card = pclist.get(position).isCard();
                int Pc_Printer = pclist.get(position).isPrinter();
                int Pc_Office = pclist.get(position).isOffice();
                int Pc_Charger = pclist.get(position).isCharger();
                String Pc_CPU = null;
                if(pclist.get(position).getCPU_B() == 1)
                    Pc_CPU = "Intel " + pclist.get(position).getCPU_G()+"세대 "+pclist.get(position).getCPU_C();
                else if(pclist.get(position).getCPU_B() == 2)
                    Pc_CPU = "AMD " + pclist.get(position).getCPU_G()+"세대 "+pclist.get(position).getCPU_C();
                String Pc_GPU = Pc_GPU = pclist.get(position).getGPU();
                int Pc_RAM = pclist.get(position).getRAM();
                String Pc_Pc_main = pclist.get(position).getPc_main();
                String Pc_PC_menu = pclist.get(position).getPc_menu();
                String Pc_Pc_seat = pclist.get(position).getpc_seat();

                Intent intent = new Intent(getApplicationContext(), pcinfoActivity.class);
                intent.putExtra("pid",Pc_index);
                intent.putExtra("name",Pc_name);
                intent.putExtra("number", Pc_number);
                intent.putExtra("address",Pc_address);
                intent.putExtra("Card",Pc_Card);
                intent.putExtra("Printer",Pc_Printer);
                intent.putExtra("Office",Pc_Office);
                intent.putExtra("Charger",Pc_Charger);
                intent.putExtra("CPU",Pc_CPU);
                intent.putExtra("GPU",Pc_GPU);
                intent.putExtra("RAM",Pc_RAM);
                intent.putExtra("Pc_main",Pc_Pc_main);
                intent.putExtra("PC_menu",Pc_PC_menu);
                //intent.putExtra("PC_seat",Pc_Pc_seat);

                startActivity(intent);
                // TODO : use item data.
            }
        }) ;
        //스피너 리스너 세팅
        ads_1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    ads_1 = null;
                    ads_2 = null;
                    ads_3 = null;
                    addresssetAdapter(ads_2Spinner,init);
                    addresssetAdapter(ads_3Spinner,init);
                }
                else {
                    ads_1 = ads_1Spinner.getItemAtPosition(i).toString();
                    getArray = getResources().getStringArray(getResources().getIdentifier(englisharray.get(i),"array",getPackageName()));
                    ads_2hangularray.clear();
                    ads_2englisharray.clear();
                    for(int si_i = 0 ; si_i < getArray.length ; si_i++){
                        ary = getArray[si_i].split(",");
                        ads_2hangularray.add(ary[0]);
                        ads_2englisharray.add(ary[1]);
                    }
                    ads_2array = ads_2hangularray.toArray(new String[ads_2hangularray.size()]);
                    addresssetAdapter(ads_2Spinner,ads_2array);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ads_2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) {
                    ads_2 = null;
                    ads_3 = null;
                    addresssetAdapter(ads_3Spinner,init);
                }
                else {
                    ads_2 = ads_2Spinner.getItemAtPosition(i).toString();
                    getArray = getResources().getStringArray(getResources().getIdentifier(ads_2englisharray.get(i),"array",getPackageName()));
                    hangularray.clear();
                    for(int rns_i = 0 ; rns_i < getArray.length ; rns_i++){
                        ary = getArray[rns_i].split(",");
                        hangularray.add(ary[0]);
                    }
                    ads_3array = hangularray.toArray(new String[hangularray.size()]);
                    addresssetAdapter(ads_3Spinner,ads_3array);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ads_3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0)
                    ads_3 = ads_3Spinner.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SearchActivity.this,
                    "잠시만 기다려주세요", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            //mTextViewResult.setText(result);

            Log.d(TAG, "response - " + result);

            if (result == null){
                Toast.makeText(SearchActivity.this, "서버의 상태가 원활하지 않습니다", Toast.LENGTH_LONG).show();
            }

            else {
                mJsonString = result;
                showResult();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword = params[0];

            postParameters = "";
            //검색 SQL문 세팅
            if(ads_1 != null)
                postParameters += "Addr_city="+ads_1+"&";
            if(ads_2 != null)
                postParameters += "Addr_country="+ads_2+"&";
            if(ads_3 != null)
                postParameters += "Addr_district="+ads_3+"&";
            if(cpubrend != 0)
                postParameters += "CPU_B=" + Integer.toString(cpubrend)+"&";
            if(gen != 0)
                postParameters += "CPU_G=" + Integer.toString(gen)+"&";
            if(core != null)
                postParameters += "CPU_C=" + core+"&";
            if(ram != 0)
                postParameters += "RAM=" + Integer.toString(ram)+"&";
            if(gpu != null)
                postParameters += "gpu=" + gpu+"&";
            if(printer)
                postParameters += "Printer=1&";
            if(card)
                postParameters += "Card=1&";
            if(office)
                postParameters += "Office=1&";
            if(charger)
                postParameters += "Charger=1&";

            if(postParameters.length() != 0)
                postParameters = postParameters.substring(0,postParameters.length()-1);

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
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                pcinfo_item pcinfo = new pcinfo_item();
                pcinfo.setPcindex(item.optInt("pid"));
                pcinfo.setName(item.optString("pname"));
                pcinfo.setAd_si(item.optString("addr_city"));
                pcinfo.setAd_rns(item.optString("addr_country"));
                pcinfo.setAd_rn(item.optString("addr_district"));
                pcinfo.setNumber(item.optString("pcallnum"));
                pcinfo.setLo_x(item.optInt("location_x"));
                pcinfo.setLo_y(item.optInt("location_y"));
                pcinfo.setCPU_B(item.optInt("CPU_B"));
                pcinfo.setCPU_G(item.optInt("CPU_G"));
                pcinfo.setCPU_C(item.optString("CPU_C"));
                //pcinfo.setCPU_S(item.optInt("CPU_S"));
                pcinfo.setRAM(item.optInt("RAM"));
                pcinfo.setGPU(item.optString("GPU"));
                pcinfo.setCard(item.optInt("card"));
                pcinfo.setCharger(item.optInt("charger"));
                pcinfo.setPrinter(item.optInt("printer"));
                pcinfo.setOffice(item.optInt("office"));
                pcinfo.setPc_main("http://117.16.43.25/"+item.optString("pimg"));
                pcinfo.setPc_menu("http://117.16.43.25/"+item.optString("mimg"));

                pclist.add(pcinfo);
                String fullAddress = pclist.get(i).getAd_si() +" "+ pclist.get(i).getAd_rns()+" "+pclist.get(i).getAd_rn();
                //임시 게임검색
                adapter.addItem(pclist.get(i).getPcindex(), ContextCompat.getDrawable(getApplicationContext(), R.drawable.logo), pclist.get(i).getName(), pclist.get(i).getNumber(), fullAddress, false);
            }

            /*ListAdapter adapter = new SimpleAdapter(

                    MainActivity.this, mArrayList, R.layout.item_list,

                    new String[]{TAG_ID,TAG_NAME, TAG_ADDRESS},

                    new int[]{R.id.textView_list_id, R.id.textView_list_name, R.id.textView_list_address}

            );*/
            search_ListView.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
    private void addresssetAdapter(Spinner spinner, String[] array){
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, array);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}

class MyAdapter extends BaseAdapter {
    Context context;
    int layout;
    String img[];
    LayoutInflater inf;

    public MyAdapter(Context context, int layout, String[] img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public Object getItem(int position) {
        return img[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inf.inflate(layout, null);
        TextView localView = (TextView)convertView.findViewById(R.id.search_popup_local_localname) ;
        localView.setText(img[position]);

        return convertView;
    }
}



