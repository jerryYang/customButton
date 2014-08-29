package com.example.jerry.custombutton;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.jerry.custombutton.View.RoundButton;


public class MyActivity extends Activity {

  private int mStroke = 5;
  private int mRadius = 50;
  private int mWidth = 200;
  private int mHeight = 100;
  private boolean mShowShadow = true;
  private int mStrokeColor = Color.BLUE;
  private int mFillColor = Color.CYAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_my);
      init();
    }

    private void init(){
      final RoundButton button = ( RoundButton ) findViewById( R.id.button );

      button.update( mStroke, mRadius, mWidth, mHeight, mShowShadow, mStrokeColor, mFillColor );

      SeekBar widthSB = ( SeekBar ) findViewById( R.id.width );
      widthSB.setMax( 1000 );
      widthSB.setProgress( 200 );
      widthSB.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged( SeekBar seekBar, int i, boolean b ) {
          button.updateWidth( i );
        }

        @Override
        public void onStartTrackingTouch( SeekBar seekBar ) {

        }

        @Override
        public void onStopTrackingTouch( SeekBar seekBar ) {

        }
      } );

      SeekBar heightSB = ( SeekBar ) findViewById( R.id.height );
      heightSB.setMax( 800 );
      heightSB.setProgress( 100 );
      heightSB.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged( SeekBar seekBar, int i, boolean b ) {
          button.updateHeight( i );
        }

        @Override
        public void onStartTrackingTouch( SeekBar seekBar ) {

        }

        @Override
        public void onStopTrackingTouch( SeekBar seekBar ) {

        }
      } );
      SeekBar radiusSB = ( SeekBar ) findViewById( R.id.radius );
      radiusSB.setMax( 500 );
      radiusSB.setProgress( 50 );
      radiusSB.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged( SeekBar seekBar, int i, boolean b ) {
          button.updateRadius( i );
        }

        @Override
        public void onStartTrackingTouch( SeekBar seekBar ) {

        }

        @Override
        public void onStopTrackingTouch( SeekBar seekBar ) {

        }
      } );

      SeekBar strokeSB = ( SeekBar ) findViewById( R.id.stroke );
      strokeSB.setMax( 50 );
      strokeSB.setProgress( 5 );
      strokeSB.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged( SeekBar seekBar, int i, boolean b ) {
          button.updateStroke( i );
        }

        @Override
        public void onStartTrackingTouch( SeekBar seekBar ) {

        }

        @Override
        public void onStopTrackingTouch( SeekBar seekBar ) {

        }
      } );

      Switch shadowSW = ( Switch ) findViewById( R.id.shadow );
      shadowSW.setChecked( true );
      shadowSW.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged( CompoundButton compoundButton, boolean b ) {
          button.showShadow( b );
        }
      });
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
