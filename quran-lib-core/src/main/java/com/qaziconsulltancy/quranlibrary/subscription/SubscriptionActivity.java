package com.qaziconsulltancy.quranlibrary.subscription;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.qaziconsulltancy.quranlibrary.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class SubscriptionActivity extends Activity {
	TextView email;
	Button subscribe;
	Button verify;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
    Random random;
    TelephonyManager tm; 
    int code;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_subscription);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		email=(TextView)findViewById(R.id.email);
		subscribe=(Button)findViewById(R.id.subscribe);
		verify=(Button)findViewById(R.id.verify);
		random = new Random();
		if(sharedPref.getInt("code", -1)==-1){
		code = random.nextInt(10000)+10000;
		editor.putInt("code", code);
		editor.commit();}
		else code=sharedPref.getInt("code", -1);
		
		subscribe.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!email.getText().toString().contains("@")||!email.getText().toString().contains(".")||email.getText().toString().equals(""))
				{
					final AlertDialog.Builder alertbox = new AlertDialog.Builder(SubscriptionActivity.this);
					alertbox.setTitle("Incorrect Email Address");
				    alertbox.setIcon(android.R.drawable.stat_notify_error);
					
			        
			              alertbox.setMessage("You have entered an invalid email address. Please enter a valid email address.");
			              alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			              public void onClick(DialogInterface arg0, int arg1) {
			            	 
			                }
			            });
			              alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { } });
			              alertbox.show();
				}
				else if(!internetIsConnected(SubscriptionActivity.this))
				{
					final AlertDialog.Builder alertbox = new AlertDialog.Builder(SubscriptionActivity.this);
					alertbox.setTitle("No Network Connection");
				    alertbox.setIcon(android.R.drawable.stat_notify_error);
					
			        
			              alertbox.setMessage("You are not connected to the internet. Please check your connection.");
			              alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			              public void onClick(DialogInterface arg0, int arg1) {
			            	 
			                }
			            });
			              alertbox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { } });
			              alertbox.show();
				}
				else{String to = email.getText().toString();
                String subject = "Validation Code";
                String message = "Your validation code is: "+code;
                editor.putString("email", email.getText().toString());
        		editor.commit();
        		new SendEmailTask().execute();
                //sendMail(to, subject, message);
				}
			}
			
		});
		verify.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(sharedPref.getString("email", "").equals(""))
				{
					final AlertDialog.Builder alertbox5 = new AlertDialog.Builder(SubscriptionActivity.this);
					alertbox5.setTitle("No email sent");
				    alertbox5.setIcon(android.R.drawable.stat_notify_error);
					
			        
			              alertbox5.setMessage("You have not yet sent a validation code that you can verify. Please type your email in and send a validation code.");
			              alertbox5.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			              public void onClick(DialogInterface arg0, int arg1) {
			            	 
			                }
			            });
			              alertbox5.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { } });
			              alertbox5.show();
				}
				else{
				Intent intent = new Intent(SubscriptionActivity.this,Validate.class);
	            intent.putExtra("code", code);
	            intent.putExtra("email", sharedPref.getString("email", ""));
	            startActivity(intent);
				}
			}
			
		});
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

   
    public class SendEmailTask extends AsyncTask<Void, Void, Boolean> {

	    String responseString;
	    HttpResponse response;
	    private ProgressDialog progressDialog;

	    @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SubscriptionActivity.this, "Please wait", "Sending mail", true, false);
        }
	    
	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	try {
	    	HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost2 = new HttpPost("http://www.qaziconsultancy.com/php/upload.php");

		
		    // Add your data
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
		    nameValuePairs.add(new BasicNameValuePair("email", email.getText().toString()));
		    nameValuePairs.add(new BasicNameValuePair("verify", "false"));
		    nameValuePairs.add(new BasicNameValuePair("package", "Arab Quran"));
		    nameValuePairs.add(new BasicNameValuePair("code", code+""));
		    if(SubscriptionActivity.this.getResources().getConfiguration().locale.getDisplayCountry().contains("?"))
		    nameValuePairs.add(new BasicNameValuePair("country", tm.getSimCountryIso()));
		    else
		    nameValuePairs.add(new BasicNameValuePair("country", SubscriptionActivity.this.getResources().getConfiguration().locale.getDisplayCountry()));
		    httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		    // Execute HTTP Post Request
		     response = httpclient.execute(httppost2);

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
	    	 progressDialog.dismiss();
	            Intent intent = new Intent(SubscriptionActivity.this,Validate.class);
	            intent.putExtra("code", code);
	            intent.putExtra("email", email.getText().toString());
	            startActivity(intent);
	            
	        
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
