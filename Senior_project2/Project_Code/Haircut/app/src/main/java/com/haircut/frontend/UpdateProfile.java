package com.haircut.frontend;

import static com.google.common.base.Throwables.getRootCause;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;
import com.haircut.R;
import com.haircut.backend.Service;
import com.haircut.backend.User;
import com.haircut.backend.Utility;

import java.util.concurrent.CompletableFuture;

public class UpdateProfile extends Fragment {
    EditText fNameField;
    EditText lNameField;
    EditText emailField;
    Button updateProfileBtn;
    Button updatePasswordBtn;
    TextView errorMessageView;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);
        User user = Service.getCurrentUser();
        Toolbar toolbar = requireActivity().findViewById(R.id.profile_label);
        if (toolbar != null) {
            toolbar.setTitle("Profile");
        }
        fNameField = view.findViewById(R.id.update_profile_f_name);
        lNameField = view.findViewById(R.id.update_profile_l_name);
        emailField = view.findViewById(R.id.update_profile_email_field);

        updateProfileBtn = view.findViewById(R.id.update_profile_btn);
        updatePasswordBtn = view.findViewById(R.id.update_profile_pwd_btn);
        errorMessageView = view.findViewById(R.id.update_profile_errorMessageTextView);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        //Set the text inside the fields to the corresponding user's data.
        fNameField.setText(user.getFirstName());
        lNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());

        TextWatcher fieldsWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String updatedFName = fNameField.getText().toString();
                String updatedLName = lNameField.getText().toString();
                String updatedEmail = emailField.getText().toString();

                if (!Utility.isValidName(updatedFName)) {
                    // Shows the message
                    showMessage(getString(R.string.update_password_error_message5), R.color.red); //set the text to the error message text saved in Strings.xml
                    updateProfileBtn.setVisibility(View.INVISIBLE);
                }else if (Utility.isValidName(updatedFName) && updatedFName.equalsIgnoreCase(user.getFirstName())) {
                    // same first name no update needed
//                    showMessage(getString(R.string.update_password_error_message5), R.color.red); //set the text to the error message text saved in Strings.xml
                    updateProfileBtn.setVisibility(View.INVISIBLE);
                } else if (!Utility.isValidName(updatedLName)) {
                    showMessage(getString(R.string.update_password_error_message6), R.color.red); //set the text to the error message text saved in Strings.xml
                    updateProfileBtn.setVisibility(View.INVISIBLE);
                } else if (Utility.isValidName(updatedLName) && updatedLName.equalsIgnoreCase(user.getLastName())) {
                    // same last name no update needed
//                    showMessage(getString(R.string.update_password_error_message6), R.color.red); //set the text to the error message text saved in Strings.xml
                    updateProfileBtn.setVisibility(View.INVISIBLE);
                } else if (!Utility.isValidEmail(updatedEmail)) {
                    // Hides the error message when passwords match
                    showMessage(getString(R.string.update_password_error_message7), R.color.red); //set the text to the error message text saved in Strings.xml
                    updateProfileBtn.setVisibility(View.INVISIBLE);
                } else if (Utility.isValidEmail(updatedEmail) && updatedEmail.equalsIgnoreCase(user.getEmail())) {
                    // Hides the error message when passwords match
//                    showMessage(getString(R.string.update_password_error_message7), R.color.red); //set the text to the error message text saved in Strings.xml
                    updateProfileBtn.setVisibility(View.INVISIBLE);
                } else {
                    clearMessage();
                    updateProfileBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        fNameField.addTextChangedListener(fieldsWatcher);
        lNameField.addTextChangedListener(fieldsWatcher);
        emailField.addTextChangedListener(fieldsWatcher);

        // Handle update button to update profile;
        updateProfileBtn.setEnabled(true);
        updateProfileBtn.setOnClickListener(v -> {
            updateProfileBtn.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            String fName = fNameField.getText().toString();
            String lName = lNameField.getText().toString();
            String email = emailField.getText().toString();

            user.setFirstName(fName);
            user.setLastName(lName);
            user.setEmail(email);
            updateUserEmail(email, Service.getCurrentAuth().getCurrentUser()).thenApply(isUpdated -> {
                if (!isUpdated)

                    throw new RuntimeException("Failed to update email.");
                return Service.persistUser(user);
            }).thenAccept(isPersisted -> {
                progressBar.setVisibility(View.INVISIBLE);
                updateProfileBtn.setEnabled(true);
                showMessage(getString(R.string.update_password_success_message), R.color.green, true); //set the text to the error message text saved in Strings.xml
            }).exceptionally(throwable -> {
                Throwable ex = getRootCause(throwable);
                showMessage(throwable.getMessage(), R.color.red, true);
                updateProfileBtn.setEnabled(true);
                return null;
            });

//            // Perform the update logic here (e.g., save profile information)
//            Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
//
//            // After updating, pop the current fragment and go back to barber_page
//            requireActivity().getSupportFragmentManager().popBackStack();
        });

        updatePasswordBtn.setOnClickListener(v2 -> {
                updatePasswordDialog passwordDialog = new updatePasswordDialog();
                passwordDialog.show(getParentFragmentManager(), "updatePasswordDialog");
            });

        return view;
    }

    private CompletableFuture<Boolean> updateUserEmail(String email, FirebaseUser fUser) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        if (fUser != null && email != null) {
            fUser.verifyBeforeUpdateEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    future.complete(true);
                } else {
                    Log.e("updateUserEmail", "Update email failed: " + task.getException().getMessage());
                    future.completeExceptionally(new Exception(task.getException()));
                }
            });
        } else {
            Log.e("updateUserEmail", "User not logged in or email unavailable");
            future.completeExceptionally(new Exception("User not logged in or email unavailable"));
        }
        return future;
    }

    private void updateCurrentPassword(User user, String fName, String lName, String email) {

    }

    private void clearMessage() {
        errorMessageView.setText("");
        errorMessageView.setVisibility(View.INVISIBLE);
    }

    private void showMessage(String message, int colorRes, boolean... isDelayed) {
        boolean delay = isDelayed.length > 0 && isDelayed[0];
        progressBar.setVisibility(View.INVISIBLE);
        Context context = getContext();
        if (context != null) {
            errorMessageView.setText(message);
            errorMessageView.setTextColor(ContextCompat.getColor(context, colorRes)); // Use dialog's context
            errorMessageView.setVisibility(View.VISIBLE);
            if (delay) {
                errorMessageView.postDelayed(this::clearMessage, 2000);
            }
        } else {
            Log.e("showMessage", "Context is null, cannot update UI");
        }
    }
}
