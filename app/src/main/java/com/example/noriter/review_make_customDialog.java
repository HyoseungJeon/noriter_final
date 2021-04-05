package com.example.noriter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class review_make_customDialog{

    private Context context;
    private int pid;
    private String name;
    private String title;
    private String content;
    private String date;
    private static final String TAG_RESULTS = "result";
    String mJsonString;
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    String serverURL;

    //private SharedPreferences ad;
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="result";
    private static final String TAG_Pname = "Pname";

    public review_make_customDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.pcinfo_review_make);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText titleEditText = (EditText) dlg.findViewById(R.id.pcinfo_review_make_title);
        final EditText contentEditText = (EditText) dlg.findViewById(R.id.pcinfo_review_make_Contents);
        final Button applyButton = (Button) dlg.findViewById(R.id.pcinfo_review_make_apply);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = titleEditText.getText().toString();
                content = contentEditText.getText().toString();
                if(title.length() > 0) {
                    getTime();
                    GetData task = new GetData();
                    task.execute("");
                    // 커스텀 다이얼로그를 종료한다.
                    dlg.dismiss();
                }
                else{
                    Toast.makeText(context, "1글자 이상의 제목을 입력하세요", Toast.LENGTH_SHORT).show();
                }
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
            //검색 SQL문 세팅 title과 content를 넘김
            //postParameters = "pid="+index;
            postParameters = "pid="+pid+"&name="+name+"&title="+title+"&content="+content+"&date="+date;

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

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    //서버 URL 세팅
    public void setServerURL(String url){
        serverURL=url+"regist_review.php";
    }
    public void setData(int pid1, String name1){
        pid = pid1;
        name = name1;
    }
    private void getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        date =  mFormat.format(mDate);
    }
    public String getName(){return name;}
    public String getDate(){return date;}
    public String getTitle(){return title;}
    public String getContent(){return content;}
}
