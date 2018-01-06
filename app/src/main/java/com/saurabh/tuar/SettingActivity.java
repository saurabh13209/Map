package com.saurabh.tuar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
        listView = findViewById(R.id.SettingList);
        listView.setAdapter(new CustomAdapter());
        shareHolder = new ShareHolder(SettingActivity.this);
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 2;
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
                    }
                }
            });
            return settingView;
        }
    }
}
