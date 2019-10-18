package PVP;

import GUI.CherkerBoard;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import GUI.*;
import PVE.*;


public class MorePK {
    /**
     * description:多人对战
     * 
      * @Param: null
     * @return 
     */
    static int player_color=0;

    Image image1=null;
    Image image2=null;
    private String name1;
    private String name2;

    int number=0;

    //通信socket
    Socket socket=null;

    public MorePK(String name1,String name2,Socket socket,int number){
        this.name1=name1;
        this.name2=name2;
        this.socket=socket;
        this.number=number;

        //加载图片
        ImageIcon icon=new ImageIcon("src//image/back_one.jpg");
        //Image im=new Image(icon);
        //将图片放入label中
        JLabel label=new JLabel(icon);

        //设置label的大小
        label.setBounds(0,0,(icon.getIconWidth()/4)*3,(icon.getIconHeight()/4)*3);

        JFrame frame=new JFrame();
        //不绘制标题栏和边框
        frame.setUndecorated(true);

        frame.setLayout(null);
        //获取窗口的第二层，将label放入

        frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));

        //获取frame的顶层容器,并设置为透明
        JPanel j=(JPanel)frame.getContentPane();
        j.setOpaque(false);


        //玩家1时间
        PlayerTime playerTime=new PlayerTime();
        playerTime.setBounds(100,500,200,100);
        //playerTime.start_time();
        playerTime.setOpaque(false);
        frame.add(playerTime);

        //玩家2时间
        PlayerTime playerTime1=new PlayerTime();
        playerTime1.setBounds(1130,500,200,100);
        //playerTime1.start_time();
        playerTime1.setOpaque(false);
        frame.add(playerTime1);



        //棋盘
        MyPVPpanel myPVPpanel=new MyPVPpanel(socket,number,playerTime,playerTime1);
        myPVPpanel.setBounds(400,60,675,675);
        frame.add(myPVPpanel);
        myPVPpanel.setOpaque(false);
        //玩家1的图标
        try {
            image1= ImageIO.read(new File("E:\\idea_project\\chess_javaSubject\\src\\image\\player22.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MyImagePanle myImagePanle=new MyImagePanle(128,128,image1);
        myImagePanle.setLayout(null);
        myImagePanle.setBounds(130,100,128,128);
        myImagePanle.setOpaque(false);
        frame.add(myImagePanle);


        //玩家1的名字
        JLabel player1=new JLabel(name1);
        player1.setBounds(130,300,300,50);
        player1.setFont(new java.awt.Font("华文行楷", 4, 35));
        player1.setForeground(Color.BLACK);
        frame.add(player1);


        //玩家2的图片
        try {
            image2= ImageIO.read(new File("E:\\idea_project\\chess_javaSubject\\src\\image\\player1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MyImagePanle myImagePanle1=new MyImagePanle(128,128,image2);
        myImagePanle1.setLayout(null);
        myImagePanle1.setBounds(1150,100,128,128);
        myImagePanle1.setOpaque(false);
        frame.add(myImagePanle1);



        //玩家2的名字（右边）
        JLabel player2=new JLabel(name2);
        player2.setBounds(1130,300,300,50);
        player2.setFont(new java.awt.Font("华文行楷", 4, 35));
        player2.setForeground(Color.BLACK);
        frame.add(player2);
        //选择先手
        JButton choosChess=new JButton("选边");
        choosChess.setBounds(500,750,100,50);
        choosChess.setFont(new java.awt.Font("华文行楷", 4, 32));
        choosChess.setFocusPainted(false);
        choosChess.setForeground(Color.BLACK);
        frame.add(choosChess);


        choosChess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sel=JOptionPane.showConfirmDialog(null,
                        "是否选择先手","选择先手",JOptionPane.YES_NO_OPTION);
                if(sel==JOptionPane.YES_OPTION){

                    int random=(int)(Math.random()*10+1);
                    try {
                        System.out.println("发送先手");
                        PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
                        printWriter.write("black"+"\n");
                        printWriter.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else {
                    PrintWriter printWriter= null;
                    try {
                        printWriter = new PrintWriter(socket.getOutputStream());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    printWriter.write("white"+"\n");
                    printWriter.flush();
                }

            }
        });

        //悔棋
        JButton bakeChess=new JButton("悔棋");
        bakeChess.setBounds(620,750,100,50);
        bakeChess.setFont(new java.awt.Font("华文行楷", 4, 32));
        bakeChess.setFocusPainted(false);
        bakeChess.setForeground(Color.BLACK);
        frame.add(bakeChess);

        bakeChess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
                    printWriter.write("back"+"\n");
                    printWriter.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton giveUp=new JButton("认输");
        giveUp.setBounds(740,750,100,50);
        giveUp.setFont(new java.awt.Font("华文行楷", 4, 32));
        giveUp.setFocusPainted(false);
        giveUp.setForeground(Color.BLACK);
        frame.add(giveUp);

        giveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
                    printWriter.write("giveup"+"\n");
                    printWriter.flush();
                    JOptionPane.showConfirmDialog(null,
                            "对不起，您输了","失败",JOptionPane.PLAIN_MESSAGE);
                    if(number==1){
                        playerTime.stop_time();
                    }else {
                        playerTime1.stop_time();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton restart=new JButton("重开");
        restart.setBounds(860,750,100,50);
        restart.setFont(new java.awt.Font("华文行楷", 4, 32));
        restart.setFocusPainted(false);
        restart.setForeground(Color.BLACK);
        frame.add(restart);

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new CherkerBoard();
            }
        });


        //必须设置为透明的。否则看不到图片

        Toolkit kit =Toolkit.getDefaultToolkit();
        Dimension screenSize=kit.getScreenSize();
        int screenHeight=screenSize.height;
        int screenWidth=screenSize.width;
        //置于屏幕中央   
        frame.setSize((screenWidth/4)*3 ,(screenHeight/4)*3 );
        frame.setLocation((screenWidth/8) ,(screenHeight/ 8));
        frame.setVisible(true);
        
    }

}
