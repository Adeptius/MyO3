package ua.freenet.cabinet.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutorService;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.activities.MainActivity;
import ua.freenet.cabinet.model.ChannelOllTv;
import ua.freenet.cabinet.utils.Utilits;

import static ua.freenet.cabinet.utils.Utilits.log;

public abstract class BaseFragment extends Fragment{

    protected Handler HANDLER = Utilits.HANDLER;
    protected ExecutorService EXECUTOR = Utilits.EXECUTOR;
    protected View baseView;
    protected Context context;
    protected LinearLayout mainLayout;
    public static final int COLOR_BLUE = Color.parseColor("#1976D2");
    public static final int COLOR_GREEN = Color.parseColor("#388E3C");
    public static final int COLOR_RED = Color.RED;
    protected ProgressBar progressBar;
    protected ProgressDialog progressDialog;
    protected boolean hardwareIsHidden = true;
    protected String titleText;
    protected String descriptionText;
    protected int fragmentId;
    protected int titleImage;
    protected int layoutId;
    protected int SCREEN_WIDTH;
    protected final int TIME_TO_WAIT_BEFORE_UPDATE = 4000;

    public static final ViewGroup.LayoutParams WRAP_WRAP = new ViewGroup
            .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
            , ViewGroup.LayoutParams.WRAP_CONTENT);
    public static final ViewGroup.LayoutParams MATCH_WRAP = new ViewGroup
            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
            , ViewGroup.LayoutParams.WRAP_CONTENT);
    public static final LinearLayout.LayoutParams MATCH_WRAP_WEIGHT150 = new LinearLayout
            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1.5);
    public static final LinearLayout.LayoutParams MATCH_WRAP_WEIGHT1 = new LinearLayout
            .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, (float) 1);


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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

    protected void addViewToMainLayout(final List<View> viewsToAdd) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                for (View view : viewsToAdd) {
                    view.setVisibility(View.GONE);
                    mainLayout.addView(view);
                }
            }
        });
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
        } catch (Exception e) {
            log(e.getLocalizedMessage());
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
                    } catch (InterruptedException ignored) {
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


    protected void startBackgroundTask() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    updateTitle();
                    ChannelOllTv.drawScreen();
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
                            processIfFail();
                        }
                    });
                } finally {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) {
                    }
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

    protected void updateTitle() {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                TextView titleTextView = (TextView) getActivity().findViewById(R.id.title_text_view);
                titleTextView.setText(titleText);

                TextView descriptionTextView = (TextView) getActivity().findViewById(R.id.description_text_view);
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

    protected void progressDialogShow() {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Зачекайте..");
                progressDialog.show();
            }
        });
    }

    protected void hideProgressDialog() {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }

    protected void progressDialogStopAndShowMessage(final String message, final View view) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
        if (!message.equals("")) {
            makeSimpleSnackBar(message, view);
        }
    }

    protected void progressDialogWaitStopShowMessageReload(String message, View view) {
        makeSimpleSnackBar(message, view);
        try {
            Thread.sleep(TIME_TO_WAIT_BEFORE_UPDATE);
        } catch (InterruptedException ignored) {
        }
        hideProgressDialog();
        reloadFragment();
    }
}
