import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: socket_test
 * @description: 服务端
 * @author: Mr.Qin
 * @create: 2019-10-15 20:46
 **/

public class Serve {


    public static Socket socketArr[] = new Socket[100];


    public static void main(String[] args) throws IOException {
        socketServe();
    }

    public static void socketServe() throws IOException {

        int peopleNum = 0;
        ServerSocket serverSocket = new ServerSocket(8888);

        while (true) {
            Socket socket = serverSocket.accept();
            peopleNum++;
            System.out.println(socket.getInetAddress().getHostAddress() + "连接进入" + peopleNum);

            socketArr[peopleNum] = socket;
            if (peopleNum % 2 == 0) {

                new SocketThrea(socketArr[peopleNum], socketArr[peopleNum - 1]).start();
            }


        }
    }
}

class SocketThrea extends Thread {


    private static List<PrintWriter> list1 = new ArrayList<>();
    private static List<PrintWriter> list2 = new ArrayList<>();
    private BufferedReader bufferedReader1;
    private PrintWriter printWriter1;
    private BufferedReader bufferedReader2;
    private PrintWriter printWriter2;
    String string1 = null;
    String string2 = null;
    boolean isCilent1 = false;
    boolean isCilent2 = false;


    public SocketThrea(Socket socket1, Socket socket2) throws IOException {

        System.out.println(socket1 + " " + socket2);
        this.printWriter1 = new PrintWriter(socket1.getOutputStream());
        this.bufferedReader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));


        list1.add(printWriter1);

        this.printWriter2 = new PrintWriter(socket2.getOutputStream());
        this.bufferedReader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));


        list2.add(printWriter2);

    }

    @Override
    public void run() {


        System.out.println("chengong");


        //通知连接成功
        printWriter1.write("Success1\n");
        printWriter1.flush();
        printWriter2.write("Success2\n");
        printWriter2.flush();
        System.out.println(printWriter1 + " " + printWriter2);

        //0为黑棋，1为白棋

            try {

                //客户端1读进程
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        while (true) {
                            try {
                                string1 = bufferedReader1.readLine();
                                System.out.println("客户端1消息：" + string1);

                                if(string1.equals("black")||string1.equals("white")) {

                                    printWriter1.write("black"+"\n");
                                    printWriter1.flush();
                                }else if(string1.equals("back")){

                                    printWriter2.write(string1 + "\n");
                                    printWriter2.flush();
                                }else if(string1.equals("giveup")){
                                    printWriter2.write(string1 + "\n");
                                    printWriter2.flush();
                                }else {
                                    printWriter2.write(string1 + "\n");
                                    printWriter2.flush();
                                    System.out.println("已发给客户端2：" + string1);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    }).start();

                //客户端2读进程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                string2 = bufferedReader2.readLine();
                                System.out.println("客户端2消息：" + string2);

                                if(string2.equals("black")||string2.equals("white")) {

                                    printWriter2.write("white"+"\n");
                                    printWriter2.flush();
                                }else if(string2.equals("back")){
                                    printWriter1.write(string2 + "\n");
                                    printWriter1.flush();
                                }else if(string2.equals("giveup")){
                                    printWriter1.write(string2 + "\n");
                                    printWriter1.flush();
                                }else {
                                    printWriter1.write(string2 + "\n");
                                    printWriter1.flush();
                                    System.out.println("已发给客户端1：" + string2);
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }).start();

                //写给客户端1




                //写给客户端2



            } catch (Exception e) {
                e.printStackTrace();
            }
        }


}


