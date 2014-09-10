package com.example.jerry.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.jerry.myapplication.R;
import com.example.jerry.myapplication.views.CustomButton;


public class CustomButtonActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView( R.layout.custom_button_layout );
        CustomButton customButton = ( CustomButton ) findViewById( R.id.myview);
        customButton.setSize( 700, 200 );
        }

        }