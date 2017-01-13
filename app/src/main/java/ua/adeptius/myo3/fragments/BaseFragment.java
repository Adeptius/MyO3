package ua.adeptius.myo3.fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

    protected LinearLayout mainLayout;
    protected final int COLOR_BLUE = Color.parseColor("#1976D2");
    protected final int COLOR_GREEN = Color.parseColor("#388E3C");
    protected final int COLOR_RED = Color.RED;
    protected ProgressBar progressBar;

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


    protected String titleText;
    protected String descriptionText;
    protected int fragmentId;
    protected int titleImage;
    protected int layoutId;
    protected int SCREEN_WIDTH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
//        appBarLayout.setExpanded(true);
        setAllSettings();
        baseView = inflater.inflate(fragmentId, container, false);
        mainLayout = (LinearLayout) baseView.findViewById(layoutId);
        context = getActivity();
        SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;

        updateTitle();
        showImageInTop();
        init();
        startBackgroundTask();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < android.os.Build.VERSION_CODES.LOLLIPOP) {
            progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
            mainLayout.addView(progressBar);
        } else {
            progressBar = (ProgressBar) getActivity().findViewById(R.id.main_progress_bar);
            progressBar.setVisibility(View.VISIBLE);
        }

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

    protected void hideAllViewsInMainScreen() {
        for (int i = 0; i < mainLayout.getChildCount(); i++) {
            mainLayout.getChildAt(i).setVisibility(View.GONE);
        }
    }

    protected void showImageInTop() {
        final Bitmap loadedBitMap = BitmapFactory
                .decodeResource(getResources(), titleImage);
        ImageView view = (ImageView) getActivity().findViewById(R.id.backdrop);
        view.setImageBitmap(loadedBitMap);
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    protected void reloadFragment() {
        FragmentManager fm = getFragmentManager();
        try {
            fm.beginTransaction().replace(R.id.content_frame, this.getClass().newInstance()).commit();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void animateScreen() {
        mainLayout.removeView(progressBar);
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mainLayout.getChildCount(); i++) {
                    final int a = i;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ignored) {}
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



    protected void startBackgroundTask() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    updateTitle();
                    doInBackground();
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            processIfOk();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
//                            showError();
                            processIfFail();
                        }
                    });
                } finally {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    protected void goTo(BaseFragment fragment) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    abstract void doInBackground() throws Exception;

    abstract void processIfOk();

    private void processIfFail() {
        final View itemView = LayoutInflater.from(context).inflate(R.layout.item_page_load_error, null);
        Button reloadButton = (Button) itemView.findViewById(R.id.reload_button);
        mainLayout.addView(itemView);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadFragment();
            }
        });
    }

//    protected void showError(){
//        titleText = "Помилка";
//        descriptionText = "Не вдалось завантажити дані";
//        updateTitle();
//    }

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

    protected void updateTitle() {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {

                TextView titleTextView = (TextView) getActivity().findViewById(R.id.title_text_view);
                titleTextView.setText(titleText);

                TextView descriptionTextView = (TextView) getActivity().findViewById(R.id.description_text_view);
                // TODO исправить отображение титла при переходе в другой фрагмент, если коллапс тулбар біл закріт
                MainActivity.title = titleText;
                if ("".equals(descriptionText)) {
                    descriptionTextView.setVisibility(View.GONE);
                } else {
                    descriptionTextView.setVisibility(View.VISIBLE);
                }
                descriptionTextView.setText(descriptionText);
            }
        });
    }

    abstract void init();

    abstract void setAllSettings();
}
