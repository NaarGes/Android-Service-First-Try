package com.example.user.servicefirsttry;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class MyService extends Service {

    private final IBinder binder = new LocalBinder();
    private Random randomGenerator;
    private int randomNumber = 0;

    class LocalBinder extends Binder {
        MyService getService() { 
            return MyService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        getRandomNumber();
        return START_STICKY;
    }

    public int getRandomNumber() {
        randomNumber += randomGenerator.nextInt(10);
        if (!isForeground(getPackageName()))
            Toast.makeText(getApplicationContext(), String.valueOf(randomNumber),
                    Toast.LENGTH_SHORT).show();
        return randomNumber;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        randomGenerator = new Random();

        Notification notification = new Notification();
        startForeground(42, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind()");
        return binder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.i(TAG, "onRebind() ");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind() ");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //timer.cancel();
        Log.i(TAG, "onDestroy()");
    }

    public boolean isForeground(String myPackage) {
        // Get the Activity Manager
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        // Get a list of running tasks, we are only interested in the last one,
        // the top most so we give a 1 as parameter so we only get the topmost.
        List< ActivityManager.RunningTaskInfo > runningTaskInfos = manager.getRunningTasks(1);

        // Get the info we need for comparison.
        ComponentName componentInfo = runningTaskInfos.get(0).topActivity;

        // Check if it matches our package name.
        return componentInfo.getPackageName().equals(myPackage);
    }
}