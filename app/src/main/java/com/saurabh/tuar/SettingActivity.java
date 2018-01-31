package com.saurabh.tuar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    ListView listView;
    ShareHolder shareHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        shareHolder = new ShareHolder(SettingActivity.this);
        listView = findViewById(R.id.SettingList);
        listView.setAdapter(new CustomAdapter());
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (shareHolder.isUser()){
                return 3;
            }else{
                return 2;
            }
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            View settingView = getLayoutInflater().inflate(R.layout.setting_eachrow, null);
            final TextView Setting = settingView.findViewById(R.id.SettingName);
            ImageView image = settingView.findViewById(R.id.SettingImage);
            switch (i) {
                case 0:
                    Setting.setText("Profile Setting");
                    image.setImageResource(R.mipmap.ic_person);
                    break;
                case 1:
                    Setting.setText("Maps Setting");
                    image.setImageResource(R.mipmap.ic_map);
                    break;
                case 2:
                    Setting.setText("Sign Out");
                    Setting.setTextColor(getResources().getColor(R.color.RED));
                    image.setImageResource(R.mipmap.ic_out);
                    break;

            }

            LinearLayout linearLayout = settingView.findViewById(R.id.SettingEachLayout);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i) {
                        case 0:
                            if (shareHolder.isUser()) {
                                startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
                                finish();
                            } else {
                                Toast.makeText(SettingActivity.this, "Login to use this", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case 1:
                            startActivity(new Intent(SettingActivity.this, MapSettingActivity.class));
                            finish();
                            break;
                        case 2:

                            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                            View PassView = getLayoutInflater().inflate(R.layout.password_layout , null);
                            final EditText editText = PassView.findViewById(R.id.PasswordLayEditText);
                            Button btn = PassView.findViewById(R.id.PasswordLayButton);
                            builder.setView(PassView);

                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (shareHolder.getPassword().toString().equals(editText.getText().toString())){
                                        shareHolder.setName("");
                                        shareHolder.setPassword("");
                                        Toast.makeText(SettingActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SettingActivity.this , MapsActivity.class));
                                        finish();
                                    }
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            break;
                    }
                }
            });
            return settingView;
        }
    }
}
