package ua.adeptius.myo3.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.dao.DbCache;
import ua.adeptius.myo3.fragments.BalanceFragment;
import ua.adeptius.myo3.fragments.BaseFragment;
import ua.adeptius.myo3.fragments.BonusFragment;
import ua.adeptius.myo3.fragments.CreditFragment;
import ua.adeptius.myo3.fragments.DivanTvFragment;
import ua.adeptius.myo3.fragments.DocumentFragment;
import ua.adeptius.myo3.fragments.DrWebFragment;
import ua.adeptius.myo3.fragments.FreeDayFragment;
import ua.adeptius.myo3.fragments.FriendFragment;
import ua.adeptius.myo3.fragments.GarantServiceFragment;
import ua.adeptius.myo3.fragments.MainFragment;
import ua.adeptius.myo3.fragments.MegogoFragment;
import ua.adeptius.myo3.fragments.NewsFragment;
import ua.adeptius.myo3.fragments.OllTvFragment;
import ua.adeptius.myo3.fragments.OnOffInternet;
import ua.adeptius.myo3.fragments.PayFragment;
import ua.adeptius.myo3.fragments.SettingsFragment;
import ua.adeptius.myo3.fragments.ContactFragment;
import ua.adeptius.myo3.fragments.TarifFragment;
import ua.adeptius.myo3.fragments.TurboDayFragment;
import ua.adeptius.myo3.model.Person;
import ua.adeptius.myo3.utils.Settings;
import ua.adeptius.myo3.utils.Utilits;


// TODO подключить аналитику
// TODO добавить обработку ошибок в констукторы
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String title = "";


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

        //TODO переместить это в сплэш скрин
        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));

        try{
            Person person = DbCache.getPerson();
            setDrawlerText(person.getName(),"Угода " + Settings.getCurrentLogin());
        }catch (Exception ignored){}

        goTo(new MainFragment());
    }


    private void setDrawlerText(String title, String description){
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
//            FragmentManager fm = getFragmentManager();
//            if (fm.getBackStackEntryCount() > 0) {
//                fm.popBackStackImmediate();
//            } else {
//                super.onBackPressed();
//            }
        }
    }

    private void goTo(final BaseFragment fragment){
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        Utilits.EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack("tag").commit();
            }
        });


    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
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
        } else if (id == R.id.nav_dr_web) {
            goTo(new DrWebFragment());
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
        } else if (id == R.id.nav_settings) {
            goTo(new SettingsFragment());
        }else if (id == R.id.nav_exit) {
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
        text.setText("Ви впевнені, що хочете вийти з облікового запису?\n"
                +"\nПри наступному вході в додаток вам доведеться знову вводити логін та пароль.");
        builder.setView(textLayout);
        builder.setPositiveButton("Вийти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Settings.setCurrentPassportNumber("");
                Settings.setCurrentPassportSerial("");
                Settings.setCurrentLogin("");
                Settings.setCurrentPassword("");
                MainActivity.this.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
