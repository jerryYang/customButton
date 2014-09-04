package com.example.jerry.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.jerry.myapplication.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * TODO: document your custom view class.
 */
public class TopBounceView extends View {

  private ArrayList<Point> points = new ArrayList<Point>(  );
  private ArrayList<Point> controlPoints = new ArrayList<Point>(  );

  private Paint mPaint;
  private Paint mTextPaint;
  private int viewHeight = 150;
  private int arcHeight = 0;
  private int bounceHeight = 250;
  private boolean mIsGoBackCancelled = false;
  private float factor = 1;
  private float factor1 = 0.8f;

  private String mTitle = "top bounce";
  private float textLen = 0;

    public TopBounceView( Context context ) {
        super(context);
        init(null, 0);
    }

    public TopBounceView( Context context, AttributeSet attrs ) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TopBounceView( Context context, AttributeSet attrs, int defStyle ) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
      setBackgroundColor( 0x00000000 );

      mPaint = new Paint();
      mPaint.setAntiAlias( true );
      mPaint.setStyle( Paint.Style.FILL_AND_STROKE );
      mPaint.setStrokeWidth( 2 );
      mPaint.setColor( Color.BLACK );

      mTextPaint = new Paint();
      mTextPaint.setColor( Color.WHITE );
      mTextPaint.setTextSize( 60 );

      textLen = measureTextLength();

      initPoints();
    }

    private void initPoints(){
      int screenWidth = DeviceUtils.getScreenWidth( getContext() );
      Point p1 = new Point( 0,0 );
      Point p2 = new Point(0, viewHeight);
      Point p3 = new Point( screenWidth / 2, viewHeight + arcHeight );
      Point p4 = new Point( screenWidth, viewHeight );
      Point p5 = new Point( screenWidth, 0 );
      Point p6 = new Point( screenWidth / 2, arcHeight);
      points.add( p1 );
      points.add( p2 );
      points.add( p3 );
      points.add( p4 );
      points.add( p5 );
      points.add( p6 );

      Point cp1 = new Point( 0, (p2.y - p1.y) / 3 );
      Point cp2 = new Point( 0, (p2.y - p1.y) * 2 / 3 );
      Point cp3 = new Point( screenWidth / 6, p2.y);
      Point cp4 = new Point( screenWidth * 2 / 6, p3.y);
      Point cp5 = new Point( screenWidth * 4 / 6, p3.y);
      Point cp6 = new Point( screenWidth * 5 / 6, p4.y);
      Point cp7 = new Point( screenWidth, (p4.y - p5.y) * 2 / 3);
      Point cp8 = new Point( screenWidth, (p4.y - p5.y) / 3);
      Point cp9 = new Point( screenWidth * 5 / 6, p5.y );
      Point cp10 = new Point( screenWidth * 4 / 6, p6.y);
      Point cp11 = new Point( screenWidth * 2 / 6, p6.y);
      Point cp12 = new Point( screenWidth / 6, p1.y);
      controlPoints.add( cp1 );
      controlPoints.add( cp2 );
      controlPoints.add( cp3 );
      controlPoints.add( cp4 );
      controlPoints.add( cp5 );
      controlPoints.add( cp6 );
      controlPoints.add( cp7 );
      controlPoints.add( cp8 );
      controlPoints.add( cp9 );
      controlPoints.add( cp10 );
      controlPoints.add( cp11 );
      controlPoints.add( cp12 );
    }

    private void updatePoints(int arcHeight){
      int screenWidth = DeviceUtils.getScreenWidth( getContext() );
      Point p3 = new Point( screenWidth / 2, viewHeight + arcHeight );
      points.set( 2, p3 );

      Point cp4 = new Point( screenWidth * 2 / 6, p3.y);
      Point cp5 = new Point( screenWidth * 4 / 6, p3.y);
      controlPoints.set( 3, cp4 );
      controlPoints.set( 4, cp5 );

    }

    public void cancelPotentialGoBack(){
      mIsGoBackCancelled = true;
    }

    public void enableGoBack(){
      mIsGoBackCancelled = false;
    }

  public void goBack(){
      enableGoBack();
      final int period = 10;
      final int segTime = 100;

      final Timer timer = new Timer(  );
      timer.scheduleAtFixedRate( new TimerTask() {
        int time = 0;
        int interval = bounceHeight / (segTime / period);
        int plus = -1;
        float height = 0;
        @Override
        public void run() {
          if(bounceHeight == 0){
            timer.cancel();
          }
          time += period;
          height = height + plus * interval;

          Log.d("jerry Height", height + "");

          doRefresh( 0, height );

          if(time == segTime){
            time = 0;
            if(height != 0){
              plus *= -1;
            } else {
              bounceHeight = ( int ) (bounceHeight * 0.8);
              interval = bounceHeight / (segTime / period);
            }
          }
        }
      }, 0, period);
    }

    public void update(float x, double y){
      cancelPotentialGoBack();
      doRefresh( x, y );
    }

    private void doRefresh(float x, double y){
      y /= 3;
      updatePoints( ( int ) (arcHeight + y) );
      postInvalidate();
    }

  private Path drawCurve(ArrayList<Point> points, ArrayList<Point> controlPoints) {
    Path path = new Path(  );
    boolean isFirst = true;
    for(int i = 0; i < points.size(); i++){
      Point point = points.get( i );
      if(isFirst){
        path.moveTo( point.x, point.y);
        isFirst = false;
      } else {
        Point controlPoint1 = controlPoints.get( 2 * (i - 1) );
        Point controlPoint2 = controlPoints.get( 2 * (i - 1) + 1);
        path.cubicTo( controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, point.x, point.y );
      }
    }

    // cubic back to the first point
    Point controlPoint1 = controlPoints.get( controlPoints.size() - 2);
    Point controlPoint2 = controlPoints.get( controlPoints.size() - 1);
    path.cubicTo( controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, points.get( 0 ).x, points.get( 0 ).y );
    return path;
  }

    @Override
    protected void onDraw(Canvas canvas) {
      super.onDraw( canvas );
      Path path = drawCurve( points, controlPoints );
      canvas.drawPath( path, mPaint );

      int screenWidth = DeviceUtils.getScreenWidth( getContext() );
      canvas.drawText( mTitle, (screenWidth - textLen) / 2 , viewHeight / 2, mTextPaint);
    }

  private float measureTextLength(){
    float length = mTextPaint.measureText( mTitle );

    return length;
  }
}
