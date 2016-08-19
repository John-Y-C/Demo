package com.example.app2048_1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends AppCompatActivity implements GameView.OnClickListener {

    private Button bt_optionactivity_done;
    private Button bt_optionactivity_score;
    private Button bt_optionactivity_lines;
    private Button bt_optionactivity_back;
    private SharedPreferences sharedPreferences;
    private int lineNumber;
    private int targetScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        //取消标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //取消状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        new Thread()
        {
            @Override
            public void run() {
                super.run();
            }
        }.start();

        sharedPreferences = getSharedPreferences("record",MODE_PRIVATE);
        lineNumber = sharedPreferences.getInt("lineNumber", 4);
        targetScore = sharedPreferences.getInt("targetScore", 2048);

        bt_optionactivity_lines = (Button) findViewById(R.id.bt_optionactivity_lines);
        bt_optionactivity_score = (Button) findViewById(R.id.bt_optionactivity_score);
        bt_optionactivity_back = (Button) findViewById(R.id.bt_optionactivity_back);
        bt_optionactivity_done = (Button) findViewById(R.id.bt_optionactivity_done);

        bt_optionactivity_lines.setText(lineNumber+"");
        bt_optionactivity_score.setText(targetScore+"");

        bt_optionactivity_lines.setOnClickListener(this);
        bt_optionactivity_score.setOnClickListener(this);
        bt_optionactivity_back .setOnClickListener(this);
        bt_optionactivity_done .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.bt_optionactivity_lines:
                setLines();
                break;

            case R.id.bt_optionactivity_score:
                setScore();
                break;

            case R.id.bt_optionactivity_back:
                back();
                break;

            case R.id.bt_optionactivity_done:
                done();
                break;
        }

    }

    private void setLines() {

        final String[] lines={"4","5","6"};

        new AlertDialog.Builder(this)
                .setTitle("设置棋盘行列数")
                .setSingleChoiceItems(lines, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lineNumber=Integer.parseInt(lines[which]);
                        bt_optionactivity_lines.setText(lines[which]);
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void setScore() {

        final String[] target={"1024","2048","4096","32"};

        new AlertDialog.Builder(this)
                .setTitle("设置游戏难度")
                .setSingleChoiceItems(target, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        targetScore=Integer.parseInt(target[which]);
                        bt_optionactivity_score.setText(target[which]);
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void done() {

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("lineNumber",lineNumber);
        editor.putInt("targetScore",targetScore);
        editor.commit();

        Intent intent=new Intent();
        intent.putExtra("lineNumber",lineNumber);
        intent.putExtra("targetScore",targetScore);
        setResult(200,intent);

        finish();

    }

    private void back() {
        finish();
    }

}
