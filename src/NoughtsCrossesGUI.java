
/**
 *  La GUI correspondiente del juego
 */

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class NoughtsCrossesGUI

{
	public static NoughtsCrossesComponent comp;
	public static JFrame frame;
	private static String opponent;

	public static void pull(int position) {
		comp.makeMove(position);
		comp.setMyTurn(true);
	}
	/**
 	* Invocado en caso de que el jugador se rinda
 	*/
	public static void forfeit() {
		comp.forfait();
		Client.tell(Command.FORFEIT);
		Client.tell(opponent);
	}
	///
	// Comprueba si el oponente se rindió.
	//
	public static void opponentSurrendered() {
		comp.opponentSurrendered();
	}

	public static void startGame(String name, int playerNumber) {
		opponent = name;
		NoughtsCrosses game = new NoughtsCrosses();
		frame = new JFrame("Playing against " + name);

		comp = new NoughtsCrossesComponent(game, name, playerNumber, frame);

		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(frame,
						"Estás seguro de que quieres rendirte? Perderás el juego!", "Confirma tu elección",
						JOptionPane.YES_NO_OPTION);

				if (confirmed == JOptionPane.YES_OPTION) {
					forfeit();
				}
			}
		});

		frame.getContentPane().add(comp);

		frame.setVisible(true);
	}
}
