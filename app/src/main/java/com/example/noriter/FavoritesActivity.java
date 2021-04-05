package com.example.noriter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

/**
 * Created by 전효승 on 2018-02-19.
 */

public class FavoritesActivity extends AppCompatActivity{
    private ArrayList<Integer> favoritespc;
    private SharedPreferences ad;
    private String id;
    private String favorites;
    private ArrayList<Integer> bookmark = new ArrayList<Integer>();
    String mJsonString;
    private String postParameters;
    private ArrayList<pcinfo_item> pclist = new ArrayList<pcinfo_item>();
    private ListView favorites_ListView;
    private ListViewAdapter adapter;
    double lo_x;
    double lo_y;

    String serverURL;
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="result";
    private static final String TAG_Pname = "Pname";

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getSupportActionBar().setTitle("즐겨찾기");
        setContentView(R.layout.favorites);
        serverURL = getString(R.string.serverIP)+"favorites.php";
        ad = getSharedPreferences("ad",MODE_PRIVATE);
        id = ad.getString("id","");
        favorites = ad.getString("favorites","");

        favorites_ListView = (ListView) findViewById(R.id.favorites_pclist);
        adapter = (ListViewAdapter) new ListViewAdapter();
        favorites_ListView.setAdapter(adapter);
        TextView empty_text = (TextView) findViewById(R.id.empty_text);
        favorites_ListView.setEmptyView(empty_text);
        adapter.notifyDataSetChanged();

        if(favorites== "" || favorites.isEmpty()) {
            Toast.makeText(this, "추가된 피시방이 없습니다", Toast.LENGTH_SHORT).show();
        }
        else{
            GetData task = new GetData();
            task.execute("");
        }
        //favorites 짤라서 bookmark 리스트에 넣기


        //읽어온 bookmark 리스트로 postparameter값 만들어서 서버에 호출

        favorites_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                lo_x=pclist.get(position).getLo_x();
                lo_y=pclist.get(position).getLo_y();
                Log.i("갖고오기",lo_x+"    "+lo_y);
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
                intent.putExtra("lo_x",lo_x);
                intent.putExtra("lo_y",lo_y);
                intent.putExtra("Pc_seat",Pc_Pc_seat);

                startActivity(intent);
                // TODO : use item data.
            }
        }) ;
    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FavoritesActivity.this,
                    "잠시만 기다려주세요", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
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

            postParameters = "pid="+favorites;
            //검색 SQL문 세팅

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
                pcinfo.setPc_main(getString(R.string.serverIP)+item.optString("pimg"));
                pcinfo.setPc_menu(getString(R.string.serverIP)+item.optString("mimg"));
                if(!item.optString("simg").isEmpty() || item.optString("simg").length()>0)
                    pcinfo.setPc_seat("http://117.16.43.25/"+item.optString("simg"));
                else
                    pcinfo.setPc_seat("");
                if(pcinfo.getNumber().length() == 0)
                    pcinfo.setNumber("해당 정보가 없습니다");
                pclist.add(pcinfo);
                String fullAddress = pclist.get(i).getAd_si() +" "+ pclist.get(i).getAd_rns()+" "+pclist.get(i).getAd_rn();
                //임시 게임검색
                adapter.addItem(pclist.get(i).getPcindex(), ContextCompat.getDrawable(getApplicationContext(), R.drawable.pc_logo), pclist.get(i).getName(), pclist.get(i).getNumber(), fullAddress, false);
            }
            favorites_ListView.setAdapter(adapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
