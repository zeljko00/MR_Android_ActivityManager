package etf.mr.project.activitymanager.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.textview.MaterialTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import etf.mr.project.activitymanager.R;
//import etf.mr.project.activitymanager.carousel.CarouselLayoutManager;
import etf.mr.project.activitymanager.adapters.ActivityListAdapter;
import etf.mr.project.activitymanager.adapters.CarouselAdapter;
import etf.mr.project.activitymanager.databinding.ActivityMainBinding;
import etf.mr.project.activitymanager.dialogs.DeleteActivityDialog;
import etf.mr.project.activitymanager.interfaces.DeleteActivityInterface;
import etf.mr.project.activitymanager.model.ActivityDTO;
import etf.mr.project.activitymanager.model.ActivityPOJO;
import etf.mr.project.activitymanager.viewmodel.SelectedActivityViewModel;

public class ActivityDetailsFragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private MapView mapView;
    private GoogleMap googleMap;

    private Marker marker;

    private SelectedActivityViewModel selectedActivityViewModel;

    private DateFormat dateFormat;

    private MaterialTextView title;
    private MaterialTextView desc;
    private MaterialTextView address;
    private MaterialTextView type;
    private MaterialTextView starts;
    private MaterialTextView ends;
    private ImageView icon;

    private CarouselAdapter carouselAdapter;
    private RecyclerView carousel;
    public ActivityDetailsFragment() {
        // Required empty public constructor
    }

    public static ActivityDetailsFragment newInstance(String param1, String param2) {
        ActivityDetailsFragment fragment = new ActivityDetailsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_details, container, false);
        mapView = view.findViewById(R.id.locationView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        title = view.findViewById(R.id.titleView);
        desc = view.findViewById(R.id.descView);
        address = view.findViewById(R.id.addressView);
        type = view.findViewById(R.id.typeView);
        starts = view.findViewById(R.id.startsView);
        ends = view.findViewById(R.id.endsView);
        icon = view.findViewById(R.id.type_icon);

        carousel=view.findViewById(R.id.carousel_recycler_view);
        carousel.setLayoutManager(new CarouselLayoutManager());
        carousel.setHasFixedSize(true);
        carouselAdapter = new CarouselAdapter(selectedActivityViewModel.getSharedData().getValue().getImgs());
        carousel.setAdapter(carouselAdapter);

        if (selectedActivityViewModel != null && selectedActivityViewModel.getSharedData() != null && selectedActivityViewModel.getSharedData().getValue() != null) {
            title.setText(selectedActivityViewModel.getSharedData().getValue().getTitle());
            desc.setText(selectedActivityViewModel.getSharedData().getValue().getDesc());
            address.setText(selectedActivityViewModel.getSharedData().getValue().getAddress());
            type.setText(selectedActivityViewModel.getSharedData().getValue().getType());
            starts.setText(dateFormat.format(selectedActivityViewModel.getSharedData().getValue().getStarts()).replace(" ", " " + getContext().getResources().getString(R.string.at) + " "));
            ends.setText(dateFormat.format(selectedActivityViewModel.getSharedData().getValue().getEnds()).replace(" ", " " + getContext().getResources().getString(R.string.at) + " "));
            icon.setImageDrawable(selectedActivityViewModel.getSharedData().getValue().getIcon());
            Log.d("type",selectedActivityViewModel.getSharedData().getValue().toString());
            if (selectedActivityViewModel.getSharedData().getValue().getType().equals(getContext().getResources().getString(R.string.travel)))
                mapView.setVisibility(View.VISIBLE);
            else if (selectedActivityViewModel.getSharedData().getValue().getType().equals(getContext().getResources().getString(R.string.freetime)))
                carousel.setVisibility(View.VISIBLE);
            else
                Log.d("type",selectedActivityViewModel.getSharedData().getValue().toString());
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null)

            mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (selectedActivityViewModel != null && selectedActivityViewModel.getSharedData() != null && selectedActivityViewModel.getSharedData().getValue() != null) {
            LatLng markerPosition = new LatLng(selectedActivityViewModel.getSharedData().getValue().getX(), selectedActivityViewModel.getSharedData().getValue().getY());
            marker = googleMap.addMarker(new MarkerOptions().position(markerPosition).draggable(true));

            // Set the camera position to the marker
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 12));
        }

    }

}