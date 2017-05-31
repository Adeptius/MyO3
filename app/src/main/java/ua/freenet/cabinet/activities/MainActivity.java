package ua.freenet.cabinet.activities;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Field;

import ua.freenet.cabinet.R;
import ua.freenet.cabinet.dao.DbCache;
import ua.freenet.cabinet.dao.GetInfo;
import ua.freenet.cabinet.fragments.BalanceFragment;
import ua.freenet.cabinet.fragments.BaseFragment;
import ua.freenet.cabinet.fragments.BonusFragment;
import ua.freenet.cabinet.fragments.ContactFragment;
import ua.freenet.cabinet.fragments.CreditFragment;
import ua.freenet.cabinet.fragments.DivanTvFragment;
import ua.freenet.cabinet.fragments.DocumentFragment;
import ua.freenet.cabinet.fragments.DrWebFragment;
import ua.freenet.cabinet.fragments.FreeDayFragment;
import ua.freenet.cabinet.fragments.FriendFragment;
import ua.freenet.cabinet.fragments.GarantServiceFragment;
import ua.freenet.cabinet.fragments.MainFragment;
import ua.freenet.cabinet.fragments.MegogoFragment;
import ua.freenet.cabinet.fragments.NewsFragment;
import ua.freenet.cabinet.fragments.OllTvFragment;
import ua.freenet.cabinet.fragments.OnOffInternet;
import ua.freenet.cabinet.fragments.PayFragment;
import ua.freenet.cabinet.fragments.TarifFragment;
import ua.freenet.cabinet.fragments.TurboDayFragment;
import ua.freenet.cabinet.model.Person;
import ua.freenet.cabinet.utils.MyAlertDialogBuilder;
import ua.freenet.cabinet.utils.Settings;
import ua.freenet.cabinet.utils.Utilits;

import static ua.freenet.cabinet.utils.Utilits.EXECUTOR;
import static ua.freenet.cabinet.utils.Utilits.HANDLER;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String title = "";
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        try { // увеличение поля захвата закрытой панели
            Field mDragger = drawer.getClass().getDeclaredField("mLeftDragger");//mRightDragger for right obviously
            mDragger.setAccessible(true);
            ViewDragHelper draggerObj = (ViewDragHelper) mDragger.get(drawer);
            Field mEdgeSize = draggerObj.getClass().getDeclaredField("mEdgeSize");
            mEdgeSize.setAccessible(true);
            int edge = mEdgeSize.getInt(draggerObj);
            mEdgeSize.setInt(draggerObj, edge * 5); //optimal value as for me, you may set any constant in dp
        } catch (Exception ignored) {

        }

        disableNavigationViewScrollbars(navigationView);

        menu = navigationView.getMenu();

        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));
        menu.findItem(R.id.nav_main_info).setChecked(true);

        try {
            Person person = DbCache.getPerson();
            setDrawlerText(person.getName(), "Договір " + person.getCard());

            if (person.isYur() || person.isVip()) {
                menu.findItem(R.id.nav_free_day).setVisible(false);
                menu.findItem(R.id.nav_turbo_day).setVisible(false);
                menu.findItem(R.id.nav_dovira).setVisible(false);
                menu.findItem(R.id.nav_garant_service).setVisible(false);
//                menu.findItem(R.id.nav_dr_web).setVisible(false);
                menu.findItem(R.id.nav_megogo).setVisible(false);
                menu.findItem(R.id.nav_divan_tv).setVisible(false);
                menu.findItem(R.id.nav_friend).setVisible(false);
                menu.findItem(R.id.nav_bonus).setVisible(false);
                menu.findItem(R.id.nav_oll_tv).setVisible(false);
                menu.findItem(R.id.nav_oll_tv).setVisible(false);
            }
            if (person.isYur()) {
                menu.findItem(R.id.nav_add_balance).setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        FirebaseMessaging.getInstance().subscribeToTopic("news");
//        System.out.println("TOKEN IS: " + FirebaseInstanceId.getInstance().getToken());

        String message = getIntent().getStringExtra("notAnothMessage");
        if (message != null && !"".equals(message)) {
            showWarningIfInternetInactive(message);
        }
        goTo(new MainFragment());
        try{
            checkNewVersion();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    private void checkNewVersion() {
        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String currentVersion = pInfo.versionName;
                    if (!GetInfo.isLastVersion(currentVersion)) {
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog dialog = new MyAlertDialogBuilder(MainActivity.this)
                                        .setPositiveButtonWithRunnableForHandler("Оновити", new Runnable() {
                                            @Override
                                            public void run() {
                                                final String appPackageName = "ua.freenet.cabinet"; // getPackageName() from Context or Activity object
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName));
                                                    startActivity(i);
                                                }
                                            }
                                        }).setNegativeButtonForClose("Не зараз")
                                        .setTitleText("Оновлення")
                                        .setMessage("Доступна нова версія в Google Play")
                                        .create();
                                dialog.setCanceledOnTouchOutside(false);
                                if (!isFinishing())
                                dialog.show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Utilits.log("Ошибка проверки версии");
                }
            }
        });
    }

    private void showWarningIfInternetInactive(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(true);
        TextView titleView = new TextView(this);
        titleView.setText("Шановний абонент!");
        titleView.setGravity(Gravity.CENTER);
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(Color.parseColor("#1976D2"));
        builder.setCustomTitle(titleView);
        View textLayout = LayoutInflater.from(this).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText(message);
        builder.setView(textLayout);
        android.app.AlertDialog dialog = builder.create();

        if (!isFinishing())
        dialog.show();
    }

    private void setDrawlerText(String title, String description) {
        NavigationView navigation = (NavigationView) findViewById(R.id.nav_view);
        LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigation);
        TextView drawlerTitle = (TextView) findViewById(R.id.drawler_title);
        TextView drawlerDescription = (TextView) findViewById(R.id.drawler_description);
        drawlerTitle.setText(title);
        drawlerDescription.setText(description);
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        Fragment f = getFragmentManager().findFragmentById(R.id.content_frame);
        if (!(f instanceof MainFragment)) {
            menu.findItem(R.id.nav_main_info).setChecked(true);
            goTo(new MainFragment());
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Snackbar.make(findViewById(R.id.main_progress_bar), "Подвійне натиснення для виходу", Snackbar.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 500);
        }
    }

    private void goTo(final BaseFragment fragment) {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true, false);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            goTo(new NewsFragment());
        } else if (id == R.id.nav_main_info) {
            goTo(new MainFragment());
        } else if (id == R.id.nav_balance) {
            goTo(new BalanceFragment());
        } else if (id == R.id.nav_add_balance) {
            goTo(new PayFragment());
        } else if (id == R.id.nav_tarif_plans) {
            goTo(new TarifFragment());
        } else if (id == R.id.nav_free_day) {
            goTo(new FreeDayFragment());
        } else if (id == R.id.nav_turbo_day) {
            goTo(new TurboDayFragment());
        } else if (id == R.id.nav_dovira) {
            goTo(new CreditFragment());
        } else if (id == R.id.nav_garant_service) {
            goTo(new GarantServiceFragment());
        } else if (id == R.id.nav_vkl_internet) {
            goTo(new OnOffInternet());
//        } else if (id == R.id.nav_dr_web) {
//            goTo(new DrWebFragment());
        } else if (id == R.id.nav_megogo) {
            goTo(new MegogoFragment());
        } else if (id == R.id.nav_divan_tv) {
            goTo(new DivanTvFragment());
        } else if (id == R.id.nav_oll_tv) {
            goTo(new OllTvFragment());
        } else if (id == R.id.nav_friend) {
            goTo(new FriendFragment());
        } else if (id == R.id.nav_bonus) {
            goTo(new BonusFragment());
        } else if (id == R.id.nav_contact) {
            goTo(new ContactFragment());
        } else if (id == R.id.nav_documents) {
            goTo(new DocumentFragment());
        } else if (id == R.id.nav_exit) {
            exit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        View textLayout = LayoutInflater.from(this).inflate(R.layout.item_alert_message, null);
        TextView text = (TextView) textLayout.findViewById(R.id.text);
        text.setText("Ви впевнені, що хочете вийти з облікового запису?");
        builder.setView(textLayout);
        builder.setPositiveButton("Вийти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Settings.clearAllData();
                MainActivity.this.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}
