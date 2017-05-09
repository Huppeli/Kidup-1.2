package com.thedroidboy.lockscreentest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.CountDownTimer;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class CountDownService extends Service {

    public int onStartCommand (Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
//        Log.d("VEIKKO2", "Time got" + timeGot);
        if (intent != null) {
            timeGot = intent.getFloatExtra("timeGot", 0);
            timeLeft = timeLeft + (long)timeGot;
            mTimer.cancel();
            startTimer();
            Log.d("VEIKKO2", "cancel timer if there is some step");

//            timeGot = 0;
            Log.d("VEIKKO2", "Time got" + timeGot);
            Log.d("VEIKKO2", "Time left after take more step " + timeLeft);
        }
        return START_STICKY;

    }
    @SuppressLint("NewApi")

    CountDownTimer mTimer = null;
    static long timeLeft = 9000000 ;
    float timeGot = 0 ;
    boolean timer_was_touched = false;

//    public static class CounterClass extends CountDownTimer {
//
//        public CounterClass(long millisInFuture, long countDownInterval){
//            super(millisInFuture, countDownInterval);
//        }
//
////        @Override
////        public void onTick(long millisUntilFinished){
////            long millis = millisUntilFinished ;
//////            MainActivity.timeLeft = millisUntilFinished;
////            String hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)) );
////
////            System.out.println(hms);
////            MainActivity.textViewTime.setText(hms);
////        }
////
////        @Override
////        public void onFinish(){
////
////
////            MainActivity.textViewTime.setText("Out of time!");
////            boolean active = MainActivity.deviceManager.isAdminActive(MainActivity.compName);
////            if (active) {
////                MainActivity.deviceManager.lockNow();
////            }
////        }
//    }

    public CountDownService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d("VEIKKO2", "On Create in the service");
        startTimer();

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        IntentFilter ScreenOn = new IntentFilter(Intent.ACTION_SCREEN_ON);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("VEIKKO2", "ACTION_SCREEN_OFF");
//                if (mTimer != null)
//                {
//                    mTimer.cancel();
//                }
                if (timer_was_touched == true){
                    mTimer.cancel();
                    Log.d("VEIKKO2", "cancel timer ");
                }
            }
        },filter );

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("VEIKKO2", "ACTION_SCREEN_ON");
                if (mTimer != null)
                {
                    mTimer.cancel();
                    Log.d("VEIKKO2", "cancel timer ");
                }
//                timeGot = intent.getFloatExtra("timeGot", 0);
//                timeLeft = timeLeft + (long) timeGot;
//                Log.d("VEIKKO2","long time got" + (long) timeGot );
                if (timer_was_touched == true){

                    mTimer.cancel();
                    Log.d("VEIKKO2", "cancel timer ");

                }

                if (timeGot != 0){

                    mTimer.cancel();
                    Log.d("VEIKKO2", "cancel timer ");

                }

                mTimer = new CountDownTimer(timeLeft, 1000) {

                    public void onTick(long millisUntilFinished) {
                        long millis = millisUntilFinished ;
                        String hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)) );

                        System.out.println(hms);
                       /* MainActivity.textViewTime.setText(hms); */
                        MainActivity.pieView.setInnerText(hms);
                        timeLeft = millisUntilFinished;
                        Intent mIntent = new Intent(CountDownService.this, MainActivity.class);
                        mIntent.putExtra("timeLeft", millisUntilFinished);
                        CountDownService.this.startService(mIntent);
                        Log.d("VEIKKO2", "Time left on tick : " + timeLeft);
                        Log.d("VEIKKO2", "Sending time left to main : " + millisUntilFinished);
                    }



                    public void onFinish() {
                        Log.d("VEIKKO2", "Timer onFinish");
                      /*  MainActivity.textViewTime.setText("Out of time!"); */
                        MainActivity.pieView.setInnerText("Out of time!");
                        DevicePolicyManager deviceManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
                        ComponentName compName = new ComponentName(CountDownService.this, MyAdmin.class);
                        boolean active = deviceManager.isAdminActive(compName);
                        if (active) {
                            Log.d("VEIKKO2", "Locking the screen!");

                            deviceManager.lockNow();
                        }
                    }
                }.start();
                }

        },ScreenOn );

       // MainActivity.timer = new CounterClass((long)MainActivity.timeLeft,1000);
       // MainActivity.timer.start();

    }

    protected void startTimer()
    {
//        if (mTimer != null)
//        {
//            mTimer.cancel();
//        }
//        Intent intent = new Intent();
//        timeGot = intent.getFloatExtra("timeGot", 0);
//        timeLeft = timeLeft + (long) timeGot;
        timer_was_touched = true;
//        mTimer.cancel();
        mTimer = new CountDownTimer(timeLeft, 1000) {

            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished ;
                String hms = String.format("%02d:%02d:%02d" , TimeUnit.MILLISECONDS.toHours(millis),TimeUnit.MILLISECONDS.toMinutes(millis)-TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)) );

                System.out.println(hms);
               /* MainActivity.textViewTime.setText(hms); */
                MainActivity.pieView.setInnerText(hms);
                timeLeft = millisUntilFinished;
                Intent mIntent = new Intent(CountDownService.this, MainActivity.class);
                mIntent.putExtra("timeLeft", millisUntilFinished);
                CountDownService.this.startService(mIntent);
                Log.d("VEIKKO2", "Time left ontick in service" + timeLeft);

//                if (timeLeft <= 100000){
//                    boolean timenoti = true;
//                    Intent timenotiIntent = new Intent(CountDownService.this, MainActivity.class);
//                    mIntent.putExtra("timenoti", timenoti);
//                    CountDownService.this.startService(timenotiIntent);
//                    Log.d("VEIKKO2" , "service sending timenoti Intent " + timenotiIntent);
//
//                }


            }

            public void onFinish() {
                Log.d("VEIKKO2", "Timer onFinish");
                /*MainActivity.textViewTime.setText("Out of time!"); */
                MainActivity.pieView.setInnerText("Out of time!");
                DevicePolicyManager deviceManager = (DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
                ComponentName compName = new ComponentName(CountDownService.this, MyAdmin.class);
                boolean active = deviceManager.isAdminActive(compName);
                if (active) {
                    Log.d("VEIKKO2", "Locking the screen!");

                    deviceManager.lockNow();
                }
            }
        }.start();

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
