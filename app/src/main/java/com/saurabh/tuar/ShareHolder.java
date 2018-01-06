package com.saurabh.tuar;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class ShareHolder {
    Context context;
    SharedPreferences sharedPreferences;
    ShareHolder(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Database",Context.MODE_PRIVATE);
    }

    public void AddUser(String Name , String password){
        Editor editor = sharedPreferences.edit();
        editor.putString("Name" , Name);
        editor.putString("Password" , password);
        editor.commit();
        if ((ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(context , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)){
            context.startService(new Intent(context , PositionService.class));
        }else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void setPassword(String password){
        Editor editor = sharedPreferences.edit();
        editor.putString("Password" ,password );
        editor.commit();
    }

    public void setName(String Name){
        Editor editor = sharedPreferences.edit();
        editor.putString("Name" , Name);
        editor.commit();
    }

    public String getPassword(){
        return sharedPreferences.getString("Password","");
    }

    public String getName(){
        return sharedPreferences.getString("Name","");
    }

    public boolean isUser(){
        if (sharedPreferences.getString("Name","").equals("")){
            return false;
        }else {
            return true;
        }
    }

    public String getLat(){
        return sharedPreferences.getString("Lat","");
    }


    public String getLng() {
        return sharedPreferences.getString("Lng","");
    }

    public void AddLocation(String Lat, String Lng) {
        Editor editor = sharedPreferences.edit();
        editor.putString("Lat",Lat);
        editor.putString("Lng",Lng);
        editor.commit();
    }


    public void setMap(String map){
        Editor editor = sharedPreferences.edit();
        editor.putString("map" , map);
        editor.commit();
    }

    public String getMap(){
        return sharedPreferences.getString("map","");
    }
}
