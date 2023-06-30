package etf.mr.project.activitymanager.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import etf.mr.project.activitymanager.interfaces.DeleteActivityInterface;
import etf.mr.project.activitymanager.R;

public class DeleteActivityDialog extends DialogFragment {

    private long activityID;
    private DeleteActivityInterface handler;

    public DeleteActivityDialog() {
    }

    public DeleteActivityDialog(long activityID, DeleteActivityInterface handler) {
        this.activityID = activityID;
        this.handler = handler;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.sure))
                .setPositiveButton(R.string.delete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(getContext(), "Deleting id=" + activityID, Toast.LENGTH_SHORT);
                                handler.delete(activityID);
                            }
                        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        return builder.create();
    }
}
