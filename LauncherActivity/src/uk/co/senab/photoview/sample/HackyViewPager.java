package uk.co.senab.photoview.sample;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Hacky fix for Issue #4 and
 * http://code.google.com/p/android/issues/detail?id=18990
 * <p/>
 * ScaleGestureDetector seems to mess up the touch events, which means that
 * ViewGroups which make use of onInterceptTouchEvent throw a lot of
 * IllegalArgumentException: pointerIndex out of range.
 * <p/>
 * There's not much I can do in my code for now, but we can mask the result by
 * just catching the problem and ignoring it.
 *
 * @author Chris Banes
 */
public class HackyViewPager extends ViewPager {

    public ActionBar actionBar;
    private static long mDeBounce = 0;

    public HackyViewPager(Context context) {
        super(context);
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setActionBar(ActionBar actionBar){
        this.actionBar=actionBar;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if ( Math.abs(mDeBounce - motionEvent.getEventTime()) < 2500) {
            //Ignore if it's been less then 250ms since
            //the item was last clicked
            return super.onInterceptTouchEvent(motionEvent);
        }

        int intCurrentY = Math.round(motionEvent.getY());
        int intCurrentX = Math.round(motionEvent.getX());
        int intStartY = motionEvent.getHistorySize() > 0 ? Math.round(motionEvent.getHistoricalY(0)) : intCurrentY;
        int intStartX = motionEvent.getHistorySize() > 0 ? Math.round(motionEvent.getHistoricalX(0)) : intCurrentX;

        if ( (motionEvent.getAction() == MotionEvent.ACTION_UP) && (Math.abs(intCurrentX - intStartX) < 3) && (Math.abs(intCurrentY - intStartY) < 3) ) {
            if ( mDeBounce > motionEvent.getDownTime() ) {
                //Still got occasional duplicates without this
                return true;
            }

            //Handle the click
            if(actionBar.isShowing())
                actionBar.hide();
            else
                actionBar.show();

            mDeBounce = motionEvent.getEventTime();
            return true;
        }

        try {
            return super.onInterceptTouchEvent(motionEvent);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        return super.onTouchEvent(motionEvent);
    }
}
