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
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.jerry.myapplication.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CubicDrawView extends View implements OnTouchListener {

    private static final float STROKE_WIDTH = 5f;

    List<Point> points = new ArrayList<Point>();
    List<Point> controlPointsLeft  = new ArrayList<Point>();
    List<Point> controlPointsRight  = new ArrayList<Point>();
    Paint paint = new Paint();

    private ArrayList<Integer> weights = new ArrayList<Integer>(  );
    private int maxHeight = 0;
    private int count = 2;
  private int height = 150;


  public CubicDrawView( Context context, AttributeSet attributeSet ) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(Color.RED);

    int screenWidth = DeviceUtils.getScreenWidth( getContext() );

      Point p1 = new Point();
    p1.x = 0;
    p1.y = 0;

    Point p2 = new Point();
    p2.x = screenWidth / 2 ;
    p2.y = height;

    Point p3 = new Point();
    p3.x = 0;
    p3.y = screenWidth;
    points.add( p1 );
    points.add( p2 );
    points.add( p3 );
    }

  private void init(){
    weights.add( 1 );
    weights.add( 2 );
    for(int i = 2 ; i < count; i ++){
      weights.add( weights.get( i - 2 ) + weights.get( i - 1 ));
    }
    maxHeight = weights.get( count - 1 );

    for(int i = count; i < 2 * count - 1; i ++){
      weights.add( weights.get( 2 * count - 1 - (i + 1) ) );
    }

    int screenWidth = DeviceUtils.getScreenWidth( getContext() );
    int interval = screenWidth / (2 * count - 1);
    for(int i = 0; i < 2 * count - 1; i ++){
      Point point = new Point();
      point.x = i * interval;
      point.y = weights.get( i ) * height / maxHeight;
      points.add( point );
    }
  }


  public void goBack(){
    final float interval = (float)height / 100;
    Log.d( "jerry height", height + "" );
    Log.d( "jerry interval", interval + "" );

    final Timer timer = new Timer(  );
    timer.scheduleAtFixedRate( new TimerTask() {
      @Override
      public void run() {
        if(height <= 0){
          timer.cancel();
        }
        doRefresh( 0, -1 * interval * 3 );
      }
    }, 0, 10);
  }

  public void update(float x, double y){
    doRefresh( x, y );
  }

  private void doRefresh(float x, double y){
    y /= 3;
    height += y;
    postInvalidate();
  }

    public void onDraw(Canvas canvas) {
        Path path = new Path();

        if(points.size() > 1){
            for(int i = 0; i < points.size(); i++){
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
            if(i == points.size() - 1 || i == points.size() - 2){
              path.moveTo( point.x, point.y );
            }
            else{
                Point prev = points.get(i + 1);

                path.cubicTo(prev.x + prev.dx, prev.y + prev.dy, point.x - point.dx, point.y - point.dy, point.x, point.y);
            }
        }

        canvas.drawPath(path, paint);
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

    class Point {
        float x, y;
        float dx, dy;

        @Override
        public String toString() {
            return x + ", " + y;
        }
    }
}

