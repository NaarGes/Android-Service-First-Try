package com.example.user.servicefirsttry;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private MyService myService;
    private Handler handler;

    private static final int TIMER_INTERVAL = 5000; // 5 sec

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        textView = findViewById(R.id.number);

//        if (!isMyServiceRunning(myService.getClass())) {
            Intent intent = new Intent(this, MyService.class);
            startService(intent);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
//        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            final MyService.LocalBinder binder = (MyService.LocalBinder) service;
            myService = binder.getService();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handler.postDelayed(this, TIMER_INTERVAL);
                    textView.setText(String.valueOf(myService.getRandomNumber()));
                }
            };
            handler.post(runnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService.stopSelf();
        }
    };

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }
}
