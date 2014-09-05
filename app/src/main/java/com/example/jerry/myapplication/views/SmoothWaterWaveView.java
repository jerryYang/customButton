package com.example.jerry.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.jerry.myapplication.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jerry on 9/3/14.
 */
public class SmoothWaterWaveView extends View implements View.OnTouchListener{
  private Spring[] springs;
  private int number = 100;
  private final float k = 0.025f;
  private final float d = 0.04f;
  private final float targetHeight = 0;
  private final int timeInterval = 20;
  private final float spread = 0.2f;

  private Paint mPaint;
  private Timer timer;

  private float currentY;
  private float pressedX;

  private ArrayList<Point> mPoints;
  private boolean isDrag = true;

  private int screenHeight = 0;

  public SmoothWaterWaveView( Context context ) {
    super( context );
    init();
  }

  public SmoothWaterWaveView( Context context, AttributeSet attrs ) {
    super( context, attrs );
    init();
  }

  public SmoothWaterWaveView( Context context, AttributeSet attrs, int defStyleAttr ) {
    super( context, attrs, defStyleAttr );
    init();
  }

  private void init(){
    setOnTouchListener( this );

    screenHeight = DeviceUtils.getScreenHeight( getContext() );

    springs = new Spring[number];

    for(int i =0; i < number; i++){
      int y = screenHeight / (number - 1) * i;
      Spring spring = new Spring(targetHeight,  y);
      springs[i] = spring;
    }

    mPaint = new Paint(  );
    mPaint.setColor( Color.BLACK );
    mPaint.setStyle( Paint.Style.FILL );

    initPoints();
  }

  private void initPoints(){
    mPoints = new ArrayList<Point>( 3 );
    Point po0 = new Point();
    Point po1 = new Point();
    Point po2 = new Point();

    mPoints.add( po0 );
    mPoints.add( po1 );
    mPoints.add( po2 );
  }

  private void updatePoints(int dx){
    mPoints.get( 0 ).set( 0, ( int ) (currentY - screenHeight / 8) );
    mPoints.get( 1 ).set( dx, ( int ) currentY );
    mPoints.get( 2 ).set( 0, ( int ) (currentY + screenHeight / 8) );

    invalidate();
  }

  private void clearPoints(){
    mPoints.clear();
  }

  public static class Spring{
    private float x;
    private float y;
    private float velocity;
    private float targetHeight;
    private float dx;
    private float dy;

    public Spring(float targetHeight, float y){
      this.y = y;
      this.targetHeight = targetHeight;
    }

    public void reset(){
      this.velocity = 0;
      this.x = 0;
    }

    public void update(float k, float d)
    {
      float x = this.x - targetHeight;
      float acceleration = -k * x - velocity * d;

      this.x += velocity;
      velocity += acceleration;

      if(this.x <= 0){
        this.x = 0;
      }
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
          leftDeltas[i] = spread * (springs[i].x - springs [i - 1].x );
          springs[i - 1].velocity += leftDeltas[i];
        }
        if (i < springs.length - 1)
        {
          rightDeltas[i] = spread * (springs[i].x - springs [i + 1].x );
          springs[i + 1].velocity += rightDeltas[i];
        }
      }

      for (int i = 0; i < springs.length; i++)
      {
        if (i > 0)
          springs[i - 1].x += leftDeltas[i];
        if (i < springs.length - 1)
          springs[i + 1].x += rightDeltas[i];
      }
    }
  }

  public void reset(){
    for(int i = 0; i < springs.length; i ++){
      springs[i].reset();
    }
  }

  public void autoWave(int i, float velocity){
    splash( i, velocity );
    doWave();
  }

  public void splash(int i, float velocity){
    reset();
    if(i < springs.length && i >= 0){
      springs[i].velocity = velocity;
    }

    update( spread );
   // invalidate();
  }

  public void doWave(){
    timer = new Timer(  );
    timer.scheduleAtFixedRate( new TimerTask() {
      int framesConsumed = 0;
      @Override
      public void run() {
        framesConsumed ++;
      //  offset = offset * (frames - framesConsumed) / frames;

        update( spread);
        postInvalidate();
      }
    }, 0, timeInterval);
  }

  @Override
  protected void onDraw( Canvas canvas ) {
    super.onDraw( canvas );

    if(isDrag){
      drawDragWave( canvas );
    } else {
      drawWaterWave( canvas );
    }
  }

  private void drawDragWave(Canvas canvas){
    Path path = new Path(  );
    boolean first = true;
    for(int i = 0; i < mPoints.size(); i ++){
      Point point = mPoints.get( i );
      if(first){
        path.moveTo( point.x, point.y );
        first = false;
      } else {
        int radius = screenHeight / 8;
        Point prePoint = mPoints.get( i - 1 );
        path.cubicTo( prePoint.x, prePoint.y + radius / 3, point.x, point.y - radius / 3, point.x, point.y );
      }
    }

    path.lineTo( mPoints.get( 0 ).x, mPoints.get( 0 ).y );
    canvas.drawPath( path, mPaint );
  }

  private void drawWaterWave( Canvas canvas){
    Path path = new Path(  );
    path.moveTo( 0,0 );

    if(springs.length > 1){
      for(int i = 0; i < springs.length; i++){
        if(i >= 0){
          Spring point = springs[i];

          if(i == 0){
            Spring next = springs[i + 1];
            point.dx = ((next.x - point.x) / 3);
            point.dy = ((next.y - point.y) / 3);
          }
          else if(i == springs.length - 1){
            Spring prev = springs[i - 1];
            point.dx = ((point.x - prev.x) / 3);
            point.dy = ((point.y - prev.y) / 3);
          }
          else{
            Spring next = springs[i + 1];
            Spring prev = springs[i - 1];
            point.dx = ((next.x - prev.x) / 3);
            point.dy = ((next.y - prev.y) / 3);
          }
        }
      }
    }

    boolean first = true;
    path.moveTo( 0, 0 );
    for(int i = 0; i < springs.length; i++){
      Spring point = springs[i];
      if(first){
        first = false;
        path.lineTo(point.x, point.y);
      } else {
        Spring prev = springs[i - 1];

        path.cubicTo(prev.x + prev.dx, prev.y + prev.dy, point.x - point.dx, point.y - point.dy, point.x, point.y);
      }
    }

    path.lineTo( 0, springs[springs.length - 1].y);
    path.lineTo( 0, 0 );
    canvas.drawPath( path, mPaint );
  }

  @Override
  public boolean onTouch( View view, MotionEvent motionEvent ) {
    int action = motionEvent.getAction();
    switch ( action ){
      case MotionEvent.ACTION_DOWN:
        if(timer != null){
          timer.cancel();
        }

        isDrag = true;
        reset();
        pressedX = motionEvent.getX();
        currentY = motionEvent.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        currentY = motionEvent.getY();
        float dx = motionEvent.getX() - pressedX;

        updatePoints( ( int ) (dx / 4) );

        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        updatePoints( 0 );
        currentY = motionEvent.getY();
        dx = motionEvent.getX() - pressedX;

        int pointIndex = ( int ) (currentY * number / screenHeight);
        splash( pointIndex, dx );
        isDrag = false;
        doWave();
    }
    return true;
  }
}
