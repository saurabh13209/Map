package com.saurabh.tuar;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText editText;
    private ImageButton imageButton;
    private ProgressDialog progressDialog;
    private ShareHolder shareHolder;
    private AlertDialog dialog;
    private LatLng position = new LatLng(-34, 151);

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MapsActivity.this, PositionService.class);
                    startService(intent);
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!provider.contains("gps")) { //if gps is disabled
            Toast.makeText(this, "Please turn ON your GPS", Toast.LENGTH_LONG).show();
            finish();
        }


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        imageButton = findViewById(R.id.MapSearch);
        editText = findViewById(R.id.MapName);

        shareHolder = new ShareHolder(MapsActivity.this);


        if (shareHolder.isUser()) {
            if ((ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                Log.d("return_ans", "main");
                Intent intent = new Intent(MapsActivity.this, PositionService.class);
                startService(intent);
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }

            if (shareHolder.getLat() != "" && shareHolder.getLng() != "") {
                position = new LatLng(Double.valueOf(shareHolder.getLat()), Double.valueOf(shareHolder.getLng()));
            }

        }
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchMain();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    SearchMain();
                    return false;
                }
                return true;
            }
        });
    }

    private void SearchMain() {
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);

        if (shareHolder.isUser()) {
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://browbeaten-fingers.000webhostapp.com/FindMe/getLocation.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray array = new JSONArray(response);
                        if (!array.get(0).toString().equals("null")) {
                            position = new LatLng(Double.valueOf(array.get(0).toString()), Double.valueOf(array.get(1).toString()));
                            setLocation(position);
                        } else {
                            Toast.makeText(MapsActivity.this, "Search not found..", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", editText.getText().toString());
                    return map;
                }
            };

            MySending.getInstance(MapsActivity.this).addToRequestQueue(stringRequest);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
            View view = getLayoutInflater().inflate(R.layout.login_layout, null);
            builder.setView(view);

            dialog = builder.create();
            dialog.show();

            TextView SingInButton = view.findViewById(R.id.SignInButtob);
            SingInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.setContentView(R.layout.signin_layout);
                    Button next = dialog.getWindow().getDecorView().findViewById(R.id.CreateAccountButton);

                    final EditText Name = dialog.getWindow().getDecorView().findViewById(R.id.CreateAccountUserName);
                    final EditText Password = dialog.getWindow().getDecorView().findViewById(R.id.CreateAccountUserPassword);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Password.getText().toString().length() > 3) {
                                progressDialog.show();
                                createAccount(Name.getText().toString(), Password.getText().toString());
                            }else {
                                Toast.makeText(MapsActivity.this, "Password length minimum of 4 character", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });

            Button Login = view.findViewById(R.id.LoginButton);
            final EditText Name = view.findViewById(R.id.LoginUserName);
            final EditText Pass = view.findViewById(R.id.LoginUserPassword);
            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog.show();
                    LoginUser(Name.getText().toString(), Pass.getText().toString());
                }
            });
        }

    }

    private void LoginUser(final String Name, final String Password) {
        StringRequest LoginRequest = new StringRequest(Request.Method.POST, "https://browbeaten-fingers.000webhostapp.com/FindMe/MainName.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                dialog.dismiss();
                Log.d("return_ans", response.toString());
                try {
                    JSONArray array = new JSONArray(response);
                    if (array.get(0).toString().equals("true")) {
                        Toast.makeText(MapsActivity.this, "Welcome " + Name, Toast.LENGTH_SHORT).show();
                        shareHolder.AddUser(Name);
                    } else {
                        Toast.makeText(MapsActivity.this, "No Account found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                Log.d("return_ans", Name + " - " + Password);
                map.put("username", Name);
                map.put("password", Password);
                return map;
            }
        };

        MySending.getInstance(MapsActivity.this).addToRequestQueue(LoginRequest);
    }

    private void createAccount(final String Name, final String password) {
        StringRequest CreateAccount = new StringRequest(Request.Method.POST, "https://browbeaten-fingers.000webhostapp.com/FindMe/generate_account.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                dialog.dismiss();
                if (response.toString().equals("yes")) {
                    Toast.makeText(MapsActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    shareHolder.AddUser(Name);
                } else {
                    Toast.makeText(MapsActivity.this, "Name Occupied", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", Name);
                map.put("password", password);
                return map;
            }
        };

        MySending.getInstance(MapsActivity.this).addToRequestQueue(CreateAccount);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        setLocation(position);
    }

    public void setLocation(LatLng latLng) {
        mMap.clear();
        CameraPosition cameraPosition = CameraPosition.builder().target(latLng).zoom(17).build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
