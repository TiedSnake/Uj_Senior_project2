package com.example.haircut.frontend;  // Update with your actual package name

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haircut.R;

public class fragment_home_customer extends Fragment {

    public fragment_home_customer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment_home_customer layout inside this fragment
        return inflater.inflate(R.layout.fragment_home_customer, container, false);
    }
}
