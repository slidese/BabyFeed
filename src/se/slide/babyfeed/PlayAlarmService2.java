
package se.slide.babyfeed;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import se.slide.babyfeed.db.DatabaseManager;
import se.slide.babyfeed.model.FeedLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayAlarmService2 extends Service {
    private static int NOTIFICATION_ID = 0;

    private boolean mKeepPlaying = false;
    // private AsyncTaskPlayAlarm mPlayAlarm;
    private Ringtone mRingtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // mKeepPlaying = true;
        // AsyncTaskPlayAlarm mPlayAlarm = new AsyncTaskPlayAlarm(this);
        // mPlayAlarm.execute();
        //Toast.makeText(this, "onCreate", Toast.LENGTH_LONG).show();
    }

    /*
     * (non-Javadoc)
     * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        playAlarm();
        showNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void playAlarm() {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (soundUri == null)
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mRingtone = RingtoneManager.getRingtone(this, soundUri);
        mRingtone.play();
    }
    
    private void showNotification() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = "";
        if (sharedPreferences != null)
            name = sharedPreferences.getString("example_text", "The baby");
        if (name.length() < 1)
            name = "The baby";
        
        Date latestSinceDate = null;
        FeedLog flog = DatabaseManager.getInstance().getLatestFeedLog();
        if (flog != null)
            latestSinceDate = flog.getDateWithTime();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_actionbar)
                        .setContentTitle("Feed " + name)
                        .setAutoCancel(true)
                        .setContentText("Last fed " + DateFormat.getTimeInstance(DateFormat.SHORT).format(latestSinceDate));
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, FeedActivity.class);

        // The stack builder object will contain an artificial back stack for
        // the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(FeedActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                        );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(PlayAlarmService2.NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDestroy() {
        // mPlayAlarm.cancel(true);
        // mKeepPlaying = false;
        mRingtone.stop();
    }

    /*
     * public class AsyncTaskPlayAlarm extends AsyncTask<Void, Void, Void> {
     * private Context context; public AsyncTaskPlayAlarm(Context context) {
     * this.context = context; }
     * @Override protected Void doInBackground(Void... arg0) { // Play alarm
     * sound int i = 0; while (i < 1) { i++; if (isCancelled()) return null; Uri
     * soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); if
     * (soundUri == null) soundUri =
     * RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
     * Ringtone r = RingtoneManager.getRingtone(context, soundUri); r.play(); }
     * return null; } }
     */
}
