package qazi.musab.desiquran.curlutils;

/**
 * Created by Musab on 8/20/2014.
 */
public class SizeChangedObserver implements ChildCurlView.SizeChangedObserver {
    ChildCurlView mCurlView;
    public SizeChangedObserver(ChildCurlView mCurlView) {
        this.mCurlView=mCurlView;
    }

    @Override
    public void onSizeChanged(int w, int h) {
        if (w > h) {
            mCurlView.setViewMode(ChildCurlView.SHOW_TWO_PAGES);
            mCurlView.setMargins(0f, 0f, 0f, 0f);
        } else {
            mCurlView.setViewMode(ChildCurlView.SHOW_ONE_PAGE);
            mCurlView.setMargins(0f, 0f, .0f, 0f);
        }
    }
}