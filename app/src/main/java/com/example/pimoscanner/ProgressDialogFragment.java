package com.example.pimoscanner;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class ProgressDialogFragment extends DialogFragment {
    private static final String KEY_PROGRESS_MESSAGE = "KEY_PROGRESS_MESSAGE";

    private DialogInterface.OnCancelListener mOnCancelListener;

    public ProgressDialogFragment() {
    }

    /**
     * Creates a new instance of ProgressDialogFragment.
     *
     * @param message
     *            the progress message
     * @return an instance of ProgressDialogFragment
     */
    public static ProgressDialogFragment newInstance(String message) {
        ProgressDialogFragment dlg = new ProgressDialogFragment();

        Bundle args = new Bundle();
        args.putString(KEY_PROGRESS_MESSAGE, message);

        dlg.setArguments(args);
        return dlg;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String msg = args.getString(KEY_PROGRESS_MESSAGE);

        ProgressDialog progressDlg = new ProgressDialog(getActivity());
        progressDlg.setIndeterminate(true);
        progressDlg.setMessage(msg);
        progressDlg.setCanceledOnTouchOutside(false);

        return progressDlg;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // check if the host activity or fragment implements OnCancelListener
        //
        Fragment targetFragment = getTargetFragment();
        if (targetFragment instanceof DialogInterface.OnCancelListener) {
            mOnCancelListener = (DialogInterface.OnCancelListener) targetFragment;
        } else if (activity instanceof DialogInterface.OnCancelListener) {
            mOnCancelListener = (DialogInterface.OnCancelListener) activity;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);

        if (mOnCancelListener != null) {
            mOnCancelListener.onCancel(dialog);
        }
    }
}
