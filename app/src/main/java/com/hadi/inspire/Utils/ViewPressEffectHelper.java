package com.hadi.inspire.Utils;

import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class ViewPressEffectHelper {

    public static void attach(View view){
        view.setOnTouchListener(new ASetOnTouchListener(view));
    }

    private static class ASetOnTouchListener implements View.OnTouchListener{

        final float ZERO_ALPHA = 1.0f;
        final float HALF_ALPHA = 0.5f;
        final int FIXED_DURATION = 100;
        float alphaOrginally = 1.0f;

        public ASetOnTouchListener(View v){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                alphaOrginally = v.getAlpha();
            }
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                        final AlphaAnimation animation = new AlphaAnimation(ZERO_ALPHA, HALF_ALPHA);
                        animation.setDuration(FIXED_DURATION);
                        animation.setFillAfter(true);
                        v.startAnimation(animation);
                    } else{
                        v.animate().setDuration(FIXED_DURATION).alpha(HALF_ALPHA);
                    }
                }
                break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:{
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                        final AlphaAnimation animation = new AlphaAnimation(HALF_ALPHA, ZERO_ALPHA);
                        animation.setDuration(FIXED_DURATION);
                        animation.setFillAfter(true);
                        v.startAnimation(animation);
                    } else {
                        v.animate().setDuration(100).alpha(alphaOrginally);
                    }
                }
                break;
            }
            return false;
        }

    }
}
