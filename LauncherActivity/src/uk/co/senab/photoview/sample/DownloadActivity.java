package uk.co.senab.photoview.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.qaziconsultancy.thirteenlinequran.R;

public class DownloadActivity extends Activity {

	/*TextView d;
	Handler mHandler;
	int i;
	Button read;
	NotificationManager mNotificationManager1;
	NotificationManager mNotificationManager;*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		final AlertDialog.Builder alertbox5 = new AlertDialog.Builder(DownloadActivity.this);
		alertbox5.setTitle("Download");
		alertbox5.setCancelable(false);
	    alertbox5.setIcon(android.R.drawable.stat_notify_error);
		
              alertbox5.setMessage("To start reciting the Quran, you will need to download some files from the internet. Please make sure you have 190mb of free space before you start. Would you like to start the download now?");
              alertbox5.setNeutralButton("OK", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface arg0, int arg1) {
            	  Intent intent =new Intent(DownloadActivity.this,DownloadService.class);
      			
      			startService(intent);
				Toast.makeText(DownloadActivity.this, "Please continue your work. You will be notified when your download is complete.", Toast.LENGTH_LONG).show();
				finish();
                }
            });
              alertbox5.setNegativeButton("Later", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {finish();} });
		alertbox5.show();
		/*d=(TextView)findViewById(R.id.hello);
		read=(Button)findViewById(R.id.read);
		mHandler = new Handler();
		d.setText("");
		File folder = new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/16linequran");
		  folder.mkdirs();
		new DownloadTask().execute();
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            // TODO Auto-generated method stub
	            while (true) {
	                try {
	                	
	                	for( i=90;i>0;i--){
	                    Thread.sleep(2000);
	                    mHandler.post(new Runnable() {

	                        @Override
	                        public void run() {
	                            // TODO Auto-generated method stub
	                            // Write your code here to update the UI.
	                        	d.setText("Estimated waiting time: "+(i/60)+" minutes and "+(i%60)+" seconds");
              					
	                        }
	                    });}
	                } catch (Exception e) {
	                    // TODO: handle exception
	                }
	            }
	        }
	    }).start();
		
		read.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(DownloadActivity.this,CurlActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(in);
			}
			
		});
		*/
		//startService(new Intent(DownloadActivity.this,DownloadService.class));
		//startService(new Intent(DownloadActivity.this,DownloadService.class));
		//Toast.makeText(DownloadActivity.this, "Please continue your work. We will notify you when your download is complete.", Toast.LENGTH_LONG).show();
		//finish();
	}

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.download, menu);
		return true;
	}
	
	
/*	public static void downloadFile(String from, String to) throws Exception {
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
	}*/
	
	
	 /*private class DownloadTask extends AsyncTask<Void, Double, Void> {

			// Executed on main UI thread
			@SuppressWarnings("deprecation")
			@Override
			protected void onPreExecute() {
			super.onPreExecute();
			read.setVisibility(4);
			String ns = Context.NOTIFICATION_SERVICE;
			mNotificationManager1 = (NotificationManager) getSystemService(ns);

			int icon = R.drawable.icon;        
			CharSequence tickerText = "Download Started"; 
			long when = System.currentTimeMillis();         
			Context context = getApplicationContext();     
			CharSequence contentTitle = "Download in progress";  
			CharSequence contentText = "Downloading...";      
			Intent notificationIntent = new Intent(DownloadActivity.this, DownloadActivity.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(DownloadActivity.this, 0, notificationIntent, 0);
			Notification notification = new Notification(icon, tickerText, when);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			final int HELLO_ID = 1;
			mNotificationManager1.notify(HELLO_ID, notification);
			//Toast.makeText(DownloadActivity.this, "Please continue your work. We will notify you when your download is complete.", Toast.LENGTH_LONG).show();
			//finish();
			
			}
			

			@Override
			protected Void doInBackground(Void... params) {
				
					//for(int i=1;i<560;i++){
					try {
						downloadFile("http://www.qaziconsultancy.com/Quran/16linequran.zip",Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran.zip");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//if(i%20==0)
					//this.publishProgress(i/5.59);
					UnzipUtility u=new UnzipUtility();
					try {
						u.unzip(Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran.zip", Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran/");
						File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran.zip");
						boolean deleted = file.delete();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
			read.setVisibility(0);
			String ns = Context.NOTIFICATION_SERVICE;
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

			int icon = R.drawable.icon;        
			CharSequence tickerText = "Download Complete!"; 
			long when = System.currentTimeMillis();         
			Context context = getApplicationContext();     
			CharSequence contentTitle = "Download Complete!";  
			CharSequence contentText = "Click here to start reading Quran.";      
			Intent notificationIntent = new Intent(DownloadActivity.this, CurlActivity.class);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent contentIntent = PendingIntent.getActivity(DownloadActivity.this, 0, notificationIntent, 0);
			Notification notification = new Notification(icon, tickerText, when);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

			// and this
			mNotificationManager1.cancel(1);
			final int HELLO_ID = 2;
			mNotificationManager.notify(HELLO_ID, notification);
			
			}



			

			
			}
	@Override
	public void onBackPressed()
	{
		/*new DownloadTask().cancel(true);
		File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran.zip");
		boolean deleted = file.delete();
		File file2 = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran");
		boolean deleted2 = file2.delete();*/
		//finish();
	//}
}
