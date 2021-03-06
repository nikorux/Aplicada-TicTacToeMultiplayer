/**
 * Contiene el botón de salida y el tablero del juego
 */

import javax.swing.*;
import java.awt.*;

public class NoughtsCrossesComponent extends JPanel {

	NoughtsCrossesModel model;
	BoardView board;
	int playerNumber;

	public NoughtsCrossesComponent(NoughtsCrosses game, String opponent, int playerNumber, JFrame frame) {
		super();
		this.playerNumber = playerNumber;
		model = new NoughtsCrossesModel(game);

		board = new BoardView(model, opponent, playerNumber, frame);
		board.setBackground(new Color(255, 239, 213));
		ButtonPanel controls = new ButtonPanel(model, frame);
		controls.setBackground(new Color(255, 239, 213));

		model.addObserver(board);

		setLayout(new BorderLayout());

		add(board, BorderLayout.CENTER);
		add(controls, BorderLayout.SOUTH);
	}

	public void makeMove(int position) {
		model.turn((position / 3), (position % 3));
	}
	/**
 	* Se llama una vez de que el jugador haya decidido rendirse. Establece al oponente como el ganador
 	*/
	public void forfait() {
		if (playerNumber == Command.PLAYER_ONE) {
			model.setWinner(NoughtsCrosses.NOUGHT);
		} else
			model.setWinner(NoughtsCrosses.CROSS);
	}

	public void setMyTurn(boolean isMyTurn) {
		board.setMyTurn(isMyTurn);

	}

	public void opponentSurrendered() {
		if (playerNumber == Command.PLAYER_ONE) {
			model.setWinner(NoughtsCrosses.CROSS);
		} else
			model.setWinner(NoughtsCrosses.NOUGHT);
	}
}
