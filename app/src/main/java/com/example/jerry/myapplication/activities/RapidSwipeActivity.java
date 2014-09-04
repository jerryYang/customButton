package com.example.jerry.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.jerry.myapplication.R;
import com.example.jerry.myapplication.views.RapidSwipeView;

import java.util.Timer;
import java.util.TimerTask;


public class RapidSwipeActivity extends Activity implements View.OnTouchListener{

  private float rowX;
  private float rowY;
  private RapidSwipeView mRapidView;
  private View mRootView;
  private int dismissTime = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.rapid_swipe_layout );
        mRapidView = ( RapidSwipeView ) findViewById( R.id.myview);
        mRootView = findViewById( R.id.root );
        View rootView = findViewById( R.id.root );
      rootView.setOnTouchListener( this );
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

  private void dismiss(){
    Timer timer = new Timer(  );
    timer.scheduleAtFixedRate( new TimerTask() {
      @Override
      public void run() {

      }
    }, 0, 10 );
  }

  @Override
  public boolean onTouch( View view, MotionEvent motionEvent ) {

    Log.d("jerry", motionEvent.getAction() + "");
    switch ( motionEvent.getAction() ){
      case MotionEvent.ACTION_DOWN:
        rowX = motionEvent.getRawX();
        rowY = motionEvent.getRawY();
        break;
      case MotionEvent.ACTION_MOVE:
        mRapidView.update( motionEvent.getX() - rowX, motionEvent.getRawY() - rowY );
        rowX = motionEvent.getRawX();
        rowY = motionEvent.getRawY();

        break;
      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        mRapidView.goBack();
        break;
    }
    return true;
  }
}