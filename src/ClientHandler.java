import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class ClientHandler implements Runnable {
    Socket client;
    PrintWriter pw;
    Scanner sc;
    BlockingQueue<Message> messages;
    String name;
    Dispatcher dispatcher;
    boolean online = false;

    public ClientHandler(Socket client, BlockingQueue<Message> messages, Dispatcher dispatcher) throws IOException {
        this.client = client;
        this.pw = new PrintWriter(client.getOutputStream(), true);
        this.sc = new Scanner(client.getInputStream());
        this.messages = messages;
        this.dispatcher = dispatcher;
    }


    public void protocol() throws IOException, InterruptedException {

        String msg = "";
        String[] arrOfStr;
        boolean closeConnection = false;

        pw.println("Please enter username to continue:");
        while (!closeConnection) {

            try {
                msg = sc.nextLine(); //Blocking call
            }catch (NoSuchElementException nse){
                client.close();
            }

            arrOfStr = msg.split("#", 2);

            try {
                msg = arrOfStr[1];
            } catch (IndexOutOfBoundsException ie) {
                pw.println("No command");
            }

            switch (arrOfStr[0]) {
                case "CONNECT":
                    if (Server.listOfUsers.contains(msg) && !online){
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
                    pw.println("OFFLINE");
                    break;
                case "SEND":
                    String[] sendArr = msg.split("#", 2);
                    messages.put(new Message(sendArr[0], sendArr[1], name));
                    break;
                case "CLOSE":
                    Server.listOfOnlineUsers.remove(name);
                    closeConnection = true;
                    pw.println("CLOSE#0");
                    break;

                default:
                    msg = "No message";
            }

        }
        client.close();
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.protocol();
            }
        } catch (IOException | InterruptedException io) {
            io.printStackTrace();
        }
    }

    public String getUserInput(String msg) {
        pw.println(msg);
        return sc.nextLine();

    }
}
