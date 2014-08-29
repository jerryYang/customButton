package com.example.jerry.custombutton.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by jerry on 8/29/14.
 */
public class RoundButton extends Button {

  private int mStroke;
  private int mRadius;
  private int mWidth;
  private int mHeight;
  private boolean mShowShadow;
  private int mStrokeColor;
  private int mFillColor;

  private Paint mFillPaint;
  private Paint mStrokePaint;


  public RoundButton( Context context ) {
    super( context );
    init();
  }

  public RoundButton( Context context, AttributeSet attrs ) {
    super( context, attrs );
    init();
  }

  public RoundButton( Context context, AttributeSet attrs, int defStyleAttr ) {
    super( context, attrs, defStyleAttr );
    init();
  }

  private void init(){
    mFillPaint = new Paint(  );
    mFillPaint.setAntiAlias( true );

    mStrokePaint = new Paint(  );
    mStrokePaint.setAntiAlias( true );
  }

  public void updateWidth(int width){
    mWidth = width;
    invalidate();
  }

  public void updateHeight(int height){
    mHeight = height;
    invalidate();
  }

  public void updateStroke(int stroke){
    mStroke = stroke;
    invalidate();
  }

  public void updateRadius(int radius){
    mRadius = radius;
    invalidate();
  }

  public void showShadow(boolean showShadow){
    mShowShadow = showShadow;
    invalidate();
  }

  public void update(int stroke, int radius, int width, int height, boolean hasShadow, int strokeColor, int fillColor){
    mStroke = stroke;
    mRadius = radius;
    mWidth = width;
    mHeight = height;
    mShowShadow = hasShadow;
    mStrokeColor = strokeColor;
    mFillColor = fillColor;
    invalidate();
  }

  @Override
  protected void onDraw( Canvas canvas ) {
    super.onDraw( canvas );

    // draw filled round rect
    RectF rectF = new RectF(mStroke, mStroke, mWidth + mStroke, mHeight + mStroke);
    mFillPaint.setStyle( Paint.Style.FILL );
    mFillPaint.setColor( mFillColor );
    if( mShowShadow ){
      // shadow offset, blur, and color
      mFillPaint.setShadowLayer( 10, 10 + mStroke, 10 + mStroke, Color.BLACK );
    } else {
      mFillPaint.setShadowLayer( 0, 0,0 , Color.BLACK );
    }

    canvas.drawRoundRect( rectF, mRadius, mRadius, mFillPaint );

    // draw stroke for round rect
    mStrokePaint.setStyle( Paint.Style.STROKE );
    mStrokePaint.setStrokeWidth( mStroke );
    mStrokePaint.setColor( mStrokeColor );

    canvas.drawRoundRect( rectF, mRadius, mRadius, mStrokePaint );
  }
}
