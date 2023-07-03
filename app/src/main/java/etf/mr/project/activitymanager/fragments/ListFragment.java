package etf.mr.project.activitymanager.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import etf.mr.project.activitymanager.MainActivity;
import etf.mr.project.activitymanager.R;
import etf.mr.project.activitymanager.adapters.ActivityListAdapter;
import etf.mr.project.activitymanager.dialogs.DeleteActivityDialog;
import etf.mr.project.activitymanager.interfaces.DeleteActivityInterface;
import etf.mr.project.activitymanager.interfaces.NewActivityListener;
import etf.mr.project.activitymanager.model.Activity;
import etf.mr.project.activitymanager.model.ActivityDTO;
import etf.mr.project.activitymanager.viewmodel.ActivitiesViewModel;
import etf.mr.project.activitymanager.viewmodel.SelectedActivityViewModel;

public class ListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ActivityListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SelectedActivityViewModel selectedActivityViewModel;
    private ActivitiesViewModel activitiesViewModel;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(String param1, String param2) {
        ListFragment fragment = new ListFragment();
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
        activitiesViewModel = new ViewModelProvider(requireActivity()).get(ActivitiesViewModel.class);
        activitiesViewModel.setNewActivityListener(new NewActivityListener() {
            @Override
            public void newActivity(ActivityDTO activityDTO) {
                activitiesViewModel.getSharedData().getValue().add(activityDTO);
                activitiesViewModel.getSharedData().getValue().sort((a1,a2)-> {
                    return a1.getStarts().compareTo(a2.getStarts());
                });
                mAdapter.changeData(activitiesViewModel.getSharedData().getValue());
            }
        });


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        // optimizing
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // creating adapter
        activitiesViewModel.getSharedData().getValue().sort((a1,a2)-> {
            return (int)(a1.getStarts().compareTo(a2.getStarts()));
        });
        try {
            Log.d("type", activitiesViewModel.getSharedData().getValue().stream().filter(a -> a.getTitle().equals("new")).collect(Collectors.toList()).get(0).toString());
        }catch (Exception e){}
        mAdapter = new ActivityListAdapter(activitiesViewModel.getSharedData().getValue(), new ActivityListAdapter.ItemClickHandler() {
            @Override
            public void handleItemClick(ActivityDTO item) {
                selectedActivityViewModel.setSharedData(item);
                Navigation.findNavController(view).navigate(R.id.action_navigation_list_to_navigation_details);
            }
        }, new ActivityListAdapter.ItemLongClickHandler() {
            @Override
            public void handleItemLongClick(ActivityDTO item) {
                DeleteActivityDialog dialog = new DeleteActivityDialog(item.getId(), new DeleteActivityInterface() {
                    @Override
                    public void delete(long id) {

                        // obrisati iz baze

                        activitiesViewModel.setSharedData(activitiesViewModel.getSharedData().getValue().stream().filter(a -> a.getId() != id).collect(Collectors.toList()));
                        mAdapter.changeData(activitiesViewModel.getSharedData().getValue());
                    }
                });
                dialog.show(getChildFragmentManager(), "DeleteActivityDialog");
            }

        });
        // binding adapter and recycler view
        recyclerView.setAdapter(mAdapter);
        FloatingActionButton fab = view.findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_list_to_navigation_location_picker);
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.equals(""))
                    mAdapter.changeData(activitiesViewModel.getSharedData().getValue().stream().filter(a -> a.getTitle().toLowerCase().contains(newText.toLowerCase())).collect(Collectors.toList()));
                else
                    mAdapter.changeData(activitiesViewModel.getSharedData().getValue());
                return true;
            }
        });
    }
}