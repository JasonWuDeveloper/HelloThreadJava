package com.codlab.hellothreadjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button btn;

    private Handler handler = new Handler() {
        //接收消息等待處理
        @Override
        public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 1 :
                btn.setText("123456789");
                break;
            case 2:
                btn.setText("23456");
                break;
            case 3:
                btn.setText("6543");
                break;
        }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn =(Button) findViewById(R.id.btn);
    }
    public void btnOnClick(View view) {
        aSyncThread();
    }
    //1.Handler 負責收發消息 通過handler實現其他線程與主線程之間的消息傳遞
    //2.Looper 負責管理線程的消息列隊和消息循環
    //3.Message 線程通訊的消息載體 裡面可存任何想傳遞的消息 (把要傳遞的消息封裝在message裏)
    //4.MessageQueue 消息對列 先進先出(先進來的message會先出去) 保存等待線程處理的消息
    //4個的關係是 在其他線程調用handler.message()方法(參數是Message對象)
    // 將需要MainThread處理的事件添加到MainThread的MessageQueue,
    // MainLooper從消息列隊中取出handler發過來的這個消息時,會回調handler的handlerMessage()方法
    public void aSyncThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(5 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                handler.sendEmptyMessageDelayed(1,1*1000);
                handler.sendEmptyMessageDelayed(2, 5*1000);
                handler.sendEmptyMessageDelayed(3, 10*1000);

            }
        }).start();
    }
    //緩存線程池
    public void testCache() {
        ExecutorService cacheThreadPool = Executors.newCachedThreadPool();
        for (int i=0; i<10;i++) {
            final int index = i;

            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cacheThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", Thread.currentThread().getName() + " " +index);


                }
            });
        }
    }
    //定長線程池,可控制縣城最大併發數,超出縣城會在列隊中等待
    public void testFixed() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

        for (int i = 0; i<10; i++) {
            final int index = i;
            fixedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", Thread.currentThread().getName() + " " + index);

                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    //定時線程池 定時任務,週期性任務
    public void testSchedule() {

        Log.d("TAG", "testScheduled");
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//
//        scheduledExecutorService.schedule(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("TAG","delay 3 seconds");
//            }
//        },3, TimeUnit.SECONDS);
        //定時2秒後執行 每隔3秒執行一次
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", "delay 2 seconds, and excute every 3 seconds");
            }
        },2,3,TimeUnit.SECONDS);
        scheduledExecutorService.shutdown();
    }
    //單一線程池,只會用唯一的工作線程執行任務,保證所有任務按指定順序執行
    public void testSingle() {
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int index = i;
            singleThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Log.d("TAG", Thread.currentThread().getName() + "" + index);

                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
        }
    }
    public void testThread() {
        MyThread mt1 = new MyThread();
        MyThread mt2 = new MyThread();
        mt1.start();
        mt2.start();
    }

    public void testRunnable() {
        MyRunnable mr1 = new MyRunnable();
        MyRunnable mr2 = new MyRunnable();
        Thread t1 = new Thread(mr1);
        Thread t2 = new Thread(mr2);
        t1.start();
        t2.start();
    }

    public void testSale() {
        SaleTicket saleTicket = new SaleTicket();

        Thread t1 = new Thread(saleTicket,"A代理");
        Thread t2 = new Thread(saleTicket,"B代理");
        Thread t3 = new Thread(saleTicket,"C代理");
        Thread t4 = new Thread(saleTicket,"D代理");

        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}