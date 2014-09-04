package com.example.jerry.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.jerry.myapplication.R;
import com.example.jerry.myapplication.views.TopBounceView;


public class TopBounceActivity extends Activity implements View.OnTouchListener{

  private float rowX;
  private float rowY;
  private TopBounceView mSlowSwipeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.top_bounce_layout );
        mSlowSwipeView = ( TopBounceView ) findViewById( R.id.myview);
        mSlowSwipeView.goBack();
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

  @Override
  public boolean onTouch( View view, MotionEvent motionEvent ) {

    Log.d("jerry", motionEvent.getAction() + "");
    switch ( motionEvent.getAction() ){
      case MotionEvent.ACTION_DOWN:
        rowX = motionEvent.getRawX();
        rowY = motionEvent.getRawY();
        break;
      case MotionEvent.ACTION_MOVE:
        mSlowSwipeView.update( motionEvent.getX() - rowX, motionEvent.getRawY() - rowY );
        rowX = motionEvent.getRawX();
        rowY = motionEvent.getRawY();

        break;
      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        rowX = 0;
        rowY = 0;
        mSlowSwipeView.goBack();

        break;


    }
    return true;
  }
}