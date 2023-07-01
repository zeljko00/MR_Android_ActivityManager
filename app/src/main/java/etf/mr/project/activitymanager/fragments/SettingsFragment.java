package etf.mr.project.activitymanager.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;

import etf.mr.project.activitymanager.interfaces.LngChangeListenerInterface;
import etf.mr.project.activitymanager.interfaces.PeriodChangeListenerInterface;
import etf.mr.project.activitymanager.R;
import etf.mr.project.activitymanager.dialogs.LanguageDialog;
import etf.mr.project.activitymanager.dialogs.PeriodDialog;

public class SettingsFragment extends Fragment{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static final String NOTIFICATIONS_PERIOD = "Notifications.Period";
    private static final String NOTIFICATIONS_ENABLED = "Notifications.Enabled";
    private String mParam1;
    private String mParam2;
    private View rootView;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        rootView=inflater.inflate(R.layout.fragment_settings, container, false);

        TextView lngLbl = rootView.findViewById(R.id.lng_lbl);
        lngLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLanguageDialog();
            }
        });
        TextView periodLbl = rootView.findViewById(R.id.period_lbl);
        periodLbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPeriodDialog();
            }
        });
        Switch switchBtn = rootView.findViewById(R.id.switchBtn);
        switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    saveNotificationsEnabled(isChecked);
            }
        });


        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String lang = sharedPreferences.getString(SELECTED_LANGUAGE, getActivity().getResources().getString(R.string.eng_value));
            String langLabel=lang.equals(getActivity().getResources().getString(R.string.eng_value))?getActivity().getResources().getString(R.string.eng):getActivity().getResources().getString(R.string.srb);
            String period=sharedPreferences.getString(NOTIFICATIONS_PERIOD, getActivity().getResources().getStringArray(R.array.period_values)[0]);
            String periodLabel;
            if(period.equals(getActivity().getResources().getString(R.string.p1d_value)))
                periodLabel=getActivity().getResources().getString(R.string.p1d);
            else if(period.equals(getActivity().getResources().getString(R.string.p1w_value)))
                periodLabel=getActivity().getResources().getString(R.string.p1w);
            else
                periodLabel=getActivity().getResources().getString(R.string.p1h);
            boolean enabled=sharedPreferences.getBoolean(NOTIFICATIONS_ENABLED,true);
            changeSelectedLng(langLabel);
            changeSelectedPeriod(periodLabel);
            changeEnabled(enabled);
        }catch(Exception e){
        }
        return rootView;

    }
    private void changeSelectedLng(String lng){
        ((TextView)rootView.findViewById(R.id.lng)).setText(lng);
    }
    private void changeSelectedPeriod(String period){
        ((TextView)rootView.findViewById(R.id.period)).setText(period);
    }
    private void changeEnabled(boolean enabled){
        Switch switchBtn = rootView.findViewById(R.id.switchBtn);
        switchBtn.setChecked(enabled);
    }
    private void saveSelectedLng(String lng){
        SharedPreferences shPreferences =PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = shPreferences.edit();
        String lngValue=lng.equals(getActivity().getResources().getString(R.string.eng))?getActivity().getResources().getString(R.string.eng_value):getActivity().getResources().getString(R.string.srb_value);
        Log.d("lng","selected value="+lng);
        Log.d("lng","value to save="+lngValue);
        editor.putString(SELECTED_LANGUAGE,lngValue);
        editor.apply();
        changeConfig(lngValue);
    }
    private void changeConfig(String lng){
        Locale locale = new Locale(lng);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getActivity().getBaseContext().getResources().updateConfiguration(config,
                getActivity().getBaseContext().getResources().getDisplayMetrics());
        getActivity().recreate();
    }
    private void saveSelectedPeriod(String period){
        SharedPreferences shPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = shPreferences.edit();
        String periodValue;
        if(period.equals(getActivity().getResources().getString(R.string.p1d)))
            periodValue=getActivity().getResources().getString(R.string.p1d_value);
        else if(period.equals(getActivity().getResources().getString(R.string.p1w)))
            periodValue=getActivity().getResources().getString(R.string.p1w_value);
        else
            periodValue=getActivity().getResources().getString(R.string.p1h_value);
        Log.d("period","selected value="+period);
        Log.d("period","value to save="+periodValue);
        editor.putString(NOTIFICATIONS_PERIOD, periodValue);
        editor.apply();
    }
    private void saveNotificationsEnabled(boolean enabled){
        SharedPreferences shPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = shPreferences.edit();
        editor.putBoolean(NOTIFICATIONS_ENABLED, enabled);
        editor.apply();
    }
    private void showLanguageDialog() {
        LanguageDialog dialogFragment = new LanguageDialog();
        dialogFragment.setLngChangeListenerInterface(new LngChangeListenerInterface() {
            @Override
            public void change(String lng) {
                saveSelectedLng(lng);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "LanguageDialog");
    }
    private void showPeriodDialog() {
        PeriodDialog dialogFragment = new PeriodDialog();
        dialogFragment.setPeriodChangeListenerInterface(new PeriodChangeListenerInterface() {
            @Override
            public void change(String period) {
                changeSelectedPeriod(period);
                saveSelectedPeriod(period);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "PeriodDialog");
    }

}