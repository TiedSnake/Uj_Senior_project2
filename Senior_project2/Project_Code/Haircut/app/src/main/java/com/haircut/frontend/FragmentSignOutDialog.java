package com.haircut.frontend;


import static com.google.common.base.Throwables.getRootCause;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.haircut.R;
import com.haircut.backend.Service;

import android.util.Log;

import java.util.concurrent.CompletionException;

public class FragmentSignOutDialog extends DialogFragment {
    private final static String TAG = "fragment_sign_out.java";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_sign_out_dialog, container, false);

        Button signOutPositiveBtn = view.findViewById(R.id.signout_positive);
        Button signOutNegativeBtn = view.findViewById(R.id.signout_negative);

        signOutNegativeBtn.setOnClickListener(v -> {
            //Navigating back into the fragment stack to cancel the process of the sign-out confirmation message after the user clicks on `no`
            requireActivity().getSupportFragmentManager().popBackStack();
            dismiss(); //Closes the dialog box
        });

        signOutPositiveBtn.setOnClickListener(v -> {
            signOutUser();
        });

        return view;
    }

    // Method to handle sign out and navigate to the welcome page
    private void signOutUser() {
        // Perform sign out logic, such as clearing shared preferences or user session
        // After signing out, redirect to the Welcome Page (assuming it's an Activity)
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
