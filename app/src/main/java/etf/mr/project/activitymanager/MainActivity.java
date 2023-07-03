package etf.mr.project.activitymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import etf.mr.project.activitymanager.databinding.ActivityMainBinding;
import etf.mr.project.activitymanager.model.Activity;
import etf.mr.project.activitymanager.model.ActivityDTO;
import etf.mr.project.activitymanager.model.CoordTuple;
import etf.mr.project.activitymanager.viewmodel.ActivitiesViewModel;
import etf.mr.project.activitymanager.viewmodel.SelectedActivityViewModel;
import etf.mr.project.activitymanager.viewmodel.SelectedDateViewModel;
import etf.mr.project.activitymanager.viewmodel.SharedViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedViewModel sharedViewModel;
    private SelectedActivityViewModel selectedActivityViewModel;
    private ActivitiesViewModel activitiesViewModel;
    private SelectedDateViewModel selectedDateViewModel;
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        selectedActivityViewModel = new ViewModelProvider(this).get(SelectedActivityViewModel.class);
        activitiesViewModel=new ViewModelProvider(this).get(ActivitiesViewModel.class);
        selectedDateViewModel=new ViewModelProvider(this).get(SelectedDateViewModel.class);

        initializeData();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Locale locale = new Locale(sharedPreferences.getString(SELECTED_LANGUAGE, "en"));
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_details, R.id.navigation_calendar, R.id.navigation_list, R.id.navigation_settings, R.id.navigation_new_activity, R.id.navigation_location_picker)
                .build();

        // fragment navigation setup
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    private void initializeData(){
        List<ActivityDTO> data=new ArrayList<>();

        //citanje iz baze
        //mapiranje

        Random gen=new Random();
        for (int i = 0; i < 10; i++) {
            Activity item = new Activity();
            item.setX(gen.nextDouble()*i);
            item.setY(gen.nextDouble()*i);
            item.setId(i);
            item.setTitle("Naslov " + (i + 1));
            if (i % 5 == 0)
                item.setTitle("NaslovNaslov Naslov NaslovNaslov Naslov" + (i + 1));
            switch (i % 3) {
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

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, i % 7);
            item.setStarts(calendar.getTime());
            item.setEnds(calendar.getTime());
            item.setDesc("Opis Opis Opis Opis Opis Opis Opis Opis Opis");
            item.setAddress("Adres Adres Adres Adres Adres Adres Adres Adres");
            ActivityDTO dto = map(item);
            data.add(dto);
        }
        activitiesViewModel.setSharedData(data);

    }
    public ActivityDTO map(Activity item){
        ActivityDTO dto = new ActivityDTO();
        String day = "MON";
        String month = "DEC";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(item.getStarts());
        int iDay = calendar.get(Calendar.DAY_OF_WEEK);
        int iMonth = calendar.get(Calendar.MONTH);
        switch (iDay) {
            case 1:
                day = getResources().getString(R.string.sun);
                break;
            case 2:
                day = getResources().getString(R.string.mon);
                break;
            case 3:
                day = getResources().getString(R.string.tue);
                break;
            case 4:
                day = getResources().getString(R.string.wed);
                break;
            case 5:
                day = getResources().getString(R.string.thu);
                break;
            case 6:
                day = getResources().getString(R.string.fri);
                break;
            default:
                day = getResources().getString(R.string.sat);
        }
        switch (iMonth) {
            case 0:
                month = getResources().getString(R.string.jan);
                break;
            case 1:
                month = getResources().getString(R.string.feb);
                break;
            case 2:
                month = getResources().getString(R.string.mar);
                break;
            case 3:
                month = getResources().getString(R.string.apr);
                break;
            case 4:
                month = getResources().getString(R.string.may);
                break;
            case 5:
                month = getResources().getString(R.string.jun);
                break;
            case 6:
                month = getResources().getString(R.string.jul);
                break;
            case 7:
                month = getResources().getString(R.string.avg);
                break;
            case 8:
                month = getResources().getString(R.string.sep);
                break;
            case 9:
                month = getResources().getString(R.string.oct);
                break;
            case 10:
                month = getResources().getString(R.string.nov);
                break;
            default:
                month = getResources().getString(R.string.dec);
        }
        dto.setTitle(item.getTitle());
        dto.setId(item.getId());
        dto.setAddress(item.getAddress());
        dto.setDesc(item.getDesc());
        dto.setX(item.getX());
        dto.setY(item.getY());
        dto.setImgs(item.getImgs());
        if (item.getType().equals(getResources().getString(R.string.work_val)))
            dto.setType(getResources().getString(R.string.work));
        else if (item.getType().equals(getResources().getString(R.string.travel_val)))
            dto.setType(getResources().getString(R.string.travel));
        else
            dto.setType(getResources().getString(R.string.freetime));
        dto.setStarts(item.getStarts());
        dto.setEnds(new Date());
        dto.setDate(Integer.toString(calendar.get(Calendar.DATE)));
        dto.setMonth(month);
        dto.setDay(day);
        if (dto.getType().equals(getResources().getString(R.string.work)))
            dto.setIcon(getDrawable(R.drawable.ic_work));
        else if (dto.getType().equals(getResources().getString(R.string.travel)))
            dto.setIcon(getDrawable(R.drawable.ic_trip));
        else
            dto.setIcon(getDrawable(R.drawable.ic_freetime));
        dto.getIcon().setColorFilter(getColor(R.color.primary), PorterDuff.Mode.SRC_IN);
        return dto;
    }
}