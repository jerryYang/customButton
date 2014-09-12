package com.example.jerry.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jerry on 9/5/14.
 */
public class CustomButton extends Button implements View.OnTouchListener{
  private int mWidth;
  private int mHeight;
  private int mRoundRadius;

  private int RADIUS = 100;

  private float ROUND_CURVE_FACTOR_MAX = 0.6f;
  private float CP_FACTOR_Y_MAX = 0.4f;

  private final float TOP_BOTTOM_CURVE_DEPTH_FACTOR = 0.15f;
  private float TOP_BOTTOM_CURCE_DEPTH;

  private Timer mTimer;

  private ArrayList<Point> mPoints;

  private Paint mPaint;

  public CustomButton( Context context ) {
    super( context );
    init();
  }

  public CustomButton( Context context, AttributeSet attrs ) {
    super( context, attrs );
    init();
  }
    public CustomButton( Context context, AttributeSet attrs, int defStyleAttr ) {
      super( context, attrs, defStyleAttr );
      init();
    }

  public void setSize(int width, int height ){
    this.mWidth = width;
    this.mHeight = height;
    init();
    invalidate();
  }

  private void init(){
    setOnTouchListener( this );

    mPaint = new Paint(  );
    mPaint.setStyle( Paint.Style.FILL );
    mPaint.setColor( Color.GRAY );
    mPaint.setAntiAlias( true );

    mPoints = new ArrayList<Point>();
    mRoundRadius = mHeight / 2;
    setRadius();
    TOP_BOTTOM_CURCE_DEPTH = RADIUS * TOP_BOTTOM_CURVE_DEPTH_FACTOR;

    for(int i = 0; i < 12; i++){
      Point point = new Point(  );
      mPoints.add( point );
    }
    resetPoints();
  }

  private void setRadius(){
    if(mWidth - mHeight > mRoundRadius * 1.5 * 2 ){
      RADIUS = ( int ) (mRoundRadius * 1.5);
    } else {
      RADIUS = (mWidth - mHeight) / 2;
    }
  }

  private void resetPoints(){
    setRadius();
    // top touch point curve
    Point point0 = mPoints.get( 0 );
    point0.setCP( -1, 0, 1, 0 );
    Point point1 = mPoints.get( 1 );
    point1.setCP( -1, 0, 1, 0 );
    Point point2 = mPoints.get( 2 );
    point2.setCP( -1, 0, 1, 0 );

    point0.setXY( mRoundRadius, 0 );
    point1.setXY( mRoundRadius, 0 );
    point2.setXY( mRoundRadius, 0 );

    // right round curve
    Point point3 = mPoints.get( 3 );
    point3.setCP( -1, 0, 1, 0 );
    Point point4 = mPoints.get( 4 );
    point4.setCP( 0, -1, 0, 1 );
    Point point5 = mPoints.get( 5 );
    point5.setCP( 1, 0, -1, 0 );

    point3.setXY( mWidth - mRoundRadius, 0 );
    point4.setXY( mWidth, mRoundRadius );
    point5.setXY( mWidth - mRoundRadius, mHeight );

    // botton touch point curve
    Point point6 = mPoints.get( 6 );
    point6.setCP( 1, 0, -1, 0 );
    Point point7 = mPoints.get( 7 );
    point7.setCP( 1, 0, -1, 0 );
    Point point8 = mPoints.get( 8 );
    point8.setCP( 1, 0, -1, 0 );

    point6.setXY( mRoundRadius, mHeight);
    point7.setXY( mRoundRadius, mHeight );
    point8.setXY( mRoundRadius, mHeight );

    // left round curve
    Point point9 = mPoints.get( 9 );
    point9.setCP( 1, 0, -1, 0 );
    Point point10 = mPoints.get( 10 );
    point10.setCP( 0, 1, 0, -1 );
    Point point11 = mPoints.get( 11 );
    point11.setCP( -1, 0, 1, 0 );

    point9.setXY( mRoundRadius, mHeight);
    point10.setXY( 0, mRoundRadius );
    point11.setXY( mRoundRadius, 0 );
  }

  private class Point{
    public float x;
    public float y;
    public float lCPx;
    public float rCPx;
    public float lCPy;
    public float rCPy;

    public Point(float x, float y, float lCPx, float lCPy, float rCPx, float rCPy){
      this.x = x;
      this.y = y;
      this.lCPx = lCPx;
      this.lCPy = lCPy;
      this.rCPx = rCPx;
      this.rCPy = rCPy;
    }

    public Point(float x, float y){
      this.x = x;
      this.y = y;
      this.lCPx = 0;
      this.lCPy = 0;
      this.rCPx = 0;
      this.rCPy = 0;
    }

    public Point(){

    }

    public void setXY(float x, float y){
      this.x = x;
      this.y = y;
    }

    public void setCP(float lCPx, float lCPy, float rCPx, float rCPy){
      this.lCPx = lCPx;
      this.lCPy = lCPy;
      this.rCPx = rCPx;
      this.rCPy = rCPy;
    }
}

  // offsetY is from 0 -> max -> 0
  private void bounceTopBottomCurve(float offsetY){
    Point point1 = mPoints.get( 1 );
    point1.setXY( point1.x, point1.y - offsetY);

    Point point7 = mPoints.get( 7 );
    point7.setXY( point7.x, point7.y + offsetY);
    postInvalidate();

  }

  private void updateRoundCurve(boolean left, float factorY, float factor){
    if(left){
      // first update round corner vertical factor
      Point point9 = mPoints.get( 9 );
      Point point11 = mPoints.get( 11 );

      point9.setCP( point9.lCPx, point9.lCPy + factorY, point9.rCPx, point9.rCPy - factorY );
      point11.setCP( point11.lCPx, point11.lCPy + factorY, point11.rCPx, point11.rCPy - factorY);

      // factor on all round corners to do shape change on round corner
      for(int i = 0; i < 3; i++){
        Point point = mPoints.get( mPoints.size() - (i + 1) );
        point.setCP( point.lCPx * factor, point.lCPy * factor, point.rCPx * factor, point.rCPy * factor );
      }

      // update top bottom curve points to make the joint points smooth
      Point point1 = mPoints.get( 1 );
      Point point7 = mPoints.get( 7 );
      point1.setCP( point11.lCPx, point11.lCPy, point11.rCPx, point11.rCPy);
      point7.setCP( point9.lCPx, point9.lCPy, point9.rCPx, point9.rCPy );
      Point point0 = mPoints.get( 0 );
      Point point8 = mPoints.get( 8 );
      point0.setCP( 0,0,0,0 );
      point8.setCP( 0,0,0,0 );

    } else {
      // first update round corner vertical factor
      Point point5 = mPoints.get( 5 );
      Point point3 = mPoints.get( 3 );

      point5.setCP( point5.lCPx, point5.lCPy - factorY, point5.rCPx, point5.rCPy + factorY );
      point3.setCP( point3.lCPx, point3.lCPy - factorY, point3.rCPx, point3.rCPy + factorY);

      // factor on all round corners to do shape change on round corner
      for(int i = 3; i < 6; i++){
        Point point = mPoints.get( i );
        point.setCP( point.lCPx * factor, point.lCPy * factor, point.rCPx * factor, point.rCPy * factor );
      }

      // update top bottom curve points to make the joint points smooth
      Point point1 = mPoints.get( 1 );
      Point point7 = mPoints.get( 7 );
      point1.setCP( point3.lCPx, point3.lCPy, point3.rCPx, point3.rCPy);
      point7.setCP( point5.lCPx, point5.lCPy, point5.rCPx, point5.rCPy );
      Point point2 = mPoints.get( 2 );
      Point point6 = mPoints.get( 6 );
      point2.setCP( 0, 0, 0, 0 );
      point6.setCP( 0, 0, 0, 0 );
    }
  }

  private void updateTopBottomCurve(float pressedX){
    Point point0 = mPoints.get( 0 );
    point0.setXY( pressedX - RADIUS, point0.y );
    Point point1 = mPoints.get( 1 );
    point1.setXY( pressedX, point1.y + TOP_BOTTOM_CURCE_DEPTH );
    Point point2 = mPoints.get( 2 );
    point2.setXY( pressedX + RADIUS, point2.y );

    Point point6 = mPoints.get( 6 );
    point6.setXY( pressedX + RADIUS, point6.y );
    Point point7 = mPoints.get( 7 );
    point7.setXY( pressedX, point6.y - TOP_BOTTOM_CURCE_DEPTH );
    Point point8 = mPoints.get( 8 );
    point8.setXY( pressedX - RADIUS, point8.y );
  }

  private void updateTopBottomCurveCP(boolean left, float factor){
    if(left){
      Point point0 = mPoints.get( 0 );
      point0.setCP( point0.lCPx * factor, point0.lCPy * factor, point0.rCPx * factor, point0.rCPy * factor );
      Point point1 = mPoints.get( 1 );
      point1.setCP( point1.lCPx * factor, point1.lCPy * factor, point1.rCPx * factor, point1.rCPy * factor );

      Point point8 = mPoints.get( 8 );
      point8.setCP( point8.lCPx * factor, point8.lCPy * factor, point8.rCPx * factor, point8.rCPy * factor );
      Point point9 = mPoints.get( 7 );
      point9.setCP( point9.lCPx * factor, point9.lCPy * factor, point9.rCPx * factor, point9.rCPy * factor );

    } else {
      Point point2 = mPoints.get( 2 );
      point2.setCP( point2.lCPx * factor, point2.lCPy * factor, point2.rCPx * factor, point2.rCPy * factor );
      Point point1 = mPoints.get( 1 );
      point1.setCP( point1.lCPx * factor, point1.lCPy * factor, point1.rCPx * factor, point1.rCPy * factor );

      Point point6 = mPoints.get( 6 );
      point6.setCP( point6.lCPx * factor, point6.lCPy * factor, point6.rCPx * factor, point6.rCPy * factor );
      Point point7 = mPoints.get( 7 );
      point7.setCP( point7.lCPx * factor, point7.lCPy * factor, point7.rCPx * factor, point7.rCPy * factor );
    }
  }

  private void updateCorner(boolean left, float pressedX){
    // any way, update the top bottom curve points
    if(pressedX < mRoundRadius){
      updateTopBottomCurve( mRoundRadius);
    } else if(pressedX > mWidth - mRoundRadius){
      updateTopBottomCurve( mWidth - mRoundRadius );
    } else {
      updateTopBottomCurve( pressedX );
    }

    float downGradeFactor = 0.4f;

    if(left){
      float offsetY = (1 - (pressedX - mRoundRadius) / RADIUS ) * TOP_BOTTOM_CURCE_DEPTH * 1 / downGradeFactor;
      if(offsetY > RADIUS * TOP_BOTTOM_CURVE_DEPTH_FACTOR){
        offsetY = RADIUS * TOP_BOTTOM_CURVE_DEPTH_FACTOR;
        int roundCornerDownGradeWidth = ( int ) (mRoundRadius + RADIUS - RADIUS * downGradeFactor);
        float factor = 1 - (1 - ROUND_CURVE_FACTOR_MAX) * (roundCornerDownGradeWidth - pressedX) / roundCornerDownGradeWidth;
        if(pressedX <= mRoundRadius){
          float factorY = CP_FACTOR_Y_MAX * (mRoundRadius - pressedX) / mRoundRadius;
          updateRoundCurve( true, factorY, factor );
        } else {
          updateRoundCurve( true, 0, factor );
        }
      }
      Point point0 = mPoints.get(0);
      point0.setXY( mRoundRadius , point0.y + offsetY);

      Point point8 = mPoints.get( 8 );
      point8.setXY( mRoundRadius, point8.y - offsetY );

      Point point11 = mPoints.get( 11 );
      point11.setXY( point0.x, point0.y );

      Point point9 = mPoints.get( 9 );
      point9.setXY( point8.x, point8.y );

    } else {
      float offsetY = (1 - (mWidth - pressedX - mRoundRadius) / RADIUS ) * TOP_BOTTOM_CURCE_DEPTH * 1 / downGradeFactor;
      if(offsetY > TOP_BOTTOM_CURCE_DEPTH){
        offsetY = TOP_BOTTOM_CURCE_DEPTH;
        int roundCornerDownGradeWidth = ( int ) (mRoundRadius + RADIUS - RADIUS * downGradeFactor);
        float factor = 1 - (1 - ROUND_CURVE_FACTOR_MAX) * (roundCornerDownGradeWidth - (mWidth - pressedX)) / roundCornerDownGradeWidth;
        if(pressedX >= mWidth - mRoundRadius){
          float factorY = CP_FACTOR_Y_MAX * (pressedX - (mWidth - mRoundRadius)) / mRoundRadius;
          updateRoundCurve( false, factorY, factor );
        } else {
          updateRoundCurve( false, 0, factor );
        }
      }

      Point point2 = mPoints.get(2);
      point2.setXY( mWidth - mRoundRadius , point2.y + offsetY);

      Point point6 = mPoints.get( 6 );
      point6.setXY( mWidth - mRoundRadius, point6. y - offsetY );

      Point point3 = mPoints.get( 3 );
      point3.setXY( point2.x, point2.y );

      Point point5 = mPoints.get( 5 );
      point5.setXY( point6.x, point6.y );

    }
  }

  private void updateButton(float pressedX){
    if(pressedX >= mRoundRadius + RADIUS && pressedX <= mWidth - (mRoundRadius + RADIUS)){
      // update top bottom curve
      updateTopBottomCurve( pressedX );
    } else if(pressedX > 0 && pressedX < mRoundRadius + RADIUS){
      // update left round curve and top bottom curve
      updateCorner(true, pressedX);

    } else if(pressedX < mWidth && pressedX > mWidth - (mRoundRadius + RADIUS)){
      // update right round curve and top bottom curve
      updateCorner(false, pressedX);
    }

    invalidate();
  }

  private void bounce(){
    if(mTimer != null){
      mTimer.cancel();
    }

    float topBottomBounceOffset = TOP_BOTTOM_CURCE_DEPTH * 2;
    final int period = 10;
    final int segTime = 200;
    final int hitTop = 5;
    final float interval = topBottomBounceOffset / (segTime / period);
    mTimer = new Timer(  );
    mTimer.scheduleAtFixedRate( new TimerTask() {
      int time = 0;
      int plus = 1;
      int hit = 1;
      @Override
      public void run() {
        if(hit == hitTop && time == segTime / 2){
          mTimer.cancel();
        }
        time += period;

        bounceTopBottomCurve( interval * plus);

        if(time == segTime){
          hit ++;
          time = 0;
          plus *= -1;
        }

      }
    }, 0, period );
  }

  @Override
  protected void onDraw( Canvas canvas ) {
    Path path = new Path(  );

    int horizontalControlPointWidth = RADIUS / 2;
    int verticalControlPointHeight = mHeight / 4;

    boolean first = true;
    for(int i = 0; i < mPoints.size(); i++){
      Point point = mPoints.get( i );
      if(first){
        path.moveTo( point.x, point.y );
        first = false;
      } else {
        Point prePoint = mPoints.get( i - 1 );
        path.cubicTo( prePoint.x + prePoint.rCPx * horizontalControlPointWidth, prePoint.y + prePoint.rCPy *  verticalControlPointHeight,
                point.x + point.lCPx * horizontalControlPointWidth, point.y +point.lCPy * verticalControlPointHeight,
                point.x, point.y);
      }
    }

    path.lineTo( mPoints.get( 0 ).x, mPoints.get( 0 ).y );

    canvas.drawPath( path, mPaint );
  }

  @Override
  public boolean onTouch( View view, MotionEvent motionEvent ) {
    int action = motionEvent.getAction();

    switch ( action ){
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_MOVE:
        resetPoints();
        float x = motionEvent.getX();
        updateButton( x );
        break;
      case MotionEvent.ACTION_CANCEL:
      case MotionEvent.ACTION_UP:
        resetPoints();
        invalidate();
        break;

    }
    return true;
  }
}