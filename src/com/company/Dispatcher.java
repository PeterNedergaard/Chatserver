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

        while (true) {
            for (ClientHandler c : clients) {
                c.pw.println("Hej fra Dispatcher");
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
