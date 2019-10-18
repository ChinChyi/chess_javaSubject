package PVP;

import GUI.Chess;
import GUI.PlayerTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

/**
 * @program: chess_javaSubject
 * @description: 多人对战棋盘
 * @author: Mr.Qin
 * @create: 2019-10-14 20:46
 **/

public class MyPVPpanel extends JPanel implements MouseListener {

    int Chesses[][]=new int[15][15];
    ArrayList<Chess> chessArrayList =new ArrayList<Chess>();
    int M_x,M_y;
    boolean postMessage = false;
    public String whatChess=null;

    int number=0;//编号

    String getStr=null;
    //通信socket
    Socket socket=null;
    boolean isWin=false;
    PlayerTime time1=null;
    PlayerTime time2=null;


//0是白子，1是黑子
    MyPVPpanel(Socket socket,int number,PlayerTime time1,PlayerTime time2){
        super();
        this.socket=socket;
        this.number=number;
        this.time1=time1;
        this.time2=time2;
        this.addMouseListener(this);
        for(int i=0;i<15;i++)
            for(int j=0;j<15;j++)
            {
                Chesses[i][j]=0;
            }
        //读进程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));
                    while (true){
                        getStr=bufferedReader.readLine();
                        if(getStr.equals("back")){
                            int res=JOptionPane.showConfirmDialog(null,"是否允许对方悔棋"
                            ,"悔棋",JOptionPane.YES_NO_OPTION);
                            if(res==JOptionPane.YES_OPTION){
                                PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
                                printWriter.write("yesback"+"\n");
                                printWriter.flush();
                                Chess chess1=chessArrayList.get(chessArrayList.size()-1);
                                Chesses[chess1.getM_X()][chess1.getM_y()]=0;
                                chessArrayList.remove(chessArrayList.size()-1);
                                Chess chess2=chessArrayList.get(chessArrayList.size()-1);
                                Chesses[chess2.getM_X()][chess2.getM_y()]=0;
                                chessArrayList.remove(chessArrayList.size()-1);
                                repaint();

                            }else {
                                PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
                                printWriter.write("noback"+"\n");
                                printWriter.flush();
                            }
                        }else if(getStr.equals("giveup")){

                            JOptionPane.showConfirmDialog(null,
                                    "恭喜你赢了","成功",JOptionPane.PLAIN_MESSAGE);
                            time2.stop_time();
                            time1.stop_time();
                            isWin=false;
                        }else if (getStr.equals("black")){
                            whatChess="black";
                            JOptionPane.showConfirmDialog(null,
                                    "执黑先行","选边结果",JOptionPane.PLAIN_MESSAGE);
                            isWin=true;
                            if(number==1){
                                time1.start_time();
                                time2.stop_time();
                            }else {
                                time2.start_time();
                                time1.stop_time();
                            }
                        } else if (getStr.equals("white")){
                            whatChess="white";
                            JOptionPane.showConfirmDialog(null,
                                    "执白后行","选边结果",JOptionPane.PLAIN_MESSAGE);

                            if(number==1){

                                time2.stop_time();
                            }else {

                                time1.stop_time();
                            }
                        }else if(getStr.equals("false")){
                            JOptionPane.showConfirmDialog(null,
                                    "对不起，你输了","失败",JOptionPane.PLAIN_MESSAGE);
                            time2.stop_time();
                            time1.stop_time();
                            isWin=false;
                        }else if(getStr.equals("yesback")){
                            Chess chess1=chessArrayList.get(chessArrayList.size()-1);
                            Chesses[chess1.getM_X()][chess1.getM_y()]=0;
                            chessArrayList.remove(chessArrayList.size()-1);
                            Chess chess2=chessArrayList.get(chessArrayList.size()-1);
                            Chesses[chess2.getM_X()][chess2.getM_y()]=0;
                            chessArrayList.remove(chessArrayList.size()-1);
                            repaint();
                            if(number==1){
                                time1.start_time();
                                time2.stop_time();
                            }else {
                                time2.start_time();
                                time1.stop_time();
                            }
                        }else if(getStr.equals("noback")){
                            JOptionPane.showConfirmDialog(null,
                                    "对方不允许你悔棋","悔棋失败",JOptionPane.PLAIN_MESSAGE);
                            if(number==1){
                                time1.start_time();
                                time2.stop_time();
                            }else {
                                time2.start_time();
                                time1.stop_time();
                            }
                        }
                        else {

                            String arr[]=getStr.split(" {1,}");
                            int x= parseInt(arr[0]);
                            int y= parseInt(arr[1]);
                            M_x=x;
                            M_y=y;
                            System.out.println("获得的坐标是："+x+" "+y);
                            int style= 0;
                            if(whatChess.equals("black")){
                                style=0;

                                Chesses[x][y]=-1;
                            }else if(whatChess.equals("white")){
                                style=1;
                                Chesses[x][y]=1;
                            }
                            Chess chess=new Chess(x*45,y*45,style);
                            chessArrayList.add(chess);
                            repaint();
                            isWin=true;
                            if(number==1){
                                time1.start_time();
                                time2.stop_time();
                            }else {
                                time2.start_time();
                                time1.stop_time();
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


            //写进程


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        for(int i=23;i<=700;i+=45){
            g.drawLine(i,23,i,653);
        }
        for(int i=23;i<=700;i+=45){
            g.drawLine(23,i,653,i);
        }
        System.out.println("repaint");
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

        if(isWin) {
            int x, y;
            x = e.getX() - 23;
            y = e.getY() - 23;
            System.out.println("x=" + x + " " + "y=" + y);
            if (x % 45 >= 22.5) {
                int tx = x / 45;
                x = (tx + 1) * 45;
                M_x = tx + 1;

            } else {
                int tx = x / 45;
                x = (tx) * 45;
                M_x = tx;
            }
            if (y % 45 >= 22.5) {
                int ty = y / 45;
                y = (ty + 1) * 45;
                M_y = ty + 1;
            } else {
                int ty = y / 45;
                y = (ty) * 45;
                M_y = ty;
            }
            int style = 0;
            if (whatChess.equals("white")) {
                style = 0;
            } else if (whatChess.equals("black")) {
                style = 1;
            }
            Chess chess = new Chess(x, y, style);
            chessArrayList.add(chess);
            if(style==0){
                Chesses[M_x][M_y]=-1;
            }else if(style==1){
                Chesses[M_x][M_y]=1;
            }
//        try {
//            SendChess sendChess= new SendChess(socket,M_x,M_y);
//            GetChess getChess=new GetChess(socket);
//            sendChess.start();
//            getChess.start();
//            sendChess.setPostMessage(true);
//            new GetChess(socket).start();
//        } catch (IOException ee) {
//            ee.printStackTrace();
//        }
            repaint();

            try {
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.write(M_x + " " + M_y + "\n");
                printWriter.flush();
                System.out.println(M_x + " " + M_y);
                isWin=false;
                if(number==1){
                    time1.stop_time();
                    time2.start_time();
                }else {
                    time2.stop_time();
                    time1.start_time();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(this.isWiner()){
                JOptionPane.showConfirmDialog(null,"恭喜你赢了！","成功",
                        JOptionPane.PLAIN_MESSAGE);
                isWin=false;
                PrintWriter printWriter = null;
                time2.stop_time();
                time1.stop_time();

                try {
                    printWriter = new PrintWriter(socket.getOutputStream());
                    printWriter.write("false"+ "\n");
                    printWriter.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    //判赢
    public boolean isWiner() {
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

}






class SendChess extends Thread{
    private PrintWriter printWriter;
    private Socket socket;
    private int M_x;
    private int M_y;
    private boolean postMessage;
    public SendChess(Socket socket,int M_x,int M_y) throws IOException {
        this.socket=socket;
        this.printWriter=new PrintWriter(socket.getOutputStream());
        this.M_x=M_x;
        this.M_y=M_y;
    }

    @Override
    public void run() {
        while (true) {
            while (postMessage == true) {
                System.out.println("发送坐标：" + M_x + " " + M_y);
                printWriter.write(M_x + " " + M_y + "\n");
                printWriter.flush();
                postMessage = false;
            }
        }
    }

    public void setPostMessage(boolean postMessage) {
        this.postMessage = postMessage;
    }
}
class GetChess extends Thread{
    private BufferedReader bufferedReader;
    private String string;
    private Socket socket;





    public GetChess(Socket socket) throws IOException {
        this.socket=socket;
        this.bufferedReader=new BufferedReader(new InputStreamReader
                (socket.getInputStream()));
    }
    public String getString()
    {
        return string;
    }

    @Override
    public void run() {
        while (true) {
            try {
                string = bufferedReader.readLine();
                System.out.println("收到的消息：" + string);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}