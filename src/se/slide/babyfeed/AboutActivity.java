
package se.slide.babyfeed;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class AboutActivity extends FragmentActivity {

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);
        
        
        TextView message = (TextView) findViewById(R.id.aboutMessage);
        message.setText(Html.fromHtml(getString(R.string.about_message)));
        
        TextView acks = (TextView) findViewById(R.id.aboutAcknowledgments);
        acks.setText(Html.fromHtml(getString(R.string.about_acknowledgements)));
        acks.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                CustomDialog firstUseDialog = CustomDialog.newInstance(getString(R.string.acknowledgements_title), getString(R.string.acknowledgements_message));
                firstUseDialog.show(fm, "custom_dialog_tag");
            }
        });

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(AboutActivity.this, SettingsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
      super.onStart();
      EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
      super.onStop();
      EasyTracker.getInstance(this).activityStop(this);
    }
}
