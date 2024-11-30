package com.haircut.frontend;  // Update with your actual package name

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haircut.R;

public class fragment_about_us extends Fragment {

    public fragment_about_us() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment_about_us layout inside this fragment
        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }
}
