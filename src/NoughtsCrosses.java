
/**
 * 
 * El "motor" del juego.
 *
 */
public class NoughtsCrosses {
	public static final int BLANK = 0;
	public static final int CROSS = 1;
	public static final int NOUGHT = 2;

	private boolean crossTurn;
	private int[][] board;
	
	private boolean noughtsWon = false;
	private boolean crossesWon = false;

	public NoughtsCrosses() {
		board = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = BLANK;
			}
		}
		crossTurn = true;
	}

	public int get(int i, int j) {
		return board[i][j];
	}

	public boolean isCrossTurn() {
		return crossTurn;
	}

	public void turn(int i, int j) {
		if (board[i][j] == BLANK) {
			if (crossTurn) {
				board[i][j] = CROSS;
			} else {
				board[i][j] = NOUGHT;
			}
			crossTurn = !crossTurn;
		} else {
			throw new IllegalArgumentException("Tablero no vacio en (" + i + ", " + j + ")");
		}
	}

	public boolean isOver() {
		boolean draw = true;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == BLANK)
					draw = false;
			}

		}
		return (draw || winner(CROSS) || winner(NOUGHT) || crossesWon || noughtsWon);
	}

	private boolean winner(int player) {
		return (board[0][0] == player && board[0][1] == player && board[0][2] == player)
				|| (board[1][0] == player && board[1][1] == player && board[1][2] == player)
				|| (board[2][0] == player && board[2][1] == player && board[2][2] == player)
				|| (board[0][0] == player && board[1][0] == player && board[2][0] == player)
				|| (board[0][1] == player && board[1][1] == player && board[2][1] == player)
				|| (board[0][2] == player && board[1][2] == player && board[2][2] == player)
				|| (board[0][0] == player && board[1][1] == player && board[2][2] == player)
				|| (board[0][2] == player && board[1][1] == player && board[2][0] == player);
	}

	public int whoWon() {
		if (winner(CROSS)||crossesWon) {
			return CROSS;
		} else if (winner(NOUGHT)||noughtsWon) {
			return NOUGHT;
		} else {
			return BLANK;
		}
	}

	public void newGame() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = BLANK;
			}
		}
		crossTurn = true;
	}

	public void setWinner(int who) {
		if(who == NOUGHT){
			noughtsWon = true;
		}
		else crossesWon = true;
		
	}

	public boolean wonBySurrender() {
		return noughtsWon || crossesWon;
	}

}