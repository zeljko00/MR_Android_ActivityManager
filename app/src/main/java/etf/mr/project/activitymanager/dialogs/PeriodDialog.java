package etf.mr.project.activitymanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import etf.mr.project.activitymanager.interfaces.PeriodChangeListenerInterface;
import etf.mr.project.activitymanager.R;

public class PeriodDialog extends DialogFragment {
    private PeriodChangeListenerInterface handler;

    public void setPeriodChangeListenerInterface(PeriodChangeListenerInterface handler) {
        this.handler = handler;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items =requireContext().getResources().getStringArray(R.array.period_values);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.period_select))
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String selectedItem = items[which];
                        if(handler!=null)
                            handler.change(selectedItem);
                    }
                });
        return builder.create();
    }
}
