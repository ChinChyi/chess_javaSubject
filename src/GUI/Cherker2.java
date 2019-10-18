package GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.*;
import PVE.*;
import PVP.*;

public class Cherker2
{



    private ArrayList<String> nameList=new ArrayList<>();
    String name=null;
    public Cherker2(){


        //加载图片
        ImageIcon icon=new ImageIcon("src//image/back.png");
        //Image im=new Image(icon);
        //将图片放入label中
        JLabel label=new JLabel(icon);

        //设置label的大小
        label.setBounds(0,0,icon.getIconWidth()/2,icon.getIconHeight()/2);

        JFrame frame=new JFrame();

        //获取窗口的第二层，将label放入

        frame.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));

        //获取frame的顶层容器,并设置为透明
        JPanel j=(JPanel)frame.getContentPane();
        j.setOpaque(false);

        name=JOptionPane.showInputDialog("填写你的昵称");
        //System.out.println(name);
        nameList.add(name);
        JLabel nameLabel =new JLabel("欢迎你 "+name);
        nameLabel.setBounds(700,50,200,100);
        nameLabel.setFont(new java.awt.Font("华文行楷", 4, 30));
        nameLabel.setForeground(Color.BLACK);
        frame.add(nameLabel);

        JPanel panel=new JPanel();
        placeComplent(panel,frame,name);






        //必须设置为透明的。否则看不到图片
        panel.setOpaque(false);

        frame.add(panel);
        frame.add(panel);
        Toolkit kit =Toolkit.getDefaultToolkit();
        Dimension screenSize=kit.getScreenSize();
        int screenHeight=screenSize.height;
        int screenWidth=screenSize.width;
        //置于屏幕中央   
        frame.setSize(screenWidth/2 ,screenHeight/ 2 );
        frame.setLocation(screenWidth/4 ,screenHeight/ 4 );
        frame.setVisible(true);



    }
    public static void main(String[] args)
    {

        new CherkerBoard();
    }
    private static void placeComplent(JPanel jPanel, JFrame jFrame,String name){


        /**
         * description:最外层是BorderLayout,North是JLabel,center是一个GridLayout(3,2)
         *
         * @Param: jPanel 传进来的画布
         * @Param: jLabel “联机五子棋”标题栏
         * @Param: mainText 中间的网格布局
         * @Param: onepk 人机对战Button
         * @Param: morepk 匹配模式Button

         * @return void
         */
        BorderLayout borderLayout=new BorderLayout(5,10);
        jPanel.setLayout(borderLayout);



        //上
        JLabel jLabel=new JLabel("联机五子棋");
        jLabel.setPreferredSize(new Dimension(500,150));
        jLabel.setFont(new java.awt.Font("华文行楷", 4, 35));
        jLabel.setForeground(Color.BLACK);
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel.add(jLabel,BorderLayout.NORTH);

        //中

        JPanel mainText=new JPanel();
        mainText.setOpaque(false);
        mainText.setLayout(new GridLayout(2,3,50,50));




        JButton onepk=new JButton("人 机 对 战");
        onepk.setSize(200,90);
        onepk.setContentAreaFilled(false);
        onepk.setBorderPainted(false);
        onepk.setBorder(BorderFactory.createLoweredBevelBorder());
        onepk.setFont(new java.awt.Font("华文行楷", 1, 40));
        JButton morepk=new JButton("匹 配 对 战");
        morepk.setSize(200,90);
        morepk.setContentAreaFilled(false);
        morepk.setBorderPainted(false);
        morepk.setBorder(BorderFactory.createLoweredBevelBorder());
        morepk.setFont(new java.awt.Font("华文行楷", 1, 40));
        mainText.add(onepk);
        mainText.add(morepk);

        //点击事件
        onepk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jFrame.dispose();
                OnePK onePK=new OnePK(name);
            }
        });


        morepk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InfiniteProgressPanel glasspane = new InfiniteProgressPanel();
                Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
                glasspane.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
                jFrame.setGlassPane(glasspane); glasspane.start();//开始动画加载效果

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String str=null;
                        String se_name=null;
                        String number=null;
                        try {
                            Socket socket=new Socket("139.219.10.25",8888);
                            if(socket.isConnected()){
                                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(
                                        socket.getInputStream()));
                                str=bufferedReader.readLine();
                                System.out.println(str);


                                PrintWriter printWriter=new PrintWriter(socket.getOutputStream());
                                printWriter.write(name+"\n");
                                printWriter.flush();

                                se_name=bufferedReader.readLine();
                                System.out.println(se_name);
                                number=str.substring(str.length()-1);
                                str=str.substring(0,str.length()-1);
                                System.out.println(number);
                                System.out.println(str);
                                if(str.equals("Success")){

                                    jFrame.dispose();
                                    if(number.equals("1"))
                                    {
                                        MorePK morePK=new MorePK(name,se_name,socket,1);
                                    }
                                    else if(number.equals("2")){
                                        MorePK morePK=new MorePK(se_name,name,socket,2);
                                    }
                                }
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }


                    }
                }).start();

            }
        });

        //下
        JLabel but=new JLabel("ada");
        but.setPreferredSize(new Dimension(500,150));
        jPanel.add(but,BorderLayout.SOUTH);
        jPanel.add(mainText,BorderLayout.CENTER);

    }
}


class read1 extends Thread {
    private Socket socket;
    private BufferedReader bufferedReader;
    private String str = null;

    public read1(Socket socket) throws IOException {
        this.socket = socket;
        this.bufferedReader = new BufferedReader(new InputStreamReader(socket
                .getInputStream()));
    }

    @Override
    public void run() {
        while (true) {

            try {
                str = bufferedReader.readLine();
                System.out.println(str);
            } catch (IOException e) {
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }
    }
}