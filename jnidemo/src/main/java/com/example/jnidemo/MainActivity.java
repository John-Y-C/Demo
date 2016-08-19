package com.example.jnidemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @butterknife.BindView(R.id.tv_mainactivity_showinfo)
    TextView tv_mainactivity_showinfo;
    @butterknife.BindView(R.id.bt_mainactivity_ac)
    Button bt_mainactivity_ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        butterknife.ButterKnife.bind(this);
    }

    //调用native interface 实现数学运算
    @butterknife.OnClick(R.id.bt_mainactivity_ac)
    public void onClick() {

        MyNativeUtils myNativeUtils = new MyNativeUtils();
        int caculate = myNativeUtils.caculate(1, 2);
        tv_mainactivity_showinfo.setText(caculate+"");

    }
}

class MyNativeUtils
{
    //类加载的时候去加载底层库
    static
    {
        System.loadLibrary("jnidemo");
    }
    //让底层C语言的代码去做数学运算，把结果返回来
    public native int caculate(int x,int y);
}
