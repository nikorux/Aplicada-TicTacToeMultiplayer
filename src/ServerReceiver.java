import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

// Obtiene mensajes del cliente y los coloca en una cola

public class ServerReceiver extends Thread {
	private String myClientsName;
	private BufferedReader myClient;
	private ClientTable clientTable;
	private boolean quit;

	public ServerReceiver(String n, BufferedReader c, ClientTable t) {
		myClientsName = n;
		myClient = c;
		clientTable = t;
		quit = false;
	}

	public void run() {
		try {
			while (!quit) {
				String text = myClient.readLine();
				String recipient = myClient.readLine();

				if (recipient != null && text != null) {
					Message msg = new Message(myClientsName, text);
					// si el mensaje es global, envíelo a cada cola
					if (recipient.equals(Command.SEND_TO_ALL)) {
						// para cada clave (nick) en clientTable
						ConcurrentMap<String, MessageQueue> table = clientTable.getTable();
						for (ConcurrentMap.Entry<String, MessageQueue> entry : table.entrySet()) {
							if (entry.getValue() != null) {
								entry.getValue().offer(msg);
							}
						}

					} else {
						MessageQueue recipientsQueue = clientTable.getQueue(recipient);
						if (recipientsQueue != null)
							recipientsQueue.offer(msg);
						else
							System.err.println("Esto no debería ocurrir." + recipient
									+ ": " + text);
					}
					if (text.equals(Command.IM_OUT)) {
						quit = true;
						myClient.close();
					}

				} else {
					myClient.close();
					return;
				}

			}
		} catch (IOException e) {
			// Esto debería activarse (solo) cuando un cliente cierra su cliente.
			System.err.println("Perdio la conexión con el cliente " + myClientsName + ". Probablemente desconectado del servidor.");
		}
	}
}
