package com.lznby.baidumapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lznby.baidumapdemo.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BaiduMap.OnMarkerClickListener{
    public LocationClient mLocationClient;

    private TextView positionText;

    private MapView mapView;

    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        positionText = (TextView) findViewById(R.id.position_text_view);

        /**
         * 测试解析
         */
        Util.sendRequestWithOkHttp();


        /**
         * 权限请求
         */
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }

        /**
         * 地图上绘制单个标记
         */
/*        //定义Maker坐标点

        LatLng point = new LatLng(39.963175, 116.400244);

        //构建Marker图标

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.mark);

        //构建MarkerOption，用于在地图上添加Marker

        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Marker，并显示

        baiduMap.addOverlay(option);*/

        /**
         * Mark动画，出错闪烁，gif定义
         */
        // 通过Marker的icons设置一组图片，再通过period设置多少帧刷新一次图片资源
        BitmapDescriptor bitmap_red = BitmapDescriptorFactory
                .fromResource(R.drawable.mark_red);
        BitmapDescriptor bitmap_white = BitmapDescriptorFactory
                .fromResource(R.drawable.mark_white);

        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();

        giflist.add(bitmap_red);
        giflist.add(bitmap_white);

        /**
         * 批量设置多个标记
         */
        //设置标记样式
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.mark_green);

        //创建OverlayOptions的集合
        List<OverlayOptions> options = new ArrayList<OverlayOptions>();
        //设置坐标点

        LatLng point1 = new LatLng(39.92235, 116.380338);
        LatLng point2 = new LatLng(39.947246, 116.414977);

        //创建OverlayOptions属性

        OverlayOptions option1 =  new MarkerOptions()
                .position(point1)
                .icon(bitmap);
        OverlayOptions option2 =  new MarkerOptions()
                .position(point2)
                .icons(giflist).zIndex(0).period(10);
        //将OverlayOptions添加到list
        options.add(option1);
        options.add(option2);
        //在地图上批量添加
        baiduMap.addOverlays(options);

        //为自定义Mark添加监听
        baiduMap.setOnMarkerClickListener(this);


        /**
         * Mark动画，出错闪烁
         */
        // 通过Marker的icons设置一组图片，再通过period设置多少帧刷新一次图片资源
/*        BitmapDescriptor bitmap_red = BitmapDescriptorFactory
                .fromResource(R.drawable.mark_red);
        BitmapDescriptor bitmap_white = BitmapDescriptorFactory
                .fromResource(R.drawable.mark_white);



        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();

        giflist.add(bitmap_red);
        giflist.add(bitmap_white);

        OverlayOptions ooD = new MarkerOptions().position(point2).icons(giflist)
                .zIndex(0).period(10);

        mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));*/

    }

    /**
     * 设置当前位置为地图中心
     * @param location
     */
    private void navigateTo(BDLocation location) {
        if (isFirstLocate) {
            //将当前位置设置为地图中心
            isFirstLocate = false;
            //LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            //LatLng ll = new LatLng(29,121);
            Log.d("navigateTo","navigateTo执行,传入纬度：" + location.getLatitude()+"经度：" + location.getLongitude());
            Log.d("navigateTo","navigateTo执行,传入纬度：" + ll.latitude+"经度：" + ll.longitude);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLngZoom(ll,10f);//设置缩放大小
            baiduMap.animateMapStatus(update);
        }
        //显示我当前的位置
        MyLocationData.Builder locationBuilder = new MyLocationData.
                Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    /**
     * 启动定位
     */
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    /**
     * 5000毫秒刷新一次当前位置
     */
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    /**
     * 请求权限结果反馈，及错误提示
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    /**
     * 对自定义标记Mark进行监听
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this,marker.getId().toString()+"", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     *
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //            StringBuilder currentPosition = new StringBuilder();
            //            currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
            //            currentPosition.append("经线：").append(location.getLongitude()).append("\n");
            //            currentPosition.append("国家：").append(location.getCountry()).append("\n");
            //            currentPosition.append("省：").append(location.getProvince()).append("\n");
            //            currentPosition.append("市：").append(location.getCity()).append("\n");
            //            currentPosition.append("区：").append(location.getDistrict()).append("\n");
            //            currentPosition.append("街道：").append(location.getStreet()).append("\n");
            //            currentPosition.append("定位方式：");
            //            if (location.getLocType() == BDLocation.TypeGpsLocation) {
            //                currentPosition.append("GPS");
            //            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
            //                io.append("网络");
            //            }
            //            positionText.setText(currentPosition);
            if (location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }
        }
    }


}
