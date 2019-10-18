package GUI;

import sun.dc.pr.PRError;

/**
 * @program: chess_javaSubject
 * @description: 棋子类
 * @author: Mr.Qin
 * @create: 2019-10-05 19:42
 **/

public class Chess {
    private int x,y;
    private int style;
    private int M_X;
    private int M_y;

    public Chess(int x,int y,int style){
        this.x=x;
        this.y=y;
        this.style=style;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getM_X() {
        return M_X;
    }

    public void setM_X(int m_X) {
        M_X = m_X;
    }

    public int getM_y() {
        return M_y;
    }

    public void setM_y(int m_y) {
        M_y = m_y;
    }
}
