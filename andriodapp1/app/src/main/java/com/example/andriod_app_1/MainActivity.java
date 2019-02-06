package com.example.andriod_app_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void search(View view){
        EditText addressText = (EditText)findViewById(R.id.editText);
        String address = addressText.getText().toString();
        final TextView dataTest = (TextView)findViewById(R.id.textView);


            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=1600%20Amphitheatre%20Parkway%2C%20Mountain%20View%2C%20CA&key=AIzaSyBsOyKBK7Wu1f-d-zAWgdURKOVWKdCUxD8&fbclid=IwAR29qRaNGlOUjRGCaEyFi8-dnd1Gc11ZacQp2oxkOK-jPGSnxfYTNqT1CTg";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dataTest.setText("Response is: " + response.substring(0, 500));
                }
            }, new Response.ErrorListener(){
                    @Override
                            public void onErrorResponse(VolleyError error){
                        dataTest.setText("FAIL");
                    }
            });
            queue.add(stringRequest);


    }


}
