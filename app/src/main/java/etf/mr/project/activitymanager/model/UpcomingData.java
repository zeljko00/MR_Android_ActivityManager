package etf.mr.project.activitymanager.model;

import java.io.Serializable;
import java.util.List;

public class UpcomingData implements Serializable {
    private List<ActivityDTO> data;

    public List<ActivityDTO> getData() {
        return data;
    }

    public void setData(List<ActivityDTO> data) {
        this.data = data;
    }
}
