package com.example.noriter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class signupActivity extends AppCompatActivity {
    private String id;
    private String pw1;
    private String pw2;
    private String email;
    private int checkid;
    private boolean check = true;
    private boolean idcheck = false;
    String mJsonString;

    String serverURL;
    String serverURL2;
    private static String TAG = "phpquerytest";
    private static final String TAG_JSON="result";
    private static final String TAG_Pname = "Pname";

    @Override
    protected void onCreate(@Nullable Bundle bundle){
        super.onCreate(bundle);
        getSupportActionBar().setTitle("회원가입");
        setContentView(R.layout.sign_up);

        //서버 URL 세팅
        serverURL = getString(R.string.serverIP)+"signup.php";
        serverURL2 = getString(R.string.serverIP)+"check_id.php";

        final EditText id_et = (EditText) findViewById(R.id.sign_up_id);
        final EditText pw1_et = (EditText) findViewById(R.id.sign_up_password1);
        final EditText pw2_et = (EditText) findViewById(R.id.sign_up_password2);
        final EditText email_et = (EditText) findViewById(R.id.sign_up_email);
        Button sign_up_button = (Button) findViewById(R.id.sign_up_signup);

        //가입하기 버튼
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id  = id_et.getText().toString();
                pw1 = pw1_et.getText().toString();
                pw2 = pw2_et.getText().toString();
                email = email_et.getText().toString();
                check = true;

                //id가 입력되지 않은 경우
                if(id.length() == 0){
                    //id_et.setBackgroundResource(R.color.warring);
                    id_et.setTextColor(getResources().getColor(R.color.warring));
                    Toast.makeText(signupActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    check = false;
                }
                else if(id.length() > 0 && id.length() < 5){
                    //id_et.setBackgroundResource(R.color.warring);
                    id_et.setTextColor(getResources().getColor(R.color.warring));
                    Toast.makeText(signupActivity.this, "5글자 이상 아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                    check = false;
                }
                else{ //id가 입력되었는데 중복일 경우
                    GetData2 task2 = new GetData2();
                    task2.execute("");
                    if(idcheck) {
                        Toast.makeText(signupActivity.this, "입력하신 아이디가 존재합니다", Toast.LENGTH_SHORT).show();
                        check = false;
                    }
                    else{
                        //비밀번호가 입력되지 않은경우
                        if (pw1.length() == 0){
                            pw1_et.setTextColor(getResources().getColor(R.color.warring));
                            Toast.makeText(signupActivity.this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                            check = false;
                        }
                        else if (pw1.length() > 0 && pw1.length() < 6){
                            pw1_et.setTextColor(getResources().getColor(R.color.warring));
                            Toast.makeText(signupActivity.this, "6글자 이상 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                            check = false;
                        }
                        else{
                            //2차비밀번호가 입력되지 않은경우
                            if(pw2.length() == 0){
                                //pw2_et.setBackgroundResource(R.color.warring);
                                pw2_et.setTextColor(getResources().getColor(R.color.warring));
                                Toast.makeText(signupActivity.this, "2차 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                                check = false;
                            }
                            else{
                                //비밀번호가 다를경우
                                if(!pw1.equals(pw2)){
                                    //pw1_et.setBackgroundResource(R.color.warring);
                                    //pw2_et.setBackgroundResource(R.color.warring);
                                    pw1_et.setTextColor(getResources().getColor(R.color.warring));
                                    pw2_et.setTextColor(getResources().getColor(R.color.warring));
                                    Toast.makeText(signupActivity.this, "1차, 2차 비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                                    check = false;
                                }
                                else{
                                    //email이 입력되지 않은 경우
                                    if(email.length() == 0){
                                        //email_et.setBackgroundResource(R.color.warring);
                                        email_et.setTextColor(getResources().getColor(R.color.warring));
                                        Toast.makeText(signupActivity.this, "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
                                        check = false;
                                    }
                                }
                            }
                        }
                    }
                }
                if(check){//회원가입 성공
                    Toast.makeText(signupActivity.this, "가입 완료", Toast.LENGTH_SHORT).show();
                    GetData task = new GetData();
                    task.execute("");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        });

        //텍스트 글자색 초기화 함수
        edittextevent(id_et);
        edittextevent(pw1_et);
        edittextevent(pw2_et);
        edittextevent(email_et);
    }

    //텍스트 글자색 초기화 함수
    public void edittextevent(final EditText edit){
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //edit.setBackgroundResource(R.color.white);
                edit.setTextColor(getResources().getColor(R.color.black));
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //edit.setBackgroundResource(R.color.white);
                edit.setTextColor(getResources().getColor(R.color.black));
            }
        });
    }
    //서버에 회원정보 업로드 접근 함수
    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(signupActivity.this,
                    //"회원가입 중", null, true, true);
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

            postParameters = "id="+id+"&pwd="+pw1+"&email="+email;

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
            /*JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
            }*/
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    //아이디 중복 체크 서버 호출 코드
    private class GetData2 extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;
        @Override

        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(signupActivity.this,
                    //"아이디 중복체크 확인중", null, true, true);
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
                showResult2();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword = params[0];
            String postParameters = "id="+id;
            //검색 SQL문 세팅

            if(postParameters.length() != 0)
                postParameters = postParameters.substring(0,postParameters.length()-1);

            try {
                URL url = new URL(serverURL2);
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
    private void showResult2(){
        try {
            int count = 0;
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);
                count = item.optInt("flag");
            }
            if(count > 0)
                idcheck = true;
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}

