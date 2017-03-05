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

    public static int expand(final View v) {
        v.setVisibility(View.VISIBLE);

        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        int duration = (int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density);
        a.setDuration(duration);
        v.startAnimation(a);
        return duration;
    }

    public static int collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 3dp/ms
        int duration = initialHeight / 3;
        a.setDuration(duration);

        v.startAnimation(a);
        return duration;
    }

    protected void removeAllViewsAndCollapse(final View view) {
        final int timeAnimation = collapse(view);
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timeAnimation);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        ((LinearLayout) view).removeAllViews();
                        view.setVisibility(View.GONE);
                    }
                });
            }

        });
    }


    protected void addHardWareRequirementsToMainScreenFor(final String tvType) {
        final String ollTvPackageName = "tv.oll.app";
        final String megogoPackageName = "com.megogo.application";
        final String divanTvPackageName = "divan.tv.DivanTV";

        final View hardware = LayoutInflater.from(context).inflate(R.layout.item_hardware, null);
        final Button hideButton = (Button) hardware.findViewById(R.id.button_hide);
        final LinearLayout hideLayout = (LinearLayout) hardware.findViewById(R.id.lay_hide);
        final Button downloadButton = (Button) hardware.findViewById(R.id.button_download);

        if (tvType.equals("megogo")) {
            LinearLayout stb = (LinearLayout) hardware.findViewById(R.id.stb);
            hideLayout.removeView(stb);
        }
        hardware.setVisibility(View.GONE);
        mainLayout.addView(hardware);
        hideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentPackage = "";
                        if (tvType.equals("oll")) {
                            currentPackage = ollTvPackageName;
                        } else if (tvType.equals("megogo")) {
                            currentPackage = megogoPackageName;
                        } else if (tvType.equals("divan")) {
                            currentPackage = divanTvPackageName;
                        }

                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + currentPackage)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + currentPackage)));
                        }
                    }
                });

                if (hardwareIsHidden) {
                    hideButton.setText("Сховати");
                    hardwareIsHidden = false;
                    EXECUTOR.submit(new Runnable() {
                        @Override
                        public void run() {
                            HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    hideLayout.setVisibility(View.VISIBLE);
                                    hideLayout.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    final int targetHeight = hideLayout.getMeasuredHeight();
                                    hideLayout.getLayoutParams().height = 1;
                                    Animation a = new Animation() {
                                        @Override
                                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                                            hideLayout.getLayoutParams().height = interpolatedTime == 1
                                                    ? ViewGroup.LayoutParams.WRAP_CONTENT
                                                    : (int) (targetHeight * interpolatedTime);
                                            hideLayout.requestLayout();
                                        }

                                        @Override
                                        public boolean willChangeBounds() {
                                            return true;
                                        }
                                    };

                                    int duration = (int) (targetHeight / hideLayout.getContext().getResources().getDisplayMetrics().density);
                                    a.setDuration(duration);
                                    a.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            Animation anim = AnimationUtils.loadAnimation(context, R.anim.splash_screen_alpha);
                                            hideLayout.startAnimation(anim);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    hideLayout.startAnimation(a);
                                }
                            });
                        }
                    });
                } else {
                    hardwareIsHidden = true;
                    hideButton.setText("Показати необхідне обладнання");
                    Animation anim = AnimationUtils.loadAnimation(context, R.anim.splash_screen_alpha_gone);
                    hideLayout.startAnimation(anim);
                    collapse(hideLayout);
                }
            }
        });
    }
}
