
package se.slide.babyfeed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //WakefulIntentService.sendWakefulWork(context, PlayAlarmService.class);
        
        context.startService(new Intent(context, PlayAlarmService2.class));

        /*
         * try { Bundle bundle = intent.getExtras(); String message =
         * bundle.getString("alarm_message"); Toast.makeText(context, message,
         * Toast.LENGTH_SHORT).show(); context.startService(new Intent(context,
         * PlayAlarmService2.class)); } catch (Exception e) {
         * Toast.makeText(context,
         * "There was an error somewhere, but we still received an alarm",
         * Toast.LENGTH_SHORT).show(); e.printStackTrace(); }
         */
    }

}
