package com.home.timon.businessradio;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.home.timon.businessradio.fragments.JournalFragment;
import com.home.timon.businessradio.fragments.MoreFragment;
import com.home.timon.businessradio.fragments.ProgramFragment;
import com.home.timon.businessradio.fragments.RadioFragment;
import com.home.timon.businessradio.fragments.TVFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading the default fragment
        loadFragment(new RadioFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()){
                    case R.id.action_program:
                        fragment = new ProgramFragment();
                        break;
                    case R.id.action_tv:
                        fragment = new TVFragment();
                        break;
                    case R.id.action_radio:
                        fragment = new RadioFragment();
                        break;
                    case R.id.action_journal:
                        fragment = new JournalFragment();
                        break;
                    case R.id.action_more:
                        fragment = new MoreFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;
    }
}
