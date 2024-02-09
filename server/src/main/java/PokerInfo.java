import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

// server pokerInfo
public class PokerInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private int anteWager;
    private int pairPlusWager;
    ArrayList<Integer> shuffled_cards;
    ArrayList<Integer> client_cards;
    ArrayList<Integer> server_cards;
    boolean queenHigh;
    boolean fold;
    int winnings;
    int totalWinnings;
    int winningsPair;
    int cardIndex;
    boolean newGame;
    boolean nextHand;
    boolean play;

    String gameInfoMessage;


    public void set_anteWager(int anteWager) { this.anteWager = anteWager; }
    public void set_pairPlusWager(int pairPlusWager) { this.pairPlusWager = pairPlusWager; }
    public void set_shuffledCards(ArrayList<Integer> shuffled_cards) { this.shuffled_cards = shuffled_cards; }
    public void set_clientCards(ArrayList<Integer> client_cards) { this.client_cards = client_cards; }
    public void set_serverCards(ArrayList<Integer> server_cards) { this.server_cards = server_cards; }
    public void set_queenHigh(Boolean queenHigh) { this.queenHigh = queenHigh; }

    public int get_anteWager() { return anteWager; }
    public int get_paiPlusWager() { return pairPlusWager; }
    public ArrayList<Integer> get_shuffledCards() { return shuffled_cards; }
    public ArrayList<Integer> get_clientCards() { return client_cards; }
    public ArrayList<Integer> get_serverCards() { return server_cards; }

    public String getGameMessage() {
        return gameInfoMessage;
    }

    public void setGameMessage(String gameInfoMessage) {
        this.gameInfoMessage = gameInfoMessage;
    }

    public void print_info() {
        System.out.println("-----------------------------------------------------");
        System.out.println("Ante Wager is " + anteWager);
        System.out.println("Pair plus wager is " + pairPlusWager);
        System.out.println("Card Index is " + cardIndex);

        // print cards
        System.out.println("Shuffled Cards are ...");
        for (int i = 0; i < 52; i++) {
            System.out.print(shuffled_cards.get(i) + "|");
        }
        System.out.println("\nClient Cards are ...");
        for (int i = 0; i < 3; i++) {
            System.out.print(client_cards.get(i) + "|");
        }
        System.out.println("\nServer Cards are ...");
        for (int i = 0; i < 3; i++) {
            System.out.print(server_cards.get(i) + "|");
        }
        // print bool values
        System.out.println("\nqueen High is " + queenHigh);
        System.out.println("fold is " + fold);
        System.out.println("winnings is " + winnings);
        System.out.println("winnings from pair plus is " + winningsPair);


        System.out.println("-----------------------------------------------------");
    }

    public void reset() {
        queenHigh = false;
        fold = false;
        winnings = 0;
        cardIndex = 0;
        newGame = false;
    }

    // constructor
    PokerInfo(int anteWager, int pairPlusWager) {
        this.anteWager = anteWager;
        this.pairPlusWager = pairPlusWager;
    }
}

