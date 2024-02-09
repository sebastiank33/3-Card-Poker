import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.ArrayList;

//test1
public class Client extends Thread{
    Socket socketClient;
    ObjectOutputStream out;
    ObjectInputStream in;

    // UNDO UNTIL HERE
    private Consumer<Serializable> callback;
    int port_number = 0;
    String IP_addr = "";
    PokerInfo info = new PokerInfo(0, 0);

    // ArrayList<Integer> clientCards; //idk if this is needed

    Client(Consumer<Serializable> call, int port_number, String IP_addr){
        callback = call;
        this.port_number = port_number;
        this.IP_addr = IP_addr;
    }

    public void run() {
        try {
            socketClient = new Socket(IP_addr, port_number);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);
        } catch (Exception e) { }

        while (true) {
            try {
                // gets info from server
                PokerInfo serverData = (PokerInfo) in.readObject();
                serverData.print_info();
                callback.accept(serverData);
                info = serverData;
                // info.print_info();
                info.gameInfoMessage = serverData.getGameMessage();

            } catch (Exception e) { }
        }
    }
    // sends info to server
    public void send(PokerInfo instance) {
        try {
            out.writeObject(instance);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
