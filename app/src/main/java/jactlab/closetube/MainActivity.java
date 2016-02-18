package jactlab.closetube;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStyle;
import com.google.android.youtube.player.YouTubePlayer.PlaylistEventListener;
import com.google.android.youtube.player.YouTubePlayerView;

import jactlab.closetube.background.StreamService;


public class MainActivity extends Activity {

   // public static YouTubePlayer gmPlayer = null;
    public static Context gContext;
    public static TextView t;

    // service
    private Intent pmService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gContext = getApplicationContext();

       // YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        //youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        t = (TextView) findViewById(R.id.textview);
        /*youTubeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                smPlayer.play();
                return false;
            }
        });
*/

       pmService = new Intent(this, StreamService.class);
       startService(pmService);

        finish();
    }


    public void onDestroy() {
        super.onDestroy();
        /*if(pmService != null) {
            stopService(pmService);
            pmService = null;
        }*/
    }

    protected void onPause() {
        super.onPause();
        //Intent lmService = new Intent(this, StreamService.class);
       // startService(lmService);
    }

  /* @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored
                && gmPlayer==null
                ) {
            gmPlayer = player;
           //player.cueVideo("wKJ9KzGQq0w");

            MainActivity.gmPlayer.loadVideo("wKJ9KzGQq0w");
           // Intent lmService = new Intent(this, StreamService.class);
            //startService(lmService);
        }
       // player.play();
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }*/
}
