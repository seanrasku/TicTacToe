package ttt.games;
import java.util.Scanner;

public class PlayNim2 {
	public PlayNim2() {
		g = new Nim();
	}

	public void doComputerMove() {
		boolean legal;
		System.out.println(g.toString());
		do {
			System.out.println("row: ");
			int row = scan.nextInt();
			System.out.println("# of stars: ");
			int col = scan.nextInt();
			legal = g.makeMove(Nim.COMPUTER, row, col);
			if (!legal)
				System.out.println("Illegal move, try again");
		} while (!legal);
	}

	public void doHumanMove() {
		boolean legal;
		System.out.println(g.toString());
		do {
			System.out.println("row: ");
			int row = scan.nextInt();
			System.out.println("# of stars: ");
			int col = scan.nextInt();
			legal = g.makeMove(Nim.HUMAN, row, col);
			if (!legal)
				System.out.println("Illegal move, try again");
		} while (!legal);
	}
	
	// return true if game is continuing, false if done
	boolean checkAndReportStatus() {
		if (g.isWin(Nim.COMPUTER)) {
			System.out.println("Computer says: I WIN!!");
			return false; // game is done
		}
		if (g.isWin(Nim.HUMAN)) {
			System.out.println("Computer says: You WIN!!");
			return false; // game is done
		}
		System.out.println("game continuing");
		return true;
	}

	// do one round of playing the game, return true at end of game
	public boolean getAndMakeMoves() {
		// let computer go first...
		doComputerMove();
		System.out.println("\nback from doComputerMove");
		// System.out.println("count = " + t.getCount());
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
		PlayNim2 ui = new PlayNim2();
		ui.playOneGame();
	}

	private Nim g; // g for game
	private Scanner scan = new Scanner(System.in);


}
