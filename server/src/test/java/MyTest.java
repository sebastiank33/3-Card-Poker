import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MyTest {

	@Test
	public void testQueenOrHigher_noQueenOrHigher() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			dealerCards.add(i);
		}
		boolean expectedOutcome = false;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	@Test
	public void testQueenOrHigher_allAboveQueen() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		for (int i = 12; i < 17; i++) {
			dealerCards.add(i);
		}
		boolean expectedOutcome = true;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	@Test
	public void testQueenOrHigher_queenPresentNoHigher() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		dealerCards.add(11);
		for (int i = 0; i < 4; i++) {
			dealerCards.add(i);
		}
		boolean expectedOutcome = true;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	@Test
	public void testQueenOrHigher_multipleQueens() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			dealerCards.add(11);
		}
		boolean expectedOutcome = true;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	@Test
	public void testQueenOrHigher_noQueenOrHigher2() {
		ArrayList<Integer> dealerCards = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			dealerCards.add(i + 13);
		}
		boolean expectedOutcome = false;
		assertEquals(expectedOutcome, compute.queenOrHigher(dealerCards));
	}

	@Test
	//test pair
	public void testPair() {
		assertTrue(compute.pair(new ArrayList<>(Arrays.asList(0, 13, 29))));
		assertFalse(compute.pair(new ArrayList<>(Arrays.asList(0, 1, 2))));
	}

	//test pair2
	@Test
	public void testPair2() {
		assertTrue(compute.pair(new ArrayList<>(Arrays.asList(7, 20, 29))));
		assertFalse(compute.pair(new ArrayList<>(Arrays.asList(0, 1, 2))));
	}

	@Test
	public void testStraight() {
		assertTrue(compute.straight(new ArrayList<>(Arrays.asList(1, 2, 3))));
		assertFalse(compute.straight(new ArrayList<>(Arrays.asList(0, 13, 26))));
	}

	@Test
	public void testStraight2() {
		assertTrue(compute.straight(new ArrayList<>(Arrays.asList(0, 1, 2))));
		assertFalse(compute.straight(new ArrayList<>(Arrays.asList(0, 13, 26))));
	}

	@Test
	//test flush
	public void testFlush() {
		assertTrue(compute.flush(new ArrayList<>(Arrays.asList(17, 13, 14))));
		assertFalse(compute.flush(new ArrayList<>(Arrays.asList(0, 13, 26))));
	}

	@Test
	//test flush with different nums
	public void testFlush2() {
		assertTrue(compute.flush(new ArrayList<>(Arrays.asList(17, 13, 14))));
		assertFalse(compute.flush(new ArrayList<>(Arrays.asList(0, 13, 26))));
	}

	@Test
	//test 3 of a kind
	public void testThreeOfAKind() {
		assertTrue(compute.threeOfAKind(new ArrayList<>(Arrays.asList(0, 13, 26))));
		assertFalse(compute.threeOfAKind(new ArrayList<>(Arrays.asList(0, 13, 28))));
	}

	@Test
	//test 3 of a kind with different nums
	public void testThreeOfAKind2() {
		assertTrue(compute.threeOfAKind(new ArrayList<>(Arrays.asList(0, 13, 26))));
		assertFalse(compute.threeOfAKind(new ArrayList<>(Arrays.asList(0, 13, 28))));
	}

	//testing payouts

	@Test
	//test winnings
	public void TestWinningsStraight() {
		PokerInfo info = new PokerInfo(20, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(0, 1, 2));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 13, 26));
		assertEquals(40, compute.winnings(info));
	}

	@Test
	//test winningsStraight2
	public void TestWinningsStraight2() {
		PokerInfo info = new PokerInfo(35, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(4, 5, 6));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 13, 26));
		assertEquals(70, compute.winnings(info));
	}

	@Test
	//test winningsFlush
	public void TestWinningsFlush() {
		PokerInfo info = new PokerInfo(20, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(17, 13, 14));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 28, 31));
		assertEquals(40, compute.winnings(info));
	}

	@Test
	//test winningsFlush2
	public void TestWinningsFlush2() {
		PokerInfo info = new PokerInfo(56, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(26, 27, 29));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 28, 31));
		assertEquals(112, compute.winnings(info));
	}

	@Test
	//test winningsThreeOfAKind
	public void TestWinningsThreeOfAKind() {
		PokerInfo info = new PokerInfo(20, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(0, 13, 26));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 14, 29));
		assertEquals(40, compute.winnings(info));
	}
	@Test
	//test winningsThreeOfAKind2
	public void TestWinningsThreeOfAKind2() {
		PokerInfo info = new PokerInfo(56, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(1, 27, 40));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 14, 29));
		assertEquals(112, compute.winnings(info));
	}

	@Test
	//test winningsStraightFlush
	public void TestWinningsStraightFlush() {
		PokerInfo info = new PokerInfo(20, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(0, 1, 2));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 13, 26));
		assertEquals(40, compute.winnings(info));
	}

	@Test
	//test winningsStraightFlush2
	public void TestWinningsStraightFlush2() {
		PokerInfo info = new PokerInfo(56, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(9, 10, 11));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 13, 26));
		assertEquals(112, compute.winnings(info));
	}


	@Test
	//test pairplusWinnings2 for 3 of a kind
	public void TestPairPlusWinnings1() {
		PokerInfo info = new PokerInfo(20, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(0, 13, 26));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 14, 29));
		assertEquals(300, compute.pairPlusWinnings(info));
	}

	@Test
	//test pairplusWinnings2 for flush
	public void TestPairPlusWinnings2() {
		PokerInfo info = new PokerInfo(20, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(17, 13, 14));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 28, 31));
		assertEquals(30, compute.pairPlusWinnings(info));
	}

	@Test
	//test pairplusWinnings2 for straightflush
	public void TestPairPlusWinnings3() {
		PokerInfo info = new PokerInfo(20, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(4, 5, 6));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 13, 26));
		assertEquals(400, compute.pairPlusWinnings(info));
	}

	@Test
	//test pairplusWinnings2 for pair
	public void TestPairPlusWinnings4() {
		PokerInfo info = new PokerInfo(20, 10);
		info.client_cards = new ArrayList<>(Arrays.asList(0, 13, 38));
		info.server_cards = new ArrayList<>(Arrays.asList(0, 2, 40));
		assertEquals(10, compute.pairPlusWinnings(info));
	}


}
