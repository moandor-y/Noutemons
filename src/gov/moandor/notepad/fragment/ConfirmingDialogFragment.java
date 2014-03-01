package gov.moandor.notepad.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import gov.moandor.notepad.R;

public class ConfirmingDialogFragment extends DialogFragment {
    public static final String MESSAGE = "message";
    
    private OnConfirmListener mListener;
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle args = getArguments();
        String message = args.getString(MESSAGE);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int witch) {
                if (mListener != null) {
                    mListener.onConfirm();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }
    
    public void setOnConfirmListener(OnConfirmListener l) {
        mListener = l;
    }
    
    public static interface OnConfirmListener {
        public void onConfirm();
    }
}
