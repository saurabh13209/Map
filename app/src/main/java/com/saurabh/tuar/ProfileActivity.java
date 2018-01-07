package com.saurabh.tuar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private View view;
    private ShareHolder shareHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ListView list = findViewById(R.id.ProfileList);
        list.setAdapter(new CustomAdapter());
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        shareHolder = new ShareHolder(ProfileActivity.this);

    }

    private void ChangeMain(final String Url, final boolean task, final EditText editText) {

        progressDialog.show();
        StringRequest changePassword = new StringRequest(Request.Method.POST, Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if (task) {
                    alertDialog.dismiss();
                    shareHolder.setPassword(editText.getText().toString());
                    Toast.makeText(ProfileActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    shareHolder.setName(editText.getText().toString());
                    Toast.makeText(ProfileActivity.this, "UserName Changed", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Sorry ,  Please try again..", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("OldName", shareHolder.getName().toString());
                map.put("NewName", editText.getText().toString());
                if (task) {
                    map.put("task", "Pass");
                } else {
                    map.put("task", "Name");
                }
                return map;
            }
        };
        MySending.getInstance(ProfileActivity.this).addToRequestQueue(changePassword);

    }

    private void ChangeValue(final String Url, final boolean task) {

        builder = new AlertDialog.Builder(ProfileActivity.this);
        view = getLayoutInflater().inflate(R.layout.value_change, null);

        TextView textView = view.findViewById(R.id.ChangeValueName);
        final EditText editText = view.findViewById(R.id.ChangeValueEditText);
        Button next = view.findViewById(R.id.ChangeValueButton);

        if (!task) {
            textView.setText("Enter new Username");
            editText.setHint("New Name");
        } else {
            textView.setText("Enter new Password");
            editText.setHint("New Password");
        }

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task) {
                    if (editText.getText().toString().length() < 4) {
                        Toast.makeText(ProfileActivity.this, "Password Length must greater than 4 character", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        ChangeMain(Url, task, editText);
                    }
                } else {
                    ChangeMain(Url, task, editText);
                }
            }
        });

    }

    private void CheckPassword(final Boolean task) {
        builder = new AlertDialog.Builder(ProfileActivity.this);
        view = getLayoutInflater().inflate(R.layout.password_layout, null);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        final EditText password = view.findViewById(R.id.PasswordLayEditText);
        Button button = view.findViewById(R.id.PasswordLayButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shareHolder.getPassword().equals("")) {
                    progressDialog.show();
                    StringRequest getPassword = new StringRequest(Request.Method.POST, "https://browbeaten-fingers.000webhostapp.com/FindMe/getPassword.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressDialog.dismiss();
                                JSONArray array = new JSONArray(response);
                                shareHolder.setPassword(array.getString(0).toString());
                                if (array.get(0).toString().equals(password.getText().toString())) {
                                    alertDialog.dismiss();
                                    Toast.makeText(ProfileActivity.this, "Password Matched", Toast.LENGTH_SHORT).show();
                                    ChangeValue("https://browbeaten-fingers.000webhostapp.com/FindMe/ChangeUser.php", task);
                                } else {
                                    Toast.makeText(ProfileActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
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
                            Map map = new HashMap();
                            map.put("Name", shareHolder.getName());
                            return map;
                        }
                    };

                    MySending.getInstance(ProfileActivity.this).addToRequestQueue(getPassword);
                } else {
                    alertDialog.dismiss();
                    if (shareHolder.getPassword().equals(password.getText().toString())) {
                        Toast.makeText(ProfileActivity.this, "Password Matched", Toast.LENGTH_SHORT).show();
                        ChangeValue("https://browbeaten-fingers.000webhostapp.com/FindMe/ChangeUser.php", task);
                    } else {
                        Toast.makeText(ProfileActivity.this, "wrong Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void ChangeName() {
        CheckPassword(false);
    }

    private void ChangePassword() {
        CheckPassword(true);
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
            View Pro = getLayoutInflater().inflate(R.layout.profile_eachrow, null);
            TextView text = Pro.findViewById(R.id.ProfileName);
            ImageView imageView = Pro.findViewById(R.id.ProfileImage);
            switch (i) {
                case 0:
                    text.setText("Change Username");
                    imageView.setImageResource(R.mipmap.ic_person);
                    break;
                case 1:
                    text.setText("Change Password");
                    imageView.setImageResource(R.drawable.ic_finger);
                    break;
            }

            Pro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (i) {
                        case 0:
                            ChangeName();
                            break;
                        case 1:
                            ChangePassword();
                            break;
                    }
                }
            });
            return Pro;
        }
    }


}
