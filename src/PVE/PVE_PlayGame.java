package PVE; /**
 * @program: chess_javaSubject
 * @description: 人机算法参考
 * @author: Mr.Qin
 * @create: 2019-10-12 15:38
 **/


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class PVE_PlayGame extends JFrame implements MouseListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int M_x, M_y; // 定义鼠标的坐标
    int[][] Chesses = new int[15][15]; // 用数组来保存棋子，0表示无子，1表示玩家，-1表示机器
    int[][] Score = new int[15][15];

    boolean Player = true; //用来表示玩家，true是黑子，false表示白子
    boolean NPlayer = true; //用来表示当前下棋的一方
    boolean Is_Play = true; // 用来表示当前游戏是否进行
    //// 记录棋子顺序，方便实现悔棋
    int[] Chess_X = new int[255];
    int[] Chess_Y = new int[255];
    int CountX, CountY;
    String Now_Player = "玩家先下";

    public PVE_PlayGame() {
        // TODO 自动生成的构造函数存根
        this.setTitle("人机对弈五子棋");
        this.setSize(1250, 800);
        this.setLocationRelativeTo(null); // 界面
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false); // 设置窗口不可改变，固定窗口大小
        this.setVisible(true);

        this.repaint(); // java里repaint()是重绘component的方法；
        this.addMouseListener(this);
    }

    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        BufferedImage buf = new BufferedImage(1250, 800, BufferedImage.TYPE_INT_RGB); // 将界面背景变成黑色
        Graphics g1 = buf.createGraphics(); // 创建画笔
        g1.setColor(new Color(0, 170, 160));
        g1.fill3DRect(0, 0, 800, 800, true);

        for (int i = 0; i < 15; i++) {
            g1.setColor(Color.WHITE);
            g1.drawLine(50, 50 + i * 50, 750, 50 + i * 50); /// 横线
            g1.drawLine(50 + i * 50, 50, 50 + i * 50, 750); /// 竖线
        }
//        g1.drawRoundRect(10,150,40,40,20,20);//画圆g.setColor(Color.red);
//        g1.fillRoundRect(10,150,40,40,20,20);//画圆块；
        // 棋盘点
        g1.drawOval(190, 190, 20, 20);
        g1.fillOval(190, 190, 20, 20);

        g1.drawOval(190, 590, 20, 20);
        g1.fillOval(190, 590, 20, 20);

        g1.drawOval(390, 390, 20, 20);
        g1.fillOval(390, 390, 20, 20);

        g1.drawOval(590, 190, 20, 20);
        g1.fillOval(590, 190, 20, 20);

        g1.drawOval(590, 590, 20, 20);
        g1.fillOval(590, 590, 20, 20);

        g1.setFont(new Font("黑体", Font.BOLD, 25));
        g1.drawString("游戏信息:" + Now_Player, 925, 600);


        g1.drawRect(974, 78, 105, 35);
        g1.drawString("重新开始", 975, 105); //重新开始游戏

        g1.drawRect(974, 138, 105, 35);
        g1.drawString("悔棋", 1000, 165); //悔棋

        g1.drawRect(974, 198, 105, 35);
        g1.drawString("认输", 1000, 225); //认输

        g1.drawRect(974, 258, 105, 35);
        g1.drawString("退出游戏", 975, 285); //退出游戏

        g1.drawRect(974, 318, 105, 35);
        g1.drawString("选择棋子", 975, 345); //选择棋子

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                // 画实心黑子
                if (Chesses[i][j] == 1) {
                    int tempX = i * 50 + 30;
                    int tempY = j * 50 + 30;
                    g1.setColor(Color.BLACK);
                    g1.fillOval(tempX, tempY, 40, 40);
                    g1.setColor(Color.BLACK);
                    g1.drawOval(tempX, tempY, 40, 40);
                }

                // 画实心白子
                if (Chesses[i][j] == -1) {
                    int tempX = i * 50 + 30;
                    int tempY = j * 50 + 30;
                    g1.setColor(Color.WHITE);
                    g1.fillOval(tempX, tempY, 40, 40);
                    g1.setColor(Color.WHITE);
                    g1.drawOval(tempX, tempY, 40, 40);
                }
            }
        }
        g.drawImage(buf, 0, 0, this);
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

    //将棋盘清空
    public void Restart_Game(){
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Chesses[i][j] = 0;
            }
        }
        NPlayer = true;
        Now_Player = "玩家先下";
        CountX=0;
        CountY=0;
        Is_Play = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO 自动生成的方法存根
        if (Is_Play) {
            if (NPlayer==Player) {
                M_x = e.getX();
                M_y = e.getY(); // 用来获取鼠标坐标
                if (M_x >= 30 && M_x < 750 && M_y >= 30 && M_y < 750) {
                    // 让鼠标在棋盘范围内
                    if ((M_x - 50) % 50 > 25) {
                        M_x = (M_x - 50) / 50 + 1;
                    } else {
                        M_x = (M_x - 50) / 50;
                    }
                    if ((M_y - 50) % 50 > 25) {
                        M_y = (M_y - 50) / 50 + 1;
                    } else {
                        M_y = (M_y - 50) / 50;
                    }
                    if (Player == true) {
                        // 落子(玩家先手)
                        if (Chesses[M_x][M_y] == 0) {
                            Chess_X[CountX++] = M_x;
                            Chess_Y[CountY++] = M_y;
                            Chesses[M_x][M_y] = 1;
                            NPlayer = false;
                            Now_Player = "机器下棋";
                            this.repaint();
                            if (this.isWin()) {
                                if (Chesses[M_x][M_y] == 1) {
                                    JOptionPane.showMessageDialog(this, "恭喜你打败了机器！");
                                }
                                this.Is_Play = false; // 表示游戏结束
                            }
                        }
                    }
                    else {
                        if (Chesses[M_x][M_y] == 0) {
                            Chess_X[CountX++] = M_x;
                            Chess_Y[CountY++] = M_y;
                            Chesses[M_x][M_y] = -1;
                            NPlayer = true;
                            Now_Player = "机器下棋";
                            this.repaint();
                            if (this.isWin()) {
                                if (Chesses[M_x][M_y] == -1) {
                                    JOptionPane.showMessageDialog(this, "恭喜你打败了机器！");
                                }
                                this.Is_Play = false; // 表示游戏结束
                            }
                        }
                    }
                }
            }
        }



        ////选择棋子
        if(e.getX()>=974 && e.getX()<=(974+105) && e.getY()>=318 && e.getY()<=(318+35)) {
            int preslt = JOptionPane.showConfirmDialog(this, "是否选择先手？（先手为黑棋，默认也是黑棋）", "选择棋子", JOptionPane.YES_NO_OPTION);
            if(preslt!=0){
                Player = false; //用来表示玩家，true是黑子，false表示白子
                Restart_Game();
                M_x=7;
                M_y=7;
                Chess_X[CountX++] = M_x;
                Chess_Y[CountY++] = M_y;
                Chesses[M_x][M_y] = 1;
                NPlayer = Player;
                Now_Player = "玩家下棋";
                this.repaint();
            }
        }


        ////重新开始游戏
        if(e.getX() >=974 && e.getY() <= (974+105)  && e.getY() >= 78 && e.getY() <= (78+35)){
            int result = JOptionPane.showConfirmDialog(this, "是否重新开始游戏？");
            if(result == 0){
                Player=true;
                Restart_Game();
            }
            this.repaint(); // 重绘棋盘
        }

        // 悔棋
        if (e.getX() >= 974 && e.getX() <= (974 + 105) && e.getY() >= 138 && e.getY() <= (138 + 35)) {
            if (Is_Play) {
                if (CountX != 0) {
                    int result = JOptionPane.showConfirmDialog(null, "是否选择悔棋？", "请求强行续命", JOptionPane.YES_NO_OPTION);
                    // result = 0为悔棋,即选择了是
                    if (result == 0) {
                        if (NPlayer == Player && Player==true) {
                            Chesses[Chess_X[--CountX]][Chess_Y[--CountY]] = 0;
                            Chesses[Chess_X[--CountX]][Chess_Y[--CountY]] = 0;
                            Now_Player = "玩家下棋";
                        } else {
                            if (NPlayer == Player && (CountX - 1) != 0) {
                                Chesses[Chess_X[--CountX]][Chess_Y[--CountY]] = 0;
                                Chesses[Chess_X[--CountX]][Chess_Y[--CountY]] = 0;
                                Now_Player = "玩家下棋";
                            } else {
                                JOptionPane.showMessageDialog(null, "你都没棋子了，你还悔棋！！！", "警告警告！！！",
                                        JOptionPane.WARNING_MESSAGE);
                            }
                        }

                        this.repaint(); // 重绘棋盘
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "你都没棋子了，你还悔棋！！！", "警告警告！！！", JOptionPane.WARNING_MESSAGE);
                }
            }
        }


        //认输
        if(e.getX() >= 974 && e.getX() <= (974 + 105) && e.getY() >= 198 && e.getY() <= (198 + 35)){
            int result = JOptionPane.showConfirmDialog(null, "是否认输?", "下不过我吧，哈哈哈哈",JOptionPane.YES_NO_OPTION);
            if(result==0){
                JOptionPane.showMessageDialog(null,"游戏结束,"+"你输给了机器！","还有谁！",JOptionPane.PLAIN_MESSAGE);
                Restart_Game();
                int result1 = JOptionPane.showConfirmDialog(this, "是否立即重新开始游戏？");
                if(result1 == 0){
                    Player=true;
                    Restart_Game();
                    this.repaint(); // 重绘棋盘
                }

            }
        }

        //退出游戏
        if(e.getX()>=974 && e.getX()<=(974+105) && e.getY()>=258 && e.getY()<=(258+35)){
            int result = JOptionPane.showConfirmDialog(null,"是否退出游戏？", "不玩了吗？要去学习了吗？",JOptionPane.YES_NO_OPTION);
            if(result == 0){
                System.exit(0);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO 自动生成的方法存根
        if (NPlayer != Player) {
            if (Player == true) {

                searchLocation();
                Chess_X[CountX++] = M_x;
                Chess_Y[CountY++] = M_y;
                Chesses[M_x][M_y] = -1;
                NPlayer = true;
                Now_Player = "玩家下棋";
                this.repaint();
            }
            else {
                searchLocation();
                Chess_X[CountX++] = M_x;
                Chess_Y[CountY++] = M_y;
                Chesses[M_x][M_y] = 1;
                NPlayer = false;
                Now_Player = "玩家下棋";
                this.repaint();
            }
        }
        if (this.isWin()) {
            if (Chesses[M_x][M_y] == -1 && Player==true) {
                JOptionPane.showMessageDialog(this, "很遗憾，你输给了机器！");
            }
            else{
                if (Chesses[M_x][M_y] == 1 && Player==false) {
                    JOptionPane.showMessageDialog(this, "很遗憾，你输给了机器！");
                }
            }
            this.Is_Play = false; // 表示游戏结束
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO 自动生成的方法存根

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO 自动生成的方法存根

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

    public static void main(String[] args) {
        new PVE_PlayGame();
    }
}
