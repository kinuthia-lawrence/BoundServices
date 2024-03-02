package com.larrykin343.boundservices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Locale;
import java.util.Random;

public class ExampleService extends Service {
    private IBinder binder = new LocalBinder();
    private Random random = new Random();
    //    create a communication between service and its client

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    public class LocalBinder extends Binder {
        ExampleService getService() {
            return ExampleService.this;
        }
    }
    public int getRandom(){
        return random.nextInt();
    }
}
