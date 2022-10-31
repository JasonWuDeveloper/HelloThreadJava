package com.codlab.hellothreadjava;

import android.util.Log;

public class MyRunnable implements Runnable{
    @Override
    public void run() {
        Log.d("TAG",Thread.currentThread().getName()+".run()");
    }
}
