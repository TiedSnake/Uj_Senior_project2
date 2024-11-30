package com.haircut.frontend;


import static com.google.common.base.Throwables.getRootCause;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.haircut.R;
import com.haircut.backend.Service;

public class FragmentSignOutDialog extends DialogFragment {
    private final static String TAG = "fragment_sign_out.java";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // We create a Dialog using an AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Inflate the custom view for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_sign_out_dialog, null);

        // Find the buttons in the inflated layout
        Button signOutPositiveBtn = view.findViewById(R.id.signout_positive); //signout "yes"
        Button signOutNegativeBtn = view.findViewById(R.id.signout_negative); //signout "no"

        // Handle 'No' (negative) button click (dismiss the dialog)
        signOutNegativeBtn.setOnClickListener(v -> dismiss());

        // Handle 'Yes' (positive) button click (perform sign-out)
        signOutPositiveBtn.setOnClickListener(v -> signOutUser());

        // Set the custom view to the dialog
        builder.setView(view)
                .setCancelable(false); // Fixing the bug when clicking outside the emulator, by making the dialog non-cancelable by touching outside

        // Create the dialog
        AlertDialog dialog = builder.create();

        return dialog;
    }

    // Method to handle sign out and navigate to the welcome page
    private void signOutUser() {
        // After signing out, redirect to the Welcome Page
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            Intent intent = new Intent(activity, WelcomePage.class);
            Service.signout().thenAccept(isSignedOut -> {
                if (isSignedOut) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    dismiss();
                } else {
                    Log.e(TAG, "error while signing out");
                }
            }).exceptionally(ex -> {
                Throwable rootCause = getRootCause(ex);
                Log.e(TAG, "Error: failed to signout in the system due to:\n", rootCause);
                return null;
            });
        }else
            Log.e(TAG, "Fragment not attached to Activity. Cannot proceed with signout.");
    }
}
