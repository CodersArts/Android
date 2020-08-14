package com.example.usercaranimation;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.usercaranimation.map.common;
import com.example.usercaranimation.map.interface1;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<LatLng> polyLineList;
    private Marker marker;
    private float v;
    private double lat, lng;
    private Handler handler;
    private LatLng startPosition, endPosition;
    private int index, next;
    private Button btngo;
    private EditText editPlace;
    private String destination;
    private PolylineOptions polylineOptions, blackpolylineoptions;
    private Polyline blackpolyline, grayPolyline;
    private LatLng mlocation;
    interface1 mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //  mapFragment.getMapAsync(this);
        polyLineList = new ArrayList<>();
        btngo = findViewById(R.id.BtnSearch);
        editPlace = findViewById(R.id.editplace);
        btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destination = editPlace.getText().toString();
                destination = destination.replace("", "+");
                mapFragment.getMapAsync(MapsActivity.this);
            }
        });
        mService = common.GetInterface1();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(false);
        mMap.setIndoorEnabled(false);
        mMap.setBuildingsEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);

      

        // Add a marker in Sydney and move the camera
        final LatLng sydney = new LatLng(26.016560, 81.643900);
        mMap.addMarker(new MarkerOptions().position(sydney).title("my location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(googleMap.getCameraPosition().target)
                .zoom(17)
                .bearing(30)
                .tilt(45).build()));
        String requestUrl = null;
        try {
            requestUrl = "https://maps.googleapis.com/maps/api/directions/json?" + "modemdrivig&"+"transit_routing_preference=less_driving"+"origin="+sydney.latitude+sydney.latitude+"&"+"destination="+destination+"&"+"key="+getResources().getString(R.string.google_direction_key);
            Log.d("onMapReady: ", requestUrl);
            mService.getdataFromGoogleApi(requestUrl).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray jsonArray = jsonObject.getJSONArray("routes");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject rout = jsonArray.getJSONObject(i);
                            JSONObject poly = rout.getJSONObject("overview_polyline");
                            String polyline = poly.getString("position");
                            polyLineList = decodepoly(polyline);
                        }
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng latLng : polyLineList) builder.include(latLng);
                        LatLngBounds bounds = builder.build();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 2);
                        mMap.animateCamera(cameraUpdate);
                        polylineOptions = new PolylineOptions();
                        polylineOptions.color(Color.GRAY);
                        polylineOptions.width(5);
                        polylineOptions.startCap(new SquareCap());
                        polylineOptions.endCap(new SquareCap());
                        polylineOptions.jointType(JointType.ROUND);
                        polylineOptions.addAll(polyLineList);
                        grayPolyline = mMap.addPolyline(polylineOptions);

                        blackpolylineoptions = new PolylineOptions();
                        blackpolylineoptions.color(Color.BLACK);
                        blackpolylineoptions.width(5);
                        blackpolylineoptions.startCap(new SquareCap());
                        blackpolylineoptions.endCap(new SquareCap());
                        blackpolylineoptions.jointType(JointType.ROUND);
                        blackpolylineoptions.addAll(polyLineList);
                        blackpolyline = mMap.addPolyline(blackpolylineoptions);
                        mMap.addMarker(new MarkerOptions().position(polyLineList.get(polyLineList.size() - 1)));
                        final ValueAnimator polylineAnimation = ValueAnimator.ofInt(0, 100);
                        polylineAnimation.setDuration(2000);
                        polylineAnimation.setInterpolator(new LinearInterpolator());
                        polylineAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                List<LatLng> points = grayPolyline.getPoints();
                                int percentValue = (int) animation.getAnimatedValue();
                                int size = points.size();
                                int newPoints = (int) (size * (percentValue / 100.0f));
                                List<LatLng> p = points.subList(0, newPoints);
                                blackpolyline.setPoints(p);

                            }
                        });
                        polylineAnimation.start();
                        marker = mMap.addMarker(new MarkerOptions().position(sydney)
                                .flat(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_car_black_24dp)));
                        handler = new Handler();
                        index = -1;
                        next = -1;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (index < polyLineList.size() - 1) {
                                    index++;
                                    next = index + 1;
                                }
                                if (index < polyLineList.size() - 1) {
                                    startPosition = polyLineList.get(index);
                                    endPosition = polyLineList.get(next);
                                }
                                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                                valueAnimator.setDuration(3000);
                                valueAnimator.setInterpolator(new LinearInterpolator());
                                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                    @Override
                                    public void onAnimationUpdate(ValueAnimator animation) {
                                        v = animation.getAnimatedFraction();
                                        lng = v * endPosition.longitude + (1 - v) * startPosition.longitude;
                                        lat = v * endPosition.longitude + (1 - v) * startPosition.latitude;
                                        LatLng newPos = new LatLng(lat, lng);
                                        marker.setPosition(newPos);
                                        marker.setAnchor(0.5f, .05f);
                                        marker.setRotation(getBearing(startPosition, newPos));
                                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(newPos).zoom(15.5f).build()));


                                    }
                                });
                                valueAnimator.start();
                                handler.postDelayed(this, 3000);

                            }
                        }, 3000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(MapsActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private float getBearing(LatLng startPosition, LatLng newPos) {
        double lat = Math.abs(startPosition.latitude - newPos.latitude);
        double lng = Math.abs(startPosition.longitude - newPos.longitude);

        if (startPosition.latitude < newPos.latitude && startPosition.longitude < newPos.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if(startPosition.latitude>=newPos.latitude&&startPosition.longitude < newPos.longitude)
            return (float)(90-Math.toDegrees(Math.atan(lng/lat))+90);
        else if(startPosition.latitude>=newPos.latitude&&startPosition.longitude >= newPos.longitude)
            return (float)(Math.toDegrees(Math.atan(lng/lat))+180);
        else if(startPosition.latitude<newPos.latitude&&startPosition.longitude >=newPos.longitude)
            return (float)(90-Math.toDegrees(Math.atan(lng/lat))+270);
        return -1;
    }


    private List<LatLng> decodepoly(String encoded) {
        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            poly.add(new LatLng((double) lat / 1E5, (double) lng / 1E5));
        }

        return poly;

    }

}
