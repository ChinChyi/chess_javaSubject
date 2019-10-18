package PVE;

import GUI.Chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import GUI.*;
/**
 * @program: chess_javaSubject
 * @description: 棋盘
 * @author: Mr.Qin
 * @create: 2019-10-03 21:12
 **/

public class Mypanel extends JPanel implements MouseListener {

    int player=1;
    ArrayList<Chess> chessArrayList =new ArrayList<Chess>();

    private int palyer_color;
    PlayerTime leftTime=null;
    PlayerTime rightTime=null;

    static boolean isStart=false;

    boolean Is_Play = true; // 用来表示当前游戏是否进行
    boolean Player = true; //用来表示玩家，true是黑，false表示白
    boolean NPlayer = true; //用来表示当前下棋的一方

    boolean timeLimit=true;



    int Chesses[][]=new int[15][15];// 用数组来保存棋子，0表示无子，1表示玩家，-1表示机器
    int Score[][]=new int[15][15];
    int M_x,M_y;
    int flag=0;

    public Mypanel(PlayerTime leftTime, PlayerTime rightTime){
        super();
        this.addMouseListener(this);
        this.leftTime=leftTime;
        this.rightTime=rightTime;
        for(int i=0;i<15;i++)
            for(int j=0;j<15;j++)
            {
                Chesses[i][j]=0;
                Score[i][j]=0;
            }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    timeLimit = rightTime.getTimer();
                    if (timeLimit == false) {
                        Is_Play = false;
                        rightTime.stop_time();
                        JOptionPane.showConfirmDialog(null, "您的时间已到，视为自动认输", "时间到", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        }).start();
    }
    @Override
    protected void paintComponent(Graphics g) {
//        System.out.println("11");
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        for(int i=23;i<=700;i+=45){
            g.drawLine(i,23,i,653);
        }
        for(int i=23;i<=700;i+=45){
            g.drawLine(23,i,653,i);
        }

        for(int i=0;i<chessArrayList.size();i++){
            if(chessArrayList.get(i).getStyle()==1)
                g.setColor(Color.BLACK);
            else if(chessArrayList.get(i).getStyle()==0)
                g.setColor(Color.white);
            g.fillOval(chessArrayList.get(i).getX(),chessArrayList.get(i).getY(),40,40);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(Is_Play&&Player==NPlayer&&isStart){

            long start = System.currentTimeMillis( );
            Thread t = new Thread(new Runnable() {
                public void run() {
//                System.out.println("22");
                    int x, y;
                    x = e.getX() - 23;
                    y = e.getY() - 23;
                    System.out.println("x="+x+" "+"y="+y);
                    if (x % 45 >= 22.5) {
                        int tx=x/45;
                        x = (tx + 1) * 45;
                        M_x = tx +1;

                    } else {
                        int tx=x/45;
                        x = (tx) * 45;
                        M_x = tx;
                    }
                    if (y % 45 >= 22.5) {
                        int ty=y/45;
                        y = (ty+ 1) * 45;
                        M_y = ty+1;
                    } else {
                        int ty=y/45;
                        y = (ty) * 45;
                        M_y = ty;
                    }
                    if (M_x < 15 && M_y < 15) {
                        System.out.println("M_x:" + M_x + " " + "M_y:" + M_y);
                        if (Player == true) {
                            if (Chesses[M_x][M_y] == 0) {
                                player = 1;
                                Chesses[M_x][M_y] = 1;
                                NPlayer = false;
                                Chess chess = new Chess(x, y, player);
                                chess.setM_X(M_x);
                                chess.setM_y(M_y);
                                chessArrayList.add(chess);

                                repaint();
                                if (isWin()) {
                                    if (Chesses[M_x][M_y] == 1) {

                                        int res = JOptionPane.showConfirmDialog(null, "恭喜你赢了世界上最聪明的AI", "成功", JOptionPane.YES_OPTION);
                                        rightTime.stop_time();
                                    }
                                    Is_Play = false;
                                }


                            }
                        } else {
                            if (Chesses[M_x][M_y] == 0) {
                                NPlayer = true;
                                player = 0;
                                Chesses[M_x][M_y] = -1;
                                Chess chess = new Chess(x, y, player);
                                chess.setM_X(M_x);
                                chess.setM_y(M_y);
                                chessArrayList.add(chess);

                                repaint();
                                if (isWin()) {
                                    if (Chesses[M_x][M_y] == 1) {
                                        int res = JOptionPane.showConfirmDialog(null, "恭喜你赢了世界上最聪明的AI", "成功", JOptionPane.YES_OPTION);
                                        rightTime.stop_time();
                                    }
                                    Is_Play = false;
                                }
                            }
                        }


                    }
                }
            });

            t.start();


            long end = System.currentTimeMillis( );//获取结束时间
            long diff = (end - start)/1000;//转换为秒数
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(NPlayer !=Player){
            rightTime.stop_time();


            if (Player == true) {
                searchLocation();
                System.out.println("computer:"+"M_x:"+M_x+" "+"M_y:"+M_y);
                Chesses[M_x][M_y] = -1;
                player=0;
                Chess chess=new Chess((M_x)*45,(M_y)*45,player);
                chess.setM_X(M_x);
                chess.setM_y(M_y);
                chessArrayList.add(chess);
                NPlayer = true;
                this.repaint();

                rightTime.start_time();
            }
            else {
                searchLocation();
                System.out.println("computer:"+"M_x:"+M_x+" "+"M_y:"+M_y);
                Chesses[M_x][M_y] = 1;
                player=1;
                Chess chess=new Chess((M_x)*45,(M_y)*45,player);
                chess.setM_X(M_x);
                chess.setM_y(M_y);
                chessArrayList.add(chess);
                NPlayer = false;
                this.repaint();

                rightTime.start_time();
            }
        }

        if (this.isWin()) {
            if (Chesses[M_x][M_y] == -1 && Player==true) {
                JOptionPane.showMessageDialog(this, "很遗憾，你输给了机器！");
                rightTime.stop_time();
            }
            else{
                if (Chesses[M_x][M_y] == 1 && Player==false) {
                    JOptionPane.showMessageDialog(this, "很遗憾，你输给了机器！");
                    rightTime.stop_time();
                }
            }
            this.Is_Play = false; // 表示游戏结束
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    public void searchLocation(){
        //每次都初始化下score评分数组
        for(int i = 0; i  < 15; i++){
            for(int j = 0; j < 15; j++){
                Score[i][j] = 0;
            }
        }

        //每次机器找寻落子位置，评分都重新算一遍（虽然算了很多多余的，因为上次落子时候算的大多都没变）
        //先定义一些变量
        int humanChessmanNum = 0;//五元组中的黑棋数量
        int machineChessmanNum = 0;//五元组中的白棋数量
        int tupleScoreTmp = 0;//五元组得分临时变量

        int goal_X = -1;//目标位置x坐标
        int goal_Y = -1;//目标位置y坐标
        int maxScore = -1;//最大分数

        //1.扫描横向的15个行
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
                int k = j;
                while(k < j + 5){

                    if(Chesses[i][k] == -1) machineChessmanNum++;
                    else if(Chesses[i][k] == 1)humanChessmanNum++;

                    k++;
                }
                tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                //为该五元组的每个位置添加分数
                for(k = j; k < j + 5; k++){
                    Score[i][k] += tupleScoreTmp;
                }
                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量
            }
        }

        //2.扫描纵向15行
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 11; j++){
                int k = j;
                while(k < j + 5){
                    if(Chesses[k][i] == -1) machineChessmanNum++;
                    else if(Chesses[k][i] == 1)humanChessmanNum++;

                    k++;
                }
                tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                //为该五元组的每个位置添加分数
                for(k = j; k < j + 5; k++){
                    Score[k][i] += tupleScoreTmp;
                }
                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量
            }
        }

        //3.扫描右上角到左下角上侧部分
        for(int i = 14; i >= 4; i--){
            for(int k = i, j = 0; j < 15 && k >= 0; j++, k--){
                int m = k;
                int n = j;
                while(m > k - 5 && k - 5 >= -1){
                    if(Chesses[m][n] == -1) machineChessmanNum++;
                    else if(Chesses[m][n] == 1)humanChessmanNum++;

                    m--;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if(m == k-5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m > k - 5 ; m--, n++){
                        Score[m][n] += tupleScoreTmp;
                    }
                }

                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量

            }
        }

        //4.扫描右上角到左下角下侧部分
        for(int i = 1; i < 15; i++){
            for(int k = i, j = 14; j >= 0 && k < 15; j--, k++){
                int m = k;
                int n = j;
                while(m < k + 5 && k + 5 <= 15){
                    if(Chesses[n][m] == -1) machineChessmanNum++;
                    else if(Chesses[n][m] == 1)humanChessmanNum++;

                    m++;
                    n--;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if(m == k+5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m < k + 5; m++, n--){
                        Score[n][m] += tupleScoreTmp;
                    }
                }
                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量

            }
        }

        //5.扫描左上角到右下角上侧部分
        for(int i = 0; i < 11; i++){
            for(int k = i, j = 0; j < 15 && k < 15; j++, k++){
                int m = k;
                int n = j;
                while(m < k + 5 && k + 5 <= 15){
                    if(Chesses[m][n] == -1) machineChessmanNum++;
                    else if(Chesses[m][n] == 1)humanChessmanNum++;

                    m++;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if(m == k + 5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m < k + 5; m++, n++){
                        Score[m][n] += tupleScoreTmp;
                    }
                }

                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量

            }
        }

        //6.扫描左上角到右下角下侧部分
        for(int i = 1; i < 11; i++){
            for(int k = i, j = 0; j < 15 && k < 15; j++, k++){
                int m = k;
                int n = j;
                while(m < k + 5 && k + 5 <= 15){
                    if(Chesses[n][m] == -1) machineChessmanNum++;
                    else if(Chesses[n][m] == 1)humanChessmanNum++;

                    m++;
                    n++;
                }
                //注意斜向判断的时候，可能构不成五元组（靠近四个角落），遇到这种情况要忽略掉
                if(m == k + 5){
                    tupleScoreTmp = tupleScore(humanChessmanNum, machineChessmanNum);
                    //为该五元组的每个位置添加分数
                    for(m = k, n = j; m < k + 5; m++, n++){
                        Score[n][m] += tupleScoreTmp;
                    }
                }

                //置零
                humanChessmanNum = 0;//五元组中的黑棋数量
                machineChessmanNum = 0;//五元组中的白棋数量
                tupleScoreTmp = 0;//五元组得分临时变量

            }
        }

        //从空位置中找到得分最大的位置
        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(Chesses[i][j] == 0 && Score[i][j] > maxScore){
                    goal_X = i;
                    goal_Y = j;
                    maxScore = Score[i][j];
                }
            }
        }

        if(goal_X != -1 && goal_Y != -1){
            M_x=goal_X;
            M_y=goal_Y;
            return;
        }

        //没找到坐标说明平局了，笔者不处理平局
        M_x=-1;
        M_y=-1;
        return;
    }

    //各种五元组情况评分表
    public int tupleScore(int humanChessmanNum, int machineChessmanNum){
        //1.既有人类落子，又有机器落子，判分为0
        if(humanChessmanNum > 0 && machineChessmanNum > 0){
            return 0;
        }
        //2.全部为空，没有落子，判分为7
        if(humanChessmanNum == 0 && machineChessmanNum == 0){
            return 7;
        }
        //3.机器落1子，判分为35
        if(machineChessmanNum == 1){
            return 35;
        }
        //4.机器落2子，判分为800
        if(machineChessmanNum == 2){
            return 800;
        }
        //5.机器落3子，判分为15000
        if(machineChessmanNum == 3){
            return 15000;
        }
        //6.机器落4子，判分为800000
        if(machineChessmanNum == 4){
            return 800000;
        }
        //7.人类落1子，判分为15
        if(humanChessmanNum == 1){
            return 15;
        }
        //8.人类落2子，判分为400
        if(humanChessmanNum == 2){
            return 400;
        }
        //9.人类落3子，判分为1800
        if(humanChessmanNum == 3){
            return 1800;
        }
        //10.人类落4子，判分为100000
        if(humanChessmanNum == 4){
            return 100000;
        }
        return -1;//若是其他结果肯定出错了。这行代码根本不可能执行
    }

    public boolean isWin() {
        boolean flag = false;
        int count = 1; // 用来保存共有相同颜色多少棋子相连，初始值为1
        int color = Chesses[M_x][M_y]; // color = 1 (黑子) color = -1(白子)

        // 判断横向是否有5个棋子相连，特点:纵坐标是相同，即Chesses[M_x][M_y] 中y值是相同
        count = this.checkCount(1, 0, color);
        if (count >= 5) {
            flag = true;
        } else {
            // 判断纵向
            count = this.checkCount(0, 1, color);
            if (count >= 5) {
                flag = true;
            } else {
                // 判断右上,左下
                count = this.checkCount(1, -1, color);
                if (count >= 5) {
                    flag = true;
                } else {
                    // 判断右下,左上
                    count = this.checkCount(1, 1, color);
                    if (count >= 5) {
                        flag = true;
                    }
                }
            }
        }

        return flag;
    }

    /**
     * 检查棋盘中的五子棋是否连成五子
     *
     * @param xChange
     * @param yChenge
     * @param color
     * @return
     */
    public int checkCount(int xChange, int yChenge, int color) {
        int count = 1;
        int tempX = xChange;
        int tempy = yChenge; // 保存初始值

        // 全局变量x,y最初为鼠标点击的坐标，
        // 经下棋方法已经将x,y的范围变成0-15(遍历整个棋盘,寻找相同颜色的棋子)
        while (M_x + xChange >= 0 && M_x + xChange < 15 && M_y + yChenge >= 0 && M_y + yChenge < 15
                && color == Chesses[M_x + xChange][M_y + yChenge]) {

            count++;
            if (xChange != 0)
                xChange++;
            if (yChenge != 0) {
                if (yChenge != 0) {
                    if (yChenge > 0) {
                        yChenge++;
                    } else {
                        yChenge--;
                    }
                }
            }

        }

        xChange = tempX;
        yChenge = tempy; // 恢复初始值

        while (M_x - xChange >= 0 && M_x - xChange < 15 && M_y - yChenge >= 0 && M_y - yChenge < 15
                && color == Chesses[M_x - xChange][M_y - yChenge]) {
            count++;
            if (xChange != 0) {
                xChange++;
            }
            if (yChenge != 0) {
                if (yChenge > 0) {
                    yChenge++;
                } else {
                    yChenge--;
                }
            }
        }

        return count;
    }

    //选边
    public void setPalyer_color(int palyer_color) {
        this.palyer_color = palyer_color;
        if(palyer_color==0){
            try {
                Robot robot=new Robot();
                robot.mousePress(KeyEvent.BUTTON1_MASK);
                robot.mouseRelease(KeyEvent.BUTTON1_MASK);
            } catch (AWTException e) {
                e.printStackTrace();
            }
            Player=false;
            NPlayer=true;
            isStart=true;
        }else {

            rightTime.start_time();
            isStart=true;
            Player=true;//用来表示玩家，true是黑子，false表示白子
            Player=NPlayer;
        }
    }

    //悔棋
    public  void takeBack(){

        Chess chess1=chessArrayList.get(chessArrayList.size()-1);
        Chesses[chess1.getM_X()][chess1.getM_y()]=0;
        chessArrayList.remove(chessArrayList.size()-1);
        Chess chess2=chessArrayList.get(chessArrayList.size()-1);
        Chesses[chess2.getM_X()][chess2.getM_y()]=0;
        chessArrayList.remove(chessArrayList.size()-1);
        repaint();
    }

    public void giveUp(){
        Is_Play=false;
        JOptionPane.showConfirmDialog(null,"您输给了机器","失败",JOptionPane.PLAIN_MESSAGE);
    }


}


