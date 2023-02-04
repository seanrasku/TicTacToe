package ttt.games;

public class Nim4 implements Game{
	final static int NUM_ROWS = 3;
	private final static int SZ_ROW0 = 5;
	private final static int SZ_ROW1 = 3;
	private final static int SZ_ROW2 = 1;

	// These fields represent the actual position at any time.
	private int[] heap = new int[NUM_ROWS];
	private int nextPlayer;

	/**
	 * Construct an instance of the cs310.games.Nim Game
	 */
	public Nim4() {
	}

	/**
	 * Set up the position ready to play.
	 */
	public void init() {
		heap[0] = SZ_ROW0;
		heap[1] = SZ_ROW1;
		heap[2] = SZ_ROW2;
		nextPlayer = COMPUTER;
	}
	
	/**
	 * Has the player of side side won? Returns true or false
	 * 
	 * @return true iff side side has won.
	 */
	public boolean isAWin(int side) {
		// win happens with no stars left for other player, now nextPlayer
		// for example, HUMAN took last star, so nextPlayer == COMPUTER now
		return getStarsLeft() == 0 && side == nextPlayer;
	}

	/**
	 * Make a move
	 * 
	 * @param side of player making move
	 * @param row 
	 * @param number of stars taken.
	 * @returns false if move is illegal.
	 */
	public boolean makeMove(int side, int row, int number) {
		if (side != nextPlayer) {
			return false; // wrong player played
		}
		if (!isLegal(row, number)) {
			return false;
		} else {
			nextPlayer = (nextPlayer == COMPUTER ? HUMAN : COMPUTER);
			heap[row] = heap[row] - number;
		}
		return true;
	}
	
	/**
	 * What are the rules of the game? How are moves entered interactively?
	 * 
	 * @return a String with this information.
	 */
	public String help() {
		StringBuffer s = new StringBuffer("\ncs310.games.Nim is the name of the game.\n");
		s.append("The board contains three ");
		s.append("rows of stars.\nA move removes stars (at least one) ");
		s.append("from a single row.\nThe player who takes the last star loses.\n");
		s.append("Type Xn (or xn) at the terminal to remove n stars from row X.\n");
		s.append("? displays the current position, q quits.\n");
		return s.toString();
	}

	public BestMove chooseMove(int side, int depth) {
		BestMove reply; // Opponent's best reply
		int simpleEval; // Result of an immediate evaluation
		int bestRow = -1; // Initialize running value with out-of-range value
		int optimalStarCount = -1;
		int value;

		if ((simpleEval = positionValue()) != UNCLEAR) {
			return new BestMove(simpleEval);
		}

		// Initialize running values with out-of-range values (good software practice)
		// Here also to ensure that *some* move is chosen, even if a hopeless case
		if (side == COMPUTER) {
			//nextPlayer = HUMAN;
			value = HUMAN_WIN - 1; // impossibly low value
		} else {
			//nextPlayer = COMPUTER;
			value = COMPUTER_WIN + 1; // impossibly high value
		}

		for (int heap_index = 0; heap_index < NUM_ROWS; heap_index++){
			for (int stars = 1; stars <= heap[heap_index]; stars++) {
				makeMove(nextPlayer, heap_index, stars);

				reply = chooseMove(nextPlayer, depth + 1);
				heap[heap_index] += stars;
				nextPlayer = (nextPlayer == COMPUTER ? HUMAN : COMPUTER);

				if (side == COMPUTER && reply.val > value || side == HUMAN && reply.val < value) {
					value = reply.val;
					bestRow = heap_index;
					optimalStarCount = stars;
				}
			}
		}
		return new BestMove(value, bestRow, optimalStarCount);
	}

	private int positionValue() {
		return isAWin(COMPUTER) ? COMPUTER_WIN : isAWin(HUMAN) ? HUMAN_WIN : UNCLEAR;
	}
	/**
	 * This method displays current position and next player
	 * i.e., the full current state of the game
	 */
	public void printBoard() {
		StringBuilder board = new StringBuilder("");

		for (int i = 0; i < NUM_ROWS; i++) {
			char c = (char) ((int) 'A' + i);
			board.append(c + ": ");
			for (int j = heap[i]; j > 0; j--) {
				board.append("* ");
			}
			board.append('\n');
		}
		board.append("next to play: "+ (nextPlayer==0?"HUMAN":"COMPUTER"));
		System.out.println(board);
		//return board.toString();
	}
	public boolean isADraw(){ return false;}
	/**
	 * Compute the total number of stars left.
	 */
	private int getStarsLeft() {
		return (heap[0] + heap[1] + heap[2]);
	}

	private boolean isLegal(int row, int stars) {
		return 0 <= row && row <= 2 && stars >= 1 && stars <= heap[row];
	}

	// unit test (not part of API, just a test of it)
	public static void main(String[] args) {

		Nim4 g = new Nim4();

		g.init();
		System.out.println("Start of game:");
		System.out.println(g);

		System.out.println("play with hard coded moves");
		try {
			System.out.println("doing move: A4");
			g.makeMove(Nim4.COMPUTER, 0, 4);
			System.out.println(g);
			System.out.println("doing move: B2");
			g.makeMove(Nim4.HUMAN, 1, 2);
			System.out.println(g);
			System.out.println("doing move: C1");
			g.makeMove(Nim4.COMPUTER, 2, 1);
			System.out.println(g);
			System.out.println("Human won? " + g.isAWin(Nim4.HUMAN));
			System.out.println("Computer won? " + g.isAWin(Nim4.COMPUTER));
			System.out.println("doing move: B1");
			g.makeMove(Nim4.HUMAN, 1, 1);
			System.out.println(g);
			System.out.println("Human won? " + g.isAWin(Nim4.HUMAN));
			System.out.println("Computer won? " + g.isAWin(Nim4.COMPUTER));
			System.out.println("doing move: A1");
			g.makeMove(Nim4.COMPUTER, 0, 1);
			System.out.println(g);
			System.out.println("Human won? " + g.isAWin(Nim4.HUMAN));
			System.out.println("Computer won? " + g.isAWin(Nim4.COMPUTER));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
