
package se.slide.babyfeed.db;

import se.slide.babyfeed.model.FeedLog;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "BabyFeed.sqlite";
    private static final int DATABASE_VERSION = 1;

    private Dao<FeedLog, Integer> feedlogDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        try {
            TableUtils.createTable(connectionSource, FeedLog.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
            int arg3) {

        /*
         * try { List<String> allSql = new ArrayList<String>();
         * switch(oldVersion) { case 1:
         * //allSql.add("alter table AdData add column `new_col` VARCHAR");
         * //allSql.add("alter table AdData add column `new_col2` VARCHAR"); }
         * for (String sql : allSql) { db.execSQL(sql); } } catch (SQLException
         * e) { Log.e(DatabaseHelper.class.getName(),
         * "exception during onUpgrade", e); throw new RuntimeException(e); }
         */

    }

    public Dao<FeedLog, Integer> getFeedLogDao() {
        if (feedlogDao == null) {
            try {
                feedlogDao = getDao(FeedLog.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return feedlogDao;
    }
}
