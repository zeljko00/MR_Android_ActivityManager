package etf.mr.project.activitymanager.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import etf.mr.project.activitymanager.R;
import etf.mr.project.activitymanager.adapters.ActivityListAdapter;
import etf.mr.project.activitymanager.db.AppDatabase;
import etf.mr.project.activitymanager.dialogs.DeleteActivityDialog;
import etf.mr.project.activitymanager.interfaces.ActivityDAO;
import etf.mr.project.activitymanager.interfaces.DeleteActivityInterface;
import etf.mr.project.activitymanager.interfaces.NewActivityListener;
import etf.mr.project.activitymanager.model.ActivityDTO;
import etf.mr.project.activitymanager.model.UpcomingData;
import etf.mr.project.activitymanager.viewmodel.ActivitiesViewModel;
import etf.mr.project.activitymanager.viewmodel.SelectedActivityViewModel;
public class UpcomingActivities extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ActivityListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SelectedActivityViewModel selectedActivityViewModel;
    public UpcomingActivities() {
        // Required empty public constructor
    }

    public static UpcomingActivities newInstance(String param1, String param2) {
        UpcomingActivities fragment = new UpcomingActivities();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectedActivityViewModel = new ViewModelProvider(requireActivity()).get(SelectedActivityViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_activities, container, false);

        recyclerView = view.findViewById(R.id.recycler2);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        Bundle args=getArguments();
        UpcomingData data=(UpcomingData) args.getSerializable("data");

        mAdapter = new ActivityListAdapter(data.getData(), new ActivityListAdapter.ItemClickHandler() {
            @Override
            public void handleItemClick(ActivityDTO item) {
                selectedActivityViewModel.setSharedData(item);
                Navigation.findNavController(view).navigate(R.id.action_navigation_upcoming_to_navigation_details);
            }
        }, new ActivityListAdapter.ItemLongClickHandler() {
            @Override
            public void handleItemLongClick(ActivityDTO item) {
            }
        });
        // binding adapter and recycler view
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}