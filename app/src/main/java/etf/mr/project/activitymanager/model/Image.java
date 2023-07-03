package etf.mr.project.activitymanager.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "imgs",
        foreignKeys = @ForeignKey(
                entity = ActivityEntity.class,
                parentColumns = "id",
                childColumns = "activity_id",
                onDelete = ForeignKey.CASCADE
        ))
public class Image {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long activity_id;
    private String path;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(long activity_id) {
        this.activity_id = activity_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", activity_id=" + activity_id +
                ", path='" + path + '\'' +
                '}';
    }
}
