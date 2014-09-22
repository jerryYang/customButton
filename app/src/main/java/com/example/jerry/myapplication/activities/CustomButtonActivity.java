package com.example.jerry.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.jerry.myapplication.R;
import com.example.jerry.myapplication.views.CustomButton;


public class CustomButtonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView( R.layout.custom_button_layout );
        final CustomButton customButton = ( CustomButton ) findViewById( R.id.myview);
        customButton.setSize( 400, 200 );

      Switch switcher = ( Switch ) findViewById( R.id.bounce );

      switcher.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged( CompoundButton compoundButton, boolean b ) {
          customButton.enableBounce( b );
        }
      } );
    }

    }