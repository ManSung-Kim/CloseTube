//package jactlab.closetube;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.TextView;
//
//import com.google.android.youtube.player.YouTubePlayer;
//import com.google.android.youtube.player.YouTubePlayerView;
//
//import jactlab.closetube.background.StreamService;
//
//
//public class MainActivity_cpy extends YouTubeFailureRecoveryActivity {
//
//    public static YouTubePlayer gmPlayer = null;
//    public static Context gContext;
//    public static TextView t;
//
//    // service
//    private Intent pmService = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        gContext = getApplicationContext();
//
//
//        t = (TextView) findViewById(R.id.textview);
//        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
//        /*youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
//        youTubeView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                smPlayer.play();
//                return false;
//            }
//        });
//*/
//
//        pmService = new Intent(this, StreamService.class);
//        startService(pmService);
//    }
//
//
//    public void onDestroy() {
//        super.onDestroy();
//        if(pmService != null) {
//            stopService(pmService);
//            pmService = null;
//        }
//    }
//
//    protected void onPause() {
//
//        //Intent lmService = new Intent(this, StreamService.class);
//       // startService(lmService);
//    }
//
//    @Override
//    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
//                                        boolean wasRestored) {
//        if (!wasRestored
//                && gmPlayer==null
//                ) {
//            gmPlayer = player;
//           //player.cueVideo("wKJ9KzGQq0w");
//
//            MainActivity_cpy.gmPlayer.loadVideo("wKJ9KzGQq0w");
//           // Intent lmService = new Intent(this, StreamService.class);
//            //startService(lmService);
//        }
//       // player.play();
//    }
//
//    @Override
//    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
//        return (YouTubePlayerView) findViewById(R.id.youtube_view);
//    }
//}
