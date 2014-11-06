package qazi.musab.desiquran.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Musab on 8/20/2014.
 */
public class Contributor {
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
    String oneDollarYearly = "com.qaziconsultancy.onedollaryearly";
    String twoDollarYearly = "com.qaziconsultancy.twodollarsyearly";
    String oneBuckYearly = "com.qaziconsultancy.onebuckyearly";
    Context context;
    IInAppBillingService mservice;
    Activity activity;
    String packageName;

    public Contributor(IInAppBillingService mservice,Activity activity) {
        this.activity=activity;
        this.context=activity.getApplicationContext();
        this.mservice=mservice;
        this.packageName=activity.getPackageName();
    }

    public boolean subscriptionExists(String subName){
        try{
            Bundle ownedItems=getActiveSubs();
            ArrayList ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
            ArrayList purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
            for(int i=0;i<ownedSkus.size();i++){
                if(ownedSkus.get(i).toString().equals(subName)){
                    return true;
                }
            }
            return false;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void contributeOneDollarYearly()
    {
        new Thread(new Runnable(){
            public void run(){
                ArrayList skuList = new ArrayList();
                skuList.add(oneDollarYearly);
                Bundle querySkus = new Bundle();
                querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
                final Bundle skuDetails;
                try {

                    skuDetails = mservice.getSkuDetails(3, packageName,
                            "subs", querySkus);



                    int response = skuDetails.getInt("RESPONSE_CODE");
                    if (response == 0) {

                        ArrayList<String> responseList = skuDetails
                                .getStringArrayList("DETAILS_LIST");

                        for (String thisResponse : responseList) {
                            JSONObject object = new JSONObject(thisResponse);
                            String sku = object.getString("productId");
                            String price = object.getString("price");
                            if (sku.equals(oneDollarYearly)) {
                                System.out.println("price " + price);
                                Bundle buyIntentBundle = mservice
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void contributeOneBuckYearly()
    {
        new Thread(new Runnable(){
            public void run(){
                ArrayList skuList = new ArrayList();
                skuList.add(oneBuckYearly);
                Bundle querySkus = new Bundle();
                querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
                final Bundle skuDetails;
                try {

                    skuDetails = mservice.getSkuDetails(3, packageName,
                            "subs", querySkus);



                    int response = skuDetails.getInt("RESPONSE_CODE");
                    if (response == 0) {

                        ArrayList<String> responseList = skuDetails
                                .getStringArrayList("DETAILS_LIST");

                        for (String thisResponse : responseList) {
                            JSONObject object = new JSONObject(thisResponse);
                            String sku = object.getString("productId");
                            String price = object.getString("price");
                            if (sku.equals(oneBuckYearly)) {
                                System.out.println("price " + price);
                                Bundle buyIntentBundle = mservice
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void contributeTwoDollarsYearly()
    {
        new Thread(new Runnable(){
            public void run(){
                ArrayList skuList = new ArrayList();
                skuList.add(twoDollarYearly);
                Bundle querySkus = new Bundle();
                querySkus.putStringArrayList("ITEM_ID_LIST", skuList);
                final Bundle skuDetails;
                try {

                    skuDetails = mservice.getSkuDetails(3, packageName,
                            "subs", querySkus);



                    int response = skuDetails.getInt("RESPONSE_CODE");
                    if (response == 0) {

                        ArrayList<String> responseList = skuDetails
                                .getStringArrayList("DETAILS_LIST");

                        for (String thisResponse : responseList) {
                            JSONObject object = new JSONObject(thisResponse);
                            String sku = object.getString("productId");
                            String price = object.getString("price");
                            if (sku.equals(twoDollarYearly)) {
                                System.out.println("price " + price);
                                Bundle buyIntentBundle = mservice
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public Bundle getActiveSubs()  {
        Bundle activeSubs = null;
        try {
            activeSubs= new GetActiveSubsBundle().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return activeSubs;
    }

    public void checkOwnedItems()
    {
        try
        {
            Bundle ownedItems = mservice.getPurchases(3, packageName, "subs", null);

            if(ownedItems.getInt("RESPONSE_CODE") == 0)
            {
                ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String> signatureList = ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
                String continuationToken = ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                if(purchaseDataList.size() > 0)
                {
                    //Item(s) owned

                    for(int i=0; i<purchaseDataList.size(); ++i)
                    {
                        String purchaseData = purchaseDataList.get(i);
                        String sku = ownedSkus.get(i);
                        Log.e("purchase data", purchaseData);
                        Log.e("owned Skus",sku);
                    }

                }
                else{
                    //Toast.makeText(context, "You are not subscribed or your subscription has expired.", Toast.LENGTH_LONG).show();
                    /*Intent suscribe= new Intent(context,YearlySubscriptionActivity.class);
                    suscribe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    activity.startActivity(suscribe);
                    activity.finish();*/
                }
                /*else
                {
                    //Item(s) not owned

                    String base64EncodedPublicKey = "public_key";

                    mHelper = new IabHelper(this, base64EncodedPublicKey);

                    mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener()
                    {
                        @Override
                        public void onIabSetupFinished(IabResult result)
                        {
                            if(!result.isSuccess())
                            {
                                Log.d("TEST", "In-app Billing setup failed: " + result);
                            }
                            else
                            {
                                Log.d("TEST", "In-app Billing is set up OK");
                            }
                        }
                    });
                }*/
            }
            else
            {
                Toast.makeText(context,"Error", Toast.LENGTH_LONG).show();
                //Error checking owned items
            }
        }
        catch(RemoteException e)
        {
            Toast.makeText(context,"Remote Exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private class GetActiveSubsBundle extends AsyncTask<Void, Double, Bundle> {

        // Executed on main UI thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected Bundle doInBackground(Void... params) {
            Bundle activeSubs = null;
            try {
                activeSubs = mservice.getPurchases(3, packageName,
                        "subs", null);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return activeSubs;
        }

        // Executed on main UI thread
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Bundle result) {
            super.onPostExecute(result);
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

                    skuDetails = mservice.getSkuDetails(3, packageName,
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
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
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

                    skuDetails = mservice.getSkuDetails(3, packageName,
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
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
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

                    skuDetails = mservice.getSkuDetails(3, packageName,
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
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {

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

                    skuDetails = mservice.getSkuDetails(3, packageName,
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
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
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

                    skuDetails = mservice.getSkuDetails(3, packageName,
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
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
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

                    skuDetails = mservice.getSkuDetails(3, packageName,
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
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
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

                    skuDetails = mservice.getSkuDetails(3, packageName,
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
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
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

                    skuDetails = mservice.getSkuDetails(3, packageName,
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
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
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

                    skuDetails = mservice.getSkuDetails(3, packageName,
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
                                        .getBuyIntent(3, packageName, sku,
                                                "inapp",
                                                "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
                                PendingIntent pendingIntent = buyIntentBundle
                                        .getParcelable("BUY_INTENT");
                                activity.startIntentSenderForResult(
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
                } catch (IntentSender.SendIntentException e) {
                    //
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
