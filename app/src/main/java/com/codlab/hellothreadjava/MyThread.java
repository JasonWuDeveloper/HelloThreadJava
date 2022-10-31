package com.codlab.hellothreadjava;

import android.util.Log;

public class MyThread extends Thread{
    @Override
    public void run() {
        Log.d("TAG",Thread.currentThread().getName() + ".run()");
    }
}
