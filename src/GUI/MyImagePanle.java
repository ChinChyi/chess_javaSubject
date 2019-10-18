package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @program: chess_javaSubject
 * @description: 图片类
 * @author: Mr.Qin
 * @create: 2019-10-04 19:38
 **/

public class MyImagePanle extends JPanel {
    int height,width;
    Image image;
    public MyImagePanle(int height,int width,Image image){
        super();
        this.height=height;
        this.width=width;
        this.image=image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image,0,0,width,height,this);
        repaint();
    }
}
