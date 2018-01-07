package com.saurabh.tuar;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PositionService extends Service {

    LocationManager locationManager;
    ShareHolder shareHolder;
    String lat, lng;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LaunchLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if ((ActivityCompat.checkSelfPermission(PositionService.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            if (shareHolder.getName()!=""){   // Not Sign Out...
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 0, locationListener);
            }
        }else {
            Toast.makeText(this, "Permissions are Denied..", Toast.LENGTH_SHORT).show();
        }

        return START_STICKY;
    }

    @Override
    public void onCreate() {

        shareHolder = new ShareHolder(PositionService.this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        super.onCreate();
    }

    public void LaunchLocation(final Location location) {

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                OnDataBase(location);
            }
        });
    }

    public void OnDataBase(final Location location) {
        lat = shareHolder.getLat();
        lng = shareHolder.getLng();
        Log.d("return_ans", location.getLatitude() + " - " + location.getLongitude());

        if (!(lat.equals(String.valueOf(location.getLatitude())) || lng.equals(String.valueOf(location.getLongitude())))) {
            shareHolder.AddLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            Log.d("return_ans", "Location changed...");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://browbeaten-fingers.000webhostapp.com/FindMe/position.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", shareHolder.getName());
                    Log.d("return_ans",shareHolder.getName());
                    map.put("lat", String.valueOf(location.getLatitude()));
                    map.put("lng", String.valueOf(location.getLongitude()));
                    return map;
                }
            };
            MySending.getInstance(PositionService.this).addToRequestQueue(stringRequest);
        }

    }
}
