package com.example.app2048_1;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences record;
    private TextView tv_mainActivity_record;
    private TextView tv_mainActivity_score;
    private GameView gameView;
    private Button bu_mainActivity_revert;
    private Button bu_mainActivity_restart;
    private Button bu_mainActivity_options;
    private String TAG="==========";
    private TextView tv_mainActivity_targetcore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        record = getSharedPreferences("record", MODE_PRIVATE);
        int record_score_saved=record.getInt("record_score",0);

        RelativeLayout rl_mainactivity_center = (RelativeLayout) findViewById(R.id.rl_mainactivity_center);

        tv_mainActivity_targetcore = (TextView) findViewById(R.id.tv_mainActivity_targetcore);
        tv_mainActivity_record = (TextView)findViewById(R.id.tv_mainActivity_record);
        tv_mainActivity_score = (TextView)findViewById(R.id.tv_mainActivity_score);

        bu_mainActivity_revert = (Button) findViewById(R.id.bu_mainActivity_revert);
        bu_mainActivity_restart = (Button) findViewById(R.id.bu_mainActivity_restart);
        bu_mainActivity_options = (Button) findViewById(R.id.bu_mainActivity_options);

        bu_mainActivity_revert.setOnClickListener(this);
        bu_mainActivity_restart.setOnClickListener(this);
        bu_mainActivity_options.setOnClickListener(this);

        tv_mainActivity_record.setText(record_score_saved+"");


//        // 实例化广告条
//        AdView adView = new AdView(this, AdSize.FIT_SCREEN);
//
//        // 获取要嵌入广告条的布局
//        LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
//
//        // 将广告条加入到布局中
//        adLayout.addView(adView);

        // 实例化 LayoutParams（重要）
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

// 设置广告条的悬浮位置
        layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT; // 这里示例为右下角

// 实例化广告条
        AdView adView = new AdView(this, AdSize.FIT_SCREEN);

        // 调用 Activity 的 addContentView 函数
        this.addContentView(adView, layoutParams);


        //不变布局使用静态，游戏主布局使用动态
        gameView=new GameView(this);
        rl_mainactivity_center.addView(gameView);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.bu_mainActivity_options:
                option();
                break;
            case R.id.bu_mainActivity_restart:
                restart();
                break;

            case R.id.bu_mainActivity_revert:
                revert();
                break;
        }

    }

    public void setScore(int score)
    {
        tv_mainActivity_score.setText(score+"");
    }

    public void setTargetScore(int score){

        tv_mainActivity_targetcore.setText(score+"");
    }

    public void updateRecordScore(int record_score)
    {
        int record_score_saved=record.getInt("record_score",0);
        if(record_score>record_score_saved)
        {
            SharedPreferences.Editor editor=record.edit();
            editor.putInt("record_score",record_score);
            editor.commit();
            tv_mainActivity_record.setText(record_score+"");
        }
    }

    private void revert()
    {
        new AlertDialog.Builder(this)
                .setTitle("重新开始")
                .setMessage("确定要撤销上一步吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.revert();
                    }
                })
                .setNegativeButton("否",null)
                .show();
    }

    private void restart() {

        new AlertDialog.Builder(this)
                .setTitle("重新开始")
                .setMessage("确定要重新开始新一局吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameView.restart();
                    }
                })
                .setNegativeButton("否",null)
                .show();

    }

    private void option() {
        startActivityForResult(new Intent(this,OptionActivity.class),100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==100&&resultCode==200)
        {

            int line=data.getIntExtra("lineNumber",4);
            int targetScore=data.getIntExtra("targetScore",2048);
            Log.i(TAG, line+"-----"+targetScore);

            //更新设置之后的分数
            setTargetScore(targetScore);
            setScore(0);

            //更新棋盘
            gameView.set_rowNumber(line);
            gameView.set_columnNumber(line);
            gameView.setTarget_score(targetScore);
            gameView.restart();
            Log.i(TAG, "restart");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
