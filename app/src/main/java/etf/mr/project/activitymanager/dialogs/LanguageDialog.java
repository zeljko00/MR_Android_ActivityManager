package etf.mr.project.activitymanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import etf.mr.project.activitymanager.interfaces.LngChangeListenerInterface;
import etf.mr.project.activitymanager.R;

public class LanguageDialog extends DialogFragment {

    private String selectedLng;
    private LngChangeListenerInterface handler;

    public void setLngChangeListenerInterface(LngChangeListenerInterface handler) {
        this.handler = handler;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = requireContext().getResources().getStringArray(R.array.lngs);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.lng_select))
                .setSingleChoiceItems(items,-1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedItem = items[which];
                        selectedLng = selectedItem;
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (selectedLng != null)
            handler.change(selectedLng);
    }
}

