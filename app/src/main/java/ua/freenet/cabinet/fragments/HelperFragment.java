package ua.freenet.cabinet.fragments;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.TextView;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;

public abstract class HelperFragment extends BaseFragment {

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

    protected void openInBrowser(String url){
        try{
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }catch (ActivityNotFoundException e){
            new MyAlertDialogBuilder(context)
                    .setTitleText("Помилка")
                    .setMessage("На пристрої не знайдено браузеру")
                    .setPositiveButtonForClose("ОК")
                    .createAndShow();
        }
    }

    public static int expand(final View v) {
        v.setVisibility(View.VISIBLE);

        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

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


    protected void insertHardWareRequirementsToLayout(LinearLayout pasteInLay, final String tvType) {
        final String ollTvPackageName = "tv.oll.app";
        final String megogoPackageName = "com.megogo.application";
        final String divanTvPackageName = "divan.tv.DivanTV";

        final View hardware = LayoutInflater.from(context).inflate(R.layout.item_hardware, null);
        pasteInLay.addView(hardware);

        final Button hideButton = (Button) hardware.findViewById(R.id.button_hide);
        final LinearLayout hideLayout = (LinearLayout) hardware.findViewById(R.id.lay_hide);
        final Button downloadButton = (Button) hardware.findViewById(R.id.button_download);

        if (tvType.equals("megogo")) {
            LinearLayout stb = (LinearLayout) hardware.findViewById(R.id.stb);
            hideLayout.removeView(stb);
        }

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
