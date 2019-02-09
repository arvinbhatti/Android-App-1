package com.example.mapstemplate;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double lat;
    private Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
    public void changeMap(Double lat, Double lng, String marker){
            mMap.clear();
            LatLng loc = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(loc).title(marker));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

    }

    public void search(View view){
        final EditText addressText = (EditText)findViewById(R.id.editText);
        String address = addressText.getText().toString();
        address = address.trim();
        address = address.replaceAll(" ", "+");
        final TextView weatherText = (TextView)findViewById(R.id.weather);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="+address+"&key=AIzaSyBsOyKBK7Wu1f-d-zAWgdURKOVWKdCUxD8";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject responseJSON = (JSONObject) parser.parse(response);;
                    JSONArray resultsJSON = (JSONArray) responseJSON.get("results");
                    if (resultsJSON.size() == 0){
                        return;
                    }
                    JSONObject resultsJSON1 = (JSONObject) resultsJSON.get(0);
                    JSONObject geometryJSON = (JSONObject) resultsJSON1.get("geometry");
                    JSONObject locationJSON = (JSONObject) geometryJSON.get("location");
                    lat = (Double) locationJSON.get("lat");
                    lng = (Double) locationJSON.get("lng");
                    String formattedAdress = (String) resultsJSON1.get("formatted_address");
                    addressText.setText(formattedAdress);
                    changeMap(lat,lng,formattedAdress);
                    searchWeather(lat, lng);

                }
                catch (Exception e){
                    weatherText.setText(e.toString());
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                weatherText.setText("FAIL");
            }
        });
        queue.add(stringRequest);


    }
    public void searchWeather(Double lat, Double lng){
        final TextView weatherText = (TextView)findViewById(R.id.weather);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.darksky.net/forecast/4fb85cd756e4277986bfabc165fbe85b/"+lat+","+lng;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //temperature, humidity, wind speed, precipitation
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject responseJSON = (JSONObject) parser.parse(response);;
                    JSONObject currentlyJSON = (JSONObject) responseJSON.get("currently");
                    Object temp = currentlyJSON.get("temperature");
                    Object hum = currentlyJSON.get("humidity");
                    Object wind = currentlyJSON.get("windSpeed");
                    Object precipProbability = currentlyJSON.get("precipProbability");
                    String precipProbabilityString = "" + precipProbability;
                    if (!precipProbabilityString.equals("0")){
                        Object precipType = currentlyJSON.get("precipType");
                        weatherText.setText("Currenty: " + temp + " F; Humidity: " + hum + "\n Wind Speed:" + wind + "MPH; Precipitation: " +  precipType);
                    }
                    else{
                        weatherText.setText("Currenty: " + temp + " F; Humidity: " + hum + "\n Wind Speed:" + wind + "MPH; Precipitation: NONE");
                    }

                }
                catch (Exception e){
                    weatherText.setText(e.toString());
                }

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                weatherText.setText("FAIL");
            }
        });
        queue.add(stringRequest);


    }
}
