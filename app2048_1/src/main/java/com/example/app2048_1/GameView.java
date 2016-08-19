package com.example.app2048_1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1/001.
 */
public class GameView extends GridLayout{

    private String TAG="============";
    private int current_score=0;
    //只要有空格可以滑动就会产生新的textview，不考虑方向问题，false为不可滑动
    boolean can_slide=false;
    private MainActivity activity;
    private Context context;
    private int target_score=1024;
    int[][] history_matrix;
    private boolean can_revert=false;
    private SharedPreferences sharedPreferences;

    //一般只需继承前两个构造方法
    public GameView(Context context) {
        super(context);
        init(context);
    }

    private int column_number = 4;
    private int row_number = 4;

    //记录当前棋盘上，空白位置的一个数组
    //放一个（x,y）表示空白棋盘的行列
    List<Point> blankItemList;//未占用位置集合

    //创建一个二维数组，以便精确到具体的位置
    NumberItem[][] itemMatrix;//已占用位置数组

    public void set_rowNumber(int row)
    {
        this.row_number=row;
    }

    public void set_columnNumber(int column)
    {
        this.column_number=column;
    }

    public void setTarget_score(int target)
    {
        this.target_score=target;
    }

    private void init(Context ctx) {

        this.context=ctx;
        activity=(MainActivity) ctx;

        sharedPreferences = ctx.getSharedPreferences("record",ctx.MODE_PRIVATE);
        row_number=sharedPreferences.getInt("lineNumber",4);
        column_number=row_number;

        initView(ctx);

    }

    private void initView(Context ctx) {

        removeAllViews();

        //设置行列数
        setColumnCount(column_number);
        setRowCount(row_number);
        setTarget_score(target_score);
        Log.i(TAG, "target_score");
        Log.i(TAG, "column_number"+column_number);
        Log.i(TAG, "row_number"+row_number);

        itemMatrix=new NumberItem[row_number][column_number];
        history_matrix=new int[row_number][column_number];
        blankItemList=new ArrayList<Point>();

        updateCurrentScore(0);
        //获取屏幕的宽度，动态部署每个textview的大小，应用于本上下文，要用本上下文的context点出来
        WindowManager windowManager= (WindowManager) ctx.getSystemService(ctx.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();//metrices度量
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int window_width=metrics.widthPixels;//Pixels像素

        for(int i=0;i<row_number;i++)//行数
        {
            for(int j=0;j<column_number;j++)//列数
            {
                NumberItem numberItem=new NumberItem(ctx);
                itemMatrix[i][j]=numberItem;//将一个新的numberItem（即新的textview）放置在i行j列，直至放置满

                //初始化未占用集合blankItemList
                Point point=new Point();
                point.x=i;
                point.y=j;
                blankItemList.add(point);

                //向GameView中添加textview，宽高均为屏款的四分之一
                addView(numberItem,window_width/row_number,window_width/row_number);
            }
        }

        //在未占用的随机位置显示一个不为0的数字
        //当随机位置相同时怎么处理？？？
        addRandomLocation();
        addRandomLocation();
    }

    private void addRandomLocation() {

        updateBlankNumberList();
        //在未占用位置随机放置textView位置，需随机产生i和j
        int blankLength=blankItemList.size();//16  获取blankItemList.size()的用处？用于后面判断所剩空格数？
        Log.i(TAG,blankLength+"");
        int random_location= (int) Math.floor(Math.random()*blankLength);//floor向下去整  原为*16
                            //？？？(int) Math.random()*blankLength运算顺序，先运算前面的强转，再*blankLength，所有获取到的都是0
                            //若只是运算顺序的问题，则无需加floor
        Log.i(TAG,random_location+"");
        Point point=blankItemList.get(random_location);
        NumberItem numberItem=itemMatrix[point.x][point.y];//在未占用位置放置一个textview，并赋初值2+
        numberItem.setNumber(2);

    }

    //每次去找一个随机的，产生数字的位置之前，需要去更新一下产和那个随机数的范围
    private void updateBlankNumberList() {

        blankItemList.clear();
        for(int i=0;i<row_number;i++)
        {
            for(int j=0;j<column_number;j++)
            {
                int number=itemMatrix[i][j].getNumber();
                if(number==0)
                {
                    //将所有的空位添加到blankItemList中，以便在空位随机放置新的textview
                    blankItemList.add(new Point(i,j));
                }
            }
        }
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //使用onTouchListener以便获取滑动始末位置，从而判断滑动方向
    int start_x;
    int start_y;
    int end_x;
    int end_y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                start_x= (int) event.getX();
                start_y= (int) event.getY();

                Log.i(TAG, "ACTION_DOWN");
                break;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ACTION_MOVE");
                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP");
                end_x= (int) event.getX();
                end_y= (int) event.getY();

                judgeDirection();
                updateCurrentScore(current_score);
                handleNext(isOver());

                break;

        }
        return true;

    }

    private void updateCurrentScore(int current_score) {
        this.current_score=current_score;
        activity.setScore(current_score);
    }

    private int isOver() {

        int over=1;
        for(int i=0;i<row_number;i++)
        {
            for(int j=0;j<column_number;j++)
            {
                if(itemMatrix[i][j].getNumber()==target_score)
                {
                    return 0;
                }
            }
        }

        //水平方向可以合并
        int pre_number=-1;
        for(int i=0;i<row_number;i++)
        {
            for(int j=0;j<column_number;j++)
            {
                int number=itemMatrix[i][j].getNumber();
                if(pre_number!=-1)
                {
                    if(pre_number==number)
                    {
                        return 1;//存在可以合并的两个item
                    }
                    pre_number=number;
                }
            }
        }

        //竖直方向可以合并
        pre_number=-1;
        for(int i=0;i<column_number;i++)
        {
            for(int j=0;j<row_number;j++)
            {
                int number=itemMatrix[j][i].getNumber();
                if(pre_number!=-1)
                {
                    if(pre_number==number)
                    {
                        return 1;
                    }
                    pre_number=number;
                }
            }
        }

        updateBlankNumberList();
        if(blankItemList.size()==0)
        {
            return -1;
        }

        return over;
    }

    private void handleNext(int over)
    {   //over=-1,fail  over=0,win   over==else,continue
        if(over==0)
        {
            //提示获胜
            activity.updateRecordScore(current_score);

            new AlertDialog.Builder(context)
                    .setTitle("恭喜你")
                    .setMessage("您已经完成，是否再来一局？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();//关闭当前Activity
                        }
                    })
                    .show();
        }
        else if (over==-1)
        {
            new AlertDialog.Builder(context)
                    .setTitle("抱歉")
                    .setMessage("挑战失败，是否重来？")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restart();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.finish();
                        }
                    }).show();
        }
        else
        {
//            if(can_slide)
//            {
                addRandomLocation();
//                can_slide=false;
//            }
        }
    }

    void restart() {
        initView(activity);
    }

    private void judgeDirection()
    {
        int absx = Math.abs(end_x - start_x);//abs返回绝对值
        int absy = Math.abs(end_y - start_y);

        saveToHistory();

//        if(start_y==end_y||start_x==end_x)
//        {
//            return;
//        }

        if(absx>absy)//水平方向移动
        {
            if(end_x-start_x>0)
            {
                slideRight();
            }
            else
            {
                slideLeft();
            }
        }
        else//竖直方向移动
        {
            if(end_y-start_y>0)
            {
                slideDown();
            }
            else
            {
                slideUp();
            }
        }
    }

    public void saveToHistory()
    {
        for(int i=0;i<row_number;i++)
        {
            for(int j=0;j<column_number;j++)
            {
                history_matrix[i][j]=itemMatrix[i][j].getNumber();
            }
        }
        can_revert=true;
    }

    public void revert()
    {
        if(can_revert)
        {
            for(int i=0;i<row_number;i++)
            {
                for(int j=0;j<column_number;j++)
                {
                    itemMatrix[i][j].setNumber(history_matrix[i][j]);
                }
            }
        }
    }


    private void slideUp() {
        Log.i(TAG, "slideUp");

        ArrayList<Integer> cacuList=new ArrayList<Integer>(row_number);

        //按列扫描
        for(int i=0;i<column_number;i++)
        {
            int pre_number=-1;//当前item的前一个item里的数字
            for(int j=0;j<row_number;j++)
            {
                int currentItemNumber=itemMatrix[j][i].getNumber();
                if(currentItemNumber==0)//当前取得数字为零，忽略
                {
                    continue;
                }
                else
                {
                    //当前拿到的数不为0
                    if(currentItemNumber==pre_number)//当前数字与前一个一样时，合并
                    {
                        cacuList.add(currentItemNumber*2);
                        current_score+=pre_number*2;
                        pre_number=-1;
                    }
                    else// 当前数字与前一个不一样时
                    {

                        if(pre_number==-1)//若前一个保存的是-1，则需要进一步看下一个数字
                        {
                            pre_number=currentItemNumber;
                        }
                        else//前一个不是-1,（当前这个与前一个不相等）
                        {
                            cacuList.add(pre_number);
                            pre_number=currentItemNumber;
                        }
                    }
                }
            }

            //如果某一行只有最后一个item上有数字，则会漏掉最后一个item
            if(pre_number!=-1)
            {
                cacuList.add(pre_number);
            }

            //计算的结果可能小于当前行的长度
            //将当前行获取到并放在cacuList中的数字赋值给itemMatrix矩阵
            for(int k=0;k<cacuList.size();k++)
            {
                int new_number=cacuList.get(k);
                itemMatrix[k][i].setNumber(new_number);

            }

            //剩下的部分，当前行直接填充0
            for(int l=cacuList.size();l<column_number;l++)
            {
                itemMatrix[l][i].setNumber(0);
            }

            //清空caculist
            cacuList.clear();
        }
    }

    private void slideDown() {

        Log.i(TAG, "slideDown");

        ArrayList<Integer> cacuList=new ArrayList<Integer>(row_number);
        for(int i=0;i<column_number;i++)
        {
            int pre_number=-1;
            for(int j=row_number-1;j>=0;j--)
            {
                int currentItemNumber=itemMatrix[j][i].getNumber();
                if(currentItemNumber==0)
                {
                    continue;
                }
                else
                {
                    if(currentItemNumber==pre_number)
                    {
                        cacuList.add(pre_number*2);
                        current_score+=pre_number*2;
                        pre_number=-1;
                    }
                    else
                    {
                        if(pre_number==-1)
                        {
                            pre_number=currentItemNumber;
                        }
                        else
                        {
                            cacuList.add(pre_number);
                            pre_number=currentItemNumber;
                        }
                    }
                }
            }

            if(pre_number!=-1)
            {
                cacuList.add(pre_number);
            }

            for(int l=0;l<cacuList.size();l++)
            {
                int new_number=cacuList.get(l);
                itemMatrix[column_number-1-l][i].setNumber(new_number);
            }

            //剩余部分为0
            for(int p=column_number-cacuList.size()-1;p>=0;p--)
            {
                itemMatrix[p][i].setNumber(0);
            }

            cacuList.clear();
        }
    }

    private void slideLeft() {  //左划事件
        Log.i(TAG, "slideLeft");

        ArrayList<Integer> cacuList=new ArrayList<Integer>(column_number);//放置新数字的集合
        for(int i=0;i<row_number;i++)
        {
            int pre_number=-1;//当前item的前一个item里的数字
            for(int j=0;j<column_number;j++)
            {
                int currentItemNumber=itemMatrix[i][j].getNumber();
                if(currentItemNumber==0)//当前取得数字为零，忽略
                {
                    continue;
                }
                else
                {
                    //当前拿到的数不为0
                    if(currentItemNumber==pre_number)//当前数字与前一个一样时，合并
                    {
                        cacuList.add(currentItemNumber*2);
                        current_score+=pre_number*2;
                        pre_number=-1;
                    }
                    else// 当前数字与前一个不一样时
                    {
                        if(pre_number==-1)//若前一个保存的是-1，则需要进一步看下一个数字
                        {
                            pre_number=currentItemNumber;
                        }
                        else//前一个不是-1,（当前这个与前一个不相等）
                        {
                            cacuList.add(pre_number);
                            pre_number=currentItemNumber;
//                            if(pre_number!=0)
//                            {
//                                can_slide=false;
//                                return;
//                            }
                            //can_slide=false;

                        }
                    }
                }
            }

            //如果某一行只有最后一个item上有数字，则会漏掉最后一个item
            if(pre_number!=-1)
            {
                cacuList.add(pre_number);
            }

            //计算的结果可能小于当前行的长度
            //将当前行获取到并放在cacuList中的数字赋值给itemMatrix矩阵
            for(int k=0;k<cacuList.size();k++)
            {
                int new_number=cacuList.get(k);
                itemMatrix[i][k].setNumber(new_number);

            }

            //剩下的部分，当前行直接填充0
            for(int l=cacuList.size();l<column_number;l++)
            {
                itemMatrix[i][l].setNumber(0);
            }

            //清空caculist
            cacuList.clear();

        }
    }

    private void slideRight()
    {
        Log.i(TAG, "slideRight");

        ArrayList<Integer> cacuList=new ArrayList<Integer>(column_number);
        for(int i=0;i<row_number;i++)
        {
            int pre_number=-1;
            for(int j=column_number-1;j>=0;j--)
            {
                int currentItemNumber=itemMatrix[i][j].getNumber();
                if(currentItemNumber==0)
                {
                    continue;
                }
                else
                {
                    if(currentItemNumber==pre_number)
                    {
                        cacuList.add(pre_number*2);
                        current_score+=pre_number*2;
                        pre_number=-1;
                    }
                    else
                    {
                        /*if(pre_number!=0&&pre_number!=-1)
                        {
                            can_slide=false;
                            return;
                        }
                        else */
                        if(pre_number==-1)
                        {
                            pre_number=currentItemNumber;
                        }
                        else
                        {
                            cacuList.add(pre_number);
                            pre_number=currentItemNumber;
                        }
                    }
                }
            }

            if(pre_number!=-1)
            {
                cacuList.add(pre_number);
            }

            for(int l=0;l<cacuList.size();l++)
            {
                int new_number=cacuList.get(l);
                itemMatrix[i][column_number-1-l].setNumber(new_number);
            }

            //剩余部分为0
            for(int p=column_number-cacuList.size()-1;p>=0;p--)
            {
                itemMatrix[i][p].setNumber(0);
            }

            cacuList.clear();
        }
    }


}
