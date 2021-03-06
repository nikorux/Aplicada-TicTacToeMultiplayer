import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {

	// Implementación LinkedBlockingQueue de BlockingQueue:
	private BlockingQueue<Message> queue = new LinkedBlockingQueue<Message>();

	public void offer(Message m) {
		queue.offer(m);
	}

	public Message take() {

		while (true) {
			try {
				return (queue.take());
			} catch (InterruptedException e) {
			}

		}
	}
}
