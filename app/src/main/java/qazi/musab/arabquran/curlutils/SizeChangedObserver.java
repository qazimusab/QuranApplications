package qazi.musab.arabquran.curlutils;

/**
 * Created by Musab on 8/20/2014.
 */
public class SizeChangedObserver implements CurlView.SizeChangedObserver {
    CurlView mCurlView;
    public SizeChangedObserver(CurlView mCurlView) {
        this.mCurlView=mCurlView;
    }

    @Override
    public void onSizeChanged(int w, int h) {
        if (w > h) {
            mCurlView.setViewMode(CurlView.SHOW_TWO_PAGES);
            mCurlView.setMargins(0f, 0f, 0f, 0f);
        } else {
            mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
            mCurlView.setMargins(0f, 0f, .0f, 0f);
        }
    }
}