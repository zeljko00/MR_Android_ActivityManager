package etf.mr.project.activitymanager.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;

import etf.mr.project.activitymanager.MainActivity;
import etf.mr.project.activitymanager.R;
import etf.mr.project.activitymanager.model.CoordTuple;
import etf.mr.project.activitymanager.viewmodel.SelectedActivityViewModel;
import etf.mr.project.activitymanager.viewmodel.SharedViewModel;

public class LocationPickerFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private SharedViewModel sharedViewModel;
    private View view;
    private MapView mapView;
    private GoogleMap googleMap;

    private Marker marker;

    private double lat = 44.766769914889494;
    private double lng = 17.18670582069851;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LocationPickerFragment() {
        // Required empty public constructor
    }

    public static LocationPickerFragment newInstance(String param1, String param2) {
        LocationPickerFragment fragment = new LocationPickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_location_picker, container, false);

        mapView = view.findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        MaterialButton save = view.findViewById(R.id.save);
        save.setOnClickListener((View v) -> {

            CoordTuple tuple = new CoordTuple();
            tuple.setLat(lat);
            tuple.setLng(lng);
            sharedViewModel.setSharedData(tuple);
            Navigation.findNavController(v).navigate(R.id.action_navigation_location_picker_to_navigation_new_activity);
        });

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

        // Add a marker at a specific location
        LatLng markerPosition = new LatLng(44.766769914889494, 17.18670582069851);
        marker = googleMap.addMarker(new MarkerOptions().position(markerPosition).draggable(true));

        // Set the camera position to the marker
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerPosition, 12));

        // Set the marker drag listener
        googleMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        // Called when the marker drag starts
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        // Called while the marker is being dragged
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        // Called when the marker drag ends
        LatLng position = marker.getPosition();
        lat = position.latitude;
        lng = position.longitude;
    }
}