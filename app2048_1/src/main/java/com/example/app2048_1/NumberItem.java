package com.example.app2048_1;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/8/1/001.
 */
public class NumberItem extends FrameLayout {

    private TextView tv_numberItem;
    private int number;

    public NumberItem(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        //创建GameView内的小方块
        tv_numberItem = new TextView(context);
        tv_numberItem.setText("");
        tv_numberItem.setGravity(Gravity.CENTER);
        tv_numberItem.setTextSize(20);//20px

        setNumber(0);//0为方法内判断数字
        tv_numberItem.setBackgroundColor(Color.GRAY);

        //给每个textview设置一个margin
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(5,5,5,5);

        //把textview添加到framelayout
        addView(tv_numberItem,layoutParams);
    }


    public NumberItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNumber(int number) {

        this.number = number;

        switch (number){

            case 0:
                setText("");
                tv_numberItem.setBackgroundColor(Color.GRAY);
                break;

            case 2:
                setText("2");
                tv_numberItem.setBackgroundColor(0xFFFFF5EE);

                break;
            case 4:
                setText("4");

                tv_numberItem.setBackgroundColor(0xFFFFEC8B);

                break;
            case 8:
                setText("8");
                tv_numberItem.setBackgroundColor(0xFFFFE4C4);
                break;
            case 16:
                setText("16");
                tv_numberItem.setBackgroundColor(0xFFFFDAB9);
                break;
            case 32:
                tv_numberItem.setBackgroundColor(0xFFFFC125);
                setText("32");
                break;
            case 64:
                setText("64");
                tv_numberItem.setBackgroundColor(0xFFFFB6C1);
                break;
            case 128:
                setText("128");
                tv_numberItem.setBackgroundColor(0xFFFFA500);
                break;
            case 256:
                setText("256");
                tv_numberItem.setBackgroundColor(0xFFFF83FA);
                break;
            case 512:
                setText("512");
                tv_numberItem.setBackgroundColor(0xFFFF7F24);
                break;
            case 1024:
                setText("1024");
                tv_numberItem.setBackgroundColor(0xFFFF6A6A);
                break;
            case 2048:
                setText("2048");
                tv_numberItem.setBackgroundColor(0xFFFF1493);
                break;
            case 4096:
                setText("4096");
                tv_numberItem.setBackgroundColor(0xFFFF3030);
                break;


        }
    }

    public void setText(String text) {
        tv_numberItem.setText(text);
    }

    public int getNumber()
    {
        return number;
    }
}
