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

        while (!msg.equals("CLOSE#")) {
            msg = sc.nextLine();
            res = null;
            arrOfStr = msg.split("#", 2);

            try {
                msg = arrOfStr[1];
            } catch (IndexOutOfBoundsException ie) {
                pw.println("No command");
            }

            switch (arrOfStr[0]) {
                case "UPPER":
                    msg = msg.toUpperCase();
                    break;
                case "LOWER":
                    msg = msg.toLowerCase();
                    break;
                case "REVERSE":
                    res = new StringBuilder(msg);
                    res = res.reverse();
                    msg = res.toString();
                case "ALL":
                    //indsæt besked i delt ressource
                    queue.add(msg);
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
