package qazi.musab.arabquran.curlutils;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.InputFilter;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.analytics.tracking.android.EasyTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import qazi.musab.arabquran.R;
import qazi.musab.arabquran.bookmarks.BookmarksActivity;
import qazi.musab.arabquran.bookmarks.BookmarksDbAdapter;
import qazi.musab.arabquran.download.NotificationDownloader;
import qazi.musab.arabquran.subscription.YearlySubscriptionActivity;
import qazi.musab.arabquran.util.Contributor;
import qazi.musab.arabquran.util.Dialogs;
import qazi.musab.arabquran.util.Encryter;

/**
 * Created by Musab on 8/20/2014.
 */
public abstract class BaseQuranReaderActivity extends Activity {
    protected IInAppBillingService mservice;
    protected ServiceConnection connection;
    protected Contributor contributor;
    protected SharedPreferences sharedPref;
    protected SharedPreferences.Editor editor;
    protected CurlView mCurlView;
    protected SizeChangedObserver sizeChangedObserver;
    protected Dialogs dialogs;
    protected int index;
    protected Boolean ran;
    protected PageProvider pageProvider;
    protected Boolean rated;
    protected boolean isZoomed;
    protected int zoomIndex;
    protected int code;
    protected String email;
    protected Boolean subscribed;
    protected BookmarksDbAdapter mDbHelper;
    protected Long mRowId;
    protected ListView listView;
    protected Handler mHandler;
    protected CheckBox dontShowAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowFeatures();
        setContentView(R.layout.quran_reader);
        makeActionBarTransparent();
        connectForContribution();
        initVariables();
        checkIfSubscribed();
        initializeDatabaseDelper(savedInstanceState);
        findCurrentIndexToStart();
        initCurlView();
        showDialogForFullScreen();
    }

    protected void checkIfTrialFinished() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/.ArabQuranBlue/Arab Quran Blue/config.txt");
        String line = null;
        Date startDate = null;
        Date todayDate = new Date();
        int daysdiff=0;
        if(!file.exists()) {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date date = new Date();
            Writer writer = null;

            try {
                String text = dateFormat.format(date) + "";
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(Encryter.encode(text));
                Toast.makeText(this,"30 day free trail started.",Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            try{

                //Create object of FileReader
                FileReader inputFile = new FileReader(file);

                //Instantiate the BufferedReader Class
                BufferedReader bufferReader = new BufferedReader(inputFile);

                //Variable to hold the one line data


                // Read file line by line and print on the console
                line = Encryter.decode(bufferReader.readLine());
                //Close the buffer reader
                bufferReader.close();
            }catch(Exception e){
                System.out.println("Error while reading file line by line:"
                        + e.getMessage());
            }
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            try {
                Log.e("date",line);
                startDate =  df.parse(line);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = startDate.getTime() - todayDate.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            daysdiff = (int) Math.abs(diffDays);
            Log.e("dif",daysdiff+"");
            Toast.makeText(this,(daysdiff)+" days used",Toast.LENGTH_LONG).show();
            if(daysdiff>=30){
                Intent intent = new Intent(getCurlActivity(), YearlySubscriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

    protected void showDialogForFullScreen() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        LayoutInflater adbInflater = LayoutInflater.from(this);
        View eulaLayout = adbInflater.inflate(R.layout.checkbox, null);
        dontShowAgain = (CheckBox) eulaLayout.findViewById(R.id.skip);
        adb.setView(eulaLayout);
        adb.setTitle("Hint");

        adb.setMessage("Double Tap + Long Press = Full Screen");
        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";
                if (dontShowAgain.isChecked())
                    checkBoxResult = "checked";
                editor.putString("skipMessage", checkBoxResult);
                editor.commit();
            }
        });

        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String checkBoxResult = "NOT checked";
                if (dontShowAgain.isChecked())
                    checkBoxResult = "checked";
                editor.putString("skipMessage", checkBoxResult);
                editor.commit();
            }
        });
        String skipMessage = sharedPref.getString("skipMessage", "NOT checked");
        if (!skipMessage.equals("checked"))
            adb.show();
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void makeActionBarTransparent() {
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
        getActionBar().setSplitBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
    }

    public void showFullScreenMessage() {
        Toast.makeText(getBaseContext(), "Double Tap + Long Press = Full Screen",Toast.LENGTH_LONG).show();
    }

    protected void initCurlView() {
        mCurlView = (CurlView) findViewById(R.id.curl);
        sizeChangedObserver=new SizeChangedObserver(mCurlView);
        mCurlView.setActionBar(getActionBar());
        mCurlView.setPageProvider(pageProvider);
        mCurlView.setSizeChangedObserver(sizeChangedObserver);
        mCurlView.setCurrentIndex(index);
        mCurlView.setBackgroundColor(0xFF202830);
        dialogs= new Dialogs(mCurlView,(QuranReaderActivity)getCurlActivity());
    }

    protected void initVariables() {
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        isZoomed=false;
        zoomIndex =0;
        getOverflowMenu();
        pageProvider=new PageProvider();
        rated=false;
        rated= sharedPref.getBoolean("rated", false);
    }

    protected void findCurrentIndexToStart() {
        if (getLastNonConfigurationInstance() != null) {
            index = (Integer) getLastNonConfigurationInstance();
        }
        String page="";
        index=pageProvider.getPageCount()-1;
        if(sharedPref.getInt("continuePage", -1)!=-1)
        {
            index=(sharedPref.getInt("continuePage", -1));
        }
        try{
            Intent intent = getIntent();
            page=intent.getStringExtra("PAGE");
            index=Integer.parseInt(page);
        }
        catch(NullPointerException d){
            d.printStackTrace();
        }
        catch(RuntimeException e)
        {
            e.printStackTrace();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void initializeDatabaseDelper(Bundle savedInstanceState) {
        mDbHelper = new BookmarksDbAdapter(this);
        mDbHelper.open();
        mRowId = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(BookmarksDbAdapter.KEY_ROWID);
        if (mRowId == null) {
            Bundle extras = getIntent().getExtras();
            mRowId = extras != null ? extras.getLong(BookmarksDbAdapter.KEY_ROWID)
                    : null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    protected abstract int getPagesOfQuranNotIncludingIndex();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item1:
                String[] pages=new String [getPagesOfQuranNotIncludingIndex()];
                for(int i=0;i< getPagesOfQuranNotIncludingIndex();i++)
                {
                    pages[i]=i+1+"";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getCurlActivity());
                builder
                        .setTitle("Go To Page")
                        .setItems(pages, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                exitZoom(); dialog.dismiss();
                                mCurlView.setPageProvider(pageProvider);
                                mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                mCurlView.setCurrentIndex(pageProvider.getPageCount()-4-which);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                dialog.show();
                return true;
            case R.id.item2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getCurlActivity());
                String[] juz=getResources().getStringArray(R.array.juz);
                builder2
                        .setTitle("Go To Juz (Parah)")
                        .setItems(juz, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-1);

                                }
                                if(which==1){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-22);

                                }
                                if(which==2){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-42);

                                }
                                if(which==3){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-62);

                                }
                                if(which==4){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-82);

                                }
                                if(which==5){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-102);

                                }
                                if(which==6){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-121);

                                }
                                if(which==7){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-142);

                                }
                                if(which==8){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-162);

                                }
                                if(which==9){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-182);

                                }
                                if(which==10){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-202);

                                }
                                if(which==11){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-222);

                                }
                                if(which==12){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-242);

                                }
                                if(which==13){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-262);

                                }
                                if(which==14){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-282);

                                }
                                if(which==15){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-301);

                                }
                                if(which==16){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-322);

                                }
                                if(which==17){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-341);

                                }
                                if(which==18){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-362);

                                }
                                if(which==19){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-382);

                                }
                                if(which==20){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-402);

                                }
                                if(which==21){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-422);

                                }
                                if(which==22){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-442);

                                }
                                if(which==23){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-462);

                                }
                                if(which==24){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-482);

                                }
                                if(which==25){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-502);

                                }
                                if(which==26){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-522);

                                }
                                if(which==27){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-542);

                                }
                                if(which==28){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-562);

                                }
                                if(which==29){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-582);

                                }
                            }
                        });
                AlertDialog dialog2 = builder2.create();
                dialog2.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                dialog2.show();
                return true;
            case R.id.item3:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(getCurlActivity());
                String[] surah=getResources().getStringArray(R.array.surah);
                builder3
                        .setTitle("Go To Surah")
                        .setItems(surah, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-1);
                                }
                                if(which==1){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-2);
                                }
                                if(which==2){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-50);
                                }
                                if(which==3){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-77);
                                }
                                if(which==4){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-106);
                                }
                                if(which==5){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-128);
                                }
                                if(which==6){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-151);
                                }
                                if(which==7){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-177);
                                }
                                if(which==8){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-187);
                                }
                                if(which==9){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-208);
                                }
                                if(which==10){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-221);
                                }
                                if(which==11){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-235);
                                }
                                if(which==12){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-249);
                                }
                                if(which==13){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-255);
                                }
                                if(which==14){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-262);
                                }
                                if(which==15){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-267);
                                }
                                if(which==16){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-282);
                                }
                                if(which==17){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-293);
                                }
                                if(which==18){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-305);
                                }
                                if(which==19){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-312);
                                }
                                if(which==20){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-322);
                                }
                                if(which==21){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-332);
                                }
                                if(which==22){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-342);
                                }
                                if(which==23){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-350);
                                }
                                if(which==24){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-359);
                                }
                                if(which==25){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-367);
                                }
                                if(which==26){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-377);
                                }
                                if(which==27){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-385);
                                }
                                if(which==28){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-396);
                                }
                                if(which==29){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-404);
                                }
                                if(which==30){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-411);
                                }
                                if(which==31){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-415);
                                }
                                if(which==32){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-418);
                                }
                                if(which==33){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-428);
                                }
                                if(which==34){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-434);
                                }
                                if(which==35){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-440);
                                }
                                if(which==36){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-446);
                                }
                                if(which==37){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-453);
                                }
                                if(which==38){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-458);
                                }
                                if(which==39){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-467);
                                }
                                if(which==40){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-477);
                                }
                                if(which==41){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-483);
                                }
                                if(which==42){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-489);
                                }
                                if(which==43){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-496);
                                }
                                if(which==44){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-499);
                                }
                                if(which==45){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-502);
                                }
                                if(which==46){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-508);
                                }
                                if(which==47){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-511);
                                }
                                if(which==48){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-515);
                                }
                                if(which==49){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-518);
                                }
                                if(which==50){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-520);
                                }
                                if(which==51){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-523);
                                }
                                if(which==52){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-526);
                                }
                                if(which==53){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-528);
                                }
                                if(which==54){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-531);
                                }
                                if(which==55){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-534);
                                }
                                if(which==56){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-537);
                                }
                                if(which==57){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-542);
                                }
                                if(which==58){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-545);
                                }
                                if(which==59){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-549);
                                }
                                if(which==60){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-551);
                                }
                                if(which==61){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-553);
                                }
                                if(which==62){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-554);
                                }
                                if(which==63){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-556);
                                }
                                if(which==64){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-558);
                                }
                                if(which==65){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-560);
                                }
                                if(which==66){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-562);
                                }
                                if(which==67){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-564);
                                }
                                if(which==68){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-566);
                                }
                                if(which==69){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-568);
                                }
                                if(which==70){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-570);
                                }
                                if(which==71){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-572);
                                }
                                if(which==72){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-574);
                                }
                                if(which==73){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-575);
                                }
                                if(which==74){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-577);
                                }
                                if(which==75){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-578);
                                }
                                if(which==76){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-580);
                                }
                                if(which==77){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-582);
                                }
                                if(which==78){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-583);
                                }
                                if(which==79){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-585);
                                }
                                if(which==80){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-586);
                                }
                                if(which==81){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-587);
                                }
                                if(which==82){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-587);
                                }
                                if(which==83){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-589);
                                }
                                if(which==84){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-590);
                                }
                                if(which==85){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-591);
                                }
                                if(which==86){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-591);
                                }
                                if(which==87){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-592);
                                }
                                if(which==88){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-593);
                                }
                                if(which==89){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-594);
                                }
                                if(which==90){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-595);
                                }
                                if(which==91){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-595);
                                }
                                if(which==92){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-596);
                                }
                                if(which==93){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-596);
                                }
                                if(which==94){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-597);
                                }
                                if(which==95){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-597);
                                }
                                if(which==96){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-598);
                                }
                                if(which==97){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-598);
                                }
                                if(which==98){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-599);
                                }
                                if(which==99){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-599);
                                }
                                if(which==100){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-600);
                                }
                                if(which==101){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-600);
                                }
                                if(which==102){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-601);
                                }
                                if(which==103){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-601);
                                }
                                if(which==104){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-601);
                                }
                                if(which==105){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-602);
                                }
                                if(which==106){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-602);
                                }
                                if(which==107){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-602);
                                }
                                if(which==108){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-603);
                                }
                                if(which==109){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-603);
                                }
                                if(which==110){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-603);
                                }
                                if(which==111){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-604);
                                }
                                if(which==112){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-604);
                                }
                                if(which==113){
                                    exitZoom(); dialog.dismiss();
                                    mCurlView.setPageProvider(pageProvider);
                                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                                    mCurlView.setCurrentIndex(pageProvider.getPageCount()-3-604);
                                }
                            }
                        });
                AlertDialog dialog3 = builder3.create();
                dialog3.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                dialog3.show();
                return true;
            case R.id.item4:
                try{
                    zoom();
                    Toast.makeText(getCurlActivity(), "Pinch to zoom and press the back button to navigate again.", Toast.LENGTH_SHORT).show();
                }

                catch(NoClassDefFoundError b)
                {
                    Toast.makeText(getCurlActivity(), "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG ).show();
                }
                catch(Exception b)
                {
                    Toast.makeText(getCurlActivity(), "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG ).show();
                }
                return true;
            case R.id.item5:
                AlertDialog.Builder builderr = new AlertDialog.Builder(this);
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
                Intent i=new Intent(getCurlActivity(),BookmarksActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_up,android.R.anim.fade_out);
                return true;
            case R.id.item7:
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:Qazi+Musab")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/developer?id=Qazi+Musab")));
                }
                return true;
            case R.id.item8:
                String appName2="qazi.musab.arabquran";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+appName2)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName2)));
                }
                return true;
            /*case R.id.item9:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.gomedina.com/advertisement")));
                return true;*/
            case R.id.item10:
                if(!isZoomed)
                    mCurlView.setCurrentIndex(mCurlView.getCurrentIndex()-1);
                return true;
            case R.id.item11:
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



                AlertDialog.Builder chooser = new AlertDialog.Builder(getCurlActivity());
                chooser
                        .setTitle("Contribute")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch(which) {
                                    case 0:
                                        contributor.contributeOneDollar();
                                        break;
                                    case 1:
                                        contributor.contributetwoDollars();
                                        break;
                                    case 2:
                                        contributor.contributeThreeDollars();
                                        break;
                                    case 3:
                                        contributor.contributeFourDollars();
                                        break;
                                    case 4:
                                        contributor.contributeFiveDollars();
                                        break;
                                    case 5:
                                        contributor.contributeTenDollars();
                                        break;
                                    case 6:
                                        contributor.contributeTwentyDollars();
                                        break;
                                    case 7:
                                        contributor.contributeFiftyDollars();
                                        break;
                                    case 8:
                                        contributor.contributeHundredDollars();
                                        break;
                                }
                            }
                        });
                AlertDialog myChooser = chooser.create();
                myChooser.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                myChooser.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void connectForContribution() {
        connection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mservice = null;

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mservice = IInAppBillingService.Stub.asInterface(service);
                contributor=new Contributor(mservice,BaseQuranReaderActivity.this);
                checkIfImagesExist();
            }
        };

        bindService(new Intent(
                        "com.android.vending.billing.InAppBillingService.BIND"),
                connection, Context.BIND_AUTO_CREATE);
    }

    protected void checkIfSubscribed() {
        //subscribed=sharedPref.getBoolean("subscribed", false);
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
			Intent suscribe= new Intent(getCurlActivity(),SubscriptionActivity.class);
			suscribe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(suscribe);
			finish();
		}*/
    }

    protected void checkIfImagesExist() {
        File n=new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.ArabQuranBlue/Arab Quran Blue/624.jpg");
        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/ArabQuranBlue.zip");
        if(file.exists()&&n.exists())
            file.delete();
        if(!n.exists())
        {
            Intent intent =new Intent(getCurlActivity(),NotificationDownloader.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else{
            if(!contributor.subscriptionExists("com.qaziconsultancy.onebuckyearly")){
                checkIfTrialFinished();
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


    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(BaseQuranReaderActivity.this);  // Add this method.
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(BaseQuranReaderActivity.this);  // Add this method.
    }


    protected int getScreenOrientation()
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

    protected void getOverflowMenu() {

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

    protected int getResId(CurlView mCurlView,ImageView imageView, String name) {

        try {
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
    public void onPause() {
        editor.putInt("continuePage", mCurlView.getCurrentIndex());
        editor.commit();
        super.onPause();
        mCurlView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCurlView.onResume();
    }

    @Override
    public void onBackPressed()
    {
        final AlertDialog.Builder alertbox5 = new AlertDialog.Builder(getCurlActivity());
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
                String appName2="qazi.musab.arabquran";
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appName2)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+appName2)));
                }
            }
        });
        alertbox5.setNegativeButton("Exit", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int which) { editor.putInt("continuePage", mCurlView.getCurrentIndex());
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
                finish();}
        }
    }

    public void exitZoom()
    {
        setContentView(R.layout.quran_reader);
        if (getLastNonConfigurationInstance() != null) {
            index = (Integer) getLastNonConfigurationInstance();
        }
        mCurlView = (CurlView) findViewById(R.id.curl);
        mCurlView.setPageProvider(pageProvider);
        mCurlView.setSizeChangedObserver(sizeChangedObserver);
        mCurlView.setCurrentIndex(zoomIndex);
        mCurlView.setBackgroundColor(0xFF202830);
        mCurlView.setActionBar(getActionBar());

        isZoomed=false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isZoomed",isZoomed);
        outState.putInt("index",index);
        outState.putInt("zoomIndex", zoomIndex);
    }
    //Restore instance state
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        isZoomed=savedInstanceState.getBoolean("isZoomed");
        index=savedInstanceState.getInt("index");
        zoomIndex =savedInstanceState.getInt("zoomIndex");
        if(isZoomed&&getScreenOrientation()==Configuration.ORIENTATION_PORTRAIT)
        {
            try{
                FrameLayout view = new FrameLayout(this);


                FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.TOP | Gravity.LEFT);

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT));
                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.ArabQuranBlue/Arab Quran Blue/"+(624-mCurlView.getCurrentIndex())+".jpg");
                Uri uri = Uri.fromFile(file);
                imageView.setImageURI(uri);
                zoomIndex =(mCurlView.getCurrentIndex());


                //Use line below for large images if you have hardware rendering turned on
                //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                // Line below is optional, depending on what scaling method you want
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                view.addView(imageView, fp);
                view.setOnTouchListener(new PanAndZoomListener(view, imageView, 1, getActionBar()));
                setContentView(view);
                view.setVisibility(View.VISIBLE);
                isZoomed=true;
            }
            catch(NoClassDefFoundError b)
            {
                Toast.makeText(getCurlActivity(), "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG).show();
            }
            catch(Exception b)
            {
                Toast.makeText(getCurlActivity(), "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG).show();
            }
        }
        else if(isZoomed&&getScreenOrientation()==Configuration.ORIENTATION_LANDSCAPE)
        {
            try{
                zoom();
            }
            catch(NoClassDefFoundError b)
            {
                Toast.makeText(getCurlActivity(), "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG ).show();
            }
            catch(Exception b)
            {
                Toast.makeText(getCurlActivity(), "Sorry, but your phone does not have the capability for this feature.", Toast.LENGTH_LONG ).show();
            }
        }

    }

    public void zoom()
    {

        FrameLayout view = new FrameLayout(getCurlActivity());

        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.TOP | Gravity.LEFT);

        ImageView imageView = new ImageView(getCurlActivity());
        imageView.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.FILL_PARENT));
        //String res="R.drawable.page"+mCurlView.getCurrentIndex();
        //imageView.setImageResource(getResId("page"+mCurlView.getCurrentIndex()+1,Drawable.class));
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.ArabQuranBlue/Arab Quran Blue/"+(624-mCurlView.getCurrentIndex())+".jpg");
        Uri uri = Uri.fromFile(file);
        imageView.setImageURI(uri);
	       /* if(getResId(mCurlView,imageView,"page"+(608-mCurlView.getCurrentIndex()+1))!=-1)
	            imageView.setImageResource(getResId(mCurlView,imageView,"page"+(608-mCurlView.getCurrentIndex()+1)));
	        else
	        	 imageView.setImageResource(getResId(mCurlView,imageView,"coverpage"));*/
        zoomIndex =(mCurlView.getCurrentIndex());

        //Use line below for large images if you have hardware rendering turned on
        //imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // Line below is optional, depending on what scaling method you want

        if(getScreenOrientation()==Configuration.ORIENTATION_PORTRAIT)
        {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);}
        else
        {}
        view.addView(imageView, fp);
        view.setOnTouchListener(new PanAndZoomListener(view, imageView, 1,getActionBar()));
        setContentView(view);
        view.setVisibility(View.VISIBLE);
        isZoomed=true;

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
                        e.printStackTrace();
                    } catch (Exception e) {
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
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
                catch (JSONException e) {

                    e.printStackTrace();
                }
            }

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
                                e.printStackTrace();
                            } catch (Exception e) {
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
    public Object onRetainNonConfigurationInstance() {
        return mCurlView.getCurrentIndex();
    }

    protected void startAnimator(int id,View view){
        ObjectAnimator anim = (ObjectAnimator) AnimatorInflater.loadAnimator(getApplicationContext(), id);
        anim.setTarget(view);
        anim.setDuration(3000);
        anim.start();
    }

    protected abstract Activity getCurlActivity();
}
