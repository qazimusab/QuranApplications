package qazi.musab.arabquran.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import qazi.musab.arabquran.R;
import qazi.musab.arabquran.util.StringUtil;

public class ProgressAsyncTask extends AsyncTask<Object, Integer, Object> {

	public static final String NOTIFICATION_DOWNLOAD_ERROR = "Download Error";
	public static final String NOTIFICATION_DOWNLOADING = "Downloading...";
	public static final String NOTIFICATION_DOWNLOAD_DONE = "Download Complete!";
	public static final String NOTIFICATION_UNKOWN_ERROR = "Unknown Error";
	public static final String NOTIFICATION_DOWENLOAD_SPEED_UNITS = " KB/S";
	public static final String NOTIFICATION_DOWENLOAD_FILESIZE_UNITS = " MB";
    public static final String NOTIFICATION_EXTRACTING_FILE = "Extracting";
    public static final String NOTIFICATION_READY_TO_READ = "Click to start reciting!";

	public static final int NOTIFICATION_STATUS_FLAG_ERROR = -1;
	public static final int NOTIFICATION_STATUS_FLAG_NORMAL = 1;
	public static final int NOTIFICATION_STATUS_FLAG_DONE = 2;
	public static final int NOTIFICATION_STATUS_FLAG_UNKOWN_ERROR = 3;
    public static final int NOTIFICATION_STATUS_EXTRACTING_FILE = 4;
    public static final int NOTIFICATION_STATUS_READY_TO_READ = 5;

	private static final long UPDATE_INTERVAL = 500;


	private String fileUrl;

	private String fileSavePath;

	private String fileName;

	private NotificationManager nm;
	private Notification notification;
	private Builder mBuilder;
	private int progress;
	private int fileSize;
	private String fileSizeM;

	private int notifyId;

	private boolean isHighVersion;

	/**
	 * 
	 * @param fileUrl
	 * @param fileSavePath
	 * @param fileName
	 * @param isHighVersion
     *
	 * @param nm
	 * @param mBuilder
	 * @param notifyId
	 */
	public ProgressAsyncTask(String fileUrl, String fileSavePath,
			String fileName, boolean isHighVersion, NotificationManager nm,
			Builder mBuilder, int notifyId) {
		this.fileUrl = fileUrl;
		this.fileSavePath = fileSavePath;
		this.fileName = fileName;
		this.nm = nm;
		this.mBuilder = mBuilder;
		this.notifyId = notifyId;
		this.isHighVersion = isHighVersion;
	}

	/**
	 * 
	 * @param fileUrl
	 * @param fileSavePath
	 * @param fileName
	 * @param isGreatVersion
     *
	 * @param nm
	 * @param notifyId
	 * @param notification
	 */
	public ProgressAsyncTask(String fileUrl, String fileSavePath,
			String fileName, boolean isGreatVersion, NotificationManager nm,
			int notifyId, Notification notification) {
		this(fileUrl, fileSavePath, fileName, isGreatVersion, nm, null,
				notifyId);
		this.notification = notification;
    }



	@Override
	protected Object doInBackground(Object... params) {
		disableConnectionReuseIfNecessary();
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		try {
			if (TextUtils.isEmpty(fileName)) {
				fileName = StringUtil.getUrlFileName(fileUrl);
			}
			final URL url = new URL(fileUrl);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.connect();
			fileSize = urlConnection.getContentLength();
			fileSizeM = String.format(Locale.US, "%.2f",
					(float) fileSize / 1024 / 1024);
			bis = new BufferedInputStream(urlConnection.getInputStream());
			File saveFile = new File(fileSavePath, fileName);
			fos = new FileOutputStream(saveFile);

			if (isHighVersion) {
				downloadFile(bis, fos, isHighVersion);

				mBuilder.setContentText(getBuildNotificationContent(0,
						100 + "", fileSizeM + "", NOTIFICATION_STATUS_FLAG_DONE));
				mBuilder.setProgress(fileSize, fileSize, false);
                nm.notify(notifyId, mBuilder.build());
			} else {
				downloadFile(bis, fos, isHighVersion);

				notifyLowVersionNotification(0, 100, fileSize,
						NOTIFICATION_STATUS_FLAG_DONE);
			}
			return true;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			notifyNotification(0, NOTIFICATION_STATUS_FLAG_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			notifyNotification(0, NOTIFICATION_STATUS_FLAG_ERROR);
		} catch (Exception e) {
			e.printStackTrace();

			notifyNotification(0, NOTIFICATION_STATUS_FLAG_UNKOWN_ERROR);
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void downloadFile(InputStream in, OutputStream out,
			boolean isGreatVersion) throws IOException {
		int len;
		byte[] buffer = new byte[1024 * 2];
		int current = 0;
		int bytesInThreshold = 0;
		long updateDelta = 0;
		long updateStart = System.currentTimeMillis();
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
			out.flush();
			current += len;
			bytesInThreshold += len;

			// if (percent % 5 == 0) {
			// if (percent > random.nextInt(10)) {
			if (updateDelta > UPDATE_INTERVAL) {
				double percent = current  / (fileSize/100);

				long downloadSpeed = bytesInThreshold / updateDelta;
				if (isGreatVersion) {
					updateProgress(current, (int) percent, (int) downloadSpeed);
				} else {
					publishProgress(current, (int)percent, (int) downloadSpeed);
				}
				// reset data
				updateStart = System.currentTimeMillis();
				bytesInThreshold = 0;
			}
			updateDelta = System.currentTimeMillis() - updateStart;
		}
	}

    public void unzipFile(){
        new ExtractTask().execute();
    }

    private class ExtractTask extends AsyncTask<Void, Double, Void> {

        // Executed on main UI thread
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            notifyNotification(0, NOTIFICATION_STATUS_EXTRACTING_FILE);
        }


        @Override
        protected Void doInBackground(Void... params) {
            UnzipUtility u=new UnzipUtility();
            try {
                u.unzip(Environment.getExternalStorageDirectory().getPath() +"/Pictures/ArabQuranBlue.zip", Environment.getExternalStorageDirectory().getPath() +"/Pictures/.ArabQuranBlue/");
                File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Pictures/ArabQuranBlue.zip");
                boolean deleted = file.delete();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        // Executed on main UI thread
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            NotificationDownloader.bus.post(new DownloadCompleteEvent());
            notifyNotification(0, NOTIFICATION_STATUS_READY_TO_READ);
        }

    }

	/**
	 *
	 * @param current
	 * @param percent
	 * @param downloadSpeed
	 */
	private void updateProgress(int current, int percent, long downloadSpeed) {
		mBuilder.setContentText(getBuildNotificationContent(downloadSpeed,
				percent, fileSizeM, NOTIFICATION_STATUS_FLAG_NORMAL));
		mBuilder.setProgress(fileSize, current, false);
		nm.notify(notifyId, mBuilder.build());
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		notifyNotification(100, NOTIFICATION_STATUS_FLAG_NORMAL);
	}

	@Override
	protected void onPostExecute(Object result) {
        unzipFile();
        super.onPostExecute(result);

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		progress = values[0];
		int percent = values[1];
		int speed = values[2];
		notifyLowVersionNotification(speed, percent, fileSize,
				NOTIFICATION_STATUS_FLAG_NORMAL);
	}

	/**
	 * Workaround for bug pre-Froyo, see here for more info:
	 * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
	 */
	public static void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			System.setProperty("http.keepAlive", "false");
		}
	}


	public static String getBuildNotificationContent(Serializable speed,
			Serializable percent, String fileSize, int type) {
		StringBuilder builder = new StringBuilder();
		switch (type) {
		case NOTIFICATION_STATUS_FLAG_ERROR:
			builder.append(NOTIFICATION_DOWNLOAD_ERROR);
			break;
		case NOTIFICATION_STATUS_FLAG_NORMAL:
			builder.append(NOTIFICATION_DOWNLOADING).append(speed)
					.append(NOTIFICATION_DOWENLOAD_SPEED_UNITS);
			builder.append("            ").append(percent).append("%")
					.append("  ").append(fileSize)
					.append(NOTIFICATION_DOWENLOAD_FILESIZE_UNITS);
			break;
		case NOTIFICATION_STATUS_FLAG_DONE:
			builder.append(NOTIFICATION_DOWNLOAD_DONE);
			break;
		case NOTIFICATION_STATUS_FLAG_UNKOWN_ERROR:
			builder.append(NOTIFICATION_UNKOWN_ERROR);
			break;
        case NOTIFICATION_STATUS_EXTRACTING_FILE:
			builder.append(NOTIFICATION_EXTRACTING_FILE);
			break;
            case NOTIFICATION_STATUS_READY_TO_READ:
			builder.append(NOTIFICATION_READY_TO_READ);
			break;
		}
		return builder.toString();
	}


	private void notifyHighVersionNotification(Serializable speed,
			Serializable percent, int fileSize, int type) {
		mBuilder.setContentText(getBuildNotificationContent(0, 0,
				TextUtils.isEmpty(fileSizeM) ? "0" : fileSizeM, type));
		mBuilder.setProgress(fileSize, 0, false);
		nm.notify(notifyId, mBuilder.build());
	}


	private void notifyLowVersionNotification(Serializable speed,
			Serializable percent, int fileSize, int type) {
		switch (type) {
		case NOTIFICATION_STATUS_FLAG_ERROR:
			notification.contentView.setTextViewText(
					R.id.notification_progress_layout_tv_content,
					NOTIFICATION_DOWNLOAD_ERROR);
			notification.contentView.setTextViewText(
					R.id.notification_progress_layout_tv_content2, "");
			notification.contentView.setProgressBar(
					R.id.notification_progress_layout_pb, 0, 0, false);

			break;
		case NOTIFICATION_STATUS_FLAG_NORMAL:
			StringBuilder builder = new StringBuilder();
			builder.append(NOTIFICATION_DOWNLOADING).append(speed)
					.append(NOTIFICATION_DOWENLOAD_SPEED_UNITS);
			notification.contentView.setTextViewText(
					R.id.notification_progress_layout_tv_content,
					builder.toString());
			builder.delete(0, builder.length());
			builder.append(percent).append("%  ")
					.append(TextUtils.isEmpty(fileSizeM) ? "0" : fileSizeM)
					.append(NOTIFICATION_DOWENLOAD_FILESIZE_UNITS);
			notification.contentView.setTextViewText(
					R.id.notification_progress_layout_tv_content2,
					builder.toString());
			notification.contentView.setProgressBar(
					R.id.notification_progress_layout_pb, fileSize, progress,
					false);
			break;
		case NOTIFICATION_STATUS_FLAG_DONE:
			notification.contentView.setTextViewText(
					R.id.notification_progress_layout_tv_content,
					NOTIFICATION_DOWNLOAD_DONE);
			notification.contentView.setTextViewText(
					R.id.notification_progress_layout_tv_content2, "");
			notification.contentView.setProgressBar(
					R.id.notification_progress_layout_pb, 100, 100, false);
			break;
		case NOTIFICATION_STATUS_FLAG_UNKOWN_ERROR:
			notification.contentView.setTextViewText(
					R.id.notification_progress_layout_tv_content,
					NOTIFICATION_UNKOWN_ERROR);
			notification.contentView.setTextViewText(
					R.id.notification_progress_layout_tv_content2, "");
			notification.contentView.setProgressBar(
					R.id.notification_progress_layout_pb, 0, 0, false);
			break;
            case NOTIFICATION_STATUS_EXTRACTING_FILE:
                notification.contentView.setTextViewText(
                        R.id.notification_progress_layout_tv_content,
                        NOTIFICATION_EXTRACTING_FILE);
                notification.contentView.setTextViewText(
                        R.id.notification_progress_layout_tv_content2, "");
                notification.contentView.setProgressBar(
                        R.id.notification_progress_layout_pb, 100, 100, false);
                break;
            case NOTIFICATION_STATUS_READY_TO_READ:
                notification.contentView.setTextViewText(
                        R.id.notification_progress_layout_tv_content,
                        NOTIFICATION_READY_TO_READ);
                notification.contentView.setTextViewText(
                        R.id.notification_progress_layout_tv_content2, "");
                notification.contentView.setProgressBar(
                        R.id.notification_progress_layout_pb, 100, 100, false);
                break;
		}
		nm.notify(notifyId, notification);
	}


	private void notifyNotification(int fileSize, int type) {
		if (isHighVersion) {
			notifyHighVersionNotification(0, 0, fileSize, type);
		} else {
			notifyLowVersionNotification(0, 0, fileSize, type);
		}
	}

}
