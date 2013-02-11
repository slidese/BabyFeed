package se.slide.babyfeed;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;

public class PlayAlarmService extends WakefulIntentService {

    public PlayAlarmService() {
        super("PlayAlarmService");
    }
    
    @Override
    protected void doWakefulWork(Intent arg0) {
        
        
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (soundUri == null)
            soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
        r.play();
    }

    /* (non-Javadoc)
     * @see android.app.IntentService#onDestroy()
     */
    @Override
    public void onDestroy() {
        Toast.makeText(this, "Destroy", Toast.LENGTH_LONG).show();
        
        super.onDestroy();
    }

}
