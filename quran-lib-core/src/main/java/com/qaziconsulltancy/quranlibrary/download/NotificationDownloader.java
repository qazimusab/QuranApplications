package com.qaziconsulltancy.quranlibrary.download;

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
import android.widget.Toast;

import com.qaziconsulltancy.quranlibrary.R;
import com.qaziconsulltancy.quranlibrary.curlutils.QuranReaderActivity;
import com.qaziconsulltancy.quranlibrary.util.Titanic;
import com.qaziconsulltancy.quranlibrary.util.TitanicTextView;

import java.io.File;


public class NotificationDownloader extends Activity {
    Intent intent;
    Boolean downloadStarted=false;
    boolean downloadFinished=false;
    Titanic titanic;
    ProgressBar bar;
    TitanicTextView titanicTextView;
    ImageButton btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_activity);
		btn = (ImageButton) findViewById(R.id.btn);
        bar = (ProgressBar)findViewById(R.id.progressBar2);
        titanicTextView=(TitanicTextView)findViewById(R.id.my_text_view);
        titanicTextView.setVisibility(View.INVISIBLE);
        titanic = new Titanic();
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                checkIfImagesExist();
			}
		});

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
                    titanicTextView.setVisibility(View.VISIBLE);
                    titanic.start(titanicTextView);
                    intent = new Intent(NotificationDownloader.this,NotificationDownloadService.class);
                    startService(intent);
                    Toast.makeText(NotificationDownloader.this,"Your download has started",Toast.LENGTH_LONG).show();
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

    private class CkeckDownloadCompleteTast extends AsyncTask<Void, Double, Void> {

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

}
