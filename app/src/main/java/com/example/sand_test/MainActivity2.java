package com.example.sand_test;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/*
public class MainActivity2 extends AppCompatActivity {

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE=100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        if(!checkLocationServiceStatus()){
            showDialogForLocationSerivceSetting();
        }
        else{
            chechRunTimePermission();
        }

        final TextView textView_address = (TextView) findViewById(R.id.txtResult);

        Button showLocationButton = (Button) findViewById(R.id.button1);
        showLocationButton.setOnClickListener(new View.onClickListener() {
            @Override
            public void onClick2(View arg0) {
                gpsTracker = new GpsTracker(MainActivity2.this);

                double latitude = gpsTracker.getLatitude();
                double lonitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);
                textView_address.setText(address);

                Toast.makeText(MainActivity2.this,"현재위치 \n위도" + latitude + "\n경도" + longitude, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRequestPermissionResult(int permsRequestCode, @nonNull String[] permissions, @NonNull int[] grandResults){
        if(permsRequestCode==PERMISSIONS_REQUEST_CODE && grandResults.length==REQUIRED_PERMISSIONS.length){

            boolean check_result = true;

            for(int result: grandResults){
                if(result!= PackageManager.PERMISSION_GRANTED){
                    check_result=false;
                    break;
                }
            }

            if(check_result){
                ;
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(MainActivity2.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(MainActivity2.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.", Toast.LENGTH_LONG).show();
                }
            }
        }
        void checkRunTimePermission(){
            int h
        }
    }
*/
public class MainActivity2 extends AppCompatActivity {
    private Button button1;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        button1 = (Button) findViewById(R.id.button1);
        txtResult = (TextView) findViewById(R.id.txtResult);

        final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // getSYstemServive()메소드를 사용하여 객체 참조
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            0);
                } else {
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    // 가장최근 위치정보 가져오기
                    if (location != null) {
                        String provider = location.getProvider();
                        double longitude = location.getLongitude();
                        double latitude = location.getLatitude();
                        double altitude = location.getAltitude();


                        txtResult.setText("위치정보 : " + provider + "\n" +
                                "위도 : " + longitude + "\n" +
                                "경도 : " + latitude + "\n" +
                                "고도  : " + altitude);
                    }
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            1000,
                            1,
                            gpsLocationListener);
                }
            }
        });

    }

    final LocationListener gpsLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            double altitude = location.getAltitude();

            txtResult.setText("위치정보 : " + provider + "\n" +
                    "경도 : " + longitude + "\n" +
                    "위도 : " + latitude + "\n" +
                    "고도  : " + altitude);

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }


    };
}

