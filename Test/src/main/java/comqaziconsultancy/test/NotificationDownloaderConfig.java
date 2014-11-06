package comqaziconsultancy.test;

import android.os.Environment;

import com.qaziconsulltancy.quranlibrary.download.NotificationDownloader;

/**
 * Created by qa185001 on 8/21/2014.
 */
public class NotificationDownloaderConfig extends NotificationDownloader {

    public String getUrlOfQuranFile() {
        return "http://www.qaziconsultancy.com/Quran/zip_folder/ArabQuranBlue.zip";
    }

    public String getSavePathOfQuranFile() {
        return Environment.getExternalStorageDirectory().getPath() +"/Pictures";
    }

    public String getFileName() {
        return "ArabQuran.zip";
    }
}
