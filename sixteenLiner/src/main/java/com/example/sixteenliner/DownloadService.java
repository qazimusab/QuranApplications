package com.example.sixteenliner;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import qazi.musab.sixteenliner.R;

public class DownloadService extends Service {
	NotificationManager mNotificationManager1;
	NotificationManager mNotificationManager;
	NotificationManager mNotificationManager2;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
  

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Override
    public void onCreate() {
    	new DownloadTask().execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
    	
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
      
    }
    
    
    private class DownloadTask extends AsyncTask<Void, Double, Void> {

		// Executed on main UI thread
		@Override
		protected void onPreExecute() {
		super.onPreExecute();
		
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager1 = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.iconoo;        
		CharSequence tickerText = "Download Started"; 
		long when = System.currentTimeMillis();         
		Context context = getApplicationContext();     
		CharSequence contentTitle = "Download in progress";  
		CharSequence contentText = "Downloading, please be patient...";      
		Intent notificationIntent = new Intent(DownloadService.this, DownloadActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(DownloadService.this, 0, notificationIntent, 0);
		Notification notification = new Notification(icon, tickerText, when);
		
		notification.setLatestEventInfo(context, contentTitle, contentText, null);
		notification.defaults |= Notification.DEFAULT_SOUND; 
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		final int HELLO_ID = 1;
		mNotificationManager1.notify(HELLO_ID, notification);
		//Toast.makeText(DownloadActivity.this, "Please continue your work. We will notify you when your download is complete.", Toast.LENGTH_LONG).show();
		//finish();
		
		}
		

		@Override
		protected Void doInBackground(Void... params) {
			
				//for(int i=1;i<560;i++){
				try {
					downloadFile("http://www.samawar.com/Quran/16linequran.zip",Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran.zip");
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//if(i%20==0)
				//this.publishProgress(i/5.59);
				
			//Decompress d=new Decompress(Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran.zip",Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran/");
			//d.unzip();
			return null;
		}


		// Executed on main UI thread.
		@Override
		protected void onProgressUpdate(Double... values) {
		super.onProgressUpdate(values);
		//d.setText("Downloaded "+values[0].intValue()+"%");
		}

		// Executed on main UI thread
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		new ExtractTask().execute();
		
		}
		
    }
    
    private class ExtractTask extends AsyncTask<Void, Double, Void> {

		// Executed on main UI thread
		@Override
		protected void onPreExecute() {
		super.onPreExecute();
		
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager2 = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.iconoo;        
		CharSequence tickerText = "Extraction Started"; 
		long when = System.currentTimeMillis();         
		Context context = getApplicationContext();     
		CharSequence contentTitle = "Extraction in progress";  
		CharSequence contentText = "Extracting, please be patient...";      
		Intent notificationIntent = new Intent(DownloadService.this, CurlActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(DownloadService.this, 0, notificationIntent, 0);
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, null);
		notification.defaults |= Notification.DEFAULT_SOUND; 
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		// and this
		mNotificationManager1.cancel(1);
		final int HELLO_ID = 2;
		mNotificationManager2.notify(HELLO_ID, notification);
		}
		

		@Override
		protected Void doInBackground(Void... params) {
			UnzipUtility u=new UnzipUtility();
			try {
				u.unzip(Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran.zip", Environment.getExternalStorageDirectory().getPath() +"/Pictures/.16linequran/");
				File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran.zip");
				boolean deleted = file.delete();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}


		// Executed on main UI thread.
		@Override
		protected void onProgressUpdate(Double... values) {
		super.onProgressUpdate(values);
		//d.setText("Downloaded "+values[0].intValue()+"%");
		}

		// Executed on main UI thread
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		String ns = Context.NOTIFICATION_SERVICE;
		mNotificationManager = (NotificationManager) getSystemService(ns);

		int icon = R.drawable.iconoo;        
		CharSequence tickerText = "Download Complete!"; 
		long when = System.currentTimeMillis();         
		Context context = getApplicationContext();     
		CharSequence contentTitle = "Download Complete!";  
		CharSequence contentText = "Click here to start reading Quran.";      
		Intent notificationIntent = new Intent(DownloadService.this, CurlActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(DownloadService.this, 0, notificationIntent, 0);
		Notification notification = new Notification(icon, tickerText, when);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		notification.defaults |= Notification.DEFAULT_SOUND; 
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		
		// and this
		mNotificationManager2.cancel(1);
		final int HELLO_ID = 2;
		mNotificationManager.notify(HELLO_ID, notification);
		DownloadService.this.stopSelf();
		}
		
    }
    
    
    public static void downloadFile(String from, String to) throws Exception {
	    HttpURLConnection conn = (HttpURLConnection)new URL(from).openConnection();
	    conn.setDoInput(true);
	    conn.setConnectTimeout(10000); // timeout 10 secs
	    conn.connect();
	    InputStream input = conn.getInputStream();
	    FileOutputStream fOut = new FileOutputStream(to);
	    int byteCount = 0;
	    byte[] buffer = new byte[4096];
	    int bytesRead = -1;
	    while ((bytesRead = input.read(buffer)) != -1) {
	        fOut.write(buffer, 0, bytesRead);
	        byteCount += bytesRead;
	    }
	    fOut.flush();
	    fOut.close();
	}
    public void stop()
    {
    	DownloadService.this.stopSelf();
    }
}