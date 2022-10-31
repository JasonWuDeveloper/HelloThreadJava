package com.codlab.hellothreadjava;

import android.util.Log;

public class SaleTicket implements Runnable{

    private int ticket = 20;

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                if (ticket > 0) {
                    Log.d("TAG",Thread.currentThread().getName()+"賣出了第" +(20-ticket + 1) + "張票");
                    ticket--;
                }

                else {
                    break;
                }
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
