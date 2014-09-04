/**
 * Copyright 2012 John Ericksen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.jerry.myapplication.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.jerry.myapplication.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

public class CubicFreehandDrawView extends View implements OnTouchListener {

    private static final float STROKE_WIDTH = 5f;

    List<Point> points = new ArrayList<Point>();
    Paint paint = new Paint();
    private int mPointsNum = 500;
    private float offsetHeight = 4.0f;

  Point[] firstControlPoints;
  Point[] secondControlPoints;

  public CubicFreehandDrawView( Context context, AttributeSet attributeSet ) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);

        this.setOnTouchListener(this);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias( true );

      //  initPoints();
    }

    private void initPoints(){
      paint.setStyle(Paint.Style.FILL_AND_STROKE);

        int screenWidth = DeviceUtils.getScreenWidth( getContext() ) - 300;
        float offset = 0;
        for(int i = 0 ; i < mPointsNum; i++){
          float x = screenWidth * i / mPointsNum;
          float y = 200 * i/ mPointsNum + offset;
          offset += offsetHeight * i / mPointsNum;
          Point point = new Point();
          point.x = x;
          point.y = y;
          points.add( point);
        }

        firstControlPoints = new Point[points.size()];
        secondControlPoints = new Point[points.size()];
    }

    public void onDraw(Canvas canvas) {
        Path path = new Path();

        if(points.size() > 1){
            for(int i = points.size() - 2; i < points.size(); i++){
                if(i >= 0){
                    Point point = points.get(i);

                    if(i == 0){
                        Point next = points.get(i + 1);
                        point.dx = ((next.x - point.x) / 3);
                        point.dy = ((next.y - point.y) / 3);
                    }
                    else if(i == points.size() - 1){
                        Point prev = points.get(i - 1);
                        point.dx = ((point.x - prev.x) / 3);
                        point.dy = ((point.y - prev.y) / 3);
                    }
                    else{
                        Point next = points.get(i + 1);
                        Point prev = points.get(i - 1);
                        point.dx = ((next.x - prev.x) / 3);
                        point.dy = ((next.y - prev.y) / 3);
                    }
                }
            }
        }

        boolean first = true;
        for(int i = 0; i < points.size(); i++){
            Point point = points.get(i);
            if(first){
                first = false;
                path.moveTo(point.x, point.y);
            }
            else{
                Point prev = points.get(i - 1);
                path.cubicTo(prev.x + prev.dx, prev.y + prev.dy, point.x - point.dx, point.y - point.dy, point.x, point.y);
            }
        }

        canvas.drawPath(path, paint);
    }

  public void onDraw1(Canvas canvas) {

    Point[] knots = new Point[points.size()];
    for(int i = 0; i < knots.length; i ++){
      knots[i] = points.get( i );
    }
    GetCurveControlPoints( knots);

    Path path = new Path();

    boolean first = true;
    for(int i = 0; i < points.size()-2; i++){
      Point point = points.get(i);
      if(first){
        first = false;
        path.moveTo(point.x, point.y);
      }
      else{

        path.cubicTo(firstControlPoints[i].x, firstControlPoints[i].y, secondControlPoints[i].x, secondControlPoints[i].y,
                point.x, point.y);
      }
    }
    canvas.drawPath( path, paint );
  }

    /// <summary>
  /// Get open-ended Bezier Spline Control Points.
  /// </summary>
  /// <param name="knots">Input Knot Bezier spline points.</param>
  /// <param name="firstControlPoints">Output First Control points
  /// array of knots.Length - 1 length.</param>
  /// <param name="secondControlPoints">Output Second Control points
  /// array of knots.Length - 1 length.</param>
  /// <exception cref="ArgumentNullException"><paramref name="knots"/>
  /// parameter must be not null.</exception>
  /// <exception cref="ArgumentException"><paramref name="knots"/>
  /// array must contain at least two points.</exception>
  public void GetCurveControlPoints(Point[] knots)
  {

    int n = knots.length - 1;
    if (n == 1)
    { // Special case: Bezier curve should be a straight line.
      firstControlPoints = new Point[1];
      // 3P1 = 2P0 + P3
      firstControlPoints[0].x = (2 * knots[0].x + knots[1].x) / 3;
      firstControlPoints[0].y = (2 * knots[0].y + knots[1].y) / 3;

      secondControlPoints = new Point[1];
      // P2 = 2P1 – P0
      secondControlPoints[0].x = 2 *
              firstControlPoints[0].x - knots[0].x;
      secondControlPoints[0].y = 2 *
              firstControlPoints[0].y - knots[0].y;
      return;
    }

    // Calculate first Bezier control points
    // Right hand side vector
    double[] rhs = new double[n];

    // Set right hand side X values
    for (int i = 1; i < n - 1; ++i)
      rhs[i] = 4 * knots[i].x + 2 * knots[i + 1].x;
    rhs[0] = knots[0].x + 2 * knots[1].x;
    rhs[n - 1] = (8 * knots[n - 1].x + knots[n].x) / 2.0;
    // Get first control points X-values
    double[] x = GetFirstControlPoints(rhs);

    // Set right hand side Y values
    for (int i = 1; i < n - 1; ++i)
      rhs[i] = 4 * knots[i].y + 2 * knots[i + 1].y;
    rhs[0] = knots[0].y + 2 * knots[1].y;
    rhs[n - 1] = (8 * knots[n - 1].y + knots[n].y) / 2.0;
    // Get first control points Y-values
    double[] y = GetFirstControlPoints(rhs);

    // Fill output arrays.
    firstControlPoints = new Point[n];
    secondControlPoints = new Point[n];
    for (int i = 0; i < n; ++i)
    {
      // First control point
      firstControlPoints[i] = new Point(x[i], y[i]);
      // Second control point
      if (i < n - 1)
        secondControlPoints[i] = new Point(2 * knots[i + 1].x - x[i + 1], 2 * knots[i + 1].y - y[i + 1]);
      else
        secondControlPoints[i] = new Point((knots
                [n].x + x[n - 1]) / 2,
                (knots[n].y + y[n - 1]) / 2);
    }
  }

  /// <summary>
  /// Solves a tridiagonal system for one of coordinates (x or y)
  /// of first Bezier control points.
  /// </summary>
  /// <param name="rhs">Right hand side vector.</param>
  /// <returns>Solution vector.</returns>
  private static double[] GetFirstControlPoints(double[] rhs)
  {
    int n = rhs.length;
    double[] x = new double[n]; // Solution vector.
    double[] tmp = new double[n]; // Temp workspace.

    double b = 2.0;
    x[0] = rhs[0] / b;
    for (int i = 1; i < n; i++) // Decomposition and forward substitution.
    {
      tmp[i] = 1 / b;
      b = (i < n - 1 ? 4.0 : 3.5) - tmp[i];
      x[i] = (rhs[i] - x[i - 1]) / b;
    }
    for (int i = 1; i < n; i++)
      x[n - i - 1] -= tmp[n - i] * x[n - i]; // Backsubstitution.

    return x;
  }

    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_UP) {
            for (int i = 0; i < event.getHistorySize(); i++) {
                Point point = new Point();
                point.x = event.getHistoricalX(i);
                point.y = event.getHistoricalY(i);
                points.add(point);
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void clear() {
        points.clear();
      invalidate();
    }

    static class Point {
        float x, y;
        float dx, dy;

        @Override
        public String toString() {
            return x + ", " + y;
        }

        public Point(float x, float y){
          this.x = x;
          this.y = y;
        }

        public Point(double x, double y){
          this.x = ( float ) x;
          this.y = ( float ) y;
        }

        public Point(){}
    }
}

