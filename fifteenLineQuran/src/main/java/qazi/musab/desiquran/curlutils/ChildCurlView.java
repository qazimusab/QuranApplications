package qazi.musab.desiquran.curlutils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Musab on 9/8/2014.
 */
public class ChildCurlView extends CurlView {
    private static long mDeBounce = 0;
    public ChildCurlView(Context ctx) {
        super(ctx);
    }

    public ChildCurlView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }

    public ChildCurlView(Context ctx, AttributeSet attrs, int defStyle) {
        super(ctx, attrs, defStyle);
    }



    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if ( Math.abs(mDeBounce - motionEvent.getEventTime()) < 250) {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP)//user released the screen
            {
                if(actionBar.isShowing())
                    actionBar.hide();
                else
                    actionBar.show();
                Log.e("place","1");
            }
            Log.e("place","2");
            return true;
        }

        int intCurrentY = Math.round(motionEvent.getY());
        int intCurrentX = Math.round(motionEvent.getX());
        int intStartY = motionEvent.getHistorySize() > 0 ? Math.round(motionEvent.getHistoricalY(0)) : intCurrentY;
        int intStartX = motionEvent.getHistorySize() > 0 ? Math.round(motionEvent.getHistoricalX(0)) : intCurrentX;

        if ( (motionEvent.getAction() == MotionEvent.ACTION_UP) && (Math.abs(intCurrentX - intStartX) <3) && (Math.abs(intCurrentY - intStartY) <3) ) {
            if ( mDeBounce > motionEvent.getDownTime() ) {
                Log.e("place","3");
                return true;

            }

            //Handle the click


            mDeBounce = motionEvent.getEventTime();
            Log.e("place","4");
            return super.onTouch(view, motionEvent);
        }
        Log.e("place","5");
        return super.onTouch(view, motionEvent);
    }

}

