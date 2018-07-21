package com.home.timon.spsradio.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.ibitprogress.timon.spsradio.R;
import com.home.timon.spsradio.RadioService;

import java.util.Objects;

/**
 * Created by timon9551 on 7/9/2018.
 */

public class RadioFragment extends Fragment {

    //region Vars

    private static final String TAG = RadioFragment.class.getName();

    private boolean paused;
    private Button bt_play_pause;
    private View viewFragment;

    //Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private String id;
    private String name;

    private Intent radio_intent;

    //endregion

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        paused = true;
        viewFragment = inflater.inflate(R.layout.fragment_radio, container, false);



        //region Button Play/Pause logic
        bt_play_pause = viewFragment.findViewById(R.id.radio_button_play_pause);
        bt_play_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24px);
        bt_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radio_intent = new Intent(getActivity(), RadioService.class);
                switch (view.getId()) {
                    case R.id.radio_button_play_pause:

                        //Firebase Analytics
                        AnalyticsOnClickItem("0", "Radio play button", mFirebaseAnalytics);

                        if (paused) {
                            bt_play_pause.setBackgroundResource(R.drawable.ic_baseline_pause_24px);
                            startRadioService();
                            paused = false;
                        } else {
                            bt_play_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24px);
                            paused = true;
                            stopRadioService();
                        }
                }
            }

            private void startRadioService() {
                Objects.requireNonNull(getActivity()).startService(radio_intent);
            }

            private void stopRadioService() {
                Objects.requireNonNull(getActivity()).stopService(radio_intent);
            }
        });
        //endregion

        return viewFragment;
    }




    private void AnalyticsOnClickItem(String id, String name, FirebaseAnalytics mFirebaseAnalytics) {
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }
}