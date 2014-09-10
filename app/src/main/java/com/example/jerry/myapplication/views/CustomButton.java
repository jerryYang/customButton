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

/**
 * Created by jerry on 9/5/14.
 */
public class CustomButton extends Button implements View.OnTouchListener{
  private int mWidth;
  private int mHeight;
  private int mRoundRadius;
  private int mTouchX;

  private int RADIUS = 80;

  private final float ROUND_CURVE_FACTOR_MAX = 0.6f;
  private final float TOP_BOTTOM_CURVE_DEPTH_FACTOR = 1 / 6;

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
    mPaint.setColor( Color.BLACK );

    mPoints = new ArrayList<Point>();
    mRoundRadius = mHeight / 2;
    RADIUS = ( int ) (mRoundRadius * 1.5);

    for(int i = 0; i < 12; i++){
      Point point = new Point(  );
      mPoints.add( point );
    }
    resetPoints();
  }

  private void resetPoints(){
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

    point9.setXY( mRoundRadius, mHeight );
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

  private void updateRoundCurve(boolean left, float factor){
    if(left){
      for(int i = 0; i < 3; i++){
        Point point = mPoints.get( mPoints.size() - (i + 1) );
        point.setCP( point.lCPx * factor, point.lCPy * factor, point.rCPx * factor, point.rCPy * factor );
      }
    } else {
      for(int i = 3; i < 6; i++){
        Point point = mPoints.get( i );
        point.setCP( point.lCPx * factor, point.lCPy * factor, point.rCPx * factor, point.rCPy * factor );
      }
    }
  }

  private void updateTopBottomCurve(float pressedX){
    Point point0 = mPoints.get( 0 );
    point0.setXY( pressedX - RADIUS, point0.y );
    Point point1 = mPoints.get( 1 );
    point1.setXY( pressedX, point1.y + RADIUS * TOP_BOTTOM_CURVE_DEPTH_FACTOR );
    Point point2 = mPoints.get( 2 );
    point2.setXY( pressedX + RADIUS, point2.y );

    Point point6 = mPoints.get( 6 );
    point6.setXY( pressedX + RADIUS, point6.y );
    Point point7 = mPoints.get( 7 );
    point7.setXY( pressedX, point6.y - RADIUS * TOP_BOTTOM_CURVE_DEPTH_FACTOR );
    Point point8 = mPoints.get( 8 );
    point8.setXY( pressedX - RADIUS, point6.y );
  }

  private void updateButton(float pressedX){
    if(pressedX <= mRoundRadius){
      // update round curve in the left
      float factor = 1 - ( float ) ((1 - ROUND_CURVE_FACTOR_MAX) * (mRoundRadius - pressedX) / mRoundRadius);
      updateRoundCurve( true, factor );
    } else if(pressedX >= mWidth - mRoundRadius ){
      // update round curve in the right
      float factor  = ( float ) (1 - ((1 - ROUND_CURVE_FACTOR_MAX)  * (mRoundRadius + pressedX - mWidth )/ mRoundRadius));
      updateRoundCurve( false, factor );
    } else if(pressedX >= mRoundRadius + RADIUS && pressedX <= mWidth - (mRoundRadius + RADIUS)){
      // update top bottom curve
      updateTopBottomCurve( pressedX );
    } else if(pressedX > mRoundRadius && pressedX < mRoundRadius + RADIUS){
      // update left round curve and top bottom curve

    } else if(pressedX < mWidth - mRoundRadius && pressedX > mWidth - (mRoundRadius + RADIUS)){
      // update right round curve and top bottom curve

    }

    invalidate();
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

        break;

    }
    return true;
  }
}
