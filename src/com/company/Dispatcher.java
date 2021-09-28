package com.company;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;


public class Dispatcher implements Runnable {

    CopyOnWriteArrayList<ClientHandler> clients;

    public Dispatcher(CopyOnWriteArrayList<ClientHandler> clients) {
        this.clients = clients;
    }


    @Override
    public void run() {

    }

    public void sendMsg(String name, String msg, String sender) {
        String[] nameArr;

        if (name.contains(",")) {
            nameArr = name.split(",", 0);

            for (String s : nameArr) {
                if (!Server.listOfOnlineUsers.contains(s)) {
                    sendMsg(sender, s + " not found", "Server");
                }
                for (ClientHandler c : clients) {

                    if (c.name.equals(s)) {
                        c.pw.println("MESSAGE#" + sender + "#" + msg);
                    }
                }
            }
        } else {
            for (ClientHandler c : clients) {
                if (c.name != null && c.name.equals(name) && c.online) {
                    c.pw.println("MESSAGE#" + sender + "#" + msg);
                }
            }
        }

    }


    public void connectMsg() {
        for (ClientHandler c : clients) {
            if (c.online) {
                c.pw.println("ONLINE#" + Server.listOfOnlineUsers.toString());
            }
        }
    }

}
