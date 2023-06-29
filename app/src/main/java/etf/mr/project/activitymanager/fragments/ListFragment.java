package etf.mr.project.activitymanager.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
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
import java.util.stream.Collectors;

import etf.mr.project.activitymanager.R;
import etf.mr.project.activitymanager.adapters.ActivityListAdapter;
import etf.mr.project.activitymanager.model.Activity;
import etf.mr.project.activitymanager.model.ActivityDTO;

public class ListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private ActivityListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Activity> activityList=new ArrayList<>();
    private List<ActivityDTO> data=new ArrayList<>();
    private List<ActivityDTO> filteredData;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.fragment_list, container, false);

        for (int i = 0; i < 100; i++) {
            Activity item = new Activity();
            item.setTitle("Naslov " + (i + 1));
            if(i%5==0)
                item.setTitle("NaslovNaslov Naslov NaslovNaslov Naslov" + (i + 1));
            switch (i%3){
                case 0:
                    item.setType("work");
                    break;
                case 1:
                    item.setType("travel");
                    break;
                case 2:
                    item.setType("relaxation");
                    break;
            }

            Calendar calendar=Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE,i%14);
            item.setStarts(calendar.getTime());
            String day="MON";
            String month="DEC";
            int iDay=calendar.get(Calendar.DAY_OF_WEEK);
            int iMonth=calendar.get(Calendar.MONTH);
            switch (iDay){
                case 1:
                    day=getContext().getResources().getString(R.string.sun);
                    break;
                case 2:
                    day=getContext().getResources().getString(R.string.mon);
                    break;
                case 3:
                    day=getContext().getResources().getString(R.string.tue);
                    break;
                case 4:
                    day=getContext().getResources().getString(R.string.wed);
                    break;
                case 5:
                    day=getContext().getResources().getString(R.string.thu);
                    break;
                case 6:
                    day=getContext().getResources().getString(R.string.fri);
                    break;
                default:
                    day=getContext().getResources().getString(R.string.sat);
            }
            switch (iMonth){
                case 0:
                    month=getContext().getResources().getString(R.string.jan);
                    break;
                case 1:
                    month=getContext().getResources().getString(R.string.feb);
                    break;
                case 2:
                    month=getContext().getResources().getString(R.string.mar);
                    break;
                case 3:
                    month=getContext().getResources().getString(R.string.apr);
                    break;
                case 4:
                    month=getContext().getResources().getString(R.string.may);
                    break;
                case 5:
                    month=getContext().getResources().getString(R.string.jun);
                    break;
                    case 6:
                        month=getContext().getResources().getString(R.string.jul);
                    break;
                case 7:
                    month=getContext().getResources().getString(R.string.avg);
                    break;
                case 8:
                    month=getContext().getResources().getString(R.string.sep);
                    break;
                case 9:
                    month=getContext().getResources().getString(R.string.oct);
                    break;
                case 10:
                    month=getContext().getResources().getString(R.string.nov);
                    break;
                default:
                    month=getContext().getResources().getString(R.string.dec);
            }
                ActivityDTO dto=new ActivityDTO();
                dto.setTitle(item.getTitle());
                if(item.getType().equals(getContext().getResources().getString(R.string.work_val)))
                    dto.setType(getContext().getResources().getString(R.string.work));
                else if(item.getType().equals(getContext().getResources().getString(R.string.travel_val)))
                    dto.setType(getContext().getResources().getString(R.string.travel));
                else
                    dto.setType(getContext().getResources().getString(R.string.freetime));
                dto.setStarts(item.getStarts());
                dto.setDate(Integer.toString(calendar.get(Calendar.DATE)));
                dto.setMonth(month);
                dto.setDay(day);
                data.add(dto);
                if(dto.getType().equals(getContext().getResources().getString(R.string.work_val)))
                    dto.setIcon(getContext().getDrawable(R.drawable.ic_work));
                else  if(dto.getType().equals(getContext().getResources().getString(R.string.travel_val)))
                    dto.setIcon(getContext().getDrawable(R.drawable.ic_trip));
                else
                    dto.setIcon(getContext().getDrawable(R.drawable.ic_freetime));
                dto.getIcon().setColorFilter(getContext().getColor(R.color.primary), PorterDuff.Mode.SRC_IN);
            activityList.add(item);
        }
filteredData=data;
        recyclerView = view.findViewById(R.id.recycler);
        // optimizing
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        // creating adapter
        mAdapter = new ActivityListAdapter(filteredData, new ActivityListAdapter.ItemClickHandler() {
            @Override
            public void handleItemClick(ActivityDTO item) {
                Toast.makeText(getContext(),item.getTitle(), Toast.LENGTH_SHORT).show();
            }

        },new ActivityListAdapter.ItemLongClickHandler() {
            @Override
            public void handleItemLongClick(ActivityDTO item) {
                Toast.makeText(getContext(),"Long: "+item.getTitle(), Toast.LENGTH_SHORT).show();
            }

        });
        // binding adapter and recycler view
        recyclerView.setAdapter(mAdapter);
        FloatingActionButton fab=view.findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Adding", Toast.LENGTH_SHORT).show();
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
                mAdapter.filterData(data,query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText==null ||newText.equals(""))
                    mAdapter.filterData(data,null);
                return true;
            }
        });
    }
}