import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;


public class Dispatcher implements Runnable {

    CopyOnWriteArrayList<ClientHandler> clients;
    BlockingQueue<Message> messages;

    public Dispatcher(CopyOnWriteArrayList<ClientHandler> clients, BlockingQueue<Message> messages) {
        this.clients = clients;
        this.messages = messages;
    }


    @Override
    public void run() {
        while(true){
            Message message = null;
            try {
                message = messages.take(); //Blocking call
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendMsg(message.reciever, message.msg, message.sender);
            System.out.println("hej igen");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
                } else if(!Server.listOfOnlineUsers.contains(name)) {
                    sendMsg(c.name, name + " not found", "Server");
                    return;
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
