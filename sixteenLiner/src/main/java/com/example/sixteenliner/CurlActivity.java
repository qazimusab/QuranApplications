package com.example.sixteenliner;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.WindowManager.BadTokenException;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.analytics.tracking.android.EasyTracker;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import qazi.musab.sixteenliner.R;

/**
 * Simple Activity for curl testing.
 * 
 * @author harism
 */
@SuppressLint("NewApi")
public class CurlActivity extends Activity {

	IInAppBillingService mservice;
	ServiceConnection connection;
	String oneDollar = "com.qaziconsultancy.asingle"; 
	String twoDollars = "com.qaziconsultancy.atwo"; 
	String threeDollars = "com.qaziconsultancy.athree"; 
	String fourDollars = "com.qaziconsultancy.afour"; 
	String fiveDollars = "com.qaziconsultancy.afive"; 
	String tenDollars = "com.qaziconsultancy.aten"; 
	String twentyDollars = "com.qaziconsultancy.atwenty"; 
	String fiftyDollars = "com.qaziconsultancy.afifty"; 
	String hundredDollars = "com.qaziconsultancy.ahundred"; 
	String twoHundredDollars = "com.qaziconsultancy.atwohundred"; 
	private CurlView mCurlView;
	public boolean isZoomed;
	int ind;
	int index;
	private NotesDbAdapter mDbHelper;
	private Long mRowId;
	Handler mHandler;
	Boolean rated;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	Long time;
	Boolean ran;
	int totalPages;
	Boolean subscribed;
	int code;
	String email;
	int continuePage;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		connection = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				mservice = null;

			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mservice = IInAppBillingService.Stub.asInterface(service);
			}
		};
		
		bindService(new Intent(
				"com.android.vending.billing.InAppBillingService.BIND"),
				connection, Context.BIND_AUTO_CREATE);
		getOverflowMenu();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		mHandler = new Handler();
		rated=false;
		rated= sharedPref.getBoolean("rated", false);
    	time=sharedPref.getLong("time", 604803000);
		ran=false;
		//setTitle("");
		File n=new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.16linequran/16 line quran/558.jpg");
		File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/16linequran.zip");
		if(file.exists()&&n.exists())
		file.delete();
		if(!n.exists())
		{
			 Intent intent =new Intent(CurlActivity.this,DownloadActivity.class);
   			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   			startActivity(intent);
   			finish();
			/*startActivity(intent);*/
			/*startService(intent);
			Toast.makeText(CurlActivity.this, "Please continue your work. We will notify you when your download is complete.", Toast.LENGTH_LONG).show();
			finish();*/
			//if(startService(intent)!=null)
			//{
				//intent.putExtra("start", "dont");
				//Toast.makeText(getBaseContext(), "Your download has been restarted.", Toast.LENGTH_LONG).show();
				//finish();
			//}
			//else{
				
			//}
		}
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		subscribed=sharedPref.getBoolean("subscribed", false);
		/*if(!subscribed)
		{
					
			subscribed=getIntent().getBooleanExtra("validated", false);
			if(subscribed==true)
			{
				editor.putBoolean("subscribed", true);
				editor.commit();
				getIntent().removeExtra("validated");
				getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				
			}
		}
		
		
		if(!subscribed){
			Intent suscribe= new Intent(CurlActivity.this,SubscriptionActivity.class);
			suscribe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(suscribe);
			finish();
		}
		*/
		
		
		
		
		//if(getIntent().getBooleanExtra("validated", true)==false){
		if(getIntent().hasExtra("code")&&sharedPref.getBoolean("done", false)==false||sharedPref.contains("code")&&sharedPref.getBoolean("done", false)==false){
			Log.e("place", "0");
			
			
		if(getIntent().hasExtra("code")){
			code=getIntent().getIntExtra("code", 0);
			email=getIntent().getStringExtra("email");
			editor.putString("email", email);
		editor.putInt("code", code);
		editor.commit();
		Log.e("place", sharedPref.getInt("code", 0)+"");}
		
		if(!internetIsConnected(CurlActivity.this))
		{
			Log.e("place", "1");
		}
		else if(internetIsConnected(CurlActivity.this))
		{
			Log.e("place", "2");
			new SendTask().execute();
		}
		}
		String page="";
		
		index=557;
		if(sharedPref.getInt("continuePage", -1)!=-1)
		{
			index=(sharedPref.getInt("continuePage", -1));
		}
		try{
		Intent intent = getIntent();
		page=intent.getStringExtra("PAGE");
		index=Integer.parseInt(page);
		
		Log.e("Page", page);
		}
		catch(NullPointerException d)
		{
			Log.e("Page", "none1");
		}
		catch(RuntimeException e)
		{
			Log.e("Page", "none"+e);
		}
		mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(NotesDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(NotesDbAdapter.KEY_ROWID)
									: null;}
		isZoomed=false;
		
		ind=0;
		if (getLastNonConfigurationInstance() != null) {
			index = (Integer) getLastNonConfigurationInstance();
		}
		mCurlView = (CurlView) findViewById(R.id.curl);
		mCurlView.setPageProvider(new PageProvider());
		mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		mCurlView.setCurrentIndex(index);
		mCurlView.setBackgroundColor(0xFF202830);
		
		final AlertDialog.Builder alertbox4 = new AlertDialog.Builder(CurlActivity.this);
		alertbox4.setTitle("Weekly Link!");
	    alertbox4.setIcon(android.R.drawable.stat_notify_error);
		
        
              alertbox4.setMessage("Check out the Cool Weekly Link! Once you check it out, you won't see this popup for another week.");
              alertbox4.setNeutralButton("OK", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface arg0, int arg1) {
            	  
            	 
            	  Calendar c = Calendar.getInstance();
            	// year, month, day, hourOfDay, minute
            	c.set(1990, 7, 12, 9, 34);
            	long millis = c.getTimeInMillis();
            	  editor.putLong("time", millis);
          		editor.commit();
            	  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gomedina.com/advertisement")));
                }
            });
              alertbox4.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} });
		//alertbox4.show();
            //Calendar c2 = Calendar.getInstance();
          	// year, month, day, hourOfDay, minute
          	//c2.set(1990, 7, 12, 9, 34);
          //	long millis2 = c2.getTimeInMillis();
          //	Log.e("math", time +"-"+millis2+"="+(time-millis2));
          	
		new Thread(new Runnable() {
	        @Override
	        public void run() {
	            // TODO Auto-generated method stub
	            while (true) {
	                try {
	                    Thread.sleep(2000);
	                    mHandler.post(new Runnable() {

	                        @Override
	                        public void run() {
	                            // TODO Auto-generated method stub
	                            // Write your code here to update the UI.
	                        	
              					Calendar c2 = Calendar.getInstance();
	                        	// year, month, day, hourOfDay, minute
	                        	c2.set(1990, 7, 12, 9, 34);
	                        	long millis2 = c2.getTimeInMillis();
	                        	Log.e("math", time +"-"+millis2+"="+(time-millis2));
	                        	if(Math.abs(millis2-time)>604800000&&ran==false)
	                        	{
	                        		try{
	                        			
	                        	alertbox4.show();
	                        		}
	                        		catch(BadTokenException e)
	                        		{
	                        			
	                        		}
	                        	}
	                        	ran=true;
	                        }
	                    });
	                } catch (Exception e) {
	                    // TODO: handle exception
	                }
	            }
	        }
	    }).start();
          	
	}
		
		// This is something somewhat experimental. Before uncommenting next
		// line, please see method comments in CurlView.
		// mCurlView.setEnableTouchPressure(true);
	
	
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
	
	 public void saveState(String name) {
	        String title = name;
	        String body = mCurlView.getCurrentIndex()+"";

	        
	            long id = mDbHelper.createNote(title, body);
	            if (id > 0) {
	                mRowId = id;
	            }
	       
	    }
	 
	
	@Override
	public void onPause() {
		super.onPause();
		mCurlView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mCurlView.onResume();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		return mCurlView.getCurrentIndex();
	}

	/**
	 * Bitmap provider.
	 */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;	 
	}
	
	@Override
  	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId())
		{
		case R.id.item1:
			//mCurlView.setPageProvider(new PageProvider());
			//mCurlView.setSizeChangedObserver(new SizeChangedObserver());
			//mCurlView.setCurrentIndex((new PageProvider().getPageCount()-1));
			// 1. Instantiate an AlertDialog.Builder with its constructor
			
			String[] pages=new String [549];
			for(int i=0;i<549;i++)
			{
				pages[i]=i+1+"";
			}
			 AlertDialog.Builder builder = new AlertDialog.Builder(CurlActivity.this);
			@SuppressWarnings("unused")
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		              R.layout.mytextview, pages);
			// 2. Chain together various setter methods to set the dialog characteristics
			builder
			       .setTitle("Go To Page")
			 		.setItems(pages, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   exitZoom(); dialog.dismiss();
	            	   mCurlView.setPageProvider(new PageProvider());
	       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
	       			mCurlView.setCurrentIndex(556-which);
	       			
	               // The 'which' argument contains the index position
	               // of the selected item
	           }
	    });
	    

			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		case R.id.item2:
			//mCurlView.setPageProvider(new PageProvider());
			//mCurlView.setSizeChangedObserver(new SizeChangedObserver());
			//mCurlView.setCurrentIndex((new PageProvider().getPageCount()-1));
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder2 = new AlertDialog.Builder(CurlActivity.this);
			
			String[] juz=getResources().getStringArray(R.array.juz);
			
			 @SuppressWarnings("unused")
			ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
		              R.layout.mytextview,  juz);
			// 2. Chain together various setter methods to set the dialog characteristics
			builder2
			       .setTitle("Go To Juz (Parah)")
			 		.setItems(juz, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   if(which==0){
	            		   exitZoom(); dialog.dismiss();
	            	   mCurlView.setPageProvider(new PageProvider());
	       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
	       			mCurlView.setCurrentIndex(557-2);
	       			
	            	   }
	            	   if(which==1){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-21);
		       			
		            	   }
	            	   if(which==2){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-39);
		       			
		            	   }
	            	   if(which==3){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-57);
		       			
		            	   }
	            	   if(which==4){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-75);
		       			
		            	   }
	            	   if(which==5){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-93);
		       			
		            	   }
	            	   if(which==6){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-111);
		       			
		            	   }
	            	   if(which==7){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-129);
		       			
		            	   }
	            	   if(which==8){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-147);
		       			
		            	   }
	            	   if(which==9){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-165);
		       			
		            	   }
	            	   if(which==10){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-183);
		       			
		            	   }
	            	   if(which==11){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-201);
		       			
		            	   }
	            	   if(which==12){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-219);
		       			
		            	   }
	            	   if(which==13){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-237);
		       			
		            	   }
	            	   if(which==14){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-255);
		       			
		            	   }
	            	   if(which==15){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-273);
		       			
		            	   }
	            	   if(which==16){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-291);
		       			
		            	   }
	            	   if(which==17){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-309);
		       			
		            	   }
	            	   if(which==18){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-327);
		       			
		            	   }
	            	   if(which==19){
	            		   exitZoom(); dialog.dismiss();
	            	   mCurlView.setPageProvider(new PageProvider());
	       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
	       			mCurlView.setCurrentIndex(557-345);
	       			
	            	   }
	            	   if(which==20){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-363);
		       			
		            	   }
	            	   if(which==21){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-381);
		       			
		            	   }
	            	   if(which==22){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-399);
		       			
		            	   }
	            	   if(which==23){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-417);
		       			
		            	   }
	            	   if(which==24){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-435);
		       			
		            	   }
	            	   if(which==25){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-453);
		       			
		            	   }
	            	   if(which==26){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-471);
		       			
		            	   }
	            	   if(which==27){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-489);
		       			
		            	   }
	            	   if(which==28){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-509);
		       			
		            	   }
	            	   if(which==29){
	            		   exitZoom(); dialog.dismiss();
		            	   mCurlView.setPageProvider(new PageProvider());
		       			mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		       			mCurlView.setCurrentIndex(557-529);
		       			
		            	   }
	            	   
	            	   
	               // The 'which' argument contains the index position
	               // of the selected item
	           }
	    });
	    

			// 3. Get the AlertDialog from create()
			AlertDialog dialog2 = builder2.create();
			dialog2.show();
			return true;	
		case R.id.item3:
			//mCurlView.setPageProvider(new PageProvider());
			//mCurlView.setSizeChangedObserver(new SizeChangedObserver());
			//mCurlView.setCurrentIndex((new PageProvider().getPageCount()-1));
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder3 = new AlertDialog.Builder(CurlActivity.this);
			
			String[] surah=getResources().getStringArray(R.array.surah);
			
			 @SuppressWarnings("unused")
			ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
		              R.layout.mytextview,  surah);
			// 2. Chain together various setter methods to set the dialog characteristics
			builder3
			       .setTitle("Go To Surah")
			 		.setItems(surah, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   if(which==0){
	            		   exitZoom(); dialog.dismiss();
	            		   mCurlView.setPageProvider(new PageProvider());
	            		   mCurlView.setSizeChangedObserver(new SizeChangedObserver());
	            		   mCurlView.setCurrentIndex(557-2);
	            		   }
	            		   if(which==1){
	            		   exitZoom(); dialog.dismiss();
	            		   mCurlView.setPageProvider(new PageProvider());
	            		   mCurlView.setSizeChangedObserver(new SizeChangedObserver());
	            		   mCurlView.setCurrentIndex(557-3);
	            		   }
	            		   if(which==2){
	            		   exitZoom(); dialog.dismiss();
	            		   mCurlView.setPageProvider(new PageProvider());
	            		   mCurlView.setSizeChangedObserver(new SizeChangedObserver());
	            		   mCurlView.setCurrentIndex(557-46);
	            		   }
	            		   if(which==3){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-70);
							}
							if(which==4){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-97);
							}
							if(which==5){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-116);
							}
							if(which==6){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-137);
							}
							if(which==7){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-160);
							}
							if(which==8){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-169);
							}
							if(which==9){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-188);
							}
							if(which==10){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-200);
							}
							if(which==11){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-213);
							}
							if(which==12){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-225);
							}
							if(which==13){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-231);
							}
							if(which==14){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-236);
							}
							if(which==15){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-241);
							}
							if(which==16){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-255);
							}
							if(which==17){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-265);
							}
							if(which==18){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-276);
							}
							if(which==19){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-282);
							}
							if(which==20){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-291);
							}
							if(which==21){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-300);
							}
							if(which==22){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-309);
							}
							if(which==23){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-316);
							}
							if(which==24){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-325);
							}
							if(which==25){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-331);
							}
							if(which==26){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-340);
							}
							if(which==27){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-348);
							}
							if(which==28){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-358);
							}
							if(which==29){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-365);
							}
							if(which==30){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-371);
							}
							if(which==31){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-373);
							}
							if(which==32){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-377);
							}
							if(which==33){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-386);
							}
							if(which==34){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-392);
							}
							if(which==35){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-397);
							}
							if(which==36){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-402);
							}
							if(which==37){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-409);
							}
							if(which==38){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-413);
							}
							if(which==39){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-421);
							}
							if(which==40){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-430);
							}
							if(which==41){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-435);
							}
							if(which==42){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-441);
							}
							if(which==43){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-447);
							}
							if(which==44){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-449);
							}
							if(which==45){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-453);
							}
							if(which==46){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-457);
							}
							if(which==47){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-461);
							}
							if(which==48){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-464);
							}
							if(which==49){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-467);
							}
							if(which==50){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-469);
							}
							if(which==51){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-472);
							}
							if(which==52){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-474);
							}
							if(which==53){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-476);
							}
							if(which==54){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-479);
							}
							if(which==55){
								exitZoom(); dialog.dismiss();
								mCurlView.setPageProvider(new PageProvider());
								mCurlView.setSizeChangedObserver(new SizeChangedObserver());
								mCurlView.setCurrentIndex(557-482);
								}
							if(which==56){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-485);
							}
							if(which==57){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-489);
							}
							if(which==58){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-492);
							}
							if(which==59){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-496);
							}
							if(which==60){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-498);
							}
							if(which==61){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-500);
							}
							if(which==62){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-501);
							}
							if(which==63){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-503);
							}
							if(which==64){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-505);
							}
							if(which==65){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-507);
							}
							if(which==66){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-509);
							}
							if(which==67){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-511);
							}
							if(which==68){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-513);
							}
							if(which==69){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-515);
							}
							if(which==70){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-517);
							}
							if(which==71){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-519);
							}
							if(which==72){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-521);
							}
							if(which==73){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-522);
							}
							if(which==74){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-524);
							}
							if(which==75){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-525);
							}
							if(which==76){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-527);
							}
							if(which==77){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-529);
							}
							if(which==78){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-530);
							}
							if(which==79){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-531);
							}
							if(which==80){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-533);
							}
							if(which==81){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-533);
							}
							if(which==82){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-534);
							}
							if(which==83){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-535);
							}
							if(which==84){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-536);
							}
							if(which==85){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-537);
							}
							if(which==86){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-538);
							}
							if(which==87){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-538);
							}
							if(which==88){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-539);
							}
							if(which==89){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-540);
							}
							if(which==90){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-541);
							}
							if(which==91){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-541);
							}
							if(which==92){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-542);
							}
							if(which==93){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-542);
							}
							if(which==94){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-543);
							}
							if(which==95){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-543);
							}
							if(which==96){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-544);
							}
							if(which==97){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-544);
							}
							if(which==98){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-545);
							}
							if(which==99){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-545);
							}
							if(which==100){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-545);
							}
							if(which==101){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-546);
							}
							if(which==102){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-546);
							}
							if(which==103){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-546);
							}
							if(which==104){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-547);
							}
							if(which==105){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-547);
							}
							if(which==106){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-547);
							}
							if(which==107){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-547);
							}
							if(which==108){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-548);
							}
							if(which==109){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-548);
							}
							if(which==110){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-548);
							}
							if(which==111){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-548);
							}
							if(which==112){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-549);
							}
							if(which==113){
							exitZoom(); dialog.dismiss();
							mCurlView.setPageProvider(new PageProvider());
							mCurlView.setSizeChangedObserver(new SizeChangedObserver());
							mCurlView.setCurrentIndex(557-549);
							}
							
	               // The 'which' argument contains the index position
	               // of the selected item
	           }
	    });
	    

			// 3. Get the AlertDialog from create()
			AlertDialog dialog3 = builder3.create();
			dialog3.show();
			return true;	
		case R.id.item4:
			try{
			zoom();
			Toast.makeText(CurlActivity.this, "Pinch to zoom and press the back button to navigate again.", Toast.LENGTH_SHORT).show();
			}
		
		catch(NoClassDefFoundError b)
		{
			Toast.makeText(CurlActivity.this, "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG ).show();
		}
			catch(Exception b)
			{
				Toast.makeText(CurlActivity.this, "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG ).show();
			}
			return true;
		case R.id.item5:
			Builder builderr = new Builder(this);
			Date cDate = new Date();
       	 final String fDate = new SimpleDateFormat("MM/dd/yy").format(cDate);
       	 Date dt=new Date();
       	int hours = dt.getHours();
        int minutes = dt.getMinutes();
        
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mmaa");
        String time1 = sdf.format(dt);
     
            final EditText input = new EditText(this);
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(18);
            input.setFilters(FilterArray);
            
            input.setText(fDate+" "+time1.toLowerCase());
            builderr
                .setTitle("Bookmark")
                .setMessage("Name your Bookmark")
                .setView(input)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                    	
                    	if(!input.getText().toString().equals(""))
                    	saveState(input.getText().toString());
                    	else
                    		saveState(fDate);
            			setResult(RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {}

                });

                builderr.show();
			
            
			return true;
		case R.id.item6:
			Intent i=new Intent(CurlActivity.this,Notepadv3.class);
			
			startActivity(i);
			return true;
		case R.id.item7:
			try {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Qazi+Musab")));
			} catch (android.content.ActivityNotFoundException anfe) {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Qazi+Musab")));
			}
			return true;
		case R.id.item8:
			String appName2="qazi.musab.sixteenliner";
			try {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName2)));
			} catch (android.content.ActivityNotFoundException anfe) {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName2)));
			}
			return true;
		case R.id.item9:
		    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gomedina.com/advertisement")));
		return true;
		case R.id.item10:
			//exitZoom(); 
			Log.e("index", mCurlView.getCurrentIndex()+"");
			if(!isZoomed)
			mCurlView.setCurrentIndex(mCurlView.getCurrentIndex()-1);
		return true;
		case R.id.item11:
			//exitZoom(); 
			Log.e("index", mCurlView.getCurrentIndex()+"");
			if(!isZoomed)
			mCurlView.setCurrentIndex(mCurlView.getCurrentIndex()+1);
		return true;
		case R.id.item12:
			String[] items=new String [9];
			items[0]="$1";
			items[1]="$2";
			items[2]="$3";
			items[3]="$4";
			items[4]="$5";
			items[5]="$10";
			items[6]="$20";
			items[7]="$50";
			items[8]="$100";
			
			
			
			 AlertDialog.Builder chooser = new AlertDialog.Builder(CurlActivity.this);
			chooser
			       .setTitle("Contribute")
			 		.setItems(items, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   switch(which) {
	            	    case 0:
	            	       contributeOneDollar();
	            	        break;
	            	    case 1:
	            	        contributetwoDollars();
	            	        break;
	            	    case 2:
		            	       contributeThreeDollars();
		            	        break;
		            	    case 3:
		            	        contributeFourDollars();
		            	        break;
		            	    case 4:
			            	       contributeFiveDollars();
			            	        break;
			            	    case 5:
			            	        contributeTenDollars();
			            	        break;
			            	    case 6:
				            	       contributeTwentyDollars();
				            	        break;
				            	    case 7:
				            	        contributeFiftyDollars();
				            	        break;
				            	    case 8:
					            	       contributeHundredDollars();
					            	        break;
					            	   
	            	    
	            	       
	            	}
	               }
	    });
	    

			// 3. Get the AlertDialog from create()
			AlertDialog myChooser = chooser.create();
			myChooser.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private class PageProvider implements CurlView.PageProvider {

	

		@Override
		public int getPageCount() {
			return 558;
		}

		private Bitmap loadBitmap(int width, int height, int index) throws FileNotFoundException {
			Bitmap b = Bitmap.createBitmap(width, height,
					Bitmap.Config.ARGB_8888);
			b.eraseColor(0xFFFFFFFF);
			Canvas c = new Canvas(b);
			Drawable d;
			//if(index==610){
				String pathName = Environment.getExternalStorageDirectory().getPath()+"/Pictures/.16linequran/16 line quran/"+(558-index)+".jpg";
				Log.e("message",pathName);
				 d = Drawable.createFromPath(pathName);
			//}
			//else
			//d = getResources().getDrawable(mBitmapIds[610-index]);
			int margin = 0;
			int border = 0;
			Rect r = new Rect(margin, margin, width - margin, height - margin);

			int imageWidth = (r.width() - (border * 2));
			int imageHeight = r.height();
			if (imageHeight > r.height() - (border * 2)) {
				imageHeight = r.height() - (border * 2);
				imageWidth = imageHeight * d.getIntrinsicWidth()
						/ d.getIntrinsicHeight();
			}

			r.left += ((r.width() - imageWidth) / 2) - border;
			r.right = r.left + imageWidth + border + border;
			r.top += ((r.height() - imageHeight) / 2) - border;
			r.bottom = r.top + imageHeight + border + border;

			Paint p = new Paint();
			p.setColor(0xFFC0C0C0);
			c.drawRect(r, p);
			r.left += border;
			r.right -= border;
			r.top += border;
			r.bottom -= border;

			d.setBounds(r);
			d.draw(c);

			return b;
			
		}

		@Override
	    public void updatePage(CurlPage page, int width, int height, int index) {
			try{
	        Bitmap front = loadBitmap(width, height, index);
	        page.setTexture(front, CurlPage.SIDE_BOTH);
	        page.setColor(Color.argb(127, 255, 255, 255), CurlPage.SIDE_BACK);
			}
			catch(FileNotFoundException e)
			{
				finish();
			}
			catch(Exception e)
			{
				finish();
			}
	    }
	}

	/**
	 * CurlView size changed observer.
	 */
	private class SizeChangedObserver implements CurlView.SizeChangedObserver {
		@Override
		public void onSizeChanged(int w, int h) {
			if (w > h) {
				mCurlView.setViewMode(CurlView.SHOW_TWO_PAGES);
				mCurlView.setMargins(0f, 0f, 0f, 0f);
			} else {
				mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
				mCurlView.setMargins(0f, 0f, .0f, 0f);
			}
		}
	}
	private void getOverflowMenu() {

	    try {
	       ViewConfiguration config = ViewConfiguration.get(this);
	       Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	       if(menuKeyField != null) {
	           menuKeyField.setAccessible(true);
	           menuKeyField.setBoolean(config, false);
	       }
	   } catch (Exception e) {
	       e.printStackTrace();
	   }
	 }
	public static int getResId(CurlView mCurlView,ImageView imageView, String name) {

		  try {
	            @SuppressWarnings("rawtypes")
				Class res = R.drawable.class;
	            Field field = res.getField(name);
	            int drawableId = field.getInt(field);
	            return drawableId;
	        }
	        catch (Exception e) {
	            Log.e("MyTag", "Failure to get drawable id.", e);
	            return -1;
	        }
	}
	@Override
	public void onBackPressed()
	{
		final AlertDialog.Builder alertbox5 = new AlertDialog.Builder(CurlActivity.this);
		alertbox5.setTitle("Rate Us!");
	    alertbox5.setIcon(android.R.drawable.stat_notify_error);
		
        
              alertbox5.setMessage("Please rate us and leave a suggestion so we can enhance this app to your liking! Once you rate, this dialog box won't show again.");
              alertbox5.setNeutralButton("Rate", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface arg0, int arg1) {
            	  editor.putInt("continuePage", mCurlView.getCurrentIndex());
 				 editor.commit();
            	  rated=true;
            	  editor.putBoolean("rated", rated);
          		editor.commit();
            	  String appName2="qazi.musab.sixteenliner";
      			try {
      			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName2)));
      			} catch (android.content.ActivityNotFoundException anfe) {
      			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName2)));
      			}
                }
            });
              alertbox5.setNegativeButton("Exit", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {editor.putInt("continuePage", mCurlView.getCurrentIndex());
				 editor.commit();finish();} });
		if(isZoomed==true){
			exitZoom();
		}
		else{
			if(!rated)
			alertbox5.show();
			else{
				editor.putInt("continuePage", mCurlView.getCurrentIndex());
				 editor.commit();
			finish();
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
	  super.onSaveInstanceState(outState);	 
	  outState.putBoolean("isZoomed",isZoomed);
	  outState.putBoolean("ran",ran);
	  outState.putInt("index",index);
	  outState.putInt("ind",ind);

	  //outState.putBoolean("submitOn", submitOn);
	  //outState.putBoolean("autofillOn", autofillOn);
	}
	//Restore instance state
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		isZoomed=savedInstanceState.getBoolean("isZoomed");
		ran=savedInstanceState.getBoolean("ran");
		index=savedInstanceState.getInt("index");
		ind=savedInstanceState.getInt("ind");
		if(isZoomed&&getScreenOrientation()==Configuration.ORIENTATION_PORTRAIT)
		{
			try{
			 FrameLayout view = new FrameLayout(this);
			 
			 
		        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.TOP | Gravity.LEFT);

		        ImageView imageView = new ImageView(this);
		        imageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		        //String res="R.drawable.page"+mCurlView.getCurrentIndex();
		        //imageView.setImageResource(getResId("page"+mCurlView.getCurrentIndex()+1,Drawable.class));
		        /*if(getResId(mCurlView,imageView,"page"+(608-mCurlView.getCurrentIndex()+1))!=-1)
		            imageView.setImageResource(getResId(mCurlView,imageView,"page"+(608-mCurlView.getCurrentIndex()+1)));
		        else
		        	 imageView.setImageResource(getResId(mCurlView,imageView,"coverpage"));*/
		        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.16linequran/16 line quran/"+(558-mCurlView.getCurrentIndex())+".jpg");
        		Uri uri = Uri.fromFile(file);
        		imageView.setImageURI(uri);
		      ind=(mCurlView.getCurrentIndex());
		      
		      
		        //Use line below for large images if you have hardware rendering turned on
		        //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		        // Line below is optional, depending on what scaling method you want
		        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		        view.addView(imageView, fp);
		        view.setOnTouchListener(new PanAndZoomListener(view, imageView, 1));
		        
		        setContentView(view);
		        view.setVisibility(View.VISIBLE);
		        isZoomed=true;
			}
			catch(NoClassDefFoundError b)
			{
				Toast.makeText(CurlActivity.this, "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG ).show();
			}
		}
		else if(isZoomed&&getScreenOrientation()==Configuration.ORIENTATION_LANDSCAPE)
		{
			try{
			zoom();
			}
			catch(NoClassDefFoundError b)
			{
				Toast.makeText(CurlActivity.this, "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG ).show();
			}
			catch(Exception b)
			{
				Toast.makeText(CurlActivity.this, "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG ).show();
			}
		}
	}
	public void exitZoom()
	{
		setContentView(R.layout.main);
		if (getLastNonConfigurationInstance() != null) {
			index = (Integer) getLastNonConfigurationInstance();
		}
		mCurlView = (CurlView) findViewById(R.id.curl);
		mCurlView.setPageProvider(new PageProvider());
		mCurlView.setSizeChangedObserver(new SizeChangedObserver());
		mCurlView.setCurrentIndex(ind);
		mCurlView.setBackgroundColor(0xFF202830);
			
		isZoomed=false;
	}
	public class ImageLoader extends AsyncTask<String, Void, Void > {
	    

	    protected void onPreExecute(){
	       
	    }

	    @Override
	    protected Void doInBackground(String... params) {
	           
	        return null;
	    }

	    protected void onPostExecute(Void... params){
	       
	    }
	    
	}
	
	public int getScreenOrientation()
	{
	    Display getOrient = getWindowManager().getDefaultDisplay();
	    int orientation = Configuration.ORIENTATION_UNDEFINED;
	    if(getOrient.getWidth()==getOrient.getHeight()){
	        orientation = Configuration.ORIENTATION_SQUARE;
	    } else{ 
	        if(getOrient.getWidth() < getOrient.getHeight()){
	            orientation = Configuration.ORIENTATION_PORTRAIT;
	        }else { 
	             orientation = Configuration.ORIENTATION_LANDSCAPE;
	        }
	    }
	    return orientation;
	}

	 public void zoom()
	 {
		 
		 FrameLayout view = new FrameLayout(CurlActivity.this);

	        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.TOP | Gravity.LEFT);

	        ImageView imageView = new ImageView(CurlActivity.this);
	        imageView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	        //String res="R.drawable.page"+mCurlView.getCurrentIndex();
	        //imageView.setImageResource(getResId("page"+mCurlView.getCurrentIndex()+1,Drawable.class));
	        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.16linequran/16 line quran/"+(558-mCurlView.getCurrentIndex())+".jpg");
	        		Uri uri = Uri.fromFile(file);
	        		imageView.setImageURI(uri);
	       /* if(getResId(mCurlView,imageView,"page"+(608-mCurlView.getCurrentIndex()+1))!=-1)
	            imageView.setImageResource(getResId(mCurlView,imageView,"page"+(608-mCurlView.getCurrentIndex()+1)));
	        else
	        	 imageView.setImageResource(getResId(mCurlView,imageView,"coverpage"));*/
	      ind=(mCurlView.getCurrentIndex());
	        
	        //Use line below for large images if you have hardware rendering turned on
	        //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	        // Line below is optional, depending on what scaling method you want
	      
	      if(getScreenOrientation()==Configuration.ORIENTATION_PORTRAIT)
			{
	      imageView.setScaleType(ImageView.ScaleType.FIT_XY);}
	      else
	      {}
	        view.addView(imageView, fp);
	        view.setOnTouchListener(new PanAndZoomListener(view, imageView, 1));
	        
	        setContentView(view);
	        view.setVisibility(View.VISIBLE);
	        isZoomed=true;
	        
	 }
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	    if (requestCode == 1001) {           
	       int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
	       final String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
	       String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
	       new Thread(new Runnable(){
               public void run(){          
                   //Thread.sleep(5*1000);
              	 try {
              		Bundle ownedItems = mservice.getPurchases(3, getPackageName(), "inapp", null);
              	// Check response
              	int responseCode = ownedItems.getInt("RESPONSE_CODE");
              	if (responseCode != 0) {
              	   throw new Exception("Error");
              	}
              	// Get the list of purchased items
              	ArrayList<String> purchaseDataList = 
              	    ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
              	for (String purchaseData : purchaseDataList) {
              	    JSONObject o = new JSONObject(purchaseData);
              	    String purchaseToken = o.optString("token", o.optString("purchaseToken"));
              	    // Consume purchaseToken, handling any errors
              	    mservice.consumePurchase(3, getPackageName(), purchaseToken);
              	}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
               }           
           }).start(); 
	       if (resultCode == RESULT_OK) {
	          try {
	             JSONObject jo = new JSONObject(purchaseData);
	             String sku = jo.getString("productId");
	             new Thread(new Runnable(){
	                 public void run(){          
	                	 try {
	                   		Bundle ownedItems = mservice.getPurchases(3, getPackageName(), "inapp", null);
	                   	// Check response
	                   	int responseCode = ownedItems.getInt("RESPONSE_CODE");
	                   	if (responseCode != 0) {
	                   	   throw new Exception("Error");
	                   	}
	                   	// Get the list of purchased items
	                   	ArrayList<String> purchaseDataList = 
	                   	    ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
	                   	for (String purchaseData : purchaseDataList) {
	                   	    JSONObject o = new JSONObject(purchaseData);
	                   	    String purchaseToken = o.optString("token", o.optString("purchaseToken"));
	                   	    // Consume purchaseToken, handling any errors
	                   	    mservice.consumePurchase(3, getPackageName(), purchaseToken);
	                   	}
	     					} catch (RemoteException e) {
	     						// TODO Auto-generated catch block
	     						e.printStackTrace();
	     					} catch (Exception e) {
	     						// TODO Auto-generated catch block
	     						e.printStackTrace();
	     					}
	                    }                 
	             }).start(); 
	            
	           }
	           catch (JSONException e) {
	             
	              e.printStackTrace();
	           }
	       }
	    }
	 }
	 @Override
	 public void onDestroy() {
	     super.onDestroy();
	     if (mservice != null) {
	         unbindService(connection);
	     }   
	 }
	 public void contributeFiveDollars()
		{
			new Thread(new Runnable(){
	            public void run(){    
			ArrayList skuList = new ArrayList();
			skuList.add(fiveDollars);
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			final Bundle skuDetails;
			try {
				      
	                	 skuDetails = mservice.getSkuDetails(3, getPackageName(),
	     						"inapp", querySkus);

	                 
				
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {

					ArrayList<String> responseList = skuDetails
							.getStringArrayList("DETAILS_LIST");

					for (String thisResponse : responseList) {
						JSONObject object = new JSONObject(thisResponse);
						String sku = object.getString("productId");
						String price = object.getString("price");
						if (sku.equals(fiveDollars)) {
							System.out.println("price " + price);
							Bundle buyIntentBundle = mservice
									.getBuyIntent(3, getPackageName(), sku,
											"inapp",
											"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
							PendingIntent pendingIntent = buyIntentBundle
									.getParcelable("BUY_INTENT");
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
					}
				}
				//
			} 
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
				}           
	        }).start(); 
			
		}
	 public void contributeOneDollar()
		{
			new Thread(new Runnable(){
	            public void run(){    
			ArrayList skuList = new ArrayList();
			skuList.add(oneDollar);
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			final Bundle skuDetails;
			try {
				      
	                	 skuDetails = mservice.getSkuDetails(3, getPackageName(),
	     						"inapp", querySkus);

	                 
				
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {

					ArrayList<String> responseList = skuDetails
							.getStringArrayList("DETAILS_LIST");

					for (String thisResponse : responseList) {
						JSONObject object = new JSONObject(thisResponse);
						String sku = object.getString("productId");
						String price = object.getString("price");
						if (sku.equals(oneDollar)) {
							System.out.println("price " + price);
							Bundle buyIntentBundle = mservice
									.getBuyIntent(3, getPackageName(), sku,
											"inapp",
											"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
							PendingIntent pendingIntent = buyIntentBundle
									.getParcelable("BUY_INTENT");
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
					}
				}
				//
			} 
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
				}           
	        }).start(); 
			
		}
	 public void contributeTenDollars()
		{
			new Thread(new Runnable(){
	            public void run(){    
			ArrayList skuList = new ArrayList();
			skuList.add(tenDollars);
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			final Bundle skuDetails;
			try {
				      
	                	 skuDetails = mservice.getSkuDetails(3, getPackageName(),
	     						"inapp", querySkus);

	                 
				
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {

					ArrayList<String> responseList = skuDetails
							.getStringArrayList("DETAILS_LIST");

					for (String thisResponse : responseList) {
						JSONObject object = new JSONObject(thisResponse);
						String sku = object.getString("productId");
						String price = object.getString("price");
						if (sku.equals(tenDollars)) {
							System.out.println("price " + price);
							Bundle buyIntentBundle = mservice
									.getBuyIntent(3, getPackageName(), sku,
											"inapp",
											"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
							PendingIntent pendingIntent = buyIntentBundle
									.getParcelable("BUY_INTENT");
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
					}
				}
				//
			} 
			catch (RemoteException e) {
				
				e.printStackTrace();
			}
			catch (JSONException e) {
				
				e.printStackTrace();
			} catch (SendIntentException e) {
				
				e.printStackTrace();
			} 
				}           
	        }).start(); 
			
		}
	 public void contributeTwentyDollars()
		{
			new Thread(new Runnable(){
	            public void run(){    
			ArrayList skuList = new ArrayList();
			skuList.add(twentyDollars);
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			final Bundle skuDetails;
			try {
				      
	                	 skuDetails = mservice.getSkuDetails(3, getPackageName(),
	     						"inapp", querySkus);

	                 
				
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {

					ArrayList<String> responseList = skuDetails
							.getStringArrayList("DETAILS_LIST");

					for (String thisResponse : responseList) {
						JSONObject object = new JSONObject(thisResponse);
						String sku = object.getString("productId");
						String price = object.getString("price");
						if (sku.equals(twentyDollars)) {
							System.out.println("price " + price);
							Bundle buyIntentBundle = mservice
									.getBuyIntent(3, getPackageName(), sku,
											"inapp",
											"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
							PendingIntent pendingIntent = buyIntentBundle
									.getParcelable("BUY_INTENT");
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
					}
				}
				//
			} 
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
				}           
	        }).start(); 
			
		}
	 public void contributeFiftyDollars()
		{
			new Thread(new Runnable(){
	            public void run(){    
			ArrayList skuList = new ArrayList();
			skuList.add(fiftyDollars);
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			final Bundle skuDetails;
			try {
				      
	                	 skuDetails = mservice.getSkuDetails(3, getPackageName(),
	     						"inapp", querySkus);

	                 
				
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {

					ArrayList<String> responseList = skuDetails
							.getStringArrayList("DETAILS_LIST");

					for (String thisResponse : responseList) {
						JSONObject object = new JSONObject(thisResponse);
						String sku = object.getString("productId");
						String price = object.getString("price");
						if (sku.equals(fiftyDollars)) {
							System.out.println("price " + price);
							Bundle buyIntentBundle = mservice
									.getBuyIntent(3, getPackageName(), sku,
											"inapp",
											"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
							PendingIntent pendingIntent = buyIntentBundle
									.getParcelable("BUY_INTENT");
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
					}
				}
				//
			} 
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
				}           
	        }).start(); 
			
		}
	 public void contributeHundredDollars()
		{
			new Thread(new Runnable(){
	            public void run(){    
			ArrayList skuList = new ArrayList();
			skuList.add(hundredDollars);
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			final Bundle skuDetails;
			try {
				      
	                	 skuDetails = mservice.getSkuDetails(3, getPackageName(),
	     						"inapp", querySkus);

	                 
				
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {

					ArrayList<String> responseList = skuDetails
							.getStringArrayList("DETAILS_LIST");

					for (String thisResponse : responseList) {
						JSONObject object = new JSONObject(thisResponse);
						String sku = object.getString("productId");
						String price = object.getString("price");
						if (sku.equals(hundredDollars)) {
							System.out.println("price " + price);
							Bundle buyIntentBundle = mservice
									.getBuyIntent(3, getPackageName(), sku,
											"inapp",
											"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
							PendingIntent pendingIntent = buyIntentBundle
									.getParcelable("BUY_INTENT");
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
					}
				}
				//
			} 
			catch (RemoteException e) {
				// 
				e.printStackTrace();
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SendIntentException e) {
				// 
				e.printStackTrace();
			} 
				}           
	        }).start(); 
			
		}
	 public void contributetwoDollars()
		{
			new Thread(new Runnable(){
	            public void run(){    
			ArrayList skuList = new ArrayList();
			skuList.add(twoDollars);
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			final Bundle skuDetails;
			try {
				      
	                	 skuDetails = mservice.getSkuDetails(3, getPackageName(),
	     						"inapp", querySkus);

	                 
				
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {

					ArrayList<String> responseList = skuDetails
							.getStringArrayList("DETAILS_LIST");

					for (String thisResponse : responseList) {
						JSONObject object = new JSONObject(thisResponse);
						String sku = object.getString("productId");
						String price = object.getString("price");
						if (sku.equals(twoDollars)) {
							System.out.println("price " + price);
							Bundle buyIntentBundle = mservice
									.getBuyIntent(3, getPackageName(), sku,
											"inapp",
											"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
							PendingIntent pendingIntent = buyIntentBundle
									.getParcelable("BUY_INTENT");
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
					}
				}
				//
			} 
			catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
				}           
	        }).start(); 
			
		}
	 public void contributeThreeDollars()
		{
			new Thread(new Runnable(){
	            public void run(){    
			ArrayList skuList = new ArrayList();
			skuList.add(threeDollars);
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			final Bundle skuDetails;
			try {
				      
	                	 skuDetails = mservice.getSkuDetails(3, getPackageName(),
	     						"inapp", querySkus);

	                 
				
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {

					ArrayList<String> responseList = skuDetails
							.getStringArrayList("DETAILS_LIST");

					for (String thisResponse : responseList) {
						JSONObject object = new JSONObject(thisResponse);
						String sku = object.getString("productId");
						String price = object.getString("price");
						if (sku.equals(threeDollars)) {
							System.out.println("price " + price);
							Bundle buyIntentBundle = mservice
									.getBuyIntent(3, getPackageName(), sku,
											"inapp",
											"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
							PendingIntent pendingIntent = buyIntentBundle
									.getParcelable("BUY_INTENT");
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
					}
				}
				//
			} 
			catch (RemoteException e) {
				// 
				e.printStackTrace();
			}
			catch (JSONException e) {
				// 
				e.printStackTrace();
			} catch (SendIntentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
				}           
	        }).start(); 
			
		}
	 public void contributeFourDollars()
		{
			new Thread(new Runnable(){
	            public void run(){    
			ArrayList skuList = new ArrayList();
			skuList.add(fourDollars);
			Bundle querySkus = new Bundle();
			querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
			final Bundle skuDetails;
			try {
				      
	                	 skuDetails = mservice.getSkuDetails(3, getPackageName(),
	     						"inapp", querySkus);

	                 
				
				int response = skuDetails.getInt("RESPONSE_CODE");
				if (response == 0) {

					ArrayList<String> responseList = skuDetails
							.getStringArrayList("DETAILS_LIST");

					for (String thisResponse : responseList) {
						JSONObject object = new JSONObject(thisResponse);
						String sku = object.getString("productId");
						String price = object.getString("price");
						if (sku.equals(fourDollars)) {
							System.out.println("price " + price);
							Bundle buyIntentBundle = mservice
									.getBuyIntent(3, getPackageName(), sku,
											"inapp",
											"bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
							PendingIntent pendingIntent = buyIntentBundle
									.getParcelable("BUY_INTENT");
							startIntentSenderForResult(
									pendingIntent.getIntentSender(), 1001,
									new Intent(), Integer.valueOf(0),
									Integer.valueOf(0), Integer.valueOf(0));
						}
					}
				}
				//
			} 
			catch (RemoteException e) {
				// 
				e.printStackTrace();
			}
			catch (JSONException e) {
				// 
				e.printStackTrace();
			} catch (SendIntentException e) {
				// 
				e.printStackTrace();
			} 
				}           
	        }).start(); 
			
		}
	 public class SendTask extends AsyncTask<Void, Void, Boolean> {

		    String responseString;
		    HttpResponse response;

		    @Override
		    protected Boolean doInBackground(Void... params) {
		    	
		    	
		    		Log.e("place", "4");
		    		try {
		    	HttpClient httpclient = new DefaultHttpClient();
			    HttpPost httppost = new HttpPost("http://www.qaziconsultancy.com/php/verify.php");

			
			    // Add your data
			    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			    nameValuePairs.add(new BasicNameValuePair("email", email));
			   // nameValuePairs.add(new BasicNameValuePair("verify", "true"));
			    nameValuePairs.add(new BasicNameValuePair("code", sharedPref.getInt("code", 0)+""));
			   // nameValuePairs.add(new BasicNameValuePair("country", Validate.this.getResources().getConfiguration().locale.getDisplayCountry()));
			    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			    // Execute HTTP Post Request
			     response = httpclient.execute(httppost);
			     editor.putBoolean("done", true);
					editor.commit();
					Log.e("place", code+"");

			} catch (ClientProtocolException e) {
			    
			} catch (IOException e) {
			    // 
			}  
		        return false;
		    	
		    }
		    @Override
		    protected void onPostExecute(Boolean success) {
		        
		       //Toast.makeText(Validate.this, Validate.this.getResources().getConfiguration().locale.getDisplayCountry()+"\n"+code, Toast.LENGTH_LONG).show();
		           
		        
		    }
		}

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