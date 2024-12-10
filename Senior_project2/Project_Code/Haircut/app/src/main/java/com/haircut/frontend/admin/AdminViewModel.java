package com.haircut.frontend.admin;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminViewModel extends ViewModel {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("schema/users");

    public final MutableLiveData<List<FirebaseUser>> users = new MutableLiveData<>();
    public final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void getUsersFromFirebase() {
        fetchAppointmentsFromFirebase();
    }

    private void fetchAppointmentsFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<FirebaseUser> appointmentsList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    FirebaseUser appointment = data.getValue(FirebaseUser.class);
                    if (appointment != null) {
                        Object isBlocked = data.child("isBlocked").getValue();
                        appointment.setIsBlocked(isBlocked);
                        appointmentsList.add(appointment);
                    }
                }

                users.postValue(appointmentsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorMessage.postValue(error.getMessage());
            }
        });
    }

    public void updateIsBlocked(String id, boolean isBlocked) {
        List<FirebaseUser> usersList = users.getValue();
        if (usersList == null) {
            return;
        }

        FirebaseUser appointment = null;
        for (FirebaseUser user : usersList) {
            if (user.getUuid().equals(id)) {
                appointment = user;
                break;
            }
        }

        if (appointment == null) {
            return;
        }

        String appointmentId = appointment.getUuid();

        Map<String, Object> updates = new HashMap<>();
        updates.put("isBlocked", isBlocked);

        databaseReference.child(appointmentId).updateChildren(updates)
                .addOnSuccessListener(unused -> getUsersFromFirebase())
                .addOnFailureListener(exception ->
                        errorMessage.postValue("Failed to update: " + exception.getMessage())
                );
    }
}

