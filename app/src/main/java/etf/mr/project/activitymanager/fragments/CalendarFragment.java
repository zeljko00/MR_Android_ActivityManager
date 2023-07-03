package etf.mr.project.activitymanager.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import etf.mr.project.activitymanager.model.ActivityDTO;
import etf.mr.project.activitymanager.viewmodel.ActivitiesViewModel;

public class CalendarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private CalendarView calendarView;
    private ActivitiesViewModel activitiesViewModel;

    private DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

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
        Log.d("events - groups",groups.toString());
         List<EventDay> events = new ArrayList<>();
        if (groups != null) {
            for (Map.Entry<String, List<ActivityDTO>> entry : groups.entrySet()) {

                    try {
                        Calendar calendar=Calendar.getInstance();
                        calendar.setTime(format.parse(entry.getKey()));
                        events.add(new EventDay(calendar,R.drawable.ic_event));
                    } catch (Exception e) {
                        e.printStackTrace();

                    }

            }
            Log.d("events",events.toString());
        } else Log.d("events","groups = null");


//        Calendar calendar1 = Calendar.getInstance();
//        Calendar calendar2 = Calendar.getInstance();
//        Calendar calendar3 = Calendar.getInstance();
//
//
//        calendar1.add(Calendar.DATE, 2);
//        Log.d("date", calendar1.toString());
//        events.add(new EventDay(calendar1, R.drawable.ic_work, Color.parseColor("#228B22")));
//        calendar2.add(Calendar.DATE, 5);
//        Log.d("date", calendar2.toString());
//        events.add(new EventDay(calendar2, R.drawable.ic_freetime, Color.parseColor("#228B22")));
//        calendar3.add(Calendar.DATE, 8);
//        Log.d("date", calendar3.toString());
//        events.add(new EventDay(calendar3, R.drawable.ic_trip, Color.parseColor("#228B22")));

        Log.d("events",events.toString());
        calendarView.setEvents(events);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(@NonNull EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Log.d("date", clickedDayCalendar.toString());
            }
        });
        return view;
    }
}