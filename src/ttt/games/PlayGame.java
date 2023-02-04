package ttt.games;
import java.util.Scanner;

public class PlayGame {
	public PlayGame(String[] args) {
		if (args.length == 1)
			g = new Nim4();
		else g = new TicTacToe4();

	}

	public void doComputerMove() {
		g.printBoard();
		BestMove compMove = g.chooseMove(g.COMPUTER, 0);
		System.out.println("Computer Plays: \ni = " + compMove.i + "\nj = " + compMove.j);
		g.makeMove(g.COMPUTER, compMove.i, compMove.j);
	}

	public void doHumanMove() {
		boolean legal;
		g.printBoard();
		do {
			System.out.println("i: ");
			int row = scan.nextInt();
			System.out.println("j: ");
			int col = scan.nextInt();
			legal = g.makeMove(g.HUMAN, row, col);
			if (!legal)
				System.out.println("Illegal move, try again");
		} while (!legal);
	}
	
	// return true if game is continuing, false if done
	boolean checkAndReportStatus() {
		if (g.isAWin(g.COMPUTER)) {
			System.out.println("Computer says: I WIN!!");
			return false; // game is done
		}
		if (g.isAWin(g.HUMAN)) {
			System.out.println("Computer says: You WIN!!");
			return false; // game is done
		}
		if (g.isADraw()){
			System.out.println(" Game is a DRAW");
			return false;
		}
		System.out.println("game continuing");
		return true;
	}

	// do one round of playing the game, return true at end of game
	public boolean getAndMakeMoves() {
		// let computer go first...
		doComputerMove();
		System.out.println("\nback from doComputerMove");
		if (!checkAndReportStatus())
			return false; // game over
		doHumanMove();
		if (!checkAndReportStatus())
			return false; // game over
		return true;
	}

	void playOneGame() {
		boolean continueGame = true;
		g.init();
		while (continueGame) {
			continueGame = getAndMakeMoves();
		}
	}

	public static void main(String[] args) {
		if (args.length > 0 && !args[0].equals("Nim")) {
			System.out.println("Not a valid game, exiting...");
			System.exit(-1);
		}
		else if (args.length > 1){
			System.out.println("Not a valid game, exiting...");
			System.exit(-1);
		}
		else {
			PlayGame ui = new PlayGame(args);
			ui.playOneGame();
		}

	}

	private Game g; // g for game
	private Scanner scan = new Scanner(System.in);


}
