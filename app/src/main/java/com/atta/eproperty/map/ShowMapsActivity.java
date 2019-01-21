package com.atta.eproperty.map;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.atta.eproperty.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    float latitude, longitude;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        latitude = (float) getIntent().getSerializableExtra("latitude");
        longitude = (float) getIntent().getSerializableExtra("longitude");
        type = (String) getIntent().getSerializableExtra("type");
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

        //Place current location marker
        LatLng myLatLng = new LatLng(latitude, longitude);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 16.0f));
        MarkerOptions marker = new MarkerOptions().position(myLatLng).title(type);
        mMap.addMarker(marker);
    }

}
