package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {
    private int port;

    public Server(int port){
        this.port = port;
    }

    public void startServer() throws IOException {

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(50);

        CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

        ServerSocket serverSocket = new ServerSocket(port);

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        while (true) {
            Socket client = serverSocket.accept();

            ClientHandler cl = new ClientHandler(client,queue);

            clients.add(cl);

            executorService.execute(cl);
        }

    }
}
