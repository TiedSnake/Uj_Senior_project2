package com.haircut.frontend.shared;

import static com.google.common.base.Throwables.getRootCause;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.haircut.R;
import com.haircut.backend.Service;
import com.haircut.backend.Utility;

import java.util.concurrent.CompletableFuture;

public class updatePasswordDialog extends DialogFragment {
    EditText passwordField;
    EditText newPasswordField;
    EditText retypedPasswordField;
    TextView errorMessageView;
    Button verifyPasswordBtn;
    Button updatePasswordBtn;
    LinearLayout currentPasswordBar;
    LinearLayout newPasswordBar;
    LinearLayout retypedPasswordBar;
    LinearLayout verifyPasswordButtonBar;
    LinearLayout updatePasswordButtonBar;
    ProgressBar progressBar;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_password_dialog, null);

        builder.setView(view).setCancelable(false); // Fixing the bug when clicking outside the emulator, by making the dialog non-cancelable by touching outside

        passwordField = view.findViewById(R.id.update_profile_password_field);
        newPasswordField = view.findViewById(R.id.update_profile_new_password_field);
        retypedPasswordField = view.findViewById(R.id.update_profile_retype_password_field);

        currentPasswordBar = view.findViewById(R.id.current_password_bar);
        newPasswordBar = view.findViewById(R.id.new_password_bar);
        retypedPasswordBar = view.findViewById(R.id.retyped_password_bar);
        verifyPasswordButtonBar = view.findViewById(R.id.verify_password_button_bar);
        updatePasswordButtonBar = view.findViewById(R.id.update_password_button_bar);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


        verifyPasswordBtn = view.findViewById(R.id.verify_pwd_btn); // update password
        updatePasswordBtn = view.findViewById(R.id.update_pwd_btn); // update password
        errorMessageView = view.findViewById(R.id.errorMessageTextView); //error message


        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPassword = newPasswordField.getText().toString();
                String retypedPassword = retypedPasswordField.getText().toString();

                if (!newPassword.equals(retypedPassword)) {
                    // Shows the message
                    showMessage(getString(R.string.update_password_error_message2), R.color.red); //set the text to the error message text saved in Strings.xml
                    updatePasswordButtonBar.setVisibility(View.INVISIBLE);
                } else if (!Utility.isValidPassword(newPassword)) {
                    showMessage(getString(R.string.update_password_error_message3), R.color.red); //set the text to the error message text saved in Strings.xml
                    updatePasswordButtonBar.setVisibility(View.INVISIBLE);
                } else {
                    // Hides the error message when passwords match
                    clearMessage();
                    updatePasswordButtonBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };


        verifyPasswordBtn.setOnClickListener(v -> {
            currentPasswordBar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            String currentPassword = passwordField.getText().toString();
            if (Utility.isValidPassword(currentPassword)) {
                verifyCurrentPassword(Service.getCurrentAuth(), currentPassword).thenAccept(firebaseUser -> {
                    currentPasswordBar.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    verifyPasswordButtonBar.setVisibility(View.INVISIBLE);
                    newPasswordBar.setVisibility(View.VISIBLE);
                    retypedPasswordBar.setVisibility(View.VISIBLE);
                    updatePasswordButtonBar.setVisibility(View.VISIBLE);
                    newPasswordField.addTextChangedListener(passwordWatcher);
                    retypedPasswordField.addTextChangedListener(passwordWatcher);
                    updatePasswordBtn.setOnClickListener(v2 -> {
                        String newPassword = newPasswordField.getText().toString();
                        updateCurrentPassword(firebaseUser, newPassword);
                    });
                }).exceptionally(throwable -> {if (throwable != null) {
                        Throwable ex = getRootCause(throwable);
                        String error = Utility.errorMessage(ex);

//                        if (ex instanceof FirebaseAuthException) {
                        showMessage(error, R.color.red, true);
//                        } else {
//                            showMessage(throwable.getMessage(), R.color.red, true);
//                            System.out.println("Error: " + throwable.getMessage());
//                        }
                    }
                    return null;
                });
            } else {
                showMessage(getString(R.string.update_password_error_message3), R.color.red, true);
            }
        });
        // Create the dialog
        AlertDialog dialog = builder.create();
        return dialog;
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

    private void clearMessage() {
        errorMessageView.setText("");
        errorMessageView.setVisibility(View.INVISIBLE);
    }

    private void updateCurrentPassword(FirebaseUser user, String newPassword) {
        // if Reauthentication successful, update the password
        if (user != null) {
            currentPasswordBar.setVisibility(View.INVISIBLE);
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Password update successful
                    showMessage(getString(R.string.update_password_success_message), R.color.green, true); //set the text to the error message text saved in Strings.xml
                    dismiss();
                } else {
                    // Password update failed
//                    future.completeExceptionally(new Exception((FirebaseAuthException) task.getException()));
                    showMessage(getString(R.string.update_password_error_message4), R.color.red, true); //set the text to the error message text saved in Strings.xml
                }
            });
        } else {
            System.out.println("User is null, can't update the password");
            showMessage("User object is null can't update the password", R.color.red, true); //set the text to the error message text saved in Strings.xml

        }
    }

    private CompletableFuture<FirebaseUser> verifyCurrentPassword(FirebaseAuth auth, String currentPassword) {
        CompletableFuture<FirebaseUser> future = new CompletableFuture<>();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null && currentUser.getEmail() != null) {
            String email = currentUser.getEmail();
            Log.d("VerifyPassword", "Current User Email: " + email);

            // Get currentUser credentials using email & password
            AuthCredential credential = EmailAuthProvider.getCredential(email, currentPassword);

            // Reauthenticate the currentUser
            currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("VerifyPassword", "Reauthentication successful.");
                    future.complete(currentUser);
                } else {
                    Log.e("VerifyPassword", "Reauthentication failed: " + task.getException().getMessage());
                    future.completeExceptionally(new Exception(task.getException()));
                }
            });
        } else {
            Log.e("VerifyPassword", "User not logged in or email unavailable");
            future.completeExceptionally(new Exception("User not logged in or email unavailable"));
        }
        return future;
    }


}
