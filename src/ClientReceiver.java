

/**
 * Obtiene mensajes de otros clientes a través del servidor (por el Hilo de ServerSender).
 */

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;

public class ClientReceiver extends Thread {

	private BufferedReader server;
	private Client client;
	private boolean quit;

	private int playerNumber;
	private ClientGUI clientGUI;

	public ClientReceiver(Client client, BufferedReader server) {
		this.server = server;
		this.client = client;
		quit = false;
		playerNumber = -33;

	}

	public ClientGUI getClientGUI() {
		return clientGUI;
	}

	public void quit() {
		quit = true;

	}

	public void run() {
		// Imprime al usuario cualquier cosa que obtengamos del servidor:
		try {
			while (!quit) {
				String s = server.readLine();
				String aux;
				if (s == null) {
					server.close(); 
					throw new IOException();
				} else {
					switch (s) {
					case Command.NICK_TAKEN:
						JOptionPane.showMessageDialog(client.getLogin().getFrame(),
								"Este nombre de usuario ya existe o no es aceptado por el servidor. Elija uno diferente.",
								"Nombre de usuario no valido", JOptionPane.ERROR_MESSAGE);
						client.getLogin().setConnectNotPressed(true);
						while (client.getLogin().notConfirmed()) {
							System.out.print("");
						}
						Client.tell(LoginGUI.getNickname());
						Client.tell(LoginGUI.getNickname());
						break;

					case Command.NICK_OK:

						LoginGUI.quit();
						clientGUI = new ClientGUI(client.getNickname(), client.createScoreTableValues(), client);
						clientGUI.show();

					case Command.NICK_CHANGED:
						aux = server.readLine();
						client.setNickname(aux);

						break;

					case Command.INVITE_RECEIVED:
						aux = server.readLine();
						clientGUI.addInvite(aux);
						clientGUI.refreshInvites();
						break;

					case Command.INVITE_ACCEPTED:
						String opponent = server.readLine();
						client.removeFromSentInvites(opponent);
						Client.tell(Command.ENTERED_GAME);
						Client.tell(Command.SEND_TO_ALL);
						NoughtsCrossesGUI.startGame(opponent, playerNumber);

						break;

					case Command.INVITE_DECLINED:
						String by = server.readLine();
						client.removeFromSentInvites(by);
						JOptionPane.showMessageDialog(clientGUI.getFrame(), "Tu invitacion ha sido rechazada por " + by,
								"Invitacion rechazada", JOptionPane.ERROR_MESSAGE);
						break;

					case Command.NEW_USER:
						client.addEntryToStats(server.readLine());
						clientGUI.setTableScoreValues(client.createScoreTableValues());
						break;

					case Command.WIN_INC:
						client.getStats().increaseWins(server.readLine());
						clientGUI.setTableScoreValues(client.createScoreTableValues());
						break;

					case Command.DRAW_INC:
						client.getStats().increaseDraws(server.readLine());
						clientGUI.setTableScoreValues(client.createScoreTableValues());
						break;

					case Command.DEF_INC:
						client.getStats().increaseDefeats(server.readLine());
						clientGUI.setTableScoreValues(client.createScoreTableValues());
						break;

					case Command.ENTERED_GAME:
						client.getStats().setInGame(server.readLine(), true);
						clientGUI.setTableScoreValues(client.createScoreTableValues());
						break;

					case Command.FINISHED_GAME:
						aux = server.readLine();
						client.getStats().setInGame(aux, false);
						clientGUI.setTableScoreValues(client.createScoreTableValues());
						break;

					case Command.I_AM_NEW:
						client.getStats().addEntry(server.readLine());
						break;

					case Command.IM_OUT:
						aux = server.readLine();
						if (aux == null) {
							client.removeEntryFromStats(client.getNickname());
							clientGUI.setTableScoreValues(client.createScoreTableValues());
							Client.terminate();
							quit();
						} else {
							client.removeEntryFromStats(aux);
							clientGUI.setTableScoreValues(client.createScoreTableValues());
						}
						break;

					case Command.GARBAGE:
						server.readLine();
						break;

					case Command.OPP_PLAYER_NO:
						server.readLine();
						int opPlayerNumber = Integer.parseInt(server.readLine());
						if (opPlayerNumber == Command.PLAYER_ONE) {
							playerNumber = Command.PLAYER_TWO;
						} else
							playerNumber = Command.PLAYER_ONE;
						server.readLine();
						break;

					case Command.OPP_MOVED:
						server.readLine();
						NoughtsCrossesGUI.pull(Integer.parseInt(server.readLine()));
						server.readLine();
						break;

					case Command.FORFEIT:
						NoughtsCrossesGUI.opponentSurrendered();
						break;

					default:
						break;
					}

				}
			}
		} catch (

		IOException e)
		{
			JOptionPane.showMessageDialog(clientGUI.getFrame(), "El servidor esta inactivo. Seras desconectado.",
					"Servidor inactivo", JOptionPane.ERROR_MESSAGE);
			Client.kill();
			quit();
		}
	}
}
