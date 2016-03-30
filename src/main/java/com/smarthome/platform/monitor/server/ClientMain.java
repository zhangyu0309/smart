package com.smarthome.platform.monitor.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
 
class ClientMain {
    /**
     * @param args
     */
    public static void main(String[] args) {
        Socket socket = null;
        System.out.println("ClientSocket Begin........");
        try {
            for(int i = 0;i<1;i++){
                socket = new Socket("127.0.0.1",5000);
                new Thread(new ClientThread(socket,i),"ClientThread "+i).start();
            }
             
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
     
}
 
/**
 * 客户端线程
 * @author JCC
 *
 */
class ClientThread implements Runnable{
    Socket socket = null;
    int id = 0;
    public ClientThread(Socket socket,int id){
        this.socket = socket;
        this.id = id;
    }
    public void run() {
        OutputStream out = null;
        InputStream in = null;
        System.out.println("Begin to Chat to server...");
        try {
            out = socket.getOutputStream();
            in = socket.getInputStream();
            //循环发送与服务端不停的交互数据
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                doWrite(out);
//                System.out.println("begin read message from server.");
//                doRead(in);
            }
             
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
     
    /**
     * 读取服务端数据
     * @param in
     * @return
     */
    public static boolean doRead(InputStream in){
        //引用关系，不要在此处关闭流
        byte[] bytes = new byte[1024];
        try {
            in.read(bytes);
            System.out.println("line:"+new String(bytes).trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        return true;
    }
     
    /**
     * 发送数据到服务端
     * @param out
     * @return
     */
    public boolean doWrite(OutputStream out){
        //引用关系，不要在此处关闭流
        String line = "Hello server, I am client = "+id +",";
        line = line +"I want you to do something for me";
        try {
            out.write(line.getBytes());
            out.flush();        
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}