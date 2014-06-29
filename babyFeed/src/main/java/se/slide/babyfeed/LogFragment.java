
package se.slide.babyfeed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import se.slide.babyfeed.db.DatabaseManager;
import se.slide.babyfeed.model.FeedLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class LogFragment extends ListFragment {

    private ResponseReceiver mReceiver;

    public LogFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Remove the list item divider
        getListView().setDivider(null);
        getListView().setDividerHeight(0);

        // Initialize the receiver
        mReceiver = new ResponseReceiver();

        attachAdapter();
        registerReceiver();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onStart()
     */
    @Override
    public void onStart() {
        super.onStart();

        attachAdapter();
        registerReceiver();
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(LogFragment.ResponseReceiver.INTENT_CODE_LOG_ADDED);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        getActivity().registerReceiver(mReceiver, filter);
    }

    private void unregisterReceiver() {
        getActivity().unregisterReceiver(mReceiver);
    }

    private void attachAdapter() {
        final List<FeedLog> feedLogs = DatabaseManager.getInstance().getAllFeedLogs();

        List<Date> logs = new ArrayList<Date>();
        for (FeedLog flog : feedLogs) {
            logs.add(0, flog.getDateWithTime());
        }

        FeedLogArrayAdapter adapter = new FeedLogArrayAdapter(getActivity(),
                R.layout.listview_feedlog_item, logs);
        setListAdapter(adapter);
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();

        attachAdapter();
        registerReceiver();
    }

    /*
     * (non-Javadoc)
     * @see android.support.v4.app.Fragment#onPause()
     */
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    public class ResponseReceiver extends BroadcastReceiver {
        public static final String INTENT_CODE_LOG_ADDED = "se.slide.babyfeed.intent.action.LOG_ADDED";
        public static final String INTENT_PARAM_DATETIME = "datetime";

        @Override
        public void onReceive(Context context, Intent intent) {
            attachAdapter();
        }
    }

}
