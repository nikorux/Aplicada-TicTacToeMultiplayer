import   java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClientTable {

	private ConcurrentMap<String, MessageQueue> queueTable = new ConcurrentHashMap<String, MessageQueue>();

	public void add(String nickname) {
		queueTable.put(nickname, new MessageQueue());
	}

	public MessageQueue getQueue(String nickname) {
		return queueTable.get(nickname);
	}

	public boolean contains(String nickname) {
		return queueTable.containsKey(nickname);
	}

	public void remove(String nickname) {
		queueTable.remove(nickname);

	}

	public ConcurrentMap<String, MessageQueue> getTable() {
		return queueTable;
	}

}
