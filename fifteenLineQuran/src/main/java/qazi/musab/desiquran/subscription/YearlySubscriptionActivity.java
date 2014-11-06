package qazi.musab.desiquran.subscription;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;

import com.android.vending.billing.IInAppBillingService;

import qazi.musab.desiquran.R;
import qazi.musab.desiquran.curlutils.BaseActivity;
import qazi.musab.desiquran.curlutils.QuranReaderActivity;
import qazi.musab.desiquran.util.Contributor;

public class YearlySubscriptionActivity extends BaseActivity {

    public ImageButton subscribeYearlyButton;
    IInAppBillingService mservice;
    ServiceConnection connection;
    Contributor contributor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly_subscription);
        subscribeYearlyButton = (ImageButton)findViewById(R.id.subscribeyearlybutton);
        connectToSubscribe();
        subscribeYearlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contributor.contributeOneBuckYearly();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Intent suscribe= new Intent(YearlySubscriptionActivity.this,QuranReaderActivity.class);
            suscribe.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(suscribe);
            finish();
            overridePendingTransition(R.anim.slide_up,android.R.anim.fade_out);
        }
    }

    private void connectToSubscribe() {
        connection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mservice = null;

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mservice = IInAppBillingService.Stub.asInterface(service);
                contributor=new Contributor(mservice,YearlySubscriptionActivity.this);
            }
        };

        bindService(new Intent(
                        "com.android.vending.billing.InAppBillingService.BIND"),
                connection, Context.BIND_AUTO_CREATE);
    }

}
