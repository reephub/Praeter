package com.praeter.ui.mainactivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.praeter.R
import com.praeter.data.local.model.Ancient
import com.praeter.data.local.model.ClassModel
import com.praeter.databinding.ActivityMainBinding
import com.praeter.ui.base.BaseActivity
import com.praeter.ui.mainactivity.fragment.bottomfragment.BottomSheetFragment
import com.praeter.ui.mainactivity.fragment.classes.ClassesFragment.OnClassItemSelectedListener
import com.praeter.ui.mainactivity.fragment.home.HomeFragment
import com.praeter.ui.mainactivity.fragment.metttheancient.MeetTheAncientFragment.OnMeetTheAncientItemSelectedListener
import timber.log.Timber


class MainActivity
    : BaseActivity(),
    OnClassItemSelectedListener, OnMeetTheAncientItemSelectedListener {


    private var _viewBinding: ActivityMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    // Views
    private lateinit var toolbar: Toolbar
    private lateinit var drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Check for login session. If not logged in launch
         * login activity
         */
        //TODO : Init check user exist in shared
        /*if (PraeterApplication.getInstance().getPrefManager().getUser() == null) {
            navigator.callLoginActivity();
        }*/
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration =
            AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build()
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration)
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_home) {
            Timber.e("item.getItemId() == R.id.action_home")

            // https://stackoverflow.com/questions/51385067/android-navigation-architecture-component-get-current-visible-fragment
            // Android Navigation Architecture Component - Get current visible fragment
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navHostFragment.childFragmentManager.fragments[0]

            // Store Home Fragment
            val currentFragment = navHostFragment.childFragmentManager.fragments[0]
            if (currentFragment is HomeFragment
                && currentFragment.isVisible()
            ) {
                //DO STUFF
                Timber.e("Fragment is already visible, do nothing")
            } else {
                //Whatever
                Timber.e("call fragment manager do the transaction")
                navController.navigate(R.id.nav_home)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        return (NavigationUI.navigateUp(
            navController,
            mAppBarConfiguration
        ) || super.onSupportNavigateUp())
    }

    override fun onDestroy() {
        super.onDestroy()

        _viewBinding = null
    }

    override fun onClassClick(className: String?, classType: String?) {
        val bottomSheetFragment: BottomSheetFragment =
            BottomSheetFragment.newInstance(ClassModel(className!!, classType!!))
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    override fun onAncientClick(ancientName: String?, ancientDescription: String?) {
        val bottomSheetFragment: BottomSheetFragment =
            BottomSheetFragment.newInstance(Ancient(ancientName))
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }
}