package com.saurabh.tuar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MapSettingActivity extends AppCompatActivity {

    private ShareHolder shareHolder;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_setting);
        shareHolder = new ShareHolder(MapSettingActivity.this);
        listView = findViewById(R.id.MapsSettingList);
        listView.setAdapter(new CustomAdapter());
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 3;
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
            final View map = getLayoutInflater().inflate(R.layout.map_setting_eachrow, null);
            ImageView image = map.findViewById(R.id.MapsSettingImage);
            TextView Major = map.findViewById(R.id.MapsSettingMajorText);
            TextView Minor = map.findViewById(R.id.MapsSettingMinorText);

            switch (i) {
                case 1:
                    image.setImageResource(R.drawable.sate);
                    Major.setText("Satellite");
                    Minor.setText("Map show Satellite View + No name of location and place");
                    break;

                case 2:
                    image.setImageResource(R.drawable.hybrid);
                    Major.setText("Hybrid Map");
                    Minor.setText("Map show Satellite View + Give name of location and surrounding");
                    break;

            }
            
            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i){
                        case 0:
                            shareHolder.setMap("normal");
                            startActivity(new Intent(MapSettingActivity.this , MapsActivity.class));
                            finish();
                            break;
                        case 1:
                            shareHolder.setMap("sate");
                            startActivity(new Intent(MapSettingActivity.this , MapsActivity.class));
                            finish();
                            break;
                        case 2:
                            shareHolder.setMap("hybrid");
                            startActivity(new Intent(MapSettingActivity.this , MapsActivity.class));
                            finish();
                            break;
                    }
                }
            });
            return map;
        }
    }
}
