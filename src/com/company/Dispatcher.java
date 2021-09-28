package com.company;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Dispatcher implements Runnable{

    CopyOnWriteArrayList<ClientHandler> clients;

    public Dispatcher(CopyOnWriteArrayList<ClientHandler> clients){
        this.clients = clients;
    }


    @Override
    public void run() {

//        while (true) {
//            for (ClientHandler c : clients) {
//                if (c.name != null && c.name.equals("Peter")) {
//                    c.pw.println("Hej fra Dispatcher");
//                }
//            }
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void sendMsg(String name, String msg){
        for (ClientHandler c : clients) {
            if (c.name != null && c.name.equals(name) && c.online) {
                c.pw.println(msg);
            }
        }
    }


    public void connectMsg(){
        for (ClientHandler c : clients) {
            if (c.online) {
                c.pw.println("ONLINE#" + Server.listOfOnlineUsers.toString());
            }
        }
    }

}
