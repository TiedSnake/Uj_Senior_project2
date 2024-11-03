package com.example.haircut;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class fragment_view_ratings extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_ratings_page, container, false);

        // Set up the Go Back button
        ImageButton btnGoBack = view.findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle go back action, perhaps pop the fragment from back stack
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // You can set additional data or references here if needed
        TextView tvTitle = view.findViewById(R.id.tvTitle);
        // Set the title or any other data as needed

        return view;
    }
}
