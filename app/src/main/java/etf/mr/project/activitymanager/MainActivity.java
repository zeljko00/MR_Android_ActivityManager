package etf.mr.project.activitymanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

import etf.mr.project.activitymanager.databinding.ActivityMainBinding;
import etf.mr.project.activitymanager.db.AppDatabase;
import etf.mr.project.activitymanager.interfaces.ActivityDAO;
import etf.mr.project.activitymanager.model.Activity;
import etf.mr.project.activitymanager.model.ActivityDTO;
import etf.mr.project.activitymanager.model.ActivityEntity;
import etf.mr.project.activitymanager.model.ActivityPOJO;
import etf.mr.project.activitymanager.model.CoordTuple;
import etf.mr.project.activitymanager.model.Image;
import etf.mr.project.activitymanager.model.UpcomingData;
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
    private AppDatabase db;
    private ActivityDAO activityDAO;
    private static final String CHANEL_ID = "ma_chanel";
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static final String NOTIFICATIONS_PERIOD = "Notifications.Period";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Channel Name";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        selectedActivityViewModel = new ViewModelProvider(this).get(SelectedActivityViewModel.class);
        activitiesViewModel = new ViewModelProvider(this).get(ActivitiesViewModel.class);
        selectedDateViewModel = new ViewModelProvider(this).get(SelectedDateViewModel.class);

        db = Room.databaseBuilder(this, AppDatabase.class, "app-database")
                .build();

        activityDAO = db.activityDAO();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        MainActivity main = this;

        Locale locale = new Locale(sharedPreferences.getString(SELECTED_LANGUAGE, "en"));
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String period = sharedPreferences.getString(NOTIFICATIONS_PERIOD, "1dddd");
        Log.d("reminder", "period: " + period);
        if (period.equals(getResources().getString(R.string.p1h_value)))
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        else if (period.equals(getResources().getString(R.string.p1d_value)))
            calendar.add(Calendar.DATE, 1);
        else
            calendar.add(Calendar.DATE, 7);
        Log.d("reminder", "before: " + calendar.getTime());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<ActivityPOJO> entities = activityDAO.getActivities();
                List<ActivityDTO> data = entities.stream().map(a -> map(a.getActivity(), a.getImgs())).collect(Collectors.toList());
                List<ActivityDTO> toNotify = data.stream().filter(a -> {
                    return a.getStarts().compareTo(calendar.getTime()) < 0;
                }).collect(Collectors.toList());
                Log.d("reminder", "to notify: " + toNotify.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activitiesViewModel.setSharedData(data);
                        binding = ActivityMainBinding.inflate(getLayoutInflater());
                        setContentView(binding.getRoot());
                        showNotification(toNotify);


                        BottomNavigationView navView = findViewById(R.id.nav_view);
                        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                                R.id.navigation_upcoming,R.id.navigation_details, R.id.navigation_calendar, R.id.navigation_list, R.id.navigation_settings, R.id.navigation_new_activity, R.id.navigation_location_picker)
                                .build();

                        // fragment navigation setup
                        NavController navController = Navigation.findNavController(main, R.id.nav_host_fragment_activity_main);
                        NavigationUI.setupActionBarWithNavController(main, navController, appBarConfiguration);
                        NavigationUI.setupWithNavController(binding.navView, navController);
                    }
                });
            }

        });


    }

//    private void initializeData() {
//        List<ActivityDTO> data = new ArrayList<>();
//
//        //citanje iz baze
//        //mapiranje
//
//        Random gen = new Random();
//        for (int i = 0; i < 10; i++) {
//            ActivityEntity item = new ActivityEntity();
//            item.setX(gen.nextDouble() * i);
//            item.setY(gen.nextDouble() * i);
//            item.setId(i);
//            item.setTitle("Naslov " + (i + 1));
//            if (i % 5 == 0)
//                item.setTitle("NaslovNaslov Naslov NaslovNaslov Naslov" + (i + 1));
//            switch (i % 3) {
//                case 0:
//                    item.setType("work");
//                    break;
//                case 1:
//                    item.setType("travel");
//                    break;
//                case 2:
//                    item.setType("relaxation");
//                    break;
//            }
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(new Date());
//            calendar.add(Calendar.DATE, i % 7);
//            item.setStarts(calendar.getTime());
//            item.setEnds(calendar.getTime());
//            item.setDesc("Opis Opis Opis Opis Opis Opis Opis Opis Opis");
//            item.setAddress("Adres Adres Adres Adres Adres Adres Adres Adres");
//            ActivityDTO dto = map(item, new ArrayList<>());
//            data.add(dto);
//        }
//        activitiesViewModel.setSharedData(data);
//
//    }


    public ActivityDTO map(ActivityEntity item, List<Image> imgs) {
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
        dto.setImgs(imgs.stream().map(i -> i.getPath()).collect(Collectors.toList()));
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

    //    private void showNotification(List<String> list) {
//        String content="";
//        for(int i=1; i<=list.size(); i++){
//            content+=list.get(i-1)+System.lineSeparator()+(i== list.size()?"":"  |  ");
//        }
//        // Create a notification builder
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANEL_ID)
//                .setSmallIcon(R.drawable.ic_notify)
//                .setContentTitle(getResources().getString(R.string.notify_label))
//                .setContentText(content)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//
//        // Create an explicit intent for the notification's destination
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
//
//        // Show the notification
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("notify", "no permission");
//            return;
//        } else notificationManager.notify(11, builder.build());
//
//    }
    private void showNotification(List<ActivityDTO> list) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), getResources().getString(R.string.remind).replace("##",Integer.toString(list.size())), Snackbar.LENGTH_SHORT);

        android.app.Activity act=this;
        snackbar.setAction(getResources().getString(R.string.view), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(act, R.id.nav_host_fragment_activity_main);

// Create a bundle to hold the data
                Bundle bundle = new Bundle();
                UpcomingData data=new UpcomingData();
                data.setData(list);
                bundle.putSerializable("data", data); // Replace "key" with the actual argument key and customObject with your custom object

// Navigate to the fragment and set the arguments
                navController.navigate(R.id.navigation_upcoming, bundle);
            }
        });
        snackbar.show();

    }
}