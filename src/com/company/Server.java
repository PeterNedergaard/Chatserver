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

        BlockingQueue<Message> messages = new ArrayBlockingQueue<>(50);

        CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

        ServerSocket serverSocket = new ServerSocket(port);

        Dispatcher dispatcher = new Dispatcher(clients,messages);

//        Thread t1 = new Thread(dispatcher);
//        t1.start();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        executorService.execute(dispatcher);

        while (true) {
            Socket client = serverSocket.accept();

            ClientHandler cl = new ClientHandler(client,messages,dispatcher);

            clients.add(cl);

            executorService.execute(cl);
        }

    }


}
