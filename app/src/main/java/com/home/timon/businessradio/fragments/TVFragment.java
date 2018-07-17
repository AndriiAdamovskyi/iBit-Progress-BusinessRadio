package com.home.timon.businessradio.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.home.timon.businessradio.MainActivity;
import com.home.timon.businessradio.R;

/**
 * Created by timon9551 on 7/9/2018.
 */

public class TVFragment extends Fragment {

    private boolean paused;
    private Button bt_play_pause;
    private View viewFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        paused = true;
        viewFragment = inflater.inflate(R.layout.fragment_tv, container, false);
        bt_play_pause = viewFragment.findViewById(R.id.tv_button_play_pause);
        bt_play_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24px);
        bt_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_button_play_pause:
                        if (paused) {
                            bt_play_pause.setBackgroundResource(R.drawable.ic_baseline_pause_24px);
                            paused = false;
                        } else {
                            bt_play_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24px);
                            paused = true;
                        }
                }
            }
        });
        return viewFragment;
    }
}
