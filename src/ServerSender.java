import java.io.PrintStream;

public class ServerSender extends Thread {
	private MessageQueue queue;
	private PrintStream client;
	private boolean quit;
	private Server server;
	private String clientName;

	public ServerSender(String clientName, MessageQueue q, PrintStream c, Server server) {
		this.clientName = clientName;
		queue = q;
		client = c;
		quit = false;
		this.server = server;
	}

	public void run() {
		while (!quit) {
			Message msg = queue.take();
			client.println(msg.getText());
			client.println(msg.getSender());

			if (msg.getText().equals(Command.IM_OUT) && msg.getSender().equals(clientName)) {
				quit = true;
				server.removeFromAllNicknames(clientName);
				server.removeClientTableEntry(msg.getSender());
				client.close();
			}

		}
	}
}
