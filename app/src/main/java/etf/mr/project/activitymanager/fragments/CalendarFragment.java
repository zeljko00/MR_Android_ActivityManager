package etf.mr.project.activitymanager.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import etf.mr.project.activitymanager.R;
import etf.mr.project.activitymanager.adapters.ActivityListAdapter;
import etf.mr.project.activitymanager.dialogs.DeleteActivityDialog;
import etf.mr.project.activitymanager.interfaces.DeleteActivityInterface;
import etf.mr.project.activitymanager.model.ActivityDTO;
import etf.mr.project.activitymanager.viewmodel.ActivitiesViewModel;
import etf.mr.project.activitymanager.viewmodel.SelectedActivityViewModel;
import etf.mr.project.activitymanager.viewmodel.SelectedDateViewModel;

public class CalendarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private CalendarView calendarView;
    private SelectedActivityViewModel selectedActivityViewModel;
    private ActivitiesViewModel activitiesViewModel;
    private SelectedDateViewModel selectedDateViewModel;
    private RecyclerView.LayoutManager layoutManager;

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    private ActivityListAdapter mAdapter;

    private RecyclerView recyclerView;
    private TextView label;
    private ImageView empty;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
        selectedDateViewModel = new ViewModelProvider(requireActivity()).get(SelectedDateViewModel.class);
        activitiesViewModel = new ViewModelProvider(requireActivity()).get(ActivitiesViewModel.class);
        activitiesViewModel.getSharedData().getValue().sort((a1, a2) -> {
            return (int) (a1.getStarts().compareTo(a2.getStarts()));
        });

//        for(Map.Entry<String,List<ActivityDTO>> entry: groups.entrySet()){
//            Log.d("events",entry.getKey());
//            for(ActivityDTO act: entry.getValue())
//                Log.d("events",act.getTitle());
//        }


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.calendarRecycler);
        empty=view.findViewById(R.id.emptyView);
        label=view.findViewById(R.id.label);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        Map<String, List<ActivityDTO>> groups = activitiesViewModel.getSharedData().getValue().stream().collect(Collectors.groupingBy((ActivityDTO a) -> {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(a.getStarts());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    try {
                        return format.format(calendar.getTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return "10.09.2023";
                    }
                })
        );
        Log.d("events - groups", groups.toString());
        List<EventDay> events = new ArrayList<>();
        if (groups != null) {
            for (Map.Entry<String, List<ActivityDTO>> entry : groups.entrySet()) {

                try {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(format.parse(entry.getKey()));
                    events.add(new EventDay(calendar, R.drawable.ic_event));
                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
            Log.d("events", events.toString());
        } else Log.d("events", "groups = null");

        Log.d("events", events.toString());
        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NonNull EventDay eventDay) {
                try {
                    String date = format.format(eventDay.getCalendar().getTime());
                    selectedDateViewModel.setSharedData(date);
                    List<ActivityDTO> temp=activitiesViewModel.getSharedData().getValue().stream().filter(a -> {
                        try {
                            return format.format(a.getStarts()).equals(date);
                        } catch (Exception ee) {
                            return false;
                        }
                    }).collect(Collectors.toList());
                    mAdapter.changeData(temp);
                    if(temp.size()!=0){
                        Log.d("visib","set to invisible");
                        empty.setVisibility(View.INVISIBLE);
                        label.setVisibility(View.INVISIBLE);
                    } else{
                        Log.d("visib","set to visible");
                        empty.setVisibility(View.VISIBLE);
                        label.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        List<ActivityDTO> temp = null;

        if (selectedDateViewModel.getSharedData() == null || selectedDateViewModel.getSharedData().getValue() == null) {
            try {
                selectedDateViewModel.setSharedData(format.format(new Date()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (selectedDateViewModel.getSharedData() != null && selectedDateViewModel.getSharedData().getValue() != null)

            temp = activitiesViewModel.getSharedData().getValue().stream().filter(a -> {
                try {
                    return format.format(a.getStarts()).equals(selectedDateViewModel.getSharedData().getValue());
                } catch (Exception ee) {
                    return false;
                }
            }).collect(Collectors.toList());
        else
            temp = activitiesViewModel.getSharedData().getValue();

        try {
            List<Calendar> calendars = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(selectedDateViewModel.getSharedData().getValue()));
            calendars.add(calendar);
            calendarView.setDate(calendar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(temp.size()!=0){
            Log.d("visib","set to invisible");
            empty.setVisibility(View.INVISIBLE);
            label.setVisibility(View.INVISIBLE);
        } else{
            Log.d("visib","set to visible");
            empty.setVisibility(View.VISIBLE);
            label.setVisibility(View.VISIBLE);
        }
        mAdapter = new ActivityListAdapter(temp, new ActivityListAdapter.ItemClickHandler() {
            @Override
            public void handleItemClick(ActivityDTO item) {
                selectedActivityViewModel.setSharedData(item);
                Navigation.findNavController(view).navigate(R.id.action_navigation_calendar_to_navigation_details);
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