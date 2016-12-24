package ua.adeptius.myo3.activities.fragments;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected Handler HANDLER = new Handler();
    protected LinearLayout layout;
    protected View baseView;
    protected Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(setFragmentId(),container,false);
        layout = (LinearLayout) baseView.findViewById(setLayoutId());
        context = layout.getContext();
        doWork();
        return baseView;
    }

    protected TextView getTextView(int id){
        return (TextView) baseView.findViewById(id);
    }

    protected CheckBox getCheckBox(int id){
        return (CheckBox) baseView.findViewById(id);
    }


    abstract void doWork();

    abstract int setFragmentId();

    abstract int setLayoutId();

    protected View proceed(View view){
        return view;
    }
}
