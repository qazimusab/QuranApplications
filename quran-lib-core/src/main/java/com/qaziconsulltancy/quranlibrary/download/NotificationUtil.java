package com.qaziconsulltancy.quranlibrary.download;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.qaziconsulltancy.quranlibrary.R;
import com.qaziconsulltancy.quranlibrary.curlutils.QuranReaderActivity;
import com.qaziconsulltancy.quranlibrary.util.StorageUtil;
import com.qaziconsulltancy.quranlibrary.util.Utils;

import java.io.File;

public class NotificationUtil {

	public static final int NOTIFY_ID_PROGRESS = 1;
	private static NotificationManager nm;
	public static String URL="";
	private static final String TITLE = "Download Status";
    public static String savePath =Environment.getExternalStorageDirectory().getPath() +"/Pictures";
    public static String fileName ="15LineQuran.zip";

	public static void notifyPtogressNotification(Context context, int notifyId) {
		if (nm == null) {
			nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		final Builder mBuilder = new Builder(context);


		if (TextUtils.isEmpty(savePath)) {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("market://details?id="
					+ context.getPackageName()));
			mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, i,
					PendingIntent.FLAG_UPDATE_CURRENT));
			mBuilder.setContentText("Downloading...");
			mBuilder.setContentTitle("Download Status");
			setNotificationIcon(context, mBuilder);
			nm.notify(notifyId, mBuilder.build());
			return;
		}
		// 当前手机版本>=4.0
		if (Utils.hasIceCreamSandwich()) {
			Intent intent = new Intent(context, QuranReaderActivity.class);
			PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setAutoCancel(true);
			mBuilder.setContentIntent(pi);
			mBuilder.setContentTitle(TITLE).setContentText("Downloading");
			setNotificationIcon(context, mBuilder);
			new ProgressAsyncTask(URL, savePath, fileName, true, nm, mBuilder,
					notifyId).execute();
		} else {// 4.0以前版本
			// // 一定要设置Icon,否则不显示
			// setNotificationIcon(context, mBuilder);
            Intent intent = new Intent(context, QuranReaderActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pi);
            mBuilder.setAutoCancel(true);
			new ProgressAsyncTask(URL, savePath, null, false, nm, notifyId,
					getNotification(context, TITLE)).execute();
		}
	}

	/**
	 * 设置Notification的icon,<br>
	 * 如果不设置图标则无法显示Notification,并且LargeIcon,SmallIcon都需要设置
	 * 
	 * @param context
	 * @param mBuilder
	 */
	private static void setNotificationIcon(Context context,
			final Builder mBuilder) {
		// 如果不设置小图标则无法显示Notification
		mBuilder.setLargeIcon(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.icon_launcher));
		mBuilder.setSmallIcon(R.drawable.icon_launcher);
	}

	/**
	 * 兼容4.0以下设备
	 * 
	 * @param context
	 * @param title
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Notification getNotification(Context context, String title) {
		Notification notification = new Notification(
				R.drawable.icon_launcher, "Downloading",
				System.currentTimeMillis());
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.contentView = new RemoteViews(context.getPackageName(),
				R.layout.notification_progress_layout);
		notification.contentView.setProgressBar(
				R.id.notification_progress_layout_pb, 100, 0, false);
		notification.contentView.setTextViewText(
				R.id.notification_progress_layout_tv_title, title);
		notification.contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, NotificationDownloader.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		return notification;
	}

	/**
	 * 获取文件保存目录
	 * 
	 * @return
	 */
	private static String getSavePath(Context context, String subDir) {
		// 判断SD卡是否存在
		boolean sdCardExist = StorageUtil.isExternalStorageAvailable();
		File file = null;
		if (sdCardExist) {
			file = Environment.getExternalStorageDirectory();
		} else {// 内存存储空间
			file = context.getFilesDir();
			return getPath(file, subDir);
		}
		return getPath(file, subDir);
	}

	private static String getPath(File f, String subDir) {
		File file = new File(f.getAbsolutePath() + subDir);
		if (!file.exists()) {
			boolean isSuccess = file.mkdirs();
			System.out.println(isSuccess);
		}
		return file.getAbsolutePath();
	}
}
