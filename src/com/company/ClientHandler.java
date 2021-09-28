package com.company;

import com.sun.java.accessibility.util.Translator;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {
    Socket client;
    PrintWriter pw;
    Scanner sc;
    BlockingQueue<String> queue;
    String name;
    Dispatcher dispatcher;
    boolean online = false;

    public PrintWriter getPw() {
        return pw;
    }

    public Scanner getSc() {
        return sc;
    }

    public ClientHandler(Socket client, BlockingQueue<String> queue, Dispatcher dispatcher) throws IOException {
        this.client = client;
        this.pw = new PrintWriter(client.getOutputStream(), true);
        this.sc = new Scanner(client.getInputStream());
        this.queue = queue;
        this.dispatcher = dispatcher;
    }


    public void protocol() throws IOException {

        String msg = "";
        StringBuilder res;
        String[] arrOfStr;
        boolean closeConnection = false;

        while (!closeConnection) {
            msg = sc.nextLine(); //Blocking call
            res = null;
            arrOfStr = msg.split("#", 2);


            try {
                msg = arrOfStr[1];
            } catch (IndexOutOfBoundsException ie) {
                pw.println("No command");
            }

            switch (arrOfStr[0]) {
                case "CONNECT":
                    if (Server.listOfUsers.contains(msg)){
                        Server.listOfOnlineUsers.add(msg);
                        name = msg;
                        pw.println("ONLINE");
                        online = true;
                        dispatcher.connectMsg();
                    } else {
                        msg = "CLOSE#2";
                    }
                    break;
                case "LOGOUT":
                    Server.listOfOnlineUsers.remove(name);
                    name = "";
                    online = false;
                    dispatcher.connectMsg();
                    break;
                case "SEND":
                    String[] sendArr = msg.split("#", 2);
                    dispatcher.sendMsg(sendArr[0], sendArr[1]);
                    break;
                case "CLOSE":
                    Server.listOfOnlineUsers.remove(name);
                    closeConnection = true;
                    break;

                default:
                    msg = "No message";
            }

            pw.println(msg);

            //messagePrinter();

        }
        client.close();
    }

    public void messagePrinter() {
        if (queue.size() > 0) {
            pw.println(queue.peek());
        }
    }


    @Override
    public void run() {
        try {
            while (true) {
                this.protocol();
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public String getUserInput(String msg) {
        pw.println(msg);
        return sc.nextLine();

    }
}
