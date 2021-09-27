package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.*;

public class Server {
    private int port;

    public static ArrayList<String> listOfUsers = new ArrayList<>();
    public static ArrayList<String> listOfOnlineUsers = new ArrayList<>();

    public Server(int port){
        this.port = port;
    }

    public void startServer() throws IOException {

        listOfUsers.add("Peter");
        listOfUsers.add("Rabee");
        listOfUsers.add("Knud");

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(50);

        CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

        ServerSocket serverSocket = new ServerSocket(port);

        Thread t1 = new Thread(new Dispatcher(clients));
        t1.start();
        //Dispatcher dispatcher = new Dispatcher(clients);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        while (true) {
            Socket client = serverSocket.accept();

            ClientHandler cl = new ClientHandler(client,queue);

            clients.add(cl);

            executorService.execute(cl);
        }

    }
}
