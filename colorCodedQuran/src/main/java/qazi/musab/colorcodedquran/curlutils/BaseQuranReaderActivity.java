package qazi.musab.colorcodedquran.curlutils;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.analytics.tracking.android.EasyTracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import qazi.musab.colorcodedquran.R;
import qazi.musab.colorcodedquran.bookmarks.BookmarksDbAdapter;
import qazi.musab.colorcodedquran.download.NotificationDownloader;
import qazi.musab.colorcodedquran.util.Contributor;
import qazi.musab.colorcodedquran.util.Dialogs;


/**
 * Created by Musab on 8/20/2014.
 */
@SuppressWarnings("ALL")
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
        connectForContribution();;
        initVariables();
        checkIfSubscribed();
        initializeDatabaseDelper(savedInstanceState);
        findCurrentIndexToStart();
        initCurlView();
//        showFullScreenMessage();
        showDialogForFullScreen();
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

    protected void makeActionBarTransparent() {
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
        getActionBar().setSplitBackgroundDrawable(new ColorDrawable(Color.argb(128, 0, 0, 0)));
    }

    protected void showFullScreenMessage() {
        Toast.makeText(getBaseContext(), "Tap + Long Press = Full Screen",Toast.LENGTH_LONG).show();
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
        File n=new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.TajweedQuran/Tajweed Quran/624.jpg");
        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/TajweedQuran.zip");
        if(file.exists()&&n.exists())
            file.delete();
        if(!n.exists())
        {
            Intent intent =new Intent(getCurlActivity(),NotificationDownloader.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
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

        EasyTracker.getInstance(this).activityStart(this);  // Add this method.
    }

    @Override
    public void onStop() {
        super.onStop();

        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
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
                String appName2="qazi.musab.colorcodedquran";
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
        mCurlView.setActionBar(getActionBar());
        mCurlView.setPageProvider(pageProvider);
        mCurlView.setSizeChangedObserver(sizeChangedObserver);
        mCurlView.setCurrentIndex(zoomIndex);
        mCurlView.setBackgroundColor(0xFF202830);

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
                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.TajweedQuran/Tajweed Quran/"+(624-mCurlView.getCurrentIndex())+".jpg");
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
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/Pictures/.TajweedQuran/Tajweed Quran/"+(624-mCurlView.getCurrentIndex())+".jpg");
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
        view.setOnTouchListener(new PanAndZoomListener(view, imageView, 1, getActionBar()));

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

