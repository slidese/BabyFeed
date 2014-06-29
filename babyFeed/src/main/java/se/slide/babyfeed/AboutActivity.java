
package se.slide.babyfeed;

import android.view.View;
import android.widget.ImageView;

import com.google.analytics.tracking.android.EasyTracker;

import se.slide.utils.Statics;
import se.slide.utils.about.AboutLines;
import se.slide.utils.about.KenBurnsView;
import se.slide.utils.about.NoBoringActionBarActivity;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends NoBoringActionBarActivity {

    @Override
    public List<AboutLines> getAboutLines() {
        ArrayList<AboutLines> lines = new ArrayList<AboutLines>();
        
        AboutLines a = new AboutLines();
        a.row1 = "Version";
        a.row2 = Statics.getAppVersion(this);
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "By";
        a.row2 = "slide.se";
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "Note";
        a.row2 = "This app was made when I had my baby boy (late January, 2013). I wanted to keep track of when he was being fed and I soon realized that it would be nice to have the phone remind me three hours after every meal. I hope it helps you out too! :)";
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "";
        a.row2 = "";
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "Crashlytics";
        a.row2 = "https://www.crashlytics.com/";
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "Google Analytics";
        a.row2 = "https://www.google.com/analytics/";
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "NotBoringActionBar";
        a.row2 = "https://github.com/flavienlaurent/NotBoringActionBar";
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "divide by zero";
        a.row2 = "http://fonts.tom7.com/";
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "Some icons";
        a.row2 = "http://www.androidicons.com/";
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "JazzyViewPager";
        a.row2 = "https://github.com/jfeinstein10/JazzyViewPager";
        lines.add(a);
        
        a = new AboutLines();
        a.row1 = "ORMLite";
        a.row2 = "http://ormlite.com/sqlite_java_android_orm.shtml";
        lines.add(a);
        
        return lines;
    }

    @Override
    public int[] getDrawableResources() {
        return new int[] { R.drawable.babyfeed_about };
    }

    @Override
    public void setupLogo(ImageView logo) {
        logo.setImageResource(R.drawable.babyfeed_about_nodpi);
    }

    public ImageView[] setupKenBurnsView(KenBurnsView mHeaderPicture) {
        View view = View.inflate(this, R.layout.view_kenburns_one_image, mHeaderPicture);
        
        ImageView[] mImageViews = new ImageView[1];
        mImageViews[0] = (ImageView) view.findViewById(R.id.image0);
        
        return mImageViews;
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
