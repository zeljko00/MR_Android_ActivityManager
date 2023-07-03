package etf.mr.project.activitymanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import etf.mr.project.activitymanager.interfaces.NewActivityListener;
import etf.mr.project.activitymanager.model.ActivityDTO;

public class SelectedDateViewModel extends ViewModel {

    private MutableLiveData<String> sharedData = new MutableLiveData<>();
    private NewActivityListener newActivityListener;

    public void setSharedData(String data) {
        sharedData.setValue(data);
    }

    public LiveData<String> getSharedData() {
        return sharedData;
    }
    public void addActivity(ActivityDTO activityDTO){
        newActivityListener.newActivity(activityDTO);
    }
    public void setNewActivityListener(NewActivityListener listener){
        newActivityListener=listener;
    }

}
