package ua.adeptius.myo3.activities.fragments;


import android.view.View;

import ua.adeptius.myo3.activities.MainActivity;

public class NewsFragment extends BaseFragment {

    @Override
    void doWork() {
        String titleText = "";
        String descriptionText = "";
        MainActivity.titleTextView.setText(titleText);
        MainActivity.descriptionTextView.setText(descriptionText);

    }

    @Override
    int setFragmentId() {
        return 0;
    }

    @Override
    int setLayoutId() {
        return 0;
    }

    @Override
    public void onClick(View view) {

    }
}
