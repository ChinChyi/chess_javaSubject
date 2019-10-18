package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: chess_javaSubject
 * @description: 下棋时间计时
 * @author: Mr.Qin
 * @create: 2019-10-04 20:34
 **/

public class PlayerTime extends JPanel  implements ActionListener{
    //定义一个标签用于显示
    private JLabel label;
    //定义一个timer用于计时
    private Timer timer;

    public int i=0;
    //flag变量，用于记录
    private int flag = 0;

    //时间参数
    private int minute = 0;
    private int second = 0;

    private int m=0,s=0;

    public PlayerTime(){
        setBackground(Color.white);
        setPreferredSize(new Dimension(200,100));
        setLayout(null);

        //数字显示
        label = new JLabel("00:00");
        label.setBounds(0, 0, 200, 100);
        label.setFont(new java.awt.Font("Dialog", 1, 72));
        add(label);

        timer = new Timer(1000, this);
        timer.start();


    }


//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//
//                i=1;
//                if(i==1) {
//                    if (flag == 1 && timer.isRunning()) {
//                        label.setText(countTime());
//                    } else if (flag == 0 && !timer.isRunning()) {
//                        label.setText(stopTime());
//                    }
//                    i=0;
//                }
//            }
//        }).start();
//
//
//    }

    public void reset_time(){
        timer.stop();
        flag = 0;
        label.setText(resetTime());
    }

    public void start_time(){
        flag = 1;
        label.setText(countTime());
        timer.restart();
    }

    public void stop_time(){
        timer.stop();
        flag = 0;
        label.setText(stopTime());
    }
    /*
     * 计时方法
     */
    public String countTime(){
        this.second += 1;
        if(this.second == 60){
            this.minute += 1;
            this.second = 0;
        }
        if(this.minute == 60){
            this.minute = 0;
        }
        String str = this.toString(this.minute, this.second);

        return str;
    }

    /*
     * 暂停计时方法
     */
    public String stopTime(){
        this.minute=0;
        this.second=0;
        String str = this.toString( this.minute, this.second);
        return str;
    }

    /*
     * 重置计时方法
     */
    public String resetTime(){

        this.minute=0;
        this.second=0;
        String str = this.toString( this.minute, this.second);
        return str;
    }

    /*
     * 显示时间方法
     */
    public String toString(int minute, int second){

        m=minute;
        s=second;
        String str2 = String.format("%02d", this.minute);
        String str3 = String.format("%02d", this.second);
        return (str2 + ":" + str3);
    }

    public boolean getTimer(){

        if(second+minute*60>=20)
            return false;
        else
            return true;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(flag==1 && timer.isRunning()){
            label.setText(countTime());
//            System.out.println( minute + ":" + second);
        }
        else if(flag==0 && !timer.isRunning()){
            label.setText(stopTime());
//            System.out.println( minute + ":" + second);
        }
    }
}



