package etf.mr.project.activitymanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import etf.mr.project.activitymanager.model.ActivityDTO;

public class SelectedActivityViewModel extends ViewModel {

    private MutableLiveData<ActivityDTO> sharedData = new MutableLiveData<>();

    public void setSharedData(ActivityDTO data) {
        sharedData.setValue(data);
    }

    public LiveData<ActivityDTO> getSharedData() {
        return sharedData;
    }
}