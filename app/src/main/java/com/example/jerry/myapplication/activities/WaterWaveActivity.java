package com.example.jerry.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.jerry.myapplication.R;
import com.example.jerry.myapplication.views.WaterWaveView;


public class WaterWaveActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView( R.layout.water_wave_layout );

      WaterWaveView view = ( WaterWaveView ) findViewById( R.id.myview );
      view.splash( 10 , 200f);
    }
}