package com.example.noriter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class signinActivity extends AppCompatActivity {
    private String id;
    private String pw;
    private String sid = "qmflwj1441";
    private String spw="111111";
    private String favorites;
    private String email;
    private boolean check = true;
    private SharedPreferences ad;
    String mJsonString;

    String serverURL;
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="result";
    private static final String TAG_Pname = "Pname";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("로그인");
        setContentView(R.layout.sign_in);
        ad = getSharedPreferences("ad",MODE_PRIVATE);

        serverURL = getString(R.string.serverIP)+"signup.php";

        final EditText id_et = (EditText) findViewById(R.id.sign_in_id);
        final EditText pw_et = (EditText) findViewById(R.id.sign_in_password1);

        Button log_in = (Button) findViewById(R.id.sign_in_signin);
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id  = id_et.getText().toString();
                pw = pw_et.getText().toString();
                check=true;

                if(id.length() != 0 && pw.length() != 0) {
                    //GetData task = new GetData();
                    //task.execute("");
                    //id가 존재하지 않는 경우
                    if(spw.length() == 0 || !id.equals(sid)){
                        Toast.makeText(signinActivity.this, "아이디가 존재하지 않습니다", Toast.LENGTH_SHORT).show();
                        check=false;
                    }
                    else{
                        //pw가 틀린 경우
                        if(!spw.equals(pw)){
                            Toast.makeText(signinActivity.this, "비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                            check=false;
                        }
                    }
                }
                else{
                    Toast.makeText(signinActivity.this, "아이디와 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    check=false;
                }
                if(check){
                    //로그인 성공
                    SharedPreferences.Editor editor = ad.edit();
                    editor.putString("id",id);
                    editor.putString("favorites",favorites);
                    editor.putString("email",email);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

        TextView sign_up_button = (TextView) findViewById(R.id.sign_in_signup);
        sign_up_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), signupActivity.class));
            }
        });


        /*TextView find_id_button = (TextView) findViewById(R.id.sign_in_findid);
        find_id_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //아이디, 비밀번호 찾기 리스너 동작
            }
        });*/

    }
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(signinActivity.this,
                    //"Log in", null, true, true);
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

            postParameters = postParameters + "id="+id+"&pw="+pw;

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
                spw = item.getString("pwd");
                favorites = item.getString("favorites");
                email = item.getString("email");
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
