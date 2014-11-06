package qazi.musab.colorcodedquran.subscription;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Musab on 8/20/2014.
 */
public class SendTask extends AsyncTask<String, Void, Boolean> {

    HttpResponse response;
    String email,sharedPrefCode;
    SharedPreferences.Editor editor;


    public SendTask(String email, String sharedPrefCode,SharedPreferences.Editor editor) {
        super();
        this.email=email;
        this.sharedPrefCode=sharedPrefCode;//sharedPref.getInt("code", 0)+"")
        this.editor=editor;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Log.e("place", "4");
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://www.qaziconsultancy.com/php/verify.php");


            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("email", email));
            // nameValuePairs.add(new BasicNameValuePair("verify", "true"));
            nameValuePairs.add(new BasicNameValuePair("code",sharedPrefCode ));
            // nameValuePairs.add(new BasicNameValuePair("country", Validate.this.getResources().getConfiguration().locale.getDisplayCountry()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            response = httpclient.execute(httppost);
            editor.putBoolean("done", true);
            editor.commit();

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
