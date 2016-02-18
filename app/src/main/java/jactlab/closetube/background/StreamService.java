package jactlab.closetube.background;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.SystemClock;
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
        pmWebView.setClickable(false);
        pmWebView.setFocusable(false);
        pmWebView.setLongClickable(false);
        pmWebView.setFocusableInTouchMode(false);
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
        pmTransLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Utils.logd("Touch Long");
               /* pmTransLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Utils.logd("Touch : "+event.getAction());
                        if(event.getAction() == MotionEvent.ACTION_DOWN) {
                            pmDownInitX = event.getX();
                            pmDownInitY = event.getY();
                        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                            pmWParams.x = pmWParams.x + (int)(event.getX() - pmDownInitX);
                            pmWParams.y = pmWParams.y + (int)(event.getY() - pmDownInitY);
                            pmWManager.updateViewLayout(pmMainLayout, pmWParams);
                        } else if(event.getAction() == MotionEvent.ACTION_UP) {
                            pmTransLayout.setOnTouchListener(null);
                        }
                        return true;
                    }
                });*/
                return true;
            }
        });
        pmTransLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.logd("Touch : "+event.getAction());
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    long lmGenTime = SystemClock.uptimeMillis();
                    generateMotionEvent(MotionEvent.ACTION_DOWN, lmGenTime);
                    generateMotionEvent(MotionEvent.ACTION_UP, lmGenTime+StaticData.EVENT_GEN_ADD_TIME);

                    pmInitDownX = pmWParams.x;
                    pmInitDownY = pmWParams.y;
                    pmInitDownRawX = event.getRawX();
                    pmInitDownRawY = event.getRawY();
                } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    Utils.logd("[X,Y]] "+event.getX()+" "+event.getY());
                    pmWParams.x = pmInitDownX + (int)(event.getRawX() - pmInitDownRawX);
                    pmWParams.y = pmInitDownY + (int)(event.getRawY() - pmInitDownRawY);

                    pmWManager.updateViewLayout(pmMainLayout, pmWParams);
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                }
                return true;
            }
        });

        /*pmTransLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utils.logd("Touch");
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    pmDownInitX = event.getX();
                    pmDownInitY = event.getY();
                } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                    pmWParams.x = pmWParams.x + (int)(event.getX() - pmDownInitX);
                    pmWParams.y = pmWParams.y + (int)(event.getY() - pmDownInitY);
                    pmWManager.updateViewLayout(pmMainLayout, pmWParams);
                }
                return true;
            }
        });*/

        //pmMainLayout.addView(pmWebView, lmWebViewParam);
        pmMainLayout.addView(pmWebView, pmWParams);
        //pmWManager.addView(lmTransLayout, pmWParams);
        pmMainLayout.addView(pmTransLayout, pmWParams);

        pmWManager.addView(pmMainLayout, pmWParams);
        pmWebView.loadUrl("https://www.youtube.com/watch?v=ufPTL-nbNs4");

        /*

        //
        pmImageView = new ImageView(this);
        pmImageView.setImageResource(R.drawable.ic_launcher);
        pmImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    pmDownInitX = event.getX();
                    pmDownInitY = event.getY();
                } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                     pmWParams.x = pmWParams.x + (int)(event.getX() - pmDownInitX);
                     pmWParams.y = pmWParams.y + (int)(event.getY() - pmDownInitY);
                     pmWManager.updateViewLayout(pmImageView, pmWParams);
                 }
                return true;
            }
        });

        pmWManager.addView(pmImageView, pmWParams);*/
    }

    private void generateMotionEvent(int action, long genTime) {
        long lmDownT = genTime;
        long lmEvtT = lmDownT + 100;
        float lmEvtX = 250.0f;
        float lmEvtY = 250.0f;
        int lmMetaState = 0;
        MotionEvent e = MotionEvent.obtain(
                lmDownT,lmEvtT,
                //MotionEvent.ACTION_DOWN,
                action,
                lmEvtX, lmEvtY,
                lmMetaState
        );
        pmWebView.dispatchTouchEvent(e);
    }

    public void onDestroy() {
        super.onDestroy();
        if(pmImageView != null) {
            pmWManager.removeView(pmImageView);
            pmImageView = null;
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

}
