package gov.moandor.notepad.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import gov.moandor.notepad.R;

public class EditTitleDialogFragment extends DialogFragment {
    public static final String OLD_TITLE = "old_title";
    
    private OnEditFinishedListener mListener;
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_title, null);
        final EditText editText = (EditText) view.findViewById(R.id.title_edit);
        Bundle args = getArguments();
        if (args != null) {
            editText.setText(args.getString(OLD_TITLE));
        }
        builder.setView(view);
        builder.setTitle(R.string.edit_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mListener != null) {
                    mListener.onEditFinished(editText.getText().toString());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }
    
    public void setOnEditFinishedListener(OnEditFinishedListener l) {
        mListener = l;
    }
    
    public interface OnEditFinishedListener {
        void onEditFinished(String result);
    }
}
