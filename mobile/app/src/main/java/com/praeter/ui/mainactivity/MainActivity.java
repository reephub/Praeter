package com.praeter.ui.mainactivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.praeter.R;
import com.praeter.navigator.Navigator;
import com.praeter.ui.base.BaseActivity;

import javax.inject.Inject;

import timber.log.Timber;

public class MainActivity extends BaseActivity<MainActivityView> {


    // Views
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    @Inject
    Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * Check for login session. If not logged in launch
         * login activity
         * */
        //TODO : Init check user exist in shared
        /*if (PraeterApplication.getInstance().getPrefManager().getUser() == null) {
            navigator.callLoginActivity();
        }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            Timber.e("item.getItemId() == R.id.action_home");

            // https://stackoverflow.com/questions/51385067/android-navigation-architecture-component-get-current-visible-fragment
            // Android Navigation Architecture Component - Get current visible fragment
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            navHostFragment.getChildFragmentManager().getFragments().get(0);

            // Store Home Fragment
            Fragment currentFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

            if (currentFragment instanceof HomeFragment
                    && currentFragment.isVisible()) {
                //DO STUFF
                Timber.e("Fragment is already visible, do nothing");
            } else {
                //Whatever
                Timber.e("call fragment manager do the transaction");
                navController.navigate(R.id.nav_home);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        view.onDestroy();
        super.onDestroy();
    }
}