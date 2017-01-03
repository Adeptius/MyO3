package ua.adeptius.myo3.activities.fragments;

import android.view.LayoutInflater;
import android.view.View;

import ua.adeptius.myo3.R;


public class DivanTvFragment extends BaseFragment {

    @Override
    void init() {
        hideAllViewsInMainScreen();
    }

    @Override
    void doInBackground() throws Exception {
        draw();
        animateScreen();
    }

    private void draw() {

    }

    @Override
    void processIfOk() {

    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_base_scrolling;
    }

    @Override
    int setLayoutId() {
        return R.id.base_scroll_view;
    }

    @Override
    public void onClick(View v) {

    }
}
