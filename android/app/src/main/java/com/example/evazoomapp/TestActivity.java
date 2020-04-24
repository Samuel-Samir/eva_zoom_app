package com.example.evazoomapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import us.zoom.sdk.ZoomSDKInitializeListener;

public class TestActivity extends AppCompatActivity implements ZoomSDKInitializeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    public void onZoomSDKInitializeResult(int i, int i1) {

    }

    @Override
    public void onZoomAuthIdentityExpired() {

    }
}
