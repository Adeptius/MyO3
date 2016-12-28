package ua.adeptius.myo3.activities;

import android.app.FragmentManager;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ua.adeptius.myo3.R;
import ua.adeptius.myo3.activities.fragments.BalanceFragment;
import ua.adeptius.myo3.activities.fragments.BaseFragment;
import ua.adeptius.myo3.activities.fragments.MainFragment;
import ua.adeptius.myo3.activities.fragments.NewsFragment;
import ua.adeptius.myo3.activities.fragments.PayFragment;
import ua.adeptius.myo3.activities.fragments.TarifFragment;
import ua.adeptius.myo3.dao.Web;
import ua.adeptius.myo3.model.Settings;
import ua.adeptius.myo3.model.exceptions.CantGetSessionIdException;

import static ua.adeptius.myo3.utils.Utilits.EXECUTOR;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TextView titleTextView, descriptionTextView;
    public static String title = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        Settings.setsPref(getSharedPreferences("settings", MODE_PRIVATE));



        // Задание фона колапс тулбара
        try {
            Glide.with(this).load(R.drawable.cover).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }




        titleTextView = (TextView) findViewById(R.id.title_text_view);
        descriptionTextView = (TextView) findViewById(R.id.description_text_view);

//        Settings.setCurrentLogin("02514521");
//        Settings.setCurrentPassword("5351301");


//        Settings.setCurrentLogin("561100728");
//        Settings.setCurrentPassword("5276229");

        Settings.setCurrentLogin("561600377");
        Settings.setCurrentPassword("1217229");




        EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Settings.setSessionID(Web.getPhpSession(Settings.getCurrentLogin(),
                            Settings.getCurrentPassword()));
                } catch (CantGetSessionIdException e) {
                    e.printStackTrace();
                }
            }
        });
        setDrawlerText("Володимир","Угода " + Settings.getCurrentLogin());
        goTo(new TarifFragment(), R.drawable.background_main1);
    }

    private void setDrawlerText(String title, String description){
        NavigationView navigation = (NavigationView) findViewById(R.id.nav_view);
        LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigation);
        TextView drawlerTitle = (TextView) findViewById(R.id.drawler_title);
        TextView drawlerDescription = (TextView) findViewById(R.id.drawler_description);
        drawlerTitle.setText(title);
        drawlerDescription.setText(description);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
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
            super.onBackPressed();
        }
    }

    private void goTo(BaseFragment fragment,int imageId){
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_frame, fragment).commit();
        Glide.with(this).load(imageId).into((ImageView) findViewById(R.id.backdrop));
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_news) {
            goTo(new NewsFragment(), R.drawable.background_news4);
        } else if (id == R.id.nav_main_info) {
            goTo(new MainFragment(), R.drawable.background_main1);
        } else if (id == R.id.nav_balance) {
            goTo(new BalanceFragment(), R.drawable.background_main1);
        } else if (id == R.id.nav_add_balance) {
            goTo(new PayFragment(), R.drawable.background_main1);
        } else if (id == R.id.nav_tarif_plans) {
            goTo(new TarifFragment(), R.drawable.background_main1);
        } else if (id == R.id.nav_free_day) {

        } else if (id == R.id.nav_turbo_day) {

        } else if (id == R.id.nav_dovira) {

        } else if (id == R.id.nav_garant_service) {

        } else if (id == R.id.nav_vkl_internet) {

        } else if (id == R.id.nav_dr_web) {

        } else if (id == R.id.nav_megogo) {

        } else if (id == R.id.nav_divan_tv) {

        } else if (id == R.id.nav_oll_tv) {

        } else if (id == R.id.nav_friend) {

        } else if (id == R.id.nav_bonus) {

        } else if (id == R.id.nav_support) {

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
