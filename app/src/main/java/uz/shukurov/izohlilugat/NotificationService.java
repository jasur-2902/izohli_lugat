package uz.shukurov.izohlilugat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class NotificationService extends Service {


	Dictionary dic;
	private NotificationManager mManager;
	MainActivity mainActivity;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		DictionaryActivity DicActivity = new DictionaryActivity(this);
		DicActivity.createDatabase();
		DicActivity.openDatabase();
		dic = Dictionary.getInstance();


	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);




		Word wotd = dic.getWordFromDatabase();

		String title = wotd.getWord();
		// Set Notification Text
		String text = wotd.getDefinition();

		// Open NotificationView Class on Notification Click
		Intent intent2 = new Intent(this, SearchActivity.class);
		// Send data to NotificationView Class



		intent2.putExtra("something", title);
		intent2.putExtra("text", text);
		// Open NotificationView.java Activity
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

		PendingIntent dismissIntent = NotificationActivity.getDismissIntent(0, getApplicationContext());

		// Create Notification using NotificationCompat.Builder
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
				// Set Icon
				.setSmallIcon(getNotificationIcon())

				// Set Ticker Message
				.setTicker("Kunning so'zi")// Set Title
				.setContentTitle(wotd.getWord())

				// Set Text
				.setContentText(wotd.getDefinition())

				// Add an Actions Buttons below Notification
				.addAction(R.drawable.ic_close, getString(R.string.close_text), dismissIntent)

				// Set PendingIntent into Notification
				.setContentIntent(pIntent)

				// Dismiss Notification
				.setAutoCancel(true);

		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager
		notificationmanager.notify(0, builder.build());
//		// Getting Notification Service
//		mManager = (NotificationManager) this.getApplicationContext()
//				.getSystemService(
//						this.getApplicationContext().NOTIFICATION_SERVICE);
//		/*
//		 * When the user taps the notification we have to show the Home Screen
//		 * of our App, this job can be done with the help of the following
//		 * Intent.
//		 */
//		Intent intent1 = new Intent(this.getApplicationContext(), MyView.class);
//
//		Notification notification = new Notification(R.drawable.ic_launcher,
//				"See My App something for you", System.currentTimeMillis());
//
//		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
//				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
//				this.getApplicationContext(), 0, intent1,
//				PendingIntent.FLAG_UPDATE_CURRENT);
//
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
////
////		notification.setLatestEventInfo(this.getApplicationContext(),
////				"SANBOOK", "See My App something for you",
////				pendingNotificationIntent);
//
//		mManager.notify(0, notification);
	}

	private int getNotificationIcon() {
		boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
		return useWhiteIcon ? R.drawable.ic_fiber_new : R.mipmap.ic_launcher;

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}