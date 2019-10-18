/**
 * @program: chess_javaSubject
 * @description: 剩余时间
 * @author: Mr.Qin
 * @create: 2019-10-04 20:26
 **/

/*
 * #Time.java文档
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;

public class Time extends JPanel implements ActionListener{
    /**
     * 反序列化
     */
    private static final long serialVersionUID = 1L;

    //定义三个按钮
    private JButton start, stop, reset;

    //定义一个标签用于显示
    private JLabel label;

    //定义一个timer用于计时
    private Timer timer;

    //flag变量，用于记录
    private int flag = 0;

    //时间参数
    private int hour = 0;
    private int minute = 0;
    private int second = 0;

    /*
     * 构造方法，用于创建计时器对象
     */
    public Time(){

        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(800,600));
        setLayout(null);

        //数字显示
        label = new JLabel("00:00:00");
        label.setBounds(240, 120, 400, 80);
        label.setFont(new java.awt.Font("Dialog", 1, 72));
        add(label);

        JPanel p1 = new JPanel();
        p1.setBackground(Color.LIGHT_GRAY);
        p1.setBounds(240, 240, 300, 60);
        add(p1);

        start = new JButton("Start");
        start.setFont(new Font("SAN_SERIF",Font.BOLD,24));
        start.setBackground(Color.green);
        stop = new JButton("Stop");
        stop.setFont(new Font("SAN_SERIF",Font.BOLD,24));
        stop.setBackground(Color.red);
        reset = new JButton("Reset");
        reset.setFont(new Font("SAN_SERIF",Font.BOLD,24));
        reset.setBackground(Color.yellow);

        start.addActionListener(this);
        stop.addActionListener(this);
        reset.addActionListener(this);

        p1.add(start);
        p1.add(stop);
        p1.add(reset);

        timer = new Timer(1000, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource() == reset){// 清除标签内容
            timer.stop();
            flag = 0;
            label.setText(resetTime());
            System.out.println(hour + ":" + minute + ":" + second);
        }
        else if(e.getSource() == start){
            flag = 1;
            label.setText(countTime());
            System.out.println(hour + ":" + minute + ":" + second);
            timer.restart();
        }
        else if(e.getSource() == stop){
            timer.stop();
            flag = 0;
            label.setText(stopTime());
            System.out.println(hour + ":" + minute + ":" + second);
        }
        else if(flag==1 && timer.isRunning()){
            label.setText(countTime());
            System.out.println(hour + ":" + minute + ":" + second);
        }
        else if(flag==0 && !timer.isRunning()){
            label.setText(stopTime());
            System.out.println(hour + ":" + minute + ":" + second);
        }

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
            this.hour += 1;
        }

        String str = this.toString(this.hour, this.minute, this.second);
        return str;
    }

    /*
     * 暂停计时方法
     */
    public String stopTime(){
        String str = this.toString(this.hour, this.minute, this.second);
        return str;
    }

    /*
     * 重置计时方法
     */
    public String resetTime(){
        this.hour=0;
        this.minute=0;
        this.second=0;
        String str = this.toString(this.hour, this.minute, this.second);
        return str;
    }

    /*
     * 显示时间方法
     */
    public String toString(int hour, int minute, int second){
        String str1 = String.format("%02d", this.hour);
        String str2 = String.format("%02d", this.minute);
        String str3 = String.format("%02d", this.second);
        return (str1 + ":" + str2 + ":" + str3);
    }

}
