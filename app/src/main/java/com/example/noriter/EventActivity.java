package com.example.noriter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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
 * Created by 전효승 on 2018-02-19.
 */

public class EventActivity extends AppCompatActivity{
    String mJsonString;

    String serverURL;
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="result";
    private static final String TAG_Pname = "Pname";
    ListView listView;
    EventListViewAdapter adapter = new EventListViewAdapter();

    private String eventImage;
    private String title;
    private String date;
    private String address;
    private String writer;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        getSupportActionBar().setTitle("이벤트");
        setContentView(R.layout.event);
        serverURL = getString(R.string.serverIP)+"signup.php";

        listView = (ListView) findViewById(R.id.events);
        adapter = new EventListViewAdapter();
        listView.setAdapter(adapter);
        TextView empty_set = (TextView)findViewById(R.id.empty_text_event);
        listView.setEmptyView(empty_set);
        adapter.addItem(1,"","사용자1","아키에이지 PC방 이벤트","2018-04-17","서울특별시 종로구 부암동");
        adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // get item
                EventListViewItem item = (EventListViewItem) parent.getItemAtPosition(position) ;

                //int eventindex = item.geteventindex();
                eventImage = item.geteventimage();
                writer = item.gettitle();
                date = item.getdate();

                Intent intent = new Intent(getApplicationContext(), Event_detail_Activity.class);
                intent.putExtra("eventImage",eventImage);
                intent.putExtra("writer",writer);
                intent.putExtra("date",date);

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
            //progressDialog = ProgressDialog.show(EventActivity.this,"", null, true, true);
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
            String postParameters = "";
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

            int eid;
            String eventImage1;
            String writer1;
            String title1;
            String date1;
            String address1;
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                eid = item.getInt("Eid");
                eventImage1 = item.getString("Poster");;
                writer1= item.getString("writer");
                title1=item.getString("title");
                date1=item.getString("Date");
                address1=item.getString("address");
                adapter.addItem(eid,eventImage1,writer1,title1,date1,address1);
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
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
