package com.example.jerry.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.jerry.myapplication.utils.DeviceUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jerry on 9/3/14.
 */
public class WaterWaveView extends View implements View.OnTouchListener{
  private Spring[] springs;
  private int number = 30;
  private final float k = 0.025f;
  private final float d = 0.0f;
  private final float targetHeight = 10;

  private Paint mPaint;

  public WaterWaveView( Context context ) {
    super( context );
    init();
  }

  public WaterWaveView( Context context, AttributeSet attrs ) {
    super( context, attrs );
    init();
  }

  public WaterWaveView( Context context, AttributeSet attrs, int defStyleAttr ) {
    super( context, attrs, defStyleAttr );
    init();
  }

  private void init(){
    int screenWidth = DeviceUtils.getScreenWidth( getContext() );

    springs = new Spring[number];

    for(int i =0; i < number; i++){
      int x = screenWidth / (number - 1) * i;
      Spring spring = new Spring(x,  targetHeight);
      springs[i] = spring;
    }

    mPaint = new Paint(  );
    mPaint.setColor( Color.BLACK );
    mPaint.setStyle( Paint.Style.FILL );
  }

  public static class Spring{
    private float x;
    private float height;
    private float velocity;
    private float targetHeight;

    public Spring(float x, float targetHeight){
      this.x = x;
      this.targetHeight = targetHeight;
    }

    public void update(float k, float d)
    {
      float x = height - targetHeight;
      float acceleration = -k * x - velocity * d;

      height += velocity;
      velocity += acceleration;
    }
  }

  public void update(float spread){
    for (int i = 0; i < springs.length; i++)
      springs[i].update( k, d );

    float[] leftDeltas = new float[springs.length];
    float[] rightDeltas = new float[springs.length];

    // do some passes where springs pull on their neighbours
    for (int j = 0; j < 8; j++)
    {
      for (int i = 0; i < springs.length; i++)
      {
        if (i > 0)
        {
          leftDeltas[i] = spread * (springs[i].height - springs [i - 1].height );
          springs[i - 1].velocity += leftDeltas[i];
        }
        if (i < springs.length - 1)
        {
          rightDeltas[i] = spread * (springs[i].height - springs [i + 1].height );
          springs[i + 1].velocity += rightDeltas[i];
        }
      }

      for (int i = 0; i < springs.length; i++)
      {
        if (i > 0)
          springs[i - 1].height += leftDeltas[i];
        if (i < springs.length - 1)
          springs[i + 1].height += rightDeltas[i];
      }
    }
  }

  public void splash(int i, float velocity){
    if(i < springs.length && i >= 0){
      springs[i].velocity = velocity;
    }

    //invalidate();

    Timer timer = new Timer(  );
    timer.scheduleAtFixedRate( new TimerTask() {
      @Override
      public void run() {
        update( 0.2f );
        postInvalidate();
      }
    }, 0, 50);
  }

  @Override
  protected void onDraw( Canvas canvas ) {
    super.onDraw( canvas );

    for(int i = 0; i < springs.length; i ++){
      Spring spring = springs[i];
      Rect rect = new Rect(  );
      rect.left = ( int ) spring.x;
      rect.right = ( int ) (spring.x + 10);
      rect.top = ( int ) spring.height;
      rect.bottom = ( int ) (spring.height + 10);
      canvas.drawRect( rect, mPaint );
    }
  }

  @Override
  public boolean onTouch( View view, MotionEvent motionEvent ) {
    int action = motionEvent.getAction();

    switch ( action ){
      case MotionEvent.ACTION_DOWN:
        splash( 6, 20f );
    }
    return true;
  }
}
