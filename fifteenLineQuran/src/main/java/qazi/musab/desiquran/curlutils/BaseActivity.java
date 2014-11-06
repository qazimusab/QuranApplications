package qazi.musab.desiquran.curlutils;

import android.app.Activity;

import com.google.analytics.tracking.android.EasyTracker;

/**
 * Created by Musab on 9/14/2014.
 */
public class BaseActivity extends Activity {
    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
    }
}
