package PVE;

import GUI.CherkerBoard;
import GUI.MyImagePanle;
import PVE.Mypanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import GUI.*;

public class OnePK {

    static int player_color=0;

    Image image1=null;
    Image image2=null;
    String name;
    /**
     * description:class discription
     *
      * @Param: mypanel 棋盘
     * @return
     */
    public OnePK(String name){

        this.name=name;
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


        //电脑时间
        PlayerTime playerTime=new PlayerTime();
        playerTime.setBounds(100,500,200,100);
        //playerTime.start_time();
        playerTime.setOpaque(false);
        frame.add(playerTime);

        //玩家时间
        PlayerTime playerTime1=new PlayerTime();
        playerTime1.setBounds(1130,500,200,100);
        //playerTime1.start_time();
        playerTime1.setOpaque(false);
        frame.add(playerTime1);



        //棋盘
        Mypanel mypanel=new Mypanel(playerTime,playerTime1);
        mypanel.setBounds(400,60,675,675);
        frame.add(mypanel);
        mypanel.setOpaque(false);

        //电脑的图标
        try {
            image1= ImageIO.read(new File("E:\\idea_project\\chess_javaSubject\\src\\image\\computer.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MyImagePanle myImagePanle=new MyImagePanle(128,128,image1);
        myImagePanle.setLayout(null);
        myImagePanle.setBounds(130,100,128,128);
        myImagePanle.setOpaque(false);
        frame.add(myImagePanle);


        //电脑的名字
        JLabel player1=new JLabel("电脑玩家");
        player1.setBounds(130,300,300,50);
        player1.setFont(new java.awt.Font("华文行楷", 4, 35));
        player1.setForeground(Color.BLACK);
        frame.add(player1);


        //玩家的图片
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



        //玩家的名字（右边）
        JLabel player2=new JLabel(name,JLabel.CENTER);
        System.out.println(name);
        player2.setBounds(1130,300,200,50);
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
                    int sel=JOptionPane.showConfirmDialog(null,"是否选择先手?","玩家选择",JOptionPane.YES_NO_OPTION);
                    if(sel==JOptionPane.YES_OPTION){
                        player_color=1;
                    }else{
                        player_color=0;
                    }
                mypanel.setPalyer_color(player_color);
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
                mypanel.takeBack();
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
                mypanel.giveUp();
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
