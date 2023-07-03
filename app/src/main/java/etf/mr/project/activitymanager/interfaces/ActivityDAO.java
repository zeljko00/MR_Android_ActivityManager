package etf.mr.project.activitymanager.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import etf.mr.project.activitymanager.model.Activity;
import etf.mr.project.activitymanager.model.ActivityEntity;
import etf.mr.project.activitymanager.model.ActivityPOJO;
import etf.mr.project.activitymanager.model.Image;

@Dao
public interface ActivityDAO {
    @Transaction
    @Query("SELECT * FROM activities")
    List<ActivityPOJO> getActivities();

    @Transaction
    @Insert
    long insertActivity(ActivityEntity activity);

    @Transaction
    @Insert
    void insertImg(Image image);

    @Transaction
    @Query("DELETE FROM activities WHERE id=:id")
    void deleteActivity(long id);

}
