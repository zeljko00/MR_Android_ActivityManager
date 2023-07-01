package etf.mr.project.activitymanager.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import etf.mr.project.activitymanager.model.CoordTuple;

public class SharedViewModel extends ViewModel {

    private MutableLiveData<CoordTuple> sharedData = new MutableLiveData<>();

    public void setSharedData(CoordTuple data) {
        sharedData.setValue(data);
    }

    public LiveData<CoordTuple> getSharedData() {
        return sharedData;
    }
}
