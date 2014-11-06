package qazi.musab.arabquran.curlutils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Musab on 8/20/2014.
 */
public class PageProvider implements CurlView.PageProvider {



    @Override
    public int getPageCount() {
        return 624;
    }

    private Bitmap loadBitmap(int width, int height, int index) throws IOException, ExecutionException, InterruptedException {
        Bitmap b = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        b.eraseColor(0xFFFFFFFF);
        Canvas c = new Canvas(b);
        Drawable d;
        //if(index==610){
        String pathName = Environment.getExternalStorageDirectory().getPath()+"/Pictures/.ArabQuranBlue/Arab Quran Blue/"+(624-index)+".jpg";
        //String pathName ="http://www.qaziconsultancy.com/Quran/ArabQuranBlue/"+(624-index)+".jpg";
        Log.e("message", pathName);
            /*DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.background) // resource or drawable
                    .showImageForEmptyUri(R.drawable.book) // resource or drawable
                    .showImageOnFail(R.drawable.download_icon) // resource or drawable
                    .resetViewBeforeLoading(false)  // default
                    .delayBeforeLoading(1000)
                    .cacheInMemory(false) // default
                    .cacheOnDisk(false) // default
            .considerExifParams(false) // default
                    .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                    .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
                    .handler(new Handler()) // default
                    .build();*/
        // Bitmap bmp=imageLoader.loadImageSync(pathName,options);
        d=Drawable.createFromPath(pathName);
        // d = drawableFromUrl(pathName);
        //}
        //else
        //d = getResources().getDrawable(mBitmapIds[610-index]);
        int margin = 0;
        int border = 0;
        Rect r = new Rect(margin, margin, width - margin, height - margin);

        int imageWidth = (r.width() - (border * 2));
        int imageHeight = r.height();
        if (imageHeight > r.height() - (border * 2)) {
            imageHeight = r.height() - (border * 2);
            imageWidth = imageHeight * d.getIntrinsicWidth()
                    / d.getIntrinsicHeight();
        }

        r.left += ((r.width() - imageWidth) / 2) - border;
        r.right = r.left + imageWidth + border + border;
        r.top += ((r.height() - imageHeight) / 2) - border;
        r.bottom = r.top + imageHeight + border + border;

        Paint p = new Paint();
        p.setColor(0xFFC0C0C0);
        c.drawRect(r, p);
        r.left += border;
        r.right -= border;
        r.top += border;
        r.bottom -= border;

        d.setBounds(r);
        d.draw(c);

        return b;

    }

    public class GetImageTask extends AsyncTask<String , Void, Drawable> {

        Drawable d;

        @Override
        protected Drawable doInBackground(String... params) {
            try {
                InputStream is = new URL(params[0]).openStream();
                d = Drawable.createFromStream(is, "src");
                return d;
            } catch (MalformedURLException e) {
                // e.printStackTrace();
            } catch (IOException e) {
                // e.printStackTrace();
            }
            return null;            }

        @Override
        protected void onPostExecute(Drawable drawable) {
            super.onPostExecute(drawable);
        }
    }

    public  Drawable drawableFromUrl(String url) throws IOException, ExecutionException, InterruptedException {
        return new GetImageTask().execute(url).get();
    }

    @Override
    public void updatePage(CurlPage page, int width, int height, int index) {
        try{
            Bitmap front = loadBitmap(width, height, index);
            page.setTexture(front, CurlPage.SIDE_BOTH);
            page.setColor(Color.argb(127, 255, 255, 255), CurlPage.SIDE_BACK);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

