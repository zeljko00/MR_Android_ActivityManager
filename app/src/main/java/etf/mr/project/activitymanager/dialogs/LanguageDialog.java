package etf.mr.project.activitymanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import etf.mr.project.activitymanager.LngChangeListenerInterface;
import etf.mr.project.activitymanager.R;

public class LanguageDialog extends DialogFragment {

    private LngChangeListenerInterface lngChangeListenerInterface;

    public void setLngChangeListenerInterface(LngChangeListenerInterface lngChangeListenerInterface) {
        this.lngChangeListenerInterface = lngChangeListenerInterface;
    }
    private String selectedLng;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items =requireContext().getResources().getStringArray(R.array.lngs);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.lng_select))
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedItem = items[which];
                        selectedLng=selectedItem;
                    }
                });
        return builder.create();
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(selectedLng!=null)
            lngChangeListenerInterface.change(selectedLng);
    }
}

