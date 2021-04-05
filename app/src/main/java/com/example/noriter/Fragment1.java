package com.example.noriter;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.NMapView.OnMapViewTouchEventListener;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fragment1 extends Fragment {
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    View view;
    NMapView mMapView;        //MapView 객체(지도 생성, 지도데이터)
    NMapController mMapController;    //지도 상태 컨트롤 객체
    NMapViewerResourceProvider mMapViewerResourceProvider;    //지도 뷰어 리소스 곱급자 객체 생성
    NMapOverlayManager mOverlayManager;        //오버레이 관리 객체
    //OnStateChangeListener onPOIdataStateChangeListener;		//오버레이 아이템 변화 이벤트 콜백 인터페이스
    NMapMyLocationOverlay mMyLocationOverlay;    //지도 위에 현재 위치를 표시하는 오버레이 클래스
    NMapLocationManager mMapLocationManager;    //단말기의 현재 위치 탐색 기능 사용 클래스
    NMapCompassManager mMapCompassManager;        //단말기의 나침반 기능 사용 클래스
    OnMapViewTouchEventListener onMapViewTouchEventListener;
    OnMapStateChangeListener onMapViewStateChangeListener;
    NMapPOIitem[] items = new NMapPOIitem[20000];
    JSONObject[] JsonItems = new JSONObject[20000];
    JSONArray peoples = null;
    String myJSON;
    BottomSheetDialog bottomSheetDialog;
    TextView name;
    private static final String TAG_RESULTS = "result";
    double a = 0, b = 0;
    private NMapContext mMapContext;
    private static final String CLIENT_ID = "CszUaxqSM37gzrSc6sdK";// 애플리케이션 클라이언트 아이디 값
    int[] distances = new int[20000];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nmapfragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = null;
        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapView = (NMapView) getView().findViewById(R.id.nmapview);
        mMapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mMapContext.setupMapView(mMapView);
        mMapView.setClickable(true);
        mMapView.setBuiltInZoomControls(true, null);
        mMapView.displayZoomControls(true);
        mMapView.setOnMapStateChangeListener(onMapViewStateChangeListener);
        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        mMapController = mMapView.getMapController();
        // 줌 인/아웃 버튼 생성
        mMapView.setBuiltInZoomControls(true, null);
        mMapViewerResourceProvider = new NMapViewerResourceProvider(getContext());
        mOverlayManager = new NMapOverlayManager(getContext(), mMapView, mMapViewerResourceProvider);
        startMyLocation();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapContext.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapContext.onPause();
    }

    @Override
    public void onStop() {
        mMapContext.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mMapContext.onDestroy();
        super.onDestroy();
    }

    private void startMyLocation() {//내 위치 찾아서 이동.
        //Toast.makeText(getContext(), "찾는중...", Toast.LENGTH_SHORT).show();

        mMapLocationManager = new NMapLocationManager(getContext());
        mMapLocationManager.setOnLocationChangeListener(onMyLocationChangeListener);
        mMapLocationManager.enableMyLocation(true);

        boolean isMyLocationEnabled = mMapLocationManager.enableMyLocation(true);
        if (!isMyLocationEnabled) {    //위치 탐색이 불가능하면
            Toast.makeText(getContext(), "GPS권한을 허용해주십시오.", Toast.LENGTH_LONG).show();
            Intent goToSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(goToSettings);
            return;
        }
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(mMapLocationManager, mMapCompassManager);
        //Toast.makeText(getContext(), "찾았습니다!", Toast.LENGTH_SHORT).show();
    }
    private void stopMyLocation() {
        mMapLocationManager.disableMyLocation();    //현재 위치 탐색 종료
        if (mMapView.isAutoRotateEnabled()) {        //지도 회전기능이 활성화 상태라면
            mMyLocationOverlay.setCompassHeadingVisible(false);    //나침반 각도표시 제거
            mMapCompassManager.disableCompass();    //나침반 모니터링 종료
            mMapView.setAutoRotateEnabled(false, false);//지도 회전기능 중지
        }
    }
    private final NMapPOIdataOverlay.OnStateChangeListener onPOIdataStateChangeListener = new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onCalloutClick(NMapPOIdataOverlay poiDataOverlay, NMapPOIitem poiItem) { //오버레이의 버튼을 눌렀을때
            try {
                Log.i("이름", poiItem.getTitle());
                bottomSheetDialog = BottomSheetDialog.getInstance();
                JSONObject c = JsonItems[poiItem.getId()];
                int pcindex = c.getInt("pid");
                String pname = c.getString("pname");
                String ad_si = c.getString("addr_city");
                String ad_rns = c.getString("addr_country");
                String ad_rn = c.getString("addr_district");
                String number = c.getString("pcallnum");
                double lo_x = poiItem.getPoint().getLongitude();
                double lo_y = poiItem.getPoint().getLatitude();
                String Pc_CPU;
                int CPU;
                CPU= c.getInt("CPU_B");
                String CPU2;
                CPU2= c.getString("CPU_C");
                int CPU3;
                CPU3= c.getInt("CPU_G");
                int RAM;
                RAM= c.getInt("RAM");
                String GPU;
                GPU= c.getString("GPU");
                String city = ad_si + " " + ad_rns + " " + ad_rn;
                if(CPU== 1)
                    Pc_CPU = "Intel " +CPU3+"세대 "+CPU2;
                else
                    Pc_CPU = "AMD " + CPU3+"세대 "+CPU2;
               /* int charger = c.getInt("charger");
                int printer = c.getInt("printer");
                int office = c.getInt("office");
                int card = c.getInt("Card");*/
                bottomSheetDialog.pcc.setPcindex(pcindex);
                bottomSheetDialog.pcc.setPcindex(pcindex);
                bottomSheetDialog.pcc.setName(pname);
                bottomSheetDialog.pcc.setAd_si(ad_si);
                bottomSheetDialog.pcc.setAd_rns(ad_rns);
                bottomSheetDialog.pcc.setAd_rn(ad_rn);
                bottomSheetDialog.pcc.setNumber(number);
                bottomSheetDialog.pcc.setLo_x(lo_x);
                bottomSheetDialog.pcc.setLo_y(lo_y);
                bottomSheetDialog.pcc.setCPU_C(Pc_CPU);
                bottomSheetDialog.pcc.setRAM(RAM);
                bottomSheetDialog.pcc.setGPU(GPU);
                bottomSheetDialog.pcc.setLo_x(lo_x);
                Log.i("something","보내준것"+lo_x+"@"+lo_y);
                bottomSheetDialog.pcc.setLo_y(lo_y);
                /*bottomSheetDialog.pcc.setCharger(charger);
                bottomSheetDialog.pcc.setPrinter(printer);
                bottomSheetDialog.pcc.setOffice(office);
                bottomSheetDialog.pcc.setCard(card);*/
                bottomSheetDialog.setPid(pcindex);
                bottomSheetDialog.setName(poiItem.getTitle());
                bottomSheetDialog.setAddr(city);
                bottomSheetDialog.setNumb(number);
                bottomSheetDialog.setDist(distances[poiItem.getId()]);
                bottomSheetDialog.show(getFragmentManager(), "bottomSheet");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem poiitem) {
        }
    };
    private final NMapLocationManager.OnLocationChangeListener onMyLocationChangeListener = new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager locationManager, NGeoPoint myLocation) {
            if (mMapController != null) {
                mMapController.animateTo(myLocation);
            }

            a = myLocation.getLongitude();
            b = myLocation.getLatitude();
            Log.i("!@#",a+"좌표"+b);
            getData("http://117.16.43.25/php_connect.php");
            return true;
        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager locationManager) {
            Toast.makeText(getContext(), "내 위치 찾는중...", Toast.LENGTH_SHORT).show();
            startMyLocation();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager locationManager, NGeoPoint myLocation) {
            Toast.makeText(getContext(), "GPS를 이용할 수 없는 지역입니다.", Toast.LENGTH_SHORT).show();
            stopMyLocation();
        }
    };

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(String result) {
                Log.i("내위치", a + "@@@@" + b);
                myJSON = result;
                int cnt = 0;
                double aa = 0;
                int markerId = NMapPOIflagType.PIN;    //마커 id설정
                //POI 데이터 관리 클래스 생성(POI데이터 수, 사용 리소스 공급자)
                NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
                poiData.beginPOIdata(2);    // POI 아이템 추가 시작
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);
                    for (int i = 0; i < peoples.length(); i++) {
                        JSONObject c = peoples.getJSONObject(i);
                        int pcindex = c.getInt("pid");
                        String name = c.getString("pname");
                        double locationx = change(c.getInt("location_y"));
                        double locationy = change(c.getInt("location_x"));
                        Log.i("PHPRequest", "#####" + c.getInt("location_y") + "@@@" + c.getInt("location_x"));
                        aa = distance(a, b, locationx, locationy);
                        if (aa <= 2000) {//20km내의 피시방들
                            items[cnt] = poiData.addPOIitem(locationx, locationy, name, markerId, cnt);
                            items[cnt].setRightButton(false);
                            JsonItems[cnt] = c;
                            distances[cnt]=(int)aa;
                            Log.i("itemoveray", "표시했다." + items[cnt].getPoint().getLongitude() + "!!!" + items[cnt].getPoint().getLatitude() + "@@" + name + "!@#!@#!@#!@#" + items[cnt].getId());
                            cnt++;
                        }
                        Log.i("cnt의 값", "cnt는" + cnt);
                    }
                    poiData.endPOIdata();        // POI 아이템 추가 종료
                    NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
                    poiDataOverlay.showAllPOIdata(9);    //모든 POI 데이터를 화면에 표시(zomLevel)
                    //POI 아이템이 선택 상태 변경 시 호출되는 콜백 인터페이스 설정
                    poiDataOverlay.setOnStateChangeListener(onPOIdataStateChangeListener);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환
        return dist;
    }

    private double deg2rad(double deg) {
        return (double) (deg * Math.PI / (double) 180d);
    }
    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad) {
        return (double) (rad * (double) 180d / Math.PI);
    }

    public double change(int a) {
        double b = Math.pow(10, 7);
        return (double) a / b * 1.0;
    }
}
