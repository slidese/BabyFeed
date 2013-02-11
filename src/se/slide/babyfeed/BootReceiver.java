
package se.slide.babyfeed;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import se.slide.babyfeed.db.DatabaseManager;
import se.slide.babyfeed.model.FeedLog;
import se.slide.babyfeed.utils.Utils;

import java.util.Calendar;
import java.util.Date;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        // Remove previous alarms
        mgr.cancel(pi);
        
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        
        int min = -1;
        String minutes = sharedPreferences.getString("sync_frequency", null);
        if (minutes != null) {
            try {
                min = Integer.valueOf(minutes);
            }
            catch (NumberFormatException nfe) {
                Log.e("se.slide.babyfeed", nfe.getMessage());
            }
        }
        
        Date latestSinceDate = null;
        FeedLog flog = DatabaseManager.getInstance().getLatestFeedLog();
        if (flog != null)
            latestSinceDate = flog.getDateWithTime();

        if (min > 0 && latestSinceDate != null) {
            //SystemClock.elapsedRealtime() + minutes*60*1000
            Calendar cal = Calendar.getInstance();
            cal.setTime(latestSinceDate);
            cal.add(Calendar.MINUTE, min);
            
            Calendar now = Calendar.getInstance();
            if (cal.compareTo(now) < 1)
                return;
            
            String friendlyDate = Utils.getFriendlyDatetimeInterval(new Date(), cal.getTime());
            
            //Toast.makeText(context, "Alarm: " + friendlyDate, Toast.LENGTH_LONG).show();
            
            mgr.set(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(),
                    pi);    
        }
        
    }

}
