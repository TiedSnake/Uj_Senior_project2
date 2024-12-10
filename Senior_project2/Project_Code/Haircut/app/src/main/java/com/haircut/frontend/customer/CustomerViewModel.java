package com.haircut.frontend.customer;

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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerViewModel extends ViewModel {

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("schema/appointments");
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MutableLiveData<List<Appointment>> appointments = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void getAppointments() {
        executor.execute(() -> {
            try {
                List<Appointment> appointmentsList = fetchAppointmentsFromFirebase();
                appointments.postValue(appointmentsList);
            } catch (Exception e) {
                errorMessage.postValue(e.getMessage());
            }
        });
    }

    private List<Appointment> fetchAppointmentsFromFirebase() throws Exception {
        List<Appointment> appointmentsList = new ArrayList<>();

        DataSnapshot snapshot = fetchSnapshotFromFirebase();

        for (DataSnapshot data : snapshot.getChildren()) {
            Appointment appointment = data.getValue(Appointment.class);
            if (appointment != null) {
                appointment.setIsReserved(data.child("isReserved").getValue());
                appointmentsList.add(appointment);
            }
        }

        return appointmentsList;
    }

    private DataSnapshot fetchSnapshotFromFirebase() throws Exception {
        final Object lock = new Object();
        final List<DataSnapshot> result = new ArrayList<>();
        final List<Exception> error = new ArrayList<>();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                synchronized (lock) {
                    result.add(snapshot);
                    lock.notify();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                synchronized (lock) {
                    error.add(new Exception(databaseError.getMessage()));
                    lock.notify();
                }
            }
        });

        synchronized (lock) {
            lock.wait();
        }

        if (!error.isEmpty()) {
            throw error.get(0);
        }

        return result.get(0);
    }

    public void updateIsReserved(int index, boolean isReserved, String name) {
        executor.execute(() -> {
            try {
                List<Appointment> appointmentsList = appointments.getValue();
                if (appointmentsList == null || index < 0 || index >= appointmentsList.size()) return;

                Appointment appointment = appointmentsList.get(index);
                String appointmentId = String.valueOf(appointment.getId());

                Map<String, Object> updates = new HashMap<>();
                updates.put("isReserved", isReserved);
                updates.put("customer", name);

                databaseReference.child(appointmentId).updateChildren(updates)
                        .addOnSuccessListener(aVoid -> getAppointments())
                        .addOnFailureListener(e -> errorMessage.postValue("Failed to update: " + e.getMessage()));
            } catch (Exception e) {
                errorMessage.postValue("Error: " + e.getMessage());
            }
        });
    }

    public void declineAppointment(int id) {
        executor.execute(() -> {
            try {
                List<Appointment> appointmentsList = appointments.getValue();
                if (appointmentsList == null) return;

                Appointment appointment = null;
                for (Appointment appt : appointmentsList) {
                    if (Integer.parseInt(String.valueOf(appt.getId())) == id) {
                        appointment = appt;
                        break;
                    }
                }

                if (appointment == null) return;

                String appointmentId = String.valueOf(appointment.getId());

                Map<String, Object> updates = new HashMap<>();
                updates.put("isReserved", false);
                updates.put("customer", "");

                databaseReference.child(appointmentId).updateChildren(updates)
                        .addOnSuccessListener(aVoid -> getAppointments())
                        .addOnFailureListener(e -> errorMessage.postValue("Failed to update: " + e.getMessage()));
            } catch (Exception e) {
                errorMessage.postValue("Error: " + e.getMessage());
            }
        });
    }
}

