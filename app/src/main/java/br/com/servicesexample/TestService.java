package br.com.servicesexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TestService extends Service {

    public List<Worker> threads = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("LOG", "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LOG", "onStartCommand()");
        Worker worker = new Worker(startId);
        worker.start();
        threads.add(worker);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < threads.size(); i++) {
            threads.get(i).active = false;
        }
    }

    private class Worker extends Thread {
        int count = 0;
        int startId;
        boolean active = true;

        Worker(int startId) {
            this.startId = startId;
            Log.i("LOG", "Worker()");
        }

        @Override
        public void run() {
            super.run();
            while (active && count < 10) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                Log.i("LOG", "Count: " + count);
            }
            stopSelf(startId);
        }
    }
}
