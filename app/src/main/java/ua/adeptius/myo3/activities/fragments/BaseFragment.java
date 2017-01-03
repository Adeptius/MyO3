package ua.adeptius.myo3.activities.fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.concurrent.ExecutorService;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.activities.MainActivity;
import ua.adeptius.myo3.utils.Utilits;

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected Handler HANDLER = Utilits.HANDLER;
    protected ExecutorService EXECUTOR = Utilits.EXECUTOR;
    protected View baseView;
    protected Context context;
    protected String titleText;
    protected String descriptionText;
    protected LinearLayout mainLayout;
    protected final int COLOR_BLUE = Color.parseColor("#1976D2");
    protected final int COLOR_GREEN = Color.parseColor("#388E3C");

    public static final ViewGroup.LayoutParams WRAP_WRAP = new ViewGroup
            .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
            , ViewGroup.LayoutParams.WRAP_CONTENT);
    public static final ViewGroup.LayoutParams MATCH_WRAP = new ViewGroup
            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
            , ViewGroup.LayoutParams.WRAP_CONTENT);
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
        mainLayout = (LinearLayout) baseView.findViewById(setLayoutId());
        context = mainLayout.getContext();
        setTitle(titleText, descriptionText);
        MainActivity.progressBar.setVisibility(View.VISIBLE);
        init();
        startBackgroundTask();
        return baseView;
    }

    public void makeSimpleSnackBar(final String message, final View text) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(text, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    protected void hideAllViewsInMainScreen(){
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            mainLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    protected void reloadFragment(){
        FragmentManager fm = getFragmentManager();
        try {
            fm.beginTransaction().replace(R.id.content_frame, this.getClass().newInstance()).commit();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void animateScreen(){
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mainLayout.getChildCount(); i++) {
                    final int a = i;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            mainLayout.getChildAt(a).setVisibility(View.VISIBLE);
                            mainLayout.getChildAt(a).startAnimation(AnimationUtils.loadAnimation(context,
                                    R.anim.main_screen_trans));
                        }
                    });
                }
            }
        });
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
                }finally {
                    MainActivity.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    abstract void doInBackground() throws Exception;

    abstract void processIfOk();

    private void processIfFail(){
        final View itemView = LayoutInflater.from(context).inflate(R.layout.page_load_error_try_again, null);
        Button reloadButton = (Button) itemView.findViewById(R.id.reload_button);
        mainLayout.addView(itemView);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadFragment();
            }
        });
    }

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

    protected LinearLayout getLayout(int id) {
        return (LinearLayout) baseView.findViewById(id);
    }

    protected Button getButton(int id) {
        return (Button) baseView.findViewById(id);
    }

    protected void setTitle(final String titleText, final String descriptionText){
       HANDLER.post(new Runnable() {
           @Override
           public void run() {
               MainActivity.titleTextView.setText(titleText);
               MainActivity.title = titleText;
               if ("".equals(descriptionText)){
                   MainActivity.descriptionTextView.setVisibility(View.GONE);
               }else {
                   MainActivity.descriptionTextView.setVisibility(View.VISIBLE);
               }
               MainActivity.descriptionTextView.setText(descriptionText);
           }
       });
    }

    abstract void init();

    abstract int setFragmentId();

    abstract int setLayoutId();

}
