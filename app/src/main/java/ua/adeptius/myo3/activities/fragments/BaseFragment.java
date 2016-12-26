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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;

import ua.adeptius.myo3.activities.MainActivity;
import ua.adeptius.myo3.utils.Utilits;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected Handler HANDLER = Utilits.HANDLER;
    protected ExecutorService EXECUTOR = Utilits.EXECUTOR;
    protected LinearLayout layout;
    protected View baseView;
    protected Context context;
    protected String titleText;
    protected String descriptionText;
//    int mCurCheckPosition;

    public static final ViewGroup.LayoutParams WRAP_MACH = new ViewGroup
            .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
            , ViewGroup.LayoutParams.MATCH_PARENT);
    public static final ViewGroup.LayoutParams WRAP_WRAP = new ViewGroup
            .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
            , ViewGroup.LayoutParams.WRAP_CONTENT);
    public static final ViewGroup.LayoutParams MATCH_WRAP = new ViewGroup
            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
            , ViewGroup.LayoutParams.WRAP_CONTENT);
    public static final ViewGroup.LayoutParams MATCH_MATCH = new ViewGroup
            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
            , ViewGroup.LayoutParams.MATCH_PARENT);
    public static final LinearLayout.LayoutParams WRAP_WRAP_WEIGHT1 = new LinearLayout
            .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
    public static final LinearLayout.LayoutParams WRAP_WRAP_WEIGHT036 = new LinearLayout
            .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 0.36);
    public static final LinearLayout.LayoutParams MATCH_WRAP_WEIGHT150 = new LinearLayout
            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1.5);
    public static final LinearLayout.LayoutParams MATCH_WRAP_WEIGHT1 = new LinearLayout
            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1);



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        baseView = inflater.inflate(setFragmentId(), container, false);
        layout = (LinearLayout) baseView.findViewById(setLayoutId());
        context = layout.getContext();
        setTitle(titleText, descriptionText);
        init();
        return baseView;
    }


    protected void startBackgroundTask(){
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try{
                    setTitle(titleText, descriptionText);
                    doInBackground();
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            processIfOk();
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            showError();
                            processIfFail();
                        }
                    });
                }
            }
        });
//        EXECUTOR.submit(() -> {
//            try{
//                setTitle(titleText, descriptionText);
//                doInBackground();
//                HANDLER.post(() -> processIfOk());
//            }catch (Exception e){
//                e.printStackTrace();
//                HANDLER.post(() -> {
//                    showError();
//                    processIfFail();
//                });
//            }
//        });
    }

    abstract void doInBackground() throws Exception;

    abstract void processIfOk();

    abstract void processIfFail();


    protected void showError(){
        setTitle("Помилка", "Не вдалось завантажити дані");
    }

    protected TextView getTextView(int id) {
        return (TextView) baseView.findViewById(id);
    }

    protected CheckBox getCheckBox(int id) {
        return (CheckBox) baseView.findViewById(id);
    }

    protected ImageView getImageView(int id) {
        return (ImageView) baseView.findViewById(id);
    }

    protected void setTitle(final String titleText, final String descriptionText){
       HANDLER.post(new Runnable() {
           @Override
           public void run() {
               MainActivity.titleTextView.setText(titleText);
               MainActivity.title = titleText;
               MainActivity.descriptionTextView.setText(descriptionText);
           }
       });
    }

    abstract void init();

    abstract int setFragmentId();

    abstract int setLayoutId();

//    @Override
//    public void onResume() {
//        processIfOk();
//        super.onResume();
//    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//            // Restore last state for checked position.
//            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt("curChoice", mCurCheckPosition);
//    }
}
