package com.example.app2048_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.youmi.android.AdManager;
import net.youmi.android.spot.SpotManager;

public class AdvertisePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise_page);

        AdManager.getInstance(this).init("eabf154c255320f6", "93c27051f2daeb86", true);

        //加载广告到本地APP
        SpotManager.getInstance(this).loadSpotAds();

        SpotManager.getInstance(this).setSpotOrientation(SpotManager.ORIENTATION_PORTRAIT);

        SpotManager.getInstance(this).setAnimationType(SpotManager.ANIM_ADVANCE);

        SpotManager.getInstance(this).showSpotAds(this);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.currentThread().sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(AdvertisePage.this,MainActivity.class));
                finish();
            }
        }).start();
    }
}
