package com.example.jerry.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.jerry.myapplication.views.CubicFreehandDrawView.Point;
import com.example.jerry.myapplication.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jerry on 9/2/14.
 */
public class SiblingSwipeThroughView extends View implements View.OnTouchListener {

  private final float xThresholder = 30.0f;
  private final float factor = 0.25f;

  private float xHeight;
  private float waveOffset;

  private int screenHeight;
  private ArrayList<Point> points;
  private float touchPointY;
  private float originalHeight;

  private int interval = 10;

  private Timer mTimer;

  private Paint paint;

  private float x;
  private float y;

  public SiblingSwipeThroughView( Context context ) {
    super( context );
    init();
  }

  public SiblingSwipeThroughView( Context context, AttributeSet attrs ) {
    super( context, attrs );
    init();
  }

  public SiblingSwipeThroughView( Context context, AttributeSet attrs, int defStyleAttr ) {
    super( context, attrs, defStyleAttr );
    init();
  }

  private void init(){
    paint = new Paint( Paint.ANTI_ALIAS_FLAG );
    paint.setColor( Color.BLACK );
    paint.setStyle( Paint.Style.FILL_AND_STROKE );

    screenHeight = DeviceUtils.getScreenHeight( getContext() );
    points = new ArrayList<CubicFreehandDrawView.Point>();
    setOnTouchListener( this );

    initPoints();
  }

  private void initPoints(){
    // always have four points
    Point pointLeftTop = new Point(  );
    pointLeftTop.x = 0;
    pointLeftTop.y = 0;
    points.add( pointLeftTop );

    Point pointRightTop = new Point(  );
    pointRightTop.x = waveOffset;
    pointRightTop.y = 0;
    points.add( pointRightTop );

    Point pointRightBottom = new Point(  );
    pointRightBottom.x = waveOffset;
    pointRightBottom.y = screenHeight;
    points.add( pointRightBottom );

    Point pointLeftBottom = new Point(  );
    pointLeftBottom.x = 0;
    pointLeftBottom.y = screenHeight;
    points.add( pointLeftBottom );

  }

  private void insertPoints(int startIndex, Point[] ps){
    for(int i = 0; i < ps.length; i ++){
      points.add( startIndex + i, ps[i]);
    }
  }

  private void update(float dx){
    xHeight += dx;
    if(xHeight > xThresholder){
      waveOffset = (xHeight - xThresholder) * factor;
    }
    points.clear();
    initPoints();

    // insert new points
    int radius = screenHeight / 6;
    Point[] ps = new Point[3];
    ps[0] = new Point( waveOffset, touchPointY - radius );
    ps[1] = new Point( xHeight, touchPointY );
    ps[2] = new Point( waveOffset, touchPointY + radius);
    insertPoints( 2, ps );

    invalidate();
  }

  private void update1(){
    points.clear();
    initPoints();

    // insert new points
    int radius = screenHeight / 6;
    Point[] ps = new Point[3];
    ps[0] = new Point( waveOffset, touchPointY - radius );
    ps[1] = new Point( xHeight, touchPointY );
    ps[2] = new Point( waveOffset, touchPointY + radius);
    insertPoints( 2, ps );

    postInvalidate();
  }

  private void goBack(){
    // time for xHeight to 0
    final int xHeightTime = 300;
    final int totalTime = 1500;
    final float xHeightInterval = (xHeight - waveOffset) * 10 / xHeightTime;
    final float waveOffsetInterval = waveOffset * 10 / totalTime;
    mTimer = new Timer( );
    mTimer.scheduleAtFixedRate( new TimerTask() {
      boolean isGoBack = true;
      @Override
      public void run() {
        if(waveOffset <= 0){
          mTimer.cancel();
        }
        // always need to downgrade wave offset
        waveOffset -= waveOffsetInterval;

        if(xHeight > waveOffset && isGoBack){
          xHeight -= xHeightInterval;
        } else {
          // this is to settle down main wave
          if(xHeight <= waveOffset){
            if(xHeight <= 0){
              isGoBack = false;
            }

            if(isGoBack){
              xHeight -= 2;
            } else if(xHeight < waveOffset){
              xHeight += 2;
            } else {
              xHeight = waveOffset;
            }
          } else if(!isGoBack){
            xHeight = waveOffset;
          }

          // this is to animate the waves to pass down

        }

        update1();

      }
    }, 0, interval);
  }

  @Override
  protected void onDraw( Canvas canvas ) {
    super.onDraw( canvas );

    Path path = new Path(  );
    boolean first = true;
    for(int i = 0; i < points.size(); i++){
      Point point = points.get( i );
      if(first){
        path.moveTo( point.x, point.y );
        first = false;
        continue;
      } else {
        Point prePoint = points.get( i - 1 );
        if(point.x == prePoint.x || point.y == prePoint.y){
          // just move
          path.lineTo( point.x, point.y );
        } else {
          int radius = screenHeight / 6;
          path.cubicTo( prePoint.x, prePoint.y + radius / 3, point.x, point.y - radius / 3, point.x, point.y );
        }
      }
    }

    path.lineTo( points.get( 0 ).x, points.get( 0 ).y );

    canvas.drawPath( path, paint );
  }

  @Override
  public boolean onTouch( View view, MotionEvent motionEvent ) {
    int action = motionEvent.getAction();
    switch ( action ){
      case MotionEvent.ACTION_DOWN:
        if(mTimer != null){
          mTimer.cancel();
        }

        x = motionEvent.getX();
        y = motionEvent.getY();
        touchPointY = y;
        break;

      case MotionEvent.ACTION_MOVE:
        float currentX = motionEvent.getX();
        float currentY = motionEvent.getY();
        float dx = currentX - x;
        float dy = currentY - y;

        x = currentX;
        y = currentY;
        touchPointY = y;

        // currently, only need dx
        update( dx / 2);
        break;

      case MotionEvent.ACTION_UP:
        goBack();
        originalHeight = xHeight;
        break;

      case MotionEvent.ACTION_CANCEL:

        break;
    }
    return true;
  }
}
