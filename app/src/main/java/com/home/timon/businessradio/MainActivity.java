package com.home.timon.businessradio;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.Inflater;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.home.timon.businessradio.fragments.JournalFragment;
import com.home.timon.businessradio.fragments.MoreFragment;
import com.home.timon.businessradio.fragments.ProgramFragment;
import com.home.timon.businessradio.fragments.RadioFragment;
import com.home.timon.businessradio.fragments.TVFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();

    //region Vars

    //UI
    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private Button buttonRadioPlayPause;
    private Button buttonTVPlayPause;
    private VideoView myVideoView;

    public static View view;

    //Logic
    private boolean paused;

    private int position;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    //etc
    private FirebaseAnalytics mFirebaseAnalytics;

    //endregion

    //region Activity lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //region Play/Pause radio from notification buttons
        Intent radio_intent = new Intent(getApplicationContext(), RadioService.class);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                //Cry about not being clicked on
            } else if (extras.getBoolean("PauseClick")) {
                //Do your stuff here mate :)
                Objects.requireNonNull(getApplicationContext()).startService(radio_intent);
            }
            else if (extras.getBoolean("PlayClick")) {
                //Do your stuff here mate :)
                Objects.requireNonNull(getApplicationContext()).stopService(radio_intent);
            }
        }
        //endregion

        Log.d(TAG, "onCreate: started.");

//        view = findViewById(android.R.id.content);//the root view

        //loading the default fragment
        loadFragment(new RadioFragment());

        //region Set the toolbar as ActionBar
        // Set a Toolbar to replace the ActionBar.
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);
        //actionBar.hide();
        //endregion

//        //region NavigationDrawer
//
//        //getting  navigation view and attaching the listener
//        mDrawer = findViewById(R.id.drawer_layout);
//        drawerToggle = setupDrawerToggle();
//        // Find our drawer view
//        NavigationView navigationView = findViewById(R.id.nvView);
//        // Setup drawer view
//        setupDrawerContent(navigationView);
//
//        //Setup drawer listener
//        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
//            @Override
//            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
//                // Respond when the drawer's position changes
//            }
//
//            @Override
//            public void onDrawerOpened(@NonNull View drawerView) {
//                // Respond when the drawer is opened
//            }
//
//            @Override
//            public void onDrawerClosed(@NonNull View drawerView) {
//                // Respond when the drawer is closed
//            }
//
//            @Override
//            public void onDrawerStateChanged(int newState) {
//                // Respond when the drawer motion state changes
//            }
//        });
//
//        //endregion

        //region BottomBar

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Create a new fragment and specify the fragment to show based on nav item clicked
                Fragment fragment = null;
                Class fragmentClass;
                switch (item.getItemId()) {
                    case R.id.action_program:
                        fragmentClass = ProgramFragment.class;
                        break;
                    case R.id.action_tv:
                        fragmentClass = TVFragment.class;
                        break;
                    case R.id.action_radio:
                        fragmentClass = RadioFragment.class;
                        break;
                    case R.id.action_journal:
                        fragmentClass = JournalFragment.class;
                        break;
                    case R.id.action_more:
                        fragmentClass = MoreFragment.class;
                        break;
                    default:
                        fragmentClass = MoreFragment.class;
                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

                // Highlight the selected item has been done by NavigationView
                item.setChecked(true);
                // Set action bar title
                setTitle(item.getTitle());
                // Close the navigation drawer
                return loadFragment(fragment);
            }
        });



        //endregion

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            //initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            //releasePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //hideSystemUi();
//        if ((Util.SDK_INT <= 23 || player == null)) {
//            //initializePlayer();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            //releasePlayer();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        //myVideoView = findViewById(R.id.video_view2);
        //savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        //myVideoView.pause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //we use onRestoreInstanceState in order to play the video playback from the stored position
        //myVideoView = findViewById(R.id.video_view2);
        //position = savedInstanceState.getInt("Position");
        //myVideoView.seekTo(position);

    }

    //endregion



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //region Drawer
    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem item) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.action_program:
                fragmentClass = ProgramFragment.class;
                break;
            case R.id.action_tv:
                fragmentClass = TVFragment.class;
                break;
            case R.id.action_radio:
                fragmentClass = RadioFragment.class;
                break;
            case R.id.action_journal:
                fragmentClass = JournalFragment.class;
                break;
            case R.id.action_more:
                fragmentClass = MoreFragment.class;
                break;
            default:
                fragmentClass = MoreFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
    }
    //endregion

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        switch (item.getItemId()) {
            case R.id.action_program:
                fragmentClass = ProgramFragment.class;
                break;
            case R.id.action_tv:
                fragmentClass = TVFragment.class;
                break;
            case R.id.action_radio:
                fragmentClass = RadioFragment.class;
                break;
            case R.id.action_journal:
                fragmentClass = JournalFragment.class;
                break;
            case R.id.action_more:
                fragmentClass = MoreFragment.class;
                break;
            default:
                fragmentClass = MoreFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        item.setChecked(true);
        // Set action bar title
        setTitle(item.getTitle());
        return loadFragment(fragment);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

}

