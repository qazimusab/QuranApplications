package com.qaziconsulltancy.quranlibrary.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Musab on 8/20/2014.
 */
public class Checker {
    public static boolean internetIsConnected(final Activity activity) {

        boolean  connected=false;

        // Get connect mangaer
        final ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        // check for wifi
        final android.net.NetworkInfo wifi =  connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // check for mobile data
        final android.net.NetworkInfo mobile =  connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if( wifi.isAvailable() ){

            connected = true;

        }  else if( mobile.isAvailable() ){

            connected = true;

        }  else  {

            connected = false;
        }

        return connected;

    }
}
