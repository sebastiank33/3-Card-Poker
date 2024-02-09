import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class compute {

    static boolean queenOrHigher(ArrayList<Integer> dealerCards) { // 11 = queen, 12 = king, 13 = ace
        for (int card: dealerCards) { // 0-12 = clubs, 13-25 = diamonds, 26-38 = hearts, 39-51 = spades
            if (card % 13 == 11 || card % 13 == 12)  { //
                return true;
            }
        }
        return false;
    }

    // consecutive cards, the value of the card is classed as the remainder of the card number divided by 13
    static boolean straight(ArrayList<Integer> cards) {
        Collections.sort(cards); //sorts the cards in ascending order
        int cardType1 = cards.get(0) % 13;
        int cardType2 = cards.get(1) % 13;
        int cardType3 = cards.get(2) % 13;
        return ((cardType1 == cardType2 - 1) && (cardType2 == cardType3 - 1)); //checks if the cards are consecutive
    }

    // cards of the same suit, the suit of the card is classed as the quotient of the card number divided by 13, 0 is a club, 1 is a spade, 2 is a heart, and 3 is a diamond
    static boolean flush(ArrayList<Integer> cards) {
        return (((cards.get(0) / 13) == (cards.get(1) / 13)) &&
                ((cards.get(1) / 13) == (cards.get(2) / 13)));
    }

    // three of a king, therefore every card % 13 must yield the same result
    static boolean threeOfAKind(ArrayList<Integer> cards) {
        return (((cards.get(0) % 13) == (cards.get(1) % 13)) &&
                ((cards.get(1) % 13) == (cards.get(2) % 13)));
    }

    // two cards of the same type, only two of the three cards must yield the same result % 13
    static boolean pair(ArrayList<Integer> cards) {
        boolean bool1 = (((cards.get(0) % 13) == (cards.get(1) % 13)) ^
                ((cards.get(1) % 13) == (cards.get(2) % 13))); // checks if the first and second card are the same

        boolean bool2 = (cards.get(0) % 13) == (cards.get(2) % 13); // checks if the first and last card are the same

        return bool1 || bool2; // returns true if either of the two conditions are true
    }

    static int pairPlusWinnings(PokerInfo info) { // returns the winnings for the pair plus bet
        int wager = info.get_paiPlusWager(); // gets the wager for the pair plus bet

        if (info.get_paiPlusWager() > 0) { // if the wager is greater than 0, then the player has placed a bet
            if (straight(info.client_cards) && flush(info.client_cards)) { return wager * 40; } // if the player has a straight flush, then they win 40 times their wager
            else if (threeOfAKind(info.client_cards)) { return wager * 30; } //payout for 3 of a kind pair plus
            else if (straight(info.client_cards))     { return wager * 6; } //pay out for straight pair plus
            else if (flush(info.client_cards))        { return wager * 3; } //pay out for flush pair plus
            else if (pair(info.client_cards))         { return wager; } //pay out for pair plus
            else                                      { return info.get_paiPlusWager() * -1; }
        }
        return 0;
    }

    static int winnings(PokerInfo info) { // returns the winnings for the ante bet

        int totalWager = info.get_anteWager() + info.get_anteWager(); // gets the total wager for the ante bet

        int clientHand = 0;
        int serverHand = 0;

        if (straight(info.client_cards) && flush(info.client_cards)) { clientHand = 5; }
        else if (threeOfAKind(info.client_cards)) { clientHand = 4; }
        else if (straight(info.client_cards))     { clientHand = 3; }
        else if (flush(info.client_cards))        { clientHand = 2; }
        else if (pair(info.client_cards))         { clientHand = 1; }

        if (straight(info.server_cards) && flush(info.server_cards)) { clientHand = 5; }
        else if (threeOfAKind(info.server_cards)) { serverHand = 4; }
        else if (straight(info.server_cards))     { serverHand = 3; }
        else if (flush(info.server_cards))        { serverHand = 2; }
        else if (pair(info.server_cards))         { serverHand = 1; }

        if (serverHand > clientHand) {
            return totalWager * -1;
        }
        else if (serverHand < clientHand) {
            return totalWager;
        }
        else {
            return 0;
        }
    }
}

