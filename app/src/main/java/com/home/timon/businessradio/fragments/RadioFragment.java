package com.home.timon.businessradio.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.home.timon.businessradio.MainActivity;
import com.home.timon.businessradio.R;

import java.io.IOException;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by timon9551 on 7/9/2018.
 */

public class RadioFragment extends Fragment {

    //region Vars

    private static final String TAG = RadioFragment.class.getName();

    private boolean paused;
    private Button bt_play_pause;
    private View viewFragment;

    //endregion

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        paused = true;
        viewFragment = inflater.inflate(R.layout.fragment_radio, container, false);
        bt_play_pause = viewFragment.findViewById(R.id.radio_button_play_pause);
        bt_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.radio_button_play_pause:
                        if (paused = true) {
                            ((MainActivity)getActivity()).playRadio();
                            paused = false;
                            bt_play_pause.setBackgroundResource(R.mipmap.pause_iconhdpi);
                        } else if (paused = false){
                            bt_play_pause.setBackgroundResource(R.mipmap.play_iconhdpi);
                        }
                }
            }
        });
        return viewFragment;
    }


}
