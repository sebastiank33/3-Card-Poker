import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.net.InetSocketAddress;

//test2
public class Server{

    int count = 1; // client number
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>(); // list of clients
    TheServer server; // server
    private Consumer<Serializable> callback; // callback function
    int port_number;

    int TotalCountClients = 0; // total number of clients

    Set<String> uniqueClients = new HashSet<>(); // set of unique clients to print whether the same client is playing another hand

    //boolean property to control client connections
    private volatile boolean allowClients = true;
    // setter method for the boolean property
    public void setAllowClients(boolean allowClients) {
        this.allowClients = allowClients; // set the property to the value passed in
        if (!allowClients) {
            for (ClientThread client : clients) { // loop through all the clients
                client.setConnected(false); // set the connected property to false
                try {
                    client.connection.close(); // close the connection(server off)
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // constructor
    Server(Consumer<Serializable> call, int port_number){ // constructor
        callback = call; // callback function
        this.port_number = port_number; // port number
        server = new TheServer(); // server
        server.start(); // start server
    }

    public class TheServer extends Thread{

        public void run() {
            try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) { //this try block is for the server socket
                serverSocketChannel.bind(new InetSocketAddress(port_number)); // bind the server socket to the port number
                serverSocketChannel.configureBlocking(false); // set the server socket to non-blocking

                while (true) {
                    if (allowClients) { // if the server is allowed to accept clients
                        SocketChannel socketChannel = serverSocketChannel.accept(); // accept the client
                        if (socketChannel != null) { // if the client is not null
                            ClientThread c = new ClientThread(socketChannel.socket(), count); // create a new client thread
                            callback.accept("client has connected to server: " + "client #" + count); // print to the server console
                            clients.add(c); // add the client to the list of clients
                            c.start(); // start the client thread
                            count++; // increment the client count
                            TotalCountClients++; // increment the total number of clients
                            callback.accept("Total number of clients: " + TotalCountClients); // print the total num of clients to the listView
                        } else {
                            Thread.sleep(100); //aids with the non-blocking server socket and cpu usage
                        }
                    } else {
                        Thread.sleep(1000); //aids with the non-blocking server socket and cpu usage
                    }
                }
            } catch (Exception e) {
                callback.accept("Server socket did not launch");
            }
        }
    }

    class ClientThread extends Thread{
        Socket connection;
        int count;
        ObjectInputStream in; // input stream
        ObjectOutputStream out; // output stream
        PokerInfo info; // poker info object

        String uniqueId; // unique id for each client used in the set of unique clients

        private volatile boolean connected; // boolean property to control client connections

        ClientThread(Socket s, int count){ // constructor
            this.connection = s; // socket
            this.count = count;
            this.uniqueId = s.getInetAddress().toString() + ":" + s.getPort(); // set the unique id to the ip address and port number used for the set of unique clients
            connected = true;
        }

        public void setConnected(boolean connected) {
            this.connected = connected;
        }

        public ArrayList<Integer> draw_three_cards(int card_index) { // draws three cards
            ArrayList<Integer> three_cards = new ArrayList<>(); // list of three cards
            for (int i = card_index; i < card_index + 3; i++) { // loop through the three cards
                three_cards.add(info.get_shuffledCards().get(i)); // add the card to the list of three cards
                info.cardIndex++;
            }
            return three_cards; //returns the list of three cards(shuffled)
        }

        public void shuffleCards() { // shuffles the cards
            ArrayList<Integer> shuffledCardDeck = new ArrayList<>(); // list of shuffled cards
            for (int i = 0; i < 52; i++) { // loop through the cards
                shuffledCardDeck.add(i);
            }
            Collections.shuffle(shuffledCardDeck); // shuffle the cards
            info.set_shuffledCards(shuffledCardDeck); // set the shuffled cards to the poker info object
            info.set_clientCards(draw_three_cards(info.cardIndex)); // set three cards for the client
            info.set_serverCards(draw_three_cards(info.cardIndex)); // set three cards for the server/dealer
        }

        // sends info to client
        public void send(PokerInfo instance) {
            try {
                instance.print_info();
                out.writeObject(instance);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void run(){ // run method which is called when the thread is started it waits for the client to send info and then it sends info back to the client
            try {
                in = new ObjectInputStream(connection.getInputStream()); // input stream
                out = new ObjectOutputStream(connection.getOutputStream()); // output stream
                connection.setTcpNoDelay(true); // set the connection to no delay
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            while(connected) {
                try {
                    // gets info from client
                    PokerInfo clientData = (PokerInfo) in.readObject();

                    if (!uniqueClients.contains(uniqueId)) { // if the client is not in the set of unique clients
                        uniqueClients.add(uniqueId);
                    } else {
                        // Print callback statement if the same client is playing another hand
                        callback.accept("client #" + count + " is playing another hand");
                    }

                    callback.accept("client # " + count + " ante wager: $" +
                            clientData.get_anteWager() + " pair plus wager: $" + clientData.get_paiPlusWager()); // print the ante wager and pair plus wager to the server console
                    System.out.println("newGame is " + clientData.newGame); //testing
                    info = clientData; // set the poker info object to the client data in order to send it back to the client

                    if (clientData.fold) { // if the client folds
                        info.winnings = (info.get_anteWager() + -info.get_paiPlusWager()) * -1; // set the winnings to the ante wager and pair plus wager
                        info.setGameMessage("Player has folded."); // set the game message to the player has folded
                    }
                    else {
                        if (clientData.newGame) { // if the client starts a new game
                            int oldWinnings = info.totalWinnings; // set the old winnings to the total winnings
                            this.info = new PokerInfo(0, 0); // create a new poker info object
                            shuffleCards(); // shuffle the cards
                            info.totalWinnings = oldWinnings; // set the total winnings to the old winnings
                            info.set_anteWager(clientData.get_anteWager()); // set the ante wager to the ante wager from the client
                            info.set_pairPlusWager(clientData.get_paiPlusWager());// set the pair plus wager to the pair plus wager from the client
                        }
                        else if (clientData.nextHand) { // if the client wants to play another hand
                            info.server_cards = draw_three_cards(info.cardIndex); // draw three cards for the server
                            info.nextHand = false; // set the next hand to false
                            info.set_anteWager(clientData.get_anteWager()); // set the ante wager to the ante wager from the client
                            info.set_pairPlusWager(clientData.get_paiPlusWager()); // set the pair plus wager to the pair plus wager from the client
                        }
                        if (clientData.play) { // if the client selects play
                            if (!compute.queenOrHigher(info.get_serverCards())) { // if the server does not have a queen or higher
                                info.set_queenHigh(false); // set the queen high to false
                            } else { // if the server has a queen or higher
                                info.set_queenHigh(true); // set the queen high to true
                                info.winnings = compute.winnings(info); // set the winnings to the winnings from the compute class
                                info.winningsPair = compute.pairPlusWinnings(info); // set the winnings pair to the winnings pair from the compute class
                                info.totalWinnings = info.totalWinnings + info.winnings + info.winningsPair; // set the total winnings to the total winnings plus the winnings and winnings pair
                            }
                        }
                    }

                    int totalWinnings = info.winnings + info.winningsPair; // set the total winnings to the winnings plus the winnings pair

                    if (info.winnings > 0) { // if the winnings are greater than 0 than the client has one, otherwise they've lost
                        callback.accept("client # " + count + " has won $" + totalWinnings);
                    } else {
                        callback.accept("client # " + count + " has lost $" + (-totalWinnings));
                    }

                    send(info); // send the info to the client
                }
                catch(Exception e) {
                    callback.accept("client: " + count + " left the game!");
                    TotalCountClients--;
                    clients.remove(this);
                    callback.accept("Total number of clients: " + TotalCountClients);
                    break;
                }
            }
        }//end of run
    }//end of client thread
}






