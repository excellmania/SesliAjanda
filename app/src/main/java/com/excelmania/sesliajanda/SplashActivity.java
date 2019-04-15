package com.excelmania.sesliajanda;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends Activity {
    private static long SLEEP_TIME = 2;
    private static String TAG = SplashActivity.class.getName();

    private class IntentLauncher extends Thread {
        private IntentLauncher() {
        }

        public void run() {
            try {
                Thread.sleep(SplashActivity.SLEEP_TIME * 1000);
            } catch (Exception e) {
                Log.e(SplashActivity.TAG, e.getMessage());
            }
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, CustomizeListView.class));
            SplashActivity.this.finish();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.splash);
        new IntentLauncher().start();
    }
}