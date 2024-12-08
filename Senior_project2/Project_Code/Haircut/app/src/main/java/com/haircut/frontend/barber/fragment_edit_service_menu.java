package com.haircut.frontend.barber;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.haircut.R;

public class fragment_edit_service_menu extends Fragment {

    private Button updateMenuButton;
    private EditText serviceName1, servicePrice1, serviceName2, servicePrice2, serviceName3, servicePrice3;
    private EditText serviceName4, servicePrice4, serviceName5, servicePrice5, serviceName6, servicePrice6, serviceName7, servicePrice7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_service_menu, container, false);

        // Initialize Update Menu button
        updateMenuButton = view.findViewById(R.id.update_menu_button);

        // Initialize EditTexts for each row
        serviceName1 = view.findViewById(R.id.service_name1);
        servicePrice1 = view.findViewById(R.id.service_price1);
        serviceName2 = view.findViewById(R.id.service_name2);
        servicePrice2 = view.findViewById(R.id.service_price2);
        serviceName3 = view.findViewById(R.id.service_name3);
        servicePrice3 = view.findViewById(R.id.service_price3);
        serviceName4 = view.findViewById(R.id.service_name4);
        servicePrice4 = view.findViewById(R.id.service_price4);
        serviceName5 = view.findViewById(R.id.service_name5);
        servicePrice5 = view.findViewById(R.id.service_price5);
        serviceName6 = view.findViewById(R.id.service_name6);
        servicePrice6 = view.findViewById(R.id.service_price6);
        serviceName7 = view.findViewById(R.id.service_name7);
        servicePrice7 = view.findViewById(R.id.service_price7);

        // Set Update Menu button listener
        updateMenuButton.setOnClickListener(v -> updateMenu());

        return view;
    }

    private void updateMenu() {
        // Retrieve text from EditTexts
        String updatedService1 = serviceName1.getText().toString();
        String updatedPrice1 = servicePrice1.getText().toString();
        String updatedService2 = serviceName2.getText().toString();
        String updatedPrice2 = servicePrice2.getText().toString();
        String updatedService3 = serviceName3.getText().toString();
        String updatedPrice3 = servicePrice3.getText().toString();
        String updatedService4 = serviceName4.getText().toString();
        String updatedPrice4 = servicePrice4.getText().toString();
        String updatedService5 = serviceName5.getText().toString();
        String updatedPrice5 = servicePrice5.getText().toString();
        String updatedService6 = serviceName6.getText().toString();
        String updatedPrice6 = servicePrice6.getText().toString();
        String updatedService7 = serviceName7.getText().toString();
        String updatedPrice7 = servicePrice7.getText().toString();

        // Save the updated menu to database or SharedPreferences
        // Replace with your database or storage logic here

        Toast.makeText(getContext(), "Menu updated successfully!", Toast.LENGTH_SHORT).show();
    }
}
