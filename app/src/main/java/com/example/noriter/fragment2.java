package com.example.noriter;
import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nhn.android.maps.NMapCompassManager;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.NMapView.OnMapViewTouchEventListener;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
public class fragment2 extends Fragment {

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    double lo_x;
    double lo_y;
    String name;
    View view;
    NMapView mMapView;        //MapView 객체(지도 생성, 지도데이터)
    NMapViewerResourceProvider mMapViewerResourceProvider;    //지도 뷰어 리소스 곱급자 객체 생성
    NMapOverlayManager mOverlayManager;        //오버레이 관리 객체
    //OnStateChangeListener onPOIdataStateChangeListener;		//오버레이 아이템 변화 이벤트 콜백 인터페이스
    NMapMyLocationOverlay mMyLocationOverlay;    //지도 위에 현재 위치를 표시하는 오버레이 클래스
    NMapLocationManager mMapLocationManager;    //단말기의 현재 위치 탐색 기능 사용 클래스
    NMapCompassManager mMapCompassManager;        //단말기의 나침반 기능 사용 클래스
   // OnMapViewTouchEventListener onMapViewTouchEventListener;
    OnMapStateChangeListener onMapViewStateChangeListener;
    double a = 0, b = 0;
    private NMapContext mMapContext;
    private static final String CLIENT_ID = "CszUaxqSM37gzrSc6sdK";// 애플리케이션 클라이언트 아이디 값
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nmapfragment, container, false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        name=getArguments().getString("name");
        lo_x=getArguments().getDouble("lo_x");
        lo_y=getArguments().getDouble("lo_y");
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
        mMapView.setOnMapViewTouchEventListener(onMapViewTouchEventListener);
        mMapViewerResourceProvider = new NMapViewerResourceProvider(getContext());
        mOverlayManager = new NMapOverlayManager(getContext(), mMapView, mMapViewerResourceProvider);
        NMapPOIdata poiData = new NMapPOIdata(2, mMapViewerResourceProvider);
        int markerId = NMapPOIflagType.PIN;    //마커 id설정
        poiData.beginPOIdata(2);    // POI 아이템 추가 시작
        poiData.addPOIitem(lo_x, lo_y, name, markerId, 1);
        poiData.endPOIdata();        // POI 아이템 추가 종료
        NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(12);    //모든 POI 데이터를 화면에 표시(zomLevel)
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
    private final OnMapViewTouchEventListener onMapViewTouchEventListener = new OnMapViewTouchEventListener (){
        @Override
        public void onLongPress(NMapView nMapView, MotionEvent motionEvent) {
        }
        @Override
        public void onLongPressCanceled(NMapView nMapView) {
        }
        @Override
        public void onTouchDown(NMapView nMapView, MotionEvent motionEvent) {
        }
        @Override
        public void onTouchUp(NMapView nMapView, MotionEvent motionEvent) {
        }
        @Override
        public void onScroll(NMapView nMapView, MotionEvent motionEvent, MotionEvent motionEvent1) {
        }
        @Override
        public void onSingleTapUp(NMapView nMapView, MotionEvent motionEvent) {
            Log.i("탭","탭");
            Intent intent = new Intent(getActivity(),infomap.class);
            intent.putExtra("lo_x",lo_x);
            intent.putExtra("lo_y",lo_y);
            intent.putExtra("name", name);
            startActivity(intent);
        }
    };
    public interface dataPass{
        public void datapass(double x, double y);
    }

}