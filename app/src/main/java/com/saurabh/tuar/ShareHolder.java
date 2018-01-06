package com.saurabh.tuar;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareHolder {
    Context context;
    SharedPreferences sharedPreferences;
    ShareHolder(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Database",Context.MODE_PRIVATE);
    }

    public void AddUser(String Name){
        Editor editor = sharedPreferences.edit();
        editor.putString("Name" , Name);
        editor.commit();
        context.startService(new Intent(context , PositionService.class));
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

    public String getName() {
        return sharedPreferences.getString("Name","");
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
