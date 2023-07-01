package etf.mr.project.activitymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import java.util.List;
import java.util.Locale;

import etf.mr.project.activitymanager.databinding.ActivityMainBinding;
import etf.mr.project.activitymanager.model.Activity;
import etf.mr.project.activitymanager.model.ActivityDTO;
import etf.mr.project.activitymanager.model.CoordTuple;
import etf.mr.project.activitymanager.viewmodel.SelectedActivityViewModel;
import etf.mr.project.activitymanager.viewmodel.SharedViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SharedViewModel sharedViewModel;
    private SelectedActivityViewModel selectedActivityViewModel;
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Locale locale = new Locale(sharedPreferences.getString(SELECTED_LANGUAGE, "en"));
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        selectedActivityViewModel = new ViewModelProvider(this).get(SelectedActivityViewModel.class);
//        if(selectedActivityViewModel.getSharedData()!=null && selectedActivityViewModel.getSharedData().getValue()!=null)
//            Log.d("xxxx--",selectedActivityViewModel.getSharedData().getValue().toString());
//        else
//            Log.d("xxxx--","No dat presetn");

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_details, R.id.navigation_calendar, R.id.navigation_list, R.id.navigation_settings, R.id.navigation_new_activity, R.id.navigation_location_picker)
                .build();

        // fragment navigation setup
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }

    public SharedViewModel getSharedViewModel() {
        return sharedViewModel;
    }


    public SelectedActivityViewModel getSelectedActivityViewModel() {
        return selectedActivityViewModel;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Hi from Main", Toast.LENGTH_SHORT);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // Obtain the ViewModel again using the ViewModelProvider
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        selectedActivityViewModel = new ViewModelProvider(this).get(SelectedActivityViewModel.class);
    }
}