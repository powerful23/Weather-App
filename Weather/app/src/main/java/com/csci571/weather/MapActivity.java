package com.csci571.weather;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Zhuo on 2015/11/24.
 */
public class MapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String lat = intent.getStringExtra("lat");
        String lng = intent.getStringExtra("lng");

        Bundle bundle = new Bundle();
        bundle.putString("lat",lat);
        bundle.putString("lng", lng);

        MapFragment mapFragment = new MapFragment();
        mapFragment.setArguments(bundle);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, mapFragment).commit();
        setContentView(R.layout.activity_map);
    }
}
