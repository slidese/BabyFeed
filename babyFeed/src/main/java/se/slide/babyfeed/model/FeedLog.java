
package se.slide.babyfeed.model;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class FeedLog {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private Date dateWithTime;

    public Date getDateWithTime() {
        return dateWithTime;
    }

    public void setDateWithTime(Date dateWithTime) {
        this.dateWithTime = dateWithTime;
    }

}
