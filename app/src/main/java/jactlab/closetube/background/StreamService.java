package jactlab.closetube.background;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import jactlab.closetube.R;
import jactlab.closetube.StaticData;
import jactlab.closetube.Utils;

/**
 * Created by KIMMANSUNG-NOTE on 2016-02-11.
 */
public class StreamService extends Service {

    private WindowManager pmWManager = null;
    private WindowManager.LayoutParams pmWParams = null;
    private ImageView pmImageView = null;

    private FrameLayout pmMainLayout = null;
    private WebView pmWebView = null;
    private WebViewClient pmWebClient = null;
    private LinearLayout pmTransLayout = null;
    private LinearLayout.LayoutParams pmTransLayoutParam = null;

    //
    private float pmDownInitX = 0;
    private float pmDownInitY = 0;
    private float pmMoveMainX = 0;
    private float pmMoveMainY = 0;
    private int pmInitDownX = 0;
    private int pmInitDownY = 0;
    private float pmInitDownRawX = 0;
    private float pmInitDownRawY = 0;

    // web source
    private static String pmWebVideoHash = "wxxb311BWRM";
    private static String pmWebSource =
            "<iframe " +
                    "src=\"https://www.youtube.com/embed/" +
                    pmWebVideoHash +
                    "\" frameborder=\"0\" allowfullscreen></iframe>";

    // long press gesture
    private static float pgLongTouchInitX = 0;
    private static float pgLongTouchInitY = 0;
    private static float pgLongTouchEndX = 0;
    private static float pgLongTouchEndY = 0;
    private static boolean pgTouchState = false;
    private static boolean pgLongTouchState = false;
    private final Handler pfmHandler = new Handler();
    private Runnable pmRunnableLongPress = new Runnable() {
        @Override
        public void run() {
            if(pgTouchState == true &&
                    (
                        Math.abs(pgLongTouchInitX-pgLongTouchEndX)<StaticData.LONG_PRESS_DISTANCE
                        && Math.abs(pgLongTouchInitY-pgLongTouchEndY)<StaticData.LONG_PRESS_DISTANCE
                    )
               ) {
                Utils.logd("Long press");
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(100);
                pgTouchState = false;
                pgLongTouchState = true;
            }
        }
    };

    public void onCreate() {
        super.onCreate();

        // Window Manager
        pmWManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Layout Params
        pmWParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                //WindowManager.LayoutParams.TYPE_PHONE,
                //WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                //PixelFormat.TRANSLUCENT
                //WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        pmWParams.gravity = Gravity.TOP | Gravity.LEFT;
        pmWParams.x = 100;
        pmWParams.y = 100;
        //pmWParams.x = 0;
        //pmWParams.y = 0;
        pmWParams.width = StaticData.WEBVIEW_WIDTH;
        pmWParams.height = StaticData.WEBVIEW_HEIGHT;

        //
        pmMainLayout = new FrameLayout(this);
        FrameLayout.LayoutParams lmMainLayoutParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        lmMainLayoutParam.width = StaticData.WEBVIEW_WIDTH;
        lmMainLayoutParam.height = StaticData.WEBVIEW_HEIGHT;
        pmMainLayout.setLayoutParams(lmMainLayoutParam);

        //
        pmWebView = new WebView(this);
        pmWebView.getSettings().setJavaScriptEnabled(true);
        pmWebView.getSettings().setLoadWithOverviewMode(true); // 크기 자동 조정
        pmWebView.setClickable(false);
        pmWebView.setFocusable(false);
        pmWebView.setLongClickable(false);
        pmWebView.setFocusableInTouchMode(false);
        pmWebView.setHorizontalScrollBarEnabled(false);
        pmWebView.setVerticalScrollBarEnabled(false);
        pmWebView.setBackgroundColor(0);
        LinearLayout.LayoutParams lmWebViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lmWebViewParam.width = StaticData.WEBVIEW_WIDTH;
        lmWebViewParam.height = StaticData.WEBVIEW_HEIGHT;
        pmWebView.setLayoutParams(lmWebViewParam);
        pmWebClient = new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Utils.logd("url : "+url);
                super.onPageStarted(view, url, favicon);
            }

        };
        pmWebView.setWebViewClient(pmWebClient);
        //pmWebView.loadUrl("https://www.youtube.com/watch?v=ufPTL-nbNs4");


        pmTransLayout = new LinearLayout(this);
        pmTransLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        pmTransLayoutParam.width = StaticData.WEBVIEW_WIDTH;
        pmTransLayoutParam.height = StaticData.WEBVIEW_HEIGHT;
        pmTransLayout.setLayoutParams(pmTransLayoutParam);
       /* pmTransLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.logd("Touch Long");
                //pmTransLayout.setOnTouchListener(pmTouchMoveListener);
                return true;
            }
        });*/
        pmTransLayout.setOnTouchListener(pmTouchMoveListener);


        //pmMainLayout.addView(pmWebView, lmWebViewParam);
        pmMainLayout.addView(pmWebView, pmWParams);
        //pmWManager.addView(lmTransLayout, pmWParams);
        pmMainLayout.addView(pmTransLayout, pmWParams);

        pmWManager.addView(pmMainLayout, pmWParams);
        //pmWebView.loadUrl("https://www.youtube.com/watch?v=ufPTL-nbNs4");
        pmWebView.loadData(pmWebSource, "text/html","UTF-8");

    }

    //private void generateMotionEvent(MotionEvent e, int action, long genTime) {
    private void generateMotionEvent(float touchX, float touchY, int action, long genTime) {
        long lmDownT = genTime;
        long lmEvtT = lmDownT + 100;
        float lmEvtX = touchX;
        float lmEvtY = touchY;
        int lmMetaState = 0;
        MotionEvent lmGenEvent = MotionEvent.obtain(
                lmDownT,lmEvtT,
                action,
                lmEvtX, lmEvtY,
                lmMetaState
        );
        pmWebView.dispatchTouchEvent(lmGenEvent);
    }

    public void onDestroy() {
        super.onDestroy();
        if(pmImageView != null) {
            pmWManager.removeView(pmImageView);
            pmImageView = null;
        }

        if(pmTransLayout != null) {
            pmMainLayout.removeView(pmTransLayout);
            pmTransLayout = null;
            Utils.logd("Delete transe layout");
        }
        if(pmWebView != null) {
            pmWebView.loadUrl("about:blank");
            pmMainLayout.removeView(pmWebView);
            pmWebView = null;
            Utils.logd("Delete webview layout");
        }
        if(pmMainLayout != null) {
            pmWManager.removeView(pmMainLayout);
            pmMainLayout = null;
            Utils.logd("Delete main layout");
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent,flags,startId);


        return START_STICKY;
    }

    private void initLongPressProcessingData(float updateX, float updateY) {
        pgLongTouchInitX = updateX;
        pgLongTouchInitY = updateY;
        pgLongTouchEndX = 0;
        pgLongTouchEndY = 0;
        pgTouchState = true; // is touch
        pgLongTouchState = false; // false
    }
    private void setLongPressProcessingData(float updateX, float updateY) {
        pgLongTouchEndX = updateX;
        pgLongTouchEndY = updateY;
    }
    private void removeLongPressData() {
        pgLongTouchInitX = 0;
        pgLongTouchInitY = 0;
        pgLongTouchEndX = 0;
        pgLongTouchEndY = 0;
        pgTouchState = false;
        pgLongTouchState = false;
    }

    private View.OnTouchListener pmTouchMoveListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                Utils.logd("Touch Down: "+event.getAction());

                // for Long press processing
                initLongPressProcessingData(event.getX(), event.getY());

                pfmHandler.postDelayed(pmRunnableLongPress, StaticData.LONG_PRESS_TIME);

                //Utils.logd("[rawX,rawY] "+event.getX()+" "+event.getY());
                pmInitDownX = pmWParams.x;
                pmInitDownY = pmWParams.y;
                pmInitDownRawX = event.getRawX();
                pmInitDownRawY = event.getRawY();
                //return false;
            } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                //Utils.logd("[X,Y]] "+event.getX()+" "+event.getY());

                // for long press processing
                setLongPressProcessingData(event.getX(), event.getY());

                // for layout move
                if(pgLongTouchState == true ) {
                    pmWParams.x = pmInitDownX + (int) (event.getRawX() - pmInitDownRawX);
                    pmWParams.y = pmInitDownY + (int) (event.getRawY() - pmInitDownRawY);
                    pmWManager.updateViewLayout(pmMainLayout, pmWParams);
                }
            } else if(event.getAction() == MotionEvent.ACTION_UP) {
                Utils.logd("Touch Up: "+event.getAction());
                // for generate touch event
                if(pgLongTouchState == false) {
                    Utils.logd("Gen touch event[x,y]: ["+pmInitDownX+","+pmInitDownY+"]");
                    long lmGenTime = SystemClock.uptimeMillis();
                    generateMotionEvent(pgLongTouchInitX, pgLongTouchInitY, MotionEvent.ACTION_DOWN, lmGenTime);
                    generateMotionEvent(pgLongTouchInitX, pgLongTouchInitY, MotionEvent.ACTION_UP, lmGenTime + StaticData.EVENT_GEN_ADD_TIME);
                } else if (pgLongTouchState == true) { // after long press
                    if( pmWParams.y <50 ) {
                        stopSelf();
                        return false;
                    }
                }

                removeLongPressData();
            }
            return true;
        }
    };

}
