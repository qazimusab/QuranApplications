package qazi.musab.arabquran.curlutils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;


/**
 * Simple Activity for curl testing.
 * 
 * @author harism
 */
public class QuranReaderActivity extends BaseQuranReaderActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Crashlytics.start(this);

        final AlertDialog.Builder alertbox4 = new AlertDialog.Builder(QuranReaderActivity.this);
        alertbox4.setTitle("Contribute");
        alertbox4.setIcon(android.R.drawable.stat_notify_error);


        alertbox4.setMessage("Please consider contributing towards the development of this application.");
        alertbox4.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                String[] items = new String[9];
                items[0] = "$1";
                items[1] = "$2";
                items[2] = "$3";
                items[3] = "$4";
                items[4] = "$5";
                items[5] = "$10";
                items[6] = "$20";
                items[7] = "$50";
                items[8] = "$100";


                AlertDialog.Builder chooser = new AlertDialog.Builder(QuranReaderActivity.this);
                chooser
                        .setTitle("Contribute")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
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
                myChooser.show();
            }
        });
        alertbox4.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    @Override
    protected int getPagesOfQuranNotIncludingIndex() {
        return 604;
    }


    /**
	 * CurlView size changed observer.
	 */



    @Override
    protected Activity getCurlActivity() {
        return QuranReaderActivity.this;
    }
}