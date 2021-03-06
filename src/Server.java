import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	private ClientTable clientTable;
	private ArrayList<PrintStream> toClients;
	private ServerSocket serverSocket;
	private ArrayList<String> allNicknames;
	private Command command;

	public Server() {
		clientTable = new ClientTable();
		toClients = new ArrayList<PrintStream>();
		serverSocket = null;
		allNicknames = new ArrayList<String>();
		command = new Command();
	}

	// Ejecuta el servidor:
	public void run() {

		// Solución a mi problema continuo
		try {
			serverSocket = new ServerSocket(Port.number);
		} catch (IOException e) {
			System.err.println("El puerto esta ocupado, intente de nuevo con otro " + Port.number);
			System.exit(1);
		}
		// Intentar de nuevo
		try { //Bucles como un server normal
			while (true) {
				Socket socket = serverSocket.accept();

				BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintStream toClient = new PrintStream(socket.getOutputStream());
				toClients.add(toClient);
				// Para el nombre:
				String clientName = fromClient.readLine();
				boolean badNickFound = false;
				while (clientTable.contains(clientName) || command.getCommands().contains(clientName)) {
					badNickFound = true;
					toClient.println(Command.NICK_TAKEN);
					clientName = fromClient.readLine();
					fromClient.readLine(); // Para mantener la conexión
				}
				if (badNickFound) {
					toClient.println(Command.NICK_CHANGED);
					toClient.println(clientName);
				}
				toClient.println(Command.NICK_OK);
				toClient.println(clientName);
				System.out.println(clientName + " connected");
				allNicknames.add(clientName);

				// Se agrega el cliente al tablero:
				clientTable.add(clientName);
				for (String name : allNicknames) {
					sendToAllClients(Command.NEW_USER);
					sendToAllClients(name);
				}
				// Se crea y se pone en marcha un nuevo hilo para leer desde el cliente:
				(new ServerReceiver(clientName, fromClient, clientTable)).start();
				(new ServerSender(clientName, clientTable.getQueue(clientName), toClient, this)).start();
			}
		} catch (IOException e) {
			// Para que se mantenga corriendo
		}
	}

	public void sendToAllClients(String message) {
		for (int i = 0; i < toClients.size(); i++) {
			toClients.get(i).println(message);
		}

	}

	public void removeFromAllNicknames(String what) {
		allNicknames.remove(what);
	}

	public void removeClientTableEntry(String nickname) {
		clientTable.remove(nickname);

	}

	public static void main(String[] args) {
		new Server().run();
	}
}
