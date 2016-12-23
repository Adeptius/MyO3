package ua.adeptius.myo3.activities.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.adeptius.myo3.R;


public class SecondFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("init SecondFragment");
        return inflater.inflate(R.layout.fragment_second,container,false);
    }
}
