package com.home.timon.businessradio.fragments;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.home.timon.businessradio.MainActivity;
import com.home.timon.businessradio.R;

/**
 * Created by timon9551 on 7/9/2018.
 */

public class TVFragment extends Fragment {

    private boolean paused;
    private Button bt_play_pause;
    private View viewFragment;

    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private VideoView myVideoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        paused = true;
        viewFragment = inflater.inflate(R.layout.fragment_tv, container, false);

        //region Button Play/Pause
        bt_play_pause = viewFragment.findViewById(R.id.tv_button_play_pause);
        bt_play_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24px);
        bt_play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_button_play_pause:
                        if (paused) {
                            bt_play_pause.setBackgroundResource(R.drawable.ic_baseline_pause_24px);
                            myVideoView.start();
                            myVideoView.setMediaController(null);
                            paused = false;
                        } else {
                            bt_play_pause.setBackgroundResource(R.drawable.ic_baseline_play_arrow_24px);
                            paused = true;
                            myVideoView.pause();
                        }
                }
            }
        });
        //endregion

        //region VideoView

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(getContext());
        }

        //initialize the VideoView
        myVideoView = viewFragment.findViewById(R.id.video_view2);

        // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(getContext());
        // set a title for the progress bar
        progressDialog.setTitle("TV");
        // set a message for the progress bar
        progressDialog.setMessage("Loading...");
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();

        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            myVideoView.setVideoURI(Uri.parse("http://radiosps.com/polska.mp4"));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();

        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
                progressDialog.dismiss();

                //if we have a position on savedInstanceState, the video playback should start from here
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }
            }
        });



        //endregion

        return viewFragment;
    }
}
