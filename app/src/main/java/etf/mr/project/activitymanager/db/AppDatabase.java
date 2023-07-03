package etf.mr.project.activitymanager.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import etf.mr.project.activitymanager.interfaces.ActivityDAO;
import etf.mr.project.activitymanager.model.ActivityEntity;
import etf.mr.project.activitymanager.model.Image;

@Database(entities = {ActivityEntity.class, Image.class}, version=1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ActivityDAO activityDAO();
}
