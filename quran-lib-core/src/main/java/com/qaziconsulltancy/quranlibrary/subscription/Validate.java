package com.qaziconsulltancy.quranlibrary.subscription;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.qaziconsulltancy.quranlibrary.R;
import com.qaziconsulltancy.quranlibrary.curlutils.QuranReaderActivity;

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


public class Validate extends Activity {
	TextView val;
	TextView tv;
	Button begin;
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	String email;
	boolean submitted;
	int code;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validation);
		sharedPref = this.getPreferences(Context.MODE_PRIVATE);
		editor = sharedPref.edit();
		final Intent me =getIntent();
		code=me.getIntExtra("code", -1);
		email=me.getStringExtra("email");
		editor.putString("email", email);
		editor.commit();
		email=sharedPref.getString("email", "");
		val=(TextView)findViewById(R.id.val);
		tv=(TextView)findViewById(R.id.textView1);
		begin=(Button)findViewById(R.id.begin);
		tv.setText("You have been sent an email with a validation code to validate:\n "+email+"\n Please type that code below and begin reciting!\n");
		begin.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final AlertDialog.Builder alertbox5 = new AlertDialog.Builder(Validate.this);
				alertbox5.setTitle("Wrong Code");
			    alertbox5.setIcon(android.R.drawable.stat_notify_error);
				
		        
		              alertbox5.setMessage("You entered the wrong code. If you would like to recieve your validation code again, click resend.");
		              alertbox5.setNeutralButton("Resend", new DialogInterface.OnClickListener() {
		              public void onClick(DialogInterface arg0, int arg1) {
		            	  Intent suscribe= new Intent(Validate.this,SubscriptionActivity.class);
		      			suscribe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		      			startActivity(suscribe);
		      			finish();
		                }
		            });
		              alertbox5.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { } });
		              if(val.getText().toString().equals("")||val.getText().toString().contains(".")||val.getText().toString().contains("-")||val.getText().toString().length()>8)
				{
					alertbox5.show();
				}
				else if(Integer.parseInt(val.getText().toString())==code){
					new SendTask().execute();
					//Send();
					/*Intent intent = new Intent(Validate.this,ViewPagerActivity.class);		            
		            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		            intent.putExtra("validated", true);
		            startActivity(intent);
		            finish();*/
				}
				else
				{
					
			              alertbox5.show();
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
	
	public class SendTask extends AsyncTask<Void, Void, Boolean> {

	    String responseString;
	    HttpResponse response;

	    @Override
	    protected Boolean doInBackground(Void... params) {
	    	if(!internetIsConnected(Validate.this))
			{
	    		submitted=false;
				return false;
			}
	    	else {try {
	    	HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://www.qaziconsultancy.com/php/verify.php");

		
		    // Add your data
		    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		    nameValuePairs.add(new BasicNameValuePair("email", email));
		   // nameValuePairs.add(new BasicNameValuePair("verify", "true"));
		    nameValuePairs.add(new BasicNameValuePair("code", code+""));
		   // nameValuePairs.add(new BasicNameValuePair("country", Validate.this.getResources().getConfiguration().locale.getDisplayCountry()));
		    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		    // Execute HTTP Post Request
		     response = httpclient.execute(httppost);
		     submitted=true;

		} catch (ClientProtocolException e) {
		    // TODO Auto-generated catch block
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		}  
	        return false;
	    	}
	    }
	    @Override
	    protected void onPostExecute(Boolean success) {
	        // TODO: Do something more useful here
	       //Toast.makeText(Validate.this, Validate.this.getResources().getConfiguration().locale.getDisplayCountry()+"\n"+code, Toast.LENGTH_LONG).show();
	            Intent intent = new Intent(Validate.this,QuranReaderActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	            intent.putExtra("validated", true);
	            if(!submitted){
	            intent.putExtra("code", code);
	            intent.putExtra("email", email);
	            }
	            intent.putExtra("submitted", submitted);
	            startActivity(intent);
	            finish();
	        
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
