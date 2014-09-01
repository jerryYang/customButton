package com.example.jerry.myapplication;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by jerry on 8/25/14.
 */
public final class DeviceUtils {
  public static int getScreenWidth(Context context){
    WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);

    return size.x;
  }

  public static int getScreenHeight(Context context){
    WindowManager wm = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);

    return size.y;
  }

  public static double getEaseInElastic( int currentTime, int startValue, int endValue, int totalTime ){
    double s=1.70158;
    float p=0;
    int a=endValue;

    if (currentTime==0){
      return startValue;
    }
    if ((currentTime/=totalTime)==1) {
      return startValue+endValue;
    }
    if (p == 0) {
      p= ( int ) (totalTime * 0.3);
    }

    if (a < Math.abs(endValue)) {
      a=endValue;
      s=p/4;
    } else {
      s = p/(2*Math.PI) * Math.asin (endValue/a);
    }
    return -(a*Math.pow(2,10*(currentTime-=1)) * Math.sin( (currentTime*totalTime-s)*(2*Math.PI)/p )) + startValue;
  }
}
