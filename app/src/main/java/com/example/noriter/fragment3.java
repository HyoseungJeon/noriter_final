package com.example.noriter;
import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.NMapView.OnMapStateChangeListener;
import com.nhn.android.maps.NMapView.OnMapViewTouchEventListener;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class fragment3 extends Fragment {
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    View view;
    NMapView mMapView;        //MapView 객체(지도 생성, 지도데이터)
    NMapController mMapController;    //지도 상태 컨트롤 객체
    NMapViewerResourceProvider mMapViewerResourceProvider;    //지도 뷰어 리소스 곱급자 객체 생성
    NMapOverlayManager mOverlayManager;        //오버레이 관리 객체
    OnMapViewTouchEventListener onMapViewTouchEventListener;
    OnMapStateChangeListener onMapViewStateChangeListener;
    double lo_x;
    double lo_y;
    String name;
    private static final String TAG_RESULTS = "result";
    double a = 0, b = 0;
    private NMapContext mMapContext;
    private static final String CLIENT_ID = "CszUaxqSM37gzrSc6sdK";// 애플리케이션 클라이언트 아이디 값
    int[] distances = new int[20];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nmapfragment, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = null;
        name=getArguments().getString("name");
        lo_x=getArguments().getDouble("lo_x");
        lo_y=getArguments().getDouble("lo_y");
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
}