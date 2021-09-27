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

    public PrintWriter getPw() {
        return pw;
    }

    public Scanner getSc() {
        return sc;
    }

    public ClientHandler(Socket client, BlockingQueue<String> queue) throws IOException {
        this.client = client;
        this.pw = new PrintWriter(client.getOutputStream(), true);
        this.sc = new Scanner(client.getInputStream());
        this.queue = queue;
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
                        pw.println("ONLINE");
                    } else {
                        msg = "CLOSE#2";
                    }
                    break;
                case "SEND":
                    String[] sendArr = msg.split("#", 2);
                    //Send besked til sendArr[0] med beskeden sendArr[1]
                    break;
                case "CLOSE":
                    closeConnection = true;
                    break;

                default:
                    msg = "No message";
            }

            pw.println(msg);

            //Den læser og/eller sletter første element i message queuen
            //find en måde at printe nye beskeder så snart de sendes,
            //da sc.nextline() blokerer tråden, så messagePrinter() kun kaldes når man
            //selv har sendt en besked.
            messagePrinter();

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
