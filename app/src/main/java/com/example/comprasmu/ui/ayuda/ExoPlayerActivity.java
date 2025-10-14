package com.example.comprasmu.ui.ayuda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;


import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.comprasmu.R;

public class ExoPlayerActivity extends AppCompatActivity {
    private ExoPlayer player;
    private PlayerView playerView;
    public static String ARG_LIGAVIDEO="comprasmu.player.ligavideo";
    private String TAG="ExoPlayerActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo_player);
        playerView = findViewById(R.id.player_view);

        // Initialize ExoPlayer
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        Bundle bundle = getIntent().getExtras();

            // Build the MediaItem
       // String videoUrl = "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4";
        String videoUrl = "https://muesmerc.com/comprasv1/videotutoriales/prueba2.mp4";
        String liga;
        if (bundle != null) {  //pra tomar los que vienen del navhost selcleitefragment
            liga = bundle.getString(ARG_LIGAVIDEO);
            Log.i(TAG,"nombre archivo:"+liga);
        }
        Uri uri = Uri.parse(videoUrl);
        MediaItem mediaItem = MediaItem.fromUri(uri);
      //  https://www.youtube.com/watch?v=dQw4w9WgXcQ
        // Prepare the player with the media item
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true); // Start playing when ready
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.release();
            player = null;
        }

    }
}