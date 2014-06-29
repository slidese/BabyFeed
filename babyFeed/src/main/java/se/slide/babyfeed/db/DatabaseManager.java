
package se.slide.babyfeed.db;

import java.sql.SQLException;
import java.util.List;

import se.slide.babyfeed.model.FeedLog;
import android.content.Context;

import com.j256.ormlite.stmt.QueryBuilder;

public class DatabaseManager {
    static private DatabaseManager instance;
    private DatabaseHelper helper;

    static public void init(Context ctx) {
        if (instance == null) {
            instance = new DatabaseManager(ctx);
        }
    }

    static public DatabaseManager getInstance() {
        return instance;
    }

    private DatabaseManager(Context ctx) {
        helper = new DatabaseHelper(ctx);
    }

    private DatabaseHelper getHelper() {
        return helper;
    }

    public FeedLog getLatestFeedLog() {
        try {
            QueryBuilder<FeedLog, Integer> qBuilder = getHelper().getFeedLogDao().queryBuilder();
            qBuilder.orderBy("dateWithTime", false);
            // qBuilder.limit(1);
            FeedLog latestFeedLog = getHelper().getFeedLogDao().queryForFirst(qBuilder.prepare());
            return latestFeedLog;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<FeedLog> getAllFeedLogs() {
        List<FeedLog> feedlogLists = null;
        try {
            feedlogLists = getHelper().getFeedLogDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedlogLists;
    }

    public void addFeedLog(FeedLog f) {
        try {
            getHelper().getFeedLogDao().create(f);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
