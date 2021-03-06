
/**
 * Muestra el tablero del tic tac toe una vez que se ha iniciado el juego
 */

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class BoardView extends JPanel implements Observer {
	private NoughtsCrossesModel model;
	private JButton[][] cell;
	private boolean isMyTurn;
	private int playerNumber;
	private JFrame frame;

	public BoardView(NoughtsCrossesModel model, String opponent, int playerNumber, JFrame frame) {
		super();
		setBackground(new Color(255, 239, 213));
		this.playerNumber = playerNumber;
		this.frame = frame;

		if (playerNumber == Command.PLAYER_ONE) {
			isMyTurn = true;
			
		} else {
			isMyTurn = false;

		}

		this.model = model;

		cell = new JButton[3][3];

		setLayout(new GridLayout(3, 3));

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cell[i][j] = new JButton(" ");
				final int x = i;
				final int y = j;
				cell[i][j].addActionListener(e -> { //cuando se presione un boton
					if (isMyTurn) {
						model.turn(x, y);
						isMyTurn = false;
						Client.tell(Command.OPP_MOVED);
						Client.tell(opponent);
						Client.tell(x * 3 + y);
						Client.tell(opponent);
					} else {
						JOptionPane.showMessageDialog(frame, "Por favor, espera por tu turno.", "No es tu turno",
								JOptionPane.ERROR_MESSAGE);
					}
				});
				add(cell[i][j]);
			}
		}
	}

	public void setMyTurn(boolean isMyTurn) {
		this.isMyTurn = isMyTurn;
	}

	public boolean iLost() {
		return ((playerNumber == Command.PLAYER_ONE && model.whoWon() == NoughtsCrosses.NOUGHT)
				|| (playerNumber == Command.PLAYER_TWO && model.whoWon() == NoughtsCrosses.CROSS));
	}

	public boolean iWon() {
		return ((playerNumber == Command.PLAYER_ONE && model.whoWon() == NoughtsCrosses.CROSS)
				|| (playerNumber == Command.PLAYER_TWO && model.whoWon() == NoughtsCrosses.NOUGHT));
	}
/**
 * actualiza el tablero después de los eventos que se han producido (movimientos realizados, juego terminado, etc.)
 */
	public void update(Observable obs, Object obj) {
		// Para cada cuadrado se hace lo siguiente:
		// Si es NOUGHT = O
		// Si es CROSS = X
		boolean notOver = (!model.isOver());
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (model.get(i, j) == NoughtsCrosses.CROSS) {
					cell[i][j].setText("X");
					cell[i][j].setEnabled(false);
				} else if (model.get(i, j) == NoughtsCrosses.NOUGHT) {
					cell[i][j].setText("O");
					cell[i][j].setEnabled(false);
				} else {
					cell[i][j].setText(" ");

					cell[i][j].setEnabled(notOver);
				}
			}
		}
		repaint();
//  Notificar a todos en el servidor
		if (!notOver) {

			if (iWon()) {
				if (model.wonBySurrender())
					JOptionPane.showMessageDialog(frame, "El oponente se rindio, asi que ganaste.", "Victoria",
							JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(frame, "Ganaste.", "Victoria", JOptionPane.INFORMATION_MESSAGE);
				Client.tell(Command.WIN_INC);
			}

			else if (iLost()) {
				JOptionPane.showMessageDialog(frame, "Perdiste", "Derrota.", JOptionPane.INFORMATION_MESSAGE);
				Client.tell(Command.DEF_INC);
			} else {
				JOptionPane.showMessageDialog(frame, "El juego ha terminado con un empate.", "Empate.",
						JOptionPane.INFORMATION_MESSAGE);
				Client.tell(Command.DRAW_INC);
			}
			Client.tell(Command.SEND_TO_ALL);


			Client.tell(Command.FINISHED_GAME);
			Client.tell(Command.SEND_TO_ALL);
			frame.dispose();

		}
	}
}
