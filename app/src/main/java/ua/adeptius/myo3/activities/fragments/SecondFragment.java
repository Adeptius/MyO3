package ua.adeptius.myo3.activities.fragments;

import android.view.View;
import android.widget.TextView;

import ua.adeptius.myo3.R;

public class SecondFragment extends BaseFragment  {

    @Override
    void init() {
        TextView textView = new TextView(context);
        textView.setText("Second frame");
        layout.addView(textView);
    }

    @Override
    int setFragmentId() {
        return R.layout.fragment_second;
    }

    @Override
    void doInBackground() throws Exception {

    }

    @Override
    void processIfOk() {

    }

    @Override
    void processIfFail() {

    }

    @Override
    int setLayoutId() {
        return R.id.scroll_view_second;
    }

    @Override
    public void onClick(View view) {

    }
}
