package etf.mr.project.activitymanager.interfaces;

import etf.mr.project.activitymanager.model.ActivityDTO;

@FunctionalInterface
public interface NewActivityListener {

    void newActivity(ActivityDTO activityDTO);
}
