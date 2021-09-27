package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
	    Server server = new Server(2345);

        try {
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
