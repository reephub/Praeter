package com.praeter.ui.mainactivity;

import android.annotation.SuppressLint;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.praeter.R;
import com.praeter.ui.base.BaseViewImpl;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


@SuppressLint("NonConstantResourceId")
public class MainActivityView extends BaseViewImpl<MainActivityPresenter>
        implements MainActivityContract.View {

    // TAG & Context
    private MainActivity context;

    private AppBarConfiguration mAppBarConfiguration;

    //Views
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    /*@BindView(R.id.app_recyclerView)
    RecyclerView appRecyclerView;*/
    /*@BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar_api)
    ProgressBar progressBar;
    @BindView(R.id.linear_fetching_data)
    LinearLayout linearFetchData;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.btn_quit_app)
    Button btnExitApp;*/

    //@Inject
    //MoviesApiListAdapter apiAdapter;

    //@Inject
    //MovieDatabaseListAdapter databaseAdapter;


    @Inject
    MainActivityView(MainActivity context) {
        this.context = context;
    }


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    public void onCreate() {

        // Attach view with presenter
        getPresenter().attachView(this);

        // Butterknife view binding
        ButterKnife.bind(context);

        // Call presenter to fetch data
//        getPresenter().getApplications();
    }

    public void onDestroy() {
        getPresenter().detachView();
        context = null;
    }


    /////////////////////////////////////
    //
    // BUTTERKNIFE
    //
    /////////////////////////////////////
    /*@OnClick(R.id.btn_quit_app)
    void onClickExitApp() {
        closeApp();
    }*/


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////

    /**
     * Set up views (recyclerviews, spinner, etc...)
     */
    private void initViews() {

        /*rvMovie.setLayoutManager(new LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        ));
        rvMovie.setAdapter(apiAdapter);*/
    }


    /////////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////////
    @Override
    public void showLoading() {

        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void closeApp() {

    }

    /////////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////////
}
