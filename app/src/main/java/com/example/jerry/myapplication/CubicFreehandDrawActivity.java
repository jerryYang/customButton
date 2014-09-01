package com.example.jerry.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class CubicFreehandDrawActivity extends Activity {

  private float rowX;
  private float rowY;
  private CubicFreehandDrawView myViewSingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_my_view_freehand );
        myViewSingle = ( CubicFreehandDrawView ) findViewById( R.id.myview);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.freehand, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.clear) {
            myViewSingle.clear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}