package qazi.musab.colorcodedquran.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import qazi.musab.colorcodedquran.R;
import qazi.musab.colorcodedquran.curlutils.CurlView;
import qazi.musab.colorcodedquran.curlutils.PageProvider;
import qazi.musab.colorcodedquran.curlutils.QuranReaderActivity;
import qazi.musab.colorcodedquran.curlutils.SizeChangedObserver;


/**
 * Created by Musab on 8/20/2014.
 */
public class Dialogs {
    CurlView mCurlView;
    Context context;
    SizeChangedObserver sizeChangedObserver;
    QuranReaderActivity quranReaderActivity;

    public Dialogs(CurlView mCurlView,QuranReaderActivity quranReaderActivity) {
        sizeChangedObserver=new SizeChangedObserver(mCurlView);
        this.quranReaderActivity = quranReaderActivity;
        this.mCurlView=mCurlView;
        context= quranReaderActivity.getApplicationContext();
    }


    AlertDialog getSurahChooserDialog(){
        AlertDialog.Builder builder3 = new AlertDialog.Builder(quranReaderActivity);

        String[] surah= quranReaderActivity.getResources().getStringArray(R.array.surah);
        builder3.setTitle("Go To Surah");
        builder3.setItems(surah, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(620 - 0);
                }
                if (which == 1) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(620 - 1);
                }
                if (which == 2) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 50);
                }
                if (which == 3) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 77);
                }
                if (which == 4) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 106);
                }
                if (which == 5) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 128);
                }
                if (which == 6) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 151);
                }
                if (which == 7) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 177);
                }
                if (which == 8) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 187);
                }
                if (which == 9) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 208);
                }
                if (which == 10) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 221);
                }
                if (which == 11) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 235);
                }
                if (which == 12) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 249);
                }
                if (which == 13) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 255);
                }
                if (which == 14) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 262);
                }
                if (which == 15) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 267);
                }
                if (which == 16) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 282);
                }
                if (which == 17) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 293);
                }
                if (which == 18) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 305);
                }
                if (which == 19) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 312);
                }
                if (which == 20) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 322);
                }
                if (which == 21) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 332);
                }
                if (which == 22) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 342);
                }
                if (which == 23) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 350);
                }
                if (which == 24) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 359);
                }
                if (which == 25) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 367);
                }
                if (which == 26) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 377);
                }
                if (which == 27) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 385);
                }
                if (which == 28) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 396);
                }
                if (which == 29) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 404);
                }
                if (which == 30) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 411);
                }
                if (which == 31) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 415);
                }
                if (which == 32) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 418);
                }
                if (which == 33) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 428);
                }
                if (which == 34) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 434);
                }
                if (which == 35) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 440);
                }
                if (which == 36) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 446);
                }
                if (which == 37) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 453);
                }
                if (which == 38) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 458);
                }
                if (which == 39) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 467);
                }
                if (which == 40) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 477);
                }
                if (which == 41) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 483);
                }
                if (which == 42) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 489);
                }
                if (which == 43) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 496);
                }
                if (which == 44) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 499);
                }
                if (which == 45) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 502);
                }
                if (which == 46) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 508);
                }
                if (which == 47) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 511);
                }
                if (which == 48) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 515);
                }
                if (which == 49) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 518);
                }
                if (which == 50) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 520);
                }
                if (which == 51) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 523);
                }
                if (which == 52) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 526);
                }
                if (which == 53) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 528);
                }
                if (which == 54) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 531);
                }
                if (which == 55) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 534);
                }
                if (which == 56) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 537);
                }
                if (which == 57) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 542);
                }
                if (which == 58) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 545);
                }
                if (which == 59) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 549);
                }
                if (which == 60) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 551);
                }
                if (which == 61) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 553);
                }
                if (which == 62) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 554);
                }
                if (which == 63) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 556);
                }
                if (which == 64) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 558);
                }
                if (which == 65) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 560);
                }
                if (which == 66) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 562);
                }
                if (which == 67) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 564);
                }
                if (which == 68) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 566);
                }
                if (which == 69) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 568);
                }
                if (which == 70) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 570);
                }
                if (which == 71) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 572);
                }
                if (which == 72) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 574);
                }
                if (which == 73) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 575);
                }
                if (which == 74) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 577);
                }
                if (which == 75) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 578);
                }
                if (which == 76) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 580);
                }
                if (which == 77) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 582);
                }
                if (which == 78) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 583);
                }
                if (which == 79) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 585);
                }
                if (which == 80) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 586);
                }
                if (which == 81) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 587);
                }
                if (which == 82) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 587);
                }
                if (which == 83) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 589);
                }
                if (which == 84) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 590);
                }
                if (which == 85) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 591);
                }
                if (which == 86) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 591);
                }
                if (which == 87) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 592);
                }
                if (which == 88) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 593);
                }
                if (which == 89) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 594);
                }
                if (which == 90) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 595);
                }
                if (which == 91) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 595);
                }
                if (which == 92) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 596);
                }
                if (which == 93) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 596);
                }
                if (which == 94) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 597);
                }
                if (which == 95) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 597);
                }
                if (which == 96) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 598);
                }
                if (which == 97) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 598);
                }
                if (which == 98) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 599);
                }
                if (which == 99) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 599);
                }
                if (which == 100) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 600);
                }
                if (which == 101) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 600);
                }
                if (which == 102) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 601);
                }
                if (which == 103) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 601);
                }
                if (which == 104) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 601);
                }
                if (which == 105) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 602);
                }
                if (which == 106) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 602);
                }
                if (which == 107) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 602);
                }
                if (which == 108) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 603);
                }
                if (which == 109) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 603);
                }
                if (which == 110) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 603);
                }
                if (which == 111) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 604);
                }
                if (which == 112) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 604);
                }
                if (which == 113) {
                    quranReaderActivity.exitZoom();
                    dialog.dismiss();
                    mCurlView.setPageProvider(new PageProvider());
                    mCurlView.setSizeChangedObserver(sizeChangedObserver);
                    mCurlView.setCurrentIndex(621 - 604);
                }

                // The 'which' argument contains the index position
                // of the selected item
            }
        });


        // 3. Get the AlertDialog from create()
        AlertDialog dialog3 = builder3.create();
        return dialog3;
    }

    AlertDialog getJuzChooserDialog(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(quranReaderActivity);

        String[] juz= quranReaderActivity.getResources().getStringArray(R.array.juz);
        builder2
                .setTitle("Go To Juz (Parah)")
                .setItems(juz, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-1);

                        }
                        if(which==1){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-22);

                        }
                        if(which==2){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-42);

                        }
                        if(which==3){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-62);

                        }
                        if(which==4){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-82);

                        }
                        if(which==5){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-102);

                        }
                        if(which==6){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-121);

                        }
                        if(which==7){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-142);

                        }
                        if(which==8){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-162);

                        }
                        if(which==9){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-182);

                        }
                        if(which==10){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-202);

                        }
                        if(which==11){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-222);

                        }
                        if(which==12){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-242);

                        }
                        if(which==13){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-262);

                        }
                        if(which==14){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-282);

                        }
                        if(which==15){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-301);

                        }
                        if(which==16){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-322);

                        }
                        if(which==17){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-341);

                        }
                        if(which==18){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-362);

                        }
                        if(which==19){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-382);

                        }
                        if(which==20){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-402);

                        }
                        if(which==21){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-422);

                        }
                        if(which==22){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-442);

                        }
                        if(which==23){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-462);

                        }
                        if(which==24){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-482);

                        }
                        if(which==25){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-502);

                        }
                        if(which==26){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-522);

                        }
                        if(which==27){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-542);

                        }
                        if(which==28){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-562);

                        }
                        if(which==29){
                            quranReaderActivity.exitZoom(); dialog.dismiss();
                            mCurlView.setPageProvider(new PageProvider());
                            mCurlView.setSizeChangedObserver(sizeChangedObserver);
                            mCurlView.setCurrentIndex(621-582);

                        }


                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                });


        // 3. Get the AlertDialog from create()
        AlertDialog dialog2 = builder2.create();
        return dialog2;
    }
}
