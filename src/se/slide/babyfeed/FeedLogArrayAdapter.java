
package se.slide.babyfeed;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedLogArrayAdapter extends ArrayAdapter<Date> {

    Context context;

    public FeedLogArrayAdapter(Context context, int resourceId, List<Date> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Date date = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_feedlog_item, null);

            holder = new ViewHolder();
            holder.txtTime = (TextView) convertView.findViewById(R.id.textTime);
            holder.txtDate = (TextView) convertView.findViewById(R.id.textDate);
            holder.frameLayout = (FrameLayout) convertView.findViewById(R.id.frameLayout);
            holder.icon = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        if (isDay(date)) {
            holder.frameLayout.setBackgroundColor(context.getResources().getColor(
                    android.R.color.holo_blue_bright));
            holder.icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_action_sun));
        }
        else {
            holder.frameLayout.setBackgroundColor(context.getResources().getColor(
                    android.R.color.holo_blue_light));
            holder.icon.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.ic_action_cloud));
        }

        holder.txtTime.setText(getFormatedDateTime(date, "HH:mm:ss"));
        holder.txtDate.setText(getFormatedDateTime(date, "EEEE, MMM dd"));

        return convertView;
    }

    private String getFormatedDateTime(Date time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(time);
    }

    private boolean isDay(Date date) {
        Calendar now = Calendar.getInstance();
        now.setTime(date);

        Calendar morning = Calendar.getInstance();
        morning.setTime(date);
        morning.set(Calendar.HOUR_OF_DAY, 7);

        Calendar night = Calendar.getInstance();
        night.setTime(date);
        night.set(Calendar.HOUR_OF_DAY, 20);

        if (now.after(morning) && now.before(night))
            return true;
        else
            return false;

    }

    /* private view holder class */
    private class ViewHolder {
        TextView txtTime;
        TextView txtDate;
        FrameLayout frameLayout;
        ImageView icon;
    }
}
