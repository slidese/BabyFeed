
package se.slide.babyfeed;

import android.app.AlarmManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import se.slide.babyfeed.db.DatabaseManager;
import se.slide.babyfeed.model.FeedLog;
import se.slide.babyfeed.utils.Utils;

import java.util.Calendar;
import java.util.Date;

public class FeedFragment extends Fragment {

    private TextView mTextLastFeedLog = null;
    private TextView mTextRemindIn = null;
    private Date mLatestSinceDate = null;
    private SharedPreferences mSharedPreferences = null;

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_add, null);
        LinearLayout btnAddFeedLog = (LinearLayout) view.findViewById(R.id.btnAddFeedLog);
        btnAddFeedLog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                addFeedLog();
            }
        });

        mTextLastFeedLog = (TextView) view.findViewById(R.id.textLastFeedLog);
        mTextRemindIn = (TextView) view.findViewById(R.id.textRemindIn);

        updateSinceTimeFromLatest();

        return view;
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onPause()
     */
    @Override
    public void onPause() {
        super.onPause();

    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();

        updateSinceTimeFromLatest();
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();

    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onStop()
     */
    @Override
    public void onStop() {
        super.onStop();
    }

    public void updateSinceTimeFromLatest() {
        if (mLatestSinceDate == null) {
            FeedLog flog = DatabaseManager.getInstance().getLatestFeedLog();
            if (flog != null) {
                mLatestSinceDate = flog.getDateWithTime();
            }
        }

        if (mLatestSinceDate == null)
            return; // Maybe show notification about error?

        setSinceTime();
        setRemindIn();
    }

    private void setSinceTime() {
        long date1 = mLatestSinceDate.getTime();
        long date2 = Calendar.getInstance().getTime().getTime();

        long elapsed = date2 - date1;

        final long ONE_SECOND = 1000;
        final long ONE_MINUTE = ONE_SECOND * 60;
        final long ONE_HOUR = ONE_MINUTE * 60;
        final long ONE_DAY = ONE_HOUR * 24;
        long days = elapsed / ONE_DAY;
        elapsed %= ONE_DAY;
        long hours = elapsed / ONE_HOUR;
        elapsed %= ONE_HOUR;
        long minutes = elapsed / ONE_MINUTE;
        elapsed %= ONE_MINUTE;
        long seconds = elapsed / ONE_SECOND;

        String since = "";
        if (days > 0)
            since = "over a day";
        else {
            String h = String.valueOf(hours);
            if (hours > 1)
                since += h + " hours ";
            else if (hours > 0)
                since += h + " hour ";

            String m = String.valueOf(minutes);
            if (minutes > 1)
                since += m + " minutes";
            else if (minutes > 0)
                since += m + " minute";

            String s = String.valueOf(seconds);
            if (hours < 1 && minutes < 1)
                since = s + " seconds";
        }

        String name = "";
        if (mSharedPreferences != null)
            name = mSharedPreferences.getString("example_text", "The baby");
        if (name.length() < 1)
            name = "The baby";

        mTextLastFeedLog.setText(Html.fromHtml(name + " was last fed<br/><b>" + since
                + "</b> ago."));
    }
    
    private void setRemindIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int min = -2;
        String minutes = sharedPreferences.getString("sync_frequency", null);
        if (minutes != null) {
            try {
                min = Integer.valueOf(minutes);
            }
            catch (NumberFormatException nfe) {
                Log.e("se.slide.babyfeed", nfe.getMessage());
            }
        }
        
        if (min == -1) {
            mTextRemindIn.setText("I will not remind you.");
        }
        else if (min == -2) {
            mTextRemindIn.setText("An error occured trying to figure out when to remind you. Please reset your remind frequency in Settings.");
        }
        
        if (min > 0 && mLatestSinceDate != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(mLatestSinceDate);
            cal.add(Calendar.MINUTE, min);
            
            Calendar now = Calendar.getInstance();
            
            String friendlyDate = Utils.getFriendlyDatetimeInterval(new Date(), cal.getTime());
            
            String remindText = "I will remind you again in about<br/>" + friendlyDate;
            if (cal.compareTo(now) < 1)
                remindText = "I have reminded you. Have you fed your baby after the last reminder?";
            
            mTextRemindIn.setText(Html.fromHtml(remindText));
        }
    }

    private void addFeedLog() {
        AsyncTaskAddFeed addInBackground = new AsyncTaskAddFeed();
        addInBackground.execute();
    }

    public class AsyncTaskAddFeed extends AsyncTask<Void, Integer, Date>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Date doInBackground(Void... arg0)
        {
            FeedLog flog = new FeedLog();
            Date result = Calendar.getInstance().getTime();
            flog.setDateWithTime(result);
            DatabaseManager.getInstance().addFeedLog(flog);
            
            /*
            // get a Calendar object with current time
            Calendar cal = Calendar.getInstance();
            // add 5 minutes to the calendar object
            cal.add(Calendar.SECOND, 5);
            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            intent.putExtra("alarm_message", "O'Doyle Rules!");
            // In reality, you would want to have a static variable for the request code instead of 192837
            PendingIntent sender = PendingIntent.getBroadcast(getActivity(), 192837, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            
            // Get the AlarmManager service
            AlarmManager am = (AlarmManager) getActivity().getSystemService(FeedActivity.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
            */
            
            getActivity().sendBroadcast(new Intent(getActivity(), BootReceiver.class));

            return result;
        }

        @Override
        protected void onPostExecute(Date result)
        {
            mLatestSinceDate = result;
            updateSinceTimeFromLatest();

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(LogFragment.ResponseReceiver.INTENT_CODE_LOG_ADDED);
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra(LogFragment.ResponseReceiver.INTENT_PARAM_DATETIME,
                    result.getTime());
            getActivity().sendBroadcast(broadcastIntent);
        }
    }

}
