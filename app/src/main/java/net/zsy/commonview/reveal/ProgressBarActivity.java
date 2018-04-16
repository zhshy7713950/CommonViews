package net.zsy.commonview.reveal;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import net.zsy.commonview.progressbar.RingProgressBar;

/**
 * Created by Android on 2018/4/11.
 */

public class ProgressBarActivity extends Activity {

    private RingProgressBar rpb;
    private int progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressbar_layout);
        rpb = findViewById(R.id.rpb);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress <= 100){
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rpb.post(new Runnable() {
                        @Override
                        public void run() {
                            rpb.setProgress(progress++);
                        }
                    });
                }
            }
        }).start();
    }
}
