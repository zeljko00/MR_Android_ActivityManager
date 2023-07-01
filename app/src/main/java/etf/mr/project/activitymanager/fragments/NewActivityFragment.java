package etf.mr.project.activitymanager.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import etf.mr.project.activitymanager.MainActivity;
import etf.mr.project.activitymanager.R;
import etf.mr.project.activitymanager.model.Activity;
import etf.mr.project.activitymanager.viewmodel.SharedViewModel;


public class NewActivityFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int REQUEST_IMAGE_PICK = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final DateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private View view;
    private EditText title;
    private EditText desc;
    private EditText type;
    private EditText startDate;
    private EditText endDate;
    private EditText startTime;
    private EditText endTime;
    private Uri takenPhoto;
    private Activity activity;
    private String path;
    private double lat = 44.766769914889494;
    private double lng = 17.18670582069851;

    private Geocoder geocoder;
    private SharedViewModel sharedViewModel;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewActivityFragment newInstance(String param1, String param2) {
        NewActivityFragment fragment = new NewActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedViewModel = ((MainActivity) requireActivity()).getSharedViewModel();
        geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_activity, container, false);
        AutoCompleteTextView spinner = view.findViewById(R.id.comboBox);
        String[] types = getContext().getResources().getStringArray(R.array.types);
        ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_dropdown_item_1line, types);
        spinner.setAdapter(adapter);

        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

            }
        });
        startDate = view.findViewById(R.id.editStartDate);
        startDate.setOnClickListener((View v) -> showDatePickerDialog(v, R.id.editStartDate));
        endDate = view.findViewById(R.id.editEndDate);
        endDate.setOnClickListener((View v) -> showDatePickerDialog(v, R.id.editEndDate));
        startTime = view.findViewById(R.id.editStartTime);
        startTime.setOnClickListener((View v) -> showTimePickerDialog(v, R.id.editStartTime));
        endTime = view.findViewById(R.id.editEndTime);
        endTime.setOnClickListener((View v) -> showTimePickerDialog(v, R.id.editEndTime));
        title = view.findViewById(R.id.title_input_field);
        desc = view.findViewById(R.id.desc_input_field);
        type = view.findViewById(R.id.comboBox);
        MaterialButton submit = view.findViewById(R.id.submit);
        submit.setOnClickListener((View v) -> {
            if (!checkInputData())
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.fill), Toast.LENGTH_SHORT).show();
            activity = collectData();
            Log.d("submit", activity.toString());
            Navigation.findNavController(v).navigate(R.id.action_navigation_new_activity_to_navigation_list);
        });

        MaterialButton upload = view.findViewById(R.id.upload);
        upload.setOnClickListener((View v) -> {
            pickImage();
        });
        MaterialButton take = view.findViewById(R.id.take);
        take.setOnClickListener((View v) -> {
//            dispatchTakePictureIntent();
        });

        List<TextInputLayout> tils = new ArrayList<>();
        tils.add(view.findViewById(R.id.title_input));
        tils.add(view.findViewById(R.id.desc_input));

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if (dpWidth > 500) {
            tils.stream().forEach(l -> {
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) l.getLayoutParams();
                layoutParams.width = 800;
                l.setLayoutParams(layoutParams);
            });
        }

        return view;
    }


    public void showDatePickerDialog(View v, int fieldID) {
        // Create a Calendar instance to get the current date
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a DatePickerDialog and set the initial date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (vv, selectedYear, selectedMonth, selectedDay) -> {
                    EditText editTextDate = view.findViewById(fieldID);
                    String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                    editTextDate.setText(selectedDate);
                },
                year, month, day);

        // Show the date picker dialog
        datePickerDialog.show();
    }

    public void showTimePickerDialog(View v, int fieldID) {
        // Create a Calendar instance to get the current date
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Create a DatePickerDialog and set the initial date
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (vv, selectedHour, selectedMinute) -> {
                    EditText editTextDate = view.findViewById(fieldID);
                    String selectedDate = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute);
                    editTextDate.setText(selectedDate);
                },
                hour, minute, false);

        // Show the date picker dialog
        timePickerDialog.show();
    }

    private Activity collectData() {
        Activity activity = new Activity();
        activity.setTitle(title.getText().toString());
        activity.setDesc(desc.getText().toString());
        activity.setType(type.getText().toString());

        if (sharedViewModel.getSharedData().getValue() != null) {
            activity.setX(sharedViewModel.getSharedData().getValue().getLat());
            activity.setY(sharedViewModel.getSharedData().getValue().getLng());
        } else {
            activity.setX(lat);
            activity.setY(lng);
        }

        try {
            List<Address> addresses = geocoder.getFromLocation(activity.getX(), activity.getY(), 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                // Access the address components
                String addressLine = address.getAddressLine(0); // Full address
                activity.setAddress(addressLine);

            }
        } catch (IOException e) {
            e.printStackTrace();
            activity.setAddress("unknown");
        }
        try {
            activity.setStarts(dateFormater.parse(startDate.getText().toString() + " " + startTime.getText().toString()));
        } catch (Exception e) {
            activity.setStarts(new Date());
        }
        try {
            activity.setEnds(dateFormater.parse(endDate.getText().toString() + " " + endTime.getText().toString()));
        } catch (Exception e) {
            activity.setEnds(new Date());
        }
        Log.d("location", sharedViewModel.getSharedData().getValue().getLat() + " " + sharedViewModel.getSharedData().getValue().getLng());
        return activity;
    }

    private boolean checkInputData() {
        if (title.getText().toString().equals("") || type.getText().toString().equals("") || desc.getText().toString().equals("") || startDate.getText().toString().equals("") || startTime.getText().toString().equals("") || endDate.getText().toString().equals("") || endTime.getText().toString().equals(""))
            return false;
        else return true;
    }

    private void pickImage() {
        Toast.makeText(getContext(),"picking",Toast.LENGTH_SHORT);
        Log.d("xxxx","picking");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(getContext(),"Hi from new",Toast.LENGTH_SHORT);
//        Log.d("taken","request "+requestCode);
//        Log.d("taken","result "+resultCode);
//        super.onActivityResult(requestCode, resultCode, data);
////        Log.d("taken",data.toString());
//        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
//            if (data != null && data.getData() != null) {
//                // Get the selected image URI
//                Uri imageUri = data.getData();
//                Log.d("image", imageUri.toString());
////                view.findViewById(R.id.uploaded).setVisibility(View.VISIBLE);
//            }
//        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            // Use the captured photo data
//            if (takenPhoto != null) {
//                Log.d("taken",takenPhoto.toString());
////                Bundle extras = data.getExtras();
////                Bitmap imageBitmap = (Bitmap) extras.get("data");
////                Log.d("taken", imageBitmap.toString());
//            galleryAddPic();
//            }
//
//            // Do something with the photo
//            // ...
//        }
    }
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(path);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        getActivity().sendBroadcast(mediaScanIntent);
//    }
//    private void dispatchTakePictureIntent() {
//        try {
//            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//                // Create a file to save the photo
//                File photoFile = createPhotoFile();
//
//                if (photoFile != null) {
//                    // Get the file URI
//                takenPhoto = FileProvider.getUriForFile(getContext(),
//                        "etf.mr.project.activitymanager", photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, takenPhoto);
//
//                    // Start the camera intent
//                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//    }

//    private File createPhotoFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File imageFile = File.createTempFile(
//                imageFileName,  // prefix
//                ".jpg",         // suffix
//                storageDir      // directory
//        );
//        path=imageFile.getAbsolutePath();
//        return imageFile;
//    }
}