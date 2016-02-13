package jactlab.closetube.background;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import jactlab.closetube.MainActivity;
import jactlab.closetube.R;
import jactlab.closetube.Utils;

/**
 * Created by KIMMANSUNG-NOTE on 2016-02-11.
 */
public class StreamService extends Service {

    private WindowManager pmWManager = null;
    private WindowManager.LayoutParams pmWParams = null;
    private ImageView pmImageView = null;

    private float pmDownInitX = 0;
    private float pmDownInitY = 0;

    public void onCreate() {
        super.onCreate();

        // Window Manager
        pmWManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Layout Params
        pmWParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        pmWParams.gravity = Gravity.TOP | Gravity.LEFT;
        pmWParams.x = 100;
        pmWParams.y = 100;

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

        pmWManager.addView(pmImageView, pmWParams);
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
