package com.example.haircut;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class BookAppointment extends AppCompatActivity {

    private TextView selectedDate;
    private TextView confirmationMessage;
    private LinearLayout timeSlotsContainer;
    private ArrayList<String> availableTimes;
    private String selectedTime = "";  // To track the selected time

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_appointment);  // Set to the book_appointment layout

        // Find views by ID
        ImageButton goBackButton = findViewById(R.id.go_back_button);
        CalendarView calendarView = findViewById(R.id.calendar_view);
        selectedDate = findViewById(R.id.selected_date);
        TextView availableTimesLabel = findViewById(R.id.available_times_label);
        timeSlotsContainer = findViewById(R.id.time_slots_container);
        Button bookAppointmentButton = findViewById(R.id.book_appointment_button);
        confirmationMessage = findViewById(R.id.confirmation_message);

        // Set up the CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month + 1) + "/" + year; // Adjusting month
            selectedDate.setText("Selected Date: " + date);
            selectedTime = "";  // Reset the selected time when a new date is picked
            showAvailableTimes(); // Show available times when a date is selected
        });

        // Go Back Button functionality
        goBackButton.setOnClickListener(v -> finish()); // Go back to the previous activity

        // Book Appointment Button functionality
        bookAppointmentButton.setOnClickListener(v -> {
            String date = selectedDate.getText().toString();
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            } else {
                confirmationMessage.setText("Reservation completed for " + date + " at " + selectedTime);
                confirmationMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    // Simulated method to show available times
    private void showAvailableTimes() {
        // Clear previous time slots
        timeSlotsContainer.removeAllViews();
        timeSlotsContainer.setVisibility(View.VISIBLE);

        // Simulate available times
        availableTimes = new ArrayList<>();
        availableTimes.add("10:00 AM");
        availableTimes.add("11:00 AM");
        availableTimes.add("1:00 PM");
        availableTimes.add("2:00 PM");

        // Add time slots to the layout
        for (String time : availableTimes) {
            Button timeSlotButton = new Button(this);
            timeSlotButton.setText(time);
            timeSlotButton.setTag(time); // Set a tag to identify the button
            timeSlotButton.setOnClickListener(v -> selectTime(timeSlotButton, time));
            timeSlotsContainer.addView(timeSlotButton);
        }
    }

    private void selectTime(Button selectedButton, String time) {
        // Deselect all buttons first
        for (int i = 0; i < timeSlotsContainer.getChildCount(); i++) {
            Button button = (Button) timeSlotsContainer.getChildAt(i);
            button.setEnabled(true); // Enable all buttons
            button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Reset background color
        }

        // Highlight the selected button and disable it
        selectedButton.setEnabled(false);
        selectedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

        // Store the selected time
        selectedTime = time;
    }
}
