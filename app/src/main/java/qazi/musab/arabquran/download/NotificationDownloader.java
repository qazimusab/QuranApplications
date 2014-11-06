package qazi.musab.arabquran.download;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.File;

import qazi.musab.arabquran.R;
import qazi.musab.arabquran.curlutils.QuranReaderActivity;

public class NotificationDownloader extends Activity {
    Intent intent;
    Boolean downloadStarted=false;
    boolean downloadFinished=false;
    ProgressBar bar;
    ImageButton btn;
    TextView message;
    public static Bus bus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_activity);
		btn = (ImageButton) findViewById(R.id.btn);
        message=(TextView)findViewById(R.id.textView2);
        bar = (ProgressBar)findViewById(R.id.progressBar2);
        bar.setVisibility(View.INVISIBLE);
        bus = new Bus();
        bus.register(this);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                checkIfImagesExist();
			}
		});

	}

    @Subscribe
    public void onDownloadCompleteEvent(DownloadCompleteEvent event){
        Toast.makeText(this,"Download Complete",Toast.LENGTH_LONG).show();
        Intent startReciting = new Intent(NotificationDownloader.this, QuranReaderActivity.class);
        startReciting.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startReciting);
        overridePendingTransition(R.anim.slide_up,android.R.anim.fade_out);
        finish();
    }

    protected void checkIfImagesExist() {
        File n=new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.ArabQuranBlue/Arab Quran Blue/624.jpg");
        if(n.exists())
        {
            Intent intent =new Intent(NotificationDownloader.this, QuranReaderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_up,android.R.anim.fade_out);
        }
        else{
            final AlertDialog.Builder alertbox5 = new AlertDialog.Builder(NotificationDownloader.this);
            alertbox5.setTitle("Download");
            alertbox5.setCancelable(false);
            alertbox5.setIcon(android.R.drawable.stat_notify_error);


            alertbox5.setMessage("To start reciting the Quran, you will need to download some files from the internet. Please make sure you are connected to Wi-Fi and that you have 130mb of free space before you start. Would you like to start the download now?");
            alertbox5.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    downloadStarted=true;
                    btn.setVisibility(View.INVISIBLE);
                    bar.setVisibility(View.VISIBLE);
                    intent = new Intent(NotificationDownloader.this,NotificationDownloadService.class);
                    startService(intent);
                    Toast.makeText(NotificationDownloader.this,"Your download has started! You may exit this application while the download finishes. Please refer to the notification for the status of your download.",Toast.LENGTH_LONG).show();
                    message.setText("Please refer to the notification for actual download status. You may also exit the app as the download will continue in the background.");
                }
            });
            alertbox5.setNegativeButton("Later", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) {} });
            if(!downloadStarted)
                alertbox5.show();
            else
                Toast.makeText(NotificationDownloader.this,"Your download is already in progress. Please be patient.",Toast.LENGTH_LONG).show();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private class CheckDownloadCompleteTask extends AsyncTask<Void, Double, Void> {

        // Executed on main UI thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {
            while(!downloadFinished){
                File n=new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.ArabQuranBlue/Arab Quran Blue/624.jpg");
                File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/ArabQuranBlue.zip");
                if(!file.exists()&&n.exists()) {
                    downloadFinished = true;
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        // Executed on main UI thread
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            bar.setVisibility(View.GONE);
        }


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
