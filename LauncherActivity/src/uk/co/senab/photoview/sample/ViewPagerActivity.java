/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package uk.co.senab.photoview.sample;

import android.annotation.SuppressLint;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.view.PagerAdapter;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;

import com.android.vending.billing.IInAppBillingService;
import com.google.analytics.tracking.android.EasyTracker;
import com.qaziconsultancy.thirteenlinequran.R;

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
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class ViewPagerActivity extends Activity {

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
	static HackyViewPager mViewPager;
	static Display getOrient;
	private NotesDbAdapter mDbHelper;
	private Long mRowId;
	Boolean rated;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	int continuePage;
	Boolean subscribed;
	int code;
	String email;
	
	
    @SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setWindowFeatures();
		setContentView(R.layout.activity_view_pager);
        makeActionBarTransparent();
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
		
        
        
        
        File n=new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.13linequran/13 line quran/881.jpg");
		File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/13linequran.zip");
		if(file.exists()&&n.exists())
		file.delete();
		if(!n.exists())
		{
			 Intent intent =new Intent(ViewPagerActivity.this,DownloadActivity.class);
   			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
   			startActivity(intent);
   			finish();
		}
	
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		/*subscribed=sharedPref.getBoolean("subscribed", false);
		if(!subscribed)
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
			Intent suscribe= new Intent(ViewPagerActivity.this,SubscriptionActivity.class);
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
		
		if(!internetIsConnected(ViewPagerActivity.this))
		{
			Log.e("place", "1");
		}
		else if(internetIsConnected(ViewPagerActivity.this))
		{
			Log.e("place", "2");
			new SendTask().execute();
		}
		}
		//}
        int index = 881;
        String page="";
        getOverflowMenu();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getOrient = getWindowManager().getDefaultDisplay();
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		rated=false;
		rated= sharedPref.getBoolean("rated", false);
        mViewPager.setActionBar(getActionBar());
		mViewPager.setAdapter(new SamplePagerAdapter());
		mViewPager.setCurrentItem(index);
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
		mViewPager.setCurrentItem(index);
		mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
		
	}

    protected void setWindowFeatures() {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
    }

    protected void makeActionBarTransparent() {
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
        getActionBar().setSplitBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
    }

	
	
	
    
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
			//
			//
			//mViewPager.setCurrentItem((new PageProvider().getPageCount()-1));
			// 1. Instantiate an AlertDialog.Builder with its constructor
			
			String[] pages=new String [850];
			for(int i=0;i<850;i++)
			{
				pages[i]=i+1+"";
			}
			 AlertDialog.Builder builder = new AlertDialog.Builder(ViewPagerActivity.this);
			
			// 2. Chain together various setter methods to set the dialog characteristics
			builder
			       .setTitle("Go To Page")
			 		.setItems(pages, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	  dialog.dismiss();
	            	mViewPager.setCurrentItem(849-which);   
	       			
	               // The 'which' argument contains the index position
	               // of the selected item
	           }
	    });
	    

			// 3. Get the AlertDialog from create()
			AlertDialog dialog = builder.create();
			dialog.show();
			return true;
		case R.id.item2:
			//
			//
			//mViewPager.setCurrentItem((new PageProvider().getPageCount()-1));
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder2 = new AlertDialog.Builder(ViewPagerActivity.this);
			
			String[] juz=getResources().getStringArray(R.array.juz);
			
			// 2. Chain together various setter methods to set the dialog characteristics
			builder2
			       .setTitle("Go To Juz (Parah)")
			 		.setItems(juz, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   if(which==0){
	            		    dialog.dismiss();
	            	   
	       			
	       			mViewPager.setCurrentItem(850-1);
	       			
	            	   }
	            	   if(which==1){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-31);
		       			
		            	   }
	            	   if(which==2){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-59);
		       			
		            	   }
	            	   if(which==3){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-87);
		       			
		            	   }
	            	   if(which==4){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-115);
		       			
		            	   }
	            	   if(which==5){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-143);
		       			
		            	   }
	            	   if(which==6){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-171);
		       			
		            	   }
	            	   if(which==7){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-199);
		       			
		            	   }
	            	   if(which==8){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-227);
		       			
		            	   }
	            	   if(which==9){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-255);
		       			
		            	   }
	            	   if(which==10){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-283);
		       			
		            	   }
	            	   if(which==11){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-311);
		       			
		            	   }
	            	   if(which==12){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-339);
		       			
		            	   }
	            	   if(which==13){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-367);
		       			
		            	   }
	            	   if(which==14){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-395);
		       			
		            	   }
	            	   if(which==15){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-423);
		       			
		            	   }
	            	   if(which==16){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-451);
		       			
		            	   }
	            	   if(which==17){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-479);
		       			
		            	   }
	            	   if(which==18){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-507);
		       			
		            	   }
	            	   if(which==19){
	            		    dialog.dismiss();
	            	   
	       			
	       			mViewPager.setCurrentItem(850-535);
	       			
	            	   }
	            	   if(which==20){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-561);
		       			
		            	   }
	            	   if(which==21){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-589);
		       			
		            	   }
	            	   if(which==22){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-615);
		       			
		            	   }
	            	   if(which==23){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-643);
		       			
		            	   }
	            	   if(which==24){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-669);
		       			
		            	   }
	            	   if(which==25){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-699);
		       			
		            	   }
	            	   if(which==26){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-729);
		       			
		            	   }
	            	   if(which==27){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-759);
		       			
		            	   }
	            	   if(which==28){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-789);
		       			
		            	   }
	            	   if(which==29){
	            		    dialog.dismiss();
		            	   
		       			
		       			mViewPager.setCurrentItem(850-821);
		       			
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
			//
			//
			//mViewPager.setCurrentItem((new PageProvider().getPageCount()-1));
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder3 = new AlertDialog.Builder(ViewPagerActivity.this);
			
			String[] surah=getResources().getStringArray(R.array.surah);
			
			// 2. Chain together various setter methods to set the dialog characteristics
			builder3
			       .setTitle("Go To Surah")
			 		.setItems(surah, new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	            	   if(which==0){
	            		    dialog.dismiss();
	            		   
	            		   
	            		   mViewPager.setCurrentItem(620-4);
	            		   }
	            		   if(which==1){
	            		    dialog.dismiss();
	            		   
	            		   
	            		   mViewPager.setCurrentItem(620-5);
	            		   }
	            		   if(which==2){
	            		    dialog.dismiss();
	            		   
	            		   
	            		   mViewPager.setCurrentItem(850-70);
	            		   }
	            		   if(which==3){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-108);
							}
							if(which==4){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-149);
							}
							if(which==5){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-179);
							}
							if(which==6){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-211);
							}
							if(which==7){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-247);
							}
							if(which==8){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-262);
							}
							if(which==9){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-290);
							}
							if(which==10){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-310);
							}
							if(which==11){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-330);
							}
							if(which==12){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-348);
							}
							if(which==13){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-357);
							}
							if(which==14){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-366);
							}
							if(which==15){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-374);
							}
							if(which==16){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-390);
							}
							if(which==17){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-410);
							}
							if(which==18){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-427);
							}
							if(which==19){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-437);
							}
							if(which==20){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-451);
							}
							if(which==21){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-464);
							}
							if(which==22){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-479);
							}
							if(which==23){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-489);
							}
							if(which==24){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-503);
							}
							if(which==25){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-513);
							}
							if(which==26){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-527);
							}
							if(which==27){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-539);
							}
							if(which==28){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-554);
							}
							if(which==29){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-564);
							}
							if(which==30){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-573);
							}
							if(which==31){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-579);
							}
							if(which==32){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-583);
							}
							if(which==33){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-597);
							}
							if(which==34){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-605);
							}
							if(which==35){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-613);
							}
							if(which==36){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-620);
							}
							if(which==37){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-630);
							}
							if(which==38){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-637);
							}
							if(which==39){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-649);
							}
							if(which==40){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-661);
							}
							if(which==41){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-670);
							}
							if(which==42){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-679);
							}
							if(which==43){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-688);
							}
							if(which==44){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-693);
							}
							if(which==45){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-699);
							}
							if(which==46){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-706);
							}
							if(which==47){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-712);
							}
							if(which==48){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-718);
							}
							if(which==49){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-723);
							}
							if(which==50){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-728);
							}
							if(which==51){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-731);
							}
							if(which==52){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-734);
							}
							if(which==53){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-738);
							}
							if(which==54){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-742);
							}
							if(which==55){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-747);
							}
							if(which==56){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-752);
							}
							if(which==57){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-759);
							}
							if(which==58){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-763);
							}
							if(which==59){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-768);
							}
							if(which==60){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-772);
							}
							if(which==61){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-775);
							}
							if(which==62){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-777);
							}
							if(which==63){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-779);
							}
							if(which==64){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-782);
							}
							if(which==65){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-785);
							}
							if(which==66){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-789);
							}
							if(which==67){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-792);
							}
							if(which==68){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-796);
							}
							if(which==69){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-799);
							}
							if(which==70){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-802);
							}
							if(which==71){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-805);
							}
							if(which==72){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-808);
							}
							if(which==73){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-810);
							}
							if(which==74){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-813);
							}
							if(which==75){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-815);
							}
							if(which==76){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-818);
							}
							if(which==77){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-821);
							}
							if(which==78){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-822);
							}
							if(which==79){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-824);
							}
							if(which==80){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-826);
							}
							if(which==81){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-827);
							}
							if(which==82){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-828);
							}
							if(which==83){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-830);
							}
							if(which==84){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-831);
							}
							if(which==85){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-832);
							}
							if(which==86){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-833);
							}
							if(which==87){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-834);
							}
							if(which==88){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-835);
							}
							if(which==89){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-837);
							}
							if(which==90){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-838);
							}
							if(which==91){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-839);
							}
							if(which==92){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-840);
							}
							if(which==93){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-840);
							}
							if(which==94){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-841);
							}
							if(which==95){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-841);
							}
							if(which==96){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-842);
							}
							if(which==97){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-842);
							}
							if(which==98){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-843);
							}
							if(which==99){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-844);
							}
							if(which==100){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-845);
							}
							if(which==101){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-845);
							}
							if(which==102){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-846);
							}
							if(which==103){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-846);
							}
							if(which==104){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-846);
							}
							if(which==105){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-847);
							}
							if(which==106){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-847);
							}
							if(which==107){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-848);
							}
							if(which==108){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-848);
							}
							if(which==109){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-848);
							}
							if(which==110){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-849);
							}
							if(which==111){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-849);
							}
							if(which==112){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-849);
							}
							if(which==113){
							 dialog.dismiss();
							
							
							mViewPager.setCurrentItem(850-850);
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
		case R.id.item5:
			Intent i=new Intent(ViewPagerActivity.this,Notepadv3.class);
			
			startActivity(i);
			return true;
		case R.id.item6:
			try {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Qazi+Musab")));
			} catch (android.content.ActivityNotFoundException anfe) {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Qazi+Musab")));
			}
			return true;
		case R.id.item7:
			String appName2="com.qaziconsultancy.13linequran";
			try {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName2)));
			} catch (android.content.ActivityNotFoundException anfe) {
			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName2)));
			}
			return true;
		case R.id.item8:
			mViewPager.setCurrentItem(mViewPager.getCurrentItem()-1);
		return true;
		case R.id.item9:
			mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
		return true;
		case R.id.item10:
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
			
			
			
			 AlertDialog.Builder chooser = new AlertDialog.Builder(ViewPagerActivity.this);
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
		return true;
		}
	}



    static class SamplePagerAdapter extends PagerAdapter {
	
		//private static int[] sDrawables = {  R.drawable.wallpaper,R.drawable.a, R.drawable.b, R.drawable.c,
			//	R.drawable.d, R.drawable.wallpaper };

		@Override
		public int getCount() {
			return 881;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			Drawable d;
			String pathName = Environment.getExternalStorageDirectory().getPath()+"/Pictures/.13linequran/13 line quran/"+(881-position)+".jpg";
			d = Drawable.createFromPath(pathName);
			photoView.setImageDrawable(d);
			if(getScreenOrientation()==Configuration.ORIENTATION_PORTRAIT)
			photoView.setScaleType(ScaleType.FIT_XY);
			

			// Now just add PhotoView to ViewPager and return it
			container.addView(photoView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public int getScreenOrientation()
		{
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
	
	public void saveState(String name) {
        String title = name;
        String body = mViewPager.getCurrentItem()+"";

        
            long id = mDbHelper.createNote(title, body);
            if (id > 0) {
                mRowId = id;
            }
       
    }
	
	@Override
	public void onBackPressed()
	{
		final AlertDialog.Builder alertbox5 = new AlertDialog.Builder(ViewPagerActivity.this);
		alertbox5.setTitle("Rate Us!");
	    alertbox5.setIcon(android.R.drawable.stat_notify_error);
		
        
              alertbox5.setMessage("Please rate us and leave a suggestion so we can enhance this app to your liking! Once you rate, this dialog box won't show again.");
              alertbox5.setNeutralButton("Rate", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface arg0, int arg1) {
            	  editor.putInt("continuePage", mViewPager.getCurrentItem());
 				 editor.commit();
            	  rated=true;
            	  editor.putBoolean("rated", rated);
          		editor.commit();
            	  String appName2="com.qaziconsultancy.thirteenlinequran";
      			try {
      			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName2)));
      			} catch (android.content.ActivityNotFoundException anfe) {
      			    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName2)));
      			}
                }
            });
              alertbox5.setNegativeButton("Exit", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { editor.putInt("continuePage", mViewPager.getCurrentItem());
				 editor.commit();finish();} });
		
			if(!rated)
			alertbox5.show();
			else{
				 editor.putInt("continuePage", mViewPager.getCurrentItem());
				 editor.commit();
			finish();}
		
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
		    // TODO Auto-generated catch block
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		}  
	        return false;
	    	
	    }
	    @Override
	    protected void onPostExecute(Boolean success) {
	        // TODO: Do something more useful here
	       //Toast.makeText(Validate.this, Validate.this.getResources().getConfiguration().locale.getDisplayCountry()+"\n"+code, Toast.LENGTH_LONG).show();
	           
	        
	    }
	}

    @Override
    protected void onPause() {
        editor.putInt("continuePage", mViewPager.getCurrentItem());
        editor.commit();
        super.onPause();
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
