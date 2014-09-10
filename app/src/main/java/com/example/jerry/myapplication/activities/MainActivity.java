package com.example.jerry.myapplication.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.jerry.myapplication.R;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.main_layout );
      findViewById( R.id.rapid ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          rapidSwipe();
        }
      } );

      findViewById( R.id.slow ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          slowSwipe();
        }
      } );

      findViewById( R.id.demo ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          navigate();
        }
      } );

      findViewById( R.id.freehand ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          navigateToCubicFreehand();
        }
      } );

      findViewById( R.id.cubic ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          navigateToCubicDraw();
        }
      } );

      findViewById( R.id.topBounce ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          topBounce();
        }
      } );

      findViewById( R.id.swipe_through ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          navigateToSiblingSwipeThrough();
        }
      } );

      findViewById( R.id.water_wave ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          waterWave();
        }
      } );

      findViewById( R.id.smooth_water_wave ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          smoothWaterWave();
        }
      } );

      findViewById( R.id.custom_button ).setOnClickListener( new View.OnClickListener() {
        @Override
        public void onClick( View view ) {
          customButton();
        }
      } );
    }

    private void rapidSwipe(){
      Intent intent = new Intent(this, RapidSwipeActivity.class);
      startActivity( intent );
    }

    private void slowSwipe(){
      Intent intent = new Intent(this, SlowSwipeActivity.class);
      startActivity( intent );
    }

    private void topBounce(){
      Intent intent = new Intent(this, TopBounceActivity.class);
      startActivity( intent );
    }

    private void navigate(){
      Intent intent = new Intent( this, DemoActivity.class );
      startActivity( intent );
    }

    private void navigateToCubicFreehand(){
      Intent intent = new Intent( this, CubicFreehandDrawActivity.class );
      startActivity( intent );
    }

    private void navigateToSiblingSwipeThrough(){
      Intent intent = new Intent( this, SiblingSwipeThroughActivity.class );
      startActivity( intent );
    }

  private void navigateToCubicDraw(){
    Intent intent = new Intent(this, CubicDrawActivity.class);
    startActivity( intent );
  }

  private void waterWave(){
    Intent intent = new Intent(this, WaterWaveActivity.class);
    startActivity( intent );
  }

  private void customButton(){
    Intent intent = new Intent(this, CustomButtonActivity.class);
    startActivity( intent );
  }

  private void smoothWaterWave(){
    Intent intent = new Intent(this, SmoothWaterWaveActivity.class);
    startActivity( intent );
  }

  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}