package se.slide.babyfeed.utils;

import java.util.Date;

public class Utils {
    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = ONE_SECOND * 60;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final long ONE_DAY = ONE_HOUR * 24;
    
    public static final String PREF_FIRST_USE = "first_use";

    public static String getFriendlyDatetimeInterval(Date from, Date to) {
        long elapsed = to.getTime() - from.getTime();
        
        long days = elapsed / ONE_DAY;
        elapsed %= ONE_DAY;
        long hours = elapsed / ONE_HOUR;
        elapsed %= ONE_HOUR;
        long minutes = elapsed / ONE_MINUTE;
        elapsed %= ONE_MINUTE;
        long seconds = elapsed / ONE_SECOND;

        if (days > 0)
            return "over a day";
        
        if (days < 1 && hours < 1 && minutes < 1) {
            String s = String.valueOf(seconds);
            if (seconds == 0)
                return s + " seconds";
            else if (seconds == 1)
                return s + " second";
            else
                return s + "seconds";
        }

        StringBuilder builder = new StringBuilder();
        
        String h = String.valueOf(hours);
        if (hours == 1) {
            builder.append(h);
            builder.append(" hour");
        }
        else if (hours > 1) {
            builder.append(h);
            builder.append(" hours");
        }
        
        if (hours > 0 && minutes > 0)
            builder.append(" and ");

        String m = String.valueOf(minutes);
        if (minutes == 1) {
            builder.append(m);
            builder.append(" minute");
        }
        else if (minutes > 1) {
            builder.append(m);
            builder.append(" minutes");
        }
        
        return builder.toString();
    }
}
