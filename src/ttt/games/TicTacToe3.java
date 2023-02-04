package ttt.games;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TicTacToe3 {
	/*
	INSTANCE VARIABLES:
	*/
	private int[][] board = new int[3][3];
	private Map<Position3, Integer> store = new HashMap<>();
	/*
	CONSTANTS:
	*/
	public static final int HUMAN = 0;
	public static final int COMPUTER = 1;
	public static final int EMPTY = 2;

	public static final int HUMAN_WIN = 0;
	public static final int DRAW = 1;
	public static final int UNCLEAR = 2;
	public static final int COMPUTER_WIN = 3;
	private String computerSide = "O";
	private String humanSide = "X";
	// Constructor
	public TicTacToe3() {
		clearBoard();
	}
	
	// TODO: get rid of this encapsulation-busting public method
	// and instead, implement toString()
	void printBoard() {
		// TODO: stop using this encapsulation-busting method
		// move it to cs310.games.TicTacToe
		StringBuilder boardStr = new StringBuilder("");
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				String spot;
				switch (board[row][col]) {
					case TicTacToe3.HUMAN:
						spot = " " + humanSide + " ";
						break;
					case TicTacToe3.COMPUTER:
						spot = " " + computerSide + " ";
						break;
					case TicTacToe3.EMPTY:
						spot = "   ";
						break;
					default:
						System.out.println("Bad board entry in printBoard");
						return;
				}
				boardStr.append(spot);
				if (col < 2)
					boardStr.append("|");
			}
			if (row < 2)
				boardStr.append("\n-----------\n");
		}
		System.out.println(boardStr);

	}

	// Find optimal move
	public BestMove chooseMove(int side, int depth) {
		int opp; // The other side
		BestMove reply; // Opponent's best reply
		int simpleEval; // Result of an immediate evaluation
		int bestRow = -1; // Initialize running value with out-of-range value
		int bestColumn = -1;
		int value;
		Position3 thisPosition = new Position3(board);

		if ((simpleEval = positionValue()) != UNCLEAR)
			return new BestMove(simpleEval);
		
		// Don't look up top-level value: at top level, we need to explore moves
		// out from here to find the best move to make)
		if (depth > 0) {
			Integer lookupVal = store.get(thisPosition);
			if (lookupVal != null)
				return new BestMove(lookupVal);
		}
		// Initialize running values with out-of-range values (good software practice)
		// Here also to ensure that *some* move is chosen, even if a hopeless case
		if (side == COMPUTER) {
			opp = HUMAN;
			value = HUMAN_WIN - 1; // impossibly low value
		} else {
			opp = COMPUTER;
			value = COMPUTER_WIN + 1; // impossibly high value
		}

		for (int row = 0; row < 3; row++)
			for (int column = 0; column < 3; column++)
				if (squareIsEmpty(row, column)) {
					place(row, column, side);
					reply = chooseMove(opp, depth+1);
					place(row, column, EMPTY);
					// Update if side gets better position
					if (side == COMPUTER && reply.val > value || side == HUMAN && reply.val < value) {
						value = reply.val;
						bestRow = row;
						bestColumn = column;
					}
				}
		store.put(thisPosition, value);
		return new BestMove(value, bestRow, bestColumn);
	}

	// Play move, including checking legality
	public boolean playMove(int side, int row, int column) {
		if (row < 0 || row >= 3 || column < 0 || column >= 3 || board[row][column] != EMPTY)
			return false;
		board[row][column] = side;
		return true;
	}

	// Simple supporting routines
	public void clearBoard() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				board[i][j] = EMPTY;
	}
	
	// added by eoneil: more general-purpose than "boardIsFull"
	public boolean isADraw() {
		return !isAWin(HUMAN)&&!isAWin(COMPUTER)&&boardIsFull();
	}
	
	public boolean isAWin(int side) {
		int row, column;

		/* Look for all in a row */
		for (row = 0; row < 3; row++) {
			for (column = 0; column < 3; column++)
				if (board[row][column] != side)
					break;
			if (column >= 3)
				return true;
		}

		/* Look for all in a column */
		for (column = 0; column < 3; column++) {
			for (row = 0; row < 3; row++)
				if (board[row][column] != side)
					break;
			if (row >= 3)
				return true;
		}

		/* Look on diagonals */
		if (board[1][1] == side && board[2][2] == side && board[0][0] == side)
			return true;

		if (board[0][2] == side && board[1][1] == side && board[2][0] == side)
			return true;

		return false;
	}
	
	// TODO: put toString here, so client can display board to user
	// without using getBoard()

	@Override
	public String toString() {
		return "TicTacToe3{" +
				"computerSide='" + computerSide + '\'' +
				", humanSide='" + humanSide + '\'' +
				", board=" + Arrays.toString(board) +
				", store=" + store +
				'}';
	}



	// made private by eoneil: now a helper to isADraw of public API
		private boolean boardIsFull() {
			for (int row = 0; row < 3; row++)
				for (int column = 0; column < 3; column++)
					if (board[row][column] == EMPTY)
						return false;
			return true;
		}	
	// Play a move, possibly clearing a square
	private void place(int row, int column, int piece) {
		board[row][column] = piece;
	}

	private boolean squareIsEmpty(int row, int column) {
		return board[row][column] == EMPTY;
	}

	// Compute static value of current position (win, draw, etc.)
	private int positionValue() {
		return isAWin(COMPUTER) ? COMPUTER_WIN : isAWin(HUMAN) ? HUMAN_WIN : boardIsFull() ? DRAW : UNCLEAR;
	}
	private class Position3 {
		public int[][] board;

		Position3(int[][] theBoard) {
			board = new int[3][3];
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					board[i][j] = theBoard[i][j];
		}

		public boolean equals(Object rhs) {

			if (!(rhs instanceof Position3))
				return false;

			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					if (board[i][j] != ((Position3) rhs).board[i][j])
						return false;
			return true;
		}

		public int hashCode() {
			int hashVal = 0;

			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++)
					hashVal = hashVal * 4 + board[i][j];

			return hashVal;
		}
	}
}
