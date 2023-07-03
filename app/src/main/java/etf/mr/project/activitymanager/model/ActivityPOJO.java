package etf.mr.project.activitymanager.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ActivityPOJO {
    @Embedded
    private ActivityEntity activity;
    @Relation(parentColumn ="id", entityColumn = "activity_id")
    private List<Image> imgs;

    public ActivityEntity getActivity() {
        return activity;
    }

    public void setActivity(ActivityEntity activity) {
        this.activity = activity;
    }

    public List<Image> getImgs() {
        return imgs;
    }

    public void setImgs(List<Image> imgs) {
        this.imgs = imgs;
    }

    @Override
    public String toString() {
        return "ActivityPOJO{" +
                "activity=" + activity +
                ", imgs=" + imgs +
                '}';
    }
}
