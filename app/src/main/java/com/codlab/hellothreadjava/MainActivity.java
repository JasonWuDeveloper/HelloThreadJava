package com.codlab.hellothreadjava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button btn;
    private ProgressBar pb;
    private int progress;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
        switch (msg.what) {
            case 1 :
                btn.setText("123456789");
                break;
            case 2:
                String string = (String) msg.obj;
                btn.setText(string);
                break;
            case 3:
                if (progress < 100) {
                    progress += 10;
                    pb.setProgress(progress);
                    handler.sendEmptyMessageDelayed(3, 2* 1000);
                }
                break;
        }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn =(Button) findViewById(R.id.btn);
        pb = (ProgressBar) findViewById(R.id.pb);
    }
    public void btnOnClick(View view) {
//        aSyncThread();
//    testTimer();
//        testPost();
    new TryAsync().execute();

    }

    class TryAsync extends AsyncTask<Void,Integer,Boolean> {
    int progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TAG", "準備下載");
            pb.setVisibility(View.VISIBLE);
        }
        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.d("TAG", "正在下載");

            try {
                while (true) {
                 //每隔1秒下載10%
                        Thread.sleep(1000);
                        progress+=10;
                        publishProgress(progress);
                    if (progress >= 100) {
                        break;
                    }
                }
            }catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("TAG", "下載回調:" + values[0]);
            pb.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                Log.d("TAG", "下載成功");
                pb.setVisibility(View.GONE);
            } else {
                Log.d("TAG","下載失敗");
            }
        }
    }


    class TestAsync extends AsyncTask<Void, Void, String> {

        //當前還在主線程當中,做一些準備工作
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("TAG", "onPreExecute");
        }
        //在異步線成立面執行
        @Override
        protected String doInBackground(Void... voids) {

            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String string = "123ABC456";
            //通知主線程當前的進度是多少
//            publishProgress(10);
            return string;
        }
        //當前切換到主線程, 可以根據傳遞的參數做UI的更新
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.d("TAG","onProgressUpdate");
        }

        // 切換到主線程裡面執行
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("TAG", "onPostExecute");
            btn.setText(s);
        }
    }




    public void testPost() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("TAG", "handler post");
//            }
//        });
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("TAG", "handler post delay");
//            }
//        },2 *1000);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                //在非UI線程裡面更新UI控件
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        btn.setText("123ABC");
//                    }
//                });
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        btn.setText("234ABC");
//                    }
//                }, 2 * 1000);
//                btn.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        btn.setText("View123");
//                    }
//                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn.setText("Run On UI Thread");
                    }
                });
            }
        }).start();
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

                String string = "ABC123";

                Message message = new Message();
                message.what = 2;
                message.obj = string;

                handler.sendMessageDelayed(message,5 *1000);
                handler.sendEmptyMessageDelayed(1,1*1000);
//                handler.sendEmptyMessageDelayed(2, 5*1000);
//                handler.sendEmptyMessageDelayed(3, 10*1000);

            }
        }).start();
    }
    public void testTimer() {
        pb.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(3, 2 * 1000);
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