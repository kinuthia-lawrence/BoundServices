package com.larrykin343.boundservices;

import androidx.appcompat.app.AppCompatActivity;

import android.content.AsyncQueryHandler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private boolean isBound = false;
    private ExampleService service;
    private TextView textView;
    private DisplayRandomAsyncTask displayRandomAsyncTask;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ExampleService.LocalBinder binder = (ExampleService.LocalBinder) iBinder;
            service = binder.getService();

            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//doing background task with interaction  with the client(UI) eg. activity unlike started services.
        /*
        * services are another solution to do your task in the background
        * started service--are not bound to any client, eg activity or any bracket receiver>> you can't have any interraction with the client(cannot update UI)
        * bound services are bound to the client
        * */
        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this,ExampleService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound && service != null){
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != displayRandomAsyncTask){
            if(!displayRandomAsyncTask.isCancelled()){
                displayRandomAsyncTask.cancel(true);
            }
        }
    }

    public class DisplayRandomAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i =0; i<10; i++){
                if (isBound && service != null){
                    publishProgress(service.getRandom());
                    SystemClock.sleep(1000);
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            textView.setText(String.valueOf(values[0]));

            displayRandomAsyncTask = new DisplayRandomAsyncTask();
            displayRandomAsyncTask.execute();
        }
    }
}