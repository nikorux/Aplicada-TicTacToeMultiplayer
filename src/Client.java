import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class Client {
	private String hostname;
	private String nickname;
	private static ClientReceiver receiver;
	private Statistic stats;
	private static LoginGUI login = new LoginGUI(); // Necesita ser inicializado aquí para correr
	private static boolean notExited = true; 
	static PrintStream toServer;
	private ArrayList<String> invitesSent;

	public Client(String nickname, String hostname) {
		this.nickname = nickname;
		this.hostname = hostname;
		stats = new Statistic();
		invitesSent = new ArrayList<String>();
	}

	public LoginGUI getLogin() {
		return login;
	}

	public void setHostname(String h) {
		hostname = h;
	}

	public Statistic getStats() {
		return stats;
	}

	public void removeEntryFromStats(String nickname) {
		stats.removeEntry(nickname);
	}

	public void addEntryToStats(String nickname) {
		stats.addEntry(nickname);
	}

	public void setInGame(String nickname, boolean val) {
		stats.setInGame(nickname, val);
	}

	public void increaseWins(String nickname) {
		stats.increaseWins(nickname);
	}

	public void increaseDraws(String nickname) {
		stats.increaseDraws(nickname);
	}

	public void increaseDefeats(String nickname) {
		stats.increaseDefeats(nickname);
	}
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String newNickname) {
		this.nickname = newNickname;
	}

	// Se obtiene el client server
	public ClientReceiver getReceiver() {
		return receiver;
	}


	 //Calcula la matriz de valores de la tabla de puntuación, según las estadísticas utilizadas antes
	 //mostrando la tabla de puntajes
	 
	 // retorna la matriz de valores

	public String[][] createScoreTableValues() {
		String[][] values = new String[stats.size()][5];
		Iterator<Entry<String, StatisticEntry>> it = stats.getTable().entrySet().iterator();
		int i = 0;

		while (it.hasNext()) {
			Entry<String, StatisticEntry> mapping = (Entry<String, StatisticEntry>) it.next();

			values[i][0] = mapping.getKey();
			values[i][1] = Integer.toString(mapping.getValue().getWins());
			values[i][2] = Integer.toString(mapping.getValue().getDraws());
			values[i][3] = Integer.toString(mapping.getValue().getDefeats());
			if (mapping.getValue().isInGame())
				values[i][4] = "YES";
			else
				values[i][4] = "NO";

			i++;
		}

		return values;
	}

	public void run() {
		// Sockets abiertos:
		toServer = null;
		BufferedReader fromServer = null;
		Socket server = null;
		boolean notConnectedBecauseOfHost = true;
		// Esto asegura que el host es bueno y permite que el usuario lo vuelva a intentar
		// después de ingresar un nombre de host incorrecto
		while (notConnectedBecauseOfHost) {
			try {
				server = new Socket(hostname, Port.number);
				toServer = new PrintStream(server.getOutputStream());
				fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
				notConnectedBecauseOfHost = false;
			} catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(getLogin().getFrame(),
						"Este nombre de host no corresponde a ningun servidor activo. Elija uno diferente",
						"Host no valido", JOptionPane.ERROR_MESSAGE);
				getLogin().setConnectNotPressed(true);
				// Esto espera la confirmación de la GUI de inicio de sesión.
				while (getLogin().notConfirmed()) {
					System.out.print("");
				}
				setNickname(LoginGUI.getNickname());
				setHostname(LoginGUI.getHost());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "El servidor se ha detenido. Vuelva a intentarlo más tarde.",
						"Server inactivo", JOptionPane.ERROR_MESSAGE);
				kill();
			}
		}

		receiver = new ClientReceiver(this, fromServer);
		// Le dice al servidor cuál es mi nombre de usuario:
		toServer.println(nickname);

		receiver.start();

		// Espera a que el receiver termine y cierre los sockets.
		try {
			receiver.join();
			fromServer.close();
			server.close();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Algo no funciona con la conexión. Intente iniciar sesión nuevamente.",
					"Servidor inactivo", JOptionPane.ERROR_MESSAGE);
			kill();
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "Algo no funciona. Intenta iniciar sesión de nuevo..", "Servidor inactivo",
					JOptionPane.ERROR_MESSAGE);
			kill();
		}
	}
	
	//Detiene el receiver y cierra el socket
	public static void close() {
		receiver.quit();
		toServer.close();

	}

	public static void terminate() {
		notExited = false;
	}

	public static void kill() {
		System.exit(1);
	}

//	
//	 Permite a todos saber que este cliente está fuera para que puedan actualizar sus
//	  estadísticas
//	
	public void quit() {

		toServer.println(Command.IM_OUT);
		toServer.println(Command.SEND_TO_ALL);

	}


	public static void tell(Object mes) {
		toServer.println(mes);
	}

	public void addToSentInvites(String inv) {
		invitesSent.add(inv);
	}

	public void removeFromSentInvites(String inv) {
		invitesSent.remove(inv);
	}

	public ArrayList<String> getSentInvites() {
		return invitesSent;
	}

	public static void main(String[] args) {

		// Mostrando GUI de inicio de sesión
		login.run();
		while (login.notConfirmed() && notExited) {
			System.out.print("");
		}
		if (notExited) {
			Client client = new Client(LoginGUI.getNickname(), LoginGUI.getHost());
			client.run();
		}

	}

}
